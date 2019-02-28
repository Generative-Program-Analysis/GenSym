package sai

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

@virtualize
trait SAIRepMonads { self: SAIDsl =>

  trait RepMonad[M[_]] {
    def pure[A: Manifest](a: Rep[A]): M[A]
    def flatMap[A: Manifest, B: Manifest](ma: M[A])(f: Rep[A] => M[B]): M[B]
    def map[A: Manifest,B: Manifest](ma: M[A])(f: Rep[A] => Rep[B]): M[B] = flatMap(ma)(a => pure(f(a)))
  }

  object RepMonad {
    def apply[M[_]](implicit m: RepMonad[M]): RepMonad[M] = m
  }

  /////////////////////////////////////////////////

  trait RepMonadPlus[M[_]]  {
    def mzero[A: Manifest]: M[A]
    def mplus[A: Manifest](a: M[A], b: M[A]): M[A]
  }

  object RepMonadPlus {
    def apply[M[_]](implicit m: RepMonadPlus[M]): RepMonadPlus[M] = m
  }

  /////////////////////////////////////////////////

  object RepIdMonadInstance {
    type Id[T] = Rep[T]
    implicit val RepIdMonad: RepMonad[Id] = new RepMonad[Id] {
      def pure[A: Manifest](a: Rep[A]): Id[A] = a
      def flatMap[A: Manifest, B: Manifest](ma: Id[A])(f: Rep[A] => Id[B]): Id[B] = f(ma)
    }
  }

  /////////////////////////////////////////////////

  trait RepMonadReader[F[_], R] extends RepMonad[F] {
    def ask: F[R]
    def local[A: Manifest](fa: F[A])(f: Rep[R] => Rep[R]): F[A]
  }

  object RepMonadReader {
    def apply[F[_], S: Manifest](implicit r: MonadReader[F, S]): MonadReader[F, S] = r
  }

  object RepReaderT {
    def apply[M[_]: RepMonad, R: Manifest, A: Manifest](implicit m: RepReaderT[M, R, A]): RepReaderT[M, R, A] = m
    implicit def apply[M[_]: RepMonad, R: Manifest]: RepMonad[RepReaderT[M, R, ?]] = RepReaderTMonad[M, R]

    implicit def RepReaderTMonad[M[_]: RepMonad, R: Manifest] = new RepMonadReader[RepReaderT[M, R, ?], R] {
      def flatMap[A: Manifest, B: Manifest](fa: RepReaderT[M, R, A])(f: Rep[A] => RepReaderT[M, R, B]): RepReaderT[M, R, B] =
        fa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): RepReaderT[M, R, A] = RepReaderT(_ => RepMonad[M].pure(a))

      def ask: RepReaderT[M, R, R] = RepReaderT(r => RepMonad[M].pure(r))
      def local[A: Manifest](fa: RepReaderT[M, R, A])(f: Rep[R] => Rep[R]): RepReaderT[M, R, A] =
        RepReaderT(f andThen fa.run)
    }

    def liftM[G[_]: RepMonad, R: Manifest, A: Manifest](ga: G[A]): RepReaderT[G, R, A] =
      RepReaderT(r => ga)
  }

  case class RepReaderT[M[_]: RepMonad, R: Manifest, A: Manifest](run: Rep[R] => M[A]) {
    import RepReaderT._
    def apply(r: Rep[R]): M[A] = run(r)
    def flatMap[B: Manifest](f: Rep[A] => RepReaderT[M, R, B]): RepReaderT[M, R, B] =
      RepReaderT(r => RepMonad[M].flatMap(run(r))(a => f(a).run(r)))
    def map[B: Manifest](f: Rep[A] => Rep[B]): RepReaderT[M, R, B] =
      RepReaderT((r: Rep[R]) => RepMonad[M].map(run(r))(f))
  }

  /////////////////////////////////////////////////

  trait RepMonadState[F[_], S] extends RepMonad[F] {
    def get: F[S]
    def put(s: Rep[S]): F[Unit]
    def mod(f: Rep[S] => Rep[S]): F[Unit]
  }

  object RepMonadState {
    def apply[F[_], S](implicit s: RepMonadState[F, S]): RepMonadState[F, S] = s
  }

  object RepStateT {
    def apply[M[_]: RepMonad, S: Manifest, A: Manifest](implicit m: RepStateT[M, S, A]): RepStateT[M, S, A] = m
    implicit def apply[M[_]: RepMonad, S: Manifest]: RepMonad[RepStateT[M, S, ?]] = RepStateTMonad[M, S]

    implicit def RepStateTMonad[M[_]: RepMonad, S: Manifest] = new RepMonadState[RepStateT[M, S, ?], S] {
      def flatMap[A: Manifest, B: Manifest](sa: RepStateT[M, S, A])(f: Rep[A] => RepStateT[M, S, B]) = sa.flatMap(f)
      def pure[A: Manifest](a: Rep[A]): RepStateT[M, S, A] = RepStateT(s => RepMonad[M].pure((a, s)))

      def get: RepStateT[M, S, S] = RepStateT(s => RepMonad[M].pure((s, s)))
      def put(s: Rep[S]): RepStateT[M, S, Unit] = RepStateT(_ => RepMonad[M].pure((unit(()), s)))
      def mod(f: Rep[S] => Rep[S]): RepStateT[M, S, Unit] = RepStateT(s => RepMonad[M].pure((unit(()), f(s))))
    }

    implicit def RepStateTMonadPlus[M[_]: RepMonad : RepMonadPlus, S: Manifest] = new RepMonadPlus[RepStateT[M, S, ?]] {
      def mzero[A: Manifest]: RepStateT[M, S, A] = ???
      def mplus[A: Manifest](a: RepStateT[M, S, A], b: RepStateT[M, S, A]): RepStateT[M, S, A] =
        RepStateT(s => RepMonadPlus[M].mplus(a.run(s), b.run(s)))
    }

    def liftM[G[_]: RepMonad, S: Manifest, A: Manifest](ga: G[A]): RepStateT[G, S, A] =
      RepStateT(s => RepMonad[G].map(ga)(a => (a, s)))
  }

  case class RepStateT[M[_]: RepMonad, S: Manifest, A: Manifest](run: Rep[S] => M[(A, S)]) {
    import RepStateT._
    def apply(s: Rep[S]): M[(A, S)] = run(s)
    def flatMap[B: Manifest](f: Rep[A] => RepStateT[M, S, B]): RepStateT[M, S, B] =
      RepStateT(s => RepMonad[M].flatMap(run(s)) { case as1: Rep[(A, S)] =>
                  val a = as1._1; val s1 = as1._2
                  f(a).run(s1)
                })
    def map[B: Manifest](f: Rep[A] => Rep[B]): RepStateT[M, S, B] =
      flatMap(a => RepStateT(s => RepMonad[M].pure((f(a), s))))
  }

}

trait RepEnvInterpreter extends SAIDsl with SAIRepMonads {
  import PCFLang._
  import RepIdMonadInstance._
  import RepReaderT._
  import RepStateT._

  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CloV[Env](lam: Lam, e: Env) extends Value

  type Ident = String
  type Value0 = Value

  type Addr0 = Int
  type Addr = Rep[Addr0]

  type Env0 = Map[Ident, Addr0]
  type Env = Rep[Env0]

  type Store0 = Map[Addr0, Value0]
  type Store = Rep[Store0]

  type StoreT[F[_], B] = RepStateT[Id, Store0, B]
  type EnvT[F[_], T] = RepReaderT[F, Env0, T]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value0]

  def num(i: Int): Ans = ???

  def ask_env: AnsM[Env0] = RepReaderTMonad[StoreM, Env0].ask
  def ext_env(x: Rep[String], a: Rep[Addr0]): AnsM[Env0] = for { ρ <- ask_env } yield ρ + (x → a)

  def alloc(σ: Store, x: String): Rep[Addr0] = σ.size + 1
  def alloc(x: String): AnsM[Addr0] = for { σ <- get_store } yield σ.size + 1

  def get_store: AnsM[Store0] = RepReaderT.liftM[StoreM, Env0, Store0](RepStateTMonad[Id, Store0].get)

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
      /*
    case Var(x) => for {
      ρ <- RepReaderTMonad[StoreM, Env0].ask
    } yield ρ(x)
       */
  }


}
