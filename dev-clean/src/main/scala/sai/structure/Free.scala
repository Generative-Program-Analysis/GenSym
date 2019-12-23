package sai.structure.monad

import sai.structure.functor._
import sai.structure.NaturalTransformation

abstract class Free[F[_]: Functor, A]
case class Pure[F[_]: Functor, A](a: A) extends Free[F, A]
case class Impure[F[_]: Functor, A](x: F[Free[F, A]]) extends Free[F, A]

object Free {
  def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f
}

object FreeFunctor {
  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
    new Functor[Free[F, ?]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x match {
        case Pure(a: A) => Pure(f(a))
        case Impure(x: F[Free[F, A]]) => Impure(Functor[F].map(x){
                                                  case xf: Free[F, A] =>
                                                    Functor[Free[F, ?]].map(xf)(f)
                                                })
      }
    }
}

object FreeMonad {
  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, ?]] =
    new Monad[Free[F, ?]] {
      def pure[A](a: A): Free[F, A] = Pure[F, A](a)
      def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma match {
        case Pure(a: A) => f(a)
        case Impure(x: F[Free[F, A]]) => Impure(Functor[F].map(x){ case xf: Free[F, A] =>
                                                  Monad[Free[F, ?]].flatMap(xf)(f)
                                                })
      }
      def filter[A](ma: Free[F, A])(f: A => Boolean): Free[F, A] =
        throw new Exception("Not supported")
    }
}

package free.alternative {
  abstract class Free[F[_], A] {
    import free.alternative.FreeMonad._
    def flatMap[B](f: A => Free[F, B]): Free[F, B] = Monad[Free[F, ?]].flatMap(this)(f)
    def map[B](f: A => B): Free[F, B] = Monad[Free[F, ?]].map(this)(f)
  }
  case class Stop[F[_], A](a: A) extends Free[F, A]
  case class Step[F[_], A, B](x: F[B], k: B => Free[F, A]) extends Free[F, A]

  object Free {
    def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f
    def liftF[F[_], A](fa: F[A]): Free[F, A] = Step[F, A, A](fa, Stop.apply)
    // TODO: what about A =/= B?
    def run[F[_], G[_]: Monad, A](f: Free[F, A], nt: NaturalTransformation[F, G]): G[A] =
      f match {
        case Stop(a) => Monad[G].unit(a)
        case Step(fb: F[_], k) =>
          val res: G[_] = nt.transform(fb)
          Monad[G].flatMap(res) { a => run(k(a), nt) }
      }
  }

  object FreeFunctor {
    implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
      new Functor[Free[F, ?]] {
        def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x match {
          case Stop(x: A) => Stop(f(x))
          case Step(fb: F[B], k: (B => Free[F, A])) =>
            Step(fb, (b: B) => Functor[Free[F, ?]].map(k(b))(f))
        }
      }
  }

  object FreeMonad {
    implicit def FreeMonadInstance[F[_]]: Monad[Free[F, ?]] =
      new Monad[Free[F, ?]] {
        def pure[A](a: A): Free[F, A] = Stop[F, A](a)
        def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma match {
          case Stop(x: A) => f(x)
          case Step(fb: F[B], k: (B => Free[F, A])) => //FIXME
            Step(fb, (b: B) => Monad[Free[F, ?]].flatMap(k(b))(f))
        }
        def filter[A](ma: Free[F, A])(f: A => Boolean): Free[F, A] =
          throw new Exception("Not supported")
      }
  }

  object Example {
    import FreeMonad._
    import IdMonadInstance._

    trait Interaction[A]
    case class Tell(s: String) extends Interaction[Unit]
    case class Ask(q: String) extends Interaction[String]

    type InteractionDsl[A] = Free[Interaction, A]

    def tell(s: String): InteractionDsl[Unit] = Free.liftF(Tell(s))
    def ask(q: String): InteractionDsl[String] = Free.liftF(Ask(q))

    def aProgram = for {
      _ <- tell("Hello")
      x <- ask("What is your name?")
      _ <- tell(s"Hi, $x!")
    } yield ()

    def execute[A](i: Interaction[A]): A = i match {
      case Tell(str) =>
        println(str); ()
      case Ask(q) =>
        println(q)
        scala.io.StdIn.readLine()
    }

    def run[A](prog: InteractionDsl[A]): A = prog match {
      case Stop(a) => a
      case Step(fb, k) =>
        val res = execute(fb)
        run(k(res))
    }

    def executeIO = new NaturalTransformation[Interaction, Id] {
      override def transform[A](fa: Interaction[A]) = fa match {
        case Tell(str) =>
          println(str); ()
        case Ask(q) =>
          println(q)
          scala.io.StdIn.readLine()
      }
    }

    def main(args: Array[String]): Unit = {
      //run(aProgram)
      Free.run(aProgram, executeIO)
    }
  }
}
