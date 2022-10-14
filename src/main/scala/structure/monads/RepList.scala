package gensym.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import gensym.lmsx._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

@virtualize
trait RepListMonad { self: RepMonads with SAIOps =>
  object ListM {
    def apply[A: Manifest](implicit m: ListM[A]): ListM[A] = m

    implicit def ListMonad = new Monad[ListM] {
      def flatMap[A: Manifest, B: Manifest](la: ListM[A])(f: Rep[A] => ListM[B]) = la.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ListM[A] = ListM[A](List(a))
      override def filter[A: Manifest](la: ListM[A])(f: Rep[A] => Rep[Boolean]): ListM[A] = la.filter(f)
    }

    implicit def ListMonadPlus = new MonadPlus[ListM] {
      def mzero[A: Manifest : RepLattice]: ListM[A] = ListM(List())
      def mplus[A: Manifest : RepLattice](a: ListM[A], b: ListM[A]): ListM[A] = a ++ b
    }

    def fromList[A: Manifest](xs: Rep[List[A]]): ListM[A] = ListM(xs)
    def empty[A: Manifest]: ListM[A] = ListM[A](List())
  }

  case class ListM[A: Manifest](run: Rep[List[A]]) {
    def apply: Rep[List[A]] = run
    def ++(ys: ListM[A]): ListM[A] = ListM(run ++ ys.run)
    def flatMap[B: Manifest](f: Rep[A] => ListM[B]): ListM[B] = {
      ListM[B](run.foldLeft(List[B]())({ case (acc, a) =>
        acc ++ f(a).run
      }))
    }
    def map[B: Manifest](f: Rep[A] => Rep[B]): ListM[B] = ListM[B](run.map(f))
    def filter(f: Rep[A] => Rep[Boolean]): ListM[A] = ListM[A](run.filter(f))
    def withFilter(f: Rep[A] => Rep[Boolean]): ListM[A] = filter(f)
  }

  object ListT {
    def apply[M[_]: Monad, A: Manifest](implicit m: ListT[M, A]): ListT[M, A] = m

    implicit def ListTMonad[M[_]: Monad] = new Monad[ListT[M, *]] {
      def flatMap[A: Manifest, B: Manifest](la: ListT[M, A])(f: Rep[A] => ListT[M, B]) = la.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ListT[M, A] = ListT(Monad[M].pure(List(a)))
      override def filter[A: Manifest](la: ListT[M, A])(f: Rep[A] => Rep[Boolean]): ListT[M, A] = la.filter(f)
    }

    implicit def ListTMonadPlus[M[_]: Monad] = new MonadPlus[ListT[M, *]] {
      def mzero[A: Manifest : RepLattice]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
      def mplus[A: Manifest : RepLattice](a: ListT[M, A], b: ListT[M, A]): ListT[M, A] = a ++ b
    }

    def fromList[M[_]: Monad, A: Manifest](xs: Rep[List[A]]): ListT[M, A] =
      ListT(Monad[M].pure(xs))
    def liftM[G[_]: Monad, A: Manifest](ga: G[A]): ListT[G, A] =
      ListT(Monad[G].map(ga)((a: Rep[A]) => List(a)))

    def empty[M[_]: Monad, A: Manifest]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
  }

  case class ListT[M[_]: Monad, A: Manifest](run: M[List[A]]) {
    import ListT._

    def apply: M[List[A]] = run

    def ++(ys: ListT[M, A]): ListT[M, A] =
      ListT(Monad[M].flatMap(run) { list1 => Monad[M].map(run) { list2 => list1 ++ list2 } })

    def flatMap[B: Manifest](f: Rep[A] => ListT[M, B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { case xs: Rep[List[A]] =>
        val zero: Rep[List[B]] = List[B]()
        // flatMap: (Rep[A] => Rep[List[B]]) => Rep[List[B]]
        val bs: Rep[List[B]] = xs.foldLeft(zero) { case (acc, x) =>
          val mb: M[List[B]] = f(x).run
          val mb1: M[List[B]] = Monad[M].map(mb) { case listb: Rep[List[B]] => acc ++ listb }
          ???
        }
        Monad[M].pure(bs)
      })

    def map[B: Manifest](f: Rep[A] => Rep[B]): ListT[M, B] =
      ListT(Monad[M].flatMap(run) { list => Monad[M].pure(list.map(f)) })

    def filter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      ListT(Monad[M].map(run) { list => list.filter(f) })

    def withFilter(f: Rep[A] => Rep[Boolean]): ListT[M, A] =
      filter(f)
  }
}
