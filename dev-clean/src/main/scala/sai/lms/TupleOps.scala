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
    def apply[A: Manifest, B: Manifest](a: Rep[A], b: Rep[B]) =
      Wrap[Tuple2[A, B]](Adapter.g.reflect("tuple2-new", Unwrap(a), Unwrap(b)))
  }

  implicit def __liftTuple2Rep[A: Manifest, B: Manifest](t: (Rep[A], Rep[B])) =
    Tuple2[A, B](t._1, t._2)
  implicit def __liftTuple2RepLhs[A: Manifest, B: Manifest](t: (A, Rep[B])) =
    Tuple2[A, B](unit(t._1), t._2)
  implicit def __liftTuple2RepRhs[A: Manifest, B: Manifest](t: (Rep[A], B)) =
    Tuple2[A, B](t._1, unit(t._2))
  implicit def __liftTuple2[A: Manifest, B: Manifest](t: (A, B)) =
    (unit(t._1), unit(t._2))

  implicit class Tuple2Ops[A: Manifest, B: Manifest](t: Rep[(A, B)]) {
    val _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple2-1", Unwrap(t)))
    val _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple2-2", Unwrap(t)))
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
    (unit(t._1), unit(t._2), unit(t._3))

  implicit class Tuple3Ops[A: Manifest, B: Manifest, C: Manifest](t: Rep[(A, B, C)]) {
    val _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple3-1", Unwrap(t)))
    val _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple3-2", Unwrap(t)))
    val _3: Rep[C] = Wrap[C](Adapter.g.reflect("tuple3-3", Unwrap(t)))
    def unliftLeft: ((Rep[A], Rep[B]), Rep[C]) =
      ((this._1, this._2), this._3)
    def unliftRight: (Rep[A], (Rep[B], Rep[C])) =
      (this._1, (this._2, this._3))
  }
}

trait ScalaCodeGen_Tuple extends ExtendedScalaCodeGen {
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
      emit("("); shallow(fst); emit(", "); shallow(snd); emit(", "); shallow(trd); emit(")")
    case Node(s, "tuple3-1", List(t), _) =>
      shallow(t); emit("._1")
    case Node(s, "tuple3-2", List(t), _) =>
      shallow(t); emit("._2")
    case Node(s, "tuple3-3", List(t), _) =>
      shallow(t); emit("._3")
    case _ => super.shallow(n)
  }
}
