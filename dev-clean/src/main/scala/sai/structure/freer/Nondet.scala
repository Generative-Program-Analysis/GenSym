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

  def run_with_mt[A]: Comp[Nondet ⊗ ∅, A] => Comp[∅, List[A]] =
    handler[Nondet, ∅, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, List[A]] {
      def apply[X] = (_, _) match {
        case Nondet$(xs, k) =>
          ret(xs.foldLeft(List[A]()) { case (zs, x) =>
            // Here: it works because k(x) directly yields a pure value, involves no effectful computation.
            zs ++ k(x)
          })
      }
    })

  /* Idea: transform nondet effects to a local CPS form
  def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, CPS[List[A]]] =
    handler[Nondet, E, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, E, List[A]] {
      def apply[X] = (_, _) match {
        case Nondet$(xs, k) =>
          def cont(kk: X => List[A]): List[A] = {

          }
          cont
      }
    })
   */

  // def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, ( => ) => Rep[List[A]]] =

  def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    handler[Nondet, E, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, E, List[A]] {
      def apply[X] = (_, _) match {
        case Nondet$(xs, k) =>
          xs.foldLeft[Comp[E, List[A]]](ret(List())) { case (comp, x) =>
            for {
              rs <- comp
              xs <- k(x)
            } yield rs ++ xs
          }
          /*
          def nd(xs: List[X], acc: Comp[E, List[A]], k: (Comp[E, List[A]], X) => Comp[E, List[A]]): Comp[E, List[A]] = {
            if (xs.isEmpty) acc
            else nd(xs.tail, k(acc, xs.head), k)
          }
          nd(xs, Return(List()), { case (comp, x) =>
            comp.flatMap(rs => k(x).map(xs => rs ++ xs))
          })
           */
          /*
          var i: Int = 0
          var c: Comp[E, List[A]] = Return(List()) // technically, should be fail
          while (i < xs.size) {
            val xs_i = xs(i)
            c = c.flatMap { rs =>
              k(xs_i).map(xs_ => rs ++ xs_)
            }
            i = i + 1
          }
          c
           */
          /*
          xs.map(x => k(x)).foldLeft[Comp[E, List[A]]](ret(List())) { case (a, b) =>
            for {
              xs <- a
              ys <- b
            } yield xs ++ ys
           }
           */
        // case Nondet$(xs: Rep[List[A]], k: Rep[A] => Comp[E, Rep[List[A]]]) =>

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

  def run_with_mt[A]: Comp[Nondet ⊗ ∅, A] => Comp[∅, List[A]] =
    handler[Nondet, ∅, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, List[A]] {
      def apply[X] = (_, _) match {
        case Fail$() => ret(List())
        case Choice$((), k) =>
          val xs: List[A] = k(true)
          val ys: List[A] = k(false)
          ret(xs ++ ys)
      }
    })

}
