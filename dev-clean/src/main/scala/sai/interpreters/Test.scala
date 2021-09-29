package sai

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lang.FunLang._
import sai.lmsx._

object mainGeneric {
  import sai.lang.FunLang.Examples._

  def specializeAbs(e: Expr): SAIDriver[Unit, Unit] = new StagedAbstractDriver {
    @virtualize
    def snippet(u: Rep[Unit]) = {
      val vs = run(e)
      println(vs._1)
      println(vs._2.size)
    }
  }

  def specializeConc(e: Expr): SAIDriver[Unit, Unit] = new StagedConcreteDriver {
    @virtualize
    def snippet(u: Rep[Unit]) = {
      val vs = run(e)
      println(vs)
    }
  }

  def testStagedConcrete() = {
    val code = specializeConc(fact5)
    println(code.code)
    code.eval(())
  }

  def testStagedAbstract() = {
    val code = specializeAbs(fact5)
    println(code.code)
    code.eval(())
  }

  def testConcrete() = {
    val interpreter = new ConcreteSemantics {}
    val res = interpreter.run(fact5)
    println(res)
  }

  def testAbstract() = {
    val interpreter = new AbstractSemantics {
      def mCache: Manifest[Cache] = manifest[Cache]
    }
    val res = interpreter.run(fact5)
    //val res = interpreter.run(ifif)
    //res = interpreter.run(simpleif)
    //println(AbsInterpreter.run(fact5))
    println(res._1)
  }

  def toString(i: Int): String = i.toString
  def toDouble(i: Int): Double = i.toDouble

  def combine_fix[A, B, C](f: A => B, g: A => C)(ev: (A => (B, C)) => (A => (B, C)) => A => (B, C)): A => (B, C) =
    a => ev(a => (f(a), g(a)))(combine_fix(f, g)(ev))(a)

  def main(args: Array[String]): Unit = {
    val f = combine_fix(toString, toDouble) {
      eval_base => eval_rec => {
        case n if n > 0 => eval_rec(n-1)
        case 0 => eval_base(0)
      } }
    println(f(5))
    println(f(0))
    /*
    args(0) match {
      case "concrete" => testConcrete()
      case "staged-concrete" => testStagedConcrete()
      case "abstract" => testAbstract()
      case "staged-abstract" => testStagedAbstract()
    }
    */
  }

}
