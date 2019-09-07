package sai
package lms

import scala.virtualization.lms.common._
import scala.virtualization.lms.internal.GenericNestedCodegen
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

/* Fixed code generator for tupled functions */

trait SAI_ScalaGenTupledFunctions extends ScalaGenFunctions with GenericGenUnboxedTupleAccess {
  val IR: TupledFunctionsExp
  import IR._

  override def quote(x: Exp[Any]) : String = x match {
    case UnboxedTuple(t) => t.map(quote).mkString("((", ",", "))")
    case _ => super.quote(x)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case Lambda(fun, UnboxedTuple(xs), y) =>
      emitValDef(sym, "{" + xs.map(s=>quote(s)+":"+remap(s.tp)).mkString("(",",",")") + " => ")
      emitBlock(y)
      stream.println(quote(getBlockResult(y)) + ": " + remap(y.tp))
      stream.println("}")

    case Apply(fun, UnboxedTuple(args)) =>
      emitValDef(sym, quote(fun) + args.map(quote).mkString("(", ",", ")"))

    case _ => super.emitNode(sym,rhs)
  }

  def unwrapTupleStr[A](m: Manifest[A]): String = {
    val s = m.toString
    if (s.startsWith("scala.Tuple")) {
      //System.err.println(m.typeArguments.map(a => remap(a)).mkString(","))
      //System.err.println(s.slice(s.indexOf("[")+1, s.length-1).filter(c => c != ' '))
      //s.slice(s.indexOf("[")+1, s.length-1).filter(c => c != ' ')//.split(",")
      m.typeArguments.map(a => remap(a)).mkString(",")
    } else {
      remap(m)
    }
  }

  def topLevelTupleSize[A](m: Manifest[A]): Int = {
    val s = m.toString
    if (s.startsWith("scala.Tuple")) {
      s.slice(s.indexOf("scala.Tuple")+"scala.Tuple".size, s.indexOf("[")).toInt
    }
    else 1
  }

  override def remap[A](m: Manifest[A]): String = m.toString match {
    case f if f.startsWith("scala.Function") =>
      val targs = m.typeArguments.dropRight(1)
      val res = remap(m.typeArguments.last)
      //val targsUnboxed = targs.flatMap(t => unwrapTupleStr(t))
      val targsUnboxed = targs.map(t => unwrapTupleStr(t))
      if (targsUnboxed.head == "Unit") { //Note: GW hacks here
        val size = targs.map(s => topLevelTupleSize(s)).foldLeft(0)(_ + _)
        val sep = if (targsUnboxed.tail.length > 0) "," else ""
        s"scala.Function${size-1}[" + targsUnboxed.tail.mkString(",") + sep + res + "]"
      } else {
        val size = targs.map(s => topLevelTupleSize(s)).foldLeft(0)(_ + _)
        //println(s"targsUnboxed.length: ${targsUnboxed.length}")
        //println(s"targsUnboxed: ${targsUnboxed.mkString(",")}")
        val sep = if (targsUnboxed.length > 0) "," else ""
        //"scala.Function" + (targsUnboxed.length) + "[" + targsUnboxed.mkString(",") + sep + res + "]"
        s"scala.Function${size}[" + targsUnboxed.mkString(",") + sep + res + "]"
      }
    case _ => super.remap(m)
  }
}
