package sai

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import FunLang._
import sai.lmsx._

object mainGeneric {
  import FunLang.Examples._

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
    //val res = interpreter.run(fact5)
    val res = interpreter.run(ifif)
    //res = interpreter.run(simpleif)
    //println(AbsInterpreter.run(fact5))
    println(res._1)
  }

  def main(args: Array[String]): Unit = { 
    args(0) match {
      case "concrete" => testConcrete()
      case "staged-concrete" => testStagedConcrete()
      case "abstract" => testAbstract()
      case "staged-abstract" => testStagedAbstract()
    }   
  }
}
