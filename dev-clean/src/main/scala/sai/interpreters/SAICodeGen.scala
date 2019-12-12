package sai
package lmsx

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SAICodeGenBase extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Function2") {
      val fst = m.typeArguments(0)
      val snd = m.typeArguments(1)
      val ret = m.typeArguments(2)
      s"Function2[${remap(fst)}, ${remap(snd)}, ${remap(ret)}]"
    } else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "λ", _, _) ⇒ false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::(Backend.Const("val"))::Nil, _) =>
      val types = b.in.map { a => remap(typeMap.getOrElse(a, manifest[Unknown])) }
      val typesStr = types.map(_.toString).mkString(", ")
      val retType = remap(typeMap.getOrElse(b.res, manifest[Unit]))
      val eff = quoteEff(b.ein)
      //emitln(s"var ${quote(f)} : (($typesStr) => $retType $eff) = null.asInstanceOf[(($typesStr) => $retType)]")
      emit(s"val ${quote(f)} : (($typesStr) => $retType $eff) = ")
      val pattern = PTuple(b.in.map(PVar(_)))
      quoteCaseBlock(b, pattern)
      emitln()
    case _ => super.traverse(n)
  }
}
