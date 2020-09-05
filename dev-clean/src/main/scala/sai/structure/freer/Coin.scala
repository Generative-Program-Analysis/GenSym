package sai.structure.freer

import sai.structure.freer.Coin.{Coin, Coin$, biasedCoin, coinChoice}
import sai.structure.freer3.Eff.{Eff, ∅, ⊗}
import sai.structure.freer3.Freer._
import sai.structure.freer3.Handlers.{DeepHO, HO, Handler}
import sai.structure.freer3.Nondet.{Choice$, Fail$, Nondet, fail}
import sai.structure.freer3.OpenUnion._
import ∈._

import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.util.Random

object Coin {
  sealed trait Coin[K]
  case class FlipCoin(weight : Float) extends Coin[Boolean] {
    assert(0f <= weight)
    assert(weight <= 1.0f)
  }

  object Coin$ {
    def apply(weight : Float): FlipCoin = FlipCoin(weight)

    def unapply[X,R](p : (Coin[X], X => R)) : Option[(Float, Boolean => R)] = p match {
      case (FlipCoin(w), k) => Some(w, k.asInstanceOf[Boolean => R])
      case _ => None
    }
  }

  def coinChoice[E <: Eff, A](p : Float) =
    Handler[A, Nondet ⊗ E , A, Coin ⊗ (Nondet ⊗ E)].! {
      case Return(x) => ret(x)
    } (ν[HO[Nondet, Coin ⊗ (Nondet ⊗ ∅), E, Coin ⊗ (Nondet ⊗ E), A]] {
      case Choice$((), k) =>
        //TODO do not understand why perform(Coin$(p)) requires explicit proof
        perform(Coin$(p))(member[Coin, Coin ⊗ (Nondet ⊗ E)]) >>= k

      case Fail$() => fail
    })

  def biasedCoin(p: Float): Boolean = Random.nextInt(100) + 1 <= p * 100

  def coinH[E <: Eff, A] =
    Handler[A,Coin ⊗ E,A,E].! {
      case Return(x) => ret(x)
    } {
      case Coin$(p, k) =>
        k (biasedCoin(p) )
    }
}

object Prob {
  sealed trait Prob[K]
  case class Weight(w: Float) extends Prob[Unit] {
    assert(0f <= w)
    assert(w <= 1.0f)
  }


  object Weight$ {
    def unapply[X,R](p : (Prob[X], X => R)): Option[(Float, Unit => R)] = p match {
      case (Weight(w), k) => Some((w,k))
      case _ => None
    }
  }

 /* def hWeight[E <: Eff, A] =
    Handler[A, Prob ⊗ (Nondet ⊗ E), A, Coin ⊗ (Nondet ⊗ E)].! {
      case Return(x) => ret(x)
    } {
      case Weight$(w, k) =>
        coinChoice(w)(k())
    } */

  //TODO in terms of combinators, this requires a handler which is shallow, handles more than one effect, and carries a parameter
  def hWeight[E <: Eff, A](p : Float = 0f)(c: Comp[Prob ⊗ (Nondet ⊗ E), A]): Comp[Coin ⊗ (Nondet ⊗ E), A] = c match {
    case Return(x) => ret(x)
    case Op(u, k) => decomp(u) match {
      case Right(ex) => (ex, k) match {
        case Weight$(p, k) =>
          hWeight(p)(k(()))
      }
      case Left(u) => decomp(u) match {
        case Right(ex) => (ex, k) match {
          case Choice$((), k) =>
            perform[Coin, Coin ⊗ (Nondet ⊗ E), Boolean](Coin$(p)) >>= { b => hWeight(p)(k(b))}

          case Fail$() => fail
        }
        case Left(u) =>
          Op(u.weakenL[Coin ⊗ (Nondet ⊗ ∅)]) { x => hWeight(p)(k(x))}
      }
    }
  }
}