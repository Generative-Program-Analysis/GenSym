package gensym
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import gensym.structure.lattices._
import gensym.structure.monad._

// Scala code generator and driver

trait SAICodeGenBase extends ExtendedScalaCodeGen
    with ScalaCodeGen_List
    with ScalaCodeGen_Map with ScalaCodeGen_Tuple
    with ScalaCodeGen_Set with ScalaCodeGen_Either
    // with ScalaCodeGen_LiftIf
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
    case Node(s, "?", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::(Backend.Const("val"))::Nil, _) =>
      /* Note: this case generates a function with "def" declaration first,
       * and then a "val" definition that eta-expands the "def" definition.
       * The later one can be used as function values when storing it to anywhere else.
       */
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

abstract class SAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIOps { q =>
  val codegen = new ScalaGenBase with SAICodeGenBase {
    val IR: q.type = q
    import IR._
  }

  val prelude: String = ""

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    source.write(prelude.getBytes)
    val statics = codegen.emitSource(wrapper, "Snippet",
      new java.io.PrintStream(source))(manifest[A], manifest[B])
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
