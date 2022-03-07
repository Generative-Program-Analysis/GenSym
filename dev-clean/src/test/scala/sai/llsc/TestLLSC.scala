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
  val concrete: List[TestPrg] = List(
    TestPrg(add, "addTest", "@main", noArg, None, nPath(1)),
    TestPrg(aliasing, "aliasingTest", "@main", noArg, None, nPath(1)),
    TestPrg(power, "powerTest", "@main", noArg, None, nPath(1)),
    TestPrg(global, "globalTest", "@main", noArg, None, nPath(1)),
    TestPrg(ptrpred, "ptrPredTest", "@main", noArg, None, nPath(1)),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", noArg, None, nPath(1)),
    TestPrg(trunc, "truncTest", "@main", noArg, None, nPath(1)),
    TestPrg(floatArith, "floatArithTest", "@main", noArg, None, nPath(1)),
    TestPrg(floatFp80, "floatFp80Test", "@main", noArg, None, nPath(1)),

    TestPrg(arrayAccess, "arrayAccTest", "@main", noArg, None, nPath(1)),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", noArg, None, nPath(1)),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", noArg, None, nPath(1)),
    TestPrg(complexStruct, "complexStructTest", "@main", noArg, None, nPath(1)),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", noArg, None, nPath(1)),
    TestPrg(structAccess, "structAccessTest", "@main", noArg, None, nPath(1)),
    TestPrg(structReturn, "structReturnTest", "@main", noArg, None, nPath(1)),

    TestPrg(unprintableCharTest, "unprintableCharTest", "@main", noArg, None, nPath(1)++status(0)),
  )

  val memModel: List[TestPrg] = List(
    TestPrg(funptr, "funptr", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(heapFunptr, "heapFunptr", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(ptrtoint, "ptrToInt", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(flexAddr, "flexAddr", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(nastyStruct, "nastyStruct", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(arrayFlow, "arrayFlow", "@main", noArg, None, nPath(15)++status(0)),
  )

  val argv: List[TestPrg] = List(
    TestPrg(argv1Test, "argvConc", "@main", useArgv, "--argv=abcdef", nPath(1)++status(0)),
    TestPrg(argv2Test, "argvSym", "@main", useArgv, "--argv=abc#{3}def", nPath(4)++status(0)),
  )

  val varArg: List[TestPrg] = List(
    TestPrg(varArgInt, "varArgInt", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(varArgCopyInt, "varArgCopyInt", "@main", noArg, None, nPath(1)++status(0)),
    // FIXME(PureLLSC): Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", noArg, None, nPath(1))
  )

  val symbolicSimple: List[TestPrg] = List(
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", noArg, None, nPath(4)),
    TestPrg(branch, "branch1", "@f", symArg(2), None, nPath(4)),
    TestPrg(branch2, "branch2", "@f", symArg(2), None, nPath(4)),
    TestPrg(branch3, "branch3", "@f", symArg(2), None, nPath(4)),
    TestPrg(switchTestSym, "switchSymTest", "@main", noArg, None, nPath(5)),
  )

  val symbolicSmall: List[TestPrg] = List(
    TestPrg(mergesort, "mergeSortTest", "@main", noArg, None, nPath(720)),
    TestPrg(bubblesort, "bubbleSortTest", "@main", noArg, None, nPath(24)),
    TestPrg(quicksort, "quickSortTest", "@main", noArg, None, nPath(120)),
    TestPrg(kmpmatcher, "kmp", "@main", noArg, None, nPath(1287)),
    TestPrg(kth, "k_of_st_arrays", "@main", noArg, None, nPath(252)),
    TestPrg(binSearch, "binSearch", "@main", noArg, None, nPath(92)),
    TestPrg(knapsack, "knapsackTest", "@main", noArg, None, nPath(1666)),
    TestPrg(nqueen, "nQueens", "@main", noArg, None, nPath(1363)),
    // The oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", noArg, None, nPath(309)),
    TestPrg(mp1024, "mp1024Test", "@f", symArg(10), None, nPath(1024)),
  )

  val symbolicLarge: List[TestPrg] = List(
    TestPrg(mp65536, "mp65kTest", "@f", symArg(16), "--disable-solver", nPath(65536)),
    TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--disable-solver", nPath(1048576)),
  )

  val external: List[TestPrg] = List(
    TestPrg(assertTest, "assertTest", "@main", noArg, None, minPath(3)),
    TestPrg(assertfixTest, "assertfix", "@main", noArg, None, nPath(4)),
    TestPrg(assumeTest, "assumeTest", "@main", noArg, None, nPath(1)++status(0)),
  )

  val filesys: List[TestPrg] = List(
    TestPrg(openTest, "openTestSucc", "@main", noArg, "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(openTest, "openTestFail", "@main", noArg, None, nPath(1)++status(1)),
    TestPrg(closeTest, "closeTest", "@main", noArg, None, nPath(1)++status(0)),
    TestPrg(read1Test, "readTestRetVal", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
    TestPrg(read2Test, "readTestPaths", "@main", noArg, "--sym-file-size 3 --add-sym-file A", nPath(3)++status(0)),
    TestPrg(write1Test, "writeTestPaths", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(2)++status(0)),
    TestPrg(stat1Test, "statTestAssign", "@main", noArg, "", nPath(1)++status(0)),
    TestPrg(stat2Test, "statTestRead", "@main", noArg, "--add-sym-file A", nPath(3)++status(0)),
    TestPrg(stat2Test, "statTestFail", "@main", noArg, "", nPath(1)++status(1)),
    TestPrg(kleefsminiTest, "kleefsmini", "@main", noArg, None, nPath(2)++status(0)),
    TestPrg(kleefsminiPackedTest, "kleefsminiPackedTest", "@main", noArg, None, nPath(2)++status(0)),
    TestPrg(kleefsglobalTest, "kleefsminiglobal", "@main", noArg, None, nPath(2)++status(0)),
    TestPrg(kleefslib64Test, "kleelib64", "@main", noArg, None, nPath(10)++status(0)),
  )

  val all: List[TestPrg] = concrete ++ memModel ++ symbolicSimple ++ symbolicSmall ++ external ++ argv

  // FIXME: out of range
  // TestPrg(struct, "structTest", "@main", noArg, 1),
  // FIXME: seg fault
  // TestPrg(largeStackArray, "largeStackArrayTest", "@main", noArg, 1),
  // TestPrg(makeSymbolicArray, "makeSymbolicArrayTest", "@main", noArg, 1),
  // TestPrg(ptrtoint, "ptrToIntTest", "@main", noArg, 1)
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

class TestPureLLSC extends TestLLSC {
  testLLSC(new PureLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new PureLLSC, TestPrg(arrayAccess, "arrayAccTest", "@main", noArg, 1))
  //testLLSC(new PureLLSC, TestCases.external)
}

// FIXME: varArg is problematic for instances other than PureLLSC

class TestPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC, TestCases.all ++ filesys ++ varArg)
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
  //testLLSC(new PureCPSLLSC, external)
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
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
  //testLLSC(new ImpLLSC, external)
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, TestCases.all ++ varArg)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", noArg, 720))
  //testLLSC(new ImpCPSLLSC, external)
}

class Playground extends TestLLSC {
  //testLLSC(new PureCPSLLSC, TestPrg(mp1048576, "mp1mTest_CPS", "@f", symArg(20), "--disable-solver", nPath(1048576)))
  val llsc = new PureCPSLLSC_Z3
  //testLLSC(llsc, TestPrg(parseFile("benchmarks/demo_benchmarks/nqueen_opt.ll"), "nQueensOpt", "@main", noArg, None, nPath(1363)))
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
