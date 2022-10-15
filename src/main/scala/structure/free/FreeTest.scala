package gensym.structure.monad.free

import gensym.structure.monad._
import gensym.structure.functor._
import gensym.structure.monad.free.VoidEff.∅
import gensym.structure.~>

object Knapsack {
  import Free._
  import Coproduct.{CoproductFunctor => _, _}
  import NondetEff.{NondetFunctor => _, _}
  import VoidEff.{VoidFunctor => _, _}
  import StateEff.{StateFunctor => _, _}
  import CutHandler.{CutFunctor => _, _}
  import NondetVoidEff._
  import StateNondetEff._

  def inc[F[_]: Functor](implicit I: State[Int, *] ⊆ F): Free[F, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  def choices[F[_]: Functor, A](prog: Free[F, A])(implicit I1: Nondet ⊆ F, I2: State[Int, *] ⊆ F): Free[F, A] =
    prog match {
      case Return(x) => ret(x)
      case Fail$() => fail
      case Choice$(p, q) =>
        for {
          _ <- inc
          pq <- choice(choices(p), choices(q))
        } yield pq
      case Impure(op) => Impure(Functor[F].map(op)(choices(_)))
    }

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def knapsack[F[_]: Functor](w: Int, vs: List[Int])(implicit I: Nondet ⊆ F): Free[F, List[Int]] = {
    if (w < 0)
      fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select(vs)
      xs <- knapsack(w-v, vs)
    } yield v :: xs
  }

  def main(args: Array[String]) {
    import VoidEff.VoidFunctor
    import StateEff.StateFunctor
    import NondetEff.NondetFunctor
    import CutHandler.CutFunctor

    // Only nondeterminism effect
    println(allsols(knapsack(3, List(3, 2, 1))))

    val global: (Int, List[List[Int]]) = {
      // Note: have to manually define an implicit functor instance here,
      //       otherwise Scala compiler complains implicit expansion divergence
      implicit val F = Functor[(State[Int, *] ⊕ ∅)#t]
      VoidEff(runGlobal(0, choices(knapsack[(Nondet ⊕ (State[Int, *] ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(global)

    val local: List[(Int, List[Int])] = {
      implicit val F = Functor[(Nondet ⊕ ∅)#t]
      VoidEff(runLocal(0, choices(knapsack[(State[Int, *] ⊕ (Nondet ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(local)

    // only computes the first solution
    val single: List[List[Int]] = {
      implicit val F = Functor[(Nondet ⊕ ∅)#t]
      allsols(once(knapsack[(Cut ⊕ (Nondet ⊕ ∅)#t)#t](3, List(3, 2, 1))))
    }
    println(single)
  }
}
