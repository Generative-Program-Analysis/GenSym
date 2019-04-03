package sai
package lms

import scala.virtualization.lms.common.{ListOps => _, ListOpsExp => _, _}
import scala.virtualization.lms.internal.GenericNestedCodegen
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

/* LMS support for immutable Sets. */

trait SetOps extends Base with Variables with ListOps {
  //implicit def setTyp[T:Manifest]: Manifest[Set[T]]

  object Set {
    def apply[A:Manifest](xs: Rep[A]*)(implicit pos: SourceContext) = set_new[A](xs)
  }

  implicit def repSetToSetOps[A:Manifest](v: Rep[Set[A]]) = new SetOpsCls(v)
  implicit def setToSetOps[A:Manifest](v: Set[A]) = new SetOpsCls(unit(v))

  class SetOpsCls[A:Manifest](s: Rep[Set[A]]) {
    def ++(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_concat(s, s1)
    def isEmpty()(implicit pos: SourceContext) = set_isEmpty(s)
    def head()(implicit pos: SourceContext) = set_head(s)
    def tail()(implicit pos: SourceContext) = set_tail(s)
    def intersect(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_intersect(s, s1)
    def union(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_union(s, s1)
    def subsetOf(s1: Rep[Set[A]])(implicit pos: SourceContext) = set_subsetof(s, s1)
    def foldLeft[B:Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B])(implicit pos: SourceContext) = set_foldLeft(s, z, f)
    def foldLeftPair[B:Manifest,C:Manifest](z: Rep[(B,C)])(f: ((Rep[B], Rep[C]), Rep[A]) => Rep[(B,C)]) = set_foldLeftPair(s, z, f)
    def toList()(implicit pos: SourceContext) = set_toList(s)
    def map[B:Manifest](f: Rep[A] => Rep[B]) = set_map(s, f)
    def filter(f: Rep[A] => Rep[Boolean]) = set_filter(s, f)
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
  def set_toList[A:Manifest](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[List[A]]
  def set_foldLeftPair[A:Manifest,B:Manifest,C:Manifest](s: Rep[Set[A]], z: Rep[(B,C)], f: ((Rep[B], Rep[C]), Rep[A]) => Rep[(B,C)])(implicit pos: SourceContext): Rep[(B,C)]
  def set_map[A:Manifest,B:Manifest](s: Rep[Set[A]], f: Rep[A] => Rep[B])(implicit pos: SourceContext): Rep[Set[B]]
  def set_filter[A: Manifest](s: Rep[Set[A]], f: Rep[A] => Rep[Boolean])(implicit pos: SourceContext): Rep[Set[A]]
}

trait SetOpsExp extends SetOps with EffectExp with VariablesExp with BooleanOpsExp with TupledFunctionsExp with ListOpsExp {
  /*
  implicit def setTyp[T:Manifest]: Manifest[Set[T]] = {
    manifest[Set[T]]
  }
  */

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
  case class SetFoldLeftPair[A:Manifest,B:Manifest,C:Manifest](s: Exp[Set[A]], z: Exp[(B,C)], acc1: Sym[B], acc2: Sym[C], x: Sym[A], block: Block[(B,C)]) extends Def[(B,C)]
  case class SetToList[A:Manifest](s: Exp[Set[A]]) extends Def[List[A]]
  case class SetMap[A:Manifest,B:Manifest](s: Exp[Set[A]], x: Sym[A], block: Block[B]) extends Def[Set[B]]
  case class SetFilter[A: Manifest](s: Exp[Set[A]], x: Sym[A], block: Block[Boolean]) extends Def[Set[A]]

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
  def set_foldLeftPair[A:Manifest,B:Manifest,C:Manifest](s: Exp[Set[A]], z: Exp[(B,C)], f: ((Exp[B], Exp[C]), Exp[A]) => Exp[(B,C)])(implicit pos: SourceContext) = {
    val acc1 = fresh[B]
    val acc2 = fresh[C]
    val x = fresh[A]
    val b = reifyEffects(f((acc1, acc2), x))
    reflectEffect(SetFoldLeftPair(s, z, acc1, acc2, x, b), summarizeEffects(b).star)
  }
  def set_toList[A:Manifest](s: Exp[Set[A]])(implicit pos: SourceContext) = SetToList(s)
  def set_map[A:Manifest,B:Manifest](s: Exp[Set[A]], f: Exp[A] => Exp[B])(implicit pos: SourceContext) = {
    val x = fresh[A]
    val b = reifyEffects(f(x))
    reflectEffect(SetMap(s, x, b), summarizeEffects(b).star)
  }
  def set_filter[A:Manifest](s: Exp[Set[A]], f: Exp[A] => Exp[Boolean])(implicit pos: SourceContext) = {
    val x = fresh[A]
    val b = reifyEffects(f(x))
    reflectEffect(SetFilter(s, x, b), summarizeEffects(b).star)
  }

  override def syms(e: Any): List[Sym[Any]] = e match {
    case SetMap(s, x, b) => syms(s) ::: syms(b)
    case SetFilter(s, x, b) => syms(s) ::: syms(b)
    case SetFoldLeft(s, z, acc, x, b) => syms(s) ::: syms(z) ::: syms(b)
    case SetFoldLeftPair(s, z, acc1, acc2, x, b) => syms(s) ::: syms(z) ::: syms(b)
    case _ => super.syms(e)
  }
  override def boundSyms(e: Any): List[Sym[Any]] = e match {
    case SetMap(s, x, b) => x :: effectSyms(b)
    case SetFilter(s, x, b) => x :: effectSyms(b)
    case SetFoldLeft(s, z, acc, x, b) => acc :: x :: effectSyms(b)
    case SetFoldLeftPair(s, z, acc1, acc2, x, b) => acc1 :: acc2 :: x :: effectSyms(b)
    case _ => super.boundSyms(e)
  }
  override def symsFreq(e: Any): List[(Sym[Any], Double)] = e match {
    case SetMap(s, x, b) => freqNormal(s) ::: freqHot(b)
    case SetFilter(s, x, b) => freqNormal(s) ::: freqHot(b)
    case SetFoldLeft(s, z, acc, x, b) => freqNormal(s) ::: freqNormal(z) ::: freqHot(b)
    case SetFoldLeftPair(s, z, acc1, acc2, x, b) => freqNormal(s) ::: freqNormal(z) ::: freqHot(b)
    case _ => super.symsFreq(e)
  }
}

trait SetOpsExpOpt extends SetOpsExp with ListOpsExp {
  override def set_concat[A : Manifest](xs1: Exp[Set[A]], xs2: Exp[Set[A]])(implicit pos: SourceContext): Exp[Set[A]] = (xs1, xs2) match {
    case (Def(SetNew(xs1, t1)), Def(SetNew(xs2, t2))) => SetNew(xs1 ++ xs2, manifest[A])
    case (Def(SetNew(Seq(), _)), xs2) => xs2
    case (xs1, Def(SetNew(Seq(), _))) => xs1
    case _ => super.set_concat(xs1, xs2)
  }

  override def set_foldLeft[A: Manifest, B: Manifest](s: Exp[Set[A]], z: Exp[B], f: (Rep[B], Rep[A]) => Rep[B])(implicit pos: SourceContext) = s match {
    case Def(SetNew(xs, _)) if xs.size == 0 => z
    case Def(SetNew(xs, _)) => xs.foldLeft(z)(f)
    case _ => super.set_foldLeft(s, z, f)
  }

  override def set_foldLeftPair[A: Manifest, B: Manifest, C: Manifest](s: Exp[Set[A]], z: Exp[(B,C)], f: ((Exp[B], Exp[C]), Exp[A]) => Exp[(B,C)])(implicit pos: SourceContext) = s match {
    case Def(SetNew(xs, _)) if xs.size == 0 => z
    case Def(SetNew(xs, _)) if xs.size == 1 => f(z, xs(0))
    // TODO: generalize
    case _ => super.set_foldLeftPair(s, z, f)
  }
  override def set_toList[A:Manifest](s: Exp[Set[A]])(implicit pos: SourceContext) = s match {
    case Def(SetNew(xs, _)) => ListNew(xs, manifest[A])
    case _ => super.set_toList(s)
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
    case SetNew(xs, mA) => emitValDef(sym, src"Set[$mA](" + (xs map {quote}).mkString(",") + ")")
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
    case SetFoldLeftPair(s, z, acc1, acc2, x, blk) =>
      gen"""val $sym = $s.foldLeft ($z) { case (($acc1, $acc2), $x) =>
            |${nestedBlock(blk)}
            |$blk
            |}"""
    case SetToList(s) => emitValDef(sym, src"$s.toList")
    case SetMap(s,x,blk) =>
      gen"""val $sym = $s.map { $x =>
           |${nestedBlock(blk)}
           |$blk
           |}"""
    case SetFilter(s,x,blk) =>
      gen"""val $sym = $s.filter { $x =>
           |${nestedBlock(blk)}
           |$blk
           |}"""
    case _ => super.emitNode(sym, rhs)
  }
}
