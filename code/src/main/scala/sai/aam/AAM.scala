package sai.aam

import sai.parser.direct._

/* Small-Step Abstracting Abstract Machine
 * TODO: Add support of numbers, `let`, and `letrec`
 * Note: we use `seen` as a List rather than a Set, to record the order
 *   when they are added. But a Set is also workable.
 */

object AAM {
  type Time = List[Expr]

  abstract class Addr
  case class BAddr(name: String, time: Time) extends Addr
  case class KAddr(callsite: Expr, time: Time) extends Addr

  type Env = Map[String, Addr]

  abstract class Kont
  object Halt extends Kont
  case class KArg(e: Expr, env: Env, addr: Addr) extends Kont
  case class KApp(lam: Lam, env: Env, addr: Addr) extends Kont

  abstract class Storable
  case class Clos(v: Lam, env: Env) extends Storable
  case class Cont(k: Kont) extends Storable

  case class Store(map: Map[Addr, Set[Storable]]) {
    def apply(addr: Addr): List[Storable] = map(addr).toList
    def update(addr: Addr, d: Set[Storable]): Store = {
      val oldd = map.getOrElse(addr, Set())
      Store(map ++ Map(addr -> (d ++ oldd)))
    }
    def update(addr: Addr, sd: Storable): Store = update(addr, Set(sd))
    def ++(other: Store): Store = {
      other.map.foldLeft(this){ case (s, (k, v)) => s.update(k, v) }
    }
    override def toString(): String = "Store: " + map.mkString("\n         ")
  }

  case class State(e: Expr, env: Env, store: Store, k: Kont, time: Time) {
    override def toString(): String = {
      "State(\n  " + e + ",\n  " + env + ",\n  " + store + ",\n  " + k + ",\n)\n"
    }
  }

  def allocBind(x: String, time: Time): Addr = BAddr(x, time)
  def allocCont1CFA(callsite: Expr, time: Time): Addr = KAddr(callsite, time)

  def k: Int = 0

  def tick(s: State): Time = s.e match {
    case app: App => (app::s.time).take(k)
    case _ => s.time
  }

  def inject(e: Expr): State = State(e, Map(), Store(Map()), Halt, List())
}

import AAM._

object SmallStepAAM {
  def step(s: State): List[State] = {
    val newTime = tick(s)
    s match {
      case State(Var(x), env, store, k, t) =>
        for (Clos(lam, newEnv) <- store(env(x))) yield {
          State(lam, newEnv, store, k, newTime)
        }
      case State(app@App(e1, e2), env, store, k, t) =>
        val kaddr = allocCont1CFA(app, newTime)
        val newK = KArg(e2, env, kaddr)
        val newStore = store.update(kaddr, Cont(k))
        List(State(e1, env, newStore, newK, newTime))
      case State(lam: Lam, env, store, KArg(e, env1, a), t) =>
        List(State(e, env1, store, KApp(lam, env, a), newTime))
      case State(lam: Lam, env, store, KApp(Lam(x, e), env1, a), t) =>
        val baddr = allocBind(x, newTime)
        for (Cont(k) <- store(a)) yield {
          State(e, env1+(x->baddr), store.update(baddr, Clos(lam, env)), k, newTime)
        }
      case State(v, _, _, Halt, _) => List(s)
    }
  }

  def drive(todo: List[State], seen: List[State]): List[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl =>
      val states = step(hd)
      println(s"Add seen: $hd")
      println(s"New states: $states")
      drive(states++tl, seen++List(hd))
  }

  def analyze(e: Expr): List[State] = drive(List(inject(e)), List())
}

object UnboundedStackSmallStepAAM {
  abstract class Kont
  object Halt extends Kont
  case class KArg(e: Expr, env: Env, k: Kont) extends Kont
  case class KApp(lam: Lam, env: Env, k: Kont) extends Kont

  case class State(e: Expr, env: Env, store: Store, k: Kont, time: Time) {
    override def toString(): String = {
      "State(\n  " + e + ",\n  " + env + ",\n  " + store + ",\n  " + k + ",\n)\n"
    }
  }

  def tick(s: State): Time = s.e match {
    case app: App => (app::s.time).take(k)
    case _ => s.time
  }

  def step(s: State): List[State] = {
    val newTime = tick(s)
    s match {
      case State(Var(x), env, store, k, t) =>
        for (Clos(lam, newEnv) <- store(env(x))) yield {
          State(lam, newEnv, store, k, newTime)
        }
      case State(App(e1, e2), env, store, k, t) =>
        val newK = KArg(e2, env, k)
        List(State(e1, env, store, newK, newTime))
      case State(lam: Lam, env, store, KArg(e, env1, k), t) =>
        List(State(e, env1, store, KApp(lam, env, k), newTime))
      case State(lam: Lam, env, store, KApp(Lam(x, e), env1, k), t) =>
        val baddr = allocBind(x, newTime)
        List(State(e, env1+(x->baddr), store.update(baddr, Clos(lam, env)), k, newTime))
      case State(v, _, _, Halt, _) => List(s)
    }
  }

  def drive(todo: List[State], seen: List[State]): List[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive(tl, seen)
    case hd::tl =>
      val states = step(hd)
      drive(states++tl, seen++List(hd))
  }

  def inject(e: Expr): State = State(e, Map(), Store(Map()), Halt, List())

  def analyze(e: Expr): List[State] = drive(List(inject(e)), List())
}

object BigStepAAM {
  /* Note: Obtained by fusing SmallStepAAM */
  def drive_step(todo: List[State], seen: List[State]): List[State] = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => drive_step(tl, seen)
    case hd::tl =>
      val newTime = tick(hd)
      val newStates = hd match {
        case State(Var(x), env, store, k, t) =>
          for (Clos(lam, newEnv) <- store(env(x))) yield {
            State(lam, newEnv, store, k, newTime)
          }
        case State(app@App(e1, e2), env, store, k, t) =>
          val kaddr = allocCont1CFA(app, newTime)
          val newK = KArg(e2, env, kaddr)
          val newStore = store.update(kaddr, Cont(k))
          List(State(e1, env, newStore, newK, newTime))
        case State(lam: Lam, env, store, KArg(e, env1, a), t) =>
          List(State(e, env1, store, KApp(lam, env, a), newTime))
        case State(lam: Lam, env, store, KApp(Lam(x, e), env1, a), t) =>
          val baddr = allocBind(x, newTime)
          for (Cont(k) <- store(a)) yield {
            State(e, env1+(x->baddr), store.update(baddr, Clos(lam, env)), k, newTime)
          }
        case State(v, _, _, Halt, _) => List(hd)
      }
      drive_step(newStates++tl, seen++List(hd))
  }

  def analyze(e: Expr): List[State] = drive_step(List(inject(e)), List())
}

object DisentangledBigStepAAM {
  /* Note: Uses an explicit `continue` function, refactored from BigSteAAM
   * Note: to mimic TracingBigStepCESK, some intermediate states are recorded manually (itmdState).
   */

  type Collection = List[State]

  def aeval(todo: List[State], seen: Collection): Collection = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => aeval(tl, seen)
    case hd::tl =>
      val newTime = tick(hd)
      hd match {
        case State(Var(x), env, store, k, t) =>
          val itmdStates = for (Clos(lam, newEnv) <- store(env(x))) yield {
            State(lam, newEnv, store, k, newTime)
          }
          continue(k, store(env(x)), store, newTime, tl, seen++List(hd)++itmdStates)
        case State(lam: Lam, env, store, k, t) =>
          continue(k, List(Clos(lam, env)), store, newTime, tl, seen++List(hd))
        case State(app@App(e1, e2), env, store, k, t) =>
          val kaddr = allocCont1CFA(app, newTime)
          val newK = KArg(e2, env, kaddr)
          val newState = State(e1, env, store.update(kaddr, Cont(k)), newK, newTime)
          aeval(newState::tl, seen++List(hd))
      }
  }

  def continue(k: Kont, ds: List[Storable], store: Store, time: Time, todo: List[State], seen: Collection): Collection = k match {
    case Halt => aeval(todo, seen)
    case KArg(e, env, kaddr) =>
      val newStates = for (Clos(lam, env1) <- ds) yield { State(e, env, store, KApp(lam, env1, kaddr), time) }
      aeval(newStates++todo, seen)
    case KApp(Lam(x, e), env, kaddr) =>
      val baddr = allocBind(x, time)
      val newStates = for (Cont(k) <- store(kaddr); clos <- ds) yield {
        State(e, env+(x->baddr), store.update(baddr, clos), k, time)
      }
      aeval(newStates++todo, seen)
  }

  def analyze(e: Expr): Collection = aeval(List(inject(e)), List())
}

object RefuncBigStepAAM {
  /* Note: Refunctionalized Big-Step AAM, based on DisentangledBigStepAAM
   * Note: Here `todo` is an an explicit stack.
   * Question: is number of `k` finite?
   */
  type Collection = List[State]
  type CollectCont = Function5[List[Storable], Time, Store, List[State], Collection, Collection]
  case class State(e: Expr, env: Env, store: Store, time: Time, k: CollectCont)

  def tick(s: State): Time = s.e match {
    case app: App => (app::s.time).take(k)
    case _ => s.time
  }

  def aeval(todo: List[State], seen: Collection): Collection = todo match {
    case Nil => seen
    case hd::tl if seen.contains(hd) => aeval(tl, seen)
    case hd::tl =>
      val newTime = tick(hd) //TODO: put into cont, for 0CFA does not matter
      hd match {
        case State(Var(x), env, store, t, k) =>
          val itmdStates = for (Clos(lam, newEnv) <- store(env(x))) yield {
            State(lam, newEnv, store, newTime, k)
          }
          k(store(env(x)), newTime, store, tl, seen ++ List(hd) ++ itmdStates)
        case State(lam: Lam, env, store, t, k) =>
          k(List(Clos(lam, env)), newTime, store, tl, seen ++ List(hd))
        case State(app@App(e1, e2), env, store, t, k) =>
          val e1State = State(e1, env, store, newTime, (e1vs: List[Storable], e1Time: Time, e1store: Store, e1todo: List[State], e1seen: Collection) => {
                                val e1clos = e1vs.asInstanceOf[List[Clos]]
                                val e2State = State(e2, env, e1store, e1Time, (e2vs: List[Storable], e2Time: Time, e2store: Store, e2todo: List[State], e2seen: Collection) => {
                                                      val newStates = for (Clos(Lam(x, body), e1env) <- e1clos) yield {
                                                        val addr = allocBind(x, e2Time)
                                                        State(body, e1env+(x->addr), e2store.update(addr, e2vs.toSet), e2Time, k)
                                                      }
                                                      aeval(newStates++e2todo, e2seen)
                                                    })
                                aeval(e2State::e1todo, e1seen)
                })
          aeval(e1State::tl, seen ++ List(hd))
      }
  }

  def done: CollectCont = (ds: List[Storable], time: Time, store: Store, todo: List[State], seen: Collection) => {
     todo match {
     case Nil => seen
     case _ => aeval(todo, seen)
     }
  }

  def inject(e: Expr): State = State(e, Map(), Store(Map()), List(), done)

  def analyze(e: Expr): Collection = aeval(List(inject(e)), List())
}

object AAMTest {
  def check(e: Expr) = {
    val sml_e_states = SmallStepAAM.analyze(e).toSet
    val big_e_states = BigStepAAM.analyze(e).toSet
    assert(sml_e_states == big_e_states)
    val refact_e_states = DisentangledBigStepAAM.analyze(e).toSet
    assert(sml_e_states == refact_e_states)
    println(s"Checked: ${sml_e_states.size} states")
  }

  def main(args: Array[String]) {
    val id = Lam("x", Var("x"))
    val e1 = App(Lam("id", App(App(Var("id"), Var("id")), App(Var("id"), Var("id")))), id)
    check(e1)

    val e2 = App(Lam("x", App(Var("x"), Var("x"))), Lam("x", App(Var("x"), Var("x"))))
    check(e2)

    val fa = Lam("fa", Var("fa"))
    val fb = Lam("fb", Var("fb"))
    val e3 = App(Lam("id", App(Lam("a", App(Lam("b", Var("a")), App(Var("id"), fb))), App(Var("id"), fa))), id)

    println(UnboundedStackSmallStepAAM.analyze(e1))

    /*
    val e3_states = SmallStepAAM.analyze(e3)
    val summary = e3_states.tail.foldLeft(e3_states.head.store)({ case (store: Store, s: State) => store ++ s.store })
    print(e3_states.size)
    println(summary)
    println("=========================================")

    val e3_refunc_states = RefuncBigStepAAM.analyze(e3)
    val refunc_summary = e3_refunc_states.tail.foldLeft(e3_refunc_states.head.store)({ case (store: Store, s: RefuncBigStepAAM.State) => store ++ s.store })
    println(refunc_summary)
     */
    //println(RefuncBigStepAAM.analyze(e3).mkString(" ==> \n\n"))

    //println(SmallStepAAM.analyze(e1).mkString(" =>\n"))
    //println(RefuncBigStepAAM.analyze(e1).map((s: sai.aam.RefuncBigStepAAM.State) => s.e).mkString(" =>\n"))
    //println(RefuncBigStepAAM.analyze(e1).size)
    //println(DisentangledBigStepAAM.analyze(e1)./*map((s: State) => s.e).*/mkString(" =>\n"))

  }
}
