package sai

object SimpIL {
  case class Prog(stmts: List[Stmt])

  sealed trait Stmt
  case class Assign(x: String, e: Exp) extends Stmt
  case class Store(e1: Exp, e2: Exp) extends Stmt
  case class Goto(e: Exp) extends Stmt
  case class Assert(e: Exp) extends Stmt
  case class Cond(cnd: Exp, then_tgt: Exp, else_tgt: Exp) extends Stmt
  case class Halt() extends Stmt

  sealed trait Exp
  case class Lit(x: Int) extends Exp
  case class Var(x: String) extends Exp
  case class Load(e: Exp) extends Exp
  case class BinOp(op: String, e1: Exp, e2: Exp) extends Exp
  case class UnaryOp(op: String, e: Exp) extends Exp
  case class GetInput(src: String) extends Exp

  object Values {
    trait Value
    case class IntV(x: Int) extends Value
  }

  object Examples {
    val ex1 = Prog(List(Assign("x", BinOp("*", Lit(2), GetInput("stdin"))),
                        Halt()))
  }
}
