package sai
package examples

import scalaz._
import Scalaz._

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

object NDTest {
  def test() = {
    type Store = Map[Int, Int]
    type NondetT[T] = ListT[Id, T]
    type StateT[F[_], B] = IndexedStateT[F, Store, Store, B]
    type AnsT[T] = StateT[NondetT, T]
    type Ans = AnsT[Int]

    val some_data: List[(Store, Int)] = List((Map(99 -> 99), 99), (Map(98 -> 98), 98))

    val a: Ans = for {
      s <- StateT.stateTMonadState[Store, NondetT].get
      x <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(1,2)))
      y <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(4,5)))
      //_ <- StateT.stateTMonadState[Store, NondetT].modify(s => s + (x -> y))
      // inject some data
      d <- MonadTrans[StateT].liftM[NondetT, (Store, Int)](ListT.fromList[Id, (Store, Int)](some_data))
      _ <- StateT.stateTMonadState[Store, NondetT].put(d._1)
      val z = d._2
    } yield z

    val b: Ans = for {
      s <- StateT.stateTMonadState[Store, NondetT].get
      x <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(4,5)))
      y <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(1,2)))
      _ <- StateT.stateTMonadState[Store, NondetT].modify(s => s + (x -> y))
    } yield x * y

    val c: Ans = for {
      v <- StateT.stateTMonadPlus[Store, NondetT].plus(a, b)
    } yield v

    println(a.run(Map[Int, Int](-1 -> -1)).run)
    //println(c.run(Map[Int, Int](-1 -> -1)))
  }

  def test2() = {
    type Store = Map[Int, Int]
    type Cache = Map[String, Set[String]]

    type OutCacheT[F[_], B] = IndexedStateT[F, Cache, Cache, B]
    type InCacheT[F[_], B] = Kleisli[F, Cache, B]
    type NondetT[F[_], B] = ListT[F, B]
    type StateT[F[_], B] = IndexedStateT[F, Store, Store, B]

    type OutCacheM[T] = OutCacheT[Id, T]
    type InOutCacheM[T] = InCacheT[OutCacheM, T]
    type NondetM[T] = NondetT[InOutCacheM, T]
    type AnsM[T] = StateT[NondetM, T]

    type Ans = AnsM[Int] // StateT[NondetT[InCacheT[OutCacheT[Id, ?], ?], ?], Int]

    def ask_incache: AnsM[Cache] = MonadTrans[StateT].liftM[NondetM, Cache](
      MonadTrans[NondetT].liftM[InOutCacheM, Cache](
        Kleisli.ask[OutCacheM, Cache]
      ))
    def get_outcache: AnsM[Cache] =
      MonadTrans[StateT].liftM[NondetM, Cache](
        MonadTrans[NondetT].liftM[InOutCacheM, Cache](
          MonadTrans[InCacheT].liftM[OutCacheM, Cache](
            StateT.stateTMonadState[Cache, Id].get
          )))

    def update_outcache(k: String, v: String): AnsM[Unit] =
      MonadTrans[StateT].liftM[NondetM, Unit](
        MonadTrans[NondetT].liftM[InOutCacheM, Unit](
          MonadTrans[InCacheT].liftM[OutCacheM, Unit](
            StateT.stateTMonadState[Cache, Id].modify(c =>
              c + (k -> (c.getOrElse(k, Set[String]()) ++ Set[String](v)))
            ))))

    val a: Ans = for {
      x <- MonadTrans[StateT].liftM[NondetM, Int](ListT.fromList[InOutCacheM, Int](List(4,5).pure[InOutCacheM]))
      y <- MonadTrans[StateT].liftM[NondetM, Int](ListT.fromList[InOutCacheM, Int](List(1,2).pure[InOutCacheM]))
      _ <- update_outcache(x.toString, y.toString)
      _ <- StateT.stateTMonadState[Store, NondetM].modify(s => s + (x -> y))
    } yield x + y

    val b: Ans = for {
      x <- MonadTrans[StateT].liftM[NondetM, Int](ListT.fromList[InOutCacheM, Int](List(7,8).pure[InOutCacheM]))
      y <- MonadTrans[StateT].liftM[NondetM, Int](ListT.fromList[InOutCacheM, Int](List(3,6).pure[InOutCacheM]))
      _ <- update_outcache(x.toString, y.toString)
      _ <- StateT.stateTMonadState[Store, NondetM].modify(s => s + (x -> y))
    } yield x + y

    val c: Ans = for {
      v <- StateT.stateTMonadPlus[Store, NondetM].plus(a, b)
    } yield v

    val result: (Cache, List[(Store, Int)]) = c.run(Map()).run(Map()).run(Map())
    println(result)

  }
}

object RepListTExample {
  trait ExampleOps extends SAIDsl with RepListTransfomer {
    type Store0 = Map[Int, Int]
    type Cache0 = Map[String, Set[String]]

    type Store = Rep[Store0]
    type Cache = Rep[Cache0]

    type OutCacheT[F[_], B] = StateT[F, Cache, B]
    type NondetT[F[_], B] = RepListT[F, B]
    type StoreT[F[_], B] = StateT[F, Store, B]

    type OutCacheM[T] = OutCacheT[Id, T]
    type NondetM[T] = NondetT[OutCacheM, T]
    type AnsM[T] = StoreT[NondetM, T]
    type Ans = AnsM[Int]

    def RepListLiftM[G[_], A: Manifest](a: G[Rep[A]])(implicit G: Monad[G]): RepListT[G, A] =
      RepListT(G.map(a)(entry => List(entry)))

    def get_outcache: AnsM[Cache] = {
      val fa = StateT.stateTMonadState[Cache, Id].get
      val ga = RepListT[OutCacheM, Cache0](fa.map((entry: Cache) => List(entry)))
      val f: Store => RepListT[OutCacheM, (Store0, Cache0)] =
        (s: Store) => ga.map((a: Cache) => {
                               val res: Rep[(Store0, Cache0)] = (s, a)
                               res
                             })
      StateT[NondetM, Store, Cache](f)
      // other monads uses Rep type, add pass into NondetT, where only takes non-Rep type.
      // TODO: rewrite StateT to use Rep type internally
      // Or, rewrite NonDetT takes Rep type, but unwrap the Rep type automatically
      ???
    }

    def update_outcache(k: String, v: String): AnsM[Unit] = ???

    val a: Ans = for {
      x <- MonadTrans[StoreT].liftM[NondetM, Int](RepListT.fromRepList[OutCacheM, Int](List(1,2).pure[OutCacheM]))
      y <- MonadTrans[StoreT].liftM[NondetM, Int](RepListT.fromRepList[OutCacheM, Int](List(4,5).pure[OutCacheM]))
    } yield x + y
  }
}
