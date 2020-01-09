package sai.structure.monad

import sai.structure.functor._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

/* Set Monad Transformer */

object SetT {
  def apply[M[_]: Monad, A](implicit m: SetT[M, A]): SetT[M, A] = m

  implicit def SetTMonad[M[_]: Monad] = new Monad[SetT[M, ?]] {
    def flatMap[A, B](la: SetT[M, A])(f: A => SetT[M, B]) = la.flatMap(f)
    def pure[A](a: A): SetT[M, A] = SetT(Monad[M].pure(Set[A](a)))
    override def filter[A](la: SetT[M, A])(f: A => Boolean): SetT[M, A] = la.filter(f)
  }

  implicit def SetTMonadPlus[M[_]: Monad : MonadPlus] = new MonadPlus[SetT[M, ?]] {
    def mzero[A: Lattice]: SetT[M, A] = SetT(Monad[M].pure(Set[A]()))
    def mplus[A: Lattice](a: SetT[M, A], b: SetT[M, A]): SetT[M, A] = {
      SetT(MonadPlus[M].mplus(a.run, b.run))
    }
  }

  def fromSet[M[_]: Monad, A](xs: Set[A]): SetT[M, A] =
    SetT(Monad[M].pure(xs))

  def liftM[G[_]: Monad, A](ga: G[A]): SetT[G, A] =
    SetT(Monad[G].map(ga)((a: A) => Set(a)))

  def empty[M[_]: Monad, A]: SetT[M, A] = SetT(Monad[M].pure(Set[A]()))
}

case class SetT[M[_]: Monad, A](run: M[Set[A]]) {
  import SetT._
  def apply: M[Set[A]] = run
  def ++(ys: SetT[M, A]): SetT[M, A] =
    SetT(Monad[M].flatMap(run) { list1 =>
            Monad[M].flatMap(ys.run) { list2 =>
              Monad[M].pure(list1 ++ list2)
            }
          })

  def flatMap[B](f: A => SetT[M, B])(implicit mB: Manifest[B] = null): SetT[M, B] =
    SetT(Monad[M].flatMap(run) { (s: Set[A]) =>
           s.foldLeft(SetT.empty[M,B])((acc, a) => acc ++ f(a)).run
         })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): SetT[M, B] =
    SetT(Monad[M].flatMap(run) { s => Monad[M].pure(s.map(f)) })

  def filter(f: A => Boolean): SetT[M, A] =
    SetT(Monad[M].map(run) { list => list.filter(f) })
  def withFilter(f: A => Boolean): SetT[M, A] = filter(f)
}
