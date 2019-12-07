package sai

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SAICodeGenBase extends ExtendedScalaCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "λ", _, _) ⇒ false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }
}
