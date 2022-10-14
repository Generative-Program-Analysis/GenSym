package gensym.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import gensym.lmsx._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

@virtualize
trait RepEitherMonad { self: RepMonads with SAIOps =>
  object EitherT {
    def apply[M[_]: Monad, E, A](implicit m: EitherT[M, E, A]) = m

    implicit def EitherTMonadInstance[M[_]: Monad, E: Manifest] = new Monad[EitherT[M, E, *]] {
      def flatMap[A: Manifest, B: Manifest](fa: EitherT[M, E, A])(f: Rep[A] => EitherT[M, E, B]): EitherT[M, E, B] = fa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): EitherT[M, E, A] = EitherT(Monad[M].pure(Either.right[E, A](a)))
    }

    def liftM[G[_]: Monad, E: Manifest, A: Manifest](ga: G[A]): EitherT[G, E, A] =
      EitherT(Monad[G].map(ga)(Either.right(_)))

    def left[M[_]: Monad, E: Manifest, A: Manifest](e: Rep[E]): EitherT[M, E, A] =
      EitherT(Monad[M].pure(Either.left(e)))

    def right[M[_]: Monad, E: Manifest, A: Manifest](a: Rep[A]): EitherT[M, E, A] =
      EitherT(Monad[M].pure(Either.right(a)))
  }

  case class EitherT[M[_]: Monad, E: Manifest, A: Manifest](run: M[Either[E, A]]) {
    import EitherT._

    def apply: M[Either[E, A]] = run
    def flatMap[B: Manifest](f: Rep[A] => EitherT[M, E, B]): EitherT[M, E, B] =
      EitherT(Monad[M].flatMap(run) { case e: Rep[Either[E, A]] =>
        Unwrap(e) match {
          case Adapter.g.Def("either-new-right", mE::mB::(x: Backend.Exp)::Nil) =>
            f(Wrap[A](x)).run
          case Adapter.g.Def("either-new-left", unwrapped_xs) =>
            val l = Wrap[Either[E, B]](Adapter.g.reflect("either-new-right", unwrapped_xs:_*))
            Monad[M].pure(l)
          case _ => ???
        }
      })
    def map[B: Manifest](f: Rep[A] => Rep[B]): EitherT[M, E, B] =
      EitherT(Monad[M].flatMap(run) { case e: Rep[Either[E, A]] =>
        Unwrap(e) match {
          case Adapter.g.Def("either-new-right", mE::mB::(x: Backend.Exp)::Nil) =>
            val r = Either.right[E, B](f(Wrap[A](x)))
            Monad[M].pure(r)
          case Adapter.g.Def("either-new-left", mE::mB::(x: Backend.Exp)::Nil) =>
            val l = Either.left[E, B](Wrap[E](x))
            Monad[M].pure(l)
          case _ => ???
        }
      })
    def filter(f: Rep[A] => Rep[Boolean]): EitherT[M, E, A] = ???
  }
}

