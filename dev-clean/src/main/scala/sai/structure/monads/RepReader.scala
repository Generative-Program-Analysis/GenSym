package sai.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

@virtualize
trait RepReaderMonad { self: RepMonads with SAIOps =>
  trait MonadReader[F[_], R] extends Monad[F] {
    def ask: F[R]
    def local[A: Manifest](fa: F[A])(f: Rep[R] => Rep[R]): F[A]
  }

  object MonadReader {
    def apply[F[_], S: Manifest](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
  }

  object ReaderT {
    def apply[M[_]: Monad, R: Manifest, A: Manifest](implicit m: ReaderT[M, R, A]): ReaderT[M, R, A] = m
    implicit def apply[M[_]: Monad, R: Manifest]: Monad[ReaderT[M, R, ?]] = ReaderTMonad[M, R]

    implicit def ReaderTMonad[M[_]: Monad, R: Manifest] = new MonadReader[ReaderT[M, R, ?], R] {
      def flatMap[A: Manifest, B: Manifest](fa: ReaderT[M, R, A])(f: Rep[A] => ReaderT[M, R, B]): ReaderT[M, R, B] =
        fa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): ReaderT[M, R, A] = ReaderT(_ => Monad[M].pure(a))

      def ask: ReaderT[M, R, R] = ReaderT(r => Monad[M].pure(r))
      def local[A: Manifest](fa: ReaderT[M, R, A])(f: Rep[R] => Rep[R]): ReaderT[M, R, A] =
        ReaderT(f andThen fa.run)

      override def filter[A: Manifest](fa: ReaderT[M, R, A])(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] = fa.filter(f)
    }

    implicit def ReaderTMonadPlus[M[_]: Monad : MonadPlus, R: Manifest] = new MonadPlus[ReaderT[M, R, ?]] {
      def mzero[A: Manifest : RepLattice]: ReaderT[M, R, A] = ReaderT(r => MonadPlus[M].mzero)
      def mplus[A: Manifest : RepLattice](a: ReaderT[M, R, A], b: ReaderT[M, R, A]): ReaderT[M, R, A] =
        ReaderT(r => MonadPlus[M].mplus(a.run(r), b.run(r)))
    }

    def liftM[G[_]: Monad, R: Manifest, A: Manifest](ga: G[A]): ReaderT[G, R, A] =
      ReaderT(r => ga)
  }

  case class ReaderT[M[_]: Monad, R: Manifest, A: Manifest](run: Rep[R] => M[A]) {
    import ReaderT._

    def apply(r: Rep[R]): M[A] = run(r)
    def flatMap[B: Manifest](f: Rep[A] => ReaderT[M, R, B]): ReaderT[M, R, B] =
      ReaderT(r => Monad[M].flatMap(run(r))(a => f(a).run(r)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): ReaderT[M, R, B] =
      ReaderT((r: Rep[R]) => Monad[M].map(run(r))(f))

    def filter(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] =
      ReaderT(r => Monad[M].filter(run(r))(f))
    def withFilter(f: Rep[A] => Rep[Boolean]): ReaderT[M, R, A] =
      filter(f)
  }
}

