package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<llsc.hpp>")
  registerLibraryPath("../stp/build/lib")

  val codegenFolder: String

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "list-new", _, _) => true
    case _ => super.mayInline(n)
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
    case n @ Node(s, "P", List(x), _) => es"std::cout << $x << std::endl"
    case Node(s,"kStack", _, _) => emit("LocV::kStack")
    case Node(s,"kHeap", _, _) => emit("LocV::kHeap")
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

  override def registerTopLevelFunction(id: String, streamId: String = "general")(f: => Unit) =
    if (!registeredFunctions(id)) {
      //if (ongoingFun(streamId)) ???
      //ongoingFun += streamId
      registeredFunctions += id
      withStream(functionsStreams.getOrElseUpdate(id, {
        val functionsStream = new java.io.ByteArrayOutputStream()
        val functionsWriter = new java.io.PrintStream(functionsStream)
        (functionsWriter, functionsStream)
      })._1)(f)
      //ongoingFun -= streamId
    }

  def emitHeaderFile: Unit = {
    val filename = codegenFolder + "/common.h"
    val out = new java.io.PrintStream(filename)
    withStream(out) {
      emitln("/* Emitting header file */")
      emitHeaders(stream)
      emitln("using namespace immer;")
      emitFunctionDecls(stream)
      emitDatastructures(stream)
      emitln("/* End of header file */")
    }
    out.close
  }

  def emitFunctionFiles: Unit = {
    for ((f, (_, funStream)) <- functionsStreams) {
      val filename = s"$codegenFolder/$f.cpp"
      val out = new java.io.PrintStream(filename)
      out.println("#include \"common.h\"")
      funStream.writeTo(out)
      out.close
    }
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    val efs = ""
    val stt = dce.statics.toList.map(quoteStatic).mkString(", ")

    val src = run(name, ng)

    emitHeaderFile
    emitFunctionFiles
    emitInit(stream)

    emitln(s"/* Generated main file: $name */")
    emitln("#include \"common.h\"")
    emit(src)
    emitln("""
    |int main(int argc, char *argv[]) {
    |  initRand();
    |  if (argc != 2) {
    |    printf("usage: %s <arg>\n", argv[0]);
    |    return 0;
    |  }""".stripMargin)
    if (initStream.size > 0)
      emitln("if (init()) return 0;")
    emitln(s"""
    |  // TODO: what is the right way to pass arguments?
    |  $name(${convert("argv[1]", m1)});
    |  return 0;
    |}""".stripMargin)
  }
}

