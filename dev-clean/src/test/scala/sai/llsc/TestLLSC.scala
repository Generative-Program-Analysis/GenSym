package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.utils.Utils.time
import scala.collection.immutable.{List => StaticList}
import scala.collection.mutable.HashMap

import sai.llsc.imp.ImpLLSCEngine
import sai.llsc.imp.CPSLLSCEngine

import sys.process._

import org.scalatest.FunSuite

class TestPureLLSC extends FunSuite {
  import sai.llvm.Benchmarks._
  import sai.llvm.OOPSLA20Benchmarks._

  case class TestPrg(m: Module, name: String, f: String, nSym: Int, path: Int, result: Option[Int] = None)
  val tests: List[TestPrg] = List(
    // concrete run
    TestPrg(add, "addTest", "@main", 0, 1),
    TestPrg(power, "powerTest", "@main", 0, 1),
    TestPrg(global, "globalTest", "@main", 0, 1),
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", 0, 4),
    TestPrg(ptrpred, "ptrPredTest", "@main", 0, 1),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", 0, 1),
    TestPrg(trunc, "truncTest", "@main", 0, 1),
    TestPrg(varArgInt, "varArgInt", "@main", 0, 1),

    TestPrg(arrayAccess, "arrayAccTest", "@main", 0, 1),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", 0, 1),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", 0, 1),
    TestPrg(complexStruct, "complexStructTest", "@main", 0, 1),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", 0, 1),
    TestPrg(structAccess, "structAccessTest", "@main", 0, 1),
    TestPrg(structReturn, "structReturnTest", "@main", 0, 1),

    // symbolic run
    TestPrg(branch, "branch1", "@f", 2, 4),
    TestPrg(branch2, "branch2", "@f", 2, 4),
    TestPrg(branch3, "branch3", "@f", 2, 4),
    TestPrg(switchTestSym, "switchSymTest", "@main", 0, 5),
    //
    TestPrg(mergesort, "mergeSortTest", "@main", 0, 720),
    TestPrg(bubblesort, "bubbleSortTest", "@main", 0, 24),
    TestPrg(quicksort, "quickSortTest", "@main", 0, 120),
    TestPrg(kmpmatcher, "kmp", "@main", 0, 1287),
    TestPrg(kth, "k_of_st_arrays", "@main", 0, 252),
    TestPrg(binSearch, "binSearch", "@main", 0, 92),
    TestPrg(knapsack, "knapsackTest", "@main", 0, 1666),

    // this tests the oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", 2, 309),
    TestPrg(mp1024, "mp1024Test", "@f", 10, 1024),

    // FIXME: Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", 0, 1),
    // FIXME: out of range
    // TestPrg(struct, "structTest", "@main", 0, 1),
    // FIXME: seg fault
    // TestPrg(largeStackArray, "largeStackArrayTest", "@main", 0, 1),
    // TestPrg(makeSymbolicArray, "makeSymbolicArrayTest", "@main", 0, 1),
    // TestPrg(ptrtoint, "ptrToIntTest", "@main", 0, 1)
    // FIXME: parsing error
    // TestPrg(floatArith, "floatArithTest", "@main", 0, 1),
  )

  val pureLLSC = new PureLLSC

  tests.foreach { case TestPrg(m, name, f, nSym, expPath, expRetOpt) =>
    test(name) {
      val code = pureLLSC.newInstance(m, name, f, nSym)
      code.genAll
      val mkRet = code.make(2)
      assert(mkRet == 0, "make failed")
      val (output, ret) = code.runWithStatus(1)
      System.err.println(output)
      val path = output.split("\n").last.split(" ").last.toInt
      assert(path == expPath, "Unexpected path number")
      if (expRetOpt.nonEmpty) {
        assert(ret == expRetOpt.get, "Unexpected returned status")
      }
      // TODO: check the number of generated test files?
      // TODO: for concrete runs, also check result?
    }
  }

}