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
    TestPrg(add, "addTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(aliasing, "aliasingTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(power, "powerTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(global, "globalTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(ptrpred, "ptrPredTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(trunc, "truncTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(floatArith, "floatArithTest", "@main", Config(0, false), None, nPath(1)),
    // FIXME: Support parsing fp80 literals <2022-01-12, David Deng> //
    // TestPrg(floatFp80, "floatFp80Test", "@main", Config(0, false), 1),

    TestPrg(arrayAccess, "arrayAccTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(complexStruct, "complexStructTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(structAccess, "structAccessTest", "@main", Config(0, false), None, nPath(1)),
    TestPrg(structReturn, "structReturnTest", "@main", Config(0, false), None, nPath(1)),
  )

  val memModel: List[TestPrg] = List(
    TestPrg(funptr, "funptr", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(heapFunptr, "heapFunptr", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(ptrtoint, "ptrToInt", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(flexAddr, "flexAddr", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(nastyStruct, "nastyStruct", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(arrayFlow, "arrayFlow", "@main", Config(0, false), None, nPath(15)++status(0)),
  )

  val argv: List[TestPrg] = List(
    TestPrg(argv1Test, "argvConc", "@main", Config(0, true), "--argv=abcdef", nPath(1)++status(0)),
    TestPrg(argv2Test, "argvSym", "@main", Config(0, true), "--argv=abc#{3}def", nPath(4)++status(0)),
  )

  val varArg: List[TestPrg] = List(
    TestPrg(varArgInt, "varArgInt", "@main", Config(0, false), None, nPath(1))
    // FIXME(PureLLSC): Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", Config(0, false), None, nPath(1))
  )

  val symbolicSimple: List[TestPrg] = List(
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", Config(0, false), None, nPath(4)),
    TestPrg(branch, "branch1", "@f", Config(2, false), None, nPath(4)),
    TestPrg(branch2, "branch2", "@f", Config(2, false), None, nPath(4)),
    TestPrg(branch3, "branch3", "@f", Config(2, false), None, nPath(4)),
    TestPrg(switchTestSym, "switchSymTest", "@main", Config(0, false), None, nPath(5)),
  )

  val symbolicSmall: List[TestPrg] = List(
    TestPrg(mergesort, "mergeSortTest", "@main", Config(0, false), None, nPath(720)),
    TestPrg(bubblesort, "bubbleSortTest", "@main", Config(0, false), None, nPath(24)),
    TestPrg(quicksort, "quickSortTest", "@main", Config(0, false), None, nPath(120)),
    TestPrg(kmpmatcher, "kmp", "@main", Config(0, false), None, nPath(1287)),
    TestPrg(kth, "k_of_st_arrays", "@main", Config(0, false), None, nPath(252)),
    TestPrg(binSearch, "binSearch", "@main", Config(0, false), None, nPath(92)),
    TestPrg(knapsack, "knapsackTest", "@main", Config(0, false), None, nPath(1666)),
    TestPrg(nqueen, "nQueens", "@main", Config(0, false), None, nPath(1363)),
    // The oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", Config(0, false), None, nPath(309)),
    TestPrg(mp1024, "mp1024Test", "@f", Config(10, false), None, nPath(1024)),
  )

  val symbolicLarge: List[TestPrg] = List(
    TestPrg(mp65536, "mp65kTest", "@f", Config(16, false), "--disable-solver", nPath(65536)),
    TestPrg(mp1048576, "mp1mTest", "@f", Config(20, false), "--disable-solver", nPath(1048576)),
  )

  val external: List[TestPrg] = List(
    TestPrg(assertTest, "assertTest", "@main", Config(0, false), None, minPath(3)),
    TestPrg(assertfixTest, "assertfix", "@main", Config(0, false), None, nPath(4)),
  )

  val filesys: List[TestPrg] = List(
    TestPrg(openTest, "openTestSucc", "@main", Config(0, false), "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(openTest, "openTestFail", "@main", Config(0, false), None, nPath(1)++status(1)),
    TestPrg(closeTest, "closeTest", "@main", Config(0, false), None, nPath(1)++status(0)),
    TestPrg(read1Test, "readTestRetVal", "@main", Config(0, false), "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
    TestPrg(read2Test, "readTestPaths", "@main", Config(0, false), "--sym-file-size 3 --add-sym-file A", nPath(3)++status(0)),
    TestPrg(write1Test, "writeTestPaths", "@main", Config(0, false), "--sym-file-size 10 --add-sym-file A", nPath(2)++status(0)),
    TestPrg(stat1Test, "statTestAssign", "@main", Config(0, false), "", nPath(1)++status(0)),
    TestPrg(stat2Test, "statTestRead", "@main", Config(0, false), "--add-sym-file A", nPath(3)++status(0)),
    TestPrg(stat2Test, "statTestFail", "@main", Config(0, false), "", nPath(1)++status(1)),
    TestPrg(kleefsminiTest, "kleefsmini", "@main", Config(0, false), None, nPath(2)++status(0)),
    TestPrg(kleefsglobalTest, "kleefsminiglobal", "@main", Config(0, false), None, nPath(2)++status(0)),
  )

  val all: List[TestPrg] = concrete ++ memModel ++ symbolicSimple ++ symbolicSmall ++ external

  // FIXME: out of range
  // TestPrg(struct, "structTest", "@main", Config(0, false), 1),
  // FIXME: seg fault
  // TestPrg(largeStackArray, "largeStackArrayTest", "@main", Config(0, false), 1),
  // TestPrg(makeSymbolicArray, "makeSymbolicArrayTest", "@main", Config(0, false), 1),
  // TestPrg(ptrtoint, "ptrToIntTest", "@main", Config(0, false), 1)
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
  //testLLSC(new PureLLSC, TestPrg(arrayAccess, "arrayAccTest", "@main", Config(0, false), 1))
  //testLLSC(new PureLLSC, TestCases.external)
}

// FIXME: varArg is problematic for instances other than PureLLSC

class TestPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC, TestCases.all ++ filesys)
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", Config(0, false), 720))
  //testLLSC(new PureCPSLLSC, external)
}

class TestPureCPSLLSC_Z3 extends TestLLSC {
  import sai.lang.llvm.TestComp._
  val llsc = new PureCPSLLSC_Z3
  testLLSC(llsc, TestCases.all ++ filesys)
  //testLLSC(llsc, TestPrg(funptr, "funptr", "@main", 0, 1))
  //testLLSC(llsc, TestPrg(heapFunptr, "heapFunptr", "@main", 0, 1))
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoop", "@main", Config(0, false), "--timeout=2", minTest(1)))
  testLLSC(llsc, TestPrg(unboundedLoop, "unboundedLoopMT", "@main", Config(0, false), "--thread=2 --timeout=2", minTest(1)))
  testLLSC(llsc, TestPrg(arraySet1, "testCompArraySet1", "@main", Config(0, false), None, status(255)))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, TestCases.all)
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", Config(0, false), 720))
  //testLLSC(new ImpLLSC, external)
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, TestCases.all)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", Config(0, false), 720))
  //testLLSC(new ImpCPSLLSC, external)
}

class Playground extends TestLLSC {
  //testLLSC(new PureCPSLLSC_Z3, TestPrg(mergesort, "mergeSortTest", "@main", 0, None, nPath(720)))
  //testLLSC(new PureCPSLLSC, TestPrg(mp1048576, "mp1mTest_CPS", "@f", 20, "--disable-solver", nPath(1048576)))
  import sai.lang.llvm.TestComp._
  val llsc = new PureCPSLLSC_Z3
  //testLLSC(llsc, TestPrg(bubbleSort2Ground, "bubbleSort2Ground", "@main", 0, None, status(255)))
  //testLLSC(llsc, TestPrg(bubbleSortGround2, "bubbleSortGround2", "@main", 0, None, status(255)))
}
