package sai.lang

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._

import scala.collection.immutable.{List => SList}

object ImpLang {
  sealed trait Stmt
  case class Skip() extends Stmt
  case class Break() extends Stmt
  case class Assign(x: String, e: Expr) extends Stmt
  case class Cond(e: Expr, thn: Stmt, els: Stmt) extends Stmt
  case class Seq(s1: Stmt, s2: Stmt) extends Stmt
  case class While(b: Expr, s: Stmt) extends Stmt

  sealed trait Expr
  case class Lit(x: Any) extends Expr {
    override def toString: String = s"Lit(${x.toString})"
  }
  case class Var(x: String) extends Expr {
    override def toString: String = "Var(\"" + x.toString + "\")"
  }
  case class Op1(op: String, e: Expr) extends Expr {
    override def toString: String = "Op1(\"" + op + "\"," + s"${e.toString})"
  }
  case class Op2(op: String, e1: Expr, e2: Expr) extends Expr {
    override def toString: String =
      "Op2(\"" + op + "\"," + s"${e1.toString}, ${e2.toString})"
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

    val another_fact5 =
      let_("i", 1){ i =>
        let_("fact", 1){ fact =>
          while_(Op2("<=", i, Lit(5)),
            let_("fact", Op2("*", fact, i)){ _ =>
              set_("i", Op2("+", i, Lit(1)))
            })}}

    println(another_fact5)
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

    val cond2 =
      Seq(Cond(Op2("<=", Var("x"), Var("y")),
               Assign("z", Var("x")),
               Assign("z", Var("y"))),
          Assign("z", Op2("+", Var("z"), Lit(1))))

    val cond3 =
      Seq(Cond(Op2("<=", x, y),
               Assign("z", x),
               Assign("z", y)),
        Seq(Assign("z", Op2("+", z, Lit(1))),
          Seq(Cond(Op2(">=", z, y),
            Assign("z", Op2("+", z, Lit(2))),
            Assign("z", Op2("+", z, Lit(3)))),
          Skip())))

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
        println(v)
        0
      }
    }

  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] = new SymStagedImpDriver {
    def snippet(u: Rep[Unit]) = {
      //val init: Rep[Ans] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")),
      //                      Set[Expr]())
      val init: Rep[Ans] = (Map("n" -> SymV("n")), Set[Expr]())
      val v = exec(s)(init).run
      println(v)
      print("path number: ")
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
    val code = specialize(fact5)
    println(code.code)
    code.eval(0)

    //List(((),(Map(x -> IntV(3), z -> IntV(4), y -> IntV(5)),Set(Op2("<=",Var("x"), Var("y"))))),
    //     ((),(Map(x -> IntV(3), z -> IntV(6), y -> IntV(5)),Set(Op1("-",Op2("<=",Var("x"), Var("y")))))))

    /* Symbolic execution */
    //val code = specSym(fact_n)
    //println(code.code)
    //code.eval(())

  }
}
