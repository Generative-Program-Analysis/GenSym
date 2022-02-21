package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.utils.Utils.time
import scala.collection.immutable.{List => StaticList}
import scala.collection.mutable.HashMap

import sai.llsc.imp.ImpLLSCEngine
import sai.llsc.imp.ImpCPSLLSCEngine

import sys.process._

abstract class GenericLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
    extends SAISnippet[A, B] with SAIOps { q =>
  import java.io.{File, PrintStream}

  val codegen: GenericLLSCCodeGen
  var extraFlags: String = ""

  // Assuming the working directory only contains subdir "build" or "tests"
  // TODO: remove this to somewhere for utilities
  def createNewDir: Boolean = {
    val codegenFolderFile = new File(codegen.codegenFolder)
    if (!codegenFolderFile.exists()) codegenFolderFile.mkdir
    else {
      val entries = codegenFolderFile.list()
      entries.map(x => {
        if (x == "build" || x == "tests") {
          val build_dir = new File(codegenFolderFile.getPath, x)
          build_dir.list.map(x => new File(build_dir.getPath, x).delete)
          build_dir.delete
        }
        else new File(codegenFolderFile.getPath, x).delete
      })
      codegenFolderFile.delete
      codegenFolderFile.mkdir
    }
  }

  def genSource: Unit = {
    val folderFile = new File(folder)
    if (!folderFile.exists()) folderFile.mkdir
    createNewDir
    val mainStream = new PrintStream(s"$folder/$appName/$appName.cpp")
    val statics = Adapter.emitCommon1(appName, codegen, mainStream)(manifest[A], manifest[B])(x => Unwrap(wrapper(Wrap[A](x))))
    mainStream.close
  }

  def genMakefile: Unit = {
    val out = new PrintStream(s"$folder/$appName/Makefile")
    val curDir = new File(".").getCanonicalPath
    val libraries = codegen.libraryFlags.mkString(" ")
    val includes = codegen.includePaths.map(s"-I $curDir/" + _).mkString(" ")
    val libraryPaths = codegen.libraryPaths.map(s"-L $curDir/" + _).mkString(" ")

    out.println(s"""|BUILD_DIR = build
    |SRC_DIR = .
    |SOURCES = $$(shell find $$(SRC_DIR)/ -name "*.cpp")
    |TARGET = $appName
    |OBJECTS = $$(SOURCES:$$(SRC_DIR)/%.cpp=$$(BUILD_DIR)/%.o)
    |CC = g++ -std=c++17 -O3
    |CXXFLAGS = $includes $extraFlags
    |LDFLAGS = $libraryPaths
    |LDLIBS = $libraries -lpthread
    |
    |default: $$(TARGET)
    |
    |.SECONDEXPANSION:
    |
    |$$(OBJECTS): $$$$(patsubst $$(BUILD_DIR)/%.o,$$(SRC_DIR)/%.cpp,$$$$@)
    |\tmkdir -p $$(@D)
    |\t$$(CC) -c -o $$@ $$< $$(CXXFLAGS)
    |
    |$$(TARGET): $$(OBJECTS)
    |\t$$(CC) -o $$@ $$^ $$(LDFLAGS) $$(LDLIBS)
    |
    |clean:
    |\t@rm $appName 2>/dev/null || true
    |\t@rm build -rf 2>/dev/null || true
    |\t@rm tests -rf 2>/dev/null || true
    |
    |.PHONY: default clean
    |""".stripMargin)
    out.close
  }

  def genAll: Unit = {
    genSource
    genMakefile
  }

  def make(j: Int = 1): Int = {
    Process(s"make -j$j", new File(s"$folder/$appName")).!
  }

  // returns the number of paths, obtained by parsing the output
  def run(opt: String = ""): Int = {
    val cmd = s"./$appName $opt"
    System.out.println(s"running $cmd")
    val ret = Process(cmd, new File(s"$folder/$appName")).!!
    ret.split("\n").last.split(" ").last.toInt
  }
  // returns the number of paths, and the return status of the process
  def runWithStatus(opt: String = ""): (String, Int) = {
    import collection.mutable.ListBuffer
    val cmd = s"./$appName $opt"
    System.out.println(s"running $cmd")
    val output = ListBuffer[String]()
    val ret = Process(cmd, new File(s"$folder/$appName")).run(ProcessLogger(r => output += r, e => output += e)).exitValue
    (output.mkString("\n"), ret)
  }
}

// Using immer data structures for
//   1) internal state/memory representation
//   2) function call argument list
//   3) function return result list
abstract class PureLLSCDriver[A: Manifest, B: Manifest](val m: Module, appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with LLSCEngine { q =>
  val codegen = new PureLLSCCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.funNameMap
    def blockMap: HashMap[Int, String] = q.blockNameMap
  }
}

// Using immer data structures but generating CPS code,
// avoding reifying the returned nondet list.
abstract class PureCPSLLSCDriver[A: Manifest, B: Manifest](val m: Module, appName: String, folder: String = ".")
    extends GenericLLSCDriver[A, B](appName, folder) with PureCPSLLSCEngine { q =>
  val codegen = new PureLLSCCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.funNameMap
    def blockMap: HashMap[Int, String] = q.blockNameMap
  }
}

// Using C++ std containers for internal state/memory representation,
// but still using immer containers for function call argument lists and result lists.
abstract class ImpLLSCDriver[A: Manifest, B: Manifest](val m: Module, appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with ImpLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.funNameMap
    def blockMap: HashMap[Int, String] = q.blockNameMap
  }
}

// Using C++ std containers for internal state/memory representation,
// function call argument lists, and result lists.
// Note the composition with `StdVectorCodeGen`.
abstract class ImpVecLLSCDriver[A: Manifest, B: Manifest](val m: Module, appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with ImpLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen with StdVectorCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.funNameMap
    def blockMap: HashMap[Int, String] = q.blockNameMap
  }
}

// Generting CPS code with C++ containers for internal state/memory representation.
// Function call argument lists and result lists still use immer containers.
abstract class ImpCPSLLSCDriver[A: Manifest, B: Manifest](val m: Module, appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with ImpCPSLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen /* with StdVectorCodeGen */ {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.funNameMap
    def blockMap: HashMap[Int, String] = q.blockNameMap
  }
}

trait LLSC {
  val insName: String
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericLLSCDriver[_, _]
  def runLLSC(m: Module, name: String, fname: String, config: Config = Config(0, true)): Unit = {
    val (_, t) = time {
      val code = newInstance(m, name, fname, config)
      code.genAll
    }
    println(s"[$insName] compiling $name, time $t ms")
  }
}

case class Config(nSym: Int, argv: Boolean /* potentially other compile-time configuration*/) {
  def args(implicit d: ValueDefs) = {
    if (argv) {
      d.mainArgs
    } else {
      d.SymV.makeSymVList(nSym)
    }
  }
}

class PureLLSC extends LLSC {
  val insName = "PureLLSC"
  def newInstance(m: Module, name: String, fname: String, config: Config) =
    new PureLLSCDriver[Int, Unit](m, name, "./llsc_gen") {
      implicit val me: this.type = this
      @virtualize
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args).foreach { s => checkPCToFile(s._1) }
        ()
      }
    }
}

class PureCPSLLSC extends LLSC {
  val insName = "PureCPSLLSC"
  def newInstance(m: Module, name: String, fname: String, config: Config) =
    new PureCPSLLSCDriver[Int, Unit](m, name, "./llsc_gen") {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = config.args
        val k: Rep[Cont] = fun { case sv =>
          checkPCToFile(sv._1); ()
        }
        exec(fname, config.args, k)
      }
    }
}

class PureCPSLLSC_Z3 extends PureCPSLLSC {
  override val insName = "PureCPSLLSC_Z3"
  override def newInstance(m: Module, name: String, fname: String, config: Config) = {
    val llsc = super.newInstance(m, name, fname, config)
    llsc.codegen.registerLibrary("-lz3")
    llsc.extraFlags = "-D USE_TP -D Z3"
    llsc
  }
}

class ImpLLSC extends LLSC {
  val insName = "ImpLLSC"
  def newInstance(m: Module, name: String, fname: String, config: Config) =
    new ImpLLSCDriver[Int, Unit](m, name, "./llsc_gen") {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        val res = exec(fname, config.args)
        res.foreach { s => checkPCToFile(s._1)}
        ()
      }
    }
}

class ImpVecLLSC extends LLSC {
  val insName = "ImpLLSC"
  def newInstance(m: Module, name: String, fname: String, config: Config) =
    new ImpVecLLSCDriver[Int, Unit](m, name, "./llsc_gen") {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        val res = exec(fname, config.args)
        res.foreach { s => checkPCToFile(s._1)}
        ()
      }
    }
}

class ImpCPSLLSC extends LLSC {
  val insName = "ImpCPSLLSC"
  def newInstance(m: Module, name: String, fname: String, config: Config) =
    new ImpCPSLLSCDriver[Int, Unit](m, name, "./llsc_gen") {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        val k: Rep[Cont] = fun { case sv =>
          checkPCToFile(sv._1); ()
        }
        exec(fname, config.args, k)
      }
    }
}

object RunLLSC {
  def main(args: Array[String]): Unit = {
    // TODO: we also need to refactor the command-line arguments of the front-end compiler
    //   --use-argv, generate code for main entrance
    //   --symint="...", specify symbolic integers with width
    // and in the near future:
    //   --mem="...", specify memory models to use
    //   --O0/O1/O2, specify optimizations to use
    val usage = """
    Usage: llsc <.ll-filepath> <app-name> <entrance-fun-name> [n-sym-var]
    """
    if (args.size < 3) {
      println(usage)
    } else {
      val filepath = args(0)
      val appName = args(1)
      val fun = args(2)
      val nSym = if (args.isDefinedAt(3)) args(3).toInt else 0
      val llsc = new PureLLSC
      // TODO: create Config value according to command-line arguments, pass to runLLSC/newInstance
      llsc.runLLSC(parseFile(filepath), appName, fun, Config(nSym, false))
    }

    //runLLSC(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1m", "@f", 20)
    //runLLSC(sai.llvm.Benchmarks.arrayAccess, "arrAccess", "@main")
    //runLLSC(sai.llvm.LLSCExpr.structReturnLong, "structR1", "@main")
    //runLLSC(sai.llvm.Coreutils.echo, "echo", "@main")
    //runLLSC(sai.llvm.LLSCExpr.complexStruct, "complexStruct", "@main")
    //runLLSC(sai.llvm.LLSCExpr.runCommandLine, "runCommandLine", "@main")
    //runLLSC(parseFile("benchmarks/demo_benchmarks/bubblesort.ll"), "bubble", "@main")
  }
}
