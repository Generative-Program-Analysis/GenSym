package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object Benchmarks {
  lazy val maze = parseFile("benchmarks/llvm/maze.ll")
  lazy val power = parseFile("benchmarks/llvm/power.ll")
  lazy val add = parseFile("benchmarks/llvm/add.ll")
  lazy val arrayAccess = parseFile("benchmarks/llvm/add.ll")
  lazy val arrayAccessLocal = parseFile("benchmarks/llvm/arrayAccessLocal.ll")
  lazy val arrayGetSet = parseFile("benchmarks/llvm/arrayGetSet.ll")
  lazy val loop = parseFile("benchmarks/llvm/loopTest.ll")
  lazy val branch = parseFile("benchmarks/llvm/branch.ll")
  lazy val makeSymbolicArray = parseFile("benchmarks/llvm/makeSymbolicArray.ll")
  lazy val makeSymbolicDouble = parseFile("benchmarks/llvm/makeSymbolicDouble.ll")
  lazy val switchTestConc = parseFile("benchmarks/llvm/switchTestConc.ll")
  lazy val switchTestSimple = parseFile("benchmarks/llvm/switchTestSimple.ll")

  lazy val sp1 = parseFile("benchmarks/llvm/single_path.ll")
  lazy val sp2 = parseFile("benchmarks/llvm/single_path2.ll")
  lazy val sp3 = parseFile("benchmarks/llvm/single_path3.ll")
  lazy val sp4 = parseFile("benchmarks/llvm/single_path4.ll")
  lazy val sp5 = parseFile("benchmarks/llvm/single_path5.ll")
}

object OOPSLA20Benchmarks {
  lazy val maze = parseFile("benchmarks/oopsla20/maze_test.ll")
  lazy val mp1024 = parseFile("benchmarks/oopsla20/multipath_1024_sym.ll")
  lazy val mp65536 = parseFile("benchmarks/oopsla20/multipath_65536_sym.ll")
  lazy val mp1048576 = parseFile("benchmarks/oopsla20/multipath_1048576_sym.ll")
}
