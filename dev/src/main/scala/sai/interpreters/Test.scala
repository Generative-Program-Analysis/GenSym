package sai

import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext
import sai.lms._
import FunLang._

object MainGeneric {
  import FunLang.Examples._

  def specCon(e: Expr): DslDriver[Unit, Unit] = new StagedConcreteSemanticsDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vs = run(e)
      println(vs)
    }
  }

  def specAbs(e: Expr): DslDriver[Unit, Unit] = new StagedAbstractSemanticsDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vsc = run(e)
      println(vsc._1)
      println(vsc._2.size)
    }
  }

  def testConcrete() = {
    val interpreter = new ConcreteSemantics {}
    val res = interpreter.run(fact5)
    println(res)
  }

  def testStagedConcrete() = {
    val code = specCon(fact5)
    println(code.code)
    code.eval(())
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

  def testStagedAbstract() = {
    val code = specAbs(fact5)
    println(code.code)
    code.eval(())
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
