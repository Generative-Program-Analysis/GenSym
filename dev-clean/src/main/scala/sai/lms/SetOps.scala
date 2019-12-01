package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait SetOps { b: Base =>
  object Set {
    def apply[A: Manifest](xs: Rep[A]*)(implicit pos: SourceContext) =
      Wrap[Set[A]](Adapter.g.reflect("set-new", xs.map(Unwrap(_)):_*))
  }

  implicit def liftConstSet[A: Manifest](xs: Set[A]): SetOps[A] = new SetOps(xs)
  implicit def liftVarSet[A: Manifest](xs: Var[Set[A]]): SetOps[A] = new SetOps(readVar(xs))

  implicit class SetOps[A: Manifest](s: Rep[Set[A]]) {
    def apply(a: Rep[A]): Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("set-apply", Unwrap(s), Unwrap(a)))
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("set-size", Unwrap(s)))
    def isEmpty: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("set-isEmpty", Unwrap(s)))
    def head: Rep[A] = Wrap[A](Adapter.g.reflect("set-head", Unwrap(s)))
    def tail: Rep[Set[A]] = Wrap[Set[A]](Adapter.g.reflect("set-tail", Unwrap(s)))
    def toList: Rep[List[A]] = Wrap[List[A]](Adapter.g.reflect("set-toList", Unwrap(s)))
    def ++(s1: Rep[Set[A]]): Rep[Set[A]] = Wrap[Set[A]](Adapter.g.reflect("set-++", Unwrap(s), Unwrap(s1)))
    def intersect(s1: Rep[Set[A]]): Rep[Set[A]] =
      Wrap[Set[A]](Adapter.g.reflect("set-intersect", Unwrap(s), Unwrap(s1)))
    def union(s1: Rep[Set[A]]): Rep[Set[A]] =
      Wrap[Set[A]](Adapter.g.reflect("set-union", Unwrap(s), Unwrap(s1)))
    def subsetOf(s1: Rep[Set[A]]): Rep[Boolean] =
      Wrap[Boolean](Adapter.g.reflect("set-subsetOf", Unwrap(s), Unwrap(s1)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): Rep[Set[B]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[Set[B]](Adapter.g.reflect("set-map", Unwrap(s), block))
    }
    def foldLeft[B: Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B]): Rep[B] = {
      val block = Adapter.g.reify((x, y) => Unwrap(f(Wrap[B](x), Wrap[A](y))))
      Wrap[B](Adapter.g.reflect("set-foldLeft", Unwrap(s), Unwrap(z), block))
    }
    def filter(f: Rep[A] => Rep[Boolean]) = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[Set[A]](Adapter.g.reflect("set-filter", Unwrap(s), block))
    }
  }
}

trait ScalaCodeGen_Set extends ExtendedScalaCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "set-++", _, _) => false
    case Node(_, "set-intersect", _, _) => false
    case Node(_, "set-union", _, _) => false
    case Node(_, "set-subsetOf", _, _) => false
    case Node(_, "set-map", _, _) => false
    case Node(_, "set-foldLeft", _, _) => false
    case Node(_, "set-filter", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "set-new", xs, _) =>
      val ty = remap(typeMap.get(s).map(_.typeArguments(0)).getOrElse(manifest[Unknown]))
      emit("Set["); emit(ty); emit("](")
      xs.zipWithIndex.map { case (x, i) =>
        shallow(x)
        if (i != xs.length-1) emit(", ")
      }
      emit(")")
    case Node(_, "set-apply", List(s, x), _) =>
      shallow(s); emit("("); shallow(x); emit(")")
    case Node(_, "set-size", List(s), _) =>
      shallow(s); emit(".size")
    case Node(_, "set-isEmpty", List(s), _) =>
      shallow(s); emit(".isEmpty")
    case Node(_, "set-head", List(s), _) =>
      shallow(s); emit(".head")
    case Node(_, "set-tail", List(s), _) =>
      shallow(s); emit(".tail")
    case Node(_, "set-toList", List(s), _) =>
      shallow(s); emit(".toList")
    case Node(_, "set-++", List(s1, s2), _) =>
      shallow(s1); emit(" ++ "); shallow(s2)
    case Node(_, "set-intersect", List(s1, s2), _) =>
      shallow(s1); emit(".intersect("); shallow(s2); emit(")")
    case Node(_, "set-union", List(s1, s2), _) =>
      shallow(s1); emit(".union("); shallow(s2); emit(")")
    case Node(_, "set-subsetOf", List(s1, s2), _) =>
      shallow(s1); emit(".subsetOf("); shallow(s2); emit(")")
    case Node(_, "set-map", List(s, b), _) =>
      shallow(s); emit(".map("); shallow(b); emit(")")
    case Node(_, "set-foldLeft", List(s, z, b), _) =>
      shallow(s); emit(".foldLeft("); shallow(z); emit(")("); shallow(b); emit(")")
    case Node(_, "set-filter", List(s, b), _) =>
      shallow(s); emit(".filter("); shallow(b); emit(")")
    case _ => super.shallow(n)
  }
}
