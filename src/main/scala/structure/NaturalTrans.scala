package gensym.structure

trait NaturalTransformation[F[_], G[_]] {
  def transform[A](fa: F[A]): G[A]
}

trait ~>[F[_], G[_]] extends NaturalTransformation[F, G]
