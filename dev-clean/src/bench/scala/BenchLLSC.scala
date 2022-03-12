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

// TODO: refactor and max share with test/...
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
  val prefix = "benchmarks/perf-mon"
  val benchcases: List[TestPrg] = List(
    TestPrg(parseFile(s"$prefix/knapsack.ll"), "knapsackTest", "@main", noArg, None, nPath(1666)),
    TestPrg(parseFile(s"$prefix/nqueen.ll"), "nQueens", "@main", noArg, None, nPath(1363)),
    TestPrg(parseFile(s"$prefix/kmpmatcher.ll"), "kmp", "@main", noArg, None, nPath(1287)),
    // These benchmarks have a larger input size compared with those in demo_benchmarks
    TestPrg(parseFile(s"$prefix/mergesort.ll"), "mergeSortTest", "@main", noArg, None, nPath(5040)),
    TestPrg(parseFile(s"$prefix/bubblesort.ll"), "bubbleSortTest", "@main", noArg, None, nPath(720)),
    TestPrg(parseFile(s"$prefix/quicksort.ll"), "quickSortTest", "@main", noArg, None, nPath(720)),
    TestPrg(parseFile(s"$prefix/multipath_1048576_sym.ll"), "mp1m", "@f", symArg(20), "--disable-solver", nPath(1048576)),
  )
  val paraBenchcases: List[TestPrg] = List(2, 4, 8, 16).flatMap { case tn => 
    List(
      TestPrg(parseFile(s"$prefix/knapsack.ll"), s"par${tn}_knapsackTest", "@main", noArg, s"--thread=$tn", nPath(1666)),
      TestPrg(parseFile(s"$prefix/nqueen.ll"), s"par${tn}_nQueens", "@main", noArg, s"--thread=$tn", nPath(1363)),
      TestPrg(parseFile(s"$prefix/kmpmatcher.ll"), s"par${tn}_kmp", "@main", noArg, s"--thread=$tn", nPath(1287)),
      TestPrg(parseFile(s"$prefix/mergesort.ll"), s"par${tn}_mergeSortTest", "@main", noArg, s"--thread=$tn", nPath(5040)),
      TestPrg(parseFile(s"$prefix/bubblesort.ll"), s"par${tn}_bubbleSortTest", "@main", noArg, s"--thread=$tn", nPath(720)),
      TestPrg(parseFile(s"$prefix/quicksort.ll"), s"par${tn}_quickSortTest", "@main", noArg, s"--thread=$tn", nPath(720)),
      TestPrg(parseFile(s"$prefix/multipath_1048576_sym.ll"), s"par${tn}_mp1m", "@f", symArg(20), s"--disable-solver --thread=$tn", nPath(1048576)),
    )
  }
}
import TestCases._

abstract class TestLLSC extends FunSuite {
  import java.time.LocalDateTime

  case class TestResult(time: LocalDateTime, commit: String, engine: String, testName: String,
    solverTime: Double, wholeTime: Double, blockCov: Double,
    pathNum: Int, brQueryNum: Int, testQueryNum: Int, cexCacheHit: Int) {
    override def toString() =
      s"$time,$commit,$engine,$testName,$solverTime,$wholeTime,$blockCov,$pathNum,$brQueryNum,$testQueryNum,$cexCacheHit"
  }

  val gitCommit = Process("git rev-parse --short HEAD").!!.trim

  def parseOutput(engine: String, testName: String, output: String): TestResult = {
    val pattern = raw"\[([^s]+)s/([^s]+)s\] #blocks: (\d+)/(\d+); #paths: (\d+); .+; #queries: (\d+)/(\d+) \((\d+)\)".r
    output.split("\n").last match {
      case pattern(solverTime, wholeTime, blockCnt, blockAll, pathNum, brQuerynum, testQueryNum, cexCacheHit) =>
        TestResult(LocalDateTime.now(), gitCommit, engine, testName,
                   solverTime.toDouble, wholeTime.toDouble, blockCnt.toDouble / blockAll.toDouble,
                   pathNum.toInt, brQuerynum.toInt, testQueryNum.toInt, cexCacheHit.toInt)
    }
  }

  def checkResult(resStat: TestResult, ret: Int, exp: Map[String, Any]) = {
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
    import java.io.{File, FileWriter}
    val writer = new FileWriter(new File("bench.csv"), true)
    writer.append(s"$resStat\n")
    writer.close()
  }

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val nTest = 5
    val TestPrg(m, name, f, config, cliArgOpt, exp) = tst
    test(name) {
      val code = llsc.newInstance(m, llsc.insName + "_" + name, f, config)
      code.genAll
      val mkRet = code.make(4)
      assert(mkRet == 0, "make failed")
      for (i <- 1 to nTest) {
        Thread.sleep(1 * 1000)
        val numactl = "numactl -N1 -m1"
        val (output, ret) = code.runWithStatus(cliArgOpt.getOrElse(""), numactl)
        val resStat = parseOutput(llsc.insName, name, output)
        System.out.println(resStat)
        checkResult(resStat, ret, exp)
      }
    }
  }

  def testLLSC(llsc: LLSC, tests: List[TestPrg]): Unit = tests.foreach(testLLSC(llsc, _))
}

trait LinkSTP extends LLSC {
  abstract override def newInstance(m: Module, name: String, fname: String, config: Config) = {
    val llsc = super.newInstance(m, name, fname, config)
    llsc.codegen.registerIncludePath("../third-party/stp/build/include")
    llsc.codegen.registerLibraryPath("../third-party/stp/build/lib")
    llsc
  }
}

trait LinkZ3 extends LLSC {
  abstract override def newInstance(m: Module, name: String, fname: String, config: Config) = {
    val llsc = super.newInstance(m, name, fname, config)
    llsc.codegen.registerIncludePath("../third-party/z3/src/api")
    llsc.codegen.registerIncludePath("../third-party/z3/src/api/c++")
    llsc.codegen.registerLibraryPath("../third-party/z3/build")
    llsc
  }
}

class BenchPureLLSC extends TestLLSC {
  testLLSC(new PureLLSC with LinkSTP, benchcases)
}

class BenchPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC with LinkSTP, benchcases)
}

class BenchPureCPSLLSCZ3 extends TestLLSC {
  testLLSC(new PureCPSLLSC_Z3 with LinkZ3, benchcases ++ paraBenchcases)
}
