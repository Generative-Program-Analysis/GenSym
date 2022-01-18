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
  // TODO: generating functions with proper names, instead of x1, x2 ...

  // TODO: sym_exit return type in C should be void
  def sym_exit[T: Manifest](ss: Rep[SS], args: Rep[List[Value]]): Rep[T] =
    "sym_exit".reflectWith[T](ss, args)

  def gen_llsc_assert[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val v = args(0)
    if (v.isConc) {
      // Note: we directly project the integer field of v, which is safe if
      // the source program is type checked against the C llsc_assert declaration.
      if (v.int == 0) sym_exit[T](ss, args)
      else k(ss, IntV(1, 32))
    } else {
      val ss1 = ss.addPC(v.toSMTBoolNeg)
      if (checkPC(ss1.pc)) sym_exit[T](ss1, args)
      else k(ss1, IntV(1, 32))
    }
  }

  def llsc_assert(ss: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] =
    gen_llsc_assert[List[(SS, Value)]](ss, args, { case (s, v) => List[(SS, Value)]((s, v)) })

  def llsc_assert_k(ss: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] =
    gen_llsc_assert[Unit](ss, args, { case (s, v) => k(s, v) })
}

class ExternalLLSCDriver(folder: String = ".") extends SAISnippet[Int, Unit] with SAIOps with GenExternal { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val funNameMap: HashMap[Int, String] = new HashMap()
  val blockNameMap: HashMap[Int, String] = new HashMap()

  val codegen: GenericLLSCCodeGen = new GenericLLSCCodeGen {
    val codegenFolder: String = folder
    def funMap: HashMap[Int, String] = funNameMap
    def blockMap: HashMap[Int, String] = blockNameMap
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      val ng = init(g)
      run(name, ng)
      emitln("/* LLSC - External utility functions and library modeling functions */")
      emitFunctionDecls(stream)
      emitFunctions(stream)
    }
  }

  def genHeader: Unit = {
    val mainStream = new PrintStream(s"$folder/external.hpp")
    val statics = Adapter.emitCommon1("header", codegen, mainStream)(manifest[Int], manifest[Unit])(x => Unwrap(wrapper(Wrap[Int](x))))
    mainStream.close
  }

  // TODO: refactor into SAIOps <2022-01-18, David Deng> //

  def hardTopFun[A:Manifest,B:Manifest](f: Rep[A] => Rep[B], s: String): Rep[A => B] = {
    val unwrapped = __hardTopFun(f, 1, xn => Unwrap(f(Wrap[A](xn(0)))))
    if (!s.trim.isEmpty) {
      val n = unwrapped.asInstanceOf[Backend.Sym].n
      funNameMap(n) = s
    }
    Wrap[A=>B](unwrapped)
  }

  def hardTopFun[A:Manifest,B:Manifest,C:Manifest](f: (Rep[A], Rep[B]) => Rep[C], s: String): Rep[(A, B) => C] = {
    val unwrapped = __hardTopFun(f, 2, xn => Unwrap(f(Wrap[A](xn(0)), Wrap[B](xn(1)))))
    if (!s.trim.isEmpty) {
      val n = unwrapped.asInstanceOf[Backend.Sym].n
      funNameMap(n) = s
    }
    Wrap[(A,B)=>C](unwrapped)
  }

  def hardTopFun[A:Manifest,B:Manifest,C:Manifest,D:Manifest](f: (Rep[A], Rep[B], Rep[C]) => Rep[D], s: String): Rep[(A, B, C) => D] = {
    val unwrapped = __hardTopFun(f, 3, xn => Unwrap(f(Wrap[A](xn(0)), Wrap[B](xn(1)), Wrap[C](xn(2)))))
    if (!s.trim.isEmpty) {
      val n = unwrapped.asInstanceOf[Backend.Sym].n
      funNameMap(n) = s
    }
    Wrap[(A,B,C)=>D](unwrapped)
  }

  def snippet(u: Rep[Int]) = {
    hardTopFun(llsc_assert(_,_), "llsc_assert")
    hardTopFun(llsc_assert_k(_,_,_), "llsc_assert_k")
    ()
  }
}

object TestGenerateExternal {
  def main(args: Array[String]): Unit = {
    val code = new ExternalLLSCDriver
    code.genHeader
  }
}
