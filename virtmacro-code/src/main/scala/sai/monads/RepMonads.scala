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
trait SAIMonads extends RepLattices { self: SAIDsl =>

  trait Monad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
    def map[A: Manifest,B: Manifest](ma: M[A])(f: Rep[A] => Rep[B]): M[B] = flatMap(ma)(a => pure(f(a)))
    def filter[A: Manifest](ma: M[A])(f: Rep[A] => Rep[Boolean]): M[A]
  }

  object Monad {
    def apply[M[_]](implicit m: Monad[M]): Monad[M] = m
  }

  /////////////////////////////////////////////////

  trait MonadPlus[M[_]] { // TODO: Why not extends from Monad?
    def mzero[A: Manifest : RepLattice]: M[A]
    def mplus[A: Manifest : RepLattice](a: M[A], b: M[A]): M[A]
  }

  object MonadPlus {
    def apply[M[_]](implicit m: MonadPlus[M]): MonadPlus[M] = m
  }

  /////////////////////////////////////////////////

  object IdMonadInstance {
    type Id[T] = Rep[T]
    implicit val IdMonad: Monad[Id] = new Monad[Id] {
      def pure[A: Manifest](a: Rep[A]): Id[A] = a
      def flatMap[A: Manifest, B: Manifest](ma: Id[A])(f: Rep[A] => Id[B]): Id[B] = f(ma)
      def filter[A: Manifest](ma: Id[A])(f: Rep[A] => Rep[Boolean]): Id[A] = throw new Exception("Not supported")
    }
  }

  object IdMonadObj {
    def apply[A: Manifest](implicit m: IdM[A]): IdM[A] = m

    implicit val IdMonadInstance: Monad[IdM] = new Monad[IdM] {
      def pure[A: Manifest](a: Rep[A]): IdM[A] = IdM(a)
      def flatMap[A: Manifest, B: Manifest](ma: IdM[A])(f: Rep[A] => IdM[B]): IdM[B] = ma.flatMap(f)
      def filter[A: Manifest](ma: IdM[A])(f: Rep[A] => Rep[Boolean]): IdM[A] = throw new Exception("Not supported")
    }
  }
  case class IdM[A: Manifest](run: Rep[A]) {
    import IdMonadObj._
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

    implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Manifest : RepLattice] = new MonadPlus[StateT[M, S, ?]] {
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
      StateT(s => Monad[M].flatMap(run(s)) {
               case as1: Rep[(A, S)] =>
                 val a: Rep[A] = as1._1; val s1: Rep[S] = as1._2
                 f(a).run(s1)
             })
    def map[B: Manifest](f: Rep[A] => Rep[B]): StateT[M, S, B] =
      flatMap(a => StateT(s => Monad[M].pure((f(a), s))))

    def filter(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] =
      StateT(s => Monad[M].filter(run(s)) {
               case as1: Rep[(A, S)] =>
                 val a: Rep[A] = as1._1 //; val s1: Rep[S] = as1._2
                 f(a)
             })
    def withFilter(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] = filter(f)
  }

  /////////////////////////////////////////////////

  @deprecated
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

  @deprecated
  case class ListT[M[_]: Monad, A: Manifest](run: M[List[A]]) {
    import ListT._

    def apply: M[List[A]] = run

    def ++(ys: ListT[M, A]): ListT[M, A] =
      ListT(Monad[M].flatMap(run) { list1 => Monad[M].map(run) { list2 => list1 ++ list2 } })

    def flatMap[B: Manifest](f: Rep[A] => ListT[M, B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { case list: Rep[List[A]] =>
              val merge: Rep[List[B]] =
                list.foldLeft(List[B]()) {
                  case (acc: Rep[List[B]], a: Rep[A]) =>
                    val fa: M[List[B]] = f(a).run
                    val listb: M[List[B]] = Monad[M].map(fa) { case falist: Rep[List[B]] => acc ++ falist }
                    ??? // expected Rep[List[B]], a comonad operation `extract: w a -> a`!
                }
              Monad[M].pure[List[B]](merge)
            })

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
                         lista.foldLeft(init) { case (acc: Rep[(List[B], S)], a: Rep[A]) =>
                           val fa: ListReaderStateM[R, S, B] = f(a)
                           val listacc = acc._1
                           val s1 = acc._2
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

}

