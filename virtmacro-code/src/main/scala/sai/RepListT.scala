package sai

import scalaz._
import Scalaz._

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.internal.Expressions
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._

trait RepListTransfomer extends SAIDsl {
  case class RepListT[M[_], A: Manifest](run: M[Rep[List[A]]]) {
    // TODO: uncons

    def ::(a: Rep[A])(implicit M: Functor[M]): RepListT[M, A] = new RepListT(M.map(run)(list => a :: list))

    // TODO: collect

    def isEmpty(implicit M: Functor[M]): M[Rep[Boolean]] = M.map(run)(_.isEmpty)

    // TODO: headOption, find, headMaybe, tailM

    def filter(p: Rep[A] => Rep[Boolean])(implicit M: Functor[M]): RepListT[M, A] =
      new RepListT(M.map(run)(_.filter(p)))

    // TODO: drop, dropWhile, takeWhile

    def take(n: Rep[Int])(implicit M: Functor[M]): RepListT[M, A] = new RepListT(M.map(run)(_.take(n)))

    def ++(bs: => RepListT[M, A])(implicit M: Bind[M]): RepListT[M, A] =
      new RepListT(M.bind(run) { list1: Rep[List[A]] =>
                     M.map(bs.run) { list2: Rep[List[A]] =>
                       list1 ++ list2
                     }})

    def flatMap[B: Manifest](f: Rep[A] => RepListT[M, B])(implicit M: Monad[M]): RepListT[M, B] =
      new RepListT(M.bind(run) { list: Rep[List[A]] =>
                     ??? //list.foldMap(f)
                   })

    def flatMapF[B: Manifest](f: Rep[A] => M[Rep[List[B]]])(implicit M: Monad[M]): RepListT[M, B] =
      flatMap(a => RepListT(f(a)))

    def map[B: Manifest](f: Rep[A] => Rep[B])(implicit M: Functor[M]): RepListT[M, B] =
      new RepListT(M.map(run)(_.map(f)))

    def mapF[B: Manifest](f: Rep[A] => M[Rep[B]])(implicit M: Monad[M]): RepListT[M, B] =
      flatMapF { a => M.map(f(a))(b => List(b)) }

    def mapT[F[_], B: Manifest](f: M[Rep[List[A]]] => F[Rep[List[B]]]): RepListT[F, B] =
      RepListT(f(run))

    def tail(implicit M: Functor[M]): RepListT[M, A] = new RepListT(M.map(run)(_.tail))

    // TODO: tailOption

    def foldLeft[B: Manifest](z: Rep[B])(f: (=> Rep[B], => Rep[A]) => Rep[B])(implicit M: Functor[M]): M[Rep[B]] =
      M.map(run) { list =>
        list.foldLeft(z){ (left, right) => f(left, right) }
      }

    def toRepList: M[Rep[List[A]]] = run

    //TODO: foldRight

    def length(implicit M: Functor[M]): M[Rep[Int]] = M.map(run)(_.length)
  }

  sealed abstract class RepListTInstances2 {
    implicit def repListTFunctor[F[_]](implicit F0: Functor[F]): Functor[RepListT[F, ?]] =
      new RepListTFunctor[F] {
        implicit def F: Functor[F] = F0
      }

    implicit def repListTSemigroup[F[_], A: Manifest](implicit F0: Bind[F]): Semigroup[RepListT[F, A]] =
      new RepListTSemigroup[F, A]{
        val m: Manifest[A] = ???
        implicit def F: Bind[F] = F0
      }
  }

  sealed abstract class RepListTInstances1 extends RepListTInstances2 {
    implicit def repListTMonoid[F[_], A: Manifest](implicit F0: Monad[F]): Monoid[RepListT[F, A]] =
      new RepListTMonoid[F, A] {
        val m: Manifest[A] = manifest
        implicit def F: Monad[F] = F0
      }
  }

  sealed abstract class RepListTInstances extends RepListTInstances1 {
    implicit def repListTMonadPlus[F[_]](implicit F0: Monad[F]): MonadPlus[RepListT[F, ?]] =
      new RepListTMonadPlus[F] {
        implicit def F: Monad[F] = F0
      }

    /*
     implicit def listTEqual[F[_], A](implicit E: Equal[F[IList[A]]]): Equal[ListT[F, A]] =
     E.contramap((_: ListT[F, A]).toIList)

     implicit def listTShow[F[_], A](implicit E: Show[F[IList[A]]]): Show[ListT[F, A]] =
     Contravariant[Show].contramap(E)((_: ListT[F, A]).toIList)

     implicit val repListTHoist: Hoist[RepListT] =
     new RepListTHoist {}
     */
  }

  object RepListT extends RepListTInstances  {
    /*
     def listT[M[_]]: (λ[α => M[IList[α]]] ~> ListT[M, ?]) =
     λ[λ[α => M[IList[α]]] ~> ListT[M, ?]](
     new ListT(_)
     )
     */

    def empty[M[_], A: Manifest](implicit M: Applicative[M]): RepListT[M, A] =
      new RepListT[M, A](M.point(List()))

    def fromRepList[M[_], A: Manifest](mas: M[Rep[List[A]]]): RepListT[M, A] =
      new RepListT(mas)

    /*
     def fromIList[M[_], A](mas: M[IList[A]]): ListT[M, A] =
     new ListT(mas)

     def fromList[M[_], A](mas: M[List[A]])(implicit M: Functor[M]): ListT[M, A] =
     new ListT(M.map(mas)(IList.fromList))
     */
  }

  private trait RepListTFunctor[F[_]] extends Functor[RepListT[F, ?]] {
    implicit def F: Functor[F]
    //A = Rep[A'], B = Rep[B']
    override def map[A, B](fa: RepListT[F, A])(f: A => B): RepListT[F, B] = ??? //fa.map(f)
  }

  private trait RepListTSemigroup[F[_], A] extends Semigroup[RepListT[F, A]] {
    implicit def F: Bind[F]
    implicit val m: Manifest[A]
    def append(f1: RepListT[F, A], f2: => RepListT[F, A]): RepListT[F, A] = f1 ++ f2
  }

  private trait RepListTMonoid[F[_], A] extends Monoid[RepListT[F, A]] with RepListTSemigroup[F, A] {
    implicit def F: Monad[F]
    implicit val m: Manifest[A]
    def zero: RepListT[F, A] = RepListT.empty[F, A]
  }

  private trait RepListTMonadPlus[F[_]] extends MonadPlus[RepListT[F, ?]] with RepListTFunctor[F] {
    implicit def F: Monad[F]

    //def bind[A: Manifest, B: Manifest](fa: RepListT[F, A])(f: Rep[A] => RepListT[F, B]): RepListT[F, B] = ??? // fa flatMap f
    def bind[A, B](fa: RepListT[F, A])(f: A => RepListT[F, B]): RepListT[F, B] = ??? // fa flatMap f

    //def point[A: Manifest](a: => Rep[A]): RepListT[F, A] = a :: RepListT.empty[F, A]
    def point[A](a: => A): RepListT[F, A] = ???

    def empty[A]: RepListT[F, A] = {
      ??? //RepListT.empty[F, A]
    }

    def plus[A](a: RepListT[F, A], b: => RepListT[F, A]): RepListT[F, A] = a ++ b
  }

  private trait RepListTHoist extends Hoist[RepListT] {
    import RepListT._

    implicit def apply[G[_]: Monad]: Monad[RepListT[G, ?]] = ???

    def liftM[G[_], A: Manifest](a: G[A])(implicit G: Monad[G]): RepListT[G, A] =
      new RepListT(G.map(a)(entry => List(unit(entry))))

    def liftRepM[G[_], A: Manifest](a: G[Rep[A]])(implicit G: Monad[G]): RepListT[G, A] =
      new RepListT(G.map(a)(entry => List(entry)))

    def hoist[M[_], N[_]](f: M ~> N)(implicit M: Monad[M]): RepListT[M, ?] ~> RepListT[N, ?] =
      ???
      //λ[RepListT[M, ?] ~> RepListT[N, ?]](_ mapT f.apply)
  }
}
