package gensym
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait ScalaCodeGen_LiftIf extends ExtendedScalaCodeGen {
  override def shallow(n: Node): Unit = n match {
    case n @ Node(f, "?", List(c, thn: Block, els: Block), _) =>
      emit(s"def ${f}_then() =")
      quoteBlock(traverse(thn))
      emit("\n")
      emit(s"def ${f}_else() = ")
      quoteBlock(traverse(els))
      emit("\n")

      emit("if ("); shallow(c); emit(") ")
      emit(s"${f}_then()")
      emit(" else ")
      emitln(s"${f}_else()")
    case _ => super.shallow(n)
  }
}
