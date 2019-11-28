package sai
package lmsx

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize
import lms.core._
import Backend._

trait ListOps { b: Base =>
  object List {
    def apply[A: Manifest](xs: Rep[A]*)(implicit pos: SourceContext) =
      Wrap[List[A]](Adapter.g.reflect("List", xs.map(Unwrap(_)):_*))
  }

  implicit def __liftConstList[A: Manifest](xs: List[A]): ListOps[A] = new ListOps(unit(xs))
  implicit def __liftVarList[A: Manifest](xs: Var[List[A]]): ListOps[A] = new ListOps(readVar(xs))

  implicit class ListOps[A: Manifest](xs: Rep[List[A]]) {
    def apply(i: Rep[Int]): Rep[A] = Wrap[A](Adapter.g.reflect("list_apply", Unwrap(xs), Unwrap(i)))
    def map[B: Manifest](f: Rep[A] => Rep[B]) = Wrap[List[B]](Adapter.g.reflect("list_map", Unwrap(xs), Unwrap(f)))
  }

  /*
  class ListOpsCls[A:Manifest](l: Rep[List[A]]) {
    def map[B:Manifest](f: Rep[A] => Rep[B]) = list_map(l,f)
    def flatMap[B : Manifest](f: Rep[A] => Rep[List[B]]) = list_flatMap(l,f)
    def withFilter(f: Rep[A] => Rep[Boolean]) = list_filter(l, f)
    def filter(f: Rep[A] => Rep[Boolean]) = list_filter(l, f)
    def sortBy[B:Manifest:Ordering](f: Rep[A] => Rep[B]) = list_sortby(l,f)
    def ::(e: Rep[A]) = list_prepend(l,e)
    def ++[B>:A:Manifest] (l2: Rep[List[B]]) = list_concat(l, l2)
    def mkString = list_mkString(l)
    def mkString(s:Rep[String]) = list_mkString2(l,s)
    def head = list_head(l)
    def tail = list_tail(l)
    def isEmpty = list_isEmpty(l)
    def toArray = list_toarray(l)
    def toSeq = list_toseq(l)
    def zip[B:Manifest](rhs: Rep[List[B]]) = list_zip(l, rhs)
    def take(i: Rep[Int]) = list_take(l, i)
    def foldLeft[B:Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B]) = list_foldLeft(l, z, f)
    def foldLeftPair[B:Manifest,C:Manifest](z: Rep[(B,C)])(f: ((Rep[B], Rep[C]), Rep[A]) => Rep[(B,C)]) = list_foldLeftPair(l, z, f)
    def containsSlice(that: Rep[List[A]]) = list_containsSlice(l, that)
    def intersect(that: Rep[List[A]]) = list_intersect(l, that)
  }
  */
}

class ScalaCodeGen_List extends ScalaCodeGen {
  override def traverse(n: Node): Unit = n match {
    case Node(s, "list_apply", List(xs, i), _) =>
      emit(s"val $s = ${quote(xs)}(${quote(i)})")
    case Node(s, "list_map", List(xs, f), _) =>
      emit(s"val $s = ${quote(xs)}.map(${quote(i)})")
    case _ => super.traverse(n)
  }
}

trait SAIDsl extends Dsl

trait SAIOpsExp extends DslExp
