package sai.structure.freer

import sai.structure.freer.Coin.{Coin, Coin$, biasedCoin, coinChoice}
import sai.structure.freer.Eff._
import sai.structure.freer.Freer._
import sai.structure.freer.Handlers._
import sai.structure.freer.OpenUnion._
import ∈._

import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.util.Random

object Coin {
  import sai.structure.freer.Nondet.{Choice$, Fail$, Nondet, fail}

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

  def biasedCoin(p: Float): Boolean = Random.nextInt(100) + 1 <= p * 100

  def coinChoice[E <: Eff, A](p : Float) =
    Handler[A, Nondet ⊗ E , A, Coin ⊗ (Nondet ⊗ E)].! {
      case Return(x) => ret(x)
    } (ν[HO[Nondet, Coin ⊗ (Nondet ⊗ ∅), E, Coin ⊗ (Nondet ⊗ E), A]] {
      case Choice$((), k) =>
        //TODO do not understand why perform(Coin$(p)) requires explicit proof
        perform(Coin$(p))(member[Coin, Coin ⊗ (Nondet ⊗ E)]) >>= k
      case Fail$() => fail
    })

  def coinH[E <: Eff, A] =
    Handler[A,Coin ⊗ E,A,E].! {
      case Return(x) => ret(x)
    } {
      case Coin$(p, k) => k(biasedCoin(p))
    }
}

object CoinList {
  import sai.structure.freer.NondetList._

  sealed trait Coin[K]
  case class FlipCoin[A](w: Float) extends Coin[A]

  object Coin$ {
    def apply[A](weight : Float): FlipCoin[A] = FlipCoin(weight)

    def unapply[X,R](p : (Coin[X], X => R)) : Option[(Float, Boolean => R)] = p match {
      case (FlipCoin(w), k) => Some(w, k.asInstanceOf[Boolean => R])
      case _ => None
    }
  }

  def biasedCoin(p: Float): Boolean = Random.nextInt(100) + 1 <= p * 100

  def biasedCoinList[A](xs: List[A], ps: List[Float]): A = {
    assert(ps.foldLeft(0.0f)(_ + _) == 1.0f)
    val r: Int = Random.nextInt(100) + 1
    val acc: Float = 0
    for ((x, p) <- xs.zip(ps)) {
      if (r <= (acc + p) * 100)
        return x
    }
    throw new RuntimeException("cannot interpret probabilities: " + ps)
  }

  import NondetPList$.??

  def run_with_mt[A]: Comp[Nondet ⊗ ∅, A] => Comp[∅, List[A]] =
    handler[Nondet, ∅, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, List[A]] {
      def apply[X] = (_, _) match {
        case NondetPList$(??(xs, ps, k)) =>
          k(biasedCoinList(xs, ps))
      }
    })

  // not using List[A]
  def run_with_mt_single[A]: Comp[Nondet ⊗ ∅, A] => Comp[∅, A] =
    handler[Nondet, ∅, A, A] {
      case Return(x) => ret(x)
    } (new DeepH[Nondet, ∅, A] {
      def apply[X] = (_, _) match {
        case NondetPList$(??(xs, ps, k)) =>
          k(biasedCoinList(xs, ps))
      }
    })
}

object Prob {
  import sai.structure.freer.Nondet.{Choice$, Fail$, Nondet, fail}

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
