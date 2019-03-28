package sai.evaluation

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.monads._
import sai.lattices._
import sai.lattices.Lattices._

import sai.evaluation.parser._

trait SchemeAnalyzer {
  type MonadOps[R[_], M[_], A] = {
    def map[B: Manifest](f: R[A] => R[B]): M[B]
    def flatMap[B: Manifest](f: R[A] => M[B]): M[B]
  }
  type Ident = String
  type R[_]
  type Addr
  type Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type AnsM[T] <: MonadOps[R, AnsM, T]
  type Ans = AnsM[Value]
  type Result
  type EvalFun = Expr => Ans

  // Environment operations
  def ask_env: AnsM[Env]
  def ext_env(ans: Ans)(xα: (String, R[Addr])): Ans

  // Store operations
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def set_store(αv: (R[Addr], R[Value])): AnsM[Unit]

  // allocate addresses
  def alloc(x: String): AnsM[Addr]
  def alloc(σ: R[Store], x: String): R[Addr]

  // Primitive operations
  def void: Ans
  def literal(i: Any): Ans
  def get(ρ: R[Env], x: String): R[Addr]
  def get(σ: R[Store], ρ: R[Env], x: String): R[Value]
  def br(test: R[Value], thn: => Ans, els: => Ans): Ans
  def arith(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[List[Value]]): Ans

  // Fixpoint wrapper and top-level interface
  def fix(ev: EvalFun => EvalFun): EvalFun
  def run(e: Expr): Result

  implicit def mValue: Manifest[Value]

  def mapM[A, B](xs: List[A])(f: A => AnsM[B]): AnsM[List[B]]
  def forM[A, B](xs: List[A])(f: A => AnsM[B]): AnsM[B]

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Void() => void
    case Sym(x) => literal(x)
    case CharLit(c) => literal(c)
    case IntLit(i) => literal(i)
    case FloatLit(f) => literal(f)
    case BoolLit(b) => literal(b)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(ev)(Lam(x, e), ρ)
    case Set_!(x, e) => for {
      v <- ev(e)
      ρ <- ask_env
      _ <- set_store(get(ρ, x) → v)
      n <- void
    } yield n
    case Define(x, e) => for {
      α <- alloc(x)
      v <- ext_env(ev(e))(x → α)
      _ <- set_store(α → v)
      n <- void
    } yield n
    case App(e1, args) => for {
      v1 <- ev(e1)
      v2 <- mapM(args)(ev)
      rt <- ap_clo(ev)(v1, v2)
    } yield rt
    case Begin(es) => forM(es)(ev)
    case If(c, t, e) => for {
      cnd <- ev(c)
      rt <- br(cnd, ev(t), ev(e))
    } yield rt
  }
}

// Unstaged 0CFA for Scheme, precise store
trait UnstagedSchemeAnalyzer extends SchemeAnalyzer {
  import ReaderT._
  import StateT._
  import ListT._
  import IdM._

  trait AbsValue
  case object IntV extends AbsValue
  case object BoolV extends AbsValue
  case object VoidV extends AbsValue
  case object FloatV extends AbsValue
  case object CharV extends AbsValue
  case object EofV extends AbsValue
  case object InputPortV extends AbsValue
  case object OutputPortV extends AbsValue
  case object MtListV extends AbsValue
  case class PrimOpV(op: String) extends AbsValue
  case class ConsV(a: Addr, b: Addr) extends AbsValue
  case class VectorV(vs: List[Addr]) extends AbsValue
  case class CloV(lam: Lam, env: Env) extends AbsValue
  //addr as value?

  trait Addr
  case class ZCFAAddr(x: String) extends Addr
  case class OCfaAddr(x: String, ctx: Expr) extends Addr

  type Value = Set[AbsValue]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]
  type R[T] = T

  // Transformers
  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]
  type InCacheT[F[_], B] = ReaderT[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]
  // Monads
  type OutCacheM[T] = OutCacheT[IdM, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type NdInOutCacheM[T] = NondetT[InOutCacheM, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  def mapM[A, B](xs: List[A])(f: A => AnsM[B]): AnsM[List[B]] = Monad.mapM(xs)(f)
  def forM[A, B](xs: List[A])(f: A => AnsM[B]): AnsM[B] = Monad.forM(xs)(f)

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + xα)

  // Store operations
  def get_store: AnsM[Store] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(αv)))

  // allocate addresses
  def alloc(σ: Store, x: String): Addr = ZCFAAddr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Primitive operations
  def void: Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set(VoidV))
  def literal(i: Any): Ans = {
    val v: AbsValue = i match {
      case i: Int => IntV
      case f: Double => FloatV
      case c: Char => CharV
      case b: Boolean => BoolV
      case _ =>
        println(s"value representation for $i not implemented")
        ???
    }
    ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set(v))
  }
  def get(ρ: Env, x: String): Addr = ρ(x)
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def br(test: Value, thn: => Ans, els: => Ans): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)
  def arith(op: Symbol, v1: Value, v2: Value): Value = Set(IntV, FloatV)
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = ???
  def ap_clo(ev: EvalFun)(fun: Value, arg: List[Value]): Ans = ???

  def primtives(v: AbsValue, args: List[Value]): Value = ???

  def lift_nd[T](vs: List[T]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListT.fromList[InOutCacheM, T](vs)
      ))

  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListT.liftM[InOutCacheM, Cache](
          ReaderTMonad[OutCacheM, Cache].ask
        )))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListT.liftM[InOutCacheM, Cache](
          ReaderT.liftM[OutCacheM, Cache, Cache](
            StateTMonad[IdM, Cache].get
          ))))
  def put_out_cache(out: Cache): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].put(out)
          ))))
  def update_out_cache(cfg: Config, vs: (Value, Store)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].mod(c => c ⊔ Map(cfg → Set(vs))
            )))))

  def fix(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    val cfg = (e, ρ, σ)
    rt <- if (out.contains(cfg)) {
      for {
        (v, s) <- lift_nd[(Value, Store)](out(cfg).toList)
        _ <- put_store(s)
      } yield v
    } else {
      val ans_in = in.getOrElse(cfg, Lattice[Set[(Value, Store)]].bot)
      for {
        _ <- put_out_cache(out + (cfg → ans_in))
        v <- ev(fix(ev))(e)
        σ <- get_store
        _ <- update_out_cache(cfg, (v, σ))
      } yield v
    }
  } yield rt

  // Fixpoint wrapper and top-level interface
  val ρ0: Env = Map()
  val σ0: Store = Map()
  val cache0: Cache = Map()

  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run

  def mValue: Manifest[Value] = manifest[Value]
}
