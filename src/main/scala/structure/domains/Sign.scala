package sai.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

import sai.structure.lattices.Lattices._

trait Sign {
  def +(s: Sign): Sign
  def -(s: Sign): Sign
  def *(s: Sign): Sign
  def /(s: Sign): Sign
}

object Signs {
  val pos = IntPos
  val neg = IntNeg
  val zero = O

  implicit def SignDomainInstance: NumD[Sign] = new NumD[Sign] {
    def +(x: Sign, y: Sign): Sign = x.+(y)
    def -(x: Sign, y: Sign): Sign = x.-(y)
    def *(x: Sign, y: Sign): Sign = x.*(y)
    def /(x: Sign, y: Sign): Sign = x./(y)
  }

  implicit def SignLatticeInstance: Lattice[Sign] = new Lattice[Sign] {
    lazy val bot: Sign = ⊥
    lazy val top: Sign = ⊤
    def ⊑(s1: Sign, s2: Sign): Boolean = (s1, s2) match {
      case (⊥, _) ⇒ true
      case (_, ⊤) ⇒ true
      case (_, _) ⇒ false
    }
    def ⊔(s1: Sign, s2: Sign): Sign = (s1, s2) match {
      case (⊥, s) ⇒ s
      case (s, ⊥) ⇒ s
      case (_, _) ⇒ ⊤
    }
    def ⊓(s1: Sign, s2: Sign): Sign = (s1, s2) match {
      case (⊤, s) ⇒ s
      case (s, ⊤) ⇒ s
      case (_, _) ⇒ ⊥
    }
  }
}

case object ⊤ extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case _ ⇒ ⊤
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case _ ⇒ ⊤
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case _ ⇒ ⊤
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case _ ⇒ ⊤
  }
}

case object ⊥ extends Sign {
  def +(s: Sign): Sign = ⊥
  def -(s: Sign): Sign = ⊥
  def *(s: Sign): Sign = ⊥
  def /(s: Sign): Sign = ⊥
}

case object O extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case IntPos ⇒ IntPos
    case IntNeg ⇒ IntNeg
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case IntPos ⇒ IntNeg
    case IntNeg ⇒ IntPos
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ O
    case O ⇒ O
    case IntPos ⇒ O
    case IntNeg ⇒ O
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case IntPos ⇒ O
    case IntNeg ⇒ O
  }
}

case object IntPos extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ IntPos
    case IntPos ⇒ IntPos
    case IntNeg ⇒ ⊤
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ IntPos
    case IntPos ⇒ ⊤
    case IntNeg ⇒ IntPos
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case IntPos ⇒ IntPos
    case IntNeg ⇒ IntNeg
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case IntPos ⇒ IntPos
    case IntNeg ⇒ IntNeg
  }
}

case object IntNeg extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ IntNeg
    case IntPos ⇒ ⊤
    case IntNeg ⇒ IntNeg
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ IntNeg
    case IntPos ⇒ IntNeg
    case IntNeg ⇒ ⊤
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case IntPos ⇒ IntNeg
    case IntNeg ⇒ IntPos
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case IntPos ⇒ IntNeg
    case IntNeg ⇒ IntPos
  }
}
