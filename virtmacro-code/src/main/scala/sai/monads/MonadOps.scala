package sai
package monads

import sai.lattices._
import sai.lattices.Lattices._

object NoRep {
  type NoRep[+T] = T
}

trait RMonadOps[R[_], M[_], A] {
  def map[B](f: R[A] => R[B])(implicit mB: Manifest[B] = null): M[B]
  def flatMap[B](f: R[A] => M[B])(implicit mB: Manifest[B] = null): M[B]
}
