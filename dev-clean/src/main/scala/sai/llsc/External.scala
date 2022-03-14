package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._

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
import sai.lmsx.smt.SMTBool

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

// Writing models of syscall/external functions is often painful and error-prone
// in a low-level language, and we don't want to maintain multiple version of those
// external/intrinsic functions with only slightly backend difference.
// Can we generate them from our Scala DSL?

@virtualize
trait GenExternal extends SymExeDefs {
  // TODO: sym_exit return type in C should be void
  def sym_exit[T: Manifest](ss: Rep[SS], args: Rep[List[Value]]): Rep[T] =
    "sym_exit".reflectWith[T](ss, args)

  def getString(ptr: Rep[Value], s: Rep[SS]): Rep[String] = "get_string".reflectWith[String](ptr, s)

  def llsc_assert[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val v = args(0)
    if (v.isConc) {
      // Note: we directly project the integer field of v, which is safe if
      // the source program is type checked against the C llsc_assert declaration.
      if (v.int == 0) sym_exit[T](ss, args)
      else k(ss, IntV(1, 32))
    } else {
      val ss1 = ss.addPC(v.toSMTBoolNeg)
      if (checkPC(ss1.pc)) sym_exit[T](ss1, args)
      else k(ss.addPC(v.toSMTBool), IntV(1, 32))
    }
  }

  def open[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val ptr = args(0)
    val name: Rep[String] = getString(ptr, ss)
    val flags = args(1)
    // val mode = args(2) // how to handle optional argument?
    val fs: Rep[FS] = ss.getFs
    val ret: Rep[Fd] = fs.openFile(name, 0)
    ss.setFs(fs)
    k(ss, IntV(ret, 32))
  }

  def close[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    // Only cast by asRepOf when deemed safe
    val fd: Rep[Int] = args(0).toIntV.int.asRepOf[Int]
    val fs: Rep[FS] = ss.getFs
    val ret: Rep[Int] = fs.closeFile(fd)
    ss.setFs(fs)
    k(ss, IntV(ret, 32))
  }

  def read[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Int] = args(0).int.asRepOf[Int]
    val buf: Rep[Value] = args(1)
    val count: Rep[Int] = args(2).int.asRepOf[Int]
    val fs: Rep[FS] = ss.getFs
    val (content, size): (Rep[List[Value]], Rep[Int]) = fs.readFile(fd, count).unlift
    val ss1 = ss.updateSeq(buf, content)
    ss1.setFs(fs)
    k(ss1, IntV(size, 64))
  }

  def write[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Int] = args(0).int.asRepOf[Int]
    val buf: Rep[Value] = args(1)
    val count: Rep[Int] = args(2).int.asRepOf[Int]
    val fs: Rep[FS] = ss.getFs
    val content: Rep[List[Value]] = ss.lookupSeq(buf, count)
    val size = fs.writeFile(fd, content, count)
    ss.setFs(fs)
    k(ss, IntV(size, 64))
  }

  def stat[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val ptr = args(0)
    val name: Rep[String] = getString(ptr, ss)
    val buf: Rep[Value] = args(1)
    val fs: Rep[FS] = ss.getFs
    val (content, status): (Rep[List[Value]], Rep[Int]) = fs.statFile(name).unlift
    if (status == 0) {
      val ss1 = ss.updateSeq(buf, content)
      ss1.setFs(fs)
      k(ss1, IntV(status, 32))
    } else {
      k(ss, IntV(status, 32))
    }
  }

  def openat[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    // TODO: implement this <2022-01-23, David Deng> //
    // int __fd_openat(int basefd, const char *pathname, int flags, mode_t mode);
    // if (fd == AT_FDCWD), call open
    k(ss, IntV(0, 32))
  }

  def gen_k(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[Unit]) => Rep[Unit]): ((Rep[SS], Rep[List[Value]], Rep[Cont]) => Rep[Unit]) = { case (ss, l, k) => ( gen(ss, l, { case (s,v) => k(s,v) }))}

  def gen_p(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[List[(SS, Value)]]) => Rep[List[(SS, Value)]]): ((Rep[SS], Rep[List[Value]]) => Rep[List[(SS, Value)]]) = { case (ss, l) => ( gen(ss, l, { case (s,v) => List[(SS, Value)]((s,v)) }))}

}

class ExternalLLSCDriver(folder: String = "./headers/llsc") extends SAISnippet[Int, Unit] with SAIOps with GenExternal { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericLLSCCodeGen = new GenericLLSCCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Int, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      val ng = init(g)
      run(name, ng)
      emitln("/* LLSC - External utility functions and library modeling functions */")
      emitln("/* Generated by sai.llsc.TestGenerateExternal */")
      emitln("#ifndef LLSC_EXTERNAL_HEADERS_GEN")
      emitln("#define LLSC_EXTERNAL_HEADERS_GEN")
      emitFunctionDecls(stream)
      emitFunctions(stream)
      emitln("#endif // LLSC_EXTERNAL_HEADERS_GEN")
    }
  }

  def genHeader: Unit = {
    val mainStream = new PrintStream(s"$folder/external.hpp")
    val statics = Adapter.emitCommon1("header", codegen, mainStream)(manifest[Int], manifest[Unit])(x => Unwrap(wrapper(Wrap[Int](x))))
    mainStream.close
  }

  def snippet(u: Rep[Int]) = {
    // TODO: llsc_assert_k depends on sym_exit, which doesn't have a _k version right now <2022-01-23, David Deng> //
    // hardTopFun(gen_p(llsc_assert), "llsc_assert", "inline")
    // hardTopFun(gen_k(llsc_assert), "llsc_assert", "inline")
    hardTopFun(gen_p(open), "open", "inline")
    hardTopFun(gen_k(open), "open", "inline")
    hardTopFun(gen_p(close), "close", "inline")
    hardTopFun(gen_k(close), "close", "inline")
    hardTopFun(gen_p(read), "read", "inline")
    hardTopFun(gen_k(read), "read", "inline")
    hardTopFun(gen_p(write), "write", "inline")
    hardTopFun(gen_k(write), "write", "inline")
    hardTopFun(gen_p(stat), "stat", "inline")
    hardTopFun(gen_k(stat), "stat", "inline")
    // hardTopFun(gen_p(openat), "openat", "inline")
    // hardTopFun(gen_k(openat), "openat", "inline")
    ()
  }
}

object TestGenerateExternal {
  def main(args: Array[String]): Unit = {
    val code = new ExternalLLSCDriver
    code.genHeader
  }
}
