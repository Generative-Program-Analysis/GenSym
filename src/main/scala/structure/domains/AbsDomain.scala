package gensym.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

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
