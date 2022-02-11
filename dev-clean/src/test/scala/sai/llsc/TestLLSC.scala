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

import sys.process._

import org.scalatest.FunSuite

/* m: the parsed LLVM module
 * name: test name
 * f: entrance function name
 * nSym: number of symbolic input to f
 * nPath: expected number of explored paths
 * nTest: expteted number of test cases generated
 * runOpt: the command line argument to run the compiled executable
 * result: expected return status of the compiled executable
 */
case class TestPrg(m: Module, name: String, f: String, nSym: Int, runOpt: Option[String], exp: Map[String, Any])
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
    TestPrg(add, "addTest", "@main", 0, None, nPath(1)),
    TestPrg(power, "powerTest", "@main", 0, None, nPath(1)),
    TestPrg(global, "globalTest", "@main", 0, None, nPath(1)),
    TestPrg(ptrpred, "ptrPredTest", "@main", 0, None, nPath(1)),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", 0, None, nPath(1)),
    TestPrg(trunc, "truncTest", "@main", 0, None, nPath(1)),
    TestPrg(floatArith, "floatArithTest", "@main", 0, None, nPath(1)),
    // FIXME: Support parsing fp80 literals <2022-01-12, David Deng> //
    // TestPrg(floatFp80, "floatFp80Test", "@main", 0, 1),

    TestPrg(arrayAccess, "arrayAccTest", "@main", 0, None, nPath(1)),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", 0, None, nPath(1)),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", 0, None, nPath(1)),
    TestPrg(complexStruct, "complexStructTest", "@main", 0, None, nPath(1)),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", 0, None, nPath(1)),
    TestPrg(structAccess, "structAccessTest", "@main", 0, None, nPath(1)),
    TestPrg(structReturn, "structReturnTest", "@main", 0, None, nPath(1)),
  )

  val memModel: List[TestPrg] = List(
    TestPrg(funptr, "funptr", "@main", 0, None, nPath(1)),
    TestPrg(heapFunptr, "heapFunptr", "@main", 0, None, nPath(1)),
    TestPrg(ptrtoint, "ptrToInt", "@main", 0, None, nPath(1)),
    TestPrg(flexAddr, "flexAddr", "@main", 0, None, nPath(1)),
    TestPrg(nastyStruct, "nastyStruct", "@main", 0, None, nPath(1)),
    // TestPrg(arrayFlow, "arrayFlow", "@main", 0, None, nPath(15)),
  )

  val varArg: List[TestPrg] = List(
    TestPrg(varArgInt, "varArgInt", "@main", 0, None, nPath(1))
    // FIXME(PureLLSC): Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", 0, None, nPath(1))
  )

  val symbolicSimple: List[TestPrg] = List(
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", 0, None, nPath(4)),
    TestPrg(branch, "branch1", "@f", 2, None, nPath(4)),
    TestPrg(branch2, "branch2", "@f", 2, None, nPath(4)),
    TestPrg(branch3, "branch3", "@f", 2, None, nPath(4)),
    TestPrg(switchTestSym, "switchSymTest", "@main", 0, None, nPath(5)),
  )

  val symbolicSmall: List[TestPrg] = List(
    TestPrg(mergesort, "mergeSortTest", "@main", 0, None, nPath(720)),
    TestPrg(bubblesort, "bubbleSortTest", "@main", 0, None, nPath(24)),
    TestPrg(quicksort, "quickSortTest", "@main", 0, None, nPath(120)),
    TestPrg(kmpmatcher, "kmp", "@main", 0, None, nPath(1287)),
    TestPrg(kth, "k_of_st_arrays", "@main", 0, None, nPath(252)),
    TestPrg(binSearch, "binSearch", "@main", 0, None, nPath(92)),
    TestPrg(knapsack, "knapsackTest", "@main", 0, None, nPath(1666)),
    TestPrg(nqueen, "nQueens", "@main", 0, None, nPath(1363)),
    // The oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", 2, None, nPath(309)),
    TestPrg(mp1024, "mp1024Test", "@f", 10, None, nPath(1024)),
  )

  val symbolicLarge: List[TestPrg] = List(
    TestPrg(mp65536, "mp65kTest", "@f", 16, "--disable-solver", nPath(65536)),
    TestPrg(mp1048576, "mp1mTest", "@f", 20, "--disable-solver", nPath(1048576)),
  )

  val external: List[TestPrg] = List(
    TestPrg(assertTest, "assertTest", "@main", 0, None, minPath(3)),
    TestPrg(assertfixTest, "assertfix", "@main", 0, None, nPath(4)),
  )

  val filesys: List[TestPrg] = List(
    TestPrg(openTest, "openTestSucc", "@main", 0, "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(openTest, "openTestFail", "@main", 0, None, nPath(1)++status(1)),
    TestPrg(closeTest, "closeTest", "@main", 0, None, nPath(1)++status(0)),
    TestPrg(read1Test, "readTestRetVal", "@main", 0, "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
    TestPrg(read2Test, "readTestPaths", "@main", 0, "--sym-file-size 3 --add-sym-file A", nPath(3)++status(0)),
    TestPrg(write1Test, "writeTestPaths", "@main", 0, "--sym-file-size 10 --add-sym-file A", nPath(2)++status(0)),
    TestPrg(stat1Test, "statTestAssign", "@main", 0, "", nPath(1)++status(0)),
    TestPrg(stat2Test, "statTestRead", "@main", 0, "--add-sym-file A", nPath(3)++status(0)),
    TestPrg(stat2Test, "statTestFail", "@main", 0, "", nPath(1)++status(1)),
    TestPrg(kleefsminiTest, "kleefsmini", "@main", 0, None, nPath(2)++status(0)),
    TestPrg(kleefsglobalTest, "kleefsminiglobal", "@main", 0, None, nPath(2)++status(0)),
  )

  val all: List[TestPrg] = concrete ++ memModel ++ symbolicSimple ++ symbolicSmall ++ external

  // FIXME: out of range
  // TestPrg(struct, "structTest", "@main", 0, 1),
  // FIXME: seg fault
  // TestPrg(largeStackArray, "largeStackArrayTest", "@main", 0, 1),
  // TestPrg(makeSymbolicArray, "makeSymbolicArrayTest", "@main", 0, 1),
  // TestPrg(ptrtoint, "ptrToIntTest", "@main", 0, 1)
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
    val TestPrg(m, name, f, nSym, cliArgOpt, exp) = tst
    test(name) {
      val code = llsc.newInstance(m, llsc.insName + "_" + name, f, nSym)
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
  //testLLSC(new PureLLSC, TestPrg(arrayAccess, "arrayAccTest", "@main", 0, 1))
  //testLLSC(new PureLLSC, TestCases.external)
}

// FIXME: varArg is problematic for instances other than PureLLSC

class TestPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC, TestCases.all ++ filesys)
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new PureCPSLLSC, external)
}

class TestPureCPSLLSC_Z3 extends TestLLSC {
  testLLSC(new PureCPSLLSC_Z3, TestCases.all ++ filesys)
  //testLLSC(llsc, TestPrg(funptr, "funptr", "@main", 0, 1))
  //testLLSC(llsc, TestPrg(heapFunptr, "heapFunptr", "@main", 0, 1))
  testLLSC(new PureCPSLLSC_Z3, TestPrg(unboundedLoop, "unboundedLoop", "@main", 0, "--timeout=2", minTest(1)))
  testLLSC(new PureCPSLLSC_Z3, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", 0, "--thread=2 --timeout=2", minTest(1)))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, TestCases.all)
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new ImpLLSC, external)
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, TestCases.all)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new ImpCPSLLSC, external)
}
