package sai.imp

import sai.lang.ImpLang._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._

import scala.collection.immutable.{List => SList}

trait SymStagedImpGen extends StagedImpGen {
  override def remap(m: Manifest[_]): String = {
    if (m.toString == "java.lang.String") "String"
    else if (m.toString.endsWith("$Value")) "Value"
    else if (m.toString.endsWith("$Expr")) "Expr"
    else super.remap(m)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "BoolV-pred", List(i), _) =>
      shallow(i)
      emit(".isInstanceOf[BoolV]")
    case Node(s, "SymV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[SymV].s")
    case Node(s, "op", List(op, x1, x2), _) =>
      emit("op_2(")
      shallow(op); emit(", ")
      shallow(x1); emit(", ")
      shallow(x2); emit(")")
    case _ => super.shallow(n)
  }
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
    case Node(s, "BoolV-pred", List(i), _) =>
      emit("is_BoolV(")
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

trait GenericSymStagedImpDriver[A, B] extends SAIDriver[A, B] { q =>
  override val codegen = new ScalaGenBase with SymStagedImpGen {
    val IR: q.type = q
    import IR._
  }

  override val prelude =
    """
import sai.lang.ImpLang._
import sai.imp.SymRuntime._
"""
}

trait GenericCppSymStagedImpDriver[A, B] extends CppSAIDriver[A, B] { q =>
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
