package sai.structure.freer

import scala.language.{higherKinds, implicitConversions, existentials}

import sai.structure.freer.Eff._
import sai.structure.freer.Freer._
import sai.structure.freer.Handlers._
import sai.structure.freer.OpenUnion._

import lms.core._
import lms.core.stub.{While => _, _}
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

@virtualize
trait StagedNondet extends SAIOps {

  // def f[B: Manifest](x: Rep[List[B]]): Rep[List[B]] = x ++ x
  // def ++[A: Manifest](xs: Rep[List[A]], ys: Rep[List[A]]): Rep[List[A]] =
  //  Wrap[List[A]](Adapter.g.reflect("list-concat", Unwrap(xs), Unwrap(ys)))

  // case class NondetList[A: Manifest](xs: Rep[List[A]])
  abstract class Nondet[+A]
  case class NondetList[A : Manifest](xs: Rep[List[A]]) extends Nondet[Rep[A]] {
    val m: Manifest[A] = implicitly
  }
  case object BinChoice extends Nondet[Boolean]

  def fail[R <: Eff, A: Manifest](implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(List()))

  def choice[R <: Eff, A: Manifest](x: Rep[A], y: Rep[A])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(List(x, y)))

  def choice[R <: Eff, A: Manifest](a: Comp[R, Rep[A]], b: Comp[R, Rep[A]])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Boolean](BinChoice) >>= {
      case true => a
      case false => b
    }

  def select[R <: Eff, A: Manifest](xs: Rep[List[A]])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, Rep[A]](NondetList(xs))

  object NondetList$ {
    def unapply[A: Manifest, R, X](n: (Nondet[X], X => R)): Option[(Rep[List[A]], Rep[A] => R)] =
      n match {
        case (NondetList(xs), k) => Some((xs.asInstanceOf[Rep[List[A]]], k))
        case _ => None
      }
  }
  object NondetListEx$ {
    trait Result[+R] {
      type K
      def get : (Manifest[K], Rep[List[K]])
    }
    def unapply[R, X](n: Nondet[X]): Option[Result[R]] =
      n match {
        case nl @ NondetList(xs) => Some(new Result[R] {
          override type K = Any
          override def get = (nl.m.asInstanceOf[Manifest[K]], xs.asInstanceOf[Rep[List[K]]])
        })

        case _ => None
      }
    object ?? {
      def unapply[R](r : Result[R]) : Option[(Manifest[r.K], Rep[List[r.K]])] = Some(r.get)
    }
  }
  object BinChoice$ {
    def unapply[K, R](n: (Nondet[K], K => R)): Option[(Unit, Boolean => R)] = n match {
      case (BinChoice, k) => Some(((), k))
      case _ => None
    }
  }

  // Observation: curried style works really bad when having Manifest
  def runRepNondet[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = {
    val h = handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X] = {
        case NondetList$(xs, k) =>
          //xs : Rep[List[B]], k : Rep[B] => Comp[∅, Rep[List[A]]], need manifest of B?
          ret(xs.foldLeft(List[A]()) { case (acc, x) =>
            acc ++ k(x)
          })
        case BinChoice$((), k) =>
          for {
            xs <- k(true)
            ys <- k(false)
          } yield xs ++ ys
      }
    })
    h(comp)
  }

  def runRepNondet2[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = comp match {
    case Return(x) => ret(List(x))
    case Op(u, k) => decomp(u) match {
      case Right(nd) => nd match {
        case NondetList(xs) =>
          ret(xs.foldLeft(List[A]()) { case (acc, x) =>
            acc ++ extract(runRepNondet2(k(x)))
          })
        case BinChoice =>
          for {
            xs <- runRepNondet2(k(true))
            ys <- runRepNondet2(k(false))
          } yield xs ++ ys
      }
      case Left(u) =>
        Op(u) { x => runRepNondet2(k(x)) }
    }
  }

  //implicit def manifestfromndet[A](nl : NondetList[A]): Manifest[A] = nl.m
  //implicit def manifestfromresult[A](r : NondetListEx$.Result[A]): Manifest[r.K] = r.m
  // TODO: Doesn't work yet
  def runRepNondet3[E <: Eff, A: Manifest](comp: Comp[Nondet ⊗ E, Rep[A]]): Comp[E, Rep[List[A]]] = {
    import NondetListEx$.??
    comp match {
      case Return(x) => ret(List(x))
      case Op(u, k) => decomp(u) match {
        case Right(nd) => nd match {
          case nl @ NondetListEx$(??(m,xs)) =>
            def handleNdet[B : Manifest](xs : Rep[List[B]]): Comp[E, Rep[List[A]]] = {
              // k: Rep[B] => Comp[Nondet x E, List[A]]
              // k_eta: Comp[E, Rep[B => List[A]]]
              // k_eta': Comp[E, Rep[List[A]]]
              val k_eta: Comp[E, Rep[B=>List[A]]] =
                close[B, A, B, List[A], B=>List[A], Comp[Nondet ⊗ E, *], Comp[E, *]](x => k(x), { (c, m) =>
                  runRepNondet3(m).map(c(_))
                })
              k_eta.map(k => xs.foldLeft(List[A]()) { case (acc, x) => acc ++ k(x) })

              /*
              close[B, List[A], B, List[A], List[A], Comp[E, *], Comp[E, *]](x => runRepNondet3(k(x)), { (c, m) =>
                m.map { f =>
                  val f2: Rep[B => List[A]] = c(f)
                  xs.foldLeft(List[A]()) { case (acc, x) => acc ++ f2(x.asInstanceOf[Rep[B]]) }
                }
              })
               */
            }
            handleNdet(xs)(m)
        }
        case Left(u) =>
          Op(u) { x => runRepNondet3(k(x)) }
      }
    }
  }
}

