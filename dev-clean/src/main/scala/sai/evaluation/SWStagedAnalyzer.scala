package sai.evaluation

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._
import sai.structure.monad._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._
import sai.evaluation.parser._

@virtualize
trait SWStagedSchemeAnalyzerOps extends AbstractComponents with SAIOps {
  import ReaderT._
  import StateT._
  import SetStateReaderStateM._

  type Config = (Int, Env)
  type Cache = Map[Config, Set[Value]]

  def mCache: Manifest[Cache] = manifest[Cache]
  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]

  type R[T] = Rep[T]
  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type NdStoreInOutCacheM[T] = SetStateReaderStateM[Cache, Store, Cache, T]
  type AnsM[T] = EnvT[NdStoreInOutCacheM, T]

  type Result = ((Set[Value], Store), Cache)

  def mapM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[List[B]] = Monad.mapM(xs)(f)
  def forM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[B] = Monad.forM(xs)(f)

  def emit_ap_clo(f: Rep[AbsValue], arg: Rep[List[Value]], σ: Rep[Store],
    in: Rep[Cache], out: Rep[Cache]): Rep[Result] = {
    Wrap[Result](Adapter.g.reflect("sai-ap-clo",
      Unwrap(f), Unwrap(arg), Unwrap(σ), Unwrap(in), Unwrap(out)))
  }

  type CompClo = (Rep[List[Value]], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[Result]
  def emit_compiled_clo(f: CompClo, λ: Lam, ρ: Rep[Env]): Rep[AbsValue] = {
    val block = Adapter.g.reify(4, syms => {
      val vs :: σ :: in :: out :: Nil = syms
      val w_vs = Wrap[List[Value]](vs)
      val w_σ = Wrap[Store](σ)
      val w_in = Wrap[Cache](in)
      val w_out = Wrap[Cache](out)
      Unwrap(f(w_vs, w_σ, w_in, w_out))
    })
    val block_node = Wrap[(List[Value], Store, Cache, Cache) => Result](
      Adapter.g.reflect("λ", block, Backend.Const("val")))
    Wrap[AbsValue](Adapter.g.reflect("sai-comp-clo", Unwrap(block_node), Unwrap(unit[Int](λ.hashCode)), Unwrap(ρ)))
  }

  def emit_addr(x: String): Rep[Addr] = unit(ZCFAAddr(x))

  // environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[NdStoreInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans =
    ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))
  def local_env(ans: Ans)(ρ: Rep[Env]): Ans = ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(_ => ρ)

  // allocate addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = emit_addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // store operations
  def get_store: AnsM[Store] = ReaderT.liftM[NdStoreInOutCacheM, Env, Store](SetStateReaderStateMonad[Cache, Store, Cache].get1)
  def put_store(σ: Rep[Store]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](SetStateReaderStateMonad[Cache, Store, Cache].put1(σ))
  def set_store(αv: (Rep[Addr], Rep[Value])): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetStateReaderStateMonad[Cache, Store, Cache].mod1(σ => σ ⊔ Map(αv))
    )

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
    ReaderTMonad[NdStoreInOutCacheM, Env].pure[Value](Set[AbsValue](unit(v)))
  }
  def get(ρ: Rep[Env], x: String): Rep[Addr] = ρ(x)
  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))
  def br(ev: EvalFun)(test: Expr, thn: Expr, els: Expr): Ans =
    ReaderTMonadPlus[NdStoreInOutCacheM, Env].mplus(ev(thn), ev(els))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(params, e) = λ
    val f: (Rep[List[Value]], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[Result] = {
      case (args, σ, in, out) =>
        val αs: List[Rep[Addr]] = params.map(x => alloc(σ, x))
        val repαs: Rep[List[Addr]] = List(αs :_*)
        val ρ_* : Rep[Env] = params.map(unit[String]).zip(αs).foldLeft(ρ)(_ + _)
        val σ_* = repαs.zip(args).foldLeft(σ) {
          case (σ, αv) => σ ⊔ Map(αv)
        }
        val res = ev(e)(ρ_*)(σ_*, in, out)
        (res._1, res._2): (Rep[(Set[Value], Store)], Rep[Cache])
    }
    Set[AbsValue](emit_compiled_clo(f, λ, ρ))
  }

  def ap_clo(ev: EvalFun)(fun: Rep[Value], args: Rep[List[Value]]): Ans = for {
    clo <- lift_clo[AbsValue](fun)
    in  <- ask_in_cache
    out <- get_out_cache
    σ   <- get_store
    res <- lift_nd[Result](Set(emit_ap_clo(clo, args, σ, in, out)))
    _   <- put_out_cache(res._2)
    _   <- put_store(res._1._2)
    v   <- lift_nd[Value](res._1._1)
  } yield v

  // auxiliary function that lifts values
  def lift_clo[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    lift_nd[T](vs.filter(x => unchecked[Boolean](x, ".isInstanceOf[CompiledClo]")))

  def lift_nd[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, T](
      fromSet[Cache, Store, Cache, T](vs)
    )

  // cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](
      SetStateReaderStateMonad[Cache, Store, Cache].ask
    )
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](
      SetStateReaderStateMonad[Cache, Store, Cache].get2
    )
  def put_out_cache(out: Rep[Cache]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetStateReaderStateMonad[Cache, Store, Cache].put2(out)
    )
  def update_out_cache(cfg: Rep[Config], vs: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](
      SetStateReaderStateMonad[Cache, Store, Cache].mod2(c => c ⊔ Map(cfg → Set(vs)))
    )

  def primMaps = scala.collection.immutable.Map[String, Rep[Set[AbsValue]]](
      "not" -> Set[AbsValue](unit(BoolV))
    , "ceiling" -> Set[AbsValue](unit(FloatV))
    , "-" -> Set[AbsValue](unit(IntV))
    , "log" -> Set[AbsValue](unit(FloatV))
    , "vector" -> Set[AbsValue](unit(VectorVTop))
    , "display" -> Set[AbsValue](unit(VoidV))
    , "<=" -> Set[AbsValue](unit(BoolV))
    , "or" -> Set[AbsValue](unit(BoolV))
    , "=" -> Set[AbsValue](unit(BoolV))
    , "*" -> Set[AbsValue](unit(IntV))
    , "and" -> Set[AbsValue](unit(BoolV))
    , "/" -> Set[AbsValue](unit(IntV))
    , "random" -> Set[AbsValue](unit(FloatV))
    , "modulo" -> Set[AbsValue](unit(IntV))
    , "newline" -> Set[AbsValue](unit(VoidV))
    , "odd?" -> Set[AbsValue](unit(BoolV))
    , ">" -> Set[AbsValue](unit(BoolV))
    , "error" -> Set[AbsValue](unit(VoidV))
    , "cons" -> Set[AbsValue](unit(ListVTop))
    , "cdr" -> Set[AbsValue](unit(ListVTop))
    , "car" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "<" -> Set[AbsValue](unit(BoolV))
    , "quotient" -> Set[AbsValue](unit(IntV))
    , "gcd" -> Set[AbsValue](unit(IntV))
    /*****************************************/
    , "fl+" -> Set[AbsValue](unit(FloatV))
    , "+" -> Set[AbsValue](unit(IntV))
    , "->fl" -> Set[AbsValue](unit(FloatV))
    , "read" -> Set[AbsValue](unit(EofV), unit(VectorVTop))
    , ">=" -> Set[AbsValue](unit(BoolV))
    , "fl>" -> Set[AbsValue](unit(BoolV))
    , "vector-set!" -> Set[AbsValue](unit(VoidV))
    , "imag-part" -> Set[AbsValue](unit(IntV))
    , "make-rectangular" -> Set[AbsValue](unit(VectorVTop))
    , "number->string" -> Set[AbsValue](unit(VectorVTop))
    , "vector-ref" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "real-part" -> Set[AbsValue](unit(IntV))
    , "fl*" -> Set[AbsValue](unit(FloatV))
    , "write" -> Set[AbsValue](unit(VoidV))
    , "make-vector" -> Set[AbsValue](unit(VectorVTop))
    /*****************************************/
    , "less" -> Set[AbsValue](unit(SymV))
    , "high" -> Set[AbsValue](unit(SymV))
    , "low" -> Set[AbsValue](unit(SymV))
    , "uncomparable" -> Set[AbsValue](unit(SymV))
    , "equal" -> Set[AbsValue](unit(SymV))
    , "more" -> Set[AbsValue](unit(SymV))
    //lattice
    , "set-cdr!" -> Set[AbsValue](unit(VoidV))
    , "remainder" -> Set[AbsValue](unit(IntV))
    , "eq?" -> Set[AbsValue](unit(BoolV))
    , "null?" -> Set[AbsValue](unit(BoolV))
    , "memq" -> Set[AbsValue](unit(ListVTop))
    , "append" -> Set[AbsValue](unit(ListVTop))
    , "else" -> Set[AbsValue](unit(BoolV))
    , "list" -> Set[AbsValue](unit(ListVTop))
    , "%" -> Set[AbsValue](unit(IntV))
    , "brother" -> Set[AbsValue](unit(SymV)) //matrix symbols
    , "child" -> Set[AbsValue](unit(SymV))
    , "now" -> Set[AbsValue](unit(SymV))
    , "puke" -> Set[AbsValue](unit(SymV))
    , "caar" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "for-each" -> Set[AbsValue](unit(VoidV))
    , "map" -> Set[AbsValue](unit(ListVTop))
    , "expt" -> Set[AbsValue](unit(IntV))
    , "even?" -> Set[AbsValue](unit(BoolV))
    , "length" -> Set[AbsValue](unit(IntV))
    , "reverse" -> Set[AbsValue](unit(ListVTop))
    , "cadr" -> Set[AbsValue](unit(ListVTop))
    , "vector-ref" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "cddr" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "zero?" -> Set[AbsValue](unit(BoolV))
    , "symbol?" -> Set[AbsValue](unit(BoolV))
    , "equal?" ->  Set[AbsValue](unit(BoolV))
    , "pair?" ->   Set[AbsValue](unit(BoolV))
    , "char?" ->   Set[AbsValue](unit(BoolV))
  )

  def primitives(ev: EvalFun)(x: String, args: List[Expr]): Ans = {
    if (x == "apply") {
      val (f::rest) = args
      ev(App(f, rest))
    } else {
      for {
        _ <- mapM(args)(ev)
      } yield primMaps(x)
    }
  }

  def fix_select: EvalFun = e => e match {
    case Void() | SSym(_) | CharLit(_) | IntLit(_)
       | FloatLit(_) | BoolLit(_) | Var(_) | Lam(_, _) => eval(fix_select)(e)
    case _ => fix_cache(e)
  }

  def fix_cache(e: Expr): Ans = for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    cfg <- lift_nd[Config](Set((unit(e.hashCode), ρ)))
    res <- lift_nd[((Set[Value], Store), Cache)](Set(
      if (out.contains(cfg)) {
        //FIXME: better syntax
        val t: Rep[(Set[Value], Store)] = (out(cfg), σ)
        (t, out): Rep[((Set[Value], Store), Cache)]
      } else {
        val ans_bot = in.getOrElse(cfg, RepLattice[Set[Value]].bot)
        val m: Ans = for {
          _ <- put_out_cache(out + (cfg → ans_bot))
          v <- eval(fix_select)(e)
          _ <- update_out_cache(cfg, v)
        } yield v
        val t = m(ρ)(σ, in, out)
        (t._1, t._2): (Rep[(Set[Value], Store)], Rep[Cache])
      }))
    _ <- put_store(res._1._2)
    _ <- put_out_cache(res._2)
    v <- lift_nd(res._1._1)
  } yield v

  lazy val ρ0: Rep[Env] = Map[String, Addr]()
  lazy val σ0: Rep[Store] = Map[Addr, Value]()
  lazy val cache0: Rep[Cache] = Map[Config, Set[Value]]()

  def fix(ev: EvalFun => EvalFun): EvalFun = fix_select
  def run(e: Expr): Rep[Result] = {
    def staged_iter: Rep[(Cache, Cache) => ((Set[Value], Store), Cache)] = fun({
      case (in: Rep[Cache], out: Rep[Cache]) =>
        val result = fix(eval)(e)(ρ0)(σ0, in, out)
        val out_* = result._2
        if (in == out_*) {
          //FIXME: better syntax
          val vs: Rep[(Set[Value], Store)] = (result._1._1, result._1._2)
          (vs, result._2): Rep[((Set[Value], Store), Cache)]
        } else staged_iter(out_*, cache0)
    })
    staged_iter(cache0, cache0)
  }

  def run_once(e: Expr): Rep[Result] = {
    val result = fix(eval)(e)(ρ0)(σ0, cache0, cache0)
    val vs: Rep[(Set[Value], Store)] = (result._1._1, result._1._2)
    (vs, result._2): Rep[((Set[Value], Store), Cache)]
  }
}

trait SWStagedSchemeAnalyzerDriver extends SAIDriver[Unit, Unit] with SWStagedSchemeAnalyzerOps { q =>
  override val codegen = new ScalaGenBase with StagedSchemeAnalyzerGen {
    val IR: q.type = q
    import IR._
  }

  override val prelude = """
import sai.evaluation.parser._
import sai.evaluation.SWSAIRuntime._
"""
}


/*
trait SWZeroCFAEnvOpt extends MapOpsExpOpt { self: SWStagedSchemeAnalyzerOps =>
  override def map_apply[K:Manifest,V:Manifest](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = (m, k) match {
    case (m1: Exp[Map[String,Addr]], Const(x: String)) => unit(ZCFAAddr(x).asInstanceOf[V])
    case _ => super.map_apply(m, k)
  }
}
 */

