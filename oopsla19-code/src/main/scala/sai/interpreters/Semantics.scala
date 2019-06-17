package sai

import PCFLang._

trait Semantics {
  // Basic type definitions
  type Ident = String
  type Addr
  type Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  // Binding-time polymorphism
  type R[_]

  // Monadic interface
  type MonadOps[R[_], M[_], A] = {
    def map[B: Manifest](f: R[A] => R[B]): M[B]
    def flatMap[B: Manifest](f: R[A] => M[B]): M[B]
  }
  type AnsM[T] <: MonadOps[R, AnsM, T]
  type Ans = AnsM[Value]

  // Environment operations
  def ask_env: AnsM[Env]
  def ext_env(ans: Ans)(xα: (String, R[Addr])): Ans
  def local_env(ans: Ans)(ρ: R[Env]): Ans

  // Store operations
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def set_store(αv: (R[Addr], R[Value])): AnsM[Unit]

  // allocate addresses
  def alloc(x: String): AnsM[Addr]
  def alloc(σ: R[Store], x: String): R[Addr]

  // Primitive operations
  type EvalFun = Expr => Ans

  def num(i: Int): Ans
  def get(σ: R[Store], ρ: R[Env], x: String): R[Value]
  def br0(test: R[Value], thn: => Ans, els: => Ans): Ans
  def arith(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[Value]): Ans

  // Fixpoint wrapper and top-level interface
  def fix(ev: EvalFun => EvalFun): EvalFun
  type Result
  def run(e: Expr): Result

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(ev)(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
      rt <- ap_clo(ev)(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- ev(rhs)
      α <- alloc(x)
      _ <- set_store(α → v)
      rt <- ext_env(ev(e))(x → α)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- br0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield arith(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      v <- ext_env(ev(rhs))(x → α)
      _ <- set_store(α → v)
      rt <- ext_env(ev(e))(x → α)
    } yield rt
  }

  val ρ0: R[Env]
  val σ0: R[Store]
  implicit def mValue: Manifest[Value]
}
