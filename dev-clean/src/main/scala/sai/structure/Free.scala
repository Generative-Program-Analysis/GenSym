package sai.structure.monad
package free

import sai.structure.functor._
import sai.structure.monad.free.VoidEff.∅
import sai.structure.~>

abstract class Free[F[_]: Functor, A] {
  def map[B](f: A => B): Free[F, B] = this match {
    case Return(a) => Return(f(a))
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
  }

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
    case Return(a) => f(a)
    case Impure(x) =>
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }
}
case class Return[F[_]: Functor, A](a: A) extends Free[F, A]
case class Impure[F[_]: Functor, A](x: F[Free[F, A]]) extends Free[F, A]

object Free {
  def apply[F[_], A](implicit f: Free[F, A]): Free[F, A] = f

  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, ?]] =
    new Functor[Free[F, ?]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x.map(f)
    }

  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, ?]] =
    new Monad[Free[F, ?]] {
      def pure[A](a: A): Free[F, A] = Return[F, A](a)
      def flatMap[A, B](ma: Free[F, A])(f: A => Free[F, B]): Free[F, B] = ma.flatMap(f)
    }

  def ret[F[_]: Functor, A](x: A): Free[F,A] = Return(x)
}

object Coproduct {
  abstract class Coprod[F[_], G[_], A]
  case class Left[F[_], G[_], A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_], G[_], A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})

  implicit def CoproductFunctor[F[_]: Functor, G[_]: Functor]: Functor[(F ⊕ G)#t] =
    new Functor[(F ⊕ G)#t] {
      def map[A, B](x: Coprod[F, G, A])(f: A => B): Coprod[F, G, B] = x match {
        case Left(x) => Left(Functor[F].map(x)(f))
        case Right(x) => Right(Functor[G].map(x)(f))
      }
    }
}

sealed trait ⊆[F[_], G[_]] {
  def inj[A](sub: F[A]): G[A]
  def prj[A](sup: G[A]): Option[F[A]]
}

object ⊆ {
  import Free._
  import Coproduct._

  def inject[F[_]: Functor, G[_]: Functor, R](x: F[Free[G, R]])(implicit I: F ⊆ G): Free[G, R] =
    Impure(I.inj(x))

  def project[F[_]: Functor, G[_]: Functor, R](x: Free[G, R])(implicit I: F ⊆ G): Option[F[Free[G, R]]] =
    x match {
      case Impure(f) => I.prj(f)
      case _ => None
    }

  implicit def injRefl[F[_]] = new (F ⊆ F) {
    def inj[A](sub: F[A]) = sub
    def prj[A](sup: F[A]) = Some(sup)
  }

  implicit def injLeft[F[_], G[_]] = new (F ⊆ (F ⊕ G)#t) {
    def inj[A](sub: F[A]) = Left(sub)
    def prj[A](sup: Coprod[F, G, A]) = sup match {
      case Left(fa) => Some(fa)
      case Right(_) => None
    }
  }

  implicit def injRight[F[_], G[_], H[_]](implicit I: F ⊆ G) =
    new (F ⊆ (H ⊕ G)#t) {
      def inj[A](sub: F[A]) = Right(I.inj(sub))
      def prj[A](sup: Coprod[H, G, A]) = sup match {
        case Left(_) => None
        case Right(x) => I.prj(x)
      }
    }
}

object NondetEff {
  import Free._
  import Coproduct._
  import ⊆._

  trait Nondet[+K]
  case object Fail extends Nondet[Nothing]
  case class Choice[K](k1: K, k2: K) extends Nondet[K]

  implicit def NondetFunctor: Functor[Nondet] =
    new Functor[Nondet] {
      def map[A, B](x: Nondet[A])(f: A => B): Nondet[B] = x match {
        case Fail => Fail
        case Choice(k1, k2) => Choice[B](f(k1), f(k2))
      }
    }

  def fail[F[_]: Functor, A](implicit I: Nondet ⊆ F): Free[F, A] = inject[Nondet, F, A](Fail)

  def choice[F[_]: Functor, A](f: Free[F, A], g: Free[F, A])(implicit I: Nondet ⊆ F): Free[F, A] =
    inject[Nondet, F, A](Choice(f, g))

  object Fail$ {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Boolean =
      project[Nondet, F, A](x) match {
        case Some(Fail) => true
        case _ => false
      }
  }

  object Choice${
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Nondet ⊆ F): Option[(Free[F, A], Free[F, A])] =
      project[Nondet, F, A](x) match {
        case Some(Choice(p, q)) => Some((p, q))
        case _ => None
      }
  }

  def run[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] =
    prog match {
      case Return(x) => ret(List(x))
      case Fail$() => ret(List())
      case Choice$(p, q) =>
        for {
          ps <- run(p)
          qs <- run(q)
        } yield ps ++ qs
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(a => run[F, A](a)))
    }

  def apply[F[_]: Functor, A](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] = run(prog)
}

object KondEff {
  import ⊆._
  import Free._
  import Coproduct._

  case class Cnd[E, X, A](cnd: E, thn: A, els: A, k: X => A)

  implicit def CndFunctor[E, X]: Functor[Cnd[E, X, *]] =
    new Functor[Cnd[E, X, *]] {
      def map[A, B](x: Cnd[E, X, A])(f: A => B): Cnd[E, X, B] = {
        val Cnd(c, t, e, k) = x
        Cnd(c, f(t), f(e), x => f(k(x)))
      }
    }

  object Cnd$ {
    def unapply[F[_] : Functor, E, X, A](x: Free[F, A])
      (implicit I: Cnd[E, X, ?] ⊆ F): Option[(E, Free[F, A], Free[F, A], X => Free[F, A])] =
      project[Cnd[E, X, ?], F, A](x) match {
        case Some(Cnd(c, t, e, k)) => Some((c, t, e, k))
        case _ => None
      }
  }

  def cond[F[_]: Functor, E, X, A](cnd: E, thn: Free[F, A], els: Free[F, A])
    (implicit I: Cnd[E, X, *] ⊆ F): Free[F, A] = {
    inject[Cnd[E, X, *], F, A](Cnd(cnd, thn, els, x => {
      System.out.println(x)
      // FIXME: This is not right.
      Return[F, A](x.asInstanceOf[A])
    }))
  }

  def run[F[_]: Functor, E, A](prog: Free[(Cnd[E, A, *] ⊕ F)#t, A])
    (implicit select: (E, A, A) ⇒ A): Free[F, A] = {
    prog match {
      case Return(x) => ret(x)
      case Cnd$(c, t, e, k) =>
        for {
          tv <- run(t)
          ev <- run(e)
          v <- run(k(select(c, tv, ev)))
        } yield { v }
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(a => run[F, E, A](a)))
    }
  }
}

object CondEff {
  import ⊆._
  import Free._
  import Coproduct._

  case class Cnd[E, K](cnd: E, thn: K, els: K)

  implicit def CndFunctor[E]: Functor[Cnd[E, *]] =
    new Functor[Cnd[E, *]] {
      def map[A, B](x: Cnd[E, A])(f: A => B): Cnd[E, B] = {
        val Cnd(c, t, e) = x
        Cnd(c, f(t), f(e))
      }
    }

  object Cnd$ {
    def unapply[F[_] : Functor, E, A](x: Free[F, A])
      (implicit I: Cnd[E, ?] ⊆ F): Option[(E, Free[F, A], Free[F, A])] =
      project[Cnd[E, ?], F, A](x) match {
        case Some(Cnd(c, t, e)) => Some((c, t, e))
        case _ => None
      }
  }

  def cond[F[_]: Functor, E, A](cnd: E, thn: Free[F, A], els: Free[F, A])
    (implicit I: Cnd[E, *] ⊆ F): Free[F, A] = {
    inject[Cnd[E, *], F, A](Cnd(cnd, thn, els))
  }

  type BCnd[T] = Cnd[Boolean, T]
  def bcndSelect[T](b: Boolean, t: T, e: T): T = {
    if (b) t else e
  }

  def run[F[_]: Functor, E, A](prog: Free[(Cnd[E, *] ⊕ F)#t, A])
    (implicit select: (E, A, A) ⇒ A): Free[F, A] = {
    prog match {
      case Return(x) => ret(x)
      case Cnd$(c, t, e) =>
        for {
          tv <- run(t)
          ev <- run(e)
        } yield { select(c, tv, ev) }
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(a => run[F, E, A](a)))
    }
  }

  abstract class CondScope[+K]
  case class BCond[K](k: K) extends CondScope[K]
  case class ECond[K](k: K) extends CondScope[K]

  object BCond$ {
    def unapply[F[_] : Functor, A](x: Free[F, A])
      (implicit I: CondScope ⊆ F): Option[Free[F, A]] =
      project[CondScope, F, A](x) match {
        case Some(BCond(k)) => Some(k)
        case _ => None
      }
  }

  object ECond$ {
    def unapply[F[_] : Functor, A](x: Free[F, A])
      (implicit I: CondScope ⊆ F): Option[Free[F, A]] =
      project[CondScope, F, A](x) match {
        case Some(ECond(k)) => Some(k)
        case _ => None
      }
  }

  implicit def CondScopeFunctor: Functor[CondScope] =
    new Functor[CondScope] {
      def map[A, B](x: CondScope[A])(f: A => B): CondScope[B] = x match {
        case BCond(a) => BCond(f(a))
        case ECond(a) => ECond(f(a))
      }
    }

  def condWithScope[F[_]: Functor, E, A](cnd: E, thn: Free[F, A], els: Free[F, A])
    (implicit I1: Cnd[E, *] ⊆ F, I2: CondScope ⊆ F): Free[F, A] = {
    for {
      _ <- inject[CondScope, F, Unit](BCond(Return()))
      x <- inject(Cnd(cnd, thn, els))
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x
  }

  def upcast[F[_]: Functor, G[_]: Functor, A](prog: Free[G, A]): Free[(F ⊕ G)#t, A] =
    prog match {
      case Return(x) => Return(x)
      case Impure(op) =>
        Impure(Right(Functor[G].map(op)(a => upcast(a))))
    }

  def runBCond[F[_]: Functor, E, A](prog: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A])
    (implicit select: (E, A, A) => A): Free[(Cnd[E, *] ⊕ F)#t, A] = {
    prog match {
      case Return(x) => ret(x)
      case BCond$(k) =>
        implicit def SelectFree(c: E, t: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A], e: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]): Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A] = {
          for {
            tv <- t
            ev <- e
          } yield select(c, tv, ev)
        }
        for {
          u <- upcast[Cnd[E, *], F, Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]](run(runECond(k)))
          v <- runBCond(u)
        } yield v
      case ECond$(k) => throw new RuntimeException("Mismatched BCond")
      case Impure(Right(op)) =>
        Impure[(Cnd[E, *] ⊕ F)#t, A](Functor[(Cnd[E, *] ⊕ F)#t].map(op)(a => runBCond[F, E, A](a)))
    }
  }

  def runECond[F[_]: Functor, E, A](prog: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A])
    (implicit select: (E, A, A) => A):
      Free[(Cnd[E, *] ⊕ F)#t, Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]] = {
    prog match {
      case Return(x) => ret(Return(x))
      case BCond$(k) =>
        implicit def SelectFree(c: E, t: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A], e: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]): Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A] = {
          for {
            tv <- t
            ev <- e
          } yield select(c, tv, ev)
        }
        for {
          u <- upcast[Cnd[E, *], F, Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]](run(runECond(k)))
          v <- runECond(k)
        } yield v
      case ECond$(k) => ret(k)
      case Impure(Right(op)) =>
        Impure(Functor[(Cnd[E, *] ⊕ F)#t].map(op)(a => runECond(a)))
    }
  }

  // Still have the problem of code duplication
  def runWithScope[F[_]: Functor, E, A](prog: Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A])
    (implicit select: (E, A, A) => A): Free[F, A] = {
    run(runBCond(prog))
  }


}


object StateEff {

  import Free._
  import Coproduct._
  import ⊆._

  trait State[S, A]
  case class Get[S, A](k: S => A) extends State[S, A]
  case class Put[S, A](s: S, k: A) extends State[S, A]

  /*
  object Get {
    def apply[F[_]: Functor, S](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
      inject[State[S, ?], F, S](Get(Return(_)))
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: (State[S, ?] ⊆ F)): Option[S => Free[F, A]] = {
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
    }
  }
   */

  implicit def StateFunctor[S]: Functor[State[S, ?]] =
    new Functor[State[S, ?]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: Functor, S](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
    inject[State[S, ?], F, S](Get(Return(_)))

  def put[F[_]: Functor, S](s: S)(implicit I: State[S, ?] ⊆ F): Free[F, Unit] =
    inject[State[S, ?], F, Unit](Put(s, ret(())))

  object Get$ {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[S => Free[F, A]] =
      project[State[S, ?], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
  }

  object Put$ {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, ?] ⊆ F): Option[(S, Free[F, A])] =
      project[State[S, ?], F, A](x) match {
        case Some(Put(s, a)) => Some((s, a))
        case _ => None
      }
  }

  def run[F[_] : Functor, S, A](s: S, prog: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      case Return(a) => ret((s, a))
      case Get$(k) => run(s, k(s))
      case Put$(s1, k) => run(s1, k)
      case Impure(Right(op)) => //TODO: have deep_handle and shallow_handle combinators that hide the default case
        Impure(Functor[F].map(op)(run(s, _)))
    }

  def statefun[F[_] : Functor, S, A](comp: Free[(State[S, ?] ⊕ F)#t, A]): S => Free[F, A] =
    comp match {
      case Return(x) => { _ => ret(x) }
      case Get$(k) => { s =>
        val f = statefun(k(s)) //implicit resolution stumbles with statefun(k(s))(s)
        f(s)
      }
      case Put$(s, k) => { _ =>
        val f = statefun(k)
        f(s)
      }
      case Impure(Right(op)) => { s =>
        Impure(Functor[F].map(op) { a =>
          val f = statefun[F, S, A](a)
          f(s)
        })
      }
    }

  def stateref[F[_] : Functor, S, A](init: S)(comp: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, A] = {
    var state: S = init
    def handler(comp: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, A] = {
      comp match {
        case Return(x) => ret(x)
        case Get$(k) => handler(k(state))
        case Put$(s, k) =>
          state = s
          handler(k)
        case Impure(Right(op)) =>
          Impure(Functor[F].map(op)(handler))
      }
    }
    handler(comp)
  }

  //TODO move these
  //TODO make an idiomatic handler arrow type?
  //TODO have implicit rules that calculate the handler type from the given functions?
  def deep_handler[Eff[_]: Functor, E[_]: Functor, A, B]
                  (r: Return[Eff, A] => Free[E, B])
                  (h: Eff[Free[E, B]] => Free[E, B]): Free[(Eff ⊕ E)#t, A] => Free[E, B] =
    comp => comp match {
      case Return(x) =>
        r(Return(x))
      case Impure(Left(op)) =>
        h(Functor[Eff].map(op)(deep_handler(r)(h))) //this is what makes the handler "deep"
      case Impure(Right(op)) =>
        Impure(Functor[E].map(op)(deep_handler(r)(h)))
    }

  def shallow_handler[Eff[_]: Functor, E[_]: Functor, A, B]
                     (r: Return[Eff, A] => Free[E, B])
                     (h: Eff[Free[(Eff ⊕ E)#t, A]] => Free[E, B]): Free[(Eff ⊕ E)#t, A] => Free[E, B] =
    comp => comp match {
      case Return(x) => r(Return(x))
      case Impure(Left(op)) => h(op)
      case Impure(Right(op)) => Impure(Functor[E].map(op)(shallow_handler(r)(h)))
    }

  //Effect-polymorphic open handler in style of handlers in action paper
  def deep_handler_open[Eff[_]: Functor, G[_]: Functor, E[_]: Functor, A, B]
                   (r: Return[Eff, A] => Free[(G ⊕ E)#t, B])
                   (h: Eff[Free[(G ⊕ E)#t, B]] => Free[(G ⊕ E)#t, B]): Free[(Eff ⊕ E)#t, A] => Free[(G ⊕ E)#t, B] =
    comp => comp match {
      case Return(x) =>
        r(Return(x))
      case Impure(Left(op)) =>
        h(Functor[Eff].map(op)(deep_handler_open(r)(h))) //this is what makes the handler "deep"
      case Impure(Right(op)) =>
        Impure(Right(Functor[E].map(op)(deep_handler_open(r)(h))))
    }

  //TODO move this
  implicit def extract[A](comp: Free[∅, A]): A = comp match {
    case Return(a) => a
  }

  def stateh[E[_]: Functor, S, A] =
    deep_handler[State[S, ?], ∅, A, S => Free[E, A]] {
      case Return(x) => ret(_ => ret(x))
    }{
      case Get(k)    => ret(s => k(s)(s))
      case Put(s, k) => ret(_ => k(s))
    }

  /*
  def stateh_shallow[E[_]: Functor, S, A] =
    shallow_handler[State[S, ?], ∅, A, S => Free[E, A]] {
      case Return(x) => ret(_ => ret(x))
    }{
      case Get(k) => ret(s =>  //Free[∅, S => Free[E, A]]
      case Put(s, k) => ret(_ => ???) //k(s))
    }
   */

  /*def stateh2[E[_]: Functor, S, A] =
    deep_handler_open[State[S, ?], ∅, E, A, S => A] {
      case Return(x) => ret { _ => x}
    }{
      case Get(k)    =>
      // k : S => Free[0+E, S => A]
      //
    }*/
}

object StateNondetEff {
  import ⊆._
  import Free._
  import Coproduct._
  import StateEff._
  import NondetEff._

  def runLocal[F[_]: Functor, S, A](s: S, prog: Free[(State[S, ?] ⊕ (Nondet ⊕ F)#t)#t, A]): Free[F, List[(S, A)]] =
    NondetEff.run(StateEff.run(s, prog))

  def runGlobal[F[_]: Functor, S, A](s: S, prog: Free[(Nondet ⊕ (State[S, ?] ⊕ F)#t)#t, A]): Free[F, (S, List[A])] =
    StateEff.run(s, NondetEff.run(prog))
}

object VoidEff {
  trait ∅[+K]

  implicit val VoidFunctor: Functor[∅] =
    new Functor[∅] {
      def map[A, B](x: ∅[A])(f: A => B): ∅[B] = x.asInstanceOf[∅[B]]
    }

  def run[A](f: Free[∅, A]): A = f match {
    case Return(x) => x
  }

  def apply[A](f: Free[∅, A]): A = run(f)
}

object NondetVoidEff {
  import Coproduct._
  import NondetEff._
  import VoidEff._

  def allsols[A](prog: Free[(Nondet ⊕ ∅)#t, A]): List[A] =
    VoidEff(NondetEff(prog))
}

object CutHandler {
  import Free._
  import Coproduct._
  import ⊆._
  import NondetEff._

  trait Cut[+A]
  case object Cutfail extends Cut[Nothing]

  implicit val CutFunctor: Functor[Cut] =
    new Functor[Cut] {
      def map[A, B](x: Cut[A])(f: A => B): Cut[B] = x.asInstanceOf[Cut[B]]
    }

  object Cutfail$ {
    def unapply[F[_]: Functor, A](x: Free[F, A])(implicit I: Cut ⊆ F): Boolean =
      project[Cut, F, A](x) match {
        case Some(Cutfail) => true
        case _ => false
      }
  }

  def cutfail[F[_]: Functor, A](implicit I: Cut ⊆ F): Free[F, A] =
    inject[Cut, F, A](Cutfail)

  def go[F[_]: Functor, A](p: Free[(Cut ⊕ F)#t, A], q: Free[F, A])(implicit I: Nondet ⊆ F): Free[F, A] =
    p match {
      case Return(a) => choice(ret(a), q)
      case Fail$() => q
      case Cutfail$() => fail
      case Choice$(a, b) => go(a, go(b, q))
      case Impure(Right(op)) =>
        Impure(Functor[F].map(op)(go(_, q)))
    }

  def call[F[_]: Functor, A](p: Free[(Cut ⊕ F)#t, A])(implicit I: Nondet ⊆ F): Free[F, A] = go(p, fail)

  def skip[M[_]: Monad]: M[Unit] = Monad[M].pure(())

  def cut[F[_]: Functor, A](implicit I1: Nondet ⊆ F, I2: Cut ⊆ F): Free[F, Unit] =
    choice(skip[Free[F,?]], cutfail)

  def once[F[_]: Functor, A](p: Free[(Cut ⊕ F)#t, A])(implicit I: Nondet ⊆ F): Free[F, A] =
    call(for { x <- p; _ <- cut[(Cut ⊕ F)#t, A] } yield x)
}

// TODO: CPS effect, to support Imp's break from while loop

object Knapsack {
  import Free._
  import Coproduct.{CoproductFunctor => _, _}
  import NondetEff.{NondetFunctor => _, _}
  import VoidEff.{VoidFunctor => _, _}
  import StateEff.{StateFunctor => _, _}
  import CutHandler.{CutFunctor => _, _}
  import NondetVoidEff._
  import StateNondetEff._

  def inc[F[_]: Functor](implicit I: State[Int, ?] ⊆ F): Free[F, Unit] =
    for {
      x <- get
      _ <- put(x + 1)
    } yield ()

  def choices[F[_]: Functor, A](prog: Free[F, A])(implicit I1: Nondet ⊆ F, I2: State[Int, ?] ⊆ F): Free[F, A] =
    prog match {
      case Return(x) => ret(x)
      case Fail$() => fail
      case Choice$(p, q) =>
        for {
          _ <- inc
          pq <- choice(choices(p), choices(q))
        } yield pq
      case Impure(op) => Impure(Functor[F].map(op)(choices(_)))
    }

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def knapsack[F[_]: Functor](w: Int, vs: List[Int])(implicit I: Nondet ⊆ F): Free[F, List[Int]] = {
    if (w < 0)
      fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select(vs)
      xs <- knapsack(w-v, vs)
    } yield v :: xs
  }

  def main(args: Array[String]) {
    import VoidEff.VoidFunctor
    import StateEff.StateFunctor
    import NondetEff.NondetFunctor
    import CutHandler.CutFunctor

    // Only nondeterminism effect
    println(allsols(knapsack(3, List(3, 2, 1))))

    val global: (Int, List[List[Int]]) = {
      // Note: have to manually define an implicit functor instance here,
      //       otherwise Scala compiler complains implicit expansion divergence
      implicit val F = Functor[(State[Int, ?] ⊕ ∅)#t]
      VoidEff(runGlobal(0, choices(knapsack[(Nondet ⊕ (State[Int, ?] ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(global)

    val local: List[(Int, List[Int])] = {
      implicit val F = Functor[(Nondet ⊕ ∅)#t]
      VoidEff(runLocal(0, choices(knapsack[(State[Int, ?] ⊕ (Nondet ⊕ ∅)#t)#t](3, List(3, 2, 1)))))
    }
    println(local)

    // only computes the first solution
    val single: List[List[Int]] = {
      implicit val F = Functor[(Nondet ⊕ ∅)#t]
      allsols(once(knapsack[(Cut ⊕ (Nondet ⊕ ∅)#t)#t](3, List(3, 2, 1))))
    }
    println(single)
  }
}

object ImpEff {
  import Free._
  import Coproduct.{CoproductFunctor => _, _}
  import NondetEff.{NondetFunctor => _, _}
  import VoidEff.{VoidFunctor => _, _}
  import StateEff.{StateFunctor => _, _}
  import NondetVoidEff._
  import StateNondetEff._

  import sai.lang.ImpLang._

  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value

  type Store = Map[String, Value]

  def eval(e: Expr, σ: Store): Value = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val IntV(i) = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val IntV(i1) = eval(e1, σ)
      val IntV(i2) = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def eval[F[_]: Functor](e: Expr)(implicit I1: Nondet ⊆ F, I2: State[Store, ?] ⊆ F): Free[F, Value] =
    for {
      σ <- get
    } yield eval(e, σ)

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def select[F[_]: Functor](e: Expr, s1: Stmt, s2: Stmt)(implicit I: Nondet ⊆ F, I2: State[Store, ?] ⊆ F): Free[F, Stmt] =
    for {
      v <- eval(e)
    } yield {
      val BoolV(b) = v
      if (b) s1 else s2
    }

  def exec[F[_]: Functor](s: Stmt)(implicit I1: Nondet ⊆ F, I2: State[Store, ?] ⊆ F): Free[F, Unit] = s match {
    case Skip() => ret(())
    case Assign(x, e) =>
      for {
        σ <- get
        v <- eval(e)
        _ <- put(σ + (x → v))
      } yield ()
    case Cond(e, s1, s2) =>
      for {
        n <- select(e, s1, s2)
        _ <- exec(s)
      } yield ()
    case Seq(s1, s2) =>
      for {
        _ <- exec(s1)
        _ <- exec(s2)
      } yield ()
    case While(e, b) =>
      for {
        n <- select(e, Seq(b, s), Skip())
        _ <- exec(n)
      } yield ()
  }

  def main(args: Array[String]): Unit = {
    import Examples._
    import VoidEff.VoidFunctor
    import StateEff.StateFunctor
    import NondetEff.NondetFunctor

    //implicit val F1 = Functor[(State[Store, ?] ⊕ ∅)#t]
    implicit val F2 = Functor[(Nondet ⊕ ∅)#t]
    //println(fact5)

    // concrete execution
    println(VoidEff.run(NondetEff.run(StateEff.run(Map[String, Value](), exec[(State[Store, ?] ⊕ (Nondet ⊕ ∅)#t)#t](fact5)))))
  }
}

import lms.core._
import lms.core.stub._
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

import Free._
import Coproduct.{CoproductFunctor => _, _}
import NondetEff.{NondetFunctor => _, _}
import VoidEff.{VoidFunctor => _, _}
import StateEff.{StateFunctor => _, _}
//import CondEff.{CndFunctor => _, CondScopeFunctor => _, _}
import KondEff.{CndFunctor => _, _}
import NondetVoidEff._
import StateNondetEff._
import ⊆._

@virtualize
trait StagedImpEff extends SAIOps {
  import sai.lang.ImpLang._

  trait Value
  //case class IntV(i: Int) extends Value
  //case class BoolV(b: Boolean) extends Value
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def BoolV(b: Rep[Boolean]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))

  implicit def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  implicit def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }

  type Store = Map[String, Value]

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val i: Rep[Int] = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val i1: Rep[Int] = eval(e1, σ)
      val i2: Rep[Int] = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def eval[F[_]: Functor](e: Expr)
    (implicit I: State[Rep[Store], ?] ⊆ F): Free[F, Rep[Value]] =
    for { σ <- get } yield eval(e, σ)

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def select[F[_]: Functor](e: Expr, s1: Stmt, s2: Stmt)
    (implicit I: Nondet ⊆ F, I2: State[Rep[Store], ?] ⊆ F): Free[F, Rep[Stmt]] =
    for {
      v <- eval(e)
    } yield {
      val b = rep_bool_proj(v)
      if (b) s1 else s2
    }

  type Result = (Store, Unit)

  def h[F[_]: Functor](prog: Free[F, Rep[Unit]])
    (implicit I: State[Rep[Store], ?] ⊆ F): Rep[Result] =
    ???

  def r[F[_]: Functor](res: Rep[Result])
    (implicit I: State[Rep[Store], ?] ⊆ F): Free[F, Rep[Unit]] =
    ???
    //r_state[F, Store, Unit](res)

  def upcast[F[_]: Functor, G[_]: Functor, A: Manifest](prog: Free[G, A]): Free[(F ⊕ G)#t, A] =
    prog match {
      case Return(x) => Return(x)
      case Impure(op) => Impure(Right(Functor[G].map(op)(a => upcast(a))))
    }

  def h_state[S: Manifest, A: Manifest](s: Rep[S])
    (prog: Free[(State[Rep[S], *] ⊕ ∅)#t, Rep[A]]): Rep[(S, A)] = 
    VoidEff.run(StateEff.run(s, prog))

  def r_state[S: Manifest, A: Manifest]
    (res: Rep[(S, A)]): Free[(State[Rep[S], *] ⊕ ∅)#t, Rep[A]] = {
    val s = res._1
    val a = res._2
    for {
      _ <- inject[State[Rep[S], *], (State[Rep[S], *] ⊕ ∅)#t, Unit](Put(s, ret(())))
    } yield a
  }

  def r_nondet[F[_]: Functor, A: Manifest]
    (res: Rep[List[A]]): Free[(F ⊕ Nondet)#t, Rep[A]] = {
    val size: Rep[Int] = res.size
    // we need to know the size of res
    ???
  }

  //def exec[F[_]: Functor](s: Stmt)(implicit I: State[Rep[Store], ?] ⊆ F): Free[F, Rep[Unit]] =
  def exec(s: Stmt): Free[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Unit]] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) =>
        for {
          σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
          v <- eval[(State[Rep[Store], *] ⊕ ∅)#t](e)
          _ <- put[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]](σ + (x → v))
        } yield ()
      case Cond(e, s1, s2) =>
        /*
        val res: Free[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Unit]] =
          for {
            b <- eval[(State[Rep[Store], *] ⊕ ∅)#t](e)
            σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
            _ <- Return[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Result]](if (b) h_state(σ)(exec(s1)) else h_state(σ)(exec(s2)))
          } yield ()
        for {
          _ <- res
        } yield ()
         */
        /*
        // h_state': Rep[Store] => Free[State + F, Rep[Unit]] => Free[F, Rep[Unit]]
        // Seq(Cond(...), Assign(...))
        val m1 = for {
          b <- eval[(State[Rep[Store], *] ⊕ ∅)#t](e)
          σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
          val then = h_state(σ)(exec(s1)_ >>= ...
          val els = h_state... //Free[???, Rep[Unit]

          f1 <- shift { (k: Rep[Unit] => Free[F, Rep[Unit]]) => h_state'(σ)(exec(s1)) >>= k }
          //v1 <- (h_state(σ)(exec(s1)) >>= ((r, s) => for { _ <- put(s) } yield r))
          //v2 <- (h_state(σ)(exec(s2)) >>= ((r, s) => ... ))
        } yield { quote { if (b) splice(then) else splice(els) } }

          splice[A]: Free[???, Rep[A]] => Rep[A]            
          quote = handler {
            x => x
            //k: Rep[A] => Free[???,???]
            splice(comp), k => comp >>= k
          }

          outer_state_handler {
            exec(Cond):

            then: Rep[Store] = h_state#1(get#outer){ exec(s1) //get,put handled by #1    } >>= (r,s) => put(s); r
            els = h_state#2(get#outer){ exec(s2) //get,put handled by #2 }  >>= (r,s) => put(s); r
          }
         */
        val res: Free[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Result]] =
          for {
            b <- eval[(State[Rep[Store], *] ⊕ ∅)#t](e)
            σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
          } yield { if (b) h_state(σ)(exec(s1)) else h_state(σ)(exec(s2)) }
        for {
          e <- res
          a <- r_state(e)
        } yield a
      case Seq(s1, s2) =>
        for {
          _ <- exec(s1)
          _ <- exec(s2)
        } yield ()
      case While(e, b) =>
        // This becomes super ugly, needs clean up
        def loop(in: Rep[Result]): Rep[Result] = {
          val f: Free[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Result]] =
            for {
              _ <- r_state(in)
              v <- eval[(State[Rep[Store], *] ⊕ ∅)#t](e)
              σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
            } yield {
              if (v) rep_loop(h_state(σ)(exec(b))) else h_state(σ)(exec(Skip()))
            }
          val out = h_state[Store, Result](Map())(f) // Here the mt store doesn't matter
          out._2
        }
        def rep_loop: Rep[Result => Result] = fun(loop)

        val res: Free[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Result]] =
          for {
            σ <- get[(State[Rep[Store], *] ⊕ ∅)#t, Rep[Store]]
          } yield { rep_loop((σ, ())) }
        for {
          e <- res
          a <- r_state(e)
        } yield a
    }
}

trait StagedImpEffGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    //case n @ Node(s, op, List(x), _) if op.startsWith("unchecked") => ???
    case Node(s, "IntV", List(i), _) =>
      emit("IntV(")
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[IntV].i")
    case Node(s, "BoolV", List(b), _) =>
      emit("BoolV(")
      shallow(b)
      emit(")")
    case Node(s, "BoolV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[BoolV].b")
    case _ => super.shallow(n)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "?", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(s, op, List(), _) if op.startsWith("unchecked") =>
      emit(op.substring("unchecked".size, op.size))
    case n @ Node(s, op, List(x), _) if op.startsWith("unchecked") =>
      shallow(x)
    case _ => super.traverse(n)
  }
}

trait StagedImpEffDriver[A, B] extends SAIDriver[A, B] with StagedImpEff { q =>
  override val codegen = new ScalaGenBase with StagedImpEffGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString == "java.lang.String") "String"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import sai.lang.ImpLang._
trait Value
case class IntV(i: Int) extends Value
case class BoolV(b: Boolean) extends Value
"""
}

object StagedImpEff {
  import sai.lang.ImpLang._

  val cond_ex =
    Seq(
      Cond(Op2("<=", Var("x"), Var("y")),
        Assign("z", Var("x")),
        Assign("z", Var("y"))),
      Assign("z", Op2("+", Var("z"), Lit(1))))

  val fact_n =
    Seq(Assign("i", Lit(1)),
      Seq(Assign("fact", Lit(1)),
        While(Op2("<=", Var("i"), Var("n")),
          Seq(Assign("fact", Op2("*", Var("fact"), Var("i"))),
            Assign("i", Op2("+", Var("i"), Lit(1)))))))

  @virtualize
  def specialize(): SAIDriver[Int, Int] =
    new StagedImpEffDriver[Int, Int] {

      def snippet1(u: Rep[Int]) = {
        val s0: Rep[Map[String, Value]] = Map(("x", IntV(u)), ("y", IntV(u+1)))
        val f = exec(cond_ex)
        val result = h_state(s0)(f)
        println(result)
        result._1("z")
      }

      def snippet2(u: Rep[Int]) = {
        val s0: Rep[Map[String, Value]] = Map(("n", IntV(u)))
        val f = exec(fact_n)
        val result = h_state(s0)(f)
        println(result)
        result._1("fact")
      }

      def snippet3(u: Rep[Int]): Rep[Int] = {

        def exprog1[F[_]: Functor]
          (implicit I1: State[Rep[List[Int]], *] ⊆ F): Free[F, Rep[Int]] =
          for {
            xs <- get[F, Rep[List[Int]]]
            _  <- put[F, Rep[List[Int]]](xs.map(x => x + 2))
            ys <- get[F, Rep[List[Int]]]
          } yield ys(0)

        def exprog2[F[_]: Functor]
          (implicit I1: State[Rep[List[Int]], *] ⊆ F): Free[F, Rep[Int]] =
          for {
            xs <- get[F, Rep[List[Int]]]
            _  <- put[F, Rep[List[Int]]](xs.map(x => x * x))
            ys <- get[F, Rep[List[Int]]]
          } yield ys(0)

        //val res = VoidEff.run(StateEff.run(List(u), exprog2[(State[Rep[List[Int]], *] ⊕ ∅)#t]))
        //res._2

        type RBCnd[T] = Cnd[Rep[Boolean], (Rep[List[Int]], Rep[Int]), T]
        implicit def bcndSelect(b: Rep[Boolean], t: Rep[Int], e: Rep[Int]): Rep[Int] = {
          System.out.println("rep boolean select")
          if (b) t else e
        }
        implicit val F1 = Functor[(State[Rep[List[Int]], *] ⊕ ∅)#t]
        implicit val F2 = Functor[(RBCnd ⊕ ∅)#t]

        def exprog3[F[_]: Functor]
          (implicit I1: RBCnd ⊆ F,
            I2: State[Rep[List[Int]], *] ⊆ F
            /*  I3: CondScope ⊆ F */
          ): Free[F, Rep[Int]] = {
          for {
            x <- get[F, Rep[List[Int]]]
            y <- cond[F, Rep[Boolean], (Rep[List[Int]], Rep[Int]), Rep[Int]](x == 0, exprog2[F], exprog1[F])
            //y <- condWithScope(x == 0, exprog2[F], exprog1[F])
            _ <- put[F, Rep[List[Int]]](List(y))
            z <- get[F, Rep[List[Int]]]
          } yield z(0)
        }

        //val res1 = VoidEff.run(StateEff.run(List(u),
        //  KondEff.run(exprog3[(RBCnd ⊕ (State[Rep[List[Int]], *] ⊕ ∅)#t)#t])))

        implicit def bcndSelectState(b: Rep[Boolean],
          t: (Rep[List[Int]], Rep[Int]), e: (Rep[List[Int]], Rep[Int])): (Rep[List[Int]], Rep[Int]) = {
          System.out.println("rep boolean select")
          val r: Rep[(List[Int], Int)] = if (b) t else e
          (r._1, r._2)
        }

        //implicit val F3 = Functor[(CondScope ⊕ (RBCnd ⊕ ∅)#t)#t]

        val res1 = VoidEff.run(KondEff.run(StateEff.run(List(u),
          exprog3[(State[Rep[List[Int]], *] ⊕ (RBCnd ⊕ ∅)#t)#t])))

        //val res1 = VoidEff.run(CondEff.runWithScope(StateEff.run(List(u),
        //  exprog3[(State[Rep[List[Int]], *] ⊕ (CondScope ⊕ (RBCnd ⊕ ∅)#t)#t)#t])))

        res1._2
      }

      def snippet(u: Rep[Int]): Rep[Int] = snippet3(u)
    }

  def main(args: Array[String]) {
    val code = specialize()
    println(code.code)
    println(code.eval(5))
  }
}
