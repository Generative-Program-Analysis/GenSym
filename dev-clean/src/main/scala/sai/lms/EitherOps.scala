package sai.lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait EitherOps { b: Base =>
  object Either {
    def left[A: Manifest, B: Manifest](a: Rep[A]) = {
      val mA = Backend.Const(manifest[A])
      val mB = Backend.Const(manifest[B])
      val unwrapped_xs = Seq(mA, mB, Unwrap(a))
      Wrap[Either[A, B]](Adapter.g.reflect("either-new-left", unwrapped_xs:_*))
    }
    def right[A: Manifest, B: Manifest](b: Rep[B]) = {
      val mA = Backend.Const(manifest[A])
      val mB = Backend.Const(manifest[B])
      val unwrapped_xs = Seq(mA, mB, Unwrap(b))
      Wrap[Either[A, B]](Adapter.g.reflect("either-new-right", unwrapped_xs:_*))
    }
  }

  implicit def __liftConstEither[A: Manifest, B: Manifest](e: Either[A, B]): EitherOps[A, B] =
    new EitherOps(unit(e))

  implicit class EitherOps[A: Manifest, B: Manifest](e: Rep[Either[A, B]]) {
    def isLeft: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("either-isLeft", Unwrap(e)))
    def isRight: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("either-isRight", Unwrap(e)))
    def left: Rep[Left[A, B]] = Wrap[Left[A, B]](Adapter.g.reflect("either-left", Unwrap(e)))
    def right: Rep[Right[A, B]] = Wrap[Right[A, B]](Adapter.g.reflect("either-right", Unwrap(e)))
  }

  implicit class LeftOps[A: Manifest, B: Manifest](l: Rep[Left[A, B]]) {
    def value: Rep[A] = Wrap[A](Adapter.g.reflect("left-value", Unwrap(l)))
  }

  implicit class RightOps[A: Manifest, B: Manifest](r: Rep[Right[A, B]]) {
    def value: Rep[B] = Wrap[B](Adapter.g.reflect("right-value", Unwrap(r)))
  }
}

trait ScalaCodeGen_Either extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName == "scala.util.Either") {
      val aty = m.typeArguments(0)
      val bty = m.typeArguments(1)
      s"Either[${remap(aty)}, ${remap(bty)}]"
    } else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "either-new-left", _, _) => false
    case Node(_, "either-new-right", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "either-new-left", Const(mA: Manifest[_])::Const(mB: Manifest[_])::a::Nil, _) =>
      val aty = remap(mA)
      val bty = remap(mB)
      emit("Left[")
      emit(aty); emit(", "); emit(bty); emit("](")
      shallow(a)
      emit(")")
    case Node(s, "either-new-right", Const(mA: Manifest[_])::Const(mB: Manifest[_])::b::Nil, _) =>
      val aty = remap(mA)
      val bty = remap(mB)
      emit("Right[")
      emit(aty); emit(", "); emit(bty); emit("](")
      shallow(b)
      emit(")")
    case Node(s, "either-isLeft", List(e), _) =>
      shallow(e); emit(".isLeft")
    case Node(s, "either-isRight", List(e), _) =>
      shallow(e); emit(".isRight")
    case Node(s, "either-left", List(e), _) =>
      shallow(e); emit(".left")
    case Node(s, "either-right", List(e), _) =>
      shallow(e); emit(".right")
    case Node(s, "left-value", List(l), _) =>
      shallow(l); emit(".value")
    case Node(s, "right-value", List(r), _) =>
      shallow(r); emit(".value")
    case _ => super.shallow(n)
  }

}
