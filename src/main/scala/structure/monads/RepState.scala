package sai.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import gensym.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

@virtualize
trait RepStateMonad { self: RepMonads with SAIOps =>
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
    implicit def apply[M[_]: Monad, S: Manifest]: Monad[StateT[M, S, *]] = StateTMonad[M, S]

    implicit def StateTMonad[M[_]: Monad, S: Manifest] = new MonadState[StateT[M, S, *], S] {
      def flatMap[A: Manifest, B: Manifest](sa: StateT[M, S, A])(f: Rep[A] => StateT[M, S, B]) = sa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): StateT[M, S, A] = StateT(s => Monad[M].pure((a, s)))

      def get: StateT[M, S, S] = StateT(s => Monad[M].pure((s, s)))
      def put(s: Rep[S]): StateT[M, S, Unit] = StateT(_ => Monad[M].pure((unit(()), s)))
      def mod(f: Rep[S] => Rep[S]): StateT[M, S, Unit] = StateT(s => Monad[M].pure((unit(()), f(s))))

      override def filter[A: Manifest](sa: StateT[M, S, A])(f: Rep[A] => Rep[Boolean]): StateT[M, S, A] = sa.filter(f)
    }

    implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Manifest : RepLattice] =
      new MonadPlus[StateT[M, S, *]] {
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
}
