package sai.structure.arrow

import sai.structure.functor._
import sai.structure.monoid._
import sai.structure.monad._

trait Arrow[A[_, _]] {
  def arr[B, C](f: B => C): A[B, C]
  def >>>[B, C, D](a1: A[B, C], a2: A[C, D]): A[B, D]
  def fst[B, C, D](a: A[B, C]): A[(B, D), (C, D)]

  def snd[B, C, D](f: A[B, C]): A[(D, B), (D, C)] =
    >>>(>>>(arr(swap[D, B]), fst[B, C, D](f)), arr(swap[C, D]))
  def ***[B, C, D, E](f: A[B, C], g: A[D, E]): A[(B, D), (C, E)] =
    >>>(fst(f), snd(g))
  def &&&[B, C, D](f: A[B, C], g: A[B, D]): A[B, (C, D)] =
    >>>(arr((b: B) => (b, b)), ***(f, g))

  private def swap[A, B](ab: (A, B)): (B, A) = ab match { case (a, b) => (b, a) }
}

case class Kleisli[M[_], A, B](run: A => M[B]) {
  def apply(a: A): M[B] = run(a)
}

object Arrow {
  def apply[A[_, _]](implicit a: Arrow[A]): Arrow[A] = a

  implicit class ArrowOps[A[_, _]: Arrow, B, C](f: A[B, C]) {
    def >>>[D](g: A[C, D]): A[B, D] = Arrow[A].>>>(f, g)
    def ***[D, E](g: A[D, E]): A[(B, D), (C, E)] = Arrow[A].***(f, g)
    def &&&[D](g: A[B, D]): A[B, (C, D)] = Arrow[A].&&&(f, g)
  }

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

  // An example

  def add[A[_, _]: Arrow, B](f: A[B, Int], g: A[B, Int]): A[B, Int] = {
    val A = Arrow[A]; import A._
    (f &&& g) >>> arr { case (u: Int, v: Int) => u + v }
  }
}
