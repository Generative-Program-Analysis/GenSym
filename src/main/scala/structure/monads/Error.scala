package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

/* Error Monad */

abstract class Err[+A]
case object Bad extends Err[Nothing]
case class Ok[+A](a: A) extends Err[A]

object ErrM {
  def apply[A](implicit m: ErrM[A]): ErrM[A] = m
  implicit val ErrMonadInstance: Monad[ErrM] = new Monad[ErrM] {
    def pure[A](a: A): ErrM[A] = ErrM(Ok(a))
    def flatMap[A, B](ma: ErrM[A])(f: A => ErrM[B]): ErrM[B] = ma.flatMap(f)
  }
  implicit val ErrMonadPlusInstance: MonadPlus[ErrM] = new MonadPlus[ErrM] {
    def mzero[A: Lattice]: ErrM[A] = ErrM(Ok(Lattice[A].bot))
    def mplus[A: Lattice](a: ErrM[A], b: ErrM[A]): ErrM[A] = (a, b) match {
      case (ErrM(Ok(x)), ErrM(Ok(y))) => ErrM(Ok(x ⊔ y))
      case (ErrM(Ok(x)), ErrM(Bad)) => ErrM(Ok(x))
      case (ErrM(Bad), ErrM(Ok(y))) => ErrM(Ok(y))
      case (ErrM(Bad), ErrM(Bad)) => ErrM(Bad)
    }
  }

  def err[A]: ErrM[A] = ErrM[A](Bad)
}

case class ErrM[A](run: Err[A]) {
  def apply: Err[A] = run
  def flatMap[B](f: A => ErrM[B])(implicit mB: Manifest[B] = null): ErrM[B] = run match {
    case Ok(a) => f(a)
    case Bad => ErrM[B](Bad)
  }
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ErrM[B] = run match {
    case Ok(a) => ErrM(Ok(f(a)))
    case Bad => ErrM[B](Bad)
  }
}

