package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions

import sai.common.ai._
import sai.common.ai.Lattice._
import sai.direct.core.parser._

/* Small-step AAM */

object AAM {
  val ℙ = Set
  type ℙ[T] = Set[T]

  type Var = String
  type Time = List[Control]

  trait BAddr
  case class IAddr(i: Int) extends BAddr
  case class CtxBAddr(x: Var, time: Time) extends BAddr

  trait KAddr
  case class CtxKAddr(callsite: Expr, time: Time) extends KAddr
  case class P4FKAddr(callsite: Expr, tgtEnv: Env) extends KAddr
  case object HaltAddr extends KAddr

  type Env = Map[Var, BAddr]

  trait AbsValue
  case class  CloV(λ: Lam, ρ: Env) extends AbsValue
  case class  NumV(i: Int) extends AbsValue with Control
  case object NumVTop extends AbsValue with Control
  //case class NumV(n: NumAbsDomain) extends AbsValue with Control //TODO

  trait Kont
  case class KArg(e: Expr, ρ: Env, κ: KAddr) extends Kont
  case class KApp(λ: Lam, ρ: Env, κ: KAddr) extends Kont
  case class KLrc(as: List[BAddr], bds: List[Bind], e: Expr, ρ: Env, κ: KAddr) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, κ: KAddr) extends Kont
  case class KAOp(op: Symbol, vs: List[AbsValue], es: List[Expr], ρ: Env, κ: KAddr) extends Kont

  case class Store[K, V: Lattice](map: Map[K, V]) {
    def apply(k: K): V = map(k)
    def getOrElse(k: K, dft: V): V = map.getOrElse(k, dft)
    def update(k: K, d: V): Store[K, V] = {
      val oldd = map.getOrElse(k, d.bot)
      Store[K, V](map + (k → (oldd ⊔ d)))
    }
    def update(kv: (K, V)): Store[K, V] = update(kv._1, kv._2)
    def contains(k: K): Boolean = map.contains(k)
    def +(kv: (K, V)): Store[K, V] = update(kv._1, kv._2)
    def ⊔(that: Store[K, V]): Store[K, V] = Store[K, V](this.map ⊔ that.map)
  }

  type BStore = Store[BAddr, ℙ[AbsValue]]
  type KStore = Store[KAddr, ℙ[Kont]]

  def allocBind(x: Var, time: Time): BAddr = CtxBAddr(x, time)
  def allocKont(tgtExpr: Expr, tgtEnv: Env, time: Time): KAddr = CtxKAddr(tgtExpr, time)

  val k: Int = 0

  case class State(e: Control, ρ: Env, bσ: BStore, kσ: KStore, κ: KAddr, τ: Time) {
    def tick: Time = (e :: τ) take k
  }

  /* TODO: pattern match on vs, check that they are NumV/NumVTop */
  def evalArith(op: Symbol, vs: List[AbsValue]): Control = NumVTop

  def isNum(n: Control): Boolean = n.isInstanceOf[NumV] || n == NumVTop

  def eq0(n: AbsValue): Boolean = n match {
    case NumV(0) ⇒ true
    case _ ⇒ false
  }

  def ρ0 = Map[Var, BAddr]()
  def bσ0 = Store[BAddr, ℙ[AbsValue]](Map())
  def τ0 = List[Control]()
}

object SSAAM {
  import AAM._
  def step(s: State): ℙ[State] = {
    println(s)
    val τ_* = s.tick
    s match {
      case State(Lit(i), ρ, bσ, kσ, κ, τ) ⇒
        ℙ(State(NumV(i), ρ, bσ, kσ, κ, τ_*))
      case State(Var(x), ρ, bσ, kσ, κ, τ) ⇒
        for (v ← bσ(ρ(x))) yield v match {
          case NumV(i)     ⇒ State(NumV(i), ρ, bσ, kσ, κ, τ_*)
          case NumVTop     ⇒ State(NumVTop, ρ, bσ, kσ, κ, τ_*)
          case CloV(λ, _ρ) ⇒ State(λ, _ρ, bσ, kσ, κ, τ_*)
        }

      case State(n, ρ, bσ, kσ, κ, τ) if isNum(n) ⇒
        val n_* = n.asInstanceOf[AbsValue]
        (for (cont ← kσ(κ)) yield cont match {
          case KArg(ar, ρ_*, κ_*) ⇒ throw new RuntimeException("Expected a function, not a number")
          case KApp(Lam(x, e), ρ_*, κ_*) ⇒
            val α = allocBind(x, τ_*)
            val ρ_** = ρ_* + (x → α)
            val bσ_* = bσ + (α → ℙ(n_*))
            ℙ(State(e, ρ_**, bσ_*, kσ, κ_*, τ_*))
          case KLrc(α::αs, bds, body, ρ_*, κ_*) ⇒
            bds match {
              case Nil ⇒ ℙ(State(body, ρ_*, bσ + (α → ℙ(n_*)), kσ, κ_*, τ_*))
              case Bind(x, e)::bds ⇒
                val κ_** = allocKont(e, ρ_*, τ_*)
                val kσ_* = kσ + (κ_** → ℙ(KLrc(αs, bds, body, ρ_*, κ_*)))
                ℙ(State(e, ρ_*, bσ + (α → ℙ(n_*)), kσ_*, κ_**, τ_*))
            }
          case KIf0(thn, els, ρ_*, κ_*) ⇒
            if (eq0(n_*)) ℙ(State(thn, ρ_*, bσ, kσ, κ_*, τ_*))
            else ℙ(State(thn, ρ_*, bσ, kσ, κ_*, τ_*), State(els, ρ_*, bσ, kσ, κ_*, τ_*))
          case KAOp(op, vs, e::es, ρ_*, κ_*) ⇒
            val κ_** = allocKont(e, ρ_*, τ_*)
            val kσ_* = kσ + (κ_** → ℙ(KAOp(op, n_* :: vs, es, ρ_*, κ_*)))
            ℙ(State(e, ρ_*, bσ, kσ_*, κ_**, τ_*))
          case KAOp(op, vs, Nil, ρ_*, κ_*) ⇒
            ℙ(State(evalArith(op, n_* :: vs), ρ_*, bσ, kσ, κ_*, τ_*))
        }).flatten
      case State(λ: Lam, ρ, bσ, kσ, κ, τ) ⇒
        for (cont ← kσ(κ)) yield cont match {
          case KArg(ar, ρ_*, κ_*) ⇒
            val κ_** = allocKont(ar, ρ_*, τ_*)
            val kσ_* = kσ + (κ_** → ℙ(KApp(λ, ρ, κ_*)))
            State(ar, ρ_*, bσ, kσ_*, κ_**, τ_*)
          case KApp(Lam(x, e), ρ_*, κ_*) ⇒
            val α = allocBind(x, τ_*)
            val ρ_** = ρ_* + (x → α)
            val bσ_* = bσ + (α → ℙ(CloV(λ, ρ)))
            State(e, ρ_**, bσ_*, kσ, κ_*, τ_*)
          case KLrc(α::αs, bds, body, ρ_*, κ_*) ⇒
            bds match {
              case Nil ⇒ State(body, ρ_*, bσ + (α → ℙ(CloV(λ, ρ))), kσ, κ_*, τ_*)
              case Bind(x, e)::bds ⇒
                val κ_** = allocKont(e, ρ_*, τ_*)
                val kσ_* = kσ + (κ_** → ℙ(KLrc(αs, bds, body, ρ_*, κ_*)))
                State(e, ρ_*, bσ + (α → ℙ(CloV(λ, ρ))), kσ_*, κ_**, τ_*)
            }
          case KIf0(thn, els, ρ_*, κ_*) ⇒ throw new RuntimeException("Expected a number, not a function")
          case KAOp(op, vs, es, ρ_*, κ_*) ⇒ throw new RuntimeException("Expected a number, not a function")
        }
      case State(Lrc(bds, body), ρ, bσ, kσ, κ, τ) ⇒
        val (ρ_*, bσ_*, αs) = bds.foldRight (ρ, bσ, List[BAddr]()) { case (Bind(x, _), (ρ_, bσ_, αs_)) ⇒
          val α = allocBind(x, τ_*)
          (ρ_ + (x → α), bσ_ + (α → ℙ()), α::αs_)
        }
        val Bind(_, e) = bds.head
        val κ_* = allocKont(e, ρ_*, τ_*)
        val kσ_* = kσ + (κ_* → ℙ(KLrc(αs, bds.tail, body, ρ_*, κ)))
        ℙ(State(e, ρ_*, bσ_*, kσ_*, κ_*, τ_*))
      case State(If0(cnd, thn, els), ρ, bσ, kσ, κ, τ) ⇒
        val κ_* = allocKont(cnd, ρ, τ_*)
        val kσ_* = kσ + (κ_* → ℙ(KIf0(thn, els, ρ, κ)))
        ℙ(State(cnd, ρ, bσ, kσ_*, κ_*, τ_*))
      case State(AOp(op, e1, e2), ρ, bσ, kσ, κ, τ) ⇒
        val κ_* = allocKont(e1, ρ, τ_*)
        val kσ_* = kσ + (κ_* → ℙ(KAOp(op, List(), List(e2), ρ, κ)))
        ℙ(State(e1, ρ, bσ, kσ_*, κ_*, τ_*))
      case State(App(e1, e2), ρ, bσ, kσ, κ, τ) ⇒
        val κ_* = allocKont(e1, ρ, τ_*)
        val kσ_* = kσ + (κ_* → ℙ(KArg(e2, ρ, κ)))
        ℙ(State(e1, ρ, bσ, kσ_*, κ_*, τ_*))
    }
  }

  def drive(todo: List[State], seen: Set[State]): Set[State] = todo match {
    case Nil ⇒ seen
    case hd::tl if seen.contains(hd) ⇒ drive(tl, seen)
    case hd::tl ⇒ drive(step(hd).toList ++ tl, seen + hd)
  }

  def κ0 = HaltAddr
  def kσ0 = Store[KAddr, ℙ[Kont]](Map()) + (κ0 → ℙ())
  def inject(e: Expr): State = State(e, ρ0, bσ0, kσ0, κ0, τ0)

  def analyze(e: Expr): Set[State] = drive(List(inject(e)), Set())
}

object PDAAM {
  //TODO
}

