package sai
package monads

object NoRep {
  type NoRep[T] = T
}

import NoRep._

trait RMonadOps[R[_], Mo[_], E] {
  def map[B](f: R[E] => R[B])(implicit mB: Manifest[B] = null): Mo[B]
  def flatMap[B](f: R[E] => Mo[B])(implicit mB: Manifest[B] = null): Mo[B]
}

trait MonadOps[Mo[_], A] extends RMonadOps[NoRep, Mo, A]
