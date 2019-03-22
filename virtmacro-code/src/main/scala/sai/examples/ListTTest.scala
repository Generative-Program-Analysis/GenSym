package sai
package examples

import sai._
import sai.monads._

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.lattices._
import sai.lattices.Lattices._

object ListTTests {
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

  def test2() = {
    // test state merge inside of ListT
    type Store = Map[Int, Set[Int]]

    type NondetT[F[_], B] = ListT[F, B]
    type StoreT[F[_], B] = StateT[F, Store, B]

    type StoreM[T] = StoreT[Id, T]
    type AnsM[T] = NondetT[StoreM, T]
    type Ans = AnsM[Int]

    val a: Ans = for {
      x <- ListT.fromList[StoreM, Int](List(1,2))
      y <- ListT.fromList[StoreM, Int](List(4,5))
      _ <- ListT.liftM[StoreM, Unit](StateTMonad[Id, Store].mod(s => s + (x -> Set(y))))
    } yield x + y

    val b: Ans = for {
      x <- ListT.fromList[StoreM, Int](List(1, 2))
      y <- ListT.fromList[StoreM, Int](List(6, 7))
      _ <- ListT.liftM[StoreM, Unit](StateTMonad[Id, Store].mod(s => s + (x -> Set(y))))
    } yield x + y

    println(a.run(Map()))
    println(b.run(Map()))

    val c: Ans = MonadPlus[AnsM].mplus(a, b)
    println(c.run(Map()))
  }
}

object RepListTExample {
  import sai.monads._

  @virtualize
  trait ExampleOps extends SAIDsl with RepMonads {
    import ListReaderStateM._

    type Cache = Map[String, Set[Int]]
    type AnsM[T] = ListReaderStateM[Cache, Cache, T]
    type Ans = AnsM[Int]

    def get_outcache: AnsM[Cache] = ListReaderStateMonad[Cache, Cache].get
    def update_outcache(k: Rep[String], v: Rep[Int]): AnsM[Unit] =
      ListReaderStateMonad[Cache, Cache].mod(s => s + (k -> (s.getOrElse(k, Set[Int]()) ++ Set(v))))

    val a: Ans = for {
      x <- fromList[Cache, Cache, Int](List(1,2))
      y <- fromList[Cache, Cache, Int](List(4,5))
      _ <- update_outcache(x.toString + "+" + y.toString, x + y)
    } yield x + y

    val b: Ans = for {
      z <- fromList[Cache, Cache, Int](List(10,11))
      w <- fromList[Cache, Cache, Int](List(3,6))
      _ <- update_outcache(z.toString + "*" + w.toString, z * w)
    } yield z * w

    val c: Ans = ListReaderStateMonadPlus[Cache, Cache].mplus[Int](a, b)
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

object ListTTest {
  def main(args: Array[String]): Unit = {
    // RepListT
    /*
    val s = RepListTExample.test
    println(s.code)
    s.eval(())
     */

    ListTTests.test2
  }
}
