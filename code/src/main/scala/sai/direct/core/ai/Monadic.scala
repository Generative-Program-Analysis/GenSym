package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import simulacrum._

import sai.utils._
import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOps => _, SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

/* Scala implementation for paper The Essence of Functional Programming, Philip Wadler */

object Lang {
  type Name = String
  trait Term
  case class Var(x: String) extends Term
  case class Con(i: Int) extends Term
  case class Add(e1: Term, e2: Term) extends Term
  case class Lam(x: Name, e: Term) extends Term
  case class App(e1: Term, e2: Term) extends Term

  case class Fail() extends Term
  case class Amb(e1: Term, e2: Term) extends Term

  case class Tick() extends Term
  case class Fetch() extends Term
}

object Monadics {
  import Lang._

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  trait Apply[F[_]] extends Functor[F] {
    def ap[A, B](fa: F[A])(f: F[A => B]): F[B]
  }

  trait Bind[F[_]] extends Apply[F] {
    def bind[A, B](fa: F[A])(f: A => F[B]): F[B]
  }

  trait Applicative[F[_]] extends Apply[F] {
    def point[A](a: A): F[A]
  }

  //trait Monad[M[_]] extends Applicative[M] with Bind[M] {
  trait Monad[M[_]] {
    def pure[A](a: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    def map[A,B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => pure(f(a)))
    def map2[A,B,C](ma: M[A], mb: M[B])(f: (A, B) => C): M[C] =
      flatMap(ma)(a => map(mb)(b => f(a, b)))
  }

  object Monad {
    def apply[M[_]](implicit m: Monad[M]): Monad[M] = m
  }

  implicit class MonadOps[M[_]: Monad, A](ma: M[A]) {
    def map[B](f: A => B): M[B] = Monad[M].map(ma)(f)
    def flatMap[B](f: A => M[B]): M[B] = Monad[M].flatMap(ma)(f)
  }

  trait Semantics {
    type Env
    type Value
    type M[T]
    implicit val m: Monad[M]

    def num(i: Int): M[Value]
    def close(lam: Lam, env: Env): M[Value]
    def add(v1: Value, v2: Value): M[Value]
    def apply(v1: Value, v2: Value): M[Value]
    def lookup(x: Name, env: Env): M[Value]
    def extend(env: Env, x: Name, v: Value): Env

    def interp(e: Term, env: Env): M[Value] = e match {
      case Con(i) => num(i)
      case Var(x) => lookup(x, env)
      case Lam(x, e) => close(Lam(x, e), env)
      case Add(e1, e2) => for {
        v1 <- interp(e1, env)
        v2 <- interp(e2, env)
        rt <- add(v1, v2)
      } yield rt
      case App(e1, e2) => for {
        v1 <- interp(e1, env)
        v2 <- interp(e2, env)
        rt <- apply(v1, v2)
      } yield rt
    }
  }

  //trait Concrete extends Semantics {
  trait Concrete { self: Semantics =>
    trait Value
    case object Wrong extends Value
    case class Num(i: Int) extends Value
    case class Fun(f: Value => M[Value]) extends Value

    type Env = Map[Name, Value]

    def num(i: Int): M[Value] = Monad[M].pure(Num(i))

    def close(lam: Lam, env: Env): M[Value] = lam match {
      case Lam(x, e) => Monad[M].pure(Fun(v => interp(e, extend(env, x, v))))
    }

    def lookup(x: Name, env: Env): M[Value] = env.get(x) match {
      case Some(v) => Monad[M].pure(v)
      case None => Monad[M].pure(Wrong)
    }

    def extend(env: Env, x: Name, v: Value) = env + (x -> v)

    def add(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Num(x), Num(y)) => Monad[M].pure(Num(x + y))
      case _ => Monad[M].pure(Wrong)
    }

    def apply(v1: Value, v2: Value): M[Value] = (v1, v2) match {
      case (Fun(k), a) => k(a)
      case _ => Monad[M].pure(Wrong)
    }
  }

  /****************************************************/

  type Id[T] = T
  implicit val IdMonad: Monad[Id] = new Monad[Id] {
    def pure[A](a: A): A = a
    def flatMap[A, B](ma: Id[A])(f: A => Id[B]): Id[B] = f(ma)
  }

  //case class StandardInterp() extends Concrete {
  case class StandardInterp() extends Concrete with Semantics {
    type M[T] = Id[T]
    implicit val m = IdMonad
  }

  /****************************************************/

  abstract class ErrMsg[T]
  case class Success[T](a: T) extends ErrMsg[T]
  case class Error[T](msg: String) extends ErrMsg[T]

  implicit val errMonad: Monad[ErrMsg] = new Monad[ErrMsg] {
    def pure[A](a: A) = Success(a)
    def flatMap[A, B](m: ErrMsg[A])(f: A => ErrMsg[B]) = m match {
      case Success(a) => f(a)
      case Error(msg) => Error(msg)
    }
  }

  case class ErrMsgInterp() extends Concrete with Semantics {
    type M[T] = ErrMsg[T]
    implicit val m = errMonad
  }

  /****************************************************/

  type IntState[T] = Int => (T, Int)
  implicit val intStateMonad: Monad[IntState] = new Monad[IntState] {
    def pure[A](a: A) = s => (a, s)
    def flatMap[A, B](m: IntState[A])(f: A => IntState[B]) = s => {
      val (a, s1) = m(s)
      val (b, s2) = f(a)(s1)
      (b, s2)
    }
  }

  case class StateInterp() extends Concrete with Semantics {
    type M[T] = IntState[T]
    implicit val m = intStateMonad
  }

  /****************************************************/

  implicit val ndMonad: Monad[List] = new Monad[List] {
    def pure[A](a: A) = List(a)
    def flatMap[A, B](m: List[A])(f: A => List[B]) =
      for (a <- m; b <- f(a)) yield b
  }

  case class NDInterp() extends Concrete with Semantics {
    type M[T] = List[T]
    implicit val m = ndMonad
    override def interp(e: Term, env: Env) = e match {
      case Fail() => List()
      case Amb(e1, e2) => interp(e1, env) ++ interp(e2, env)
      case _ => super.interp(e, env)
    }
  }

  /****************************************************/

  trait MonadReader[F[_], S] extends Monad[F] {
    def ask: F[S]
    def local[A](f: S => S)(fa: F[A]): F[A]
  }
  object MonadReader {
    def apply[F[_], S](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
  }

  trait ReaderSemantics extends Semantics {
    implicit val m: MonadReader[M, Env]
    import m._

    def interp(e: Term): M[Value] = e match {
      case Con(i) => num(i)
      case Var(x) => for {
        env <- ask
        v <- lookup(x, env)
      } yield v
      case Lam(x, e) => for {
        env <- ask
        cls <- close(Lam(x, e), env)
        env_* <- local(extend(_, "dummy", cls))(ask)
      } yield cls
      case Add(e1, e2) => for {
        v1 <- interp(e1)
        v2 <- interp(e2)
        rt <- add(v1, v2)
      } yield rt
      case App(e1, e2) => for {
        v1 <- interp(e1)
        v2 <- interp(e2)
        rt <- apply(v1, v2)
      } yield rt
    }
  }

  case class Reader[R, A](run: R => A)
  def ReaderMonad[R] = new MonadReader[Reader[R, ?], R] {
    def pure[A](a: A): Reader[R, A] = Reader(_ => a)
    def flatMap[A, B](ra: Reader[R, A])(f: A => Reader[R, B]): Reader[R, B] =
      Reader(r => f(ra.run(r)).run(r))
    def ask: Reader[R, R] = Reader(r => r)
    def local[A](f: R => R)(fa: Reader[R, A]): Reader[R, A] =
      Reader(r => fa.run(f(r)))
  }

  case class ReaderInterp() extends Concrete with ReaderSemantics {
    type M[T] = Reader[Env, T]
    implicit val m = ReaderMonad[Env]
  }

  trait MonadTrans[T[_[_], _]] {
    def liftM[M[_]: Monad, A](a: M[A]): T[M, A]
    implicit def apply[M[_]: Monad]: Monad[T[M, ?]]
  }

  case class IdT[F[_]: Monad, A](run: F[A])

  object IdT {// extends MonadTrans[IdT] {
    def liftM[M[_]: Monad, A](a: M[A]): IdT[M, A] = IdT[M, A](a)
    def apply[M[_]: Monad, A](implicit m: IdT[M, A]): IdT[M, A] = m
    implicit def apply[M[_]: Monad]: Monad[IdT[M, ?]] = IdTMonad[M]
  }

  def IdTMonad[F[_]: Monad] = new Monad[IdT[F, ?]] {
    def flatMap[A, B](fa: IdT[F, A])(f: A => IdT[F, B]): IdT[F, B] =
      IdT(fa.run.flatMap(a => f(a).run))
    def pure[A](a: A): IdT[F, A] = IdT(Monad[F].pure(a))
  }

  //IdT Id example
  /*
   implicit val m: Monad[IdT[Id, ?]] = IdTMonad[Id] // IdT[Id]
   def id2M(x: Int): IdT[Id, Int] = IdT[Id, Int](x)
   //val three: IdT[Id, Int] = Monad[IdT[Id, ?]].pure(3)
   val a = new MonadOps[IdT[Id, ?], Int](id2M(3))
   val b = new MonadOps[IdT[Id, ?], Int](id2M(4))
   val z = for {x <- a; y <- b } yield x + y
   IdT.liftM[Id, Int](1)
   */

  case class StandardInterp2() extends Concrete with Semantics {
    type M[T] = IdT[Id, T]
    implicit val m = IdTMonad[Id]
  }

  case class ReaderT[M[_]: Monad, R, A](run: R => M[A])
  object ReaderT {
    def apply[M[_]: Monad, R, A](implicit m: ReaderT[M, R, A]): ReaderT[M, R, A] = m
    implicit def apply[M[_]: Monad, R]: Monad[ReaderT[M, R, ?]] = ReaderTMonad[M, R]
  }
  def ReaderTMonad[M[_]: Monad, R] = new MonadReader[ReaderT[M, R, ?], R] {
    def pure[A](a: A): ReaderT[M, R, A] = ReaderT(_ => Monad[M].pure(a))
    def flatMap[A, B](ra: ReaderT[M, R, A])(f: A => ReaderT[M, R, B]): ReaderT[M, R, B] =
      ReaderT(r => ra.run(r).flatMap(a => f(a).run(r)))
    def ask: ReaderT[M, R, R] = ReaderT(r => Monad[M].pure(r))
    def local[A](f: R => R)(fa: ReaderT[M, R, A]): ReaderT[M, R, A] =
      ReaderT(r => fa.run(f(r)))
  }

  case class ReaderInterp2() extends Concrete with ReaderSemantics {
    type M[T] = ReaderT[Id, Env, T]
    implicit val m = ReaderTMonad[Id, Env]
  }

  /****************************************************/

  trait MonadState[F[_], S] extends Monad[F] {
    def get: F[S]
    def put(s: S): F[Unit]
    def mod(f: S => S): F[Unit]
  }

  object MonadState {
    def apply[F[_], S](implicit s: MonadState[F, S]): MonadState[F, S] = s
  }

  case class State[S, A](run: S => (A, S))
  def stateMonad[S] = new MonadState[State[S, ?], S] {
    def pure[A](a: A): State[S, A] = State(s => (a, s))
    def flatMap[A, B](sa: State[S, A])(f: A => State[S, B]): State[S, B] =
      State(s => { val (a, s1) = sa.run(s); f(a).run(s1) })
    def get: State[S, S] = State(s => (s, s))
    def put(s: S): State[S, Unit] = State(_ => ((), s))
    def mod(f: S => S): State[S, Unit] = State(s => ((), f(s)))
  }

  case class StateT[M[_]: Monad, S, A](run: S => M[(A, S)])
  object StateT {
    def apply[M[_]: Monad, S, A](implicit m: StateT[M, S, A]): StateT[M, S, A] = m
    implicit def apply[M[_]: Monad, S]: Monad[StateT[M, S, ?]] = StateTMonad[M, S]
  }
  def StateTMonad[M[_]: Monad, S] = new MonadState[StateT[M, S, ?], S] {
    def pure[A](a: A): StateT[M,S,A] = StateT(s => Monad[M].pure((a, s)))
    def flatMap[A, B](sa: StateT[M,S,A])(f: A => StateT[M,S,B]): StateT[M,S,B] =
      StateT(s => sa.run(s).flatMap { case (a, s1) => f(a).run(s1) })
    def get: StateT[M,S,S] = StateT(s => Monad[M].pure((s, s)))
    def put(s: S): StateT[M,S,Unit] = StateT(s => Monad[M].pure(((), s)))
    def mod(f: S => S): StateT[M,S,Unit] = StateT(s => Monad[M].pure(((), f(s))))
  }

  /*
  case class ReaderStateInterp() extends ReaderSemantics {
    type Store = Int
    type M[T] = ReaderT[StateT[Id, Store, ?], Env, T]
    implicit val s = StateTMonad[Id, Store]
    implicit val m = ReaderTMonad[StateT[Id, Store, ?], Env]
    import m._
    import s._
    override def interp(e: Term): M[Value] = e match {
      case Tick() => for {
        _ <- mod(_ + 1)
        t <- get
        i <- num(t)
      } yield i
      case Fetch() => for {
        t <- get
        i <- num(t)
      } yield i
      case _ => super.interp(e)
    }
  }
   */


  /****************************************************/

  def main(args: Array[String]) = {
    val term1 = App(Lam("x", Add(Var("x"), Var("x"))),
                    (Add(Con(10), Con(11))))
    val interp1 = StandardInterp()
    println(interp1.interp(term1, Map()))

    val interp11 = StandardInterp()
    println(interp1.interp(term1, Map()))

    val readerInterp = ReaderInterp()
    println(readerInterp.interp(term1).run(Map()))

    val term2 = App(Lam("x", Add(Var("x"), Var("x"))), (Amb(Con(1), Con(2))))
    val interp2 = NDInterp()
    println(interp2.interp(term2, Map()))
  }

}