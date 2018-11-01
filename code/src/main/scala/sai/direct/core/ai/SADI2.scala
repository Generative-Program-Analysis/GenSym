package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.common.ai._
import sai.common.ai.Lattices._
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

object OhMyNewAI {

  trait EnvTrait[R[_],K,V,E<:EnvTrait[R,K,V,E]] {
    def apply(k: R[K]): R[V]
    def +(kv: (R[K], R[V])): E
  }

  trait StoreTrait[R[_], K, V, S <: StoreTrait[R,K,V,S]] {
    def apply(k: R[K]): R[V]
    def getOrElse(k: R[K], dft: R[V]): R[V]
    def +(kv: (R[K], R[V])): S
    def update(kv: (R[K], R[V])): S
    def update(k: R[K], v: R[V]): S
    def contains(k: R[K]): R[Boolean]
    def ⊔(that: S): S
  }

  trait Sem[R[_]] {
    type Addr
    type Ident
    //type Contour
    type AbsValue
    type E <: EnvTrait[R,Ident,Addr,E]
    type S <: StoreTrait[R,Addr,AbsValue,S]
    type Ans
    def alloc(x: Ident, σ: S): Addr
    type EvalFun = (Expr, E, S) ⇒ Ans
    def fix(ev: EvalFun => EvalFun)(e: Expr, ρ: E, σ: S): Ans = ev(fix(ev))(e, ρ, σ)
    def aeval(ev: EvalFun)(e: Expr, ρ: E, σ: S): Ans
    def aeval_top(e: Expr): Ans
  }

  case class NoRepConSem() extends Sem[NoRep] {
    type Ident = String
    type Addr = Int
    case class Env(map: Map[Ident, Addr]) extends EnvTrait[NoRep,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    type E = Env
    val ρ0: E = Env(Map[Ident,Addr]())
    case class CloV(λ: Lam, ρ: E)
    type AbsValue = CloV
    case class Store(map: Map[Addr, AbsValue]) extends StoreTrait[NoRep,Addr,AbsValue,Store] {
      def apply(k: Addr): AbsValue = map(k)
      def update(k: Addr, v: AbsValue): Store = Store(map + (k → v))
      def getOrElse(k: Addr, dft: AbsValue): AbsValue = map.getOrElse(k, dft)
      def +(kv: (Addr, AbsValue)): Store = update(kv._1, kv._2)
      def update(kv: (Addr, AbsValue)): Store = update(kv._1, kv._2)
      def contains(k: Addr): Boolean = map.contains(k)
      def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
    }
    type S = Store
    val σ0: S = Store(Map[Addr,AbsValue]())
    type Ans = (AbsValue, S)
    def alloc(x: Ident, σ: S): Addr = σ.map.size + 1
    def aeval(ev: EvalFun)(e: Expr, ρ: E, σ: S): Ans = e match {
      case Var(x) => (σ(ρ(x)), σ)
      case Lam(x, e) => (CloV(Lam(x,e), ρ), σ)
      case App(e1, e2) =>
        val (CloV(Lam(x,e), λρ), e1σ) = ev(e1, ρ, σ)
        val (e2v, e2σ) = ev(e2, ρ, e1σ)
        val α = alloc(x, e2σ)
        val ρ_* = λρ + (x → α)
        val σ_* = e2σ + (α → e2v)
        ev(e, ρ_*, σ_*)
    }
    def aeval_top(e: Expr): Ans = fix(aeval)(e, ρ0, σ0)
  }

  trait Staging extends DslExp
      with MapOpsExp
      with SetOpsExp
      with ListOpsExp
      with TupleOpsExp
      with TupledFunctionsRecursiveExp {

    trait RepConSem extends Sem[Rep] {
      type Ident = String
      type Addr = Int
      case class Env(map: Rep[Map[Ident, Addr]]) extends EnvTrait[Rep,Ident,Addr,Env] {
        def apply(k: Rep[Ident]): Rep[Addr] = map(k)
        def +(kv: (Rep[Ident], Rep[Addr])): Env = Env(map + (kv._1, kv._2))
      }
      type E = Env
      val ρ0: E = Env(Map[Ident,Addr]())
      case class CloV(λ: Lam, ρ: E)
      type AbsValue = CloV
      case class Store(map: Rep[Map[Addr, AbsValue]]) extends StoreTrait[Rep,Addr,AbsValue,Store] {
        def apply(k: Rep[Addr]): Rep[AbsValue] = map(k)
        def update(k: Rep[Addr], v: Rep[AbsValue]): Store = Store(map + (k → v))
        def getOrElse(k: Rep[Addr], dft: Rep[AbsValue]): Rep[AbsValue] = map.getOrElse(k, dft)
        def +(kv: (Rep[Addr], Rep[AbsValue])): Store = update(kv._1, kv._2)
        def update(kv: (Rep[Addr], Rep[AbsValue])): Store = update(kv._1, kv._2)
        def contains(k: Rep[Addr]): Rep[Boolean] = map.contains(k)
        def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
      }
    }

  }

  case class NoRepAbsSem() extends Sem[NoRep] {
    type Ident = String
    //type Contour = List[Expr]
    case class Addr(x: Ident)
    case class Env(map: Map[Ident, Addr]) extends EnvTrait[NoRep,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    type E = Env
    val ρ0: E = Env(Map[Ident,Addr]())
    case class CloV(λ: Lam, ρ: E)
    type AbsValue = Set[CloV]
    case class Store(map: Map[Addr, AbsValue]) extends StoreTrait[NoRep,Addr,AbsValue,Store] {
      def apply(k: Addr): AbsValue = map(k)
      def update(k: Addr, v: AbsValue): Store = {
        val oldv: AbsValue = map.getOrElse(k, v.bot)
        Store(map + (k → (oldv ⊔ v)))
      }
      def getOrElse(k: Addr, dft: AbsValue): AbsValue = map.getOrElse(k, dft)
      def +(kv: (Addr, AbsValue)): Store = update(kv._1, kv._2)
      def update(kv: (Addr, AbsValue)): Store = update(kv._1, kv._2)
      def contains(k: Addr): Boolean = map.contains(k)
      def ⊔(that: Store): Store = Store(this.map ⊔ that.map)
    }
    type S = Store
    val σ0: S = Store(Map[Addr,AbsValue]())

    type Ans = (AbsValue, S)
    def alloc(x: Ident, σ: S): Addr = Addr(x)
    def aeval(ev: EvalFun)(e: Expr, ρ: E, σ: S): Ans = e match {
      case Var(x) ⇒ (σ(ρ(x)), σ)
      case Lam(x, e) ⇒ (Set[CloV](CloV(Lam(x,e), ρ)), σ)
      case App(e1, e2) ⇒
        val (e1vs, e1σ) = ev(e1, ρ, σ)
        val (e2vs, e2σ) = ev(e2, ρ, e1σ)
        val result = for (CloV(Lam(x,e), λρ) <- e1vs; e2v <- e2vs) yield {
          val α = alloc(x, e2σ)
          val ρ_* = λρ + (x → α)
          val σ_* = e2σ + (α → Set(e2v))
          ev(e, ρ_*, σ_*)
        }
        result.reduceLeft[Ans] { case ((v1,s1), (v2,s2)) => (v1++v2, s1⊔s2) }
    }

    def aeval_top(e: Expr): Ans = fix(aeval)(e, ρ0, σ0)
  }

  def main(args: Array[String]) {
    val omega = App(Lam("x", App(Var("x"), Var("x"))), Lam("x", App(Var("x"), Var("x"))))
    val ci1 = NoRepConSem()
    val ai1 = NoRepAbsSem()
    // ci1.aeval_top(omega) /* Stack overflow */
    // ai1.aeval_top(omega) /* Stack overflow */
  }
}
