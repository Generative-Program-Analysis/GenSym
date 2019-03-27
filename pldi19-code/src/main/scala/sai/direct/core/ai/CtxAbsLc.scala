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
    def choices(ts: R[Set[VST]]): R[VST] @cps[Ans]
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
          val e1vst = choices(e1ans)
          val e2ans = ev(e2, ρ, getσ(e1vst), getτ(e1vst))
          val e2vst = choices(e2ans)
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
    def apply_closure(ev: EvalFun)(fs: Value, arg: Value, σ: Store, τ: Time): Ans @cps[Ans] = {
      nd[AbsValue](fs, Set(), { case CloV(Lam(x, e), ρ) =>
           val α = alloc(σ, x, τ)
           val ρ_* = put(ρ, x, α)
           val σ_* = put(σ, α, arg)
           reset { ev(e, ρ_*, σ_*, τ) }
         })
    }
    def nd[T](ts: Set[T], acc: Ans, k: (T) => Ans): Ans = {
      if (ts.isEmpty) acc
      else nd(ts.tail, acc ⊔ k(ts.head), k)
    }
    def choices(ts: Set[VST]): VST @cps[Ans] = shift {
      f: ((VST) => Ans) => nd(ts, Set[VST](), f)
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

  trait LMSOps extends Dsl with MapOps with UncheckedOps with TupleOps with SetOps with TupledFunctions with ListOps
  trait LMSOpsExp extends DslExp with MapOpsExp with UncheckedOpsExp with TupleOpsExp with SetOpsExp with TupledFunctionsRecursiveExp with ListOpsExp
  trait RepAbsInterpOps extends Abstract with LMSOps {
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

    type R[+T] = Rep[T]
    implicit def exprTyp: Typ[Expr]
    implicit def absValueTyp: Typ[AbsValue]
    implicit def addrTyp: Typ[Addr]

    val ρ0: Rep[Env] = Map[Ident, Addr]()
    val σ0: Rep[Store] = Map[Addr, Value]()
    val τ0: Rep[Time] = List[Expr]()
    val k: Int = 0
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ.getOrElse(a, RepLattice[Value].bot)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = σ + (a → RepLattice[Value].⊔(v, get(σ, a)))
    def tick(e: Expr, τ: Rep[Time]): Rep[Time] = (unit(e) :: τ).take(k)
    def ans(vst: (Rep[Value], Rep[Store], Rep[Time])): Ans = Set[VST](vst)
    def alloc(σ: Rep[Store], x: Ident, τ: Rep[Time]): Rep[Addr] = (unit(x), τ)
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(x, e) = λ
      val f: Rep[(Value, Store, Time)] => Rep[Set[VST]] = (vst: Rep[(Value,Store,Time)]) => {
        val args = vst._1; val σ = vst._2; val τ = vst._3
        val α = alloc(σ, x, τ)
        reset { ev(e, put(ρ, x, α), put(σ, α, args), τ) }
      }
      unchecked[Value]("Set[AbsValue](CompiledClo(", fun(f), ",", λ, ",", ρ, "))")
    }
    def num(i: Int): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")
    def branch0(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = thn ⊔ els
    def prim_eval(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")
    def getv(vst: R[(Value,Store,Time)]): R[Value] = vst._1
    def getσ(vst: R[(Value,Store,Time)]): R[Store] = vst._2
    def getτ(vst: R[(Value,Store,Time)]): R[Time] = vst._3

    type Config = (Expr, Env, Store, Time)
    case class CacheFix(evev: EvalFun => EvalFun) {
      var in = Map[Config, Set[VST]](); var out = Map[Config, Set[VST]]()
      def cached_ev(e: Expr, ρ: Rep[Env], σ: Rep[Store], τ: Rep[Time]): Ans @cps[Ans] = {
        val cfg: Rep[Config] = (unit(e), ρ, σ, τ)
        if (out.contains(cfg)) out(cfg)
        else {
          val ans0: Ans = in.getOrElse(cfg, RepLattice[Set[VST]].bot)
          out = out + (cfg -> ans0)
          val ans1: Ans = reset { evev(cached_ev)(e, ρ, σ, τ) } //TODO: reset here?
          out = out + (cfg -> RepLattice[Set[VST]].⊔(ans0, ans1))
          ans1
        }
      }
      def iter(e: Expr, ρ: Rep[Env], σ: Rep[Store], τ: Rep[Time]): Ans = {
        def iter_aux: Rep[Unit => Set[VST]] = fun { () =>
          in = out; out = Map[Config, Set[VST]]()
          reset { cached_ev(e, ρ, σ, τ) }
          if (in === out) out((unit(e), ρ, σ, τ)) else iter_aux()
        }
        iter_aux()
      }
    }
    override def eval_top(e: Expr, ρ: Rep[Env], σ: Rep[Store], τ: Rep[Time]): Ans = CacheFix(eval).iter(e, ρ, σ, τ)
  }

  trait RepAbsInterpOpsExp extends RepAbsInterpOps with LMSOpsExp {
    implicit def exprTyp: Typ[Expr] = manifestTyp
    implicit def addrTyp: Typ[Addr] = manifestTyp
    implicit def absValueTyp: Typ[AbsValue] = manifestTyp
    case class ApplyClosures(fs: Rep[Value], arg: Rep[Value], σ: Rep[Store], τ: Rep[Time]) extends Def[Set[VST]]
    def apply_closure(ev: EvalFun)(fs: Rep[Value], arg: Rep[Value], σ: Rep[Store], τ: Rep[Time]): Ans @cps[Ans] = {
      reflectEffect(ApplyClosures(fs, arg, σ, τ))
    }
    case class Choices(ts: Rep[Set[VST]], f: Rep[((Value,Store,Time)) => Set[VST]]) extends Def[Set[VST]]
    def choices(ts: Rep[Set[VST]]): Rep[VST] @cps[Ans] = shift {
      f: (Rep[VST] => Ans) => reflectEffect(Choices(ts, fun(f)))
    }

  }

  trait MyScalaGenTupleOps extends ScalaGenBase with TupleGenBase with ScalaGenStruct {
    val IR: TupleOpsExp
    import IR._

    override def remap[A](m: Typ[A]) = m.runtimeClass.getSimpleName match {
      case "Tuple2" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple3" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple4" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple5" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case _ => super.remap(m)
    }
  }

  trait RepAbsInterpGen extends GenericNestedCodegen {
    val IR: RepAbsInterpOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      case ApplyClosures(fs, arg, σ, τ) =>
        emitValDef(sym, "apply_closures_norep(" + quote(fs) + "," + quote(arg) + "," + quote(σ) +  "," + quote(τ) + ")")
      case Choices(ts, f) =>
        emitValDef(sym, "nd[VST](" + quote(ts) + ", Set[VST](), " + quote(f) + ")")
      case Struct(tag, elems) =>
        //TODO: merge back to LMS
        registerStruct(structName(sym.tp), elems)
        val typeName = sym.tp.runtimeClass.getSimpleName + "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
        emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
      case _ => super.emitNode(sym, rhs)
    }
  }

  trait RepAbsInterpDriver extends DslDriver[Unit, Unit] with RepAbsInterpOpsExp { q =>
    override val codegen = new DslGen with ScalaGenMapOps with MyScalaGenTupleOps
        with RepAbsInterpGen with MyScalaGenTupledFunctions with ScalaGenUncheckedOps
        with ScalaGenSetOps with ScalaGenListOps {
      val IR: q.type = q
      override def remap[A](m: Typ[A]): String = {
        if (m.toString.endsWith("$Value")) "Value"
        else if (m.toString.endsWith("$AbsValue")) "AbsValue"
        else if (m.toString.endsWith("$Addr")) "Addr"
        else super.remap(m)
      }
      override def emitSource[A : Typ](args: List[Sym[_]], body: Block[A],
                                       className: String,
                                       stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
        val prelude = """
import sai.direct.core.parser._
import sai.common.ai.Lattices._
object RTSupport {
  type Addr = (String, Time)
  trait AbsValue
  case class NumV() extends AbsValue
  type Value = Set[AbsValue]
  type Store = Map[Addr,Value]
  type Time = List[Expr]
  type VST = (Value, Store, Time)
  type Ans = Set[VST]
  case class CompiledClo(f: (Value, Store, Time) => Set[VST], λ: Lam, ρ: Map[String,Addr]) extends AbsValue {
    def canEqual(a: Any) = a.isInstanceOf[CompiledClo]
    override def equals(that: Any): Boolean = that match {
      case that: CompiledClo => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode: Int = {
      val prime = 31
      var result = 1
      result = prime * result + λ.hashCode
      result = prime * result + ρ.hashCode
      result
    }
  }

  def nd[T](ts: Set[T], acc: Ans, k: T => Ans): Ans = {
    if (ts.isEmpty) acc
    else nd(ts.tail, acc ++ k(ts.head), k)
  }
  def apply_closures_norep(fs: Value, arg: Value, σ: Map[Addr,Value], τ: Time): Set[VST] = {
    nd[AbsValue](fs, Set(), { case CompiledClo(f, _, _) => f(arg, σ, τ) })
  }
}
import RTSupport._
"""
        stream.println(prelude)
        super.emitSource(args, body, className, stream)
      }
    }
  }
}

object KCFATest {
  import CtxAnalysis._
  def main(args: Array[String]) {
    def specialize(p: Expr): DslDriver[Unit, Unit] =
      new RepAbsInterpDriver {
        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val vsts = eval_top(p)
          println(vsts)
        }
      }

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
    //println(AbsInterp.eval_top(fact5))

    val code = specialize(omega)
    println(code.code)
    code.eval(())
  }
}
