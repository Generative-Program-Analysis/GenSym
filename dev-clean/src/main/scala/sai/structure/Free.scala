package sai.structure.monad

import sai.structure.functor._
import sai.structure.NaturalTransformation

package free {
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

  object Example {
    trait ReaderWriter[I, O, X]
    case class Get[I, O, X](f: I => X) extends ReaderWriter[I, O, X]
    case class Put[I, O, X](o: O, f: Unit => X) extends ReaderWriter[I, O, X]
  }
}

package freer {
  abstract class FFree[F[_], A] {
    import freer.FFreeMonad._
    def flatMap[B](f: A => FFree[F, B]): FFree[F, B] = Monad[FFree[F, ?]].flatMap(this)(f)
    def map[B](f: A => B): FFree[F, B] = Monad[FFree[F, ?]].map(this)(f)
  }
  case class Stop[F[_], A](a: A) extends FFree[F, A]
  case class Step[F[_], A, X](x: F[X], k: X => FFree[F, A]) extends FFree[F, A]

  object FFree {
    def apply[F[_], A](implicit f: FFree[F, A]): FFree[F, A] = f
    def liftF[F[_], A](fa: F[A]): FFree[F, A] = Step[F, A, A](fa, Stop.apply)
    // TODO: what about A =/= X?
    def run[F[_], G[_]: Monad, A](f: FFree[F, A], nt: NaturalTransformation[F, G]): G[A] =
      f match {
        case Stop(a) => Monad[G].unit(a)
        case Step(fb: F[_], k) =>
          val res: G[_] = nt.transform(fb)
          Monad[G].flatMap(res) { a => run(k(a), nt) }
      }
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
          case Step(fb, k) => Step(fb, k.andThen(x => flatMap(x)(f)))
        }
        def filter[A](ma: FFree[F, A])(f: A => Boolean): FFree[F, A] =
          throw new Exception("Not supported")
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
      FFree.run(aProgram, executeIO)
    }
  }
}
