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
  val pos = `ℤ+`
  val neg = `ℤ-`
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
    case `ℤ+` ⇒ `ℤ+`
    case `ℤ-` ⇒ `ℤ-`
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case `ℤ+` ⇒ `ℤ-`
    case `ℤ-` ⇒ `ℤ+`
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ O
    case O ⇒ O
    case `ℤ+` ⇒ O
    case `ℤ-` ⇒ O
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case `ℤ+` ⇒ O
    case `ℤ-` ⇒ O
  }
}

case object `ℤ+` extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ `ℤ+`
    case `ℤ+` ⇒ `ℤ+`
    case `ℤ-` ⇒ ⊤
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ `ℤ+`
    case `ℤ+` ⇒ ⊤
    case `ℤ-` ⇒ `ℤ+`
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case `ℤ+` ⇒ `ℤ+`
    case `ℤ-` ⇒ `ℤ-`
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case `ℤ+` ⇒ `ℤ+`
    case `ℤ-` ⇒ `ℤ-`
  }
}

case object `ℤ-` extends Sign {
  def +(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ `ℤ-`
    case `ℤ+` ⇒ ⊤
    case `ℤ-` ⇒ `ℤ-`
  }
  def -(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ `ℤ-`
    case `ℤ+` ⇒ `ℤ-`
    case `ℤ-` ⇒ ⊤
  }
  def *(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ O
    case `ℤ+` ⇒ `ℤ-`
    case `ℤ-` ⇒ `ℤ+`
  }
  def /(s: Sign): Sign = s match {
    case ⊥ ⇒ ⊥
    case ⊤ ⇒ ⊤
    case O ⇒ ⊥
    case `ℤ+` ⇒ `ℤ-`
    case `ℤ-` ⇒ `ℤ+`
  }
}
