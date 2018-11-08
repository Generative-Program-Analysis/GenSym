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

trait Symantics {
  type R[+_]
  type Ident = String
  type Addr
  sealed trait Value
  type Env
  type Store
  type Ans = (R[Value], R[Store])
  def get(ρ: R[Env], x: Ident): R[Addr]
  def put(ρ: R[Env], x: Ident, a: R[Addr]): R[Env]
  def get(σ: R[Store], a: R[Addr]): R[Value]
  def put(σ: R[Store], a: R[Addr], v: R[Value]): R[Store]
  def alloc(σ: R[Store], x: Ident): R[Addr]
  def close(λ: Lam, ρ: R[Env]): R[Value]
  def apply_closure(f: R[Value], arg: R[Value], σ: R[Store]): Ans
  def eval(e: Expr, ρ: R[Env], σ: R[Store]): Ans = e match {
    case Var(x) => (get(σ, get(ρ, x)), σ)
    case Lam(x, e) => (close(Lam(x, e), ρ), σ)
    case App(e1, e2) =>
      val (e1v, e1σ) = eval(e1, ρ, σ)
      val (e2v, e2σ) = eval(e2, ρ, e1σ)
      apply_closure(e1v, e2v, e2σ)
  }
}

object ConcSym extends Symantics {
  type R[+T] = T
  type Addr = Int
  case class CloV(λ: Lam, ρ: Env) extends Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  def get(ρ: Env, x: Ident): Addr = ρ(x)
  def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
  def get(σ: Store, a: Addr): Value = σ(a)
  def put(σ: Store, a: Addr, v: Value): Store = σ + (a → v)
  def alloc(σ: Store, x: Ident): Addr = σ.size + 1
  def close(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def apply_closure(f: Value, arg: Value, σ: Store): Ans = f match {
    case CloV(Lam(x, e), ρ) =>
      val α = alloc(σ, x)
      val ρ_* = put(ρ, x, α)
      val σ_* = put(σ, α, arg)
      eval(e, ρ_*, σ_*)
  }
}

trait RepConcSymOps extends Symantics with Dsl with MapOps with UncheckedOps with TupleOps {
  type R[+T] = Rep[T]
  type Addr = Int
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  implicit def valueTyp: Typ[Value]
  def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
  def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
  def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ(a)
  def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = σ + (a → v)
  def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = σ.size + 1
  def close(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: Rep[(Value,Store)]=>Rep[(Value,Store)] = (as: Rep[(Value, Store)]) => {
      val arg = as._1; val σ = as._2
      val α = alloc(σ, x)
      eval(e, put(ρ, x, α), put(σ, α, arg))
    }
    unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
  }
}

trait RepConcSymExp extends RepConcSymOps
    with DslExp with MapOpsExp with UncheckedOpsExp with TupleOpsExp {
  implicit def valueTyp: Typ[Value] = manifestTyp
  case class ApplyClosure(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]) extends Def[(Value, Store)]
  def apply_closure(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Ans = {
    reflectEffect(ApplyClosure(f, arg, σ))
  }
}

trait RepConcSymGen extends GenericNestedCodegen {
  val IR: RepConcSymExp
  import IR._
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ApplyClosure(f, arg, σ) => emitValDef(sym, "apply_closure_norep(" + quote(f) + "," + quote(arg) + "," + quote(σ) + ")")
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
    case "Tuple3" => IR.structName(m)
    case "Tuple4" => IR.structName(m)
    case "Tuple5" => IR.structName(m)
    case _ => super.remap(m)
  }
}

trait RepConcSymDriver extends DslDriver[Unit, Unit] with RepConcSymExp { q =>
  override val codegen = new DslGen with ScalaGenMapOps with MyScalaGenTupleOps with ScalaGenUncheckedOps with RepConcSymGen with MyScalaGenTupledFunctions {
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
  case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
  def apply_closure_norep(f: Value, arg: Value, σ: Map[Int,Value]) = f match {
    case CompiledClo(fun, λ, ρ) => fun(arg, σ)
  }
}
import RTSupport._
      """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}

object SADI5 {
  def main(args: Array[String]) {
    def specialize(p: Expr): DslDriver[Unit, Unit] =
      new RepConcSymDriver {
        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val (v, s) = eval(p, Map[String,Addr](), Map[Addr,Value]())
          println(v); println(s)
        }
      }
    val id4 = App(Lam("x", App(App(Var("x"), Var("x")), Var("x"))), Lam("y", Var("y")))
    println(ConcSym.eval(id4, Map(), Map()))

    val code = specialize(id4)
    println(code.code)
    code.eval(())
  }
}
