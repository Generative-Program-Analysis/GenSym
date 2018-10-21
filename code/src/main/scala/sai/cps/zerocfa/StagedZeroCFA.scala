package sai.cps.zerocfa

import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, _}

import sai.utils.Utils
import sai.cps.parser._
import sai.common._
import BasicTypes._

trait StagedZeroCFA extends DslExp with LamOpsExp with MapOpsExp with TupledFunctionsRecursiveExp with SetOpsExp {
  type Ans = Rep[Set[Lam]]

  //TODO: The store doesn't have to be staged, the addresses are known at stage-time.
  //      Try Store = Map[Addr, Rep[Set[Lam]]]

  //implicit def StoreTyp: Typ[Store] = manifestTyp

  implicit def lift[T:Typ](lst: List[T]): Rep[List[T]] = List[T](lst.map(lift(_)):_*)
  def lift[T:Typ](x: T) = unit[T](x)

  def primEval(e: Expr, σ: RStore): Ans = e match {
    case Lit(_) ⇒ Set[Lam]()
    case Var(x) ⇒ σ(x)
    case l: Lam ⇒ Set[Lam](lift(l))
  }

  case class RStore(map: Rep[Cache]) {
    implicit def CacheTyp: Typ[Rep[Cache]] = manifestTyp
    def apply(α: Addr): Ans = map.getOrElse(α, Set[Lam]())
    def update(α: Rep[Addr], d: Rep[Set[Lam]]): RStore = RStore(map ++ Map(α → (d ++ map(α))))
    def update(αs: Rep[List[Addr]], ds: List[Rep[Set[Lam]]]): RStore = {
      //if (addrs.isEmpty && ds.isEmpty) { map }
      //TODO: addrs is type Rep, if uses addrs.isEmpty we need to stage the function
      if (ds.isEmpty) this
      else update(αs.head, ds.head).update(αs.tail, ds.tail)
    }
  }

  /* TODO: specialized Map
  case class StagedMap[K,V](size: Int) {
    private var map = new Array[V](size)
    def apply(kv: (K, V)): StagedMap = ???
    def getOrElse(key: K, dft: V): V = ???
    def ++(m: StagedMap[K,V]): StagedMap = ???
  }

  case class RStore(map: Map[Addr, Set[Rep[Lam]]]) {
    def apply(α: Addr): Set[Rep[Lam]] = map.getOrElse(α, Set[Rep[Lam]]())
    def update(α: Addr, d: Set[Rep[Lam]]): RStore =
      RStore(map ++ Map(α → (d ++ this(α))))
    def update(αs: List[Addr], ds: List[Set[Rep[Lam]]]): RStore = {
      if (αs.isEmpty && ds.isEmpty) this
      else update(αs.head, ds.head).update(α.tail, ds.tail)
    }
  }
   def analysisAbsApp(args: List[Expr], fs: Set[Rep[Lam]], σ: RStore): RStore = {
   if (fs.isEmpty) σ
   else {
   val new_args = args.map(primEval(_, σ))
   val σ_* = σ.update(fs.head.vars, new_args)
   analysisAbsApp(args, fs.tail, σ_*)
   }
   }
  */

  def analysisProgram(prog: Expr, σ: RStore): RStore = analysisCall(prog, σ)

  def analysisCall(call: Expr, σ: RStore): RStore = call match {
    case App(f, args) ⇒ analysisApp(f, args, analysisArg(args, σ))
    case Letrec(bds, body) ⇒
      val σ_* = σ.update(bds.map(_.name), bds.map(b => Set(lift(b.value.asInstanceOf[Lam]))))
      val σ_** = analysisArg(bds.map(_.value), σ_*)
      analysisCall(body, σ_**)
  }

  def analysisApp(f: Expr, args: List[Expr], σ: RStore): RStore = f match {
    case Var(x) => analysisAbsApp(args, σ(x), σ)
    case Op(_) => analysisArg(args, σ)
    case Lam(vars, body) =>
      val new_args = args.map(primEval(_, σ))
      val σ_* = σ.update(vars, new_args)
      analysisCall(body, σ_*)
  }

  def analysisAbsApp(args: List[Expr], fs: Rep[Set[Lam]], σ: RStore): RStore =
    //if (fs.isEmpty) σ
    if (true) σ
    else {
      val new_args = args.map(primEval(_, σ))
      val σ_* = σ.update(fs.head.vars, new_args)
      analysisAbsApp(args, fs.tail, σ_*)
    }

  def analysisArg(args: List[Expr], σ: RStore): RStore = args match {
    case Nil => σ
    case arg::rest =>
      val σ_* = arg match {
        case Lam(vars, body) => analysisCall(body, σ)
        case _ => σ
      }
      analysisArg(rest, σ_*)
  }

  def analysis(prog: Expr): RStore = {
    //TODO: Should not stage
    def iter(σ: RStore): RStore = {
      val σ_* = analysisProgram(prog, σ)
      if (σ == σ_*) σ else iter(σ_*)
    }
    iter(RStore(Map[String, Set[Lam]]()))
  }
}

abstract class StagedZeroCFADriver extends DslDriver[Unit, Map[String, Set[Lam]]] with StagedZeroCFA { q =>
  override val codegen = new DslGen with ScalaGenLamOps with ScalaGenSetOps with
      ScalaGenMapOps with MyScalaGenTupledFunctions with ScalaGenListOps {
    val IR: q.type = q
  }
}

object StagedZeroCFATest extends TutorialFunSuite {
  val under = "not applicable"

  def specialize(prog: Expr): DslDriver[Unit, Map[String, Set[Lam]]] =
    new StagedZeroCFADriver {
      def snippet(unit: Rep[Unit]): Rep[Map[String, Set[Lam]]] = analysis(prog).map
    }

  def printSpecializedCode(e: Expr) {
    val code = specialize(e)
    println(code.code)
  }
}
