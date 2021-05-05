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
  lazy val largeStackArray = parseFile("benchmarks/llvm/largeStackArray.ll")

  lazy val sp1 = parseFile("benchmarks/llvm/single_path.ll")
  lazy val sp2 = parseFile("benchmarks/llvm/single_path2.ll")
  lazy val sp3 = parseFile("benchmarks/llvm/single_path3.ll")
  lazy val sp4 = parseFile("benchmarks/llvm/single_path4.ll")
  lazy val sp5 = parseFile("benchmarks/llvm/single_path5.ll")

  lazy val floatArith = parseFile("benchmarks/llvm/floatArith.ll")
  lazy val global = parseFile("benchmarks/llvm/global.ll")
  lazy val struct1 = parseFile("benchmarks/llvm/struct1.ll")
}

object LLSCExpr {
  lazy val structReturnLong = parseFile("benchmarks/llscExpr/structReturnLong.ll")
  lazy val complexStruct = parseFile("benchmarks/llscExpr/complexStruct.ll")
  lazy val strcmpCaller = parseFile("benchmarks/llscExpr/externalTest/strcmpCaller.ll")
  lazy val strcmp = parseFile("benchmarks/llscExpr/externalTest/strcmp.ll")
  lazy val strcpy = parseFile("benchmarks/llscExpr/externalTest/strcpy.ll")
  lazy val externalFun = parseFile("benchmarks/llscExpr/externalTest/externalFun.ll")
  lazy val varargInt = parseFile("benchmarks/llscExpr/varArgInt.ll")
  lazy val malloc = parseFile("benchmarks/llscExpr/externalTest/malloc.ll")
  lazy val trunc = parseFile("benchmarks/llscExpr/trunc.ll")
}

object Coreutils {
  lazy val echo = parseFile("benchmarks/coreutils/echo/echo.ll")
  lazy val echoWithLib = parseFile("benchmarks/coreutils/echo/echoStdlibString.ll")
}

object OOPSLA20Benchmarks {
  lazy val maze = parseFile("benchmarks/oopsla20/maze_test.ll")
  lazy val mp1024 = parseFile("benchmarks/oopsla20/multipath_1024_sym.ll")
  lazy val mp65536 = parseFile("benchmarks/oopsla20/multipath_65536_sym.ll")
  lazy val mp1048576 = parseFile("benchmarks/oopsla20/multipath_1048576_sym.ll")
}
