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
  //import ListT._
  import SetT._
  import IdM._

  type R[T] = T
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

  // Transformers
  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  //type NondetT[F[_], B] = ListT[F, B]
  type NondetT[F[_], B] = SetT[F, B]
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
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => { σ ⊔ Map(αv) }))

  // allocate addresses
  def alloc(σ: Store, x: String): Addr = { ZCFAAddr(x) }
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Primitive operations
  def void: Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set())
  def literal(i: Any): Ans = {
    val v: AbsValue = i match {
      case i: Int => IntV
      case f: Double => FloatV
      case c: Char => CharV
      case b: Boolean => BoolV
      case x: String => SymV
      case _ =>
        println(s"value representation for $i not implemented")
        ???
    }
    ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set(v))
  }

  def get(ρ: Env, x: String): Addr = ρ(x)
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def br(ev: EvalFun)(test: Expr, thn: Expr, els: Expr): Ans =
    /*for {
    v1 <- ev(thn)
    v2 <- ev(els)
  } yield v1 ++ v2
     */
  ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(ev(thn), ev(els)) // they use different store and cache?
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
  def ap_clo(ev: EvalFun)(fun: Value, args: List[Value]): Ans = for {
    CloV(Lam(params, e), ρ: Env) <- lift_nd(fun)
    αs <- mapM(params)(alloc)
    _ <- mapM(αs.zip(args))(set_store)
    v <- local_env(ev(e))(params.zip(αs).foldLeft(ρ)(_+_))
  } yield v

  def primMaps = Map[String, Value](
        "not" -> Set(BoolV)
      , "ceiling" -> Set(FloatV)
      , "-" -> Set(IntV)
      , "log" -> Set(FloatV)
      , "vector" -> Set(VectorVTop)
      , "display" -> Set()
      , "<=" -> Set(BoolV)
      , "or" -> Set(BoolV)
      , "=" -> Set(BoolV)
      , "*" -> Set(IntV)
      , "and" -> Set(BoolV)
      , "/" -> Set(IntV)
      , "random" -> Set(FloatV)
      , "modulo" -> Set(IntV)
      , "newline" -> Set()
      , "odd?" -> Set(BoolV)
      , ">" -> Set(BoolV)
      , "error" -> Set()
      , "cons" -> Set(ListVTop)
      , "cdr" -> Set(ListVTop)
      , "car" -> Set(IntV, FloatV, CharV, BoolV) //FIXME
      , "<" -> Set(BoolV)
      , "quotient" -> Set(IntV)
      , "gcd" -> Set(IntV)
      , "fl+" -> Set(FloatV)
      , "+" -> Set(IntV)
      , "->fl" -> Set(FloatV)
      , "read" -> Set(EofV, VectorVTop)
      , ">=" -> Set(BoolV)
      , "fl>" -> Set(BoolV)
      , "vector-set!" -> Set()
      , "imag-part" -> Set(IntV)
      , "make-rectangular" -> Set(VectorVTop)
      , "number->string" -> Set(VectorVTop)
      , "vector-ref" -> Set(IntV, FloatV, CharV, BoolV)
      , "real-part" -> Set(IntV)
      , "fl*" -> Set(FloatV)
      , "write" -> Set()
      , "make-vector" -> Set(VectorVTop)
      /***************************************/
      , "less" -> Set(SymV)
      , "high" -> Set(SymV)
      , "low" -> Set(SymV)
      , "uncomparable" -> Set(SymV)
      , "equal" -> Set(SymV)
      , "more" -> Set(SymV)
      //lattice
      , "set-cdr!" -> Set()
      , "remainder" -> Set(IntV)
      , "eq?" -> Set(BoolV)
      , "null?" -> Set(BoolV)
      , "memq" -> Set(ListVTop)
      , "append" -> Set(ListVTop)
      , "else" -> Set(BoolV)
      , "list" -> Set(ListVTop)
      // matrix
      , "child" -> Set(SymV)
      , "now" -> Set(SymV)
      , "puke" -> Set(SymV)
      , "brother" -> Set(SymV) //matrix symbols
      , "caar" -> Set(IntV, FloatV, CharV, BoolV)
      , "for-each" -> Set()
      , "map" -> Set(ListVTop)
      , "expt" -> Set(IntV)
      , "even?" -> Set(BoolV)
      , "length" -> Set(IntV)
      , "reverse" -> Set(ListVTop)
      , "cadr" -> Set(ListVTop)
      , "vector-ref" -> Set(IntV, FloatV, CharV, BoolV)
      , "cddr" -> Set(IntV, FloatV, CharV, BoolV)
      , "zero?" -> Set(BoolV)
      , "%" -> Set(BoolV)
      , "symbol?" -> Set(BoolV)
      , "equal?" -> Set(BoolV)
      , "pair?" -> Set(BoolV)
      , "char?" -> Set(BoolV)
  )

  def primitives(ev: EvalFun)(x: String, args: List[Expr]): Ans = {
    if (x == "apply") {
      val (f::rest) = args
      for {
        fv <- ev(f)
        as <- mapM(rest)(ev)
        v <- ap_clo(ev)(fv, as)
      } yield v
    } else {
      for {
        _ <- mapM(args)(ev)
      } yield primMaps(x)
    }
  }

  def foldVss(vss: List[Value]): Value = vss.foldLeft(Lattice[Value].bot)(_ ⊔ _)

  def lift_nd[T](vs: Set[T]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        SetT.fromSet[InOutCacheM, T](vs)
      ))

  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        SetT.liftM[InOutCacheM, Cache](
          ReaderTMonad[OutCacheM, Cache].ask
        )))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        SetT.liftM[InOutCacheM, Cache](
          ReaderT.liftM[OutCacheM, Cache, Cache](
            StateTMonad[IdM, Cache].get
          ))))
  def put_out_cache(out: Cache): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        SetT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].put(out)
          ))))
  def update_out_cache(cfg: Config, vs: (Value, Store)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        SetT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].mod(c => c ⊔ Map(cfg → Set(vs))
            )))))

  def fix(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    val cfg = (e, ρ, σ)
    in <- ask_in_cache
    out <- get_out_cache
    //val _ = println(s"Eval out: ${out.size}")
    rt <- if (out.contains(cfg)) {
      //val _ = println(s"MISS ${ASTUtils.exprToString(e)}, ρ: ${ρ.size}, σ: ${σ.size}, out: ${out.size}")
      for {
        //_ <- put_out_cache(out)
        (v, s) <- lift_nd[(Value, Store)](out(cfg))
        _ <- put_store(s)
      } yield v
    } else {
      //val _ = println(s"HIT ${ASTUtils.exprToString(e)}, ρ: ${ρ.size}, σ: ${σ.size}, out: ${out.size}")
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

  type Result = (Set[(Value, Store)], Cache)
  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run

  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]
}
