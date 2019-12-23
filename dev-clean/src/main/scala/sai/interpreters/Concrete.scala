package sai

import sai.lang.FunLang._
import sai.structure.monad._

trait ConcreteComponents extends Semantics {
  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CloV(lam: Lam, e: Env) extends Value

  type Addr = Int
  type Result = (R[Value], R[Store])

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)
  def mValue: Manifest[Value] = manifest[Value]
}

trait ConcreteSemantics extends ConcreteComponents {
  import ReaderT._
  import StateT._
  import IdM._

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]
  type StoreM[T] = StoreT[IdM, T]

  type R[T] = T
  type AnsM[T] = ReaderT[StateT[IdM, Store, ?], Env, T]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = for {
    ρ <- ask_env
    v <- local_env(ans)(ρ + xα)
  } yield v
  def local_env(ans: Ans)(ρ: Env): Ans = ReaderTMonad[StoreM, Env].local(ans)(_ => ρ)

  // Allocating addresses
  def alloc(σ: Store, x: String) = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + αv))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](IntV(i))
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      _ <- set_store(α → arg)
      rt <- local_env(ev(e))(ρ + (x → α))
    } yield rt
  }
  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els
  def arith(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  val ρ0: Env = Map[Ident, Addr]()
  val σ0: Store = Map[Addr, Value]()
  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run
}
