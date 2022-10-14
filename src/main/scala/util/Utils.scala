package gensym.utils

object Utils {
  def time[R](block: => R): (R, Double) = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val t = (t1 - t0) / 1000000.0 //to ms
    //val t = (t1 - t0) / 1000000000.0 //to ms
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
  def toSec(t: Double): Double = t
  override def toString: String = s"#: ${ts.size}, Mean: ${toSec(mean)}, Median: ${toSec(perc50)}"

  def median_speedup(t2: Timing): Double = {
    val m_t1 = toSec(perc50)
    val m_t2 = t2.toSec(t2.perc50)
    m_t2 / m_t1
  }
}
