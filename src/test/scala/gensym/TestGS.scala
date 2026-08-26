package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.parser.Parser._
import gensym.Constants._

import gensym.lmsx._
import gensym.utils.Utils.time
import gensym.llvm.Benchmarks._
import gensym.llvm.OOPSLA20Benchmarks._
import gensym.llvm.TestComp.ArrayExamples._
import gensym.llvm.TestComp.ArrayPrograms._

import sys.process._

import org.scalatest.FunSuite

import Config._
import TestPrg._
import TestCases._

abstract class TestGS extends FunSuite {
  import java.time.LocalDateTime

  private val forbiddenGeneratedRuntimeTokens = Seq("immer", "parallel_hashmap", "gensym.hpp", "gensym/state_", "gensym/value_ops")
  private def generatedSources(root: java.io.File): Seq[java.io.File] =
    Option(root.listFiles).toSeq.flatten.flatMap { file =>
      if (file.isDirectory) generatedSources(file)
      else if (file.getName.endsWith(".cpp") || file.getName == "common.h") Seq(file)
      else Seq.empty
    }
  private def assertThinGeneratedSources(root: java.io.File): Unit = generatedSources(root).foreach { file =>
    val source = scala.io.Source.fromFile(file)
    val text = try source.mkString finally source.close()
    forbiddenGeneratedRuntimeTokens.foreach { token =>
      assert(!text.contains(token), s"Generated file ${file.getPath} leaked private runtime token '$token'")
    }
  }

  case class TestResult(time: LocalDateTime, commit: String, engine: String, testName: String,
    extSolverTime: Double, intSolverTime: Double, wholeTime: Double,
    blockCount: Int, blockTotal: Int, partialBranchCount: Int, fullBranchCount: Int,
    totalBranchCount: Int, pathNum: Int, threadCount: Int, queuedTaskCount: Int,
    brQueryNum: Int, testQueryNum: Int, cexCacheHit: Int) {
    val blockCov = blockCount.toDouble / blockTotal.toDouble
    val partialBrCov = partialBranchCount.toDouble / totalBranchCount.toDouble
    val fullBrCov = fullBranchCount.toDouble / totalBranchCount.toDouble
    override def toString() =
      s"$time,$commit,$engine,$testName,$extSolverTime,$intSolverTime,$wholeTime,$partialBrCov,$fullBrCov,$blockCov,$pathNum,$brQueryNum,$testQueryNum,$cexCacheHit"
  }

  val gitCommit = Process("git rev-parse --short HEAD").!!.trim

  def parseOutput(engine: String, testName: String, output: String): TestResult = {
    // example:
    // [43.4s/43.5s/46.0s] #blocks: 12/12; #br: 0/1/2; #paths: 1666; #threads: 1; #task-in-q: 0; #queries: 7328/1666 (1996)
    val pattern = raw"\[([^s]+)s/([^s]+)s/([^s]+)s/([^s]+)s\] #blocks: (\d+)/(\d+); #br: (\d+)/(\d+)/(\d+); #paths: (\d+); #threads: (\d+); #task-in-q: (\d+); #queries: (\d+)/(\d+) \((\d+)\)".r
    output.split("\n").last match {
      case pattern(extSolverTime, intSolverTime, _/*fsTime ignored*/, wholeTime, blockCnt, blockAll,
        partialBr, fullBr, totalBr, pathNum, threadNum, queuedTaskNum, brQuerynum, testQueryNum, cexCacheHit) =>
        TestResult(LocalDateTime.now(), gitCommit, engine, testName,
          extSolverTime.toDouble, intSolverTime.toDouble, wholeTime.toDouble,
          blockCnt.toInt, blockAll.toInt, partialBr.toInt, fullBr.toInt, totalBr.toInt,
          pathNum.toInt, threadNum.toInt, queuedTaskNum.toInt, brQuerynum.toInt,
          testQueryNum.toInt, cexCacheHit.toInt)
    }
  }

  def testWithGlobalConfig[T](name: String)(thunk: => T): Unit = {
    val config0 = Global.config.copy()
    test(name) {
      val config1 = Global.config.copy()
      Global.config = config0
      val result = thunk
      Global.config = config1
    }
  }

  def testGS(gs: GenSym, tst: TestPrg, libPath: Option[String] = None): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp, runCode) = tst
    val outname = if (gs.insName == "ImpCPSGS_lib") name
                  else gs.insName + "_" + name

    testWithGlobalConfig(name) {
      val code = gs.run(m, outname, f, config, libPath)
      assertThinGeneratedSources(new java.io.File(s"$outputDir/$outname"))
      val mkRet = code.makeWithAllCores
      assert(mkRet == 0, "make failed")
      if (runCode) {
        val (output, ret) = code.runWithStatus(cliArg)
        System.err.println(output)
        val resStat = parseOutput(gs.insName, name, output)
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
        if (exp.contains(blockCounts)) assert((resStat.blockCount, resStat.blockTotal) == exp(blockCounts), "Unexpected block counts")
        if (exp.contains(branches)) assert((resStat.partialBranchCount, resStat.fullBranchCount, resStat.totalBranchCount) == exp(branches), "Unexpected branch counts")
        if (exp.contains(threads)) assert(resStat.threadCount == exp(threads), "Unexpected thread count")
        if (exp.contains(queuedTasks)) assert(resStat.queuedTaskCount == exp(queuedTasks), "Unexpected queued task count")
        if (exp.contains(queries)) assert((resStat.brQueryNum, resStat.testQueryNum, resStat.cexCacheHit) == exp(queries), "Unexpected query counts")
      }
    }
  }

  def testGS(gs: GenSym, tests: List[TestPrg]): Unit = tests.foreach(testGS(gs, _))
}

class TestImpCPSGS extends TestGS {
  val gs = new ImpCPSGS
  testGS(gs, TestCases.all ++ filesys ++ varArg)
  // Note: compile-time switch merge is only implemented for ImpCPS so far
  testGS(gs, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))

  // Test uninitialized ptr access, only enabled for CPS+thread pool version
  val rtOpt = "--thread=1"
  Global.config.symbolicUninit = true
  testGS(gs, TestPrg(symPtr, "symPtrTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtrCond, "uninitPtrCondTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtr, "unintPtrTest", "@main", noArg, rtOpt, nPath(1)))
  testGS(gs, TestPrg(uninitPtrUpdate, "uninitPtrUpdate", "@main", noArg, rtOpt, nPath(2)))

  testGS(gs, TestPrg(argv2Test, "argvConcSymUninit", "@main", useArgv, s"$rtOpt --argv=abcdef", nPath(1)++status(0)))
  testGS(gs, TestPrg(argv2Test, "argvSymSymUninit", "@main", useArgv, s"$rtOpt --argv=abc#{3}def", nPath(4)++status(0)))
  testGS(gs, TestPrg(
    openSymTest, "openSymTestSymUninit", "@main", noArg, s"$rtOpt --add-sym-file A --add-sym-file B", nPath(3)++status(0))
  )

  testGS(gs, TestPrg(assumeTest, "assumeTestSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))
  testGS(gs, TestPrg(flexAddr, "flexAddrSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))
  testGS(gs, TestPrg(printfTest, "printfTestSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))

  // FIXME: faultyBstTestSymUninit on CI produces 176 paths (vs 642)
  // testGS(gs, TestPrg(faultyBst, "faultyBstTestSymUninit", "@main", noArg, rtOpt, nPath(642)))
  Global.config.symbolicUninit = false
}

class TestImpCPSGS_Z3 extends TestGS {
  val gs = new ImpCPSGS
  val cases =  (TestCases.all ++ filesys ++ varArg).map { t =>
    t.copy(runOpt = t.runOpt ++ Seq("--solver=z3"))
  }
  testGS(gs, cases)

  // Test uninitialized ptr access, only enabled for CPS+thread pool version
  val rtOpt = "--thread=1 --solver=z3"
  Global.config.symbolicUninit = true
  testGS(gs, TestPrg(symPtr, "symPtrTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtrCond, "uninitPtrCondTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtr, "unintPtrTest", "@main", noArg, rtOpt, nPath(1)))
  testGS(gs, TestPrg(uninitPtrUpdate, "uninitPtrUpdate", "@main", noArg, rtOpt, nPath(2)))

  testGS(gs, TestPrg(argv2Test, "argvConcSymUninit", "@main", useArgv, s"$rtOpt --argv=abcdef", nPath(1)++status(0)))
  testGS(gs, TestPrg(argv2Test, "argvSymSymUninit", "@main", useArgv, s"$rtOpt --argv=abc#{3}def", nPath(4)++status(0)))
  testGS(gs, TestPrg(
    openSymTest, "openSymTestSymUninit", "@main", noArg, s"$rtOpt --add-sym-file A --add-sym-file B", nPath(3)++status(0))
  )

  testGS(gs, TestPrg(assumeTest, "assumeTestSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))
  testGS(gs, TestPrg(flexAddr, "flexAddrSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))
  testGS(gs, TestPrg(printfTest, "printfTestSymUninit", "@main", noArg, rtOpt, nPath(1)++status(0)))

  // FIXME: faultyBstTestSymUninit on CI produces 176 paths (vs 642)
  //testGS(gs, TestPrg(faultyBst, "faultyBstTestSymUninit", "@main", noArg, rtOpt, nPath(642)))
  Global.config.symbolicUninit = false
}

class TestLibrary extends TestGS {
  testGS(new ImpCPSGS_lib, TestPrg(linkLib, "libtest", "@main", useArgv, noOpt, nPath(1)++status(0), false))
  testGS(new ImpCPSGS_app, TestPrg(linkApp, "libapp", "@main", useArgv, "--argv=''", nPath(1)++status(0)), s"${outputDir}/libtest")
}

class Playground extends TestGS {
  import gensym.llvm.parser.Parser._
  Global.config.enableOpt
  val gs = new ImpCPSGS
  val rtOpt = "--thread=1 --solver=z3"

  //val cases = CoreutilsPOSIX.coreutils

  /*
  val cases = List(TestPrg(
    CoreutilsPOSIX.echo,
    "echo_linked_posix", "@main",  noMainFileOpt,
    "--output-tests-cov-new --thread=1 --search-strategy=random-path --solver=z3 --output-ktest --argv=./echo.bc --sym-stdout --sym-arg 2 --sym-arg 7",
    nPath(216136)++status(0)))
  */
  val cases = List(
    TestPrg(coverageGuidedAssert, "coverageGuidedAssert", "@main", noArg,
      "--thread=1 --search-strategy=coverage-guided --timeout=1",
      // If not using coverage-guided search, it cannot find the assertion violation in 1 sec
      nPath(11) ++ nTest(1) ++ expectBlocks(148, 148) ++ branches(0, 35, 35)),
  )
  testGS(gs, cases)


}
