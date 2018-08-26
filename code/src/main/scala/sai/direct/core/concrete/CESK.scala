package sai.direct.core.concrete

import sai.direct.core.parser._

/* Concrete small-step and big-step CESK machines */

object CESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Storable]

  abstract class Storable
  object NotAValue extends Storable
  case class NumV(i: Int) extends Storable with Expr
  case class CloV(λ: Lam, ρ: Env) extends Storable

  abstract class Kont
  object Halt extends Kont
  case class KArg(e: Expr, ρ: Env, κ: Kont) extends Kont
  case class KApp(lam: Lam, ρ: Env, κ: Kont) extends Kont
  case class KLet(x: String, e: Expr, ρ: Env, κ: Kont) extends Kont
  case class KLrc(as: List[Addr], bds: List[Bind], e: Expr, ρ: Env, κ: Kont) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, κ: Kont) extends Kont
  case class KAOp(op: Symbol, vs: List[NumV], es: List[Expr], ρ: Env, κ: Kont) extends Kont

  case class State(e: Expr, ρ: Env, σ: Store, κ: Kont)

  def alloc(σ: Store): Addr = σ.keys.size + 1

  def isValue(e: Expr): Boolean = e.isInstanceOf[NumV] | e.isInstanceOf[Lam]

  def inject(e: Expr): State = State(e, Map(), Map(), Halt)

  def evalArith(op: Symbol, vs: List[NumV]): NumV = op match {
    case '+ ⇒ vs.foldRight (NumV(0)) { case (NumV(i), NumV(j)) ⇒ NumV(j+i) }
    case '* ⇒ vs.foldRight (NumV(1)) { case (NumV(i), NumV(j)) ⇒ NumV(j*i) }
  }
}

import CESK._

object SmallStepCESK {
  /* Single step function */
  def step(s: State): State = s match {
    case State(Var(x), ρ, σ, κ) ⇒ σ(ρ(x)) match {
      case CloV(λ, _ρ) ⇒ State(λ, _ρ, σ, κ)
      case NumV(i) ⇒ State(Num(i), ρ, σ, κ)
      case NotAValue => throw new RuntimeException(s"Variable not defined: $x")
    }

    case State(Let(x, e, body), ρ, σ, κ) ⇒
      State(e, ρ, σ, KLet(x, body, ρ, κ))
    case State(NumV(i), ρ, σ, KLet(x, body, _ρ, κ)) ⇒
      val α = alloc(σ)
      State(body, _ρ + (x → α), σ + (α → NumV(i)), κ)
    case State(λ: Lam, ρ, σ, KLet(x, body, _ρ, κ)) ⇒
      val α = alloc(σ)
      State(body, _ρ + (x → α), σ + (α → CloV(λ, ρ)), κ)

    case State(Lrc(bds, body), ρ, σ, κ) ⇒
      val Bind(x, e) = bds.head
      val (_ρ, _σ, αs) = bds.foldRight (ρ, σ, List[Addr]()) { case (Bind(x, _), (ρ_*, σ_*, αs_*)) ⇒
        val α = alloc(σ_*)
        (ρ_* + (x → α), σ_* + (α → NotAValue), α::αs_*)
      }
      State(e, _ρ, _σ, KLrc(αs, bds.tail, body, _ρ, κ))
    case State(NumV(i), ρ, σ, KLrc(α::αs, bds, body, _ρ, κ)) ⇒
      bds match {
        case Nil ⇒
          State(body, _ρ, σ + (α → NumV(i)), κ)
        case Bind(x, e)::bds ⇒
          State(e, _ρ, σ + (α → NumV(i)), KLrc(αs, bds, body, _ρ, κ))
      }
    case State(λ: Lam, ρ, σ, KLrc(α::αs, bds, body, _ρ, κ)) ⇒
      bds match {
        case Nil ⇒
          State(body, _ρ, σ + (α → CloV(λ, ρ)), κ)
        case Bind(x, e)::bds ⇒
          State(e, _ρ, σ + (α → CloV(λ, ρ)), KLrc(αs, bds, body, _ρ, κ))
      }

    case State(AOp(op, e1, e2), ρ, σ, κ) ⇒
      State(e1, ρ, σ, KAOp(op, List(), List(e2), ρ, κ))
    case State(NumV(i), ρ, σ, KAOp(op, vs, Nil, _ρ, κ)) ⇒
      State(evalArith(op, NumV(i)::vs), ρ, σ, κ)
    case State(NumV(i), ρ, σ, KAOp(op, vs, e::es, _ρ, κ)) ⇒
      State(e, _ρ, σ, KAOp(op, NumV(i)::vs, es, _ρ, κ))

    case State(If0(cnd, thn, els), ρ, σ, κ) ⇒
      State(cnd, ρ, σ, KIf0(thn, els, ρ, κ))
    case State(NumV(i), ρ, σ, KIf0(thn, els, _ρ, κ)) ⇒
      if (i == 0) State(thn, _ρ, σ, κ)
      else State(els, _ρ, σ, κ)

    case State(App(e1, e2), ρ, σ, κ) ⇒
      State(e1, ρ, σ, KArg(e2, ρ, κ))
    case State(λ: Lam, ρ, σ, KArg(e, _ρ, κ)) ⇒
      State(e, _ρ, σ, KApp(λ, ρ, κ))
    case State(λ: Lam, ρ, σ, KApp(Lam(x, e), _ρ, κ)) ⇒
      val α = alloc(σ)
      State(e, _ρ + (x → α), σ + (α → CloV(λ, ρ)), κ)
    case State(NumV(i), ρ, σ, KApp(Lam(x, e), _ρ, κ)) ⇒
      val α = alloc(σ)
      State(e, _ρ + (x → α), σ + (α → NumV(i)), κ)

    case _ ⇒
      throw new RuntimeException(s"Invalid state: $s")
  }

  /* Evaluate to the final state */
  def drive(s: State): State = s match {
    case State(v, _, _, Halt) if isValue(v) ⇒ s
    case _ ⇒ drive(step(s))
  }

  /* Top-level evaluation function */
  def eval(e: Expr): State = drive(inject(e))
}

object RefuncBigStepCESK {
  /* A refunctionalized CPS interpreter. */
  type Cont = (Storable, Store) => Storable
  case class State(e: Expr, env: Env, store: Store, k: Cont)

  def interp(s: State): Storable = s match {
    case State(Var(x), env, store, k) => k(store(env(x)), store)
    case State(λ: Lam, env, store, k) => k(CloV(λ, env), store)
    case State(App(e1, e2), env, store, k) =>
      interp(State(e1, env, store, (e1v: Storable, e1store: Store) => {
        val e1clos = e1v.asInstanceOf[CloV]
        interp(State(e2, env, e1store, (e2v: Storable, e2store: Store) => {
          val addr = alloc(e2store)
          interp(State(e1clos.λ.body, e1clos.ρ+ (e1clos.λ.x -> addr), e2store + (addr -> e2v), k))
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
      interp(e1clos.λ.body, e1clos.ρ + (e1clos.λ.x -> addr), e2store + (addr -> e2v))
  }

  def eval(e: Expr): Storable = interp(e, Map(), Map())._1
}

object CESKTest {
  def main(args: Array[String]) {
    val e1 = App(Lam("id", App(App(Var("id"), Var("id")), App(Var("id"), Var("id")))), Lam("x", Var("x")))
    val big_e1 = BigStepCESK.eval(e1)
    val sml_e1 = SmallStepCESK.eval(e1)
    assert(big_e1 == sml_e1)

    println("-----------------------------------")

    val refunc_e1 = RefuncBigStepCESK.eval(e1)
  }
}
