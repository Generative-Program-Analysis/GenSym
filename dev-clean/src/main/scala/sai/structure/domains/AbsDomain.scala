package sai.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

import lms.macros.SourceContext
import lms.core.virtualize

import sai.lmsx._

trait NumAbsDomain {
  type AD

  def +(that: AD): AD
  def -(that: AD): AD
  def *(that: AD): AD
  def /(that: AD): AD
}
