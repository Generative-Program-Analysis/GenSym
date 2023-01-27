package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.parser.Parser._
import gensym.llvm.Benchmarks._
import gensym.llvm.OOPSLA20Benchmarks._
import gensym.llvm.TestComp.ArrayExamples._
import gensym.llvm.TestComp.ArrayPrograms._

import Config._

/* m: the parsed LLVM module
 * name: test name
 * f: entrance function name
 * config: compile time configuration
 * runOpt: the command line argument to run the compiled executable
 * exp: expected return status of the compiled executable, use the Map combinator in object TestPrg
 */
case class TestPrg(m: Module, name: String, f: String, config: Config, runOpt: Seq[String], exp: Map[String, Any], runCode: Boolean = true) {
  override def toString = s"${m.mname}, $name, $f, $config, $runOpt, $exp"
}

object TestPrg {
  val nPath = "nPath"     // expected number of explored paths
  val nTest = "nTest"     // expteted number of test cases generated
  val minPath = "minPath" // minimal number of paths
  val minTest = "minTest" // minimal number of generated tests
  val status = "status"   // the return status of executable
  def nPath(n: Int): Map[String, Any] = Map(nPath -> n)
  def nTest(n: Int): Map[String, Any] = Map(nTest -> n)
  def minTest(n: Int): Map[String, Any] = Map(minTest -> n)
  def minPath(n: Int): Map[String, Any] = Map(minPath -> n)
  def status(n: Int): Map[String, Any] = Map(status -> n)

  def noOpt: Seq[String] = Seq()

  implicit def lift[T](t: T): Option[T] = Some(t)
  implicit def stringToSeq(s: String): Seq[String] = s.split("\\s+").toSeq
}

import TestPrg._

object TestCases {
  val concrete: List[TestPrg] = List(
    //TestPrg(add, "addTest", "@main", noArg, noOpt, nPath(1)),
    //TestPrg(power, "powerTest", "@main", noArg, noOpt, nPath(1)),

    TestPrg(aliasing, "aliasingTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(global, "globalTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(ptrpred, "ptrPredTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(switchTestConc, "switchConcreteTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(trunc, "truncTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(floatArith, "floatArithTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(floatFp80, "floatFp80Test", "@main", noArg, noOpt, nPath(1)),

    TestPrg(arrayAccess, "arrayAccTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(arrayAccessLocal, "arrayAccLocalTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(arrayGetSet, "arrayGetSetTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(complexStruct, "complexStructTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(structReturnLong, "structReturnLongTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(structAccess, "structAccessTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(structReturn, "structReturnTest", "@main", noArg, noOpt, nPath(1)),

    TestPrg(unprintableCharTest, "unprintableCharTest", "@main", noArg, noOpt, nPath(1)++status(0)),
  )

  val memModel: List[TestPrg] = List(
    TestPrg(funptr, "funptr", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(heapFunptr, "heapFunptr", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(ptrtoint, "ptrToInt", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(flexAddr, "flexAddr", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(nastyStruct, "nastyStruct", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(arrayFlow, "arrayFlow", "@main", noArg, noOpt, nPath(15)++status(0)),
    TestPrg(pointerSymOff, "pointerSymOff", "@main", noArg, noOpt, nPath(4)++status(0)),
  )

  val argv: List[TestPrg] = List(
    TestPrg(argv1Test, "argvConc", "@main", useArgv, "--argv=abcdef", nPath(1)++status(0)),
    TestPrg(argv2Test, "argvSym", "@main", useArgv, "--argv=abc#{3}def", nPath(4)++status(0)),
  )

  val varArg: List[TestPrg] = List(
    TestPrg(varArgInt, "varArgInt", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(varArgCopyInt, "varArgCopyInt", "@main", noArg, noOpt, nPath(1)++status(0)),
    // FIXME(PureGS): Sext an invalid value
    // TestPrg(varArgChar, "varArgChar", "@main", noArg, noOpt, nPath(1))
  )

  val symbolicSimple: List[TestPrg] = List(
    TestPrg(makeSymbolic, "makeSymbolicTest", "@main", noArg, noOpt, nPath(4)),
    TestPrg(branch, "branch1", "@f", symArg(2), noOpt, nPath(4)),
    TestPrg(branch2, "branch2", "@f", symArg(2), noOpt, nPath(4)),
    TestPrg(branch3, "branch3", "@f", symArg(2), noOpt, nPath(4)),
    TestPrg(switchTestSym, "switchSymTest", "@main", noArg, noOpt, nPath(5)),
    //TestPrg(switchMergeSym, "switchMergeTest", "@main", noArg, noOpt, nPath(3)),
    TestPrg(selectTestSym, "selectTest", "@main", noArg, noOpt, nPath(1)),
    TestPrg(i1Bool, "i1Bool", "@main", noArg, noOpt, nPath(5)++status(0)),
    TestPrg(flagTest, "flagTest", "@main", noArg, "--add-sym-file A", nPath(1)++status(0)),
  )

  val symbolicSmall: List[TestPrg] = List(
    TestPrg(bst, "bstTest", "@main", noArg, noOpt, nPath(458)),
    TestPrg(bstArr, "bstArrTest", "@main", noArg, noOpt, nPath(458)),
    TestPrg(mergesort, "mergeSortTest", "@main", noArg, noOpt, nPath(720)),
    TestPrg(bubblesort, "bubbleSortTest", "@main", noArg, noOpt, nPath(24)),
    TestPrg(quicksort, "quickSortTest", "@main", noArg, noOpt, nPath(120)),
    TestPrg(kmpmatcher, "kmp", "@main", noArg, noOpt, nPath(1287)),
    TestPrg(kth, "k_of_st_arrays", "@main", noArg, noOpt, nPath(252)),
    TestPrg(binSearch, "binSearch", "@main", noArg, noOpt, nPath(92)),
    TestPrg(knapsack, "knapsackTest", "@main", noArg, noOpt, nPath(1666)),
    TestPrg(nqueen, "nQueens", "@main", noArg, noOpt, nPath(1363)),
    // The oopsla20 version of maze
    TestPrg(maze, "mazeTest", "@main", noArg, noOpt, nPath(309)),
    TestPrg(mp1024, "mp1024Test", "@f", symArg(10), noOpt, nPath(1024)),
  )

  val symbolicLarge: List[TestPrg] = List(
    TestPrg(mp65536, "mp65kTest", "@f", symArg(16), "--solver=disable", nPath(65536)),
    TestPrg(mp1048576, "mp1mTest", "@f", symArg(20), "--solver=disable", nPath(1048576)),
  )

  val external: List[TestPrg] = List(
    TestPrg(assertTest, "assertTest", "@main", noArg, noOpt, minPath(3)),
    TestPrg(assertfixTest, "assertfix", "@main", noArg, noOpt, nPath(4)),
    TestPrg(assumeTest, "assumeTest", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(getValue, "getValue", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(printfTest, "printfTest", "@main", noArg, noOpt, nPath(1)++status(0))
  )

  val filesys: List[TestPrg] = List(
    TestPrg(openTest, "openTest", "@main", noArg, "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(openSymTest, "openSymTest", "@main", noArg, "--add-sym-file A --add-sym-file B", nPath(3)++status(0)),
    TestPrg(closeTest, "closeTest", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(read1Test, "readTestRetVal", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
    TestPrg(read2Test, "readTestPaths", "@main", noArg, "--sym-file-size 3 --add-sym-file A", nPath(3)++status(0)),
    TestPrg(write1Test, "writeTestPaths", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(3)++status(0)),
    TestPrg(stat1Test, "statTestAssign", "@main", noArg, "", nPath(1)++status(0)),
    TestPrg(stat2Test, "statTestRead", "@main", noArg, "--add-sym-file A", nPath(3)++status(0)),
    TestPrg(stat2Test, "statTestFail", "@main", noArg, noOpt, nPath(1)++status(1)),
    TestPrg(statSymTest, "statSymBr1", "@main", noArg, "--add-sym-file A", nPath(2)++status(0)),
    TestPrg(statSymTest, "statSymBr2", "@main", noArg, "--add-sym-file A --add-sym-file B", nPath(3)++status(0)),
    TestPrg(fstatTest, "fstatTest", "@main", noArg, "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(statfsTest, "statfsTest", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(seekTest, "seekTest", "@main", noArg, "--sym-file-size 10 --add-sym-file A", nPath(1)++status(0)),
    TestPrg(mkdirTest, "mkdirTest", "@main", noArg, noOpt, nPath(1)++status(0)),
    TestPrg(creatUnlinkTest, "creatUnlinkTest", "@main", noArg, noOpt, nPath(2)++status(0)),
    TestPrg(chmodTest, "chmodTest", "@main", noArg, noOpt, nPath(2)++status(0)),
    TestPrg(stdinTest, "stdinTest", "@main", noArg, "--sym-stdin 10", nPath(2)++status(0)),
    TestPrg(ioctlTest, "ioctlTest", "@main", noArg, "--add-sym-file A", nPath(1)++status(0)),
    TestPrg(kleefsminiTest, "kleefsmini", "@main", noArg, noOpt, nPath(2)++status(0)),
    TestPrg(kleefsminiPackedTest, "kleefsminiPackedTest", "@main", noArg, noOpt, nPath(2)++status(0)),
    TestPrg(kleefsglobalTest, "kleefsminiglobal", "@main", noArg, noOpt, nPath(2)++status(0)),
    TestPrg(kleefslib64Test, "kleelib64", "@main", noArg, noOpt, nPath(10)++status(0)),
  )

  val all: List[TestPrg] = concrete ++ memModel ++ symbolicSimple ++ symbolicSmall ++ external ++ argv
}

object CoreutilsPOSIX {
  import ICSE23CoreutilsPOSIX._
  lazy val coreutils: List[TestPrg] = List(
    TestPrg(echo,    "echo_linked_posix",    "@main",  noMainFileOpt, "--argv=./echo.bc --sym-stdout --sym-arg 2 --sym-arg 7", nPath(216136)++status(0)),
    TestPrg(cat,     "cat_linked_posix",     "@main",  noMainFileOpt, "--argv=./cat.bc --sym-stdout --sym-stdin 2 --sym-arg 2", nPath(28567)++status(0)),
    TestPrg(base32,  "base32_linked_posix",  "@main",  noMainFileOpt, "--argv=./base32 --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10621)++status(0)),
    TestPrg(base64,  "base64_linked_posix",  "@main",  noMainFileOpt, "--argv=./base64.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10624)++status(0)),
    TestPrg(comm,    "comm_linked_posix",    "@main",  noMainFileOpt, "--argv=./comm.bc --sym-stdout  --sym-stdin 2 --sym-arg 2  --sym-arg 1 -sym-files 2 2", nPath(23846)++status(0)),
    TestPrg(cut,     "cut_linked_posix",     "@main",  noMainFileOpt, "--argv=./cut.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg  2 -sym-files 2 2", nPath(28481)++status(0)), // or --sym-args 0 2 2
    TestPrg(dirname, "dirname_linked_posix", "@main",  noMainFileOpt, "--argv=./dirname.bc --sym-stdout  --sym-stdin 2 --sym-arg 6 --sym-arg 10", nPath(287386)++status(0)),
    TestPrg(expand,  "expand_linked_posix",  "@main",  noMainFileOpt, "--argv=./expand.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10870)++status(0)),
    TestPrg(fls,   "false_linked_posix",   "@main",  noMainFileOpt, "--argv=./false.bc --sym-stdout  --sym-arg 10", nPath(16)++status(0)),
    TestPrg(tru,    "true_linked_posix",    "@main",  noMainFileOpt, "--argv=./true.bc --sym-stdout  --sym-arg 10", nPath(16)++status(0)),
    TestPrg(fold,    "fold_linked_posix",    "@main",  noMainFileOpt, "--argv=./fold.bc --sym-stdout  --sym-stdin 2 --sym-arg 2    -sym-files 2 2", nPath(11015)++status(0)),
    TestPrg(join,    "join_linked_posix",    "@main",  noMainFileOpt, "--argv=./join.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(25046)++status(0)),
    TestPrg(link,    "link_linked_posix",    "@main",  noMainFileOpt, "--argv=./link.bc --sym-stdout  --sym-stdin 2 --sym-arg 2   --sym-arg 1  --sym-arg 1  -sym-files 2 2", nPath(11233)++status(0)),
    TestPrg(paste,   "paste_linked_posix",   "@main",  noMainFileOpt, "--argv=./paste.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(22622)++status(0)),
    TestPrg(pathchk, "pathchk_linked_posix", "@main",  noMainFileOpt, "--argv=./pathchk.bc --sym-stdout  --sym-stdin 2 --sym-arg 2", nPath(10614)++status(0)),
  )
}

object CoreutilsUClibc {
  import ICSE23CoreutilsPOSIX._
  lazy val coreutils: List[TestPrg] = List(
    TestPrg(echo,    "echo_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./echo.bc --sym-stdout --sym-arg 2 --sym-arg 7", nPath(216136)++status(0)),
    TestPrg(cat,     "cat_linked_uclibc",     "@main",  noMainFileOpt, "--argv=./cat.bc --sym-stdout --sym-stdin 2 --sym-arg 2", nPath(28567)++status(0)),
    TestPrg(base32,  "base32_linked_uclibc",  "@main",  noMainFileOpt, "--argv=./base32 --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10621)++status(0)),
    TestPrg(base64,  "base64_linked_uclibc",  "@main",  noMainFileOpt, "--argv=./base64.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10624)++status(0)),
    TestPrg(comm,    "comm_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./comm.bc --sym-stdout  --sym-stdin 2 --sym-arg 2  --sym-arg 1 -sym-files 2 2", nPath(23846)++status(0)),
    TestPrg(cut,     "cut_linked_uclibc",     "@main",  noMainFileOpt, "--argv=./cut.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg  2 -sym-files 2 2", nPath(28481)++status(0)), // or --sym-args 0 2 2
    TestPrg(dirname, "dirname_linked_uclibc", "@main",  noMainFileOpt, "--argv=./dirname.bc --sym-stdout  --sym-stdin 2 --sym-arg 6 --sym-arg 10", nPath(287386)++status(0)),
    TestPrg(expand,  "expand_linked_uclibc",  "@main",  noMainFileOpt, "--argv=./expand.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 -sym-files 2 2", nPath(10870)++status(0)),
    TestPrg(fls,   "false_linked_uclibc",   "@main",  noMainFileOpt, "--argv=./false.bc --sym-stdout  --sym-arg 10", nPath(16)++status(0)),
    TestPrg(tru,    "true_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./true.bc --sym-stdout  --sym-arg 10", nPath(16)++status(0)),
    TestPrg(fold,    "fold_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./fold.bc --sym-stdout  --sym-stdin 2 --sym-arg 2    -sym-files 2 2", nPath(11015)++status(0)),
    TestPrg(join,    "join_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./join.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(25046)++status(0)),
    TestPrg(link,    "link_linked_uclibc",    "@main",  noMainFileOpt, "--argv=./link.bc --sym-stdout  --sym-stdin 2 --sym-arg 2   --sym-arg 1  --sym-arg 1  -sym-files 2 2", nPath(11233)++status(0)),
    TestPrg(paste,   "paste_linked_uclibc",   "@main",  noMainFileOpt, "--argv=./paste.bc --sym-stdout  --sym-stdin 2 --sym-arg 2 --sym-arg 1  -sym-files 2 2", nPath(22622)++status(0)),
    TestPrg(pathchk, "pathchk_linked_uclibc", "@main",  noMainFileOpt, "--argv=./pathchk.bc --sym-stdout  --sym-stdin 2 --sym-arg 2", nPath(10614)++status(0)),
  )
}
