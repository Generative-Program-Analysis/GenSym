package sai.structure.freer

import sai.structure.freer.Coin.Coin
import sai.structure.freer3.Eff.{Eff, ⊗}
import sai.structure.freer3.Freer._
import sai.structure.freer3.Handlers.{DeepHO, Handler}
import sai.structure.freer3.Nondet.{Choice$, Fail$, Nondet}

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
    Handler[A,Nondet ⊗ E , List[A], Coin ⊗ E].! {
      case Return(x) => ret(List(x))
    } (ν[DeepHO[Nondet, Coin, E, List[A]]] {
      case Choice$((), k) =>
        perform(Coin$(p)) >>= k
      case Fail$() => ret(List())
    })

  def biasedCoin(p: Float): Boolean = Random.nextInt(100) + 1 <= p * 100

  def coinH[E <: Eff, A] =
    Handler[A,Coin ⊗ E,A,E].! {
      case Return(x) => ret(x)
    } { //TODO using the PartialFunction[Any,Any] is not a good idea, as typing information is lost for the continuation's return type
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

  def hWeight[E <: Eff, A] = ???
    //TODO this is problematic due to the effect rows ATM
    //Handler[A, Prob ⊗ (Nondet ⊗ E), List[A], Coin ⊗ E].s_! ??? ???

  //TODO Explore handler (Fig. 11)
}