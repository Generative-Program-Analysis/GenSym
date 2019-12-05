package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait MapOps { b: Base =>
  object Map {
    def apply[K: Manifest, V: Manifest](kvs: (Rep[K], Rep[V])*)(implicit pos: SourceContext) = {
      val kvs_* = kvs.map { case (k, v) =>
        Wrap[(K, V)](Adapter.g.reflect("tuple2-new", Unwrap(k), Unwrap(v)))
      }
      Wrap[Map[K, V]](Adapter.g.reflect("map-new", kvs_*.map(Unwrap):_*))
    }
    def empty[K: Manifest, V: Manifest](implicit pos: SourceContext) = {
      Wrap[Map[K, V]](Adapter.g.reflect("map-new"))
    }
  }

  implicit def __liftConstMap[K: Manifest, V: Manifest](m: Map[K, V]): MapOps[K, V] = new MapOps(m)
  implicit def __liftVarMap[K: Manifest, V: Manifest](m: Var[Map[K, V]]): MapOps[K, V] = new MapOps(readVar(m))

  implicit class MapOps[K: Manifest, V: Manifest](m: Rep[Map[K, V]]) {
    def apply(k: Rep[K]): Rep[V] = Wrap[V](Adapter.g.reflect("map-apply", Unwrap(m), Unwrap(k)))
    def contains(k: Rep[K]): Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("map-contains", Unwrap(m), Unwrap(k)))
    def get(k: Rep[K]): Rep[Option[V]] = Wrap[Option[V]](Adapter.g.reflect("map-get", Unwrap(m), Unwrap(k)))
    def getOrElse(k: Rep[K], default: Rep[V]): Rep[V] =
      Wrap[V](Adapter.g.reflect("map-getOrElse", Unwrap(m), Unwrap(k), Unwrap(default)))
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("map-size", Unwrap(m)))
    def +(kv: Rep[(K, V)]): Rep[Map[K, V]] =
      Wrap[Map[K, V]](Adapter.g.reflect("map-+", Unwrap(m), Unwrap(kv)))
    def ++(m1: Rep[Map[K, V]]): Rep[Map[K, V]] =
      Wrap[Map[K, V]](Adapter.g.reflect("map-++", Unwrap(m), Unwrap(m1)))
    def ===(m1: Rep[Map[K, V]]): Rep[Boolean] =
      Wrap[Boolean](Adapter.g.reflect("map-===", Unwrap(m), Unwrap(m1)))
    def keySet: Rep[Set[K]] = Wrap[Set[K]](Adapter.g.reflect("map-keySet", Unwrap(m)))
    def isEmpty: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("map-isEmpty", Unwrap(m)))
    def foldLeft[B: Manifest](z: Rep[B])(f: (Rep[B], (Rep[K], Rep[V])) => Rep[B]) = {
      val block = Adapter.g.reify(3, syms =>
        Unwrap(f(Wrap[B](syms(0)), (Wrap[K](syms(1)), Wrap[V](syms(2))))))
      Wrap[B](Adapter.g.reflect("map-foldLeft", Unwrap(m), Unwrap(z), block))
    }
    def foreach(f: ((Rep[K], Rep[V])) => Rep[Unit]): Rep[Unit] = {
      val block = Adapter.g.reify(2, syms => Unwrap(f(Wrap[K](syms(0)), Wrap[V](syms(1)))))
      Wrap[Unit](Adapter.g.reflect("map-foreach", Unwrap(m), block))
    }
    def filter(f: ((Rep[K], Rep[V])) => Rep[Boolean]): Rep[Map[K, V]] = {
      val block = Adapter.g.reify(2, syms => Unwrap(f(Wrap[K](syms(0)), Wrap[V](syms(1)))))
      Wrap[Map[K, V]](Adapter.g.reflect("map-filter", Unwrap(m), block))
    }
    def map[A: Manifest](f: ((Rep[K], Rep[V])) => Rep[A]): Rep[List[A]] = {
      val block = Adapter.g.reify(2, syms => Unwrap(f(Wrap[K](syms(0)), Wrap[V](syms(1)))))
      Wrap[List[A]](Adapter.g.reflect("map-map", Unwrap(m), block))
    }
    def map[K1: Manifest, V1: Manifest](f: ((Rep[K], Rep[V])) => Rep[(K1, V1)]): Rep[Map[K1,V1]] = {
      val block = Adapter.g.reify(2, syms => Unwrap(f(Wrap[K](syms(0)), Wrap[V](syms(1)))))
      Wrap[Map[K1, V1]](Adapter.g.reflect("map-mapmap", Unwrap(m), block))
    }
  }
}

trait ScalaCodeGen_Map extends ExtendedScalaCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "map-new", _, _) => false
    case Node(_, "map-+", _, _) => false
    case Node(_, "map-++", _, _) => false
    case Node(_, "map-getOrElse", _, _) => false
    case Node(_, "map-foldLeft", _, _) => false
    case Node(_, "map-foreach", _, _) => false
    case Node(_, "map-filter", _, _) => false
    case Node(_, "map-map", _, _) => false
    case Node(_, "map-mapmap", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "map-new", kvs, _) =>
      val kty = remap(typeMap.get(s).map(_.typeArguments(0)).getOrElse(manifest[Unknown]))
      val vty = remap(typeMap.get(s).map(_.typeArguments(1)).getOrElse(manifest[Unknown]))
      emit("Map[")
      emit(kty); emit(", "); emit(vty)
      emit("](")
      kvs.zipWithIndex.map { case (kv, i) =>
        shallow(kv)
        if (i != kvs.length-1) emit(", ")
      }
      emit(")")
    case Node(s, "map-apply", List(m, k), _) =>
      shallow(m); emit("("); shallow(k); emit(")")
    case Node(s, "map-contains", List(m, k), _) =>
      shallow(m); emit(".contains("); shallow(k); emit(")")
    case Node(s, "map-get", List(m, k), _) =>
      shallow(m); emit(".get("); shallow(k); emit(")")
    case Node(s, "map-getOrElse", List(m, k, d), _) =>
      shallow(m); emit(".getOrElse("); shallow(k); emit(", "); shallow(d); emit(")")
    case Node(s, "map-size", List(m), _) =>
      shallow(m); emit(".size");
    case Node(s, "map-+", List(m, kv), _) =>
      shallow(m); emit(" + ("); shallow(kv); emit(")")
    case Node(s, "map-++", List(m1, m2), _) =>
      shallow(m1); emit(" ++ "); shallow(m2)
    case Node(s, "map-keySet", List(m), _) =>
      shallow(m); emit(".keySet");
    case Node(s, "map-isEmpty", List(m), _) =>
      shallow(m); emit(".isEmpty");
    case Node(s, "map-foldLeft", List(m, z, b: Block), _) =>
      val p = PTuple(List(PVar(b.in(0)), PTuple(List(PVar(b.in(1)), PVar(b.in(2))))))
      shallow(m); emit(".foldLeft(")
      shallow(z); emit(") ")
      quoteCaseBlock(b, p) //Note: quoteBlock will emit `{` and `}`
    case Node(s, "map-foreach", List(m, b: Block), _) =>
      val p = PTuple(b.in.map(PVar(_)))
      shallow(m); emit(".foreach "); quoteCaseBlock(b, p)
    case Node(s, "map-filter", List(m, b: Block), _) =>
      val p = PTuple(b.in.map(PVar(_)))
      shallow(m); emit(".filter "); quoteCaseBlock(b, p)
    case Node(s, "map-map", List(m, b: Block), _) =>
      val p = PTuple(b.in.map(PVar(_)))
      shallow(m); emit(".map "); quoteCaseBlock(b, p)
    case Node(s, "map-mapmap", List(m, b: Block), _) =>
      val p = PTuple(b.in.map(PVar(_)))
      shallow(m); emit(".map "); quoteCaseBlock(b, p)
    case _ => super.shallow(n)
  }
}
