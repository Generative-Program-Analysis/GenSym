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
      Wrap[List[A]](Adapter.g.reflect("list-new", xs.map(Unwrap(_)):_*))
  }

  implicit def __liftConstList[A: Manifest](xs: List[A]): ListOps[A] = new ListOps(unit(xs))
  implicit def __liftVarList[A: Manifest](xs: Var[List[A]]): ListOps[A] = new ListOps(readVar(xs))

  implicit class ListOps[A: Manifest](xs: Rep[List[A]]) {
    def apply(i: Rep[Int]): Rep[A] = Wrap[A](Adapter.g.reflect("list-apply", Unwrap(xs), Unwrap(i)))
    def head: Rep[A] = Wrap[A](Adapter.g.reflect("list-head", Unwrap(xs)))
    def tail: Rep[List[A]] = Wrap[List[A]](Adapter.g.reflect("list-tail", Unwrap(xs)))
    def isEmpty: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("list-isEmpty", Unwrap(xs)))
    def take(i: Rep[Int]) = Wrap[List[A]](Adapter.g.reflect("list-take", Unwrap(xs), Unwrap(i)))
    def ::(x: Rep[A]): Rep[List[A]] =
      Wrap[List[A]](Adapter.g.reflect("list-prepend", Unwrap(xs), Unwrap(x)))
    def ++[B >: A : Manifest](ys: Rep[List[B]]): Rep[List[B]] =
      Wrap[List[A]](Adapter.g.reflect("lsit-concat", Unwrap(xs), Unwrap(ys)))
    def mkString: Rep[String] = mkString(unit(""))
    def mkString(sep: Rep[String]): Rep[String] =
      Wrap[String](Adapter.g.reflect("list-mkString", Unwrap(xs), Unwrap(sep)))
    def toArray: Rep[Array[A]] = Wrap[Array[A]](Adapter.g.reflect("list-toArray", Unwrap(xs)))
    def toSeq: Rep[Seq[A]] = Wrap[Seq[A]](Adapter.g.reflect("list-toSeq", Unwrap(xs)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): Rep[List[B]] = {
      // TODO: for those HO functions, what if it has side-effects? any special treatment?
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[B]](Adapter.g.reflect("list-map", Unwrap(xs), block))
    }
    def flatMap[B: Manifest](f: Rep[A] => Rep[List[B]]): Rep[List[B]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[B]](Adapter.g.reflect("list-flatMap", Unwrap(xs), block))
    }
    def foldLeft[B: Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B]): Rep[B] = {
      val block = Adapter.g.reify((x, y) => Unwrap(f(Wrap[B](x), Wrap[A](y))))
      Wrap[B](Adapter.g.reflect("list-foldLeft", Unwrap(xs), Unwrap(z), block))
    }
    def zip[B: Manifest](ys: Rep[List[B]]): Rep[List[(A, B)]] =
      Wrap[List[(A, B)]](Adapter.g.reflect("list-zip", Unwrap(xs), Unwrap(ys)))
    def filter(f: Rep[A] => Rep[Boolean]): Rep[List[A]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[A]](Adapter.g.reflect("list-filter", Unwrap(xs), block))
    }
    def withFilter(f: Rep[A] => Rep[Boolean]): Rep[List[A]] = filter(f)
    // TODO: what if the Ordering of B not exists at the next stage?
    def sortBy[B: Manifest : Ordering](f: Rep[A] => Rep[B]): Rep[List[A]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[A]](Adapter.g.reflect("list-sortBy", Unwrap(xs), block))
    }
    def containsSlice[B <: A : Manifest](ys: Rep[List[B]]): Rep[Boolean] =
      Wrap[Boolean](Adapter.g.reflect("list-containsSlice", Unwrap(xs), Unwrap(ys)))
    def intersect[B >: A : Manifest](ys: Rep[List[B]]): Rep[List[A]] =
      Wrap[List[A]](Adapter.g.reflect("list-intersect", Unwrap(xs), Unwrap(ys)))
  }

  /*
    def foldLeftPair[B:Manifest,C:Manifest](z: Rep[(B,C)])(f: ((Rep[B], Rep[C]), Rep[A]) => Rep[(B,C)])
  */
}

trait ScalaCodeGen_List extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.List") {
      val kty = m.typeArguments(0)
      s"List[${remap(kty)}]"
    } else { super.remap(m) }
  }

  // TODO: is there any other ways to prevent inlining?
  override def mayInline(n: Node): Boolean = n match {
    //case Node(_, "list-new", _, _) => false
    case Node(_, "list-map", _, _) => false
    case Node(_, "list-flatMap", _, _) => false
    case Node(_, "list-foldLeft", _, _) => false
    case Node(_, "list-take", _, _) => false
    case Node(_, "list-prepend", _, _) => false
    case Node(_, "list-concat", _, _) => false
    case Node(_, "list-zip", _, _) => false
    case Node(_, "list-sortBy", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "list-new", xs, _) =>
      val ty = remap(typeMap.get(s).map(_.typeArguments(0)).getOrElse(manifest[Unknown]))
      emit("List[")
      emit(ty)
      emit("](")
      xs.zipWithIndex.map { case (x, i) =>
        shallow(x)
        if (i != xs.length-1) emit(", ")
      }
      emit(")")
    case Node(s, "list-apply", List(xs, i), _) =>
      shallow(xs); emit("("); shallow(i); emit(")")
    case Node(s, "list-head", List(xs), _) =>
      shallow(xs); emit(".head")
    case Node(s, "list-tail", List(xs), _) =>
      shallow(xs); emit(".tail")
    case Node(s, "list-isEmpty", List(xs), _) =>
      shallow(xs); emit(".isEmpty")
    case Node(s, "list-take", List(xs, i), _) =>
      shallow(xs); emit(".take("); shallow(i); emit(")")
    case Node(s, "list-prepend", List(xs, x), _) =>
      shallow(x); emit("::"); shallow(xs)
    case Node(s, "list-concat", List(xs, ys), _) =>
      shallow(xs); emit("++"); shallow(ys)
    case Node(s, "list-mkString", List(xs, Const("")), _) =>
      shallow(xs); emit(".mkString")
    case Node(s, "list-mkString", List(xs, sep), _) =>
      shallow(xs); emit(".mkString("); shallow(sep); emit(")")
    case Node(s, "list-toArray", List(xs), _) =>
      shallow(xs); emit(".toArray")
    case Node(s, "list-toSeq", List(xs), _) =>
      shallow(xs); emit(".toSeq")
    case Node(s, "list-map", List(xs, b), _) =>
      shallow(xs); emit(".map("); shallow(b); emit(")")
    case Node(s, "list-flatMap", List(xs, b), _) =>
      shallow(xs); emit(".flatMap("); shallow(b); emit(")")
    case Node(s, "list-foldLeft", List(xs, z, b), _) =>
      shallow(xs); emit(".foldLeft("); shallow(z); emit(")("); shallow(b, false); emit(")")
    case Node(s, "list-zip", List(xs, ys), _) =>
      shallow(xs); emit(".zip("); shallow(ys); emit(")")
    case Node(s, "list-filter", List(xs, b), _) =>
      shallow(xs); emit(".filter("); shallow(b); emit(")")
    case Node(s, "list-sortBy", List(xs, b), _) =>
      shallow(xs); emit(".sortBy("); shallow(b); emit(")")
    case Node(s, "list-containsSlice", List(xs, ys), _) =>
      shallow(xs); emit(".containsSlice("); shallow(ys); emit(")")
    case Node(s, "list-intersect", List(xs, ys), _) =>
      shallow(xs); emit(".intersect("); shallow(ys); emit(")")
    case _ => super.shallow(n)
  }

  //TODO: what should be added here?
  override def traverse(n: Node): Unit = n match {
    case _ => super.traverse(n)
  }

  //TODO: symFreq
  override def symsFreq(n: Node): Set[(Def, Double)] = n match {
    case _ => super.symsFreq(n)
  }
}
