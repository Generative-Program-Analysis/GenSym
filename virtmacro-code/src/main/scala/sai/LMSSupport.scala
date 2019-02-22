package sai

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._

trait SAIDsl extends Dsl
    with MapOps
    with SetOps
    with TupleOps
    with UncheckedOps
    with TupledFunctions

trait SAIOpsExp extends DslExp
    with MapOpsExp
    with SetOpsExp
    with TupleOpsExp
    with UncheckedOpsExp
    with TupledFunctionsRecursiveExp
