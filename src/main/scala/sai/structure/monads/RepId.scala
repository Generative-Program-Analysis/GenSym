package sai.structure.monad

import lms.core._
import lms.core.stub._
import lms.macros.SourceContext

import gensym.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

@virtualize
trait RepIdMonad { self: RepMonads with SAIOps =>
  @deprecated("Use IdM", "")
  object IdMonadInstance {
    type Id[T] = Rep[T]
    implicit val IdMonad: Monad[Id] = new Monad[Id] {
      def pure[A: Manifest](a: Rep[A]): Id[A] = a
      def flatMap[A: Manifest, B: Manifest](ma: Id[A])(f: Rep[A] => Id[B]): Id[B] = f(ma)
      //def filter[A: Manifest](ma: Id[A])(f: Rep[A] => Rep[Boolean]): Id[A] = throw new Exception("Not supported")
    }
  }

  /////////////////////////////////////////////////

  object IdM {
    def apply[A: Manifest](implicit m: IdM[A]): IdM[A] = m

    implicit val IdMonadInstance: Monad[IdM] = new Monad[IdM] {
      def pure[A: Manifest](a: Rep[A]): IdM[A] = IdM(a)
      def flatMap[A: Manifest, B: Manifest](ma: IdM[A])(f: Rep[A] => IdM[B]): IdM[B] = ma.flatMap(f)
      //def filter[A: Manifest](ma: IdM[A])(f: Rep[A] => Rep[Boolean]): IdM[A] = throw new Exception("Not supported")
    }

    implicit val IdMonadPlus: MonadPlus[IdM] = new MonadPlus[IdM] {
      def mzero[A: Manifest : RepLattice]: IdM[A] = IdM(RepLattice[A].bot)
      def mplus[A: Manifest : RepLattice](a: IdM[A], b: IdM[A]): IdM[A] = IdM(a.run ⊔ b.run)
    }
  }

  case class IdM[A: Manifest](run: Rep[A]) {
    import IdM._
    def apply: Rep[A] = run
    def flatMap[B: Manifest](f: Rep[A] => IdM[B]): IdM[B] = f(run)
    def map[B: Manifest](f: Rep[A] => Rep[B]): IdM[B] = IdM(f(run))
  }
}

