package sai.evaluation.utils

object Utils {
  def time[R](block: => R): (R, Double) = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val t = (t1 - t0) //1000000.0
    //println("Elapsed time: " + t + "ms")
    (result, t)
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
  val perc95 = sorted_ts(sorted_ts.size - (sorted_ts.size/20).toInt - 1)
  def toSec(t: Double): Double = t / 1000000.0
  override def toString: String = s"#: ${ts.size}, Mean: ${toSec(mean)}, Median: ${toSec(perc50)}"
}
