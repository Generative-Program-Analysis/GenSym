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
      //val oldv = get(σ, a); σ + (a → Lattice[Value].⊔(v, oldv))
      val oldv = get(σ, a); σ + (a → (v ⊔ oldv))
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
      (vs.reduce(Lattice[Value].⊔), σ0)
    }
    def branch0(cnd: Value, thn: => Ans, els: => Ans): Ans = {
      thn ⊔ els //FIXME
      //(GenericLattice[Value,R].⊔(thn._1, els._1), Lattice[Store].⊔(thn._2, els._2))
    }
    def prim_eval(op: Symbol, v1: Value, v2: Value): Value = Set(NumV())
    type Config = (Expr, R[Env], R[Store])

    case class CacheFix(evev: EvalFun => EvalFun) {
      var in = Map[Config, Ans](); var out = Map[Config, Ans]()
      def cached_ev(e: Expr, ρ: Env, σ: Store): Ans = {
        val cfg: Config = (e, ρ, σ)
        if (out.contains(cfg)) out(cfg)
        else {
          val ans0: Ans = in.getOrElse(cfg, Lattice[(Value, Store)].bot)
          out = out + (cfg -> ans0)
          val ans1: Ans = evev(cached_ev)(e, ρ, σ)
          out = out + (cfg -> Lattice[(Value, Store)].⊔(ans0, ans1))
          ans1
        }
      }
      def iter(e: Expr, ρ: Env, σ: Store): Ans = {
        in = out; out = Map[Config, Ans](); cached_ev(e, ρ, σ)
        if (in == out) out((e, ρ, σ)) else iter(e, ρ, σ)
      }
    }
    override def eval_top(e: Expr, ρ: Env, σ: Store): Ans = CacheFix(eval).iter(e, ρ, σ)
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

    //val h = new scala.collection.mutable.HashMap[String, Int]()
    //var next: Int = 0
    //h(x) = next; next += 1; \rho  += a

    implicit def absValueTyp: Typ[AbsValue]
    implicit def addrTyp: Typ[Addr]
    val ρ0: Rep[Env] = Map[Ident, Addr]()
    val σ0: Rep[Store] = Map[Addr, Value]()
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ.getOrElse(a, RepLattice[Value].bot)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = {
      val oldv = get(σ, a); σ + (a → RepLattice[Value].⊔(v, oldv))
    }
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = unchecked[Addr]("Addr(\"", x, "\")")
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(x, e) = λ
      //val f: Rep[(Value, Store)]=>Rep[(Value,Store)] = {
      // case (args: Rep[Value], σ: Rep[Store]) =>
      val f: Rep[(Value, Store)]=>Rep[(Value,Store)] = (as: Rep[(Value,Store)]) => {
        val args = as._1; val σ = as._2
        val α = alloc(σ, x)
        ev(e, put(ρ, x, α), put(σ, α, args))
      }
      unchecked[Value]("Set[AbsValue](CompiledClo(", fun(f), ",", λ, ",", ρ, "))")
    }
    def num(i: Lit): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")
    def branch0(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val thnans = thn
      val elsans = els
      (RepLattice[Value].⊔(thn._1, els._1), RepLattice[Store].⊔(thn._2, els._2))
    }
    def prim_eval(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = unchecked[Value]("Set[AbsValue](NumV())")
  }

  trait RepAbsInterpOpsExp extends RepAbsInterpOps with LMSOpsExp {
    implicit def exprTyp: Typ[Expr] = manifestTyp
    implicit def addrTyp: Typ[Addr] = manifestTyp
    implicit def absValueTyp: Typ[AbsValue] = manifestTyp
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
  type Value = Set[AbsValue]
  case class CompiledClo(f: (Value, Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Lam, ρ: Map[String,Addr]) extends AbsValue {
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
        type Config = (Expr, Env, Store)
        case class CacheFix(evev: EvalFun => EvalFun) {
          var in = Map[Config, (Value,Store)]()
          var out = Map[Config, (Value,Store)]()

          def cached_ev(e: Expr, ρ: Rep[Env], σ: Rep[Store]): Rep[(Value, Store)] = {
            println(s"calling cachev_ev $e")
            val cfg: Rep[Config] = (unit(e), ρ, σ)
            if (out.contains(cfg)) {
              out(cfg)
            }
            else {
              val ans0: Ans = in.getOrElse(cfg, RepLattice[(Value, Store)].bot)
              out = out + (cfg -> ans0)
              val ans1: Ans = evev(cached_ev)(e, ρ, σ)
              out = out + (cfg -> RepLattice[(Value, Store)].⊔(ans0, ans1))
              ans1
            }
          }
          def iter(e: Expr, ρ: Rep[Env], σ: Rep[Store]): Rep[(Value,Store)] = {
            def iter_aux: Rep[Unit => (Value,Store)] = fun { () =>
              println(s"Start iteration ${e}")
              in = out;
              out = Map[Config, (Value,Store)]()
              cached_ev(e, ρ, σ)
              if (in === out) out((unit(e), ρ, σ)) else iter_aux()
            }
            iter_aux()
          }
        }
        override def eval_top(e: Expr, ρ: R[Env], σ: R[Store]): Ans = CacheFix(eval).iter(e, ρ, σ)

        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val (v, s) = eval_top(p)
          println(v); println(s)
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

    //println(AbsInterp.eval_top(id4))
    //println(AbsInterp.eval_top(omega))
    println(AbsInterp.eval_top(fact5))

    val code = specialize(fact5)
    println(code.code)
    code.eval(())
  }
}
