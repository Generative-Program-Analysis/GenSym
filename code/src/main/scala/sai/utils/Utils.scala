package sai.utils

object Utils {

  def time[R](block: => R): (R, Double) = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val t = (t1 - t0)//1000000.0
    //println("Elapsed time: " + t + "ms")
    (result, t)
  }

}
