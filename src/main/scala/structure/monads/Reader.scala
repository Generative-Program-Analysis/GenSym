package gensym.structure.monad

import gensym.structure.functor._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

/* Reader Monad and Transformer */

trait MonadReader[F[_], R] extends Monad[F] {
  def ask: F[R]
  def local[A](fa: F[A])(f: R => R): F[A]
}

object MonadReader {
  def apply[F[_], S](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
}

object ReaderM {
  def apply[R, A](implicit m: ReaderM[R, A]): ReaderM[R, A] = m

  implicit def ReaderMonadInstance[R] = new MonadReader[ReaderM[R, *], R] {
    def flatMap[A, B](fa: ReaderM[R, A])(f: A => ReaderM[R, B]): ReaderM[R, B] = fa.flatMap(f)
    def pure[A](a: A): ReaderM[R, A] = ReaderM(_ => a)
    def ask: ReaderM[R, R] = ReaderM(r => r)
    def local[A](fa: ReaderM[R, A])(f: R => R): ReaderM[R, A] =
      ReaderM(f andThen fa.run)
  }
}

case class ReaderM[R, A](run: R => A) {
  import ReaderM._
  def apply(r: R): A = run(r)
  def flatMap[B](f: A => ReaderM[R, B])(implicit mB: Manifest[B] = null): ReaderM[R, B] =
    ReaderM(r => f(run(r)).run(r))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ReaderM[R, B] =
    ReaderM(r => f(run(r)))
  //def filter(f: A => Boolean): ReaderM[R, A] = throw new Exception("Not supported")
  //def withFilter(f: A => Boolean): ReaderM[R, A] = filter(f)
}

object ReaderT {
  def apply[M[_]: Monad, R, A](implicit m: ReaderT[M, R, A]): ReaderT[M, R, A] = m

  implicit def ReaderTMonad[M[_]: Monad, R] = new MonadReader[ReaderT[M, R, *], R] {
    def flatMap[A, B](fa: ReaderT[M, R, A])(f: A => ReaderT[M, R, B]) = fa.flatMap(f)
    def pure[A](a: A): ReaderT[M, R, A] = ReaderT(_ => Monad[M].pure(a))
    override def filter[A](fa: ReaderT[M, R, A])(f: A => Boolean): ReaderT[M, R, A] = fa.filter(f)

    def ask: ReaderT[M, R, R] = ReaderT(r => Monad[M].pure(r))
    def local[A](fa: ReaderT[M, R, A])(f: R => R): ReaderT[M, R, A] =
      ReaderT(f andThen fa.run)
  }
  implicit def ReaderTMonadPlus[M[_]: Monad : MonadPlus, R] = new MonadPlus[ReaderT[M, R, *]] {
    def mzero[A: Lattice]: ReaderT[M, R, A] = ReaderT(r => MonadPlus[M].mzero)
    def mplus[A: Lattice](a: ReaderT[M, R, A], b: ReaderT[M, R, A]): ReaderT[M, R, A] =
      ReaderT(r => MonadPlus[M].mplus(a.run(r), b.run(r)))
  }

  def liftM[G[_]: Monad, R, A](ga: G[A]): ReaderT[G, R, A] =
    ReaderT(r => ga)
}

case class ReaderT[M[_]: Monad, R, A](run: R => M[A]) {
  import ReaderT._
  def apply(r: R): M[A] = run(r)

  def flatMap[B](f: A => ReaderT[M, R, B])(implicit mB: Manifest[B] = null): ReaderT[M, R, B] =
    ReaderT(r => Monad[M].flatMap(run(r))(a => f(a).run(r)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ReaderT[M, R, B] =
    ReaderT(r => Monad[M].map(run(r))(f))
  def filter(f: A => Boolean): ReaderT[M, R, A] =
    ReaderT(r => Monad[M].filter(run(r))(f))
  def withFilter(f: A => Boolean): ReaderT[M, R, A] = filter(f)
}

