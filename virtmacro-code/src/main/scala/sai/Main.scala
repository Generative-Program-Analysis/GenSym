package sai

import scala.language.implicitConversions

import cats._
import cats.{Id, ~>}
import cats.implicits._
import cats.data.State
import cats.data.IndexedStateT
import cats.data.Kleisli
import cats.free.Free
import cats.free.Free.liftF
import cats.arrow.FunctionK
import cats.syntax.applicative._

import scala.virtualization.lms.common._
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.examples._

object PCFLang {
  sealed trait Expr
  sealed trait AExpr extends Expr
  sealed trait CExpr extends Expr
  case class Lit(i: Int) extends AExpr
  case class Var(x: String) extends AExpr
  case class Lam(x: String, e: Expr) extends AExpr
  case class App(e1: Expr, e2: Expr) extends CExpr
  case class Let(x: String, rhs: Expr, body: Expr) extends CExpr
  case class Rec(x: String, rhs: Expr, body: Expr) extends CExpr
  case class If0(e1: Expr, e2: Expr, e3: Expr) extends CExpr
  case class Aop(op: Symbol, e1: Expr, e2: Expr) extends CExpr

  object Values {
    sealed trait Value
    case class IntV(i: Int) extends Value
    case class CloV[Env](lam: Lam, e: Env) extends Value
  }

  val p1 = Let("x", Lit(1),
               Let("y", Lit(2),
                   Let("f", Lam("z", Aop('+, Var("z"), Var("y"))),
                       App(Var("f"), Var("x")))))

  val fact = Lam("n",
                 If0(Var("n"),
                     Lit(1),
                     Aop('*, Var("n"), App(Var("fact"), Aop('-, Var("n"), Lit(1))))))
  val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))
}

object Misc {
  type Reader[A, B] = Kleisli[Id, A, B]
  object Reader {
    def apply[A, B](f: A => B): Reader[A, B] = Kleisli[Id, A, B](f)
  }
}

object EnvInterpreter {
  /* An environment interpreter using Reader Monad */
  import PCFLang._
  import PCFLang.Values._
  type Ident = String
  type Env = Map[Ident, Value]

  type ReaderT[F[_], A, B] = Kleisli[F, A, B]
  val ReaderT = Kleisli
  import ReaderT._

  type Ans = ReaderT[Id, Env, Value]

  def ap_clo(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => eval(e).local[Env](_ => ρ + (x → arg))
  }

  def branch0(test: Value, thn: Expr, els: Expr): Ans =
    if (test == IntV(0)) eval(thn) else eval(els)

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def eval(e: Expr): Ans = e match {
    case Lit(i) => pure(IntV(i))
    case Var(x) => for {
      ρ <- ask[Id, Env]
    } yield ρ(x)
    case Lam(x, e) => for {
      ρ <- ask[Id, Env]
    } yield CloV(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      ρ <- ask[Id, Env]
      rt <- eval(e).local[Env](_ => ρ + (x → v))
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, e2, e3)
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => ???
  }
}

object EnvStoreInterpreter {
  /* An environment-and-store interpreter using Reader Monad and State Monad */
  import PCFLang._
  import PCFLang.Values._

  type Ident = String
  type Addr = Int
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type ReaderT[F[_], A, B] = Kleisli[F, A, B]
  val ReaderT = Kleisli

  type StateT[T] = IndexedStateT[Id, Store, Store, T]
  val StateT = IndexedStateT

  type AnsT[T] = ReaderT[StateT, Env, T]
  type Ans = AnsT[Value]

  def alloc(x: String): AnsT[Addr] = for { σ <- get } yield σ.size + 1

  def ap_clo(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      _ <- update_store(α → arg)
      ρ <- ext_env(x → α)
      rt <- local_env(e, ρ)
    } yield rt
  }

  def ext_env(xv: (String, Addr)): AnsT[Env] = for { ρ <- ask } yield ρ + xv

  def update_store(av: (Addr, Value)): AnsT[Unit] = for {
    _ <- ReaderT.lift(StateT.modify[Id, Store, Store](σ => σ + av))
  } yield ()

  def ask: AnsT[Env] = ReaderT.ask[StateT, Env]

  def get: AnsT[Store] = ReaderT.lift(StateT.get[Id, Store])

  def branch0(test: Value, thn: Expr, els: Expr): Ans =
    if (test == IntV(0)) eval(thn) else eval(els)

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def local_env(e: Expr, ρ: Env): Ans = for {
    rt <- eval(e).local[Env](_ => ρ)
  } yield rt

  def eval(e: Expr): Ans = e match {
    case Lit(i) => ReaderT.pure(IntV(i))
    case Var(x) => for {
      ρ <- ask
      σ <- get
    } yield σ(ρ(x))
    case Lam(x, e) => for {
      ρ <- ask
    } yield CloV(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      α <- alloc(x)
      _ <- update_store(α → v)
      ρ <- ext_env(x → α)
      rt <- local_env(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, e2, e3)
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x → α)
      v <- local_env(rhs, ρ)
      _ <- update_store(α → v)
      rt <- local_env(e, ρ)
    } yield rt
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  def run(e: Expr): (Store, Value) = eval(e).run(ρ0).runF(σ0)
}

object StagedEnvStoreInterpreter {}
object AbsEnvStoreInterpreter {

}

object Main {
  def main(args: Array[String]): Unit = {
    //println("Hello")
    //println(DSL.result1)
    import PCFLang._
    //import EnvInterpreter._
    //println(eval(p1).run(Map()))
    //import EnvStoreInterpreter._
    //println(run(fact5))
    SPower.test
  }
}
