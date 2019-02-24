package sai
package lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

object Interval {
  private val `+∞` = Double.PositiveInfinity
  private val `-∞` = Double.NegativeInfinity
  val ⊤ = Interval(`-∞`, `+∞`)
  val ⊥ = Interval(Double.NaN, Double.NaN)
  val bot = ⊥
  val top = ⊤
}

/* Note: lower bound and upper bound are both closed. */
case class Interval(lb: Double, ub: Double) extends NumAbsDomain {
  require(lb <= ub)

  import Interval._
  override type AD = Interval
  override def toString: String = s"[$lb, $ub]"

  def +(that: Interval): Interval = that match {
    case Interval(lb_, ub_) ⇒ Interval(lb + lb_, ub + ub_)
  }

  def -(that: Interval): Interval = that match {
    case Interval(lb_, ub_) ⇒ Interval(lb - lb_, ub_ - ub)
  }

  def *(that: Interval): Interval = that match {
    case Interval(lb_, ub_) ⇒
      val lb1lb2 = lb * lb_
      val lb1ub2 = lb * ub_
      val ub1lb2 = ub * lb_
      val ub1ub2 = ub * ub_
      val arr = List[Double](lb1lb2, lb1ub2, ub1lb2, ub1ub2)
      Interval(arr.reduce(math.min(_, _)), arr.reduce(math.max(_, _)))
  }

  def /(that: Interval): Interval = this * (that match {
                                              case Interval(lb_, ub_) if !(lb_ <= 0 && 0 <= ub_) ⇒ Interval(1/ub_, 1/lb_)
                                              case Interval(lb_, 0) ⇒ Interval(`-∞`, 1/lb_)
                                              case Interval(0, ub_) ⇒ Interval(1/ub_, `+∞`)
                                              case Interval(lb_, ub_) if lb_ < 0 && 0 < ub_ ⇒ ⊤
                                            })
}
