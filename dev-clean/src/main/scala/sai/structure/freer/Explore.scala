package sai.structure.freer

import scala.language.higherKinds
import lms.core.virtualize
import sai.imp.RepNondet
import sai.lmsx.SAIOps
import sai.structure.freer3.Eff.{Eff, ∅, ⊗}
import sai.structure.freer3.Freer
import sai.structure.freer3.Freer.{Comp, Op, Return, ret}
import sai.structure.freer3.OpenUnion.decomp

import scala.collection.immutable.Queue

trait Explore {
  type □[_] //stage
  type N[_] //nondet version TODO: have stage-polymorphic nondet
  type Row <: Eff
  type In
  final type C[+X] = Comp[Row, X]
  final type CIn   = Comp[N ⊗ Row, □[In]]
  type Cont[-A]
  type Sol  //TODO: parameterize over a monoid for In
  type World
  type Worlds  //TODO: parameterize over the data structure, is this also a monoid?

  // this is the handler
  def apply(sol : □[Sol], worlds: □[Worlds])(c : CIn): C[□[Sol]]
  //factors out the scheduling decision, mutually rec. with apply
  def schedule(sol: □[Sol], worlds: □[Worlds]): C[□[Sol]]

  //aux computations, essentially ops lifted to the C[-] monad
  def extend[A](worlds: □[Worlds], xs: □[List[A]], k : Cont[□[A]]): C[□[Worlds]] //TODO will need to account for Manifest view on A
  def nonEmpty (worlds: □[Worlds])                                : C[□[Boolean]]
  def head     (worlds: □[Worlds])                                : C[□[World]]
  def tail     (worlds: □[Worlds])                                : C[□[Worlds]]

  def sol_zero(): □[Sol]
  def sol_join(sol : □[Sol], sol2 : □[Sol]): □[Sol]
  def acc(sol : □[Sol], x : □[In]) : C[□[Sol]]
}

import lms.macros.SourceContext

//old version that tries to support effects coming after nondet
@virtualize
trait StagedExplore2 { self : RepNondet with SAIOps =>
  import NondetListEx$.??
  implicit val msol   : Manifest[Sol]    = implicitly
  implicit val min    : Manifest[In]     = implicitly
  implicit val mworld : Manifest[World]  = implicitly
  implicit val mworlds: Manifest[Worlds] = implicitly

  type N[X] = self.Nondet[X]

  final type □[X] = Rep[X]
  type Row <: Eff
  type In
  final type C[+X] = Comp[Row, X]
  final type CIn   = Comp[N ⊗ Row, □[In]]
  type Sol  //TODO: parameterize over a monoid for In
  type World = Unit => In
  type Worlds   //TODO: parameterize over the data structure, is this also a monoid?


  val sol : □[Sol]
  val worlds :  □[Worlds]

  def apply(c: CIn): C[□[Sol]] = for {
    //TODO it would be more general if we had a "jump out" effect that carries the solution
    //as of now, sol must repr a global mutable state
    _ <- concurrent(c)
  } yield sol

  //TODO nicify with handler combinator
  def concurrent(c: CIn): Comp[Row, □[In]] = c match {
    case Return(x) => for {
      _    <- acc(sol,x)
      y    <- scheduleOrDone(x)
    } yield y

    case Op(u,k) => decomp(u) match {
      case Right(op) => op match {
        case NondetListEx$(??(m, xs)) =>
          def handleNdet[B : Manifest](xs : □[List[B]], k : □[B] => CIn): C[□[In]] = {
            for {
              k_eta  <- etaExpand[B](k)(concurrent)
              _      <- ext(worlds, xs, k_eta)
              res    <- schedule
            } yield res
          }
          handleNdet(xs, k)(m)
        case BinChoice => for {
          k_eta  <- etaExpand[Boolean](k)(concurrent)
          _      <- ext(worlds, k_eta)
          res    <- schedule
        } yield res
      }
      case Left(u) => Op(u) { x => concurrent(k(x))}
    }
  }

  def etaExpand[B : Manifest](k : □[B] => CIn)(h : CIn => Comp[Row, □[In]]): C[□[B => In]] =
    close[B, In, B, In, B => In, Comp[N ⊗ Row, *], Comp[Row, *]](k, { (cls, kx) =>
      h(kx).map(cls)
    })

  val schedule: C[□[In]] = ret(dequeue(worlds)(()))

  def reflect(value : □[In]): C[□[In]] = ???

  def scheduleOrDone(x : □[In]): C[□[In]] = reflect(if (nonEmpty(worlds)) dequeue(worlds)(()) else x)
  /*{
    for {
      sol <- if (nonEmpty(worlds)) dequeue(worlds)(()) else x
      // sol <- reflect(__if(ne)(reify(cur_st)(next) else else reify(cur_st)(done))
    } yield sol
  }*/


  def ext[A : Manifest](worlds: □[Worlds], xs: □[List[A]], k : □[A => In]): C[□[Unit]]
  def ext(worlds: □[Worlds], k : □[Boolean => In]): C[□[Unit]]
  def nonEmpty(worlds: □[Worlds]) : □[Boolean]
  def dequeue (worlds: □[Worlds]) : □[World]

  def acc(sol : □[Sol], x : □[In]) : C[□[Unit]]
}

@virtualize
trait StagedExplore { self : RepNondet with SAIOps =>
  import NondetListEx$.??
  implicit val msol   : Manifest[Sol]    = implicitly
  implicit val min    : Manifest[In]     = implicitly
  implicit val mworlds: Manifest[Worlds] = implicitly
  implicit val mworld: Manifest[World]  = implicitly
  final type □[X] = Rep[X]
  type In
  final type CIn   = Comp[self.Nondet ⊗ ∅, □[In]]
  type Sol  //TODO: parameterize over a monoid for In
  type Worlds   //TODO: parameterize over the data structure, is this also a monoid?
  type Kont[A] = A => Sol
  type World = Kont[Unit]

  val sol    : Var[Sol]
  val worlds : Var[Worlds]

  //TODO nicify with handler combinator
  def apply(c: CIn): □[Sol] = c match {
    case Return(x) =>
      __assign(sol, acc(sol, x))
      schedule(())

    case Op(u,k) => decomp(u) match {
      case Right(op) => op match {
        case NondetListEx$(??(m, xs)) =>
          //some type foo to deal with the existential type on the nondet op
          def handleNdet[B : Manifest](xs : □[List[B]], k : □[B] => CIn): □[Sol] = {
            def callback(b: □[B]): □[Sol] = apply(k(b))
            val kont :  □[Kont[B]] = fun(callback(_))
            ext(xs, kont)
            schedule(())
          }
          handleNdet(xs, k)(m)

        case BinChoice =>
          def callback(b: □[Boolean]): □[Sol] = apply(k(b))
          val kont :  □[Kont[Boolean]] = fun(callback(_))
          ext(kont)
          schedule(())
      }
      case Left(_) => ??? //dead code
    }
  }

  def schedule: □[Unit => Sol] = fun({ x: □[Unit] =>
    if (nonEmpty)
      next(())
    else sol
  })

  def ext[A : Manifest](xs: □[List[A]], k : □[Kont[A]]): □[Unit]
  def ext(k : □[Kont[Boolean]]): □[Unit]
  def nonEmpty : □[Boolean]
  def next: □[World]
  def acc(sol : □[Sol], x : □[In]) : □[Sol]
}

@virtualize
trait StagedListExplore extends StagedExplore { self : RepNondet with SAIOps =>
  type Worlds = List[World]
  type Sol    = List[In]

  val sol    : Var[Sol] = var_new(List[In]())
  val worlds : Var[Worlds] = var_new(List[World]())

  override def nonEmpty: □[Boolean] = !readVar(worlds).isEmpty
  override def next: □[World] = {
    val w : □[World] = worlds.head
    __assign(worlds, worlds.tail)
    w
  }
  override def acc(sol : □[Sol], x : □[In]) : □[Sol] = x :: sol
}

@virtualize
trait StagedBFSExplore extends StagedListExplore { self : RepNondet with SAIOps =>
  override def ext[A : Manifest](xs: □[List[A]], k : □[Kont[A]]): □[Unit] = {
    def mkWorld(x: □[A]): □[World] = fun({ _: □[Unit] => k(x) })
    __assign(worlds, worlds ++ (xs.map(mkWorld(_)) ))
  }

  override def ext(k : □[Kont[Boolean]]): □[Unit] = ext(List(true,false), k)
}

@virtualize
trait StagedDFSExplore extends StagedListExplore { self : RepNondet with SAIOps =>
  override def ext[A : Manifest](xs: □[List[A]], k : □[Kont[A]]): □[Unit] = {
    def mkWorld(x: □[A]): □[World] = fun({ _: □[Unit] => k(x) })
    __assign(worlds, (xs.map(mkWorld(_)) ++ worlds ))
  }

  override def ext(k : □[Kont[Boolean]]): □[Unit] = ext(List(true,false), k)
}

//TODO generalize the impl to stage-polymorphic version, need to abstract over if-then-else semantics, and application on World
class Exhaustive[E <: Eff, A] extends Explore {
  import sai.structure.freer3.NondetList.NondetList$.??
  import sai.structure.freer3.NondetList.{Fail$, Nondet, NondetList, NondetList$}
  type □[X] = X
  type Row = E
  type N[X] = Nondet[X]
  type Cont[-A] = A => CIn
  type In = A

  type Sol       = List[In]     //TODO: parameterize over a monoid for In
  type Worlds    = Queue[World] //TODO: parameterize over the data structure, is this also a monoid?
  type Thunk[+X] = () => X
  type World     = Thunk[CIn]

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
        case NondetList$(??(xs , k)) =>
          // k_rep: Rep[Boolean => List[...]] using LMS's `fun`
          for {
            worlds <- extend(worlds, xs, k)
            sol    <- schedule(sol, worlds)
          } yield sol
      }
      case Left(u) =>
        Op(u) { x => apply(sol,worlds)(k(x))}
    }
  }
  // schedule needs next-stage repr, □[(Sol , Worlds) => Sol]
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

  def extend[A](worlds: Worlds, xs: List[A], k : Cont[A]): C[Worlds] = {
    ret(worlds.enqueue(xs map {x => () => k(x)}))
  }

  def nonEmpty(worlds: Worlds): C[Boolean] = ret(worlds.nonEmpty)
  def head(worlds: Worlds): C[World] = ret(worlds.dequeue._1)
  def tail(worlds: Worlds): C[Worlds] = ret(worlds.dequeue._2)

  def sol_zero() : Sol = Nil
  def sol_join(s1: Sol, s2: Sol): Sol = s1 ++ s2
  def acc(sol : Sol, x : In) : C[Sol] = ret(x :: sol)
}

object Explore {
  def bfs[E <: Eff, A]: Exhaustive[E, A] = new Exhaustive[E, A]
}
