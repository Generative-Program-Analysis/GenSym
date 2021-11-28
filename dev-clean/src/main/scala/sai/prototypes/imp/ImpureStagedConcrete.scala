package sai.imp

import sai.lang.ImpLang._
import lms.core._
import lms.macros._
import lms.core.stub._
import lms.core.Backend._
import sai.lmsx._
import scala.collection.immutable.{List => SList}

// An experimental version that stages/compiles to impure stateful code

@virtualize
trait ImpureStagedImpSemantics extends SAIOps {
  trait Value
  def IntV(i: Rep[Int]): Rep[Value] = "IntV".reflectWith[Value](i)
  def BoolV(b: Rep[Boolean]): Rep[Value] = "BoolV".reflectWith[Value](b)

  implicit def repIntProj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", SList(v: Backend.Exp)) => Wrap[Int](v)
    case _ => "IntV-proj".reflectWith[Int](i)
  }
  implicit def repBoolProj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", SList(v: Backend.Exp)) => Wrap[Boolean](v)
    case _ => "BoolV-proj".reflectWith[Boolean](b)
  }

  trait MutState
  def newMutState(kvs: (String, Rep[Value])*): Rep[MutState] =
    "mutstate-new".reflectMutableWith[MutState](kvs.map({ case (k, v) => __liftTuple2RepLhs(k, v) }):_*)
  implicit class MutStateOps(s: Rep[MutState]) {
    def apply(x: String): Rep[Value] = "mutstate-read".reflectReadWith[Value](s, x)(s)
    def +=(x: String, v: Rep[Value]): Rep[Unit] = "mutstate-update".reflectWriteWith[Unit](s, x, v)(s)
  }
  def dummyRead(s: Rep[MutState]): Rep[Unit] = "mutstate-dummyread".reflectRWWith[Unit]()(s)(Adapter.CTRL)

  def eval(e: Expr, σ: Rep[MutState]): Rep[Value] = e match {
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

  def exec(s: Stmt, σ: Rep[MutState]): Rep[Unit] = s match {
    case Skip() => ()
    case Assign(x, e) => σ += (x, eval(e, σ))
    case Cond(e, s1, s2) => if (eval(e, σ)) exec(s1, σ) else exec(s2, σ)
    case Seq(s1, s2) => exec(s1, σ); exec(s2, σ)
    case While(e, b) => while (eval(e, σ)) exec(b, σ)
  }
}

trait ImpureStagedImpGen extends SAICodeGenBase {
  override def traverse(n: Node): Unit = n match {
    case Node(s, "mutstate-new", kvs, _) =>
      es"val ${quote(s)} = Map[String, Value]("
      kvs.zipWithIndex.map { case (kv, i) =>
        shallow(kv)
        if (i != kvs.length-1) emit(", ")
      }
      esln")"
    case Node(_, "mutstate-update", List(s, x, v), _) => esln"$s($x) = $v"
    case Node(_, "mutstate-dummyread", _, _) => es""
    case _ => super.traverse(n)
  }
  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) => es"IntV($i)"
    case Node(s, "BoolV", List(b), _) => es"BoolV($b)"
    case Node(s, "IntV-proj", List(i), _) => es"$i.I"
    case Node(s, "BoolV-proj", List(i), _) => es"$i.B"
    case Node(_, "mutstate-read", List(s, x), _) => es"$s($x)"
    case _ => super.shallow(n)
  }
}

trait ImpureStagedImpDriver[A, B] extends SAIDriver[A, B] with ImpureStagedImpSemantics { q =>
  override val codegen = new ScalaGenBase with ImpureStagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString.endsWith("$MutState")) "MutState"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import scala.collection.mutable.Map
import sai.lang.ImpLang._
object Prelude {
  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value
  implicit class ValueOps(v: Value) {
    def I: Int = v.asInstanceOf[IntV].i
    def B: Boolean = v.asInstanceOf[BoolV].b
  }
}
import Prelude._
"""
}

object ImpureStagedImpTest {
  import sai.lang.ImpLang._
  import sai.lang.ImpLang.Examples._
  def test: Unit = {
    val code = new ImpureStagedImpDriver[Int, Unit] {
      @virtualize
      def snippet(u: Rep[Int]) = {
        val st: Rep[MutState] = newMutState("x" -> IntV(3), "y" -> IntV(4))
        exec(cond3, st)
        exec(fact5, st)
        dummyRead(st)
        println(st)
      }
    }
    println(code.code)
    code.eval(0)
  }
}
