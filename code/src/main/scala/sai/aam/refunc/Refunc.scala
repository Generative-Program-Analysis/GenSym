package sai.aam.refunc

import scala.util.continuations._
import sai.parser.direct._

object ANFAAM {
  def isAtomic(e: Expr): Boolean =
    e.isInstanceOf[Var] ||
    e.isInstanceOf[Lam] ||
    e.isInstanceOf[Num] ||
    e.isInstanceOf[PrimOp]

  case class Store[K,V](map: Map[K, Set[V]]) {
    def contains(addr: K): Boolean = map.contains(addr)
    def getOrElse(addr: K, default: Set[V]): Set[V] = map.getOrElse(addr, default)
    def apply(addr: K): Set[V] = map(addr)
    def update(addr: K, d: Set[V]): Store[K,V] = { 
      val oldd = map.getOrElse(addr, Set())
      Store[K, V](map ++ Map(addr -> (d ++ oldd)))
    }
    def update(addr: K, sd: V): Store[K,V] = update(addr, Set(sd))
    def join(s: Store[K,V]): Store[K,V] = { 
      var store = this
      for ((addr, vals) <- s.map) {
        store = store.update(addr, vals)
      }
      store
    }
  }

  type Time = List[Expr]

  case class BAddr(x: String, time: Time)
  type Env = Map[String, BAddr]

  abstract class Storable
  case class Clos(v: Lam, env: Env) extends Storable
  case class NumV(i: Int) extends Storable
  type BStore = Store[BAddr, Storable]

  abstract class KAddr
  //case class ContAddr(tgt: Expr, time: Time) extends KAddr
  //case class P4FContAddr(tgt: Expr, tgtEnv: Env, time: Time) extends KAddr
  case class ContAddr(tgt: Expr) extends KAddr
  case class P4FContAddr(tgt: Expr, tgtEnv: Env) extends KAddr
  case object Halt extends KAddr

  case class Frame(x: String, e: Expr, env: Env)
  case class Cont(frame: Frame, kaddr: KAddr)
  type KStore = Store[KAddr, Cont]

  var k: Int = 0

  def allocBind(x: String, time: Time): BAddr = BAddr(x, time)
  //def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = ContAddr(tgtExpr, time)
  def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = ContAddr(tgtExpr)

  case class State(e: Expr, env: Env, bstore: BStore, kstore: KStore, k: KAddr, time: Time)

  def tick(s: State): Time = (s.e::s.time).take(k)

  def inject(e: Expr, env: Env = Map(), bstore: Store[BAddr, Storable] = Store[BAddr, Storable](Map())): State =
    State(e, env, bstore, Store[KAddr, Cont](Map(Halt -> Set())), Halt, List())

  def aeval(e: Expr, env: Env, bstore: BStore): Set[Storable] = e match {
    case Num(i) => Set(NumV(i))
    case Var(x) => bstore(env(x)).toSet
    case lam@Lam(x, body) => Set(Clos(lam, env))
  }

  val atomicEval = aeval _
}

import ANFAAM._

object SmallStep {
  def step(s: State): List[State] = {
    val new_time = tick(s)
    s match {
      case State(Let(x, ae, e), env, bstore, kstore, kaddr, time) if isAtomic(ae) =>
        val baddr = allocBind(x, new_time)
        val new_env = env + (x -> baddr)
        val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, new_env, newBStore, kstore, kaddr, new_time))
      case State(Letrec(bds, body), env, bstore, kstore, kaddr, time) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => { accenv + (bd.x -> allocBind(bd.x, new_time)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: B) => { accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst)) })
        List(State(body, new_env, newBStore, kstore, kaddr, time))
      case State(Let(x, App(f, ae), e), env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newKAddr = allocKont(body, c_env, newBStore, new_time)
          val newKStore = kstore.update(newKAddr, Cont(Frame(x, e, env), kaddr))
          State(body, new_env, newBStore, newKStore, newKAddr, new_time)
        }
      case State(ae, env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Cont(Frame(x, e, f_env), f_kaddr) <- kstore(kaddr).toList) yield {
          val baddr = allocBind(x, new_time)
          val new_env = f_env + (x -> baddr)
          val new_store = bstore.update(baddr, aeval(ae, env, bstore))
          State(e, new_env, new_store, kstore, f_kaddr, new_time)
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd).toList ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive(List(inject(e, env, bstore)), Set())
}

object SmallStepUBStack {
  case class State(e: Expr, env: Env, bstore: BStore, konts: List[Frame], time: Time)

  def tick(s: State): Time = (s.e::s.time).take(k)

  def inject(e: Expr, env: Env = Map(), bstore: Store[BAddr, Storable] = Store[BAddr, Storable](Map())): State =
    State(e, env, bstore, List(), List())

  def step(s: State): List[State] = {
    val new_time = tick(s)
    s match {
      case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
        val baddr = allocBind(x, new_time)
        val new_env = env + (x -> baddr)
        val new_store = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, new_env, new_store, konts, new_time))
      case State(Letrec(bds, body), env, bstore, konts, time) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => { accenv + (bd.x -> allocBind(bd.x, new_time)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: B) => { accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst)) })
        List(State(body, new_env, newBStore, konts, time))
      case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val frame = Frame(x, e, env)
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = bstore.update(baddr, aeval(ae, env, bstore))
          State(body, new_env, new_store, frame::konts, new_time)
        }
      case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
        konts match {
          case Nil => List()
          case Frame(x, e, f_env)::konts =>
            val baddr = allocBind(x, new_time)
            val new_env = f_env + (x -> baddr)
            val new_store = bstore.update(baddr, aeval(ae, env, bstore))
            List(State(e, new_env, new_store, konts, new_time))
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd) ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive(List(inject(e, env, bstore)), Set())
}

object SmallStepP4F {
  def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = P4FContAddr(tgtExpr, tgtEnv)

  def step(s: State): List[State] = {
    val new_time = tick(s)
    s match {
      case State(Let(x, ae, e), env, bstore, kstore, kaddr, time) if isAtomic(ae) =>
        val baddr = allocBind(x, new_time)
        val new_env = env + (x -> baddr)
        val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, new_env, newBStore, kstore, kaddr, new_time))
      case State(Letrec(bds, body), env, bstore, kstore, kaddr, time) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => { accenv + (bd.x -> allocBind(bd.x, new_time)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: B) => { accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst)) })
        List(State(body, new_env, newBStore, kstore, kaddr, time))
      case State(Let(x, App(f, ae), e), env, bstore, kstore, kaddr, time) =>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newKAddr = allocKont(body, c_env, newBStore, new_time)
          val newKStore = kstore.update(newKAddr, Cont(Frame(x, e, env), kaddr))
          State(body, new_env, newBStore, newKStore, newKAddr, new_time)
        }
      case State(ae, env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Cont(Frame(x, e, f_env), f_kaddr) <- kstore(kaddr).toList) yield {
          val baddr = allocBind(x, new_time)
          val new_env = f_env + (x -> baddr)
          val new_store = bstore.update(baddr, aeval(ae, env, bstore))
          State(e, new_env, new_store, kstore, f_kaddr, new_time)
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd) ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive(List(inject(e, env, bstore)), Set())
}

object FusedUBStack {
  import SmallStepUBStack._

  def drive_step(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl =>
      if (seen.contains(hd)) return drive_step(tl, seen)

      val new_time = tick(hd)
      val newStates = hd match {
        case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
          val baddr = allocBind(x, new_time)
          val new_env = env + (x -> baddr)
          val new_store = bstore.update(baddr, aeval(ae, env, bstore))
          List(State(e, new_env, new_store, konts, new_time))
        case State(Letrec(bds, body), env, bstore, konts, time) =>
          val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => { accenv + (bd.x -> allocBind(bd.x, new_time)) })
          val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: B) => { accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst)) })
          List(State(body, new_env, newBStore, konts, new_time))
        case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
          for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
            val frame = Frame(x, e, env)
            val baddr = allocBind(v, new_time)
            val new_env = c_env + (v -> baddr)
            val new_store = bstore.update(baddr, aeval(ae, env, bstore))
            State(body, new_env, new_store, frame::konts, new_time)
          }
        case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
          konts match {
            case Nil => List()
            case Frame(x, e, f_env)::konts =>
              val baddr = allocBind(x, new_time)
              val new_env = f_env + (x -> baddr)
              val new_store = bstore.update(baddr, aeval(ae, env, bstore))
              List(State(e, new_env, new_store, konts, new_time))
          }
      }
      drive_step(newStates ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive_step(List(inject(e)), Set())
}

/*
 Like Danvy's showed the first step of transforming CEK to interpreter is to fuse
 the step and drive function.
 We do the same thing as the first step for abstract CESK.
 But the fused AAM actually implicitly does two more jobs than a concrete CEK machine:
 1) it handles non-determinism using a todo list, the order we use todo list doesn't matter,
 because the continuation in the state indicates what should do;
 2) it also use a set of state `seen` as a cache to implement collecting semantics and
 to prevent non-terminating computation. The state space is finite, so eventually we will
 reach a fixed-point of `seen` that contains all reachable states.
 The assumption behind `seen` is if we have saw this state, then it means we have
 saw its successors.
 In big-step version, we will have the same assumption: if we have saw this configuration,
 then it means we have also saw the values of this term.

 Unfortunately these two jobs in AAM lack of data representation.
 */

object DisentangledUBStack {
  import SmallStepUBStack._

  def aval(state: State, todo: List[State], seen: Set[State]): Set[State] = {
    if (seen.contains(state)) return collect(todo, seen)

    val new_time = tick(state)
    state match {
      case State(Let(x, App(f, ae), e), env, bstore, konts, time) if isAtomic(ae) =>
        val newStates = for (Clos(Lam(v, body), c_env) <- atomicEval(f, env, bstore).toList) yield {
          val frame = Frame(x, e, env)
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = bstore.update(baddr, atomicEval(ae, env, bstore))
          State(body, new_env, new_store, frame::konts, new_time)
        }
        collect(newStates ++ todo, seen + state)
      case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
        continue(konts, atomicEval(ae, env, bstore), bstore, new_time, todo, seen + state)
    }
  }

  def collect(todo: List[State], seen: Set[State]): Set[State] = {
    todo match {
      case Nil => seen
      case hd::tl => aval(hd, tl, seen)
    }
  }

  def continue(konts: List[Frame], ds: Set[Storable], bstore: BStore, time: Time,
               todo: List[State], seen: Set[State]): Set[State] = konts match {
    case Nil => collect(todo, seen)
    case Frame(x, e, f_env)::konts =>
      val baddr = allocBind(x, time)
      val new_env = f_env + (x -> baddr)
      val new_store = bstore.update(baddr, ds)
      val newState = State(e, new_env, new_store, konts, time)
      collect(newState::todo, seen)
  }

  def analyze(e: Expr): Set[State] = aval(inject(e), List(), Set())
}


object LinearSmallStepUBStack {
  import SmallStepUBStack._

  case class NDCont(cls: List[Clos], argvs: Set[Storable], store: BStore, time: Time, frames: List[Frame])

  case class NDState(e: Expr, env: Env, bstore: BStore, konts: List[Frame], time: Time, ndk: List[NDCont]) {
    def toState: State = State(e, env, bstore, konts, time)
  }

  def tick(s: NDState): Time = (s.e::s.time).take(k)

  def inject(e: Expr, env: Env = Map(), bstore: Store[BAddr, Storable] = Store[BAddr, Storable](Map())): NDState =
    NDState(e, env, bstore, List(), List(), List())

  def step(nds: NDState): NDState = {
    val new_time = tick(nds)
    nds match {
      case NDState(Let(x, App(f, ae), e), env, bstore, konts, time, ndk) =>
        val closures = atomicEval(f, env, bstore).toList.asInstanceOf[List[Clos]]
        val Clos(Lam(v, body), c_env) = closures.head
        val frame = Frame(x, e, env)
        val baddr = allocBind(v, new_time)
        val new_env = c_env + (v -> baddr)
        val argvs = atomicEval(ae, env, bstore)
        val new_store = bstore.update(baddr, argvs)
        val new_frames = frame::konts
        val new_ndk = NDCont(closures.tail, argvs, bstore, new_time, new_frames)::ndk
        NDState(body, new_env, new_store, new_frames, new_time, new_ndk)

      case NDState(ae, env, bstore, konts, time, ndk) if isAtomic(ae) =>
        konts match {
          case Nil => ndk match {
            case NDCont(Nil, _, _, _, _)::ndk => 
              NDState(ae, env, bstore, konts, time, ndk) //NOTE: konts is Nil
            case NDCont(cls, argvs, bstore, time, frames)::ndk => 
              val Clos(Lam(v, body), c_env) = cls.head
              val baddr = allocBind(v, time)
              val new_env = c_env + (v -> baddr)
              val new_store = bstore.update(baddr, argvs)
              NDState(body, new_env, new_store, frames, time, NDCont(cls.tail, argvs, bstore, time, frames)::ndk)
          }
          case Frame(x, e, f_env)::konts =>
            val baddr = allocBind(x, new_time)
            val new_env = f_env + (x -> baddr)
            val new_store = bstore.update(baddr, aeval(ae, env, bstore))
            NDState(e, new_env, new_store, konts, new_time, ndk)
        }
    }
  }

  def drive(nds: NDState, seen: Set[State]): Set[State] = {
    nds match {
      case NDState(ae, _, _, Nil, _, Nil) if isAtomic(ae) => seen
      case nds =>
        val s = nds.toState
        if (seen.contains(s)) drive(step(nds), seen)
        else drive(step(nds), seen + s)
    }
  }

  def analyze(e: Expr): Set[State] = drive(inject(e), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive(inject(e, env, bstore), Set())
}

object FusedLinearSmallStepUBStack {
  import SmallStepUBStack._

  case class NDCont(cls: List[Clos], argvs: Set[Storable], store: BStore, time: Time, frames: List[Frame])

  case class NDState(e: Expr, env: Env, bstore: BStore, konts: List[Frame], time: Time, ndk: List[NDCont]) {
    def toState: State = State(e, env, bstore, konts, time)
  }

  def tick(s: NDState): Time = (s.e::s.time).take(k)

  def inject(e: Expr, env: Env = Map(), bstore: Store[BAddr, Storable] = Store[BAddr, Storable](Map())): NDState =
    NDState(e, env, bstore, List(), List(), List())

  def drive_step(nds: NDState, seen: Set[State]): Set[State] = {
    nds match {
      case NDState(ae, _, _, Nil, _, Nil) if isAtomic(ae) => seen
      case nds =>
        val new_time = tick(nds)
        val new_ndstate = nds match {
          case NDState(Let(x, App(f, ae), e), env, bstore, konts, time, ndk) =>
            val closures = atomicEval(f, env, bstore).toList.asInstanceOf[List[Clos]]
            val Clos(Lam(v, body), c_env) = closures.head
            val frame = Frame(x, e, env)
            val baddr = allocBind(v, new_time)
            val new_env = c_env + (v -> baddr)
            val argvs = atomicEval(ae, env, bstore)
            val new_store = bstore.update(baddr, argvs)
            val new_frames = frame::konts
            val new_ndk = NDCont(closures.tail, argvs, bstore, new_time, new_frames)::ndk
            NDState(body, new_env, new_store, new_frames, new_time, new_ndk)

          case NDState(ae, env, bstore, konts, time, ndk) if isAtomic(ae) =>
            konts match {
              case Nil => ndk match {
                case NDCont(Nil, _, _, _, _)::ndk => 
                  NDState(ae, env, bstore, konts, time, ndk) //NOTE: konts is Nil
                case NDCont(cls, argvs, bstore, time, frames)::ndk => 
                  val Clos(Lam(v, body), c_env) = cls.head
                  val baddr = allocBind(v, time)
                  val new_env = c_env + (v -> baddr)
                  val new_store = bstore.update(baddr, argvs)
                  NDState(body, new_env, new_store, frames, time, NDCont(cls.tail, argvs, bstore, time, frames)::ndk)
              }
              case Frame(x, e, f_env)::konts =>
                val baddr = allocBind(x, new_time)
                val new_env = f_env + (x -> baddr)
                val new_store = bstore.update(baddr, aeval(ae, env, bstore))
                NDState(e, new_env, new_store, konts, new_time, ndk)
            }
        }
        val s = nds.toState
        if (seen.contains(s)) drive_step(new_ndstate, seen)
        else drive_step(new_ndstate, seen + s)
    }
  }

  def analyze(e: Expr): Set[State] = drive_step(inject(e), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive_step(inject(e, env, bstore), Set())
}

object DisenLinearSmallStepUBStack {
  import SmallStepUBStack._

  case class NDCont(cls: List[Clos], argvs: Set[Storable], store: BStore, time: Time, frames: List[Frame])

  case class NDState(e: Expr, env: Env, bstore: BStore, konts: List[Frame], time: Time, ndk: List[NDCont]) {
    def toState: State = State(e, env, bstore, konts, time)
  }

  def tick(s: NDState): Time = (s.e::s.time).take(k)

  def inject(e: Expr, env: Env = Map(), bstore: Store[BAddr, Storable] = Store[BAddr, Storable](Map())): NDState =
    NDState(e, env, bstore, List(), List(), List())

  def drive_step(nds: NDState, seen: Set[State]): Set[State] = {
    nds match {
      case NDState(ae, _, _, Nil, _, Nil) if isAtomic(ae) => seen
      case nds =>
        val s = nds.toState
        val new_seen = if (seen.contains(s)) seen else seen + s
        val new_time = tick(nds)
        nds match {
          case NDState(Let(x, App(f, ae), e), env, bstore, konts, time, ndk) =>
            val closures = atomicEval(f, env, bstore).toList.asInstanceOf[List[Clos]]
            val Clos(Lam(v, body), c_env) = closures.head
            val frame = Frame(x, e, env)
            val baddr = allocBind(v, new_time)
            val new_env = c_env + (v -> baddr)
            val argvs = atomicEval(ae, env, bstore)
            val new_store = bstore.update(baddr, argvs)
            val new_frames = frame::konts
            val new_ndk = NDCont(closures.tail, argvs, bstore, new_time, new_frames)::ndk
            drive_step(NDState(body, new_env, new_store, new_frames, new_time, new_ndk), new_seen)

          case NDState(ae, env, bstore, konts, time, ndk) if isAtomic(ae) =>
            continue(nds, new_seen)
        }
    }
  }

  def continue(nds: NDState, seen: Set[State]): Set[State] = {
    val NDState(ae, env, bstore, konts, time, ndk) = nds
    val new_time = tick(nds)
    konts match {
      case Nil => ndcontinue(nds, seen)
      case Frame(x, e, f_env)::konts =>
        val baddr = allocBind(x, new_time)
        val new_env = f_env + (x -> baddr)
        val new_store = bstore.update(baddr, atomicEval(ae, env, bstore))
        drive_step(NDState(e, new_env, new_store, konts, new_time, ndk), seen)
    }
  }

  def ndcontinue(nds: NDState, seen: Set[State]): Set[State] = {
    val NDState(ae, env, bstore, konts, time, ndk) = nds
    ndk match {
      case NDCont(Nil, _, _, _, _)::ndk => 
        drive_step(NDState(ae, env, bstore, konts, time, ndk), seen)
      case NDCont(cls, argvs, bstore, time, frames)::ndk => 
        val Clos(Lam(v, body), c_env) = cls.head
        val baddr = allocBind(v, time)
        val new_env = c_env + (v -> baddr)
        val new_store = bstore.update(baddr, argvs)
        drive_step(NDState(body, new_env, new_store, frames, time, 
                           NDCont(cls.tail, argvs, bstore, time, frames)::ndk),
                   seen)
    }
  }

  def analyze(e: Expr): Set[State] = drive_step(inject(e), Set())

  def analyze(e: Expr, env: Env, bstore: BStore): Set[State] = drive_step(inject(e, env, bstore), Set())
}


object FusedUBStackCache {
  import SmallStepUBStack._

  case class Cache(map: Store[State, State]) {
    def get(key: State): Set[State] = map(key)
    def join(c: Cache): Cache = Cache(map.join(c.map))
    def contains(key: State): Boolean = map.contains(key)
    def update(key: State, states: Set[State]): Cache = Cache(map.update(key, states))
    def update(key: State, state: State): Cache = Cache(map.update(key, state))
  }
  def mtCache = Cache(Store[State, State](Map()))

  def aval(todo: List[State], cache: Cache): Cache = {
    todo match {
      case Nil => cache
      case state::todo =>
        if (cache.contains(state)) return aval(todo, cache)

        val new_time = tick(state)
        val newStates = state match {
          case State(Let(x, App(f, ae), e), env, bstore, konts, time) if isAtomic(ae) =>
            for (Clos(Lam(v, body), c_env) <- atomicEval(f, env, bstore).toList) yield {
              val frame = Frame(x, e, env)
              val baddr = allocBind(v, new_time)
              val new_env = c_env + (v -> baddr)
              val new_store = bstore.update(baddr, atomicEval(ae, env, bstore))
              State(body, new_env, new_store, frame::konts, new_time)
            }
          case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
            konts match {
              case Nil => List()
              case Frame(x, e, f_env)::konts =>
                val baddr = allocBind(x, new_time)
                val new_env = f_env + (x -> baddr)
                val new_store = bstore.update(baddr, aeval(ae, env, bstore))
                List(State(e, new_env, new_store, konts, new_time))
            }
        }
        val new_cache = cache.update(state, newStates.toSet)
        aval(newStates ++ todo, new_cache)
    }
  }

  def analyze(e: Expr): Cache = aval(List(inject(e)), mtCache)
}

object RefuncNoCacheNoTime {
  /**
    This version executes a depth first exploration and back
    to the fork point, and then choose the next closure.
    CAN NOT use different time when getting back to the fork point.
    */
  import SmallStepUBStack._
  
  case class VS(vals: Set[Storable], store: BStore)
  type NCont = VS => VS
  type Cont  = NCont => (VS => VS)

  def alloc0(x: String) = allocBind(x, List())

  def aval(e: Expr, env: Env, store: BStore, cont: Cont, ncont: NCont): VS = e match {
    case Let(x, ae, e) if isAtomic(ae) =>
      val baddr = alloc0(x)
      val new_env = env + (x -> baddr)
      val new_store = store.update(baddr, aeval(ae, env, store))
      aval(e, new_env, new_store, cont, ncont)

    case Letrec(bds, body) =>
      val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => {
                                       accenv + (bd.x -> alloc0(bd.x))
                                     })
      val new_store = bds.foldLeft(store)((accbst: BStore, bd: B) => {
                                            accbst.update(alloc0(bd.x), aeval(bd.e, new_env, accbst))
                                          })
      aval(body, new_env, new_store, cont, ncont)

    case Let(x, App(f, ae), e) =>
      val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]

      def ndCont(closures: List[Clos])(r: NCont)(vs: VS): VS = {
        closures match {
          case Nil => r(vs)
          case Clos(Lam(v, body), c_env)::rest =>
            val baddr = alloc0(v)
            val new_env = c_env + (v -> baddr)
            val new_store = vs.store.update(baddr, aeval(ae, env, vs.store))
            aval(body, new_env, new_store, ndCont(rest), (vs1: VS) => {
                   r(VS(vs.vals ++ vs1.vals, vs1.store))
                 })
        }
      }

      def mtCont(vs: VS): VS = {
        val baddr = alloc0(x)
        val new_env = env + (x -> baddr)
        val new_store = vs.store.update(baddr, vs.vals)
        aval(e, new_env, new_store, cont, ncont)
      }

      ndCont(closures)(mtCont)(VS(Set(), store))

    case ae if isAtomic(ae) =>
      val ds = aeval(ae, env, store)
      cont(ncont)(VS(ds, store))
  }

  def analyze(e: Expr) = aval(e, Map(), Store[BAddr, Storable](Map()), (r: NCont) => (vs: VS) => vs, (vs: VS) => vs)
}

object RefuncNoCacheNoTime1 {
  /* Handle non-determinism */
  import SmallStepUBStack._

  case class VS(vals: Set[Storable], store: BStore)
  type NCont = VS => Set[VS]
  type Cont  = NCont => (VS => Set[VS])

  def alloc0(x: String) = allocBind(x, List())

  def aval(e: Expr, env: Env, store: BStore, cont: Cont, ncont: NCont): Set[VS] = e match {
    case Let(x, ae, e) if isAtomic(ae) =>
      val baddr = alloc0(x)
      val new_env = env + (x -> baddr)
      val new_store = store.update(baddr, aeval(ae, env, store))
      aval(e, new_env, new_store, cont, ncont)

    case Letrec(bds, body) =>
      val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => {
                                        accenv + (bd.x -> alloc0(bd.x))
                                      })
      val new_store = bds.foldLeft(store)((accbst: BStore, bd: B) => {
                                            accbst.update(alloc0(bd.x), aeval(bd.e, new_env, accbst))
                                          })
      aval(body, new_env, new_store, cont, ncont)

    case Let(x, App(f, ae), e) =>
      val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]

      def nd(closures: List[Clos]): Set[VS] = {
        val Clos(Lam(v, body), c_env)::rest = closures
        val baddr = alloc0(v)
        val new_env = c_env + (v -> baddr)
        val new_store = store.update(baddr, aeval(ae, env, store))
        val new_cont = (r: NCont) => (vs: VS) => {
          val baddr = alloc0(x)
          val new_env = env + (x -> baddr)
          val new_store = vs.store.update(baddr, vs.vals)
          aval(e, new_env, new_store, cont, r)
        }
        val new_ncont = if (rest.isEmpty) ncont else (vs: VS) => {
          println(s"halt: ${vs}")
          nd(rest) + vs
        }
        aval(body, new_env, new_store, new_cont, new_ncont)
      }

      nd(closures)
    case ae if isAtomic(ae) =>
      val ds = aeval(ae, env, store)
      cont(ncont)(VS(ds, store))
  }

  def analyze(e: Expr, env: Env = Map(), store: BStore = Store[BAddr, Storable](Map())) =
    aval(e, env, store, (r: NCont) => (vs: VS) =>  r(vs), (vs: VS) => Set(vs))
}

object RefuncNoCacheNoTime2 {
  import SmallStepUBStack._
  import RefuncNoCacheNoTime1._

  //case class VS(vals: Set[Storable], store: BStore)
  type NCont = Set[VS] => Set[VS]
  type Cont  = NCont => (Set[VS] => Set[VS])

  def alloc0(x: String) = allocBind(x, List())

  def aval(e: Expr, env: Env, store: BStore, cont: Cont, ncont: NCont): Set[VS] = e match {
    case Let(x, App(f, ae), e) =>
      val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]

      def new_ncont(ret_vss: Set[VS])(result: Set[VS]): Set[VS] = {
        val VS(vals, store) = ret_vss.head
        val baddr = alloc0(x)
        val new_env = env + (x -> baddr)
        val new_store = store.update(baddr, vals)
        aval(e, new_env, new_store, (r: NCont) => r, (vss1: Set[VS]) => {
          println(s"VSS1: ${vss1}\n") //15times
          if (ret_vss.tail.isEmpty) cont(ncont)(result ++ vss1)
          else new_ncont(ret_vss.tail)(result ++ vss1)
        })
        /*
        aval(e, new_env, new_store, (r: NCont) => (vss1: Set[VS]) => {
          println(s"VSS1: ${vss1}\n") //15times
          if (ret_vss.tail.isEmpty) r(result ++ vss1)
          else new_ncont(ret_vss.tail)(result ++ vss1)
        }, cont(ncont))
        */
        /*
        aval(e, new_env, new_store, cont, (vss1: Set[VS]) => {
          println(s"VSS1: ${vss1}\n") 
          if (ret_vss.tail.isEmpty) ncont(result ++ vss1)
         else new_ncont(ret_vss.tail)(result ++ vss1)
        })
        */
      }

      def nd(closures: List[Clos], vss_acc: Set[VS]): Set[VS] = {
        val Clos(Lam(v, body), c_env)::rest = closures
        val baddr = alloc0(v)
        val new_env = c_env + (v -> baddr)
        val new_store = store.update(baddr, aeval(ae, env, store))
        aval(body, new_env, new_store, (r: NCont) => r, (vss: Set[VS]) => {
          //println(s"new_cont: ${vss}")
          if (rest.isEmpty) new_ncont(vss_acc ++ vss)(Set())
          else nd(rest, vss_acc ++ vss)
        })
      }

      nd(closures, Set())

    case ae if isAtomic(ae) =>
      val ds = aeval(ae, env, store)
      cont(ncont)(Set(VS(ds, store)))
  }

  def analyze(e: Expr, env: Env = Map(), store: BStore = Store[BAddr, Storable](Map())) =
    aval(e, env, store, (r: NCont) => (vss: Set[VS]) =>  r(vss), (vss: Set[VS]) => vss)
}

// +time
object RefuncNoCache {
  import SmallStepUBStack._

  case class VS(vals: Set[Storable], time: Time, store: BStore)
  type NCont = Set[VS] => Set[VS]
  type Cont  = NCont => (Set[VS] => Set[VS])

  def aval(e: Expr, env: Env, store: BStore, time: Time,
           cont: Cont, ncont: NCont): Set[VS] = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, new_time)
        val new_env = env + (x -> baddr)
        val new_store = store.update(baddr, aeval(ae, env, store))
        aval(e, new_env, new_store, new_time, cont, ncont)

      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]

        def new_ncont(ret_vss: Set[VS])(result: Set[VS]): Set[VS] = {
          val VS(vals, time, store) = ret_vss.head
          val baddr = allocBind(x, time)
          val new_env = env + (x -> baddr)
          val new_store = store.update(baddr, vals)
          aval(e, new_env, new_store, time, (r: NCont) => r, (vss1: Set[VS]) => {
            if (ret_vss.tail.isEmpty) cont(ncont)(result ++ vss1)
            else new_ncont(ret_vss.tail)(result ++ vss1)
          })
        }

        def nd(closures: List[Clos], vss_acc: Set[VS]): Set[VS] = {
          if (closures.isEmpty) { new_ncont(vss_acc)(Set()) }
          else {
            val Clos(Lam(v, body), c_env) = closures.head
            val baddr = allocBind(v, new_time)
            val new_env = c_env + (v -> baddr)
            val new_store = store.update(baddr, aeval(ae, env, store))
            aval(body, new_env, new_store, new_time, (r: NCont) => r, (vss: Set[VS]) => {
                   //println(s"new_cont: ${vss}")
                   nd(closures.tail, vss_acc ++ vss)
                 })
          }
        }

        nd(closures, Set())

      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        cont(ncont)(Set(VS(ds, new_time, store)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    aval(e, env, store, mtTime, (r: NCont) => (vss: Set[VS]) =>  r(vss), (vss: Set[VS]) => vss)
}

object RefuncNoCache2 {
  import SmallStepUBStack._
  import RefuncNoCache._

  type NCont = Set[VS] => Set[VS]
  type Cont  = NCont => (Set[VS] => Set[VS])

  def aval(e: Expr, env: Env, store: BStore, time: Time, ncont: NCont): Set[VS] = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]

        def new_ncont(ret_vss: Set[VS])(result: Set[VS]): Set[VS] = {
          val VS(vals, time, store) = ret_vss.head
          val baddr = allocBind(x, time)
          val new_env = env + (x -> baddr)
          val new_store = store.update(baddr, vals)
          aval(e, new_env, new_store, time, (vss1: Set[VS]) => {
            if (ret_vss.tail.isEmpty) ncont(result ++ vss1)
            else new_ncont(ret_vss.tail)(result ++ vss1)
          })
        }

        def nd(closures: List[Clos], vss_acc: Set[VS]): Set[VS] = {
          val Clos(Lam(v, body), c_env) = closures.head
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, (vss: Set[VS]) => {
                 //println(s"new_cont: ${vss}")
                 if (closures.tail.isEmpty) new_ncont(vss_acc ++ vss)(Set())
                 else nd(closures.tail, vss_acc ++ vss)
               })
        }

        nd(closures, Set())

      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        ncont(Set(VS(ds, new_time, store)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    aval(e, env, store, mtTime, (vss: Set[VS]) => vss)
}

object RefuncNoCache3 {
  // NOTE: this is an imperative version
  import SmallStepUBStack._
  import RefuncNoCache._

  def aval(e: Expr, env: Env, store: BStore, time: Time): Set[VS] = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, new_time)
        val new_env = env + (x -> baddr)
        val new_store = store.update(baddr, aeval(ae, env, store))
        aval(e, new_env, new_store, new_time)

      case Letrec(bds, body) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => {
                                          accenv + (bd.x -> allocBind(bd.x, new_time))
                                        })
        val new_store = bds.foldLeft(store)((accbst: BStore, bd: B) => {
                                              accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst))
                                            })
        aval(body, new_env, new_store, new_time)

      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]
        val app_vss = for (Clos(Lam(v, body), c_env) <- closures) yield {
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time)
        }
        val result_vss = app_vss.foldLeft(Set[VS]())(_ ++ _)
        val e_vss = for (VS(vals, time, store) <- result_vss.toList) yield {
          val baddr = allocBind(x, time)
          val new_env = env + (x -> baddr)
          val new_store = store.update(baddr, vals)
          aval(e, new_env, new_store, time)
        }
        e_vss.foldLeft(Set[VS]())(_ ++ _)

      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        Set(VS(ds, new_time, store))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    aval(e, env, store, mtTime)
}

object RefuncNoCache4 {
  /* Implements non-determinism by fold-with-continuations.
   * The strategy it uses is to first try all closures, collect
   * values of App(f, ae) - result_vss, and for each of them,
   * go into let's body `e` seperately.
   * Breath first.
   */
  import SmallStepUBStack._
  import RefuncNoCache._
  
  type Cont = Set[VS] => Set[VS]

  def nd[T](cs: List[T], acc: Set[VS], f: (T, Set[VS], Cont) => Set[VS], g: Cont): Set[VS] = {
    cs match {
      case Nil => g(acc)
      case c::cs => f(c, acc, (vss: Set[VS]) => nd(cs, vss, f, g))
    }
  }

  def aval(e: Expr, env: Env, store: BStore, time: Time, cont: Cont): Set[VS] = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]
        nd(closures, Set[VS](), (clos: Clos, acc: Set[VS], ndk: Cont) => {
          val Clos(Lam(v, body), c_env) = clos
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, (bodyvss: Set[VS]) => { ndk(bodyvss++acc) })
        }, (result_vss: Set[VS]) => {
          nd(result_vss.toList, Set[VS](), (vs: VS, acc: Set[VS], ndk: Cont) => {
            val VS(vals, time, store) = vs
            val baddr = allocBind(x, time)
            val new_env = env + (x -> baddr)
            val new_store = store.update(baddr, vals)
            aval(e, new_env, new_store, time, (evss: Set[VS]) => { ndk(evss++acc) })
          },
          cont)
        })
      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        cont(Set(VS(ds, new_time, store)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    aval(e, env, store, mtTime, (vss => vss))
}

object RefuncCPSNoCache {
  /* Depth first */
  import SmallStepUBStack._
  import RefuncNoCache._
  
  type Ans = Set[VS]
  type Cont = Ans => Ans

  def nd[T,S](ts: Set[T], acc: S, f: (T, S, S=>S) => S, g: S => S): S = {
    if (ts.isEmpty) g(acc)
    else f(ts.head, acc, (vss: S) => nd(ts.tail, vss, f, g))
  }

  def aval(e: Expr, env: Env, store: BStore, time: Time, cont: Cont): Ans = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).asInstanceOf[Set[Clos]]

        nd[Clos, Ans](closures, Set[VS](), { case (clos, acc, closnd) =>
          val Clos(Lam(v, body), c_env) = clos
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, (bodyvss: Set[VS]) => {
            nd[VS, Ans](bodyvss, Set[VS](), { case (vs, acc_vss, bdnd) =>
              val VS(vals, time, store) = vs
              val baddr = allocBind(x, time)
              val new_env = env + (x -> baddr)
              val new_store = store.update(baddr, vals)
              aval(e, new_env, new_store, time, (evss: Ans) => bdnd(acc_vss ++ evss))
            },
            (evss: Ans) => closnd(evss ++ acc))
          })
        },
        cont)
  
      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        cont(Set(VS(ds, new_time, store)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    aval(e, env, store, mtTime, (vss => vss))
}

object RefuncDelConNoCache {
  /* Depth first */
  import SmallStepUBStack._
  import RefuncNoCache._
  
  type Ans = Set[VS]
  type Cont = Ans => Ans

  def nd[T,S](ts: Set[T], acc: S, f: ((T, S, S=>S)) => S, g: S => S): S = {
    if (ts.isEmpty) g(acc)
    else f(ts.head, acc, (vss: S) => nd(ts.tail, vss, f, g))
  }

  def ndcps[T,S](ts: Set[T], acc: S, g: S => S): (T, S, S=>S) @cps[S] = shift { f: (((T, S, S=>S)) => S) =>
    nd(ts, acc, f, g)
  }

  def aval(e: Expr, env: Env, store: BStore, time: Time): Ans @cps[Ans] = shift { cont: Cont =>
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).asInstanceOf[Set[Clos]]
        reset {
          val (clos, acc, closnd) = ndcps[Clos, Ans](closures, Set[VS](), cont)
          val Clos(Lam(v, body), c_env) = clos
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          reset { 
            val bodyvss = aval(body, new_env, new_store, new_time)
            reset {
              val (vs, acc_vss, bdnd) = ndcps[VS, Ans](bodyvss, Set[VS](), (evss: Ans) => closnd(evss ++ acc))
              val VS(vals, time, store) = vs
              val baddr = allocBind(x, time)
              val new_env = env + (x -> baddr)
              val new_store = store.update(baddr, vals)
              reset {
                val evss = aval(e, new_env, new_store, time)
                bdnd(acc_vss ++ evss)
              }
            }
          }
        }
  
      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, store)
        cont(Set(VS(ds, new_time, store)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    reset { aval(e, env, store, mtTime) }
}

object RefuncDelConNoCache2 {
  /* Depth first */
  import SmallStepUBStack._
  import RefuncNoCache._
  
  type Ans = Set[VS]
  type Cont = Ans => Ans

  def nd[T,S](ts: Set[T], acc: Set[S], k: T => Set[S]): Set[S] = {
    if (ts.isEmpty) acc
    else {
      val vss = k(ts.head)
      nd(ts.tail, acc ++ vss, k)
    }
  }
  
  def ndcps[T,S](ts: Set[T], acc: Set[S]): T @cps[Set[S]] = shift { f: (T => Set[S]) => 
    nd(ts, acc, f)
  }

  def aval(e: Expr, env: Env, store: BStore, time: Time): Ans @cps[Ans] = {
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = atomicEval(f, env, store).asInstanceOf[Set[Clos]]
        val Clos(Lam(v, body), c_env) = ndcps[Clos, VS](closures, Set[VS]())
        val vbaddr = allocBind(v, new_time)
        val new_cenv = c_env + (v -> vbaddr)
        val new_cstore = store.update(vbaddr, aeval(ae, env, store))
        val bodyvss = aval(body, new_cenv, new_cstore, new_time)
        val VS(vals, time, vsstore) = ndcps[VS, VS](bodyvss, Set[VS]())
        val baddr = allocBind(x, time)
        val new_env = env + (x -> baddr)
        val new_store = vsstore.update(baddr, vals)
        aval(e, new_env, new_store, time)
  
      case ae if isAtomic(ae) =>
        val ds = atomicEval(ae, env, store)
        Set(VS(ds, new_time, store))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) =
    reset { aval(e, env, store, mtTime) }
}

object RefuncCPS {
  /* Depth First Evaluation */
  import SmallStepUBStack._
  import RefuncNoCache._

  case class Config(e: Expr, env: Env, store: BStore, time: Time)
  case class Cache(in: Store[Config, VS], out: Store[Config, VS]) {
    def inGet(config: Config): Set[VS] = in.getOrElse(config, Set())
    def inContains(config: Config): Boolean = in.contains(config)
    def outGet(config: Config): Set[VS] = out.getOrElse(config, Set())
    def outContains(config: Config): Boolean = out.contains(config)
    def outUpdate(config: Config, vss: Set[VS]): Cache = { Cache(in, out.update(config, vss)) }
    def outUpdate(config: Config, vs: VS): Cache = { Cache(in, out.update(config, vs)) }
    def outJoin(c: Cache): Cache = { Cache(in, out.join(c.out)) }
  }
  def mtCache = Cache(Store[Config, VS](Map()), Store[Config, VS](Map()))
  
  case class Ans(vss: Set[VS], cache: Cache) {
    def ++(ans: Ans): Ans = {
      Ans(vss ++ ans.vss, ans.cache.outJoin(cache))
    }
  }
  type Cont = Ans => Ans

  def nd[T,S](ts: Set[T], acc: S, f: (T, S, S=>S) => S, g: S => S): S = {
    if (ts.isEmpty) g(acc)
    else f(ts.head, acc, (vss: S) => nd(ts.tail, vss, f, g))
  }
  
  def aval(e: Expr, env: Env, store: BStore, time: Time, cache: Cache, cont: Cont): Ans = {
    val config = Config(e, env, store, time)
    if (cache.outContains(config)) {
      return cont(Ans(cache.outGet(config), cache))
    }

    val new_time = (e::time).take(k)
    val new_cache = cache.outUpdate(config, cache.inGet(config))

    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).asInstanceOf[Set[Clos]]
        nd[Clos, Ans](closures, Ans(Set[VS](), new_cache), { case (clos, Ans(acc, cache), closnd) =>
          val Clos(Lam(v, body), c_env) = clos
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, cache, { case Ans(bodyvss, bodycache) =>
            nd[VS, Ans](bodyvss, Ans(Set[VS](), bodycache), { case (vs, Ans(acc_vss, cache), bdnd) =>
              val VS(vals, time, store) = vs
              val baddr = allocBind(x, time)
              val new_env = env + (x -> baddr)
              val new_store = store.update(baddr, vals)
              aval(e, new_env, new_store, time, cache, { case Ans(evss, ecache) => bdnd(Ans(acc_vss ++ evss, ecache)) })
            },
            { case Ans(evss, cache) => closnd(Ans(evss ++ acc, cache)) })
          })
        },
        { case Ans(vss, cache) => cont(Ans(vss, cache.outUpdate(config, vss))) })
  
      case ae if isAtomic(ae) =>
        val vs = Set(VS(aeval(ae, env, store), new_time, store))
        cont(Ans(vs, new_cache.outUpdate(config, vs)))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, new_cache) = aval(e, env, store, mtTime, cache, {
        case Ans(vss, cache) => 
          val initConfig = Config(e, env, store, mtTime)
          Ans(vss, cache.outUpdate(initConfig, vss))
      })
      if (new_cache.out == cache.out) { Ans(vss, new_cache) }
      else { iter(Cache(new_cache.out, new_cache.out)) }
    }
    iter(mtCache)
  }
}

object RefuncDelCon {
  /* Depth first */
  import SmallStepUBStack._
  import RefuncNoCache._
  //import RefuncDelConNoCache2._
  import RefuncCPS._
  
  //type Ans = Set[VS]
  type Cont = Ans => Ans

  def nd[T](ts: Set[T], acc: Ans, k: ((T, Cache)) => Ans): Ans = {
    if (ts.isEmpty) acc
    else {
      nd(ts.tail, acc ++ k(ts.head, acc.cache), k)
    }
  }
  
  def ndcps[T](ts: Set[T], acc: Ans): (T, Cache) @cps[Ans] = shift { f: (((T, Cache)) => Ans) => 
    nd(ts, acc, f)
  }
  
  def aval(e: Expr, env: Env, store: BStore, time: Time, cache: Cache): Ans @cps[Ans] = {
    val config = Config(e, env, store, time)
    if (cache.outContains(config)) Ans(cache.outGet(config), cache)
    else {
      val new_time = (e::time).take(k)
      val new_cache = cache.outUpdate(config, cache.inGet(config))
      e match {
        case Let(x, App(f, ae), e) =>
          val closures = atomicEval(f, env, store).asInstanceOf[Set[Clos]]
          val (Clos(Lam(v, body), c_env), clscache) = ndcps[Clos](closures, Ans(Set[VS](), new_cache))
          val vbaddr = allocBind(v, new_time)
          val new_cenv = c_env + (v -> vbaddr)
          val new_cstore = store.update(vbaddr, aeval(ae, env, store))
          val Ans(bodyvss, bodycache) = aval(body, new_cenv, new_cstore, new_time, clscache)
          val (VS(vals, time, vsstore), vscache) = ndcps[VS](bodyvss, Ans(Set[VS](), bodycache))
          val baddr = allocBind(x, time)
          val new_env = env + (x -> baddr)
          val new_store = vsstore.update(baddr, vals)
          val Ans(finval, fincache) = aval(e, new_env, new_store, time, vscache)
          Ans(finval, fincache.outUpdate(config, finval))

        case ae if isAtomic(ae) =>
          val vs = Set(VS(atomicEval(ae, env, store), new_time, store))
          Ans(vs, cache.outUpdate(config, vs))
      }
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) = {
    def iter(cache: Cache): Ans = {
      reset {
        val Ans(vss, anscache) = aval(e, env, store, mtTime, cache)
        val initConfig = Config(e, env, store, mtTime)
        val new_cache = anscache.outUpdate(initConfig, vss)
        if (new_cache.out == cache.out) { Ans(vss, new_cache) }
        else { iter(Cache(new_cache.out, new_cache.out)) }
      }
    }
    iter(mtCache)
  }
}

object DirectStyle {
  /* Using side effect to change new_cache every time */
  import SmallStepUBStack._
  import RefuncNoCache._
  import RefuncCPS._

  def aval(e: Expr, env: Env, store: BStore, time: Time, cache: Cache): Ans = {
    val config = Config(e, env, store, time)
    if (cache.outContains(config)) {
      return Ans(cache.outGet(config), cache)
    }

    var new_cache = cache.outUpdate(config, cache.inGet(config))
    val new_time = (e::time).take(k)

    e match {
      case Letrec(bds, body) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => {
                                          accenv + (bd.x -> allocBind(bd.x, new_time))
                                        })
        val new_store = bds.foldLeft(store)((accbst: BStore, bd: B) => {
                                              accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst))
                                            })
        val Ans(vss, cache) = aval(body, new_env, new_store, new_time, new_cache)
        Ans(vss, cache.outUpdate(config, vss))

      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]
        val letvs = for (Clos(Lam(v, body), c_env) <- closures) yield {
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          val Ans(bdv, bdcache) = aval(body, new_env, new_store, new_time, new_cache)
          new_cache = bdcache
          val evs = for (VS(vals, time, store) <- bdv) yield {
            val baddr = allocBind(x, time)
            val new_env = env + (x -> baddr)
            val new_store = store.update(baddr, vals)
            val Ans(ev, ecache) = aval(e, new_env, new_store, time, new_cache)
            new_cache = ecache
            ev
          }
          evs.foldLeft(Set[VS]())(_ ++ _)
        }
        val letv = letvs.foldLeft(Set[VS]())(_ ++ _)
        Ans(letv, new_cache.outUpdate(config, letv))

      case ae if isAtomic(ae) =>
        val vs = Set(VS(aeval(ae, env, store), new_time, store))
        Ans(vs, new_cache.outUpdate(config, vs))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, new_cache_) = aval(e, env, store, mtTime, cache)
      val new_cache = new_cache_.outUpdate(Config(e, env, store, mtTime), vss)
      if (new_cache.out == cache.out) { Ans(vss, new_cache) }
      else { iter(Cache(new_cache.out, new_cache.out)) }
    }
    iter(mtCache)
  }

}

object RefuncCPS2 {
  /* Breath First Evaluation */
  import SmallStepUBStack._
  import RefuncNoCache._
  import RefuncCPS._
  
  type Cont = (Set[VS], Cache) => (Set[VS], Cache)

  def nd[T](ts: Set[T], acc: Set[VS], cache: Cache, 
            f: (T, Set[VS], Cache, Cont) => (Set[VS], Cache), g: Cont): (Set[VS], Cache) = {
    if (ts.isEmpty) g(acc, cache)
    else f(ts.head, acc, cache, (vss: Set[VS], cache: Cache) => nd(ts.tail, vss, cache, f, g))
  }

  def aval(e: Expr, env: Env, store: BStore, time: Time, cache: Cache, cont: Cont): (Set[VS], Cache) = {
    val config = Config(e, env, store, time)
    if (cache.outContains(config)) {
      return cont(cache.outGet(config), cache)
    }

    val new_cache = cache.outUpdate(config, cache.inGet(config))
    val new_time = (e::time).take(k)
    e match {
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).asInstanceOf[Set[Clos]]
        nd(closures, Set[VS](), new_cache, (clos: Clos, acc: Set[VS], cache: Cache, ndk: Cont) => {
          val Clos(Lam(v, body), c_env) = clos
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, cache, (bodyvss: Set[VS], cache: Cache) => { ndk(bodyvss++acc, cache) })
        }, (result_vss: Set[VS], cache: Cache) => {
          nd(result_vss, Set[VS](), cache, (vs: VS, acc: Set[VS], cache: Cache, ndk: Cont) => {
            val VS(vals, time, store) = vs
            val baddr = allocBind(x, time)
            val new_env = env + (x -> baddr)
            val new_store = store.update(baddr, vals)
            aval(e, new_env, new_store, time, cache, (evss: Set[VS], cache: Cache) => { ndk(evss++acc, cache) })
          },
          (ans: Set[VS], cache: Cache) => cont(ans, cache.outUpdate(config, ans)))
        })
  
      case ae if isAtomic(ae) =>
        val vs = Set(VS(aeval(ae, env, store), new_time, store))
        cont(vs, new_cache.outUpdate(config, vs))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) = {
    def iter(cache: Cache): (Set[VS], Cache) = {
      val (vss, new_cache) = aval(e, env, store, mtTime, cache, (vss, cache) => (vss, cache.outUpdate(Config(e, env, store, mtTime), vss)))
      if (new_cache.out == cache.out) { (vss, new_cache) }
      else { iter(Cache(new_cache.out, new_cache.out)) }
    }
    iter(mtCache)
  }

}

// +time, +cache
object Refunc {
  /* Breath First Evaluation */
  import SmallStepUBStack._
  import RefuncNoCache._
  import RefuncCPS._

  def aval(e: Expr, env: Env, store: BStore, time: Time, cache: Cache): Ans = {
    val config = Config(e, env, store, time)
    if (cache.outContains(config)) {
      return Ans(cache.outGet(config), cache)
    }

    val new_cache = cache.outUpdate(config, cache.inGet(config))
    val new_time = (e::time).take(k)

    e match {
      case Letrec(bds, body) =>
        val new_env = bds.foldLeft(env)((accenv: Env, bd: B) => {
                                          accenv + (bd.x -> allocBind(bd.x, new_time))
                                        })
        val new_store = bds.foldLeft(store)((accbst: BStore, bd: B) => {
                                              accbst.update(allocBind(bd.x, new_time), aeval(bd.e, new_env, accbst))
                                            })
        val Ans(vss, cache) = aval(body, new_env, new_store, new_time, new_cache)
        Ans(vss, cache.outUpdate(config, vss))

      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, store).toList.asInstanceOf[List[Clos]]
        val app_vss_cache = for (Clos(Lam(v, body), c_env) <- closures) yield {
          val baddr = allocBind(v, new_time)
          val new_env = c_env + (v -> baddr)
          val new_store = store.update(baddr, aeval(ae, env, store))
          aval(body, new_env, new_store, new_time, new_cache) //TODO: Do we have to use the latest cache?
        }

        val app_vss = app_vss_cache.map(_.vss).foldLeft(Set[VS]())(_ ++ _).toList
        val app_cache = app_vss_cache.map(_.cache).foldLeft(mtCache)(_.outJoin(_))

        val e_vss = for (VS(vals, time, store) <- app_vss) yield {
          val baddr = allocBind(x, time)
          val new_env = env + (x -> baddr)
          val new_store = store.update(baddr, vals)
          aval(e, new_env, new_store, time, app_cache) //TODO: Do we have to use the latest cache?
        }
        val result = e_vss.map(_.vss).foldLeft(Set[VS]())(_ ++ _)
        Ans(result, e_vss.map(_.cache).foldLeft(mtCache)(_.outJoin(_)).outUpdate(config, result))

      case ae if isAtomic(ae) =>
        val vs = Set(VS(aeval(ae, env, store), new_time, store))
        Ans(vs, new_cache.outUpdate(config, vs))
    }
  }

  def mtTime = List()
  def mtEnv = Map[String, BAddr]()
  def mtStore = Store[BAddr, Storable](Map())

  def analyze(e: Expr, env: Env = mtEnv, store: BStore = mtStore) = {
    def iter(cache: Cache): Ans = {
      val Ans(vss, new_cache_) = aval(e, env, store, mtTime, cache)
      val new_cache = new_cache_.outUpdate(Config(e, env, store, mtTime), vss)
      if (new_cache.out == cache.out) { Ans(vss, new_cache) }
      else { iter(Cache(new_cache.out, new_cache.out)) }
    }
    iter(mtCache)
  }
}



object RefuncTest {
  def test_smallstep_time() {
    val initenv = Map("f" -> BAddr("f", List()))
    val initstore = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Var("x")), Map()),
                                                                         Clos(Lam("y", Var("y")), Map()))))
    val ndprog = Let("a", App(Var("f"), Num(3)), Var("a"))
    ANFAAM.k = 1
    println(SmallStep.analyze(ndprog, initenv, initstore).mkString("\n"))
    println(RefuncNoCache.analyze(ndprog, initenv, initstore))
    println(RefuncNoCache3.analyze(ndprog, initenv, initstore))
  }

  def test_refunc_nd() {
    ANFAAM.k = 0
    val ndprog = Let("a", App(Var("f"), Num(3)),
                    Var("a"))
                    //Num(10))
    //println(RefuncNoCacheNoTime.analyze(ndprog))
    val initenv = Map("f" -> BAddr("f", List()))
    val initstore = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Var("x")), Map()),
                                                                         Clos(Lam("y", Num(2)), Map()),
                                                                         Clos(Lam("z", Num(1)), Map()))))
    //println(SmallStepUBStack.analyze(ndprog,initenv, initstore))
    //println(LinearSmallStepUBStack.analyze(ndprog,initenv, initstore))

    assert(SmallStepUBStack.analyze(ndprog,initenv, initstore) == 
      LinearSmallStepUBStack.analyze(ndprog,initenv, initstore))

    assert(FusedLinearSmallStepUBStack.analyze(ndprog,initenv, initstore) == 
      LinearSmallStepUBStack.analyze(ndprog,initenv, initstore))

    assert(RefuncNoCacheNoTime1.analyze(ndprog, initenv, initstore) ==
             RefuncNoCacheNoTime2.analyze(ndprog, initenv, initstore))
    assert(RefuncNoCache2.analyze(ndprog, initenv, initstore) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore))
    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore))
    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore))

    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore))
    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore) ==
             RefuncDelConNoCache2.analyze(ndprog, initenv, initstore))

    assert(RefuncNoCache3.analyze(ndprog, initenv, initstore) ==
           Refunc.analyze(ndprog, initenv, initstore).vss)

    assert(RefuncCPS.analyze(ndprog, initenv, initstore) ==
           Refunc.analyze(ndprog, initenv, initstore))

    assert(RefuncCPS.analyze(ndprog, initenv, initstore) ==
           DirectStyle.analyze(ndprog, initenv, initstore))
    
    println(RefuncCPS.analyze(ndprog, initenv, initstore).cache)
    println(RefuncDelCon.analyze(ndprog, initenv, initstore).cache)
  
    assert(RefuncCPS.analyze(ndprog, initenv, initstore) ==
           RefuncDelCon.analyze(ndprog, initenv, initstore))

    val initstore_nd = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Let("t", App(Var("g"), Var("x")), 
                                                                                            Var("t"))),
                                                                                 Map("g" -> BAddr("g", List()))),
                                                                            Clos(Lam("y", Num(2)), Map()),
                                                                            Clos(Lam("z", Num(1)), Map())),
                                                  BAddr("g", List()) -> Set(Clos(Lam("a", Num(3)), Map()),
                                                                            Clos(Lam("b", Num(4)), Map()))))

    assert(SmallStepUBStack.analyze(ndprog,initenv, initstore_nd) == 
      LinearSmallStepUBStack.analyze(ndprog,initenv, initstore_nd))

    assert(FusedLinearSmallStepUBStack.analyze(ndprog,initenv, initstore_nd) == 
      LinearSmallStepUBStack.analyze(ndprog,initenv, initstore_nd))

    assert(RefuncNoCache2.analyze(ndprog, initenv, initstore_nd) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore_nd))
    assert(RefuncNoCache3.analyze(ndprog, initenv, initstore_nd) ==
             Refunc.analyze(ndprog, initenv, initstore_nd).vss)

    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore_nd) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore_nd))
    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore_nd) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore_nd))
    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore_nd) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore_nd))
    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore_nd) ==
             RefuncDelConNoCache2.analyze(ndprog, initenv, initstore_nd))

    //println(RefuncNoCacheNoTime2.analyze(ndprog, initenv, initstore_nd))
    assert(RefuncNoCacheNoTime1.analyze(ndprog, initenv, initstore_nd) ==
           RefuncNoCacheNoTime2.analyze(ndprog, initenv, initstore_nd))

    assert(RefuncCPS.analyze(ndprog, initenv, initstore_nd) ==
             Refunc.analyze(ndprog, initenv, initstore_nd))

    assert(RefuncCPS.analyze(ndprog, initenv, initstore_nd) ==
             DirectStyle.analyze(ndprog, initenv, initstore_nd))

    assert(RefuncCPS.analyze(ndprog, initenv, initstore_nd) ==
             RefuncDelCon.analyze(ndprog, initenv, initstore_nd))

    val initstore_nd2 = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Let("t", App(Var("g"), Var("x")), 
                                                                                            Num(5))), 
                                                                                  Map("g" -> BAddr("g", List()))),
                                                                             Clos(Lam("y", Num(2)), Map()),
                                                                             Clos(Lam("z", Num(1)), Map())
                                                                            ),
                                                  BAddr("g", List()) -> Set(Clos(Lam("a", Num(3)), Map()),
                                                                            Clos(Lam("b", Num(4)), Map()))))

    assert(SmallStepUBStack.analyze(ndprog,initenv, initstore_nd2) == 
      LinearSmallStepUBStack.analyze(ndprog,initenv, initstore_nd2))

    assert(FusedLinearSmallStepUBStack.analyze(ndprog, initenv, initstore_nd2) == 
      LinearSmallStepUBStack.analyze(ndprog, initenv, initstore_nd2)) 

    assert(RefuncNoCache2.analyze(ndprog, initenv, initstore_nd2) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore_nd2))
    //println(RefuncNoCache3.analyze(ndprog, initenv, initstore_nd2))
    assert(Refunc.analyze(ndprog, initenv, initstore_nd2).vss ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore_nd2))
    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore_nd2) ==
             RefuncNoCache3.analyze(ndprog, initenv, initstore_nd2))
    assert(RefuncNoCache4.analyze(ndprog, initenv, initstore_nd2) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore_nd2))

    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore_nd2) ==
             RefuncCPSNoCache.analyze(ndprog, initenv, initstore_nd2))
    assert(RefuncDelConNoCache.analyze(ndprog, initenv, initstore_nd2) ==
             RefuncDelConNoCache2.analyze(ndprog, initenv, initstore_nd2))

    assert(RefuncNoCacheNoTime1.analyze(ndprog, initenv, initstore_nd2) ==
           RefuncNoCacheNoTime2.analyze(ndprog, initenv, initstore_nd2))

    assert(Refunc.analyze(ndprog, initenv, initstore_nd2) ==
           RefuncCPS.analyze(ndprog, initenv, initstore_nd2))
    assert(DirectStyle.analyze(ndprog, initenv, initstore_nd2) ==
           RefuncCPS.analyze(ndprog, initenv, initstore_nd2))

    assert(RefuncDelCon.analyze(ndprog, initenv, initstore_nd2) ==
           RefuncCPS.analyze(ndprog, initenv, initstore_nd2))
    /*
    ANFAAM.k = 1
    println(RefuncNoCache.analyze(ndprog, initenv, initstore))
    println(RefuncNoCache.analyze(ndprog, initenv, initstore))
     */

    /************************************/

    val id = Lam("t", Var("t"))
    val ndprog1 = Let("a", App(Var("f"), Num(3)),
                      Let("b", App(id, Var("a")),
                          Var("b")))
    //println(RefuncNoCacheNoTime1.analyze(ndprog1, initenv, initstore))

    /************************************/

    val initenv2 = Map("f" -> BAddr("f", List()),
                       "g" -> BAddr("g", List()))
    val initstore2 = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Let("t", App(Var("g"), Var("x")), Var("t"))),
                                                                               Map("g" -> BAddr("g", List()))),
                                                                          Clos(Lam("y", Num(2)), Map()),
                                                                          Clos(Lam("z", Num(1)), Map()),
                                                                          Clos(Lam("x1", Let("t1", App(Var("h"), Var("x1")), Var("t1"))),
                                                                               Map("h" -> BAddr("h", List()))),
                                                                          Clos(Lam("p", Var("p")), Map())),
                                                BAddr("g", List()) -> Set(Clos(Lam("m", Num(4)), Map()),
                                                                          Clos(Lam("n", Num(5)), Map())),
                                                BAddr("h", List()) -> Set(Clos(Lam("d", Num(6)), Map()))))

    assert(SmallStepUBStack.analyze(ndprog1, initenv2, initstore2) == 
      LinearSmallStepUBStack.analyze(ndprog1, initenv2, initstore2))

    assert(FusedLinearSmallStepUBStack.analyze(ndprog1, initenv2, initstore2) == 
      LinearSmallStepUBStack.analyze(ndprog1, initenv2, initstore2))

    assert(RefuncNoCacheNoTime1.analyze(ndprog1, initenv2, initstore2) ==
             RefuncNoCacheNoTime2.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncNoCache2.analyze(ndprog1, initenv2, initstore2) ==
             RefuncNoCache3.analyze(ndprog1, initenv2, initstore2))
    assert(Refunc.analyze(ndprog1, initenv2, initstore2).vss ==
             RefuncNoCache3.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncNoCache4.analyze(ndprog1, initenv2, initstore2) ==
             RefuncNoCache3.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncNoCache4.analyze(ndprog1, initenv2, initstore2) ==
             RefuncCPSNoCache.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncDelConNoCache.analyze(ndprog1, initenv2, initstore2) ==
             RefuncCPSNoCache.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncDelConNoCache.analyze(ndprog1, initenv2, initstore2) ==
             RefuncDelConNoCache2.analyze(ndprog1, initenv2, initstore2))
    assert(Refunc.analyze(ndprog1, initenv2, initstore2) ==
             RefuncCPS.analyze(ndprog1, initenv2, initstore2))
    assert(DirectStyle.analyze(ndprog1, initenv2, initstore2) ==
             RefuncCPS.analyze(ndprog1, initenv2, initstore2))
    assert(RefuncDelCon.analyze(ndprog1, initenv2, initstore2) ==
             RefuncCPS.analyze(ndprog1, initenv2, initstore2))
    /*
    println("------------------------")
    println(RefuncNoCacheNoTime2.analyze(ndprog1, initenv2, initstore2))
    */
    //ANFAAM.k = 0
    //println(RefuncNoCache.analyze(ndprog1, initenv2, initstore2))
    //println(Refunc.analyze(ndprog, initenv, initstore))
  }

  def test_pushdown() {
    val id = Lam("a", Var("a"))
    val pd1 = Let("id", id,
                  Let("x", App(id, Num(1)),
                      Let("m", App(id, Num(4)),
                          Let("y", App(id, Num(2)),
                              Let("z", App(id, Num(3)),
                                  Var("x"))))))
    ANFAAM.k = 0
    println(SmallStepP4F.analyze(pd1).filter(_.e == Var("x")))
    assert(SmallStepUBStack.analyze(pd1).filter(_.e == Var("x")).head.bstore ==
             RefuncNoCache.analyze(pd1).head.store)
    //println(RefuncNoCache3.analyze(pd1))
  }

  def test_non_term() {
    //TODO: Test RefuncCPS
    """(letrec ([f1 (lambda (x)
                      (let ([x1 (f2 x)]) x1))]
                [f2 (lambda (y)
                      (let ([y1 (f1 y)]) y1))])
           (let ([res (f1 1)])
              res))"""
    val mutrec = Letrec(List(B("f1", Lam("x", Let("x1", App(Var("f2"), Var("x")), Var("x1")))),
                             B("f2", Lam("y", Let("y1", App(Var("f1"), Var("y")), Var("y1"))))),
                        Let("res", App(Var("f1"), Num(1)),
                            Num(5)))
                            //Var("res")))

    var p4fstates = SmallStepP4F.analyze(mutrec)
    var finalstore = p4fstates.map(_.bstore).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))
    println(finalstore)
    println(Refunc.analyze(mutrec).vss)

    val ntprog = Let("a", App(Var("f"), Num(3)),
                     Var("a"))
    //println(RefuncNoCacheNoTime.analyze(ndprog))
    val initenv = Map("f" -> BAddr("f", List()))
    val initstore = Store[BAddr, Storable](Map(BAddr("f", List()) ->
                                                 Set(Clos(Lam("x", Var("x")), Map()),
                                                     Clos(Lam("x1", Let("t1", App(Var("f"), Var("x1")), Var("t1"))),
                                                          Map("f" -> BAddr("f", List()))),
                                                     Clos(Lam("y", Num(2)), Map()),
                                                     Clos(Lam("z", Num(1)), Map()))))
    p4fstates = SmallStepP4F.analyze(ntprog, initenv, initstore)
    finalstore = p4fstates.map(_.bstore).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))
    println(s"p4f store: $finalstore")

    val ans = Refunc.analyze(ntprog, initenv, initstore)
    println(s"refunc vss: ${ans.vss}")
    val refuncstore = ans.vss.map(_.store).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))
    println(s"refunc store: $refuncstore")

    val ans1 = RefuncDelCon.analyze(ntprog, initenv, initstore)
    println("refunc store: " + ans1.vss.map(_.store).foldLeft(Store[BAddr, Storable](Map()))(_.join(_)))
    //RefuncNoCache3.analyze(ntprog, initenv, initstore) Not termiating
  }

  def test_cache() {
    val initenv = Map("f" -> BAddr("f", List()))
    val initstore = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Var("x")), Map()),
                                                                         Clos(Lam("y", Var("y")), Map()))))
    val ndprog = Let("a", App(Var("f"), Num(3)), Var("a"))
    ANFAAM.k = 1

    assert(RefuncNoCache.analyze(ndprog, initenv, initstore) ==
             RefuncNoCache2.analyze(ndprog, initenv, initstore))

    assert(RefuncNoCache.analyze(ndprog, initenv, initstore) ==
             Refunc.analyze(ndprog, initenv, initstore).vss)

    assert(RefuncCPS.analyze(ndprog, initenv, initstore) ==
             Refunc.analyze(ndprog, initenv, initstore))

    assert(RefuncCPS.analyze(ndprog, initenv, initstore) ==
             DirectStyle.analyze(ndprog, initenv, initstore))

    val id = Lam("t", Var("t"))
    val ndprog1 = Let("a", App(Var("f"), Num(3)),
                      Let("b", App(id, Var("a")),
                          Var("b")))
    val initenv1 = Map("f" -> BAddr("f", List()),
                       "g" -> BAddr("g", List()))
    val initstore1 = Store[BAddr, Storable](Map(BAddr("f", List()) -> Set(Clos(Lam("x", Let("t", App(Var("g"), Var("x")), Var("t"))), //4, 5
                                                                               Map("g" -> BAddr("g", List()))),
                                                                          Clos(Lam("y", Num(2)), Map()),
                                                                          Clos(Lam("z", Num(1)), Map()),
                                                                          Clos(Lam("x1", Let("t1", App(Var("h"), Var("x1")), Var("t1"))), //6, 7
                                                                               Map("h" -> BAddr("h", List()))),
                                                                          Clos(Lam("p", Var("p")), Map())),
                                                BAddr("g", List()) -> Set(Clos(Lam("m", Num(4)), Map()),
                                                                          Clos(Lam("n", Num(5)), Map())),
                                                BAddr("h", List()) -> Set(Clos(Lam("d", Num(6)), Map()),
                                                                          Clos(Lam("d", Num(7)), Map()))))

    assert(RefuncNoCache.analyze(ndprog1, initenv1, initstore1) ==
             RefuncNoCache2.analyze(ndprog1, initenv1, initstore1))

    assert(RefuncNoCache.analyze(ndprog1, initenv1, initstore1) ==
           Refunc.analyze(ndprog1, initenv1, initstore1).vss)

    assert(RefuncCPS.analyze(ndprog1, initenv1, initstore1) ==
           Refunc.analyze(ndprog1, initenv1, initstore1))
    assert(RefuncCPS.analyze(ndprog1, initenv1, initstore1).vss ==
           RefuncCPS2.analyze(ndprog1, initenv1, initstore1)._1)
    assert(RefuncCPS.analyze(ndprog1, initenv1, initstore1).cache ==
           RefuncCPS2.analyze(ndprog1, initenv1, initstore1)._2)

    assert(RefuncCPS.analyze(ndprog1, initenv1, initstore1) ==
          DirectStyle.analyze(ndprog1, initenv1, initstore1))
    
    val p4fstates = SmallStepP4F.analyze(ndprog1, initenv1, initstore1)
    val finalstore = p4fstates.map(_.bstore).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))

    val refuncstore = RefuncCPS2.analyze(ndprog1, initenv1, initstore1)._1.map(_.store).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))
    assert(finalstore == refuncstore)
  }

  def main(args: Array[String]) {
    //test_smallstep_time()
    test_non_term()
    //test_pushdown()
    //test_cache()
    //test_refunc_nd()
  }
}
