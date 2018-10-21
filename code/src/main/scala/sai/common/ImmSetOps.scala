package sai.common

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

/* LMS support for immutable Set. */

trait SetOps extends Base with Variables {
  implicit def setTyp[T:Typ]: Typ[Set[T]]

  object Set {
    def apply[A:Typ](xs: Rep[A]*)(implicit pos: SourceContext) = Set_new[A](xs)
  }

  implicit def repSetToSetOps[A:Typ](v: Rep[Set[A]]) = new SetOpsCls(v)

  class SetOpsCls[A:Typ](s: Rep[Set[A]]) {
    def ++(s1: Rep[Set[A]])(implicit pos: SourceContext) = Set_concat(s, s1)
    def isEmpty()(implicit pos: SourceContext) = Set_isEmpty(s)
    def head()(implicit pos: SourceContext) = Set_head(s)
    def tail()(implicit pos: SourceContext) = Set_tail(s)
    def foldLeft[B:Typ](z: Rep[B])(f: Rep[((B, A)) => B])(implicit pos: SourceContext) = Set_foldLeft(s, z, f)
  }

  def Set_new[A:Typ](xs: Seq[Rep[A]])(implicit pos: SourceContext): Rep[Set[A]]

  def Set_concat[A:Typ](s1: Rep[Set[A]], s2: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]

  def Set_isEmpty[A:Typ](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[Boolean]

  def Set_head[A:Typ](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[A]

  def Set_tail[A:Typ](s: Rep[Set[A]])(implicit pos: SourceContext): Rep[Set[A]]

  def Set_foldLeft[A:Typ,B:Typ](s: Rep[Set[A]], z: Rep[B], f: Rep[((B, A)) => B])(implicit pos: SourceContext): Rep[B]
}

trait SetOpsExp extends SetOps with EffectExp with VariablesExp with BooleanOpsExp with TupledFunctionsExp {
  implicit def setTyp[T:Typ]: Typ[Set[T]] = {
    implicit val ManifestTyp(m) = typ[T]
    manifestTyp
  }

  case class SetNew[A:Typ](xs: Seq[Exp[A]], mA: Typ[A]) extends Def[Set[A]]

  case class SetConcat[A:Typ](s1: Exp[Set[A]], s2: Exp[Set[A]]) extends Def[Set[A]]

  case class SetIsEmpty[A:Typ](s: Exp[Set[A]]) extends Def[Boolean]

  case class SetHead[A:Typ](s: Exp[Set[A]]) extends Def[A]

  case class SetTail[A:Typ](s: Exp[Set[A]]) extends Def[Set[A]]

  case class SetFoldLeft[A:Typ,B:Typ](s: Exp[Set[A]], z: Exp[B], f: Exp[((B, A)) => B]) extends Def[B]

  def Set_new[A:Typ](xs: Seq[Exp[A]])(implicit pos: SourceContext) = SetNew(xs, typ[A])

  def Set_concat[A:Typ](s1: Exp[Set[A]], s2: Exp[Set[A]])(implicit pos: SourceContext) = SetConcat(s1, s2)

  def Set_isEmpty[A:Typ](s: Exp[Set[A]])(implicit pos: SourceContext) = SetIsEmpty(s)

  def Set_head[A:Typ](s: Exp[Set[A]])(implicit pos: SourceContext) = SetHead(s)

  def Set_tail[A:Typ](s: Exp[Set[A]])(implicit pos: SourceContext) = SetTail(s)

  def Set_foldLeft[A:Typ,B:Typ](s: Exp[Set[A]], z: Exp[B], f: Exp[((B, A)) => B])(implicit pos: SourceContext) =
    SetFoldLeft(s, z, f)
}

trait BaseGenListOps extends GenericNestedCodegen {
  val IR: SetOpsExp
  import IR._
}

trait ScalaGenSetOps extends BaseGenListOps with ScalaGenEffect {
  val IR: SetOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case SetNew(xs, mA) => emitValDef(sym, src"collection.immutable.Set[$mA](" + (xs map {quote}).mkString(",") + ")")
    case SetConcat(s1, s2) => emitValDef(sym, src"$s1 ++ $s2")
    case SetIsEmpty(s) => emitValDef(sym, src"$s.isEmpty")
    case SetHead(s) => emitValDef(sym, src"$s.head")
    case SetTail(s) => emitValDef(sym, src"$s.tail")
    case SetFoldLeft(s, z, f) => emitValDef(sym, src"$s.foldLeft($z)($f)")
    case _ => super.emitNode(sym, rhs)
  }
}
