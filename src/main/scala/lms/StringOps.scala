package gensym
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait StringOps { b: Base =>
  // object String {
  //   // TODO: use reflect instead of reflectUnsafe when hardtopfun has support for variable dependencies <2022-05-26, David Deng> //
  //   def apply(str: String): Rep[String] = Wrap[String](Adapter.g.reflect("string-new", Unwrap(unit[String](str))))
  // }

  implicit class StringOpsExt(str: Rep[String]) {
    // def substring(s: Rep[Int], e: Rep[Int]): Rep[String] =
    //   Wrap[String](Adapter.g.reflect("string-substring", Unwrap(str), Unwrap(s), Unwrap(e)))
    // def length: Rep[Int] = Wrap[Int](Adapter.g.reflect("string-length", Unwrap(str)))
    def split(delim: Rep[String]): Rep[List[String]] =
      Wrap[List[String]](Adapter.g.reflect("string-split", Unwrap(str), Unwrap(delim)))
    def +(other: Rep[String]): Rep[String] =
      Wrap[String](Adapter.g.reflect("string-concat", Unwrap(str), Unwrap(other)))
    def ==(other: Rep[String]): Rep[Boolean] =
      Wrap[Boolean](Adapter.g.reflect("string-equal", Unwrap(str), Unwrap(other)))
  }
}
trait ScalaCodeGen_String extends ExtendedScalaCodeGen {
}

trait CppCodeGen_String extends ExtendedCPPCodeGen {
  registerHeader("headers", "<gensym/immeralgo.hpp>")
  registerHeader("<string>")

  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.String") {
      "String"
    } else { super.remap(m) }
  }

  override def mayInline(n: Node): Boolean = n match {
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    // case Node(s, "string-new", str::Nil, _) => emit("String("); shallow(str); emit(")")
    case Node(s, "String.slice", str::st::e::Nil, _) => es"${str}.substr(${st}, ${e} - ${st})" // semantics in C++?
    case Node(s, "String.length", List(str), _) => es"${str}.length()"
    case Node(s, "String.charAt", str::index::Nil, _) => es"${str}[${index}]"
    case Node(s, "string-split", str::delim::Nil, _) => es"Str::split(${str}, ${delim})"
    case Node(s, "string-concat", str::other::Nil, _) => es"${str} + ${other}"
    case Node(s, "string-equal", str::other::Nil, _) => es"${str} == ${other}"
    case _ => super.shallow(n)
  }
}
