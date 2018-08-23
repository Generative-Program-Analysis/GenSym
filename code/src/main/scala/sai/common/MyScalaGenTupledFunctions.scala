package sai.common

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

trait MyScalaGenTupledFunctions extends ScalaGenFunctions with GenericGenUnboxedTupleAccess {
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

  def unwrapTupleStr[A](m: Typ[A]): String = {
    val s = m.toString
    if (s.startsWith("scala.Tuple")) {
      //s.slice(s.indexOf("[")+1, s.length-1).filter(c => c != ' ').split(",")
      s.slice(s.indexOf("[")+1, s.length-1).filter(c => c != ' ')//.split(",")
    } else {
      remap(m)
    }
  }

  def topLevelTupleSize[A](m: Typ[A]): Int = {
    val s = m.toString
    if (s.startsWith("scala.Tuple")) {
      s.slice(s.indexOf("scala.Tuple")+"scala.Tuple".size, s.indexOf("[")).toInt
    }
    else 1
  }

  override def remap[A](m: Typ[A]): String = m.toString match {
    case f if f.startsWith("scala.Function") =>
      val targs = m.typeArguments.dropRight(1)
      val res = remap(m.typeArguments.last)
      //val targsUnboxed = targs.flatMap(t => unwrapTupleStr(t))
      val targsUnboxed = targs.map(t => unwrapTupleStr(t))
      val size = targs.map(s => topLevelTupleSize(s)).foldLeft(0)(_ + _)
      //println(s"targsUnboxed.length: ${targsUnboxed.length}")
      //println(s"targsUnboxed: ${targsUnboxed.mkString(",")}")
      val sep = if (targsUnboxed.length > 0) "," else ""
      //"scala.Function" + (targsUnboxed.length) + "[" + targsUnboxed.mkString(",") + sep + res + "]"
      s"scala.Function${size}[" + targsUnboxed.mkString(",") + sep + res + "]"

    case _ => super.remap(m)
  }
}
