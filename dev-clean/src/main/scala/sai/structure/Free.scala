package sai.structure.monad
package free

import sai.structure.functor._
import sai.structure.~>

abstract class Free[F[_]: Functor, A] {
  def map[B](f: A => B): Free[F, B] = this match {
    case Return(a) => Return(f(a))
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
  }

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
    case Return(a) => f(a)
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }
}
case class Return[F[_]: Functor, A](a: A) extends Free[F, A]
case class Impure[F[_]: Functor, A](x: F[Free[F, A]]) extends Free[F, A]

object Free {
  def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f

  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
    new Functor[Free[F, ?]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x.map(f)
    }

  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, ?]] =
    new Monad[Free[F, ?]] {
      def pure[A](a: A): Free[F, A] = Return[F, A](a)
      def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma.flatMap(f)
    }

  def ret[F[_]: Functor, A](x: A): Free[F,A] = Return(x)
}

object Coproduct {
  abstract class Coprod[F[_], G[_], A]
  case class Left[F[_], G[_], A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_], G[_], A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})

  implicit def CoproductFunctor[F[_]: Functor, G[_]: Functor]: Functor[(F ⊕ G)#t] =
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

  implicit def NondetFunctor: Functor[Nondet] =
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
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Boolean =
      project[Nondet, F, A](x) match {
        case Some(Fail) => true
        case _ => false
      }
  }

  object ChoicePattern {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Option[(Free[F, A], Free[F, A])] =
      project[Nondet, F, A](x) match {
        case Some(Choice(p, q)) => Some((p, q))
        case _ => None
      }
  }

  def run[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] =
    prog match {
      case Return(x) => ret(List(x))
      case FailPattern() => ret(List())
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
  case class Put[S, A](s: S, k: A) extends State[S, A]

  /*
  object Get {
    def apply[F[_]: Functor, S](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
      inject[State[S, ?], F, S](Get(Return(_)))
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: (State[S, ?] ⊆ F)): Option[S => Free[F, A]] = {
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
    }
  }
   */

  implicit def StateFunctor[S]: Functor[State[S, ?]] =
    new Functor[State[S, ?]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_] : Functor, S](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
    inject[State[S, ?], F, S](Get(Return(_)))

  def put[F[_] : Functor, S](s: S)(implicit I: State[S, ?] ⊆ F): Free[F, Unit] =
    inject[State[S, ?], F, Unit](Put(s, ret(())))

  object GetPattern {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[S => Free[F, A]] =
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
  }

  object PutPattern {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[(S, Free[F, A])] =
      project[State[S, ?], F, A](x) match {
        case Some(Put(s, a)) => Some((s, a))
        case _ => None
      }
  }

  def run[F[_] : Functor, S, A](s: S, prog: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      case Return(a) => ret((s, a))
      case GetPattern(k) => run(s, k(s))
      case PutPattern(s1, k) => run(s1, k)
      case Impure(Right(op)) => //TODO: have deep_handle and shallow_handle combinators that hide the default case
        Impure(Functor[F].map(op)(run(s, _)))
    }

  def statefun[F[_] : Functor, S, A](comp: Free[(State[S, ?] ⊕ F)#t, A]): S => Free[F, A] =
    comp match {
      case Return(x) => { _ => ret(x) }
      case GetPattern(k) => { s =>
        val f = statefun(k(s)) //implicit resolution stumbles with statefun(k(s))(s)
        f(s)
      }
      case PutPattern(s, k) => { _ =>
        val f = statefun(k)
        f(s)
      }
      case Impure(Right(op)) => { s =>
        Impure(Functor[F].map(op) { a =>
          val f = statefun[F, S, A](a)
          f(s)
        })
      }
    }

  /*def statefun2[F[_] : Functor, S, A](comp: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, S => A] = {
    comp match {
      case Return(x) => ret { _ => x }
      case GetPattern(k) =>
        // k: S => Free[S+F,A]
        // k s :: Free[S+F,A]
        // f := s => statefun2(k(s)) : S => Free[F, S => A]
        // ret { s =>
        //    (f s) : Free[F, S => A]
        //    .bind {g => //S => A
        //           ret (g s) //Free[F,A]
        //     } :Free[F,A]
        //     } : Free[F, S => Free[F,A]]

        // ret k: Free[F,S => Free[S+F,A]]
        // (ret k).map { f => s =>
        //               (statefun2(f(s))  : Free[F, S=> Free[F, S => A]]
        //                flatmap g => g s
        ret(k).map { f =>
          {(s:S) =>
            f(s)


          }
        }
      case PutPattern(s, k) => statefun2(k)

      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(statefun2))
    }
  }*/

  def stateref[F[_] : Functor, S, A](init: S)(comp: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, A] = {
    var state: S = init
    def handler(comp: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, A] = {
      comp match {
        case Return(x) => ret(x)
        case GetPattern(k) => handler(k(state))
        case PutPattern(s, k) =>
          state = s
          handler(k)
        case Impure(Right(op)) =>
          Impure(Functor[F].map(op)(handler))
      }
    }
    handler(comp)
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
}

object VoidHandler {
  trait ∅[+K]

  implicit val VoidFunctor: Functor[∅] =
    new Functor[∅] {
      def map[A, B](x: ∅[A])(f: A => B): ∅[B] = x.asInstanceOf[∅[B]]
    }

  def run[A](f: Free[∅, A]): A = f match {
    case Return(x) => x
  }

  def apply[A](f: Free[∅, A]): A = run(f)
}

object NondetVoidHandler {
  import Coproduct._
  import NondetHandler._
  import VoidHandler._

  def allsols[A](prog: Free[(Nondet ⊕ ∅)#t, A]): List[A] =
    VoidHandler(NondetHandler(prog))
}

// TODO: CPS effect, to support Imp's break from while loop

object Knapsack {
  import Free._
  import Coproduct.{CoproductFunctor => _, _}
  import NondetHandler.{NondetFunctor => _, _}
  import VoidHandler.{VoidFunctor => _, _}
  import StateHandler.{StateFunctor => _, _}
  import NondetVoidHandler._
  import StateNondetHandler._

  def inc[F[_]: Functor](implicit I: State[Int, ?] ⊆ F): Free[F, Unit] =
    for {
      x <- get[F, Int]
      _ <- put[F, Int](x + 1)
    } yield ()

  def choices[F[_]: Functor, A](prog: Free[F, A])(implicit I1: Nondet ⊆ F, I2: State[Int, ?] ⊆ F): Free[F, A] =
    prog match {
      case Return(x) => Monad[Free[F, ?]].pure(x)
      case FailPattern() => fail
      case ChoicePattern(p, q) =>
        for {
          _ <- inc[F]
          pq <- choice(choices(p), choices(q))
        } yield pq
      case Impure(op) => Impure(Functor[F].map(op)(choices(_)))
    }

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def knapsack[F[_]: Functor](w: Int, vs: List[Int])(implicit I: Nondet ⊆ F): Free[F, List[Int]] = {
    if (w < 0)
      fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select(vs)
      xs <- knapsack(w-v, vs)
    } yield v :: xs
  }

  def main(args: Array[String]) {
    import VoidHandler.VoidFunctor
    import StateHandler.StateFunctor
    import NondetHandler.NondetFunctor

    // Only nondeterminism effect
    println(allsols(knapsack(3, List(3, 2, 1))))

    val global: (Int, List[List[Int]]) = {
      // Note: have to manually define an implicit functor instance here,
      //       otherwise Scala compiler complains implicit expansion divergence
      implicit val F = Functor[(State[Int, ?] ⊕ ∅)#t]
      VoidHandler(runGlobal(0, choices(knapsack[(Nondet ⊕ (State[Int, ?] ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(global)

    val local: List[(Int, List[Int])] = {
      implicit val F = Functor[(Nondet ⊕ ∅)#t]
      VoidHandler(runLocal(0, choices(knapsack[(State[Int, ?] ⊕ (Nondet ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(local)
  }
}

/*******************************************/

object ReaderWriterEffect {
  trait ReaderWriter[I, O, X]
  case class Get[I, O, X](f: I => X) extends ReaderWriter[I, O, X]
  case class Put[I, O, X](o: O, f: Unit => X) extends ReaderWriter[I, O, X]
}
