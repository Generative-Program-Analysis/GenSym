package sai.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import sai.lmsx._
import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

@virtualize
trait RepCPSMonad { self: RepMonads with SAIOps =>
  object CpsM{
    def apply[R: Manifest, A: Manifest](implicit m: CpsM[R, A]): CpsM[R, A] = m

    implicit def CpsMonadInstance[R: Manifest] = new Monad[CpsM[R, ?]] {
      def flatMap[A: Manifest, B: Manifest](fa: CpsM[R, A])(f: Rep[A] => CpsM[R, B]) = fa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): CpsM[R, A] = CpsM((k: Rep[A] => Rep[R]) => k(a))
    }

    def callcc[T: Manifest, R: Manifest, A: Manifest](f: (Rep[T] => CpsM[R, A]) => CpsM[R, T]): CpsM[R, T] =
      CpsM[R, T]((k: Rep[T] => Rep[R]) => f((x: Rep[T]) => CpsM[R, A]((_: (Rep[A] => Rep[R])) => k(x))).run(k))
  }

  case class CpsM[R: Manifest, A: Manifest](run: (Rep[A] => Rep[R]) => Rep[R]) {
    import CpsM._

    def apply(k: Rep[A] => Rep[R]): Rep[R] = run(k)
    def flatMap[B: Manifest](f: Rep[A] => CpsM[R, B]): CpsM[R, B] =
      CpsM[R, B]((k: Rep[B] => Rep[R]) => run(f(_)(k)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): CpsM[R, B] =
      CpsM[R, B]((k: Rep[B] => Rep[R]) => run(f.andThen(k)))
  }
}
