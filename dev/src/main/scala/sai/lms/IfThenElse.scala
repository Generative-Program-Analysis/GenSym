package sai
package lms

import java.io.PrintWriter
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import scala.virtualization.lms.common.{ScalaGenIfThenElse => _, _}
import scala.virtualization.lms.internal.{GenericNestedCodegen, GenericFatCodegen, GenerationFailedException}

trait SAI_ScalaGenIfThenElse extends ScalaGenEffect with BaseGenIfThenElse {
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IfThenElse(c,a,b) =>
      val if_sym = quote(sym)
      val then_sym = if_sym + "_then"
      val else_sym = if_sym + "_else"
      val obj_sym = if_sym + "_obj"

      //stream.println("object " + obj_sym + " {")
        stream.println("def " + then_sym + "() = {")
        emitBlock(a)
        stream.println(quote(getBlockResult(a)))
        stream.println("}")

        stream.println("def " + else_sym + "() = {")
        emitBlock(b)
        stream.println(quote(getBlockResult(b)))
        stream.println("}")
      //stream.println("}")

      stream.print("val " + if_sym + " = if (" + quote(c) + ") ")
      //stream.print(obj_sym + "." + then_sym + "()")
      stream.print(then_sym + "()")
      stream.print(" else ")
      //stream.println(obj_sym + "." + else_sym + "()")
      stream.println(else_sym + "()")
    case _ => super.emitNode(sym, rhs)
  }
}
