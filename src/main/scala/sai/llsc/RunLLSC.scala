package sai.llsc

import sai.lang.llvm.parser.Parser._

case class Config(nSym: Int, useArgv: Boolean, mainFileOpt: String) {
  require(!(nSym > 0 && useArgv))
  def args(implicit d: ValueDefs) =
    if (useArgv) d.mainArgs
    else d.SymV.makeSymVList(nSym)
}

object SwitchType extends Enumeration {
  type SwitchType = Value
  val NonMerge, Merge = Value
  def fromString(s: String): SwitchType = s match {
    case "merge" => Merge
    case "nonMerge" => NonMerge
  }
}
import SwitchType._

object Config {
  val (o0, o1, o2, o3) = ("O0", "O1", "O2", "O3")

  /* Global compile-time configurations */
  var opt: Boolean = true
  var iteSelect: Boolean = true
  var genDebug: Boolean = true
  var emitVarIdMap: Boolean = true
  var emitBlockIdMap: Boolean = true
  var switchType: SwitchType = Merge
  var runCode: Boolean = true
  var recordInstNum: Boolean = false

  def disableOpt: Unit = opt = false
  def enableOpt: Unit = opt = true

  def disableRunCode: Unit = runCode = false
  def enableRunCode: Unit = runCode = true

  def symArg(n: Int) = Config(n, false, o3)
  def useArgv = Config(0, true, o3)
  def noArg = Config(0, false, o3)
  def noMainFileOpt = Config(0, true, o0)
  def default = noMainFileOpt
}

object RunLLSC {
  implicit class RegexOps(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def main(args: Array[String]): Unit = {
    val usage = """
    |Usage: llsc <ll-filepath> [--entrance=<string>] [--output=<string>] [--nSym=<int>]
    |            [--use-argv] [--noOpt] [--engine=<string>] [--main-O0]
    |
    |<ll-filepath>           - the input LLVM IR program (.ll)
    |--entrance=<string>     - the entrance function name (default=main)
    |--output=<string>       - the folder name containing generated Makefile/C++ code (default=basename of the input .ll file)
    |--nSym=<int>            - the number of symbolic 32-bit input arguments to `entrance`, cannot be used with --use-argv (default=0)
    |--use-argv              - take argv argument at runtime, cannot be used with --nSym
    |--noOpt                 - disable first-stage optimizations
    |--engine=<string>       - compiler/backend variant (default=ImpCPS)
    |  =ImpCPS               -   generate code in CPS with impure data structures, can run in parallel
    |  =ImpDirect            -   generate code in direct-style with impure data structures, cannot run in parallel
    |  =PureCPS              -   generate code in CPS with pure data structures, can run in parallel
    |  =PureDirect           -   generate code in direct-style with pure data structures, cannot run in parallel
    |--main-opt=<string>     - g++ optimization level when compiling the main file containing the initial heap object
    |--emit-block-id-map     - emit a map from block names to id in common.h
    |--emit-var-id-map       - emit a map from variable names to id in common.h
    |--switch-type=<string>  - compilation variants of `switch` statement (default=nonMerge)
    |  =merge                - only fork `m` paths of distinct targets
    |  =nonMerge             - fork `n` paths where `n` is the total number of feasible cases (including default)
    |--help                  - print this help message
    """

    val options: Map[String, Any] = args.toList.foldLeft(Map[String, Any]()) {
      case (options, r"--entrance=(\w+)$e") => options + ("entrance" -> e)
      case (options, r"--output=(\w+)$o") => options + ("output" -> o)
      case (options, r"--nSym=(\d+)$n") => options + ("nSym" -> n.toInt)
      case (options, r"--use-argv") => options + ("useArgv" -> true)
      case (options, r"--noOpt") => options + ("optimize" -> false)
      case (options, r"--engine=([a-zA-Z]+)$e") => options + ("engine" -> e)
      case (options, r"--main-opt=O(\d)$n") => options + ("mainOpt" -> ("O"+n))
      case (options, r"emit-block-id-map") => options + ("blockIdMap" -> true)
      case (options, r"emit-var-id-map") => options + ("varIdMap" -> true)
      case (options, r"--switch-type=(\w+)$t") => options + ("switchType" -> SwitchType.fromString(t))
      case (options, "--help") => println(usage.stripMargin); sys.exit(0)
      case (options, input) => options + ("input" -> input)
    }
    val filepath = options.getOrElse("input", throw new RuntimeException("No input .ll file")).toString
    val entrance = "@"+options.getOrElse("entrance", "main").toString
    val output = options.getOrElse("output", filepath.split("\\/").last.split("\\.")(0)).toString
    val nSym = options.getOrElse("nSym", 0).asInstanceOf[Int]
    val useArgv = options.getOrElse("useArgv", false).asInstanceOf[Boolean]
    val optimize = options.getOrElse("optimize", Config.opt).asInstanceOf[Boolean]
    val engine = options.getOrElse("engine", "ImpCPS").toString
    val mainOpt = options.getOrElse("mainOpt", Config.o0).toString
    val emitBlockIdMap = options.getOrElse("blockIdMap", Config.emitBlockIdMap).asInstanceOf[Boolean]
    val emitVarIdMap = options.getOrElse("varIdMap", Config.emitVarIdMap).asInstanceOf[Boolean]
    val switchType = options.getOrElse("switchType", SwitchType.NonMerge).asInstanceOf[SwitchType]

    val llsc = engine match {
      case "ImpCPS" => new ImpCPSLLSC
      case "ImpDirect" => new ImpLLSC
      case "PureCPS" => new PureCPSLLSC
      case "PureDirect" => new PureLLSC
    }
    Config.opt = optimize
    Config.emitBlockIdMap = emitBlockIdMap
    Config.emitVarIdMap = emitVarIdMap
    Config.switchType = switchType
    val info = s"""Running $engine with
    |  filepath=$filepath, entrance=$entrance, output=$output,
    |  nSym=$nSym, useArgv=$useArgv, optimize=$optimize, mainOpt=$mainOpt, switchType=$switchType"""
    println(info.stripMargin)
    llsc.run(parseFile(filepath), output, entrance, Config(nSym, useArgv, mainOpt))
  }
}
