package sai.cps.zerocfa

import sai.cps.parser._

/**
  Abstract Compilation of 0CFA
  */
object ACZeroCFA extends CFACommon {
  type CompiledAnalysis = Store ⇒ Store

  def compProgram(prog: Expr): CompiledAnalysis = compCall(prog)

  def compCall(call: Expr): CompiledAnalysis = {
    if (debug) println(s"compCall call[$call]")
    call match {
      case Letrec(bds, body) =>
        val C1 = compCall(body)
        val C2 = compArgs(bds.map(_.value))
        (σ: Store) => C1(C2(σ.update(bds.map(_.name), bds.map(b => Set(b.value.asInstanceOf[Lam])))))
      case App(f, args) =>
        val C1 = compApp(f, args)
        val C2 = compArgs(args)
        (σ: Store) => C1(C2(σ))
    }
  }

  def compApp(f: Expr, args: List[Expr]): CompiledAnalysis = {
    if (debug) println(s"compApp f[$f]")
    f match {
      case Var(x) => (σ: Store) => analysisAbsApp(σ.lookup(x), args, σ)
      case Op(_) => compArgs(args)
      case Lam(vars, body) =>
        val C = compCall(body)
        (σ: Store) => C(σ.update(vars, args.map(primEval(_, σ))))
    }
  }

  def compArgs(args: List[Expr]): CompiledAnalysis = {
    if (debug) println(s"compArg args[$args]")
    args match {
      case Nil => (σ: Store) => σ
      case (arg@Lam(vars, body))::rest =>
        val C1 = compCall(body)
        val C2 = compArgs(rest)
        (σ: Store) => C2(C1(σ))
      case _::rest => compArgs(rest)
    }
  }

  def analyze(compiled: CompiledAnalysis): Store = {
    def iter(σ: Store): Store = {
      val σ_* = compiled(σ)
      if (σ == σ_*) σ else iter(σ_*)
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
