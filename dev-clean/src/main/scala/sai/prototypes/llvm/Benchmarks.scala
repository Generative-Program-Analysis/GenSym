package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object Benchmarks {
  //lazy val maze = parseFile("benchmarks/llvm/maze.ll")
  lazy val power = parseFile("benchmarks/llvm/power.ll")
  lazy val add = parseFile("benchmarks/llvm/add.ll")
  lazy val loop = parseFile("benchmarks/llvm/loopTest.ll")
  lazy val unboundedLoop = parseFile("benchmarks/llvm/unboundedLoop.ll")
  lazy val branch = parseFile("benchmarks/llvm/branch.ll")
  lazy val branch2 = parseFile("benchmarks/llvm/branch2.ll")
  lazy val branch3 = parseFile("benchmarks/llvm/branch3.ll")
  lazy val makeSymbolicArray = parseFile("benchmarks/llvm/makeSymbolicArray.ll")
  lazy val makeSymbolic = parseFile("benchmarks/llvm/makeSymbolic.ll")
  lazy val switchTestConc = parseFile("benchmarks/llvm/switchTestConc.ll")
  lazy val switchTestSym = parseFile("benchmarks/llvm/switchTestSym.ll")

  lazy val struct = parseFile("benchmarks/llvm/struct.ll")
  lazy val structAccess = parseFile("benchmarks/llvm/structAccess.ll")
  lazy val structReturn = parseFile("benchmarks/llvm/structReturn.ll")
  lazy val structReturnLong = parseFile("benchmarks/llvm/structReturnLong.ll")
  lazy val complexStruct = parseFile("benchmarks/llvm/complexStruct.ll")
  lazy val arrayAccess = parseFile("benchmarks/llvm/arrayAccess.ll")
  lazy val arrayAccessLocal = parseFile("benchmarks/llvm/arrayAccessLocal.ll")
  lazy val arrayGetSet = parseFile("benchmarks/llvm/arrayGetSet.ll")
  lazy val largeStackArray = parseFile("benchmarks/llvm/largeStackArray.ll")

  lazy val ptrtoint = parseFile("benchmarks/llvm/ptrtoint.ll")
  lazy val ptrpred = parseFile("benchmarks/llvm/ptrpred.ll")
  lazy val flexAddr = parseFile("benchmarks/llvm/memchallenge.ll")

  lazy val funptr = parseFile("benchmarks/llvm/funptr.ll")
  lazy val heapFunptr = parseFile("benchmarks/llvm/heapFunptr.ll")

  lazy val sp1 = parseFile("benchmarks/llvm/single_path.ll")
  lazy val sp2 = parseFile("benchmarks/llvm/single_path2.ll")
  lazy val sp3 = parseFile("benchmarks/llvm/single_path3.ll")
  lazy val sp4 = parseFile("benchmarks/llvm/single_path4.ll")
  lazy val sp5 = parseFile("benchmarks/llvm/single_path5.ll")

  lazy val global = parseFile("benchmarks/llvm/global.ll")
  lazy val varArgChar = parseFile("benchmarks/llvm/varArgChar.ll")
  lazy val varArgInt = parseFile("benchmarks/llvm/varArgInt.ll")
  lazy val trunc = parseFile("benchmarks/llvm/trunc.ll")
  lazy val floatArith = parseFile("benchmarks/llvm/floatArith.ll")
  lazy val floatFp80 = parseFile("benchmarks/llvm/floatFp80.ll")

  lazy val runCommandLine = parseFile("benchmarks/llvm/runCommandLine.ll")

  lazy val strcmpCaller = parseFile("benchmarks/external_lib/strcmpCaller.ll")
  lazy val strcmp = parseFile("benchmarks/external_lib/strcmp.ll")
  lazy val strcpy = parseFile("benchmarks/external_lib/strcpy.ll")
  lazy val externalFun = parseFile("benchmarks/external_lib/externalFun.ll")
  lazy val malloc = parseFile("benchmarks/external_lib/malloc.ll")

  lazy val bubblesort = parseFile("benchmarks/demo_benchmarks/bubblesort.ll")
  lazy val quicksort = parseFile("benchmarks/demo_benchmarks/quicksort.ll")
  lazy val mergesort = parseFile("benchmarks/demo_benchmarks/mergesort.ll")
  lazy val kmpmatcher = parseFile("benchmarks/demo_benchmarks/kmpmatcher.ll")
  lazy val kth = parseFile("benchmarks/demo_benchmarks/kth.ll")
  lazy val binSearch = parseFile("benchmarks/demo_benchmarks/bin_search.ll")
  lazy val knapsack = parseFile("benchmarks/demo_benchmarks/knapsack.ll")

  lazy val simple0 = parseFile("benchmarks/ccbse/simple_0.ll")
  lazy val simple1 = parseFile("benchmarks/ccbse/simple_1.ll")
  lazy val simple2 = parseFile("benchmarks/ccbse/simple_2.ll")

  lazy val assertTest = parseFile("benchmarks/external_lib/assert.ll")
  lazy val assertfixTest = parseFile("benchmarks/external_lib/assert_fix.ll")
  lazy val openTest = parseFile("benchmarks/external_lib/open.ll")
  lazy val closeTest = parseFile("benchmarks/external_lib/close.ll")
  lazy val read1Test = parseFile("benchmarks/external_lib/read1.ll")
  lazy val read2Test = parseFile("benchmarks/external_lib/read2.ll")
  lazy val write1Test = parseFile("benchmarks/external_lib/write1.ll")

  lazy val kleefsminiTest = parseFile("benchmarks/external_lib/klee_fs_mini.ll")
  lazy val kleefsglobalTest = parseFile("benchmarks/external_lib/klee_fs_mini_global.ll")
  // lazy val openAtTest = parseFile("benchmarks/external_lib/openat.ll")
}

object Coreutils {
  lazy val echo = parseFile("benchmarks/coreutils/echo/echo.ll")
  lazy val echoWithLib = parseFile("benchmarks/coreutils/echo/echoStdlibString.ll")
  lazy val trueWithLib = parseFile("benchmarks/coreutils/trueWithLib.ll")
}

object OOPSLA20Benchmarks {
  lazy val maze = parseFile("benchmarks/oopsla20/maze_test.ll")
  lazy val mp1024 = parseFile("benchmarks/oopsla20/multipath_1024_sym.ll")
  lazy val mp65536 = parseFile("benchmarks/oopsla20/multipath_65536_sym.ll")
  lazy val mp1048576 = parseFile("benchmarks/oopsla20/multipath_1048576_sym.ll")
}

object KleeExamples{
  lazy val sort = parseFile("benchmarks/klee_examples/sort.ll")
  lazy val regexp = parseFile("benchmarks/klee_examples/regexp.ll")
  lazy val bin_search = parseFile("benchmarks/klee_examples/bin_search.ll")
}

object TestCCBSE {
  lazy val simple0 = parseFile("benchmarks/ccbse/simple_0.ll")
  lazy val simple1 = parseFile("benchmarks/ccbse/simple_1.ll")
  lazy val simple2 = parseFile("benchmarks/ccbse/simple_2.ll")
  lazy val simple3 = parseFile("benchmarks/ccbse/simple_3.ll")
  lazy val simple4 = parseFile("benchmarks/ccbse/simple_4.ll")
}
