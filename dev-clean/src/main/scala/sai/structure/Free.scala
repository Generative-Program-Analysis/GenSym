package sai.structure.monad

import sai.structure.functor._

trait Free[F[_], A]
case class Pure[F[_], A](a: A) extends Free[F, A]
case class Impure[F[_], A](x: F[Free[F, A]]) extends Free[F, A]

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

