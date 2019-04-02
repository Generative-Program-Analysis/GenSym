package sai
package monads

import sai.lattices._
import sai.lattices.Lattices._

trait RMonad[R[_], M[_]] {
  def pure[A](a: R[A]): M[A]
  def flatMap[A, B](ma: M[A])(f: R[A] => M[B]): M[B]
  def map[A,B](ma: M[A])(f: R[A] => R[B]): M[B] = flatMap(ma)(a => pure(f(a)))
  def filter[A](ma: M[A])(f: R[A] => R[Boolean]): M[A]
}

/*
trait RMonad[R[_], M[_]] {
  def pure[A](a: R[A])(implicit mA: Manifest[A] = null): M[A]
  def flatMap[A, B](ma: M[A])(f: R[A] => M[B])
             (implicit mA: Manifest[A] = null, mB: Manifest[B] = null): M[B]
  def map[A,B](ma: M[A])(f: R[A] => R[B])
         (implicit mA: Manifest[A] = null, mB: Manifest[B] = null): M[B] = flatMap(ma)(a => pure(f(a)))
  def filter[A](ma: M[A])(f: R[A] => R[Boolean])(implicit mA: Manifest[A] = null): M[A]
}
*/

trait Monad[M[_]] extends RMonad[NoRep.NoRep, M]

object Monad {
  def apply[M[_]](implicit m: Monad[M]): Monad[M] = m

  def mapM[M[_]: Monad, A, B](xs: List[A])(f: A => M[B])(implicit mB: Manifest[B] = null): M[List[B]] = xs match {
    case Nil => Monad[M].pure(List.empty[B])
    case x::xs => Monad[M].flatMap(f(x)) { b =>
      Monad[M].flatMap(mapM(xs)(f)) { bs =>
        Monad[M].pure(b::bs)
      }
    }
  }

  // Returns the last result
  // TODO: test
  def forM[M[_]: Monad, A, B](xs: List[A])(f: A => M[B])(implicit mB: Manifest[B] = null): M[B] = xs match {
    case x::Nil => f(x)
    case x::xs => Monad[M].flatMap(f(x)) { _ => forM(xs)(f) }
  }
}

trait MonadPlus[M[_]] {
  def mzero[A: Lattice]: M[A]
  def mplus[A: Lattice](a: M[A], b: M[A]): M[A]
}

object MonadPlus {
  def apply[M[_]](implicit m: MonadPlus[M]): MonadPlus[M] = m
}

/////////////////////////////////////////////////

@deprecated("Use IdM", "")
object IdMonadInstance {
  type Id[T] = T
  implicit val IdMonad: Monad[Id] = new Monad[Id] {
    def pure[A](a: A): A = a
    def flatMap[A, B](ma: Id[A])(f: A => Id[B]): Id[B] = f(ma)
    def filter[A](ma: Id[A])(f: A => Boolean): Id[A] = throw new Exception("Not supported")
  }

  implicit val IdMonadPlus: MonadPlus[Id] = new MonadPlus[Id] {
    def mzero[A: Lattice]: A = Lattice[A].bot
    def mplus[A: Lattice](a: A, b: A) = Lattice[A].⊔(a, b)
  }
}

object IdM {
  def apply[A](implicit m: IdM[A]): IdM[A] = m

  implicit val IdMonadInstance: Monad[IdM] = new Monad[IdM] {
    def pure[A](a: A): IdM[A] = IdM(a)
    def flatMap[A, B](ma: IdM[A])(f: A => IdM[B]): IdM[B] = ma.flatMap(f)
    def filter[A](ma: IdM[A])(f: A => Boolean): IdM[A] = throw new Exception("Not supported")
  }

  implicit val IdMonadPlus: MonadPlus[IdM] = new MonadPlus[IdM] {
    def mzero[A: Lattice]: IdM[A] = IdM(Lattice[A].bot)
    def mplus[A: Lattice](a: IdM[A], b: IdM[A]): IdM[A] = IdM(a.run ⊔ b.run)
  }
}

case class IdM[A](run: A) {
  def apply: A = run
  def flatMap[B](f: A => IdM[B])(implicit mB: Manifest[B] = null): IdM[B] = f(run)
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): IdM[B] = IdM(f(run))
}

/////////////////////////////////////////////////

trait MonadReader[F[_], R] extends Monad[F] {
  def ask: F[R]
  def local[A](fa: F[A])(f: R => R): F[A]
}

object MonadReader {
  def apply[F[_], S](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
}

object ReaderT {
  def apply[M[_]: Monad, R, A](implicit m: ReaderT[M, R, A]): ReaderT[M, R, A] = m
  implicit def apply[M[_]: Monad, R]: Monad[ReaderT[M, R, ?]] = ReaderTMonad[M, R]

  implicit def ReaderTMonad[M[_]: Monad, R] = new MonadReader[ReaderT[M, R, ?], R] {
    def flatMap[A, B](fa: ReaderT[M, R, A])(f: A => ReaderT[M, R, B]) = fa.flatMap(f)
    def pure[A](a: A): ReaderT[M, R, A] = ReaderT(_ => Monad[M].pure(a))
    def filter[A](fa: ReaderT[M, R, A])(f: A => Boolean): ReaderT[M, R, A] = fa.filter(f)

    def ask: ReaderT[M, R, R] = ReaderT(r => Monad[M].pure(r))
    def local[A](fa: ReaderT[M, R, A])(f: R => R): ReaderT[M, R, A] =
      ReaderT(f andThen fa.run)
  }
  implicit def ReaderTMonadPlus[M[_]: Monad : MonadPlus, R] = new MonadPlus[ReaderT[M, R, ?]] {
    def mzero[A: Lattice]: ReaderT[M, R, A] = ReaderT(r => MonadPlus[M].mzero)
    def mplus[A: Lattice](a: ReaderT[M, R, A], b: ReaderT[M, R, A]): ReaderT[M, R, A] =
      ReaderT(r => MonadPlus[M].mplus(a.run(r), b.run(r)))
  }

  def liftM[G[_]: Monad, R, A](ga: G[A]): ReaderT[G, R, A] =
    ReaderT(r => ga)
}

case class ReaderT[M[_]: Monad, R, A](run: R => M[A]) {
  import ReaderT._
  def apply(r: R): M[A] = run(r)

  def flatMap[B](f: A => ReaderT[M, R, B])(implicit mB: Manifest[B] = null): ReaderT[M, R, B] =
    ReaderT(r => Monad[M].flatMap(run(r))(a => f(a).run(r)))
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ReaderT[M, R, B] =
    ReaderT(r => Monad[M].map(run(r))(f))
  def filter(f: A => Boolean): ReaderT[M, R, A] =
    ReaderT(r => Monad[M].filter(run(r))(f))
  def withFilter(f: A => Boolean): ReaderT[M, R, A] = filter(f)
}

/////////////////////////////////////////////////

trait MonadState[F[_], S] extends Monad[F] {
  def get: F[S]
  def put(s: S): F[Unit]
  def mod(f: S => S): F[Unit]
}

object MonadState {
  def apply[F[_], S](implicit s: MonadState[F, S]): MonadState[F, S] = s
}

object StateT {
  def apply[M[_]: Monad, S, A](implicit m: StateT[M, S, A]): StateT[M, S, A] = m
  implicit def apply[M[_]: Monad, S]: Monad[StateT[M, S, ?]] = StateTMonad[M, S]

  implicit def StateTMonad[M[_]: Monad, S] = new MonadState[StateT[M, S, ?], S] {
    def flatMap[A, B](sa: StateT[M, S, A])(f: A => StateT[M, S, B]) = sa.flatMap(f)
    def pure[A](a: A): StateT[M, S, A] = StateT(s => Monad[M].pure((a, s)))
    def filter[A](sa: StateT[M, S, A])(f: A => Boolean): StateT[M, S, A] = sa.filter(f)

    def get: StateT[M, S, S] = StateT(s => Monad[M].pure((s, s)))
    def put(s: S): StateT[M, S, Unit] = StateT(_ => Monad[M].pure(((), s)))
    def mod(f: S => S): StateT[M, S, Unit] = StateT(s => Monad[M].pure(((), f(s))))
  }

  implicit def StateTMonadPlus[M[_]: Monad : MonadPlus, S: Lattice] = new MonadPlus[StateT[M, S, ?]] {
    def mzero[A: Lattice]: StateT[M, S, A] = StateT(s => MonadPlus[M].mzero)
    def mplus[A: Lattice](a: StateT[M, S, A], b: StateT[M, S, A]): StateT[M, S, A] =
      StateT(s => MonadPlus[M].mplus(a.run(s), b.run(s)))
  }

  def liftM[G[_]: Monad, S, A](ga: G[A]): StateT[G, S, A] =
    StateT(s => Monad[G].map(ga)(a => (a, s)))
}

case class StateT[M[_]: Monad, S, A](run: S => M[(A, S)]) {
  import StateT._
  def apply(s: S): M[(A, S)] = run(s)
  def flatMap[B](f: A => StateT[M, S, B])(implicit mB: Manifest[B] = null): StateT[M, S, B] =
    StateT(s => Monad[M].flatMap(run(s)) { case (a, s1) => f(a).run(s1) })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): StateT[M, S, B] =
    flatMap(a => StateT(s => Monad[M].pure((f(a), s))))

  def filter(f: A => Boolean): StateT[M, S, A] =
    StateT(s => Monad[M].filter(run(s)) { case (a,s) => f(a) })
  def withFilter(f: A => Boolean): StateT[M, S, A] = filter(f)
}

/////////////////////////////////////////////////

object ListT {
  def apply[M[_]: Monad, A](implicit m: ListT[M, A]): ListT[M, A] = m

  implicit def ListTMonad[M[_]: Monad] = new Monad[ListT[M, ?]] {
    def flatMap[A, B](la: ListT[M, A])(f: A => ListT[M, B]) = la.flatMap(f)
    def pure[A](a: A): ListT[M, A] = ListT(Monad[M].pure(List(a)))
    def filter[A](la: ListT[M, A])(f: A => Boolean): ListT[M, A] = la.filter(f)
  }

  implicit def ListTMonadPlus[M[_]: Monad : MonadPlus] = new MonadPlus[ListT[M, ?]] {
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
    ListT(Monad[M].flatMap(run) { (list: List[A]) =>
            list.foldLeft(ListT.empty[M,B])((acc, a) => acc ++ f(a)).run
          })
  def map[B](f: A => B)(implicit mB: Manifest[B] = null): ListT[M, B] =
    ListT(Monad[M].flatMap(run) { list => Monad[M].pure(list.map(f)) })

  def filter(f: A => Boolean): ListT[M, A] =
    ListT(Monad[M].map(run) { list => list.filter(f) })
  def withFilter(f: A => Boolean): ListT[M, A] = filter(f)
}
