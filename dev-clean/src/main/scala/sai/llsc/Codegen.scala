package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<llsc.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "list-new", _, _) => true
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  override def primitive(t: String): String = t match {
    case "Unit" => "std::monostate"
    case _ => super.primitive(t)
  }

  override def remap(m: Manifest[_]): String = {
    if (m.toString == "java.lang.String") "String"
    else if (m.toString.endsWith("$Value")) "PtrVal"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$Mem")) "Mem"
    else if (m.toString.endsWith("$SS")) "SS"
    else if (m.toString.endsWith("SMTExpr")) "Expr"
    else super.remap(m)
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
    /*
    case Node(s, "list-new", Const(mA: Manifest[_])::Nil, _) =>
      if (mA.runtimeClass.getName == "scala.Tuple2" &&
        remap(mA.typeArguments(0)) == "SS" &&
        remap(mA.typeArguments(1)) == "PtrVal") {
        emit("mt_path_result")
      } else super.shallow(n)
    */
    case Node(s, "op_2", List(Backend.Const(op: String), x, y), _) =>
      es"op_2(${quoteOp(op)}, $x, $y)"
    case Node(s, "init-ss", List(), _) => es"mt_ss"
    case Node(s, "init-ss", List(m), _) => es"SS($m, mt_stack, mt_pc)"
    case Node(s, "ss-lookup-env", List(ss, x), _) => es"$ss.env_lookup($x)"
    case Node(s, "ss-lookup-addr", List(ss, a), _) => es"$ss.at($a)"
    case Node(s, "ss-lookup-heap", List(ss, a), _) => es"$ss.heap_lookup($a)"
    case Node(s, "ss-assign", List(ss, k, v), _) => es"$ss.assign($k, $v)"
    case Node(s, "ss-assign-seq", List(ss, ks, vs), _) => es"$ss.assign_seq($ks, $vs)"
    case Node(s, "ss-stack-size", List(ss), _) => es"$ss.stack_size()"
    case Node(s, "ss-alloc-stack", List(ss, n), _) => es"$ss.alloc_stack($n)"
    case Node(s, "ss-update", List(ss, k, v), _) => es"$ss.update($k, $v)"
    case Node(s, "ss-push", List(ss), _) => es"$ss.push()"
    case Node(s, "ss-pop", List(ss, n), _) => es"$ss.pop($n)"
    case Node(s, "ss-addpc", List(ss, e), _) => es"$ss.addPC($e)"
    case Node(s, "ss-addpcset", List(ss, es), _) => es"$ss.addPCSet($es)"
    case Node(s, "is-conc", List(v), _) => es"$v->is_conc()"
    case Node(s, "to-SMTBool", List(v), _) => es"$v->to_SMTBool()"
    case _ => super.shallow(n)
  }
}

