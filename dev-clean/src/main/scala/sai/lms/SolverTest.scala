package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

abstract class SATSnippet[A:Manifest, C:Manifest] extends StagedSATOps {
  def wrapper(x: Rep[A]): Rep[C] = snippet(x)
  def snippet(x: Rep[A]): Rep[C]
}

abstract class CppSATDriver[A: Manifest, C: Manifest] extends SATSnippet[A, C] with StagedSATOps { q =>
  val codegen = new CGenBase with CppSAICodeGenBase {
    val IR: q.type = q
    import IR._
  }

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource[A, C](wrapper, "Snippet", new java.io.PrintStream(source))
    (source.toString, statics)
  }

}

object SATTest extends App {
  def test(): CppSATDriver[Unit, Unit] = new CppSATDriver[Unit, Unit] {
    def snippet(x: Rep[Unit]) = {
      import Syntax._
      val x = variable("x")
      val y = variable("y")
      assert(x ⇔ y)
      assert(x ≡ true)
      assert(! x)
    }
  }
  val code = test()
  print(code.code)
}

