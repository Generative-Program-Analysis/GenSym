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
      val lb1ub1 = lb  * ub
      val lb1ub2 = lb  * ub_
      val lb2ub1 = lb_ * ub
      val lb2ub2 = lb_ * ub_
      val arr = List[Double](lb1ub1, lb1ub2, lb2ub1, lb2ub2)
      Interval(arr.reduce(math.min(_, _)), arr.reduce(math.max(_, _)))
  }

  def /(other: Interval): Interval = this * (other match {
    case Interval(lb_, 0) ⇒ Interval(`-∞`, 1/lb_)
    case Interval(0, ub_) ⇒ Interval(1/ub_, `+∞`)
    case Interval(lb_, ub_) if lb_ <= 0 && 0 <= ub_ ⇒ ⊤
    case Interval(lb_, ub_) ⇒ Interval(1/ub_, 1/lb_)
  })
}
