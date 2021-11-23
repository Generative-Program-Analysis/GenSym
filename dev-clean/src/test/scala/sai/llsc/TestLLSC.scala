package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.utils.Utils.time
import scala.collection.immutable.{List => StaticList}
import scala.collection.mutable.HashMap

import sai.llsc.imp.ImpLLSCEngine
import sai.llsc.imp.CPSLLSCEngine

import sys.process._

import org.scalatest.FunSuite

class TestPureLLSC extends FunSuite {
  import java.io.File
  import sai.llvm.Benchmarks._

  case class TestPrg(m: Module, name: String, f: String, nSym: Int, path: Int)
  val tests: List[TestPrg] = List(
    TestPrg(add, "addTest", "@main", 0, 1),
  )

  val pureLLSC = new PureLLSC

  tests.foreach { case TestPrg(m, name, f, nSym, path) =>
    test(name) {
      val code = pureLLSC.newInstance(m, name, f, nSym)
      code.genAll
      val mkRet = code.make
      assert(mkRet == 0, "make failed")
      val ret = code.run
      assert(ret == path, "Unexpected path number")
      // TODO: check the number of generated test files?
    }
  }

}
