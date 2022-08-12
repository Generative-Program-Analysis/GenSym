package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

object IdType {
  type Id[T] = T
}

/* Binding-time-aware Monad */

trait RMonad[R[_], M[_]] {
  def pure[A](a: R[A]): M[A]
  def flatMap[A, B](ma: M[A])(f: R[A] => M[B]): M[B]
  def map[A,B](ma: M[A])(f: R[A] => R[B]): M[B] = flatMap(ma)(a => pure(f(a)))
  //TODO: refactor filter, not necessary for monad
  def filter[A](ma: M[A])(f: R[A] => R[Boolean]): M[A] =
    throw new Exception("Not supported")

  def unit[A](a: R[A]): M[A] = pure(a)
  def bind[A, B](ma: M[A])(f: R[A] => M[B]): M[B] = flatMap(ma)(f)
  def join[A](mma: M[M[A]]): M[A] = flatMap(mma) { case ma: M[A] => ma }
}

trait Monad[M[_]] extends RMonad[IdType.Id, M] with Functor[M]

object Monad {
  def apply[M[_]](implicit m: Monad[M]): Monad[M] = m

  def mapM[M[_]: Monad, A, B](xs: List[A])(f: A => M[B])(implicit mB: Manifest[B] = null): M[List[B]] =
    xs match {
      case Nil => Monad[M].pure(List.empty[B])
      case x::xs => Monad[M].flatMap(f(x)) { b =>
        Monad[M].flatMap(mapM(xs)(f)) { bs =>
          Monad[M].pure(b::bs)
        }
      }
    }

  def forM[M[_]: Monad, A, B](xs: List[A])(f: A => M[B])(implicit mB: Manifest[B] = null): M[B] =
    xs match {
      case x::Nil => f(x)
      case x::xs => Monad[M].flatMap(f(x)) { _ => forM(xs)(f) }
    }
}

/* MonadPlus */

trait MonadPlus[M[_]] {
  def mzero[A: Lattice]: M[A]
  def mplus[A: Lattice](a: M[A], b: M[A]): M[A]
}

object MonadPlus {
  def apply[M[_]](implicit m: MonadPlus[M]): MonadPlus[M] = m
}

