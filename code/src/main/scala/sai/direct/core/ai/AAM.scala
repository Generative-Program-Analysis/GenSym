package sai.direct.core.ai

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

  trait Addr
  case class IAddr(i: Int) extends Addr
  case class BAddr(x: Var, time: Time) extends Addr
  case class KAddr(callsite: Expr, time: Time) extends Addr

  type Env = Map[Var, Addr]

  trait AbsValue
  case class  CloV(λ: Lam, ρ: Env) extends AbsValue
  case class  NumV(i: Int) extends AbsValue with Control
  case object TopNumV extends AbsValue with Control
  //case class NumV(n: NumAbsDomain) extends AbsValue with Control //TODO

  trait Kont
  object Halt extends Kont
  case class KArg(e: Expr, ρ: Env, α: Addr) extends Kont
  case class KApp(λ: Lam, ρ: Env, α: Addr) extends Kont
  case class KLet(x: Var, e: Expr, ρ: Env, α: Addr) extends Kont
  case class KLrc(as: List[Addr], bds: List[Bind], e: Expr, ρ: Env, α: Addr) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, α: Addr) extends Kont
  case class KAOp(op: Symbol, vs: List[AbsValue], es: List[Expr], ρ: Env, α: Addr) extends Kont

  case class Store[K, V <% Lattice[V]](map: Map[K, V]) {
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

  type BStore = Store[Addr, ℙ[AbsValue]]
  type KStore = Store[Addr, ℙ[Kont]]

  def allocBind(x: Var, time: Time): BAddr = BAddr(x, time)
  def allocKont(tgtExpr: Expr, tgtEnv: Env, tgtStore: BStore, time: Time): KAddr =
    KAddr(tgtExpr, time)

  val k: Int = 0

  case class State(e: Control, ρ: Env, bσ: BStore, kσ: KStore, κ: Addr, τ: Time) {
    def tick: Time = (e :: τ) take k
  }

  /* TODO: pattern match on vs, check that they are NumV/TopNumV */
  def evalArith(op: Symbol, vs: List[AbsValue]): AbsValue = TopNumV

  def step(s: State): ℙ[State] = {
    val τ_* = s.tick
    s match {
      case State(Lit(i), ρ, bσ, kσ, κ, τ) ⇒
        Set(State(NumV(i), ρ, bσ, kσ, κ, τ_*))
      case State(Var(x), ρ, bσ, kσ, κ, τ) ⇒
        for (v ← bσ(ρ(x))) yield v match {
          case NumV(i) ⇒ State(NumV(i), ρ, bσ, kσ, κ, τ_*)
          case TopNumV ⇒ State(TopNumV, ρ, bσ, kσ, κ, τ_*)
          case CloV(λ, _ρ) ⇒ State(λ, _ρ, bσ, kσ, κ, τ_*)
        }
      case State(Let(x, e, body), ρ, bσ, kσ, κ, τ) ⇒
        ???
    }
  }

  val bs = Store[Addr, ℙ[AbsValue]](Map())
  val bs1 = bs.update(IAddr(1) → Set(CloV(Lam("a", Var("a")), Map())))

  val bs2 = bs.update(IAddr(1) → Set(TopNumV, NumV(3)))
  val bs3 = bs1 ⊔ bs2
}
