package sai.common

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

import scala.collection.immutable.{Set => ImmSet}

trait ImmSetOps extends Base with Variables {
  implicit def setTyp[T:Typ]: Typ[ImmSet[T]]

  object ImmSet {
    def apply[A:Typ](xs: Rep[A]*)(implicit pos: SourceContext) = immset_new[A](xs)
  }

  implicit def repImmSetToImmSetOps[A:Typ](v: Rep[ImmSet[A]]) = new ImmSetOpsCls(v)

  class ImmSetOpsCls[A:Typ](s: Rep[ImmSet[A]]) {
    def ++(s1: Rep[Set[A]])(implicit pos: SourceContext) = immset_concat(s, s1)
    def isEmpty()(implicit pos: SourceContext) = immset_isEmpty(s)
    def head()(implicit pos: SourceContext) = immset_head(s)
    def tail()(implicit pos: SourceContext) = immset_tail(s)
    def foldLeft[B:Typ](z: Rep[B])(f: Rep[((B, A)) => B])(implicit pos: SourceContext) = immset_foldLeft(s, z, f)
  }

  def immset_new[A:Typ](xs: Seq[Rep[A]])(implicit pos: SourceContext): Rep[ImmSet[A]]

  def immset_concat[A:Typ](s1: Rep[ImmSet[A]], s2: Rep[ImmSet[A]])(implicit pos: SourceContext): Rep[ImmSet[A]]

  def immset_isEmpty[A:Typ](s: Rep[ImmSet[A]])(implicit pos: SourceContext): Rep[Boolean]

  def immset_head[A:Typ](s: Rep[ImmSet[A]])(implicit pos: SourceContext): Rep[A]

  def immset_tail[A:Typ](s: Rep[ImmSet[A]])(implicit pos: SourceContext): Rep[ImmSet[A]]

  def immset_foldLeft[A:Typ,B:Typ](s: Rep[ImmSet[A]], z: Rep[B], f: Rep[((B, A)) => B])(implicit pos: SourceContext): Rep[B]
}

trait ImmSetOpsExp extends ImmSetOps with EffectExp with VariablesExp with BooleanOpsExp with TupledFunctionsExp {
  implicit def setTyp[T:Typ]: Typ[ImmSet[T]] = {
    implicit val ManifestTyp(m) = typ[T]
    manifestTyp
  }

  case class ImmSetNew[A:Typ](xs: Seq[Exp[A]], mA: Typ[A]) extends Def[ImmSet[A]]

  case class ImmSetConcat[A:Typ](s1: Exp[ImmSet[A]], s2: Exp[ImmSet[A]]) extends Def[ImmSet[A]]

  case class ImmSetIsEmpty[A:Typ](s: Exp[ImmSet[A]]) extends Def[Boolean]

  case class ImmSetHead[A:Typ](s: Exp[ImmSet[A]]) extends Def[A]

  case class ImmSetTail[A:Typ](s: Exp[ImmSet[A]]) extends Def[ImmSet[A]]

  case class ImmSetFoldLeft[A:Typ,B:Typ](s: Exp[ImmSet[A]], z: Exp[B], f: Exp[((B, A)) => B]) extends Def[B]

  def immset_new[A:Typ](xs: Seq[Exp[A]])(implicit pos: SourceContext) = ImmSetNew(xs, typ[A])

  def immset_concat[A:Typ](s1: Exp[ImmSet[A]], s2: Exp[ImmSet[A]])(implicit pos: SourceContext) = ImmSetConcat(s1, s2)

  def immset_isEmpty[A:Typ](s: Exp[ImmSet[A]])(implicit pos: SourceContext) = ImmSetIsEmpty(s)

  def immset_head[A:Typ](s: Exp[ImmSet[A]])(implicit pos: SourceContext) = ImmSetHead(s)

  def immset_tail[A:Typ](s: Exp[ImmSet[A]])(implicit pos: SourceContext) = ImmSetTail(s)

  def immset_foldLeft[A:Typ,B:Typ](s: Exp[ImmSet[A]], z: Exp[B], f: Exp[((B, A)) => B])(implicit pos: SourceContext) =
    ImmSetFoldLeft(s, z, f)
}

trait BaseGenListOps extends GenericNestedCodegen {
  val IR: ImmSetOpsExp
  import IR._
}

trait ScalaGenImmSetOps extends BaseGenListOps with ScalaGenEffect {
  val IR: ImmSetOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ImmSetNew(xs, mA) => emitValDef(sym, src"collection.immutable.Set[$mA](" + (xs map {quote}).mkString(",") + ")")
    case ImmSetConcat(s1, s2) => emitValDef(sym, src"$s1 ++ $s2")
    case ImmSetIsEmpty(s) => emitValDef(sym, src"$s.isEmpty")
    case ImmSetHead(s) => emitValDef(sym, src"$s.head")
    case ImmSetTail(s) => emitValDef(sym, src"$s.tail")
    case ImmSetFoldLeft(s, z, f) => emitValDef(sym, src"$s.foldLeft($z)($f)")
    case _ => super.emitNode(sym, rhs)
  }
}
