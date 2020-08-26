package sai.structure.freer3

import scala.language.{higherKinds, implicitConversions}

//Track effects as type-level lists of type constructors
object Eff {
  sealed trait Eff
  trait ∅ extends Eff
  trait ⊗[A[_], TL <: Eff] extends Eff

  type Lone[T[_]] = T ⊗ ∅
}

//union type as in the freer monad paper, scala-style
object OpenUnion {
  import Eff._

  sealed trait ⊎[R <: Eff, X] {
    def weaken[E[_]]: ⊎[E ⊗ R, X]
  }
  case class Union[R <: Eff, T[_], X] private(index: Int, value: T[X]) extends (R ⊎ X) {
    def weaken[E[_]]: Union[E ⊗ R, T, X] = Union(index+1,value)
  }

  //type-safe pointers into tlists
  case class Ptr[T[_], R <: Eff] private(pos: Int)
  implicit def pz[T[_], R <: Eff]: Ptr[T, T ⊗ R] = Ptr(0)
  implicit def ps[T[_], R <: Eff, U[_]](implicit pred: Ptr[T, R]): Ptr[T, U ⊗ R] = Ptr(pred.pos + 1)

  trait ∈[T[_], R <: Eff] {
    def inj[X](sub: T[X]): R ⊎ X
    def prj[X](u: R ⊎ X): Option[T[X]]
  }

  implicit def member[T[_], R <: Eff](implicit ptr: Ptr[T, R]): T ∈ R =
    new (T ∈ R) {
      override def inj[X](sub: T[X]): R ⊎ X = Union(ptr.pos, sub)

      override def prj[X](u: R ⊎ X): Option[T[X]] = u match {
        case Union(i, v) if i == ptr.pos => Some(v.asInstanceOf[T[X]])
        case _ => None
      }
    }

  //TODO can this be made more flexible? i.e. have decomp at arbitrary positions in the list R?
  def decomp[T[_], R <: Eff, X](u: (T ⊗ R) ⊎ X): Either[R ⊎ X, T[X]] = u match {
    case Union(0, v) => Right(v.asInstanceOf[T[X]])
    case Union(n, v) => Left(Union(n-1, v))
  }

  implicit def weaken[T[_], R <: Eff, X](u: ⊎[R,X]): ⊎[T ⊗ R, X] = u match {
    case Union(n, v) => Union(n+1, v)
  }
}

object Freer {
  import Eff._
  import OpenUnion._

  abstract class Comp[R <: Eff, +A] {
    def flatMap[B](f: A => Comp[R, B]): Comp[R, B]
    def map[B](f: A => B): Comp[R, B]

    def >>=[B](f: A => Comp[R, B]): Comp[R, B] = flatMap(f)
  }

  case class Return[R <: Eff, A](a: A) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] = f(a)
    override def map[B](f: A => B): Comp[R, B] = Return(f(a))
  }

  //TODO use the construction from sec. 3.1 for the continuations
  case class Op[R <: Eff, A, X](op: R ⊎ X, k: X => Comp[R, A]) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] =
      Op(op, { x: X => k(x) >>= f })

    override def map[B](f: A => B): Comp[R, B] =
      Op(op, { x: X => k(x) map f })
  }

  object Op {
    //This'll behave better with type inference, since we usually do not know the
    //actual name of the abstract continuation parameter X in a usage context. We can avoid
    //explicitly annotating the argument of the continuation k.
    def apply[R <: Eff, A, X](op: R ⊎ X)(k: X => Comp[R, A]): Comp[R, A] = Op(op, k)
  }

  def perform[T[_], R <: Eff, X](op: T[X])(implicit I: T ∈ R): Comp[R, X] = //TODO naming
    Op(I.inj(op)) { x => Return(x) }

  def ret[R <: Eff, A](x:A): Return[R, A] = Return(x)

  implicit def extract[A](c: Comp[∅, A]): A = c match {
    case Return(a) => a
  }
}

object Handlers {
  import Eff._
  import OpenUnion._
  import Freer._

  //represents the handler clauses for effect ops, and is polymorphic in the continuation type
  trait FFold[F[_], A, B] {
    def apply[X]: (F[X], X => A) => B
    def apply[X](fx: F[X])(k: X => A): B = apply(fx, k)
    //final def curried[X](fx: F[X])(k: X => A): B = apply(fx, k)
  }
  type DeepH[F[_], R <: Eff, A] = FFold[F, Comp[R, A], Comp[R, A]]
  type ShallowH[F[_], R <: Eff, A, B] = FFold[F, Comp[F ⊗ R, A], Comp[R, B]]

  /** deep handler combinator */
  def handler[E[_], R <: Eff, A, B]
             (ret: Return[E ⊗ R, A] => Comp[R, B]) //that's a stylistic choice, could as well make it A => Comp[R,B]
             (h: DeepH[E, R, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h(ex) { x => handler(ret)(h)(k(x)) }
      case Left(op) =>
        Op(op) { x => handler(ret)(h)(k(x)) }
    }
  }

  /** shallow handler combinator */
  def shallow_handler[E[_], R <: Eff, A, B]
                     (ret: Return[E ⊗ R, A] => Comp[R, B])
                     (h: ShallowH[E, R, A, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) => h(ex, k)
      case Left(op) => Op(op) { x => shallow_handler(ret)(h)(k(x)) }
    }
  }

  //open handlers a la handlers in action paper, i.e., handling an effect E by inducing other effects F
  //with more powerful effect row calculations, we could just have this kind of deep handler
  abstract class DeepHO[F[_], G[_], R <: Eff, A] {
    //to allow inducing other effects in the handler body passed by the programmer,
    //we'll need an implicit context with the capability.
    //we could probably have a leaner overall design with dotty's implicit function types
    //TODO: weak point: what if we require G to consist of multiple effects?
    implicit val canG: G ∈ (G ⊗ R) = member
    def apply[X]: (F[X], X => Comp[G ⊗ R, A]) => Comp[G ⊗ R, A]
    final def curried[X](fx: F[X])(k: X => Comp[G ⊗ R, A]): Comp[G ⊗ R, A] = apply(fx,k)
  }

  // Can E (or F) appears at anywhere inside R?
  // e.g., Return[R, A] => Comp[R, B], but requires E ∈ R and F ∈ R
  def ohandler[E[_], F[_], R <: Eff, A, B]
              (ret: Return[E ⊗ R, A] => Comp[F ⊗ R, B])
              (h: DeepHO[E, F, R, B]): Comp[E ⊗ R, A] => Comp[F ⊗ R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h.curried(ex) { x => ohandler(ret)(h)(k(x))}
      case Left(op) =>
        Op(op.weaken[F]) { x => ohandler(ret)(h)(k(x)) }
    }
  }

  //TODO: koka-style parameterized handlers?
}
