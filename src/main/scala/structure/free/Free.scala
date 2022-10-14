package gensym.structure.monad.free

import gensym.structure.monad._
import gensym.structure.functor._
import gensym.structure.monad.free.VoidEff.∅
import gensym.structure.~>

// Follows the algebraic effects implementation in Effect Handlers in Scope (Haskell '14)

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

  implicit def FreeFunctorInstance[F[_]: Functor]: Functor[Free[F, *]] =
    new Functor[Free[F, *]] {
      def map[A, B](x: Free[F, A])(f: A => B): Free[F, B] = x.map(f)
    }

  implicit def FreeMonadInstance[F[_]: Functor]: Monad[Free[F, *]] =
    new Monad[Free[F, *]] {
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

object StateEff {
  import Free._
  import Coproduct._
  import ⊆._

  trait State[S, A]
  case class Get[S, A](k: S => A) extends State[S, A]
  case class Put[S, A](s: S, k: A) extends State[S, A]

  /*
  object Get {
    def apply[F[_]: Functor, S](implicit I: (State[S, *] ⊆ F)): Free[F, S] =
      inject[State[S, *], F, S](Get(Return(_)))
    def unapply[F[_]: Functor, S, A](x: Free[F, A])(implicit I: (State[S, *] ⊆ F)): Option[S => Free[F, A]] = {
      project[State[S, *], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
    }
  }
   */

  implicit def StateFunctor[S]: Functor[State[S, *]] =
    new Functor[State[S, *]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: Functor, S](implicit I: (State[S, *] ⊆ F)): Free[F, S] =
    inject[State[S, *], F, S](Get(Return(_)))

  def put[F[_]: Functor, S](s: S)(implicit I: State[S, *] ⊆ F): Free[F, Unit] =
    inject[State[S, *], F, Unit](Put(s, ret(())))

  object Get$ {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, *] ⊆ F): Option[S => Free[F, A]] =
      project[State[S, *], F, A](x) match {
        case Some(Get(k)) => Some(k)
        case _ => None
      }
  }

  object Put$ {
    def unapply[F[_] : Functor, S, A](x: Free[F, A])(implicit I: State[S, *] ⊆ F): Option[(S, Free[F, A])] =
      project[State[S, *], F, A](x) match {
        case Some(Put(s, a)) => Some((s, a))
        case _ => None
      }
  }

  def run[F[_] : Functor, S, A](s: S, prog: Free[(State[S, *] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      case Return(a) => ret((s, a))
      case Get$(k) => run(s, k(s))
      case Put$(s1, k) => run(s1, k)
      case Impure(Right(op)) => //TODO: have deep_handle and shallow_handle combinators that hide the default case
        Impure(Functor[F].map(op)(run(s, _)))
    }

  def statefun[F[_] : Functor, S, A](comp: Free[(State[S, *] ⊕ F)#t, A]): S => Free[F, A] =
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

  def stateref[F[_] : Functor, S, A](init: S)(comp: Free[(State[S, *] ⊕ F)#t, A]): Free[F, A] = {
    var state: S = init
    def handler(comp: Free[(State[S, *] ⊕ F)#t, A]): Free[F, A] = {
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
    deep_handler[State[S, *], ∅, A, S => Free[E, A]] {
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

  def runLocal[F[_]: Functor, S, A](s: S, prog: Free[(State[S, *] ⊕ (Nondet ⊕ F)#t)#t, A]): Free[F, List[(S, A)]] =
    NondetEff.run(StateEff.run(s, prog))

  def runGlobal[F[_]: Functor, S, A](s: S, prog: Free[(Nondet ⊕ (State[S, *] ⊕ F)#t)#t, A]): Free[F, (S, List[A])] =
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
    choice(skip[Free[F, *]], cutfail)

  def once[F[_]: Functor, A](p: Free[(Cut ⊕ F)#t, A])(implicit I: Nondet ⊆ F): Free[F, A] =
    call(for { x <- p; _ <- cut[(Cut ⊕ F)#t, A] } yield x)
}

//////////////////////////////////////////////////////////////////////////////////

// NOTE: this doesn't work...

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
      (implicit I: Cnd[E, X, *] ⊆ F): Option[(E, Free[F, A], Free[F, A], X => Free[F, A])] =
      project[Cnd[E, X, *], F, A](x) match {
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

// NOTE: this doesn't work, too...

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
      (implicit I: Cnd[E, *] ⊆ F): Option[(E, Free[F, A], Free[F, A])] =
      project[Cnd[E, *], F, A](x) match {
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
    /*
    val t = inject[CondScope, F, A](BCond(for {
      x <- thn
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x))
    val e = inject[CondScope, F, A](BCond(for {
      x <- els
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x))

    cond(cnd, t, e)
     */

    val t = for {
      _ <- inject[CondScope, F, Unit](BCond(Return()))
      x <- thn
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x
    val e = for {
      _ <- inject[CondScope, F, Unit](BCond(Return()))
      x <- els
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x
    cond(cnd, t, e)

    /*
    for {
      _ <- inject[CondScope, F, Unit](BCond(Return()))
      x <- inject(Cnd(cnd, thn, els))
      _ <- inject[CondScope, F, Unit](ECond(Return()))
    } yield x
     */
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
          System.out.println("runBCond select")
          for {
            tv <- t
            ev <- e
          } yield select(c, tv, ev)
        }
        for {
          u <- upcast[Cnd[E, *], F, Free[(CondScope ⊕ (Cnd[E, *] ⊕ F)#t)#t, A]](run(runECond(k)))
          v <- runBCond(u)
        } yield v
      case ECond$(k) =>
        System.out.println(k)
        throw new RuntimeException("Mismatched BCond")
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
          System.out.println("runECond select")
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
