package sai.direct.core.ai

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.utils._
import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.core.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOps => _, SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

//type T[A]
//def valueT: T[Value]
//def is_zero(v: R[Value]): R[Boolean]
//def if_then_else[A:T](cnd: R[Boolean], thn: => R[A], els: => R[A]): R[A]

trait Semantics {
  type R[+T]
  type Ident
  type Addr
  type Value
  type Env
  type Store
  type Ans
  val ρ0: Env
  val σ0: Store

  def in_rep: Boolean
  def lift(v: Value): R[Value]
  def alloc(x: R[Ident], σ: Store): R[Addr]

  type EvalFun = (Expr, Env, Store) => Ans
  def fix(ev: EvalFun => EvalFun): EvalFun
  def base_apply(fun: R[Value], arg: R[Value], ρ: Env, σ: Store): Ans
  def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans
  def eval_top(e: Expr): Ans = fix(eval)(e, ρ0, σ0)
  def eval_top(e: Expr, ρ: Env, σ: Store): Ans = fix(eval)(e, ρ, σ)
}

trait EnvOps { self: Semantics =>
  type Env = R[Map[Ident, Addr]]
}

trait StoreOps { self: Semantics =>
  type Store = R[Map[Addr, Value]]
}

trait Concrete { self: Semantics =>
  type Ident = String
  type Addr = Int
  abstract class Value
  case class NumV(i: R[Int]) extends Value
  case class CloV(λ: Lam, ρ: Env) extends Value
  //type Ans = (R[Value], Store)
  case class Ans(v: R[Value], σ: Store)
  def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ) => ev(fix(ev))(e, ρ, σ)
}

trait Staged { self: Semantics with Dsl =>
  type R[+T] = Rep[T]
  def in_rep = true
  def lift(v: Value): Rep[Value] = unit(v)
  implicit def valueTyp: Typ[Value]
}

trait Unstaged { self: Semantics =>
  type R[+T] = T
  def in_rep = false
  def lift(v: Value): Value = v
}

trait NumVOps extends Base with PrimitiveOps { self: StagedConcreteOps =>
  implicit def repToNumVOps(n: Rep[NumV]) = new NumVOpsCls(n)
  class NumVOpsCls(n: Rep[NumV]) {
    def i: Rep[Int] = numv_i(n)
  }
  def numv_i(n: Rep[NumV])(implicit pos: SourceContext): Rep[Int]
}

trait NumVOpsExp extends BaseExp with PrimitiveOpsExp with NumVOps { self: StagedConcreteOps =>
  implicit def NumVTyp: Typ[NumV] = manifestTyp
  case class NumVI(n: Exp[NumV]) extends Def[Int]
  def numv_i(n: Exp[NumV])(implicit pos: SourceContext): Exp[Int] = NumVI(n)
}

trait AnsOps extends Base with Variables with MapOps { self: StagedConcreteOps with StoreOps =>
  implicit def repToAnsOps(ans: Rep[Ans]) = new AnsOpsCls(ans)
  class AnsOpsCls(ans: Rep[Ans]) {
    def v: Rep[Value] = ans_v(ans)
    def σ: Store = ans_store(ans)
  }
  def ans_v(ans: Rep[Ans])(implicit pos: SourceContext): Rep[Value]
  def ans_store(ans: Rep[Ans])(implicit pos: SourceContext): Store
}

trait AnsOpsExp extends BaseExp with VariablesExp with AnsOps with MapOpsExp {
  self: StagedConcreteOps with StoreOps =>
  case class AnsV(ans: Exp[Ans]) extends Def[Value]
  case class AnsStore(ans: Exp[Ans]) extends Def[Map[Addr,Value]]
  def ans_v(ans: Exp[Ans])(implicit pos: SourceContext): Exp[Value] = AnsV(ans)
  def ans_store(ans: Exp[Ans])(implicit pos: SourceContext): Exp[Map[Addr,Value]] = AnsStore(ans)
}

trait StagedConcreteOps extends Semantics with Concrete with Staged //with EnvOps with StoreOps
    with Dsl with MapOps with SetOps with StringOps with UncheckedOps
    with ListOps with TupleOps with TupledFunctions with AnsOps
    with NumVOps with PrimitiveOps { self: EnvOps with StoreOps =>
  def base_apply_rep(f: R[Value], arg: R[Value], ρ: Env, σ: Store): Ans
  def alloc(x: R[Ident], σ: Store): R[Addr] = σ.size + 1
  def base_apply(f: R[Value], arg: R[Value], ρ: Env, σ: Store): Ans =
    base_apply_rep(f, arg, ρ, σ)
  def evalArith(op: Symbol, e1v: Rep[NumV], e2v: Rep[NumV]): Rep[Value] = op match {
    case '+ ⇒ lift(NumV(e1v.i + e2v.i))
    case '- ⇒ lift(NumV(e1v.i - e2v.i))
    case '* ⇒ lift(NumV(e1v.i * e2v.i))
  }
  def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
    case Lit(i) => Ans(lift(NumV(i)), σ)
    case Var(x) => Ans(σ(ρ(x)), σ)
    case Lam(x, e) =>
      /*
      trait Program extends StagedConcreteOps { self: EnvOps with StoreOps =>
        import sai.direct.core.parser._
        def snippet(arg: Rep[Unit]): Rep[Unit] = {
          eval_top(e, ρ.asInstanceOf[Program.this.Env], σ.asInstanceOf[Program.this.Store])
          ()
        }
      }
      val r = new StagedConcreteDriver with Program
      println(r.code)
      */
      Ans(lift(CloV(Lam(x,e), ρ)), σ)
    case App(e1, e2) =>
      val Ans(e1v, e1σ) = ev(e1, ρ, σ)
      val Ans(e2v, e2σ) = ev(e2, ρ, e1σ)
      base_apply(e1v, e2v, ρ, e2σ)
    case Rec(x, f, body) =>
      val α = alloc(x, σ)
      val ρ_* = ρ + (unit(x) → α)
      val Ans(fv, fσ) = ev(f, ρ_*, σ)
      val σ_* = fσ + (α → fv)
      ev(body, ρ_*, σ_*)
    case Let(x, e, body) => ev(App(Lam(x, body), e), ρ, σ)
    case If0(cnd, thn, els) =>
      val Ans(cndv:Rep[NumV], cndσ) = ev(cnd, ρ, σ)
      if (cndv.i == 0) ev(thn, ρ, cndσ)
      else ev(els, ρ, cndσ)
    case AOp(op, e1, e2) =>
      val Ans(e1v:Rep[NumV], e1σ) = ev(e1, ρ, σ)
      val Ans(e2v:Rep[NumV], e2σ) = ev(e2, ρ, e1σ)
      Ans(evalArith(op, e1v, e2v), e2σ)
  }
}

trait StagedConcreteExp extends StagedConcreteOps with Semantics with Concrete
    with Staged with EnvOps with StoreOps
    with Dsl with DslExp with MapOpsExp with SetOpsExp with StringOpsExp with UncheckedOpsExp
    with ListOpsExp with TupleOpsExp with TupledFunctionsRecursiveExp with AnsOpsExp
    with NumVOpsExp with PrimitiveOpsExp {
  implicit def valueTyp: Typ[Value] = manifestTyp
  implicit def ansTyp: Typ[Ans] = manifestTyp
  val ρ0 = Map[Ident, Addr]()
  val σ0 = Map[Addr, Value]()

  case class CompClo(f: (Rep[Value], Store) => Ans) extends Value
  implicit def compCloTyp: Typ[CompClo] = manifestTyp

  case class BaseApplyRep(f: R[Value], arg: R[Value], ρ: Env, σ: Store) extends Def[Ans]
  def base_apply_rep(f: R[Value], arg: R[Value], ρ: Env, σ: Store): Ans = f match {
    case Const(CloV(Lam(x, e), ρ)) =>
      val α = alloc(x, σ)
      eval_top(e, ρ + (unit(x) -> α), σ + (α -> arg))
    case _ =>
      val ans: Rep[Ans] = reflectEffect(BaseApplyRep(f, arg, ρ, σ))
      Ans(ans.v, ans.σ)
  }
}

trait StagedConcreteGen extends GenericNestedCodegen {
  val IR: StagedConcreteExp
  import IR._
  override def quote(x: Exp[Any]) : String = x match {
    case Const(NumV(i)) => s"NumV(${quote(i)})"
    case Const(CloV(λ, ρ)) => "CloV(" + λ + "," + quote(ρ) + ")"
    case _ => super.quote(x)
  }
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case AnsStore(ans) => emitValDef(sym, s"${quote(ans)}.σ")
    case AnsV(ans) => emitValDef(sym, s"${quote(ans)}.v")
    case BaseApplyRep(f, arg, ρ, σ) =>
      emitValDef(sym, "base_apply(" + quote(f) + "," + quote(arg) + "," + quote(ρ) + "," + quote(σ) + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait StagedConcreteDriver extends DslDriver[Unit, Unit] with StagedConcreteExp { q =>
  override val codegen = new DslGen with ScalaGenMapOps with StagedConcreteGen {
    val IR: q.type = q
    override def remap[A](m: Typ[A]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
    override def emitSource[A : Typ](args: List[Sym[_]], body: Block[A],
                                     className: String,
                                     stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      val dump = """
import sai.direct.core.parser._
object RTSupport {
  type Env = Map[String, Int]
  type Store = Map[Int, Value]
  abstract class Value
  case class NumV(i: Int) extends Value
  case class CloV(λ: Lam, ρ: Env) extends Value
  case class Ans(v: Value, σ: Store)
  type Addr = Int
  type Ident = String
  def lift(v: Value) = v
  def alloc(x: Ident, σ: Store): Addr = σ.size + 1
  def base_apply(f: Value, arg: Value, ρ: Env, σ: Store): Ans = f match {
    case CloV(Lam(x, e), ρ) =>
      val α = alloc(x, σ)
      eval_top(e, ρ + (x -> α), σ + (α -> arg))
  }
  def evalArith(op: Symbol, e1v: NumV, e2v: NumV): NumV = op match {
    case '+ ⇒ NumV(e1v.i + e2v.i)
    case '- ⇒ NumV(e1v.i - e2v.i)
    case '* ⇒ NumV(e1v.i * e2v.i)
  }
  def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
    case Lit(i) => Ans(lift(NumV(i)), σ)
    case Var(x) => Ans(σ(ρ(x)), σ)
    case Lam(x, e) => Ans(lift(CloV(Lam(x,e), ρ)), σ)
    case App(e1, e2) =>
      val Ans(e1v, e1σ) = ev(e1, ρ, σ)
      val Ans(e2v, e2σ) = ev(e2, ρ, e1σ)
      base_apply(e1v, e2v, ρ, e2σ)
    case Rec(x, f, body) =>
      val α = alloc(x, σ)
      val ρ_* = ρ + (x → α)
      val Ans(fv, fσ) = ev(f, ρ_*, σ)
      val σ_* = fσ + (α → fv)
      ev(body, ρ_*, σ_*)
    case Let(x, e, body) => ev(App(Lam(x, body), e), ρ, σ)
    case If0(cnd, thn, els) =>
      val Ans(cndv:NumV, cndσ) = ev(cnd, ρ, σ)
      if (cndv.i == 0) ev(thn, ρ, cndσ)
      else ev(els, ρ, cndσ)
    case AOp(op, e1, e2) =>
      val Ans(e1v:NumV, e1σ) = ev(e1, ρ, σ)
      val Ans(e2v:NumV, e2σ) = ev(e2, ρ, e1σ)
      Ans(evalArith(op, e1v, e2v), e2σ)
  }
  type EvalFun = (Expr, Env, Store) => Ans
  def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ) => ev(fix(ev))(e, ρ, σ)
  def eval_top(e: Expr, ρ: Env, σ: Store): Ans = fix(eval)(e, ρ, σ)
}
import RTSupport._
"""
      stream.println(dump)
      super.emitSource(args, body, className, stream)
    }
  }
}

object UnstagedConcrete extends Semantics with Concrete with Unstaged with EnvOps with StoreOps {
  val ρ0 = Map[Ident, Addr]()
  val σ0 = Map[Addr, Value]()
  def evalArith(op: Symbol, e1v: NumV, e2v: NumV): NumV = op match {
    case '+ ⇒ NumV(e1v.i + e2v.i)
    case '- ⇒ NumV(e1v.i - e2v.i)
    case '* ⇒ NumV(e1v.i * e2v.i)
  }
  def alloc(x: Ident, σ: Store): Addr = σ.size + 1
  def base_apply(f: Value, arg: Value, ρ: Env, σ: Store): Ans = f match {
    case CloV(Lam(x, e), ρ) =>
      val α = alloc(x, σ)
      eval_top(e, ρ + (x -> α), σ + (α -> arg))
  }
  def eval(ev: EvalFun)(e: Expr, ρ: Env, σ: Store): Ans = e match {
    case Lit(i) => Ans(lift(NumV(i)), σ)
    case Var(x) => Ans(σ(ρ(x)), σ)
    case Lam(x, e) => Ans(lift(CloV(Lam(x,e), ρ)), σ)
    case App(e1, e2) =>
      val Ans(e1v, e1σ) = ev(e1, ρ, σ)
      val Ans(e2v, e2σ) = ev(e2, ρ, e1σ)
      base_apply(e1v, e2v, ρ, e2σ)
    case Rec(x, f, body) =>
      val α = alloc(x, σ)
      val ρ_* = ρ + (x → α)
      val Ans(fv, fσ) = ev(f, ρ_*, σ)
      val σ_* = fσ + (α → fv)
      ev(body, ρ_*, σ_*)
    case Let(x, e, body) => ev(App(Lam(x, body), e), ρ, σ)
    case If0(cnd, thn, els) =>
      val Ans(cndv: R[NumV], cndσ) = ev(cnd, ρ, σ)
      if (cndv.i == 0) ev(thn, ρ, cndσ)
      else ev(els, ρ, cndσ)
    case AOp(op, e1, e2) =>
      val Ans(e1v: R[NumV], e1σ) = ev(e1, ρ, σ)
      val Ans(e2v: R[NumV], e2σ) = ev(e2, ρ, e1σ)
      Ans(evalArith(op, e1v, e2v), e2σ)
  }

  def main(args: Array[String]) {
    val id = App(Lam("x", Var("x")), Lit(42))
    val id2 = App(Lam("x", Var("x")), Lam("y", Var("y")))
    val id3 = App(Lam("x", App(Var("x"), Var("x"))), Lam("y", Var("y")))
    val id4 = App(Lam("x", App(App(Var("x"), Var("x")), Var("x"))), Lam("y", Var("y")))
    val fact = Lam("n",
                   If0(Var("n"),
                       Lit(1),
                       AOp('*, Var("n"), App(Var("fact"), AOp('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))
    val fact10 = Rec("fact", fact, App(Var("fact"), Lit(10)))

    //println(eval_top(id4))
    println(eval_top(fact5))

    def specialize(p: Expr): DslDriver[Unit, Unit] =
      new StagedConcreteDriver {
        def snippet(unit: Rep[Unit]): Rep[Unit] = {
          val Ans(v, s) = eval_top(p)
          println(v)
          println(s)
        }
      }

    val code = specialize(fact5)
    println(code.code)
  }
}
