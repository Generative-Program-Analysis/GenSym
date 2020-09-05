package sai.structure.freer

import sai.structure.freer3.Eff.{Eff, ⊗}
import sai.structure.freer3.Freer.{Comp, Op, Return, ret}
import sai.structure.freer3.Nondet.{Choice$, Fail$, Nondet}
import sai.structure.freer3.OpenUnion.decomp

import scala.collection.immutable.Queue

trait Explore {
  type R <: Eff
  type In
  final type C[+X] = Comp[R, X]
  final type CIn   = Comp[Nondet ⊗ R, In]
  type Sol  //TODO: parameterize over a monoid for In
  type World
  type Worlds  //TODO: parameterize over the data structure, is this also a monoid?

  // this is the handler
  def apply(sol : Sol, worlds: Worlds)(c : CIn): C[Sol]
  //factors out the scheduling decision, mutually rec. with apply
  def schedule(sol: Sol, worlds: Worlds): C[Sol]

  //aux computations, essentially ops lifted to the C[-] monad
  def extend(worlds: Worlds, w : World): C[Worlds]
  def nonEmpty(worlds: Worlds): C[Boolean]
  def head(worlds: Worlds): C[World]
  def tail(worlds: Worlds): C[Worlds]

  def acc(sol : Sol, x : In) : C[Sol]
}

class Exhaustive[E <: Eff, A] extends Explore {
  type R = E
  type In = A

  type Sol       = List[In]     //TODO: parameterize over a monoid for In
  type Worlds    = Queue[World] //TODO: parameterize over the data structure, is this also a monoid?
  type Thunk[+X] = () => X
  type World     = Thunk[CIn]

  // TODO nicify with the handler combinator
  def apply(sol: Sol, worlds: Worlds)(c: CIn): C[Sol] = c match {
    case Return(x) => for {
      sol1 <- acc(sol,x)
      sol2 <- schedule(sol1, worlds)
    } yield sol2

    case Op(u,k) => decomp(u) match {
      case Right(op) => (op, k) match {
        case Choice$((), k) =>
          for {
            worlds <- extend(worlds, {() => k(true)})
            worlds <- extend(worlds, {() => k(false)})
            sol    <- schedule(sol, worlds)
          } yield sol

        case Fail$() => schedule(sol, worlds)
      }
      case Left(u) =>
        Op(u) { x => apply(sol,worlds)(k(x))}
    }
  }
  def schedule(sol: Sol, worlds: Worlds): C[Sol] = {
    val next : C[Sol] = for {
      w      <- head(worlds)
      worlds <- tail(worlds)
      sol    <- apply(sol, worlds)(w())
    } yield sol
    val done : C[Sol] = ret(sol)
    for {
      ne  <- nonEmpty(worlds)
      sol <- if (ne) next else done
    } yield sol
  }

  def extend(worlds: Worlds, w : World): C[Worlds] = ret(worlds.enqueue(w))
  def nonEmpty(worlds: Worlds): C[Boolean] = ret(worlds.nonEmpty)
  def head(worlds: Worlds): C[World] = ret(worlds.dequeue._1)
  def tail(worlds: Worlds): C[Worlds] = ret(worlds.dequeue._2)

  def acc(sol : Sol, x : In) : C[Sol] = ret(x :: sol)
}

object Explore {
  def bfs[E <: Eff, A]: Exhaustive[E, A] = new Exhaustive[E, A]
}