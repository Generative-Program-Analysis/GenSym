package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.utils._
import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOps => _, SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

/* Scala implementation for paper The Essence of Functional Programming, Philip Wadler */

object Lang {
  type Name = String
  trait Term
  case class Var(x: String) extends Term
  case class Con(i: Int) extends Term
  case class Add(e1: Term, e2: Term) extends Term
  case class Lam(x: Name, e: Term) extends Term
  case class App(e1: Term, e2: Term) extends Term

  case class Fail() extends Term
  case class Amb(e1: Term, e2: Term) extends Term
}

object Monadics {
  import Lang._

  /*
   trait M[+A] {
   def unit[A](a: A): M[A]
   def flatMap[B](f: A => M[B]): M[B]
   def map[B](f: A => B): M[B]
   }

   object M {
   def apply[A](implicit v: M[A]): M[A] = v
   }
  */

  trait Monad[M[_]] {
    def unit[A](a: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    def map[A,B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => unit(f(a)))
    def map2[A,B,C](ma: M[A], mb: M[B])(f: (A, B) => C): M[C] =
      flatMap(ma)(a => map(mb)(b => f(a, b)))
  }

  object Monad {
    def apply[M[_]](implicit v: Monad[M]): Monad[M] = v
  }

  implicit class MonadOps[A, M[_]: Monad](ma: M[A]) {
    def map[B](f: A => B): M[B] = Monad[M].map(ma)(f)
    def flatMap[B](f: A => M[B]): M[B] = Monad[M].flatMap(ma)(f)
  }

  type Id[T] = T
  def IdMonad[T]: Monad[Id] = new Monad[Id] {
    def unit[A](a: A): A = a
    def flatMap[A, B](ma: Id[A])(f: A => Id[B]): Id[B] = f(ma)
  }

  trait Semantics {
    type Env
    type Value
    type M[Value] <: MonadOps[Value, M]

    def num(i: Int): M[Value]
    def close(lam: Lam, env: Env): M[Value]
    def add(v1: Value, v2: Value): M[Value]
    def apply(v1: Value, v2: Value): M[Value]
    def lookup(x: Name, env: Env): M[Value]
    def extend(env: Env, x: Name, v: Value): Env

    def interp(e: Term, env: Env): M[Value] = e match {
      case Con(i) => num(i)
      case Var(x) => lookup(x, env)
      case Lam(x, e) => close(Lam(x, e), env)
      case Add(e1, e2) => for {
        v1 <- interp(e1, env)
        v2 <- interp(e2, env)
        rt <- add(v1, v2)
      } yield rt
      case App(e1, e2) => for {
        v1 <- interp(e1, env)
        v2 <- interp(e2, env)
        rt <- apply(v1, v2)
      } yield rt
    }
  }

  trait Concrete extends Semantics {
    trait Value
    case object Wrong extends Value
    case class Num(i: Int) extends Value
    case class Fun(f: Value => M[Value]) extends Value

    type Env = Map[Name, Value]

    def num(i: Int): M[Value] = Monad[M].unit(Num(i))

    /*
    def close(lam: Lam, env: Env): M[Value] = lam match {
      case Lam(x, e) => M[Value].unit(Fun(v => interp(e, extend(env, x, v))))
    }

    def lookup(x: Name, env: Env): M[Value] = env.get(x) match {
      case Some(v) => M[Value].unit(v)
      case None => M[Value].unit(Wrong)
    }

    def extend(env: Env, x: Name, v: Value) = env + (x -> v)

    def add(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Num(x), Num(y)) => M[Value].unit(Num(x + y))
      case _ => M[Value].unit(Wrong)
    }

    def apply(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Fun(k), a) => k(a)
      case _ => M[Value].unit(Wrong)
    }
     */
  }

  /****************************************************/

  /*
  type Id[T] = T
  trait IdMonad[A] extends M[A] {
    def unit[A](a: A) = a
    def bind[A, B](m: Id[A])(f: A => Id[B]) = f(m)
  }

  case class StandardInterp() extends Concrete with IdMonad

  /****************************************************/

  abstract class ErrMsg[T]
  case class Success[T](a: T) extends ErrMsg[T]
  case class Error[T](msg: String) extends ErrMsg[T]

  trait ErrMonad extends Monad[ErrMsg] {
    def unit[A](a: A) = Success(a)
    def bind[A, B](m: ErrMsg[A])(f: A => ErrMsg[B]) = m match {
      case Success(a) => f(a)
      case Error(msg) => Error(msg)
    }
  }

  case class ErrMsgInterp() extends Concrete[ErrMsg] with ErrMonad

  /****************************************************/

  type IntState[T] = Int => (T, Int)
  trait StateMonad extends Monad[IntState] {
    def unit[A](a: A) = s => (a, s)
    def bind[A, B](m: IntState[A])(f: A => IntState[B]) = s => {
      val (a, s1) = m(s)
      val (b, s2) = f(a)(s1)
      (b, s2)
    }
  }

  case class StateInterp() extends Concrete[IntState] with StateMonad

  /****************************************************/

  trait NDMonad extends Monad[List] {
    def unit[A](a: A) = List(a)
    def bind[A, B](m: List[A])(f: A => List[B]) =
      for (a <- m; b <- f(a)) yield b
  }

  case class NDInterp() extends Concrete[List] with NDMonad {
    override def interp(e: Term, env: Env) = e match {
      case Fail() => List()
      case Amb(e1, e2) => interp(e1, env) ++ interp(e2, env)
      case _ => super.interp(e, env)
    }
  }
   */
}
