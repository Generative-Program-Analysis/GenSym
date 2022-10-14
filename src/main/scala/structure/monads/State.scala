package gensym.structure.monad

import gensym.structure.functor._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

/* State Monad and Transformer */

trait MonadState[F[_], S] extends Monad[F] {
  def get: F[S]
  def put(s: S): F[Unit]
  def mod(f: S => S): F[Unit]
}

object MonadState {
  def apply[F[_], S](implicit s: MonadState[F, S]): MonadState[F, S] = s
}

object StateM {
  def apply[S, A](implicit m: StateM[S, A]): StateM[S, A] = m

  implicit def StateMonadInstance[S] = new MonadState[StateM[S, *], S] {
    def flatMap[A, B](sa: StateM[S, A])(f: A => StateM[S, B]): StateM[S, B] =
      sa.flatMap(f)
    def pure[A](a: A): StateM[S, A] = StateM(s => (a, s))

    def get: StateM[S, S] = StateM(s => (s, s))
    def put(s: S): StateM[S, Unit] = StateM(_ => ((), s))
    def mod(f: S => S): StateM[S, Unit] = StateM(s => ((), f(s)))
  }
}

case class StateM[S, A](run: S => (A, S)) {
  import StateM._

  def apply(s: S): (A, S) = run(s)
  def flatMap[B](f: A => StateM[S, B])(implicit mB: Manifest[B] = null): StateM[S, B] =
    StateM(s => run(s) match { case (a, s1) => f(a).run(s1) })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): StateM[S, B] =
    StateM(s => run(s) match { case (a, s1) => (f(a), s1) })

  //def filter(f: A => Boolean): StateM[S, A] = throw new Exception("Not supported")
  //def withFilter(f: A => Boolean): StateM[S, A] = filter(f)
}

object StateT {
  def apply[M[_]: Monad, S, A](implicit m: StateT[M, S, A]): StateT[M, S, A] = m
  implicit def apply[M[_]: Monad, S]: Monad[StateT[M, S, *]] = StateTMonad[M, S]

  implicit def StateTMonad[M[_]: Monad, S] = new MonadState[StateT[M, S, *], S] {
    def flatMap[A, B](sa: StateT[M, S, A])(f: A => StateT[M, S, B]) = sa.flatMap(f)
    def pure[A](a: A): StateT[M, S, A] = StateT(s => Monad[M].pure((a, s)))
    override def filter[A](sa: StateT[M, S, A])(f: A => Boolean): StateT[M, S, A] = sa.filter(f)

    def get: StateT[M, S, S] = StateT(s => Monad[M].pure((s, s)))
    def put(s: S): StateT[M, S, Unit] = StateT(_ => Monad[M].pure(((), s)))
    def mod(f: S => S): StateT[M, S, Unit] = StateT(s => Monad[M].pure(((), f(s))))
  }

  implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Lattice] = new MonadPlus[StateT[M, S, *]] {
    def mzero[A: Lattice]: StateT[M, S, A] = StateT(s => MonadPlus[M].mzero)
    def mplus[A: Lattice](a: StateT[M, S, A], b: StateT[M, S, A]): StateT[M, S, A] =
      StateT(s => MonadPlus[M].mplus(a.run(s), b.run(s)))
  }

  def liftM[G[_]: Monad, S, A](ga: G[A]): StateT[G, S, A] =
    StateT(s => Monad[G].map(ga)(a => (a, s)))
}

case class StateT[M[_]: Monad, S, A](run: S => M[(A, S)]) {
  import StateT._
  def apply(s: S): M[(A, S)] = run(s)
  def flatMap[B](f: A => StateT[M, S, B])(implicit mB: Manifest[B] = null): StateT[M, S, B] =
    StateT(s => Monad[M].flatMap(run(s)) { case (a, s1) => f(a).run(s1) })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): StateT[M, S, B] =
    flatMap(a => StateT(s => Monad[M].pure((f(a), s))))

  def filter(f: A => Boolean): StateT[M, S, A] =
    StateT(s => Monad[M].filter(run(s)) { case (a,s) => f(a) })
  def withFilter(f: A => Boolean): StateT[M, S, A] = filter(f)
}

