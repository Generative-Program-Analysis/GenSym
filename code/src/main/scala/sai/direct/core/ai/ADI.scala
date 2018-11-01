package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions

import sai.common.ai._
import sai.common.ai.Lattice._
import sai.direct.core.parser._

import AAM._

trait CacheTrait[K,V,R[_],C<:CacheTrait[K,V,R,C]] {
  def inGet(k: K): R[ℙ[V]]
  def outGet(k: K): R[ℙ[V]]
  def inContains(k: K): R[Boolean]
  def outContains(k: K): R[Boolean]
  def outUpdate(k: K, vs: R[ℙ[V]]): C
  //def outUpdateSingle(k: K, v: R[V]): C
  def outUpdateFromIn(k: K): C
  def ⊔(that: C): C
}

object ADI {
  case class Config(e: Expr, ρ: Env, σ: BStore, τ: Time) {
    val k: Int = 0
    def tick: Time = (e :: τ) take k
  }

  case class Cache(in: Store[Config, ℙ[VS]], out: Store[Config, ℙ[VS]]) extends CacheTrait[Config,VS,NoRep,Cache] {
    def inGet(cfg: Config): ℙ[VS] = in.getOrElse(cfg, ℙ())
    def inContains(cfg: Config): Boolean = in.contains(cfg)
    def outGet(cfg: Config): ℙ[VS] = out.getOrElse(cfg, ℙ())
    def outContains(cfg: Config): Boolean = out.contains(cfg)
    def outUpdate(cfg: Config, vss: ℙ[VS]): Cache = Cache(in, out.update(cfg, vss))
    //def outUpdateSingle(cfg: Config, vs: VS): Cache = Cache(in, out.update(cfg, ℙ(vs)))
    def outUpdateFromIn(cfg: Config): Cache = outUpdate(cfg, inGet(cfg))
    def ⊔(that: Cache): Cache = Cache(in ⊔ that.in, out ⊔ that.out)
  }

  object Cache {
    def cache0 = Cache(Store[Config, ℙ[VS]](Map()), Store[Config, ℙ[VS]](Map()))
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

  def aeval_top(e: Expr, ρ: Env, σ: BStore, τ: Time, cache: Cache): Ans = {
    val config = Config(e, ρ, σ, τ)
    if (cache.outContains(config)) {
      Ans(cache.outGet(config), cache)
    } else {
      val cache_* = cache.outUpdateFromIn(config)
      val Ans(evss, cache_**) = reset { aeval(e, ρ, σ, τ, cache_*) }
      Ans(evss, cache_**.outUpdate(config, evss))
    }
  }

  def aeval(e: Expr, ρ: Env, σ: BStore, τ: Time, cache: Cache): Ans @cps[Ans] = {
    val config = Config(e, ρ, σ, τ)
    val τ_* = config.tick
    e match {
      case Lit(i) ⇒
        Ans(ℙ(VS(ℙ(NumV(i)), τ_*, σ)), cache)
      case Var(x) ⇒
        Ans(ℙ(VS(σ(ρ(x)), τ_*, σ)), cache)
      case Lam(x, e) ⇒
        Ans(ℙ(VS(ℙ(CloV(Lam(x, e), ρ)), τ_*, σ)), cache)
      case App(e1, e2) ⇒
        val Ans(e1vss, e1cache) = aeval_top(e1, ρ, σ, τ_*, cache)
        val (VS(e1vs, e1τ, e1σ), e1cache_*) = choices[VS](e1vss, e1cache)
        val (CloV(Lam(x, body), λρ), clscache) = choices[AbsValue](e1vs, e1cache_*)
        val Ans(e2vss, e2cache) = aeval_top(e2, ρ, σ, e1τ, clscache)
        val (VS(e2vs, e2τ, e2σ), e2cache_*) = choices[VS](e2vss, e2cache)
        val α = allocBind(x, e2τ)
        val ρ_* = λρ + (x → α)
        val σ_* = e2σ + (α → e2vs)
        aeval_top(body, ρ_*, σ_*, e2τ, e2cache_*)
      case Rec(x, f, e) ⇒
        val α = allocBind(x, τ_*)
        val ρ_* = ρ + (x → α)
        val σ_* = σ + (α → ℙ())
        val Ans(fvss, fcache) = aeval_top(f, ρ_*, σ_*, τ_*, cache)
        val (VS(fvs, fτ, fσ), fcache_*) = choices[VS](fvss, fcache)
        val σ_** = fσ + (α → fvs)
        aeval_top(e, ρ_*, σ_**, fτ, fcache_*)
      case Void() ⇒
        Ans(ℙ(VS(ℙ(), τ_*, σ)), cache)
      case Set_!(x, e) ⇒
        val Ans(evss, ecache) = aeval_top(e, ρ, σ, τ_*, cache)
        val (VS(evs, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
        val eσ_* = eσ + (ρ(x) → evs)
        Ans(ℙ(VS(ℙ(), eτ, eσ_*)), ecache_*)
      case Begin(e::Nil) ⇒
        val Ans(evss, ecache) = aeval_top(e, ρ, σ, τ_*, cache)
        val (VS(evs, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
        Ans(ℙ(VS(evs, eτ, eσ)), ecache_*)
      case Begin(e::es) ⇒
        val Ans(evss, ecache) = aeval_top(e, ρ, σ, τ_*, cache)
        val (VS(_, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
        aeval(Begin(es), ρ, eσ, eτ, ecache_*) //aeval_top or aeval?
      case AOp(op, e1, e2) ⇒
        val Ans(e1vss, e1cache) = aeval_top(e1, ρ, σ, τ_*, cache)
        val (VS(e1vs, e1τ, e1σ), e1cache_*) = choices[VS](e1vss, e1cache)
        val Ans(e2vss, e2cache) = aeval_top(e2, ρ, e1σ, e1τ, e1cache_*)
        val (VS(e2vs, e2τ, e2σ), e2cache_*) = choices[VS](e2vss, e2cache)
        Ans(ℙ(VS(ℙ(NumVTop), e2τ, e2σ)), e2cache_*)
      case If0(cnd, thn, els) ⇒
        val Ans(cndvss, cndcache) = aeval_top(cnd, ρ, σ, τ_*, cache)
        val (VS(cndvs, cndτ, cndσ), cndcache_*) = choices[VS](cndvss, cndcache)
        //TODO: actually check whether cnd is 0
        val thnans = aeval_top(thn, ρ, cndσ, cndτ, cndcache_*)
        val elsans = aeval_top(els, ρ, cndσ, cndτ, cndcache_*)
        thnans ++ elsans
    }
  }

  def analyze(e: Expr, ρ: Env = ρ0, σ: BStore = bσ0): ℙ[VS] = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, anscache) = aeval_top(e, ρ, σ, τ0, cache)
      if (anscache.out == anscache.in) Ans(vss, anscache)
      else iter(Cache(anscache.out, Store[Config, ℙ[VS]](Map())))
    }
    iter(Cache.cache0).vss
  }
}

object ADI2 {
  import ADI._
  def aeval(e: Expr, ρ: Env, σ: BStore, τ: Time, cache: Cache): Ans @cps[Ans] = {
    val config = Config(e, ρ, σ, τ)
    if (cache.outContains(config))
      Ans(cache.outGet(config), cache)
    else {
      val τ_* = config.tick
      val cache_* = cache.outUpdateFromIn(config)
      e match {
        case Lit(i) ⇒
          val vs = ℙ(VS(ℙ(NumV(i)), τ_*, σ))
          Ans(vs, cache_*.outUpdate(config, vs))
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
        case Rec(x, f, e) ⇒
          val α = allocBind(x, τ_*)
          val ρ_* = ρ + (x → α)
          val σ_* = σ + (α → ℙ())
          val Ans(fvss, fcache) = aeval(f, ρ_*, σ_*, τ_*, cache_*)
          val (VS(fvs, fτ, fσ), fcache_*) = choices[VS](fvss, fcache)
          val σ_** = fσ + (α → fvs)
          val Ans(evss, ecache) = aeval(e, ρ_*, σ_**, fτ, fcache_*)
          Ans(evss, ecache.outUpdate(config, evss))
        case Void() ⇒
          val vs = ℙ(VS(ℙ(), τ_*, σ))
          Ans(vs, cache_*.outUpdate(config, vs))
        case Set_!(x, e) ⇒
          val Ans(evss, ecache) = aeval(e, ρ, σ, τ_*, cache_*)
          val (VS(evs, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
          val eσ_* = eσ + (ρ(x) → evs)
          val vs = ℙ(VS(ℙ(), eτ, eσ_*))
          Ans(vs, ecache_*.outUpdate(config, vs))
        case Begin(e::Nil) ⇒
          val Ans(evss, ecache) = aeval(e, ρ, σ, τ_*, cache_*)
          val (VS(evs, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
          val vs = ℙ(VS(evs, eτ, eσ))
          Ans(vs, ecache_*.outUpdate(config, vs))
        case Begin(e::es) ⇒
          val Ans(evss, ecache) = aeval(e, ρ, σ, τ_*, cache_*)
          val (VS(_, eτ, eσ), ecache_*) = choices[VS](evss, ecache)
          val Ans(bgv, bgcache) = aeval(Begin(es), ρ, eσ, eτ, ecache_*)
          //FIXME: is there unnecessary cache update for imtermediate config?
          Ans(bgv, bgcache.outUpdate(config, bgv))
        case AOp(op, e1, e2) ⇒
          val Ans(e1vss, e1cache) = aeval(e1, ρ, σ, τ_*, cache_*)
          val (VS(e1vs, e1τ, e1σ), e1cache_*) = choices[VS](e1vss, e1cache)
          val Ans(e2vss, e2cache) = aeval(e2, ρ, e1σ, e1τ, e1cache_*)
          val (VS(e2vs, e2τ, e2σ), e2cache_*) = choices[VS](e2vss, e2cache)
          val vs = ℙ(VS(ℙ(NumVTop), e2τ, e2σ))
          Ans(vs, e2cache_*.outUpdate(config, vs))
        case If0(cnd, thn, els) ⇒
          val Ans(cndvss, cndcache) = aeval(cnd, ρ, σ, τ_*, cache_*)
          val (VS(cndvs, cndτ, cndσ), cndcache_*) = choices[VS](cndvss, cndcache)
          //TODO: actually check whether cnd is 0
          //FIXME: Why adding reset?
          val thnans = reset { aeval(thn, ρ, cndσ, cndτ, cndcache_*) }
          val elsans = reset { aeval(els, ρ, cndσ, cndτ, cndcache_*) }
          thnans ++ elsans
      }
    }
  }

  def analyze(e: Expr, ρ: Env = ρ0, σ: BStore = bσ0): ℙ[VS] = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, anscache) = reset{ aeval(e, ρ, σ, τ0, cache) }
      if (anscache.out == anscache.in) Ans(vss, anscache)
      else iter(Cache(anscache.out, Store[Config, ℙ[VS]](Map())))
    }
    iter(Cache.cache0).vss
  }
}

object ADITest {
  import sai.common.parser.Read._
  import sai.direct.core.parser.CoreSchemeParser._

  //TODO: test on nondeterministic cases

  def main(args: Array[String]) {
    val rec = "(letrec ([f (lambda (x) x)]) (f f))".read[Expr].get

    val subto0 = "(letrec ([f (lambda (x) (if0 x x (f (- x 1))))]) (f 3))".read[Expr].get
    println(subto0)
    assert(ADI.analyze(subto0) == ADI2.analyze(subto0))
    println(ADI.analyze(subto0).mkString("\n"))
    println("————————————————————————————————————")

    val fact5 = "(letrec ([fact (lambda (n) (if0 n 1 (* n (fact (- n 1)))))]) (fact 5))".read[Expr].get
    println(fact5)
    assert(ADI.analyze(fact5) == ADI2.analyze(fact5))
    println(ADI.analyze(fact5).mkString("\n"))
    println("————————————————————————————————————")

    val fact100 = "(letrec ([fact (lambda (n) (if0 n 1 (* n (fact (- n 1)))))]) (fact 100))".read[Expr].get
    println(fact100)
    assert(ADI.analyze(fact100) == ADI2.analyze(fact100))
    println(ADI.analyze(fact100).mkString("\n"))
    println("————————————————————————————————————")

    val fact5rec = "(rec ([fact (lambda (n) (if0 n 1 (* n (fact (- n 1)))))]) (fact 5))".read[Expr].get
    println(fact5rec)
    assert(ADI.analyze(fact5rec) == ADI2.analyze(fact5rec))
    println(ADI.analyze(fact5rec).mkString("\n"))
  }
}
