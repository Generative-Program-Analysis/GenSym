package sai

import sai.cps.zerocfa._
import sai.cps.parser._
import sai.utils._

// Note: http://www.larcenists.org/benchmarksAboutR6.html

object GenerateCode {
  def main(args: Array[String]) {
    StagedZeroCFATest.printSpecializedCode(Examples.example1)
    //StagedIterZeroCFATest.printSpecializedCode(Examples.example4_expr)
  }
}

case class Timing(ts: List[Double]) {
  val mean: Double = ts.sum/ts.size
  val sorted_ts: List[Double] = ts.sorted
  val ub: Double = sorted_ts.head
  val lb: Double = sorted_ts.last
  val perc05 = sorted_ts((sorted_ts.size / 20).toInt)
  val perc25 = sorted_ts((sorted_ts.size / 4).toInt)
  val perc50 = sorted_ts((sorted_ts.size / 2).toInt)
  val perc75 = sorted_ts(((sorted_ts.size / 4) * 3).toInt)
  val perc95 = sorted_ts(sorted_ts.size - (sorted_ts.size/20).toInt)
  override def toString: String = s"#: ${ts.size}, Mean: ${mean}, 0/5/25/50/75/95/100: ${ub}/${perc05}/${perc25}/${perc50}/${perc75}/${perc95}/${lb}"
}

object Main {
  def run[R](n: Int, block: => R): Timing = {
    /* warm up*/
    //for (i <- 1 to n) block
    var tsum: List[Double] = Nil
    for (i <- 1 to n) {
      val (result, t) = Utils.time(block)
      tsum = t::tsum
    }
    val dropN = (0.0 * n).toInt
    val dropTs = tsum.sorted.drop(dropN).take(n - dropN*2)
    Timing(dropTs)
  }

  def verify(e: Expr) {
     val s1 = ZeroCFA.analyze(e)

     val ex4ac = ACZeroCFA.compProgram(e)
     val s2 = ACZeroCFA.analyze(ex4ac)

     val ex4 = StagedZeroCFATest.specialize(e)
     ex4.precompile
     val s3 = ex4.eval(())

     val ex4while = StagedIterZeroCFATest.specializeAnalysis(e)
     val s4 = ex4while(Map())

     assert(s1.map == s2.map)
     assert(s2.map == s4)
     assert(s3 == s4)
  }

  def compare(e: Expr) {
    val n = 500
    /*
    val ex4fold = new SnippetEx4Fold()
    val t5 = run(n, { ex4fold() })
    println(s"0CFA Staged (fold) average time: $t5")
    */
    val t1 = run(n, { ZeroCFA.analyze(e) })
    println(s"0CFA time - $t1")

    val ex4ac = ACZeroCFA.compProgram(e)
    val t2 = run(n, { ACZeroCFA.analyze(ex4ac) })
    println(s"0CFA AC time - $t2")
  
    val ex4 = StagedZeroCFATest.specialize(e)
    ex4.precompile
    val t3 = run(n, { ex4.eval(()) })
    println(s"0CFA Staged time - $t3")
  
    val ex4while = StagedIterZeroCFATest.specializeAnalysis(e)
    val t4 = run(n, { ex4while(Map()) })
    println(s"0CFA Staged (while) time - $t4")
  }

  def main(args: Array[String]) {
    //verify(Examples.example5_expr)
    compare(Examples.example5_expr)
  }
}
