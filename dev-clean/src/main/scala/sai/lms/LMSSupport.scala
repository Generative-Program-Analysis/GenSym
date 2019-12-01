package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait SAIOps extends Base with PrimitiveOps with LiftPrimitives with Equal
    with OrderingOps with LiftVariables
    with TupleOps with ListOps with MapOps with SetOps {
  type Typ[T] = Manifest[T]
  def typ[T: Typ] = manifest[T]
  def manifestTyp[T: Typ] = manifest[T]
}

trait SAIDslImpl extends SAIOps { q =>
  val codegen = new ScalaGenBase
      with ScalaCodeGen_List with ScalaCodeGen_Map
      with ScalaCodeGen_Tuple with ScalaCodeGen_Set {
    val IR: q.type = q
    import IR._
  }
}

abstract class SAISnippet[A:Manifest, B:Manifest] extends SAIOps {
  def wrapper(x: Rep[A]): Rep[B] = snippet(x)
  def snippet(x: Rep[A]): Rep[B]
}

abstract class SAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIDslImpl {
  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
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
