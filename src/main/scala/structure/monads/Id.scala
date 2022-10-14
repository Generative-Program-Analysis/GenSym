package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

/* Identity Monad */

@deprecated("Use IdM", "")
object IdMonadInstance {
  type Id[T] = T
  implicit val IdMonad: Monad[Id] = new Monad[Id] {
    def pure[A](a: A): A = a
    def flatMap[A, B](ma: Id[A])(f: A => Id[B]): Id[B] = f(ma)
  }

  implicit val IdMonadPlus: MonadPlus[Id] = new MonadPlus[Id] {
    def mzero[A: Lattice]: A = Lattice[A].bot
    def mplus[A: Lattice](a: A, b: A) = Lattice[A].⊔(a, b)
  }
}

object IdM {
  def apply[A](implicit m: IdM[A]): IdM[A] = m

  implicit val IdMonadInstance: Monad[IdM] = new Monad[IdM] {
    def pure[A](a: A): IdM[A] = IdM(a)
    def flatMap[A, B](ma: IdM[A])(f: A => IdM[B]): IdM[B] = ma.flatMap(f)
  }

  implicit val IdMonadPlusInstance: MonadPlus[IdM] = new MonadPlus[IdM] {
    def mzero[A: Lattice]: IdM[A] = IdM(Lattice[A].bot)
    def mplus[A: Lattice](a: IdM[A], b: IdM[A]): IdM[A] = IdM(a.run ⊔ b.run)
  }
}

case class IdM[A](run: A) {
  def apply: A = run
  def flatMap[B](f: A => IdM[B])(implicit mB: Manifest[B] = null): IdM[B] = f(run)
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): IdM[B] = IdM(f(run))
}

