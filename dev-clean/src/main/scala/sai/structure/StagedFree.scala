package sai.structure.monad
package free

import lms.core._
import lms.core.stub._
import lms.macros._

import lms.core.Backend._

import sai.lmsx._
import sai.structure.functor._

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
  case class DReturn[F[_]: Functor, A: Manifest](a: Rep[A]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] = DReturn(f(a))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] = f(a)
  }

  // Return stroing a static data of type A
  case class SReturn[F[_]: Functor, A: Manifest](a: A) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      // Note: We expect the result of f(a) is also static
      Unwrap(f(unit(a))) match {
        case Backend.Const(x: B) => SReturn(x)
      }
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      f(unit(a))
  }

  // Impure can only hold static data of type F[Free[F, A]]
  case class Impure[F[_]: Functor, A: Manifest](x: F[Free[F, A]]) extends Free[F, A] {
    def map[B: Manifest](f: Rep[A] => Rep[B]): Free[F, B] =
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.map(f)))
    def flatMap[B: Manifest](f: Rep[A] => Free[F, B]): Free[F, B] =
      Impure(Functor[F].map(x)((xf: Free[F, A]) => xf.flatMap(f)))
  }

  // Free is a DFunctor, which accepts transformations of Rep[A] => Rep[B].
  // However, the underlying structure F[_] (in Free[F, A]) is a Functor.

  implicit def FreeDFunctorInstance[F[_]: Functor]: DFunctor[Free[F, ?]] =
    new DFunctor[Free[F, ?]] {
      def map[A: Manifest, B: Manifest](x: Free[F, A])(f: Rep[A] => Rep[B]): Free[F, B] = x.map(f)
    }

  trait SMonad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
  }

  implicit def FreeMonadInstance[F[_]: Functor]: SMonad[Free[F, ?]] =
    new SMonad[Free[F, ?]] {
      def pure[A: Manifest](a: Rep[A]): Free[F, A] =
        Unwrap(a) match {
          case Backend.Const(a: A) => SReturn[F, A](a)
          case _ => DReturn[F, A](a)
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

  def inject[F[_]: Functor, G[_]: Functor, R: Manifest](x: F[Free[G, R]])(implicit I: F ⊆ G): Free[G, R] =
    Impure(I.inj(x))

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
    (implicit I: Cond ⊆ F): Free[F, A] = inject[Cond, F, A](Cond(cnd, thn, els))

  implicit def CondFunctor: Functor[Cond] =
    new Functor[Cond] {
      def map[A, B](x: Cond[A])(f: A => B): Cond[B] = x match {
        case Cond(c, t, e) => Cond(c, f(t), f(e)) // TODO: what about the join point?
      }
    }

  def runCond[F[_]: Functor, A: Manifest](prog: Free[(Cond ⊕ F)#t, A]): Free[F, A] =
    prog match {
      case SReturn(x) => SReturn(x)
      case DReturn(x) => DReturn(x)
      case _ =>
        project[Cond, (Cond ⊕ F)#t, A](prog) match {
          case Some(Cond(c, t, e)) =>
            for {
              tv <- runCond(t)
              ev <- runCond(e)
            } yield (if (c) tv else ev)
          case _ =>
            val Impure(Right(op)) = prog
            Impure(Functor[F].map(op)(a => runCond[F, A](a)))
        }
    }

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
      case SReturn(x) => SReturn(scala.collection.immutable.List(x))
      case DReturn(x) => DReturn(List(x))
      case _ =>
        project[Nondet, (Nondet ⊕ F)#t, A](prog) match {
          case Some(Fail) => SReturn(scala.collection.immutable.List())
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
  case class Get[S: Manifest, A](k: Rep[S] => A) extends State[S, A]
  case class Put[S: Manifest, A](s: Rep[S], k: A) extends State[S, A]

  // State is a _static_ functor
  implicit def StateFunctor[S: Manifest]: Functor[State[S, ?]] =
    new Functor[State[S, ?]] {
      def map[A, B](x: State[S, A])(f: A => B): State[S, B] = x match {
        case Get(k) => Get(s => f(k(s)))
        case Put(s, a) => Put(s, f(a))
      }
    }

  def get[F[_]: Functor, S: Manifest](implicit I: (State[S, ?] ⊆ F)): Free[F, S] =
    inject[State[S, ?], F, S](Get((s: Rep[S]) => DReturn(s)))

  def put[F[_]: Functor, S: Manifest](s: Rep[S])(implicit I: State[S, ?] ⊆ F): Free[F, Unit] =
    inject[State[S, ?], F, Unit](Put(s, SReturn(())))

  def runState[F[_]: Functor, S: Manifest, A: Manifest](s: Rep[S], prog: Free[(State[S, ?] ⊕ F)#t, A]): Free[F, (S, A)] =
    prog match {
      // TODO: what's the right patially-static data pattern to describe
      //       structure/shape is static, but data is dynamic?
      case SReturn(a) => DReturn((s, a)) // FIXME `a` and the structure is static, but lifted into DReturn
      case DReturn(a) => DReturn((s, a))
      //case GetPattern(k) => run(s, k(s))
      //case PutPattern(s1, k) => run(s1, k)
      case _ =>
        project[State[S, ?], (State[S, ?] ⊕ F)#t, A](prog) match {
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
    case SReturn(x) => unit(x)
    case DReturn(x) => x
  }

  // Top-level runner

  def runVoidState[S: Manifest, A: Manifest](prog: Free[(State[S, *] ⊕ ∅)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, prog))

  def runVoidCondState[S: Manifest, A: Manifest]
    (prog: Free[(State[S, *] ⊕ (Cond ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runCond(runState(s, prog)))

  def runVoidStateCond[S: Manifest, A: Manifest]
    (prog: Free[(Cond ⊕ (State[S, *] ⊕ ∅)#t)#t, A], s: Rep[S]): Rep[(S, A)] =
    runVoid(runState(s, runCond(prog)))

  def runVoidNondet[A: Manifest](prog: Free[(Nondet ⊕ ∅)#t, A]): Rep[List[A]] =
    runVoid(runNondet(prog))

  def runVoidCondNondet[A: Manifest](prog: Free[(Nondet ⊕ (Cond ⊕ ∅)#t)#t, A]): Rep[List[A]] =
    runVoid(runCond(runNondet(prog)))

  def runLocal[F[_]: Functor, S: Manifest, A: Manifest]
    (s: Rep[S], prog: Free[(State[S, ?] ⊕ (Nondet ⊕ F)#t)#t, A]): Free[F, List[(S, A)]] =
    runNondet(runState(s, prog))

  def runGlobal[F[_]: Functor, S: Manifest, A: Manifest]
    (s: Rep[S], prog: Free[(Nondet ⊕ (State[S, ?] ⊕ F)#t)#t, A]): Free[F, (S, List[A])] =
    runState(s, runNondet(prog))

  def exprog1[F[_]: Functor]
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x + 1))
      ys <- get[F, List[Int]]
    } yield ys(0)

  def exprog2[F[_]: Functor]
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F): Free[F, Int] =
    for {
      xs <- get[F, List[Int]]
      _  <- put[F, List[Int]](xs.map(x => x * x))
      ys <- get[F, List[Int]]
    } yield ys(0)

  def exprog3[F[_]: Functor](x: Rep[Int])
    (implicit I1: State[List[Int], *] ⊆ F, I2: Cond ⊆ F): Free[F, Int] =
    for {
      s <- cond(x == 0, exprog1[F], exprog2[F])
      y <- exprog1[F]
    } yield s + y

  // Knapsack

  // Note: If xs is dynamic, then we cannot statically create
  //       Return (DReturn) for each element in the list.
  /*
  def select[F[_]: Functor, A: Manifest](xs: Rep[List[A]])
    (implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map((x: Rep[A]) => Return[F, A]).foldRight[Free[F, A]](fail)(choice)
   */
  def select3[F[_]: Functor, A: Manifest](xs: Rep[List[A]])
    (implicit I: Nondet ⊆ F): Free[F, A] = {
    val x0 = DReturn[F, A](xs(0))
    val x1 = DReturn[F, A](xs(0))
    val x2 = DReturn[F, A](xs(0))
    choice(x0, choice(x1, choice(x2, fail)))
  }

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
        DReturn(List()),
        c))
  }

}

trait StagedFreeGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    case _ => super.shallow(n)
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
  def specialize(e: Int): SAIDriver[Int, Int] =
    new StagedFreeDriver[Int, Int] {
      implicit val F1 = Functor[(Cond ⊕ ∅)#t]
      implicit val F2 = Functor[(State[List[Int], *] ⊕ ∅)#t]

      def snippet(u: Rep[Int]) = {
        val xs: Rep[List[Int]] = List(u)
        // Note: Different order of interpretation produce different code.
        // This one handles state first, and then cond, and generates code
        //   with respect to the scope of then/else branch.
        // But this one causes code duplicateion!
        //val res: Rep[(List[Int], Int)] = runVoidCondState(exprog3[(State[List[Int], *] ⊕ (Cond ⊕ ∅)#t)#t](u), xs)

        // This one handles cond first, and then state, and generates code
        //   that compute both branches first, then use the results (variables)
        //   in the generated `if`.
        val res: Rep[(List[Int], Int)] = runVoidStateCond(exprog3[(Cond ⊕ (State[List[Int], *] ⊕ ∅)#t)#t](u), xs)

        println(res._1)
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

  def main(args: Array[String]): Unit = {
    val code = specialize(1)
    println(code.code)
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

