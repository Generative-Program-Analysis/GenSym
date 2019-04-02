package sai.evaluation

import sai.lms._
import sai.monads._
import sai.lattices._
import sai.lattices.Lattices._
import sai.evaluation.parser._

// Unstaged 0CFA for Scheme, withou store widening
object UnstagedSchemeAnalyzer extends AbstractComponents {
  import ReaderT._
  import StateT._
  import ListT._
  import IdM._

  type R[T] = T
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

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

  def mapM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[List[B]] = Monad.mapM(xs)(f)
  def forM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[B] = Monad.forM(xs)(f)

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + xα)
  def local_env(ans: Ans)(ρ: Env): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(_ => ρ)

  // Store operations
  def get_store: AnsM[Store] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => {
                                                                                         val news = σ ⊔ Map(αv)
                                                                                         val oldc = σ.getOrElse(αv._1, Set())
                                                                                         val newc = news(αv._1)
                                                                                         if (oldc != newc) {
                                                                                           println(s"growing: ${αv._1} oldsize: ${oldc.size} newsize: ${newc.size}")
                                                                                         }

                                                                                         news
                                                                                       }))

  // allocate addresses
  def alloc(σ: Store, x: String): Addr = ZCFAAddr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Primitive operations
  def void: Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set())
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
  def br(ev: EvalFun)(test: Value, thn: Expr, els: Expr): Ans =
    //ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(ev(thn), ev(els)) // they use different store and cache
    for { //Note: they use the same store and cache
      ρ <- ask_env
      σ <- get_store
      in <- ask_in_cache
      out <- get_out_cache
      val (v1, out1) = ev(thn)(ρ)(σ).run(in)(out).run
      val (v2, out2) = ev(els)(ρ)(σ).run(in)(out1).run
      (v, s) <- lift_nd[(Value, Store)](v1 ++ v2)
      _ <- put_store(s)
      _ <- put_out_cache(out2)
    } yield v
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
  def ap_clo(ev: EvalFun)(fun: Value, args: List[Value]): Ans = for {
    CloV(Lam(params, e), ρ: Env) <- lift_nd(fun.toList)
    αs <- mapM(params)(alloc)
    _ <- mapM(αs.zip(args))(set_store)
    v <- local_env(ev(e))(params.zip(αs).foldLeft(ρ)(_+_))
  } yield v

  def primtives(v: Value, args: List[Value]): Value = ???

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
    val _ = println(s"Eval ${ASTUtils.exprToString(e)} – ρ size: ${ρ.size} – σ size: ${σ.size} – out: ${out.size}")
    rt <- if (out.contains(cfg)) {
      for {
        (v, s) <- lift_nd[(Value, Store)](out(cfg).toList)
        _ <- put_store(s)
      } yield v
    } else {
      // TODO: get which store affects!
      val sameKeys = out.keys.filter { case (e1,ρ1,σ1) => e1 == e && ρ1 == ρ }
      println(s"## size same keys: ${sameKeys.size}")
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

  type Result = (List[(Value, Store)], Cache)
  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run

  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]
}
