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
  type R[_]
  type Ident = String
  type Addr
  type Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]

  implicit def mValue: Manifest[Value]

  type AnsM[T] <: {
    def map[B: Manifest](f: R[T] => R[B]): AnsM[B]
    def flatMap[B: Manifest](f: R[T] => AnsM[B]): AnsM[B]
  }
  type Ans = AnsM[Value]
  type Result

  type EvalFun = Expr => Ans

  // Environment operations
  def ask_env: AnsM[Env]
  def ext_env(x: String, a: R[Addr]): AnsM[Env]
  def local_env(ev: EvalFun)(e: Expr, ρ: R[Env]): AnsM[Value]

  // allocate addresses
  def alloc(x: String): AnsM[Addr]
  def alloc(σ: R[Store], x: String): R[Addr]

  // Store operations
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def update_store(a: R[Addr], v: R[Value]): AnsM[Unit]

  // Primitive operations
  def num(i: Int): Ans
  def get(σ: R[Store], ρ: R[Env], x: String): R[Value]
  def br0(test: R[Value], thn: => Ans, els: => Ans): Ans
  def prim(op: Symbol, v1: R[Value], v2: R[Value]): R[Value]
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[Value]): Ans

  val ρ0: R[Env]
  val σ0: R[Store]
  def fix(ev: EvalFun => EvalFun): EvalFun
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
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  // Allocating addresses
  def alloc(σ: Store, x: String) = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + (a → v)))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](IntV(i))
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
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

  val ρ0: Env = Map[Ident, Addr]()
  val σ0: Store = Map[Addr, Value]()
  def run(e: Expr): (Value, Store) = fix(eval)(e)(ρ0)(σ0).run
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
  def ext_env(x: String, a: Rep[Addr]): AnsM[Env] = for { ρ <- ask_env } yield ρ + (unit(x) → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Rep[Env]): Ans = ReaderTMonad[StoreM, Env].local(ev(e))(_ => ρ)

  // Allocating addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[IdM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] = ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].put(σ))
  def update_store(a: Rep[Addr], v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[IdM, Store].mod(σ => σ + (a → v)))

  // Primitive operations
  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))
  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Value](unchecked[Value]("IntV(", i, ")"))
  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
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

  def run(e: Expr): (Rep[Value], Rep[Store]) = fix(eval)(e)(ρ0)(σ0).run
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
  //implicit cM: Manifest[Cache]
  type Cache
}

trait AbstractSemantics extends AbstractComponents {
  type R[T] = T
  type AnsM[T] = ReaderT[StateT[ListT[ReaderT[StateT[IdM, Cache, ?], Cache, ?], ?], Store, ?], Env, T]
}

trait StagedAbstractSemantics extends AbstractComponents with RepMonads with SAIDsl {
  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[ListT[ReaderT[StateT[IdM, Cache, ?], Cache, ?], ?], Store, ?], Env, T]
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

  def specAbs(e: Expr): DslDriver[Unit, Unit] = ???

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

  def testAbstract() = { ??? }
  def testStagedAbstract() = { ??? }

  def main(args: Array[String]): Unit = {
    args(0) match {
      case "concrete" => testConcrete()
      case "staged-concrete" => testStagedConcrete()
      case "abstract" => testAbstract()
      case "staged-abstract" => testStagedAbstract()
    }
  }
}
