package sai.zerocfa

import sai.parser.cps._

/**
  Abstract Compilation of 0CFA
  */
object ACZeroCFA extends CFACommon {
  type CompiledAnalysis = Store => Store

  def compProgram(prog: Expr): CompiledAnalysis = compCall(prog)

  def compCall(call: Expr): CompiledAnalysis = {
    if (debug) println(s"compCall call[$call]")
    call match {
      case Letrec(bds, body) =>
        val C1 = compCall(body)
        val C2 = compArgs(bds.map(_.value))
        (s: Store) => C1(C2(s.update(bds.map(_.name), bds.map((b: Bind) => Set(b.value.asInstanceOf[Lam])))))
      case App(f, args) =>
        val C1 = compApp(f, args)
        val C2 = compArgs(args)
        (s: Store) => C1(C2(s))
    }
  }

  def compApp(f: Expr, args: List[Expr]): CompiledAnalysis = {
    if (debug) println(s"compApp f[$f]")
    f match {
      case Var(name) => (s: Store) => analysisAbsApp(s.lookup(name), args, s)
      case Op(_) => compArgs(args)
      case Lam(vars, body) =>
        val C = compCall(body)
        (s: Store) => C(s.update(vars, args.map(s.lookup(_))))
    }
  }

  def compArgs(args: List[Expr]): CompiledAnalysis = {
    if (debug) println(s"compArg args[$args]")
    args match {
      case Nil => (s: Store) => s
      case (arg@Lam(vars, body))::rest =>
        val C1 = compCall(body)
        val C2 = compArgs(rest)
        (s: Store) => C2(C1(s))
      case _::rest =>
        compArgs(rest)
    }
  }

  def analyze(compiled: CompiledAnalysis): Store = {
    def iter(store: Store): Store = {
      val newStore = compiled(store)
      if (store == newStore) store else iter(newStore)
    }
    iter(Store.mtStore)
  }

  def compileAndAnalyze(prog: Expr): Store = {
    val compiled = compProgram(prog)
    analyze(compiled)
  }

  def test(prog: Expr, name: String, printResult: Boolean) {
    println(s"Testing $name:")
    val s1 = ZeroCFA.analyze(prog)
    val s2 = ACZeroCFA.compileAndAnalyze(prog)
    if (s1 == s2) {
      println(s"Checked $name")
      if (printResult) println(s1)
    }
    else {
      println("Analysis result is different!")
      println(s"baseline 0CFA: $s1")
      println(s"AC 0CFA:       $s2")
    }
  }
}
