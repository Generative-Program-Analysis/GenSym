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
import sai.llvm.Benchmarks._
import sai.llvm.OOPSLA20Benchmarks._

import sys.process._

import org.scalatest.FunSuite

case class TestPrg(m: Module, name: String, f: String, nSym: Int, nPath: Int,
                   runOpt: String = "", result: Option[Int] = None)

object TestCases {
  val concrete: List[TestPrg] = List(
    TestPrg(add, "addTest", "@main", 0, 1),
    TestPrg(power, "powerTest", "@main", 0, 1),
    TestPrg(global, "globalTest", "@main", 0, 1),
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", 0, 4),
    TestPrg(ptrpred, "ptrPredTest", "@main", 0, 1),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", 0, 1),
    TestPrg(trunc, "truncTest", "@main", 0, 1),
    TestPrg(floatArith, "floatArithTest", "@main", 0, 1),
    // FIXME: Support parsing fp80 literals <2022-01-12, David Deng> //
    // TestPrg(floatFp80, "floatFp80Test", "@main", 0, 1),

    TestPrg(arrayAccess, "arrayAccTest", "@main", 0, 1),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", 0, 1),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", 0, 1),
    TestPrg(complexStruct, "complexStructTest", "@main", 0, 1),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", 0, 1),
    TestPrg(structAccess, "structAccessTest", "@main", 0, 1),
    TestPrg(structReturn, "structReturnTest", "@main", 0, 1),

    TestPrg(funptr, "funptr", "@main", 0, 1),
    TestPrg(heapFunptr, "heapFunptr", "@main", 0, 1),
  )

  val varArg: List[TestPrg] = List(
    TestPrg(varArgInt, "varArgInt", "@main", 0, 1),
    // FIXME(PureLLSC): Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", 0, 1),
  )

  val symbolicSimple: List[TestPrg] = List(
    TestPrg(branch, "branch1", "@f", 2, 4),
    TestPrg(branch2, "branch2", "@f", 2, 4),
    TestPrg(branch3, "branch3", "@f", 2, 4),
    TestPrg(switchTestSym, "switchSymTest", "@main", 0, 5),
  )

  val symbolicSmall: List[TestPrg] = List(
    TestPrg(mergesort, "mergeSortTest", "@main", 0, 720),
    TestPrg(bubblesort, "bubbleSortTest", "@main", 0, 24),
    TestPrg(quicksort, "quickSortTest", "@main", 0, 120),
    TestPrg(kmpmatcher, "kmp", "@main", 0, 1287),
    TestPrg(kth, "k_of_st_arrays", "@main", 0, 252),
    TestPrg(binSearch, "binSearch", "@main", 0, 92),
    TestPrg(knapsack, "knapsackTest", "@main", 0, 1666),
    // The oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", 2, 309),
    TestPrg(mp1024, "mp1024Test", "@f", 10, 1024),
  )

  val symbolicLarge: List[TestPrg] = List(
    TestPrg(mp65536, "mp65kTest", "@f", 16, 65536, "--disable-solver"),
    TestPrg(mp1048576, "mp1mTest", "@f", 20, 1048576, "--disable-solver"),
  )

  val external: List[TestPrg] = List(
    TestPrg(assertTest, "assertTest", "@main", 0, 3)
  )

  val filesys: List[TestPrg] = List(
    TestPrg(openTest, "openTestSucc", "@main", 0, 1, "--add-sym-file A", Some(0)),
    TestPrg(openTest, "openTestFail", "@main", 0, 1, "", Some(1)),
    TestPrg(closeTest, "closeTest", "@main", 0, 1, "", Some(0)),
    TestPrg(read1Test, "readTestRetVal", "@main", 0, 1, "--sym-file-size 10 --add-sym-file A", Some(0)),
    TestPrg(read2Test, "readTestPaths", "@main", 0, 3, "--sym-file-size 3 --add-sym-file A", Some(0)),
    TestPrg(write1Test, "writeTestPaths", "@main", 0, 2, "--sym-file-size 10 --add-sym-file A", Some(0)),
    TestPrg(kleefsminiTest, "kleefsmini", "@main", 0, 2, "", Some(0)),
  )

  val all: List[TestPrg] = concrete ++ varArg ++ symbolicSimple ++ symbolicSmall ++ external ++ filesys

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
    // [43.4s/46.0s] #blocks: 12/12; #paths: 1666; #threads: 1; #async created: 0; #queries: 7328/1666 (1996)
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
      val t = groups(11).split("/")
      (t(0).toInt, t(1).toInt)
    }
    val cexCacheHit = groups(12).drop(1).dropRight(1).toInt
    TestResult(engine, testName, solverTime, wholeTime, blockCov, pathNum, brQueryNum, testQueryNum, cexCacheHit)
  }

  def testLLSC(llsc: LLSC, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, nSym, expPath, runOpt, expRetOpt) = tst
    test(name) {
      val code = llsc.newInstance(m, llsc.insName + "_" + name, f, nSym)
      code.genAll
      val mkRet = code.make(2)
      assert(mkRet == 0, "make failed")
      val (output, ret) = code.runWithStatus(1, runOpt)
      System.err.println(output)
      val resStat = parseOutput(llsc.insName, name, output)
      System.err.println(resStat)
      val path = resStat.pathNum
      assert(path == expPath, "Unexpected path number")
      if (expRetOpt.nonEmpty) {
        assert(ret == expRetOpt.get, "Unexpected returned status")
      }
      // TODO: check the number of generated test files?
      // TODO: for concrete runs, also check result?
    }
  }

  def testLLSC(llsc: LLSC, tests: List[TestPrg]): Unit = tests.foreach(testLLSC(llsc, _))
}

class TestPureLLSC extends TestLLSC {
  testLLSC(new PureLLSC, TestCases.all)
  //testLLSC(new PureLLSC, TestPrg(arrayAccess, "arrayAccTest", "@main", 0, 1))
  //testLLSC(new PureLLSC, TestCases.external)
}

// FIXME: varArg is problematic for instances other than PureLLSC

class TestPureCPSLLSC extends TestLLSC {
  testLLSC(new PureCPSLLSC, concrete ++ /* varArg ++*/ symbolicSimple ++ symbolicSmall ++ external)
  //testLLSC(new PureCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new PureCPSLLSC, external)
}

class TestPureCPSLLSC_Z3 extends TestLLSC {
  val llsc = new PureCPSLLSC_Z3
  testLLSC(llsc, concrete ++ /* varArg ++*/ symbolicSimple ++ symbolicSmall ++ external)
  //testLLSC(llsc, TestPrg(funptr, "funptr", "@main", 0, 1))
  //testLLSC(llsc, TestPrg(heapFunptr, "heapFunptr", "@main", 0, 1))
}

class TestImpLLSC extends TestLLSC {
  testLLSC(new ImpLLSC, concrete ++ /* varArg ++*/ symbolicSimple ++ symbolicSmall ++ external)
  //testLLSC(new ImpLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new ImpLLSC, external)
}

class TestImpCPSLLSC extends TestLLSC {
  testLLSC(new ImpCPSLLSC, concrete ++ /* varArg ++*/ symbolicSimple ++ symbolicSmall ++ external)
  //testLLSC(new ImpCPSLLSC, TestPrg(mergesort, "mergeSortTest", "@main", 0, 720))
  //testLLSC(new ImpCPSLLSC, external)
}

class TestFS extends TestLLSC {
  testLLSC(new PureLLSC, filesys)
}
