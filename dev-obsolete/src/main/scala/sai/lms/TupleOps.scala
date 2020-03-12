package sai
package lms

import scala.virtualization.lms.common._
import scala.virtualization.lms.internal.GenericNestedCodegen
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

trait SAI_ScalaGenTupleOps extends ScalaGenBase with TupleGenBase with ScalaGenStruct {
  val IR: TupleOpsExp
  import IR._

  override def remap[A](m: Manifest[A]) = {
    m.runtimeClass.getSimpleName match {
      case "Tuple2" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple3" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple4" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple5" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case _ => super.remap(m)
    }
  }
}
