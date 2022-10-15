package gensym.structure.freer

import scala.annotation.implicitNotFound
import scala.language.{higherKinds, implicitConversions}
import scala.reflect.runtime.universe.RuntimeClass

//Track effects as type-level lists of type constructors
object Eff {
  sealed trait Eff
  trait ∅ extends Eff
  trait ⊗[A[_], TL <: Eff] extends Eff

  type Lone[T[_]] = T ⊗ ∅

  @implicitNotFound("Nothing was inferred")
  sealed trait NotNothing[-T]
  object NotNothing {
    implicit object notNothing extends NotNothing[Any]
    //We do not want Nothing to be inferred, so make an ambigous implicit
    implicit object inferredNothing extends NotNothing[Nothing]
  }
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
  object ⊎ {
    implicit class WeakenU[R <: Eff, R2 <: Eff, K](val u : ⊎[R,K]) extends AnyVal {
      def weakenL[X <: Eff](implicit cat : RowConcat[X,R,R2]): ⊎[R2,K] = u match {
        case Union(n, v) => Union(n + cat.n, v)
      }
      def weaken[T[_], X](u: ⊎[R,X]): ⊎[T ⊗ R, X] = u match {
        case Union(n, v) => Union(n+1, v)
      }
    }
  }

  //type-safe pointers into tlists
  trait PtrLvl1
  object PtrLvl1 {
    implicit def ps[T[_], R <: Eff, U[_]](implicit pred: Ptr[T, R]): Ptr[T, U ⊗ R] = Ptr(pred.pos + 1)
  }
  case class Ptr[T[_], R <: Eff] private(pos: Int) extends PtrLvl1
  object Ptr {
    implicit def pz[T[_], R <: Eff](implicit n : NotNothing[R]): Ptr[T, T ⊗ R] = Ptr(0)
  }

  @implicitNotFound("Cannot prove that ${T} is in the effect row ${R}")
  trait ∈[T[_], R <: Eff] {
    def inj[X](sub: T[X]): R ⊎ X
    def prj[X](u: R ⊎ X): Option[T[X]]
  }
  object ∈ {
    implicit def member[T[_], R <: Eff](implicit ptr: Ptr[T, R]): T ∈ R =
      new (T ∈ R) {
        override def inj[X](sub: T[X]): R ⊎ X = Union(ptr.pos, sub)

        override def prj[X](u: R ⊎ X): Option[T[X]] = u match {
          case Union(i, v) if i == ptr.pos => Some(v.asInstanceOf[T[X]])
          case _ => None
        }
      }
  }

  @implicitNotFound("Cannot concatenate RowConcat[${A}, ${B}, ${C}]")
  trait RowConcat[A <: Eff, B <: Eff, C <: Eff] {
    val n : Int
  }
  object RowConcat {
    implicit def concat1[R <: Eff]: RowConcat[∅, R, R] = new RowConcat[∅, R, R] {
      override val n: Int = 0
    }

    implicit def concat2[E[_], R1 <: Eff, R2 <: Eff, R3 <: Eff](implicit prev: RowConcat[R1, R2, R3]): RowConcat[E ⊗ R1, R2, E ⊗ R3] =
      new RowConcat[E ⊗ R1, R2, E ⊗ R3] {
        override val n: Int = prev.n + 1
      }
  }

  //TODO can this be made more flexible? i.e. have decomp at arbitrary positions in the list R?
  def decomp[T[_], R <: Eff, X](u: (T ⊗ R) ⊎ X): Either[R ⊎ X, T[X]] = u match {
    case Union(0, v) => Right(v.asInstanceOf[T[X]])
    case Union(n, v) => Left(Union(n-1, v))
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

  def mapM[E <: Eff, A, B](xs: List[A])(f: A => Comp[E, B]): Comp[E, List[B]] = xs match {
    case Nil => ret(List())
    case x::xs =>
      for {
        b <- f(x)
        bs <- mapM(xs)(f)
      } yield b::bs
  }

  def mapM2Tup[E <: Eff, A, B, C](xs: List[A])(ls: List[C])(f: (A, C) => Comp[E, B]): Comp[E, List[B]] =
    mapM(xs.zip(ls))(Function.tupled(f))

  def mapM2[E <: Eff, A, B, C](xs: List[A])(ls: List[C])(f: A => C => Comp[E, B]): Comp[E, List[B]] =
    mapM2Tup(xs)(ls)(Function.uncurried(f))
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
    implicit val canG: G ∈ (G ⊗ R) = implicitly
  }

  abstract class HO[E[_], X <: Eff, R1 <: Eff, R2 <: Eff, A] extends FFold[E, Comp[R2, A], Comp[R2, A]] {
    implicit val prefix : RowConcat[X, R1, R2] = implicitly
  }

  abstract class ShO[E[_], X <: Eff, R1 <: Eff, R2 <: Eff, A, B] extends FFold[E, Comp[E ⊗ R1, A], Comp[R2, B]] {
    implicit val prefix : RowConcat[X, R1, R2] = implicitly
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

  /**
   * The point of this is to have a designated handler type having
   * a uniform type parameter list and overall clearer and more concise
   * handler definitions in code. Intuitively, Handler[I, E, O, F] is
   * just Comp[E, I] => Comp[F, O]. Previously, different handler types
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

  /** This is to prioritize the implicit resolution of handler implementations.
   * Try first: HKind's companion object, which is the standard "delete effect from the front of row" impl.
   * Fallback: HKindLvl1's companion object, which defines "replace front with a list of other effects" impl.
   * Can extend with higher levels as needed.
   */
  trait HKindLvl2
  object HKindLvl2 {
    implicit def openHK[I,E[_],R<:Eff,X<:Eff,R2<:Eff,O](implicit cat : RowConcat[X,R,R2]) =
      new HKind[I, E ⊗ R, O, R2]  {
        override type Ret = Return[E ⊗ R, I] => Comp[R2, O]
        //TODO it would be better if we had a partial function here too, but then we have the problem of not having a
        //capability canG: G ∈ (G ⊗ R) in scope
        override type Clauses = HO[E,X,R,R2,O]

        override def !(ret: Ret)(h: Clauses): Handler[I, E ⊗ R, O, R2] = new Handler[I, E ⊗ R, O, R2] {
          override def apply(comp: Comp[E ⊗ R, I]): Comp[R2, O] = comp match {
            case Return(x) => ret(Return(x))
            case Op(u, k) => decomp(u) match {
              case Right(ex) =>
                h(ex) { x => apply(k(x))}
              case Left(op) =>
                Op(op.weakenL[X]) { x => apply(k(x)) }
            }
          }
        }

        override type SClauses = ShO[E,X,R,R2,I,O]
        override def s_!(ret: Ret)(h: SClauses): Handler[I, E ⊗ R, O, R2] = new Handler[I, E ⊗ R, O, R2] {
          override def apply(comp: Comp[E ⊗ R, I]): Comp[R2, O] = comp match {
            case Return(x) => ret(Return(x))
            case Op(u, k) => decomp(u) match {
              case Right(ex) =>
                h(ex, k)
              case Left(op) =>
                Op(op.weakenL[X]) { x => apply(k(x)) }
            }
          }
        }
      }
  }

  trait HKindLvl1 extends HKindLvl2
  object HKindLvl1 {


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
  }


  /**
   * Abstracts over the different requirements for different handler types.
   * Used as implicit evidence in the Handler companion's apply() factory method.
   * @tparam I
   * @tparam E
   * @tparam O
   * @tparam F
   */
  @implicitNotFound("Cannot construct a Handler[${I}, ${E}, ${O}, ${F}] with this combination of types.")
  trait HKind[I,E <: Eff, O, F <: Eff] extends HKindLvl1 {
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

  object Handler {
    def apply[I,E <: Eff, O, F <: Eff](implicit kind : HKind[I,E,O,F]): kind.type = kind
  }

  //TODO: koka-style parameterized handlers?
}
