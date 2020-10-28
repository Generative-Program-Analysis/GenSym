package sai.structure.monad.free.discard
package attempt2

import lms.core._
import lms.core.stub._
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

// SFunctors hold static data
trait SFunctor[F[_]] {
  def map[A, B](x: F[A])(f: A => B): F[B]
}

object SFunctor {
  def apply[F[_]](implicit f: SFunctor[F]): SFunctor[F] = f
}

trait RepFunctor { self: SAIOps =>
  // SFunctors hold dynamic data
  trait DFunctor[F[_]] {
    def map[A: Manifest, B: Manifest](x: F[A])(f: Rep[A] => Rep[B]): F[B]
  }

  abstract class Free[F[_]: SFunctor, A: Manifest] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B]
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B]
  }
}

trait RepMonad { self: SAIOps =>
  trait SMonad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
  }
}

trait Coproduct {
  abstract class Coprod[F[_]: SFunctor, G[_]: SFunctor, A]
  case class Left[F[_]: SFunctor, G[_]: SFunctor, A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_]: SFunctor, G[_]: SFunctor, A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})

  implicit def CoproductSFunctor[F[_]: SFunctor, G[_]: SFunctor]: SFunctor[(F ⊕ G)#t] =
    new SFunctor[(F ⊕ G)#t] {
      def map[A, B](x: Coprod[F, G, A])(f: A => B): Coprod[F, G, B] = x match {
        case Left(x) => Left[F, G, B](SFunctor[F].map(x)(f))
        case Right(x) => Right[F, G, B](SFunctor[G].map(x)(f))
      }
    }

  // F ⊆ G

  sealed trait ⊆[F[_], G[_]] {
    def inj[A](sub: F[A]): G[A]
    def prj[A](sup: G[A]): Option[F[A]]
  }

  implicit def injRefl[F[_]: SFunctor] = new (F ⊆ F) {
    def inj[A](sub: F[A]) = sub
    def prj[A](sup: F[A]) = Some(sup)
  }

  implicit def injLeft[F[_]: SFunctor, G[_]: SFunctor] = new (F ⊆ (F ⊕ G)#t) {
    def inj[A](sub: F[A]) = Left(sub)
    def prj[A](sup: Coprod[F, G, A]) = sup match {
      case Left(fa) => Some(fa)
      case Right(_) => None
    }
  }

  implicit def injRight[F[_]: SFunctor, G[_]: SFunctor, H[_]: SFunctor](implicit I: F ⊆ G) =
    new (F ⊆ (H ⊕ G)#t) {
      def inj[A](sub: F[A]) = Right(I.inj(sub))
      def prj[A](sup: Coprod[H, G, A]) = sup match {
        case Left(_) => None
        case Right(x) => I.prj(x)
      }
    }
}

@virtualize
trait RepFree { self: SAIOps with RepFunctor with RepMonad with Coproduct =>

  // Return storing a dynamic data of type Rep[A]
  case class Return[F[_]: SFunctor, A: Manifest](a: Rep[A]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] = Return(f(a))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] = f(a)
  }

  // Return stroing a static data of type A
  case class SReturn[F[_]: SFunctor, A: Manifest](a: A) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      // Note: We expect the result of f(a) is also static
      Unwrap(f(unit(a))) match {
        case Backend.Const(x: B) => SReturn(x)
        case _ => ???
      }
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      f(unit(a))
  }

  // Impure can only hold static data of type F[Free[F, A]]
  case class Impure[F[_]: SFunctor, A: Manifest](x: F[Free[F, A]]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      Impure(SFunctor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      Impure(SFunctor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }

  // Free is a DFunctor, which accepts transformations of Rep[A] => Rep[B].
  // However, the underlying structure F[_] (in Free[F, A]) is a SFunctor.

  implicit def FreeDFunctorInstance[F[_]: SFunctor]: DFunctor[Free[F, ?]] =
    new DFunctor[Free[F, ?]] {
      def map[A: Manifest, B: Manifest](x: Free[F, A])(f: Rep[A] => Rep[B]): Free[F, B] = x.map(f)
    }

  implicit def FreeMonadInstance[F[_]: SFunctor]: SMonad[Free[F, ?]] =
    new SMonad[Free[F, ?]] {
      def pure[A: Manifest](a: Rep[A]): Free[F, A] =
        Unwrap(a) match {
          case Backend.Const(a: A) => SReturn[F, A](a)
          case _ => Return[F, A](a)
        }
      def flatMap[A: Manifest, B: Manifest](ma: Free[F, A])(f: Rep[A] => Free[F, B]): Free[F, B] =
        ma.flatMap(f)
    }

  // Inject F into G
  def inject[F[_]: SFunctor, G[_]: SFunctor, R: Manifest](x: F[Free[G, R]])(implicit I: F ⊆ G): Free[G, R] =
    Impure(I.inj(x))

  // Project F out of G
  def project[F[_]: SFunctor, G[_]: SFunctor, R: Manifest](x: Free[G, R])(implicit I: F ⊆ G): Option[F[Free[G, R]]] =
    x match {
      case Impure(f) => I.prj(f)
      case _ => None
    }
}

trait RepVoidEff { self: SAIOps with RepFree with RepFunctor =>
  trait ∅[+K]
  implicit val VoidFunctor: SFunctor[∅] =
    new SFunctor[∅] {
      def map[A, B](x: ∅[A])(f: A => B): ∅[B] = x.asInstanceOf[∅[B]]
    }

  def runVoid[A: Manifest](f: Free[∅, A]): Rep[A] = f match {
    case SReturn(x) => unit(x)
    case Return(x) => x
  }
}

@virtualize
trait RepCondEff { self: SAIOps with RepFree with RepFunctor with RepMonad with Coproduct =>
  case class Cond[K](cnd: Rep[Boolean], thn: K, els: K)

  implicit def CondSFunctor: SFunctor[Cond] =
    new SFunctor[Cond] {
      def map[A, B](x: Cond[A])(f: A => B): Cond[B] = x match {
        case Cond(c, t, e) => Cond(c, f(t), f(e))
      }
    }

  abstract class CondScope[K]
  case class CondStart[K](k: K) extends CondScope[K]
  case class CondEnd[K](k: K) extends CondScope[K]

  implicit def CondScopeSFunctor: SFunctor[CondScope] =
    new SFunctor[CondScope] {
      def map[A, B](x: CondScope[A])(f: A => B): CondScope[B] = x match {
        case CondStart(k) => CondStart(f(k))
        case CondEnd(k) => CondEnd(f(k))
      }
    }

  def cond[F[_]: SFunctor, A: Manifest]
    (cnd: Rep[Boolean], thn: Free[F, A], els: Free[F, A])
    (implicit I: Cond ⊆ F): Free[F, A] = {
    inject[Cond, F, A](Cond(cnd, thn, els))
  }

  def runCond[F[_]: SFunctor, A: Manifest](prog: Free[(Cond ⊕ F)#t, A]): Free[F, A] = {
    prog match {
      case Return(x) => Return(x)
      case SReturn(x) => SReturn(x)
      case _ =>
        System.out.println("runCond: " + prog)
        val p = project[Cond, (Cond ⊕ F)#t, A](prog)
        System.out.println("project: " + p)
        p match {
          case Some(Cond(c, t, e)) =>
            for {
              tv <- runCond(t)
              ev <- runCond(e)
            } yield (if (c) tv else ev)
          case _ =>
            val Impure(Right(op)) = prog
            Impure(SFunctor[F].map(op)(a => runCond[F, A](a)))
        }
    }
  }

  def cond_s[F[_]: SFunctor, A: Manifest]
    (cnd: Rep[Boolean], thn: Free[F, A], els: Free[F, A])
    (implicit I1: Cond ⊆ F, I2: CondScope ⊆ F): Free[F, A] = {
    for {
      _ <- inject[CondScope, F, Unit](CondStart(Return()))
      x <- inject[Cond, F, A](Cond(cnd, thn, els))
      _ <- inject[CondScope, F, Unit](CondEnd(Return()))
    } yield x
  }

  def upcast[F[_]: SFunctor, G[_]: SFunctor, A: Manifest](prog: Free[G, A]): Free[(F ⊕ G)#t, A] =
    prog match {
      case Return(x) => Return(x)
      case SReturn(x) => SReturn(x)
      case Impure(op) =>
        Impure(Right(SFunctor[G].map(op)(a => upcast(a))))
    }

  // TODO: How to produce this
  implicit def mEff[F[_], A]: Manifest[Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]] =
    manifest[Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]]

  def runCondStart[F[_]: SFunctor, A: Manifest]
    (prog: Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]): Free[(Cond ⊕ F)#t, A] = {
    prog match {
      case Return(x) => Return(x)
      case SReturn(x) => SReturn(x)
      case _ =>
        // TODO: improve type implicit resolution here
        implicit val F1 = SFunctor[(Cond ⊕ F)#t]
        implicit val I: (CondScope ⊆ (CondScope ⊕ (Cond ⊕ F)#t)#t)  =
          injLeft[CondScope, (Cond ⊕ F)#t](SFunctor[CondScope], F1)

        project[CondScope, (CondScope ⊕ (Cond ⊕ F)#t)#t, A](prog) match {
          case Some(CondStart(k)) =>
            upcast[Cond, F, Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]](runCond(runCondEnd(k))).flatMap { u =>
                ??? //runCondStart(u)
            }
          case Some(CondEnd(k)) => ???
          case _ =>
            val Impure(Right(op)) = prog
            Impure[(Cond ⊕ F)#t, A](F1.map(op)(a => runCondStart(a)))(F1, manifest[A])
        }
    }
  }

  def runCondEnd[F[_]: SFunctor, A: Manifest]
    (prog: Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]):
      Free[(Cond ⊕ F)#t, Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]] = {
    ???
  }

  def runScopedCond[F[_]: SFunctor, A: Manifest]
    (prog: Free[(CondScope ⊕ (Cond ⊕ F)#t)#t, A]): Free[F, A] = {
    runCond(runCondStart(prog))
  }

}

@virtualize
trait RepStateEff { self: SAIOps with RepFree with RepFunctor with RepMonad with Coproduct =>
  abstract class State[S: Manifest, A]
  case class Get[S: Manifest, A](k: Rep[S] => A) extends State[S, A] {
    override def toString: String = "Get(λ)"
  }
  case class Put[S: Manifest, A](s: Rep[S], k: A) extends State[S, A]

  implicit def StateSFunctor[S: Manifest]: SFunctor[State[S, ?]] =
    new SFunctor[State[S, ?]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: SFunctor, S: Manifest](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
    inject[State[S, ?], F, S](Get((s: Rep[S]) => Return(s)))

  def put[F[_]: SFunctor, S: Manifest](s: Rep[S])(implicit I: State[S, ?] ⊆ F): Free[F, Unit] =
    inject[State[S, ?], F, Unit](Put(s, Return(())))

  def runState[F[_]: SFunctor, S: Manifest, A: Manifest](s: Rep[S], prog: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      case SReturn(a) => Return((s, a)) // FIXME `a` and the structure is static, but lifted into Return
      case Return(a) => Return((s, a))
      case _ =>
        project[State[S, ?], (State[S, ?] ⊕ F)#t, A](prog) match {
          case Some(Get(k)) => runState(s, k(s))
          case Some(Put(s1, k)) => runState(s1, k)
          case _ =>
            val Impure(Right(op)) = prog
            Impure(SFunctor[F].map(op)(runState(s, _)))
        }
    }
}

trait RepFreeTest extends SAIOps with RepFree
    with RepFunctor with RepMonad with Coproduct
    with RepStateEff with RepVoidEff {

  def runVoidState[S: Manifest, A: Manifest]
    (prog: Free[(State[S, *] ⊕ ∅)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, prog))

  def exprog1[F[_]: SFunctor]
    (implicit I1: State[List[Int], *] ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x + 2))
      ys <- get[F, List[Int]]
    } yield ys(0)

  def exprog2[F[_]: SFunctor](y: Rep[Int])
    (implicit I1: State[List[Int], *] ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x * y))
      ys <- get[F, List[Int]]
    } yield ys(1)
}


trait StagedFreeGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    //case n @ Node(s, op, List(x), _) if op.startsWith("unchecked") => ???
    case _ => super.shallow(n)
  }
  override def mayInline(n: Node): Boolean = n match {
    //case Node(_, "+", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(s, op, List(), _) if op.startsWith("unchecked") =>
      emit(op.substring("unchecked".size, op.size))
    case n @ Node(s, op, List(x), _) if op.startsWith("unchecked") =>
      shallow(x)
    case n @ Node(s,"var_set",List(x,y),_) =>
      System.out.println("A VAR SET")
      emit(s"${quote(x)} = "); shallow(y); emitln()
    case _ => super.traverse(n)
  }
}

trait StagedFreeDriver[A, B] extends SAIDriver[A, B] with RepFreeTest { q =>
  override val codegen = new ScalaGenBase with StagedFreeGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      super.remap(m)
    }
  }
}


object RepFree {
  @virtualize
  def specialize(): SAIDriver[Int, Int] =
    new StagedFreeDriver[Int, Int] {
      def snippet(u: Rep[Int]) = {
        type Eff = State[List[Int], *] ⊕ ∅
        val xs: Rep[List[Int]] = List(u, u+1)
        val res: Rep[(List[Int], Int)] = runVoidState(exprog1[Eff#t], xs)
        println(res)
        res._2
      }
    }

  /*
  def main(args: Array[String]): Unit = {
    val code = specialize()
    println(code.code)
    code.eval(0)
  }
  */
}
