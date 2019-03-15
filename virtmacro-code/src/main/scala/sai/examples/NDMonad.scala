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

import sai.lms._

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

    //println(a.run(Map[Int, Int](-1 -> -1)).run)

    val b: Ans = for {
      s <- StateT.stateTMonadState[Store, NondetT].get
      x <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(4,5)))
      y <- MonadTrans[StateT].liftM[NondetT, Int](ListT.fromList[Id, Int](List(1,2)))
      _ <- StateT.stateTMonadState[Store, NondetT].modify(s => s + (x -> y))
    } yield x * y

    val c: Ans = for {
      v <- StateT.stateTMonadPlus[Store, NondetT].plus(a, b)
    } yield v

    val result = c.run(Map[Int, Int](-1 -> -1)).run
    println(result.size)
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
  import sai.monads._

  @virtualize
  trait ExampleOps extends SAIDsl with SAIMonads {
    import ListReaderStateM._

    type Cache0 = Map[String, Set[Int]]
    type Cache = Rep[Cache0]
    type AnsM[T] = ListReaderStateM[Cache0, Cache0, T]
    type Ans = AnsM[Int]

    def get_outcache: AnsM[Cache0] = ListReaderStateMonad[Cache0, Cache0].get
    def update_outcache(k: Rep[String], v: Rep[Int]): AnsM[Unit] =
      ListReaderStateMonad[Cache0, Cache0].mod(s => s + (k -> (s.getOrElse(k, Set[Int]()) ++ Set(v))))

    val a: Ans = for {
      x <- fromList[Cache0, Cache0, Int](List(1,2))
      y <- fromList[Cache0, Cache0, Int](List(4,5))
      _ <- update_outcache(x.toString + "+" + y.toString, x + y)
    } yield x + y

    val b: Ans = for {
      z <- fromList[Cache0, Cache0, Int](List(10,11))
      w <- fromList[Cache0, Cache0, Int](List(3,6))
      _ <- update_outcache(z.toString + "*" + w.toString, z * w)
    } yield z * w

    val c: Ans = ListReaderStateMonadPlus[Cache0, Cache0].mplus[Int](a, b)
  }

  trait ExampleOpsExp extends ExampleOps with SAIOpsExp

  trait ExampleGen extends GenericNestedCodegen {
    val IR: ExampleOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      case Struct(tag, elems) =>
        //This fixes code generation for tuples, such as Tuple2MapIntValueValue
        //TODO: merge back to LMS
        registerStruct(structName(sym.tp), sym.tp, elems)
        val typeName = sym.tp.runtimeClass.getSimpleName +
          "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
        emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
      case _ => super.emitNode(sym, rhs)
    }
  }

  trait ExampleDriver extends DslDriver[Unit, Unit] with ExampleOpsExp { q =>
    override val codegen = new DslGen
        with ScalaGenMapOps
        with ScalaGenSetOps
        with ScalaGenListOps
        with ScalaGenUncheckedOps
        with SAI_ScalaGenTupleOps
        with SAI_ScalaGenTupledFunctions
        with ExampleGen {
      val IR: q.type = q
    }
  }

  def test(): DslDriver[Unit, Unit] = new ExampleDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      println(c.run(Map[String, Set[Int]]())(Map[String, Set[Int]]()))
    }
  }
}

object RepListTest {
  def main(args: Array[String]): Unit = {
    val s = RepListTExample.test
    println(s.code)
    s.eval(())
  }
}
