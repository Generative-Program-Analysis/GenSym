package sai.lang

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._

import scala.collection.immutable.{List => SList}

object WhileLang {
  sealed trait Stmt
  case class Skip() extends Stmt
  case class Assign(x: String, e: Expr) extends Stmt
  case class Cond(e: Expr, thn: Stmt, els: Stmt) extends Stmt
  case class Seq(s1: Stmt, s2: Stmt) extends Stmt
  case class While(b: Expr, s: Stmt) extends Stmt

  sealed trait Expr
  case class Lit(x: Any) extends Expr
  case class Var(x: String) extends Expr
  case class Op1(op: String, e: Expr) extends Expr
  case class Op2(op: String, e1: Expr, e2: Expr) extends Expr

  object Examples {
    val fact5 =
      Seq(Assign("i", Lit(1)),
          Seq(Assign("fact", Lit(1)),
              While(Op2("<=", Var("i"), Lit(5)),
                    Seq(Assign("fact", Op2("*", Var("fact"), Var("i"))),
                      Assign("i", Op2("+", Var("i"), Lit(1)))))))

    val cond1 =
      Cond(Op2("<=", Lit(1), Lit(2)),
        Assign("x", Lit(3)),
        Assign("x", Lit(4)))

    val cond2 =
      Seq(Cond(Op2("<=", Var("x"), Var("y")),
               Assign("z", Var("x")),
               Assign("z", Var("y"))),
          Assign("z", Op2("+", Var("z"), Lit(1))))

  }
}

import sai.structure.monad._

object WhileSemantics {
  import WhileLang._
  import CpsM._

  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value

  type Ans = Value
  type Store = Map[String, Value]
  type M[T] = CpsM[Ans, T]

  val M = Monad[M]
  import M._

  def eval(e: Expr, σ: Store): Value = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val IntV(i) = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val IntV(i1) = eval(e1, σ)
      val IntV(i2) = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def fix[A, B](f: (A => B) => A => B): A => B = a => f(fix(f))(a)

  def exec(s: Stmt)(σ: Store): M[Store] = s match {
    case Skip() => pure(σ)
    case Assign(x, e) => pure(σ + (x → eval(e, σ)))
    case Cond(e, s1, s2) =>
      val BoolV(b) = eval(e, σ)
      if (b) exec(s1)(σ) else exec(s2)(σ)
    case Seq(s1, s2) =>
      exec(s1)(σ).flatMap(σ => exec(s2)(σ))
    case While(e, s) => fix((f: Store => M[Store]) => (σ: Store) => {
                              CpsM((k: Store => Ans) => {
                                     val BoolV(b) = eval(e, σ)
                                     if (b) exec(s)(σ)(σ1 => f(σ1)(k)) else k(σ)
                                   })
                            })(σ)
  }
}

@virtualize
trait StagedWhileSemantics extends SAIOps {
  import WhileLang._
  //import CpsM._
  import StateT._

  type Ans = Store
  type Store = Map[String, Value]
  type M[T] = StateT[IdM, Ans, T]
  val M = Monad[M]
  //import M._

  //
  def fix[A: Manifest, B: Manifest](f: Rep[A => B] => Rep[A => B]): Rep[A => B] = {
    def g: Rep[A => B] = fun({ case (a: Rep[A]) => f(g)(a) })
    g
  }
  def power(x: Rep[Int])(f: Rep[Int => Int]): Rep[Int => Int] = fun({ (n: Rep[Int]) =>
    if (n == 0) 1
    else x * f(n - 1)
  })
  def power3: Rep[Int => Int] = fix(power(3))
  //

  trait Value
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def BoolV(b: Rep[Boolean]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))

  implicit def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  implicit def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val i: Rep[Int] = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val i1: Rep[Int] = eval(e1, σ)
      val i2: Rep[Int] = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def get_state: M[Store] = MonadState[M, Store].get
  def update_state(x: String, v: Rep[Value]): M[Unit] =
    MonadState[M, Store].mod(s => s + (unit(x) -> v))
  def lift_state(s: Rep[Store]): M[Unit] =
    MonadState[M, Store].put(s)

  def evalM(e: Expr): M[Value] = for {
    σ <- get_state
  } yield eval(e, σ)

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_state(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      σ <- get_state
      rt <- lift_state(if (cnd) exec(s1)(σ).run._2 else exec(s2)(σ).run._2)
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1); _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      def f: Rep[Store => Store] = fun({ s =>
        val ans = for {
          cnd <- evalM(e)
          σ <- get_state
          rt <- lift_state(if (cnd) f(exec(b)(σ).run._2) else σ)
        } yield ()
        ans(s).run._2
      })
      for {
        σ <- get_state
        σ1 <- lift_state(f(σ))
      } yield ()
  }

  /* 
  //The CPS Monad doesn't work for the While case
  def fix[A, B](f: (Rep[A] => B) => Rep[A] => B): Rep[A] => B = { a =>
    f(fix(f))(a)
  }

  def exec(s: Stmt)(σ: Rep[Store]): M[Store] = s match {
    case Skip() => pure(σ)
    case Assign(x, e) => pure(σ + (x → eval(e, σ)))
    case Cond(e, s1, s2) =>
      val b = rep_bool_proj(eval(e, σ))
      pure(if (b) exec(s1)(σ)(s => s) else exec(s2)(σ)(s => s))
    case Seq(s1, s2) =>
      exec(s1)(σ).flatMap(σ => exec(s2)(σ))
    case While(e, s) =>
      fix((f: Rep[Store] => M[Store]) => (σ: Rep[Store]) => {
        CpsM((k: Rep[Store] => Rep[Ans]) => {
          val b = rep_bool_proj(eval(e, σ))
          if (b) exec(s)(σ)(σ1 => f(σ1)(k)) else k(σ)
        })
      })(σ)
  }
   */
}

object SymRuntime {
  import WhileLang._
  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value
  case class SymV(s: String) extends Value
  case class SymE(op: String, args: List[Value]) extends Value

  type PC = Set[Expr]
  type Store = Map[String, Value]

  def op_2(op: String, v1: Value, v2: Value): Value =
    (op, v1, v2) match {
      case ("+", IntV(i1), IntV(i2)) => IntV(i1 + i2)
      case ("-", IntV(i1), IntV(i2)) => IntV(i1 - i2)
      case ("*", IntV(i1), IntV(i2)) => IntV(i1 * i2)
      case ("/", IntV(i1), IntV(i2)) => IntV(i1 / i2)
      case ("==", IntV(i1), IntV(i2)) => BoolV(i1 == i2)
      case (">=", IntV(i1), IntV(i2)) => BoolV(i1 >= i2)
      case (">", IntV(i1), IntV(i2)) => BoolV(i1 > i2)
      case ("<=", IntV(i1), IntV(i2)) => BoolV(i1 <= i2)
      case ("<", IntV(i1), IntV(i2)) => BoolV(i1 < i2)
      case (op, x1, x2) => SymE(op, List(x1, x2))
    }
}

@virtualize
trait SymStagedWhile extends SAIOps {
  import WhileLang._
  import StateT._
  import ListT._
  import sai.structure.lattices._
  import sai.structure.lattices.Lattices._

  trait Value
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def BoolV(b: Rep[Boolean]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
  def SymV(x: Rep[String]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x)))

  /*
  def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }
   */

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Unwrap(v) match {
      case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
        val v1: Rep[Int] = Wrap[Int](v)
        IntV(-v1)
      case Adapter.g.Def("SymV", scala.collection.immutable.List(v: Backend.Exp)) =>
        val v1: Rep[String] = Wrap[String](v)
        SymV(unit("-" + v1))
      case i =>
        val v1: Rep[Int] = Wrap[Int](Adapter.g.reflect("IntV-proj", i))
        IntV(-v1)
    }
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    Wrap[Value](Adapter.g.reflect("op", Unwrap(unit(op)), Unwrap(v1), Unwrap(v2)))
  }

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type Ans = (Store, PC)
  type M[T] = StateT[ListM, Ans, T] // List[(Unit, (Store, PC))]
  val M = Monad[M]

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val v = eval(e, σ)
      op_neg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      op_2(op, v1, v2)
  }

  def evalM(e: Expr): M[Value] = for {
    σ <- get_store
  } yield eval(e, σ)

  def get_store: M[Store] = for {
    ans <- MonadState[M, Ans].get
  } yield ans._1

  def update_store(x: String, v: Rep[Value]): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    _ <- MonadState[M, Ans].put((ans._1 + (x -> v), ans._2))
  } yield ()

  def br(cnd: Rep[Value], m1: M[Unit], m2: M[Unit])(σ: Rep[Store]): M[Unit] = {
    // TODO: how to merge those states?
    //val s: Rep[Ans] = (σ, Set[Expr]())
    //val res1: Rep[List[(Unit, Ans)]] = m1(s).run
    //val res2: Rep[List[(Unit, Ans)]] = m2(s).run
    //val res = res1 ++ res2
    m1.flatMap(_ => m2)
  }

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_store(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      σ <- get_store
      _ <- br(cnd, exec(s1), exec(s2))(σ)
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1)
      _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      def f: Rep[Ans => Ans] = fun({ s =>
        val ans = for {
          cnd <- evalM(e)
          σ <- get_store
          _ <- br(cnd, exec(b), exec(Skip()))(σ)
        } yield ()
        ???
      })
      ???
  }
}


trait StagedWhileGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) =>
      emit("IntV(")
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[IntV].i")
    case Node(s, "BoolV", List(b), _) =>
      emit("BoolV(")
      shallow(b)
      emit(")")
    case Node(s, "BoolV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[BoolV].b")
    case Node(s, "op", List(op, x1, x2), _) =>
      emit("op_2(")
      shallow(op); emit(", ")
      shallow(x1); emit(", ")
      shallow(x2); emit(")")
    case _ => super.shallow(n)
  }
}

trait StagedWhileDriver extends SAIDriver[Unit, Unit] with StagedWhileSemantics { q =>
  override val codegen = new ScalaGenBase with StagedWhileGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import sai.lang.WhileLang._
trait Value
case class IntV(i: Int) extends Value
case class BoolV(b: Boolean) extends Value
"""
}

trait SymStagedWhileDriver extends SAIDriver[Unit, Unit] with SymStagedWhile { q =>
  override val codegen = new ScalaGenBase with StagedWhileGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }

  override val prelude =
    """
import sai.lang.WhileLang._
import sai.lang.SymRuntime._
"""
}

object TestWhile {
  import WhileLang._
  import WhileLang.Examples._

  @virtualize
  def specialize(e: Expr): SAIDriver[Unit, Unit] = new StagedWhileDriver {
    def snippet(u: Rep[Unit]) = {
      val v = eval(e, Map())
      println(v)
    }
  }

  @virtualize
  def specialize(s: Stmt): SAIDriver[Unit, Unit] = new StagedWhileDriver {
    def snippet(u: Rep[Unit]) = {
      val v = exec(s)(Map("x" -> IntV(3), "z" -> IntV(4))).run
      println(v)
      /*
      val x = power3(2)
      println(x)
      val y = power3(3)
      println(y)
       */
    }
  }

  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] = new SymStagedWhileDriver {
    def snippet(u: Rep[Unit]) = {
      val init: Rep[Ans] = (Map("x" -> IntV(3), "z" -> IntV(4)), Set[Expr]())
      val v = exec(s)(init).run
      println(v)
    }
  }

  def main(args: Array[String]): Unit = {
    //println(exec(fact5)(Map())(σ => σ("fact")))
    //val code = specialize(Op2("+", Lit(1), Lit(2)))

    /*
    val code = specialize(fact5)
    println(code.code)
    code.eval(())
     */

    val code = specSym(cond1)
    println(code.code)
    code.eval(())

  }
}
