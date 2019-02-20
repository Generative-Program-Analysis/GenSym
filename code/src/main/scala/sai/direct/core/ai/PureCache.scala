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

object PureCache {

  trait LMSOps extends Dsl with MapOps with UncheckedOps with TupleOps with SetOps with TupledFunctions

  trait RepAbsLattices extends LMSOps {
    trait RepLattice[A] extends GenericLattice[A, Rep]
    object RepLattice {
      def apply[L](implicit l: RepLattice[L]): RepLattice[L] = l
    }
    implicit class RepLatticeOps[L: RepLattice](l: Rep[L]) {
      lazy val bot: Rep[L] = RepLattice[L].bot
      lazy val top: Rep[L] = RepLattice[L].top
      def ⊑(that: Rep[L]): Rep[Boolean] = RepLattice[L].⊑(l, that)
      def ⊔(that: Rep[L]): Rep[L] = RepLattice[L].⊔(l, that)
      def ⊓(that: Rep[L]): Rep[L] = RepLattice[L].⊓(l, that)
    }
    implicit def RepSetLattice[T:Typ]: RepLattice[Set[T]] = new RepLattice[Set[T]] {
      lazy val bot: Rep[Set[T]] = Set[T]()
      lazy val top: Rep[Set[T]] = throw new RuntimeException("No representation of top power set")
      def ⊑(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Boolean] = l1 subsetOf l2
      def ⊔(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Set[T]] = l1 union l2
      def ⊓(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Set[T]] = l1 intersect l2
    }
    implicit def RepMapLattice[K:Typ, V:Typ:RepLattice]: RepLattice[Map[K, V]] = new RepLattice[Map[K, V]] {
      lazy val bot: Rep[Map[K, V]] = {
        Map.empty[K, V]
      }
      lazy val top: Rep[Map[K, V]] = throw new RuntimeException("No representation of top map")
      def ⊑(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Boolean] = {
        m1.foreach { case (k,v) => if (!(v ⊑ m2.getOrElse(k, v.bot))) return false }
        true
      }
      def ⊔(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Map[K, V]] =
        m2.foldLeft (m1) { case (m, (k, v)) ⇒ m + ((k, m.getOrElse(k, v.bot) ⊔ v)) }
      def ⊓(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Map[K, V]] =
        (m1.keySet intersect m2.keySet).foldLeft (Map[K, V]())
      { case (m_*, k) ⇒ m_* + ((k, m1(k) ⊓ m2(k))) }
    }
    implicit def RepProductLattice[A:Typ:RepLattice, B:Typ:RepLattice]: RepLattice[(A, B)] = new RepLattice[(A, B)] {
      lazy val bot: Rep[(A, B)] = (RepLattice[A].bot, RepLattice[B].bot)
      lazy val top: Rep[(A, B)] = (RepLattice[A].top, RepLattice[B].top)
      def ⊑(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[Boolean] = RepLattice[A].⊑(l1._1, l2._1) && RepLattice[B].⊑(l1._2, l2._2)
      def ⊔(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[(A, B)] = (RepLattice[A].⊔(l1._1, l2._1), RepLattice[B].⊔(l1._2, l2._2))
      def ⊓(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[(A, B)] = (RepLattice[A].⊓(l1._1, l2._1), RepLattice[B].⊓(l1._2, l2._2))
    }
  }

  trait PureCacheAbsInterp extends RepAbsLattices {
    type R[+T] = Rep[T]
    type Ident = String

    case class Addr(x: Ident)
    sealed trait AbsValue
    case class CloV(λ: Lam, ρ: Env) extends AbsValue
    case class NumV() extends AbsValue
    type Value = Set[AbsValue]
    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]

    implicit def exprTyp: Typ[Expr]
    implicit def absValueTyp: Typ[AbsValue]
    implicit def addrTyp: Typ[Addr]

    type Config = (Expr, Env, Store)
    type Cache = Map[Config, (Value, Store)]
    //type Ans = (R[Value], R[Store], R[Cache], R[Cache])

    case class Ans(vs: Rep[Value], store: Rep[Store], in: Rep[Cache], out: Rep[Cache]) {
      def ⊔(that: Ans): Ans = {
        Ans(RepLattice[Value].⊔(vs, that.vs),
            RepLattice[Store].⊔(store, that.store),
            RepLattice[Map[Config, (Value,Store)]].⊔(in, that.in),
            RepLattice[Map[Config, (Value,Store)]].⊔(out, that.out))
      }
    }

    val ρ0: Rep[Env] = Map[Ident, Addr]()
    val σ0: Rep[Store] = Map[Addr, Value]()

    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ.getOrElse(a, RepLattice[Value].bot)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = {
      val oldv = get(σ, a); σ + (a → RepLattice[Value].⊔(v, oldv))
    }
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = unchecked[Addr]("Addr(\"", x, "\")")
  
    /*
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(x, e) = λ
      val f: Rep[(Value, Store)]=>Rep[(Value,Store)] = (as: Rep[(Value,Store)]) => {
        val args = as._1; val σ = as._2
        val α = alloc(σ, x)
        ev(e, put(ρ, x, α), put(σ, α, args))
      }
      unchecked[Value]("Set[AbsValue](CompiledClo(", fun(f), ",", λ, ",", ρ, "))")
    }
    */

    def num(i: Lit): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")
    def branch0(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val thnans = thn
      val elsans = els
      thnans ⊔ elsans
    }
    def prim_eval(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")


    /*
     def eval(e: Expr, ρ: Rep[Env], σ: Rep[Store], in: Rep[Cache], out: Rep[Cache]): Ans = {
     val cfg: Rep[Config] = (unit(e), ρ, σ)
     (if (out.contains(cfg)) {
     val vs = out.get(cfg) //FIXME: out(cfg) doesn't work
     (vs._1, vs._2, in, out)
     }
     else {
     val ans0 = in.getOrElse(cfg, RepLattice[(Value, Store)].bot)
     val nout = out + (cfg -> ans0)
     e match {
     case Lit(i) =>
     val ans1 = (num(e.asInstanceOf[Lit]), σ)
     (ans1._1, ans1._2, in, nout + (cfg -> RepLattice[(Value,Store)].⊔(ans0, ans1)))
     }
     }).asInstanceOf[Rep[(Value, Store, Cache, Cache)]]
     }
     val cache0: Rep[Cache] = Map[Config, (Value, Store)]()
     */
    //override def eval_top(e: Expr, ρ: Rep[Env], σ: Rep[Store]): Ans = eval(e, ρ, σ, cache0, cache0)
  }
}
