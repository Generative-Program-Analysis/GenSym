package gensym.structure.monad

import gensym.structure.functor._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

object CpsM {
  def apply[R, A](implicit m: CpsM[R, A]): CpsM[R, A] = m

  implicit def CpsMonadInstance[R] = new Monad[CpsM[R, *]] {
    def flatMap[A, B](fa: CpsM[R, A])(f: A => CpsM[R, B]) = fa.flatMap(f)
    def pure[A](a: A): CpsM[R, A] = CpsM((k: A => R) => k(a))
  }

  def callcc[T, R, A](f: (T => CpsM[R, A]) => CpsM[R, T]): CpsM[R, T] =
    CpsM[R, T]((k: T => R) => f((x: T) => CpsM[R, A]((_: (A => R)) => k(x))).run(k))
}

case class CpsM[R, A](run: (A => R) => R) {
  import CpsM._

  def apply(k: A => R): R = run(k)
  def flatMap[B](f: A => CpsM[R, B])(implicit mB: Manifest[B] = null): CpsM[R, B] =
    CpsM[R, B]((k: B => R) => run(f(_)(k)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): CpsM[R, B] =
    CpsM[R, B]((k: B => R) => run(f.andThen(k)))
}

object CpsT {
  def apply[M[_]: Monad, R, A](implicit m: CpsT[M, R, A]): CpsT[M, R, A] = m

  implicit def CpsTMonadInstance[M[_]: Monad, R] = new Monad[CpsT[M, R, *]] {
    def flatMap[A, B](fa: CpsT[M, R, A])(f: A => CpsT[M, R, B]) = fa.flatMap(f)
    def pure[A](a: A): CpsT[M, R, A] = CpsT((k: A => M[R]) => k(a))
  }

  def liftM[G[_]: Monad, R, A](g: G[A]): CpsT[G, R, A] =
    CpsT[G, R, A]((k: A => G[R]) => Monad[G].flatMap(g)(k))

  def callcc[M[_]: Monad, T, R, A](f: (T => CpsT[M, R, A]) => CpsT[M, R, T]): CpsT[M, R, T] =
    CpsT[M, R, T]((k: T => M[R]) => f((x: T) => CpsT[M, R, A]((_: A => M[R]) => k(x))).run(k))
}

case class CpsT[M[_]: Monad, R, A](run: (A => M[R]) => M[R]) {
  import CpsT._

  def apply(k: A => M[R]): M[R] = run(k)
  def flatMap[B](f: A => CpsT[M, R, B])(implicit mB: Manifest[B] = null): CpsT[M, R, B] =
    CpsT[M, R, B]((k: B => M[R]) => run(a => f(a).run(k)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): CpsT[M, R, B] =
    CpsT[M, R, B]((k: B => M[R]) => run(a => k(f(a))))
}
