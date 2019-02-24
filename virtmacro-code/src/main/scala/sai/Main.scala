package sai

import scala.language.implicitConversions

import scalaz._
import Scalaz._

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.examples._
import sai.lattices.Lattices._

object EnvInterpreter {
  /* An environment interpreter using Reader Monad */
  import PCFLang._
  import PCFLang.Values._
  type Ident = String
  type Env = Map[Ident, Value]

  type ReaderT[F[_], A, B] = Kleisli[F, A, B]
  val ReaderT = Kleisli
  import ReaderT._

  type AnsM[T] = ReaderT[Id, Env, T]
  type Ans = AnsM[Value]

  def num(i: Int): Ans = IntV(i).asInstanceOf[Value].pure[AnsM]

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def ap_clo(fun: Value, arg: Value): Ans = fun match { case CloV(Lam(x, e), ρ: Env) => eval(e).local[Env](_ => ρ + (x → arg)) }

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask[Id, Env]
    } yield ρ(x)
    case Lam(x, e) => for {
      ρ <- ask[Id, Env]
    } yield CloV(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      ρ <- ask[Id, Env]
      rt <- eval(e).local[Env](_ => ρ + (x → v))
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, eval(e2), eval(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => ???
  }
}

object EnvStoreInterpreter {
  /* An environment-and-store interpreter using Reader Monad and State Monad */
  import PCFLang._
  import PCFLang.Values._

  type Ident = String
  type Addr = Int
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type EnvT[F[_], B] = Kleisli[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value]

  def ask_env: AnsM[Env] = Kleisli.ask[StoreM, Env]
  def ext_env(xv: (String, Addr)): AnsM[Env] = for { ρ <- ask_env } yield ρ + xv

  def get_store: AnsM[Store] = MonadTrans[EnvT].liftM[StoreM, Store](State.get)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  def update_store(av: (Addr, Value)): AnsM[Unit] =
    MonadTrans[EnvT].liftM[StoreM, Unit](State.modify(σ => σ + av))

  def close(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def num(i: Int): Ans = IntV(i).asInstanceOf[Value].pure[AnsM]

  def local_env(e: Expr, ρ: Env): Ans = eval(e).local[Env](_ => ρ)

  def ap_clo(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      ρ <- ext_env(x → α)
      _ <- update_store(α → arg)
      rt <- local_env(e, ρ)
    } yield rt
  }

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      α <- alloc(x)
      _ <- update_store(α → v)
      ρ <- ext_env(x → α)
      rt <- local_env(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, eval(e2), eval(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x → α)
      v <- local_env(rhs, ρ)
      _ <- update_store(α → v)
      rt <- local_env(e, ρ)
    } yield rt
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  def run(e: Expr): (Store, Value) = eval(e).run(ρ0).run(σ0)
}

@virtualize
trait StagedCESOps extends SAIDsl {
  import PCFLang._

  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CloV[Env](lam: Lam, e: Env) extends Value

  type Ident = String

  type Addr0 = Int
  type Addr = Rep[Int]

  type Env0 = Map[Ident, Addr0]
  type Env = Rep[Env0]

  type Store0 = Map[Addr0, Value]
  type Store = Rep[Store0]

  type EnvT[F[_], B] = Kleisli[F, Env, B]
  type StoreT[F[_], B] = StateT[Id, Store, B]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Rep[Value]]

  def ask_env: AnsM[Env] = Kleisli.ask[StoreM, Env]
  def ext_env(x: Rep[String], a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)

  def alloc(σ: Store, x: String): Rep[Addr0] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  def put_store(σ: Store): AnsM[Unit] = MonadTrans[EnvT].liftM[StoreM, Unit](State.put(σ))
  def get_store: AnsM[Store] = MonadTrans[EnvT].liftM[StoreM, Store](State.get)
  def update_store(a: Addr, v: Rep[Value]): AnsM[Unit] =
    MonadTrans[EnvT].liftM[StoreM, Unit](State.modify(σ => σ + (a → v)))

  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    val v1n = unchecked(v1, ".asInstanceOf[IntV].i")
    val v2n = unchecked(v2, ".asInstanceOf[IntV].i")
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }

  def local_env(e: Expr, ρ: Env): Ans = eval(e).local[Env](_ => ρ)

  def branch0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = unchecked[Int](test, ".asInstanceOf[IntV].i")
    for {
      ρ <- ask_env
      σ <- get_store
      val res: Rep[(Store0, Value)] = if (i == 0) thn(ρ)(σ) else els(ρ)(σ)
      _ <- put_store(res._1)
    } yield res._2
  }

  def close(λ: Lam, ρ: Env): Rep[Value] = {
    val Lam(x, e) = λ
    val f: Rep[(Store0, Value)] => Rep[(Store0, Value)] = {
      case as: Rep[(Store0, Value)] =>
        val σ = as._1; val v = as._2
        val α = alloc(σ, x)
        eval(e).run(ρ + (unit(x) → α)).run(σ + (α → v))
    }
    unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
  }

  def ap_clo(fun: Rep[Value], arg: Rep[Value]): Ans

  def num(i: Int): Ans = unchecked[Value]("IntV(", i, ")").pure[AnsM]

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      α <- alloc(x)
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, eval(e2), eval(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(e, ρ)
    } yield rt
  }

  val ρ0: Env = Map[String, Addr0]()
  val σ0: Store = Map[Addr0, Value]()

  def run(e: Expr): (Store, Rep[Value]) = eval(e).run(ρ0).run(σ0)
}

trait StagedCESOpsExp extends StagedCESOps with SAIOpsExp {
  case class ApClo(f: Rep[Value], arg: Rep[Value], σ: Store) extends Def[(Store0, Value)]

  @virtualize
  def ap_clo(fun: Rep[Value], arg: Rep[Value]): Ans = for {
    σ <- get_store
    val res: Rep[(Store0, Value)] = reflectEffect(ApClo(fun, arg, σ))
    _ <- put_store(res._1)
  } yield res._2
}

trait StagedCESGen extends GenericNestedCodegen {
  val IR: StagedCESOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ApClo(f, arg, σ) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(σ)}, ${quote(arg)})")
    case Struct(tag, elems) =>
      //This fixes code generation for tuples, such as Tuple2MapIntValueValue
      //TODO: merge back to LMS
      registerStruct(structName(sym.tp), sym.tp, elems)
      val typeName = sym.tp.runtimeClass.getSimpleName +
        "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
      emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait StagedCESDriver extends DslDriver[Unit, Unit] with StagedCESOpsExp { q =>
  override val codegen = new DslGen
      with ScalaGenMapOps
      with ScalaGenSetOps
      with ScalaGenUncheckedOps
      with SAI_ScalaGenTupleOps
      with SAI_ScalaGenTupledFunctions
      with StagedCESGen {
    val IR: q.type = q

    override def remap[A](m: Manifest[A]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
    override def emitSource[A : Manifest](args: List[Sym[_]], body: Block[A], className: String,
                                          stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      val prelude = """
  import sai.PCFLang._
  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CompiledClo(f: (Map[Int,Value], Value) => (Map[Int,Value], Value), λ: Lam, ρ: Map[String,Int]) extends Value
        """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}

object AbsInterpreterWOCache {
  import PCFLang._

  trait AbsValue
  case object IntTop extends AbsValue
  case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]

  type Ident = String
  case class Addr(x: String)
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type EnvT[F[_], B] = Kleisli[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]

  type NondetM[T] = NondetT[Id, T]
  type StateNdM[T] = StoreT[NondetM, T]
  type AnsM[T] = EnvT[StateNdM, T]

  type Ans = AnsM[Value] // EnvT[StoreT[NondetT[Id, ?], ?], Value]

  def ask_env: AnsM[Env] = Kleisli.ask[StateNdM, Env]
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(e: Expr, ρ: Env): Ans = eval(e).local[Env](_ => ρ)

  def get_store: AnsM[Store] =
    MonadTrans[EnvT].liftMU(StateT.stateTMonadState[Store, NondetM].get)
  def put_store(σ: Store): AnsM[Unit] =
    MonadTrans[EnvT].liftMU(StateT.stateTMonadState[Store, NondetM].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    MonadTrans[EnvT].liftMU(StateT.stateTMonadState[Store, NondetM].modify(σ => σ + (a → (σ.getOrElse(a, Set()) ++ v))))

  def alloc(σ: Store, x: String): Addr = Addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case _ if v1.contains(IntTop) && v2.contains(IntTop) => Set(IntTop)
  }

  def join(e1: => Ans, e2: => Ans, ρ: Env): StateNdM[Value] =
    StateT.stateTMonadPlus[Store, NondetM].plus(e1(ρ), e2(ρ))

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans = for {
    ρ <- ask_env
    v <- MonadTrans[EnvT].liftM[StateNdM, Value](join(thn, els, ρ))
  } yield v

  def ap_clo(fun: Value, arg: Value): Ans = for {
    CloV(Lam(x, e), ρ: Env) <- MonadTrans[EnvT].liftM[StateNdM, AbsValue](MonadTrans[StoreT].liftMU(ListT.fromList[Id, AbsValue](fun.toList)))
    α <- alloc(x)
    ρ <- ext_env(x, α)
    _ <- update_store(α, arg)
    rt <- local_env(e, ρ)
  } yield rt

  def num(i: Int): Ans = Set[AbsValue](IntTop).pure[AnsM]
  def close(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      α <- alloc(x)
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, eval(e2), eval(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(e, ρ)
    } yield rt
    case Amb(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield v1 ++ v2
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  def run(e: Expr): List[(Store, Value)] = eval(e).run(ρ0).run(σ0).run
}

object AbsInterpreter {
  import PCFLang._

  trait AbsValue
  case object IntTop extends AbsValue
  case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]

  type Ident = String
  case class Addr(x: String)
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Store, Value)]]

  type EnvT[F[_], B] = Kleisli[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]
  type InCacheT[F[_], B] = Kleisli[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]

  type OutCacheM[T] = OutCacheT[Id, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type NdInOutCacheM[T] = NondetT[InOutCacheM, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  type Ans = AnsM[Value] // EnvT[StateT[NondetT[InCacheT[OutCacheT[Id, ?], ?], ?], ?], Value]

  def ask_env: AnsM[Env] = Kleisli.ask[StoreNdInOutCacheM, Env]
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ev(e).local[Env](_ => ρ)

  def get_store: AnsM[Store] =
    MonadTrans[EnvT].liftM(StateT.stateTMonadState[Store, NdInOutCacheM].get)
  def put_store(σ: Store): AnsM[Unit] =
    MonadTrans[EnvT].liftM(StateT.stateTMonadState[Store, NdInOutCacheM].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] = {
    MonadTrans[EnvT].liftM(StateT.stateTMonadState[Store, NdInOutCacheM].modify(σ =>
                             σ + (a → (σ.getOrElse(a, Lattice[Value].bot) ⊔ v))))
  }

  def alloc(σ: Store, x: String): Addr = Addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  def join(e1: => Ans, e2: => Ans, ρ: Env): StoreNdInOutCacheM[Value] =
    StateT.stateTMonadPlus[Store, NdInOutCacheM].plus(e1(ρ), e2(ρ))

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans = for {
    ρ <- ask_env
    v <- MonadTrans[EnvT].liftM[StoreNdInOutCacheM, Value](join(thn, els, ρ))
  } yield v

  def num(i: Int): Ans = Set[AbsValue](IntTop).pure[AnsM]
  def close(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case _ if v1.contains(IntTop) && v2.contains(IntTop) => Set(IntTop)
  }

  def lift_nd[T](vs: List[T]): AnsM[T] =
    MonadTrans[EnvT].liftM[StoreNdInOutCacheM, T](
      MonadTrans[StoreT].liftM[NdInOutCacheM, T](
        ListT.fromList[InOutCacheM, T](vs.pure[InOutCacheM])
      ))

  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = for {
    CloV(Lam(x, e), ρ: Env) <- lift_nd[AbsValue](fun.toList)
    α <- alloc(x)
    ρ <- ext_env(x, α)
    _ <- update_store(α, arg)
    rt <- local_env(ev)(e, ρ)
  } yield rt

  type EvalFun = Expr => Ans
  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)

  def ask_in_cache: AnsM[Cache] = MonadTrans[EnvT].liftM[StoreNdInOutCacheM, Cache](
    MonadTrans[StoreT].liftM[NdInOutCacheM, Cache](
      MonadTrans[NondetT].liftM[InOutCacheM, Cache](
        Kleisli.ask[OutCacheM, Cache]
      )))

  def get_out_cache: AnsM[Cache] = MonadTrans[EnvT].liftM[StoreNdInOutCacheM, Cache](
    MonadTrans[StoreT].liftM[NdInOutCacheM, Cache](
      MonadTrans[NondetT].liftM[InOutCacheM, Cache](
        MonadTrans[InCacheT].liftM[OutCacheM, Cache](
          StateT.stateTMonadState[Cache, Id].get
        ))))
  def update_out_cache(cfg: Config, sv: (Store,Value)): AnsM[Unit] =
    MonadTrans[EnvT].liftM[StoreNdInOutCacheM, Unit](
      MonadTrans[StoreT].liftM[NdInOutCacheM, Unit](
        MonadTrans[NondetT].liftM[InOutCacheM, Unit](
          MonadTrans[InCacheT].liftM[OutCacheM, Unit](
            StateT.stateTMonadState[Cache, Id].modify(c =>
              c + (cfg → (c.getOrElse(cfg, Lattice[Set[(Store, Value)]].bot) ⊔ Set(sv)))
            )))))

  def put_out_cache(out: Cache): AnsM[Unit] =
    MonadTrans[EnvT].liftM[StoreNdInOutCacheM, Unit](
      MonadTrans[StoreT].liftM[NdInOutCacheM, Unit](
        MonadTrans[NondetT].liftM[InOutCacheM, Unit](
          MonadTrans[InCacheT].liftM[OutCacheM, Unit](
            StateT.stateTMonadState[Cache, Id].put(out)
          ))))

  def fix_cache(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    val cfg = (e, ρ, σ)
    in <- ask_in_cache
    out <- get_out_cache
    rt <- if (out.contains(cfg)) {
      for {
        (s, v) <- lift_nd[(Store, Value)](out(cfg).toList)
        _ <- put_store(s)
      } yield v
    } else {
      val ans_in = in.getOrElse(cfg, Lattice[Set[(Store, Value)]].bot)
      val out_* = out + (cfg → ans_in)
      for {
        _ <- put_out_cache(out_*)
        v <- ev(fix_cache(ev))(e)
        σ <- get_store
        _ <- update_out_cache(cfg, (σ, v))
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
    } yield close(Lam(x, e), ρ)
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

  def run_wo_cache(e: Expr): (Cache, List[(Store, Value)]) = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
  def run(e: Expr): (Cache, List[(Store, Value)]) = fix_cache(eval)(e)(ρ0)(σ0).run(cache0)(cache0)
}

object Main {
  import PCFLang._

  def specialize(e: Expr): DslDriver[Unit, Unit] = new StagedCESDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val (s, v) = run(e)
      println(s)
      println(v)
    }
  }

  def main(args: Array[String]): Unit = {
    //val code = specialize(fact5)
    //println(code.code)
    //code.eval(())

    //val s = new Snippet()
    //println(s(()))
    //examples.NDTest.test
    //examples.NDTest.test2

    val lam = Lam("x", App(Var("x"), Var("x")))
    val omega = App(lam, lam)

    //println(AbsInterpreter.run_wo_cache(id4)._2)
    println(AbsInterpreter.run(id4)._2)
  }

}
