package sai.direct.core.ai

import scala.language.implicitConversions

import sai.common.ai._
import sai.common.ai.Lattice._
import sai.direct.core.parser._

// Small-step AAM

object AAM {
  type Var = String
  type Time = List[Expr]

  trait Addr
  case class BAddr(x: Var, time: Time) extends Addr
  case class KAddr(callsite: Expr, time: Time) extends Addr

  type Env = Map[Var, Addr]

  trait Storable
  case class CloV(λ: Lam, ρ: Env) extends Storable
  // TODO: Abstract Numerical Values

  trait Kont
  object Halt extends Kont
  case class KArg(e: Expr, ρ: Env, α: Addr) extends Kont
  case class KApp(λ: Lam, ρ: Env, α: Addr) extends Kont
  case class KLet(x: Var, e: Expr, ρ: Env, α: Addr) extends Kont
  case class KLrc(as: List[Addr], bds: List[Bind], e: Expr, ρ: Env, α: Addr) extends Kont
  case class KIf0(thn: Expr, els: Expr, ρ: Env, α: Addr) extends Kont
  //case class KAOp(op: Symbol, vs: List[NumV], es: List[Expr], ρ: Env, α: Addr) extends Kont

  case class Store[K, V <% Lattice[V]](map: Map[K, V]) {
    def apply(k: K): V = map(k)
    def contains(k: K): Boolean = map.contains(k)
    def getOrElse(k: K, dft: V): V = map.getOrElse(k, dft)
    def update(k: K, d: V): Store[K, V] = {
      val oldd = map.getOrElse(k, d.bot)
      Store[K, V](map + (k → (oldd ⊔ d)))
    }
    def join(that: Store[K, V]): Store[K, V] = Store[K, V](this.map ⊔ that.map)
  }
  type BStore = Store[Addr, Set[Storable]]
  type KStore = Store[Addr, Set[Kont]]

}
