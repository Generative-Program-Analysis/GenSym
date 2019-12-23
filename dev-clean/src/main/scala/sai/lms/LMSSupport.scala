package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import sai.lattices._
import sai.structure.monad._

// Scala code generator and driver

trait SAICodeGenBase extends ExtendedScalaCodeGen
    with ScalaCodeGen_List
    with ScalaCodeGen_Map with ScalaCodeGen_Tuple
    with ScalaCodeGen_Set // with ScalaCodeGen_LiftIf
{
  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Function2") {
      val fst = m.typeArguments(0)
      val snd = m.typeArguments(1)
      val ret = m.typeArguments(2)
      s"Function2[${remap(fst)}, ${remap(snd)}, ${remap(ret)}]"
    } else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "λ", _, _) ⇒ false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::(Backend.Const("val"))::Nil, _) =>
      super.traverse(n)
      emitln(s"val ${quote(f)}_val = ${quote(f)} _")
      /*
      val types = b.in.map { a => remap(typeMap.getOrElse(a, manifest[Unknown])) }
      val typesStr = types.map(_.toString).mkString(", ")
      val retType = remap(typeMap.getOrElse(b.res, manifest[Unit]))
      val eff = quoteEff(b.ein)
      //emitln(s"var ${quote(f)} : (($typesStr) => $retType $eff) = null.asInstanceOf[(($typesStr) => $retType)]")
      emit(s"val ${quote(f)} : (($typesStr) => $retType $eff) = ")
      val pattern = PTuple(b.in.map(PVar(_)))
      quoteCaseBlock(b, pattern)
      emitln()
      */
    case _ => super.traverse(n)
  }
}

trait SAIOps extends Base with PrimitiveOps with LiftPrimitives with Equal
    with OrderingOps with LiftVariables
    with TupleOpsOpt with ListOpsOpt with MapOpsOpt with SetOpsOpt
    with RepLattices with RepMonads {
  type Typ[T] = Manifest[T]
  def typ[T: Typ] = manifest[T]
  def manifestTyp[T: Typ] = manifest[T]

  override def __fun[T: Manifest](f: AnyRef, arity: Int, gf: List[Backend.Exp] => Backend.Exp): Backend.Exp = {
    val can = canonicalize(f)
    Adapter.funTable.find(_._2 == can) match {
      case Some((funSym, _)) => funSym
      case _ =>
        val fn = Backend.Sym(Adapter.g.fresh)
        Adapter.funTable = (fn, can) :: Adapter.funTable
        val block = Adapter.g.reify(arity, gf)
        val res = Adapter.g.reflect(fn, "λ", block)(hardSummary(fn))
        Adapter.funTable = Adapter.funTable.map {
          case (fn2, can2) => if (can == can2) (fn, can) else (fn2, can2)
        }
        res
    }
  }
}

abstract class SAISnippet[A:Manifest, B:Manifest] extends SAIOps {
  def wrapper(x: Rep[A]): Rep[B] = snippet(x)
  def snippet(x: Rep[A]): Rep[B]
}

abstract class SAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIOps { q =>
  val codegen = new ScalaGenBase {
    val IR: q.type = q
    import IR._
  }

  val prelude: String = ""

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    source.write(prelude.getBytes)
    val statics = codegen.emitSource(wrapper, "Snippet",
      new java.io.PrintStream(source))(manifestTyp[A], manifestTyp[B])
    (source.toString, statics)
  }

  lazy val f = {
    val (c1, s1) = (code, statics)
    time("scalac") { Global.sc.compile[A, B]("Snippet", c1, s1) }
  }

  def precompile: Unit = f
  def precompileSilently: Unit = utils.devnull(f)
  def eval(x: A): B = {
    val f1 = f
    time("eval")(f1(x))
  }
}

// C++ code generator and driver
/*
trait SAICXXOps

trait SAICXXCodeGen extends CGenBase

abstract class SAICXXDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAICXXOps { q =>

}
 */
