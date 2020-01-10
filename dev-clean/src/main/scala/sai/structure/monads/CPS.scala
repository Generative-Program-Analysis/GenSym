package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

object CpsM {
  def apply[R, A](implicit m: CpsM[R, A]): CpsM[R, A] = m

  implicit def CpsMonadInstance[R] = new Monad[CpsM[R, ?]] {
    def flatMap[A, B](fa: CpsM[R, A])(f: A => CpsM[R, B]) = fa.flatMap(f)
    def pure[A](a: A): CpsM[R, A] = CpsM((k: A => R) => k(a))
  }
}

case class CpsM[R, A](run: (A => R) => R) {
  import CpsM._

  def apply(k: A => R): R = run(k)
  def flatMap[B](f: A => CpsM[R, B])(implicit mB: Manifest[B] = null): CpsM[R, B] = 
    CpsM[R, B]((k: B => R) => run(a => f(a).run(k)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): CpsM[R, B] =
    CpsM[R, B]((k: B => R) => run(a => k(f(a))))
}

object CpsT {
  def apply[M[_]: Monad, R, A](implicit m: CpsT[M, R, A]): CpsT[M, R, A] = m
  
}

case class CpsT[M[_]: Monad, R, A](run: (A => M[R]) => M[R]) {
  import CpsT._

  def apply(k: A => M[R]): M[R] = run(k)
  def flatMap[B](f: A => CpsT[M, R, B])(implicit mB: Manifest[B] = null): CpsT[M, R, B] =
    CpsT[M, R, B]((k: B => M[R]) => run(a => f(a).run(k)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): CpsT[M, R, B] =
    CpsT[M, R, B]((k: B => M[R]) => run(a => k(f(a))))
}
