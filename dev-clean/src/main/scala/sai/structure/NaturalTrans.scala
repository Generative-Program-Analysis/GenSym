package sai.structure

trait NaturalTransformation[F[_], G[_]] {
  def transform[A](fa: F[A]): G[A]
}
