package sai.lang

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._
import sai.lmsx.smt._

import scala.collection.immutable.{List => SList}
import sai.lang.SimpIL.Assert

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

  }
}

object UnfoldTest {
  import ImpLang._
  import Examples._
  def unfoldW(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfoldW(w, k-1)), Skip())
    }

  def unfold(s: Stmt, k: Int): Stmt = {
    s match {
      case While(e, b) =>
        val ub = unfold(b, k)
        unfoldW(While(e, ub), k)
      case Seq(s1, s2) =>
        Seq(unfold(s1, k), unfold(s2, k))
      case _ => s
    }
  }

  def numAssign(s: Stmt): Int = {
    s match {
      case Skip() => 0
      case Assign(x, e) => 1
      case Cond(e, s1, s2) => numAssign(s1) + numAssign(s2)
      case Seq(s1, s2) => numAssign(s1) + numAssign(s2)
      case While(e, b) => numAssign(b)
    }
  }

  def main(args: Array[String]): Unit = {
    for (i <- 0 until 10) {
      val u = unfold(w2, i)
      println("k: " + i + ", " + numAssign(w2) + ", " + numAssign(u))
    }
    for (i <- 0 until 10) {
      val u = unfold(w3, i)
      println("k: " + i + ", " + numAssign(w3) + ", " + numAssign(u))
    }
  }
}

object TestImp {
  import sai.imp._
  import ImpLang._
  import ImpLang.Examples._

  @virtualize
  //def specialize(e: Expr): SAIDriver[Unit, Unit] = new StagedImpDriver {
  def specializeExpr(e: Expr): CppSAIDriver[Int, Int] =
    new CppStagedImpDriver[Int, Int] {
      def snippet(u: Rep[Int]) = {
        val v: Rep[Value] = eval(e, Map())
        println(v)
        v
      }
    }

  @virtualize
  //def specialize(s: Stmt): SAIDriver[Unit, Unit] = new StagedImpDriver {
  def specialize(s: Stmt): CppSAIDriver[Int, Int] =
    new CppStagedImpDriver[Int, Int] {
      def snippet(u: Rep[Int]) = {
        val v = exec(s)(Map("x" -> IntV(3), "z" -> IntV(4))).run
        v._2("fact")
      }
    }

  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] =
    new SymStagedImpDriver[Unit, Unit] {
      def snippet(u: Rep[Unit]) = {
        //val init: Rep[Ans] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")),
        //                      Set[Expr]())
        val init: Rep[Ans] = (Map("n" -> SymVBV("n")), Set[SMTBool]())
        val v = exec(s)(init).run
        println(v)
        println("path number: ")
        println(v.size)
      }
    }

  @virtualize
  def specSymCpp(s: Stmt): CppSAIDriver[Int, Unit] =
    new CppSymStagedImpDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        //val init: Rep[Ans] = (Map("n" -> SymV("n")), Set[Expr]())
        val init: Rep[Ans] = (Map("y" -> IntV(3)), Set[SMTBool]())
        val v: Rep[List[(Unit, Ans)]] = exec(s)(init).run
        //println(v)
        //println("path number: ")

        v.foreach(l => handle(query(not(l._2._2.foldLeft(lit(true))(and(_, _))))))
        println(v.size)
      }
    }

  def main(args: Array[String]): Unit = {
    /*
    val code = specializeExpr(Op2("+", Lit(1), Lit(2)))
    println(code.code)
    code.eval(0)
     */

    /* Concrete execution */
    /*
    val code = specialize(fact5)
    println(code.code)
    code.eval(0)
     */

    /* Symbolic execution */
    val code = specSymCpp(condAssert)
    println(code.code)
    code.eval(0)
  }
}
