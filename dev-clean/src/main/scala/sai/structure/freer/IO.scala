package sai.structure.freer

import Eff._
import Freer._
import Handlers._
import OpenUnion._
import scala.language.{higherKinds, implicitConversions}

object IO {
  sealed trait IO[K]
  case class ReadInt() extends IO[Int]
  case class WriteInt(x: Int) extends IO[Unit]

  def readInt[R <: Eff](implicit I: IO ∈ R): Comp[R, Int] =
    perform[IO, R, Int](ReadInt())
  def writeInt[R <: Eff](n: Int)(implicit I: IO ∈ R): Comp[R, Unit] =
    perform[IO, R, Unit](WriteInt(n))

  object ReadInt$ {
    def unapply[K, R](n: (IO[K], K => R)): Option[(Unit, Int => R)] = n match {
      case (ReadInt(), k) => Some(((), k))
      case _ => None
    }
  }

  object WriteInt${
    def unapply[K, R](n: (IO[K], K => R)): Option[(Int, Unit => R)] = n match {
      case (WriteInt(x), k) => Some((x, k))
      case _ => None
    }
  }

  def run[E <: Eff, A]: Comp[IO ⊗ E, A] => Comp[E, A] =
    handler[IO, E, A, A] {
      case Return(x) => ret(x)
    } (ν[DeepH[IO, E, A]] {
      case ReadInt$((), k) =>
        val n = scala.io.StdIn.readInt()
        k(n)
      case WriteInt$(x, k) =>
        System.out.println(x)
        k(())
    })
}

