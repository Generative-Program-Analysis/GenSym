package sai
package lens

case class Lens[A, B, S, T](view: S => A, update: (S, B) => T)

trait Profunctor[P[_, _]] {
  def dimap[A, B, C, D](f: C => A, g: B => D)(pab: P[A, B]): P[C, D]
}

trait Cartesian[P[_, _]] {
  def second[A, B, C](pab: P[A, B])(implicit p: Profunctor[P]): P[(C, A), (C, B)]
}
