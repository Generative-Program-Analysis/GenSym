package sai.direct.large.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.utils._
import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.large.parser._

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
    case class IntV() extends AbsValue
    case class FloatV() extends AbsValue
    case class CharV() extends AbsValue
    case class BoolV() extends AbsValue
    case class ListV() extends AbsValue
    case class VectorV() extends AbsValue
    case class VoidV() extends AbsValue
    case class SymV() extends AbsValue

    type Value = Set[AbsValue]
    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]
  }

  //note: context-insensitive, path-insensitive, flow-insensitive
  object AbsInterp extends Abstract {
    type R[+T] = T
    val ρ0 = Map[Ident, Addr]()
    val σ0 = Map[Addr, Value]()
    def get(ρ: Env, x: Ident): Addr = ρ.getOrElse(x, Addr("__somerandomthingthatdoesntexist"))
    def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
    def get(σ: Store, a: Addr): Value = σ.getOrElse(a, Lattice[Value].bot)
    def put(σ: Store, a: Addr, v: Value): Store = {
      //val oldv = get(σ, a); σ + (a → Lattice[Value].⊔(v, oldv))
      val oldv = get(σ, a); σ + (a → (v ⊔ oldv))
    }
    def alloc(σ: Store, x: Ident): Addr = Addr(x)
    def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
    def sym(s: Sym): Value = Set(SymV())
    def int(i: IntLit): Value = Set(IntV())
    def bool(b: BoolLit): Value = Set(BoolV())
    def float(f: FloatLit): Value = Set(FloatV())
    def char(c: CharLit): Value = Set(CharV())
    def void(): Value = Set(VoidV())
    def apply_closure(ev: EvalFun)(f: Value, argvs: List[Value], σ: Store): Ans = {
      var σ0 = σ
      val vs = for (CloV(Lam(argns, e), ρ) <- f) yield {
        val (ρ_*, σ_*): (Env, Store) = ((argns zip argvs) foldLeft((ρ, σ0))) {
          case ((ρ_, σ_), (argn, argv)) =>
            val α = alloc(σ_, argn)
            (put(ρ_, argn, α), put(σ_, α, argv))
        }
        val (v, vσ) = ev(e, ρ_*, σ_*)
        σ0 = vσ ⊔ σ_*; v
      }
      (vs.foldLeft(Lattice[Value].bot)(Lattice[Value].⊔), σ0)
    }

    def branch(cnd: Value, thn: => Ans, els: => Ans): Ans = {
      thn ⊔ els //FIXME
      //(GenericLattice[Value,R].⊔(thn._1, els._1), Lattice[Store].⊔(thn._2, els._2))
    }

    def prim_eval(op: String, lv: List[Value]): Value = op match {
      case op if (scala.collection.immutable.Set("+", "-", "*", "/", "%", "vector-length")(op)) => Set(IntV())
      case op if (scala.collection.immutable.Set("eq?", "null?", "pair?", "and", "or", "not", ">", "<", ">=", "<=")(op)) => Set(BoolV())
      case op if (scala.collection.immutable.Set("list", "cons", "cdr", "cadr")(op)) => Set(ListV())
      case op if ((scala.collection.immutable.Set("car", "caar", "caddr", "vector-ref"))(op)) =>
        Set(BoolV(), IntV(), FloatV(), CharV(), ListV(), VectorV(), SymV())
      case op if ((scala.collection.immutable.Set("read", "vector", "make-vector", "number->string"))(op)) =>
        Set(VectorV())
      case op if (scala.collection.immutable.Set("vector-set!", "display", "write", "newline", "set-cdr!", "error")(op)) => Set(VoidV())
    }

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
    override def eval_top(e: Expr, ρ: Env, σ: Store): Ans = CacheFix(eval).cached_ev(e, ρ, σ)
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
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ.getOrElse(x, unchecked[Addr]("Addr(\"__somerandomthingthatdoesntexist\")"))
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ.getOrElse(a, RepLattice[Value].bot)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = {
      val oldv = get(σ, a); σ + (a → RepLattice[Value].⊔(v, oldv))
    }
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = unchecked[Addr]("Addr(\"", x, "\")")
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(argns, e) = λ
      //val f: Rep[(Value, Store)]=>Rep[(Value,Store)] = {
      // case (args: Rep[Value], σ: Rep[Store]) =>
      val f: Rep[(List[Value], Store)]=>Rep[(Value,Store)] = (as: Rep[(List[Value],Store)]) => {
        val argvs = as._1; val σ = as._2
        def aux(argns: List[Ident], argvs: Rep[List[Value]], ρ: Rep[Env], σ: Rep[Store]): (Rep[Env], Rep[Store]) = argns match {
          case Nil => (ρ, σ)
          case x :: xs =>
            val α = alloc(σ, x)
            aux(xs, argvs.tail, put(ρ, x, α), put(σ, α, unchecked[Value]("Set[AbsValue](IntV())")))
        }
        val (ρ_*, σ_*) = aux(argns, argvs, ρ, σ)
        ev(e, ρ_*, σ_*)
      }
      unchecked[Value]("Set[AbsValue](CompiledClo(", fun(f), ",", λ.hashCode, ",", ρ.hashCode, "))")
    }
    def sym(s: Sym): Rep[Value] = unchecked[Value]("Set[AbsValue](SymV())")
    def int(i: IntLit): Rep[Value] = unchecked[Value]("Set[AbsValue](IntV())")
    def bool(b: BoolLit): Rep[Value] = unchecked[Value]("Set[AbsValue](BoolV())")
    def float(f: FloatLit): Rep[Value] = unchecked[Value]("Set[AbsValue](FloatV())")
    def char(c: CharLit): Rep[Value] = unchecked[Value]("Set[AbsValue](CharV())")
    def void(): Rep[Value] = unchecked[Value]("Set[AbsValue](VoidV())")

    def branch(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val thnans = thn
      val elsans = els
      (RepLattice[Value].⊔(thn._1, els._1), RepLattice[Store].⊔(thn._2, els._2))
    }

    def prim_eval(op: String, lv: List[Rep[Value]]): Rep[Value] = op match {
      case op if ((scala.collection.immutable.Set("eq?", "null?", "pair?", "and", "or", "not", ">", "<", ">=", "<=", "vector-length"))(op)) =>
        unchecked[Value]("Set[AbsValue](BoolV())")
      case op if ((scala.collection.immutable.Set("+", "-", "*", "/", "%"))(op)) =>
        unchecked[Value]("Set[AbsValue](IntV())")
      case op if ((scala.collection.immutable.Set("list", "cons", "cadr", "cdr"))(op)) =>
        unchecked[Value]("Set[AbsValue](ListV())")
      case op if ((scala.collection.immutable.Set("car", "caar", "caddr", "vector-ref"))(op)) =>
        unchecked[Value]("Set[AbsValue](BoolV(), IntV(), FloatV(), CharV(), ListV(), VectorV(), SymV())")
      case op if ((scala.collection.immutable.Set("read", "vector", "make-vector", "number->string"))(op)) =>
        unchecked[Value]("Set[AbsValue](VectorV())")
      case op if ((scala.collection.immutable.Set("display", "write", "newline", "vector-set!", "set-cdr!", "error"))(op)) =>
        unchecked[Value]("Set[AbsValue](VoidV())")
    }

    type Config = (Expr, Env, Store)
    implicit def exprTyp: Typ[Expr]
    case class CacheFix(evev: EvalFun => EvalFun) {
      var in = Map[Config, (Value,Store)]()
      var out = Map[Config, (Value,Store)]()

      def cached_ev(e: Expr, ρ: Rep[Env], σ: Rep[Store]): Rep[(Value, Store)] = {
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
          in = out;
          out = Map[Config, (Value,Store)]()
          cached_ev(e, ρ, σ)
          if (in === out) out((unit(e), ρ, σ)) else iter_aux()
        }
        iter_aux()
      }
    }
    override def eval_top(e: Expr, ρ: R[Env], σ: R[Store]): Ans = CacheFix(eval).iter(e, ρ, σ)
  }

  trait RepAbsInterpOpsExp extends RepAbsInterpOps with LMSOpsExp {
    implicit def exprTyp: Typ[Expr] = manifestTyp
    implicit def addrTyp: Typ[Addr] = manifestTyp
    implicit def absValueTyp: Typ[AbsValue] = manifestTyp
    case class ApplyClosures(fs: Rep[Value], arg: List[Rep[Value]], σ: Rep[Store]) extends Def[(Value, Store)]
    def apply_closure(ev: EvalFun)(fs: Rep[Value], args: List[Rep[Value]], σ: Rep[Store]): Ans = {
      reflectEffect(ApplyClosures(fs, args, σ))
    }
  }

  trait RepAbsInterpGen extends GenericNestedCodegen {
    val IR: RepAbsInterpOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      case ApplyClosures(fs, args, σ) =>
        val argsstr = args.map(quote).mkString(", ")
        emitValDef(sym, "apply_closures_norep(" + quote(fs) + ", List(" + argsstr + "), " + quote(σ) + ")")
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
import sai.direct.large.parser._
import sai.common.ai.Lattices._
object RTSupport {
  case class Addr(x: String)
  trait AbsValue
  case class IntV() extends AbsValue
  case class FloatV() extends AbsValue
  case class CharV() extends AbsValue
  case class BoolV() extends AbsValue
  case class ListV() extends AbsValue
  case class VectorV() extends AbsValue
  case class VoidV() extends AbsValue
  case class SymV() extends AbsValue
  type Value = Set[AbsValue]
  case class CompiledClo(f: (List[Value], Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Int, ρ: Int) extends AbsValue {
    def canEqual(a: Any) = a.isInstanceOf[CompiledClo]
    override def equals(that: Any): Boolean = that match {
      case that: CompiledClo => that.canEqual(this) && this.λ == that.λ && this.ρ == that.ρ //this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode: Int = {
      val prime = 31
      var result = 1
      result = prime * result + λ //.hashCode
      result = prime * result + ρ //.hashCode
      result
    }
  }
  def apply_closures_norep(f: Value, args: List[Value], σ: Map[Addr,Value]) = {
    val (σ0, vs) = f.foldLeft((σ, Set[Value]())) {
      case ((σ0, vs), CompiledClo(fun, _, _)) =>
        val (v, vσ) = fun(args, σ0)
        (vσ ⊔ σ0, vs + v)
      case ((σ0, vs), _) =>
        (σ0, vs)
    }
    (vs.foldLeft(Lattice[Value].bot)(Lattice[Value].⊔), σ0)
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

trait AbsLamCalTrait {
  import AbsLamCal._

  def specialize(p: Expr): DslDriver[Unit, Unit] =
  new RepAbsInterpDriver {
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val (v, s) = eval_top(p)
      println(v); println(s)
    }
  }

  def getAST(prog: String) = {
    LargeSchemeParser(prog) match {
      case Some(expr) => LargeSchemeASTDesugar(expr)
    }
  }

  def evalUnstaged(prog: Expr) = {
    AbsInterp.eval_top(prog)
  }

  def evalStaged(prog: Expr) = {
    val code = specialize(prog)
    code.precompile
    println("finished precompile")
    println(code.code)
    val writer = new java.io.PrintWriter(new java.io.File("CodeGen.scala.ignore"))
    writer.write(code.code)
    writer.close()
    code.eval(())
  }
}

object AbsLamCalTest extends AbsLamCalTrait {
  import sai.evaluation.TestPrograms._
  def main(args: Array[String]) {
    println("staged: " + evalStaged(euclid))
    println("unstaged: " + evalUnstaged(euclid))
  }
}
