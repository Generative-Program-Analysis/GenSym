package sai.lang

object SIR {
  sealed trait Atom
  case class Lit(i: Int) extends Atom
  case class Var(x: String) extends Atom

  sealed trait ValInst
  case class Op2(op: String, v1: Atom, v2: Atom) extends ValInst

  sealed trait Inst
  case class Assign(x: Var, v: ValInst) extends Inst
  case class CondBr(c: Atom, l1: Label, l2: Label) extends Inst
  case class Jmp(l: Label) extends Inst
  case class Return(v: Atom) extends Inst

  case class Block(l: Label, il: List[Inst])
  type Label = String
  type Prog = List[Block]
}