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
trait GenExternal extends SymExeDefs with EngineBase {
  def sym_exit[T: Manifest](ss: Rep[SS], args: Rep[List[Value]]): Rep[T] = ???

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
}
