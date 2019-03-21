package sai
package monads

object NoRep {
  type NoRep[T] = T
}

import NoRep._

trait RMonadOps[R[_], M[_], A] {
  def map[B](f: R[A] => R[B])(implicit mB: Manifest[B] = null): M[B]
  def flatMap[B](f: R[A] => M[B])(implicit mB: Manifest[B] = null): M[B]
}

trait MonadOps[M[_], A] extends RMonadOps[NoRep, M, A]
