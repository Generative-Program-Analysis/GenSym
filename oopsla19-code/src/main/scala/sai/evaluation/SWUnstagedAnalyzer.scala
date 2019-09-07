package sai.evaluation

import sai.lms._
import sai.monads._
import sai.lattices._
import sai.lattices.Lattices._
import sai.evaluation.parser._

// Unstaged 0CFA for Scheme, with store widening
object SWUnstagedSchemeAnalyzer extends AbstractComponents {
  import ReaderT._
  import StateT._
  import SetT._
  import IdM._

  type R[T] = T
  type Config = (Expr, Env)
  type Cache = Map[Config, Set[Value]]

  // Transformers
  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = SetT[F, B]
  type InCacheT[F[_], B] = ReaderT[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]
  // Monads
  type OutCacheM[T] = OutCacheT[IdM, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type StoreInOutCacheM[T] = StoreT[InOutCacheM, T]
  type NdStoreInOutCacheM[T] = NondetT[StoreInOutCacheM, T]
  type AnsM[T] = EnvT[NdStoreInOutCacheM, T]

  def mapM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[List[B]] = Monad.mapM(xs)(f)
  def forM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[B] = Monad.forM(xs)(f)

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[NdStoreInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(ρ => ρ + xα)
  def local_env(ans: Ans)(ρ: Env): Ans = ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(_ => ρ)

  // allocate addresses
  def alloc(σ: Store, x: String): Addr = ZCFAAddr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Store](
      SetT.liftM[StoreInOutCacheM, Store](
        StateTMonad[InOutCacheM, Store].get
      ))
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetT.liftM[StoreInOutCacheM, Unit](
        StateTMonad[InOutCacheM, Store].put(σ)
      ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetT.liftM[StoreInOutCacheM, Unit](
        StateTMonad[InOutCacheM, Store].mod(σ => σ ⊔ Map(αv))
      ))

  // Primitive operations
  def void: Ans = literal(())
  def literal(i: Any): Ans = {
    val v: AbsValue = i match {
      case i: Int => IntV
      case f: Double => FloatV
      case c: Char => CharV
      case b: Boolean => BoolV
      case x: String => SymV
      case u: Unit => VoidV
      case _ =>
        throw new RuntimeException(s"value representation for $i not implemented")
    }
    ReaderTMonad[NdStoreInOutCacheM, Env].pure[Value](Set(v))
  }

  def get(ρ: Env, x: String): Addr = ρ(x)
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def br(ev: EvalFun)(test: Expr, thn: Expr, els: Expr): Ans =
    ReaderTMonadPlus[NdStoreInOutCacheM, Env].mplus(ev(thn), ev(els))
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
      , "display" -> Set(VoidV)
      , "<=" -> Set(BoolV)
      , "or" -> Set(BoolV)
      , "=" -> Set(BoolV)
      , "*" -> Set(IntV)
      , "and" -> Set(BoolV)
      , "/" -> Set(IntV)
      , "random" -> Set(FloatV)
      , "modulo" -> Set(IntV)
      , "newline" -> Set(VoidV)
      , "odd?" -> Set(BoolV)
      , ">" -> Set(BoolV)
      , "error" -> Set(VoidV)
      , "cons" -> Set(ListVTop)
      , "cdr" -> Set(ListVTop)
      , "car" -> Set(IntV, FloatV, CharV, BoolV)
      , "<" -> Set(BoolV)
      , "quotient" -> Set(IntV)
      , "gcd" -> Set(IntV)
      , "fl+" -> Set(FloatV)
      , "+" -> Set(IntV)
      , "->fl" -> Set(FloatV)
      , "read" -> Set(EofV, VectorVTop)
      , ">=" -> Set(BoolV)
      , "fl>" -> Set(BoolV)
      , "vector-set!" -> Set(VoidV)
      , "imag-part" -> Set(IntV)
      , "make-rectangular" -> Set(VectorVTop)
      , "number->string" -> Set(VectorVTop)
      , "vector-ref" -> Set(IntV, FloatV, CharV, BoolV)
      , "real-part" -> Set(IntV)
      , "fl*" -> Set(FloatV)
      , "write" -> Set(VoidV)
      , "make-vector" -> Set(VectorVTop)
      /***************************************/
      , "less" -> Set(SymV)
      , "high" -> Set(SymV)
      , "low" -> Set(SymV)
      , "uncomparable" -> Set(SymV)
      , "equal" -> Set(SymV)
      , "more" -> Set(SymV)
      //lattice
      , "set-cdr!" -> Set(VoidV)
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
      , "for-each" -> Set(VoidV)
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
      , "integer?" -> Set(BoolV)
      , "fl/" -> Set(FloatV)
      , "fl+" -> Set(FloatV)
      , "flsqrt" -> Set(FloatV)
      , "flcos" -> Set(FloatV)
      , "fl<" -> Set(FloatV)
      , "flatan" -> Set(FloatV)
      , "fl=" -> Set(FloatV)
      , "fl-" -> Set(FloatV)
      , "fl>" -> Set(FloatV)
      , "fl<=" -> Set(FloatV)
      , "fl*" -> Set(FloatV)
      , "flsin" -> Set(FloatV)
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
    ReaderT.liftM[NdStoreInOutCacheM, Env, T](
      SetT.fromSet[StoreInOutCacheM, T](vs)
    )

  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](
      SetT.liftM[StoreInOutCacheM, Cache](
        StateT.liftM[InOutCacheM, Store, Cache](
          ReaderTMonad[OutCacheM, Cache].ask
        )))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](
      SetT.liftM[StoreInOutCacheM, Cache](
        StateT.liftM[InOutCacheM, Store, Cache](
          ReaderT.liftM[OutCacheM, Cache, Cache](
            StateTMonad[IdM, Cache].get
          ))))
  def put_out_cache(out: Cache): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetT.liftM[StoreInOutCacheM, Unit](
        StateT.liftM[InOutCacheM, Store, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].put(out)
          ))))
  def update_out_cache(cfg: Config, vs: Value): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetT.liftM[StoreInOutCacheM, Unit](
        StateT.liftM[InOutCacheM, Store, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].mod(c => c ⊔ Map(cfg → Set(vs))
            )))))

  def print_select(e: Expr)(s: String): Unit = e match {
    case Void() | Sym(_) | CharLit(_) | IntLit(_)
       | FloatLit(_) | BoolLit(_) | Var(_) | Lam(_, _) => ()
    case _ => print(e); println(s)
  }

  def fix(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    val cfg = (e, ρ)
    rt <- if (out.contains(cfg)) {
      lift_nd[Value](out(cfg))
    } else {
      val ans_bot = in.getOrElse(cfg, Lattice[Set[Value]].bot)
      for {
        _ <- put_out_cache(out + (cfg → ans_bot))
        v <- ev(fix(ev))(e)
        _ <- update_out_cache(cfg, v)
      } yield v
    }
  } yield rt

  // Fixpoint wrapper and top-level interface
  val ρ0: Env = Map()
  val σ0: Store = Map()
  val cache0: Cache = Map()

  type Result = ((Set[Value], Store), Cache)
  def iter(e: Expr)(store: Store, in: Cache, out: Cache): Result = {
    val res = fix(eval)(e)(ρ0).run(store)(in)(out).run
    if (res._1._2 == store && res._2 == in) res
    else iter(e)(res._1._2, res._2, cache0)
  }
  def run(e: Expr): Result = {
    def iter(in: Cache, out: Cache): Result = {
      val result = fix(eval)(e)(ρ0).run(σ0)(in)(out).run
      val newOut = result._2
      if (in == newOut) result else iter(newOut, cache0)
    }
    iter(cache0, cache0)
  }

  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]
}
