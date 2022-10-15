package gensym.structure.freer

import scala.language.{higherKinds, implicitConversions}

import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._
import IO._

object Knapsack {
  import Nondet._
  // (12,List(List(3), List(2, 1), List(1, 2), List(1, 1, 1)))
  // List(List((1,List(3))), List((2,List(2, 1))), List((2,List(1, 2)), (3,List(1, 1, 1))))

  // import NondetList._
  // val Nondet = NondetList

  def runLocalState[R <: Eff, S, A](s: S, comp: Comp[State[S, *] ⊗ (Nondet ⊗ R), A]): Comp[R, List[Comp[R, List[(S, A)]]]] = {
    val p: Comp[Nondet ⊗ R, S => Comp[Nondet ⊗ R, (S, A)]] = State.run[Nondet ⊗ R, S, A](comp)
    val p1: Comp[R, List[S => Comp[Nondet ⊗ R, (S, A)]]] = Nondet.run[R, S => Comp[Nondet ⊗ R, (S, A)]](p)
    for {fs <- p1} yield fs.map(f => Nondet.run(f(s)))
  }

  def inc[E <: Eff](implicit I: State[Int, *] ∈ E): Comp[E, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  def select_inc[R <: Eff, A](xs: List[A])
    (implicit I1: Nondet ∈ R, I2: State[Int, *] ∈ R, I3: IO ∈ R): Comp[R, A] =
    xs.map(Return[R, A]).foldRight[Comp[R, A]](fail) {
      case (a, b) =>
        for {
          _ <- inc
          x <- choice(a, b)
          // _ <- writeStr("select " + x.toString)
        } yield x
    }
  /*
    for {
      x <- select(xs)
      _ <- inc[R]
    } yield x
     */

  def knapsack[R <: Eff](w: Int, vs: List[Int])
    (implicit I1: Nondet ∈ R, I2: State[Int, *] ∈ R, I3: IO ∈ R): Comp[R, List[Int]] = {
    if (w < 0) fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select_inc(vs)
      xs <- knapsack(w - v, vs)
    } yield v :: xs
  }

  def runGlobalKnapsack = {
    val p: Comp[Nondet ⊗ (IO ⊗ (State[Int, *] ⊗ ∅)), List[Int]] =
      knapsack[Nondet ⊗ (IO ⊗ (State[Int, *] ⊗ ∅))](3, List(3, 2, 1))
    val p1: Comp[IO ⊗ (State[Int, *] ⊗ ∅), List[List[Int]]] = Nondet.run[IO ⊗ (State[Int, *] ⊗ ∅), List[Int]](p)
    val p2: Comp[State[Int, *] ⊗ ∅, List[List[Int]]] = IO.run[State[Int, *] ⊗ ∅, List[List[Int]]](p1)
    val p3: Comp[∅, Int => Comp[∅, (Int, List[List[Int]])]] = State.run[∅, Int, List[List[Int]]](p2)
    println(extract(extract(p3)(0)))
  }

  def runLocalKnapsack = {
    val p: Comp[IO ⊗ (State[Int, *] ⊗ (Nondet ⊗ ∅)), List[Int]] =
      knapsack[IO ⊗ (State[Int, *] ⊗ (Nondet ⊗ ∅))](3, List(3, 2, 1))
    val p1: Comp[State[Int, *] ⊗ (Nondet ⊗ ∅), List[Int]] =
      IO.run(p)
    val p2: Comp[∅, List[Comp[∅, List[(Int, List[Int])]]]] = runLocalState(0, p1)
    println(extract(p2).map(extract))
    /* Equivelent to the followings:
    val p2: Comp[Nondet ⊗ ∅, Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]] =
      State.run[Nondet ⊗ ∅, Int, List[Int]](p1)
    val p3: Comp[∅, List[Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]]] =
      Nondet.run[∅, Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]](p2)
    val p3v: List[Int => Comp[Nondet ⊗ ∅, (Int, List[Int])]] = extract(p3)
    println(p3v.map(f => extract(Nondet.run(f(0)))))
     */
    //List(List((1,List(3)), (5,List(2, 1)), (5,List(1, 2)), (9,List(1, 1, 1))))
  }

  def main(args: Array[String]) {
    //def run[E <: Eff, A]: Comp[Nondet ⊗ E, A] => Comp[E, List[A]] =
    //def run[E <: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, (S, A)]] =
    runGlobalKnapsack
    runLocalKnapsack
  }
}
