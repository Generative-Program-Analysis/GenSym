package sai.cps.zerocfa

import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

import sai.utils.Utils
import sai.cps.parser._
import sai.common._
import BasicTypes._

/* Some preliminary result on perforamnce:
 100 runs, drop top 10 and bottom 10 runs
 0CFA average time: 167439.025
 0CFA AC average time: 132463.2875
 0CFA Staged (with precompiled) average time: 75982.7375
 54.6% faster than unopt, 42.6% faster than AC
 -----------------------------------------------
 0CFA time - #: 500, Mean: 469726.998, 0/5/25/50/75/95/100: 126457.0/129778.0/162299.0/183759.0/387371.0/1574363.0/3.8219527E7
 0CFA AC time - #: 500, Mean: 163236.018, 0/5/25/50/75/95/100: 104389.0/107165.0/130640.0/146222.0/163850.0/286668.0/907706.0
 0CFA Staged time - #: 500, Mean: 154590.39, 0/5/25/50/75/95/100: 100771.0/102149.0/107035.0/111119.0/122820.0/174272.0/1.6202133E7
 */

trait StagedZeroCFA extends DslExp with LamOpsExp with MapOpsExp with TupledFunctionsRecursiveExp with SetOpsExp {
  type Ans = Rep[Set[Lam]]

  //TODO: The store doesn't have to be staged, the addresses are known at stage-time.
  //      Try Store = Map[Addr, Rep[Set[Lam]]]

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
    def update(α: Rep[Addr], d: Rep[Set[Lam]]): RStore =
      RStore(map ++ Map(α → (d ++ map.getOrElse(α, Set[Lam]()))))
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
    case App(f, args) ⇒ analysisApp(f, args, analysisArgs(args, σ))
    case Letrec(bds, body) ⇒
      val σ_* = σ.update(bds.map(_.name), bds.map(b => Set(lift(b.value.asInstanceOf[Lam]))))
      val σ_** = analysisArgs(bds.map(_.value), σ_*)
      analysisCall(body, σ_**)
  }

  def analysisApp(f: Expr, args: List[Expr], σ: RStore): RStore = f match {
    //case Var(x) => analysisAbsApp(args, σ(x), σ)
    case Var(x) => RStore(analysisAbsApp(args)(σ.map, σ(x)))
    case Op(_) => analysisArgs(args, σ)
    case Lam(vars, body) =>
      val σ_* = σ.update(vars, args.map(primEval(_, σ)))
      analysisCall(body, σ_*)
  }

  //TODO: Refactor analysisAbsApp
  def analysisAbsApp(args: List[Expr]): Rep[((Map[String, Set[Lam]], Set[Lam])) => Map[String, Set[Lam]]] =
    fun { (σ, fs) =>
      if (fs.isEmpty) σ
      else {
        val new_args = args.map(primEval(_, RStore(σ))) //TODO: this is repeative
        val σ_* = RStore(σ).update(fs.head.vars, new_args)
        analysisAbsApp(args)(σ_*.map, fs.tail)
      }
    }

  def analysisArgs(args: List[Expr], σ: RStore): RStore = args match {
    case Nil => σ
    case Lam(vars, body)::rest => analysisArgs(rest, analysisCall(body, σ))
    case _::rest => analysisArgs(rest, σ)
  }

  //Note: staging this loop function improves perforamnce.
  def analysis(prog: Expr): Rep[Map[String, Set[Lam]]] = {
    def iter: Rep[Map[String, Set[Lam]] => Map[String, Set[Lam]]] = fun { store =>
      val newStore = analysisProgram(prog, RStore(store)).map
      if (store == newStore) store else iter(newStore)
    }
    iter(Map[String, Set[Lam]]())
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
      def snippet(unit: Rep[Unit]): Rep[Map[String, Set[Lam]]] = analysis(prog)
      //def snippet(map0: Rep[Map[String, Set[Lam]]]): Rep[Map[String, Set[Lam]]] =
      //  analysisProgram(prog, RStore(map0)).map
    }

  /*
  def specializeAnalysis(prog: Expr): Map[String, Set[Lam]] => Map[String, Set[Lam]] = {
    val code = specialize(prog); code.precompile
    (σ0) => {
      def iter(σ: Map[String, Set[Lam]]): Map[String, Set[Lam]] = {
        val σ_* = code.eval(σ)
        if (σ == σ_*) σ else iter(σ_*)
      }
      iter(σ0)
    }
  }
  */

  def printSpecializedCode(e: Expr) {
    val code = specialize(e)
    println(code.code)
  }
}
