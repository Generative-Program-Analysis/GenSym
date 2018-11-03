package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

object Semantics {
  type NoRep[+T] = T

  trait EnvSpec[R[+_], K, V, E <: EnvSpec[R,K,V,E]] {
    def apply(k: R[K]): R[V]
    def +(kv: (R[K], R[V])): E
  }

  trait StoreSpec[R[_], K, V, S <: StoreSpec[R,K,V,S]] {
    def apply(k: R[K]): R[V]
    def getOrElse(k: R[K], dft: R[V]): R[V]
    def +(kv: (R[K], R[V])): S
    def update(kv: (R[K], R[V])): S
    def update(k: R[K], v: R[V]): S
    def contains(k: R[K]): R[Boolean]
    def ⊔(that: S): S
  }

  trait CacheSpec[R[_], K, V, C <: CacheSpec[R,K,V,C]] {
    def inGet(k: R[K]): R[V]
    def outGet(k: R[K]): R[V]
    def inContains(k: R[K]): R[Boolean]
    def outContains(k: R[K]): R[Boolean]
    def outUpdate(k: R[K], v: R[V]): C
    //def outUpdateSingle(k: K, v: R[V]): C
    def outUpdateFromIn(k: R[K]): C
    def ⊔(that: C): C
  }

  trait Sem {
    type R[+T]
    type Addr
    type Ident
    //type Contour
    type Value
    type Env <: EnvSpec[R,Ident,Addr,Env]
    type Store <: StoreSpec[R,Addr,Value,Store]
    type Ans

    val ρ0: Env
    val σ0: Store
    def alloc(x: R[Ident], σ: Store): R[Addr]
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans

    type EvalFun = (Expr, Env, Store) ⇒ Ans
    def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ) => ev(fix(ev))(e, ρ, σ)
    def eval_top(e: Expr): Ans = fix(eval)(e, ρ0, σ0)
  }

  trait Test extends Sem {
    type R[+T] = NoRep[T]
    //TODO: can this also be F-bound poly?
    type Ident = String
    type Addr = Int

    trait EnvLike[R[+_],K,V,E] {
      def bala(m: E, k: R[K]): R[V]
      def +(m: E, kv: (R[K], R[V])): E
    }
    object EnvLike {
      def apply[R[+_],K,V,E](implicit env: EnvLike[R,K,V,E]): EnvLike[R,K,V,E] = env
    }
    implicit class EnvOps[R[+_],K,V,E:({type λ[ε] = EnvLike[R,K,V,ε]})#λ](env: E) {
      def bala(k: R[K]): R[V] = EnvLike[R,K,V,E].bala(env, k)
    }

    //trait OhMyEnv[E] extends EnvLike[NoRep,Ident,Addr,E]

    type Env1 = Map[Ident, Addr]
    //implicit def EnvImp: EnvLike[NoRep,Ident,Addr,Env1] = new EnvLike[NoRep,Ident,Addr,Env1] {
    implicit def EnvImp: EnvLike[NoRep,Ident,Addr,Env1] = new EnvLike[NoRep,Ident,Addr,Env1] {
      def bala(m: Env1, k: Ident): Addr = m(k)
      def +(m: Env1, kv: (Ident, Addr)): Env1 = m + (kv._1 -> kv._2)
    }

    type NoRepEnv[E] = EnvLike[NoRep, Ident, Addr, E]
    def f[E: NoRepEnv](m: E): Addr = {
      m.bala("String")
    }
    val m: Env1 = Map[Ident, Addr]()
    f(m)
    //val k: Addr = m.bala("SSS")
  }

  trait Concrete extends Sem {
    type Ident = String
    type Addr = Int
    abstract class Value
    case class CloV(λ: Lam, ρ: Env) extends Value
    case class NumV(i: Int) extends Value
    type Ans = (R[Value], Store)
  }

  trait Abstract extends Sem {
    type Ident = String
    case class Addr(x: Ident)
    //type Contour = List[Expr]
    abstract class AbsValue
    case class CloV(λ: Lam, ρ: Env) extends AbsValue
    case class NumV() extends AbsValue
    type Value = Set[AbsValue]
    type Ans = (R[Value], Store)
  }

  //TODO: Make sure this is path-sensitive (not flow-sensitive).
  trait PathSenAbstract extends Sem {
    type Ident = String
    case class Addr(x: Ident)
    //type Contour = List[Expr]
    abstract class AbsValue
    case class CloV(λ: Lam, ρ: Env) extends AbsValue
    case class NumV() extends AbsValue
    type Value = Set[AbsValue]
    type Ans = Set[(Value, Store)]
  }

}

import Semantics._

object UnStaged {

  /*************************** CONCRETE ****************************/

  object NoRepConSem extends Concrete {
    type R[+T] = NoRep[T]
    case class Env(map: Map[Ident, Addr]) extends EnvSpec[NoRep,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    val ρ0: Env = Env(Map[Ident,Addr]())
    case class Store(map: Map[Addr, Value]) extends StoreSpec[NoRep,Addr,Value,Store] {
      def apply(k: Addr): Value = map(k)
      def update(k: Addr, v: Value): Store = Store(map + (k → v))
      def getOrElse(k: Addr, dft: Value): Value = map.getOrElse(k, dft)
      def +(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def update(kv: (Addr, Value)): Store = update(kv._1, kv._2)
      def contains(k: Addr): Boolean = map.contains(k)
      def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
    }
    val σ0: Store = Store(Map[Addr,Value]())

    def evalArith(op: Symbol, vs: List[NumV]): NumV = op match {
      case '+ ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j+i) }
      case '- ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j-i) }
      case '* ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j*i) }
    }

    def alloc(x: Ident, σ: Store): Addr = σ.map.size + 1
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
      case Lit(i) => (NumV(i), σ)
      case Var(x) => (σ(ρ(x)), σ)
      case Lam(x, e) => (CloV(Lam(x,e), ρ), σ)
      case App(e1, e2) =>
        val (CloV(Lam(x,e), λρ), e1σ) = ev(e1, ρ, σ)
        val (e2v, e2σ) = ev(e2, ρ, e1σ)
        val α = alloc(x, e2σ)
        ev(e, λρ + (x → α), e2σ + (α → e2v))
      case Rec(x, f, body) =>
        val α = alloc(x, σ)
        val ρ_* = ρ + (x → α)
        val (fv, fσ) = ev(f, ρ_*, σ)
        val σ_* = fσ + (α → fv)
        ev(body, ρ_*, σ_*)
      case Let(x, e, body) => ev(App(Lam(x,body),e), ρ, σ)
      case If0(cnd, thn, els) =>
        val (cndv:NumV, cndσ) = ev(cnd, ρ, σ)
        if (cndv.i == 0) ev(thn, ρ, cndσ)
        else ev(els, ρ, cndσ)
      case AOp(op, e1, e2) =>
        val (e1v:NumV, e1σ) = ev(e1, ρ, σ)
        val (e2v:NumV, e2σ) = ev(e2, ρ, e1σ)
        (evalArith(op, e2v::e1v::Nil), e2σ)
    }
  }

  /*************************** ABSTRACT ****************************/

  object NoRepAbsSem extends Abstract {
    type R[+T] = NoRep[T]
    case class Env(map: Map[Ident, Addr]) extends EnvSpec[NoRep,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    case class Store(map: Map[Addr, Value]) extends StoreSpec[NoRep,Addr,Value,Store] {
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
      case Lam(x, e) ⇒ (Set(CloV(Lam(x,e), ρ)), σ)
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
  }

  object NoRepAbsSem2 extends PathSenAbstract {
    type R[+T] = NoRep[T]
    case class Env(map: Map[Ident, Addr]) extends EnvSpec[NoRep,Ident,Addr,Env] {
      def apply(k: Ident): Addr = map(k)
      def +(kv: (Ident,Addr)): Env = Env(map + (kv._1 → kv._2))
    }
    case class Store(map: Map[Addr, Value]) extends StoreSpec[NoRep,Addr,Value,Store] {
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
      case Lit(i) => Set((Set(NumV()), σ))
      case Var(x) => Set((σ(ρ(x)), σ))
      case Lam(x, e) => Set((Set(CloV(Lam(x,e), ρ)), σ))
      case App(e1, e2) =>
        val result: Set[Ans] =
          for ((e1vs, e1σ) <- ev(e1, ρ, σ);
               CloV(Lam(x, e), λρ) <- e1vs;
               (e2vs, e2σ) <- ev(e2, ρ, e1σ)) yield {
            val α = alloc(x, e2σ)
            val ρ_* = λρ + (x → α)
            val σ_* = e2σ + (α → e2vs)
            ev(e, ρ_*, σ_*)
          }
        result.flatten
    }

    val ρ0: Env = Env(Map[Ident,Addr]())
    val σ0: Store = Store(Map[Addr,Value]())

    type Config = (Expr, Env, Store)

    case class Cache(in: Map[Config, Ans], out: Map[Config, Ans]) extends CacheSpec[NoRep,Config,Ans,Cache] {
      def inGet(cfg: Config): Ans = in.getOrElse(cfg, Set())
      def inContains(cfg: Config): Boolean = in.contains(cfg)
      def outGet(cfg: Config):Ans = out.getOrElse(cfg, Set())
      def outContains(cfg: Config): Boolean = out.contains(cfg)
      def outUpdate(cfg: Config, vss: Ans): Cache = {
        val oldv: Ans = out.getOrElse(cfg, Set())
        Cache(in, out + (cfg -> (oldv ⊔ vss)))
      }
      def outUpdateFromIn(cfg: Config): Cache = outUpdate(cfg, inGet(cfg))
      def ⊔(that: Cache): Cache = Cache(in ⊔ that.in, out ⊔ that.out)
    }

    def eval_cache(ev: EvalFun)(e: Expr, ρ: Env, σ: Store, cache: Cache): (Ans, Cache) = {
      val config = (e, ρ, σ)
      if (cache.outContains(config)) {
        (cache.outGet(config), cache)
      } else {
        val cache_* = cache.outUpdateFromIn(config)
        val ans = ev(e, ρ, σ) //TODO:
        (ans, cache_*.outUpdate(config, ans))
      }
    }

    override def eval_top(e: Expr): Ans = ???
  }
}

trait Staged extends Dsl {
  val power5 = new DslDriver[Int, Int] {
    def power(b: Rep[Int], x: Int): Rep[Int] =
      if (x == 0) 1 else b * power(b, x-1)
    def snippet(x: Rep[Int]): Rep[Int] = power(x, 5)
  }

  trait LamOps extends Base with StringOps with Variables {
    implicit def repToLamOps(l: Rep[Lam]) = new LamOpsCls(l)
    class LamOpsCls(l: Rep[Lam]) {
      def x = lam_var(l)
    }
    def lam_var(l: Rep[Lam])(implicit pos: SourceContext): Rep[String]
  }

  trait LamOpsExp extends BaseExp with LamOps with ListOpsExp with StringOpsExp with VariablesExp {
    implicit def LamTyp: Typ[Lam] = manifestTyp
    case class LamVar(l: Exp[Lam]) extends Def[String]
    def lam_var(l: Exp[Lam])(implicit pos: SourceContext): Exp[String] = LamVar(l)
  }

  trait CloVOps extends Base with Concrete with LamOps {
    implicit def repToCloVOps(c: Rep[CloV]) = new CloVOps(c)
    class CloVOps(c: Rep[CloV]) {
      def λ: Rep[Lam] = clov_lam(c)
      def ρ: Rep[Env] = clov_env(c)
    }
    def clov_lam(c: Rep[CloV])(implicit pos: SourceContext): Rep[Lam]
    def clov_env(c: Rep[CloV])(implicit pos: SourceContext): Rep[Env]
  }

  trait CloVOpsExp extends BaseExp with CloVOps with LamOpsExp {
    implicit def CloVTyp: Typ[CloV] = manifestTyp
    case class CloVLam(c: Exp[CloV]) extends Def[Lam]
    case class CloVEnv(c: Exp[CloV]) extends Def[Env]
    def clov_lam(c: Exp[CloV])(implicit pos: SourceContext): Exp[Lam] = CloVLam(c)
    def clov_env(c: Exp[CloV])(implicit pos: SourceContext): Exp[Env] = ??? //CloVEnv(c)
  }

  object RepConSem extends Concrete
      with DslExp with MapOpsExp with SetOpsExp
      with ListOpsExp with TupleOpsExp with TupledFunctionsRecursiveExp
      with LamOpsExp with CloVOpsExp {
    type R[+T] = Rep[T]

    //TODO: use a type class ?
    case class Env(map: Rep[Map[Ident, Addr]]) extends EnvSpec[Rep,Ident,Addr,Env] {
      def apply(k: Rep[Ident]): Rep[Addr] = map(k)
      def +(kv: (Rep[Ident], Rep[Addr])): Env = Env(map + (kv._1, kv._2))
    }
     val ρ0: Env = Env(Map[Ident,Addr]())
    //type Env = Rep[Map[Ident, Addr]]
    //val ρ0: Env = Map[Ident,Addr]()

    implicit def AbsValueTyp: Typ[Value] = manifestTyp
    implicit def NumVTyp: Typ[NumV] = manifestTyp

    case class Store(map: Rep[Map[Addr, Value]]) extends StoreSpec[Rep,Addr,Value,Store] {
      def apply(k: Rep[Addr]): Rep[Value] = map(k)
      def update(k: Rep[Addr], v: Rep[Value]): Store = Store(map + (k → v))
      def getOrElse(k: Rep[Addr], dft: Rep[Value]): Rep[Value] = map.getOrElse(k, dft)
      def +(kv: (Rep[Addr], Rep[Value])): Store = update(kv._1, kv._2)
      def update(kv: (Rep[Addr], Rep[Value])): Store = update(kv._1, kv._2)
      def contains(k: Rep[Addr]): Rep[Boolean] = map.contains(k)
      def ⊔(that: Store): Store = throw new RuntimeException("Not implemented")
    }

    val σ0: Store = Store(Map[Addr, Value]())
    def alloc(x: Rep[Ident], σ: Store): Rep[Addr] = σ.map.size+1
    def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
      case Lit(i) => (unit(NumV(i)), σ)
      case Var(x) => (σ(ρ(x)), σ)
      case Lam(x, e) => (unit(CloV(Lam(x,e), ρ)), σ)
      case App(e1, e2) =>
        //val (CloV(Lam(x,e), λρ), e1σ) = ev(e1, ρ, σ)
        val (clo: Rep[CloV], e1σ) = ev(e1, ρ, σ)
        val (e2v, e2σ) = ev(e2, ρ, e1σ)
        val α = alloc(clo.λ.x, e2σ)
        // clo.ρ : Rep[Env],
        //ev(e, clo.ρ + ((clo.λ.x, α)), e2σ + (α → e2v))
        ???
    }
  }

  trait ScalaGenConcInterp extends GenericNestedCodegen with ScalaGenEffect {
    val IR: LamOpsExp with CloVOpsExp
    import IR._

    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      case LamVar(lam) => emitValDef(sym, src"$lam.vars")
      case CloVLam(c) => emitValDef(sym, src"$c.λ")
      case CloVEnv(c) => emitValDef(sym, src"$c.ρ")
      case _ => super.emitNode(sym, rhs)
    }
  }

}


object SADI2 {
  import UnStaged._
  def main(args: Array[String]) {
    val omega = App(Lam("x", App(Var("x"), Var("x"))), Lam("x", App(Var("x"), Var("x"))))
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))
    val fact10 = Rec("fact", fact, App(Var("fact"), Lit(10)))
    println(NoRepConSem.eval_top(fact5))
    //NoRepConSem.eval_top(omega) /* Stack overflow */
    //NoRepAbsSem.eval_top(omega) /* Stack overflow */
  }

}
