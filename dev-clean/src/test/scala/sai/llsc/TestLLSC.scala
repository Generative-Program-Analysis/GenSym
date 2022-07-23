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
import sai.lang.llvm.Benchmarks._
import sai.lang.llvm.OOPSLA20Benchmarks._
import sai.lang.llvm.TestComp.ArrayExamples._
import sai.lang.llvm.TestComp.ArrayPrograms._

import sys.process._

import org.scalatest.FunSuite

import Config._
import TestPrg._
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
    // example:
    // [43.4s/46.0s] #blocks: 12/12; #paths: 1666; #threads: 1; #task-in-q: 0; #queries: 7328/1666 (1996)
    val pattern = raw"\[([^s]+)s/([^s]+)s\] #blocks: (\d+)/(\d+); #paths: (\d+); .+; #queries: (\d+)/(\d+) \((\d+)\)".r
    output.split("\n").last match {
      case pattern(solverTime, wholeTime, blockCnt, blockAll, pathNum, brQuerynum, testQueryNum, cexCacheHit) =>
        TestResult(LocalDateTime.now(), gitCommit, engine, testName,
                   solverTime.toDouble, wholeTime.toDouble, blockCnt.toDouble / blockAll.toDouble,
                   pathNum.toInt, brQuerynum.toInt, testQueryNum.toInt, cexCacheHit.toInt)
    }
  }

  def testUnit(path: String, name: String, j: Int = 4): Unit = {
    test(s"${name}_unit") {
      val dir = new java.io.File(path)
      val retMake = Process(s"make -j$j $name", dir).!
      assert(retMake == 0, "Make failed")
      val retTest = Process(s"./$name", dir).!
      assert(retTest == 0, s"Test $name failed")
    }
  }

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp) = tst
    test(name) {
      val code = llsc.runLLSC(m, llsc.insName + "_" + name, f, config)
      val mkRet = if(config.test_coreutil) code.makeWithAllCores else code.make(4)
      assert(mkRet == 0, "make failed")
      val (output, ret) = code.runWithStatus(cliArg)
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

class TestPureLLSC extends TestLLSC {
  testLLSC(new PureLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new PureLLSC, TestPrg(arrayAccess, "arrayAccTest", "@main", noArg, 1))
}

// FIXME: varArg is problematic for instances other than PureLLSC

class TestPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
}

class TestPureCPSLLSC_Z3 extends TestLLSC {
  val llsc = new PureCPSLLSC
  val cases =  (TestCases.all ++ filesys ++ varArg).map { t =>
    t.copy(runOpt = t.runOpt ++ Seq("--solver=z3"))
  }

  testLLSC(llsc, cases)

  // Note: these test cases need to use `--thread=2` to enable random path selection strategy
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  testLLSC(llsc, TestPrg(data_structures_set_multi_proc_ground_1, "testCompArraySet1", "@main", noArg, "--thread=2 --solver=z3", status(255)))
  testLLSC(llsc, TestPrg(standard_allDiff2_ground, "stdAllDiff2Ground", "@main", noArg, "--thread=2 --solver=z3", status(255)))
  testLLSC(llsc, TestPrg(standard_copy9_ground, "stdCopy9", "@main", noArg, "--thread=2 --solver=z3", status(255)))

  // Timeout
  //testLLSC(llsc, TestPrg(sorting_selection_ground_1, "testCompSelectionSort", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
}

class TestUnit extends TestLLSC {
  testUnit("./headers/test", "external_test")
}

class Playground extends TestLLSC {
  //val llsc = new PureCPSLLSC
  Config.enableOpt
  val llsc = new ImpCPSLLSC
  testLLSC(llsc, TestPrg(selectTestSym, "selectTest", "@main", noArg, noOpt, nPath(1)))
  //testLLSC(llsc, TestPrg(maze, "mazeTest", "@main", noArg, noOpt, nPath(309)))
  //testLLSC(llsc, TestPrg(switchTestSym, "switchSymTest", "@main", noArg, noOpt, nPath(5)))
  //testLLSC(llsc, TestPrg(arrayFlow, "arrayFlow", "@main", noArg, noOpt, nPath(15)++status(0)))
  //testLLSC(llsc, TestPrg(printfTest, "printfTest", "@main", noArg, noOpt, nPath(1)++status(0)))
  //testLLSC(new ImpCPSLLSC, List(TestPrg(base32_linked, "base32_linked_posix", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10"), nPath(4971)++status(0))))
  //testLLSC(new PureCPSLLSC, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  // testLLSC(new ImpCPSLLSC, TestPrg(standard_minInArray_ground_1, "standard_minInArray_ground_1", "@main", noArg, noOpt, status(255)))

  //testLLSC(new PureCPSLLSC, TestPrg(mp1048576, "mp1mTest_CPS", "@f", symArg(20), "--disable-solver", nPath(1048576)))

  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortPureTest", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(new PureLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new PureCPSLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new PureLLSC, List(TestPrg(echo_llsc_linked, "echo_llsc_linked", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc #{3}"), nPath(26)++status(0))))
  //testLLSC(llsc, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  //testLLSC(llsc, TestPrg(parseFile("benchmarks/demo_benchmarks/nqueen_opt.ll"), "nQueensOpt", "@main", noArg, noOpt, nPath(1363)))
  //testLLSC(new PureLLSC, List(TestPrg(true_linked, "true_linked", "@main", useArgv, "--argv=./true.bc --sym-arg 3", nPath(16)++status(0))))
  //testLLSC(new PureLLSC, List(TestPrg(false_linked, "false_linked", "@main", useArgv, "--argv=./false.bc --sym-arg 3", nPath(16)++status(0))))
  //testLLSC(llsc, TestPrg(bubbleSort2Ground, "bubbleSort2Ground", "@main", 0, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(bubbleSortGround2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))

  // Timeout:
  //testLLSC(llsc, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, noOpt, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))
}

