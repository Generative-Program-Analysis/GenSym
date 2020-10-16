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
import sai.util.symbol._

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
  case class NondetList[A : Manifest](xs: Rep[List[A]]) extends Nondet[Rep[A]] {
    val m: Manifest[A] = implicitly
  }
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
      def get : (Manifest[K], Rep[List[K]])
    }
    def unapply[R, X](n: Nondet[X]): Option[Result[R]] =
      n match {
        case nl @ NondetList(xs) => Some(new Result[R] {
          override type K = Any
          override def get = (nl.m.asInstanceOf[Manifest[K]], xs.asInstanceOf[Rep[List[K]]])
        })

        case _ => None
      }
    object ?? {
      def unapply[R](r : Result[R]) : Option[(Manifest[r.K], Rep[List[r.K]])] = Some(r.get)
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

  def runRepNondet3[E <: Eff, A: Manifest](comp: Comp[Nondet ⊗ E, Rep[A]]): Comp[E, Rep[List[A]]] = {
    import NondetListEx$.??
    comp match {
      case Return(x) => ret(List(x))
      case Op(u, k) => decomp(u) match {
        case Right(nd) => nd match {
          case nl @ NondetListEx$(??(m,xs)) =>
            def handleNdet[B : Manifest](xs : Rep[List[B]]): Comp[E, Rep[List[A]]] = {
              // k: Rep[B] => Comp[Nondet x E, List[A]]
              // k_eta: Comp[E, Rep[B => List[A]]]
              // k_eta': Comp[E, Rep[List[A]]]
              val k_eta: Comp[E, Rep[B=>List[A]]] =
                close[B, A, B, List[A], B=>List[A], Comp[Nondet ⊗ E, *], Comp[E, *]](x => k(x), { (c, m) =>
                  runRepNondet3(m).map(c(_))
                })
              k_eta.map(k => xs.foldLeft(List[A]()) { case (acc, x) => acc ++ k(x) })

              /*
              close[B, List[A], B, List[A], List[A], Comp[E, *], Comp[E, *]](x => runRepNondet3(k(x)), { (c, m) =>
                m.map { f =>
                  val f2: Rep[B => List[A]] = c(f)
                  xs.foldLeft(List[A]()) { case (acc, x) => acc ++ f2(x.asInstanceOf[Rep[B]]) }
                }
              })
               */
            }
            handleNdet(xs)(m)
        }
        case Left(u) =>
          Op(u) { x => runRepNondet3(k(x)) }
      }
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
trait StagedKnapsack extends SAIOps with RepNondet {
  // (12,List(List(3), List(2, 1), List(1, 2), List(1, 1, 1)))
  // List(List((1,List(3))), List((2,List(2, 1))), List((2,List(1, 2)), (3,List(1, 1, 1))))

  def inc[E <: Eff](implicit I: State[Int, *] ∈ E): Comp[E, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  type Eff = Nondet ⊗ (State[Rep[Int], *] ⊗ ∅)

  def reify(comp: Comp[Eff, Rep[List[Int]]]): Rep[(Int, List[List[Int]])] = {
    val p: Comp[Nondet ⊗ (State[Rep[Int], *] ⊗ ∅), Rep[List[Int]]] = comp
    val p1: Comp[(State[Rep[Int], *] ⊗ ∅), Rep[List[List[Int]]]] =
      runRepNondet3[(State[Rep[Int], *] ⊗ ∅), List[Int]](p)
    /*
    val p2: Comp[∅, Rep[Int] => Comp[∅, (Rep[Int], Rep[List[List[Int]]])]] =
      State.run[∅, Rep[Int], Rep[List[List[Int]]]](p1)
    extract(extract(p2)(0).map(a => a))
     */
    val p2: Comp[∅, (Rep[Int], Rep[List[List[Int]]])] =
      State.run2[∅, Rep[Int], Rep[List[List[Int]]]](1)(p1)
    val p3: Comp[∅, Rep[(Int, List[List[Int]])]] = p2.map(a => a)
    extract(p3)
  }

  def reflect(value: Rep[(Int, List[List[Int]])]): Comp[Eff, Rep[List[Int]]] = {
    val s: Rep[Int] = value._1
    val xs: Rep[List[List[Int]]] = value._2
    for {
      _ <- put[Rep[Int], Eff](s)
      x <- select[Eff, List[Int]](xs)
    } yield x
  }

  def select_count(xs: Rep[List[Int]]): Comp[Eff, Rep[List[Int]]] = {
    for {
      _ <- put[Rep[Int], Eff](10)
      v <- select[Eff, Int](xs)
      x <- get[Rep[Int], Eff]
      _ <- put[Rep[Int], Eff](x + v)
      // _ <- put[Rep[Int], Eff](v)
      /*
      _ <- put[Rep[Int], Eff](v + x)
      y <- { get[Rep[Int], Eff] }
       */
    } yield List(x + v)
  }

  def knapsack(w: Rep[Int], vs: Rep[List[Int]]): Comp[Eff, Rep[List[Int]]] = {
    val v =
      if (w < 0) reify(fail)
      else {
        if (w == 0) reify(ret(List()))
        else {
          /*
          def f(w: Rep[Int], vs: Rep[List[Int]]): Rep[(Int, List[List[Int]])] = {
            reify(knapsack(w, vs))
          }
          val rf: Rep[(Int, List[Int]) => (Int, List[List[Int]])] = fun(f)
          val m: Comp[Eff, Rep[List[Int]]] = for {
            v <- select[Eff, Int](vs)
            xs <- reflect(rf(w - v, vs))
          } yield v::xs
           */
          val m: Comp[Eff, Rep[List[Int]]] = for {
            v <- select[Eff, Int](vs)
            x <- get[Rep[Int], Eff]
            _ <- put[Rep[Int], Eff](x + 1)
          } yield List(v)
          reify(m)
        }
      }
    reflect(v)
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
      State.run2[Nondet ⊗ ∅, Rep[SS], Rep[Unit]](s)(comp)
    val p2: Comp[Nondet ⊗ ∅, Rep[(SS, Unit)]] = p1.map(a => a)
    val p3: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet(p2)
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

  def if_updown(s: Rep[SS], cnd: => Rep[Boolean], e1: => SymEff[Rep[Unit]], e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    reflect(if (cnd) reify(s)(e1) else reify(s)(e2))
  }

  def probChoice(s: Rep[SS], e1: => SymEff[Rep[Unit]], e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    val r: Rep[Int] = Wrap[Int](Adapter.g.reflectWrite("randInt", Unwrap(unit(100)))(Adapter.CTRL))
    if_updown(s, r <= 50, e1, e2)
  }

  def genSelect[A: Manifest, B: Manifest](i: Rep[Int], xs: List[Rep[A]], k: Rep[A] => Rep[B]): Rep[Int => Rep[B]] = {
    def select(xs: List[(Rep[A], Int)]): Rep[B] = {
      if (xs.isEmpty) unchecked[B]("???")
      else {
        if (i == xs.head._2) k(xs.head._1)
        else select(xs.tail)
      }
    }
    ???
  }

  def exec(s: Stmt): SymEff[Rep[Unit]] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) =>
        for {
          v <- eval(e)
          _ <- updateStore(x, v)
        } yield ()
      case Cond(e, s1, s2) =>
        for {
          b <- eval(e)
          s <- get[Rep[SS], E]
          u <- if_updown(s, b.isBool,
            if_updown(s, b.getBool,
              execWithPC(s1, e),
              execWithPC(s2, Op1("-", e))),
            probChoice(s, execWithPC(s1, e), execWithPC(s2, Op1("-", e)))
          )
        } yield u
      case Seq(s1, s2) =>
        for {
          _ <- exec(s1)
          _ <- exec(s2)
        } yield ()
      case While(e, s) =>
        val k = 4
        def loop(ss: Rep[SS]): Rep[List[(SS, Unit)]] = {
          val m = for {
            v <- eval(e)
            st <- get[Rep[SS], E]
            u <- reflect {
              if (v.getBool) {
                val m = for {
                  _ <- exec(s)
                  st1 <- get[Rep[SS], E]
                  r <- reflect(reploop(st1))
                } yield r
                reify(st)(m)
              } else {
                reify(st)(exec(Skip()))
              }
            }
          } yield u
          reify(ss)(m)
        }
        def reploop: Rep[SS=>List[(SS, Unit)]] = fun(loop)
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

  @virtualize
  def specSymCpp(s: Stmt): CppSAIDriver[Int, Unit] =
    new StagedCppSymImpEffDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        // make two conditions symbolic
        val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")), Set[Expr]())
        // all concrete values
        // val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> IntV(5)), Set[Expr]())
        val v = reify(init)(exec(s))
        // println(v)
        println("path number: ")
        println(v.size)
      }
    }

  @virtualize
  def specKnapsack: SAIDriver[Int, Unit] = {
    trait StagedKnapsackDriver[A, B] extends GenericSymStagedImpDriver[A, B] with StagedKnapsack
    new StagedKnapsackDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        // val k = knapsack(3, List(3, 2, 1))
        val k = select_count(u :: List(3, 2, 1))
        val res: Rep[(Int, List[List[Int]])] = reify(k)
        println(res._1)
        println(res)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import Examples._
    //val code = specKnapsack
    val code = specSymCpp(cond3)
    code.save("cond3.cpp")
    //println(code.code)
    code.eval(4)
  }
}

