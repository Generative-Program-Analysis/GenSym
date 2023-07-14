package gensym
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
    def apply[A: Manifest](xs: Rep[A]*)(implicit pos: SourceContext) = {
      val mA = Backend.Const(manifest[A])
      val unwrapped_xs = Seq(mA) ++ xs.map(Unwrap)
      Wrap[List[A]](Adapter.g.reflectMutable("list-new", unwrapped_xs:_*))
    }
    def fill[A: Manifest](x: Rep[Int])(e: Rep[A])(implicit pos: SourceContext) = {
      val mA = Backend.Const(manifest[A])
      Wrap[List[A]](Adapter.g.reflectWrite("list-fill", mA, Unwrap(x), Unwrap(e))(Adapter.CTRL))
    }
  }

  implicit def __liftConstList[A: Manifest](xs: List[A]): ListOps[A] = new ListOps(unit(xs))
  implicit def __liftVarList[A: Manifest](xs: Var[List[A]]): ListOps[A] = new ListOps(readVar(xs))

  implicit class ListOps[A: Manifest](xs: Rep[List[A]]) {
    def apply(i: Rep[Int]): Rep[A] = Wrap[A](Adapter.g.reflect("list-apply", Unwrap(xs), Unwrap(i)))
    def head: Rep[A] = Wrap[A](Adapter.g.reflect("list-head", Unwrap(xs)))
    def tail: Rep[List[A]] = Wrap[List[A]](Adapter.g.reflect("list-tail", Unwrap(xs)))
    def last: Rep[A] = Wrap[A](Adapter.g.reflect("list-last", Unwrap(xs)))
    def size: Rep[Int] = Wrap[Int](Adapter.g.reflect("list-size", Unwrap(xs)))
    def isEmpty: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("list-isEmpty", Unwrap(xs)))
    def take(i: Rep[Int]) = Wrap[List[A]](Adapter.g.reflect("list-take", Unwrap(xs), Unwrap(i)))
    def drop(i: Rep[Int]) = Wrap[List[A]](Adapter.g.reflect("list-drop", Unwrap(xs), Unwrap(i)))
    def updated(i: Rep[Int], x: Rep[A]) = 
      Wrap[List[A]](Adapter.g.reflect("list-updated", Unwrap(xs), Unwrap(i), Unwrap(x)))
    def ::(x: Rep[A]): Rep[List[A]] =
      Wrap[List[A]](Adapter.g.reflect("list-prepend", Unwrap(xs), Unwrap(x)))
    def ++(ys: Rep[List[A]]): Rep[List[A]] =
      Wrap[List[A]](Adapter.g.reflect("list-concat", Unwrap(xs), Unwrap(ys)))
    def mkString: Rep[String] = mkString(unit(""))
    def mkString(sep: Rep[String]): Rep[String] =
      Wrap[String](Adapter.g.reflect("list-mkString", Unwrap(xs), Unwrap(sep)))
    def toArray: Rep[Array[A]] = Wrap[Array[A]](Adapter.g.reflect("list-toArray", Unwrap(xs)))
    def toSeq: Rep[Seq[A]] = Wrap[Seq[A]](Adapter.g.reflect("list-toSeq", Unwrap(xs)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): Rep[List[B]] = {
      val block = Adapter.g.reifyHere(x => Unwrap(f(Wrap[A](x))))
      if (block.isPure) Wrap[List[B]](Adapter.g.reflect("list-map", Unwrap(xs), block))
      else {
        val (rdKeys, wrKeys) = Adapter.g.getEffKeys(block)
        // XXX: add CTRL as write key, due to the lose of alias information... otherwise this map will be DEC-ed.
        Wrap[List[B]](Adapter.g.reflectEffectSummaryHere("list-map", Unwrap(xs), block)((rdKeys, wrKeys + Adapter.CTRL)))
      }
    }
    def flatMap[B: Manifest](f: Rep[A] => Rep[List[B]]): Rep[List[B]] = {
      val block = Adapter.g.reifyHere(x => Unwrap(f(Wrap[A](x))))
      val mA = Backend.Const(manifest[A])
      if (block.isPure) Wrap[List[B]](Adapter.g.reflect("list-flatMap", Unwrap(xs), block, mA))
      else Wrap[List[B]](Adapter.g.reflectEffectSummaryHere("list-flatMap", Unwrap(xs), block, mA)(Adapter.g.getEffKeys(block)))
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
    // TODO correct?
    def foreach(f: Rep[A] => Rep[Unit]): Rep[List[Unit]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[A](x))))
      Wrap[List[Unit]](Adapter.g.reflect("list-foreach", Unwrap(xs), block))
    }

    def toStatic: List[Rep[A]] = Unwrap(xs) match {
      case Adapter.g.Def("list-new",  _::(ys: List[Backend.Exp])) =>
        ys.map(Wrap[A](_))
      case _ => throw new Exception("List is not static")
    }
  }
}

trait ListOpsOpt extends ListOps { b: Base =>
  implicit override def __liftConstList[A: Manifest](xs: List[A]): ListOps[A] = new ListOpsOpt(unit(xs))
  implicit override def __liftVarList[A: Manifest](xs: Var[List[A]]): ListOps[A] = new ListOpsOpt(readVar(xs))

  implicit class ListOpsOpt[A: Manifest](xs: Rep[List[A]]) extends ListOps[A](xs) {
    // TODO: apply
    override def ++(ys: Rep[List[A]]): Rep[List[A]] = (Unwrap(xs), Unwrap(ys)) match {
      case (Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])),
            Adapter.g.Def("list-new",  _::(ys: List[Backend.Exp]))) =>
        val unwrapped_xsys = Seq(mA) ++ xs ++ ys
        Wrap[List[A]](Adapter.g.reflect("list-new", unwrapped_xsys:_*))
      case (Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])), _) if xs.isEmpty =>
        ys
      case (_, Adapter.g.Def("list-new", mA::(ys: List[Backend.Exp]))) if ys.isEmpty =>
        xs
      case _ => super.++(ys)
    }

    override def map[B: Manifest](f: Rep[A] => Rep[B]): Rep[List[B]] =
      Unwrap(xs) match {
        case Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])) if xs.size == 1 =>
          List[B](f(Wrap[A](xs(0))))
        case _ => super.map(f)
      }

    override def flatMap[B: Manifest](f: Rep[A] => Rep[List[B]]): Rep[List[B]] =
      Unwrap(xs) match {
        case Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])) if xs.size == 1 =>
          f(Wrap[A](xs(0)))
        case Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])) if xs.size == 2 =>
          f(Wrap[A](xs(0))) ++ f(Wrap[A](xs(1)))
        case _ => super.flatMap(f)
      }

    override def foldLeft[B: Manifest](z: Rep[B])(f: (Rep[B], Rep[A]) => Rep[B]): Rep[B] =
      Unwrap(xs) match {
        case Adapter.g.Def("list-new", mA::(xs: List[Backend.Exp])) =>
          xs.map(Wrap[A](_)).foldLeft(z)(f)
        case _ =>
          // TODO: simplify this code
          // foldLeft(xs, List(), (acc, x) => acc ++ List(x)) ==> xs
          Unwrap(z) match {
            case Adapter.g.Def("list-new", _::Nil) if manifest[A] == manifest[B].typeArguments(0) =>
              val block = Adapter.g.reify((x, y) => Unwrap(f(Wrap[B](x), Wrap[A](y))))
              block.res match {
                case Adapter.g.Def("list-concat", x1::x2::Nil) =>
                  x2 match {
                    case Adapter.g.Def("list-new", _::x3::Nil) =>
                      //System.out.println(block.in)
                      //System.out.println(x1, x3)
                      if (block.in(0) == x1 && block.in(1) == x3) Wrap[B](Unwrap(xs))
                      else Wrap[B](Adapter.g.reflect("list-foldLeft", Unwrap(xs), Unwrap(z), block))
                    case _ => Wrap[B](Adapter.g.reflect("list-foldLeft", Unwrap(xs), Unwrap(z), block))
                  }
                case _ => Wrap[B](Adapter.g.reflect("list-foldLeft", Unwrap(xs), Unwrap(z), block))
              }
            case _ => super.foldLeft(z)(f)
          }
      }
  }
}

trait ScalaCodeGen_List extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.List") {
      val kty = m.typeArguments(0)
      s"List[${remap(kty)}]"
    } else { super.remap(m) }
  }

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

  override def quote(s: Def): String = s match {
    case Const(xs: List[_]) =>
      "List(" + xs.map(x => quote(Const(x))).mkString(", ") + ")"
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "list-new", Const(mA: Manifest[_])::xs, _) =>
      val ty = remap(mA)
      emit("List[")
      emit(ty)
      emit("](")
      if (!xs.isEmpty) {
        shallow(xs.head)
        xs.tail.map { x =>
          emit(", ")
          shallow(x)
        }
      }
      emit(")")
    case Node(s, "list-apply", List(xs, i), _) =>
      shallow(xs); emit("("); shallow(i); emit(")")
    case Node(s, "list-head", List(xs), _) =>
      shallow(xs); emit(".head")
    case Node(s, "list-tail", List(xs), _) =>
      shallow(xs); emit(".tail")
    case Node(s, "list-last", List(xs), _) =>
      shallow(xs); emit(".last")
    case Node(s, "list-size", List(xs), _) =>
      shallow(xs); emit(".size")
    case Node(s, "list-isEmpty", List(xs), _) =>
      shallow(xs); emit(".isEmpty")
    case Node(s, "list-take", List(xs, i), _) =>
      shallow(xs); emit(".take("); shallow(i); emit(")")
    case Node(s, "list-prepend", List(xs, x), _) =>
      shallow(x); emit(" :: "); shallow(xs)
    case Node(s, "list-concat", List(xs, ys), _) =>
      shallow(xs); emit(" ++ "); shallow(ys)
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
    case Node(s, "list-flatMap", xs::(b: Block)::rest, _) =>
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
}

trait CppCodeGen_List extends ExtendedCCodeGen {
  // Note: using the Immer C++ library for immutable data structures

  registerHeader("third-party/immer", "<immer/flex_vector.hpp>")
  registerHeader("third-party/immer", "<immer/algorithm.hpp>")
  registerHeader("headers", "<gensym/immeralgo.hpp>")

  val ns = "immer::"

  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.collection.immutable.List") {
      val kty = m.typeArguments(0)
      s"${ns}flex_vector<${remap(kty)}>"
    } else { super.remap(m) }
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("list-") => false
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(xs: List[_]) =>
      val mA = Adapter.typeMap(s.asInstanceOf[Backend.Exp])
      val smA = remap(mA)
      smA + "{" + xs.map(x => quote(Const(x))).mkString(", ") + "}"
    case _ => super.quote(s)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "list-foreach", List(xs, f), _) =>
      emit("Vec::foreach(")
      shallow(xs)
      emit(", ")
      shallow(f)
      emit(");")
    case _ => super.traverse(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "list-new", Const(mA: Manifest[_])::xs, _) =>
      emit(s"${ns}flex_vector<")
      emit(remap(mA))
      emit(">")
      emit("{")
      if (!xs.isEmpty) {
        shallow(xs.head)
        xs.tail.map { x =>
          emit(", ")
          shallow(x)
        }
      }
      emit("}")
    case Node(s, "list-fill", List(Const(mA: Manifest[_]), x, e), _) =>
      emit(s"${ns}flex_vector<")
      emit(remap(mA))
      emit(">("); shallow(x); emit(", "); shallow(e); emit(")")
    case Node(s, "list-apply", List(xs, i), _) =>
      shallow(xs); emit(".at("); shallow(i); emit(")")
    case Node(s, "list-head", List(xs), _) =>
      shallow(xs); emit(".front()")
    case Node(s, "list-tail", List(xs), _) =>
      shallow(xs); emit(".drop(1)")
    case Node(s, "list-last", List(xs), _) =>
      shallow(xs); emit(".back()")
    case Node(s, "list-size", List(xs), _) =>
      shallow(xs); emit(".size()")
    case Node(s, "list-isEmpty", List(xs), _) =>
      shallow(xs); emit(".size() == 0")
    case Node(s, "list-take", List(xs, i), _) =>
      shallow(xs); emit(".take("); shallow(i); emit(")")
    case Node(s, "list-drop", List(xs, i), _) =>
      shallow(xs); emit(".drop("); shallow(i); emit(")")
    case Node(s, "list-updated", List(xs, i, x), _) =>
      shallow(xs); emit(".set("); shallow(i); emit(","); shallow(x); emit(")")
    case Node(s, "list-prepend", List(xs, x), _) =>
      shallow(xs); emit(".push_front("); shallow(x); emit(")")
    case Node(s, "list-concat", List(xs, ys), _) =>
      shallow(xs); emit(" + "); shallow(ys)
    case Node(s, "list-mkString", List(xs, Const("")), _) =>
      ???
    case Node(s, "list-mkString", List(xs, sep), _) =>
      ???
    case Node(s, "list-toArray", List(xs), _) =>
      ???
    case Node(s, "list-toSeq", List(xs), _) =>
      ???
    case Node(s, "list-map", List(xs, b: Block), _) =>
      val retType = remap(typeBlockRes(b.res))
      emit(s"Vec::vmap<$retType>(")
      shallow(xs)
      emit(", ")
      shallow(b)
      emit(")")
    case Node(s, "list-flatMap", xs::(b: Block)::Const(mA: Manifest[_])::rest, _) =>
      //val eleType = remap(mA)
      // Note: b.res must return a List type, as required by flatMap
      val retType = remap(typeBlockRes(b.res).typeArguments(0))
      emit(s"Vec::flatMap<$retType>(")
      shallow(xs)
      emit(", ")
      shallow(b)
      emit(")")
    case Node(s, "list-foldLeft", List(xs, z, b), _) =>
      emit("Vec::foldLeft(")
      shallow(xs); emit(", ")
      shallow(z); emit(", ")
      shallow(b); emit(")")
    case Node(s, "list-zip", List(xs, ys), _) =>
      emit("Vec::zip(")
      shallow(xs); emit(", ")
      shallow(ys); emit(")")
    case Node(s, "list-filter", List(xs, b), _) =>
      emit("Vec::filter(")
      shallow(xs); emit(", ")
      shallow(b); emit(")")
    case Node(s, "list-sortBy", List(xs, b), _) =>
      ???
    case Node(s, "list-containsSlice", List(xs, ys), _) =>
      ???
    case Node(s, "list-intersect", List(xs, ys), _) =>
      ???
    case _ => super.shallow(n)
  }
}
