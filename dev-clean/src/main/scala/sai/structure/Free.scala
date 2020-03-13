package sai.structure.monad
package free

import sai.structure.functor._
import sai.structure.~>

abstract class Free[F[_]: Functor, A]
case class Pure[F[_]: Functor, A](a: A) extends Free[F, A]
case class Impure[F[_]: Functor, A](x: F[Free[F, A]]) extends Free[F, A]

object Free {
  def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f

  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
    new Functor[Free[F, ?]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x match {
        case Pure(a: A) => Pure(f(a))
        case Impure(x: F[Free[F, A]]) => Impure(Functor[F].map(x){
          case xf: Free[F, A] => map(xf)(f)
        })
      }
    }

  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, ?]] =
    new Monad[Free[F, ?]] {
      def pure[A](a: A): Free[F, A] = Pure[F, A](a)
      def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma match {
        case Pure(a: A) => f(a)
        case Impure(x: F[Free[F, A]]) =>
          Impure(Functor[F].map(x)((xf: Free[F, A]) => flatMap(xf)(f)))
      }
    }
}

object ReaderWriterEffect {
  trait ReaderWriter[I, O, X]
  case class Get[I, O, X](f: I => X) extends ReaderWriter[I, O, X]
  case class Put[I, O, X](o: O, f: Unit => X) extends ReaderWriter[I, O, X]
}

object Coproduct {
  abstract class Coprod[F[_], G[_], A]
  case class Left[F[_], G[_], A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_], G[_], A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})
}

sealed trait ⊆[F[_], G[_]] {
  def inj[A](sub: F[A]): G[A]
  def prj[A](sup: G[A]): Option[F[A]]
}

object ⊆ {
  import Free._
  import Coproduct._

  trait Nondet[+K]
  case object Fail extends Nondet[Nothing]
  case class Choice[K](k1: K, k2: K) extends Nondet[K]

  trait Void[+K]

  implicit val VoidFunctorInstance: Functor[Void] =
    new Functor[Void] {
      def map[A, B](x: Void[A])(f: A => B): Void[B] = x.asInstanceOf[Void[B]]
    }

  def run[A](f: Free[Void, A]): A = f match {
    case Pure(x) => x
  }

  implicit def NondetFunctorInstance: Functor[Nondet] =
    new Functor[Nondet] {
      def map[A, B](x: Nondet[A])(f: A => B): Nondet[B] = x match {
        case Fail => Fail
        case Choice(k1, k2) => Choice[B](f(k1), f(k2))
      }
    }

  implicit def CoproductFunctorInstance[F[_]: Functor, G[_]: Functor]: Functor[(F ⊕ G)#t] =
    new Functor[(F ⊕ G)#t] {
      def map[A, B](x: Coprod[F, G, A])(f: A => B): Coprod[F, G, B] = x match {
        case Left(x) => Left(Functor[F].map(x)(f))
        case Right(x) => Right(Functor[G].map(x)(f))
      }
    }

  def inject[F[_]: Functor, G[_]: Functor, R](x: F[Free[G, R]])(implicit I: (F ⊆ G)): Free[G, R] =
    Impure(I.inj(x))

  def project[F[_]: Functor, G[_]: Functor, R](x: Free[G, R])(implicit I: (F ⊆ G)): Option[F[Free[G, R]]] =
    x match {
      case Impure(f) => I.prj(f)
      case _ => None
    }

  implicit def injRefl[F[_]] = new (F ⊆ F) {
    def inj[A](sub: F[A]) = sub
    def prj[A](sup: F[A]) = Some(sup)
  }

  implicit def injLeft[F[_], G[_]] = new (F ⊆ (F ⊕ G)#t) {
    def inj[A](sub: F[A]) = Left(sub)
    def prj[A](sup: Coprod[F, G, A]) = sup match {
      case Left(fa) => Some(fa)
      case Right(_) => None
    }
  }

  implicit def injRight[F[_], G[_], H[_]](implicit I: (F ⊆ G)) =
    new (F ⊆ (H ⊕ G)#t) {
      def inj[A](sub: F[A]) = Right(I.inj(sub))
      def prj[A](sup: Coprod[H, G, A]) = sup match {
        case Left(_) => None
        case Right(x) => I.prj(x)
      }
    }

  object FailPattern {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: (Nondet ⊆ F)): Boolean = {
      project[Nondet, F, A](x) match {
        case Some(Fail) => true
        case _ => false
      }
    }
  }

  object ChoicePattern {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: (Nondet ⊆ F)): Option[(Free[F, A], Free[F, A])] = {
      project[Nondet, F, A](x) match {
        case Some(Choice(p, q)) => Some((p, q))
        case _ => None
      }
    }
  }

  def solutions[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] =
    prog match {
      case Pure(x) => Pure(List(x))
      case FailPattern() => Pure(List())
      case ChoicePattern(p, q) =>
        Monad[Free[F, ?]].flatMap(solutions(p)) { ps =>
          Monad[Free[F, ?]].flatMap(solutions(q)) { qs =>
            Monad[Free[F, ?]].pure[List[A]](ps ++ qs)
          }
        }
      case Impure(Right(op)) => Impure(Functor[F].map(op)(a => solutions[F, A](a)))
    }

  def allsols[A](prog: Free[(Nondet ⊕ Void)#t, A]): List[A] =
    run(solutions(prog))
}
