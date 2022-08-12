package sai.imp

import sai.lang.ImpLang._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt._

import scala.collection.immutable.{List => SList, Set => SSet}

/* Staged symbolic execution for Imp using monad transformers
 */

@virtualize
trait SymStagedImp extends SAIOps {
  import StateT._
  import ListT._

  implicit val bw: Int = 32

  type Value = SMTExpr
  def IntV(i: Rep[Int]): Rep[Value] = lit(i)
  def BoolV(b: Rep[Boolean]): Rep[Value] = lit(b)
  def SymVBV(x: String): Rep[Value] = bvVar(x)
  def SymVBool(x: String): Rep[Value] = boolVar(x)

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Adapter.typeMap.get(Unwrap(v)).get.runtimeClass.getName match {
      case x if x.contains("SMTBool") =>
        not(v.asInstanceOf[Rep[SMTBool]])
      case x if x.contains("BV") =>
        // TODO: use proper minus op? It seems bvNot or bvUMinus cannot handle signed values.
        bvNot(v.asInstanceOf[Rep[BV]])
      case _ => ???
    }
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    import SyntaxSMT._
    val v1BV = v1.asInstanceOf[Rep[BV]]
    val v2BV = v2.asInstanceOf[Rep[BV]]
    op match {
      case "==" => v1BV ≡ v2BV
      case "<=" => v1BV ≤ v2BV
      case ">=" => v1BV ≥ v2BV
      case "<" => v1BV < v2BV
      case ">" => v1BV > v2BV
      case "+" => v1BV + v2BV
      case "-" => v1BV - v2BV
      case "*" => v1BV * v2BV
      case "/" => v1BV / v2BV
    }
  }

  type PC = Set[SMTBool]
  type Store = Map[String, Value]
  type Ans = (Store, PC)
  type M[T] = StateT[ListM, Ans, T] // result type List[(T, (Store, PC))]
  val M = Monad[M]

  var n: Int = 0
  def fresh: String = {
    val x = "x" + n.toString
    n = n + 1 
    x
  }

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
    case Input() => SymVBV(fresh)
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

  def update_pc(e: Expr): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    pc <- evalM(e)
    _ <- MonadState[M, Ans].put((ans._1, ans._2 ++ Set(pc.asInstanceOf[Rep[SMTBool]])))
  } yield ()

  def select(cnd: Expr, m1: M[Unit], m2: M[Unit]): M[Unit] = {
    val b1 = for {
      _ <- update_pc(cnd)
      _ <- m1
    } yield ()
    val b2 = for {
      _ <- update_pc(Op1("-", cnd))
      _ <- m2
    } yield ()

    StateT[ListM, Ans, Unit]((s: Rep[Ans]) => {
      b1.run(s) ++ b2.run(s)
    })
  }

  def fix[A: Manifest, B: Manifest](f: Rep[A] => Rep[B]): Rep[A => B] = fun(f)

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_store(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      _ <- select(e, exec(s1), exec(s2))
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1)
      _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      val k = 4
      exec(unfold(While(e, b), k))
    case Assert(e) => for {
      _ <- update_pc(e)
    } yield ()
    case Output(e) => ???
  }

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }
}

trait SymStagedImpDriver[A, B] extends GenericSymStagedImpDriver[A, B] with SymStagedImp

trait CppSymStagedImpDriver[A, B] extends GenericCppSymStagedImpDriver[A, B] with SymStagedImp
