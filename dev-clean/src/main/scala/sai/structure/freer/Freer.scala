package sai.structure.freer3

import scala.annotation.implicitNotFound
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

    @implicitNotFound("Cannot concatenate RowConcat[${A}, ${B}, ${C}]")
    trait RowConcat[A <: Eff, B <: Eff, C <: Eff]

    implicit def concat1[R <: Eff]: RowConcat[∅, R, R] = null

    implicit def concat2[E[_], R1 <: Eff, R2 <: Eff, R3 <: Eff](implicit prev: RowConcat[R1, R2, R3]): RowConcat[E ⊗ R1, R2, E ⊗ R3] = null

    //TODO
    //implicit def membercatl[T[_], R1 <: Eff, R2 <: Eff, R3 <: Eff](implicit cat : RowConcat[R1,R2,R3], mem : T ∈ R1): T ∈ R3 = ???
    //implicit def membercatr[T[_], R1 <: Eff, R2 <: Eff, R3 <: Eff](implicit cat : RowConcat[R1,R2,R3], mem : T ∈ R2): T ∈ R3 = ???

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
  abstract class DeepHO[F[_], G[_], R <: Eff, A] extends FFold[F, Comp[G ⊗ R, A], Comp[G ⊗ R, A]] {
    //to allow inducing other effects in the handler body passed by the programmer,
    //we'll need an implicit context with the capability.
    //we could probably have a leaner overall design with dotty's implicit function types
    //TODO: weak point: what if we require G to consist of multiple effects?
    implicit val canG: G ∈ (G ⊗ R) = member
  }

  // Can E (or F) appears at anywhere inside R?
  // e.g., Return[R, A] => Comp[R, B], but requires E ∈ R and F ∈ R
  def ohandler[E[_], F[_], R <: Eff, A, B]
              (ret: Return[E ⊗ R, A] => Comp[F ⊗ R, B])
              (h: DeepHO[E, F, R, B]): Comp[E ⊗ R, A] => Comp[F ⊗ R, B] = {
    case Return(x) => ret(Return(x))
    case Op(u, k) => decomp(u) match {
      case Right(ex) =>
        h(ex) { x => ohandler(ret)(h)(k(x))}
      case Left(op) =>
        Op(op.weaken[F]) { x => ohandler(ret)(h)(k(x)) }
    }
  }

  abstract class ShO[F[_], G[_], R <: Eff, A, B] extends FFold[F, Comp[F ⊗ R, A], Comp[G ⊗ R, B]] {
    implicit val canG: G ∈ (G ⊗ R) = member
  }

  /**
   * The point of this is to have a designated handler type having
   * a uniform type parameter list and overall clearer and more concise
   * handler definitions in code. Intuitively, Handler[I, E, O, F] is
   * just Comp[E, I] => Comp[F, O]. Previously different handler types
   * had non-uniform type parameter lists, which are hard to memorize.
   * C.f. the Handler companion object on uses.
   * @tparam I
   * @tparam E
   * @tparam O
   * @tparam F
   */
  trait Handler[-I, E <: Eff ,+O, F <: Eff] extends (Comp[E, I] => Comp[F, O]) {
    def apply(comp: Comp[E,I]): Comp[F,O]
  }

  /**
   * Abstracts over the different requirements for different handler types.
   * Used as implicit evidence in the Handler companion's apply() factory method.
   * @tparam I
   * @tparam E
   * @tparam O
   * @tparam F
   */
  @implicitNotFound("Cannot construct a Handler[${I}, ${E}, ${O}, ${F}]")
  trait HKind[I,E <: Eff, O, F <: Eff] {
    /**
     * Type of the return clause
     */
    type Ret
    /**
     * Type of the handling clauses
     */
    type Clauses
    /**
     * Type of shallow handling clauses
     */
    type SClauses

    /**
     * Construct handler, e.g.,
     * Handler[A,Coin ⊗ E,A,E].! {
     *   case Return(x) => ret(x)
     * } {
     *   case Coin$(p, k) =>  k (biasedCoin(p) ) }
     *
     * c.f. Handler companion below.
     * Convention: ! is for the deep version of the handler, s_! for its shallow version.
     * @param ret
     * @param clauses
     * @return
     */
    def !(ret: Ret)(clauses: Clauses): Handler[I,E,O,F]

    /**
     * This should construct the shallow version of the handler
     * @param ret
     */
    def s_!(ret: Ret)(clauses: SClauses): Handler[I,E,O,F]
  }

  /**
   * A regular handler kind, discharging the front-most effect, i.e.,
   * Comp[E ⊗ R, I] => Comp[R, O]
   * To lighten the notation, we pass a PartialFunction[Any,Any] for the E[_] clauses,
   * which is internally used as a polymorphic lambda using casts.
   * This should be no less safe (and not more unsafe) than the previous version,
   * requiring the user to allocate an FFold instance by hand.
   * @tparam I
   * @tparam E
   * @tparam R
   * @tparam O
   * @return
   */
  implicit def stdHK[I,E[_], R <: Eff, O] =
    new HKind[I, E ⊗ R, O, R] {
      type Ret = Return[E ⊗ R, I] => Comp[R, O]
      type Clauses = PartialFunction[Any,Any]
      override def !(ret: Ret)(clauses: Clauses): Handler[I, E ⊗ R, O, R] = {
          val h = new DeepH[E, R, O] {
            override def apply[X]: (E[X], X => Comp[R, O]) => Comp[R, O] = { (a : E[X], b : X => Comp[R, O]) =>
              clauses((a,b)).asInstanceOf[Comp[R,O]]
            }
          }
          new Handler[I, E ⊗ R, O, R] {
            def apply(c : Comp[E ⊗ R, I]): Comp[R, O] = c match {
              case Return(x) => ret(Return(x))
              case Op(u, k) => decomp(u) match {
                case Right(ex) =>
                  h(ex) { x => apply(k(x)) }
                case Left(op) =>
                  Op(op) { x => apply(k(x)) }
              }
            }
          }
      }
      type SClauses = PartialFunction[Any,Any]
      override def s_!(ret: Ret)(clauses: Clauses): Handler[I, E ⊗ R, O, R] = {
        val h = new ShallowH[E, R, I, O] {
          override def apply[X]: (E[X], X => Comp[E ⊗ R, I]) => Comp[R, O] = { (a: E[X], b: X => Comp[E ⊗ R, I]) =>
            clauses((a, b)).asInstanceOf[Comp[R, O]]
          }
        }

        new Handler[I, E ⊗ R, O, R] {
          def apply(c: Comp[E ⊗ R, I]): Comp[R, O] = c match {
            case Return(x) => ret(Return(x))
            case Op(u, k) => decomp(u) match {
              case Right(ex) =>
                h(ex, k)
              case Left(op) =>
                Op(op) { x => apply(k(x)) }
            }
          }
        }
      }
    }

  /**
   * An open handler kind, replacing the front effect with another:
   * Comp[E ⊗ R, I] => Comp[F ⊗ R, O]. TODO: would be nice if F were an entire list of effects that we prepend to R.
   * @tparam I
   * @tparam E
   * @tparam R
   * @tparam F
   * @tparam O
   */
  implicit def openHK[I,E[_],R<:Eff,F[_],O] =
    new HKind[I, E ⊗ R, O, F ⊗ R]  {
      override type Ret = Return[E ⊗ R, I] => Comp[F ⊗ R, O]
      //TODO it would be better if we had a partial function here too, but then we have the problem of not having a
      //capability canG: G ∈ (G ⊗ R) in scope
      override type Clauses = DeepHO[E,F,R,O]

      override def !(ret: Ret)(h: Clauses): Handler[I, E ⊗ R, O, F ⊗ R] = new Handler[I, E ⊗ R, O, F ⊗ R] {
        override def apply(comp: Comp[E ⊗ R, I]): Comp[F ⊗ R, O] = comp match {
          case Return(x) => ret(Return(x))
          case Op(u, k) => decomp(u) match {
            case Right(ex) =>
              h(ex) { x => apply(k(x))}
            case Left(op) =>
              Op(op.weaken[F]) { x => apply(k(x)) }
          }
        }
      }

      override type SClauses = ShO[E,F,R,I,O]
      override def s_!(ret: Ret)(clauses: SClauses): Handler[I, E ⊗ R, O, F ⊗ R] = ???
    }

  object Handler {
    def apply[I,E <: Eff, O, F <: Eff](implicit kind : HKind[I,E,O,F]): kind.type = kind
  }

  //TODO: koka-style parameterized handlers?
}
