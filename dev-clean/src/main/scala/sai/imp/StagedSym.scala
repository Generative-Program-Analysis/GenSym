package sai.imp

import sai.lang.ImpLang._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}

@virtualize
trait SymStagedImp extends SAIOps {
  import StateT._
  import ListT._

  implicit val bw: Int = 32
  // FIXME: are we allowing Boolean exp?
  val boolOp:Set[String] = scala.collection.immutable.Set()

  type Value = SMTExpr
  def IntV(i: Rep[Int]): Rep[Value] = lit(i)
  def BoolV(b: Rep[Boolean]): Rep[Value] = lit(b)
  def SymVBV(x: String): Rep[Value] = bvVar(x)
  def SymVBool(x: String): Rep[Value] = boolVar(x)

  /*
  def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }
   */
  // TODO Change

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Adapter.typeMap.get(Unwrap(v)).get.runtimeClass.getName match {
      case x if x.contains("SMTBool") =>
        not(v.asInstanceOf[Rep[SMTBool]])
      case x if x.contains("BV") =>
        bvNeg(v.asInstanceOf[Rep[BV]])
      case _ => ???
    }
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    if (boolOp.contains(op)) {
      // import SyntaxSAT._
      // val v1B: Rep[SATBool] = v1.asInstanceOf[Rep[SATBool]]
      // val v2B: Rep[SATBool] = v2.asInstanceOf[Rep[SATBool]]
      // op match {
      //   case "==" => v1B ≡ v2B
      //   case "<=" => v1B <= v2B
      //   case ">=" => v1B >= v2B
      //   case "<" => v1B < v2B
      //   case ">" => v1B > v2B
      // }
      ???
    } else {
      import SyntaxSMT._
      val v1BV = v1.asInstanceOf[Rep[BV]]
      val v2BV = v2.asInstanceOf[Rep[BV]]
      op match {
        case "==" => v1BV ≡ v2BV
        case "<=" => v1BV <= v2BV
        case ">=" => v1BV >= v2BV
        case "<" => v1BV < v2BV
        case ">" => v1BV > v2BV
        case "+" => v1BV + v2BV
        case "-" => v1BV - v2BV
        case "*" => v1BV * v2BV
        case "/" => v1BV / v2BV
      }
    }
  }

  // def to_smt(v: Rep[Value]): Rep[SMTExpr] = {
  //   Unwrap(v) match {
  //     case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
  //       val v1: Rep[BV] = 
  //       IntV(-v1)
  //     case Adapter.g.Def("SymV", scala.collection.immutable.List(v: Backend.Exp)) =>
  //       val v1: Rep[String] = Wrap[String](v)
  //       SymV(unit("-" + v1))
  //     case Adapter.g.Def
  //   }
  // }

  type PC = Set[SMTBool]
  type Store = Map[String, Value]
  type Ans = (Store, PC)
  type M[T] = StateT[ListM, Ans, T] // List[(Unit, (Store, PC))]
  val M = Monad[M]

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val v = eval(e, σ)
      op_neg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      op_2(op, v1, v2)
  }

  def evalM(e: Expr): M[Value] = for {
    σ <- get_store
  } yield eval(e, σ)

  def get_store: M[Store] = for {
    ans <- MonadState[M, Ans].get
  } yield ans._1

  def update_store(x: String, v: Rep[Value]): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    _ <- MonadState[M, Ans].put((ans._1 + (x -> v), ans._2))
  } yield ()

  def update_pc(e: Expr): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    pc <- evalM(e)
    _ <- MonadState[M, Ans].put((ans._1, ans._2 ++ Set(pc.asInstanceOf[Rep[SMTBool]])))
  } yield ()

  def select(cnd: Expr, m1: M[Unit], m2: M[Unit]): M[Unit] = {
    val b1 = for {
      _ <- update_pc(cnd)
      _ <- m1
    } yield ()
    val b2 = for {
      _ <- update_pc(Op1("-", cnd))
      _ <- m2
    } yield ()

    StateT[ListM, Ans, Unit]((s: Rep[Ans]) => {
      b1.run(s) ++ b2.run(s)
    })
  }

  def fix[A: Manifest, B: Manifest](f: Rep[A] => Rep[B]): Rep[A => B] = fun(f)

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, Input()) => for {
      // TODO: name of the fresh variable
      _ <- update_store(x, SymVBV(x))
    } yield ()
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_store(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      //σ <- get_store
      _ <- select(e, exec(s1), exec(s2))
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1)
      _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      // TODO: add break
      /*
      def f: Rep[Ans => List[(Unit, Ans)]] = fix { s =>
        val ans = for {
          cnd <- evalM(e)
          σ <- get_store
          _ <- br(e, exec(b), exec(Skip()))
        } yield ()
        ans.run(s).run
      }
      */
      val k = 4
      exec(unfold(While(e, b), k))
    case Assert(e) => for {
      _ <- update_pc(e)
    } yield ()
  }

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }
}

trait SymStagedImpGen extends StagedImpGen {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "op", List(op, x1, x2), _) =>
      emit("op_2(")
      shallow(op); emit(", ")
      shallow(x1); emit(", ")
      shallow(x2); emit(")")
    case _ => super.shallow(n)
  }

}

trait SymStagedImpDriver extends SAIDriver[Unit, Unit] with SymStagedImp { q =>
  override val codegen = new ScalaGenBase with SymStagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString.endsWith("$Expr")) "Expr"
      else super.remap(m)
    }
  }

  override val prelude =
    """
import sai.lang.ImpLang._
import sai.imp.SymRuntime._
"""
}

object SymRuntime {
  trait Value
  case class IntV (i: Int) extends Value
  case class BoolV (b: Boolean) extends Value
  case class SymV(s: String) extends Value
  case class SymE(op: String, args: List[Value]) extends Value

  type PC = Set[Expr]
  type Store = Map[String, Value]

  def op_2(op: String, v1: Value, v2: Value): Value =
    (op, v1, v2) match {
      case ("+", IntV(i1), IntV(i2)) => IntV(i1 + i2)
      case ("-", IntV(i1), IntV(i2)) => IntV(i1 - i2)
      case ("*", IntV(i1), IntV(i2)) => IntV(i1 * i2)
      case ("/", IntV(i1), IntV(i2)) => IntV(i1 / i2)
      case ("==", IntV(i1), IntV(i2)) => BoolV(i1 == i2)
      case (">=", IntV(i1), IntV(i2)) => BoolV(i1 >= i2)
      case (">", IntV(i1), IntV(i2)) => BoolV(i1 > i2)
      case ("<=", IntV(i1), IntV(i2)) => BoolV(i1 <= i2)
      case ("<", IntV(i1), IntV(i2)) => BoolV(i1 < i2)
      case (op, x1, x2) => SymE(op, List(x1, x2))
    }
}

trait CppSymStagedImpGen extends CppSAICodeGenBase {
  registerHeader("./header", "<sai_imp_sym.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("BoolV") => false
    case Node(_, name, _, _) if name.startsWith("SymV") => false
    case Node(_, name, _, _) if name.startsWith("SymE") => false
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(s@Op2(op, x, y)) => "\"" + s.toSExp + "\""
    case Const(s@Op1(op, x)) => "\"" + s.toSExp + "\""
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) =>
      emit("make_IntV(")
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      emit("proj_IntV(")
      shallow(i)
      emit(")")
    case Node(s, "BoolV", List(b), _) =>
      emit("make_BoolV(")
      shallow(b)
      emit(")")
    case Node(s, "BoolV-proj", List(i), _) =>
      emit("proj_BoolV(")
      shallow(i)
      emit(")")
    case Node(s, "SymV", List(x), _) =>
      emit("make_SymV(")
      shallow(x)
      emit(")")
    case Node(s, "op", List(op, x1, x2), _) =>
      emit("op_2(")
      shallow(op); emit(", ")
      shallow(x1); emit(", ")
      shallow(x2); emit(")")
    case _ => super.shallow(n)
  }
}

trait CppSymStagedImpDriver[A, B] extends CppSAIDriver[A, B] with SymStagedImp { q =>
  override val codegen = new CGenBase with CppSymStagedImpGen {
    val IR: q.type = q
    import IR._

    override def primitive(t: String): String = t match {
      case "Unit" => "std::monostate"
      case _ => super.primitive(t)
    }

    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "Ptr<Value>"
      else if (m.toString.endsWith("$Expr")) "String"
      else if (m.toString.endsWith("SMTExpr")) "Expr"
      else super.remap(m)
    }
  }
}
