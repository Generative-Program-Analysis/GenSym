package sai
package lms

import scala.virtualization.lms.common._
import scala.virtualization.lms.internal.GenericNestedCodegen
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

/* LMS support for immutable Sets. */

trait SetOps extends Base with Variables {
  implicit def setTyp[T:Manifest]: Manifest[Set[T]]

  object Set {
    def apply[A:Manifest](xs: Rep[A]*)(implicit pos: SourceContext) = set_new[A](xs)
  }

  implicit def repSetToSetOps[A:Manifest](v: Rep[Set[A]]) = new SetOpsCls(v)

  class SetOpsCls[A:Manifest](s: Rep[Set[A]]) {
    def ++(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_concat(s, s1)
    def isEmpty()(implicit pos: SourceContext) = set_isEmpty(s)
    def head()(implicit pos: SourceContext) = set_head(s)
    def tail()(implicit pos: SourceContext) = set_tail(s)
    def intersect(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_intersect(s, s1)
    def union(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_union(s, s1)
    def subsetOf(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_subsetof(s, s1)
    def foldLeft[B:Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B])(implicit pos: SourceContext) = set_foldLeft(s, z, f)
  }

  def set_new[A:Manifest](xs: Seq[Rep[A]])(implicit pos: SourceContext): Rep[Set[A]]
  def set_concat[A:Manifest](s1: Rep[Set[A]], s2: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]
  def set_isEmpty[A:Manifest](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[Boolean]
  def set_head[A:Manifest](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[A]
  def set_tail[A:Manifest](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]
  def set_intersect[A:Manifest](s1: Rep[Set[A]], s2: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]
  def set_union[A:Manifest](s1: Rep[Set[A]], s2: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]
  def set_subsetof[A:Manifest](s1: Rep[Set[A]], s2: Rep[Set[A]])(implicit pos: SourceContext): Rep[Boolean]
  def set_foldLeft[A:Manifest,B:Manifest](s: Rep[Set[A]], z: Rep[B], f: (Rep[B], Rep[A]) => Rep[B])(implicit pos: SourceContext): Rep[B]
}

trait SetOpsExp extends SetOps with EffectExp with VariablesExp with BooleanOpsExp with TupledFunctionsExp {
  implicit def setTyp[T:Manifest]: Manifest[Set[T]] = {
    manifest[Set[T]]
  }

  //TODO: syms, boundSyms, symsFreq, mirror

  case class SetNew[A:Manifest](xs: Seq[Exp[A]], mA: Manifest[A]) extends Def[Set[A]]
  case class SetConcat[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]]) extends Def[Set[A]]
  case class SetIsEmpty[A:Manifest](s: Exp[Set[A]]) extends Def[Boolean]
  case class SetHead[A:Manifest](s: Exp[Set[A]]) extends Def[A]
  case class SetTail[A:Manifest](s: Exp[Set[A]]) extends Def[Set[A]]
  case class SetIntersect[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]]) extends Def[Set[A]]
  case class SetUnion[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]]) extends Def[Set[A]]
  case class SetSubsetOf[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]]) extends Def[Boolean]
  case class SetFoldLeft[A:Manifest,B:Manifest](s: Exp[Set[A]], z: Exp[B], acc: Sym[B], x: Sym[A], block: Block[B]) extends Def[B]
  def set_new[A:Manifest](xs: Seq[Exp[A]])(implicit pos: SourceContext) = SetNew(xs, manifest[A])
  def set_concat[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]])(implicit pos: SourceContext) = SetConcat(s1, s2)
  def set_isEmpty[A:Manifest](s: Exp[Set[A]])(implicit pos: SourceContext) = SetIsEmpty(s)
  def set_head[A:Manifest](s: Exp[Set[A]])(implicit pos: SourceContext) = SetHead(s)
  def set_tail[A:Manifest](s: Exp[Set[A]])(implicit pos: SourceContext) = SetTail(s)
  def set_intersect[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]])(implicit pos: SourceContext) = SetIntersect(s1, s2)
  def set_union[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]])(implicit pos: SourceContext) = SetUnion(s1, s2)
  def set_subsetof[A:Manifest](s1: Exp[Set[A]], s2: Exp[Set[A]])(implicit pos: SourceContext) = SetSubsetOf(s1, s2)
  def set_foldLeft[A:Manifest,B:Manifest](s: Exp[Set[A]], z: Exp[B], f: (Rep[B], Rep[A]) => Rep[B])(implicit pos: SourceContext) = {
    val acc = fresh[B]
    val x = fresh[A]
    val b = reifyEffects(f(acc, x))
    reflectEffect(SetFoldLeft(s, z, acc, x, b), summarizeEffects(b).star)
  }

  override def syms(e: Any): List[Sym[Any]] = e match {
    case SetFoldLeft(s, z, acc, x, b) => syms(s) ::: syms(z) ::: syms(b)
    case _ => super.syms(e)
  }
  override def boundSyms(e: Any): List[Sym[Any]] = e match {
    case SetFoldLeft(s, z, acc, x, b) => acc :: x :: effectSyms(b)
    case _ => super.boundSyms(e)
  }
  override def symsFreq(e: Any): List[(Sym[Any], Double)] = e match {
    case SetFoldLeft(s, z, acc, x, b) => freqNormal(s) ::: freqNormal(z) ::: freqNormal(b)
    case _ => super.symsFreq(e)
  }
}

trait BaseGenSetOps extends GenericNestedCodegen {
  val IR: SetOpsExp
  import IR._
}

trait ScalaGenSetOps extends BaseGenSetOps with ScalaGenEffect {
  val IR: SetOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case SetNew(xs, mA) => emitValDef(sym, src"collection.immutable.Set[$mA](" + (xs map {quote}).mkString(",") + ")")
    case SetConcat(s1, s2) => emitValDef(sym, src"$s1 ++ $s2")
    case SetIsEmpty(s) => emitValDef(sym, src"$s.isEmpty")
    case SetHead(s) => emitValDef(sym, src"$s.head")
    case SetTail(s) => emitValDef(sym, src"$s.tail")
    case SetIntersect(s1, s2) => emitValDef(sym, src"$s1.intersect($s2)")
    case SetUnion(s1, s2) => emitValDef(sym, src"$s1.union($s2)")
    case SetSubsetOf(s1, s2) => emitValDef(sym, src"$s1.subsetOf($s2)")
    case SetFoldLeft(s, z, acc, x, blk) =>
      gen"""val $sym = $s.foldLeft ($z) { case ($acc, $x) =>
            |${nestedBlock(blk)}
            |$blk
            |}"""
    case _ => super.emitNode(sym, rhs)
  }
}
