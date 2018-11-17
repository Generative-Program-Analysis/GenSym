package sai.common

import java.io.PrintWriter
import scala.reflect.SourceContext
import scala.lms.internal.{GenericNestedCodegen, GenericFatCodegen, GenerationFailedException}
import scala.lms.common.{ScalaGenIfThenElse => _, _}

trait MyScalaGenIfThenElse extends ScalaGenEffect with BaseGenIfThenElse {
  import IR._
 
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IfThenElse(c,a,b) =>
      val if_sym = quote(sym)
      val then_sym = if_sym + "_then"
      val else_sym = if_sym + "_else"

      stream.println("def " + then_sym + "() = {")
      emitBlock(a)
      stream.println(quote(getBlockResult(a)))
      stream.println("}")

      stream.println("def " + else_sym + "() = {")
      emitBlock(b)
      stream.println(quote(getBlockResult(b)))
      stream.println("}")

      stream.println("val " + if_sym + " = if (" + quote(c) + ") {")
      //emitBlock(a)
      //stream.println(quote(getBlockResult(a)))
      stream.println(then_sym + "()")
      stream.println("} else {")
      //emitBlock(b)
      //stream.println(quote(getBlockResult(b)))
      stream.println(else_sym + "()")
      stream.println("}")
    case _ => super.emitNode(sym, rhs)
  }
}
