package gensym.structure.freer.discard

import scala.language.{higherKinds, implicitConversions}

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

  sealed trait State[S, K]
  case class Put[S](x: S) extends State[S,Unit] //Put: S ~> Unit
  case class Get[S]() extends State[S,S] //Get: Unit ~> S

  def put[S, R <: Eff](x: S)(implicit I: State[S,*] ∈ R): Comp[R, Unit] =
    perform[State[S, *], R, Unit](Put(x))
  def get[S, A, R <: Eff]()(implicit I: State[S,*] ∈ R): Comp[R, S] =
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

  import Handlers._

  def stateref[E <: Eff, S, A](init: S) = {
    var state: S = init
    val deeph = new DeepH[State[S, *], E, A] {
      def apply[X] = (_, _) match {
        case Get$((), k) => k(state)
        case Put$(s, k) => state = s; k(())
      }
    }

    handler[State[S, *], E, A, A] { case Return(x) => Return(x) }(deeph)
  }

  //a little less noisy
  def stateref2[E <: Eff, S, A](init: S) = {
    var state: S = init
    handler[State[S, *], E, A, A] {
      case Return(x) => Return(x)
    }{ ν[DeepH[State[S, *], E, A]] {
         case Get$((), k) => k(state)
         case Put$(s, k) => state = s; k(())
       }
    }
  }

  // TODO: statefun_mt1 vs statefun_mt2
  def statefun_mt1[S, A]: Comp[State[S, *] ⊗ ∅, A] => Comp[∅, S => Comp[∅, A]] =
    handler[State[S,*], ∅, A, S => Comp[∅, A]] {
      case Return(x)  => ret { _: S => ret(x) }
    } (ν[DeepH[State[S, *], ∅, S => Comp[∅, A]]] {
      case Get$((), k) => ret { s: S =>  k(s)(s) }
      case Put$(s, k)  => ret { _: S => k(())(s) }
    })

  def statefun_mt2[S, A]: Comp[State[S, *] ⊗ ∅, A] => Comp[∅, S => A] =
    handler[State[S,*], ∅, A, S => A] {
      case Return(x)  => ret { _: S => x }
    } (ν[DeepH[State[S, *], ∅, S => A]] {
      case Get$((), k) => ret { s: S =>  k(s)(s) }
      case Put$(s, k)  => ret { _: S => k(())(s) }
    })

  // Generalized from statefun_mt1
  // Does the two E's have to be the same?                      ↓            ↓
  def statefun[E<: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, A]] =
    handler[State[S, *], E, A, S => Comp[E, A]] {
      case Return(x)  => ret { _: S => ret(x) }
    } (ν[DeepH[State[S, *], E, S => Comp[E, A]]] {
      case Get$((), k) => ret { s: S =>
        k(s) >>= (r => r(s))
      }
      case Put$(s, k)  => ret { _: S =>
        k(()) >>= (r => r(s))
      }
    })

  def statefun_mt_pair[S, A]: Comp[State[S, *] ⊗ ∅, A] => Comp[∅, S => (S, A)] =
    handler[State[S,*], ∅, A, S => (S, A)] {
      case Return(x)  => ret { s: S => (s, x) }
    } (ν[DeepH[State[S, *], ∅, S => (S, A)]] {
      case Get$((), k) => ret { s: S =>  k(s)(s) }
      case Put$(s, k)  => ret { _: S => k(())(s) }
    })

  def statefun_pair[E <: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, (S, A)]] =
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

  //example of intercepting and forwarding effects with open handler
  def state_square[E <: Eff, A] = ohandler[State[Int, *], State[Int, *], E, A, A] {
    case Return(x)  => ret(x)
  } (ν[DeepHO[State[Int, *], State[Int, *], E, A]] {
    case Get$((), k) =>
      get() >>= { x => k(x * x) }
    case Put$(s, k)  =>
      put(s) >>= k
  })
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
    //def unapply[E <: Eff, K, R](n: (Comp[E, K], K => R))
      //(implicit I: Nondet ∈ E): Boolean =  n match {
          /*
      case Op(f, k) => I.prj(f) match {
        case Some(Fail) => true
        case _ => false
      }
      case _ => false
           */
        //(implicit I: Nondet ∈ E): Boolean = n._1 match {
  }

  object Choice$ {
    def unapply[K,R](n: (Nondet[K], K => R)): Option[(Unit, Boolean => R)] = n match {
      case (Choice, k) => Some(((), k))
      case _ => None
    }
  }

  def fail[A, R <: Eff](implicit I: Nondet ∈ R): Comp[R,A] = perform(Fail)

  def choice[A, R <: Eff](a: Comp[R, A], b: Comp[R, A])(implicit I: Nondet ∈ R): Comp[R, A] =
    perform[Nondet, R, Boolean](Choice) >>= {
      case true  => a
      case false => b
    }

  def handleNondet[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
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

trait Selector[R[_]] {
  def apply[A: Manifest](c: R[Boolean], x: R[A], y: R[A]): R[A]
}

object CondEff2 {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  sealed trait Cnd[K]
  case class BranchBy[C](c: C) extends Cnd[C]

  def handleCond[R <: Eff, C, A]: Comp[Cnd ⊗ R, A] => Comp[R, A] =
    handler[Cnd, R, A, A] {
      case Return(x) => ret(x)
    } (new DeepH[Cnd, R, A] {
      def apply[X]: (Cnd[X], (X => Comp[R, A])) => Comp[R, A] = {
        case (fx: Cnd[X], k: (X => Comp[R, A])) =>
          val BranchBy(c) = fx
          k(c)
      }
    })

}

  /*
object CondEff {
  import Eff._
  import Freer._
  import Handlers._
  import OpenUnion._

  case class Cnd[R <: Eff, C, A](c: C, thn: Comp[R, A], els: Comp[R, A])

  def cond[R <: Eff, C, A](c: C, a: Comp[R, A], b: Comp[R, A])
    (implicit I: Cnd[R, C, *] ∈ R): Comp[R, A] = { //FIXME: Cnd[R, C, *] ∈ R, recursion?
    perform[Cnd[R, C, *], R, A](Cnd(c, a, b)) 
  }

  object Cnd${
    def unapply[R <: Eff, C, A](n: (Cnd[R, C, A], A => R)): Option[((C, Comp[R, A], Comp[R, A]), A => R)] =
      n match {
        case (Cnd(c, t, e), k) => Some(((c, t, e), k))
        case _ => None
      }
  }

  def handleCond[R <: Eff, C, A](implicit IF: Selector[C]): Comp[Cnd[R, C, *] ⊗ R, A] => Comp[R, A] =
    handler[Cnd[R, C, *], R, A, A] {
      case Return(x) => ret(x)
    } (new DeepH[Cnd[R, C, *], R, A] {
      def apply[X]: (Cnd[R, C, X], (X => Comp[R, A])) => Comp[R, A] = {
        case (fx: Cnd[R, C, X], k: (X => Comp[R, A])) =>
          val Cnd(c, t, e) = fx
          val x = for {
            v1 <- t
            v2 <- e
          } yield { IF.apply[X](c, v1, v2) }
          for {
            v <- x
            w <- k(v)
          } yield w
      }
    })
}
   */

import lms.core._
import lms.core.stub._
import lms.macros._
import lms.core.Backend._
import gensym.lmsx._

object StagedFreerExample {
  import Eff._
  import OpenUnion._
  import Freer._
  import Handlers._
  import State._
  //import CondEff._
  import CondEff2._

  type NoRep[T] = T

  def cond[R <: Eff, A: Manifest](c: Boolean, a: Comp[R, A], b: Comp[R, A])
    (implicit I: Cnd ∈ R, IF: Selector[NoRep]): Comp[R, A] = {
    perform[Cnd, R, Boolean](BranchBy(c)) >>= { x =>
      implicit def m: Manifest[Comp[R, A]] = null //The manifest is not used for unstaged version
      IF(x, a, b)
    }
  }

  /*
  @virtualize
  def specialize(): SAIDriver[Int, Int] =
    new StagedImpEffDriver[Int, Int] {
      def scond[R <: Eff, A: Manifest](c: Rep[Boolean], a: Comp[R, Rep[A]], b: Comp[R, Rep[A]])
        (implicit I: Cnd ∈ R, IF: Selector[Rep]): Comp[R, Rep[A]] = {
        perform[Cnd, R, Rep[Boolean]](BranchBy(c)) >>= { x =>
          for {
            v1 <- a
            v2 <- b
          } yield { if (x) { println(v1); v1 } else { println(v2); v2 } } //staged if
          /* The v1 and v2 are just result value of each branch, which
           * do not contain materialized effect. So this code
           * fails to generate a confluence point containing the effect
           * happened inside branches.
           */
        }
      }

      def prog1[R <: Eff](implicit I: State[Rep[Int], *] ∈ R): Comp[R, Rep[Int]] =
        for {
          x <- get
          _ <- put(x * x)
          z <- get
        } yield z

      def prog2[R <: Eff](implicit I: State[Rep[Int], *] ∈ R): Comp[R, Rep[Int]] =
        for {
          x <- get
          _ <- put(x + x)
          z <- get
        } yield z

      implicit object BoolSel extends Selector[NoRep] {
        def apply[A: Manifest](c: Boolean, x: A, y: A): A = if (c) x else y
      }
      implicit object RepBoolSel extends Selector[Rep] {
        def apply[A: Manifest](c: Rep[Boolean], x: Rep[A], y: Rep[A]): Rep[A] = if (c) x else y
      }

      def prog3[R <: Eff](implicit I1: State[Rep[Int], *] ∈ R, I2: Cnd ∈ R): Comp[R, Rep[Int]] =
        for {
          x <- get
          y <- scond[R, Int](x == 0, prog1, prog2)
          z <- get
          _ <- put(z + 1)
          w <- get
        } yield w

      def snippet(u: Rep[Int]) = {
        /* Unlike what I did in Free.scala, switching the handler of
         * Cnd and State yields the same generated code. In
         * Free.scala, applying state handler first and then cnd
         * handler gives a local state interpretation, which prevents
         * the two branches use the same state.
         * But here, they seem both generate a global state code,
         * that is, the "else" branch access the state mutated by the "then"
         * branch.
         */
        val x: Comp[∅, (Rep[Int], Rep[Int])] =
          handleCond(statefun_pair(prog3[(State[Rep[Int], *] ⊗ (Cnd ⊗ ∅))]) >>= { f => f(u) })
        val res: (Rep[Int], Rep[Int]) = x
        println(res)
        res._1

        /*
        val x: Comp[∅, Rep[Int]] =
          statefun(handleCond(prog3[(Cnd ⊗ (State[Rep[Int], *] ⊗ ∅))])) >>= { f => f(u) }
          //handleCond(statefun(prog3[(State[Rep[Int], *] ⊗ (Cnd ⊗ ∅))]) >>= { f => f(u) })
          //handleCond(statefun(prog3[(State[Rep[Int], *] ⊗ (Cnd ⊗ ∅))])) >>= { f => handleCond(f(u)) }
        val res: Rep[Int] = x
        println(res)
        res
         */
      }
    }
   */

  def unstagedCondTest() = {
    def prog1[R <: Eff](implicit I: State[Int, *] ∈ R): Comp[R, Int] =
      for {
        x <- get
        _ <- put(x * x)
        z <- get
      } yield z

    def prog2[R <: Eff](implicit I: State[Int, *] ∈ R): Comp[R, Int] =
      for {
        x <- get
        _ <- put(x + x)
        z <- get
      } yield z

    implicit object BoolSel extends Selector[NoRep] {
      def apply[A: Manifest](c: Boolean, x: A, y: A): A = if (c) x else y
    }

    def prog3[R <: Eff](implicit I1: State[Int, *] ∈ R, I2: Cnd ∈ R): Comp[R, Int] =
      for {
        x <- get
        y <- cond[R, Int](x == 0, prog1, prog2)
        z <- get
        _ <- put(z + 1)
        w <- get
      } yield w

    val init_n = 5;

    val x: Comp[∅, (Int, Int)] =
      handleCond(statefun_pair(prog3[(State[Int, *] ⊗ (Cnd ⊗ ∅))]) >>= { f => f(5) })
    //val x: Comp[∅, Int] =
    //  handleCond(statefun(prog3[(State[Int, *] ⊗ (Cnd ⊗ ∅))]) >>= { f => f(5) })

    val res = extract(x)

    println(res)
  }
}

object FreerExample {
  import Eff._
  import OpenUnion._
  import Freer._
  import Handlers._
  import State._
  import Nondet._

  def prog[R <: Eff](implicit I: State[Int, *] ∈ R): Comp[R, Int] = for {
    x <- get()
    y <- get()
    _ <- put(x * y)
    z <- get()
  } yield z

  def prog_ref: Comp[∅, Int] = stateref(2)(prog)
  def prog_ref2: Comp[∅, Int] = stateref2(2)(prog)
  def prog_fun: Comp[∅, Int] =
    for {
      f <- statefun[∅, Int, Int](prog)
    } yield f(2)
  def prog_fun2 = statefun[∅, Int, Int](prog)(2)
  def prog_ref_square: Comp[∅, Int] = stateref(2)(state_square(prog))
  def prog_ref2_square: Comp[∅, Int] = stateref2(2)(state_square(prog))
  def prog_fun_square: Comp[∅, Int] = statefun[∅, Int, Int](state_square(prog))(2)

  def runLocal[E <: Eff, S, A](s: S, prog: Comp[State[S, *] ⊗ (Nondet ⊗ E), A]): Comp[E, List[(S, A)]] = {
    /*
    val f1: Comp[Nondet ⊗ E, S => Comp[Nondet ⊗ E, (S, A)]] = statefun_pair[Nondet ⊗ E, S, A](prog)
    val f2: Comp[Nondet ⊗ E, (S, A)] = f1 >>= { g => g(s) }
    val f3: Comp[E, List[(S, A)]] = handleNondet(f2)
     */
    handleNondet(statefun_pair(prog) >>= { f => f(s) })
  }

  def runGlobal[E <: Eff, S, A](s: S, prog: Comp[Nondet ⊗ (State[S, *] ⊗ E), A]): Comp[E, (S, List[A])] = {
    val f1: Comp[State[S, *] ⊗ E, List[A]] = handleNondet[State[S, *] ⊗ E, A](prog)
    val f2: Comp[E, S => Comp[E, (S, List[A])]] = statefun_pair(f1)
    val f3: Comp[E, (S, List[A])] = f2 >>= { g => g(s) }
    f3
  }

  def select[E <: Eff, A](xs: List[A])(implicit I: Nondet ∈ E): Comp[E, A] =
    xs.map(Return[E, A]).foldRight[Comp[E, A]](fail)(choice)

  def inc[E <: Eff](implicit I: State[Int, *] ∈ E): Comp[E, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  /*
  def choices[E <: Eff, A](prog: Comp[E, A])
    (implicit I1: Nondet ∈ E, I2: State[Int, *] ∈ E): Comp[E, A] = {
    val h = ohandler[Nondet, Nondet, E, A, A] {
      case Return(x) => ret(x)
    } (ν[DeepHO[Nondet, Nondet, E, A]] {
      case Fail$() => fail[A, E]
      case Choice$((), k) => ???
    })
    h(prog)
  }
   */

  def main(args: Array[String]): Unit = {
    println(f"prog_ref: $prog_ref%d")
    println(f"prog_ref2: $prog_ref2%d")
    println(f"prog_fun: $prog_fun%d")
    println(f"prog_fun2: $prog_fun2%d")
    println(f"prog_ref_square: $prog_ref_square%d")
    println(f"prog_ref2_square: $prog_ref2_square%d")
    println(f"prog_fun_square: $prog_fun_square%d")
  }
}
