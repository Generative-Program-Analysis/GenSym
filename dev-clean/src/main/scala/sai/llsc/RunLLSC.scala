package sai.llsc

import sai.lang.llvm.parser.Parser._

case class Config(nSym: Int, useArgv: Boolean, mainFileOpt: String) {
  require(!(nSym > 0 && useArgv))
  def args(implicit d: ValueDefs) =
    if (useArgv) d.mainArgs
    else d.SymV.makeSymVList(nSym)
}

object Config {
  val (o0, o1, o2, o3) = ("O0", "O1", "O2", "O3")

  /* Global compile-time configurations */
  var opt: Boolean = true
  var iteSelect: Boolean = true

  def disableOpt: Unit = opt = false
  def enableOpt: Unit = opt = true

  def symArg(n: Int) = Config(n, false, o3)
  def useArgv = Config(0, true, o3)
  def noArg = Config(0, false, o3)
  def noMainFileOpt = Config(0, true, o0)
  def default = useArgv
}

object RunLLSC {
  implicit class RegexOps(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def main(args: Array[String]): Unit = {
    // TODO: --use-argv?
    val usage = """
    |Usage: llsc <ll-filepath> [--entrance=<string>] [--output=<string>] [--nSym=<int>] [--noOpt] [--engine=<string>]
    |
    |<ll-filepath>           - the input LLVM IR program (.ll)
    |--entrance=<string>     - the entrance function name (default=main)
    |--output=<string>       - the folder name containing generated Makefile/C++ code (default=basename of the input .ll file)
    |--nSym=<int>            - the number of symbolic 32-bit input arguments to `entrance` (default=0)
    |--noOpt                 - disable first-stage optimizations
    |--engine=<string>       - compiler/backend variant (default=ImpCPS)
    |  =ImpCPS               -   generate code in CPS with impure data structures, can run in parallel
    |  =ImpDirect            -   generate code in direct-style with impure data structures, cannot run in parallel
    |  =PureCPS              -   generate code in CPS with pure data structures, can run in parallel
    |  =PureDirect           -   generate code in direct-style with pure data structures, cannot run in parallel
    |--help                  - print this help message
    """

    val options: Map[String, Any] = args.toList.foldLeft(Map[String, Any]()) {
      case (options, r"--entrance=(\w+)$e") => options + ("entrance" -> e)
      case (options, r"--output=(\w+)$o") => options + ("output" -> o)
      case (options, r"--nSym=(\d+)$n") => options + ("nSym" -> n.toInt)
      case (options, r"--noOpt") => options + ("optimize" -> false)
      case (options, r"--engine=([a-zA-Z]+)$e") => options + ("engine" -> e)
      case (options, "--help") => println(usage); sys.exit(0)
      case (options, input) => options + ("input" -> input)
    }
    val filepath = options.getOrElse("input", throw new RuntimeException("No input .ll file")).toString
    val entrance = "@"+options.getOrElse("entrance", "main").toString
    val output = options.getOrElse("output", filepath.split("\\/").last.split("\\.")(0)).toString
    val nSym = options.getOrElse("nSym", 0).asInstanceOf[Int]
    val optimize = options.getOrElse("optimize", true).asInstanceOf[Boolean]
    val engine = options.getOrElse("engine", "ImpCPS").toString

    val llsc = engine match {
      case "ImpCPS" => new ImpCPSLLSC
      case "ImpDirect" => new ImpLLSC
      case "PureCPS" => new PureCPSLLSC
      case "PureDirect" => new PureLLSC
    }
    Config.opt = optimize
    println(s"Running $engine with filepath=$filepath, entrance=$entrance, output=$output, nSym=$nSym, optimize=$optimize")
    llsc.run(parseFile(filepath), output, entrance, Config.symArg(nSym))
  }
}
