package sai.aam

import sai.direct.parser._

/* Concrete small-step and big-step CESK machines */

object CESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Storable]

  abstract class Storable
  case class Clos(lam: Lam, env: Env) extends Storable

  abstract class Kont
  object Done extends Kont
  case class KArg(e: Expr, env: Env, k: Kont) extends Kont
  case class KApp(lam: Lam, env: Env, k: Kont) extends Kont

  case class State(e: Expr, env: Env, store: Store, k: Kont)

  def alloc(store: Store): Addr = store.keys.size + 1

  def isValue(e: Expr): Boolean = e.isInstanceOf[Value]

  def inject(e: Expr): State = State(e, Map(), Map(), Done)
}

import CESK._

object SmallStepCESK {
  def step(s: State): State = s match {
    case State(Var(x), env, store, k) => store(env(x)) match {
      case Clos(lam, env1) => State(lam, env1, store, k)
    }
    case State(App(e1, e2), env, store, k) =>
      State(e1, env, store, KArg(e2, env, k))
    case State(lam: Lam, env, store, KArg(e, env1, k)) =>
      State(e, env1, store, KApp(lam, env, k))
    case State(lam: Lam, env, store, KApp(Lam(x, e), env1, k)) =>
      val addr = alloc(store)
      State(e, env1 + (x -> addr), store + (addr -> Clos(lam, env)), k)
  }

  /* Evaluate to final state */
  def drive(s: State): State = s match {
    case State(v, _, _, Done) if isValue(v) => s
    case _ => drive(step(s))
  }

  def eval(e: Expr): State = drive(inject(e))

  /* Trace semantics */
  def tracingDrive(s: State): List[State] = s match {
    case State(v, _, _, Done) if isValue(v) => List(s)
    case _ => s::tracingDrive(step(s))
  }

  def tracingEval(e: Expr): List[State] = tracingDrive(inject(e))
}

object BigStepCESK {
  /* Note: obtained by fusion */
  def drive_step(s: State): State = s match {
    case State(Var(x), env, store, k) => store(env(x)) match {
      case Clos(lam, env1) => drive_step(State(lam, env1, store, k))
    }
    case State(App(e1, e2), env, store, k) =>
      drive_step(State(e1, env, store, KArg(e2, env, k)))
    case State(lam: Lam, env, store, KArg(e, env1, k)) =>
      drive_step(State(e, env1, store, KApp(lam, env, k)))
    case State(lam: Lam, env, store, KApp(Lam(x, e), env1, k)) =>
      val addr = alloc(store)
      drive_step(State(e, env1 + (x -> addr), store + (addr -> Clos(lam, env)), k))
    case State(v, _, _, Done) if isValue(v) => s
  }

  def eval(e: Expr): State = drive_step(inject(e))
}

object TracingBigStepCESK {
  /* Note: based on BigStepCESK, adding tracing semantics */
  def drive_step(s: State): List[State] = s match {
    case State(Var(x), env, store, k) => store(env(x)) match {
      case Clos(lam, env1) => s::drive_step(State(lam, env1, store, k))
    }
    case State(App(e1, e2), env, store, k) =>
      s::drive_step(State(e1, env, store, KArg(e2, env, k)))
    case State(lam: Lam, env, store, KArg(e, env1, k)) =>
      s::drive_step(State(e, env1, store, KApp(lam, env, k)))
    case State(lam: Lam, env, store, KApp(Lam(x, e), env1, k)) =>
      val addr = alloc(store)
      s::drive_step(State(e, env1 + (x -> addr), store + (addr -> Clos(lam, env)), k))
    case State(v, _, _, Done) if isValue(v) => List(s)
  }

  def tracingEval(e: Expr): List[State] = drive_step(inject(e))
}

object RefactoredBigStepCESK {
  /* Note: uses an explicit `continue` function, refactored from BigStepCESK */
  def eval_top(s: State): State = s match {
    case State(Var(x), env, store, k) => continue(k, store(env(x)), env, store)
    case State(lam: Lam, env, store, k) => continue(k, Clos(lam, env), env, store)
    case State(App(e1, e2), env, store, k) => eval_top(State(e1, env, store, KArg(e2, env, k)))
  }

  def continue(k: Kont, w: Storable, env: Env, store: Store): State = k match {
    case Done =>
      val clos = w.asInstanceOf[Clos]
      State(clos.lam, clos.env, store, k)
    case KArg(e, env, k) =>
      val clos = w.asInstanceOf[Clos]
      eval_top(State(e, env, store, KApp(clos.lam, clos.env, k)))
    case KApp(Lam(x, e), env, k) =>
      val addr = alloc(store)
      eval_top(State(e, env + (x -> addr), store + (addr -> w), k))
  }

  def eval(e: Expr): State = eval_top(inject(e))
}

object RefactoredTracingBigStepCESK {
  /* Note: based on RefactoredBigStepCESK, adding tracing semantics.
   * Note: since the execution has been linearized, are changes to store are accumulated to the end.
   * Note: to mimic TracingBigStepCESK, some intermediate states are recorded manually (itmdState).
   *   If the control is a variable, TracingBigStepCESK will transit to a state that holds
   *   the value of this variable; but RefactoredTracingBigStepCESK transits to a state
   *   that the control is coming from current continuation.
   */
  def eval_top(s: State): List[State] = s match {
    case State(Var(x), env, store, k) =>
      val v = store(env(x)).asInstanceOf[Clos]
      val itmdState = State(v.lam, v.env, store, k)
      s::itmdState::continue(k, v, env, store)
    case State(lam: Lam, env, store, k) =>
      s::continue(k, Clos(lam, env), env, store)
    case State(App(e1, e2), env, store, k) => s::eval_top(State(e1, env, store, KArg(e2, env, k)))
  }

  def continue(k: Kont, w: Storable, env: Env, store: Store): List[State] = k match {
    case Done => List()
    case KArg(e, env, k) =>
      val clos = w.asInstanceOf[Clos]
      eval_top(State(e, env, store, KApp(clos.lam, clos.env, k)))
    case KApp(Lam(x, e), env, k) =>
      val addr = alloc(store)
      eval_top(State(e, env + (x -> addr), store + (addr -> w), k))
  }

  def tracingEval(e: Expr): List[State] = eval_top(inject(e))
}

object RefuncBigStepCESK {
  /* Note: A CPS interpreter, based on RefactoredBigStepCESK.
   */
  type Cont = Function2[Storable, Store, Storable]
  case class State(e: Expr, env: Env, store: Store, k: Cont)

  def eval_top(s: State): Storable = s match {
    case State(Var(x), env, store, k) => k(store(env(x)), store)
    case State(lam: Lam, env, store, k) => k(Clos(lam, env), store)
    case State(App(e1, e2), env, store, k) =>
      eval_top(State(e1, env, store, (e1v: Storable, e1store: Store) => {
                       val e1clos = e1v.asInstanceOf[Clos]
                       eval_top(State(e2, env, e1store, (e2v: Storable, e2store: Store) => {
                                        val addr = alloc(e2store)
                                        eval_top(State(e1clos.lam.body, e1clos.env + (e1clos.lam.x -> addr), e2store + (addr -> e2v), k))
                                      }))
                     }))
  }

  def inject(e: Expr): State = State(e, Map(), Map(), (s: Storable, store: Store) => s)
  def eval(e: Expr): Storable = eval_top(inject(e))
}

object RefuncTracingBigStepCESK {
  /* Note: Tracing semantics, based on RefuncBigStepCESK.
   * TODO: do we need to accumulate the store? what would happen if we allocate continuations on store?
   * Note: to mimic TracingBigStepCESK, some intermediate states are recorded manually (itmdState)
   */
  type TracingCont = Function3[Storable, Store, List[State], List[State]]
  case class State(e: Expr, env: Env, store: Store, k: TracingCont)

  def eval_top(s: State): List[State] = s match {
    case State(Var(x), env, store, k) =>
      val v = store(env(x)).asInstanceOf[Clos]
      val itmdState = State(v.lam, v.env, store, k)
      k(store(env(x)), store, List(s, itmdState))
    case State(lam: Lam, env, store, k) => k(Clos(lam, env), store, List(s))
    case State(App(e1, e2), env, store, k) =>
      eval_top(State(e1, env, store, (e1v: Storable, e1store: Store, e1ss: List[State]) => {
                       val e1clos = e1v.asInstanceOf[Clos]
                       eval_top(State(e2, env, e1store, (e2v: Storable, e2store: Store, e2ss: List[State]) => {
                                        val addr = alloc(e2store)
                                        eval_top(State(e1clos.lam.body, e1clos.env + (e1clos.lam.x -> addr), e2store + (addr -> e2v),
                                                       (bv: Storable, store: Store, bdss: List[State]) => {
                                                         k(bv, store, List(s) ++ e1ss ++ e2ss ++ bdss)
                                                       }))
                                      }))
                     }))
  }

  def inject(e: Expr): State = State(e, Map(), Map(), (s: Storable, store: Store, ss: List[State]) => ss)
  def eval(e: Expr): List[State] = eval_top(inject(e))
}

object DirectBigStepCESK {
  /* Note: transform back to direct style, based on RefuncBigStepCESK
   */
  def eval_top(e: Expr, env: Env, store: Store): (Storable, Store) = e match {
    case Var(x) => (store(env(x)), store)
    case lam: Lam => (Clos(lam, env), store)
    case App(e1, e2) =>
      val (e1v, e1store) = eval_top(e1, env, store)
      val (e2v, e2store) = eval_top(e2, env, e1store)
      val addr = alloc(e2store)
      val e1clos = e1v.asInstanceOf[Clos]
      eval_top(e1clos.lam.body, e1clos.env + (e1clos.lam.x -> addr), e2store + (addr -> e2v))
  }

  def eval(e: Expr): Storable = eval_top(e, Map(), Map())._1
}

object DirectTracingBigStepCESK {
  /* TODO: Tracing semantics, have to introduce a CPS layer?
   * Idea: To analyzing direct-style programs, AAM uses reduction/small-step semantics to
   *   expose the continuation of analyzed language explicitly, can we use continuation
   *   of defining language instead?
   */
}

object CESKTest {
  def main(args: Array[String]) {
    val e1 = App(Lam("id", App(App(Var("id"), Var("id")), App(Var("id"), Var("id")))), Lam("x", Var("x")))
    val big_e1 = BigStepCESK.eval(e1)
    val sml_e1 = SmallStepCESK.eval(e1)
    //println(big_e1)
    //println(sml_e1)
    assert(big_e1 == sml_e1)

    val big_e1_trace = TracingBigStepCESK.tracingEval(e1)
    val sml_e1_trace = SmallStepCESK.tracingEval(e1)
    assert(big_e1_trace == sml_e1_trace)

    val refact_big_e1_trace = RefactoredTracingBigStepCESK.tracingEval(e1)
    assert(refact_big_e1_trace == big_e1_trace)
    println(refact_big_e1_trace.mkString("\n"))

    println("-----------------------------------")

    val refunc_e1 = RefuncBigStepCESK.eval(e1)
    val refunc_e1_trace = RefuncTracingBigStepCESK.eval(e1)
    println(refunc_e1_trace.mkString("\n"))
    println(refunc_e1_trace.size)
    /*
    println("-----------------------------")
    println(refact_big_e1_trace.mkString(" -> \n"))
    assert(big_e1_trace == refact_big_e1_trace)
    */
  }
}
