package sai.structure.freer3

import scala.language.{higherKinds, implicitConversions}

object NondetList {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  case class Nondet[A](xs: List[A])

  def fail[R <: Eff, A](implicit I: Nondet ∈ R): Comp[R, A] = perform(Nondet(List()))

  def choice[R <: Eff, A](x: A, y: A)(implicit I: Nondet ∈ R): Comp[R, A] =
    perform(Nondet(List(x, y)))

  def select[R <: Eff, A](xs: List[A])(implicit I: Nondet ∈ R): Comp[R, A] =
    perform(Nondet(xs))

  object Nondet$ {
    def unapply[A, R](n: (Nondet[A], A => R)): Option[(List[A], A => R)] =
      n match {
        case (Nondet(xs), k) => Some((xs, k))
        case _ => None
      }
  }

  def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    handler[Nondet, E, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, E, List[A]] {
      def apply[X] = (_, _) match {
        case Nondet$(xs, k) =>
          for {
            x <- k(xs(0))
            y <- k(xs(1))
          } yield x ++ y
          /*
          for {
            xs <- k(true)
            ys <- k(false)
          } yield xs ++ ys
           */
      }
    })
}

object Nondet {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  sealed trait Nondet[K]
  case object Fail extends Nondet[Nothing]   //Fail ()   ~> Nothing
  case object Choice extends Nondet[Boolean] //Choice () ~> Boolean

  object Fail$ {
    def unapply[K,R](n: (Nondet[K], K => R)): Boolean = n match {
      case (Fail, _) => true
      case _ => false
    }
  }

  object Choice$ {
    def unapply[K,R](n: (Nondet[K], K => R)): Option[(Unit, Boolean => R)] = n match {
      case (Choice, k) => Some(((), k))
      case _ => None
    }
  }

  def fail[R <: Eff, A](implicit I: Nondet ∈ R): Comp[R,A] = perform(Fail)

  def choice[R <: Eff, A](a: Comp[R, A], b: Comp[R, A])(implicit I: Nondet ∈ R): Comp[R, A] =
    perform(Choice) >>= {
      case true  => a
      case false => b
    }

  def select[R <: Eff, A](xs: List[A])(implicit I: Nondet ∈ R): Comp[R, A] =
    xs.map(Return[R, A]).foldRight[Comp[R, A]](fail)(choice)

  def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    handler[Nondet, E, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, E, List[A]] {
      def apply[X] = (_, _) match {
        case Fail$() => ret(List())
        case Choice$((), k) =>
          for {
            xs <- k(true)
            ys <- k(false)
          } yield xs ++ ys
      }
    })

}
