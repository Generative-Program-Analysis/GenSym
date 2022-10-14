package gensym.structure.monoid

trait Monoid[A] {
  def zero: A
  def append(a: A, b: A): A
}
object Monoid {
  def apply[A](implicit m: Monoid[A]): Monoid[A] = m
}
