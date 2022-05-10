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

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArgOpt, exp) = tst
    test(name) {
      val code = llsc.newInstance(m, llsc.insName + "_" + name, f, config)
      code.genAll
      val mkRet = if(config.test_coreutil) code.make_all_cores else code.make(4)
      assert(mkRet == 0, "make failed")
      val (output, ret) = code.runWithStatus(cliArgOpt.getOrElse(Seq()))
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
  val llsc = new PureCPSLLSC_Z3
  testLLSC(llsc, TestCases.all ++ filesys ++ varArg)

  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoop", "@main", noArg, "--timeout=2", minTest(1)))
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", noArg, "--thread=2 --timeout=2", minTest(1)))
  testLLSC(llsc, TestPrg(data_structures_set_multi_proc_ground_1, "testCompArraySet1", "@main", noArg, None, status(255)))
  testLLSC(llsc, TestPrg(standard_allDiff2_ground, "stdAllDiff2Ground", "@main", noArg, None, status(255)))
  testLLSC(llsc, TestPrg(standard_copy9_ground, "stdCopy9", "@main", noArg, None, status(255)))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, TestCases.all ++ varArg)
  // TODO: fix filesys <2022-03-15, David Deng> //
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, TestCases.all ++ varArg)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
}

class Playground extends TestLLSC {
  //testLLSC(new PureCPSLLSC, TestPrg(mp1048576, "mp1mTest_CPS", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  val llsc = new PureCPSLLSC_Z3
  //testLLSC(llsc, TestPrg(mergesort, "mergeSortTest", "@main", noArg, None, nPath(720)))
  //testLLSC(new PureLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new PureCPSLLSC, List(TestPrg(echo_linked, "echo_linked_posix", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc --sym-stdout --sym-arg 8"), nPath(4971)++status(0))))
  //testLLSC(new PureLLSC, List(TestPrg(echo_llsc_linked, "echo_llsc_linked", "@main", testcoreutil, Seq("--cons-indep","--argv=./true.bc #{3}"), nPath(26)++status(0))))
  //testLLSC(llsc, TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  //testLLSC(llsc, TestPrg(parseFile("benchmarks/demo_benchmarks/nqueen_opt.ll"), "nQueensOpt", "@main", noArg, None, nPath(1363)))
  //testLLSC(new PureLLSC, List(TestPrg(true_linked, "true_linked", "@main", useArgv, "--argv=./true.bc --sym-arg 3", nPath(16)++status(0))))
  //testLLSC(new PureLLSC, List(TestPrg(false_linked, "false_linked", "@main", useArgv, "--argv=./false.bc --sym-arg 3", nPath(16)++status(0))))
  //testLLSC(llsc, TestPrg(bubbleSort2Ground, "bubbleSort2Ground", "@main", 0, None, status(255)))
  //testLLSC(llsc, TestPrg(bubbleSortGround2, "bubbleSortGround2", "@main", 0, None, status(255)))

  // Timeout:
  //testLLSC(llsc, TestPrg(standard_minInArray_ground_1, "standard_minInArray_ground_1", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, None, status(255)))
  //testLLSC(llsc, TestPrg(copysome1_2, "copysome1_2", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(copysome2_2, "copysome2_2", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_2_ground, "bubbleSort2Ground", "@main", noArg, None, status(255)))
  //testLLSC(llsc, TestPrg(sorting_bubblesort_ground_2, "bubbleSortGround2", "@main", 0, None, status(255)))
}

