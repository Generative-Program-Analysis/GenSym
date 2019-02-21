package sai
package examples

import cats._
import cats.{Id, ~>}
import cats.implicits._
import cats.data.State
import cats.data.IndexedStateT
import cats.data.Kleisli
import cats.free.Free
import cats.free.Free.liftF
import cats.arrow.FunctionK

object DSL {
  import scala.collection.mutable
  /* An DSL example using free monads */
  sealed trait KVStoreA[A]
  case class Put[T](key: String, value: T) extends KVStoreA[Unit]
  case class Get[T](key: String) extends KVStoreA[Option[T]]
  case class Del(key: String) extends KVStoreA[Unit]

  type KVStore[A] = Free[KVStoreA, A]

  def put[T](key: String, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))
  def get[T](key: String): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get[T](key))
  def del(key: String): KVStore[Unit] =
    liftF(Del(key))
  def update[T](key: String, f: T => T): KVStore[Unit] =
    for {
      v <- get[T](key)
      _ <- v.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
    } yield ()

  def impureCompiler: KVStoreA ~> Id = new (KVStoreA ~> Id) {
    val kvs = mutable.Map.empty[String, Any]
    def apply[A](fa: KVStoreA[A]): Id[A] =
      fa match {
        case Put(key, value) =>
          kvs(key) = value
          ()
        case Get(key) =>
          kvs.get(key).map(_.asInstanceOf[A])
        case Del(key) =>
          kvs.remove(key)
          ()
      }
  }

  type KVStoreState[A] = State[Map[String, Any], A]
  def pureCompiler: KVStoreA ~> KVStoreState = new (KVStoreA ~> KVStoreState) {
    def apply[A](fa: KVStoreA[A]): KVStoreState[A] = fa match {
      case Put(key, value) => State.modify(_.updated(key, value))
      case Get(key) => State.inspect(_.get(key).map(_.asInstanceOf[A]))
      case Del(key) => State.modify(_ - key)
    }
  }

  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 1)
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- put("tame-cats", 2)
      n <- get[Int]("wild-cats")
      _ <- del("tame-cats")
    } yield n

  val result0: Option[Int] = program.foldMap(impureCompiler)
  val result1: (Map[String, Any], Option[Int]) = program.foldMap(pureCompiler).run(Map.empty).value
}
