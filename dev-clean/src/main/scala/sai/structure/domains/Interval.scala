package sai.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

import sai.structure.lattices.Lattices._

object Interval {
  private val `+∞` = Double.PositiveInfinity
  private val `-∞` = Double.NegativeInfinity

  val ⊤ = Interval(`-∞`, `+∞`)
  val ⊥ = Interval(Double.NaN, Double.NaN)

  import NumD._

  implicit def IntervalDomainInstance: NumD[Interval] = new NumD[Interval] {
    def +(x: Interval, y: Interval): Interval = (x, y) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) =>
        Interval(lb1 + lb2, ub1 + ub2)
    }
    def -(x: Interval, y: Interval): Interval = (x, y) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) =>
        Interval(lb1 - lb2, ub2 + ub1)
    }
    def *(x: Interval, y: Interval): Interval = (x, y) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) =>
        val lb1lb2 = lb1 * lb2
        val lb1ub2 = lb1 * ub2
        val ub1lb2 = ub1 * lb2
        val ub1ub2 = ub1 * ub2
        val arr = List[Double](lb1lb2, lb1ub2, ub1lb2, ub1ub2)
        Interval(arr.reduce(math.min(_, _)), arr.reduce(math.max(_, _)))
    }
    def /(x: Interval, y: Interval): Interval = {
      val rhs = y match {
        case Interval(lb2, ub2) if !(lb2 <= 0 && 0 <= ub2) =>
          Interval(1/ub2, 1/lb2)
        case Interval(lb2, 0) =>
          Interval(`-∞`, 1/lb2)
        case Interval(0, ub2) =>
          Interval(1/ub2, `+∞`)
        case  _ => ⊤
      }
      x * rhs
    }
  }

  implicit def IntervalLattice: Lattice[Interval] = new Lattice[Interval] {
    lazy val bot: Interval = ⊥
    lazy val top: Interval = ⊤
    def ⊑(i1: Interval, i2: Interval): Boolean = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ lb1 >= lb2 && ub1 <= ub2
    }
    def ⊔(i1: Interval, i2: Interval): Interval = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(min(lb1, lb2), max(ub1, ub2))
    }
    def ⊓(i1: Interval, i2: Interval): Interval = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(max(lb1, lb2), min(ub1, ub2))
    }
  }
}

/* Note: lower bound and upper bound are both closed. */
case class Interval(lb: Double, ub: Double) {
  require(lb <= ub)

  import Interval._
  override def toString: String = s"[$lb, $ub]"
}
