package sai.structure.functor

trait Functor[F[_]] {
  def map[A, B](x: F[A])(f: A => B): F[B]
}

object Functor {
  def apply[F[_]](implicit f: Functor[F]): Functor[F] = f
}
