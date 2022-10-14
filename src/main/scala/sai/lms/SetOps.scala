package gensym
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
    def apply[A: Manifest](xs: Rep[A]*)(implicit pos: SourceContext) = {
      val mA = Backend.Const(manifest[A])
      val unwrapped_xs = Seq(mA) ++ xs.map(Unwrap)
      Wrap[Set[A]](Adapter.g.reflect("set-new", unwrapped_xs:_*))
    }
  }

  implicit def __liftConstSet[A: Manifest](xs: Set[A]): SetOps[A] = new SetOps(xs)
  implicit def __liftVarSet[A: Manifest](xs: Var[Set[A]]): SetOps[A] = new SetOps(readVar(xs))

  implicit class SetOps[A: Manifest](s: Rep[Set[A]]) {
    def apply(a: Rep[A]): Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("set-apply", Unwrap(s), Unwrap(a)))
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("set-size", Unwrap(s)))
    def isEmpty: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("set-isEmpty", Unwrap(s)))
    def head: Rep[A] = Wrap[A](Adapter.g.reflect("set-head", Unwrap(s)))
    def tail: Rep[Set[A]] = Wrap[Set[A]](Adapter.g.reflect("set-tail", Unwrap(s)))
    def toList: Rep[List[A]] = Wrap[List[A]](Adapter.g.reflect("set-toList", Unwrap(s)))
    def +(x: Rep[A]): Rep[Set[A]] = Wrap[Set[A]](Adapter.g.reflect("set-+", Unwrap(s), Unwrap(x)))
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

trait SetOpsOpt extends SetOps { b: Base =>
  implicit override def __liftConstSet[A: Manifest](xs: Set[A]): SetOps[A] = new SetOpsOpt(unit(xs))
  implicit override def __liftVarSet[A: Manifest](xs: Var[Set[A]]): SetOps[A] = new SetOpsOpt(readVar(xs))

  implicit class SetOpsOpt[A: Manifest](xs: Rep[Set[A]]) extends SetOps[A](xs) {
    override def ++(ys: Rep[Set[A]]): Rep[Set[A]] = (Unwrap(xs), Unwrap(ys)) match {
      case (Adapter.g.Def("set-new", mA::(xs: List[Backend.Exp])),
            Adapter.g.Def("set-new",  _::(ys: List[Backend.Exp]))) =>
        val unwrapped_xsys = Seq(mA) ++ xs ++ ys
        Wrap[Set[A]](Adapter.g.reflect("set-new", unwrapped_xsys:_*))
      case (Adapter.g.Def("set-new", mA::(xs: List[Backend.Exp])), _) if xs.isEmpty =>
        ys
      case (_, Adapter.g.Def("set-new", mA::(ys: List[Backend.Exp]))) if ys.isEmpty =>
        xs
      case _ => super.++(ys)
    }
    override def foldLeft[B: Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B]): Rep[B] =
      Unwrap(xs) match {
        case Adapter.g.Def("set-new", mA::(xs: List[Backend.Exp])) =>
          xs.map(Wrap[A](_)).foldLeft(z)(f)
        case _ => super.foldLeft(z)(f)
      }
  }
}

trait ScalaCodeGen_Set extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.Set") {
      val kty = m.typeArguments(0)
      s"Set[${remap(kty)}]"
    } else { super.remap(m) }
  }

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
    case Node(s, "set-new", Const(mA: Manifest[_])::xs, _) =>
      val ty = remap(mA)
      emit("Set["); emit(ty); emit("](")
      if (!xs.isEmpty) {
        shallow(xs.head)
        xs.tail.map { x =>
          emit(", ")
          shallow(x)
        }
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
    case Node(_, "set-++", List(s1, x), _) =>
      shallow(s1); emit(" + "); shallow(x)
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


trait CppCodeGen_Set extends ExtendedCCodeGen {
  override def remap(m: Manifest[_]): String = {
    //Note: assuming the backend already uses namespace "immer::".
    val ns = "";
    if (m.runtimeClass.getName == "scala.collection.immutable.Set") {
      val kty = m.typeArguments(0)
      s"${ns}set<${remap(kty)}>"
    } else { super.remap(m) }
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("set-") => false
      /*
    case Node(_, "set-++", _, _) => false
    case Node(_, "set-intersect", _, _) => false
    case Node(_, "set-union", _, _) => false
    case Node(_, "set-subsetOf", _, _) => false
    case Node(_, "set-map", _, _) => false
    case Node(_, "set-foldLeft", _, _) => false
    case Node(_, "set-filter", _, _) => false
       */
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "set-new", Const(mA: Manifest[_])::xs, _) =>
      val ty = remap(mA)
      emit("Set::make_set<"); emit(ty); emit(">({")
      if (!xs.isEmpty) {
        shallow(xs.head)
        xs.tail.map { x =>
          emit(", ")
          shallow(x)
        }
      }
      emit("})")
    case Node(_, "set-apply", List(s, x), _) =>
      emit("Set::contains(")
      shallow(s); emit(", ")
      shallow(x); emit(")")
    case Node(_, "set-size", List(s), _) =>
      shallow(s); emit(".size()")
    case Node(_, "set-isEmpty", List(s), _) =>
      shallow(s); emit(".empty()")
    case Node(_, "set-head", List(s), _) =>
      ???
    case Node(_, "set-tail", List(s), _) =>
      ???
    case Node(_, "set-toList", List(s), _) =>
      emit("set_to_list("); shallow(s); emit(")")
    case Node(_, "set-+", List(s1, x), _) =>
      es"$s1.insert($x)"
    case Node(_, "set-++", List(s1, s2), _) =>
      es"Set::join($s1, $s2)"
    case Node(_, "set-intersect", List(s1, s2), _) =>
      es"Set::intersect($s1, $s2)"
    case Node(_, "set-union", List(s1, s2), _) =>
      es"Set::join($s1, $s2)"
    case Node(_, "set-subsetOf", List(s1, s2), _) =>
      es"Set::subsetOf($s1, $s2)"
    case Node(_, "set-map", List(s, b: Block), _) =>
      val tty = remap(typeBlockRes(b.res))
      es"Set::map<$tty>($s, $b)"
    case Node(_, "set-foldLeft", List(s, z, b: Block), _) =>
      val tty = remap(typeBlockRes(b.res))
      es"Set::foldLeft<$tty>($s, $z, $b)"
    case Node(_, "set-filter", List(s, b), _) =>
      es"Set::filter($s, $b)"
    case _ => super.shallow(n)
  }
}
