package gensym.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import gensym.lmsx._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

@virtualize
trait RepMonads extends RepLattices
  with RepIdMonad with RepEitherMonad with RepReaderMonad
  with RepStateMonad with RepListMonad with RepCPSMonad { self: SAIOps =>

  trait Monad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
    def map[A: Manifest,B: Manifest](ma: M[A])(f: Rep[A] => Rep[B]): M[B] = flatMap(ma)(a => pure(f(a)))
    def filter[A: Manifest](ma: M[A])(f: Rep[A] => Rep[Boolean]): M[A] =
      throw new Exception("Not supported")
  }

  object Monad {
    def apply[M[_]](implicit m: Monad[M]): Monad[M] = m

    // Note: type A does not have to be a Rep type
    def mapM[M[_]: Monad, A, B: Manifest](xs: List[A])(f: A => M[B]): M[List[B]] = xs match {
      case Nil => Monad[M].pure(List[B]())
      case x::xs => Monad[M].flatMap(f(x)) { b =>
        Monad[M].flatMap(mapM(xs)(f)) { bs =>
          Monad[M].pure(b::bs)
        }
      }
    }

    def forM[M[_]: Monad, A, B: Manifest](xs: List[A])(f: A => M[B]): M[B] = xs match {
      case x::Nil => f(x)
      case x::xs => Monad[M].flatMap(f(x)) { _ => forM(xs)(f) }
    }
  }

  /////////////////////////////////////////////////

  trait MonadPlus[M[_]] {
    def mzero[A: Manifest : RepLattice]: M[A]
    def mplus[A: Manifest : RepLattice](a: M[A], b: M[A]): M[A]
  }

  object MonadPlus {
    def apply[M[_]](implicit m: MonadPlus[M]): MonadPlus[M] = m
  }

  /////////////////////////////////////////////////

  object ListReaderStateM {
    def apply[R: Manifest, S: Manifest, A: Manifest](implicit m: ListReaderStateM[R, S, A]): ListReaderStateM[R, S, A] = m

    implicit def ListReaderStateMonad[R: Manifest, S: Manifest] =
      new Monad[ListReaderStateM[R, S, *]] with MonadState[ListReaderStateM[R, S, *], S] with MonadReader[ListReaderStateM[R, S, *], R] {
        def flatMap[A: Manifest, B: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[A] => ListReaderStateM[R, S, B]) = ma.flatMap(f)
        def pure[A: Manifest](a: Rep[A]): ListReaderStateM[R, S, A] = ListReaderStateM(r => s => (List[A](a), s))
        override def filter[A: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[A] => Rep[Boolean]): ListReaderStateM[R, S, A] = ma.filter(f)

        def get: ListReaderStateM[R, S, S] = ListReaderStateM(r => s => (List[S](s), s))
        def put(s: Rep[S]): ListReaderStateM[R, S, Unit] = ListReaderStateM(r => _ => (List[Unit](()), s))
        def mod(f: Rep[S] => Rep[S]): ListReaderStateM[R, S, Unit] = ListReaderStateM(r => s => (List[Unit](()), f(s)))

        def ask: ListReaderStateM[R, S, R] = ListReaderStateM(r => s => (List[R](r), s))
        def local[A: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[R] => Rep[R]): ListReaderStateM[R, S, A] =
          ListReaderStateM(r => s => ma.run(f(r))(s))

      }

    implicit def ListReaderStateMonadPlus[R: Manifest, S: Manifest : RepLattice] = new MonadPlus[ListReaderStateM[R, S, *]] {
      def mzero[A: Manifest : RepLattice]: ListReaderStateM[R, S, A] = empty[R, S, A]
      def mplus[A: Manifest : RepLattice](a: ListReaderStateM[R, S, A], b: ListReaderStateM[R, S, A]): ListReaderStateM[R, S, A] =
        ListReaderStateM(r => s => {
          val (lista, sa) = a.run(r)(s)
          val (listb, sb) = b.run(r)(s)
          (lista ⊔ listb, sa ⊔ sb)
        })
    }

    def fromList[R: Manifest, S: Manifest, A: Manifest](xs: List[A]): ListReaderStateM[R, S, A] =
      ListReaderStateM(r => s => (unit(xs), s))

    def fromList[R: Manifest, S: Manifest, A: Manifest](xs: Rep[List[A]]): ListReaderStateM[R, S, A] =
      ListReaderStateM(r => s => (xs, s))

    def empty[R: Manifest, S: Manifest, A: Manifest]: ListReaderStateM[R, S, A] =
      ListReaderStateM(r => s => (List[A](), s))
  }

  // ListT[ReaderT[StateT[Id, *], *], *]
  // ListT: run: M[List[A]]
  // ReaderT: run: R => M[A]
  // StateT: run: S => M[(A, S)]
  // R => S => (List[A], S)

  case class ListReaderStateM[R: Manifest, S: Manifest, A: Manifest](run: Rep[R] => Rep[S] => (Rep[List[A]], Rep[S])) {
    import ListReaderStateM._

    def apply(r: Rep[R])(s: Rep[S]): (Rep[List[A]], Rep[S]) = run(r)(s)

    def flatMap[B: Manifest](f: Rep[A] => ListReaderStateM[R, S, B]): ListReaderStateM[R, S, B] =
      ListReaderStateM(r => s => {
        val (lista, s0) = run(r)(s)
        val init = (List[B](), s0)
        val res = lista.foldLeft(init) { case (acc_s1, a) =>
          val listacc = acc_s1._1
          val s1 = acc_s1._2
          val fa: ListReaderStateM[R, S, B] = f(a)
          val (listb, s2) = fa.run(r)(s1)
            (listacc ++ listb, s2): Rep[(List[B], S)]
        }
        res.unlift
      })

    def map[B: Manifest](f: Rep[A] => Rep[B]): ListReaderStateM[R, S, B] =
      ListReaderStateM(r => s => {
        val (lista, s0) = run(r)(s)
        val listb = lista.map(f)
        (listb, s0)
      })

    def filter(f: Rep[A] => Rep[Boolean]): ListReaderStateM[R, S, A] =
      ListReaderStateM(r => s => {
        val (lista, s0) = run(r)(s)
        (lista.filter(f), s0)
      })

    def withFilter(f: Rep[A] => Rep[Boolean]): ListReaderStateM[R, S, A] = filter(f)
  }

  /////////////////////////////////////////////////

  object SetReaderStateM {
    def apply[R: Manifest, S: Manifest, A: Manifest](implicit m: SetReaderStateM[R, S, A]): SetReaderStateM[R, S, A] = m

    implicit def SetReaderStateMonad[R: Manifest, S: Manifest] =
      new Monad[SetReaderStateM[R, S, *]] with MonadState[SetReaderStateM[R, S, *], S] with MonadReader[SetReaderStateM[R, S, *], R] {
        def flatMap[A: Manifest, B: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[A] => SetReaderStateM[R, S, B]) =
          ma.flatMap(f)
        def pure[A: Manifest](a: Rep[A]): SetReaderStateM[R, S, A] =
          SetReaderStateM(r => s => (Set[A](a), s))

        override def filter[A: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[A] => Rep[Boolean]): SetReaderStateM[R, S, A] =
          ma.filter(f)

        def get: SetReaderStateM[R, S, S] =
          SetReaderStateM(r => s => (Set[S](s), s))
        def put(s: Rep[S]): SetReaderStateM[R, S, Unit] =
          SetReaderStateM(r => _ => (Set[Unit](()), s))
        def mod(f: Rep[S] => Rep[S]): SetReaderStateM[R, S, Unit] =
          SetReaderStateM(r => s => (Set[Unit](()), f(s)))

        def ask: SetReaderStateM[R, S, R] = SetReaderStateM(r => s => (Set[R](r), s))
        def local[A: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[R] => Rep[R]): SetReaderStateM[R, S, A] =
          SetReaderStateM(r => s => ma.run(f(r))(s))
      }

    implicit def SetReaderStateMonadPlus[R: Manifest, S: Manifest : RepLattice] = new MonadPlus[SetReaderStateM[R, S, *]] {
      def mzero[A: Manifest : RepLattice]: SetReaderStateM[R, S, A] = empty[R, S, A]
      def mplus[A: Manifest : RepLattice](a: SetReaderStateM[R, S, A], b: SetReaderStateM[R, S, A]): SetReaderStateM[R, S, A] =
        SetReaderStateM(r => s => {
          val (lista, sa) = a.run(r)(s)
          val (listb, sb) = b.run(r)(s)
          (lista ⊔ listb, sa ⊔ sb)
        })
    }

    def fromSet[R: Manifest, S: Manifest, A: Manifest](xs: Set[A]): SetReaderStateM[R, S, A] =
      SetReaderStateM(r => s => (unit(xs), s))

    def fromSet[R: Manifest, S: Manifest, A: Manifest](xs: Rep[Set[A]]): SetReaderStateM[R, S, A] =
      SetReaderStateM(r => s => (xs, s))

    def empty[R: Manifest, S: Manifest, A: Manifest]: SetReaderStateM[R, S, A] =
      SetReaderStateM(r => s => (Set[A](), s))
  }

  case class SetReaderStateM[R: Manifest, S: Manifest, A: Manifest](run: Rep[R] => Rep[S] => (Rep[Set[A]], Rep[S])) {
    import SetReaderStateM._
    def apply(r: Rep[R])(s: Rep[S]): (Rep[Set[A]], Rep[S]) = run(r)(s)
    def flatMap[B: Manifest](f: Rep[A] => SetReaderStateM[R, S, B]): SetReaderStateM[R, S, B] =
      SetReaderStateM(r => s => {
        val (seta, s0) = run(r)(s)
        val init: Rep[(Set[B], S)] = (Set[B](), s0)
        val res = seta.foldLeft(init) { case (acc_s1, a) =>
          val setacc = acc_s1._1
          val s1 = acc_s1._2
          val fa: SetReaderStateM[R, S, B] = f(a)
          val (setb, s2) = fa.run(r)(s1)
            (setacc ++ setb, s2): Rep[(Set[B], S)]
        }
        res.unlift
      })
    def map[B: Manifest](f: Rep[A] => Rep[B]): SetReaderStateM[R, S, B] =
      SetReaderStateM(r => s => {
        val (seta, s0) = run(r)(s)
        val setb = seta.map(f)
        (setb, s0)
      })
    def filter(f: Rep[A] => Rep[Boolean]): SetReaderStateM[R, S, A] =
      SetReaderStateM(r => s => {
        val (seta, s0) = run(r)(s)
        (seta.filter(f), s0)
      })
    def withFilter(f: Rep[A] => Rep[Boolean]): SetReaderStateM[R, S, A] = filter(f)
  }

  /////////////////////////////////////////////

  object SetStateReaderStateM {
    type SSRS[R,S1,S2,A] = SetStateReaderStateM[R, S1, S2, A]
    def apply[R:Manifest,S1:Manifest,S2:Manifest,A:Manifest](implicit m: SSRS[R,S1,S2,A]): SSRS[R,S1,S2,A] = m

    implicit def SetStateReaderStateMonad[R: Manifest, S1: Manifest, S2: Manifest] =
      new Monad[SetStateReaderStateM[R,S1,S2,*]] {
        def flatMap[A:Manifest, B:Manifest](ma: SSRS[R,S1,S2,A])(f: Rep[A] => SSRS[R,S1,S2,B]) =
          ma.flatMap(f)
        def pure[A: Manifest](a: Rep[A]): SSRS[R,S1,S2,A] =
          SetStateReaderStateM(s1 => r => s2 => ((Set[A](a),s1),s2))
        def put1(s1: => Rep[S1]): SSRS[R,S1,S2,Unit] =
          SetStateReaderStateM(_ => r => s2 => ((Set[Unit](()), s1), s2))
        def put2(s2: => Rep[S2]): SSRS[R,S1,S2,Unit] =
          SetStateReaderStateM(s1 => r => _ => ((Set[Unit](()), s1), s2))
        def get1: SSRS[R,S1,S2,S1] = SetStateReaderStateM(s1 => r => s2 => ((Set[S1](s1), s1), s2))
        def get2: SSRS[R,S1,S2,S2] = SetStateReaderStateM(s1 => r => s2 => ((Set[S2](s2), s1), s2))

        def mod1(f: Rep[S1] => Rep[S1]): SSRS[R,S1,S2,Unit] =
          SetStateReaderStateM(s1 => r => s2 => ((Set[Unit](()), f(s1)), s2))
        def mod2(f: Rep[S2] => Rep[S2]): SSRS[R,S1,S2,Unit] =
          SetStateReaderStateM(s1 => r => s2 => ((Set[Unit](()), s1), f(s2)))

        def ask: SSRS[R, S1, S2, R] = SetStateReaderStateM(s1 => r => s2 => ((Set[R](r), s1), s2))
        def local[A: Manifest](ma: SSRS[R, S1, S2, A])(f: Rep[R] => Rep[R]): SSRS[R, S1, S2, A] =
          SetStateReaderStateM(s1 => r => s2 => ma.run(s1)(f(r))(s2))

        override def filter[A: Manifest](ma: SSRS[R,S1,S2,A])(f: Rep[A] => Rep[Boolean]): SSRS[R,S1,S2,A] =
          ma.filter(f)
    }

    implicit def SetStateReaderStateMonadPlus[R: Manifest, S1: Manifest : RepLattice, S2: Manifest : RepLattice] =
      new MonadPlus[SetStateReaderStateM[R,S1,S2,*]] {
        def mzero[A: Manifest : RepLattice]: SSRS[R, S1, S2, A] = empty[R, S1, S2, A]
        def mplus[A: Manifest : RepLattice](a: SSRS[R, S1, S2, A], b: SSRS[R, S1, S2, A]): SSRS[R, S1, S2, A] =
          SetStateReaderStateM(s1 => r => s2 => {
            val ((seta, s1a), s2a) = a.run(s1)(r)(s2)
            val ((setb, s1b), s2b) = b.run(s1)(r)(s2)
            ((seta ⊔ setb, s1a ⊔ s1b), s2a ⊔ s2b)
          })
      }

    def fromSet[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest](xs: Set[A]): SSRS[R,S1,S2,A] =
      SetStateReaderStateM(s1 => r => s2 => ((unit(xs), s1), s2))

    def fromSet[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest](xs: Rep[Set[A]]): SSRS[R,S1,S2,A] =
      SetStateReaderStateM(s1 => r => s2 => ((xs, s1), s2))

    def empty[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest]: SSRS[R,S1,S2,A] =
      SetStateReaderStateM(s1 => r => s2 => ((Set[A](), s1), s2))
  }

  case class SetStateReaderStateM[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest]
    (run: Rep[S1] => Rep[R] => Rep[S2] => ((Rep[Set[A]], Rep[S1]), Rep[S2])) {
    import SetStateReaderStateM._

    type Result = ((Rep[Set[A]], Rep[S1]), Rep[S2])
    type MM[T] = SetStateReaderStateM[R, S1, S2, T]
    def apply(s1: Rep[S1], r: Rep[R], s2: Rep[S2]): Result = run(s1)(r)(s2)

    def flatMap[B: Manifest](f: Rep[A] => MM[B]): MM[B] = {
      SetStateReaderStateM(s1 => r => s2 => {
        val ((seta, s10), s20) = run(s1)(r)(s2)
        val init = (Set[B](), s10, s20)
        val res = seta.foldLeft(init) { case (acc_s11_s21, a) =>
          val setacc = acc_s11_s21._1
          val s11 = acc_s11_s21._2
          val s21 = acc_s11_s21._3
          val fa: MM[B] = f(a)
          val ((setb, s12), s22) = fa.run(s11)(r)(s21)
          (setacc ++ setb, s12, s22)
        }
        res.unliftLeft
      })
    }

    def map[B: Manifest](f: Rep[A] => Rep[B]): MM[B] =
      SetStateReaderStateM(s1 => r => s2 => {
        val ((seta, s10), s20) = run(s1)(r)(s2)
        val setb = seta.map(f)
        ((setb, s10), s20)
      })

    def filter(f: Rep[A] => Rep[Boolean]): MM[A] =
      SetStateReaderStateM(s1 => r => s2 => {
        val ((seta, s10), s20) = run(s1)(r)(s2)
        ((seta.filter(f), s10), s20)
      })

    def withFilter(f: Rep[A] => Rep[Boolean]): MM[A] = filter(f)
  }
}

