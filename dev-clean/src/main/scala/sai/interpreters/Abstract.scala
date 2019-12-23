package sai

import sai.structure.monad._
import sai.lattices._
import sai.lattices.Lattices._

import FunLang._

trait AbstractComponents extends Semantics {
  sealed trait AbsValue
  case object IntTop extends AbsValue
  case class IntV(i: Int) extends AbsValue
  case class CloV(lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]
  case class Addr(x: String) { override def toString = "Addr(\"" + x + "\")" }

  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]
  type Result = (R[List[(Value, Store)]], R[Cache])

  def ask_in_cache: AnsM[Cache]
  def get_out_cache: AnsM[Cache]
  def put_out_cache(out: R[Cache]): AnsM[Unit]
  def update_out_cache(cfg: R[Config], vs: R[(Value, Store)]): AnsM[Unit]

  implicit def mCache: Manifest[Cache]
  def mValue: Manifest[Value] = manifest[Value]
}

trait AbstractSemantics extends AbstractComponents {
  import ReaderT._
  import StateT._
  import ListT._
  import IdM._

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]
  type InCacheT[F[_], B] = ReaderT[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]

  type OutCacheM[T] = OutCacheT[IdM, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type NdInOutCacheM[T] = NondetT[InOutCacheM, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]

  type R[T] = T
  type AnsM[T] = ReaderT[StateT[ListT[ReaderT[StateT[IdM, Cache, ?], Cache, ?], ?], Store, ?], Env, T]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + xα)
  def local_env(ans: Ans)(ρ: Env): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(_ => ρ)

  // Allocating addresses
  def alloc(σ: Store, x: String): Addr = Addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(αv)))

  def extract_num(v: AbsValue): Int = v match { case IntV(i) => i }

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](IntTop))
  //def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](IntV(i)))

  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))

  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)

  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))

  def arith(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case _ if v1.contains(IntTop) && v2.contains(IntTop) => Set(IntTop)
    case ('+, v1, v2) => for {
      x <- v1
      y <- v2
    } yield IntV(extract_num(x) + extract_num(y))
    case ('-, v1, v2) => for {
      x <- v1
      y <- v2
    } yield IntV(extract_num(x) - extract_num(y))
    case ('*, v1, v2) => for {
      x <- v1
      y <- v2
    } yield IntV(extract_num(x) * extract_num(y))
    case ('/, v1, v2) => for {
      x <- v1
      y <- v2
    } yield IntV(extract_num(x) / extract_num(y))
  }

  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = for {
    CloV(Lam(x, e), ρ: Env) <- lift_nd[AbsValue](fun.toList)
    α <- alloc(x)
    _ <- set_store(α → arg)
    rt <- local_env(ev(e))(ρ + (x → α))
  } yield rt

  // auxiliary function that lifts values
  def lift_nd[T](vs: List[T]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListT.fromList[InOutCacheM, T](vs)
      ))

  // Cache operations
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

  val ρ0: Env = Map()
  val σ0: Store = Map()
  val cache0: Cache = Map()

  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run
}
