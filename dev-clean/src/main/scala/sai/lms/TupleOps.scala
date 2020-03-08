package sai.lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

trait TupleOps { b: Base =>
  // Tuple2
  object Tuple2 {
    def apply[A: Manifest, B: Manifest](a: Rep[A], b: Rep[B]) = {
      Wrap[Tuple2[A, B]](Adapter.g.reflect("tuple2-new", Unwrap(a), Unwrap(b)))
    }
  }

  implicit def __liftTuple2Rep[A: Manifest, B: Manifest](t: (Rep[A], Rep[B])) =
    Tuple2[A, B](t._1, t._2)
  implicit def __liftTuple2RepLhs[A: Manifest, B: Manifest](t: (A, Rep[B])) =
    Tuple2[A, B](unit[A](t._1), t._2)
  implicit def __liftTuple2RepRhs[A: Manifest, B: Manifest](t: (Rep[A], B)) =
    Tuple2[A, B](t._1, unit[B](t._2))
  implicit def __liftTuple2[A: Manifest, B: Manifest](t: (A, B)) =
    Tuple2[A, B](unit[A](t._1), unit[B](t._2))

  implicit class Tuple2Ops[A: Manifest, B: Manifest](t: Rep[(A, B)]) {
    def _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple2-1", Unwrap(t)))
    def _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple2-2", Unwrap(t)))
    def swap: Rep[(B, A)] = Wrap[(B, A)](Adapter.g.reflect("tuple2-swap", Unwrap(t)))
    def unlift: (Rep[A], Rep[B]) = (this._1, this._2)
  }

  // Tuple3

  object Tuple3 {
    def apply[A: Manifest, B: Manifest, C: Manifest](a: Rep[A], b: Rep[B], c: Rep[C]) =
      Wrap[Tuple3[A, B, C]](Adapter.g.reflect("tuple3-new", Unwrap(a), Unwrap(b), Unwrap(c)))
  }

  implicit def __liftTuple3RepAll[A: Manifest, B: Manifest, C: Manifest](t: (Rep[A], Rep[B], Rep[C])) =
    Tuple3[A, B, C](t._1, t._2, t._3)
  implicit def __liftTuple3RepFst[A: Manifest, B: Manifest, C: Manifest](t: (A, Rep[B], Rep[C])) =
    Tuple3[A, B, C](unit(t._1), t._2, t._3)
  implicit def __liftTuple3RepSnd[A: Manifest, B: Manifest, C: Manifest](t: (Rep[A], B, Rep[C])) =
    Tuple3[A, B, C](t._1, unit(t._2), t._3)
  implicit def __liftTuple3RepTrd[A: Manifest, B: Manifest, C: Manifest](t: (Rep[A], Rep[B], C)) =
    Tuple3[A, B, C](t._1, t._2, unit(t._3))
  implicit def __liftTuple3RepFstSnd[A: Manifest, B: Manifest, C: Manifest](t: (A, B, Rep[C])) =
    Tuple3[A, B, C](unit(t._1), unit(t._2), t._3)
  implicit def __liftTuple3RepFstTrd[A: Manifest, B: Manifest, C: Manifest](t: (A, Rep[B], C)) =
    Tuple3[A, B, C](unit(t._1), t._2, unit(t._3))
  implicit def __liftTuple3RepSndTrd[A: Manifest, B: Manifest, C: Manifest](t: (Rep[A], B, C)) =
    Tuple3[A, B, C](t._1, unit(t._2), unit(t._3))
  implicit def __liftTuple3[A: Manifest, B: Manifest, C: Manifest](t: (A, B, C)) =
    Tuple3[A, B, C](unit(t._1), unit(t._2), unit(t._3))

  implicit class Tuple3Ops[A: Manifest, B: Manifest, C: Manifest](t: Rep[(A, B, C)]) {
    def _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple3-1", Unwrap(t)))
    def _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple3-2", Unwrap(t)))
    def _3: Rep[C] = Wrap[C](Adapter.g.reflect("tuple3-3", Unwrap(t)))
    def unliftLeft: ((Rep[A], Rep[B]), Rep[C]) =
      ((this._1, this._2), this._3)
    def unliftRight: (Rep[A], (Rep[B], Rep[C])) =
      (this._1, (this._2, this._3))
  }
}

trait TupleOpsOpt extends TupleOps { b: Base =>
  implicit class Tuple2OpsOpt[A: Manifest, B: Manifest](t: Rep[(A, B)]) extends Tuple2Ops[A, B](t) {
    override def _1: Rep[A] = Unwrap(t) match {
      case Adapter.g.Def("tuple2-new", List(t1: Backend.Exp, t2: Backend.Exp)) => Wrap[A](t1)
      case _ => super._1
    }
    override def _2: Rep[B] = Unwrap(t) match {
      case Adapter.g.Def("tuple2-new", List(t1: Backend.Exp, t2: Backend.Exp)) => Wrap[B](t2)
      case _ => super._2
    }
  }

  implicit class Tuple3OpsOpt[A: Manifest, B: Manifest, C: Manifest](t: Rep[(A, B, C)]) extends Tuple3Ops[A, B, C](t) {
    override def _1: Rep[A] = Unwrap(t) match {
      case Adapter.g.Def("tuple3-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp)) =>
        Wrap[A](t1)
      case _ => Wrap[A](Adapter.g.reflect("tuple3-1", Unwrap(t)))
    }
    override def _2: Rep[B] = Unwrap(t) match {
      case Adapter.g.Def("tuple3-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp)) =>
        Wrap[B](t2)
      case _ => Wrap[B](Adapter.g.reflect("tuple3-2", Unwrap(t)))
    }
    override def _3: Rep[C] = Unwrap(t) match {
      case Adapter.g.Def("tuple3-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp)) =>
        Wrap[C](t3)
      case _ => Wrap[C](Adapter.g.reflect("tuple3-3", Unwrap(t)))
    }
  }
}

trait ScalaCodeGen_Tuple extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Tuple2") {
      val fst = m.typeArguments(0)
      val snd = m.typeArguments(1)
      s"Tuple2[${remap(fst)}, ${remap(snd)}]"
    } else if (typeStr == "scala.Tuple3") {
      val fst = m.typeArguments(0)
      val snd = m.typeArguments(1)
      val thd = m.typeArguments(2)
      s"Tuple3[${remap(fst)}, ${remap(snd)}, ${remap(thd)}]"
    } else {
      super.remap(m)
    }
  }

  override def quote(s: Def): String = s match {
    case Const(t: Tuple2[_, _]) =>
      "(" + quote(Const(t._1)) + ", " + quote(Const(t._2)) + ")"
    case Const(t: Tuple3[_, _, _]) =>
      "(" + quote(Const(t._1)) + ", " + quote(Const(t._2)) + ", " + quote(Const(t._3)) + ")"
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    // Tuple2
    case Node(s, "tuple2-new", List(fst, snd), _) =>
      emit("("); shallow(fst); emit(", "); shallow(snd); emit(")")
    case Node(s, "tuple2-1", List(t), _) =>
      shallow(t); emit("._1")
    case Node(s, "tuple2-2", List(t), _) =>
      shallow(t); emit("._2")
    case Node(s, "tuple2-swap", List(t), _) =>
      shallow(t); emit(".swap")
    // Tuple3
    case Node(s, "tuple3-new", List(fst, snd, trd), _) =>
      emit("(")
      shallow(fst); emit(", ")
      shallow(snd); emit(", ")
      shallow(trd); emit(")")
    case Node(s, "tuple3-1", List(t), _) =>
      shallow(t); emit("._1")
    case Node(s, "tuple3-2", List(t), _) =>
      shallow(t); emit("._2")
    case Node(s, "tuple3-3", List(t), _) =>
      shallow(t); emit("._3")
    case _ => super.shallow(n)
  }
}

trait CppCodeGen_Tuple extends ExtendedCCodeGen {
  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Tuple2") {
      val fst = remap(m.typeArguments(0))
      val snd = remap(m.typeArguments(1))
      s"std::tuple<$fst, $snd>"
    } else if (typeStr == "scala.Typle3") {
      val fst = remap(m.typeArguments(0))
      val snd = remap(m.typeArguments(1))
      val thd = remap(m.typeArguments(2))
      s"std::tuple<$fst, $snd, $thd>"
    } else {
      super.remap(m)
    }
  }

  override def quote(s: Def): String = s match {
    case Const(t: Tuple2[_, _]) =>
      val fst = quote(Const(t._1)) 
      val snd = quote(Const(t._2)) 
      s"std::make_tuple($fst, $snd)"
    case Const(t: Tuple3[_, _, _]) =>
      val fst = quote(Const(t._1)) 
      val snd = quote(Const(t._2))
      val thd = quote(Const(t._3))
      s"std::make_tuple($fst, $snd, $thd)"
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    // Tuple2
    case Node(s, "tuple2-new", List(fst, snd), _) =>
      emit("std::make_tuple("); shallow(fst); emit(", "); shallow(snd); emit(")")
    case Node(s, "tuple2-1", List(t), _) =>
      emit("std::get<0>("); shallow(t); emit(")")
    case Node(s, "tuple2-2", List(t), _) =>
      emit("std::get<1>("); shallow(t); emit(")")
    case Node(s, "tuple2-swap", List(t), _) =>
      emit("tuple_swap("); shallow(t); emit(")")
    // Tuple3
    case Node(s, "tuple3-new", List(fst, snd, trd), _) =>
      emit("std::make_tuple(")
      shallow(fst); emit(", ")
      shallow(snd); emit(", ")
      shallow(trd); emit(")")
    case Node(s, "tuple3-1", List(t), _) =>
      emit("std::get<0>("); shallow(t); emit(")")
    case Node(s, "tuple3-2", List(t), _) =>
      emit("std::get<1>("); shallow(t); emit(")")
    case Node(s, "tuple3-3", List(t), _) =>
      emit("std::get<2>("); shallow(t); emit(")")
    case _ => super.shallow(n)
  }

}
