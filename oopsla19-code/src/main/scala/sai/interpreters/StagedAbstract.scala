package sai

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import scala.reflect._

import sai.lms._
import sai.monads._
import sai.lattices._
import sai.lattices.Lattices._
import PCFLang._

@virtualize
trait StagedAbstractSemantics extends AbstractComponents with RepMonads with RepLattices with SAIDsl {
  import ReaderT._
  import StateT._
  import ListReaderStateM._

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NdInOutCacheM[T] = ListReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[ListReaderStateM[Cache, Cache, ?], Store, ?], Env, T]

  def mCache: Manifest[Cache] = manifest[Cache]

  // code generation
  def emit_ap_clo(fun: Rep[AbsValue], arg: Rep[Value], σ: Rep[Store],
                  in: Rep[Cache], out: Rep[Cache]): Rep[(List[(Value, Store)], Cache)]
  def emit_compiled_clo(f: (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)],
                        λ: Lam, ρ: Rep[Env]): Rep[AbsValue]
  def emit_inttop: Rep[AbsValue] = unit(IntTop)
  def emit_addr(x: String): Rep[Addr] = unit(Addr(x))

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

  // auxiliary function that lifts values
  def lift_nd[T: Manifest](vs: Rep[List[T]]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListReaderStateM.fromList(vs)
      ))

  // primitive operations
  def br0(test: Rep[Value], thn: => Ans, els: => Ans): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)

  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))

  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](emit_inttop))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)] = {
      case (v, σ, in, out) =>
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ ⊔ Map(α → v)).run(in)(out)
    }
    Set[AbsValue](emit_compiled_clo(f, λ, ρ))
  }

  def arith(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = Set[AbsValue](emit_inttop)

  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = for {
    σ <- get_store
    clo <- lift_nd[AbsValue](fun.toList)
    in <- ask_in_cache
    out <- get_out_cache
    res <- lift_nd[(List[(Value, Store)], Cache)](List(emit_ap_clo(clo, arg, σ, in, out)))
    _ <- put_out_cache(res._2)
    vs <- lift_nd[(Value, Store)](res._1)
    _ <- put_store(vs._2)
  } yield vs._1

  // cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListReaderStateMonad[Cache, Cache].ask
      ))

  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListReaderStateMonad[Cache, Cache].get
      ))

  def put_out_cache(out: Rep[Cache]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListReaderStateMonad[Cache, Cache].put(out)
      ))

  def update_out_cache(cfg: Rep[Config], vs: Rep[(Value, Store)]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListReaderStateMonad[Cache, Cache].mod(c => c ⊔ Map(cfg → Set(vs)))
      ))

  def fix_select: EvalFun = e => e match {
    case Lit(_) | Var(_) | Lam(_, _) => eval(fix_select)(e)
    case _ => fix_cache(e)
  }

  def fix_cache(e: Expr): Ans = for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    cfg <- lift_nd[(Expr, Env, Store)](List((unit(e), ρ, σ)))
    res <- lift_nd[(List[(Value, Store)], Cache)](List(
      if (out.contains(cfg)) {
        (repMapToMapOps(out).apply(cfg).toList, out)
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
    _ <- put_out_cache(res._2)
    vs <- lift_nd(res._1)
    _ <- put_store(vs._2)
  } yield vs._1

  // non-selective caching
  def fix_nonsel(ev: EvalFun => EvalFun): EvalFun = { e =>
    for {
      ρ <- ask_env
      σ <- get_store
      in <- ask_in_cache
      out <- get_out_cache
      val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
      val res: Rep[(List[(Value, Store)], Cache)] =
        if (out.contains(cfg)) {
          (repMapToMapOps(out).apply(cfg).toList, out)
        } else {
          val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
          val m: Ans = for {
            _ <- put_out_cache(out + (cfg → res_in))
            v <- ev(fix(ev))(e)
            σ <- get_store
            _ <- update_out_cache(cfg, (v, σ))
          } yield v
          m(ρ)(σ)(in)(out)
        }
      _ <- put_out_cache(res._2)
      vs <- lift_nd(res._1)
      _ <- put_store(vs._2)
    } yield vs._1
  }

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()
  val cache0: Rep[Cache] = Map[Config, Set[(Value, Store)]]()

  def fix(ev: EvalFun => EvalFun): EvalFun = fix_select
  def run(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix(eval)(e)(ρ0)(σ0)(cache0)(cache0)

  def run_nonsel(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix_nonsel(eval)(e)(ρ0)(σ0)(cache0)(cache0)
}

// An optimization for 0-CFA, which eliminates the environment.
trait AbsEnvExpOpt extends MapOpsExpOpt { self: StagedAbstractSemantics =>
  override def map_apply[K:Manifest,V:Manifest](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = (m, k) match {
    case (m1: Exp[Map[String,Addr]], Const(x: String)) => unit(Addr(x).asInstanceOf[V])
    case _ => super.map_apply(m, k)
  }
}

trait StagedAbstractSemanticsExp extends StagedAbstractSemantics with SAIOpsExp with AbsEnvExpOpt {
  case class IRCompiledClo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
                           rf: Exp[((Value, Store, Cache, Cache)) => (List[(Value, Store)], Cache)],
                           λ: Exp[Lam], ρ: Exp[Env]) extends Def[AbsValue]
  case class IRApClo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store],
                     in: Exp[Cache], out: Exp[Cache]) extends Def[(List[(Value, Store)], Cache)]

  def emit_compiled_clo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
                        λ: Lam, ρ: Exp[Env]) = reflectEffect(IRCompiledClo(f, fun(f), unit(λ), ρ))

  def emit_ap_clo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) = clo match {
    //Note: egar beta-reduction, produces larger residual code
    //case Def(Reflect(CompiledClo(f, rf, λ, ρ), s, d)) => f(arg, σ, in, out)
    //case Def(CompiledClo(f, rf, λ, ρ)) => f(arg, σ, in, out)
    case _ => reflectEffect(IRApClo(clo, arg, σ, in, out))
  }
}

trait StagedAbstractSemanticsGen extends GenericNestedCodegen {
  val IR: StagedAbstractSemanticsExp
  import IR._

  override def remap[A](m: Manifest[A]): String = {
    if (m.toString.endsWith("$AbsValue")) "AbsValue"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$Expr")) "Expr"
    else super.remap(m)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IRCompiledClo(f, rf, λ, ρ) =>
      emitValDef(sym, s"CompiledClo(${quote(rf)}, ${quote(λ)}, ${quote(ρ)})")
    case IRApClo(f, arg, σ, in, out) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(arg)}, ${quote(σ)}, ${quote(in)}, ${quote(out)})")
    case Struct(tag, elems) =>
      registerStruct(structName(sym.tp), sym.tp, elems)
      val typeName = sym.tp.runtimeClass.getSimpleName +
        "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
      emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait StagedAbstractSemanticsDriver extends DslDriver[Unit, Unit] with StagedAbstractSemanticsExp { q =>
  override val codegen = new DslGen
      with ScalaGenMapOps
      with ScalaGenSetOps
      with ScalaGenListOps
      with ScalaGenUncheckedOps
      with SAI_ScalaGenTupleOps
      with SAI_ScalaGenTupledFunctions
      with StagedAbstractSemanticsGen
  {
    val IR: q.type = q
    override def emitSource[A : Manifest](args: List[Sym[_]], body: Block[A], className: String,
                                          stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      val prelude = """
  import sai.PCFLang._
  object RT {
    type Value = Set[AbsValue]
    case class Addr(x: String) { override def toString = x }
    type Env = Map[String, Addr]
    type Store = Map[Addr, Value]
    type Config = (Expr, Env, Store)
    type Cache = Map[Config, Set[(Value, Store)]]
    trait AbsValue
    case object IntTop extends AbsValue
    case class CompiledClo(f: (Value, Store, Cache, Cache) => (List[(Value, Store)], Cache), λ: Lam, ρ: Env) extends AbsValue
  }
  import RT._
      """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}
