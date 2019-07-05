package sai.monolithic

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

import sai.PCFLang._
import sai.PCFLang.Values._

object EnvInterpreter {
  import ReaderT._
  import StateT._
  import IdM._

  type Ident = String
  type Env = Map[Ident, Value]

  type AnsM[T] = ReaderT[IdM, Env, T]
  type Ans = AnsM[Value]

  def num(i: Int): Ans = ReaderTMonad[IdM, Env].pure[Value](IntV(i))

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def ap_clo(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) =>
      ReaderTMonad[IdM, Env].local(eval(e))(_ => ρ + (x → arg))
  }

  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ReaderTMonad[IdM, Env].ask
    } yield ρ(x)
    case Lam(x, e) => for {
      ρ <- ReaderTMonad[IdM, Env].ask
    } yield CloV(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      ρ <- ReaderTMonad[IdM, Env].ask
      rt <- ReaderTMonad[IdM, Env].local(eval(e))(_ => ρ + (x → v))
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- br0(cnd, eval(e2), eval(e3))
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
  import ReaderT._
  import StateT._
  import IdM._

  type Ident = String
  type Addr = Int
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]

  type StoreM[T] = StoreT[IdM, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value]

  type EvalFun = Expr => Ans

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  // Allocating addresses
  def alloc(σ: Store, x: String) = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + (a → v)))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](IntV(i))
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      _ <- update_store(α, arg)
      rt <- local_env(ev)(e, ρ)
    } yield rt
  }
  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els
  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)
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
      rt <- br0(cnd, ev(e2), ev(e3))
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
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  def run(e: Expr): (Value, Store) = fix(eval)(e)(ρ0)(σ0).run
}

@virtualize
trait StagedCESOps extends SAIDsl with RepMonads {
  import IdM._
  import ReaderT._
  import StateT._

  sealed trait Value
  //case class IntV(i: Int) extends Value
  //case class CloV[Env](lam: Lam, e: Env) extends Value

  type Ident = String
  type Addr = Int

  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type StoreM[T] = StoreT[IdM, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value]

  type EvalFun = Expr => Ans

  // Code generation
  def emit_ap_clo(fun: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)]
  def emit_compiled_clo(fun: (Rep[Value], Rep[Store]) => Rep[(Value, Store)], λ: Lam, ρ: Rep[Env]): Rep[Value]
  def emit_int_proj(i: Rep[Value]): Rep[Int]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(x: Rep[String], a: Rep[Addr]): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Rep[Env]): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  // Allocating addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] = ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def update_store(a: Rep[Addr], v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + (a → v)))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](unchecked[Value]("IntV(", i, ")"))
  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    val v1n = emit_int_proj(v1)
    val v2n = emit_int_proj(v2)
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }
  def br0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = emit_int_proj(test)
    ask_env.flatMap(ρ => get_store.flatMap(σ => {
      val res: Rep[(Value, Store)] = if (i == 0) thn(ρ)(σ).run else els(ρ)(σ).run
      put_store(res._2).map(_ => res._1)
    }))
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
      val res: Rep[(Value, Store)] = emit_ap_clo(fun, arg, σ)
      put_store(res._2).map { _ =>
        res._1
      } }
      /*
    for {
      σ <- get_store
      val res: Rep[(Value, Store)] = emit_ap_clo(fun, arg, σ)
      //val res: Rep[(Value, Store)] = emit_ap_clo(fun, arg, σ)
      _ <- put_store(res._2)
    } yield res._1
       */

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)
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
      rt <- br0(cnd, ev(e2), ev(e3))
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
  }

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()

  def run(e: Expr): Rep[(Value, Store)] = fix(eval)(e)(ρ0)(σ0).run
}

trait StagedCESOpsExp extends StagedCESOps with SAIOpsExp {
  case class IRApClo(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]) extends Def[(Value, Store)]
  case class IRCompiledClo(f: Exp[((Value,Store)) => (Value, Store)], λ: Exp[Lam], ρ: Exp[Env]) extends Def[Value]
  case class IRIntProj(i: Rep[Value]) extends Def[Int]

  override def emit_ap_clo(fun: Exp[Value], arg: Exp[Value], σ: Exp[Store]): Exp[(Value, Store)] =
    reflectEffect(IRApClo(fun, arg, σ))

  override def emit_compiled_clo(f: (Exp[Value], Exp[Store]) => Exp[(Value, Store)], λ: Lam, ρ: Exp[Env]): Exp[Value] =
    reflectEffect(IRCompiledClo(fun(f), unit(λ), ρ))

  override def emit_int_proj(i: Exp[Value]): Exp[Int] = IRIntProj(i)
}

trait StagedCESGen extends GenericNestedCodegen {
  val IR: StagedCESOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IRApClo(f, arg, σ) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(arg)}, ${quote(σ)})")
    case IRCompiledClo(f, λ, ρ) =>
      emitValDef(sym, s"CompiledClo(${quote(f)}, ${quote(λ)}, ${quote(ρ)})")
    case IRIntProj(i) =>
      emitValDef(sym, s"${quote(i)}.asInstanceOf[IntV].i")
    case Struct(tag, elems) =>
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
      with StagedCESGen
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
