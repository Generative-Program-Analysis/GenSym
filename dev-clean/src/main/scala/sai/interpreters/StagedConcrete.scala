package sai

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize

import sai.lmsx._
import sai.monads._
import FunLang._

@virtualize
trait StagedConcreteSemantics extends SAIOps with ConcreteComponents {
  import IdM._
  import StateT._
  import ReaderT._

  type EnvT[F[_], T] = ReaderT[F, Env, T]
  type StoreT[F[_], T] = StateT[F, Store, T]
  type StoreM[T] = StoreT[IdM, T]

  type R[T] = Rep[T]
  type AnsM[T] = ReaderT[StateT[IdM, Store, ?], Env, T]

  // Code generation
  def lift_ap_clo(f: Rep[Value], arg: Rep[Value], σ: Rep[Store]): Rep[(Value, Store)] = 
    Wrap[(Value, Store)](Adapter.g.reflect("sai-ap-clo", Unwrap(f), Unwrap(arg), Unwrap(σ)))
  def lift_compiled_clo(f: (Rep[Value], Rep[Store]) => Rep[(Value, Store)], λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val block = Adapter.g.reify((v, s) => Unwrap(f(Wrap[Value](v), Wrap[Store](s))))
    Wrap[Value](Adapter.g.reflect("sai-comp-clo", block, Unwrap(unit[Lam](λ)), Unwrap(ρ)))
  }
  def lift_int_proj(i: Rep[Value]): Rep[Int] = Wrap[Int](Adapter.g.reflect("sai-IntV-proj", Unwrap(i)))

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
    val v1n = lift_int_proj(v1)
    val v2n = lift_int_proj(v2)
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }

  def lift[T: Manifest](t: Rep[T]): AnsM[T] = 
    ReaderT.liftM[StoreM, Env, T](StateT.liftM[IdM, Store, T](IdM(t)))

  def br0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = lift_int_proj(test)
    for {
      ρ <- ask_env
      σ <- get_store
      res <- lift[(Value, Store)](if (i == 0) thn(ρ)(σ).run else els(ρ)(σ).run)
      _ <- put_store(res._2)
    } yield res._1
  }

  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: (Rep[Value], Rep[Store]) => Rep[(Value, Store)] = {
      case (v: Rep[Value], σ: Rep[Store]) =>
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ + (α → v)).run
    }
    lift_compiled_clo(f, λ, ρ)
  }

  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = for {
    σ  <- get_store
    vs <- lift[(Value, Store)](lift_ap_clo(fun, arg, σ))
    _  <- put_store(vs._2)
  } yield vs._1

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()

  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run.unlift
}

/*
trait StagedConcreteSemanticsExp extends StagedConcreteSemantics with SAIOpsExp {
  case class IRApClo(f: Exp[Value], arg: Exp[Value], σ: Exp[Store]) extends Def[(Value, Store)]
  case class IRCompiledClo(f: Exp[((Value,Store)) => (Value, Store)], λ: Exp[Lam], ρ: Exp[Env]) extends Def[Value]
  case class IRIntProj(i: Exp[Value]) extends Def[Int]

  override def lift_ap_clo(fun: Exp[Value], arg: Exp[Value], σ: Exp[Store]): Exp[(Value, Store)] =
    reflectEffect(IRApClo(fun, arg, σ))
  override def lift_compiled_clo(f: (Exp[Value], Exp[Store]) => Exp[(Value, Store)],
                                 λ: Lam, ρ: Exp[Env]): Exp[Value] =
    reflectEffect(IRCompiledClo(fun(f), unit(λ), ρ))
  override def lift_int_proj(i: Exp[Value]): Exp[Int] = IRIntProj(i)
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
  import sai.FunLang._
  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
        """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}
*/
