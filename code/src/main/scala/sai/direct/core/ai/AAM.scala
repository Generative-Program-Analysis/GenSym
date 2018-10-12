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
  type Time = List[Expr]

  trait Addr
  case class IAddr(i: Int) extends Addr
  case class BAddr(x: Var, time: Time) extends Addr
  case class KAddr(callsite: Expr, time: Time) extends Addr

  type Env = Map[Var, Addr]

  trait AbsVal
  case class Clos(λ: Lam, ρ: Env) extends AbsVal
  case object NumV extends AbsVal with Control
  //case class NumV(n: NumAbsDomain) extends AbsVal with Control //TODO

  trait Kont
  object Halt extends Kont
  case class KArg(e: Expr, ρ: Env, α: Addr) extends Kont
  case class KApp(λ: Lam, ρ: Env, α: Addr) extends Kont
  case class KLet(x: Var, e: Expr, ρ: Env, α: Addr) extends Kont
  case class KLrc(as: List[Addr], bds: List[Bind], e: Expr, ρ: Env, α: Addr) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, α: Addr) extends Kont
  case class KAOp(op: Symbol, vs: List[AbsVal], es: List[Expr], ρ: Env, α: Addr) extends Kont

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
  type BStore = Store[Addr, ℙ[AbsVal]]
  type KStore = Store[Addr, ℙ[Kont]]
  case class State(e: Control, ρ: Env, bσ: BStore, kσ: KStore, κ: Addr, τ: Time)

  val bs = Store[Addr, ℙ[AbsVal]](Map())
  val bs1 = bs.update(IAddr(1) → Set(Clos(Lam("a", Var("a")), Map())))

  val bs2 = bs.update(IAddr(1) → Set(NumV))
  val bs3 = bs1 ⊔ bs2

}
