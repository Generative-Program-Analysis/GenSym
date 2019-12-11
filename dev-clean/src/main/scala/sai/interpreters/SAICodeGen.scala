package sai
package lmsx

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SAICodeGenBase extends ExtendedScalaCodeGen {
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
}
