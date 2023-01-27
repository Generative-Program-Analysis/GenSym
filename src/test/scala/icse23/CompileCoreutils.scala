package icse23

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym._
import gensym.lmsx._
import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.Benchmarks._
import gensym.llvm.parser.Parser._
import gensym.Config._
import gensym.TestPrg._
import gensym.TestCases._
import gensym.Constants._
import gensym.utils.Utils.time

import sys.process._
import org.scalatest.FunSuite

class CompileCoreutilsPOSIX extends TestGS {
  import gensym.llvm.parser.Parser._
  Config.enableOpt

  val runtimeOptions = "--output-tests-cov-new --thread=1 --search=random-path --solver=z3 --output-ktest --cons-indep".split(" +").toList.toSeq
  val cases = CoreutilsPOSIX.coreutils.map { t =>
    t.copy(runOpt = runtimeOptions ++ t.runOpt, runCode = false)
  }

  testGS(new ImpCPSGS, cases)
}

class CompileCoreutilsUClibc extends TestGS {
  import gensym.llvm.parser.Parser._
  Config.enableOpt

  val runtimeOptions = "--output-tests-cov-new --thread=1 --search=random-path --solver=z3 --output-ktest --cons-indep".split(" +").toList.toSeq
  val cases = CoreutilsUClibc.coreutils.map { t =>
    t.copy(runOpt = runtimeOptions ++ t.runOpt, runCode = false)
  }

  testGS(new ImpCPSGS, cases)
}