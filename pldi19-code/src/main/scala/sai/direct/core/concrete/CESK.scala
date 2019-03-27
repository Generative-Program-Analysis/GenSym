package sai.direct.core.concrete

import sai.direct.core.parser._

/* Concrete small-step and big-step CESK machines */

object CESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Value]

  abstract class Value
  object NotAValue extends Value
  case class NumV(i: Int) extends Value with Expr
  case class CloV(λ: Lam, ρ: Env) extends Value

  abstract class Kont
  object Halt extends Kont
  case class KArg(e: Expr, ρ: Env, κ: Kont) extends Kont
  case class KApp(lam: Lam, ρ: Env, κ: Kont) extends Kont
  case class KLrc(as: List[Addr], bds: List[Bind], e: Expr, ρ: Env, κ: Kont) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, κ: Kont) extends Kont
  case class KAOp(op: Symbol, vs: List[NumV], es: List[Expr], ρ: Env, κ: Kont) extends Kont

  case class State(e: Expr, ρ: Env, σ: Store, κ: Kont)

  def alloc(σ: Store): Addr = σ.keys.size + 1

  def isValue(e: Expr): Boolean = e.isInstanceOf[NumV] | e.isInstanceOf[Lam]

  /* evalArith assumes that arguments are provided from right to left. */
  def evalArith(op: Symbol, vs: List[NumV]): NumV = op match {
    case '+ ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j+i) }
    case '- ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j-i) }
    case '* ⇒ vs.reduceRight[NumV] { case (NumV(i), NumV(j)) ⇒ NumV(j*i) }
  }
}

import CESK._

object SmallStepCESK {
  /* A small-step CESK state machine */
  def step(s: State): State = s match {
    case State(Lit(i), ρ, σ, κ) ⇒
      State(NumV(i), ρ, σ, κ)
    case State(Var(x), ρ, σ, κ) ⇒ σ(ρ(x)) match {
      case CloV(λ, _ρ) ⇒ State(λ, _ρ, σ, κ)
      case NumV(i) ⇒ State(NumV(i), ρ, σ, κ)
      case NotAValue => throw new RuntimeException(s"Variable not defined: $x")
    }

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
      State(evalArith(op, NumV(i)::vs), _ρ, σ, κ)
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

  def inject(e: Expr): State = State(e, Map(), Map(), Halt)

  /* Top-level evaluation function */
  def eval(e: Expr): State = drive(inject(e))
}

object RefuncBigStepCESK {
  /* A refunctionalized CPS interpreter. */
  type Cont = (Value, Store) ⇒ (Value, Store)

  def interp(e: Expr, ρ: Env, σ: Store, κ: Cont): (Value, Store) = e match {
    case Lit(i) ⇒ κ(NumV(i), σ)
    case Var(x) ⇒ κ(σ(ρ(x)), σ)
    case Lam(x, body) ⇒ κ(CloV(Lam(x, body), ρ), σ)
    case Lrc(bds, body) ⇒
      val Bind(x, e) = bds.head
      val (_ρ, _σ, αs) = bds.foldRight (ρ, σ, List[Addr]()) { case (Bind(x, _), (ρ_*, σ_*, αs_*)) ⇒
        val α = alloc(σ_*)
        (ρ_* + (x → α), σ_* + (α → NotAValue), α::αs_*)
      }
      def rec(bds: List[Bind], αs: List[Addr])(v: Value, σ: Store): (Value, Store) =
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

  def eval(e: Expr): (Value, Store) = interp(e, Map(), Map(), (v, σ) ⇒ (v, σ))
}

object BigStepCES {
  /* A big-step interpreter written in direct-style. */
  def interp(e: Expr, ρ: Env, σ: Store): (Value, Store) = e match {
    case Lit(i) ⇒ (NumV(i), σ)
    case Var(x) ⇒ (σ(ρ(x)), σ)
    case Lam(x, body) ⇒ (CloV(Lam(x, body), ρ), σ)
    case Lrc(bds, body) ⇒
      val (_ρ, _σ, αs) = bds.foldRight (ρ, σ, List[Addr]()) { case (Bind(x, _), (ρ_*, σ_*, αs_*)) ⇒
        val α = alloc(σ_*)
        (ρ_* + (x → α), σ_* + (α → NotAValue), α::αs_*)
      }
      val σ_* = (bds zip αs).foldLeft (_σ) { case (σ, (Bind(x, e), α)) ⇒
        val (ev, es) = interp(e, _ρ, σ)
        es + (α → ev)
      }
      interp(body, _ρ, σ_*)
    case AOp(op, e1, e2) ⇒
      val (e1v, e1s) = interp(e1, ρ, σ)
      e1v match {
        case e1v: NumV ⇒
          val (e2v, e2s) = interp(e2, ρ, e1s)
          e2v match {
            case e2v: NumV ⇒ (evalArith(op, e2v::e1v::Nil), e2s)
            case _ ⇒ throw new RuntimeException("Left-hand side is not a number")
          }
        case _ ⇒ throw new RuntimeException("Left-hand side is not a number")
      }
    case If0(cnd, thn, els) ⇒
      val (cndv, cnds) = interp(cnd, ρ, σ)
      cndv match {
        case NumV(i) ⇒
          if (i == 0) interp(thn, ρ, cnds)
          else interp(els, ρ, cnds)
        case _ ⇒ throw new RuntimeException("Condition is not a number")
      }
    case App(e1, e2) ⇒
      val (e1v, e1s) = interp(e1, ρ, σ)
      val (e2v, e2s) = interp(e2, ρ, e1s)
      val α = alloc(e2s)
      e1v match {
        case CloV(Lam(x, body), _ρ) ⇒ interp(body, _ρ + (x → α), e2s + (α → e2v))
        case _ ⇒ throw new RuntimeException("Not a function")
      }
    case _ ⇒ throw new RuntimeException("Not a valid program")
  }

  def eval(e: Expr): (Value, Store) = interp(e, Map(), Map())
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

    assert(RefuncBigStepCESK.eval(fact10)._2 == SmallStepCESK.eval(fact10).σ)

    assert(BigStepCES.eval(fact5) == RefuncBigStepCESK.eval(fact5))
    assert(BigStepCES.eval(fact10) == RefuncBigStepCESK.eval(fact10))

    //TODO: Add test of mutual recursive function
  }
}
