package gensym.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

// Staged interfaces and operations

trait StagedSMTBase extends Base { self: SMTBaseInterface =>
  type BT[+T] = Rep[T]
}

@virtualize
trait SMTBaseOps extends SMTBaseInterface with StagedSMTBase {
  final val BOOL = Backend.Const("bool")

  def boolVar(x: String): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-var", Backend.Const(x), BOOL))
  def lit(b: Boolean): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflectWrite("smt-lit", Backend.Const(b))(Adapter.CTRL))
  def lit(b: Rep[Boolean]): Rep[SMTBool] =
    if (b) lit(true) else lit(false)
  def not(x: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-not", Unwrap(x)))
  def or(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-or", Unwrap(x), Unwrap(y)))
  def and(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-and", Unwrap(x), Unwrap(y)))
  def xor(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-xor", Unwrap(x), Unwrap(y)))
  def iff(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-iff", Unwrap(x), Unwrap(y)))
  def imply(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-imply", Unwrap(x), Unwrap(y)))

  def eq(x: Rep[SMTExpr], y: Rep[SMTExpr]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-eq", Unwrap(x), Unwrap(y)))
  def ite(cnd: Rep[SMTBool], thn: Rep[SMTExpr], els: Rep[SMTExpr]): Rep[SMTExpr] =
    Wrap[SMTBool](Adapter.g.reflect("smt-ite", Unwrap(cnd), Unwrap(thn), Unwrap(els)))

  def push: Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-push")(Adapter.CTRL))
  def pop: Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-pop")(Adapter.CTRL))
  def assert(x: Rep[SMTBool]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-assert", Unwrap(x))(Adapter.CTRL))
  def isValid(x: Rep[SMTBool]): Rep[Boolean] =
    Wrap[Boolean](Adapter.g.reflectWrite("smt-is-valid", Unwrap(x))(Adapter.CTRL))
  def isSat(x: Rep[SMTBool]): Rep[Boolean] =
    Wrap[Boolean](Adapter.g.reflectWrite("smt-is-sat", Unwrap(x))(Adapter.CTRL))
  def query(x: Rep[SMTBool]): Rep[Int] =
    Wrap[Int](Adapter.g.reflectWrite("smt-query", Unwrap(x))(Adapter.CTRL))
  def getCounterEx(x: Rep[SMTExpr]): Rep[SMTExpr] =
    // TODO: what's the expression should be given here?
    Wrap[SMTExpr](Adapter.g.reflectWrite("smt-get-cex", Unwrap(x))(Adapter.CTRL))
  def printCounterEx: Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-print-cex")(Adapter.CTRL))
  def printExpr(x: Rep[SMTExpr]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-print", Unwrap(x))(Adapter.CTRL))

  def handle(n: BT[Int]): BT[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-handle", Unwrap(n))(Adapter.CTRL))
}

trait STPCodeGen_SMTBase extends ExtendedCPPCodeGen {
  //registerHeader("<stp/c_interface.h>")
  //registerHeader("./headers", "<stp_handle.hpp>")
  //registerLibraryPath("../stp/build/lib")
  registerLibrary("-lstp")

  override def remap(m: Manifest[_]): String = {
    val name = m.runtimeClass.getName
    if (name.endsWith("SMTBool")) "Expr"
    else if (name.endsWith("SMTExpr")) "Expr"
    else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("smt-") => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "smt-assert", List(x), _) =>
      emit("vc_assertFormula(vc, "); shallow(x); emitln(");")
    case Node(s, "smt-push", _, _) =>
      emitln("vc_push(vc);")
    case Node(s, "smt-pop", _, _) =>
      emitln("vc_pop(vc);")
    case Node(s, "smt-handle", List(x), _) =>
      emit("handle_query(vc, "); shallow(x); emitln(");")
    case Node(s, "smt-print-cex", _, _) =>
      emitln("vc_printCounterExample(vc);")
    case Node(s, "smt-print", List(x), _) =>
      emit("vc_printExpr(vc, "); shallow(x); emitln(");")
    case _ => super.traverse(n)
  }

  override def shallow(n: Node) = n match {
    case Node(s, "smt-var", Const(id: String)::Const("bool")::Nil, _) =>
      emit(s"""vc_varExpr(vc, \"$id\", vc_boolType(vc))""")
    case Node(s, "smt-lit", Const(true)::Nil, _) =>
      emit("vc_trueExpr(vc)")
    case Node(s, "smt-lit", Const(false)::Nil, _) =>
      emit("vc_falseExpr(vc)")
    case Node(s, "smt-not", List(x), _) =>
      emit("vc_notExpr(vc, "); shallow(x); emit(")")
    case Node(s, "smt-or", List(l, r), _) =>
      emit("vc_orExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-and", List(l, r), _) =>
      emit("vc_andExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-xor", List(l, r), _) =>
      emit("vc_xorExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-iff", List(l, r), _) =>
      emit("vc_iffExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-ite", List(c, t, e), _) =>
      emit("vc_iteExpr(vc, "); shallow(c); emit(", "); shallow(t); emit(", "); shallow(e); emit(")")
    case Node(s, "smt-imply", List(l, r), _) =>
      emit("vc_impliesExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-eq", List(l, r), _) =>
      emit("vc_eqExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-is-valid", List(x), _) =>
      emit("stp_is_valid(vc, "); shallow(x); emit(")")
    case Node(s, "smt-is-sat", List(x), _) =>
      emit("stp_is_sat(vc, "); shallow(x); emit(")")
    case Node(s, "smt-query", List(x), _) =>
      emit("vc_query(vc, "); shallow(x); emit(")")
    case Node(s, "smt-get-cex", List(x), _) =>
      emit("vc_getCounterExample(vc, "); shallow(x); emit(")");
    case _ => super.shallow(n)
  }
}
