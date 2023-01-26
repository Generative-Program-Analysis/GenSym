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

  def testGS(gs: GenSym, tst: TestPrg, libPath: Option[String] = None): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp, runCode) = tst
    val outname = if (gs.insName == "ImpCPSGS_lib") name
                  else gs.insName + "_" + name
    test(name) {
      val code = gs.run(m, outname, f, config, libPath)
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
      }
    }
  }

  def testGS(gs: GenSym, tests: List[TestPrg]): Unit = tests.foreach(testGS(gs, _))
}

class TestPureGS extends TestGS {
  testGS(new PureGS, TestCases.all ++ filesys ++ varArg)
}

class TestPureCPSGS extends TestGS {
  val gs = new PureCPSGS

  // Note: the following test cases need to use `--thread=n` to enable random path selection strategy.
  //       They also relies on block-level path switching to increase randomness, which currently has only
  //       been implemented in PureCPS engine.
  testGS(gs, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --search=random-path --output-tests-cov-new --timeout=2 --solver=z3", minTest(1)))
  testGS(gs, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  testGS(gs, TestPrg(data_structures_set_multi_proc_ground_1, "testCompArraySet1", "@main", noArg, "--thread=2 --search=random-path --solver=z3", status(255)))
  testGS(gs, TestPrg(standard_allDiff2_ground, "stdAllDiff2Ground", "@main", noArg, "--thread=2 --output-tests-cov-new --solver=z3", status(255)))
  testGS(gs, TestPrg(standard_copy9_ground, "stdCopy9", "@main", noArg, "--thread=2 --search=random-path  --solver=z3", status(255)))

  // Timeout
  //testGS(gs, TestPrg(sorting_selection_ground_1, "testCompSelectionSort", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
}

class TestImpGS extends TestGS {
  testGS(new ImpGS, TestCases.all ++ filesys ++ varArg)
}

class TestImpCPSGS extends TestGS {
  val gs = new ImpCPSGS
  testGS(gs, TestCases.all ++ filesys ++ varArg)
  // Note: compile-time switch merge is only implement for ImpCPS so far
  testGS(gs, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))

  // Test uninitialized ptr access, only enabled for CPS+thread pool version
  val rtOpt = "--thread=1"
  testGS(gs, TestPrg(symPtr, "symPtrTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtrCond, "uninitPtrCondTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtr, "unintPtrTest", "@main", noArg, rtOpt, nPath(1)))
}

class TestPtr extends TestGS {
  // TODO: what's the expected result for faultyBstTest?
  val faultyBstTest = parseFile("benchmarks/demo-benchmarks/faulty_bst.ll")
  // finds 642 paths, Klee finds 8 incomplete & 2 complete
  testGS(new ImpCPSGS, TestPrg(faultyBstTest, "faultyBstTest", "@main", noArg, "--thread=1", nPath(10)))
  testGS(new ImpCPSGS, TestPrg(faultyBstTest, "faultyBstTestZ3", "@main", noArg, "--thread=1 --solver=z3", nPath(10)))
}

class TestPtrUpdate extends TestGS {
  val uninitPtrUpdate = parseFile("benchmarks/llvm/uninit_ptr_update.ll")
  testGS(new ImpCPSGS, TestPrg(uninitPtrUpdate, "uninitPtrUpdate", "@main", noArg, "--thread=1", nPath(6)))
}

class TestImpCPSGS_Z3 extends TestGS {
  val gs = new ImpCPSGS
  val cases =  (TestCases.all ++ filesys ++ varArg).map { t =>
    t.copy(runOpt = t.runOpt ++ Seq("--solver=z3"))
  }
  testGS(gs, cases)

  // Test uninitialized ptr access, only enabled for CPS+thread pool version
  val rtOpt = "--thread=1 --solver=z3"
  testGS(gs, TestPrg(symPtr, "symPtrTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtrCond, "uninitPtrCondTest", "@main", noArg, rtOpt, nPath(2)))
  testGS(gs, TestPrg(uninitPtr, "unintPtrTest", "@main", noArg, rtOpt, nPath(1)))
}

/*
class Coreutils extends TestGS {
  import gensym.llvm.parser.Parser._
  Config.enableOpt
  val runtimeOptions = "--output-tests-cov-new  --thread=1  --search=random-path  --solver=z3   --output-ktest  --cons-indep".split(" +").toList.toSeq
  val cases = TestCases.coreutils.map { t =>
    t.copy(runOpt = runtimeOptions ++ t.runOpt, runCode = false)
  }
  testGS(new ImpCPSGS, cases)

  //testGS(new ImpCPSGS, TestPrg(cat_linked, "cat_linked_posix", "@main", noMainFileOpt, "--argv=./cat.bc --sym-stdout --sym-stdin 2 --sym-arg 2", nPath(28567)++status(0)))
}
*/

class TestLibrary extends TestGS {
  testGS(new ImpCPSGS_lib, TestPrg(linkLib, "libtest", "@main", useArgv, noOpt, nPath(1)++status(0), false))
  testGS(new ImpCPSGS_app, TestPrg(linkApp, "libapp", "@main", useArgv, "--argv=''", nPath(1)++status(0)), s"${outputDir}/libtest")
}

class Playground extends TestGS {
  import gensym.llvm.parser.Parser._
  Config.enableOpt
  val gs = new ImpCPSGS
  //testGS(gs, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --search=random-path --output-tests-cov-new --timeout=2 --solver=z3", minTest(1)))
  //testGS(gs, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))

  //testGS(gs, TestPrg(mergesort, "mergeSortTest1", "@main", noArg, noOpt, nPath(720)))
  //testGS(new PureCPSGS, TestPrg(arrayFlow, "arrayFlow", "@main", noArg, noOpt, nPath(15)++status(0)))
  //testGS(new ImpCPSGS, TestPrg(arrayFlow, "arrayFlow2", "@main", noArg, noOpt, nPath(15)++status(0)))

  //testGS(gs, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))
  //testGS(gs, TestPrg(switchTestSym, "switchSymTest", "@main", noArg, noOpt, nPath(5)))
  //testGS(gs, TestPrg(switchTestConc, "switchConcreteTest", "@main", noArg, noOpt, nPath(1)))
  //testGS(gs, TestPrg(maze, "mazeTest", "@main", noArg, noOpt, nPath(309)))

  //testGS(new PureCPSGS, TestPrg(mergesort, "mergeSortTest2", "@main", noArg, noOpt, nPath(720)))
  //testGS(new ImpGS, TestPrg(mergesort, "mergeSortTest3", "@main", noArg, noOpt, nPath(720)))
  //testGS(gs, TestPrg(knapsack, "knapsackTest", "@main", noArg, noOpt, nPath(1666)))
  //val echo_linked = parseFile("/home/kraks/research/gs/coreutils/obj-llvm/playground/echo_gs.ll")
  //testGS(gs, TestPrg(echo_linked, "echo_linked_posix", "@main",
  //  noMainFileOpt, Seq("--cons-indep", "--argv=./echo.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0)))

  //testGS(gs, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--solver=disable", nPath(1048576)))
  //testGS(gs, TestPrg(quicksort, "quickSortTest", "@main", noArg, noOpt, nPath(120)))
  //testGS(gs, TestPrg(printfTest, "printfTest", "@main", noArg, noOpt, nPath(1)++status(0)))
  //testGS(gs, TestPrg(selectTestSym, "selectTest", "@main", noArg, noOpt, nPath(1)))
  //testGS(new ImpCPSGS, List(TestPrg(base32_linked, "base32_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10"), nPath(4971)++status(0))))
  //testGS(new PureCPSGS, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--thread=2 --timeout=2 --solver=z3", minTest(1)))
  // testGS(new ImpCPSGS, TestPrg(standard_minInArray_ground_1, "standard_minInArray_ground_1", "@main", noArg, noOpt, status(255)))

  //testGS(new PureCPSGS, TestPrg(mp1048576, "mp1mTest_CPS", "@f", symArg(20), "--disable-solver", nPath(1048576)))

  //testGS(new PureGS, List(TestPrg(echo_linked, "echo_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testGS(new ImpCPSGS, List(TestPrg(cat_linked, "cat_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10"), nPath(256)++status(0))))
  //testGS(new ImpCPSGS, List(TestPrg(echo_linked, "echo_linked_posix", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testGS(new PureGS, List(TestPrg(echo_gs_linked, "echo_gs_linked", "@main", noMainFileOpt, Seq("--cons-indep","--argv=./true.bc #{3}"), nPath(26)++status(0))))
  //testGS(gs, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  //testGS(gs, TestPrg(parseFile("benchmarks/demo-benchmarks/nqueen_opt.ll"), "nQueensOpt", "@main", noArg, noOpt, nPath(1363)))
  //testGS(new PureGS, List(TestPrg(true_linked, "true_linked", "@main", useArgv, "--argv=./true.bc --sym-arg 3", nPath(16)++status(0))))
  //testGS(new PureGS, List(TestPrg(false_linked, "false_linked", "@main", useArgv, "--argv=./false.bc --sym-arg 3", nPath(16)++status(0))))
  //testGS(gs, TestPrg(bubbleSort2Ground, "bubbleSort2Ground", "@main", 0, noOpt, status(255)))
  //testGS(gs, TestPrg(bubbleSortGround2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))

  // Timeout:
  //testGS(gs, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))
  //testGS(gs, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, noOpt, status(255)))
  //testGS(gs, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, noOpt, status(255)))
}

