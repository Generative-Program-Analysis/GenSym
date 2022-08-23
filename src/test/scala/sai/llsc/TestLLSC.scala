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
      case pattern(extSolverTime, intSolverTime, fsTime, wholeTime, blockCnt, blockAll,
        partialBr, fullBr, totalBr, pathNum, brQuerynum, testQueryNum, cexCacheHit) =>
        TestResult(LocalDateTime.now(), gitCommit, engine, testName,
          extSolverTime.toDouble, intSolverTime.toDouble, wholeTime.toDouble,
          blockCnt.toDouble/blockAll.toDouble, partialBr.toDouble/totalBr.toDouble,
          fullBr.toDouble/totalBr.toDouble, pathNum.toInt, brQuerynum.toInt,
          testQueryNum.toInt, cexCacheHit.toInt)
    }
  }

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp) = tst
    test(name) {
      val code = llsc.run(m, llsc.insName + "_" + name, f, config)
      val mkRet = code.makeWithAllCores
      assert(mkRet == 0, "make failed")
      if (Config.runCode) {
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

class Coreutils extends TestLLSC {
  import sai.lang.llvm.parser.Parser._
  Config.enableOpt
  Config.disableRunCode
  val llsc = new ImpCPSLLSC
  val llsc_opt = "--output-tests-cov-new  --thread=1  --search=random-path  --solver=z3   --output-ktest  --cons-indep".split(" +").toSeq
  testLLSC(new ImpCPSLLSC, List(TestPrg(echo_linked,    "echo_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./echo.bc     --sym-stdout --sym-arg 2 --sym-arg 7", nPath(4971)++status(0))))
  // [0.75008s/6.94065s/82.0174s] #blocks: 473/2224; #br: 160/96/1128; #paths: 216136; #threads: 1; #task-in-q: 0; #queries: 646097/53 (0)
  // gcov 84.17%
  testLLSC(new ImpCPSLLSC, List(TestPrg(cat_linked,     "cat_linked_posix",     "@main",  noMainFileOpt, llsc_opt + "--argv=./cat.bc      --sym-stdout --sym-stdin 2 --sym-arg 2", nPath(256)++status(0))))
  // [17.5964s/122.277s/152.538s] #blocks: 1114/2484; #br: 371/297/1664; #paths: 28567; #threads: 1; #task-in-q: 0; #queries: 976797/71 (0)
  // gcov 81.4%
  testLLSC(new ImpCPSLLSC, List(TestPrg(base32_linked,  "base32_linked_posix",  "@main",  noMainFileOpt, llsc_opt + "--argv=./base32      --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 10", nPath(256)++status(0))))
  // [11.0384s/81.2135s/95.0274s] #blocks: 1170/2718; #br: 417/284/1860; #paths: 10621; #threads: 1; #task-in-q: 0; #queries: 475353/38 (0)
  // gcov 73.33%
  testLLSC(new ImpCPSLLSC, List(TestPrg(base64_linked,  "base64_linked_posix",  "@main",  noMainFileOpt, llsc_opt + "--argv=./base64.bc   --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 10", nPath(256)++status(0))))
  // [11.1519s/81.5593s/95.0264s] #blocks: 1164/2694; #br: 411/286/1889; #paths: 10624; #threads: 1; #task-in-q: 0; #queries: 475379/38 (0)
  // gcov 73.33%
  testLLSC(new ImpCPSLLSC, List(TestPrg(comm_linked,    "comm_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./comm.bc     --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(256)++status(0))))
  // [8.71519s/27.1575s/41.5112s] #blocks: 836/2719; #br: 325/185/2077; #paths: 10616; #threads: 1; #task-in-q: 0; #queries: 89179/89 (0)
  // gcov 22.41%
  testLLSC(new ImpCPSLLSC, List(TestPrg(cut_linked,     "cut_linked_posix",     "@main",  noMainFileOpt, llsc_opt + "--argv=./cut.bc      --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg  2 -sym-files 2 10", nPath(256)++status(0)))) // or --sym-args 0 2 2
  // [28.2764s/96.9007s/141.538s] #blocks: 1056/2751; #br: 388/242/2124; #paths: 28481; #threads: 1; #task-in-q: 0; #queries: 238501/101 (0)
  // gcov 63.26%
  testLLSC(new ImpCPSLLSC, List(TestPrg(dirname_linked, "dirname_linked_posix", "@main",  noMainFileOpt, llsc_opt + "--argv=./dirname.bc  --sym-stdout  --sym-stdin 2 --sym-arg 4 --sym-arg 10", nPath(256)++status(0))))
  // [2.14734s/16.1223s/52.5137s] #blocks: 590/2295; #br: 219/93/2130; #paths: 80125; #threads: 1; #task-in-q: 0; #queries: 1035523/21 (0)
  // gcov 100.00%
  testLLSC(new ImpCPSLLSC, List(TestPrg(expand_linked,  "expand_linked_posix",  "@main",  noMainFileOpt, llsc_opt + "--argv=./expand.bc   --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(256)++status(0))))
  // [21.482s/101.299s/116.022s] #blocks: 1167/2554; #br: 408/294/2145; #paths: 10870; #threads: 1; #task-in-q: 0; #queries: 478319/41 (0)
  // gcov 71.05%
  testLLSC(new ImpCPSLLSC, List(TestPrg(false_linked,   "false_linked_posix",   "@main",  noMainFileOpt, llsc_opt + "--argv=./false.bc    --sym-stdout  --sym-arg 10", nPath(256)++status(0))))
  // [0.012289s/0.032464s/1.00143s] #blocks: 391/2108; #br: 159/44/2276; #paths: 16; #threads: 1; #task-in-q: 0; #queries: 165/3 (0)
  // gcov 100.00%
  testLLSC(new ImpCPSLLSC, List(TestPrg(true_linked,    "true_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./true.bc     --sym-stdout  --sym-arg 10", nPath(256)++status(0))))
  // [0.012326s/0.032479s/1.0014s] #blocks: 391/2108; #br: 159/44/2276; #paths: 16; #threads: 1; #task-in-q: 0; #queries: 165/3 (0)
  // gcov 100.00%
  testLLSC(new ImpCPSLLSC, List(TestPrg(fold_linked,    "fold_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./fold.bc     --sym-stdout  --sym-stdin 2 --sym-arg 1 -sym-files 2 10", nPath(256)++status(0))))
  // [11.2871s/95.8641s/110.53s] #blocks: 1212/2603; #br: 417/301/2279; #paths: 11015; #threads: 1; #task-in-q: 0; #queries: 478287/46 (0)
  // gcov 74.36%
  testLLSC(new ImpCPSLLSC, List(TestPrg(join_linked,    "join_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./join.bc     --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(256)++status(0))))
  // [32.7084s/230.442s/263.047s] #blocks: 1444/3172; #br: 461/369/2511; #paths: 25046; #threads: 1; #task-in-q: 0; #queries: 983311/87 (0)
  // gcov 71.75%
  testLLSC(new ImpCPSLLSC, List(TestPrg(link_linked,    "link_linked_posix",    "@main",  noMainFileOpt, llsc_opt + "--argv=./link.bc     --sym-stdout  --sym-stdin 2 --sym-arg 1  --sym-arg 1  -sym-files 2 2", nPath(256)++status(0))))
  // [7.97859s/106.563s/133.037s] #blocks: 824/2282; #br: 321/188/2511; #paths: 10816; #threads: 1; #task-in-q: 0; #queries: 1243319/83 (0)
  // gcov 36.00%
  testLLSC(new ImpCPSLLSC, List(TestPrg(paste_linked,   "paste_linked_posix",   "@main",  noMainFileOpt, llsc_opt + "--argv=./paste.bc    --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(256)++status(0))))
  // [19.9342s/142.499s/170.043s] #blocks: 1180/2510; #br: 393/306/2513; #paths: 22622; #threads: 1; #task-in-q: 0; #queries: 974269/46 (0)
  // gcov 76.08%
  testLLSC(new ImpCPSLLSC, List(TestPrg(pathchk_linked, "pathchk_linked_posix", "@main",  noMainFileOpt, llsc_opt + "--argv=./pathchk.bc  --sym-stdout  --sym-stdin 2 --sym-arg 2", nPath(256)++status(0))))
  // [10.6848s/77.1473s/91.5263s] #blocks: 878/2418; #br: 314/226/2515; #paths: 10614; #threads: 1; #task-in-q: 0; #queries: 475423/30 (0)
  // gcov 40.29%
  Config.enableRunCode
}

class Playground extends TestLLSC {
  import sai.lang.llvm.parser.Parser._
  Config.enableOpt
  val llsc = new ImpCPSLLSC
  //testLLSC(llsc, TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)))
  //testLLSC(llsc, TestPrg(switchTestSym, "switchSymTest", "@main", noArg, noOpt, nPath(5)))
  //testLLSC(llsc, TestPrg(switchTestConc, "switchConcreteTest", "@main", noArg, noOpt, nPath(1)))
  //testLLSC(llsc, TestPrg(maze, "mazeTest", "@main", noArg, noOpt, nPath(309)))
  //testLLSC(llsc, TestPrg(arrayFlow, "arrayFlow", "@main", noArg, noOpt, nPath(15)++status(0)))

  //testLLSC(llsc, TestPrg(mergesort, "mergeSortTest", "@main", noArg, noOpt, nPath(720)))
  //testLLSC(new PureLLSC, TestPrg(mergesort, "mergeSortTest1", "@main", noArg, noOpt, nPath(720)))
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

