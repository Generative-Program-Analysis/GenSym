package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions

import sai.common.ai._
import sai.common.ai.Lattice._
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

// A: a dummy class for staging

case class A(i: Int)

trait AOps extends Base with Variables {
  implicit def repToAOps(a: Rep[A]) = new AOpsCls(a)
  class AOpsCls(a: Rep[A]) {
    def i = a_i(a)
  }
  def a_i(a: Rep[A])(implicit pos: SourceContext): Rep[Int]
}
trait AOpsExp extends BaseExp with AOps with VariablesExp {
  implicit def ATyp: Typ[A] = manifestTyp
  case class AI(a: Exp[A]) extends Def[Int]
  def a_i(a: Exp[A])(implicit pos: SourceContext): Exp[Int] = AI(a)
}

//////////////////////////////////////////////////////////////////////

/*
trait BAddrOps extends Base with Variables {
  implicit def repToAOps(α: Rep[BAddr]) = new BAddrOpsCls(α)
  class BAddrOpsCls(α: Rep[BAddr]) {}
}
trait BAddrOpsExp extends BaseExp with BAddrOps {
}
*/

trait SADI extends DslExp with MapOpsExp with SetOpsExp with ListOpsExp with TupledFunctionsRecursiveExp with AOpsExp with TupleOpsExp {
  import AAM._

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
    lazy val bot: Rep[Map[K, V]] = Map[K, V]()
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

  def lift[T:Typ](x: T) = unit[T](x)
  //implicit def lift[T:Typ](st: Set[T]): Rep[Set[T]] = Set[T](st.map(lift):_*)

  //TODO: adjustable size for staged map?
  //TODO: Staging support for Expr, toString

  case class Store[K: Typ, V: Typ : RepLattice](map: Rep[Map[K, V]]) {
    def apply(k: Rep[K]): Rep[V] = map(k)
    def getOrElse(k: Rep[K], dft: Rep[V]): Rep[V] = map.getOrElse(k, dft)
    def update(k: Rep[K], v: Rep[V]): Store[K, V] = {
      val oldv = map.getOrElse(k, v.bot)
      Store[K,V](map + (k → (oldv ⊔ v)))
    }
    def update(kv: (Rep[K], Rep[V])): Store[K,V] = update(kv._1, kv._2)
    def contains(k: Rep[K]): Rep[Boolean] = map.contains(k)
    def +(kv: (Rep[K], Rep[V])): Store[K,V] = update(kv._1, kv._2)
    def ⊔(that: Store[K,V]): Store[K,V] = Store[K,V](this.map ⊔ that.map)
  }

  type ℙ[A] = Set[A]
  type Env = Rep[Map[Id, BAddr]]
  type BStore = Store[BAddr, ℙ[AbsValue]]
  type Time = List[Expr] //probably unstaged because k and expr are all known at compile-time

  case class Config(e: Expr, ρ: Env, σ: BStore, τ: Time) {
    val k: Int = 0 //TODO: make it Rep[Int]?
    def tick: Time = (e :: τ).take(k)
  }

  // Result from one computation path
  case class VS(vals: Rep[ℙ[AbsValue]], τ: Rep[Time], σ: BStore)

  // Final result that groups multiple multiple VS
  case class Ans(vss: ℙ[VS], cache: Cache) {
    def ++(ans: Ans) = Ans(vss ++ ans.vss, cache ⊔ ans.cache)
  }

  type EnvTyp = Map[Id, BAddr]
  type BStoreTyp = Map[BAddr, Set[AbsValue]]
  type VSTyp = (Set[AbsValue], Time, BStoreTyp)
  type CacheMap = Map[(Expr, Time), Rep[Map[(EnvTyp, BStoreTyp), Set[VSTyp]]]]

  implicit def BAddrTyp: Typ[BAddr] = manifestTyp
  implicit def AbsValueTyp: Typ[AbsValue] = manifestTyp
  implicit def ExprTyp: Typ[Expr] = manifestTyp

  case class Cache(in:  CacheMap, out: CacheMap) {
    def inGet(cfg: Config): Rep[ℙ[VSTyp]] = {
      val m: Rep[Map[(EnvTyp, BStoreTyp), Set[VSTyp]]] = in.getOrElse((cfg.e, cfg.τ), Map[(EnvTyp, BStoreTyp), Set[VSTyp]]())
      val k: Rep[(EnvTyp, BStoreTyp)] = ???
      //m.getOrElse(???, ???)
      ???
    }
    def ⊔(that: Cache): Cache = ???
  }

  //Map[(Expr, Time), Rep[Map[(Env, BStore), (Set[AbsValue])]]]

  /*
  case class Cache(in: Store[Config, ℙ[VS]], out: Store[Config, ℙ[VS]]) {
    def inGet(cfg: Config): ℙ[VS] = in.getOrElse(cfg, ℙ())
    def inContains(cfg: Config): Boolean = in.contains(cfg)
    def outGet(cfg: Config): ℙ[VS] = out.getOrElse(cfg, ℙ())
    def outContains(cfg: Config): Boolean = out.contains(cfg)
    def outUpdate(cfg: Config, vss: ℙ[VS]): Cache = Cache(in, out.update(cfg, vss))
    def outUpdate(cfg: Config, vs: VS): Cache = Cache(in, out.update(cfg, ℙ(vs)))
    def outUpdateFromIn(cfg: Config): Cache = outUpdate(cfg, inGet(cfg))
    def ⊔ (that: Cache): Cache = Cache(in ⊔ that.in, out ⊔ that.out)
  }

  object Cache {
    def cache0 = Cache(Store[Config, ℙ[VS]](Map[Config, ℙ[VS]]()), Store[Config, ℙ[VS]](Map()))
  }

  def nd[T](ts: Iterable[T], acc: Ans, k: ((T, Cache)) ⇒ Ans): Ans = {
    if (ts.isEmpty) acc
    else nd(ts.tail, acc ++ k(ts.head, acc.cache), k)
  }

  def choices[T](ts: Iterable[T], cache: Cache): (T, Cache) @cps[Ans] = shift {
    f: (((T, Cache)) ⇒ Ans) ⇒ nd(ts, Ans(ℙ(), cache), f)
  }

  def aeval(e: Expr, ρ: Env, σ: BStore, τ: Time, cache: Cache): Ans @cps[Ans] = {
    val config = Config(e, ρ, σ, τ)
    if (cache.outContains(config))
      Ans(cache.outGet(config), cache)
    else {
      val τ_* = config.tick
      val cache_* = cache.outUpdateFromIn(config)
      e match {
        case Var(x) ⇒
          val vs = ℙ(VS(σ(ρ(x)), τ_*, σ))
          Ans(vs, cache_*.outUpdate(config, vs))
        case Lam(x, e) ⇒
          val vs = ℙ(VS(ℙ(CloV(Lam(x, e), ρ)), τ_*, σ))
          Ans(vs, cache_*.outUpdate(config, vs))
        case App(e1, e2) ⇒
          val Ans(e1vss, e1cache) = aeval(e1, ρ, σ, τ_*, cache_*)
          val (VS(e1vs, e1τ, e1σ), e1cache_*) = choices[VS](e1vss, e1cache)
          val (CloV(Lam(x, body), λρ), clscache) = choices[AbsValue](e1vs, e1cache_*)
          val Ans(e2vss, e2cache) = aeval(e2, ρ, σ, e1τ, clscache)
          val (VS(e2vs, e2τ, e2σ), e2cache_*) = choices[VS](e2vss, e2cache)
          val α = allocBind(x, e2τ)
          val ρ_* = λρ + (x → α)
          val σ_* = e2σ + (α → e2vs)
          val Ans(ret, retcache) = aeval(body, ρ_*, σ_*, e2τ, e2cache_*)
          Ans(ret, retcache.outUpdate(config, ret))
      }
    }
  }

  def analyze(e: Expr, ρ: Env = ρ0, σ: BStore = bσ0): ℙ[VS] = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, anscache) = reset { aeval(e, ρ, σ, τ0, cache) }
      if (anscache.out == anscache.in) Ans(vss, anscache)
      else iter(Cache(anscache.out, Store[Config, ℙ[VS]](Map())))
    }
    iter(Cache.cache0).vss
  }
  */
}
