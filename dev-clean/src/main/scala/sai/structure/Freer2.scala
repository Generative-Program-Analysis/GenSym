package sai.structure

//Type-level lists of type constructors
object TList {
  sealed trait TList //TODO better call it Eff?
  trait ∅ extends TList
  trait ⊗[A[_], TL <: TList] extends TList

  type Lone[T[_]] = T ⊗ ∅
}

//union type as in the freer monad paper, scala-style
object OpenUnion {
  import TList._
  sealed trait U[R <: TList, X]
  case class Union[R <: TList,T[_],X] private (index: Int, value: T[X]) extends U[R,X]

  //type-safe pointers into tlists
  case class Ptr[T[_], R <: TList] private (pos: Int)
  implicit def pz[T[_]]: Ptr[T,Lone[T]] = Ptr(0)
  implicit def ps[T[_],R <: TList,U[_]](implicit pred: Ptr[T,R]): Ptr[T, U ⊗ R] = Ptr(pred.pos + 1)

  trait ∈[T[_], R <: TList] {
    def inj[X](sub: T[X]): U[R,X]
    def prj[X](u: U[R,X]): Option[T[X]]
  }

  implicit def member[T[_], R <: TList](implicit ptr: Ptr[T,R]): T ∈ R =
    new (T ∈ R) {
      override def inj[X](sub: T[X]): U[R, X] = Union(ptr.pos, sub)

      override def prj[X](u: U[R, X]): Option[T[X]] = u match {
        case Union(i,v) if i == ptr.pos => Some(v.asInstanceOf[T[X]])
        case _ => None
      }
    }

  def decomp[T[_], R <: TList, X](u: U[T ⊗ R,X]): Either[U[R,X], T[X]] = u match {
    case Union(0, v) => Right(v.asInstanceOf[T[X]])
    case Union(n, v) => Left(Union(n-1, v))
  }

  def weaken[T[_], R <: TList, X](u: U[R,X]): U[T ⊗ R, X] = u match {
    case Union(n, v) => Union(n+1, v)
  }
}

object Freer {
  import TList._
  import OpenUnion._

  abstract class Comp[R <: TList, +A] {
    def flatMap[B](f: A => Comp[R, B]): Comp[R, B]
    def >>=[B](f: A => Comp[R, B]): Comp[R, B] = flatMap(f)
    def map[B](f: A => B): Comp[R, B]
  }

  case class Return[R <: TList, A](a: A) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] = f(a)

    override def map[B](f: A => B): Comp[R, B] = Return(f(a))
  }

  case class Op[R <: TList, A, X](op: U[R,X], k: X => Comp[R, A]) extends Comp[R, A] { //TODO use the construction from sec. 3.1 for the continuations
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] =
      Op(op, { x: X => k(x) flatMap f })

    override def map[B](f: A => B): Comp[R, B] =
      Op(op, { x: X => k(x) map f })
  }

  def perform[T[_], R <: TList, X](op: T[X])(implicit I: T ∈ R): Comp[R,X] = //TODO naming
    Op(I.inj(op), {x: X => Return(x)})
}

object Handlers {
  import TList._
  import OpenUnion._
  import Freer._

  //TODO need first class parametric polymorphism?
  //def handler[E[_], R <: TList, A, B](ret: A => Comp[R,B])(h: ): Comp[E ⊗ R,A] => Comp[R,B]

  //TODO other handler variants
}

object Nondet {
  import TList._
  import OpenUnion._
  import Freer._

  sealed trait Nondet[+K]
  case object Fail extends Nondet[Nothing]
  case object Choice extends Nondet[Boolean]

  def fail[A, R <: TList](implicit I: Nondet ∈ R): Comp[R,A] = perform(Fail)
  def choice[A, R <: TList](a: Comp[R,A], b: Comp[R,A])(implicit I: Nondet ∈ R): Comp[R,A] =
    perform(Choice) >>= {
      case true  => a
      case false => b
    }
}




