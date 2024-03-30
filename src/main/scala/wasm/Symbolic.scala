package gensym.wasm.symbolic
import gensym.wasm.ast._

import z3.scala._

case class SymV(name: String) extends SymVal
case class SymBinary(op: BinOp, lhs: SymVal, rhs: SymVal) extends SymVal
case class SymUnary(op: UnaryOp, v: SymVal) extends SymVal
case class SymIte(cond: Cond, thn: SymVal, els: SymVal) extends SymVal
case class Concrete(v: Value) extends SymVal

abstract class Cond extends SymVal
case class CondEqz(v: SymVal) extends Cond
case class Not(cond: Cond) extends Cond
case class RelCond(op: RelOp, lhs: SymVal, rhs: SymVal) extends Cond

case class InitMem(min: Int, max: Option[Int]) extends SymVal
case class MemConcat(lhs: SymVal, rhs: SymVal) extends SymVal
case class MemExtract(mem: SymVal, offset: Int, size: Int) extends SymVal
abstract class SymVal {
  def toZ3AST(implicit ctx: Z3Context): Z3AST = this match {
    case SymV(name) => ctx.mkConst(ctx.mkStringSymbol(name), ctx.mkIntSort())
    case _ => ???
  }
}
