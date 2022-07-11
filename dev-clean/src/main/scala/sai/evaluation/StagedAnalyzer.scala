package sai.oopsla19

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._
import sai.structure.monad._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._
import sai.oopsla19.parser._

@virtualize
trait StagedSchemeAnalyzerOps extends AbstractComponents with SAIOps {
  import ReaderT._
  import StateT._
  import SetReaderStateM._

  def mCache: Manifest[Cache] = manifest[Cache]
  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]

  type Config = (Int, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]
  type Result = (Set[(Value, Store)], Cache)

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type NdInOutCacheM[T] = SetReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[SetReaderStateM[Cache, Cache, *], Store, *], Env, T]

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

  def emit_addr(x: String): Rep[Addr] = unit[Addr](ZCFAAddr(x))

  // environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans =
    ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))
  def local_env(ans: Ans)(ρ: Rep[Env]): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(_ => ρ)

  // allocate addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = emit_addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def set_store(αv: (Rep[Addr], Rep[Value])): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(αv)))

  def void: Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue]())
  def literal(i: Any): Ans = {
    val v: AbsValue = i match {
      case i: Int => IntV
      case f: Double => FloatV
      case c: Char => CharV
      case b: Boolean => BoolV
      case x: String => SymV
      case _ =>
        throw new RuntimeException(s"value representation for $i not implemented")
    }
    ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](unit(v)))
  }

  def get(ρ: Rep[Env], x: String): Rep[Addr] = ρ(x)

  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))

  def br(ev: EvalFun)(test: Expr, thn: Expr, els: Expr): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(ev(thn), ev(els))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(params, e) = λ
    val f: (Rep[List[Value]], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(Set[(Value,Store)], Cache)] = {
      case (args, σ, in, out) =>
        val αs: List[Rep[Addr]] = params.foldRight(collection.immutable.List[Rep[Addr]]()) {
          case (x, αs_*) => alloc(σ, x)::αs_*
        }
        val ρ_* = params.zip(αs).foldLeft(ρ) { case (ρ, (x, α)) => ρ + (unit(x) → α) }
        val repαs: Rep[List[Addr]] = List(αs :_*)
        val σ_* = repαs.zip(args).foldLeft(σ) { case (σ, αv) => σ ⊔ Map(αv) }
        ev(e)(ρ_*)(σ_*).run(in)(out)
    }
    Set[AbsValue](emit_compiled_clo(f, λ, ρ))
  }

  def ap_clo(ev: EvalFun)(fun: Rep[Value], args: Rep[List[Value]]): Ans = for {
    σ   <- get_store
    clo <- lift_clo[AbsValue](fun)
    in  <- ask_in_cache
    out <- get_out_cache
    res <- lift_nd[(Set[(Value, Store)], Cache)](Set(emit_ap_clo(clo, args, σ, in, out)))
    _   <- put_out_cache(res._2)
    vs  <- lift_nd[(Value, Store)](res._1)
    _   <- put_store(vs._2)
  } yield vs._1

  def primMaps = scala.collection.immutable.Map[String, Rep[Set[AbsValue]]](
      "not" -> Set[AbsValue](unit(BoolV))
    , "ceiling" -> Set[AbsValue](unit(FloatV))
    , "-" -> Set[AbsValue](unit(IntV))
    , "log" -> Set[AbsValue](unit(FloatV))
    , "vector" -> Set[AbsValue](unit(VectorVTop))
    , "display" -> Set[AbsValue]()
    , "<=" -> Set[AbsValue](unit(BoolV))
    , "or" -> Set[AbsValue](unit(BoolV))
    , "=" -> Set[AbsValue](unit(BoolV))
    , "*" -> Set[AbsValue](unit(IntV))
    , "and" -> Set[AbsValue](unit(BoolV))
    , "/" -> Set[AbsValue](unit(IntV))
    , "random" -> Set[AbsValue](unit(FloatV))
    , "modulo" -> Set[AbsValue](unit(IntV))
    , "newline" -> Set[AbsValue]()
    , "odd?" -> Set[AbsValue](unit(BoolV))
    , ">" -> Set[AbsValue](unit(BoolV))
    , "error" -> Set[AbsValue]()
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
    , "vector-set!" -> Set[AbsValue]()
    , "imag-part" -> Set[AbsValue](unit(IntV))
    , "make-rectangular" -> Set[AbsValue](unit(VectorVTop))
    , "number->string" -> Set[AbsValue](unit(VectorVTop))
    , "vector-ref" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "real-part" -> Set[AbsValue](unit(IntV))
    , "fl*" -> Set[AbsValue](unit(FloatV))
    , "write" -> Set[AbsValue]()
    , "make-vector" -> Set[AbsValue](unit(VectorVTop))
    /*****************************************/
    , "less" -> Set[AbsValue](unit(SymV))
    , "high" -> Set[AbsValue](unit(SymV))
    , "low" -> Set[AbsValue](unit(SymV))
    , "uncomparable" -> Set[AbsValue](unit(SymV))
    , "equal" -> Set[AbsValue](unit(SymV))
    , "more" -> Set[AbsValue](unit(SymV))
    //lattice
    , "set-cdr!" -> Set[AbsValue]()
    , "remainder" -> Set[AbsValue](unit(IntV))
    , "eq?" -> Set[AbsValue](unit(BoolV))
    , "null?" -> Set[AbsValue](unit(BoolV))
    , "memq" -> Set[AbsValue](unit(ListVTop))
    , "append" -> Set[AbsValue](unit(ListVTop))
    , "else" -> Set[AbsValue](unit(BoolV))
    , "list" -> Set[AbsValue](unit(ListVTop))
    , "%" -> Set[AbsValue](unit(IntV))
    , "brother" -> Set[AbsValue](unit(SymV)) //matrix symbols
    , "caar" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "for-each" -> Set[AbsValue]()
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
      /*
      for {
        fv <- ev(f)
        as <- mapM(rest)(ev)
        v <- ap_clo(ev)(fv, as)
      } yield v
       */
    } else {
      for {
        _ <- mapM(args)(ev)
      } yield primMaps(x)
    }
  }

  // auxiliary function that lifts values
  def lift_nd[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        SetReaderStateM.fromSet(vs)
      ))

  def lift_clo[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    lift_nd[T](vs.filter(x => unchecked[Boolean](x, ".isInstanceOf[CompiledClo]")))

  // cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        SetReaderStateMonad[Cache, Cache].ask
      ))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        SetReaderStateMonad[Cache, Cache].get
      ))
  def put_out_cache(out: Rep[Cache]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        SetReaderStateMonad[Cache, Cache].put(out)
      ))
  def update_out_cache(cfg: Rep[Config], vs: Rep[(Value, Store)]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        SetReaderStateMonad[Cache, Cache].mod(c => c ⊔ Map(cfg → Set(vs)))
      ))

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
    cfg <- lift_nd[Config](Set((unit(e.hashCode), ρ, σ)))
    res <- lift_nd[(Set[(Value, Store)], Cache)](Set(
      if (out.contains(cfg)) {
        (out(cfg), out)
      } else {
        val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
        val m: Ans = for {
          _ <- put_out_cache(out + (cfg → res_in))
          v <- eval(fix_select)(e)
          σ <- get_store
          _ <- update_out_cache(cfg, (v, σ))
        } yield v
        m(ρ)(σ)(in)(out)
      }))
    _  <- put_out_cache(res._2)
    vs <- lift_nd(res._1)
    _  <- put_store(vs._2)
  } yield vs._1

  lazy val ρ0: Rep[Env] = Map[String, Addr]()
  lazy val σ0: Rep[Store] = Map[Addr, Value]()
  lazy val cache0: Rep[Cache] = Map[Config, Set[(Value, Store)]]()

  def fix(ev: EvalFun => EvalFun): EvalFun = fix_select
  def run(e: Expr): Rep[Result] = {
    def staged_iter: Rep[(Cache, Cache) => (Set[(Value, Store)], Cache)] = fun({
      case (in: Rep[Cache], out: Rep[Cache]) =>
        val result = fix(eval)(e)(ρ0)(σ0)(in)(out)
        val out_* = result._2
        if (in == out_*) { result }
        else { staged_iter(out_*, cache0) }
    })
    staged_iter(cache0, cache0)
  }
  def run_once(e: Expr): Rep[Result] = fix(eval)(e)(ρ0)(σ0)(cache0)(cache0)
}

trait StagedSchemeAnalyzerGen extends SAICodeGenBase {
  override def remap(m: Manifest[_]): String = {
    val ms = m.toString
    if (ms.endsWith("$AbsValue")) "AbsValue"
    else if (ms.endsWith("$ZCFAAddr")) "ZCFAAddr"
    else if (ms.endsWith("$Addr")) "Addr"
    else if (ms.endsWith("$Expr")) "Expr"
    else if (ms.endsWith("sai.oopsla19.parser.Expr")) "Expr"
    else if (ms.startsWith("scala.collection.immutable.Map[java.lang.String,")
      && ms.endsWith("$Addr]")) "Env"
    else super.remap(m)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "sai-comp-clo", List(bn, λ, ρ), _) =>
      emit("CompiledClo(")
      shallow(bn); emit("_val"); emit(", ")
      shallow(λ); emit(", ")
      shallow(ρ); emitln(")")
    case Node(s, "sai-ap-clo", List(f, args, σ, in, out), _) =>
      shallow(f)
      emit(".asInstanceOf[CompiledClo].f(")
      shallow(args); emit(", ")
      shallow(σ); emit(", ")
      shallow(in); emit(", ")
      shallow(out); emitln(")")
      /*
    case Node(s, "map-new", Backend.Const(mK: Manifest[_])::Backend.Const(mV: Manifest[_])::kvs, _) =>
      def constKeyVal(kv: Backend.Exp): Boolean = {
        ???
      }
      if (mK == manifest[String] && mV == manifest[Addr] && kvs.forall(constKeyVal)) {
        shallow(Backend.Const(kvs.hashCode))
      } else {
        super.shallow(n)
      }
       */
      /*
    case MapNew(kvs, mk, mv)
        if (mk == manifest[String] && mv == manifest[Addr] && kvs.forall(kv => kv._1.isInstanceOf[Const[String]] && kv._2.isInstanceOf[Const[Addr]])) =>
      emitValDef(sym, src"${quote(kvs.hashCode)}")
       */
    case _ => super.shallow(n)
  }
}

trait StagedSchemeAnalyzerDriver extends SAIDriver[Unit, Unit] with StagedSchemeAnalyzerOps { q =>
  override val codegen = new ScalaGenBase with StagedSchemeAnalyzerGen {
    val IR: q.type = q
    import IR._
  }

  override val prelude = """
import sai.oopsla19.parser._
import sai.oopsla19.SAIRuntimeOpt._
"""
}

/*
trait ZeroCFAEnvOpt extends MapOpsExpOpt { self: StagedSchemeAnalyzerOps =>
  override def map_apply[K:Manifest,V:Manifest](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = (m, k) match {
    case (m1: Exp[Map[String,Addr]], Const(x: String)) => unit(ZCFAAddr(x).asInstanceOf[V])
    case _ => super.map_apply(m, k)
  }
}
*/
