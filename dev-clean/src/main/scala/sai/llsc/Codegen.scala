package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import scala.collection.JavaConverters._

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}
import sai.lmsx.smt.SMTBool

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<sai_llvm_sym2.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("LocV") => false
    case Node(_, "stack_addr", _, _) => true
    case Node(_, "heap_addr", _, _) => true
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  def quoteOp(op: String): String = {
    op match {
      case "+"  => "op_plus"
      case "-"  => "op_minus"
      case "*"  => "op_mult"
      case "/"  => "op_div"
      case "="  => "op_eq"
      case "!=" => "op_neq"
      case ">=" => "op_ge"
      case ">"  => "op_gt"
      case "<=" => "op_le"
      case "<"  => "op_lt"
    }
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "op_2", List(Backend.Const(op: String), x, y), _) =>
      es"op_2(${quoteOp(op)}, $x, $y)"
    case _ => super.shallow(n)
  }
}

