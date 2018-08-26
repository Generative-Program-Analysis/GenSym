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

  /* evalArith assumes that arguments are provided from right to left. */
  def evalArith(op: Symbol, vs: List[NumV]): NumV = op match {
    case '+ ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j+i) }
    case '- ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j-i) }
    case '* ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j*i) }
  }
}

import CESK._

object SmallStepCESK {
  /* Single step function */
  def step(s: State): State = s match {
    case State(Lit(i), ρ, σ, κ) ⇒
      State(NumV(i), ρ, σ, κ)
    case State(Var(x), ρ, σ, κ) ⇒ σ(ρ(x)) match {
      case CloV(λ, _ρ) ⇒ State(λ, _ρ, σ, κ)
      case NumV(i) ⇒ State(NumV(i), ρ, σ, κ)
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
  type Cont = (Storable, Store) ⇒ (Storable, Store)
  case class State(e: Expr, env: Env, store: Store, k: Cont)

  def interp(e: Expr, ρ: Env, σ: Store, κ: Cont): (Storable, Store) = e match {
    case Lit(i) ⇒ κ(NumV(i), σ)
    case Var(x) ⇒ κ(σ(ρ(x)), σ)
    case Lam(x, body) ⇒ κ(CloV(Lam(x, body), ρ), σ)
    case Let(x, rhs, body) ⇒
      interp(rhs, ρ, σ, (rhsv, rhss) ⇒ {
               val α = alloc(rhss)
               interp(body, ρ + (x → α), rhss + (α → rhsv), κ)
             })
    case Lrc(bds, body) ⇒
      val Bind(x, e) = bds.head
      val (_ρ, _σ, αs) = bds.foldRight (ρ, σ, List[Addr]()) { case (Bind(x, _), (ρ_*, σ_*, αs_*)) ⇒
        val α = alloc(σ_*)
        (ρ_* + (x → α), σ_* + (α → NotAValue), α::αs_*)
      }
      def rec(bds: List[Bind], αs: List[Addr])(v: Storable, σ: Store): (Storable, Store) =
        (bds, αs) match {
          case (Nil, α::Nil) ⇒ interp(body, _ρ, σ + (α → v), κ)
          case (Bind(x, e)::bds, α::αs) ⇒
            interp(e, _ρ, σ + (α → v), rec(bds, αs))
        }
      interp(e, _ρ, _σ, rec(bds.tail, αs))
    case AOp(op, e1, e2) ⇒
      // TODO: multiple oprands
      interp(e1, ρ, σ, (e1v, e1s) ⇒ e1v match {
               case e1v: NumV ⇒
                 interp(e2, ρ, e1s, (e2v, e2s) ⇒ e2v match {
                          case e2v: NumV ⇒ κ(evalArith(op, e2v::e1v::Nil), e2s)
                          case _ ⇒ throw new RuntimeException("Right-hand side is not a number")
                        })
               case _ ⇒ throw new RuntimeException("Left-hand side is not a number")
             })
    case If0(cnd, thn, els) ⇒
      interp(cnd, ρ, σ, (cndv, cnds) ⇒ cndv match{
               case NumV(i) ⇒
                 if (i == 0) interp(thn, ρ, cnds, κ)
                 else interp(els, ρ, cnds, κ)
               case _ ⇒ throw new RuntimeException("Condition is not a number")
             })
    case App(e1, e2) ⇒
      interp(e1, ρ, σ, (e1v, e1s) ⇒ e1v match {
               case CloV(Lam(x, body), _ρ) ⇒
                 interp(e2, ρ, e1s, (e2v, e2s) ⇒ {
                          val α = alloc(e2s)
                          interp(body, _ρ + (x → α), e2s + (α → e2v), κ)
                        })
               case _ ⇒ throw new RuntimeException("Not a function")
             })
    case _ ⇒ throw new RuntimeException("Not a valid program")
  }

  def eval(e: Expr): (Storable, Store) = interp(e, Map(), Map(), (v, σ) ⇒ (v, σ))
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
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Lrc(List(Bind("fact", fact)),
                    App(Var("fact"), Lit(5)))
    val fact10 = Lrc(List(Bind("fact", fact)),
                     App(Var("fact"), Lit(10)))
    assert(SmallStepCESK.eval(fact5).e == NumV(120))
    assert(SmallStepCESK.eval(fact10).e == NumV(3628800))

    //val err1 = Lrc(List(Bind("x", Var("x"))), Var("x"))
    //SmallStepCESK.eval(err1)

    assert(RefuncBigStepCESK.eval(fact5)._1 == NumV(120))
    assert(RefuncBigStepCESK.eval(fact10)._1 == NumV(3628800))

  }
}
