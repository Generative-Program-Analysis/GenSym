package sai.structure.monad
package free

import sai.structure.functor._
import sai.structure.~>

abstract class Free[F[_]: Functor, A] {
  def map[B](f: A => B): Free[F, B] = this match {
    case Pure(a) => Pure(f(a))
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
  }

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
    case Pure(a) => f(a)
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }
}
case class Pure[F[_]: Functor, A](a: A) extends Free[F, A]
case class Impure[F[_]: Functor, A](x: F[Free[F, A]]) extends Free[F, A]

object Free {
  def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f

  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
    new Functor[Free[F, ?]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x.map(f)
    }

  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, ?]] =
    new Monad[Free[F, ?]] {
      def pure[A](a: A): Free[F, A] = Pure[F, A](a)
      def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma.flatMap(f)
    }
}

object Coproduct {
  abstract class Coprod[F[_], G[_], A]
  case class Left[F[_], G[_], A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_], G[_], A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})

  implicit def CoproductFunctorInstance[F[_]: Functor, G[_]: Functor]: Functor[(F ⊕ G)#t] =
    new Functor[(F ⊕ G)#t] {
      def map[A, B](x: Coprod[F, G, A])(f: A => B): Coprod[F, G, B] = x match {
        case Left(x) => Left(Functor[F].map(x)(f))
        case Right(x) => Right(Functor[G].map(x)(f))
      }
    }
}

sealed trait ⊆[F[_], G[_]] {
  def inj[A](sub: F[A]): G[A]
  def prj[A](sup: G[A]): Option[F[A]]
}

object ⊆ {
  import Free._
  import Coproduct._

  def inject[F[_]: Functor, G[_]: Functor, R](x: F[Free[G, R]])(implicit I: F ⊆ G): Free[G, R] =
    Impure(I.inj(x))

  def project[F[_]: Functor, G[_]: Functor, R](x: Free[G, R])(implicit I: F ⊆ G): Option[F[Free[G, R]]] =
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

  implicit def injRight[F[_], G[_], H[_]](implicit I: F ⊆ G) =
    new (F ⊆ (H ⊕ G)#t) {
      def inj[A](sub: F[A]) = Right(I.inj(sub))
      def prj[A](sup: Coprod[H, G, A]) = sup match {
        case Left(_) => None
        case Right(x) => I.prj(x)
      }
    }
}

object NondetHandler {
  import Free._
  import Coproduct._
  import ⊆._

  trait Nondet[+K]
  case object Fail extends Nondet[Nothing]
  case class Choice[K](k1: K, k2: K) extends Nondet[K]

  implicit def NondetFunctorInstance: Functor[Nondet] =
    new Functor[Nondet] {
      def map[A, B](x: Nondet[A])(f: A => B): Nondet[B] = x match {
        case Fail => Fail
        case Choice(k1, k2) => Choice[B](f(k1), f(k2))
      }
    }

  def fail[F[_]: Functor, A](implicit I: Nondet ⊆ F): Free[F, A] = inject[Nondet, F, A](Fail)

  def choice[F[_]: Functor, A](f: Free[F, A], g: Free[F, A])(implicit I: Nondet ⊆ F): Free[F, A] =
    inject[Nondet, F, A](Choice(f, g))

  object FailPattern {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Boolean = {
      project[Nondet, F, A](x) match {
        case Some(Fail) => true
        case _ => false
      }
    }
  }

  object ChoicePattern {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Option[(Free[F, A], Free[F, A])] = {
      project[Nondet, F, A](x) match {
        case Some(Choice(p, q)) => Some((p, q))
        case _ => None
      }
    }
  }

  def run[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] =
    prog match {
      case Pure(x) => Monad[Free[F, ?]].pure(List(x))
      case FailPattern() => Monad[Free[F, ?]].pure(List())
      case ChoicePattern(p, q) =>
        for {
          ps <- run(p)
          qs <- run(q)
        } yield ps ++ qs
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(a => run[F, A](a)))
    }

  def apply[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] = run(prog)
}

object StateHandler {
  import Free._
  import Coproduct._
  import ⊆._

  trait State[S, A]
  case class Get[S, A](k: S => A) extends State[S, A]

  /*
  object Get {
    def apply[F[_]: Functor, S](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
      inject[State[S, ?], F, S](Get(Pure(_)))
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: (State[S, ?] ⊆ F)): Option[S => Free[F, A]] = {
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
    }
  }
   */
  case class Put[S, A](s: S, k: A) extends State[S, A]

  implicit def StateFunctorInstance[S]: Functor[State[S, ?]] =
    new Functor[State[S, ?]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: Functor, S](implicit I: State[S, ?] ⊆ F): Free[F, S] =
    inject[State[S, ?], F, S](Get(Pure(_)))

  def put[F[_]: Functor, S](s: S)(implicit I: State[S, ?] ⊆ F): Free[F, Unit] =
    inject[State[S, ?], F, Unit](Put(s, Monad[Free[F, ?]].pure(())))

  object GetPattern {
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[S => Free[F, A]] = {
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
    }
  }

  object PutPattern {
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[(S, Free[F, A])] = {
      project[State[S, ?], F, A](x) match {
        case Some(Put(s, a)) => Some((s, a))
        case _ => None
      }
    }
  }

  def run[F[_]: Functor, S, A](s: S, prog: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      case Pure(a) => Monad[Free[F, ?]].pure((s, a))
      case GetPattern(k) => run(s, k(s))
      case PutPattern(s1, k) => run(s1, k)
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(a => run[F, S, A](s, a)))
    }
}

object StateNondetHandler {
  import ⊆._
  import Free._
  import Coproduct._
  import StateHandler._
  import NondetHandler._

  def runLocal[F[_]: Functor, S, A](s: S, prog: Free[(State[S, ?] ⊕ (Nondet ⊕ F)#t)#t, A]): Free[F, List[(S, A)]] =
    NondetHandler.run(StateHandler.run(s, prog))

  def runGlobal[F[_]: Functor, S, A](s: S, prog: Free[(Nondet ⊕ (State[S, ?] ⊕ F)#t)#t, A]): Free[F, (S, List[A])] =
    StateHandler.run(s, NondetHandler.run(prog))

  def inc[F[_]: Functor](implicit I: State[Int, ?] ⊆ F): Free[F, Unit] =
    for {
      x <- get[F, Int]
      _ <- put[F, Int](x + 1)
    } yield ()

  def choices[F[_]: Functor, A](prog: Free[F, A])(implicit I1: Nondet ⊆ F, I2: State[Int, ?] ⊆ F): Free[F, A] =
    prog match {
      case Pure(x) => Monad[Free[F, ?]].pure(x)
      case FailPattern() => fail
      case ChoicePattern(p, q) =>
        for {
          _ <- inc[F]
          pq <- choice(p, q)
        } yield pq
      case Impure(op) => Impure(Functor[F].map(op)(choices(_)))
    }

  //TODO: redefine knapsack/select
}

// TODO: CPS effect, to support Imp's break from while loop

object VoidHandler {
  trait Void[+K]

  implicit val VoidFunctorInstance: Functor[Void] =
    new Functor[Void] {
      def map[A, B](x: Void[A])(f: A => B): Void[B] = x.asInstanceOf[Void[B]]
    }

  def run[A](f: Free[Void, A]): A = f match {
    case Pure(x) => x
  }

  def apply[A](f: Free[Void, A]): A = run(f)
}

object NondetVoidHandler {
  import Coproduct._
  import NondetHandler._
  import VoidHandler._

  def allsols[A](prog: Free[(Nondet ⊕ Void)#t, A]): List[A] =
    VoidHandler(NondetHandler(prog))
}

object Knapsack {
  import Coproduct._
  import NondetHandler._
  import VoidHandler._
  import NondetVoidHandler._

  type Eff[A] = Free[(Nondet ⊕ Void)#t, A]

  def select[A](xs: List[A]): Eff[A] =
    xs.map(Pure[(Nondet ⊕ Void)#t, A]).foldRight[Eff[A]](fail)(choice)

  def knapsack(w: Int, vs: List[Int]): Eff[List[Int]] = {
    if (w < 0)
      fail
    else if (w == 0)
      Monad[Eff].pure(List()) //TODO: simplify syntax
    else for {
      v <- select(vs)
      xs <- knapsack(w-v, vs)
    } yield v :: xs
  }

  def main(args: Array[String]) {
    println(allsols(knapsack(3, List(3, 2, 1))))
  }
}

/*******************************************/

object ReaderWriterEffect {
  trait ReaderWriter[I, O, X]
  case class Get[I, O, X](f: I => X) extends ReaderWriter[I, O, X]
  case class Put[I, O, X](o: O, f: Unit => X) extends ReaderWriter[I, O, X]
}
