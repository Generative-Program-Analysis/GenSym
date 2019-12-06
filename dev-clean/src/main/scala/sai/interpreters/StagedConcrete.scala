package sai

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import FunLang._
import sai.lmsx._
import sai.monads._

@virtualize
trait StagedConcreteSemantics extends ConcreteComponents with SAIOps {
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
    val block_node = Wrap[(Value, Store)=>(Value, Store)](Adapter.g.reflect("λ", block))
    Wrap[Value](Adapter.g.reflect("sai-comp-clo", Unwrap(block_node), Unwrap(unit[Lam](λ)), Unwrap(ρ)))
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

  //FIXME: Adapter.g is not initialized, and an null pointer error will be throw if not using lazy
  //FIXME: Seems DCE/CP eliminates one of the definition, as they are the same value
  //       without looking at their types.
  lazy val ρ0: Rep[Env] = Map[String, Addr]()
  lazy val σ0: Rep[Store] = Map[Addr, Value]()

  def run(e: Expr): Result = fix(eval)(e)(ρ0)(σ0).run.unlift
}

trait StagedConcreteGen extends ExtendedScalaCodeGen {
  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "λ", _, _) ⇒ false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case Node(s, "sai-IntV-proj", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "sai-comp-clo", List(bn, λ, ρ), _) =>
      emit("CompiledClo(")
      shallow(bn); emit(", ")
      shallow(λ); emit(", ")
      shallow(ρ); emitln(")")
    case Node(s, "sai-ap-clo", List(f, arg, σ), _) =>
      shallow(f)
      emit(".asInstanceOf[CompiledClo].f(")
      shallow(arg); emit(", ")
      shallow(σ); emitln(")")
    case Node(s, "sai-IntV-proj", List(i), _) =>
      shallow(i); emitln(".asInstanceOf[IntV].i")
    case _ => super.shallow(n)
  }
}

trait StagedConcreteDriver extends SAIDriver[Unit, Unit] with StagedConcreteSemantics { q =>
  override val codegen = new SAICodeGen with StagedConcreteGen {
    val IR: q.type = q
    import IR._

    override def remap(m: Manifest[_]): String = {
      System.out.println(s"We got remap ${m.toString}")
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }

  override val prelude =
"""
  import sai.FunLang._
  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CompiledClo(f: (Value, Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
"""

}

object mainGeneric {
  import FunLang.Examples._

  def specializeConc(e: Expr): SAIDriver[Unit, Unit] = new StagedConcreteDriver {
    @virtualize
    def snippet(u: Rep[Unit]) = {
      val vs = run(e)
      println(vs)
    }
  }

  def testStagedConcrete() = {
    val code = specializeConc(fact5)
    println(code.code)
    code.eval(())
  }

  def main(args: Array[String]) = {
    testStagedConcrete()
  }
}
