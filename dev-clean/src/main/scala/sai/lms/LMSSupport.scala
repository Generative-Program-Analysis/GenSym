package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext


trait ListOps { b: Base =>
  object List {
    def apply[A: Manifest](xs: Rep[A]*)(implicit pos: SourceContext) =
      Wrap[List[A]](Adapter.g.reflect("List", xs.map(Unwrap(_)):_*))
  }

  implicit def __liftConstList[A: Manifest](xs: List[A]): ListOps[A] = new ListOps(unit(xs))
  implicit def __liftVarList[A: Manifest](xs: Var[List[A]]): ListOps[A] = new ListOps(readVar(xs))

  implicit class ListOps[A: Manifest](xs: Rep[List[A]]) {
    def apply(i: Rep[Int]): Rep[A] = Wrap[A](Adapter.g.reflect("list-apply", Unwrap(xs), Unwrap(i)))
    def map[B: Manifest](f: Rep[A] => Rep[B]) = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[B]](Adapter.g.reflect("list-map", Unwrap(xs), block))
    }
  }

  /*
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
  */
}

trait ScalaCodeGen_List extends ExtendedScalaCodeGen {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "list-apply", List(xs, i), _) =>
      shallow(xs); emit("("); shallow(i); emit(")")
    case Node(s, "list-map", List(xs, b), _) =>
      //FIXME: emit indented code
      shallow(xs); emit(".map("); shallow(b); emit(")")
    case _ => super.shallow(n)
  }

  //TODO: what should be added here?
  override def traverse(n: Node): Unit = n match {
    /*
    case Node(s, "list-apply", List(xs, i), _) =>
      shallow(xs); emit("("); shallow(i); emit(")")
    case Node(s, "list-map", List(xs, f), _) =>
      emit(s"val $s = ${quote(xs)}.map(${quote(f)})")
     */
    case _ => super.traverse(n)
  }
}

trait SAIOps extends Base with PrimitiveOps with LiftPrimitives with Equal
    with OrderingOps with LiftVariables
    with ListOps {
  type Typ[T] = Manifest[T]
  def typ[T: Typ] = manifest[T]
  def manifestTyp[T: Typ] = manifest[T]
}

trait SAIDslImpl extends SAIOps { q =>
  val codegen = new ScalaGenBase with ScalaCodeGen_List {
    val IR: q.type = q
    import IR._
  }
}

abstract class SAISnippet[A:Manifest, B:Manifest] extends SAIOps {
  def wrapper(x: Rep[A]): Rep[B] = snippet(x)
  def snippet(x: Rep[A]): Rep[B]
}

abstract class SAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIDslImpl {
  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource(wrapper, "Snippet",
      new java.io.PrintStream(source))(manifestTyp[A], manifestTyp[B])
    (source.toString, statics)
  }

  lazy val f = {
    val (c1, s1) = (code, statics)
    time("scalac") { Global.sc.compile[A, B]("Snippet", c1, s1) }
  }

  def precompile: Unit = f
  def precompileSilently: Unit = utils.devnull(f)
  def eval(x: A): B = {
    val f1 = f
    time("eval")(f1(x))
  }
}
