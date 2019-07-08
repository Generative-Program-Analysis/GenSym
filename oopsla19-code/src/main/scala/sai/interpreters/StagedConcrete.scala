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
import PCFLang._

@virtualize
trait StagedConcreteSemantics extends SAIDsl with ConcreteComponents with RepMonads {
  import IdM._
  import StateT._
  import ReaderT._

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]
  type StoreM[T] = StoreT[IdM, T]

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[IdM, Store, ?], Env, T]

  // Code generation
  def emit_ap_clo(fun: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)]
  def emit_compiled_clo(fun: (Rep[Value], Rep[Store]) => Rep[(Value, Store)], λ: Lam, ρ: Rep[Env]): Rep[Value]
  def emit_int_proj(i: Rep[Value]): Rep[Int]

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans =
    ReaderTMonad[StoreM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))
  def local_env(ans: Ans)(ρ: Rep[Env]): Ans = ReaderTMonad[StoreM, Env].local(ans)(_ => ρ)

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
    for {
      ρ <- ask_env
      σ <- get_store
      res <- ReaderT.liftM[StoreM, Env, (Value, Store)](
        StateT.liftM[IdM, Store, (Value, Store)](
          IdM.pure(if (i == 0) thn(ρ)(σ).run else els(ρ)(σ).run)
      ))
      _ <- put_store(res._2)
    } yield res._1
  }
    //FIXME: (run-main-3f) scala.MatchError: Sym(49) (of class scala.virtualization.lms.internal.Expressions$Sym)
    /*
    ask_env.flatMap { ρ =>
      get_store.flatMap { σ =>
        val (v, σ_*): (Rep[Value], Rep[Store]) =
          if (i == 0) thn(ρ)(σ).run else els(ρ)(σ).run
        put_store(σ_*).map(_ => v)
      } }
    for {
      ρ <- ask_env
      σ <- get_store
      val res: Rep[(Value, Store)] = if (i == 0) thn(ρ)(σ) else els(ρ)(σ)
      _ <- put_store(res._2)
    } yield res._1
     */

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
