package gensym.wasm.symbolic
import gensym.wasm.ast._

import z3.scala._

case class SymV(name: String) extends SymVal
case class SymBinary(op: BinOp, lhs: SymVal, rhs: SymVal) extends SymVal
case class SymUnary(op: UnaryOp, v: SymVal) extends SymVal
case class SymIte(cond: Cond, thn: SymVal, els: SymVal) extends SymVal
case class Concrete(v: Value) extends SymVal

// The following should be encoded to boolean in SMT
abstract class Cond extends SymVal {
  def negated: Cond = this match {
    case Not(cond)  => cond
    case _ => Not(this)
  }
}
case class CondEqz(v: SymVal) extends Cond
case class Not(cond: Cond) extends Cond
case class RelCond(op: RelOp, lhs: SymVal, rhs: SymVal) extends Cond

case class InitMem(min: Int, max: Option[Int]) extends SymVal
case class MemConcat(lhs: SymVal, rhs: SymVal) extends SymVal
case class MemExtract(mem: SymVal, offset: Int, size: Int) extends SymVal
abstract class SymVal {
  def toZ3AST(implicit ctx: Z3Context): Z3AST = this match {
    case SymV(name) => ctx.mkConst(ctx.mkStringSymbol(name), ctx.mkIntSort())
    case _          => ???
  }
}

// consider using zipper to simplify mutations
class ExploreTree(var node: Node = UnExplored(), val parent: Option[ExploreTree] = None) {
  def collectConds(): List[Cond] = {
    def collectCondsAux(tree: ExploreTree): List[Cond] = {
      tree.parent match {
        case Some(parent) => parent.node match {
          case IfElse(cond, thenNode, elseNode) =>
            if (this eq thenNode) {
              cond :: collectCondsAux(parent)
            } else if (this eq elseNode) {
              cond.negated :: collectCondsAux(parent)
            } else {
              throw new Exception("Internal Error: a tree is note pointed by its parent!")
            }
          case _ => throw new Exception(s"Internal Error: ${parent.node} is not a valid parent node!")
        }
        case None => Nil
      }
    }
    collectCondsAux(this)
  }

  def fillWithIfElse(cond: Cond): IfElse = {
    node match {
      case UnExplored() => {
        var newNode = IfElse(cond, new ExploreTree(parent = Some(this)), new ExploreTree(parent = Some(this)))
        node = newNode
        newNode
      }
      case node@IfElse(_, _, _) => node
      case _ => throw new Exception("Internal Error: Some exploration paths are not compatible!")
    }
  }

}

sealed abstract class Node {
  def cond: Option[Cond]
}

case class IfElse(
  _cond: Cond,
  thenNode: ExploreTree,
  elseNode: ExploreTree
) extends Node {
  // subnodes' parent should point to current tree

  def cond: Option[Cond] = Some(_cond)
}


case class UnExplored() extends Node {
  def cond = None
}

case class Finished() extends Node {
  def cond = None
}

