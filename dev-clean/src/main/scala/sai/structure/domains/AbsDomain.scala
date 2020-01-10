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

trait NumD[T] {
  def +(x: T, y: T): T
  def -(x: T, y: T): T
  def *(x: T, y: T): T
  def /(x: T, y: T): T
}

object NumD {
  def apply[T](implicit d: NumD[T]): NumD[T] = d

  implicit class NumDOps[D: NumD](x: D) {
    def +(y: D): D = NumD[D].+(x, y)
    def -(y: D): D = NumD[D].-(x, y)
    def *(y: D): D = NumD[D].*(x, y)
    def /(y: D): D = NumD[D]./(x, y)
  }
}
