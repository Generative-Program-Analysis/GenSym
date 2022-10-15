package gensym.structure.freer

import scala.language.{higherKinds, implicitConversions}

object State {
  import Eff._
  import OpenUnion._
  import Freer._
  import Handlers._

  sealed trait State[S, +K]
  case class Put[S](x: S) extends State[S,Unit] //Put: S ~> Unit
  case class Get[S]() extends State[S,S] //Get: Unit ~> S

  def put[S, R <: Eff](x: S)(implicit I: State[S,*] ∈ R): Comp[R, Unit] =
    perform[State[S, *], R, Unit](Put(x))
  def get[S, R <: Eff]()(implicit I: State[S,*] ∈ R): Comp[R, S] =
    perform[State[S, *], R, S](Get())

  //Scala's type checker struggles with GADTs having more than one type parameter (e.g. State[S,K]) in pattern
  //matching clauses. We define custom extractors to relieve programmers from manual type casts.
  object Get$ {
    def unapply[S, X, R](p: (State[S, X], X => R)): Option[(Unit, S => R)] = p match {
      case (Get(), k) => Some(((), k.asInstanceOf[S => R])) //the compiler cannot infer that X = S
      case _ => None
    }
  }

  object Put$ {
    def unapply[S, X, R](p: (State[S, X], X => R)): Option[(S, Unit => R)] = p match {
      case (Put(s), k) => Some((s, k))
      case _ => None
    }
  }

  def run[E <: Eff, S, A]: Comp[State[S, *] ⊗ E, A] => Comp[E, S => Comp[E, (S, A)]] =
    handler[State[S, *], E, A, S => Comp[E, (S, A)]] {
      case Return(x) => ret { s: S => ret((s, x)) }
    } (ν[DeepH[State[S, *], E, S => Comp[E, (S, A)]]] {
      case Get$((), k) => ret { s: S =>
        k(s) >>= (r => r(s))
      }
      case Put$(s, k) => ret { _: S =>
        k(()) >>= (r => r(s))
      }
    })

  // TODO: Double check this
  def runState[E <: Eff, S, A](s: S): Comp[State[S, *] ⊗ E, A] => Comp[E, (S, A)] = {
    case Return(x) => ret((s, x))
    case Op(u, k) => decomp[State[S, *], E, Any](u) match {
      case Right(op) => op match {
        case Get() => runState(s)(k(s))
        case Put(s1) => runState(s1)(k(()))
      }
      case Left(op) =>
        Op(op) { x => runState(s)(k(x)) }
    }
  }
}
