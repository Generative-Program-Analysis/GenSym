package tutorial

import lms.core._
import lms.macros._
import lms.core.stub._
import lms.core.Backend._

import gensym.lmsx._

import scala.collection.immutable.{List => SList}

object ImpLang {
  sealed trait Stmt
  case class Skip() extends Stmt
  case class Break() extends Stmt
  case class Assign(x: String, e: Expr) extends Stmt
  case class Cond(e: Expr, thn: Stmt, els: Stmt) extends Stmt
  case class Seq(s1: Stmt, s2: Stmt) extends Stmt
  case class While(b: Expr, s: Stmt) extends Stmt
  case class Output(e: Expr) extends Stmt
  case class Assert(e: Expr) extends Stmt

  sealed trait Expr {
    def toSExp: String
  }
  case class Input() extends Expr {
    def toSExp = ???
  }
  case class Lit(x: Any) extends Expr {
    override def toString: String = s"Lit(${x.toString})"
    def toSExp: String = x.toString
  }
  case class Var(x: String) extends Expr {
    override def toString: String = "Var(\"" + x.toString + "\")"
    def toSExp: String = x.toString
  }
  case class Op1(op: String, e: Expr) extends Expr {
    override def toString: String = "Op1(\"" + op + "\"," + s"${e.toString})"
    def toSExp: String = s"($op ${e.toSExp})"
  }
  case class Op2(op: String, e1: Expr, e2: Expr) extends Expr {
    override def toString: String =
      "Op2(\"" + op + "\"," + s"${e1.toString}, ${e2.toString})"
    def toSExp: String = s"($op ${e1.toSExp} ${e2.toSExp})"
  }

  def let_(x: String, rhs: Int)(body: Var => Stmt): Stmt =
    Seq(Assign(x, Lit(rhs)), body(Var(x)))
  def let_(x: String, rhs: Expr)(body: Var => Stmt): Stmt =
    Seq(Assign(x, rhs), body(Var(x)))

  def set_(x: String, rhs: Expr): Stmt = Assign(x, rhs)

  def while_(e: Expr, s: Stmt): Stmt = While(e, s)

  object Examples {
    val fact5 =
      Seq(Assign("i", Lit(1)),
          Seq(Assign("fact", Lit(1)),
              While(Op2("<=", Var("i"), Lit(5)),
                    Seq(Assign("fact", Op2("*", Var("fact"), Var("i"))),
                      Assign("i", Op2("+", Var("i"), Lit(1)))))))

    val fact_n =
      Seq(Assign("i", Lit(1)),
          Seq(Assign("fact", Lit(1)),
              While(Op2("<=", Var("i"), Var("n")),
                    Seq(Assign("fact", Op2("*", Var("fact"), Var("i"))),
                      Assign("i", Op2("+", Var("i"), Lit(1)))))))

    val w2 =
      While(Op2("<=", Var("i"), Var("x")),
        While(Op2("<=", Var("i"), Var("x")),
          Assign("x", Op2("-", Var("x"), Lit(1)))))

    val w3 =
      While(Op2("<=", Var("i"), Var("x")),
        While(Op2("<=", Var("i"), Var("x")),
          While(Op2("<=", Var("i"), Var("x")),
            Assign("x", Op2("-", Var("x"), Lit(1))))))

    val another_fact5 =
      let_("i", 1){ i =>
        let_("fact", 1){ fact =>
          while_(Op2("<=", i, Lit(5)),
            let_("fact", Op2("*", fact, i)){ _ =>
              set_("i", Op2("+", i, Lit(1)))
            })}}


    //println(another_fact5)
    assert(fact5 == another_fact5)

    val x = Var("x")
    val y = Var("y")
    val z = Var("z")
    val a = Var("a")
    val b = Var("b")
    val i = Var("i")

    val cond1 =
      Cond(Op2("<=", Lit(1), Lit(2)),
        Assign("x", Lit(3)),
        Assign("x", Lit(4)))

    /* if (x <= y) {
     *   z = x
     * } else {
     *   z = y
     * }
     * z = z + 1
     */
    val cond2 =
      Seq(Cond(Op2("<=", Var("x"), Var("y")),
               Assign("z", Var("x")),
               Assign("z", Var("y"))),
          Assign("z", Op2("+", Var("z"), Lit(1))))

    /* if (x <= y) {
     *   z = x
     * } else {
     *   z = y
     * }
     * z = z - 1
     * if (z >= y) {
     *   z = z * 2
     * } else {
     *   z = z + 3
     * }
     */
    val cond3 =
      Seq(Cond(Op2("<=", x, y),
               Assign("z", x),
               Assign("z", y)),
        Seq(Assign("z", Op2("-", z, Lit(1))),
          Seq(Cond(Op2(">=", z, y),
            Assign("z", Op2("*", z, Lit(2))),
            Assign("z", Op2("+", z, Lit(3)))),
          Skip())))

    val condInput =
      Seq(Assign("x", Input()),
      Seq(Cond(Op2("<=", x, y),
               Assign("z", x),
               Assign("z", y)),
        Seq(Assign("z", Op2("+", z, Lit(1))),
          Seq(Cond(Op2(">=", z, y),
            Assign("z", Op2("+", z, Lit(2))),
            Assign("z", Op2("+", z, Lit(3)))),
          Skip()))))

    val condAssert =
      Seq(Assign("x", Input()),
      Seq(Assert(Op2(">=", x, Lit(1))),
      Seq(Cond(Op2("<=", x, y),
               Assign("z", x),
               Assign("z", y)),
        Seq(Assign("z", Op2("+", z, Lit(1))),
          Seq(Cond(Op2(">=", z, y),
            Assign("z", Op2("+", z, Lit(2))),
            Assign("z", Op2("+", z, Lit(3)))),
          Skip())))))

    val unboundLoop =
      Seq(Assign("i", Input()),
        While(Op2("<", i, Lit(42)),
          Assign("i", Op2("+", i, Lit(1)))))
  }
}

import ImpLang._

@virtualize
trait ImpureStagedImpSemantics extends SAIOps {
  trait Value
  def IntV(i: Rep[Int]): Rep[Value] = "IntV".reflectWith[Value](i)
  def BoolV(b: Rep[Boolean]): Rep[Value] = "BoolV".reflectWith[Value](b)

  implicit def repIntProj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    //case Adapter.g.Def("IntV", SList(v: Backend.Exp)) => Wrap[Int](v)
    case _ => "IntV-proj".reflectWith[Int](i)
  }
  implicit def repBoolProj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", SList(v: Backend.Exp)) => Wrap[Boolean](v)
    case _ => "BoolV-proj".reflectWith[Boolean](b)
  }

  trait MutState
  def newMutState(kvs: (String, Rep[Value])*): Rep[MutState] =
    "mutstate-new".reflectMutableWith[MutState](kvs.map({ case (k, v) => __liftTuple2RepLhs(k, v) }):_*)
  implicit class MutStateOps(s: Rep[MutState]) {
    def apply(x: String): Rep[Value] = "mutstate-read".reflectReadWith[Value](s, x)(s)
    def +=(x: String, v: Rep[Value]): Rep[Unit] = "mutstate-update".reflectWriteWith[Unit](s, x, v)(s)
  }
  def dummyRead(s: Rep[MutState]): Rep[Unit] = "mutstate-dummyread".reflectRWWith[Unit]()(s)(Adapter.CTRL)

  def eval(e: Expr, σ: Rep[MutState]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val i: Rep[Int] = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val i1: Rep[Int] = eval(e1, σ)
      val i2: Rep[Int] = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def exec(s: Stmt, σ: Rep[MutState]): Rep[Unit] = s match {
    case Skip() => ()
    case Assign(x, e) => σ += (x, eval(e, σ))
    case Cond(e, s1, s2) =>
      if (eval(e, σ)) exec(s1, σ) else exec(s2, σ)
    case Seq(s1, s2) => exec(s1, σ); exec(s2, σ)
    case While(e, b) => while (eval(e, σ)) exec(b, σ)
  }
}

trait ImpureStagedImpGen extends SAICodeGenBase {
  override def traverse(n: Node): Unit = n match {
    case Node(s, "mutstate-new", kvs, _) =>
      es"val ${quote(s)} = Map[String, Value]("
      kvs.zipWithIndex.map { case (kv, i) =>
        shallow(kv)
        if (i != kvs.length-1) emit(", ")
      }
      esln")"
    case Node(_, "mutstate-update", List(s, x, v), _) => esln"$s($x) = $v"
    case Node(_, "mutstate-dummyread", _, _) => es""
    case _ => super.traverse(n)
  }
  // shallow : code generation for pure node/expression
  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) => es"IntV($i)"
    case Node(s, "BoolV", List(b), _) => es"BoolV($b)"
    case Node(s, "IntV-proj", List(i), _) => es"$i.I"
    case Node(s, "BoolV-proj", List(i), _) => es"$i.B"
    case Node(_, "mutstate-read", List(s, x), _) => es"$s($x)"
    case _ => super.shallow(n)
  }
}

trait ImpureStagedImpDriver[A, B] extends SAIDriver[A, B] with ImpureStagedImpSemantics { q =>
  override val codegen = new ScalaGenBase with ImpureStagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString.endsWith("$MutState")) "MutState"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import scala.collection.mutable.Map
import sai.lang.ImpLang._
object Prelude {
  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value
  implicit class ValueOps(v: Value) {
    def I: Int = v.asInstanceOf[IntV].i
    def B: Boolean = v.asInstanceOf[BoolV].b
  }
}
import Prelude._
"""
}

object ImpureStagedImpTest {
  import ImpLang._
  import ImpLang.Examples._
  def main(args: Array[String]): Unit = {
    val code = new ImpureStagedImpDriver[Int, Unit] {
      @virtualize
      def snippet(u: Rep[Int]) = {
        //val st: Rep[MutState] = newMutState("x" -> IntV(3), "y" -> IntV(4))
        //exec(cond3, st)
        val st: Rep[MutState] = newMutState()
        //exec(Seq(Assign("x", Lit(3)), Assign("y", Lit(4))), st)
        exec(fact5, st)
        dummyRead(st)
        println(st)
      }
    }
    println(code.code)
    //code.eval(0)
  }
}