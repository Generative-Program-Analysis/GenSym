package sai.structure.arrow

import sai.structure.functor._
import sai.structure.monoid._
import sai.structure.monad._

trait Arrow[A[_, _]] {
  def arr[B, C](f: B => C): A[B, C]
  def >>>[B, C, D](a1: A[B, C], a2: A[C, D]): A[B, D]
  def fst[B, C, D](a: A[B, C]): A[(B, D), (C, D)]
}

case class Kleisli[M[_], A, B](run: A => M[B]) {
  def apply(a: A): M[B] = run(a)
}

object Arrows {
  def MonadIsArrow[M[_]: Monad] = new Arrow[Kleisli[M, ?, ?]] {
    def arr[B, C](f: B => C): Kleisli[M, B, C] = 
      Kleisli[M, B, C](b => Monad[M].pure(f(b)))
    def >>>[B, C, D](f: Kleisli[M, B, C], g: Kleisli[M, C, D]): Kleisli[M, B, D] =
      Kleisli[M, B, D](b => Monad[M].flatMap(f(b))(g.apply))
    def fst[B, C, D](f: Kleisli[M, B, C]): Kleisli[M, (B, D), (C, D)] =
      Kleisli[M, (B, D), (C, D)] { case (b, d) =>
        Monad[M].flatMap(f(b))(c => Monad[M].pure(c, d))
      }
  }
}
