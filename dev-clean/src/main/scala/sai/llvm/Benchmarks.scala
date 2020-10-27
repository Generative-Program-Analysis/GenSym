package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.Parser._

object Benchmarks {
  val maze = parseFile("benchmarks/llvm/maze.ll")
  val power = parseFile("benchmarks/llvm/power.ll")
  val add = parseFile("benchmarks/llvm/add.ll")
  val arrayAccess = parseFile("benchmarks/llvm/add.ll")
  val arrayAccessLocal = parseFile("benchmarks/llvm/arrayAccessLocal.ll")
  val arrayGetSet = parseFile("benchmarks/llvm/arrayGetSet.ll")
  val loop = parseFile("benchmarks/llvm/loopTest.ll")
  val branch = parseFile("benchmarks/llvm/branch.ll")
  val makeSymbolicArray = parseFile("benchmarks/llvm/makeSymbolicArray.ll")
  val makeSymbolicDouble = parseFile("benchmarks/llvm/makeSymbolicDouble.ll")
  val switchTestConc = parseFile("benchmarks/llvm/switchTestConc.ll")
  val switchTestSimple = parseFile("benchmarks/llvm/switchTestSimple.ll")

  val sp1 = parseFile("benchmarks/llvm/single_path.ll")
  val sp2 = parseFile("benchmarks/llvm/single_path2.ll")
  val sp3 = parseFile("benchmarks/llvm/single_path3.ll")
  val sp4 = parseFile("benchmarks/llvm/single_path4.ll")
  val sp5 = parseFile("benchmarks/llvm/single_path5.ll")
}

object OOPSLA20Benchmarks {
  val maze = parseFile("benchmarks/oopsla20/maze_test.ll")
  val mp1024 = parseFile("benchmarks/oopsla20/multipath_1024_sym.ll")
  val mp65536 = parseFile("benchmarks/oopsla20/multipath_65536_sym.ll")
  val mp1048576 = parseFile("benchmarks/oopsla20/multipath_1048576_sym.ll")
}
