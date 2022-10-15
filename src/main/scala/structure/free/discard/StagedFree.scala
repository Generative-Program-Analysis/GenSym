package gensym.structure.monad.free.discard
package attempt1

import lms.core._
import lms.core.stub._
import lms.macros._
import lms.core.Backend._

import gensym.lmsx._
import gensym.structure.functor._

@virtualize
trait RepFree { self: SAIOps =>

  // Functors hold static data
  trait SFunctor[F[_]] {
    def map[A: Manifest, B: Manifest](x: F[A])(f: A => B): F[B]
  }

  // Functors hold dynamic data
  trait DFunctor[F[_]] {
    def map[A: Manifest, B: Manifest](x: F[A])(f: Rep[A] => Rep[B]): F[B]
  }

  abstract class Free[F[_]: Functor, A: Manifest] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B]
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B]
  }

  // Return storing a dynamic data of type Rep[A]
  case class Return[F[_]: Functor, A: Manifest](a: Rep[A]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] = Return(f(a))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] = f(a)
  }

  // Return stroing a static data of type A
  /*
  case class SReturn[F[_]: Functor, A: Manifest](a: A) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      // Note: We expect the result of f(a) is also static
      Unwrap(f(unit(a))) match {
        case Backend.Const(x: B) => SReturn(x)
      }
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      f(unit(a))
  }
   */

  // Impure can only hold static data of type F[Free[F, A]]
  case class Impure[F[_]: Functor, A: Manifest](x: F[Free[F, A]]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }

  // Free is a DFunctor, which accepts transformations of Rep[A] => Rep[B].
  // However, the underlying structure F[_] (in Free[F, A]) is a Functor.

  implicit def FreeDFunctorInstance[F[_]: Functor]: DFunctor[Free[F, *]] =
    new DFunctor[Free[F, *]] {
      def map[A: Manifest, B: Manifest](x: Free[F, A])(f: Rep[A] => Rep[B]): Free[F, B] = x.map(f)
    }

  trait SMonad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
  }

  implicit def FreeMonadInstance[F[_]: Functor]: SMonad[Free[F, *]] =
    new SMonad[Free[F, *]] {
      def pure[A: Manifest](a: Rep[A]): Free[F, A] =
        Unwrap(a) match {
          //case Backend.Const(a: A) => SReturn[F, A](a)
          case _ => Return[F, A](a)
        }
      def flatMap[A: Manifest, B: Manifest](ma: Free[F, A])(f: Rep[A] => Free[F, B]): Free[F, B] =
        ma.flatMap(f)
    }

  // Coproduct

  abstract class Coprod[F[_]: Functor, G[_]: Functor, A]
  case class Left[F[_]: Functor, G[_]: Functor, A](fa: F[A]) extends Coprod[F, G, A]
  case class Right[F[_]: Functor, G[_]: Functor, A](ga: G[A]) extends Coprod[F, G, A]

  type ⊕[F[_], G[_]] = ({type t[A] = Coprod[F, G, A]})

  implicit def CoproductFunctor[F[_]: Functor, G[_]: Functor]: Functor[(F ⊕ G)#t] =
    new Functor[(F ⊕ G)#t] {
      def map[A, B](x: Coprod[F, G, A])(f: A => B): Coprod[F, G, B] = x match {
        case Left(x) => Left[F, G, B](Functor[F].map(x)(f))
        case Right(x) => Right[F, G, B](Functor[G].map(x)(f))
      }
    }

  // F ⊆ G

  sealed trait ⊆[F[_], G[_]] {
    def inj[A](sub: F[A]): G[A]
    def prj[A](sup: G[A]): Option[F[A]]
  }

  // Inject F into G
  def inject[F[_]: Functor, G[_]: Functor, R: Manifest](x: F[Free[G, R]])(implicit I: F ⊆ G): Free[G, R] =
    Impure(I.inj(x))

  // Project F out of G
  def project[F[_]: Functor, G[_]: Functor, R: Manifest](x: Free[G, R])(implicit I: F ⊆ G): Option[F[Free[G, R]]] =
    x match {
      case Impure(f) => I.prj(f)
      case _ => None
    }

  implicit def injRefl[F[_]: Functor] = new (F ⊆ F) {
    def inj[A](sub: F[A]) = sub
    def prj[A](sup: F[A]) = Some(sup)
  }

  implicit def injLeft[F[_]: Functor, G[_]: Functor] = new (F ⊆ (F ⊕ G)#t) {
    def inj[A](sub: F[A]) = Left(sub)
    def prj[A](sup: Coprod[F, G, A]) = sup match {
      case Left(fa) => Some(fa)
      case Right(_) => None
    }
  }

  implicit def injRight[F[_]: Functor, G[_]: Functor, H[_]: Functor](implicit I: F ⊆ G) =
    new (F ⊆ (H ⊕ G)#t) {
      def inj[A](sub: F[A]) = Right(I.inj(sub))
      def prj[A](sup: Coprod[H, G, A]) = sup match {
        case Left(_) => None
        case Right(x) => I.prj(x)
      }
    }

  // Deterministic Cond Effect
  // Why we need this?
  //   - `if` in LMS requires the branches to be Rep type
  //   - CPS improves staging/PE
  // Caveat: thn/els are both passed by-value!

  case class Cond[K](cnd: Rep[Boolean], thn: K, els: K)

  def cond[F[_]: Functor, A: Manifest](cnd: Rep[Boolean], thn: Free[F, A], els: Free[F, A])
    (implicit I1: Cond ⊆ F /*, I2: CondScope ⊆ F */): Free[F, A] = {
    /*
    for {
      _ <- inject[CondScope, F, Unit](CondStart(Return(())))
      x <- inject(Cond(cnd, thn, els))
      _ <- inject[CondScope, F, Unit](CondEnd(Return()))
    } yield x
     */
    inject[Cond, F, A](Cond(cnd, thn, els))
  }

  implicit def CondFunctor: Functor[Cond] =
    new Functor[Cond] {
      def map[A, B](x: Cond[A])(f: A => B): Cond[B] = x match {
        case Cond(c, t, e) =>
          // TODO: what about the join point?
          //System.out.println("t: " + t)
          //System.out.println("f(t): " + f(t))
          //Cond(c, f(t), f(e))
          //Cond(c, f(t), f(e))
          Cond(c, f.asInstanceOf[B], e.asInstanceOf[B])
      }
    }

  def runCond[F[_]: Functor, A: Manifest](prog: Free[(Cond ⊕ F)#t, A]): Free[F, A] = {
    prog match {
      case Return(x) => Return(x)
      case _ =>
        System.out.println("runCond: " + prog)
        val p = project[Cond, (Cond ⊕ F)#t, A](prog)
        System.out.println("project: " + p)
        p match {
          //case Some(Cond(c, t: Free[(Cond ⊕ F)#t, A], e: Free[(Cond ⊕ F)#t, A])) =>
          case Some(Cond(c, t, e)) =>
            System.out.println(e)
            ???
          case Some(Cond(c, t, e)) =>
            runCond(for {
              //_ <- Return[F, A](unchecked("//then"))
              tv <- t //runCond(upcast(t))
              //_ <- Return[F, A](unchecked("//else"))
              ev <- e //runCond(upcast(e))
              //_ <- Return[F, A](unchecked("//end"))
            } yield (if (c) tv else ev))
          case _ =>
            System.out.println(prog)
            prog match {
              case Impure(Right(op)) =>
                //val Impure(Right(op)) = prog
                Impure(Functor[F].map(op)(a => runCond[F, A](a)))
            }
        }
    }
  }

  def upcast[F[_]: Functor, G[_]: Functor, A: Manifest](prog: Free[G, A]): Free[(F ⊕ G)#t, A] =
    prog match {
      case Return(x) => Return(x)
      case Impure(op) =>
        Impure(Right(Functor[G].map(op)(a => upcast(a))))
    }

  /*
  abstract class CondScope[K]
  case class CondStart[K](k: K) extends CondScope[K]
  case class CondEnd[K](k: K) extends CondScope[K]

  implicit def CondScopeFunctor: Functor[CondScope] =
    new Functor[CondScope] {
      def map[A, B](x: CondScope[A])(f: A => B): CondScope[B] = x match {
        case CondStart(k) => CondStart(f(k))
        case CondEnd(k) => CondEnd(f(k))
      }
    }
   */

  /*
  case class KCond[A](cnd: Rep[Boolean], thn: Any, els: Any, k: Any => A) {
    override def toString: String =
      "KCond(" + cnd + "," + thn + "," + els + ", λ)"
  }

  def kcond[F[_]: Functor, A: Manifest](cnd: Rep[Boolean], thn: Free[F, A], els: Free[F, A])
    (implicit I: KCond ⊆ F): Free[F, A] = {
    inject[KCond, F, A](KCond(cnd, thn, els, a => a.asInstanceOf[Free[F, A]]))
  }

  implicit def KCondFunctor: Functor[KCond] =
    new Functor[KCond] {
      def map[A, B](x: KCond[A])(f: A => B): KCond[B] = x match {
        case KCond(c, t, e, k) =>
          KCond(c, t, e, (r: Any) => f(k(r)))
      }
    }

  def runKCond[F[_]: Functor, A: Manifest](prog: Free[(KCond ⊕ F)#t, A]): Free[F, A] = {
    prog match {
      case Return(x) => Return(x)
      case _ =>
        val I: (KCond ⊆ (KCond ⊕ F)#t) = implicitly[(KCond ⊆ (KCond ⊕ F)#t)]
        val p = project[KCond, (KCond ⊕ F)#t, A](prog)
        System.out.println("prog: " + prog)
        System.out.println("project(prog): " + p)
        prog match {
          case Impure(f) =>
            System.out.println("Yes, impure")
            System.out.println(I.prj(f))
          case _ =>
        }
        p match {
          //case Some(KCond(c, t: Free[F, A], e: Free[F, A], k)) =>
          case Some(KCond(c, t: Free[(KCond ⊕ F)#t, A], e: Free[(KCond ⊕ F)#t, A], k)) =>
            val res = for {
              v1 <- runKCond(t)
              v2 <- runKCond(e)
            } yield {if (c) v1 else v2}
            runKCond(k(res))
          case _ =>
            System.out.println("else: " + prog)
            val Impure(Right(op)) = prog
            Impure(Functor[F].map(op)(a => runKCond(a)))
        }
    }
  }
   */

  // Nondet Effect

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

  def fail[F[_]: Functor, A: Manifest](implicit I: Nondet ⊆ F): Free[F, A] =
    inject[Nondet, F, A](Fail)

  def choice[F[_]: Functor, A: Manifest](f: Free[F, A], g: Free[F, A])
    (implicit I: Nondet ⊆ F): Free[F, A] = inject[Nondet, F, A](Choice(f, g))

  def runNondet[F[_]: Functor, A: Manifest](prog: Free[(Nondet ⊕ F)#t, A]): Free[F, List[A]] =
    prog match {
      //case SReturn(x) => SReturn(scala.collection.immutable.List(x))
      case Return(x) => Return(List(x))
      case _ =>
        project[Nondet, (Nondet ⊕ F)#t, A](prog) match {
          //case Some(Fail) => SReturn(scala.collection.immutable.List())
          case Some(Fail) => Return(List())
          case Some(Choice(p, q)) =>
            for {
              ps <- runNondet(p)
              qs <- runNondet(q)
            } yield ps ++ qs
          case _ =>
            val Impure(Right(op)) = prog
            Impure(Functor[F].map(op)(a => runNondet[F, A](a)))
        }
    }

  // State Effect

  abstract class State[S: Manifest, A]
  case class Get[S: Manifest, A](k: Rep[S] => A) extends State[S, A] {
    override def toString: String = "Get(λ)"
  }
  case class Put[S: Manifest, A](s: Rep[S], k: A) extends State[S, A]

  // State is a _static_ functor
  implicit def StateFunctor[S: Manifest]: Functor[State[S, *]] =
    new Functor[State[S, *]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: Functor, S: Manifest](implicit I: (State[S, *] ⊆ F)): Free[F, S] =
    inject[State[S, *], F, S](Get((s: Rep[S]) => Return(s)))

  def put[F[_]: Functor, S: Manifest](s: Rep[S])(implicit I: State[S, *] ⊆ F): Free[F, Unit] =
    inject[State[S, *], F, Unit](Put(s, Return(())))

  def runStateRef[F[_]: Functor, S: Manifest, A: Manifest]
    (s: Rep[S])(prog: Free[(State[S, *] ⊕ F)#t, A]): Free[F, A] = {
    //var state: Var[S] = s
    val state: Var[S] = var_new(s)
    def handler(prog: Free[(State[S, *] ⊕ F)#t, A]): Free[F, A] =
      prog match {
        //case SReturn(a) => SReturn(a)
        case Return(a) => Return(a)
        case _ =>
          project[State[S, *], (State[S, *] ⊕ F)#t, A](prog) match {
            case Some(Get(k)) => handler(k(readVar(state)))
            //case Some(Get(k)) => handler(k(state))
            case Some(Put(s1, k)) =>
              //state = s
              __assign(state, s)
              handler(k)
            case _ =>
              val Impure(Right(op)) = prog
              Impure(Functor[F].map(op)(handler))
          }
      }
    handler(prog)
  }

  def runState[F[_]: Functor, S: Manifest, A: Manifest](s: Rep[S], prog: Free[(State[S, *] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      // TODO: what's the right patially-static data pattern to describe
      //       structure/shape is static, but data is dynamic?
      //case SReturn(a) => Return((s, a)) // FIXME `a` and the structure is static, but lifted into Return
      case Return(a) => Return((s, a))
      //case GetPattern(k) => run(s, k(s))
      //case PutPattern(s1, k) => run(s1, k)
      case _ =>
        project[State[S, *], (State[S, *] ⊕ F)#t, A](prog) match {
          case Some(Get(k)) => runState(s, k(s))
          case Some(Put(s1, k)) => runState(s1, k)
          case _ =>
            val Impure(Right(op)) = prog //TODO: have deep_handle and shallow_handle combinators that hide the default case
            Impure(Functor[F].map(op)(runState(s, _)))
        }
    }

  trait ∅[+K]
  implicit val VoidFunctor: Functor[∅] =
    new Functor[∅] {
      def map[A, B](x: ∅[A])(f: A => B): ∅[B] = x.asInstanceOf[∅[B]]
    }

  def runVoid[A: Manifest](f: Free[∅, A]): Rep[A] = f match {
    //case SReturn(x) => unit(x)
    case Return(x) => x
  }

  // Top-level runner

  def runVoidState[S: Manifest, A: Manifest](prog: Free[(State[S, *] ⊕ ∅)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, prog))

  def runVoidCondState[S: Manifest, A: Manifest]
    (prog: Free[(State[S, *] ⊕ (Cond ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runCond(runState(s, prog)))

  /*
  def runVoidKCondState[S: Manifest, A: Manifest]
    (prog: Free[(State[S, *] ⊕ (KCond ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runKCond(runState(s, prog)))
   */

  def runVoidCondStateRef[S: Manifest, A: Manifest]
    (prog: Free[(State[S, *] ⊕ (Cond ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[A] = {
    val sh = runStateRef[(Cond ⊕ ∅)#t, S, A](s) _
    runVoid(runCond(sh(prog)))
  }

  def runVoidStateCond[S: Manifest, A: Manifest]
    (prog: Free[(Cond ⊕ (State[S, *] ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, runCond(prog)))

  /*
  def runVoidStateKCond[S: Manifest, A: Manifest]
    (prog: Free[(KCond ⊕ (State[S, *] ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, runKCond(prog)))
   */

  def runVoidNondet[A: Manifest](prog: Free[(Nondet ⊕ ∅)#t, A]): Rep[List[A]] =
    runVoid(runNondet(prog))

  def runVoidCondNondet[A: Manifest](prog: Free[(Nondet ⊕ (Cond ⊕ ∅)#t)#t, A]): Rep[List[A]] =
    runVoid(runCond(runNondet(prog)))

  def runLocal[F[_]: Functor, S: Manifest, A: Manifest]
    (s: Rep[S], prog: Free[(State[S, *] ⊕ (Nondet ⊕ F)#t)#t, A]): Free[F, List[(S, A)]] =
    runNondet(runState(s, prog))

  def runGlobal[F[_]: Functor, S: Manifest, A: Manifest]
    (s: Rep[S], prog: Free[(Nondet ⊕ (State[S, *] ⊕ F)#t)#t, A]): Free[F, (S, List[A])] =
    runState(s, runNondet(prog))

  def exprog1[F[_]: Functor]
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x + 2))
      ys <- get[F, List[Int]]
    } yield ys(0)

  def exprog2[F[_]: Functor](y: Rep[Int])
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x * y))
      ys <- get[F, List[Int]]
    } yield ys(1)

  def exprog3[F[_]: Functor](x: Rep[Int])
    //(implicit I1: State[List[Int], *] ⊆ F, I2: KCond ⊆ F): Free[F, Int] =
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F/*, I3: CondScope ⊆ F*/): Free[F, Int] =
    for {
      //s <- kcond(x == 0, exprog1[F], exprog2[F](x + 100))
      s <- cond(x == 0, exprog1[F], exprog2[F](x + 100))
      y <- exprog2[F](x) //Return[F, Int](3)
    } yield s + y

  def exprog4[F[_]: Functor](x: Rep[Int])
    //(implicit I1: State[List[Int], *] ⊆ F, I2: KCond ⊆ F): Free[F, Int] =
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F/*, I3: CondScope ⊆ F*/): Free[F, Int] =
    for {
      //s <- kcond(x == 12, exprog3[F](x), exprog1[F])
      s <- cond(x == 12, exprog3[F](x), exprog1[F])
      x <- Return[F, Int](5)
    } yield s + x

  ///////////////////////////////////////////

  // Knapsack

  // Note: If xs is dynamic, then we cannot statically create
  //       Return (Return) for each element in the list.
  /*
  def select[F[_]: Functor, A: Manifest](xs: Rep[List[A]])
    (implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map((x: Rep[A]) => Return[F, A]).foldRight[Free[F, A]](fail)(choice)
   */
  def select3[F[_]: Functor, A: Manifest](xs: Rep[List[A]])
    (implicit I: Nondet ⊆ F): Free[F, A] = {
    val x0 = Return[F, A](xs(0))
    val x1 = Return[F, A](xs(0))
    val x2 = Return[F, A](xs(0))
    choice(x0, choice(x1, choice(x2, fail)))
  }

  /*
  def knapsack[F[_]: Functor](w: Rep[Int], vs: Rep[List[Int]])
    (implicit I1: Nondet ⊆ F, I2: Cond ⊆ F): Free[F, List[Int]] = {
    /* Observed that:
     * 1. The conditions w < 0 and w == 0 are dynamic, but they
     *    have Free[F, A] branches.
     * 2. Similarly, if the size of `vs` is dynamic, it has to
     *    generate conditional control-flow for `select`, which
     *    maps xs with Return and folds with choice, then the condition
     *    is dynamic however the two branches are Free[F, A] objects, which
     *    are not dynamic. But we know the Free[F, A] values will be
     *    consumed/interpreted at the current stage.
     */

    /*
    if (w < 0)
      fail
    else if (w == 0)
      ret(List())
    else for {
      v <- select(vs)
      xs <- knapsack(w-v, vs)
    } yield v :: xs
     */

    val c = for {
      v <- select3[F, Int](vs)
      xs <- knapsack[F](w - v, vs)
    } yield v :: xs

    cond(w < 0,
      fail,
      cond(w == 0,
        Return(List()),
        c))
  }
   */

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

trait StagedFreeDriver[A, B] extends SAIDriver[A, B] with RepFree { q =>
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
      implicit val F1 = Functor[(Cond ⊕ ∅)#t]
      //implicit val F3 = Functor[(KCond ⊕ ∅)#t]
      implicit val F2 = Functor[(State[List[Int], *] ⊕ ∅)#t]

      def snippet(u: Rep[Int]) = {
        val xs: Rep[List[Int]] = List(u, u+1)

        // Note: Different order of interpretation produce different code.
        // This one handles state first, and then cond, and generates code
        //   with respect to the scope of then/else branch.
        // But this one causes code duplicateion!
        val res: Rep[(List[Int], Int)] = runVoidCondState(exprog4[(State[List[Int], *] ⊕ (Cond ⊕ ∅)#t)#t](u), xs)
        //val res: Rep[(List[Int], Int)] = runVoidKCondState(exprog4[(State[List[Int], *] ⊕ (KCond ⊕ ∅)#t)#t](u), xs)

        // Using mutable state handler (Not work yet)
        //val res: Rep[Int] = runVoidCondStateRef(exprog3[(State[List[Int], *] ⊕ (Cond ⊕ ∅)#t)#t](u), xs)

        // This one handles cond first, and then state, and generates code
        //   that compute both branches first, then use the results (variables)
        //   in the generated `if`.
        //val p = exprog4[(Cond ⊕ (State[List[Int], *] ⊕ ∅)#t)#t](u)
        //val res: Rep[(List[Int], Int)] = runVoidStateCond(p, xs)

        println(res)
        res._2
      }
    }


  @virtualize
  def specializeKnapsack(e: Int): SAIDriver[Int, Int] =
    new StagedFreeDriver[Int, Int] {
      implicit val F1 = Functor[(Cond ⊕ ∅)#t]
      implicit val F2 = Functor[(State[List[Int], *] ⊕ ∅)#t]

      def snippet(u: Rep[Int]) = {
        ???
      }
    }
}

/* RD[_]: Representation of Data
 * RT[_]: Representation of Transformation
 * RF[_]: Representation of Functor
 */
trait SPFunctor[RD[_], RT[_], RF[_], F[_]] {
  def map[A: Manifest, B: Manifest](x: RF[F[A]])(f: RT[RD[A] => RD[B]]): RF[F[B]]

  /* RD = Rep, RT = NR, RF = NR
   * A functor holds representations of data
   * map : F[A] → (Rep[A] → Rep[B]) → F[B]
   */

  /* RD = Rep, RT = Rep, RF = NR
   * A function holds representations, and the transformation is known at next stage
   * Does it really make sense?
   *  map : F[A] → (Rep[Rep[A] → Rep[B]]) → F[B]
   */

  /* RD = NR, RT = Rep, RF = NR
   * A function holds representations, and the transformation is known at next stage
   * map(x: F[A])(f: Rep[A => B]): F[B]
   */
}

