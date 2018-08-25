package sai.direct.core

import sai.direct.core.parser._

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

object AAMTest {

  def main(args: Array[String]) {
    val id = Lam("x", Var("x"))
    val e1 = App(Lam("id", App(App(Var("id"), Var("id")), App(Var("id"), Var("id")))), id)

    val e2 = App(Lam("x", App(Var("x"), Var("x"))), Lam("x", App(Var("x"), Var("x"))))

    val fa = Lam("fa", Var("fa"))
    val fb = Lam("fb", Var("fb"))
    val e3 = App(Lam("id", App(Lam("a", App(Lam("b", Var("a")), App(Var("id"), fb))), App(Var("id"), fa))), id)

  }
}
