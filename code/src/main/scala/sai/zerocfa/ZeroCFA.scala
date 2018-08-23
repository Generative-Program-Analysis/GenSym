package sai.zerocfa

import sai.parser.cps._

abstract class CFACommon {
  val debug = false
  def analysisAbsApp(fs: Set[Lam], args: List[Expr], store: Store): Store = {
    if (debug) println(s"analysisAbsApp fs[$fs]")
    if (fs.isEmpty) store
    else {
      val newArgs = args.map(store.lookup(_))
      val newStore = store.update(fs.head.vars, newArgs)
      analysisAbsApp(fs.tail, args, newStore)
    }
  }
}

object ZeroCFA extends CFACommon {
  def analyze(prog: Expr): Store = {
    def iter(store: Store): Store = {
      val newStore = analysisProgram(prog, store)
      if (store == newStore) store else iter(newStore)
    }
    iter(Store.mtStore)
  }

  def analysisProgram(prog: Expr, store: Store): Store = analysisCall(prog, store)

  def analysisCall(call: Expr, store: Store): Store = {
    if (debug) println(s"analysisCall call[$call]")
    call match {
      case Letrec(bds, body) =>
        val newStore = store.update(bds.map(_.name), bds.map((b: Bind) => Set(b.value.asInstanceOf[Lam])))
        val newNewStore = analysisArgs(bds.map(_.value), newStore)
        analysisCall(body, newNewStore)
      case App(f, args) => analysisApp(f, args, analysisArgs(args, store))
    }
  }

  def analysisApp(f: Expr, args: List[Expr], store: Store): Store = {
    if (debug) println(s"analysisApp f[$f]")
    f match {
      case Var(name) => analysisAbsApp(store.lookup(name), args, store)
      case Op(_) => analysisArgs(args, store)
      case Lam(vars, body) => 
        val newArgs = args.map(store.lookup(_))
        val newStore = store.update(vars, newArgs)
        analysisCall(body, newStore)
    }
  }

  def analysisArgs(args: List[Expr], store: Store): Store = {
    if (debug) println(s"analysisArg args[$args]")
    args match {
      case Nil => store
      case arg::rest =>
        val newStore = arg match {
          case Lam(vars, body) => analysisCall(body, store)
          case _ => store
        }
        analysisArgs(rest, newStore)
    }
  }
}

