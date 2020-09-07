package sai
package lmsx

import scala.language.higherKinds
import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import sai.structure.lattices._
import sai.structure.monad._

import sai.lmsx.smt._

abstract class SAISnippet[A: Manifest, B: Manifest] extends SAIOps {
  def wrapper(x: Rep[A]): Rep[B] = snippet(x)
  def snippet(x: Rep[A]): Rep[B]
}

trait SAIOps extends Base
    with PrimitiveOps with LiftPrimitives with Equal
    with OrderingOps  with LiftVariables  with TupleOpsOpt
    with ListOpsOpt   with MapOpsOpt      with SetOpsOpt
    with EitherOps    with RepLattices    with RepMonads
    with SMTBaseOps   with SMTBitVecOps   with SMTArrayOps {
    // with SMTStagedOps {
  type Typ[T] = Manifest[T]
  def typ[T: Typ] = manifest[T]
  def manifestTyp[T: Typ] = manifest[T]

  override def __fun[T: Manifest](f: AnyRef, arity: Int, gf: List[Backend.Exp] => Backend.Exp, captures: Backend.Exp*): Backend.Exp = {
    // No λforward
    val can = canonicalize(f)
    Adapter.funTable.find(_._2 == can) match {
      case Some((funSym, _)) => funSym
      case _ =>
        val fn = Backend.Sym(Adapter.g.fresh)
        Adapter.funTable = (fn, can) :: Adapter.funTable
        val block = Adapter.g.reify(arity, gf)
        val res = Adapter.g.reflect(fn, "λ", (block+:captures):_*)(hardSummary(fn))
        Adapter.funTable = Adapter.funTable.map {
          case (fn2, can2) => if (can == can2) (fn, can) else (fn2, can2)
        }
        res
    }
  }

  type CloseFun[A, B] = Rep[B] => Rep[A => B]
  def close[A: Manifest, B: Manifest, M[_]](f: Rep[A] => M[Rep[B]], k: (CloseFun[A, B], M[Rep[B]]) => M[Rep[B]]): M[Rep[B]] = {
    val g = Adapter.g

    val s: Backend.Sym = Backend.Sym(g.fresh)
    val x: Rep[A] = Wrap[A](s)

    val save = g.curBlock
    val saveEffects = g.curEffects
    val saveLocalDefs = g.curLocalDefs
    val saveLocalReads = g.curLocalReads
    val saveLocalWrites = g.curLocalWrites
    val saveReifyHere = g.reifyHere

    val here = false
    g.curBlock = Backend.Sym(g.fresh)
    g.curEffects = Backend.BlockEffect(collection.immutable.Map(), if (here) g.curEffects else null)
    g.reifyHere = here
    g.curLocalDefs = collection.immutable.Set()
    g.curLocalReads = collection.immutable.Set()
    g.curLocalWrites = collection.immutable.Set()

    val m: M[Rep[B]] = f(x)

    val curBlock = g.curBlock
    val curEffects = g.curEffects
    val curLocalDefs = g.curLocalDefs
    val curLocalReads = g.curLocalReads
    val curLocalWrites = g.curLocalWrites
    val curReifyHere = g.reifyHere

    def closefun(rb: Rep[B]): Rep[A => B] = {
      val fn = Backend.Sym(Adapter.g.fresh)
      val res = Unwrap(rb)
      val block: Block = {
        val reads = curLocalReads.filterNot(curLocalDefs)
        val writes = curLocalWrites.filterNot(curLocalDefs)
        var hard = writes.map(curEffects.map(_)._1)
        if (curEffects.map.contains(res)) // if res is a local mutable (e.g. Array)
          hard += curEffects.map(res)._1
        if (hard.isEmpty)
          hard = collection.immutable.Set(curBlock)
        Block(collection.immutable.List(s), res, curBlock, EffectSummary(collection.immutable.Set(), hard, reads, writes))
      }

      g.curBlock = save
      g.curEffects = saveEffects
      g.curLocalDefs = saveLocalDefs
      g.curLocalReads = saveLocalReads
      g.curLocalWrites = saveLocalWrites
      g.reifyHere = saveReifyHere

      val ret = Adapter.g.reflect(fn, "λ", block)(hardSummary(fn))
      Wrap[A => B](ret)
    }

    k(closefun, m)
  }

}
