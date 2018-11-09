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

object AbsLamCal {
  import LamCal._

  trait Abstract extends Semantics {
    case class Addr(x: Ident)
    sealed trait AbsValue
    case class CloV(λ: Lam, ρ: Env) extends AbsValue
    case class NumV() extends AbsValue
    type Value = Set[AbsValue]
    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]
    override def fix(ev: EvalFun => EvalFun): EvalFun = {
      ???
    }
  }

  //note: context-insensitive, path-insensitive, flow-insensitive
  object AbsInterp extends Abstract {
    type R[+T] = T
    val ρ0 = Map[Ident, Addr]()
    val σ0 = Map[Addr, Value]()
    def get(ρ: Env, x: Ident): Addr = ρ(x)
    def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
    def get(σ: Store, a: Addr): Value = σ.getOrElse(a, Lattice[Value].bot)
    def put(σ: Store, a: Addr, v: Value): Store = {
      val oldv = get(σ, a); σ + (a → Lattice[Value].⊔(v, oldv))
    }
    def alloc(σ: Store, x: Ident): Addr = Addr(x)
    def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
    def num(i: Lit): Value = Set(NumV())
    def apply_closure(ev: EvalFun)(f: Value, arg: Value, σ: Store): Ans = {
      var σ0 = σ
      val vs = for (CloV(Lam(x, e), ρ) <- f) yield {
        val α = alloc(σ0, x)   //note: use the same store?
        val ρ_* = put(ρ, x, α)
        val σ_* = put(σ0, α, arg)
        val (v, vσ) = ev(e, ρ_*, σ_*)
        σ0 = vσ; v
      }
      (vs.reduce(Lattice[Value].⊔(_,_)), σ0)
    }
    def branch0(cnd: Value, thn: => Ans, els: => Ans): Ans = {
      val thnans = thn
      val elsans = els
      (GenericLattice[Value,R].⊔(thn._1, els._1), Lattice[Store].⊔(thn._2, els._2))
    }
    def prim_eval(op: Symbol, v1: Value, v2: Value): Value = Set(NumV())
    type Config = (Expr, R[Env], R[Store])
    object CacheFix {
      var in = Map[Config, Ans]()
      var out = Map[Config, Ans]()
    }
    case class CacheFix(F: EvalFun => EvalFun) {
      import CacheFix._
      def f(e: Expr, ρ: Env, σ: Store): Ans = {
        val cfg: Config = (e, ρ, σ)
        if (out.contains(cfg)) out(cfg)
        else {
          val (v0,σ0): Ans = in.getOrElse(cfg, (Lattice[Value].bot, Lattice[Store].bot))
          out = out + (cfg -> (v0,σ0))
          val (v1,σ1): Ans = F(f)(e, ρ, σ)
          out = out + (cfg -> (Lattice[Value].⊔(v0, v1), Lattice[Store].⊔(σ0, σ1)))
          (v1, σ1)
        }
      }
      def iter(e: Expr, ρ: Env, σ: Store): Ans = {
        in = out; out = Map[Config, Ans](); f(e, ρ, σ)
        if (in == out) out((e, ρ, σ)) else iter(e, ρ, σ)
      }
    }
    override def eval_top(e: Expr, ρ: Env, σ: Store): Ans =
      CacheFix(eval).iter(e, ρ, σ)
  }

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

    type R[+T] = Rep[T]
    implicit def absValueTyp: Typ[AbsValue]
    implicit def addrTyp: Typ[Addr]
    val ρ0: Rep[Env] = Map[Ident, Addr]()
    val σ0: Rep[Store] = Map[Addr, Value]()
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ.getOrElse(a, RepSetLattice[AbsValue].bot)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = {
      val oldv = get(σ, a); σ + (a → RepSetLattice[AbsValue].⊔(v, oldv))
    }
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = unchecked[Addr]("Addr(\"", x, "\")")
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(x, e) = λ
      val f: Rep[(Value, Store)]=>Rep[(Value,Store)] = (as: Rep[(Value,Store)]) => {
        val args = as._1; val σ = as._2
        val α = alloc(σ, x)
        ev(e, put(ρ, x, α), put(σ, α, args))
      }
      unchecked[Value]("Set[AbsValue](CompiledClo(", fun(f), ",", λ, ",", ρ, "))")
    }
    def num(i: Lit): Rep[Value] = unchecked[Value]("Set(NumV())")
    def branch0(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val thnans = thn
      val elsans = els
      (RepSetLattice[AbsValue].⊔(thn._1, els._1), RepMapLattice[Addr,Value].⊔(thn._2, els._2))
    }
    def prim_eval(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = unchecked[Value]("Set(NumV())")
    type Config = (Expr, Env, Store)
    implicit val exprTyp: Typ[Expr]
    object CacheFix {
      var in: Rep[Map[Config, (Value,Store)]] = Map[Config, (Value,Store)]()
      var out: Rep[Map[Config, (Value,Store)]] = Map[Config, (Value,Store)]()
    }
    case class CacheFix(F: EvalFun => EvalFun) {
      import CacheFix._
      def f(e: Expr, ρ: Rep[Env], σ: Rep[Store]): Rep[(Value, Store)] = {
        System.out.println(s"calling f on $e")
        val cfg: Rep[Config] = (unit(e), ρ, σ)
        (if (out.contains(cfg)) out(cfg)
        else {
          val (v0,σ0): Ans = in.getOrElse(cfg, (RepSetLattice[AbsValue].bot, RepMapLattice[Addr,Value].bot))
          out = out + (cfg -> (v0,σ0))
          val (v1,σ1): Ans = F(f)(e, ρ, σ)
          out = out + (cfg -> (RepSetLattice[AbsValue].⊔(v0, v1), RepMapLattice[Addr,Value].⊔(σ0, σ1)))
          (v1, σ1)
        }).asInstanceOf[Rep[(Value,Store)]]
      }
      def iter(e: Expr): Ans = {
        System.out.println(s"calling iter on $e")
        in = out; out = Map[Config, (Value,Store)](); f(e, ρ0, σ0)
        (if (in == out) out((unit(e), ρ0, σ0)) else iter(e)).asInstanceOf[Rep[(Value,Store)]]
      }
    }
    override def eval_top(e: Expr, ρ: R[Env], σ: R[Store]): Ans = CacheFix(eval).iter(e)
  }

  trait RepAbsInterpOpsExp extends RepAbsInterpOps with LMSOpsExp {
    implicit val exprTyp: Typ[Expr] = manifestTyp
    implicit def absValueTyp: Typ[AbsValue] = manifestTyp
    implicit def addrTyp: Typ[Addr] = manifestTyp
    case class ApplyClosures(fs: Rep[Value], arg: Rep[Value], σ: Rep[Store]) extends Def[(Value, Store)]
    def apply_closure(ev: EvalFun)(fs: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Ans = {
      reflectEffect(ApplyClosures(fs, arg, σ))
    }
  }

  trait RepAbsInterpGen extends GenericNestedCodegen {
    val IR: RepAbsInterpOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      case ApplyClosures(fs, arg, σ) =>
        emitValDef(sym, "apply_closures_norep(" + quote(fs) + "," + quote(arg) + "," + quote(σ) + ")")
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
        with ScalaGenSetOps {
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
  case class Addr(x: String)
  trait AbsValue
  case class NumV() extends AbsValue
  case class CompiledClo(f: (Value, Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Lam, ρ: Map[String,Addr]) extends AbsValue
  type Value = Set[AbsValue]
  def apply_closures_norep(f: Value, arg: Value, σ: Map[Addr,Value]) = {
    var σ0 = σ
    val vs: Set[Value] = for (CompiledClo(fun, λ, ρ) <- f) yield {
      val (v, vσ) = fun(arg, σ0)
      σ0 = vσ; v
    }
    (vs.reduce(Lattice[Value].⊔(_,_)), σ0)
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

object AbsLamCalTest {
  import AbsLamCal._
  def main(args: Array[String]) {
    def specialize(p: Expr): DslDriver[Unit, Unit] =
      new RepAbsInterpDriver {
        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val (v, s) = eval_top(p)
          println(v); println(s)
        }
      }
    // ((λ (x) ((x x) x)) (λ (y) y))
    val id4 = App(Lam("x", App(App(Var("x"), Var("x")), Var("x"))), Lam("y", Var("y")))
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))

    println(AbsInterp.eval_top(id4))
    val code = specialize(id4)
    println(code.code)
    //code.eval(())
  }
}
