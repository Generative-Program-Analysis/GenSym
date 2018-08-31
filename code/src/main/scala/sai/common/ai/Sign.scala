package sai.common.ai.domain.sign

import sai.common.ai._

abstract class Sign extends NumAbsDomain {
  type AD = Sign
}

object Sign {
  val bot = ⊥
  val top = ⊤
  val pos = `ℤ+`
  val neg = `ℤ-`
  val zero = O
}

case object ⊤ extends Sign {
  def +(s: Sign): Sign = ⊤
  def -(s: Sign): Sign = ⊤
  def *(s: Sign): Sign = ⊤
  def /(s: Sign): Sign = ⊤
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
