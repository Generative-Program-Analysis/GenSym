package sai.common.ai

trait NumAbsDomain {
  type AD

  def +(that: AD): AD
  def -(that: AD): AD
  def *(that: AD): AD
  def /(that: AD): AD
}

object NumAbsValTest {
  def main(args: Array[String]) {
    assert((Interval(1,2) * Interval(2,3)) + Interval(5, 7)
             == Interval(7, 13))
    assert(Interval(79.5, 80.5) / (Interval(1.795, 1.805) * Interval(1.795, 1.805))
             == Interval(24.401286055202153, 24.98428783141037))
  }
}
