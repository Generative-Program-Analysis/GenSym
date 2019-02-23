package sai
package examples

import scalaz._
import Scalaz._

object NDTest {
  def test() = {
    type Store = Map[Int, Int]
    type NondetT[T] = ListT[Id, T]
    type StateT[F[_], B] = IndexedStateT[F, Store, Store, B]
    type AnsT[T] = StateT[NondetT, T]
    type Ans = AnsT[Int]

    val a: Ans = for {
      s <- StateT.stateTMonadState[Store, NondetT].get
      x <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(1,2,3)))
      y <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(4,5,6)))
      _ <- StateT.stateTMonadState[Store, NondetT].modify(s => s + (x -> y))
    } yield x + y

    val b: Ans = for {
      s <- StateT.stateTMonadState[Store, NondetT].get
      x <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(4,5,6)))
      y <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(1,2,3)))
      _ <- StateT.stateTMonadState[Store, NondetT].modify(s => s + (x -> y))
    } yield x * y

    println(a.run(Map()))

  }
}
