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

trait Semantics {
  // Basic type definitions
  type Ident = String
  type Addr
  type Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  // Binding-time polymorphism
  type R[_]

  // Monadic interface
  // TODO: why not a trait?
  type MonadOps[R[_], M[_], A] = {
    def map[B: Manifest](f: R[A] => R[B]): M[B]
    def flatMap[B: Manifest](f: R[A] => M[B]): M[B]
  }
  type AnsM[T] <: MonadOps[R, AnsM, T]
  type Ans = AnsM[Value]

  // Environment operations
  def ask_env: AnsM[Env]
  def ext_env(ans: Ans)(xα: (String, R[Addr])): Ans

  // Store operations
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def set_store(αv: (R[Addr], R[Value])): AnsM[Unit]

  // allocate addresses
  def alloc(x: String): AnsM[Addr]
  def alloc(σ: R[Store], x: String): R[Addr]

  // Primitive operations
  type EvalFun = Expr => Ans

  def num(i: Int): Ans
  def get(σ: R[Store], ρ: R[Env], x: String): R[Value]
  def br0(test: R[Value], thn: => Ans, els: => Ans): Ans
  def arith(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[Value]): Ans

  // Fixpoint wrapper and top-level interface
  def fix(ev: EvalFun => EvalFun): EvalFun
  type Result
  def run(e: Expr): Result

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
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
      _ <- set_store(α → v)
      rt <- ext_env(ev(e))(x → α)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- br0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield arith(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      v <- ext_env(ev(rhs))(x → α)
      _ <- set_store(α → v)
      rt <- ext_env(ev(e))(x → α)
    } yield rt
  }

  val ρ0: R[Env]
  val σ0: R[Store]
  implicit def mValue: Manifest[Value]
}

trait ConcreteComponents extends Semantics {
  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CloV(lam: Lam, e: Env) extends Value

  type Addr = Int
  type Result = (R[Value], R[Store])

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)
  def mValue: Manifest[Value] = manifest[Value]
}

trait ConcreteSemantics extends ConcreteComponents {
  import ReaderT._
  import StateT._
  import IdM._

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]
  type StoreM[T] = StoreT[IdM, T]

  ////////////////////////////////////////

  type R[T] = T
  type AnsM[T] = ReaderT[StateT[IdM, Store, ?], Env, T]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[StoreM, Env].local(ans)(ρ => ρ + xα)

  // Allocating addresses
  def alloc(σ: Store, x: String) = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def set_store(αv: (Addr, Value)): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + αv))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](IntV(i))
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      _ <- set_store(α → arg)
      rt <- ext_env(ev(e))(x → α)
    } yield rt
  }
  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els
  def arith(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  val ρ0: Env = Map[Ident, Addr]()
  val σ0: Store = Map[Addr, Value]()
  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run
}

@virtualize
trait StagedConcreteSemantics extends SAIDsl with ConcreteComponents with RepMonads {
  import IdM._
  import StateT._
  import ReaderT._

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]
  type StoreM[T] = StoreT[IdM, T]

  ////////////////////////////////////////

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[IdM, Store, ?], Env, T]

  // Code generation
  def emit_ap_clo(fun: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)]
  def emit_compiled_clo(fun: (Rep[Value], Rep[Store]) => Rep[(Value, Store)], λ: Lam, ρ: Rep[Env]): Rep[Value]
  def emit_int_proj(i: Rep[Value]): Rep[Int]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans = ReaderTMonad[StoreM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))

  // Allocating addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] = ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def set_store(αv: (Rep[Addr], Rep[Value])): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + αv))

  // Primitive operations
  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](unchecked[Value]("IntV(", i, ")"))
  def arith(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    val v1n = emit_int_proj(v1)
    val v2n = emit_int_proj(v2)
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }
  def br0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = emit_int_proj(test)
    ask_env.flatMap { ρ =>
      get_store.flatMap { σ =>
        val (v, σ_*): (Rep[Value], Rep[Store]) =
          if (i == 0) thn(ρ)(σ).run else els(ρ)(σ).run
        put_store(σ_*).map(_ => v)
      } }
    //FIXME: (run-main-3f) scala.MatchError: Sym(49) (of class scala.virtualization.lms.internal.Expressions$Sym)
    /*
    for {
      ρ <- ask_env
      σ <- get_store
      val res: Rep[(Value, Store)] = if (i == 0) thn(ρ)(σ) else els(ρ)(σ)
      _ <- put_store(res._2)
    } yield res._1
     */
  }
  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: (Rep[Value], Rep[Store]) => Rep[(Value, Store)] = {
      case (v: Rep[Value], σ: Rep[Store]) =>
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ + (α → v)).run
    }
    emit_compiled_clo(f, λ, ρ)
  }
  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans =
    get_store.flatMap { σ =>
      val (v, σ_*): (Rep[Value], Rep[Store]) = emit_ap_clo(fun, arg, σ)
      put_store(σ_*).map(_ => v)
    }
    /*
    for {
      σ <- get_store
      val res: Rep[(Value, Store)] = emit_ap_clo(fun, arg, σ)
      _ <- put_store(res._2)
    } yield res._1
       */

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()

  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run
}

trait StagedConcreteSemanticsExp extends StagedConcreteSemantics with SAIOpsExp {
  case class IRApClo(f: Exp[Value], arg: Exp[Value], σ: Exp[Store]) extends Def[(Value, Store)]
  case class IRCompiledClo(f: Exp[((Value,Store)) => (Value, Store)], λ: Exp[Lam], ρ: Exp[Env]) extends Def[Value]
  case class IRIntProj(i: Exp[Value]) extends Def[Int]

  override def emit_ap_clo(fun: Exp[Value], arg: Exp[Value], σ: Exp[Store]): Exp[(Value, Store)] =
    reflectEffect(IRApClo(fun, arg, σ))
  override def emit_compiled_clo(f: (Exp[Value], Exp[Store]) => Exp[(Value, Store)],
                                 λ: Lam, ρ: Exp[Env]): Exp[Value] =
    reflectEffect(IRCompiledClo(fun(f), unit(λ), ρ))
  override def emit_int_proj(i: Exp[Value]): Exp[Int] = IRIntProj(i)
}

trait StagedConcreteSemanticsGen extends GenericNestedCodegen {
  val IR: StagedConcreteSemanticsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IRApClo(f, arg, σ) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(arg)}, ${quote(σ)})")
    case IRCompiledClo(f, λ, ρ) =>
      emitValDef(sym, s"CompiledClo(${quote(f)}, ${quote(λ)}, ${quote(ρ)})")
    case IRIntProj(i) =>
      emitValDef(sym, s"${quote(i)}.asInstanceOf[IntV].i")
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

trait StagedConcreteSemanticsDriver extends DslDriver[Unit, Unit] with StagedConcreteSemanticsExp { q =>
  override val codegen = new DslGen
      with ScalaGenMapOps
      with ScalaGenSetOps
      with ScalaGenUncheckedOps
      with SAI_ScalaGenTupleOps
      with SAI_ScalaGenTupledFunctions
      with StagedConcreteSemanticsGen
  {
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
  case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
        """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}

////////////////////////////////////////////////

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

  //////////////////////////////////////////////////

  type R[T] = T
  type AnsM[T] = ReaderT[StateT[ListT[ReaderT[StateT[IdM, Cache, ?], Cache, ?], ?], Store, ?], Env, T]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Addr)): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ans)(ρ => ρ + xα)

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
    rt <- ext_env(ev(e))(x → α)
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

@virtualize
trait StagedAbstractSemantics extends AbstractComponents with RepMonads with RepLattices with SAIDsl {
  import ReaderT._
  import StateT._
  import ListReaderStateM._

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type NdInOutCacheM[T] = ListReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]

  def mCache: Manifest[Cache] = manifest[Cache]

  //////////////////////////////////////////////////

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[ListReaderStateM[Cache, Cache, ?], Store, ?], Env, T]

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
  //FIXME: check v1 && v2 contains Int
  def arith(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = Set[AbsValue](emit_inttop)
  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = {
    lift_nd[AbsValue](fun.toList).flatMap { clo =>
      ask_in_cache.flatMap { in =>
        get_out_cache.flatMap { out =>
          get_store.flatMap { σ =>
            val res: Rep[(List[(Value, Store)], Cache)] = emit_ap_clo(clo, arg, σ, in, out)
            put_out_cache(res._2).flatMap { _ =>
              lift_nd[(Value, Store)](res._1).flatMap { vs =>
                put_store(vs._2).map { _ =>
                  vs._1
                } } } } } } }
  }
  /*
    for {
    clo <- lift_nd[AbsValue](fun.toList)
    in <- ask_in_cache
    out <- get_out_cache
    σ <- get_store
    val res: Rep[(List[(Value, Store)], Cache)] = emit_ap_clo(clo, arg, σ, in, out)
    _ <- put_out_cache(res._2)
    vs <- lift_nd[(Value, Store)](res._1)
    _ <- put_store(vs._2)
  } yield vs._1
   */

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

  def fix_cache: EvalFun = { e =>
    ask_env.flatMap { ρ =>
      get_store.flatMap { σ =>
        ask_in_cache.flatMap { in =>
          get_out_cache.flatMap { out =>
            val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
            val res: Rep[(List[(Value, Store)], Cache)] =
              if (out.contains(cfg)) {
                (repMapToMapOps(out).apply(cfg).toList, out) //FIXME: ambigious implicit
              } else {
                val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
                val m: Ans = for {
                  _ <- put_out_cache(out + (cfg → res_in))
                  v <- eval(fix_select)(e)
                  σ <- get_store
                  _ <- update_out_cache(cfg, (v, σ))
                } yield v
                m(ρ)(σ)(in)(out)
              }
            put_out_cache(res._2).flatMap { _ =>
              lift_nd(res._1).flatMap { vs =>
                put_store(vs._2).map { _ =>
                  vs._1
                } } } } } } } }

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
          (repMapToMapOps(out).apply(cfg).toList, out) //FIXME: out(cfg)
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

trait StagedAbstractSemanticsExp extends StagedAbstractSemantics with SAIOpsExp with AbsEnvExpOpt {
  //TODO: when evaluating, change lam/rho to hash code
  case class IRCompiledClo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
                           rf: Exp[((Value, Store, Cache, Cache)) => (List[(Value, Store)], Cache)],
                           λ: Exp[Lam], ρ: Exp[Env]) extends Def[AbsValue]
  case class IRApClo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store],
                     in: Exp[Cache], out: Exp[Cache]) extends Def[(List[(Value, Store)], Cache)]

  def emit_compiled_clo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
                        λ: Lam, ρ: Exp[Env]) = reflectEffect(IRCompiledClo(f, fun(f), unit(λ), ρ))

  def emit_ap_clo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) = clo match {
    //Note: egar beta-reduction, produces larger residual code
    //case Def(Reflect(CompiledClo(f, rf, λ, ρ), s, d)) => f(arg, σ, in, out) //FIXME: how to remove reflect?
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
      //This fixes code generation for tuples, such as Tuple2MapIntValueValue
      //TODO: merge back to LMS
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

////////////////////////////////////////////////

object MainGeneric {
  def specCon(e: Expr): DslDriver[Unit, Unit] = new StagedConcreteSemanticsDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vs = run(e)
      println(vs)
    }
  }

  def specAbs(e: Expr): DslDriver[Unit, Unit] = new StagedAbstractSemanticsDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vsc = run(e)
      println(vsc._1)
      println(vsc._2.size)
    }
  }

  def testConcrete() = {
    val interpreter = new ConcreteSemantics {}
    val res = interpreter.run(fact5)
    println(res)
  }

  def testStagedConcrete() = {
    val code = specCon(fact5)
    println(code.code)
    code.eval(())
  }

  def testAbstract() = {
    val interpreter = new AbstractSemantics {
      def mCache: Manifest[Cache] = manifest[Cache]
    }
    //val res = interpreter.run(fact5)
    val res = interpreter.run(ifif)
    //res = interpreter.run(simpleif)
    //println(AbsInterpreter.run(fact5))
    println(res._1)
  }

  def testStagedAbstract() = {
    val code = specAbs(fact5)
    println(code.code)
    code.eval(())
  }

  def main(args: Array[String]): Unit = {
    args(0) match {
      case "concrete" => testConcrete()
      case "staged-concrete" => testStagedConcrete()
      case "abstract" => testAbstract()
      case "staged-abstract" => testStagedAbstract()
    }
  }
}
