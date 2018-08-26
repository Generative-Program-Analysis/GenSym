package sai.direct.core.ai

import sai.direct.core.parser._

object ANFCESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Storable]

  abstract class Storable
  case class Clos(v: Lam, env: Env) extends Storable

  case class Frame(x: String, e: Expr, env: Env)
  type Kont = List[Frame]
  
  case class State(e: Expr, env: Env, store: Store, k: Kont)

  def atomicEval(e: Expr, env: Env, store: Store): Storable = e match {
    case Var(x) => store(env(x))
    case lam@Lam(x, body) => Clos(lam, env)
  }
  def alloc(store: Store): Addr = store.keys.size + 1
  def isAtomic(e: Expr): Boolean = e.isInstanceOf[Var] || e.isInstanceOf[Lam]
  
  def step(s: State): State = s match {
    case State(Let(x, App(f, ae), e), env, store, k) if isAtomic(ae) =>
      val Clos(Lam(v, body), env_c) = atomicEval(f, env, store)
      val addr = alloc(store)
      val frame = Frame(x, e, env)
      State(body, env_c+(v->addr), store+(addr->atomicEval(ae, env, store)), frame::k)
    case State(ae, env, store, k) if isAtomic(ae) =>
      val Frame(x, e, env_k)::ks = k
      val addr = alloc(store)
      State(e, env_k+(x->addr), store+(addr->atomicEval(ae, env, store)), ks)
  }

  def drive(s: State): State = s match {
    case State(ae, _, _, Nil) if isAtomic(ae) => s
    case _ => drive(step(s))
  }

  def inject(e: Expr): State = State(e, Map(), Map(), Nil)
  def eval(e: Expr): State = drive(inject(e))
}

object ANFAAM {
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

  def k: Int = 0

  def allocBind(x: String, time: Time): BAddr = BAddr(x, time)
  //def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = ContAddr(tgtExpr, time)
  def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = ContAddr(tgtExpr)

  case class State(e: Expr, env: Env, bstore: BStore, kstore: KStore, k: KAddr, time: Time)

  def tick(s: State): Time = (s.e::s.time).take(k)
  /*
  s.e match {
    case app: App => (app::s.time).take(k)
    case _ => s.time
  }
  */

  def inject(e: Expr): State = State(e, Map(), Store[BAddr, Storable](Map()),
                                     Store[KAddr, Cont](Map(Halt -> Set())), Halt, List())

  def aeval(e: Expr, env: Env, bstore: BStore): Set[Storable] = e match {
    case Num(i) => Set(NumV(i))
    case Var(x) => bstore(env(x)).toSet
    case lam@Lam(x, body) => Set(Clos(lam, env))
  }

  def isAtomic(e: Expr): Boolean = e.isInstanceOf[Var] || e.isInstanceOf[Lam] || e.isInstanceOf[Num]
}

import ANFAAM._

object SmallStep {
  def step(s: State): List[State] = {
    val newTime = tick(s)
    s match {
      case State(Let(x, ae, e), env, bstore, kstore, kaddr, time) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, newEnv, newBStore, kstore, kaddr, newTime))

      case State(Lrc(bds, body), env, bstore, kstore, kaddr, time) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        List(State(body, newEnv, newBStore, kstore, kaddr, time))

      case State(Let(x, App(f, ae), e), env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val baddr = allocBind(v, newTime)
          val newEnv = c_env + (v -> baddr)
          val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newKAddr = allocKont(body, c_env, newBStore, newTime)
          val newKStore = kstore.update(newKAddr, Cont(Frame(x, e, env), kaddr))
          State(body, newEnv, newBStore, newKStore, newKAddr, newTime)
        }

      case State(ae, env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Cont(Frame(x, e, f_env), f_kaddr) <- kstore(kaddr).toList) yield {
          val baddr = allocBind(x, newTime)
          val newEnv = f_env + (x -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          State(e, newEnv, newStore, kstore, f_kaddr, newTime)
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd).toList ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())
}

object SmallStepUBStack {
  case class State(e: Expr, env: Env, bstore: BStore, konts: List[Frame], time: Time)

  def tick(s: State): Time = (s.e::s.time).take(k)
  def inject(e: Expr): State = State(e, Map(), Store[BAddr, Storable](Map()), List(), List())

  def step(s: State): List[State] = {
    val newTime = tick(s)
    s match {
      case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, newEnv, newStore, konts, newTime))
      case State(Lrc(bds, body), env, bstore, konts, time) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        List(State(body, newEnv, newBStore, konts, time))
      case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val frame = Frame(x, e, env)
          val baddr = allocBind(v, newTime)
          val newEnv = c_env + (v -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          State(body, newEnv, newStore, frame::konts, newTime)
        }
      case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
        konts match {
          case Nil => List()
          case Frame(x, e, f_env)::konts =>
            val baddr = allocBind(x, newTime)
            val newEnv = f_env + (x -> baddr)
            val newStore = bstore.update(baddr, aeval(ae, env, bstore))
            List(State(e, newEnv, newStore, konts, newTime))
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd) ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())
}

object SmallStepP4F {
  def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr = P4FContAddr(tgtExpr, tgtEnv)

  def step(s: State): List[State] = {
    val newTime = tick(s)
    //println(s"P4F expr: ${s.e}\nk: ${s.kstore(s.k)}")
    s match {
      case State(Let(x, ae, e), env, bstore, kstore, kaddr, time) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
        List(State(e, newEnv, newBStore, kstore, kaddr, newTime))
      case State(Lrc(bds, body), env, bstore, kstore, kaddr, time) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        List(State(body, newEnv, newBStore, kstore, kaddr, time))
      case State(Let(x, App(f, ae), e), env, bstore, kstore, kaddr, time) =>
        for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val baddr = allocBind(v, newTime)
          val newEnv = c_env + (v -> baddr)
          val newBStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newKAddr = allocKont(body, c_env, newBStore, newTime)
          val newKStore = kstore.update(newKAddr, Cont(Frame(x, e, env), kaddr))
          State(body, newEnv, newBStore, newKStore, newKAddr, newTime)
        }
      case State(ae, env, bstore, kstore, kaddr, time) if isAtomic(ae)=>
        for (Cont(Frame(x, e, f_env), f_kaddr) <- kstore(kaddr).toList) yield {
          val baddr = allocBind(x, newTime)
          val newEnv = f_env + (x -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          State(e, newEnv, newStore, kstore, f_kaddr, newTime)
        }
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl => drive(step(hd) ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())
}

object FusedUBStack {
  import SmallStepUBStack._

  def drive_step(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive_step(tl, seen)
    case hd::tl =>
      val newTime = tick(hd)
      val newStates = hd match {
        case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
          val baddr = allocBind(x, newTime)
          val newEnv = env + (x -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          List(State(e, newEnv, newStore, konts, newTime))
        case State(Lrc(bds, body), env, bstore, konts, time) =>
          val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
          val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
          List(State(body, newEnv, newBStore, konts, newTime))
        case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
          for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
            val frame = Frame(x, e, env)
            val baddr = allocBind(v, newTime)
            val newEnv = c_env + (v -> baddr)
            val newStore = bstore.update(baddr, aeval(ae, env, bstore))
            State(body, newEnv, newStore, frame::konts, newTime)
          }
        case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
          konts match {
            case Nil => List()
            case Frame(x, e, f_env)::konts =>
              val baddr = allocBind(x, newTime)
              val newEnv = f_env + (x -> baddr)
              val newStore = bstore.update(baddr, aeval(ae, env, bstore))
              List(State(e, newEnv, newStore, konts, newTime))
          }
      }
      drive_step(newStates ++ tl, seen + hd)
  }

  def analyze(e: Expr): Set[State] = drive_step(List(inject(e)), Set())
}

object DisentangledBigStepUBStack {
  import SmallStepUBStack._

  def drive_step(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive_step(tl, seen)
    case hd::tl =>
      val newTime = tick(hd)
      hd match {
        case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
          val baddr = allocBind(x, newTime)
          val newEnv = env + (x -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newState = State(e, newEnv, newStore, konts, newTime)
          drive_step(newState::tl, seen + hd)
        case State(Lrc(bds, body), env, bstore, konts, time) =>
          val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
          val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
          val newState = State(body, newEnv, newBStore, konts, newTime)
          drive_step(newState::tl, seen + hd)
        case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
          val newStates = for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
            val frame = Frame(x, e, env)
            val baddr = allocBind(v, newTime)
            val newEnv = c_env + (v -> baddr)
            val newStore = bstore.update(baddr, aeval(ae, env, bstore))
            State(body, newEnv, newStore, frame::konts, newTime)
          }
          drive_step(newStates ++ tl, seen + hd)
        case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
          continue(konts, aeval(ae, env, bstore), bstore, newTime, tl, seen + hd)
      }
  }

  def continue(konts: List[Frame], ds: Set[Storable], bstore: BStore, time: Time, todo: List[State], seen: Set[State]): Set[State] = konts match {
    case Nil => drive_step(todo, seen)
    case Frame(x, e, f_env)::konts =>
      val baddr = allocBind(x, time)
      val newEnv = f_env + (x -> baddr)
      val newStore = bstore.update(baddr, ds)
      val newState = State(e, newEnv, newStore, konts, time)
      drive_step(newState::todo, seen)
  }

  def analyze(e: Expr): Set[State] = drive_step(List(inject(e)), Set())
}

/* Disentanged big-step abstract machine with unbounded stack */
/* EXPERIMENT */
object DisentangledBigStepUBStack1 {
  import SmallStepUBStack._

  def aval(state: State, todo: List[State], seen: Set[State]): Set[State] = {
    val newTime = tick(state)
    state match {
      case State(Let(x, ae, e), env, bstore, konts, time) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        val newState = State(e, newEnv, newStore, konts, newTime)
        collect(newState::todo, seen + state)
      case State(Lrc(bds, body), env, bstore, konts, time) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        val newState = State(body, newEnv, newBStore, konts, time)
        collect(newState::todo, seen + state)
      case State(Let(x, App(f, ae), e), env, bstore, konts, time) =>
        val newStates = for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
          val frame = Frame(x, e, env)
          val baddr = allocBind(v, newTime)
          val newEnv = c_env + (v -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          State(body, newEnv, newStore, frame::konts, newTime)
        }
        collect(newStates++todo, seen + state)
      case State(ae, env, bstore, konts, time) if isAtomic(ae) =>
        continue(konts, aeval(ae, env, bstore), bstore, newTime, todo, seen + state)
    }
  }

  def collect(todo: List[State], seen: Set[State]): Set[State] = {
    todo match {
      case Nil => seen
      case hd::tl if seen.contains(hd) => collect(tl, seen)
      case hd::tl => aval(hd, tl, seen)
    }
  }

  def continue(konts: List[Frame], ds: Set[Storable], bstore: BStore, time: Time,
               todo: List[State], seen: Set[State]): Set[State] = konts match {
    case Nil => collect(todo, seen)
    case Frame(x, e, f_env)::konts =>
      val baddr = allocBind(x, time)
      val newEnv = f_env + (x -> baddr)
      val newStore = bstore.update(baddr, ds)
      val newState = State(e, newEnv, newStore, konts, time)
      collect(newState::todo, seen)
  }

  def analyze(e: Expr): Set[State] = aval(inject(e), List(), Set())
}

object RefuncBigStepUBStackFirstTry {
  import SmallStepUBStack._

  //TODO: collecting intermediate states
  //TODO: correspondence with P4F
  //case class Config(e: Expr, env: Env, store: BStore, time: Time)
  case class Config(e: Expr, env: Env, time: Time)
  case class ValStore(ds: Set[Storable], store: BStore, time: Time)
  type Cache = Map[Config, ValStore]

  type Continue = Function2[ValStore, Cache, ValStore]

  def tick(s: Config): Time = (s.e::s.time).take(k)

  def aval(e: Expr, env: Env, bstore: BStore, time: Time, cache: Cache, continue: Continue): ValStore = {
    //val config = Config(e, env, bstore, time)
    // Does <e, env> good enough?
    val config = Config(e, env, time) 
    if (cache.contains(config)) {
      return continue(cache(config), cache)
    }
    val newTime = tick(config)
    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        //TODO cache?
        aval(e, newEnv, newStore, newTime, cache, continue)

      case Lrc(bds, body) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        aval(body, newEnv, newBStore, newTime, cache, continue)

      case Let(x, App(f, ae), e) if isAtomic(ae) =>
        val closures = aeval(f, env, bstore).toList
        //println(s"size of closures: ${closures.size}")
        def nd(closures: List[Storable], accds: Set[Storable], 
               accbstore: BStore, acctime: Time, accCache: Cache): ValStore  = {
          closures match {
            case Nil =>
              // TODO: If we use `newTime` instead of `acctime`, is it m-CFA?
              val baddr = allocBind(x, acctime)
              val newEnv = env + (x -> baddr)
              val newStore = accbstore.update(baddr, accds)
              aval(e, newEnv, newStore, acctime, accCache, (vs: ValStore, accCache: Cache) => {
                //val newCache = accCache + (Config(e, newEnv, vs.store, vs.time) -> vs)
                println(s"${Config(e, newEnv, vs.time)} -> $vs")
                val newCache = accCache + (Config(e, newEnv, vs.time) -> vs)
                continue(vs, newCache)
              })
            case Clos(Lam(v, body), env_c)::rest =>
              val baddr = allocBind(v, newTime)
              val newEnv = env_c + (v -> baddr)
              val newStore = accbstore.update(baddr, aeval(ae, env, bstore)) //TODO: which store?
              //println(s"into $body")
              aval(body, newEnv, newStore, newTime, accCache, (vs: ValStore, accCache: Cache) => {
                //val newCache = accCache + (Config(body, newEnv, vs.store, vs.time) -> vs)
                val newCache = accCache + (Config(body, newEnv, vs.time) -> vs)
                nd(rest, vs.ds++accds, vs.store, vs.time, newCache)
              })
          }
        }

        nd(closures, Set(), bstore, newTime, cache)
      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, bstore)
        continue(ValStore(ds, bstore, newTime), cache)
    }
  }
  
  def done: Continue = (vs: ValStore, cache: Cache) => { vs }
  def analyze(e: Expr): ValStore = aval(e, Map(), Store[BAddr, Storable](Map()), List(), Map[Config, ValStore](), done)
}

object RefuncBigStepUBStackSecondTry {
  import SmallStepUBStack._

  //TODO: correspondence with P4F, just use Config(e, env)
  //case class Config(e: Expr, env: Env, store: BStore, time: Time)
  case class Config(e: Expr, env: Env)
  case class ValStore(ds: Set[Storable], store: BStore, time: Time)
  type Cache = Store[Config, ValStore]
  type RetType = (Set[ValStore], Cache)
  type Continue = Function5[Set[ValStore], BStore, Time, Cache, Cache, RetType]

  def tick(e: Expr, oldtime: Time): Time = (e::oldtime).take(k)

  def aval(e: Expr, env: Env, bstore: BStore, time: Time, 
           inCache: Cache, _outCache: Cache, continue: Continue): RetType = {

    val config = Config(e, env)
    if (_outCache.contains(config)) {
      //FIXME: What the time is it?
      return continue(_outCache(config), bstore, List(), inCache, _outCache)
    }
    
    val outCache = _outCache.update(config, inCache.getOrElse(config, Set()))
    val newTime = tick(e, time)

    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        aval(e, newEnv, newStore, newTime, inCache, outCache, 
          (vs: Set[ValStore], accStore: BStore, acctime: Time, accInCache: Cache, accOutCache: Cache) => {
            val newOutCache = accOutCache.update(Config(e, newEnv), vs)
            continue(vs, accStore, acctime, accInCache, newOutCache)
          })

      case Lrc(bds, body) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { 
          assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) 
        })
        val newStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { 
          accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) 
        })
        aval(body, newEnv, newStore, newTime, inCache, outCache, 
          (vs: Set[ValStore], accStore: BStore, acctime: Time, accInCache: Cache, accOutCache: Cache) => {
            val newOutCache = accOutCache.update(Config(body, newEnv), vs)
            continue(vs, accStore, acctime, accInCache, newOutCache)
          })

      case Let(x, App(f, ae), e) if isAtomic(ae) =>
        val closures = aeval(f, env, bstore).toList

        def nd(closures: List[Storable], accds: Set[Storable], accbstore: BStore, acctime: Time, 
               accInCache: Cache, accOutCache: Cache): RetType  = {
          closures match {
            case Nil =>
              // TODO: If we use `newTime` instead of `acctime`, is it m-CFA?
              // Note: DFS is store widening?
              val baddr = allocBind(x, acctime)
              val newEnv = env + (x -> baddr)
              val newStore = accbstore.update(baddr, accds)
              aval(e, newEnv, newStore, acctime, accInCache, accOutCache, 
                (vs: Set[ValStore], accStore: BStore, acctime: Time, accInCache: Cache, accOutCache: Cache) => {
                  val newOutCache = accOutCache.update(Config(e, newEnv), vs)
                  continue(vs, accStore, acctime, accInCache, newOutCache)
                })
            case Clos(Lam(v, body), env_c)::rest =>
              val baddr = allocBind(v, newTime)
              val newEnv = env_c + (v -> baddr)
              val newStore = accbstore.update(baddr, aeval(ae, env, bstore)) //TODO: which store?
              //println(s"into $body")
              aval(body, newEnv, newStore, newTime, accInCache, accOutCache, 
                (vs: Set[ValStore], accStore: BStore, acctime: Time, accInCache: Cache, accOutCache) => {
                  //val newCache = accCache + (Config(body, newEnv, vs.store, vs.time) -> vs)
                  val newOutCache = accOutCache.update(Config(body, newEnv), vs)
                  val vsds = vs.map(_.ds).foldLeft(Set[Storable]())(_ ++ _)
                  nd(rest, vsds++accds, accStore, acctime, accInCache, newOutCache)
                })
          }
        }

        nd(closures, Set(), bstore, newTime, inCache, outCache)

      case ae if isAtomic(ae) =>
        val ds = aeval(ae, env, bstore)
        continue(Set(ValStore(ds, bstore, newTime)), bstore, newTime, inCache, outCache)
    }
  }
    
  def done: Function2[Expr, Env, Continue] = (e: Expr, env: Env) => { 
    (vs: Set[ValStore], store: BStore, time: Time, inCache: Cache, outCache: Cache) => { 
      val newOutCache = outCache.update(Config(e, env), vs)
      //println(s"final out: ${newOutCache.map.mkString("\n")}\n")
      println(s"final store: ${store}\n")
      (vs, outCache)
    }
  }

  def analyze1(e: Expr, cache: Cache): RetType = { 
    aval(e, Map(), Store[BAddr, Storable](Map()), List(), cache, cache, done(e, Map()))
  }
  
  def analyze(e: Expr): Set[ValStore] = {
    def iter(cache: Cache): RetType = {
      val (vs, newCache) = analyze1(e, cache)
      if (cache == newCache) (vs, newCache)
      else iter(newCache)
    }
    val (vs, cache) = iter(Store[Config, ValStore](Map()))
    val summarized = cache.map.values.map(_.map(_.store).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))).foldLeft(Store[BAddr, Storable](Map()))(_.join(_))
    println(s"summarized: $summarized")
    vs
  }
}

object RefuncBigStep {
  type Cont = Function5[Set[Storable], BStore, Time, List[State], Set[State], Set[State]]
  case class State(e: Expr, env: Env, bstore: BStore, time: Time, k: Cont)

  def tick(s: State): Time = (s.e::s.time).take(k)
  def done: Cont = (ds: Set[Storable], bstore: BStore, time: Time, todo: List[State], seen: Set[State]) => {
    todo match {
      case Nil => seen
      case _ => abseval(todo, seen)
    }
  }
  def inject(e: Expr): State = State(e, Map(), Store[BAddr, Storable](Map()), List(), done)

  def abseval(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => abseval(tl, seen)
    case hd::tl =>
      val newTime = tick(hd)
      hd match {
        case State(Let(x, ae, e), env, bstore, time, k) if isAtomic(ae) =>
          val baddr = allocBind(x, newTime)
          val newEnv = env + (x -> baddr)
          val newStore = bstore.update(baddr, aeval(ae, env, bstore))
          val newState = State(e, newEnv, newStore, newTime, k)
          abseval(newState::tl, seen + hd)
        case State(Lrc(bds, body), env, bstore, time, k) =>
          val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
          val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
          val newState = State(body, newEnv, newBStore, time, k)
          abseval(newState::tl, seen + hd)
        case State(Let(x, App(f, ae), e), env, bstore, time, k) =>
          val newStates = for (Clos(Lam(v, body), c_env) <- aeval(f, env, bstore).toList) yield {
            val baddr = allocBind(v, newTime)
            val newEnv = c_env + (v -> baddr)
            val newStore = bstore.update(baddr, aeval(ae, env, bstore))
            State(body, newEnv, newStore, newTime, (ds: Set[Storable], bstore: BStore, time: Time, todo: List[State], seen: Set[State]) => {
                    val baddr = allocBind(x, time)
                    val newEnv = env + (x -> baddr)
                    val newStore = bstore.update(baddr, ds)
                    val newState = State(e, newEnv, newStore, time, k)
                    abseval(newState::todo, seen)
                  })
          }
          abseval(newStates ++ tl, seen + hd)
        case State(ae, env, bstore, time, k) if isAtomic(ae) =>
          k(aeval(ae, env, bstore), bstore, newTime, tl, seen + hd)
      }
  }

  def analyze(e: Expr): Set[State] = abseval(List(inject(e)), Set())
}

object RefuncBigStepInterp {
  //TODO: embedded seen into cont
  //TODO: correspondent with P4F?
  type Cont = Function3[Set[Storable], BStore, Time, Set[State]]
  case class State(e: Expr, env: Env, bstore: BStore, time: Time)

  def tick(e: Expr, oldtime: Time): Time = (e::oldtime).take(k)

  var seen: Set[State] = Set()
  def addState(s: State) = {
    seen = seen + s
    println(s"Add state, expr: ${s.e}")
  }

  def abseval(e: Expr, env: Env, bstore: BStore, time: Time, k: Cont): Set[State] = {
    val state = State(e, env, bstore, time)
    val newTime = tick(e, time)

    if (seen.contains(state)) {
      println(s"Seen ${state}")
      return seen
    }
    else { addState(state) }
    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        abseval(e, newEnv, newStore, newTime, k)
      case Lrc(bds, body) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        abseval(body, newEnv, newBStore, newTime, k)
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, bstore).filter(_.isInstanceOf[Clos]).toList
        def handleNondeterministic(closures: List[Storable], accds: Set[Storable], accbstore: BStore, acctime: Time): Set[State] = {
          closures match {
            case Nil =>
              val baddr = allocBind(x, acctime) //TODO: which time?
              val newEnv = env + (x -> baddr)
              val newStore = accbstore.update(baddr, accds)
              abseval(e, newEnv, newStore, acctime, k)
            case Clos(Lam(v, body), c_env)::rest =>
              val baddr = allocBind(v, newTime) //TODO: which time?
              val newEnv = c_env + (v -> baddr)
              val newStore = accbstore.update(baddr, aeval(ae, env, bstore)) //TODO: which store?
              abseval(body, newEnv, newStore, newTime, (ds: Set[Storable], accbstore: BStore, acctime: Time) => {
                        handleNondeterministic(rest, accds++ds, accbstore, acctime)
                      })
          }
        }
        handleNondeterministic(closures, Set(), bstore, newTime)
      case ae if isAtomic(ae) =>
        k(aeval(ae, env, bstore), bstore, newTime)
    }
  }

  def done: Cont = (ds: Set[Storable], bstore: BStore, time: Time) => seen
  def analyze(e: Expr): Set[State] = {
    seen = Set()
    abseval(e, Map(), Store[BAddr, Storable](Map()), List(), done)
  }
}

object RefuncBigStepInterp1 {
  //TODO: correspondent with P4F?
  //TODO: can we do this using delimited continuation?
  import RefuncBigStepInterp._
  type Cont = Function4[Set[Storable], BStore, Time, Set[State], Set[State]]

  def tick(e: Expr, oldtime: Time): Time = (e::oldtime).take(k)

  def abseval(e: Expr, env: Env, bstore: BStore, time: Time, seen: Set[State], k: Cont): Set[State] = {
    val state = State(e, env, bstore, time)
    val newTime = tick(e, time)
    val newSeen = seen + state
    //TODO: would miss some states?
    //TODO: more formal description of this lfp
    //TODO: use ADI's cache fixpoint
    //If we have seen this state (even without continuation), then it implies that we also have seen all the states it generates.
    //But this is not true in small-step AAM with UB stack.
    if (newSeen.size == seen.size) return seen
    println(s"Add state, expr: ${state.e}")
    e match {
      case Let(x, ae, e) if isAtomic(ae) =>
        val baddr = allocBind(x, newTime)
        val newEnv = env + (x -> baddr)
        val newStore = bstore.update(baddr, aeval(ae, env, bstore))
        abseval(e, newEnv, newStore, newTime, newSeen, k)
      case Lrc(bds, body) =>
        val newEnv = bds.foldLeft(env)((accenv: Env, bd: Bind) => { assert(isAtomic(bd.e)); accenv + (bd.x -> allocBind(bd.x, newTime)) })
        val newBStore = bds.foldLeft(bstore)((accbst: BStore, bd: Bind) => { accbst.update(allocBind(bd.x, newTime), aeval(bd.e, newEnv, accbst)) })
        abseval(body, newEnv, newBStore, newTime, newSeen, k)
      case Let(x, App(f, ae), e) =>
        val closures = aeval(f, env, bstore).filter(_.isInstanceOf[Clos]).toList.asInstanceOf[List[Clos]]
        def handleNondeterministic(closures: List[Clos], accds: Set[Storable], accbstore: BStore, acctime: Time, accseen: Set[State]): Set[State] = {
          closures match {
            case Nil =>
              val baddr = allocBind(x, acctime) //TODO: which time?
              val newEnv = env + (x -> baddr)
              val newStore = accbstore.update(baddr, accds)
              abseval(e, newEnv, newStore, acctime, accseen, k)
            case Clos(Lam(v, body), c_env)::rest =>
              val baddr = allocBind(v, newTime) //TODO: which time?
              val newEnv = c_env + (v -> baddr)
              val newStore = accbstore.update(baddr, aeval(ae, env, bstore)) //TODO: which store?
              abseval(body, newEnv, newStore, newTime, accseen, (ds: Set[Storable], accbstore: BStore, acctime: Time, accseen: Set[State]) => {
                        handleNondeterministic(rest, accds++ds, accbstore, acctime, accseen)
                      })
          }
        }
        handleNondeterministic(closures, Set(), bstore, newTime, newSeen)
      case ae if isAtomic(ae) =>
        k(aeval(ae, env, bstore), bstore, newTime, newSeen)
    }
  }

  def done: Cont = (ds: Set[Storable], bstore: BStore, time: Time, seen: Set[State]) => seen
  def analyze(e: Expr): Set[State] = { abseval(e, Map(), Store[BAddr, Storable](Map()), List(), Set(), done) }
}

object ANFAAMTest {
  def main(args: Array[String]) {
    val id = Lam("x", Var("x"))
    val e1 = Let("id", id,
                 Let("y", App(id, Num(1)),
                     Let("z", App(id, Num(2)),
                         Var("y"))))
    println(SmallStep.analyze(e1).map(s => s.bstore).mkString("\n"))
    println("=============================")
    println(SmallStepP4F.analyze(e1).map(s => s.bstore).mkString("\n"))
    println("=============================")
    println(SmallStepUBStack.analyze(e1).map(s => s.bstore).mkString("\n"))
    println("=============================")

    assert(SmallStepUBStack.analyze(e1) == FusedUBStack.analyze(e1))
    assert(FusedUBStack.analyze(e1) == DisentangledBigStepUBStack.analyze(e1))
    assert(DisentangledBigStepUBStack.analyze(e1).size == RefuncBigStep.analyze(e1).size)
    assert(DisentangledBigStepUBStack.analyze(e1).size == DisentangledBigStepUBStack1.analyze(e1).size)
    assert(RefuncBigStep.analyze(e1).size == RefuncBigStepInterp.analyze(e1).size)

    println("=============================")
    println(RefuncBigStepUBStackFirstTry.analyze(e1))
    println("=============================")
    println(RefuncBigStepUBStackSecondTry.analyze(e1))
    println("=============================")
    
    //println(RefuncBigStepInterp.analyze(e1).map(s => s.bstore).mkString("\n"))
    """(letrec ([f1 (lambda (x)
                      (let ([x1 (f2 x)]) x1))]
                [f2 (lambda (y)
                      (let ([y1 (f1 y)]) y1))])
           (let ([res (f1 1)])
              res))"""
    val mutrec = Lrc(List(Bind("f1", Lam("x", Let("x1", App(Var("f2"), Var("x")), Var("x1")))),
                             Bind("f2", Lam("y", Let("y1", App(Var("f1"), Var("y")), Var("y1"))))),
                        Let("res", App(Var("f1"), Num(1)),
                            Var("res")))
    val mutrec_ss = SmallStep.analyze(mutrec)
    println(mutrec_ss.size)
    println(mutrec_ss.toList.map(s => s.bstore).mkString("\n"))
    val mutrec_p4f = SmallStepP4F.analyze(mutrec)
    println(mutrec_p4f.size)
    val mutrec_refun = RefuncBigStepInterp.analyze(mutrec)
    println(mutrec_refun.size)

    //print(RefuncBigStepUBStackFirstTry.analyze(mutrec))
    print(RefuncBigStepUBStackSecondTry.analyze(mutrec))


    //val mutrec_refun1 = RefuncBigStepInterp1.analyze(mutrec)
    //assert(mutrec_refun == mutrec_refun1)
    //println(mutrec_refun1.size)
  
    /*
    val omega = Let("f", Lam("x", Let("d1", App(Var("x"), Var("x")), Var("d1"))),
                    Let("d2", App(Var("f"), Var("f")), Var("d2")))
    println(SmallStepP4F.analyze(omega).size)
    println(RefuncBigStepInterp1.analyze(omega).size)
    */
    //SmallStepUBStack.analyze(mutrec) // state space is not finite
    //println(RefuncBigStep.analyze(mutrec).size) // state space is not finite
    
  }
}
