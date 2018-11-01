package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

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

trait SADI extends DslExp
    with MapOpsExp
    with SetOpsExp
    with ListOpsExp
    with AOpsExp
    with TupleOpsExp
    with TupledFunctionsRecursiveExp {
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
  type RepEnv = Rep[Env]
  type RepStore = Store[BAddr, ℙ[AbsValue]]
  type Time = List[Expr] //probably unstaged because k and expr are all known at compile-time

  // Result from one computation path
  case class VS(vals: Rep[ℙ[AbsValue]], τ: Rep[Time], σ: RepStore) //TODO: currently not using

  // Final result that groups multiple multiple VS
  type StoreMap = Map[BAddr, ℙ[AbsValue]]
  type VSTyp = (Set[AbsValue], Time, StoreMap) //TODO: Update to VS
  type StcCfg = (Expr, Time)
  type DynCfg = (Env, StoreMap)
  type SubMap = Map[DynCfg, ℙ[VSTyp]]
  //type TwoLevelCacheMap = Map[StcCfg, Rep[SubMap]]
  type TwoLevelCacheMap = Rep[Map[StcCfg, SubMap]]

  type CfgTyp = (Expr, Env, StoreMap, Time)
  type CacheMap = Map[CfgTyp, ℙ[VSTyp]]
  type RepCacheMap = Rep[CacheMap]

  case class Config(e: Expr, ρ: RepEnv, σ: RepStore, τ: Time) {
    val k: Int = 0 //TODO: make it Rep[Int]?
    def tick: Time = (e :: τ).take(k)
    //def stc: (Expr, Time) = (e, τ)
    def stc: Rep[(Expr, Time)] = (lift(e), lift(τ))
    def dyn: Rep[(Env, StoreMap)] = (ρ, σ.map)
    def tpl: Rep[CfgTyp] = (lift(e), ρ, σ.map, lift(τ))
  }

  implicit def BAddrTyp: Typ[BAddr] = manifestTyp
  implicit def AbsValueTyp: Typ[AbsValue] = manifestTyp
  implicit def ExprTyp: Typ[Expr] = manifestTyp

  case class Cache(in: Store[CfgTyp, ℙ[VSTyp]], out: Store[CfgTyp, ℙ[VSTyp]]) extends CacheTrait[Config, VSTyp, Rep, Cache] {
    def inGet(cfg: Config): Rep[ℙ[VSTyp]] = in.getOrElse(cfg.tpl, lift(ℙ[VSTyp]()))
    def inContains(cfg: Config): Rep[Boolean] = in.contains(cfg.tpl)
    def outGet(cfg: Config): Rep[ℙ[VSTyp]] = out.getOrElse(cfg.tpl, lift(ℙ[VSTyp]()))
    def outContains(cfg: Config): Rep[Boolean] = out.contains(cfg.tpl)
    def outUpdate(cfg: Config, vss: Rep[ℙ[VSTyp]]): Cache = Cache(in, out.update(cfg.tpl, vss))
    def outUpdateFromIn(cfg: Config): Cache = outUpdate(cfg, inGet(cfg))
    def ⊔(that: Cache): Cache = Cache(in ⊔ that.in, out ⊔ that.out)
  }

  case class TwoLevelCache(in: TwoLevelCacheMap, out: TwoLevelCacheMap) extends CacheTrait[Config, VSTyp, Rep, TwoLevelCache] {
    private def submap_∅ : Rep[SubMap] = Map[DynCfg, ℙ[VSTyp]]()
    private def get(cache: TwoLevelCacheMap, cfg: Config): Rep[ℙ[VSTyp]] = {
      val m: Rep[SubMap] = cache.getOrElse(cfg.stc, submap_∅)
      m.getOrElse(cfg.dyn, Set[VSTyp]())
    }
    private def contains(cache: TwoLevelCacheMap, cfg: Config): Rep[Boolean] = {
      val m: Rep[SubMap] = cache.getOrElse(cfg.stc, submap_∅)
      m.contains(cfg.dyn)
    }
    private def update(cache: TwoLevelCacheMap, cfg: Config, vss: Rep[ℙ[VSTyp]]): TwoLevelCacheMap = {
      val m: Rep[SubMap] = cache.getOrElse(cfg.stc, submap_∅)
      val oldv: Rep[ℙ[VSTyp]] = m.getOrElse(cfg.dyn, Set[VSTyp]())
      val m_* = m + (cfg.dyn, (vss ++ oldv))
      cache + (cfg.stc, m_*)
    }
    private def join(c1: TwoLevelCacheMap, c2: TwoLevelCacheMap): TwoLevelCacheMap = {
      c2.foldLeft (c1) { case (m, (et, submap2)) ⇒
        val submap1 = m.getOrElse(et, submap_∅)
        val submap_* = submap2.foldLeft (submap1) { case (sm, (k, v)) ⇒
          val oldv = sm.getOrElse(k, Set[VSTyp]())
          sm + (k → (oldv ++ v))
        }
        m + (et → submap_*)
      }
    }
    def inGet(cfg: Config): Rep[ℙ[VSTyp]] = get(in, cfg)
    def inContains(cfg: Config): Rep[Boolean] = contains(in, cfg)
    def outGet(cfg: Config): Rep[ℙ[VSTyp]] = get(out, cfg)
    def outContains(cfg: Config): Rep[Boolean] = contains(out, cfg)
    //FIXME: have to use different name? outUpdate.
    def outUpdate(cfg: Config, vss: Rep[ℙ[VSTyp]]): TwoLevelCache = TwoLevelCache(in, update(out, cfg, vss))
    //def outUpdateSingle(cfg: Config, vs: Rep[VSTyp]): Cache = outUpdate(cfg, Set[VSTyp](vs))
    def outUpdateFromIn(cfg: Config): TwoLevelCache = outUpdate(cfg, inGet(cfg))
    def ⊔(that: TwoLevelCache): TwoLevelCache = TwoLevelCache(join(in, that.in), join(out, that.out))
  }

  object TwoLevelCache {
    //private def cacheMap0: TwoLevelCacheMap = collection.immutable.Map[StcCfg, Rep[SubMap]]()
    private def cacheMap0: TwoLevelCacheMap = Map[StcCfg, SubMap]()
    def cache0 = TwoLevelCache(cacheMap0, cacheMap0)
  }

  /*
  case class Ans(vss: Rep[ℙ[VSTyp]], cache: Cache) {
    def ++(ans: Ans) = Ans(vss ++ ans.vss, cache ⊔ ans.cache)
  }
   */

  type Ans = (ℙ[VSTyp], CacheMap, CacheMap)

  def ++(ans1: Rep[Ans], ans2: Rep[Ans]): Rep[Ans] = {
    (ans1._1++ans2._1, ans1._2++ans2._2, ans1._3++ans2._3)
  }

  def nd[T](implicit ev: Typ[T], k: ((Rep[T], RepCacheMap, RepCacheMap)) => Rep[Ans]): Rep[((ℙ[T], Ans)) => Ans] =
    fun { (ts, acc) =>
      if (ts.isEmpty) acc
      else nd[T](ev, k)(ts.tail, ++(acc, k(ts.head, acc._2, acc._3)))
    }

  def choices[T](implicit ev: Typ[T], ts: Rep[Set[T]], cache: Cache): (Rep[T], RepCacheMap, RepCacheMap) @cps[Rep[Ans]] = shift {
    f: (((Rep[T], RepCacheMap, RepCacheMap)) ⇒ Rep[Ans]) ⇒
      nd(ev, f)(ts, (lift(ℙ[VSTyp]()), cache.in.map, cache.out.map))
  }

  //def aeval(e: Expr, ρ: RepEnv, σ: RepStore, τ: Time): Rep[ℙ[ℙ[AbsValue]]] @cps[Rep[ℙ[ℙ[AbsValue]]]] = {
  def aeval(e: Expr, ρ: RepEnv, σ: RepStore, τ: Time): Rep[ℙ[AbsValue]] @cps[Rep[ℙ[AbsValue]]] = {
    val config = Config(e, ρ, σ, τ)
    val τ_* = config.tick
    e match {
      case Var(x) ⇒
        val vs: Rep[ℙ[AbsValue]] = σ(ρ(x))
        vs
      case Lam(x, e) ⇒ ???
        //ℙ[AbsValue](CloV(Lam(x,e), ρ))
          //ℙ[VSTyp]( (ℙ[AbsValue](CloV(Lam(x,e), ρ)), τ_*, σ.map) )
          //Ans(ℙ(VS(ℙ(CloV(Lam(x, e), ρ)), τ_*, σ)), cache)
      case App(e1, e2) ⇒ ???
          /*
           val Ans(e1vss, e1cache) = aeval_top(e1, ρ, σ, τ_*, cache)
           val (VS(e1vs, e1τ, e1σ), e1cache_*) = choices[VS](e1vss, e1cache)
           val (CloV(Lam(x, body), λρ), clscache) = choices[AbsValue](e1vs, e1cache_*)
           val Ans(e2vss, e2cache) = aeval_top(e2, ρ, σ, e1τ, clscache)
           val (VS(e2vs, e2τ, e2σ), e2cache_*) = choices[VS](e2vss, e2cache)
           val α = allocBind(x, e2τ)
           val ρ_* = λρ + (x → α)
           val σ_* = e2σ + (α → e2vs)
           aeval_top(body, ρ_*, σ_*, e2τ, e2cache_*)
           */
    }
  }

  /*
  def aeval_top(e: Expr, ρ: RepEnv, σ: RepStore, τ: Time, cache: Cache): Rep[Ans] = {
    val config = Config(e, ρ, σ, τ)
    if (cache.outContains(config)) {
      (cache.outGet(config), cache.in.map, cache.out.map)
    } else {
      val cache_* = cache.outUpdateFromIn(config)
      val (evss, cin, cout) = (???, ???, ???) //reset { aeval(e, ρ, σ, τ, cache_*) }
      val new_cache = Cache(Store(cin), Store(cout)).outUpdate(config, evss)
      (evss, new_cache.in.map, new_cache.out.map)
    }
  }
   */


  /*
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
