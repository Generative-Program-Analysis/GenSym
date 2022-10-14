package gensym.structure.freer

import Eff._
import Freer._
import Handlers._
import OpenUnion._

import scala.language.{higherKinds, implicitConversions}

object SMT {
  sealed trait SMT[K]
  case class IsSAT(e: String) extends SMT[Boolean]
  case class Concretize(e: String, x: String) extends SMT[Option[Int]]

  def isSat[R <: Eff](e: String)(implicit I: SMT ∈ R): Comp[R, Boolean] =
    perform[SMT, R, Boolean](IsSAT(e))
  def concretize[R <: Eff](e: String, x: String)(implicit I: SMT ∈ R): Comp[R, Option[Int]] =
    perform[SMT, R, Option[Int]](Concretize(e, x))
}
