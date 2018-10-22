package sai

import sai.cps.zerocfa._
import sai.cps.parser._
import sai.utils._

object GenerateCode {
  def main(args: Array[String]) {
    StagedZeroCFATest.printSpecializedCode(Examples.example1)
    //StagedIterZeroCFATest.printSpecializedCode(Examples.example4_expr)
  }
}

object Main {
  def run[R](n: Int, block: => R): Double = {
    assert(n >= 10)
    val dropN = (n * 0.1).toInt //Drop top 10% and worest 10%
    var tsum: List[Double] = Nil
    for (i <- 1 to n) {
      val (result, t) = Utils.time(block)
      //if (i == 1) { println(result) }
      tsum = t::tsum
    }
    tsum.sorted.drop(dropN).take(n - dropN*2).sum / (n - dropN*2).toDouble
  }

  def verify(e: Expr) {
     val s1 = ZeroCFA.analyze(e)

     val ex4ac = ACZeroCFA.compProgram(e)
     val s2 = ACZeroCFA.analyze(ex4ac)

     val ex4 = StagedZeroCFATest.specialize(e)
     ex4.precompile
     val s3 = ex4.eval()

     val ex4while = StagedIterZeroCFATest.specializeAnalysis(e)
     val s4 = ex4while(Map())

     assert(s1.map == s2.map)
     assert(s2.map == s4)
     assert(s3 == s4)
  }

  def compare(e: Expr) {
    val n = 100
    /*
    val ex4fold = new SnippetEx4Fold()
    val t5 = run(n, { ex4fold() })
    println(s"0CFA Staged (fold) average time: $t5")
     */

    val t1 = run(n, { ZeroCFA.analyze(e) })
    println(s"0CFA average time: $t1")

    val ex4ac = ACZeroCFA.compProgram(e)
    val t2 = run(n, { ACZeroCFA.analyze(ex4ac) })
    println(s"0CFA AC average time: $t2")
  
    val ex4 = StagedZeroCFATest.specialize(e)
    ex4.precompile
    val t3 = run(n, { ex4.eval() })
    println(s"0CFA Staged average time: $t3")

    val ex4while = StagedIterZeroCFATest.specializeAnalysis(e)
    val t4 = run(n, { ex4while(Map()) })
    println(s"0CFA Staged (while) average time: $t4")
  
  }

  def main(args: Array[String]) {
    //verify(Examples.example5_expr)
    compare(Examples.example5_expr)
  }
}
