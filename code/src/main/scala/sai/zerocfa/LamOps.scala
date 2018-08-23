package sai.zerocfa

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

import sai.parser.cps._

trait LamOps extends Base with ListOps with StringOps with Variables {
  implicit def repToLamOps(l: Rep[Lam]) = new LamOpsCls(l)

  class LamOpsCls(l: Rep[Lam]) {
    def vars = lam_vars(l)
  }

  def lam_vars(l: Rep[Lam])(implicit pos: SourceContext): Rep[List[String]]
}

trait LamOpsExp extends BaseExp with LamOps with ListOpsExp with StringOpsExp with VariablesExp {
  implicit def LamTyp: Typ[Lam] = manifestTyp

  case class LamVars(l: Exp[Lam]) extends Def[List[String]]

  def lam_vars (l: Exp[Lam])(implicit pos: SourceContext): Exp[List[String]] = LamVars(l)
}

trait ScalaGenLamOps extends GenericNestedCodegen with ScalaGenEffect {
  val IR: LamOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case LamVars(lam) => emitValDef(sym, src"$lam.vars")
    case _ => super.emitNode(sym, rhs)
  }
}
