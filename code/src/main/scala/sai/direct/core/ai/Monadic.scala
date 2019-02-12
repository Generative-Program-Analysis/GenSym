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

  trait Monad[M[_]] {
    def unit[A](a: A): M[A]
    def bind[A, B](m: M[A])(f: A => M[B]): M[B]
  }

  // TODO: abstract values

  trait Semantics[M[_]] { self: Monad[M] =>
    trait Value
    case object Wrong extends Value
    case class Num(i: Int) extends Value
    case class Fun(f: Value => M[Value]) extends Value

    type Env = Map[Name, Value]

    def lookup(x: Name, env: Env): M[Value] = env.get(x) match {
      case Some(v) => unit(v)
      case None => unit(Wrong)
    }

    def add(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Num(x), Num(y)) => unit(Num(x + y))
      case _ => unit(Wrong)
    }

    def apply(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Fun(k), a) => k(a)
      case _ => unit(Wrong)
    }

    def interp(e: Term, env: Env): M[Value] = e match {
      case Var(x) => lookup(x, env)
      case Con(i) => unit(Num(i))
      case Add(e1, e2) =>
        bind(interp(e1, env))(v1 => bind(interp(e2, env))(v2 => add(v1, v2)))
      case Lam(x, e) => unit(Fun(v => interp(e, env + (x -> v))))
      case App(e1, e2) =>
        bind(interp(e1, env))(v1 => bind(interp(e2, env))(v2 => apply(v1, v2)))
    }
  }

  /****************************************************/

  type Id[T] = T
  trait IdMonad extends Monad[Id] {
    def unit[A](a: A) = a
    def bind[A, B](m: Id[A])(f: A => Id[B]) = f(m)
  }

  case class StandardInterp() extends Semantics[Id] with IdMonad

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

  case class ErrMsgInterp() extends Semantics[ErrMsg] with ErrMonad

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

  case class StateInterp() extends Semantics[IntState] with StateMonad

  /****************************************************/

  trait NDMonad extends Monad[List] {
    def unit[A](a: A) = List(a)
    def bind[A, B](m: List[A])(f: A => List[B]) =
      for (a <- m; b <- f(a)) yield b
  }

  case class NDInterp() extends Semantics[List] with NDMonad {
    override def interp(e: Term, env: Env) = e match {
      case Fail() => List()
      case Amb(e1, e2) => interp(e1, env) ++ interp(e2, env)
      case _ => super.interp(e, env)
    }
  }
}
