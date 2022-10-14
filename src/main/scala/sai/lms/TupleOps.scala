package gensym.lmsx

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
      (Unwrap(a), Unwrap(b)) match {
        case (Adapter.g.Def("tuple-1", List(x: Backend.Exp)),
              Adapter.g.Def("tuple-2", List(y: Backend.Exp))) if x == y =>
          Wrap[Tuple2[A, B]](x)
        case _ =>
          Wrap[Tuple2[A, B]](Adapter.g.reflect("tuple2-new", Unwrap(a), Unwrap(b)))
      }
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
    def _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple-1", Unwrap(t)))
    def _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple-2", Unwrap(t)))
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
    def _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple-1", Unwrap(t)))
    def _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple-2", Unwrap(t)))
    def _3: Rep[C] = Wrap[C](Adapter.g.reflect("tuple-3", Unwrap(t)))
    def unliftLeft: ((Rep[A], Rep[B]), Rep[C]) =
      ((this._1, this._2), this._3)
    def unliftRight: (Rep[A], (Rep[B], Rep[C])) =
      (this._1, (this._2, this._3))
  }

  // Tuple 4

  object Tuple4 {
    def apply[A: Manifest, B: Manifest, C: Manifest, D: Manifest](a: Rep[A], b: Rep[B], c: Rep[C], d: Rep[D]) =
      Wrap[Tuple4[A, B, C, D]](Adapter.g.reflect("tuple4-new", Unwrap(a), Unwrap(b), Unwrap(c), Unwrap(d)))
  }

  implicit def __liftTuple4RepAll[A: Manifest, B: Manifest, C: Manifest, D: Manifest](t: (Rep[A], Rep[B], Rep[C], Rep[D])) =
    Tuple4[A, B, C, D](t._1, t._2, t._3, t._4)
  implicit def __liftTuple4[A: Manifest, B: Manifest, C: Manifest, D: Manifest](t: (A, B, C, D)) =
    Tuple4[A, B, C, D](unit(t._1), unit(t._2), unit(t._3), unit(t._4))

  implicit class Tuple4Ops[A: Manifest, B: Manifest, C: Manifest, D: Manifest](t: Rep[(A, B, C, D)]) {
    def _1: Rep[A] = Wrap[A](Adapter.g.reflect("tuple-1", Unwrap(t)))
    def _2: Rep[B] = Wrap[B](Adapter.g.reflect("tuple-2", Unwrap(t)))
    def _3: Rep[C] = Wrap[C](Adapter.g.reflect("tuple-3", Unwrap(t)))
    def _4: Rep[D] = Wrap[D](Adapter.g.reflect("tuple-4", Unwrap(t)))
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
      case _ => super._1
    }
    override def _2: Rep[B] = Unwrap(t) match {
      case Adapter.g.Def("tuple3-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp)) =>
        Wrap[B](t2)
      case _ => super._2
    }
    override def _3: Rep[C] = Unwrap(t) match {
      case Adapter.g.Def("tuple3-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp)) =>
        Wrap[C](t3)
      case _ => super._3
    }
  }

  implicit class Tuple4OpsOpt[A: Manifest, B: Manifest, C: Manifest, D: Manifest](t: Rep[(A, B, C, D)]) extends Tuple4Ops[A, B, C, D](t) {
    override def _1: Rep[A] = Unwrap(t) match {
      case Adapter.g.Def("tuple4-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp, t4: Backend.Exp)) =>
        Wrap[A](t1)
      case _ => super._1
    }
    override def _2: Rep[B] = Unwrap(t) match {
      case Adapter.g.Def("tuple4-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp, t4: Backend.Exp)) =>
        Wrap[B](t2)
      case _ => super._2
    }
    override def _3: Rep[C] = Unwrap(t) match {
      case Adapter.g.Def("tuple4-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp, t4: Backend.Exp)) =>
        Wrap[C](t3)
      case _ => super._3
    }
    override def _4: Rep[D] = Unwrap(t) match {
      case Adapter.g.Def("tuple4-new", List(t1: Backend.Exp, t2: Backend.Exp, t3: Backend.Exp, t4: Backend.Exp)) =>
        Wrap[D](t4)
      case _ => super._4
    }
  }
}

trait ScalaCodeGen_Tuple extends ExtendedScalaCodeGen {
  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Tuple2") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      s"Tuple2[$t0, $t1]"
    } else if (typeStr == "scala.Tuple3") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      val t2 = remap(m.typeArguments(2))
      s"Tuple3[$t0, $t1, $t2]"
    } else if (typeStr == "scala.Tuple4") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      val t2 = remap(m.typeArguments(2))
      val t3 = remap(m.typeArguments(3))
      s"Tuple4[$t0, $t1, $t2, $t3]"
    } else {
      super.remap(m)
    }
  }

  override def quote(s: Def): String = s match {
    case Const(t: Tuple2[_, _]) =>
      "(" + quote(Const(t._1)) + ", " + quote(Const(t._2)) + ")"
    case Const(t: Tuple3[_, _, _]) =>
      "(" + quote(Const(t._1)) + ", " + quote(Const(t._2)) + ", " + quote(Const(t._3)) + ")"
    case Const(t: Tuple4[_, _, _, _]) =>
      "(" + quote(Const(t._1)) + ", " + quote(Const(t._2)) + ", " + quote(Const(t._3)) + ", " + quote(Const(t._4)) + ")"
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    // Projections
    case Node(s, "tuple-1", List(t), _) => es"$t._1"
    case Node(s, "tuple-2", List(t), _) => es"$t._2"
    case Node(s, "tuple-3", List(t), _) => es"$t._3"
    case Node(s, "tuple-4", List(t), _) => es"$t._4"
    // Tuple2
    case Node(s, "tuple2-new", List(t1, t2), _) => es"($t1, $t2)"
    case Node(s, "tuple2-swap", List(t), _) => es"$t.swap"
    // Tuple3
    case Node(s, "tuple3-new", List(t1, t2, t3), _) => es"($t1, $t2, $t3)"
    // Tuple4
    case Node(s, "tuple4-new", List(t1, t2, t3, t4), _) => es"($t1, $t2, $t3, $t4)"
    case _ => super.shallow(n)
  }
}

trait CppCodeGen_Tuple extends ExtendedCCodeGen {
  registerHeader("<tuple>")

  override def remap(m: Manifest[_]): String = {
    val typeStr = m.runtimeClass.getName
    if (typeStr == "scala.Tuple2") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      s"std::pair<$t0, $t1>"
    } else if (typeStr == "scala.Tuple3") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      val t2 = remap(m.typeArguments(2))
      s"std::tuple<$t0, $t1, $t2>"
    } else if (typeStr == "scala.Tuple4") {
      val t0 = remap(m.typeArguments(0))
      val t1 = remap(m.typeArguments(1))
      val t2 = remap(m.typeArguments(2))
      val t3 = remap(m.typeArguments(3))
      s"std::tuple<$t0, $t1, $t2, $t3>"
    } else {
      super.remap(m)
    }
  }

  override def quote(s: Def): String = s match {
    case Const(t: Tuple2[_, _]) =>
      val t1 = quote(Const(t._1))
      val t2 = quote(Const(t._2))
      s"std::make_pair($t1, $t2)"
    case Const(t: Tuple3[_, _, _]) =>
      val t1 = quote(Const(t._1))
      val t2 = quote(Const(t._2))
      val t3 = quote(Const(t._3))
      s"std::make_tuple($t1, $t2, $t3)"
    case Const(t: Tuple4[_, _, _, _]) =>
      val t1 = quote(Const(t._1))
      val t2 = quote(Const(t._2))
      val t3 = quote(Const(t._3))
      val t4 = quote(Const(t._4))
      s"std::make_tuple($t1, $t2, $t3, $t4)"
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    // Projections
    case Node(s, "tuple-1", List(t), _) => es"std::get<0>($t)"
    case Node(s, "tuple-2", List(t), _) => es"std::get<1>($t)"
    case Node(s, "tuple-3", List(t), _) => es"std::get<2>($t)"
    case Node(s, "tuple-4", List(t), _) => es"std::get<3>($t)"
    // Tuple2
    case Node(s, "tuple2-new", List(t1, t2), _) => es"std::make_pair($t1, $t2)"
    case Node(s, "tuple2-swap", List(t), _) => es"Pair::swap($t)"
    // Tuple3
    case Node(s, "tuple3-new", List(t1, t2, t3), _) => es"{$t1, $t2, $t3}"
    // Tuple4
    case Node(s, "tuple4-new", List(t1, t2, t3, t4), _) => es"{$t1, $t2, $t3, $t4}"
    case _ => super.shallow(n)
  }
}
