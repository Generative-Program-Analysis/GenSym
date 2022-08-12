package sai

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lang.FunLang._
import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._
import sai.structure.monad._

import scala.collection.immutable.{List => StaticList}

@virtualize
trait StagedAbstractSemantics extends AbstractComponents with SAIOps {
  import ReaderT._
  import StateT._
  import ListReaderStateM._

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NdInOutCacheM[T] = ListReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[ListReaderStateM[Cache, Cache, *], Store, *], Env, T]

  def mCache: Manifest[Cache] = manifest[Cache]

  type CompClo = (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)]

  implicit class AbsValueOps(v: Rep[AbsValue]) {
    def apply(arg: Rep[Value], σ: Rep[Store], in: Rep[Cache], out: Rep[Cache]): Rep[(List[(Value, Store)], Cache)] =
      "sai-ap-clo".reflectWith[(List[(Value, Store)], Cache)](v, arg, σ, in, out)
  }
  object SAbsCloV {
    def apply(f: CompClo, λ: Lam, ρ: Rep[Env]): Rep[AbsValue] = {
      val block = Adapter.g.reify(4, syms => {
        val StaticList(v, σ, in, out) = syms
        val w_v = Wrap[Value](v)
        val w_σ = Wrap[Store](σ)
        val w_in = Wrap[Cache](in)
        val w_out = Wrap[Cache](out)
        Unwrap(f(w_v, w_σ, w_in, w_out))
      })
      val block_node = Wrap[(Value, Store, Cache, Cache) => (List[(Value, Store)], Cache)](
        Adapter.g.reflect("λ", block, Backend.Const("val")))
      "sai-comp-clo".reflectWith[AbsValue](block_node, unit[Lam](λ), ρ)
    }
  }

  def lift_int_top: Rep[AbsValue] = unit(IntTop)
  def lift_addr(x: String): Rep[Addr] = unit(Addr(x))

  // environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans =
    ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))
  def local_env(ans: Ans)(ρ: Rep[Env]): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(_ => ρ)

  // allocate addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = lift_addr(x)
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

  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](lift_int_top))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)] = {
      case (v, σ, in, out) =>
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ ⊔ Map(α → v)).run(in)(out)
    }
    Set[AbsValue](SAbsCloV(f, λ, ρ))
  }

  def arith(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = Set[AbsValue](lift_int_top)

  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = for {
    σ <- get_store
    clo <- lift_nd[AbsValue](fun.toList)
    in <- ask_in_cache
    out <- get_out_cache
    res <- lift_nd[(List[(Value, Store)], Cache)](List(clo(arg, σ, in, out)))
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
        (out(cfg).toList, out)
      } else {
        val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
        System.out.println(e)
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
      cfg <- lift_nd[(Expr, Env, Store)](List((unit(e), ρ, σ)))
      res <- lift_nd[(List[(Value, Store)], Cache)](List(
        if (out.contains(cfg)) {
          (out(cfg).toList, out)
        } else {
          val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
          val m: Ans = for {
            _ <- put_out_cache(out + (cfg → res_in))
            v <- ev(fix(ev))(e)
            σ <- get_store
            _ <- update_out_cache(cfg, (v, σ))
          } yield v
          m(ρ)(σ)(in)(out)
        }))
      _ <- put_out_cache(res._2)
      vs <- lift_nd(res._1)
      _ <- put_store(vs._2)
    } yield vs._1
  }

  lazy val ρ0: Rep[Env] = Map[String, Addr]()
  lazy val σ0: Rep[Store] = Map[Addr, Value]()
  lazy val cache0: Rep[Cache] = Map[Config, Set[(Value, Store)]]()

  def fix(ev: EvalFun => EvalFun): EvalFun = fix_select
  def run(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix(eval)(e)(ρ0)(σ0)(cache0)(cache0)

  def run_nonsel(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix_nonsel(eval)(e)(ρ0)(σ0)(cache0)(cache0)
}

trait StagedAbstractGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "sai-comp-clo", List(bn, λ, ρ), _) =>
      es"CompiledClo(${bn}_val, ${λ}, ${ρ})"
    case Node(s, "sai-ap-clo", List(f, arg, σ, in, out), _) =>
      es"$f.asInstanceOf[CompiledClo].f($arg, ${σ}, $in, ${out})"
    case _ => super.shallow(n)
  }
}

trait StagedAbstractDriver extends SAIDriver[Unit, Unit] with StagedAbstractSemantics { q =>
  override val codegen = new ScalaGenBase with StagedAbstractGen {
    val IR: q.type = q
    import IR._

    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$AbsValue")) "AbsValue"
      else if (m.toString.endsWith("$Addr")) "Addr"
      else if (m.toString.endsWith("$Expr")) "Expr"
      else if (m.toString.endsWith("String")) "String"
      else super.remap(m)
    }
  }

  override val prelude = """
  import sai.lang.FunLang._
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

}

/*

// An optimization for 0-CFA, which eliminates the environment.
trait AbsEnvExpOpt extends MapOpsExpOpt { self: StagedAbstractSemantics =>
  override def map_apply[K:Manifest,V:Manifest](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = (m, k) match {
    case (m1: Exp[Map[String,Addr]], Const(x: String)) => unit(Addr(x).asInstanceOf[V])
    case _ => super.map_apply(m, k)
  }
}
*/
