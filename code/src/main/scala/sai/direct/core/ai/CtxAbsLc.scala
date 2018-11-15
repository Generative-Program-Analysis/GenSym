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

object CtxAnalysis {
  trait Semantics {
    type R[+_]
    type Ident = String
    type Addr
    type Value
    type Env
    type Store
    type Time
    type VST = (Value, Store, Time)
    type Ans = R[Set[VST]]
    def tick(e: Expr, τ: R[Time]): R[Time]
    def ans(ans: (R[Value], R[Store], R[Time])): Ans //TODO
    def get(ρ: R[Env], x: Ident): R[Addr]
    def put(ρ: R[Env], x: Ident, a: R[Addr]): R[Env]
    def get(σ: R[Store], a: R[Addr]): R[Value]
    def put(σ: R[Store], a: R[Addr], v: R[Value]): R[Store]
    def alloc(σ: R[Store], x: Ident, τ: R[Time]): R[Addr]
    def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
    def num(i: Int): R[Value]
    def apply_closure(ev: EvalFun)(f: R[Value], arg: R[Value], σ: R[Store], τ: R[Time]): Ans @cps[Ans]
    def branch0(cnd: R[Value], thn: => Ans, els: => Ans): Ans
    def prim_eval(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
    val ρ0: R[Env]
    val σ0: R[Store]
    val τ0: R[Time]
    type EvalFun = (Expr, R[Env], R[Store], R[Time]) => Ans @cps[Ans]
    def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ, τ) => ev(fix(ev))(e, ρ, σ, τ)
    def choices[T](ts: R[Set[T]]): R[T] @cps[Ans]
    def getv(vst: R[(Value,Store,Time)]): R[Value]
    def getσ(vst: R[(Value,Store,Time)]): R[Store]
    def getτ(vst: R[(Value,Store,Time)]): R[Time]
    def eval(ev: EvalFun)(e: Expr, ρ: R[Env], σ: R[Store], τ: R[Time]): Ans @cps[Ans] = {
      val τ_* = tick(e, τ)
      e match {
        case Lit(i) => ans((num(i), σ, τ_*))
        case Var(x) => ans((get(σ, get(ρ, x)), σ, τ_*))
        case Lam(x, e) => ans((close(ev)(Lam(x, e), ρ), σ, τ_*))
        case App(e1, e2) =>
          val e1ans = ev(e1, ρ, σ, τ_*)
          val e1vst = choices[(Value,Store,Time)](e1ans)
          val e2ans = ev(e2, ρ, getσ(e1vst), getτ(e1vst))
          val e2vst = choices[(Value,Store,Time)](e2ans)
          apply_closure(ev)(getv(e1vst), getv(e2vst), getσ(e2vst), getτ(e2vst))
      }
    }
    def eval_top(e: Expr, ρ: R[Env], σ: R[Store], τ: R[Time]): Ans
    def eval_top(e: Expr): Ans = eval_top(e, ρ0, σ0, τ0)
  }

  trait Abstract extends Semantics {
    type Addr = (Ident, Time)
    type Time = List[Expr]
    sealed trait AbsValue
    case class CloV(λ: Lam, ρ: Env) extends AbsValue
    case class NumV() extends AbsValue
    type Value = Set[AbsValue]
    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]
  }

  object AbsInterp extends Abstract {
    type R[+T] = T
    val ρ0 = Map[Ident, Addr]()
    val σ0 = Map[Addr, Value]()
    val τ0 = List()
    val k: Int = 0
    def get(ρ: Env, x: Ident): Addr = ρ(x)
    def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
    def get(σ: Store, a: Addr): Value = σ.getOrElse(a, Lattice[Value].bot)
    def put(σ: Store, a: Addr, v: Value): Store = σ + (a → (v ⊔ get(σ, a)))
    def tick(e: Expr, τ: Time) = (e :: τ).take(k)
    def ans(vst: VST) = Set(vst)
    def alloc(σ: Store, x: Ident, τ: Time): Addr = (x, τ)
    def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
    def num(i: Int): Value = Set(NumV())
    def apply_closure(ev: EvalFun)(f: Value, arg: Value, σ: Store, τ: Time): Ans @cps[Ans] = {
      val CloV(Lam(x, e), ρ) = choices[AbsValue](f)
      val α = alloc(σ, x, τ)
      val ρ_* = put(ρ, x, α)
      val σ_* = put(σ, α, arg)
      ev(e, ρ_*, σ_*, τ)
    }
    def nd[T](ts: Set[T], acc: Ans, k: T => Ans): Ans = {
      if (ts.isEmpty) acc
      else nd(ts.tail, acc ⊔ k(ts.head), k)
    }
    def choices[T](ts: Set[T]): T @cps[Ans] = shift {
      f: (T => Ans) => nd(ts, Set[VST](), f)
    }
    def branch0(cnd: Value, thn: => Ans, els: => Ans): Ans = thn ⊔ els
    def prim_eval(op: Symbol, v1: Value, v2: Value): Value = Set(NumV())
    def getv(vst: (Value,Store,Time)): Value = vst._1
    def getσ(vst: (Value,Store,Time)): Store = vst._2
    def getτ(vst: (Value,Store,Time)): Time = vst._3
    type Config = (Expr, Env, Store, Time)

    case class CacheFix(evev: EvalFun => EvalFun) {
      var in = Map[Config, Ans](); var out = Map[Config, Ans]()
      def cached_ev(e: Expr, ρ: Env, σ: Store, τ: Time): Ans @cps[Ans] = {
        val cfg: Config = (e, ρ, σ, τ)
        if (out.contains(cfg)) out(cfg)
        else {
          val ans0: Ans = in.getOrElse(cfg, Lattice[Ans].bot)
          out = out + (cfg -> ans0)
          val ans1: Ans = evev(cached_ev)(e, ρ, σ, τ)
          out = out + (cfg -> Lattice[Ans].⊔(ans0, ans1))
          ans1
        }
      }
      def iter(e: Expr, ρ: Env, σ: Store, τ: Time): Ans = {
        in = out; out = Map[Config, Ans]();
        reset { cached_ev(e, ρ, σ, τ) }
        if (in == out) out((e, ρ, σ, τ)) else iter(e, ρ, σ, τ)
      }
    }
    override def eval_top(e: Expr, ρ: Env, σ: Store, τ: Time): Ans = CacheFix(eval).iter(e, ρ, σ, τ)
  }
}

object KCFATest {
  import CtxAnalysis._
  def main(args: Array[String]) {
    val lam = Lam("x", App(Var("x"), Var("x")))
    // ((λ (x) ((x x) x)) (λ (y) y))
    val id4 = App(Lam("x", App(App(Var("x"), Var("x")), Var("x"))), Lam("y", Var("y")))
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))
    val omega = App(lam, lam)
    println(AbsInterp.eval_top(omega))
    println(AbsInterp.eval_top(fact5))
  }
}
