package scala.lms.tutorial

import scala.lms.common.Compile
import scala.reflect.SourceContext

trait TrackConditionals extends Dsl {
  var cs1: Set[Rep[Boolean]] = Set.empty
  var cs0: Set[Rep[Boolean]] = Set.empty
  def push1[T](cond: Rep[Boolean], thenp: => Rep[T]): Rep[T] = {
    val save = cs1
    cs1 += cond
    try
      thenp
    finally
      cs1 = save
  }
  def push0[T](cond: Rep[Boolean], elsep: => Rep[T]): Rep[T] = {
    val save = cs0
    cs0 += cond
    try
      elsep
    finally
      cs0 = save
  }
  abstract override def __ifThenElse[T:Typ](cond: Rep[Boolean], thenp: => Rep[T], elsep: => Rep[T])(implicit pos: SourceContext) = {
    if (cs1.contains(cond)) thenp
    else if (cs0.contains(cond)) elsep
    else super.__ifThenElse(cond, push1(cond, thenp), push0(cond, elsep))
  }
}

trait DynVarWarmup extends Dsl {
  def snippet(n: Rep[Int]): Rep[Int] = {
    if (n==0) {
      if (n==0) 0 else 2
    } else 1
  }
}
trait DynVarWarmupDriver extends DslDriver[Int,Int] with DynVarWarmup
class DynVarWarmupTest extends TutorialFunSuite {
  val under = "dynvar"

  test("dynvar warmup without cond tracking") {
    val warmup0 = new DynVarWarmupDriver {}
    check("0", warmup0.code)
  }

  test("dynvar warmup with cond tracking") {
    val warmup1 = new DynVarWarmupDriver with TrackConditionals
    check("1", warmup1.code)
  }
}

