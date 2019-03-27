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

object Eval {
  trait Base[R[+_]] {
    type Addr = Int
    trait EnvSpec[E <: EnvSpec[E]] {
      def apply(k: String): Addr
      def +(kv: (String,Addr)): E
    }
    type Env <: EnvSpec[Env]
    type Store
    sealed trait Value
    case class CloV(λ: Lam, ρ: Env) extends Value
    case class NumV(i: Int) extends Value
    case class CompCloV(c: R[Value]) extends Value

    case class Ans(v: R[Value], σ: Store)
  }

  trait Semantics[R[+_]] extends Base[R] {
    type Store = Map[Addr, Value]
    def lift(v: Value): R[Value]
    def lift_clo(c: CloV): R[Value]
    def alloc(x: String, σ: Store): Addr
    def base_apply(f: R[Value], arg: R[Value], ρ: Env, σ: Store): Ans
    def inRep: Boolean
    def evalArith(op: Symbol, e1v: R[NumV], e2v: R[NumV]): R[NumV]
    def is_zero(n: R[Value]): R[Boolean]
    type T[A]
    def valueT: T[Value]
    def if_then_else(cnd: R[Boolean], thn: => Ans, els: => Ans): Ans
  }

  type NoRep[+A] = A
  object NoRepSem extends Semantics[NoRep] {
    case class Env(map: Map[String, Addr]) extends EnvSpec[Env] {
      def apply(k: String): Addr = map(k)
      def +(kv: (String,Addr)): Env = Env(map + (kv._1 -> kv._2))
    }
    def inRep = false
    def lift(v: Value) = v
    def lift_clo(c: CloV) = c
    def alloc(x: String, σ: Store): Addr = σ.size + 1
    def base_apply(f: Value, arg: Value, ρ: Env, σ: Store): Ans = {
      val α = alloc("", σ)
      base_apply_norep(f, arg, α, ρ, σ)
    }
    def evalArith(op: Symbol, e1v: NumV, e2v: NumV): NumV = op match {
      case '+ ⇒ NumV(e1v.i + e2v.i)
      case '- ⇒ NumV(e1v.i - e2v.i)
      case '* ⇒ NumV(e1v.i * e2v.i)
    }
    def is_zero(n: Value): Boolean = n match {
      case NumV(i) => i == 0
    }
    type T[A] = Unit
    def valueT = ()
    def if_then_else(cnd: Boolean, thn: => Ans, els: => Ans): Ans =
      if (cnd) thn else els
    def base_apply_norep(f: Value, arg: Value, α: Addr, ρ: Env, σ: Store): Ans = f match {
      case CloV(Lam(x, e), ρ) =>
        base_eval(e, ρ + (x → α), σ + (α → arg))
    }

    def base_eval(e: Expr, ρ: Env, σ: Store): Ans = {
      e match {
        case Var(x) => Ans(lift(σ(ρ(x))), σ)
        case Lit(i) => Ans(lift(NumV(i)), σ)
        case Lam(x, e) => Ans(lift_clo(CloV(Lam(x, e), ρ)), σ)
        case App(e1, e2) =>
          val Ans(e1v, e1σ) = base_eval(e1, ρ, σ)
          val Ans(e2v, e2σ) = base_eval(e2, ρ, e1σ)
          base_apply(e1v, e2v, ρ, e2σ)
        case Rec(x, f, body) =>
          val α = alloc(x, σ)
          val ρ_* = ρ + (x → α)
          val Ans(fv, fσ) = base_eval(f, ρ_*, σ)
          val σ_* = fσ + (α → fv)
          base_eval(body, ρ_*, σ_*)
        case Let(x, e, body) => base_eval(App(Lam(x, body), e), ρ, σ)
        case If0(cnd, thn, els) =>
          val Ans(cndv: NumV, cndσ) = base_eval(cnd, ρ, σ)
          if_then_else(is_zero(cndv),
                       base_eval(thn, ρ, cndσ),
                       base_eval(els, ρ, cndσ))
        case AOp(op, e1, e2) =>
          val Ans(e1v: NumV, e1σ) = base_eval(e1, ρ, σ)
          val Ans(e2v: NumV, e2σ) = base_eval(e2, ρ, e1σ)
          Ans(evalArith(op, e1v, e2v), e2σ)
      }
    }
  }
  
  /*
  object RepSem extends Semantics[Rep] with Dsl {
    case Env(map: Rep[
  }
  */

  

}
