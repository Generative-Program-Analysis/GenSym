package sai
package monads

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.lattices._
import sai.lattices.Lattices._

@virtualize
trait RepMonads extends RepLattices { self: SAIDsl =>

  trait Monad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
    def map[A: Manifest,B: Manifest](ma: M[A])(f: Rep[A] => Rep[B]): M[B] = flatMap(ma)(a => pure(f(a)))
    def filter[A: Manifest](ma: M[A])(f: Rep[A] => Rep[Boolean]): M[A]
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

  @deprecated("Use IdM", "")
  object IdMonadInstance {
    type Id[T] = Rep[T]
    implicit val IdMonad: Monad[Id] = new Monad[Id] {
      def pure[A: Manifest](a: Rep[A]): Id[A] = a
      def flatMap[A: Manifest, B: Manifest](ma: Id[A])(f: Rep[A] => Id[B]): Id[B] = f(ma)
      def filter[A: Manifest](ma: Id[A])(f: Rep[A] => Rep[Boolean]): Id[A] = throw new Exception("Not supported")
    }
  }

  /////////////////////////////////////////////////

  object IdM {
    def apply[A: Manifest](implicit m: IdM[A]): IdM[A] = m

    implicit val IdMonadInstance: Monad[IdM] = new Monad[IdM] {
      def pure[A: Manifest](a: Rep[A]): IdM[A] = IdM(a)
      def flatMap[A: Manifest, B: Manifest](ma: IdM[A])(f: Rep[A] => IdM[B]): IdM[B] = ma.flatMap(f)
      def filter[A: Manifest](ma: IdM[A])(f: Rep[A] => Rep[Boolean]): IdM[A] = throw new Exception("Not supported")
    }

    implicit val IdMonadPlus: MonadPlus[IdM] = new MonadPlus[IdM] {
      def mzero[A: Manifest : RepLattice]: IdM[A] = IdM(RepLattice[A].bot)
      def mplus[A: Manifest : RepLattice](a: IdM[A], b: IdM[A]): IdM[A] = IdM(a.run ⊔ b.run)
    }
  }

  case class IdM[A: Manifest](run: Rep[A]) {
    import IdM._
    def apply: Rep[A] = run
    def flatMap[B: Manifest](f: Rep[A] => IdM[B]): IdM[B] = f(run)
    def map[B: Manifest](f: Rep[A] => Rep[B]): IdM[B] = IdM(f(run))
  }

  /////////////////////////////////////////////////

  trait MonadReader[F[_], R] extends Monad[F] {
    def ask: F[R]
    def local[A: Manifest](fa: F[A])(f: Rep[R] => Rep[R]): F[A]
  }

  object MonadReader {
    def apply[F[_], S: Manifest](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
  }

  object ReaderT {
    def apply[M[_]: Monad, R: Manifest, A: Manifest](implicit m: ReaderT[M, R, A]): ReaderT[M, R, A] = m
    implicit def apply[M[_]: Monad, R: Manifest]: Monad[ReaderT[M, R, ?]] = ReaderTMonad[M, R]

    implicit def ReaderTMonad[M[_]: Monad, R: Manifest] = new MonadReader[ReaderT[M, R, ?], R] {
      def flatMap[A: Manifest, B: Manifest](fa: ReaderT[M, R, A])(f: Rep[A] => ReaderT[M, R, B]): ReaderT[M, R, B] =
        fa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ReaderT[M, R, A] = ReaderT(_ => Monad[M].pure(a))
      def filter[A: Manifest](fa: ReaderT[M, R, A])(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] = fa.filter(f)

      def ask: ReaderT[M, R, R] = ReaderT(r => Monad[M].pure(r))
      def local[A: Manifest](fa: ReaderT[M, R, A])(f: Rep[R] => Rep[R]): ReaderT[M, R, A] =
        ReaderT(f andThen fa.run)
    }

    implicit def ReaderTMonadPlus[M[_]: Monad : MonadPlus, R: Manifest] = new MonadPlus[ReaderT[M, R, ?]] {
      def mzero[A: Manifest : RepLattice]: ReaderT[M, R, A] = ReaderT(r => MonadPlus[M].mzero)
      def mplus[A: Manifest : RepLattice](a: ReaderT[M, R, A], b: ReaderT[M, R, A]): ReaderT[M, R, A] =
        ReaderT(r => MonadPlus[M].mplus(a.run(r), b.run(r)))
    }

    def liftM[G[_]: Monad, R: Manifest, A: Manifest](ga: G[A]): ReaderT[G, R, A] =
      ReaderT(r => ga)
  }

  case class ReaderT[M[_]: Monad, R: Manifest, A: Manifest](run: Rep[R] => M[A]) {
    import ReaderT._

    def apply(r: Rep[R]): M[A] = run(r)
    def flatMap[B: Manifest](f: Rep[A] => ReaderT[M, R, B]): ReaderT[M, R, B] =
      ReaderT(r => Monad[M].flatMap(run(r))(a => f(a).run(r)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): ReaderT[M, R, B] =
      ReaderT((r: Rep[R]) => Monad[M].map(run(r))(f))

    def filter(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] =
      ReaderT(r => Monad[M].filter(run(r))(f))
    def withFilter(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] =
      filter(f)
  }

  /////////////////////////////////////////////////

  trait MonadState[F[_], S] extends Monad[F] {
    def get: F[S]
    def put(s: Rep[S]): F[Unit]
    def mod(f: Rep[S] => Rep[S]): F[Unit]
  }

  object MonadState {
    def apply[F[_], S](implicit s: MonadState[F, S]): MonadState[F, S] = s
  }

  object StateT {
    def apply[M[_]: Monad, S: Manifest, A: Manifest](implicit m: StateT[M, S, A]): StateT[M, S, A] = m
    implicit def apply[M[_]: Monad, S: Manifest]: Monad[StateT[M, S, ?]] = StateTMonad[M, S]

    implicit def StateTMonad[M[_]: Monad, S: Manifest] = new MonadState[StateT[M, S, ?], S] {
      def flatMap[A: Manifest, B: Manifest](sa: StateT[M, S, A])(f: Rep[A] => StateT[M, S, B]) = sa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): StateT[M, S, A] = StateT(s => Monad[M].pure((a, s)))
      def filter[A: Manifest](sa: StateT[M, S, A])(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] = sa.filter(f)

      def get: StateT[M, S, S] = StateT(s => Monad[M].pure((s, s)))
      def put(s: Rep[S]): StateT[M, S, Unit] = StateT(_ => Monad[M].pure((unit(()), s)))
      def mod(f: Rep[S] => Rep[S]): StateT[M, S, Unit] = StateT(s => Monad[M].pure((unit(()), f(s))))
    }

    implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Manifest : RepLattice] =
      new MonadPlus[StateT[M, S, ?]] {
        def mzero[A: Manifest : RepLattice]: StateT[M, S, A] = StateT(s => MonadPlus[M].mzero)
        def mplus[A: Manifest : RepLattice](a: StateT[M, S, A], b: StateT[M, S, A]): StateT[M, S, A] =
          StateT(s => MonadPlus[M].mplus(a.run(s), b.run(s)))
      }

    def liftM[G[_]: Monad, S: Manifest, A: Manifest](ga: G[A]): StateT[G, S, A] =
      StateT(s => Monad[G].map(ga)(a => (a, s)))
  }

  case class StateT[M[_]: Monad, S: Manifest, A: Manifest](run: Rep[S] => M[(A, S)]) {
    import StateT._
    def apply(s: Rep[S]): M[(A, S)] = run(s)
    def flatMap[B: Manifest](f: Rep[A] => StateT[M, S, B]): StateT[M, S, B] =
      StateT(s => {
               Monad[M].flatMap(run(s)) {
                 case as1: Rep[(A, S)] =>
                   val a: Rep[A] = as1._1; val s1: Rep[S] = as1._2
                   f(a).run(s1)
               }
             })
    def map[B: Manifest](f: Rep[A] => Rep[B]): StateT[M, S, B] =
      flatMap(a => StateT(s => Monad[M].pure((f(a), s))))

    def filter(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] =
      StateT(s => Monad[M].filter(run(s)) { case as1: Rep[(A, S)] => f(as1._1) })
    def withFilter(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] = filter(f)
  }

  /////////////////////////////////////////////////

  object ListT {
    def apply[M[_]: Monad, A: Manifest](implicit m: ListT[M, A]): ListT[M, A] = m

    implicit def ListTMonad[M[_]: Monad] = new Monad[ListT[M, ?]] {
      def flatMap[A: Manifest, B: Manifest](la: ListT[M, A])(f: Rep[A] => ListT[M, B]) = la.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ListT[M, A] = ListT(Monad[M].pure(List(a)))
      def filter[A: Manifest](la: ListT[M, A])(f: Rep[A] => Rep[Boolean]): ListT[M, A] = la.filter(f)
    }

    implicit def ListTMonadPlus[M[_]: Monad] = new MonadPlus[ListT[M, ?]] {
      def mzero[A: Manifest : RepLattice]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
      def mplus[A: Manifest : RepLattice](a: ListT[M, A], b: ListT[M, A]): ListT[M, A] = a ++ b
    }

    def fromList[M[_]: Monad, A: Manifest](xs: Rep[List[A]]): ListT[M, A] =
      ListT(Monad[M].pure(xs))
    def listM[G[_]: Monad, A: Manifest](ga: G[A]): ListT[G, A] =
      ListT(Monad[G].map(ga)((a: Rep[A]) => List(a)))

    def empty[M[_]: Monad, A: Manifest]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
  }

  case class ListT[M[_]: Monad, A: Manifest](run: M[List[A]]) {
    import ListT._

    def apply: M[List[A]] = run

    def ++(ys: ListT[M, A]): ListT[M, A] =
      ListT(Monad[M].flatMap(run) { list1 => Monad[M].map(run) { list2 => list1 ++ list2 } })

    def flatMap[B: Manifest](f: Rep[A] => ListT[M, B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { case xs: Rep[List[A]] =>
        /*
        xs.foldLeftK(List[B]())({
          case (acc: Rep[List[B]], x: Rep[A], k: Rep[List[B]] => ListT[M, B]) =>
            (ListT.fromList(acc) ++ f(x)).flatMap(k)
        })
         */
        ???
        /*
        (foldLeftM(list, ListT.empty[M, B]) {
          case (acc: ListT[M, B], a: Rep[A]) => acc ++ f(a)
        }).run
         */
      })

    /*
    def flatMap[B: Manifest](f: Rep[A] => ListT[M, B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { case list: Rep[List[A]] =>
              val merge: Rep[List[B]] = list.foldLeft(List[B]()) {
                case (acc: Rep[List[B]], a: Rep[A]) =>
                  val fa: M[List[B]] = f(a).run
                  val listb: M[List[B]] = Monad[M].map(fa) { case falist: Rep[List[B]] => acc ++ falist }
                    ??? // expected Rep[List[B]], a comonad operation `extract: w a -> a`!
              }
              Monad[M].pure[List[B]](merge)
            })
     */

    def map[B: Manifest](f: Rep[A] => Rep[B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { list => Monad[M].pure(list.map(f)) })

    def filter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      ListT(Monad[M].map(run) { list => list.filter(f) })

    def withFilter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      filter(f)
  }

  /////////////////////////////////////////////////

  object ListReaderStateM {
    def apply[R: Manifest, S: Manifest, A: Manifest](implicit m: ListReaderStateM[R, S, A]): ListReaderStateM[R, S, A] = m

    implicit def ListReaderStateMonad[R: Manifest, S: Manifest] =
      new Monad[ListReaderStateM[R, S, ?]] with MonadState[ListReaderStateM[R, S, ?], S] with MonadReader[ListReaderStateM[R, S, ?], R] {
      def flatMap[A: Manifest, B: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[A] => ListReaderStateM[R, S, B]) = ma.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ListReaderStateM[R, S, A] = ListReaderStateM(r => s => (List[A](a), s))
      def filter[A: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[A] => Rep[Boolean]): ListReaderStateM[R, S, A] = ma.filter(f)

      def get: ListReaderStateM[R, S, S] = ListReaderStateM(r => s => (List[S](s), s))
      def put(s: Rep[S]): ListReaderStateM[R, S, Unit] = ListReaderStateM(r => _ => (List[Unit](()), s))
      def mod(f: Rep[S] => Rep[S]): ListReaderStateM[R, S, Unit] = ListReaderStateM(r => s => (List[Unit](()), f(s)))

      def ask: ListReaderStateM[R, S, R] = ListReaderStateM(r => s => (List[R](r), s))
      def local[A: Manifest](ma: ListReaderStateM[R, S, A])(f: Rep[R] => Rep[R]): ListReaderStateM[R, S, A] =
        ListReaderStateM(r => s => ma.run(f(r))(s))
    }

    implicit def ListReaderStateMonadPlus[R: Manifest, S: Manifest : RepLattice] = new MonadPlus[ListReaderStateM[R, S, ?]] {
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

  // ListT[ReaderT[StateT[Id, ?], ?], ?]
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
                         val init: Rep[(List[B], S)] = (List[B](), s0)
                         lista.foldLeftPair(init) { case ((listacc, s1), a) =>
                           val fa: ListReaderStateM[R, S, B] = f(a)
                           val (listb, s2) = fa.run(r)(s1)
                           (listacc ++ listb, s2): Rep[(List[B], S)]
                         }
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
      new Monad[SetReaderStateM[R, S, ?]] with MonadState[SetReaderStateM[R, S, ?], S] with MonadReader[SetReaderStateM[R, S, ?], R] {
      def flatMap[A: Manifest, B: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[A] => SetReaderStateM[R, S, B]) = ma.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): SetReaderStateM[R, S, A] = SetReaderStateM(r => s => (Set[A](a), s))
      def filter[A: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[A] => Rep[Boolean]): SetReaderStateM[R, S, A] = ma.filter(f)

      def get: SetReaderStateM[R, S, S] = SetReaderStateM(r => s => (Set[S](s), s))
      def put(s: Rep[S]): SetReaderStateM[R, S, Unit] = SetReaderStateM(r => _ => (Set[Unit](()), s))
      def mod(f: Rep[S] => Rep[S]): SetReaderStateM[R, S, Unit] = SetReaderStateM(r => s => (Set[Unit](()), f(s)))

      def ask: SetReaderStateM[R, S, R] = SetReaderStateM(r => s => (Set[R](r), s))
      def local[A: Manifest](ma: SetReaderStateM[R, S, A])(f: Rep[R] => Rep[R]): SetReaderStateM[R, S, A] =
        SetReaderStateM(r => s => ma.run(f(r))(s))
    }

    implicit def SetReaderStateMonadPlus[R: Manifest, S: Manifest : RepLattice] = new MonadPlus[SetReaderStateM[R, S, ?]] {
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
                        seta.foldLeftPair(init) { case ((setacc, s1), a) =>
                          val fa: SetReaderStateM[R, S, B] = f(a)
                          val (setb, s2) = fa.run(r)(s1)
                          (setacc ++ setb, s2): Rep[(Set[B], S)]
                        }
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
    def apply[R:Manifest,S1:Manifest,S2:Manifest,A:Manifest](implicit m: SetStateReaderStateM[R,S1,S2,A]): SetStateReaderStateM[R,S1,S2,A] = m

    implicit def SetStateReaderStateMonad[R: Manifest, S1: Manifest, S2: Manifest] = new Monad[SetStateReaderStateM[R,S1,S2,?]] {
        def flatMap[A:Manifest, B:Manifest](ma: SSRS[R,S1,S2,A])(f: Rep[A] => SSRS[R,S1,S2,B]) = ma.flatMap(f)
        def pure[A: Manifest](a: Rep[A]): SSRS[R,S1,S2,A] = SetStateReaderStateM(r=>s1=>s2=> ((Set[A](a),s1),s2))
        def filter[A: Manifest](ma: SSRS[R,S1,S2,A])(f: Rep[A] => Rep[Boolean]): SSRS[R,S1,S2,A] = ma.filter(f)

        def put1(s1: => Rep[S1]): SSRS[R,S1,S2,Unit] = SetStateReaderStateM(r => _ => s2 => ((Set[Unit](()), s1), s2))
        def put2(s2: => Rep[S2]): SSRS[R,S1,S2,Unit] = SetStateReaderStateM(r => s1 => _ => ((Set[Unit](()), s1), s2))
        def get1: SSRS[R,S1,S2,S1] = SetStateReaderStateM(r => s1 => s2 => ((Set[S1](s1), s1), s2))
        def get2: SSRS[R,S1,S2,S2] = SetStateReaderStateM(r => s1 => s2 => ((Set[S2](s2), s1), s2))

        def mod1(f: Rep[S1] => Rep[S1]): SSRS[R,S1,S2,Unit] = SetStateReaderStateM(r => s1 => s2 => ((Set[Unit](()), f(s1)), s2))
        def mod2(f: Rep[S2] => Rep[S2]): SSRS[R,S1,S2,Unit] = SetStateReaderStateM(r => s1 => s2 => ((Set[Unit](()), s1), f(s2)))

        def ask: SSRS[R, S1, S2, R] = SetStateReaderStateM(r => s1 => s2 => ((Set[R](r), s1), s2))
        def local[A: Manifest](ma: SSRS[R, S1, S2, A])(f: Rep[R] => Rep[R]): SSRS[R, S1, S2, A] =
          SetStateReaderStateM(r => s1 => s2 => ma.run(f(r))(s1)(s2))
    }

    implicit def SetStateReaderStateMonadPlus[R: Manifest, S1: Manifest : RepLattice, S2: Manifest : RepLattice] =
      new MonadPlus[SetStateReaderStateM[R,S1,S2,?]] {
        def mzero[A: Manifest : RepLattice]: SSRS[R, S1, S2, A] = empty[R, S1, S2, A]
        def mplus[A: Manifest : RepLattice](a: SSRS[R, S1, S2, A], b: SSRS[R, S1, S2, A]): SSRS[R, S1, S2, A] =
          SetStateReaderStateM(r => s1 => s2 => {
            val ((seta, s1a), s2a) = a.run(r)(s1)(s2)
            val ((setb, s1b), s2b) = a.run(r)(s1)(s2)
            ((seta ⊔ setb, s1a ⊔ s1b), s2a ⊔ s2b)
          })
      }

    def fromSet[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest](xs: Set[A]): SSRS[R,S1,S2,A] =
      SetStateReaderStateM(r => s1 => s2 => ((unit(xs), s1), s2))

    def fromSet[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest](xs: Rep[Set[A]]): SSRS[R,S1,S2,A] =
      SetStateReaderStateM(r => s1 => s2 => ((xs, s1), s2))

    def empty[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest]: SSRS[R,S1,S2,A] =
      SetStateReaderStateM(r => s1 => s2 => ((Set[A](), s1), s2))
  }

  case class SetStateReaderStateM[R: Manifest, S1: Manifest, S2: Manifest, A: Manifest]
    (run: Rep[R]=>Rep[S1]=>Rep[S2]=>((Rep[Set[A]], Rep[S1]), Rep[S2])) {
    import SetStateReaderStateM._

    type Result = ((Rep[Set[A]], Rep[S1]), Rep[S2])
    type MM[T] = SetStateReaderStateM[R, S1, S2, T]
    def apply(r: Rep[R], s1: Rep[S1], s2: Rep[S2]): Result = run(r)(s1)(s2)
    def flatMap[B: Manifest](f: Rep[A] => MM[B]): MM[B] = {
      SetStateReaderStateM(r => s1 => s2 => {
                             val ((seta, s10), s20) = run(r)(s1)(s2)
                             val init0: Rep[(Set[B], S1)] = (Set[B](), s10)
                             val init: Rep[((Set[B], S1), S2)] = (init0, s20)
                             val res = seta.foldLeftPairPair(init) { case (((setacc, s11), s21), a) =>
                               val fa: MM[B] = f(a)
                               val ((setb, s12), s22) = fa.run(r)(s11)(s21)
                               val res0: Rep[(Set[B], S1)] = ((setacc ++ setb), s12)
                               (res0, s22): Rep[((Set[B], S1), S2)]
                             }
                             (res._1, res._2)
                           })
    }

    def map[B: Manifest](f: Rep[A] => Rep[B]): MM[B] =
      SetStateReaderStateM(r => s1 => s2 => {
                             val ((seta, s10), s20) = run(r)(s1)(s2)
                             val setb = seta.map(f)
                             ((setb, s10), s20)
                           })

    def filter(f: Rep[A] => Rep[Boolean]): MM[A] =
      SetStateReaderStateM(r => s1 => s2 => {
                             val ((seta, s10), s20) = run(r)(s1)(s2)
                             ((seta.filter(f), s10), s20)
                           })

    def withFilter(f: Rep[A] => Rep[Boolean]): MM[A] = filter(f)
  }

}

