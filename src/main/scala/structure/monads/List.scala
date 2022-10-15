package gensym.structure.monad

import gensym.structure.functor._
import gensym.structure.lattices._
import gensym.structure.lattices.Lattices._

/* List Monad Transformer */

object ListT {
  def apply[M[_]: Monad, A](implicit m: ListT[M, A]): ListT[M, A] = m

  implicit def ListTMonad[M[_]: Monad] = new Monad[ListT[M, *]] {
    def flatMap[A, B](la: ListT[M, A])(f: A => ListT[M, B]) = la.flatMap(f)
    def pure[A](a: A): ListT[M, A] = ListT(Monad[M].pure(List(a)))
    override def filter[A](la: ListT[M, A])(f: A => Boolean): ListT[M, A] = la.filter(f)
  }

  implicit def ListTMonadPlus[M[_]: Monad : MonadPlus] = new MonadPlus[ListT[M, *]] {
    def mzero[A: Lattice]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
    def mplus[A: Lattice](a: ListT[M, A], b: ListT[M, A]): ListT[M, A] = {
      ListT(MonadPlus[M].mplus(a.run, b.run))
    }
  }

  def fromList[M[_]: Monad, A](xs: List[A]): ListT[M, A] =
    ListT(Monad[M].pure(xs))

  def liftM[G[_]: Monad, A](ga: G[A]): ListT[G, A] =
    ListT(Monad[G].map(ga)((a: A) => List(a)))

  def empty[M[_]: Monad, A]: ListT[M, A] = ListT(Monad[M].pure(List[A]()))
}

case class ListT[M[_]: Monad, A](run: M[List[A]]) {
  import ListT._
  def apply: M[List[A]] = run
  def ++(ys: ListT[M, A]): ListT[M, A] =
    ListT(Monad[M].flatMap(run) { list1 =>
      Monad[M].map(ys.run) { list2 =>
        list1 ++ list2
      }
    })

  def flatMap[B](f: A => ListT[M, B])(implicit mB: Manifest[B] = null): ListT[M, B] =
    ListT(Monad[M].flatMap(run) { (xs: List[A]) =>
      xs.foldLeft(ListT.empty[M,B])((acc, x) => acc ++ f(x)).run
    })

  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ListT[M, B] =
    ListT(Monad[M].flatMap(run) { list => Monad[M].pure(list.map(f)) })

  def filter(f: A => Boolean): ListT[M, A] =
    ListT(Monad[M].map(run) { list => list.filter(f) })
  def withFilter(f: A => Boolean): ListT[M, A] = filter(f)
}

