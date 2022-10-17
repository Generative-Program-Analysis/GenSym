package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.parser.Parser._

import gensym.lmsx._
import gensym.utils.Utils.time
import gensym.llvm.Benchmarks._
import gensym.llvm.OOPSLA20Benchmarks._
import gensym.llvm.TestComp.ArrayExamples._
import gensym.llvm.TestComp.ArrayPrograms._

import sys.process._

import org.scalatest.FunSuite

import Config._
import TestPrg._
import TestCases._

class Optimization extends TestGS {
  import scala.collection.mutable.ListBuffer
  import java.io.{File, FileWriter}
  val writer = new FileWriter(new File("opt_exp.csv"), true)
  val N = 5

  def testGS(N: Int, gs: GenSym, tst: TestPrg): Unit = {
    val TestPrg(m, name, f, config, cliArg, exp, runCode) = tst
    test(gs.insName + "_" + name) {
      val code = gs.run(m, gs.insName + "_" + name, f, config)
      val mkRet = code.make(8)
      assert(mkRet == 0, "make failed")
      for (i <- 1 to N) {
        Thread.sleep(1 * 1000)
        val prefix = "numactl -N1 -m1"
        val (output, ret) = code.runWithStatus(cliArg, prefix)
        val resStat = parseOutput(gs.insName, name, output)
        System.out.println(resStat)
        writer.append(s"${tst.toString}\n")
        writer.append(s"$output\n")
        writer.append(s"$resStat\n\n")
        writer.flush()
      }
    }
  }
}

/*
// Test algorithm benchmarks
class TestImpCPSOpt extends Optimization {
  val gs = new ImpCPSGS
  Config.enableOpt
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/kmpmatcher.ll"), "kmp_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(4181)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/mergesort.ll"), "mergeSort_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(5040)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/bubblesort.ll"), "bubbleSort_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(720)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/knapsack.ll"), "knapsack_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(1666)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/nqueen.ll"), "nqueen_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(1363)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/quicksort.ll"), "quicksort_Opt", "@main", noArg, "--cons-indep --solver=z3", nPath(5040)))
}

class TestPureCPSOpt extends Optimization {
  val gs = new PureCPSGS
  Config.enableOpt

  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/mergesort.ll"), "mergeSort_Opt", "@main", noArg, "--solver=z3", nPath(5040)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/bubblesort.ll"), "bubbleSort_Opt", "@main", noArg, "--solver=z3", nPath(720)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/knapsack.ll"), "knapsack_Opt", "@main", noArg, "--solver=z3", nPath(1666)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/kmpmatcher.ll"), "kmp_Opt", "@main", noArg, "--solver=z3", nPath(4181)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/nqueen.ll"), "nqueen_Opt", "@main", noArg, "--solver=z3", nPath(1363)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/quicksort.ll"), "quicksort_Opt", "@main", noArg, "--solver=z3", nPath(5040)))
}
*/

/*
class TestPureCPSNoOpt extends Optimization {
  val gs = new PureCPSGS
  Config.disableOpt

  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/mergesort.ll"), "mergeSort_NoOpt", "@main", noArg, noOpt, nPath(5040)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/bubblesort.ll"), "bubbleSort_NoOpt", "@main", noArg, noOpt, nPath(720)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/knapsack.ll"), "knapsack_NoOpt", "@main", noArg, noOpt, nPath(1666)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/kmpmatcher.ll"), "kmp_NoOpt", "@main", noArg, noOpt, nPath(4181)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/nqueen.ll"), "nqueen_NoOpt", "@main", noArg, noOpt, nPath(1363)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/quicksort.ll"), "quicksort_NoOpt", "@main", noArg, noOpt, nPath(5040)))
}

class TestImpCPSNoOpt extends Optimization {
  val gs = new ImpCPSGS
  Config.disableOpt
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/mergesort.ll"), "mergeSort_NoOpt", "@main", noArg, noOpt, nPath(5040)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/bubblesort.ll"), "bubbleSort_NoOpt", "@main", noArg, noOpt, nPath(720)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/knapsack.ll"), "knapsack_NoOpt", "@main", noArg, noOpt, nPath(1666)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/kmpmatcher.ll"), "kmp_NoOpt", "@main", noArg, noOpt, nPath(4181)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/nqueen.ll"), "nqueen_NoOpt", "@main", noArg, noOpt, nPath(1363)))
  testGS(N, gs, TestPrg(parseFile("benchmarks/opt-experiments/quicksort.ll"), "quicksort_NoOpt", "@main", noArg, noOpt, nPath(5040)))
}
*/
