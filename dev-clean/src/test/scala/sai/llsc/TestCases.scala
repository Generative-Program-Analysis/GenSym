package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.lang.llvm.Benchmarks._
import sai.lang.llvm.OOPSLA20Benchmarks._
import sai.lang.llvm.TestComp.ArrayExamples._
import sai.lang.llvm.TestComp.ArrayPrograms._

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
case class TestPrg(m: Module, name: String, f: String, config: Config, runOpt: Option[Seq[String]], exp: Map[String, Any])

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

  def apply(m: Module, name: String, f: String, config: Config, runOpt: String, exp: Map[String, Any]) = {
    new TestPrg(m, name, f, config, runOpt.split("\\s+").toSeq, exp)
  }
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
    TestPrg(seekTest, "seekTest", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
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
