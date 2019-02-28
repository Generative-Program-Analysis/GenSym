package sai
package examples

import sai._

object ListTTest {
  import ReaderT._
  import StateT._
  import ListT._
  import IdMonadInstance._

  def test() = {
    type Store = Map[Int, Int]

    type NondetM[T] = ListT[Id, T]
    type StoreT[F[_], B] = StateT[F, Store, B]

    type AnsM[T] = StoreT[NondetM, T]
    type Ans = AnsM[Int] // StoreT[ListT[Id, ?], ?]

    val some_data: List[(Store, Int)] = List((Map(99 -> 99), 99), (Map(98 -> 98), 98))

    val a: Ans = for {
      s <- StateTMonad[NondetM, Store].get
      x <- StateT.liftM[NondetM, Store, Int](ListT.fromList(List(1,2)))
      y <- StateT.liftM[NondetM, Store, Int](ListT.fromList(List(4,5)))
      _ <- StateTMonad[NondetM, Store].mod(s => s + (x -> y))
      d <- StateT.liftM[NondetM, Store, (Store, Int)](ListT.fromList[Id, (Store, Int)](some_data))
      _ <- StateTMonad[NondetM, Store].put(d._1)
      val z = d._2
    } yield z

    //println(a.run(Map[Int, Int](-1 -> -1)).run)

    val b: Ans = for {
      s <- StateTMonad[NondetM, Store].get
      x <- StateT.liftM[NondetM, Store, Int](ListT.fromList(List(4,5)))
      y <- StateT.liftM[NondetM, Store, Int](ListT.fromList(List(1,2)))
      _ <- StateTMonad[NondetM, Store].mod(s => s + (x -> y))
    } yield x * y

    val c: Ans = for {
      v <- StateTMonadPlus[NondetM, Store].mplus(a, b)
    } yield v

    val result = c.run(Map[Int, Int](-1 -> -1)).run
    println(result.size)
  }
}
