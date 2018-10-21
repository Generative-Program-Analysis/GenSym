package sai.cps.zerocfa

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

import scala.collection.immutable.{Set => ImmSet}

import sai.utils.Utils
import sai.cps.parser._
import sai.common._

trait StagedZeroCFA extends DslExp with LamOpsExp with MapOpsExp with TupledFunctionsRecursiveExp with ImmSetOpsExp {
  type Addr = String
  type MapT = Map[Addr, ImmSet[Lam]]

  type Ans = Rep[ImmSet[Lam]]
  type Store = Rep[Map[Addr, ImmSet[Lam]]]
  //TODO: The store doesn't have to be staged, the addresses are known at stage-time.
  //      Try Store = Map[Addr, Rep[ImmSet[Lam]]]

  implicit def StoreTyp: Typ[Store] = manifestTyp

  //def lift[T:Typ](lst: List[T]): Rep[List[T]] = List[T](lst.map(lift(_)):_*)

  def lift[T:Typ](x: T) = unit[T](x)

  def primEval(e: Expr, σ: Store): Ans = e match {
    case Lit(_) ⇒ ImmSet[Lam]()
    case Var(x) ⇒ lookup(x, σ)
    case l: Lam ⇒ ImmSet[Lam](lift(l))
  }

  def lookup(α: Addr, σ: Store): Ans = σ.getOrElse(α, ImmSet[Lam]())
  def update(σ: Store, α: Rep[Addr], d: Rep[ImmSet[Lam]]): Store = {
    val oldd = σ.getOrElse(α, ImmSet[Lam]())
    σ ++ Map(α → (d ++ oldd))
  }
  def update(σ: Store, αs: Rep[List[Addr]], ds: List[Rep[ImmSet[Lam]]]): Store = {
    //if (addrs.isEmpty && ds.isEmpty) { map }
    //TODO: addrs is type Rep, if uses addrs.isEmpty we need to stage the function
    if (ds.isEmpty) σ
    else update(update(σ, αs.head, ds.head), αs.tail, ds.tail)
  }

  def analysisProgram(prog: Expr, σ: Store): Store = analysisCall(prog, σ)

  def analysisCall(call: Expr, σ: Store): Store = call match {
    case App(f, args) ⇒ analysisApp(f, args, analysisArg(args, σ))
    case Letrec(bds, body) ⇒
      val σ_* = update(σ, lift(bds.map(_.name)), bds.map((b) => ImmSet(lift(b.value.asInstanceOf[Lam]))))
      val σ_** = analysisArg(bds.map(_.value), σ_*)
      analysisCall(body, σ_**)
  }

  def analysisApp(f: Expr, args: List[Expr], σ: Store): Store = {
    f match {
      case Var(x) => analysisAbsApp(args)(lookup(x, σ), σ)
      case Op(_) => analysisArg(args, σ)
      case Lam(vars, body) =>
        val new_args = args.map(primEval(_, σ))
        val σ_* = update(σ, lift(vars), new_args)
        analysisCall(body, σ_*)
    }
  }

  def analysisAbsApp(args: List[Expr]): Rep[((ImmSet[Lam], MapT)) => MapT] =
    fun { (fs: Rep[ImmSet[Lam]], σ: Store) ⇒
      if (fs.isEmpty) σ
      else {
        val f = fs.head
        val rest = fs.tail
        val new_args = args.map(primEval(_, σ))
        val σ_* = update(σ, f.vars, new_args)
        analysisAbsApp(args)(rest, σ_*)
      }
    }

  def analysisArg(args: List[Expr], σ: Store): Store = args match {
    case Nil => σ
    case arg::rest =>
      val σ_* : Store = arg match {
        case Lam(vars, body) => analysisCall(body, σ)
        case _ => σ
      }
      analysisArg(rest, σ_*)
  }

  def analysis(prog: Expr): Store = {
    def iter: Rep[MapT => MapT] = fun { σ =>
      val σ_* = analysisProgram(prog, σ)
      if (σ == σ_*) σ else iter(σ_*)
    }
    iter(Map[String, ImmSet[Lam]]())
  }
}

abstract class StagedZeroCFADriver extends DslDriver[Unit, Map[String, ImmSet[Lam]]] with StagedZeroCFA { q =>
  override val codegen = new DslGen with ScalaGenLamOps with ScalaGenImmSetOps with
      ScalaGenMapOps with MyScalaGenTupledFunctions with ScalaGenListOps {
    val IR: q.type = q
  }
}

object StagedZeroCFATest extends TutorialFunSuite {
  val under = "not applicable"

  def specialize(prog: Expr): DslDriver[Unit, Map[String, ImmSet[Lam]]] =
    new StagedZeroCFADriver {
      def snippet(unit: Rep[Unit]): Rep[Map[String, ImmSet[Lam]]] = analysis(prog)
    }

  def printSpecializedCode(e: Expr) {
    val code = specialize(e)
    println(code.code)
  }
}
