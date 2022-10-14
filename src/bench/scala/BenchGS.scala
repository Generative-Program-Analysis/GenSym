package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.parser.Parser._

import gensym.lmsx._
import sai.utils.Utils.time

import sys.process._

import org.scalatest.FunSuite

import Config._
import TestPrg._

object TestCases {
  val prefix = "benchmarks/perf-mon"
  val benchcases: List[TestPrg] = List(
    TestPrg(parseFile(s"$prefix/knapsack.ll"), "knapsackTest", "@main", noArg, noOpt, nPath(1666)),
    TestPrg(parseFile(s"$prefix/nqueen.ll"), "nQueens", "@main", noArg, noOpt, nPath(1363)),
    TestPrg(parseFile(s"$prefix/kmpmatcher.ll"), "kmp", "@main", noArg, noOpt, nPath(1287)),
    // These benchmarks have a larger input size compared with those in demo-benchmarks
    TestPrg(parseFile(s"$prefix/mergesort.ll"), "mergeSortTest", "@main", noArg, noOpt, nPath(5040)),
    TestPrg(parseFile(s"$prefix/bubblesort.ll"), "bubbleSortTest", "@main", noArg, noOpt, nPath(720)),
    TestPrg(parseFile(s"$prefix/quicksort.ll"), "quickSortTest", "@main", noArg, noOpt, nPath(720)),
    TestPrg(parseFile(s"$prefix/multipath_1048576_sym.ll"), "mp1m", "@f", symArg(20), "--solver=disable", nPath(1048576)),
  )
}
import TestCases._

abstract class TestGS extends FunSuite {
  import java.time.LocalDateTime

  case class TestResult(time: LocalDateTime, commit: String, engine: String, testName: String,
    extSolverTime: Double, intSolverTime: Double, wholeTime: Double, blockCov: Double,
    partialBrCov: Double, fullBrCov: Double, pathNum: Int, brQueryNum: Int,
    testQueryNum: Int, cexCacheHit: Int) {
    override def toString() =
      s"$time,$commit,$engine,$testName,$extSolverTime,$intSolverTime,$wholeTime,$partialBrCov,$fullBrCov,$blockCov,$pathNum,$brQueryNum,$testQueryNum,$cexCacheHit"
  }

  val gitCommit = Process("git rev-parse --short HEAD").!!.trim

  def parseOutput(engine: String, testName: String, output: String): TestResult = {
    val pattern = raw"\[([^s]+)s/([^s]+)s/([^s]+)s/([^s]+)s\] #blocks: (\d+)/(\d+); #br: (\d+)/(\d+)/(\d+); #paths: (\d+); .+; #queries: (\d+)/(\d+) \((\d+)\)".r
    output.split("\n").last match {
      case pattern(extSolverTime, intSolverTime, _/*fsTime ignored*/, wholeTime, blockCnt, blockAll,
        partialBr, fullBr, totalBr, pathNum, brQuerynum, testQueryNum, cexCacheHit) =>
        TestResult(LocalDateTime.now(), gitCommit, engine, testName,
          extSolverTime.toDouble, intSolverTime.toDouble, wholeTime.toDouble,
          blockCnt.toDouble/blockAll.toDouble, partialBr.toDouble/totalBr.toDouble,
          fullBr.toDouble/totalBr.toDouble, pathNum.toInt, brQuerynum.toInt,
          testQueryNum.toInt, cexCacheHit.toInt)
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

  def testGS(gs: GenSym, tst: TestPrg, solver: Option[String], threading: Boolean): Unit = {
    val nTest = 5
    val TestPrg(m, name, f, config, cliArg, exp, runCode) = tst
    test(name) {
      val code = gs.run(m, gs.insName + "_" + name, f, config)
      val mkRet = code.make(4)
      val (cliArg1, insName2) = solver match {
        case Some(x) =>
          val insName1 = s"${gs.insName}_${x.toUpperCase()}"
          if (!cliArg.exists(_.startsWith("--solver=")))
            (cliArg ++ Seq(s"--solver=$x"), insName1)
          else
            (cliArg, insName1)
        case None => (cliArg, gs.insName)
      }
      assert(mkRet == 0, "make failed")
      for (th <- if (threading) Seq(1, 2, 4, 8, 16) else Seq(1)) {
        val (cliArg2, name2) =
          if (th > 1)
            (cliArg1 ++ Seq(s"--thread=$th"), s"par${th}_${name}")
          else
            (cliArg1, name)
        for (i <- 1 to nTest) {
          Thread.sleep(1 * 1000)
          val numactl = "numactl --cpunodebind 1 --preferred 1"
          val (output, ret) = code.runWithStatus(cliArg2, numactl)
          val resStat = parseOutput(insName2, name2, output)
          System.out.println(resStat)
          checkResult(resStat, ret, exp)
        }
      }
    }
  }

  def testGS(gs: GenSym, tests: List[TestPrg], solver: Option[String] = None, threading: Boolean = false): Unit =
    tests.foreach(testGS(gs, _, solver, threading))
}

trait LinkSTP extends GenSym {
  abstract override def newInstance(m: Module, name: String, fname: String, config: Config) = {
    val gs = super.newInstance(m, name, fname, config)
    gs.codegen.registerIncludePath("./third-party/stp/build/include")
    gs.codegen.registerLibraryPath("./third-party/stp/build/lib")
    gs 
  }
}

trait LinkZ3 extends GenSym {
  abstract override def newInstance(m: Module, name: String, fname: String, config: Config) = {
    val gs = super.newInstance(m, name, fname, config)
    gs.codegen.registerIncludePath("./third-party/z3/src/api")
    gs.codegen.registerIncludePath("./third-party/z3/src/api/c++")
    gs.codegen.registerLibraryPath("./third-party/z3/build")
    gs 
  }
}

class BenchPureGS extends TestGS {
  testGS(new PureGS with LinkSTP with LinkZ3, benchcases)
}

class BenchPureCPSGS extends TestGS {
  testGS(new PureCPSGS with LinkSTP with LinkZ3, benchcases)
}

class BenchImpGS extends TestGS {
  testGS(new ImpGS with LinkSTP with LinkZ3, benchcases)
}

class BenchImpCPSGS extends TestGS {
  testGS(new ImpCPSGS with LinkSTP with LinkZ3, benchcases)
}

class BenchPureCPSGSZ3 extends TestGS {
  testGS(new PureCPSGS with LinkSTP with LinkZ3, benchcases, "z3", true)
}
