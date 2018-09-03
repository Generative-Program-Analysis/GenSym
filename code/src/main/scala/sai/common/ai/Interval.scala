package sai.common.ai.domain.interval

import scala.math._
import scala.Double.{NegativeInfinity, PositiveInfinity}

import sai.common.ai._

object Interval {
  val `+∞` = Double.PositiveInfinity
  val `-∞` = Double.NegativeInfinity
  val ⊤ = Interval(`-∞`, `+∞`)
  val ⊥ = Interval(Double.NaN, Double.NaN)
  val bot = ⊥
  val top = ⊤
}

case class Interval(lb: Double, ub: Double) extends NumAbsDomain {
  import Interval._
  override type AD = Interval
  override def toString: String = s"[$lb, $ub]"

  def +(other: Interval): Interval = other match {
    case Interval(lb_, ub_) ⇒ Interval(lb + lb_, ub + ub_)
  }

  def -(other: Interval): Interval = other match {
    case Interval(lb_, ub_) ⇒ Interval(lb - lb_, ub_ - ub)
  }

  def *(other: Interval): Interval = other match {
    case Interval(lb_, ub_) ⇒
      val lb1lb2 = lb * lb_
      val lb1ub2 = lb * ub_
      val ub1lb2 = ub * lb_
      val ub1ub2 = ub * ub_
      val arr = List[Double](lb1lb2, lb1ub2, ub1lb2, ub1ub2)
      Interval(arr.reduce(math.min(_, _)), arr.reduce(math.max(_, _)))
  }

  def /(other: Interval): Interval = this * (other match {
    case Interval(lb_, ub_) if !(lb_ <= 0 && 0 <= ub_) ⇒ Interval(1/ub_, 1/lb_)
    case Interval(lb_, 0) ⇒ Interval(`-∞`, 1/lb_)
    case Interval(0, ub_) ⇒ Interval(1/ub_, `+∞`)
    case Interval(lb_, ub_) if lb_ < 0 && 0 < ub_ ⇒ ⊤
  })
}

object IntervalTest {
  import Interval._

  def main(args: Array[String]) {
    assert((Interval(1,2) * Interval(2,3)) + Interval(5, 7)
             == Interval(7, 13))
    assert(Interval(79.5, 80.5) / (Interval(1.795, 1.805) * Interval(1.795, 1.805))
             == Interval(24.401286055202153, 24.98428783141037))
  }
}
