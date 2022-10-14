package gensym.lmsx.smt

// Unstaged interfaces and operations

trait UnstagedSMTBase { self: SMTBaseInterface =>
  type BT[+T] = T
}

trait UnstagedSMTBaseOps extends UnstagedSMTBase with SMTBaseInterface {
  /*
  private val varSet = HashSet[String]()
  private val prelude = new StringBuilder()
  private val constraints = new StringBuilder()

  val filename = "constraints.smt2"

  type SATBool = String
  type Model = String

  def lit(b: Boolean): SATBool = b.toString
  def boolVar(x: String): SATBool = {
    varSet += x
    x
  }
  def eq(x: SATBool, y: SATBool): SATBool = s"(= $x $y)"
  def or(x: SATBool, y: SATBool): SATBool = s"(or $x $y)"
  def not(x: SATBool): SATBool = s"(not $x)"
  def and(x: SATBool, y: SATBool): SATBool = s"(and $x $y)"
  def xor(x: SATBool, y: SATBool): SATBool = s"(xor $x $y)"
  def ite(cnd: SATBool, thn: SATBool, els: SATBool): SATBool = s"(ite $cnd $thn $els)"
  def iff(x: SATBool, y: SATBool): SATBool = and(implies(x, y), implies(y, x))
  def implies(x: SATBool, y: SATBool): SATBool = s"(=> $x $y)"

  def push: Unit = constraints ++= "(push)\n"
  def pop: Unit = constraints ++= "(pop)\n"
  def assert(x: String): Unit = constraints ++= s"(assert $x)\n"
  def query(x: String): Int = ???

  private def build(f: PrintStream => Unit): Unit = {
    varSet.foreach { x =>
      prelude ++= s"(declare-const $x Bool)\n"
    }
    val out = new PrintStream(filename)
    out.println(prelude)
    out.println(constraints)
    f(out)
    out.close
  }

  def check: Boolean = {
    build { ps => ps.println("(check-sat)") }
    val output: String = (s"z3 ./${filename}": ProcessBuilder).!!
    output == "sat\n"
  }
  def check(x: String): (Boolean, Boolean) = {
    // TODO: extract the model of `x`
    ???
  }
  def getModel: Model = {
    build { ps =>
      ps.println("(check-sat)")
      ps.println("(get-model)")
    }
    // TODO: properly represent unknown and timeout
    var sat: String = "unknown"
    val model = new StringBuilder()
    val r = (s"z3 ./${filename}": ProcessBuilder)! ProcessLogger { line =>
      line match {
        case "sat" => sat = line
        case "unsat" => sat = line
        case _ if sat == "sat" =>
          model ++= (line ++ "\n")
        case _ =>
      }
    }
    if (sat == "sat") model.toString else sat
  }

  def print_debug: Unit = {
    varSet.foreach { x =>
      prelude ++= s"(declare-const $x Bool)\n"
    }
    println(prelude)
    println(constraints)
  }
   */
}

trait UnstagedSMTBitVecOps extends UnstagedSMTBase with SMTBitVecInterface {

}
