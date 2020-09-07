package sai.imp

import sai.lang.ImpLang._

import scala.language.{higherKinds, implicitConversions, existentials}

import sai.structure.freer3._
import sai.structure.freer3.Eff._
import sai.structure.freer3.Freer._
import sai.structure.freer3.Handlers._
import sai.structure.freer3.OpenUnion._
import sai.structure.freer3.State._
import sai.structure.freer3.IO._
// import sai.structure.freer3.Nondet._

import lms.core._
import lms.core.stub.{While => _, _}
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

import scala.collection.immutable.{List => SList}

object Symbol {
  private val counters = scala.collection.mutable.HashMap[String,Int]()

  def freshName(prefix: String): String = {
    val count = counters.getOrElse(prefix, 1)
    counters.put(prefix, count + 1)
    prefix + "_" + count
  }
}

@virtualize
trait RepBinaryNondet extends SAIOps {
  import sai.structure.freer3.Nondet._

  def run_with_mt[A: Manifest]: Comp[Nondet ⊗ ∅, Rep[A]] => Comp[∅, Rep[List[A]]] =
    handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X] = (_, _) match {
        case Fail$() => ret(List())
        case Choice$((), k) =>
          val xs: Rep[List[A]] = k(true)
          val ys: Rep[List[A]] = k(false)
          ret(xs ++ ys)
          /*
          val r: Rep[Int] = // make choice depending p, could be 0 or 1
          __if (r == 0) {
            val xs: Rep[List[A]] = k(true)
            ret(xs)
          } else {
            val ys: Rep[List[A]] = k(false)
            ret(ys)
          }
           */
      }
    })
}

@virtualize
trait RepCoin extends SAIOps {
  
}

@virtualize
trait RepNondet extends SAIOps {

  // def f[B: Manifest](x: Rep[List[B]]): Rep[List[B]] = x ++ x
  // def ++[A: Manifest](xs: Rep[List[A]], ys: Rep[List[A]]): Rep[List[A]] =
  //  Wrap[List[A]](Adapter.g.reflect("list-concat", Unwrap(xs), Unwrap(ys)))

  // case class NondetList[A: Manifest](xs: Rep[List[A]])
  abstract class Nondet[+A]
  case class NondetList[A](xs: Rep[List[A]]) extends Nondet[Rep[A]]
  case object BinChoice extends Nondet[Boolean]

  def fail[R <: Eff, A: Manifest](implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(List()))

  def choice[R <: Eff, A: Manifest](x: Rep[A], y: Rep[A])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(List(x, y)))

  def choice[R <: Eff, A: Manifest](a: Comp[R, Rep[A]], b: Comp[R, Rep[A]])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Boolean](BinChoice) >>= {
      case true => a
      case false => b
    }

  def select[R <: Eff, A: Manifest](xs: Rep[List[A]])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(xs))

  object NondetList$ {
    def unapply[A: Manifest, R, X](n: (Nondet[X], X => R)): Option[(Rep[List[A]], Rep[A] => R)] =
      n match {
        case (NondetList(xs), k) => Some((xs.asInstanceOf[Rep[List[A]]], k))
        case _ => None
      }
  }
  object NondetListEx$ {
    trait Result[+R] {
      type K
     // implicit val m : Manifest[K]
      def get : (Rep[List[K]], Rep[K] => R)
    }
    def unapply[R, X](n: (Nondet[X], X => R)): Option[Result[R]] =
      n match {
        case (nl @ NondetList(xs), k) => Some(new Result[R] {
          override type K = Any
          //override implicit val m: Manifest[K] = nl.m.asInstanceOf[Manifest[K]]

          override def get: (Rep[List[K]], Rep[K] => R) = (xs.asInstanceOf[Rep[List[K]]], k.asInstanceOf[Rep[K] => R])
        })

        case _ => None
      }
    object ?? {
      def unapply[R](r : Result[R]) : Option[(Rep[List[r.K]], Rep[r.K] => R)] = Some(r.get)
    }
  }
  object BinChoice$ {
    def unapply[K, R](n: (Nondet[K], K => R)): Option[(Unit, Boolean => R)] = n match {
      case (BinChoice, k) => Some(((), k))
      case _ => None
    }
  }

  // Observation: curried style works really bad when having Manifest
  def runRepNondet[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = {
    val h = handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X] = {
        case NondetList$(xs, k) => //xs : Rep[List[B]], k : Rep[B] => Comp[∅, Rep[List[A]]], need manifest of B?
          ret(xs.foldLeft(List[A]()) { case (acc, x) =>
            acc ++ k(x)
          })
        case BinChoice$((), k) =>
          for {
            xs <- k(true)
            ys <- k(false)
          } yield xs ++ ys
      }
    })
    h(comp)
  }

  //implicit def manifestfromndet[A](nl : NondetList[A]): Manifest[A] = nl.m
  //implicit def manifestfromresult[A](r : NondetListEx$.Result[A]): Manifest[r.K] = r.m

  def runRepNondet3[E <: Eff, A: Manifest](comp: Comp[Nondet ⊗ E, Rep[A]]): Comp[E, Rep[List[A]]] =
    comp match {
      case Return(x) => ret(List(x))
      case Op(u, k) => decomp(u) match {
        case Right(nd) => nd match {
          case NondetList(xs) =>
            // FIXME: expose the type of list (xs) element
            type B = A
            close[B, List[A], Comp[E, *]](x => runRepNondet3(k(x)), { (c, m) =>
              m.map { f =>
                val f2: Rep[B => List[A]] = c(f)
                xs.foldLeft(List[A]()) { case (acc, x) => acc ++ f2(x.asInstanceOf[Rep[B]]) }
              }
            })
        }
        case Left(u) =>
          Op(u) { x => runRepNondet3(k(x)) }
      }
    }

 def runRepNondet2[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = comp match {
    case Return(x) => ret(List(x))
    case Op(u, k) => decomp(u) match {
      case Right(nd) => nd match {
        case NondetList(xs) =>
          ret(xs.foldLeft(List[A]()) { case (acc, x) =>
            acc ++ extract(runRepNondet2(k(x)))
          })
        case BinChoice =>
          for {
            xs <- runRepNondet2(k(true))
            ys <- runRepNondet2(k(false))
          } yield xs ++ ys
      }
      case Left(u) =>
        Op(u) { x => runRepNondet2(k(x)) }
    }
  }
}

@virtualize
trait StagedSymImpEff extends SAIOps with RepNondet {

  // Basic value representation and their constructors/matchers
  abstract class Value
  implicit class ValueOps(v: Rep[Value]) {
    def isBool: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("BoolV-pred", Unwrap(v)))
    def getBool: Rep[Boolean] = BoolV.unapply(v).get
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    Wrap[Value](Adapter.g.reflect("op", Unwrap(unit(op)), Unwrap(v1), Unwrap(v2)))
  }

  object IntV {
    def apply (i: Rep[Int]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      Some(Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(v))))
  }
  object IntVConst {
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      v match {
        case Adapter.g.Def("IntV", collection.immutable.List(v: Backend.Exp)) =>
          Some(Wrap[Int](v))
        case _ => None
      }
  }

  object BoolV {
    def apply(b: Rep[Boolean]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
    def unapply(v: Rep[Value]): Option[Rep[Boolean]] =
      Some(Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(v))))
  }
  object BoolVConst {
    def unapply(v: Rep[Value]): Option[Rep[Boolean]] =
      v match {
        case Adapter.g.Def("SymV", SList(v: Backend.Exp)) =>
          Some(Wrap[Boolean](v))
        case _ => None
      }
  }

  object SymV {
    def apply(x: Rep[String]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x)))
    def unapply(v: Rep[Value]): Option[Rep[String]] =
      Some(Wrap[String](Adapter.g.reflect("SymV-proj", Unwrap(v))))
  }
  object SymVConst {
    def unapply(v: Rep[Value]): Option[Rep[String]] =
      v match {
        case Adapter.g.Def("SymV", SList(v: Backend.Exp)) =>
          Some(Wrap[String](v))
        case _ => None
      }
  }

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Unwrap(v) match {
      case IntVConst(i) => IntV(-i)
      case SymVConst(i) => SymV(unit("-"+i))
      case IntV(i) => IntV(-i)
    }
  }

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type SS = (Store, PC)

  // type E = IO ⊗ (State[Rep[SS], *] ⊗ (Nondet ⊗ ∅))
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)
  type SymEff[T] = Comp[E, T]

  def getStore: SymEff[Rep[Store]] = for { s <- get[Rep[SS], E] } yield s._1
  def getPC: SymEff[Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._2

  def updateStore(x: Rep[String], v: Rep[Value]): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- {
        val σ: Rep[Store] = s._1 + (x -> v)
        val ss: Rep[SS] = (σ, s._2)
        put[Rep[SS], E](ss)
      }
    } yield ()

  def updatePathCond(e: Expr): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- {
        val pc: Rep[PC] = Set(e) ++ s._2
        val ss: Rep[SS] = (s._1, pc)
        put[Rep[SS], E](ss)
      }
    } yield ()

  def reify(s: Rep[SS])(comp: Comp[E, Rep[Unit]]): Rep[List[(SS, Unit)]] = {
    val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[Unit])] =
      State.run[Nondet ⊗ ∅, Rep[SS], Rep[Unit]](s)(comp)
    val p2: Comp[Nondet ⊗ ∅, Rep[(SS, Unit)]] = p1.map(a => a)
    // val p3: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet(p2)
    val p3: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet3(p2)
    p3
  }

  def reflect(res: Rep[List[(SS, Unit)]]): Comp[E, Rep[Unit]] = {
    for {
      ssu <- select[E, (SS, Unit)](res)
      _ <- put[Rep[SS], E](ssu._1)
    } yield ssu._2
  }

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Input() => SymV(Symbol.freshName("x"))
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val v = eval(e, σ)
      op_neg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      op_2(op, v1, v2)
  }

  def eval(e: Expr): SymEff[Rep[Value]] = for { σ <- getStore } yield eval(e, σ)

  def execWithPC(s: Stmt, pc: Expr): Comp[E, Rep[Unit]] =
    for {
      _ <- updatePathCond(pc)
      _ <- exec(s)
    } yield ()

  def exec(s: Stmt): SymEff[Rep[Unit]] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) =>
        for {
          v <- eval(e)
          _ <- updateStore(x, v)
        } yield ()
      case Cond(e, s1, s2) =>
        /* generating breath-first exploring code */
        for {
          ss <- get[Rep[SS], E]
          u <- reflect(reify(ss)(execWithPC(s1, e)) ++ reify(ss)(execWithPC(s2, Op1("-", e))))
        } yield u
        /* generating depth-first exploring code */
        choice(execWithPC(s1, e), execWithPC(s2, Op1("-", e)))
        /* mixing concrete evaluation of condition (a little code duplication) */
        for {
          v <- eval(e)
          ss <- get[Rep[SS], E]
          u <- reflect {
            if (v.isBool) {
              if (v.getBool) reify(ss)(exec(s1))
              else reify(ss)(exec(s2))
            } else {
              reify(ss)(choice(execWithPC(s1, e), execWithPC(s2, Op1("-", e))))
            }
          }
        } yield u
        /* mixing concrete evaluation of condition, but reify branches to functions */
        /// TODO: LMS does not correctly lifts functions, seems due to the nested `if`
        def then_br(ss: Rep[SS]): Rep[List[(SS, Unit)]] = {
          reify(ss)(execWithPC(s1, e))
        }
        def else_br(ss: Rep[SS]): Rep[List[(SS, Unit)]] = {
          reify(ss)(execWithPC(s2, Op1("-", e)))
        }
        // fun: (Rep[A] => Rep[B]) => Rep[A => B]
        val rep_then_br: Rep[SS => List[(SS, Unit)]] = fun(then_br)
        val rep_else_br: Rep[SS => List[(SS, Unit)]] = fun(else_br)
        for {
          v <- eval(e)
          ss <- get[Rep[SS], E]
          u <- reflect {
            if (v.isBool) {
              if (v.getBool) rep_then_br(ss) else rep_else_br(ss)
            } else {
              rep_then_br(ss) ++ rep_else_br(ss)
            }
          }
        } yield u
      case Seq(s1, s2) =>
        for {
          _ <- exec(s1)
          _ <- exec(s2)
        } yield ()
      case While(e, s) =>
        val k = 4
        exec(unfold(While(e, s), k))
      case Assert(e) =>
        updatePathCond(e)
    }
}

trait StagedSymImpEffDriver[A, B] extends GenericSymStagedImpDriver[A, B] with StagedSymImpEff

trait StagedCppSymImpEffDriver[A, B] extends GenericCppSymStagedImpDriver[A, B] with StagedSymImpEff

object StagedSymFreer {
  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] =
    new StagedSymImpEffDriver[Unit, Unit] {
      def snippet(u: Rep[Unit]) = {
        // make two conditions symbolic
        val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")), Set[Expr]())
        // all concrete values
        // val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> IntV(5)), Set[Expr]())
        val v = reify(init)(exec(s))
        println(v)
        println("path number: ")
        println(v.size)
      }
    }

  def main(args: Array[String]): Unit = {
    import Examples._
    val code = specSym(cond3)
    println(code.code)
    code.eval(())
  }
}

