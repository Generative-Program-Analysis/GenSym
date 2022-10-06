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
    extSolverTime: Double, intSolverTime: Double, wholeTime: Double, blockCov: Double,
    partialBrCov: Double, fullBrCov: Double, pathNum: Int, brQueryNum: Int,
    testQueryNum: Int, cexCacheHit: Int) {
    override def toString() =
      s"$time,$commit,$engine,$testName,$extSolverTime,$intSolverTime,$wholeTime,$partialBrCov,$fullBrCov,$blockCov,$pathNum,$brQueryNum,$testQueryNum,$cexCacheHit"
  }

  val gitCommit = Process("git rev-parse --short HEAD").!!.trim

  def parseOutput(engine: String, testName: String, output: String): TestResult = {
    // example:
    // [43.4s/43.5s/46.0s] #blocks: 12/12; #br: 0/1/2; #paths: 1666; #threads: 1; #task-in-q: 0; #queries: 7328/1666 (1996)
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

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp, runCode) = tst
    test(name) {
      val code = llsc.run(m, llsc.insName + "_" + name, f, config)
      val mkRet = code.makeWithAllCores
      assert(mkRet == 0, "make failed")
      if (runCode) {
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
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --search=random-path --output-tests-cov-new --timeout=2 --solver=z3", minTest(1)))
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  testLLSC(llsc, TestPrg(data_structures_set_multi_proc_ground_1, "testCompArraySet1", "@main", noArg, "--thread=2 --search=random-path --solver=z3", status(255)))
  testLLSC(llsc, TestPrg(standard_allDiff2_ground, "stdAllDiff2Ground", "@main", noArg, "--thread=2 --output-tests-cov-new --solver=z3", status(255)))
  testLLSC(llsc, TestPrg(standard_copy9_ground, "stdCopy9", "@main", noArg, "--thread=2 --search=random-path  --solver=z3", status(255)))

  // Timeout
  //testLLSC(llsc, TestPrg(sorting_selection_ground_1, "testCompSelectionSort", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, TestCases.all ++ filesys ++ varArg)
}

class TestImpCPSLLSC extends TestLLSC {
  val llsc = new ImpCPSLLSC
  testLLSC(llsc, TestCases.all ++ filesys ++ varArg)
  // Note: compile-time switch merge is only implement for ImpCPS so far
  testLLSC(llsc, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))
}

/*
class Coreutils extends TestLLSC {
  import sai.lang.llvm.parser.Parser._
  Config.enableOpt
  val runtimeOptions = "--output-tests-cov-new  --thread=1  --search=random-path  --solver=z3   --output-ktest  --cons-indep".split(" +").toList.toSeq
  val cases = TestCases.coreutils.map { t =>
    t.copy(runOpt = runtimeOptions ++ t.runOpt, runCode = false)
  }
  testLLSC(new ImpCPSLLSC, cases)

  //testLLSC(new ImpCPSLLSC, TestPrg(cat_linked, "cat_linked_posix", "@main", noMainFileOpt, "--argv=./cat.bc --sym-stdout --sym-stdin 2 --sym-arg 2", nPath(28567)++status(0)))
}
*/

class Playground extends TestLLSC {
  import sai.lang.llvm.parser.Parser._
  Config.enableOpt
  val llsc = new ImpCPSLLSC
  //testLLSC(llsc, TestPrg(mergesort, "mergeSortTest1", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(new PureCPSLLSC, TestPrg(arrayFlow, "arrayFlow", "@main", noArg, noOpt, nPath(15)++status(0)))
  //testLLSC(new ImpCPSLLSC, TestPrg(arrayFlow, "arrayFlow2", "@main", noArg, noOpt, nPath(15)++status(0)))

  //testLLSC(llsc, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))
  //testLLSC(llsc, TestPrg(switchTestSym, "switchSymTest", "@main", noArg, noOpt, nPath(5)))
  //testLLSC(llsc, TestPrg(switchTestConc, "switchConcreteTest", "@main", noArg, noOpt, nPath(1)))
  //testLLSC(llsc, TestPrg(maze, "mazeTest", "@main", noArg, noOpt, nPath(309)))

  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest2", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest3", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(llsc, TestPrg(knapsack, "knapsackTest", "@main", noArg, noOpt, nPath(1666)))
  //val echo_linked = parseFile("/home/kraks/research/llsc/coreutils/obj-llvm/playground/echo_llsc.ll")
  //testLLSC(llsc, TestPrg(echo_linked, "echo_linked_posix", "@main",
  //  noMainFileOpt, Seq("--cons-indep", "--argv=./echo.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0)))

  //testLLSC(llsc, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--solver=disable", nPath(1048576)))
  //testLLSC(llsc, TestPrg(quicksort, "quickSortTest", "@main", noArg, noOpt, nPath(120)))
  //testLLSC(llsc, TestPrg(printfTest, "printfTest", "@main", noArg, noOpt, nPath(1)++status(0)))
  //testLLSC(llsc, TestPrg(selectTestSym, "selectTest", "@main", noArg, noOpt, nPath(1)))
  //testLLSC(new ImpCPSLLSC, List(TestPrg(base32_linked, "base32_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10"), nPath(4971)++status(0))))
  //testLLSC(new PureCPSLLSC, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  // testLLSC(new ImpCPSLLSC, TestPrg(standard_minInArray_ground_1, "standard_minInArray_ground_1", "@main", noArg, noOpt, status(255)))

  //testLLSC(new PureCPSLLSC, TestPrg(mp1048576, "mp1mTest_CPS", "@f", symArg(20), "--disable-solver", nPath(1048576)))

  //testLLSC(new PureLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new ImpCPSLLSC, List(TestPrg(cat_linked, "cat_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10"), nPath(256)++status(0))))
  //testLLSC(new ImpCPSLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new PureLLSC, List(TestPrg(echo_llsc_linked, "echo_llsc_linked", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc #{3}"), nPath(26)++status(0))))
  //testLLSC(llsc, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  //testLLSC(llsc, TestPrg(parseFile("benchmarks/demo-benchmarks/nqueen_opt.ll"), "nQueensOpt", "@main", noArg, noOpt, nPath(1363)))
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

