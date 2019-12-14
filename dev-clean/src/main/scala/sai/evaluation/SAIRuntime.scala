package sai.evaluation

import sai.evaluation.parser._

object SAIRuntime {
  trait AbsValue
  case object IntV extends AbsValue
  case object SymV extends AbsValue
  case object BoolV extends AbsValue
  case object VoidV extends AbsValue
  case object FloatV extends AbsValue
  case object CharV extends AbsValue
  case object EofV extends AbsValue
  case object InputPortV extends AbsValue
  case object OutputPortV extends AbsValue
  case object MtListV extends AbsValue
  case object VectorVTop extends AbsValue
  case object ListVTop extends AbsValue
  case class PrimOpV(op: String) extends AbsValue
  case class ConsV(a: Addr, b: Addr) extends AbsValue
  case class VectorV(vs: List[Addr]) extends AbsValue
  case class CompiledClo(f: (List[Value], Store, Cache, Cache) => (Set[(Value, Store)], Cache), λ: Lam, ρ: Env) extends AbsValue

  trait Addr
  case class ZCFAAddr(x: String) extends Addr
  case class KCFAAddr(x: String, ctx: Expr) extends Addr

  type Value = Set[AbsValue]
  type Env = Map[String, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]
}

object SAIRuntimeOpt {
  trait AbsValue
  case object IntV extends AbsValue
  case object SymV extends AbsValue
  case object BoolV extends AbsValue
  case object VoidV extends AbsValue
  case object FloatV extends AbsValue
  case object CharV extends AbsValue
  case object EofV extends AbsValue
  case object InputPortV extends AbsValue
  case object OutputPortV extends AbsValue
  case object MtListV extends AbsValue
  case object VectorVTop extends AbsValue
  case object ListVTop extends AbsValue
  case class PrimOpV(op: String) extends AbsValue
  case class ConsV(a: Addr, b: Addr) extends AbsValue
  case class VectorV(vs: List[Addr]) extends AbsValue
  case class CompiledClo(f: (List[Value], Store, Cache, Cache) => (Set[(Value, Store)], Cache), λ: Int, ρ: Env) extends AbsValue

  trait Addr
  case class ZCFAAddr(x: String) extends Addr
  case class KCFAAddr(x: String, ctx: Expr) extends Addr

  type Value = Set[AbsValue]
  type Env = Map[String, Addr]
  type Store = Map[Addr, Value]
  type Config = (Int, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]
}

object SWSAIRuntime {
  trait AbsValue
  case object IntV extends AbsValue
  case object SymV extends AbsValue
  case object BoolV extends AbsValue
  case object VoidV extends AbsValue
  case object FloatV extends AbsValue
  case object CharV extends AbsValue
  case object EofV extends AbsValue
  case object InputPortV extends AbsValue
  case object OutputPortV extends AbsValue
  case object MtListV extends AbsValue
  case object VectorVTop extends AbsValue
  case object ListVTop extends AbsValue
  case class PrimOpV(op: String) extends AbsValue
  case class ConsV(a: Addr, b: Addr) extends AbsValue
  case class VectorV(vs: List[Addr]) extends AbsValue
  case class CompiledClo(f: (List[Value], Store, Cache, Cache) => ((Set[Value], Store), Cache), λ: Int, ρ: Env) extends AbsValue

  trait Addr
  case class ZCFAAddr(x: String) extends Addr
  case class KCFAAddr(x: String, ctx: Expr) extends Addr

  type Value = Set[AbsValue]
  type Env = Map[String, Addr] 
  //type Env = Int 
  type Store = Map[Addr, Value]
  type Config = (Int, Env)
  type Cache = Map[Config, Set[Value]]
}
