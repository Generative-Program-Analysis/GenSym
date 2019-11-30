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
  }

  implicit def __liftConstMap[K: Manifest, V: Manifest](m: Map[K, V]): MapOps[K, V] = new MapOps(m)

  implicit class MapOps[K: Manifest, V: Manifest](m: Rep[Map[K, V]]) {
    def apply(k: Rep[K]): Rep[V] = Wrap[V](Adapter.g.reflect("map-apply", Unwrap(m), Unwrap(k)))
  }
}

trait ScalaCodeGen_Map extends ExtendedScalaCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "map-new", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "map-new", kvs, _) =>
      emit("Map(")
      kvs.zipWithIndex.map { case (kv, i) =>
        shallow(kv)
        if (i != kvs.length-1) emit(", ")
      }
      emit(")")
    case Node(s, "map-apply", List(m, k), _) =>
      shallow(m); emit("("); shallow(k); emit(")")
    case _ => super.shallow(n)
  }
}
