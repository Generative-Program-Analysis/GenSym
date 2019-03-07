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

@virtualize
trait SAIMonads { self: SAIDsl =>

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

  trait MonadPlus[M[_]]  {
    def mzero[A: Manifest]: M[A]
    def mplus[A: Manifest](a: M[A], b: M[A]): M[A]
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

    implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Manifest] = new MonadPlus[StateT[M, S, ?]] {
      def mzero[A: Manifest]: StateT[M, S, A] = ???
      def mplus[A: Manifest](a: StateT[M, S, A], b: StateT[M, S, A]): StateT[M, S, A] =
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
                 val a: Rep[A] = as1._1; val s1: Rep[S] = as1._2
                 f(a)
             })
    def withFilter(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] = filter(f)
  }

  object ListT {
    def apply[M[_]: Monad, A: Manifest](implicit m: ListT[M, A]): ListT[M, A] = m

    implicit def ListTMonad[M[_]: Monad] = new Monad[ListT[M, ?]] {
      def flatMap[A: Manifest, B: Manifest](la: ListT[M, A])(f: Rep[A] => ListT[M, B]) = la.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ListT[M, A] = ListT(Monad[M].pure(List(a)))
      def filter[A: Manifest](la: ListT[M, A])(f: Rep[A] => Rep[Boolean]): ListT[M, A] = la.filter(f)
    }

    implicit def ListTMonadPlus[M[_]: Monad] = new MonadPlus[ListT[M, ?]] {
      def mzero[A: Manifest]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
      def mplus[A: Manifest](a: ListT[M, A], b: ListT[M, A]): ListT[M, A] = a ++ b
    }

    def fromList[M[_]: Monad, A: Manifest](xs: Rep[List[A]]): ListT[M, A] =
      ListT(Monad[M].pure(xs))
    def listM[G[_]: Monad, A: Manifest](ga: G[A]): ListT[G, A] =
      ListT(Monad[G].map(ga)((a: Rep[A]) => List(a)))

    def empty[M[_]: Monad, A: Manifest]: ListT[M, A] = ListTMonadPlus[M].mzero[A]
  }

  def emit_foldMap[M[_]: Monad, A: Manifest, B: Manifest](xs: Rep[List[A]], f: Rep[A => ListT[M, B]]): Rep[M[List[B]]]

  case class ListT[M[_]: Monad, A: Manifest](run: M[List[A]]) {
    import ListT._

    def apply: M[List[A]] = run

    def ++(ys: ListT[M, A]): ListT[M, A] =
      ListT(Monad[M].flatMap(run) { list1 =>
              Monad[M].map(run) { list2 =>
                list1 ++ list2
              }
            })

    def flatMap[B: Manifest](f: Rep[A] => ListT[M, B]): ListT[M, B] =
      // run: M[List[A]]
      ListT(Monad[M].flatMap(run) { case list: Rep[List[A]] =>
              val merge: Rep[List[B]] =
                /*
                list.foldLeft(ListT.empty[M, B]) {
                  case (acc: ListT[M, B], a: Rep[A]) => acc ++ f(a)
                }
                */
                list.foldLeft(List[B]()) {
                  case (acc: Rep[List[B]], a: Rep[A]) =>
                    val fa: ListT[M, B] = f(a)
                    val listb: M[List[B]] = Monad[M].map(fa.run) { case falist: Rep[List[B]] => acc ++ falist }
                    ??? // expected Rep[List[B]], a comonad operation `extract: w a -> a`!
                }
              Monad[M].pure[List[B]](merge)
            })

    @virtualize
    def flatMapCps[M[_]: Monad, B: Manifest](list: Rep[List[A]], acc: ListT[M, B], f: Rep[A] => ListT[M, B], k: Rep[List[B]] => Rep[List[B]]): Rep[List[B]] = {
      if (list.isEmpty) {
        k(acc)
      }
      else {
        val nacc = acc ++ f(list.hd)
        flatMapCps(list.tl, nacc, f, k)
      }
    }

    def map[B: Manifest](f: Rep[A] => Rep[B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { list => Monad[M].pure(list.map(f)) })

    def filter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      ListT(Monad[M].map(run) { list => list.filter(f) })

    def withFilter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      filter(f)
  }

}

