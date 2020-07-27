package sai.structure.freer3

import scala.language.{higherKinds, implicitConversions}

object Symbol {
  private val counters = scala.collection.mutable.HashMap[String,Int]()

  def freshName(prefix: String): String = {
    val count = counters.getOrElse(prefix, 1)
    counters.put(prefix, count + 1)
    prefix + "_" + count
  }
}

//Track effects as type-level lists of type constructors
object Eff {
  sealed trait Eff
  trait ∅ extends Eff
  trait ⊗[A[_], TL <: Eff] extends Eff

  type Lone[T[_]] = T ⊗ ∅
}

//union type as in the freer monad paper, scala-style
object OpenUnion {
  import Eff._

  sealed trait ⊎[R <: Eff, X] {
    def weaken[E[_]]: ⊎[E ⊗ R, X]
  }
  case class Union[R <: Eff, T[_], X] private(index: Int, value: T[X]) extends (R ⊎ X) {
    def weaken[E[_]]: Union[E ⊗ R, T, X] = Union(index+1,value)
  }

  //type-safe pointers into tlists
  case class Ptr[T[_], R <: Eff] private(pos: Int)
  implicit def pz[T[_], R <: Eff]: Ptr[T, T ⊗ R] = Ptr(0)
  implicit def ps[T[_], R <: Eff, U[_]](implicit pred: Ptr[T, R]): Ptr[T, U ⊗ R] = Ptr(pred.pos + 1)

  trait ∈[T[_], R <: Eff] {
    def inj[X](sub: T[X]): R ⊎ X
    def prj[X](u: R ⊎ X): Option[T[X]]
  }

  implicit def member[T[_], R <: Eff](implicit ptr: Ptr[T, R]): T ∈ R =
    new (T ∈ R) {
      override def inj[X](sub: T[X]): R ⊎ X = Union(ptr.pos, sub)

      override def prj[X](u: R ⊎ X): Option[T[X]] = u match {
        case Union(i, v) if i == ptr.pos => Some(v.asInstanceOf[T[X]])
        case _ => None
      }
    }

  //TODO can this be made more flexible? i.e. have decomp at arbitrary positions in the list R?
  def decomp[T[_], R <: Eff, X](u: (T ⊗ R) ⊎ X): Either[R ⊎ X, T[X]] = u match {
    case Union(0, v) => Right(v.asInstanceOf[T[X]])
    case Union(n, v) => Left(Union(n-1, v))
  }

  implicit def weaken[T[_], R <: Eff, X](u: ⊎[R,X]): ⊎[T ⊗ R, X] = u match {
    case Union(n, v) => Union(n+1, v)
  }
}

object Freer {
  import Eff._
  import OpenUnion._

  abstract class Comp[R <: Eff, +A] {
    def flatMap[B](f: A => Comp[R, B]): Comp[R, B]
    def map[B](f: A => B): Comp[R, B]

    def >>=[B](f: A => Comp[R, B]): Comp[R, B] = flatMap(f)
  }

  case class Return[R <: Eff, A](a: A) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] = f(a)
    override def map[B](f: A => B): Comp[R, B] = Return(f(a))
  }

  //TODO use the construction from sec. 3.1 for the continuations
  case class Op[R <: Eff, A, X](op: R ⊎ X, k: X => Comp[R, A]) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] =
      Op(op, { x: X => k(x) >>= f })

    override def map[B](f: A => B): Comp[R, B] =
      Op(op, { x: X => k(x) map f })
  }

  object Op {
    //This'll behave better with type inference, since we usually do not know the
    //actual name of the abstract continuation parameter X in a usage context. We can avoid
    //explicitly annotating the argument of the continuation k.
    def apply[R <: Eff, A, X](op: R ⊎ X)(k: X => Comp[R, A]): Comp[R, A] = Op(op, k)
  }

  def perform[T[_], R <: Eff, X](op: T[X])(implicit I: T ∈ R): Comp[R, X] = //TODO naming
    Op(I.inj(op)) { x => Return(x) }

  def ret[R <: Eff, A](x:A): Return[R, A] = Return(x)

  implicit def extract[A](c: Comp[∅, A]): A = c match {
    case Return(a) => a
  }
}

object Handlers {
  import Eff._
  import OpenUnion._
  import Freer._

  //represents the handler clauses for effect ops, and is polymorphic in the continuation type
  trait FFold[F[_], A, B] {
    def apply[X]: (F[X], X => A) => B
    def apply[X](fx: F[X])(k: X => A): B = apply(fx, k)
    //final def curried[X](fx: F[X])(k: X => A): B = apply(fx, k)
  }
  type DeepH[F[_], R <: Eff, A] = FFold[F, Comp[R, A], Comp[R, A]]
  type ShallowH[F[_], R <: Eff, A, B] = FFold[F, Comp[F ⊗ R, A], Comp[R, B]]

  /** deep handler combinator */
  def handler[E[_], R <: Eff, A, B]
             (ret: Return[E ⊗ R, A] => Comp[R, B]) //that's a stylistic choice, could as well make it A => Comp[R,B]
             (h: DeepH[E, R, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h(ex) { x => handler(ret)(h)(k(x)) }
      case Left(op) =>
        Op(op) { x => handler(ret)(h)(k(x)) }
    }
  }

  /** shallow handler combinator */
  def shallow_handler[E[_], R <: Eff, A, B]
                     (ret: Return[E ⊗ R, A] => Comp[R, B])
                     (h: ShallowH[E, R, A, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) => h(ex, k)
      case Left(op) => Op(op) { x => shallow_handler(ret)(h)(k(x)) }
    }
  }

  //open handlers a la handlers in action paper, i.e., handling an effect E by inducing other effects F
  //with more powerful effect row calculations, we could just have this kind of deep handler
  abstract class DeepHO[F[_], G[_], R <: Eff, A] {
    //to allow inducing other effects in the handler body passed by the programmer,
    //we'll need an implicit context with the capability.
    //we could probably have a leaner overall design with dotty's implicit function types
    //TODO: weak point: what if we require G to consist of multiple effects?
    implicit val canG: G ∈ (G ⊗ R) = member
    def apply[X]: (F[X], X => Comp[G ⊗ R, A]) => Comp[G ⊗ R, A]
    final def curried[X](fx: F[X])(k: X => Comp[G ⊗ R, A]): Comp[G ⊗ R, A] = apply(fx,k)
  }

  // Can E (or F) appears at anywhere inside R?
  // e.g., Return[R, A] => Comp[R, B], but requires E ∈ R and F ∈ R
  def ohandler[E[_], F[_], R <: Eff, A, B]
              (ret: Return[E ⊗ R, A] => Comp[F ⊗ R, B])
              (h: DeepHO[E, F, R, B]): Comp[E ⊗ R, A] => Comp[F ⊗ R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h.curried(ex) { x => ohandler(ret)(h)(k(x))}
      case Left(op) =>
        Op(op.weaken[F]) { x => ohandler(ret)(h)(k(x)) }
    }
  }

  //TODO: koka-style parameterized handlers?
}

object State {
  import Eff._
  import OpenUnion._
  import Freer._
  import Handlers._

  sealed trait State[S, K]
  case class Put[S](x: S) extends State[S,Unit] //Put: S ~> Unit
  case class Get[S]() extends State[S,S] //Get: Unit ~> S

  def put[S, R <: Eff](x: S)(implicit I: State[S,*] ∈ R): Comp[R, Unit] =
    perform[State[S, *], R, Unit](Put(x))
  def get[S, R <: Eff]()(implicit I: State[S,*] ∈ R): Comp[R, S] =
    perform[State[S, *], R, S](Get())

  //Scala's type checker struggles with GADTs having more than one type parameter (e.g. State[S,K]) in pattern
  //matching clauses. We define custom extractors to relieve programmers from manual type casts.
  object Get$ {
    def unapply[S, X, R](p: (State[S, X], X => R)): Option[(Unit, S => R)] = p match {
      case (Get(), k) => Some(((), k.asInstanceOf[S => R])) //the compiler cannot infer that X = S
      case _ => None
    }
  }

  object Put$ {
    def unapply[S, X, R](p: (State[S, X], X => R)): Option[(S, Unit => R)] = p match {
      case (Put(s), k) => Some((s, k))
      case _ => None
    }
  }

  def run[E <: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, (S, A)]] =
    handler[State[S, *], E, A, S => Comp[E, (S, A)]] {
      case Return(x) => ret { s: S => ret((s, x)) }
    } (ν[DeepH[State[S, *], E, S => Comp[E, (S, A)]]] {
      case Get$((), k) => ret { s: S =>
        k(s) >>= (r => r(s))
      }
      case Put$(s, k) => ret { _: S =>
        k(()) >>= (r => r(s))
      }
    })
}

object IO {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  sealed trait IO[K]
  case class ReadInt() extends IO[Int]
  case class WriteStr(x: String) extends IO[Unit]

  def readInt[R <: Eff](implicit I: IO ∈ R): Comp[R, Int] =
    perform[IO, R, Int](ReadInt())
  def writeStr[R <: Eff](n: String)(implicit I: IO ∈ R): Comp[R, Unit] =
    perform[IO, R, Unit](WriteStr(n))

  object ReadInt$ {
    def unapply[K, R](n: (IO[K], K => R)): Option[(Unit, Int => R)] = n match {
      case (ReadInt(), k) => Some(((), k))
      case _ => None
    }
  }

  object WriteStr${
    def unapply[K, R](n: (IO[K], K => R)): Option[(String, Unit => R)] = n match {
      case (WriteStr(x), k) => Some((x, k))
      case _ => None
    }
  }

  def run[E <: Eff, A]: Comp[IO ⊗ E, A] => Comp[E, A] =
    handler[IO, E, A, A] {
      case Return(x) => ret(x)
    } (ν[DeepH[IO, E, A]] {
      case ReadInt$((), k) =>
        val n = scala.io.StdIn.readInt()
        k(n)
      case WriteStr$(x, k) =>
        System.out.println(x)
        k(())
    })
}

object SMT {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  sealed trait SMT[K]
  case class IsSAT(e: String) extends SMT[Boolean]
  case class Concretize(e: String, x: String) extends SMT[Option[Int]]

  def isSat[R <: Eff](e: String)(implicit I: SMT ∈ R): Comp[R, Boolean] =
    perform[SMT, R, Boolean](IsSAT(e))
  def concretize[R <: Eff](e: String, x: String)(implicit I: SMT ∈ R): Comp[R, Option[Int]] =
    perform[SMT, R, Option[Int]](Concretize(e, x))
}

object Nondet {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  sealed trait Nondet[K]
  case object Fail extends Nondet[Nothing]   //Fail ()   ~> Nothing
  case object Choice extends Nondet[Boolean] //Choice () ~> Boolean

  object Fail$ {
    def unapply[K,R](n: (Nondet[K], K => R)): Boolean = n match {
      case (Fail, _) => true
      case _ => false
    }
  }

  object Choice$ {
    def unapply[K,R](n: (Nondet[K], K => R)): Option[(Unit, Boolean => R)] = n match {
      case (Choice, k) => Some(((), k))
      case _ => None
    }
  }

  def fail[R <: Eff, A](implicit I: Nondet ∈ R): Comp[R,A] = perform(Fail)

  def choice[R <: Eff, A](a: Comp[R, A], b: Comp[R, A])(implicit I: Nondet ∈ R): Comp[R, A] =
    perform(Choice) >>= {
      case true  => a
      case false => b
    }

  def select[R <: Eff, A](xs: List[A])(implicit I: Nondet ∈ R): Comp[R, A] =
    xs.map(Return[R, A]).foldRight[Comp[R, A]](fail)(choice)

  def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    handler[Nondet, E, A, List[A]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, E, List[A]] {
      def apply[X] = (_, _) match {
        case Fail$() => ret(List())
        case Choice$((), k) =>
          for {
            xs <- k(true)
            ys <- k(false)
          } yield xs ++ ys
      }
    })

}

object Freer3Test {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._
  import Nondet._
  import State._
  import IO._

  def runLocalState[R <: Eff, S, A](s: S, comp: Comp[State[S, *] ⊗ (Nondet ⊗ R), A]): Comp[R, List[Comp[R, List[(S, A)]]]] = {
    val p: Comp[Nondet ⊗ R, S => Comp[Nondet ⊗ R, (S, A)]] = State.run[Nondet ⊗ R, S, A](comp)
    val p1: Comp[R, List[S => Comp[Nondet ⊗ R, (S, A)]]] = Nondet.run[R, S => Comp[Nondet ⊗ R, (S, A)]](p)
    for {fs <- p1} yield fs.map(f => Nondet.run(f(s)))
  }

  def inc[E <: Eff](implicit I: State[Int, *] ∈ E): Comp[E, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  def select_inc[R <: Eff, A](xs: List[A])
    (implicit I1: Nondet ∈ R, I2: State[Int, *] ∈ R, I3: IO ∈ R): Comp[R, A] =
    xs.map(Return[R, A]).foldRight[Comp[R, A]](fail) {
      case (a, b) =>
        for {
          _ <- inc
          x <- choice(a, b)
          _ <- writeStr("select " + x.toString)
        } yield x
    }

  def knapsack[R <: Eff](w: Int, vs: List[Int])
    (implicit I1: Nondet ∈ R, I2: State[Int, *] ∈ R, I3: IO ∈ R): Comp[R, List[Int]] = {
    if (w < 0) fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select_inc(vs)
      xs <- knapsack(w - v, vs)
    } yield v :: xs
  }

  def runGlobalKnapsack = {
    val p: Comp[Nondet ⊗ (IO ⊗ (State[Int, *] ⊗ ∅)), List[Int]] =
      knapsack[Nondet ⊗ (IO ⊗ (State[Int, *] ⊗ ∅))](3, List(3, 2, 1))
    val p1: Comp[IO ⊗ (State[Int, *] ⊗ ∅), List[List[Int]]] = Nondet.run[IO ⊗ (State[Int, *] ⊗ ∅), List[Int]](p)
    val p2: Comp[State[Int, *] ⊗ ∅, List[List[Int]]] = IO.run[State[Int, *] ⊗ ∅, List[List[Int]]](p1)
    val p3: Comp[∅, Int => Comp[∅, (Int, List[List[Int]])]] = State.run[∅, Int, List[List[Int]]](p2)
    println(extract(extract(p3)(0)))
  }

  def runLocalKnapsack = {
    val p: Comp[IO ⊗ (State[Int, *] ⊗ (Nondet ⊗ ∅)), List[Int]] =
      knapsack[IO ⊗ (State[Int, *] ⊗ (Nondet ⊗ ∅))](3, List(3, 2, 1))
    val p1: Comp[State[Int, *] ⊗ (Nondet ⊗ ∅), List[Int]] =
      IO.run(p)
    val p2: Comp[∅, List[Comp[∅, List[(Int, List[Int])]]]] = runLocalState(0, p1)
    println(extract(p2).map(extract))
    /* Equivelent to the followings:
    val p2: Comp[Nondet ⊗ ∅, Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]] =
      State.run[Nondet ⊗ ∅, Int, List[Int]](p1)
    val p3: Comp[∅, List[Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]]] =
      Nondet.run[∅, Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]](p2)
    val p3v: List[Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]] = extract(p3)
    println(p3v.map(f => extract(Nondet.run(f(0)))))
     */
    //List(List((1,List(3)), (5,List(2, 1)), (5,List(1, 2)), (9,List(1, 1, 1))))
  }

  object UnstagedSymImpEff {
    import sai.lang.ImpLang._
    trait Value
    case class IntV(i: Int) extends Value
    case class BoolV(b: Boolean) extends Value
    case class SymV(e: Expr) extends Value

    type PC = Set[Expr]
    type Store = Map[String, Value]
    type SS = (Store, PC)

    def getStore[R <: Eff](implicit I: State[SS, *] ∈ R): Comp[R, Store] =
      for {s <- get} yield s._1
    def getPC[R <: Eff](implicit I: State[SS, *] ∈ R): Comp[R, PC] =
      for {s <- get} yield s._2
    def updateStore[R <: Eff](x: String, v: Value)(implicit I: State[SS, *] ∈ R): Comp[R, Unit] =
      for {s <- get; _ <- put((s._1 + (x -> v), s._2))} yield ()
    def updatePathCond[R <: Eff](e: Expr)(implicit I: State[SS, *] ∈ R): Comp[R, Unit] =
      for {s <- get; _ <- put((s._1, Set(e) ++ s._2))} yield ()

    def eval(e: Expr, σ: Store): Value =
      e match {
        case Lit(i: Int) => IntV(i)
        case Lit(b: Boolean) => BoolV(b)
        case Var(x) => σ(x)
        case Op1("-", e) =>
          eval(e, σ) match {
            case IntV(i) => IntV(-i)
            case SymV(e) => SymV(Op1("-", e))
          }
        case Op2(op, e1, e2) =>
          val v1 = eval(e1, σ)
          val v2 = eval(e2, σ)
          (op, v1, v2) match {
            case ("+", IntV(i1), IntV(i2)) => IntV(i1 + i2)
            case ("-", IntV(i1), IntV(i2)) => IntV(i1 - i2)
            case ("*", IntV(i1), IntV(i2)) => IntV(i1 * i2)
            case ("/", IntV(i1), IntV(i2)) => IntV(i1 / i2)
            case ("==", IntV(i1), IntV(i2)) => BoolV(i1 == i2)
            case ("<=", IntV(i1), IntV(i2)) => BoolV(i1 <= i2)
            case (">=", IntV(i1), IntV(i2)) => BoolV(i1 >= i2)
            case ("<", IntV(i1), IntV(i2)) => BoolV(i1 < i2)
            case (">", IntV(i1), IntV(i2)) => BoolV(i1 > i2)
            case (op, SymV(e1), SymV(e2)) => SymV(Op2(op, e1, e2))
          }
      }

    def eval[R <: Eff](e: Expr)(implicit I1: State[SS, *] ∈ R, I2: IO ∈ R): Comp[R, Value] =
      e match {
        case Input() => for { i <- readInt } yield IntV(i)
        case _ => for { σ <- getStore } yield eval(e, σ)
      }

    def exec[R <: Eff](s: Stmt)(implicit I1: State[SS, *] ∈ R, I2: IO ∈ R, I3: Nondet ∈ R): Comp[R, Unit] =
      s match {
        case Skip() => ret(())
        case Assign(x, e) => for { v <- eval(e); σ <- getStore; _ <- updateStore(x, v) } yield ()
        case Cond(e, s1, s2) =>
          for {
            v <- eval(e)
            _ <- if (v == BoolV(true)) exec(s1)
                 else if (v == BoolV(false)) exec(s2)
                 else choice(exec(s1), exec(s2))
          } yield ()
        case Seq(s1, s2) => for {_ <- exec(s1); _ <- exec(s2)} yield ()
        case While(e, s) =>
          /*
          for {
            v <- eval(e)
            _ <- if (v == BoolV(true)) exec(Seq(s, While(e, s)))
            else if (v == BoolV(false)) exec(Skip())
            else choice(exec(Seq(s, While(e, s))), exec(Skip()))
          } yield ()
           */
          // TODO: define an "unroll" effect?
          def loop: Comp[R, Unit] = for {
            v <- eval(e)
            _ <- if (v == BoolV(true)) for { _ <- exec(s); _ <- loop } yield ()
                 else if (v == BoolV(false)) exec(Skip())
                 else choice(for { _ <- exec(s); _ <- loop } yield (), exec(Skip()))
          } yield ()
          loop
        case Output(e) => for {
          v <- eval(e)
          _ <- writeStr(v.toString)
        } yield ()
      }

    def run(s: Stmt) = {
      val ss0: SS = (Map(), Set())
      val p: Comp[IO ⊗ (State[SS, *] ⊗ (Nondet ⊗ ∅)), Unit] =
        exec[IO ⊗ (State[SS, *] ⊗ (Nondet ⊗ ∅))](s)
      val p1: Comp[State[SS, *] ⊗ (Nondet ⊗ ∅), Unit] = IO.run(p)
      val p2: Comp[∅, List[Comp[∅, List[(SS, Unit)]]]] = runLocalState(ss0, p1)
      println(extract(p2).map(extract))
    }
  }

  import lms.core._
  import lms.core.stub._
  import lms.macros._
  import lms.core.Backend._
  import sai.lmsx._

  @virtualize
  trait StagedSymImpEff extends SAIOps {
    import sai.lang.ImpLang._

    // TODO change this to SMT backend?
    trait Value
    def IntV(i: Rep[Int]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
    def BoolV(b: Rep[Boolean]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
    def SymV(x: Rep[String]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x)))

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
    type SS = (Store, PC)

    def reload[R <: Eff, S, A](xs: List[(S, A)])(implicit I1: Nondet ∈ R, I2: State[S, *] ∈ R): Comp[R, A] = {
      for {
        sa <- select(xs)
        _ <- put(sa._1)
      } yield sa._2
    }

    def reload[R <: Eff, S, A](xs: List[A], s: S)(implicit I1: Nondet ∈ R, I2: State[S, *] ∈ R): Comp[R, A] = {
      for {
        _ <- put(s)
        x <- select(xs)
      } yield x
    }

    type E = IO ⊗ (State[Rep[SS], *] ⊗ (Nondet ⊗ ∅))

    def getStore: Comp[E, Rep[Store]] =
      for { s <- get[Rep[SS], E] } yield s._1
    def updateStore(x: Rep[String], v: Rep[Value]): Comp[E, Rep[Unit]] =
      for {
        s <- get[Rep[SS], E]
        _ <- {
          val σ: Rep[Store] = s._1 + (x -> v)
          val ss: Rep[SS] = (σ, s._2)
          put[Rep[SS], E](ss)
        }
      } yield ()
    def getPC: Comp[E, Rep[PC]] =
      for { s <- get[Rep[SS], E] } yield s._2
    def updatePathCond(e: Expr): Comp[E, Rep[Unit]] =
      for {
        s <- get[Rep[SS], E]
        _ <- {
          val pc: Rep[PC] = Set(e) ++ s._2
          val ss: Rep[SS] = (s._1, pc)
          put[Rep[SS], E](ss)
        }
      } yield ()

    def select_rep(xs: Rep[List[(SS, Unit)]]): Comp[E, Rep[Unit]] = {
      //xs.map(Return).foldRight[Comp[R, A]](fail)(choice)
      //xs.flatMap(x => List(x))
      ???
    }

    def H(comp: Comp[E, Rep[Unit]]): Rep[List[(SS, Unit)]] = ???
    def R(res: Rep[List[(SS, Unit)]]): Comp[E, Rep[Unit]] = {???}

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

    //def eval(e: Expr)(implicit I1: State[Rep[SS], *] ∈ E, I2: IO ∈ E): Comp[E, Rep[Value]] = ???
    def eval(e: Expr): Comp[E, Rep[Value]] = for {
      σ <- getStore
    } yield eval(e, σ)

    def execPC(s: Stmt, pc: Expr): Comp[E, Rep[Unit]] = for {
      _ <- updatePathCond(pc)
      _ <- exec(s)
    } yield ()

    //def exec(s: Stmt)(implicit I1: State[Rep[SS], *] ∈ E, I2: IO ∈ E, I3: Nondet ∈ E): Comp[E, Rep[Unit]] =
    def exec(s: Stmt): Comp[E, Rep[Unit]] =
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
            v <- choice(execPC(s1, e), execPC(s2, Op1("-", e)))
          } yield { v }
        case Seq(s1, s2) =>
          for { _ <- exec(s1); _ <- exec(s2) } yield ()
        case While(e, s) =>
          val k = 4
          exec(unfold(While(e, s), k))
        case Assert(e) =>
          updatePathCond(e)
      }
  }

  def main(args: Array[String]) {
    //def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    //def run[E <: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, (S, A)]] =
    runGlobalKnapsack
    runLocalKnapsack

    import sai.lang.ImpLang.Examples._

    UnstagedSymImpEff.run(fact5)
  }

}
