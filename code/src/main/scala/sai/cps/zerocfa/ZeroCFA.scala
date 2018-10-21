package sai.cps.zerocfa

import sai.cps.parser._

object SetExtractor {
  def unapplySeq[T](s: Set[T]): Option[Seq[T]] = Some(s.toSeq)
}

case class Store(map: Map[String, Set[Lam]]) {
  def lookup(α: String): Set[Lam] = map.getOrElse(α, Set[Lam]())

  def update(α: String, d: Set[Lam]): Store = {
    val oldd = map.getOrElse(α, Set[Lam]())
    Store(map ++ Map(α → (d ++ oldd)))
  }

  def update(α: String, v: Lam): Store = update(α, Set(v))

  def update(αs: List[String], ds: List[Set[Lam]]): Store =
    (αs zip ds).foldLeft (this) {
      case (σ, (α, v)) => σ.update(α, v)
    }
}

object Store {
  def bot = Set()
  def mtStore = Store(Map())
}

abstract class CFACommon {
  val debug = false
  def primEval(e: Expr, σ: Store): Set[Lam] = e match {
    case Lit(_) => Set()
    case Var(x) => σ.lookup(x)
    case l: Lam => Set(l)
  }

  def analysisAbsApp(fs: Set[Lam], args: List[Expr], σ: Store): Store = {
    if (debug) println(s"analysisAbsApp fs[$fs]")
    if (fs.isEmpty) σ
    else {
      val newArgs = args.map(primEval(_, σ))
      val σ_* = σ.update(fs.head.vars, newArgs)
      analysisAbsApp(fs.tail, args, σ_*)
    }
  }
}

object ZeroCFA extends CFACommon {
  def analyze(prog: Expr): Store = {
    def iter(σ: Store): Store = {
      val σ_* = analysisProgram(prog, σ)
      if (σ == σ_*) σ else iter(σ_*)
    }
    iter(Store.mtStore)
  }

  def analysisProgram(prog: Expr, σ: Store): Store = analysisCall(prog, σ)

  def analysisCall(call: Expr, σ: Store): Store = {
    if (debug) println(s"analysisCall call[$call]")
    call match {
      case Letrec(bds, body) =>
        val σ_* = σ.update(bds.map(_.name), bds.map(b => Set(b.value.asInstanceOf[Lam])))
        val σ_** = analysisArgs(bds.map(_.value), σ_*)
        analysisCall(body, σ_**)
      case App(f, args) => analysisApp(f, args, analysisArgs(args, σ))
    }
  }

  def analysisApp(f: Expr, args: List[Expr], σ: Store): Store = {
    if (debug) println(s"analysisApp f[$f]")
    f match {
      case Var(x) => analysisAbsApp(σ.lookup(x), args, σ)
      case Op(_) => analysisArgs(args, σ)
      case Lam(vars, body) =>
        val newArgs = args.map(primEval(_, σ))
        val σ_* = σ.update(vars, newArgs)
        analysisCall(body, σ_*)
    }
  }

  def analysisArgs(args: List[Expr], σ: Store): Store = {
    if (debug) println(s"analysisArg args[$args]")
    args match {
      case Nil => σ
      case arg::rest =>
        val σ_* = arg match {
          case Lam(vars, body) => analysisCall(body, σ)
          case _ => σ
        }
        analysisArgs(rest, σ_*)
    }
  }
}
