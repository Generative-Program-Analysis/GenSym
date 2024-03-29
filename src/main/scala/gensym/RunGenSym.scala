package gensym

import gensym.llvm.parser.Parser._

object SwitchType extends Enumeration {
  type SwitchType = Value
  val NonMerge, Merge = Value
  def fromString(s: String): SwitchType = s match {
    case "merge" => Merge
    case "nonMerge" => NonMerge
  }
}
import SwitchType._

case class Config(
  nSym: Int, 
  useArgv: Boolean, 
  mainFileOpt: String,
  var opt: Boolean = true,
  var iteSelect: Boolean = true,
  var genDebug: Boolean = false,
  var emitVarIdMap: Boolean = true,
  var emitBlockIdMap: Boolean = true,
  var switchType: SwitchType = Merge,
  var runCode: Boolean = true,
  var recordInstNum: Boolean = false,
  var symbolicUninit: Boolean = false,
) {
  require(!(nSym > 0 && useArgv))
  def args(implicit d: ValueDefs) =
    if (useArgv) d.mainArgs
    else d.SymV.makeSymVList(nSym)

  def disableOpt: Unit = opt = false
  def enableOpt: Unit = opt = true

  def disableRunCode: Unit = runCode = false
  def enableRunCode: Unit = runCode = true
}

object Config {
  val (o0, o1, o2, o3) = ("O0", "O1", "O2", "O3")
  def symArg(n: Int) = Config(n, false, o3)
  def useArgv = Config(0, true, o3)
  def noArg = Config(0, false, o3)
  def noMainFileOpt = Config(0, true, o0)
  def default = useArgv
}

object Global {
  /* Global compile-time configurations */
  var config = Config.default
}

object RunGenSym {
  implicit class RegexOps(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def main(args: Array[String]): Unit = {
    val usage = """
    |Usage: gensym <ll-filepath> [--entrance=<string>] [--output=<string>] [--nSym=<int>]
    |              [--use-argv] [--noOpt] [--engine=<string>] [--main-O0]
    |
    |<ll-filepath>           - the input LLVM IR program (.ll)
    |--entrance=<string>     - the entrance function name (default=main)
    |--output=<string>       - the folder name containing generated Makefile/C++ code (default=basename of the input .ll file)
    |--nSym=<int>            - the number of symbolic 32-bit input arguments to `entrance`, cannot be used with --use-argv (default=0)
    |--use-argv              - take argv argument at runtime, cannot be used with --nSym
    |--noOpt                 - disable first-stage optimizations
    |--symbolicUninit        - model uninitialized variables as symbolic values instead of a random one
    |--randomUninit          - model uninitialized variables as a random value instead of a symbolic one
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
      case (options, r"--symbolicUninit") => options + ("symbolicUninit" -> true)
      case (options, r"--randomUninit") => options + ("symbolicUninit" -> false)
      case (options, r"--engine=([a-zA-Z]+)$e") => options + ("engine" -> e)
      case (options, r"--main-opt=O(\d)$n") => options + ("mainOpt" -> ("O"+n))
      case (options, r"emit-block-id-map") => options + ("blockIdMap" -> true)
      case (options, r"emit-var-id-map") => options + ("varIdMap" -> true)
      case (options, r"--switch-type=(\w+)$t") => options + ("switchType" -> SwitchType.fromString(t))
      case (options, r"--lib=([-_A-Za-z0-9\/\.]+)$p") => options + ("lib" -> p)
      case (options, "--help") => println(usage.stripMargin); sys.exit(0)
      case (options, input) => options + ("input" -> input)
    }
    val filepath = options.getOrElse("input", throw new RuntimeException("No input .ll file")).toString
    val entrance = "@"+options.getOrElse("entrance", "main").toString
    val output = options.getOrElse("output", filepath.split("\\/").last.split("\\.")(0)).toString
    val nSym = options.getOrElse("nSym", 0).asInstanceOf[Int]
    val useArgv = options.getOrElse("useArgv", false).asInstanceOf[Boolean]
    val optimize = options.getOrElse("optimize", Global.config.opt).asInstanceOf[Boolean]
    val symbolicUninit = options.getOrElse("symbolicUninit", Global.config.symbolicUninit).asInstanceOf[Boolean]
    val engine = options.getOrElse("engine", "ImpCPS").toString
    val mainOpt = options.getOrElse("mainOpt", Config.o0).toString
    val emitBlockIdMap = options.getOrElse("blockIdMap", Global.config.emitBlockIdMap).asInstanceOf[Boolean]
    val emitVarIdMap = options.getOrElse("varIdMap", Global.config.emitVarIdMap).asInstanceOf[Boolean]
    val switchType = options.getOrElse("switchType", SwitchType.NonMerge).asInstanceOf[SwitchType]
    val libPath = options.get("lib").asInstanceOf[Option[String]]

    val gensym = engine match {
      case "ImpCPS" => new ImpCPSGS
      case "ImpDirect" => new ImpGS
      case "PureCPS" => new PureCPSGS
      case "PureDirect" => new PureGS
      case "lib" => new ImpCPSGS_lib
      case "app" => new ImpCPSGS_app
    }
    Global.config.opt = optimize
    Global.config.emitBlockIdMap = emitBlockIdMap
    Global.config.emitVarIdMap = emitVarIdMap
    Global.config.switchType = switchType
    Global.config.symbolicUninit = symbolicUninit
    val info = s"""Running $engine with
    |  filepath=$filepath, entrance=$entrance, output=$output,
    |  nSym=$nSym, useArgv=$useArgv, optimize=$optimize, mainOpt=$mainOpt, switchType=$switchType"""
    println(info.stripMargin)
    gensym.run(parseFile(filepath), output, entrance, Config(nSym, useArgv, mainOpt), libPath)
  }
}
