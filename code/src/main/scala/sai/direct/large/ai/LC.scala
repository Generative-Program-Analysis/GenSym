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

object LamCal {
  trait Semantics {
    type R[+_]
    type Ident = String
    type Addr
    type Value
    type Env
    type Store
    type Ans = (R[Value], R[Store])
    def get(ρ: R[Env], x: Ident): R[Addr]
    def put(ρ: R[Env], x: Ident, a: R[Addr]): R[Env]
    def get(σ: R[Store], a: R[Addr]): R[Value]
    def put(σ: R[Store], a: R[Addr], v: R[Value]): R[Store]
    def alloc(σ: R[Store], x: Ident): R[Addr]
    def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value] //TODO: can we just call eval_top
    def sym(s: Sym): R[Value]
    def int(i: IntLit): R[Value]
    def bool(b: BoolLit): R[Value]
    def float(f: FloatLit): R[Value]
    def char(c: CharLit): R[Value]
    def apply_closure(ev: EvalFun)(f: R[Value], arg: R[Value], σ: R[Store]): Ans
    def branch0(cnd: R[Value], thn: => Ans, els: => Ans): Ans
    val ρ0: R[Env]; val σ0: R[Store]
    type EvalFun = (Expr, R[Env], R[Store]) => Ans
    def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ) => ev(fix(ev))(e, ρ, σ)
    def eval(ev: EvalFun)(e: Expr, ρ: R[Env], σ: R[Store]): Ans = e match {
      case IntLit(_) => (int(e), σ)
      case FloatLit(_) => (float(e), σ)
      case BoolLit(_) => (bool(e), σ)
      case CharLit(_) => (char(e), σ)
      case Sym(_) => (sym(e), σ)
      case Var(x) => (get(σ, get(ρ, x)), σ)
      case Lam(x, e) => (close(ev)(Lam(x, e), ρ), σ) // FIXME: List of args
      case App(e1, e2) => // FIXME: List of args
        val (e1v, e1σ) = ev(e1, ρ, σ)
        val (e2v, e2σ) = ev(e2, ρ, e1σ)
        apply_closure(ev)(e1v, e2v, e2σ)
      case If0(cnd, thn, els) => // FIXME:
        val (cndv, cndσ) = ev(cnd, ρ, σ)
        branch0(cndv, ev(thn, ρ, cndσ), ev(els, ρ, cndσ))
    }
    def eval_top(e: Expr): Ans = eval_top(e, ρ0, σ0)
    def eval_top(e: Expr, ρ: R[Env], σ: R[Store]): Ans = fix(eval)(e, ρ, σ)
  }

  trait Concrete extends Semantics {
    type Addr = Int
    sealed trait Value
    case class CloV(λ: Lam, ρ: Env) extends Value
    case class NumV(i: Int) extends Value
    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]
  }

  object ConcInterp extends Concrete {
    type R[+T] = T
    val ρ0: Env = Map[Ident,Addr]()
    val σ0: Store = Map[Addr,Value]()
    def get(ρ: Env, x: Ident): Addr = ρ(x)
    def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
    def get(σ: Store, a: Addr): Value = σ(a)
    def put(σ: Store, a: Addr, v: Value): Store = σ + (a → v)
    def alloc(σ: Store, x: Ident): Addr = σ.size + 1
    def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
    def num(i: Lit): Value = NumV(i.i)
    def apply_closure(ev: EvalFun)(f: Value, arg: Value, σ: Store): Ans = f match {
      case CloV(Lam(x, e), ρ) =>
        val α = alloc(σ, x)
        val ρ_* = put(ρ, x, α)
        val σ_* = put(σ, α, arg)
        ev(e, ρ_*, σ_*)
    }
    def branch0(cnd: Value, thn: => Ans, els: => Ans): Ans = cnd match {
      case NumV(i) => if (i == 0) thn else els
    }
  }

  trait LMSOps extends Dsl with MapOps with UncheckedOps with TupleOps with SetOps with TupledFunctions
  trait RepConcInterpOps extends Concrete with LMSOps {
    type R[+T] = Rep[T]
    implicit def valueTyp: Typ[Value]
    val ρ0: Rep[Env] = Map[Ident,Addr]()
    val σ0: Rep[Store] = Map[Addr,Value]()
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ(a)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = σ + (a → v)
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = σ.size + 1
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(x, e) = λ
      // TODO: runtime error when using pattern matching
      // scala.MatchError: UnboxedTuple(List(Sym(7), Sym(8))) (of class scala.lms.common.TupledFunctionsExp$UnboxedTuple)
      //val f: Rep[(Value,Store)]=>Rep[(Value,Store)] = {
      //  case (arg: Rep[Value], σ: Rep[Store]) =>
      val f: Rep[(Value,Store)]=>Rep[(Value,Store)] = (as: Rep[(Value, Store)]) => {
        val arg = as._1; val σ = as._2;
        val α = alloc(σ, x)
        ev(e, put(ρ, x, α), put(σ, α, arg))
      }
      unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
    }
    def num(i: Lit): Rep[Value] = {
      unchecked("NumV(", i.i, ")")
    }
    def branch0(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val i = unchecked[Int](cnd, ".asInstanceOf[NumV].i")
      (if (i == 0) thn else els).asInstanceOf[Rep[(Value,Store)]] //FIXME: Why?
    }
  }

  trait LMSOpsExp extends DslExp with MapOpsExp with UncheckedOpsExp with TupleOpsExp with SetOpsExp with TupledFunctionsRecursiveExp
  trait RepConcInterpOpsExp extends RepConcInterpOps with LMSOpsExp {
    implicit def valueTyp: Typ[Value] = manifestTyp
    case class ApplyClosure(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]) extends Def[(Value, Store)]
    def apply_closure(ev: EvalFun)(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Ans = {
      reflectEffect(ApplyClosure(f, arg, σ))
    }
  }

  trait RepConcSemGen extends GenericNestedCodegen {
    val IR: RepConcInterpOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      //case ApplyClosure(f, arg, σ) => emitValDef(sym, "apply_closure_norep(" + quote(f) + "," + quote(arg) + "," + quote(σ) + ")")
      case ApplyClosure(f, arg, σ) => emitValDef(sym, quote(f) + ".asInstanceOf[CompiledClo].f(" + quote(arg) + "," + quote(σ) + ")")
      case Struct(tag, elems) =>
        //TODO: merge back to LMS
        registerStruct(structName(sym.tp), elems)
        val typeName = sym.tp.runtimeClass.getSimpleName + "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
        emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
      case _ => super.emitNode(sym, rhs)
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

  trait RepConcSemDriver extends DslDriver[Unit, Unit] with RepConcInterpOpsExp { q =>
    override val codegen = new DslGen with ScalaGenMapOps with MyScalaGenTupleOps
      with RepConcSemGen with MyScalaGenTupledFunctions with ScalaGenUncheckedOps
      with ScalaGenSetOps {
      val IR: q.type = q
      override def remap[A](m: Typ[A]): String = {
        if (m.toString.endsWith("$Value")) "Value"
        else super.remap(m)
      }
      override def emitSource[A : Typ](args: List[Sym[_]], body: Block[A],
                                       className: String,
                                       stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
        val prelude = """
  import sai.direct.core.parser._
  object RTSupport {
    trait Value
    case class NumV(i: Int) extends Value
    case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
/*
    def apply_closure_norep(f: Value, arg: Value, σ: Map[Int,Value]) = f match {
      case CompiledClo(fun, λ, ρ) => fun(arg, σ)
    }
*/
  }
  import RTSupport._
        """
        stream.println(prelude)
        super.emitSource(args, body, className, stream)
      }
    }
  }
}

object SADI5 {
  import LamCal._
  def main(args: Array[String]) {
    def specialize(p: Expr): DslDriver[Unit, Unit] =
      new RepConcSemDriver {
        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val (v, s) = eval_top(p)
          println(v); println(s)
        }
      }
    val id4 = App(Lam("x", App(App(Var("x"), Var("x")), Var("x"))), Lam("y", Var("y")))
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))

    val code = specialize(fact5)
    println(code.code)
    code.eval(())
    //println(ConcInterp.eval_top(id4))
    println(ConcInterp.eval_top(fact5))
  }
}
