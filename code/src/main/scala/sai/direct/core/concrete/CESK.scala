package sai.direct.core.concrete

import sai.direct.core.parser._

/* Concrete small-step and big-step CESK machines */

object CESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Storable]

  abstract class Storable
  case class NumV(i: Int) extends Storable
  case class CloV(lam: Lam, env: Env) extends Storable

  abstract class Kont
  object Done extends Kont
  case class KArg(e: Expr, env: Env, k: Kont) extends Kont
  case class KApp(lam: Lam, env: Env, k: Kont) extends Kont

  case class State(e: Expr, env: Env, store: Store, k: Kont)

  def alloc(store: Store): Addr = store.keys.size + 1

  def isValue(e: Expr): Boolean = e.isInstanceOf[NumV] | e.isInstanceOf[Lam]

  def inject(e: Expr): State = State(e, Map(), Map(), Done)
}

import CESK._

object SmallStepCESK {
  def step(s: State): State = s match {
    case State(Var(x), env, store, k) => store(env(x)) match {
      case CloV(lam, env1) => State(lam, env1, store, k)
    }
    case State(App(e1, e2), env, store, k) =>
      State(e1, env, store, KArg(e2, env, k))
    case State(lam: Lam, env, store, KArg(e, env1, k)) =>
      State(e, env1, store, KApp(lam, env, k))
    case State(lam: Lam, env, store, KApp(Lam(x, e), env1, k)) =>
      val addr = alloc(store)
      State(e, env1 + (x -> addr), store + (addr -> CloV(lam, env)), k)
  }

  /* Evaluate to final state */
  def drive(s: State): State = s match {
    case State(v, _, _, Done) if isValue(v) => s
    case _ => drive(step(s))
  }

  def eval(e: Expr): State = drive(inject(e))
}

object RefuncBigStepCESK {
  /* A refunctionalized CPS interpreter. */
  type Cont = (Storable, Store) => Storable
  case class State(e: Expr, env: Env, store: Store, k: Cont)

  def interp(s: State): Storable = s match {
    case State(Var(x), env, store, k) => k(store(env(x)), store)
    case State(lam: Lam, env, store, k) => k(CloV(lam, env), store)
    case State(App(e1, e2), env, store, k) =>
      interp(State(e1, env, store, (e1v: Storable, e1store: Store) => {
                       val e1clos = e1v.asInstanceOf[CloV]
                       interp(State(e2, env, e1store, (e2v: Storable, e2store: Store) => {
                                        val addr = alloc(e2store)
                                        interp(State(e1clos.lam.body, e1clos.env + (e1clos.lam.x -> addr), e2store + (addr -> e2v), k))
                                      }))
                     }))
  }

  def inject(e: Expr): State = State(e, Map(), Map(), (s: Storable, store: Store) => s)

  def eval(e: Expr): Storable = interp(inject(e))
}

object BigStepCESK {
  /* A big-step interpreter written in direct-style. */
  def interp(e: Expr, env: Env, store: Store): (Storable, Store) = e match {
    case Var(x) => (store(env(x)), store)
    case lam: Lam => (CloV(lam, env), store)
    case App(e1, e2) =>
      val (e1v, e1store) = interp(e1, env, store)
      val (e2v, e2store) = interp(e2, env, e1store)
      val addr = alloc(e2store)
      val e1clos = e1v.asInstanceOf[CloV]
      interp(e1clos.lam.body, e1clos.env + (e1clos.lam.x -> addr), e2store + (addr -> e2v))
  }

  def eval(e: Expr): Storable = interp(e, Map(), Map())._1
}

object CESKTest {
  def main(args: Array[String]) {
    val e1 = App(Lam("id", App(App(Var("id"), Var("id")), App(Var("id"), Var("id")))), Lam("x", Var("x")))
    val big_e1 = BigStepCESK.eval(e1)
    val sml_e1 = SmallStepCESK.eval(e1)
    //println(big_e1)
    //println(sml_e1)
    assert(big_e1 == sml_e1)

    println("-----------------------------------")

    val refunc_e1 = RefuncBigStepCESK.eval(e1)
    /*
    println("-----------------------------")
    println(refact_big_e1_trace.mkString(" -> \n"))
    assert(big_e1_trace == refact_big_e1_trace)
    */
  }
}
