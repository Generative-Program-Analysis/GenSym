package sai.evaluation

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
import sai.evaluation.parser._

trait SchemeAnalyzer {
  type MonadOps[R[_], M[_], A] = {
    def map[B: Manifest](f: R[A] => R[B]): M[B]
    def flatMap[B: Manifest](f: R[A] => M[B]): M[B]
  }
  type Ident = String
  type R[_]
  type Addr
  type Value
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type AnsM[T] <: MonadOps[R, AnsM, T]
  type Ans = AnsM[Value]
  type Result
  type EvalFun = Expr => Ans

  // Environment operations
  def ask_env: AnsM[Env]
  def ext_env(ans: Ans)(xα: (String, R[Addr])): Ans
  def local_env(ans: Ans)(ρ: R[Env]): Ans

  // Store operations
  def get_store: AnsM[Store]
  def put_store(σ: R[Store]): AnsM[Unit]
  def set_store(αv: (R[Addr], R[Value])): AnsM[Unit]

  // allocate addresses
  def alloc(x: String): AnsM[Addr]
  def alloc(σ: R[Store], x: String): R[Addr]

  // Primitive operations
  def void: Ans
  def literal(i: Any): Ans
  def get(ρ: R[Env], x: String): R[Addr]
  def get(σ: R[Store], ρ: R[Env], x: String): R[Value]
  def br(test: R[Value], thn: => Ans, els: => Ans): Ans
  def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value]
  def ap_clo(ev: EvalFun)(fun: R[Value], arg: R[List[Value]]): Ans
  def primtives(v: R[Value], args: List[R[Value]]): R[Value]

  // Fixpoint wrapper and top-level interface
  def fix(ev: EvalFun => EvalFun): EvalFun
  def run(e: Expr): Result

  implicit def mValue: Manifest[Value]
  implicit def mAddr: Manifest[Addr]

  def mapM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[List[B]]
  def forM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[B]

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Void() => void
    case Sym(x) => literal(x)
    case CharLit(c) => literal(c)
    case IntLit(i) => literal(i)
    case FloatLit(f) => literal(f)
    case BoolLit(b) => literal(b)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(ev)(Lam(x, e), ρ)
    case Set_!(x, e) => for {
      v <- ev(e)
      ρ <- ask_env
      _ <- set_store(get(ρ, x) → v)
      n <- void
    } yield n
    case Define(x, e) => for {
      α <- alloc(x)
      v <- ext_env(ev(e))(x → α)
      _ <- set_store(α → v)
      n <- void
    } yield n
    case App(e1, args) => for {
      v1 <- ev(e1)
      v2 <- mapM(args)(ev)
      rt <- ap_clo(ev)(v1, v2)
    } yield rt
    case Begin(es) => forM(es)(ev)
    case If(c, t, e) => for {
      cnd <- ev(c)
      rt <- br(cnd, ev(t), ev(e))
    } yield rt
  }
}

trait AbstractComponents extends SchemeAnalyzer {
  trait AbsValue
  case object IntV extends AbsValue
  case object BoolV extends AbsValue
  case object VoidV extends AbsValue
  case object FloatV extends AbsValue
  case object CharV extends AbsValue
  case object EofV extends AbsValue
  case object InputPortV extends AbsValue
  case object OutputPortV extends AbsValue
  case object MtListV extends AbsValue
  case class PrimOpV(op: String) extends AbsValue
  case class ConsV(a: Addr, b: Addr) extends AbsValue
  case class VectorV(vs: List[Addr]) extends AbsValue
  case class CloV(lam: Lam, env: Env) extends AbsValue
  //addr as value?

  trait Addr
  case class ZCFAAddr(x: String) extends Addr {
    override def toString = "ZCFAAddr(\"" + x + "\")"
  }
  case class OCfaAddr(x: String, ctx: Expr) extends Addr

  type Value = Set[AbsValue]
  type Ctx = R[List[Expr]]
}

