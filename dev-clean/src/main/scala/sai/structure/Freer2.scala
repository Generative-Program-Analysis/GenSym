package sai.structure

//Track effects as type-level lists of type constructors
object Eff {
  sealed trait Eff //TODO better call it Eff?
  trait ∅ extends Eff
  trait ⊗[A[_], TL <: Eff] extends Eff

  type Lone[T[_]] = T ⊗ ∅
}

//union type as in the freer monad paper, scala-style
object OpenUnion {
  import Eff._
  sealed trait U[R <: Eff, X]
  case class Union[R <: Eff,T[_],X] private(index: Int, value: T[X]) extends U[R,X]

  //type-safe pointers into tlists
  case class Ptr[T[_], R <: Eff] private(pos: Int)
  implicit def pz[T[_]]: Ptr[T,Lone[T]] = Ptr(0)  //TODO make it tail polymorphic?
  implicit def ps[T[_],R <: Eff,U[_]](implicit pred: Ptr[T,R]): Ptr[T, U ⊗ R] = Ptr(pred.pos + 1)

  trait ∈[T[_], R <: Eff] {
    def inj[X](sub: T[X]): U[R,X]
    def prj[X](u: U[R,X]): Option[T[X]]
  }

  implicit def member[T[_], R <: Eff](implicit ptr: Ptr[T,R]): T ∈ R =
    new (T ∈ R) {
      override def inj[X](sub: T[X]): U[R, X] = Union(ptr.pos, sub)

      override def prj[X](u: U[R, X]): Option[T[X]] = u match {
        case Union(i,v) if i == ptr.pos => Some(v.asInstanceOf[T[X]])
        case _ => None
      }
    }

  //TODO can this be made more flexible? i.e. have decomp at arbitrary positions in the list R?
  def decomp[T[_], R <: Eff, X](u: U[T ⊗ R,X]): Either[U[R,X], T[X]] = u match {
    case Union(0, v) => Right(v.asInstanceOf[T[X]])
    case Union(n, v) => Left(Union(n-1, v))
  }

  def weaken[T[_], R <: Eff, X](u: U[R,X]): U[T ⊗ R, X] = u match {
    case Union(n, v) => Union(n+1, v)
  }
}

object Freer {
  import Eff._
  import OpenUnion._

  abstract class Comp[R <: Eff, +A] {
    def flatMap[B](f: A => Comp[R, B]): Comp[R, B]
    def >>=[B](f: A => Comp[R, B]): Comp[R, B] = flatMap(f)
    def map[B](f: A => B): Comp[R, B]
  }

  case class Return[R <: Eff, A](a: A) extends Comp[R, A] {
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] = f(a)

    override def map[B](f: A => B): Comp[R, B] = Return(f(a))
  }

  case class Op[R <: Eff, A, X](op: U[R,X], k: X => Comp[R, A]) extends Comp[R, A] { //TODO use the construction from sec. 3.1 for the continuations
    override def flatMap[B](f: A => Comp[R, B]): Comp[R, B] =
      Op(op, { x: X => k(x) flatMap f })

    override def map[B](f: A => B): Comp[R, B] =
      Op(op, { x: X => k(x) map f })
  }
  object Op {
    //This'll behave better with type inference, since we usually do not know the
    //actual name of the abstract continuation parameter X in a usage context. We can avoid
    //explicitly annotating the argument of the continuation k.
    def apply[R <: Eff, A, X](op: U[R,X])(k: X => Comp[R, A]): Comp[R,A] = Op(op,k)
  }

  def perform[T[_], R <: Eff, X](op: T[X])(implicit I: T ∈ R): Comp[R,X] = //TODO naming
    Op(I.inj(op)) {x => Return(x)}
}

object Handlers {
  import Eff._
  import OpenUnion._
  import Freer._

  //For handling effects, we need first-class polymorphism.
  //We'll make use of kindprojector's facilities to encode it.
  trait ~>[E[_],F[_]] {
    def apply[X](x: E[X]): F[X]
  }

  type CPS[A, B, X] = (X => A) => B
  //Intuitively, DeepClauses[E, R, B] means forall X. (op:E[X]) => (k: X => Comp[R,B]) => Comp[R,B]
  //Again for better type inference, we are currying op and its continuation k.
  type DeepClauses[E[_], R <: Eff, B] = E ~> CPS[Comp[R,B], Comp[R,B], ?]
  //In contrast, ShallowClauses[E, R, A, B] means forall X. (op:E[X]) => (k: X => Comp[E ⊗ R, A]) => Comp[R,B]
  type ShallowClauses[E[_], R <: Eff, A, B] = E ~> CPS[Comp[E ⊗ R, A], Comp[R,B], ?]

  /** deep handler combinator */
  def handler[E[_], R <: Eff, A, B, X]
             (ret: Return[E ⊗ R,A] => Comp[R, B])
             (h: DeepClauses[E, R, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(r) => ret(Return(r))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h(ex) { x => handler(ret)(h)(k(x))}
      case Left(op) =>
        Op(op) { x => handler(ret)(h)(k(x)) }
    }
  }

  /** shallow handler combinator */
  def shallow_handler[E[_], R <: Eff, A, B, X]
                     (ret: Return[E ⊗ R,A] => Comp[R, B])
                     (h: ShallowClauses[E, R, A, B]): Comp[E ⊗ R, A] => Comp[R, B] = {
    case Return(r) => ret(Return(r))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h(ex)(k)
      case Left(op) =>
        Op(op) { x => shallow_handler(ret)(h)(k(x)) }
    }
  }

}

object Nondet {
  import Eff._
  import OpenUnion._
  import Freer._

  sealed trait Nondet[+K]
  case object Fail extends Nondet[Nothing] //Fail () ~> Nothing
  case object Choice extends Nondet[Boolean] //Choice () ~> Boolean

  def fail[A, R <: Eff](implicit I: Nondet ∈ R): Comp[R,A] = perform(Fail)
  def choice[A, R <: Eff](a: Comp[R,A], b: Comp[R,A])(implicit I: Nondet ∈ R): Comp[R,A] =
    perform(Choice) >>= {
      case true  => a
      case false => b
    }


}





