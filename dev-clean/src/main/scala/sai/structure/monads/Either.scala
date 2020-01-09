package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

/* Either Monad and Transformer */

object EitherM {
  def apply[E, A](implicit m: EitherM[E, A]): EitherM[E, A] = m

  implicit def EitherMonadInstance[E] = new Monad[EitherM[E, ?]] {
    def flatMap[A, B](fa: EitherM[E, A])(f: A => EitherM[E, B]): EitherM[E, B] = fa.flatMap(f)
    def pure[A](a: A): EitherM[E, A] = EitherM(Right(a))
  }

  def left[E, A](e: E): EitherM[E, A] = EitherM(Left(e))
  def right[E, A](a: A): EitherM[A, A] = EitherM(Right(a))
}

case class EitherM[E, A](run: Either[E, A]) {
  def apply: Either[E, A] = run
  def flatMap[B](f: A => EitherM[E, B])(implicit mB: Manifest[B] = null): EitherM[E, B] = run match {
    case Left(e) => EitherM[E, B](Left(e))
    case Right(a) => f(a)
  }
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): EitherM[E, B] = run match {
    case Left(e) => EitherM[E, B](Left(e))
    case Right(a) => EitherM[E, B](Right(f(a)))
  }
}

object EitherT {
  def apply[M[_]: Monad, E, A](implicit m: EitherT[M, E, A]) = m

  implicit def EitherTMonadInstance[M[_]: Monad, E] = new Monad[EitherT[M, E, ?]] {
    def flatMap[A, B](fa: EitherT[M, E, A])(f: A => EitherT[M, E, B]) = fa.flatMap(f)
    def pure[A](a: A): EitherT[M, E, A] = EitherT(Monad[M].pure(Right(a)))
  }

  def liftM[G[_]: Monad, E, A](ga: G[A]): EitherT[G, E, A] =
    EitherT(Monad[G].map(ga)(Right(_)))

  def left[M[_]: Monad, E, A](e: E): EitherT[M, E, A] =
    EitherT(Monad[M].pure(Left(e)))

  def right[M[_]: Monad, E, A](a: A): EitherT[M, E, A] =
    EitherT(Monad[M].pure(Right(a)))
}

case class EitherT[M[_]: Monad, E, A](run: M[Either[E, A]]) {
  import EitherT._

  def apply: M[Either[E, A]] = run
  def flatMap[B](f: A => EitherT[M, E, B])(implicit mB: Manifest[B] = null): EitherT[M, E, B] =
    EitherT(Monad[M].flatMap(run) {
      case Left(e) => Monad[M].pure(Left(e))
      case Right(a) => f(a).run
    })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): EitherT[M, E, B] = 
    EitherT(Monad[M].flatMap(run) {
      case Left(e) => Monad[M].pure(Left(e))
      case Right(a) => Monad[M].pure(Right(f(a)))
    })
  def filter(f: A => Boolean): EitherT[M, E, A] = ???
}

