package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import sai.lmsx._
import sai.utils.Utils.time

import sys.process._

import org.scalatest.FunSuite

import Config._

/* m: the parsed LLVM module
 * name: test name
 * f: entrance function name
 * config: compile time configuration
 * nPath: expected number of explored paths
 * nTest: expteted number of test cases generated
 * runOpt: the command line argument to run the compiled executable
 * result: expected return status of the compiled executable
 */
case class TestPrg(m: Module, name: String, f: String, config: Config, runOpt: Option[String], exp: Map[String, Any])
object TestPrg {
  val nPath = "nPath"
  val nTest = "nTest"
  val minPath = "minPath"
  val minTest = "minTest"
  val status = "status"
  def nPath(n: Int): Map[String, Any] = Map(nPath -> n)
  def nTest(n: Int): Map[String, Any] = Map(nTest -> n)
  def minTest(n: Int): Map[String, Any] = Map(minTest -> n)
  def minPath(n: Int): Map[String, Any] = Map(minPath -> n)
  def status(n: Int): Map[String, Any] = Map(status -> n)

  implicit def lift[T](t: T): Option[T] = Some(t)
}
import TestPrg._

object TestCases {
  val benchcases: List[TestPrg] = List(
    TestPrg(parseFile("benchmarks/perf-mon/knapsack.ll"), "knapsackTest", "@main", noArg, None, nPath(1666)),
    TestPrg(parseFile("benchmarks/perf-mon/nqueen.ll"), "nQueens", "@main", noArg, None, nPath(1363)),
  )
}
import TestCases._

abstract class TestLLSC extends FunSuite {
  case class TestResult(engine: String, testName: String, solverTime: Double,
    wholeTime: Double, blockCov: Double, pathNum: Int,
    brQueryNum: Int, testQueryNum: Int, cexCacheHit: Int)

  def parseOutput(engine: String, testName: String, output: String): TestResult = {
    // example:
    // [43.4s/46.0s] #blocks: 12/12; #paths: 1666; #threads: 1; #task-in-q: 0; #queries: 7328/1666 (1996)
    val lastLine = output.split("\n").last
    val groups = lastLine.split(" ")
    val (solverTime, wholeTime) = {
      val t = groups(0).drop(1).split("/")
      val solverTime = t(0).dropRight(1).toDouble
      val wholeTime = t(1).dropRight(2).toDouble
      (solverTime, wholeTime)
    }
    val blockCov = {
      val t = groups(2).dropRight(1).split("/")
      t(0).toDouble / t(1).toDouble
    }
    val pathNum = groups(4).dropRight(1).toInt
    val (brQueryNum, testQueryNum) = {
      val t = groups(10).split("/")
      (t(0).toInt, t(1).toInt)
    }
    val cexCacheHit = groups(11).drop(1).dropRight(1).toInt
    TestResult(engine, testName, solverTime, wholeTime, blockCov, pathNum, brQueryNum, testQueryNum, cexCacheHit)
  }

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArgOpt, exp) = tst
    test(name) {
      val code = llsc.newInstance(m, llsc.insName + "_" + name, f, config)
      code.genAll
      val mkRet = code.make(4)
      assert(mkRet == 0, "make failed")
      val (output, ret) = code.runWithStatus(cliArgOpt.getOrElse(""))
      System.err.println(output)
      val resStat = parseOutput(llsc.insName, name, output)
      System.err.println(resStat)
      if (exp.contains(status)) {
        assert(ret == exp(status), "Unexpected returned status")
      }
      if (exp.contains(nPath)) {
        assert(resStat.pathNum == exp(nPath), "Unexpected path number")
      }
      if (exp.contains(minPath)) {
        assert(resStat.pathNum >= exp(minPath).asInstanceOf[Int], "Unexpected number of least paths")
      }
      if (exp.contains(nTest)) {
        assert(resStat.testQueryNum == exp(nTest), "Unexpected number of test cases")
      }
      if (exp.contains(minTest)) {
        assert(resStat.testQueryNum >= exp(minTest).asInstanceOf[Int], "Unexpected number of least test cases")
      }
    }
  }

  def testLLSC(llsc: LLSC, tests: List[TestPrg]): Unit = tests.foreach(testLLSC(llsc, _))
}

class BenchPureLLSC extends TestLLSC {
  testLLSC(new PureLLSC, benchcases)
}
