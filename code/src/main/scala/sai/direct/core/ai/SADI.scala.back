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
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, _}

trait SADI extends DslExp with MapOpsExp with SetOpsExp with TupledFunctionsRecursiveExp {
  import AAM._

  def lift[T:Typ](x: T) = unit[T](x)

  //TODO: structural type of Rep
  //TODO: adjustable size for staged map?
  //TODO: instantiate emtpy Map()
  //TODO: staged store
  //TODO: Expr toString function

  case class Store[K, V <% Lattice[V]](map: Rep[Map[K, V]]) {
    def apply(k: K): V = map(k)
    def getOrElse(k: K, dft: V): V = map.getOrElse(k, dft)
    def update(k: K, d: V): Store[K, V] = {
      val oldd = map.getOrElse(k, d.bot)
      Store[K, V](map + (k → (oldd ⊔ d)))
    }
    def update(kv: (K, V)): Store[K, V] = update(kv._1, kv._2)
    def contains(k: K): Boolean = map.contains(k)
    def +(kv: (K, V)): Store[K, V] = update(kv._1, kv._2)
    def ⊔(that: Store[K, V]): Store[K, V] = Store[K, V](this.map ⊔ that.map)
  }

  type ℙ[A] = Rep[Set[A]]
  type Store = Store
  type BStore = Store[BAddr, ℙ[AbsValue]]

  case class Config(e: Expr, ρ: Env, σ: BStore, τ: Time) {
    def tick: Time = (e :: τ) take k
  }

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

  case class VS(vals: ℙ[AbsValue], τ: Time, σ: BStore)
  case class Ans(vss: ℙ[VS], cache: Cache) {
    def ++(ans: Ans) = Ans(vss ++ ans.vss, cache ⊔ ans.cache)
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
}
