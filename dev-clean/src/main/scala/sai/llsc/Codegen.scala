package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._
import java.io.FileOutputStream

import collection.mutable.HashMap

trait GenericLLSCCodeGen extends CppSAICodeGenBase {
  registerLibrary("-lz3")
  registerLibrary("-lstp")
  registerHeader("./headers", "<llsc.hpp>")

  val codegenFolder: String
  // TODO: refactor to Sym => String map?
  var funMap = new HashMap[Int, String]()
  var blockMap = new HashMap[Int, String]()

  def setFunMap(m: HashMap[Int, String]) = funMap = m
  def setBlockMap(m: HashMap[Int, String]) = blockMap = m

  def reconsMapping(subst: HashMap[Sym, Exp]): Unit = {
    val newFunMap = new HashMap[Int, String]()
    val newBlockMap = new HashMap[Int, String]()
    for ((x, nm) <- funMap) {
      if (subst.contains(Sym(x))) newFunMap(subst(Sym(x)).asInstanceOf[Sym].n) = nm
    }
    for ((x, nm) <- blockMap) {
      if (subst.contains(Sym(x))) newBlockMap(subst(Sym(x)).asInstanceOf[Sym].n) = nm
    }
    funMap = newFunMap
    blockMap = newBlockMap
  }

  override def quote(s: Def): String = s match {
    case Sym(n) =>
      funMap.getOrElse(n, blockMap.getOrElse(n, super.quote(s)))
    case _ => super.quote(s)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "list-new", _, _) => true
    case Node(_, "make_SymV", _, _) => true
    case Node(_, "make_IntV", _, _) => true
    case Node(_, "null-v", _, _) => true
    case _ => super.mayInline(n)
  }

  override def remap(m: Manifest[_]): String = {
    if (m.toString == "java.lang.String") "String"
    else if (m.toString.endsWith("$Value")) "PtrVal"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$BlockLabel")) "BlockLabel"
    else if (m.toString.endsWith("$Mem")) "Mem"
    else if (m.toString.endsWith("$SS")) "SS"
    else if (m.toString.endsWith("$PC")) "PC"
    else if (m.toString.endsWith("$FS")) "FS"
    else if (m.toString.endsWith("$File")) "Ptr<File>"
    else if (m.toString.endsWith("$Stream")) "Ptr<Stream>"
    else if (m.toString.endsWith("$Kind")) "LocV::Kind"
    else if (m.toString.endsWith("SMTExpr")) "PtrVal"
    else if (m.toString.endsWith("SMTBool")) "PtrVal"
    else if (m.runtimeClass.getName.endsWith("Future"))
      s"std::future<${remap(m.typeArguments(0))}>"
    else super.remap(m)
  }

  def quoteOp(op: String, ec: String): String = ec + "::" + "op_" + op

  override def quoteBlockP(prec: Int)(f: => Unit) = {
    def wraper(numStms: Int, l: Option[Node], y: Block)(f: => Unit) = {
      val paren = numStms > 0 || l.map(n => precedence(n) < prec).getOrElse(false)
      val brace = numStms > 0 // || (numStms == 0 ^ y.res != Const(()))
      if (paren) emit("(")
      if (brace) emitln("{")
      f
      shallow(y.res); emit(quoteEff(y.eff));
      // if (numStms > 0) emitln(";")
      if (brace) emit(";\n}")
      if (paren) emit(")")
    }
    withWraper(wraper _)(f)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "make_CPSFunV", _, _) if !dce.live(n.n) =>
    case Node(s, "make_FunV", _, _) if !dce.live(n.n) =>
    case _ => super.traverse(n)
  }

  override def shallow(n: Node): Unit = n match {
    case n @ Node(s, "P", List(x), _) => es"std::cout << $x << std::endl"
    case Node(s,"kStack", _, _) => emit("LocV::kStack")
    case Node(s,"kHeap", _, _) => emit("LocV::kHeap")
    case Node(s, "int_op_2", List(Backend.Const(op: String), x, y), _) =>
      es"int_op_2(${quoteOp(op, "iOP")}, $x, $y)"
    case Node(s, "float_op_2", List(Backend.Const(op: String), x, y), _) =>
      es"float_op_2(${quoteOp(op, "fOP")}, $x, $y)"
    case Node(s, "init-ss", List(), _) => es"mt_ss"
    case Node(s, "init-ss", List(m), _) => es"SS($m, mt_stack, mt_pc, mt_bb)"

    case Node(s, "ss-lookup-env", List(ss, x), _) => es"$ss.env_lookup($x)"
    case Node(s, "ss-lookup-addr", List(ss, a, sz), _) => es"$ss.at($a, $sz)"
    case Node(s, "ss-lookup-addr-struct", List(ss, a, sz), _) => es"$ss.at_struct($a, $sz)"
    case Node(s, "ss-lookup-addr-seq", List(ss, a, sz), _) => es"$ss.at_seq($a, $sz)"
    case Node(s, "ss-lookup-heap", List(ss, a), _) => es"$ss.heap_lookup($a)"
    case Node(s, "ss-array-lookup", List(ss, base, off, es, k), _) => es"array_lookup_k($ss, $base, $off, $es, $k)"
    case Node(s, "ss-array-lookup", List(ss, base, off, es), _) => es"array_lookup($ss, $base, $off, $es)"
    case Node(s, "ss-assign", List(ss, k, v), _) => es"$ss.assign($k, $v)"
    case Node(s, "ss-assign-seq", List(ss, ks, vs), _) => es"$ss.assign_seq($ks, $vs)"
    case Node(s, "ss-heap-size", List(ss), _) => es"$ss.heap_size()"
    case Node(s, "ss-heap-append", List(ss, vs), _) => es"$ss.heap_append($vs)"
    case Node(s, "ss-stack-size", List(ss), _) => es"$ss.stack_size()"
    case Node(s, "ss-alloc-stack", List(ss, n), _) => es"$ss.alloc_stack($n)"
    case Node(s, "ss-update", List(ss, k, v, sz), _) => es"$ss.update($k, $v, $sz)"
    case Node(s, "ss-update-seq", List(ss, k, v), _) => es"$ss.update_seq($k, $v)"
    case Node(s, "ss-push", List(ss), _) => es"$ss.push()"
    case Node(s, "ss-pop", List(ss, n), _) => es"$ss.pop($n)"
    case Node(s, "ss-addpc", List(ss, e), _) => es"$ss.add_PC($e)"
    case Node(s, "add-pc", List(pc, e), _) => es"$pc.add($e)"
    case Node(s, "ss-addpcset", List(ss, es), _) => es"$ss.add_PC_set($es)"
    case Node(s, "ss-add-incoming-block", List(ss, bb), _) => es"$ss.add_incoming_block($bb)"
    case Node(s, "ss-incoming-block", List(ss), _) => es"$ss.incoming_block()"
    case Node(s, "ss-arg", List(ss), _) => es"$ss.init_arg()"
    case Node(s, "ss-error-loc", List(ss), _) => es"$ss.init_error_loc()"
    case Node(s, "ss-get-fs", List(ss), _) => es"$ss.get_fs()"
    case Node(s, "ss-set-fs", List(ss, fs), _) => es"$ss.set_fs($fs)"
    case Node(s, "get-pc", List(ss), _) => es"$ss.get_PC()"

    case Node(s, "is-conc", List(v), _) => es"$v->is_conc()"
    case Node(s, "to-SMTNeg", List(v), _) => es"SymV::neg($v)"
    case Node(s, "ValPtr-deref", List(v), _) => es"*$v"
    case Node(s, "nullptr", _, _) => es"nullptr"
    case Node(s, "to-bytes", List(v), _) => es"$v->to_bytes()"
    case Node(s, "to-bytes-shadow", List(v), _) => es"$v->to_bytes_shadow()"
    case Node(s, "from-bytes", List(l), _) => es"Value::from_bytes($l)"
    case Node(s, "from-bytes-shadow", List(l), _) => es"Value::from_bytes_shadow($l)"
    case Node(s, "make_FloatV", List(Const(l: String), Const(80)), _) =>
      val byteArr = l.substring(3).grouped(2).toList
      val litRep = "{" + byteArr.reverse.map(v => "0x" ++ v).mkString(", ") + "}"
      es"make_FloatV_fp80($litRep)"
    case Node(s, "make_FloatV", List(f, bw), _) => es"make_FloatV($f, $bw)"
    case Node(s, "get-bw", List(v), _) => es"$v->get_bw()"
    case Node(s, "ptroff", List(v, o), _) => es"($v + ($o))"

    case Node(s, "cov-set-blocknum", List(n), _) => es"cov().set_num_blocks($n)"
    case Node(s, "cov-inc-block", List(id), _) => es"cov().inc_block($id)"
    case Node(s, "cov-inc-path", List(n), _) => es"cov().inc_path($n)"
    case Node(s, "cov-inc-inst", List(n), _) => es"cov().inc_inst($n)"
    case Node(s, "cov-start-mon", _, _) => es"cov().start_monitor()"
    case Node(s, "print-block-cov", _, _) => es"cov().print_block_cov()"
    case Node(s, "print-time", _, _) => es"cov().print_time()"
    case Node(s, "print-path-cov", _, _) => es"cov().print_path_cov()"
    case Node(s, "assert", List(cond, msg), _) => es"ASSERT(($cond), $msg)"

    case Node(s, "add_tp_task", List(b: Block), _) =>
      es"tp.add_task("
      quoteTypedBlock(b, false, true, capture = "=")
      es")"
    case Node(s, "async_exec_block", List(b: Block), _) =>
      es"async_exec_block("
      quoteTypedBlock(b, false, true, capture = "=")
      es")"

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
    } else {
      withStream(functionsStreams(id)._1)(f)
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
      emitln(s"""
      |inline Monitor& cov() {
      |  static Monitor m(${BlockCounter.count});
      |  return m;
      |}""".stripMargin)
      emitln("/* End of header file */")
    }
    out.close
  }

  // 2 pass
  def emitFunctionFiles: Unit = {
    for ((f, (_, funStream)) <- functionsStreams) {
      if (!blockMap.values.exists(_ == f)) {
        val filename = s"$codegenFolder/$f.cpp"
        val out = new java.io.PrintStream(filename)
        out.println("#include \"common.h\"")
        funStream.writeTo(out)
        out.close
      }
    }

    for ((f, (_, funStream)) <- functionsStreams) {
      if (blockMap.values.exists(_ == f)) {
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
    emitFunctionFiles
    emitInit(stream)

    emitln(s"/* Generated main file: $name */")
    emitln("#include \"common.h\"")
    emit(src)
    emitln(s"""
    |int main(int argc, char *argv[]) {
    |  prelude(argc, argv);
    |  if (can_par_tp()) {
    |    tp.add_task([]() { return $name(0); });
    |  } else {
    |    $name(0);
    |  }
    |  epilogue();
    |  return exit_code.load().value_or(0);
    |} """.stripMargin)
  }
}

trait PureLLSCCodeGen extends GenericLLSCCodeGen {
  override def shallow(n: Node): Unit = n match {
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
}

trait ImpureLLSCCodeGen extends GenericLLSCCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "ss-copy", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "ss-copy", List(ss), _) => es"$ss.copy()"
    case _ => super.shallow(n)
  }
}

trait StdVectorCodeGen extends ExtendedCPPCodeGen {
  // TODO: make it more complete

  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.List") {
      val kty = m.typeArguments(0)
      s"std::vector<${remap(kty)}>"
    } else { super.remap(m) }
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "list-new", Const(mA: Manifest[_])::xs, _) =>
      es"std::vector<${remap(mA)}>{"
      if (!xs.isEmpty) {
        shallow(xs.head)
        xs.tail.map { x =>
          emit(", ")
          shallow(x)
        }
      }
      es"}"
    case _ => super.shallow(n)
  }
}
