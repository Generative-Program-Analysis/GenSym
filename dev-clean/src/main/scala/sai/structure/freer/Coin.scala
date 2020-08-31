package sai.structure.freer

import sai.structure.freer.Coin.Coin
import sai.structure.freer3.Eff.{Eff, ∅, ⊗}
import sai.structure.freer3.Freer._
import sai.structure.freer3.Handlers.{DeepHO, DeepHOMulti, Handler}
import sai.structure.freer3.Nondet.{Choice$, Fail$, Nondet, fail}
import sai.structure.freer3.OpenUnion._

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

  //TODO we could parameterize over some other container type than list
  def coinChoice[E <: Eff, A](p : Float) =
    Handler[A, Nondet ⊗ E , A, Coin ⊗ (Nondet ⊗ E)].! {
      case Return(x) => ret(x)
    } (ν[DeepHOMulti[Nondet, Coin ⊗ (Nondet ⊗ ∅), E, Coin ⊗ (Nondet ⊗ E), A]] {
      case Choice$((), k) => ???
        //TODO do not understand why fail is ok, but not perform(Coin$(p)) requires explicit proof
        //without it, implicit expansion diverges...
        perform(Coin$(p))(member[Coin, Coin ⊗ (Nondet ⊗ E)]) >>= k
      case Fail$() => fail
    })

  def biasedCoin(p: Float): Boolean = Random.nextInt(100) + 1 <= p * 100

  def coinH[E <: Eff, A] = ???
    /*Handler[A,Coin ⊗ E,A,E].! {
      case Return(x) => ret(x)
    } {
      case Coin$(p, k) =>
        k (biasedCoin(p) )
    }*/

  def foo[E <: Eff](implicit x : RowConcat[Coin ⊗ (Nondet ⊗ ∅), E, Coin ⊗ (Nondet ⊗ E)]): Int = 0

  val x = foo[Nondet ⊗ ∅]
}

object Prob {
  sealed trait Prob[K]
  case class Weight(w: Float) extends Prob[Unit] {
    assert(0f <= w)
    assert(w <= 1.0f)
  }

  def hWeight[E <: Eff, A] = ???
    //TODO this is problematic due to the effect rows ATM
    //Handler[A, Prob ⊗ (Nondet ⊗ E), List[A], Coin ⊗ E].s_! ??? ???

  //TODO Explore handler (Fig. 11)
}