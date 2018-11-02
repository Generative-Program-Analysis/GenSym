package sai.direct.core.ai

import scala.util.continuations._
//import scala.language.implicitConversions
import scala.language.higherKinds

import sai.common.ai._
import sai.common.ai.Lattices._
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

object Semantics {
  type NR[T] = T

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
    type Value
    type Env     // <: EnvTrait[R,Ident,Addr,Env] // FIXME: this cause an error, incompatible type
    type Store  // <: StoreTrait[R,Addr,Value,Store] FIXME: incompatible type
    type Ans
    def alloc(x: Ident, σ: Store): R[Addr]
    type EvalFun = (Expr, Env, Store) ⇒ Ans
    def fix(ev: EvalFun => EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = ev(fix(ev))(e, ρ, σ)
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans
    def eval_top(e: Expr): Ans
  }
}

import Semantics._

package concreteinterp {
  trait Concrete[R[_]] extends Sem[R] {
    type Ident = String
    type Addr = Int
    case class CloV(λ: Lam, ρ: Env)
    type Value = CloV
    type Ans = (Value, Store)
  }

  object NRConSem extends Concrete[NR] {
    case class Env(map: Map[Ident, Addr]) extends EnvTrait[NR,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    val ρ0: Env = Env(Map[Ident,Addr]())
    case class Store(map: Map[Addr, Value]) extends StoreTrait[NR,Addr,Value,Store] {
      def apply(k: Addr): Value = map(k)
      def update(k: Addr, v: Value): Store = Store(map + (k → v))
      def getOrElse(k: Addr, dft: Value): Value = map.getOrElse(k, dft)
      def +(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def update(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def contains(k: Addr): Boolean = map.contains(k)
      def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
    }
    val σ0: Store = Store(Map[Addr,Value]())

    def alloc(x: Ident, σ: Store): Addr = σ.map.size + 1
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
      case Var(x) => (σ(ρ(x)), σ)
      case Lam(x, e) => (CloV(Lam(x,e), ρ), σ)
      case App(e1, e2) =>
        val (CloV(Lam(x,e), λρ), e1σ) = ev(e1, ρ, σ)
        val (e2v, e2σ) = ev(e2, ρ, e1σ)
        val α = alloc(x, e2σ)
        ev(e, λρ + (x → α), e2σ + (α → e2v))
    }
    def eval_top(e: Expr): Ans = fix(eval)(e, ρ0, σ0)
  }

  trait RepConcrete extends Dsl {
    trait RepConSem extends Concrete[Rep] with DslExp with MapOpsExp with SetOpsExp with ListOpsExp with TupleOpsExp with TupledFunctionsRecursiveExp {
      case class Env(map: Rep[Map[Ident, Addr]]) extends EnvTrait[Rep,Ident,Addr,Env] {
        def apply(k: Rep[Ident]): Rep[Addr] = map(k)
        def +(kv: (Rep[Ident], Rep[Addr])): Env = Env(map + (kv._1, kv._2))
      }
      val ρ0: Env = Env(Map[Ident,Addr]())

      implicit def AbsValueTyp: Typ[Value] = manifestTyp

      case class Store(map: Rep[Map[Addr, Value]]) extends StoreTrait[Rep,Addr,Value,Store] {
        def apply(k: Rep[Addr]): Rep[Value] = map(k)
        def update(k: Rep[Addr], v: Rep[Value]): Store = Store(map + (k → v))
        def getOrElse(k: Rep[Addr], dft: Rep[Value]): Rep[Value] = map.getOrElse(k, dft)
        def +(kv: (Rep[Addr], Rep[Value])): Store = update(kv._1, kv._2)
        def update(kv: (Rep[Addr], Rep[Value])): Store = update(kv._1, kv._2)
        def contains(k: Rep[Addr]): Rep[Boolean] = map.contains(k)
        def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
      }

      val σ0: Store = Store(Map[Addr, Value]())
      //def alloc(x: Ident, σ: Store): Rep[Addr] = σ.map.size + 1
    }
  }

}

package abstractinterp {
  trait Abstract[R[_]] extends Sem[R] {
    type Ident = String
    case class Addr(x: Ident)
    //type Contour = List[Expr]
    case class CloV(λ: Lam, ρ: Env)
    type Value = Set[CloV]
    type Ans = (Value, Store)
  }

  object NRAbsSem extends Abstract[NR] {
    case class Env(map: Map[Ident, Addr]) extends EnvTrait[NR,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    case class Store(map: Map[Addr, Value]) extends StoreTrait[NR,Addr,Value,Store] {
      def apply(k: Addr): Value = map(k)
      def update(k: Addr, v: Value): Store = {
        val oldv: Value = map.getOrElse(k, v.bot)
        Store(map + (k → (oldv ⊔ v)))
      }
      def getOrElse(k: Addr, dft: Value): Value = map.getOrElse(k, dft)
      def +(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def update(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def contains(k: Addr): Boolean = map.contains(k)
      def ⊔(that: Store): Store = Store(this.map ⊔ that.map)
    }

    def alloc(x: Ident, σ: Store): Addr = Addr(x)
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
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

    val ρ0: Env = Env(Map[Ident,Addr]())
    val σ0: Store = Store(Map[Addr,Value]())
    def eval_top(e: Expr): Ans = fix(eval)(e, ρ0, σ0)
  }

}

object SADI2 {
  import concreteinterp._
  //import abstractinterp._
  def main(args: Array[String]) {
    val omega = App(Lam("x", App(Var("x"), Var("x"))), Lam("x", App(Var("x"), Var("x"))))
    //val ai1 = NRAbsSem()
    // NRConSem.eval_top(omega) /* Stack overflow */
    // ai1.eval_top(omega) /* Stack overflow */
  }

}
