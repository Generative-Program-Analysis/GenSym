package sai.conc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._
import java.io.FileOutputStream

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<conc.hpp>")
  // registerHeader("./headers", "<intrinsics.hpp>")
  // registerHeader("./headers", "<external.hpp>")

  registerHeader("<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")

  val codegenFolder: String

  override def quote(s: Def): String = s match {
    case Sym(n) if n == FunName.conc_exec => "conc_exec" 
    case Sym(n) =>
      FunName.funMap.getOrElse(n, {
        FunName.blockMap.getOrElse(n, super.quote(s))
      })
    case _ => super.quote(s)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "list-new", _, _) => true
    case Node(_, "make_SymV", _, _) => true
    case Node(_, "make_IntV", _, _) => true
    case Node(_, "null-v", _, _) => true
    case _ => super.mayInline(n)
  }

  /*
  override def mustInline(n: Node): Boolean = n match {
    case Node(_, "make_IntV", _, _) => true
    case _ => super.mustInline(n)
  }
   */

  override def remap(m: Manifest[_]): String = {
    if (m.toString == "java.lang.String") "String"
    else if (m.toString.endsWith("$Value")) "PtrVal"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$BlockLabel")) "BlockLabel"
    else if (m.toString.endsWith("$Mem")) "Mem"
    else if (m.toString.endsWith("$SS")) "SS"
    else if (m.toString.endsWith("SMTExpr")) "SExpr"
    else if (m.toString.endsWith("SMTBool")) "SExpr"
    else if (m.runtimeClass.getName.endsWith("Future"))
      s"std::future<${remap(m.typeArguments(0))}>"
    else super.remap(m)
  }

  def quoteOp(op: String): String = "op_" + op

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
    case Node(s, "int_op_2", List(Backend.Const(op: String), x, y), _) =>
      es"int_op_2(${quoteOp(op)}, $x, $y)"
    case Node(s, "float_op_2", List(Backend.Const(op: String), x, y), _) =>
      es"float_op_2(${quoteOp(op)}, $x, $y)"
    case Node(s, "init-ss", List(), _) => es"mt_ss"
    case Node(s, "init-ss", List(m), _) => es"SS($m, mt_stack, mt_pc, mt_bb)"

    case Node(s, "ss-lookup-env", List(ss, x), _) => es"$ss.env_lookup($x)"
    case Node(s, "ss-slookup-env", List(ss, x), _) => es"$ss.senv_lookup($x)"
    case Node(s, "ss-lookup-addr", List(ss, a), _) => es"$ss.at($a)"
    case Node(s, "ss-lookup-addr-struct", List(ss, a, sz), _) => es"$ss.at($a, $sz)"
    case Node(s, "ss-slookup-addr", List(ss, a), _) => es"$ss.sat($a)"
    case Node(s, "ss-slookup-addr-struct", List(ss, a, sz), _) => es"$ss.sat($a, $sz)"
    case Node(s, "ss-lookup-heap", List(ss, a), _) => es"$ss.heap_lookup($a)"
    case Node(s, "ss-assign", List(ss, k, v), _) => es"$ss.assign($k, $v)"
    case Node(s, "ss-sassign", List(ss, k, v), _) => es"$ss.sassign($k, $v)"
    case Node(s, "ss-assign-seq", List(ss, ks, vs), _) => es"$ss.assign_seq($ks, $vs)"
    case Node(s, "ss-sassign-seq", List(ss, ks, vs), _) => es"$ss.sassign_seq($ks, $vs)"
    case Node(s, "ss-heap-size", List(ss), _) => es"$ss.heap_size()"
    case Node(s, "ss-heap-append", List(ss, vs), _) => es"$ss.heap_append($vs)"
    case Node(s, "ss-stack-size", List(ss), _) => es"$ss.stack_size()"
    case Node(s, "ss-sstack-size", List(ss), _) => es"$ss.sstack_size()"
    case Node(s, "ss-alloc-stack", List(ss, n), _) => es"$ss.alloc_stack($n)"
    case Node(s, "ss-alloc-sstack", List(ss, n), _) => es"$ss.alloc_sstack($n)"
    case Node(s, "ss-update", List(ss, k, v), _) => es"$ss.update($k, $v)"
    case Node(s, "ss-supdate", List(ss, k, v), _) => es"$ss.supdate($k, $v)"
    case Node(s, "ss-push", List(ss), _) => es"$ss.push()"
    case Node(s, "ss-pop", List(ss, n), _) => es"$ss.pop($n)"
    case Node(s, "ss-addpc", List(ss, e), _) => es"$ss.addPC($e)"
    case Node(s, "ss-addpcset", List(ss, es), _) => es"$ss.addPCSet($es)"
    case Node(s, "ss-add-incoming-block", List(ss, bb), _) => es"$ss.addIncomingBlock($bb)"
    case Node(s, "ss-incoming-block", List(ss), _) => es"$ss.incoming_block()"
    case Node(s, "ss-arg", List(ss, i), _) => es"$ss.init_arg($i)"
    case Node(s, "get-pc", List(ss), _) => es"$ss.getPC()"
    case Node(s, "null-v", _, _) => es"nullptr"

    case Node(s, "is-conc", List(v), _) => es"$v->is_conc()"
    case Node(s, "to-SMTBool", List(v), _) => es"$v->to_SMTBool()"
    case Node(s, "to-SMTBoolNeg", List(v), _) => es"to_SMTBoolNeg($v)"
    case Node(s, "ValPtr-deref", List(v), _) => es"*$v"
    case Node(s, "to-IntV", List(v), _) => es"$v->to_IntV()"

    case Node(s, "cov-set-blocknum", List(n), _) => es"cov.set_num_blocks($n)"
    case Node(s, "cov-inc-block", List(id), _) => es"cov.inc_block($id)"
    case Node(s, "cov-inc-path", List(n), _) => es"cov.inc_path($n)"
    case Node(s, "cov-start-mon", _, _) => es"cov.start_monitor()"
    case Node(s, "print-block-cov", _, _) => es"cov.print_block_cov()"
    case Node(s, "print-time", _, _) => es"cov.print_time()"
    case Node(s, "print-path-cov", _, _) => es"cov.print_path_cov()"

    case Node(s, "tp-async", List(b: Block), _) =>
      //emit("std::async(std::launch::async, [&]")
      emit("create_async<flex_vector<std::pair<SS, PtrVal>>>([&]")
      quoteBlockPReturn(traverse(b))
      emit(")")
    case Node(s, "tp-enqueue", List(b: Block), _) =>
      // FIXME: lms cannot correctly generate closure with unit/monostate argument
      emit("pool.enqueue([&]")
      quoteBlockPReturn(traverse(b))
      emit(")")
    case Node(s, "tp-future-get", List(f), _) => es"$f.get()"
    case Node(s, "can-par", _, _) => es"can_par()"
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

  // 2 pass
  def emitFunctionFiles: Unit = {
    for ((f, (_, funStream)) <- functionsStreams) {
      if (!FunName.blockMap.values.exists(_ == f)) {
        if (f == "conc_exec") {
          funStream.writeTo(stream)
        } else {
          val filename = s"$codegenFolder/$f.cpp"
          val out = new java.io.PrintStream(filename)
          out.println("#include \"common.h\"")
          funStream.writeTo(out)
          out.close
        }
      }
    }

    for ((f, (_, funStream)) <- functionsStreams) {
      if (FunName.blockMap.values.exists(_ == f)) {
        val funName = f.substring(0, f.indexOf("_Block"))
        val filename = s"$codegenFolder/$funName.cpp"
        val out = new java.io.PrintStream(new FileOutputStream(filename, true))
        funStream.writeTo(out)
        out.close
      }
    }
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    val efs = ""
    val stt = dce.statics.toList.map(quoteStatic).mkString(", ")

    val src = run(name, ng)

    emitHeaderFile
    emitInit(stream)

    emitln(s"/* Generated main file: $name */")
    emitln("#include \"common.h\"")
    emit(src)
    emitln(s"""
    |int main(int argc, char *argv[]) {
    |  init_rand();
    |  handle_cli_args(argc, argv);
    |  $name(0);
    |  cov.print_time();
    |  cov.print_block_cov();
    |  cov.print_path_cov();
    |  return 0;
    |} """.stripMargin)

    emitFunctionFiles
    //if (initStream.size > 0)
    //  emitln("if (init()) return 0;")
  }
}

