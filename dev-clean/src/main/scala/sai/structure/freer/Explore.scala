package sai.structure.freer

import scala.language.higherKinds
import lms.core.virtualize
import sai.lmsx.SAIOps
import sai.structure.freer3.Eff.{Eff, ⊗}
import sai.structure.freer3.Freer.{Comp, Op, Return, ret}
import sai.structure.freer3.Nondet.{Choice$, Fail$, Nondet}
import sai.structure.freer3.OpenUnion.decomp

import scala.collection.immutable.Queue

trait Explore {
  type □[_] //stage
  type Row <: Eff
  type In
  final type C[+X] = Comp[Row, X]
  final type CIn   = Comp[Nondet ⊗ Row, □[In]]
  type Cont
  type Sol  //TODO: parameterize over a monoid for In
  type World
  type Worlds  //TODO: parameterize over the data structure, is this also a monoid?

  // this is the handler
  def apply(sol : □[Sol], worlds: □[Worlds])(c : CIn): C[□[Sol]]
  //factors out the scheduling decision, mutually rec. with apply
  def schedule(sol: □[Sol], worlds: □[Worlds]): C[□[Sol]]

  //aux computations, essentially ops lifted to the C[-] monad
  def extend  (worlds: □[Worlds], k : Cont): C[□[Worlds]]
  def nonEmpty(worlds: □[Worlds])          : C[□[Boolean]]
  def head    (worlds: □[Worlds])          : C[□[World]]
  def tail    (worlds: □[Worlds])          : C[□[Worlds]]

  def acc(sol : □[Sol], x : □[In]) : C[□[Sol]]
}

trait ExploreDefaultImpl extends Explore {

}

@virtualize
trait StagedExplore extends ExploreDefaultImpl with SAIOps {
  final type □[X] = Rep[X]
  //TODO staged impl
}


class Exhaustive[E <: Eff, A] extends ExploreDefaultImpl {
  type □[X] = X
  type Row = E
  type In = A

  type Sol       = List[In]     //TODO: parameterize over a monoid for In
  type Worlds    = Queue[World] //TODO: parameterize over the data structure, is this also a monoid?
  type Thunk[+X] = () => X
  type World     = Thunk[CIn]
  type Cont = Boolean => CIn
  // Sol, Worlds will become Rep[_]
  // We don't want Rep[CIn] or anything like staged freer monad
  // TODO nicify with the handler combinator
  def apply(sol: □[Sol], worlds: □[Worlds])(c: CIn): C[□[Sol]] = c match {
    case Return(x) => for {
      sol1 <- acc(sol,x)
      sol2 <- schedule(sol1, worlds)
    } yield sol2

    case Op(u,k) => decomp(u) match {
      case Right(op) => (op, k) match {
        case Choice$((), k) =>
          // k_rep: Rep[Boolean => List[...]] using LMS's `fun`
          for {
            worlds <- extend(worlds, k)
            sol    <- schedule(sol, worlds)
          } yield sol

        case Fail$() => schedule(sol, worlds)
      }
      case Left(u) =>
        Op(u) { x => apply(sol,worlds)(k(x))}
    }
  }
  def schedule(sol: □[Sol], worlds: □[Worlds]): C[□[Sol]] = {
    val next : C[□[Sol]] = for {
      w      <- head(worlds)
      worlds <- tail(worlds)
      sol    <- apply(sol, worlds)(w())
    } yield sol
    val done : C[□[Sol]] = ret(sol)
    // be careful about infinite loop when recursively calling `apply`
    for {
      ne  <- nonEmpty(worlds)
      // ne: Rep[Boolean]
      sol <- if (ne) next else done
      // sol <- reflect(__if(ne) reify(cur_st)(next) else else reify(cur_st)(done))
    } yield sol
  }
  
  def extend(worlds: Worlds, k : Cont): C[Worlds] = ret(worlds.enqueue({() => k(true)}).enqueue({() => k(false)}))
  def nonEmpty(worlds: Worlds): C[Boolean] = ret(worlds.nonEmpty)
  def head(worlds: Worlds): C[World] = ret(worlds.dequeue._1)
  def tail(worlds: Worlds): C[Worlds] = ret(worlds.dequeue._2)

  def acc(sol : Sol, x : In) : C[Sol] = ret(x :: sol)
}

object Explore {
  def bfs[E <: Eff, A]: Exhaustive[E, A] = new Exhaustive[E, A]
}
