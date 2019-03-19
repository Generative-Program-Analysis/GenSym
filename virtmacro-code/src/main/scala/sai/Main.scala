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

object EnvInterpreter {
  import PCFLang._
  import PCFLang.Values._
  import ReaderT._
  import StateT._
  import IdMonadInstance._

  type Ident = String
  type Env = Map[Ident, Value]

  type AnsM[T] = ReaderT[Id, Env, T]
  type Ans = AnsM[Value]

  def num(i: Int): Ans = ReaderTMonad[Id, Env].pure[Value](IntV(i))

  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case ('+, IntV(x), IntV(y)) => IntV(x + y)
    case ('-, IntV(x), IntV(y)) => IntV(x - y)
    case ('*, IntV(x), IntV(y)) => IntV(x * y)
    case ('/, IntV(x), IntV(y)) => IntV(x / y)
  }

  def ap_clo(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) =>
      ReaderTMonad[Id, Env].local(eval(e))(_ => ρ + (x → arg))
  }

  def branch0(test: Value, thn: => Ans, els: => Ans): Ans =
    if (test == IntV(0)) thn else els

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ReaderTMonad[Id, Env].ask
    } yield ρ(x)
    case Lam(x, e) => for {
      ρ <- ReaderTMonad[Id, Env].ask
    } yield CloV(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      ρ <- ReaderTMonad[Id, Env].ask
      rt <- ReaderTMonad[Id, Env].local(eval(e))(_ => ρ + (x → v))
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
  import ReaderT._
  import StateT._
  import IdMonadInstance._

  type Ident = String
  type Addr = Int
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value]

  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)

  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[Id, Store].get)
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[Id, Store].mod(σ => σ + (a → v)))

  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](IntV(i))

  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = fun match {
    case CloV(Lam(x, e), ρ: Env) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      _ <- update_store(α, arg)
      rt <- local_env(ev)(e, ρ)
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
  }

  type EvalFun = Expr => Ans

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)

  val ρ0: Env = Map()
  val σ0: Store = Map()
  def run(e: Expr): (Value, Store) = fix(eval)(e).run(ρ0).run(σ0)
}

trait MonadOps[M[_], A] {
  def flatMap[B](f: A => M[B]): M[B]
  def map[B](f: A => B): M[B]
}

trait Semantics {
  import PCFLang._
  type R[_]
  type Ident = String
  type Addr
  type Env
  type Store
  type Value
  type AnsM[_]
  type Ans = AnsM[Value]
  implicit def m: MonadOps[AnsM, Value]

  type EvalFun = Expr => Ans

  /*
  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } ???
  }
   */

  def ask_env: AnsM[Env]
  def ext_env(x: R[String], a: R[Addr]): AnsM[Env]
  def local_env(ev: EvalFun)(e: Expr, ρ: R[Env]): AnsM[Value]

  def alloc(x: String): AnsM[Addr]
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def update_store(a: R[Addr], v: R[Value]): AnsM[Unit]

  def num(i: Int): Ans
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]

  def prim(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[Value]): Ans
  def branch0(test: R[Value], thn: => Ans, els: => Ans): Ans

  val ρ0: R[Env]
  val σ0: R[Store]

  type Result
  def run(e: Expr): Result
}

@virtualize
trait StagedCESOps extends SAIDsl with SAIMonads {
  import PCFLang._
  import IdMonadInstance._
  import ReaderT._
  import StateT._

  sealed trait Value
  //case class IntV(i: Int) extends Value
  //case class CloV[Env](lam: Lam, e: Env) extends Value

  type Ident = String

  type Addr = Int

  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  type StoreT[F[_], B] = StateT[Id, Store, B]
  type EnvT[F[_], T] = ReaderT[F, Env, T]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Value]

  implicit def m = Monad[AnsM]

  type EvalFun = Expr => Ans

  def fix(ev: EvalFun => EvalFun): EvalFun = e => ev(fix(ev))(e)

  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(x: Rep[String], a: Rep[Addr]): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Rep[Env]): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  def alloc(σ: Rep[Store], x: String): Rep[Addr] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[Id, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] = ReaderT.liftM[StoreM, Env, Unit](StateTMonad[Id, Store].put(σ))
  def update_store(a: Rep[Addr], v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[Id, Store].mod(σ => σ + (a → v)))

  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    val v1n = unchecked(v1, ".asInstanceOf[IntV].i")
    val v2n = unchecked(v2, ".asInstanceOf[IntV].i")
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }

  def branch0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = unchecked[Int](test, ".asInstanceOf[IntV].i")
    ask_env.flatMap(ρ => get_store.flatMap(σ => {
      val res: Rep[(Value, Store)] = if (i == 0) thn(ρ)(σ) else els(ρ)(σ)
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

  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](unchecked[Value]("IntV(", i, ")"))

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: Rep[(Value, Store)] => Rep[(Value, Store)] = {
      case vs: Rep[(Value, Store)] =>
        val v = vs._1; val σ = vs._2
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ + (α → v))
    }
    unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
  }

  def emit_ap_clo(fun: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)]

  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans =
    get_store.flatMap(σ => {
      val res: Rep[(Value, Store)] = emit_ap_clo(fun, arg, σ)
      put_store(res._2).map(_ => res._1)
    })
  /*
    for {
    σ <- get_store
    val res: Rep[(Value, Store)] = ap_clo_aux(fun, arg, σ)
    _ <- put_store(res._2)
    } yield res._1
   */

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
  }

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()

  def run(e: Expr): Rep[(Value, Store)] = fix(eval)(e)(ρ0)(σ0)
}

trait StagedCESOpsExp extends StagedCESOps with SAIOpsExp {
  case class ApClo(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]) extends Def[(Value, Store)]

  def emit_ap_clo(fun: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)] =
    reflectEffect(ApClo(fun, arg, σ))
}

trait StagedCESGen extends GenericNestedCodegen {
  val IR: StagedCESOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ApClo(f, arg, σ) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(arg)}, ${quote(σ)})")
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
  case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
        """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}

object Main {
  import PCFLang._

  def specialize(e: Expr): DslDriver[Unit, Unit] = new StagedCESDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vs = run(e)
      println(vs._1)
      println(vs._2)
    }
  }

  def main(args: Array[String]): Unit = {
    val code = specialize(fact5)
    println(code.code)
    code.eval(())

    val lam = Lam("x", App(Var("x"), Var("x")))
    val omega = App(lam, lam)
  }
}
