package sai.structure.monad
package freer

/*

import sai.structure.functor._
import sai.structure.~>

abstract class FFree[F[_], A] {
  import freer.FFreeMonad._
  def flatMap[B](f: A => FFree[F, B]): FFree[F, B] = Monad[FFree[F, ?]].flatMap(this)(f)
  def map[B](f: A => B): FFree[F, B] = Monad[FFree[F, ?]].map(this)(f)

  def foldMap[G[_]: Monad](nt: F ~> G): G[A] = this match {
    case Stop(a) => Monad[G].unit(a)
    case Step(fb, k) =>
      Monad[G].flatMap(nt.transform(fb)) { a => k(a).foldMap(nt) }
  }
}
case class Stop[F[_], A](a: A) extends FFree[F, A]
case class Step[F[_], A, X](x: F[X], k: X => FFree[F, A]) extends FFree[F, A]

object FFree {
  def apply[F[_], A](implicit f: FFree[F, A]): FFree[F, A] = f
  def liftF[F[_], A](fa: F[A]): FFree[F, A] = Step[F, A, A](fa, Stop.apply)
  def run[F[_], G[_]: Monad, A](f: FFree[F, A], nt: F ~> G): G[A] = f.foldMap(nt)
}

object FFreeFunctor {
  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[FFree[F, ?]] =
    new Functor[FFree[F, ?]] {
      def map[A, B](x: FFree[F, A])(f: A => B): FFree[F, B] = x match {
        case Stop(x) => Stop(f(x))
        case Step(fb, k) => Step(fb, k.andThen(x => map(x)(f)))
      }
    }
}

object FFreeMonad {
  // Note: we do not require F[_] to be a Functor (See Oleg & Ishii)
  implicit def FreeMonadInstance[F[_]]: Monad[FFree[F, ?]] =
    new Monad[FFree[F, ?]] {
      def pure[A](a: A): FFree[F, A] = Stop[F, A](a)
      def flatMap[A, B](ma: FFree[F, A])(f: A => FFree[F, B]): FFree[F, B] = ma match {
        case Stop(x) => f(x)
        case Step(fb, k) => Step(fb, k.andThen((x: FFree[F, A]) => flatMap(x)(f)))
      }
    }
}

object Example2 {
  import FFreeMonad._
  import IdMonadInstance._

  type EnvM[A] = FFree[Env, A]

  trait Env[A]
  case class Local[A](f: Map[String, Int] => Map[String, Int], m: EnvM[A]) extends Env[A]
  case class Ask() extends Env[Map[String, Int]]

  def local[A](f: Map[String, Int] => Map[String, Int], m: EnvM[A]): EnvM[A] =
    FFree.liftF(Local[A](f, m))
  def ask(): EnvM[Map[String, Int]] = FFree.liftF(Ask())

  def aProgram = for {
    a <- local(m => m + ("a" -> 1), ask)
  } yield a

  def executeEnv = new (Env ~> Id) {
    override def transform[A](fa: Env[A]) = fa match {
      case Local(f, m) => ???
      case Ask() => ???
    }
  }
}

object Example {
  import FFreeMonad._
  import IdMonadInstance._

  trait Interaction[A]
  case class Tell(s: String) extends Interaction[Unit]
  case class Ask(q: String) extends Interaction[String]

  type InteractionDsl[A] = FFree[Interaction, A]

  def tell(s: String): InteractionDsl[Unit] = FFree.liftF(Tell(s))
  def ask(q: String): InteractionDsl[String] = FFree.liftF(Ask(q))

  def aProgram = for {
    _ <- tell("Hello")
    x <- ask("What is your name?")
    _ <- tell(s"Hi, $x!")
  } yield ()

  object Console1 extends (Interaction ~> Id) {
    def transform[A](fa: Interaction[A]) = fa match {
      case Tell(str) =>
        println(str); ()
      case Ask(q) =>
        println(q)
        scala.io.StdIn.readLine()
    }
  }

  type StateCon[A] = Map[String, String] => (List[String], A)

  implicit val StateConMonad = new Monad[StateCon] {
    def pure[A](a: A) = _ => (List(), a)
    def flatMap[A, B](s: StateCon[A])(f: A => StateCon[B]) = m => {
      val (o1, a) = s(m)
      val (o2, b) = f(a)(m)
      (o1 ++ o2, b)
    }
  }

  object Console2 extends (Interaction ~> StateCon) {
    def transform[A](fa: Interaction[A]) = fa match {
      case Tell(str) => _ => (List(str), ())
      case Ask(q) => m => (List(), m(q))
    }
  }

  def main(args: Array[String]): Unit = {
    //FFree.run(aProgram, Console1)
    val r = FFree.run(aProgram, Console2).apply(Map("What is your name?" -> "kkk"))
    println(r)
  }
}

*/
