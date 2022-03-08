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
    else if (m.toString.endsWith("$FS")) "FS"
    else if (m.toString.endsWith("$Kind")) "LocV::Kind"
    else if (m.toString.endsWith("SMTExpr")) "PtrVal"
    else if (m.toString.endsWith("SMTBool")) "PtrVal"
    else if (m.runtimeClass.getName.endsWith("Future"))
      s"std::future<${remap(m.typeArguments(0))}>"
    else super.remap(m)
  }

  def quoteOp(op: String): String = "op_" + op

  override def shallow(n: Node): Unit = n match {
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
    case Node(s, "ss-lookup-addr", List(ss, a, sz), _) => es"$ss.at($a, $sz)"
    case Node(s, "ss-lookup-addr-struct", List(ss, a, sz), _) => es"$ss.at_struct($a, $sz)"
    case Node(s, "ss-lookup-addr-seq", List(ss, a, sz), _) => es"$ss.at_seq($a, $sz)"
    case Node(s, "ss-lookup-heap", List(ss, a), _) => es"$ss.heap_lookup($a)"
    case Node(s, "ss-array-lookup", List(ss, base, off, es, ns, k), _) => es"array_lookup_k($ss, $base, $off, $es, $ns, $k)"
    case Node(s, "ss-array-lookup", List(ss, base, off, es, ns), _) => es"array_lookup($ss, $base, $off, $es, $ns)"
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
    case Node(s, "ss-addpcset", List(ss, es), _) => es"$ss.add_PC_set($es)"
    case Node(s, "ss-add-incoming-block", List(ss, bb), _) => es"$ss.add_incoming_block($bb)"
    case Node(s, "ss-incoming-block", List(ss), _) => es"$ss.incoming_block()"
    case Node(s, "ss-arg", List(ss), _) => es"$ss.init_arg()"
    case Node(s, "ss-get-fs", List(ss), _) => es"$ss.get_fs()"
    case Node(s, "ss-set-fs", List(ss, fs), _) => es"$ss.set_fs($fs)"
    case Node(s, "get-pc", List(ss), _) => es"$ss.get_PC()"

    case Node(s, "is-conc", List(v), _) => es"$v->is_conc()"
    case Node(s, "to-SMTNeg", List(v), _) => es"to_SMTNeg($v)"
    case Node(s, "ValPtr-deref", List(v), _) => es"*$v"
    case Node(s, "to-IntV", List(v), _) => es"$v->to_IntV()"
    case Node(s, "to-LocV", List(v), _) => es"make_LocV($v)"
    case Node(s, "nullptr", _, _) => es"nullptr"
    case Node(s, "to-bytes", List(v), _) => es"$v->to_bytes()"
    case Node(s, "to-bytes-shadow", List(v), _) => es"$v->to_bytes_shadow()"
    case Node(s, "make_FloatV", List(f, bw), _) => bw match {
      // fp80
      case Const(80) => {
        val Const(l: String) = f
        val numStr = l.substring(3)
        def toBytes(str: String): List[String] = 
          if (str.length < 2) List() 
          else str.take(2) :: toBytes(str.drop(2))
        val byteArr = toBytes(numStr)
        val litRep = '{' + byteArr.reverse.map(v => "0x" ++ v).mkString(", ") + '}'
        emit(s"make_FloatV_fp80($litRep)")
      }
      case _ => es"make_FloatV($f)"
    }
    case Node(s, "get-bw", List(v), _) => es"$v->get_bw()"

    case Node(s, "cov-set-blocknum", List(n), _) => es"cov().set_num_blocks($n)"
    case Node(s, "cov-inc-block", List(id), _) => es"cov().inc_block($id)"
    case Node(s, "cov-inc-path", List(n), _) => es"cov().inc_path($n)"
    case Node(s, "cov-start-mon", _, _) => es"cov().start_monitor()"
    case Node(s, "print-block-cov", _, _) => es"cov().print_block_cov()"
    case Node(s, "print-time", _, _) => es"cov().print_time()"
    case Node(s, "print-path-cov", _, _) => es"cov().print_path_cov()"

    case Node(s, "fs-open-file", List(fs, p, f), _) => es"$fs.open_file($p, $f)"
    case Node(s, "fs-close-file", List(fs, fd), _) => es"$fs.close_file($fd)"
    case Node(s, "fs-read-file", List(fs, fd, n), _) => es"$fs.read_file($fd, $n)"
    case Node(s, "fs-write-file", List(fs, fd, c, n), _) => es"$fs.write_file($fd, $c, $n)"
    case Node(s, "fs-stat-file", List(fs, ptr), _) => es"$fs.stat_file($ptr)"

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
    |#ifdef USE_TP
    |  tp.add_task([]() { return $name(0); });
    |#else
    |  $name(0);
    |#endif
    |  epilogue();
    |  return exit_code.load().value_or(0);
    |} """.stripMargin)
  }
}

trait PureLLSCCodeGen extends GenericLLSCCodeGen {
  registerHeader("./headers", "<llsc.hpp>")

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
  registerHeader("./headers", "<llsc_imp.hpp>")

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
      xs.zipWithIndex.map { case (x, i) =>
        shallow(x)
        if (i != xs.length-1) emit(", ")
      }
      es"}"
    case _ => super.shallow(n)
  }
}
