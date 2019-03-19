package sai

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
import sai.examples._

object AbsInterpreter {
  import PCFLang._
  import ReaderT._
  import StateT._
  import ListT._
  import IdMonadInstance._

  trait AbsValue
  case object IntTop extends AbsValue
  case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]

  type Ident = String
  case class Addr(x: String) { override def toString = x }
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]
  type InCacheT[F[_], B] = ReaderT[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]

  type OutCacheM[T] = OutCacheT[Id, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type NdInOutCacheM[T] = NondetT[InOutCacheM, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  type Ans = AnsM[Value] // EnvT[StateT[NondetT[InCacheT[OutCacheT[Id, ?], ?], ?], ?], Value]

  type EvalFun = Expr => Ans

  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ev(e))(_ => ρ)

  def alloc(σ: Store, x: String): Addr = Addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  def get_store: AnsM[Store] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(a → v)))
  //σ + (a → (σ.getOrElse(a, Lattice[Value].bot) ⊔ v))))

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)

  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](IntTop))
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case _ if v1.contains(IntTop) && v2.contains(IntTop) => Set(IntTop)
  }

  def lift_nd[T](vs: List[T]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListT.fromList[InOutCacheM, T](vs)
    ))

  //TODO: Test withFilter!!!
  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = for {
    CloV(Lam(x, e), ρ: Env) <- lift_nd[AbsValue](fun.toList)
    α <- alloc(x)
    ρ <- ext_env(x, α)
    _ <- update_store(α, arg)
    rt <- local_env(ev)(e, ρ)
  } yield rt

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
            StateTMonad[Id, Cache].get
          ))))
  def put_out_cache(out: Cache): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[Id, Cache].put(out)
          ))))
  def update_out_cache(cfg: Config, vs: (Value, Store)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[Id, Cache].mod(c => c ⊔ Map(cfg → Set(vs))
            )))))

  def fix_no_cache(ev: EvalFun => EvalFun): EvalFun = e => ev(fix_no_cache(ev))(e)

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
      val out_* = out + (cfg → ans_in)
      for {
        _ <- put_out_cache(out_*)
        v <- ev(fix(ev))(e)
        σ <- get_store
        _ <- update_out_cache(cfg, (v, σ))
      } yield v
    }
  } yield rt

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
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
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- branch0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(ev)(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case Amb(e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield v1 ++ v2
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  val cache0: Cache = Map()

  def run_wo_cache(e: Expr): (List[(Value, Store)], Cache) = fix_no_cache(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
  def run(e: Expr): (List[(Value, Store)], Cache) = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
}

@virtualize
trait StagedAbsInterpreterOps extends SAIDsl with SAIMonads with RepLattices {
  import PCFLang._
  import ReaderT._
  import StateT._
  import ListReaderStateM._

  trait AbsValue
  //case object IntTop extends AbsValue
  //case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]
  type Ident = String
  case class Addr(x: String) { override def toString = x }

  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type NdInOutCacheM[T] = ListReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  type Ans = AnsM[Value]

  type EvalFun = Expr => Ans

  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(x: Rep[String], a: Rep[Addr]): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Rep[Env]): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ev(e))(_ => ρ)

  def alloc(σ: Rep[Store], x: String): Rep[Addr] = unchecked("Addr(", x, ")")
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  def get_store: AnsM[Store] = ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def update_store(a: Rep[Addr], v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(a → v)))
  //σ + (a → (σ.getOrElse(a, RepLattice[Value].bot) ⊔ v))))

  def branch0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)

  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](unchecked("Set[AbsValue](IntTop)"))

  def lift_nd[T: Manifest](vs: Rep[List[T]]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListReaderStateM.fromList(vs)
      ))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: Rep[(Value, Store, Cache, Cache)] => Rep[(List[(Value, Store)], Cache)] = {
      case vscc: Rep[(Value, Store, Cache, Cache)] =>
        val v = vscc._1; val σ = vscc._2
        val in = vscc._3; val out = vscc._4
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ ⊔ Map(α → v)).run(in)(out)
    }
    unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
  }

  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] =
    unchecked("Set[AbsValue](IntTop)") //FIXME: check v1 && v2 contains Int

  def emit_ap_clo(fun: Rep[AbsValue], arg: Rep[Value], σ: Rep[Store], in: Rep[Cache], out: Rep[Cache]): Rep[(List[(Value, Store)], Cache)]

  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = for {
    clo <- lift_nd[AbsValue](fun.toList)
    in <- ask_in_cache
    out <- get_out_cache
    σ <- get_store
    val res: Rep[(List[(Value, Store)], Cache)] = emit_ap_clo(clo, arg, σ, in, out)
    val vss: Rep[List[(Value, Store)]] = res._1
    _ <- put_out_cache(res._2)
    vs <- lift_nd[(Value, Store)](vss)
    _ <- put_store(vs._2)
  } yield vs._1

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

  def fix_no_cache(ev: EvalFun => EvalFun): EvalFun = e => ev(fix_no_cache(ev))(e)

  def fix(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
    val res: Rep[(List[(Value, Store)], Cache)] = if (out.contains(cfg)) {
      (out.get(cfg).toList, out) //FIXME: out(cfg) vs out.get(cfg)
    } else {
      val ans_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
      val new_out = out + (cfg → ans_in)
      val t: Ans = for {
        _ <- put_out_cache(new_out)
        v <- ev(fix(ev))(e)
        σ <- get_store
        _ <- update_out_cache(cfg, (v, σ))
      } yield v
      t(ρ)(σ).run(in)(out)
    }
    _ <- put_out_cache(res._2)
    vs <- lift_nd(res._1)
    _ <- put_store(vs._2)
  } yield vs._1

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
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
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- branch0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(ev)(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case Amb(e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield v1 ++ v2
  }

  val ρ0: Rep[Env] = Map()
  val σ0: Rep[Store] = Map()
  val cache0: Rep[Cache] = Map()

  def run_wo_cache(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) =
    fix_no_cache(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
  def run(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
}

object MainAbs {
  import PCFLang._

  def main(args: Array[String]): Unit = {
    val lam = Lam("x", App(Var("x"), Var("x")))
    val omega = App(lam, lam)

    println(AbsInterpreter.run(id4)._1)
    println("--------------------------")
    println(AbsInterpreter.run(fact5)._1)
  }
}
