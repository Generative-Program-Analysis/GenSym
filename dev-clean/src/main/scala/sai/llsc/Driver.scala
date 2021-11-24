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
import sai.llsc.imp.CPSLLSCEngine

import sys.process._

abstract class GenericLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
    extends SAISnippet[A, B] with SAIOps { q =>
  import java.io.{File, PrintStream}

  val codegen: GenericLLSCCodeGen

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
    |CXXFLAGS = $includes
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
  def run(nThread: Int = 1): Int = {
    val ret = Process(s"./$appName $nThread", new File(s"$folder/$appName")).!!
    ret.split("\n").last.split(" ").last.toInt
  }
  // returns the number of paths, and the return status of the process
  def runWithStatus(nThread: Int = 1): (String, Int) = {
    import collection.mutable.ListBuffer
    val output = ListBuffer[String]()
    val ret = Process(s"./$appName $nThread", new File(s"$folder/$appName")).run(ProcessLogger(r => output += r, e => output += e)).exitValue
    (output.mkString("\n"), ret)
  }
}

abstract class PureLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with LLSCEngine { q =>
  val codegen = new PureLLSCCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.CompileTimeRuntime.funNameMap
    def blockMap: HashMap[Int, String] = q.CompileTimeRuntime.blockNameMap
  }
}

abstract class ImpLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with ImpLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.CompileTimeRuntime.funNameMap
    def blockMap: HashMap[Int, String] = q.CompileTimeRuntime.blockNameMap
  }
}

abstract class ImpVecLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with ImpLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen with StdVectorCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.CompileTimeRuntime.funNameMap
    def blockMap: HashMap[Int, String] = q.CompileTimeRuntime.blockNameMap
  }
}

abstract class CPSLLSCDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
   extends GenericLLSCDriver[A, B](appName, folder) with CPSLLSCEngine { q =>
  val codegen = new ImpureLLSCCodeGen /* with StdVectorCodeGen */ {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    def funMap: HashMap[Int, String] = q.CompileTimeRuntime.funNameMap
    def blockMap: HashMap[Int, String] = q.CompileTimeRuntime.blockNameMap
  }
}

trait LLSC {
  val insName: String
  def newInstance(m: Module, name: String, fname: String, nSym: Int): GenericLLSCDriver[_, _]
  def runLLSC(m: Module, name: String, fname: String, nSym: Int = 0): Unit = {
    val (_, t) = time {
      val code = newInstance(m, name, fname, nSym)
      code.genAll
    }
    println(s"[$insName] compiling $name, time $t ms")
  }
}

class PureLLSC extends LLSC {
  val insName = "PureLLSC"
  def newInstance(m: Module, name: String, fname: String, nSym: Int) =
    new PureLLSCDriver[Int, Unit](name, "./llsc_gen") {
      @virtualize
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = SymV.makeSymVList(nSym)
        val res = exec(m, fname, args, false, 4)
        // FIXME: pass isCommandLine, symarg=4 seems doesn't work on mp1p?
        res.foreach { s => SS.checkPCToFile(s._1) }
        ()
      }
    }
}

class ImpLLSC extends LLSC {
  val insName = "ImpLLSC"
  def newInstance(m: Module, name: String, fname: String, nSym: Int) =
    new ImpLLSCDriver[Int, Unit](name, "./llsc_gen") {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = SymV.makeSymVList(nSym)
        val res = exec(m, fname, args, false, 4)
        res.foreach { s => SS.checkPCToFile(s._1)}
        ()
      }
    }
}

class ImpVecLLSC extends LLSC {
  val insName = "ImpLLSC"
  def newInstance(m: Module, name: String, fname: String, nSym: Int) =
    new ImpVecLLSCDriver[Int, Unit](name, "./llsc_gen") {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = SymV.makeSymVList(nSym)
        val res = exec(m, fname, args, false, 4)
        res.foreach { s => SS.checkPCToFile(s._1)}
        ()
      }
    }
}

class CPSLLSC extends LLSC {
  val insName = "CPSLLSC"
  def newInstance(m: Module, name: String, fname: String, nSym: Int) =
    new CPSLLSCDriver[Int, Unit](name, "./llsc_gen") {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = SymV.makeSymVList(nSym)
        val k: Rep[Cont] = fun { case sv =>
          SS.checkPCToFile(sv._1); ()
        }
        exec(m, fname, args, false, 4, k)
      }
    }
}

object RunLLSC {
  def experiment: Unit = {
    val pure = new PureLLSC
    val cps = new CPSLLSC
    val imp = new ImpLLSC
    val impVec = new ImpVecLLSC

    //pure.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp65536, "mp65kPure", "@f", 16)
    //imp.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp65536, "mp65kImpCtrl", "@f", 16)
    //impVec.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp65536, "mp65kImpVecCtrl", "@f", 16)
    //cps.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp65536, "mp65kCPSCtrl", "@f", 16)

    //pure.runLLSC(sai.llvm.Benchmarks.mergesort, "mergePureCtrl", "@main", 0)
    //imp.runLLSC(sai.llvm.Benchmarks.mergesort, "mergeImpCtrl", "@main", 0)
    //impVec.runLLSC(sai.llvm.Benchmarks.mergesort, "mergeImpVecCtrl", "@main", 0)
    //cps.runLLSC(sai.llvm.Benchmarks.mergesort, "mergeCPSCtrl", "@main", 0)

    //pure.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1mPure", "@f", 20)
    //imp.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1mImp", "@f", 20)
    //cps.runLLSC(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1mCPS", "@f", 20)

    //pure.runLLSC(sai.llvm.OOPSLA20Benchmarks.maze, "mazePure", "@main", 2)
    //cps.runLLSC(sai.llvm.OOPSLA20Benchmarks.maze, "mazeCPS", "@main", 2)

    // FIXME: incorrect code generated for block `if.end48`
    imp.runLLSC(sai.llvm.OOPSLA20Benchmarks.maze, "mazeImp", "@main", 2)
  }

  def main(args: Array[String]): Unit = {
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
      llsc.runLLSC(parseFile(filepath), appName, fun, nSym)
    }

    //experiment

    //runLLSC(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1m", "@f", 20)
    //runLLSC(sai.llvm.Benchmarks.arrayAccess, "arrAccess", "@main")
    //runLLSC(sai.llvm.LLSCExpr.structReturnLong, "structR1", "@main")
    //runLLSC(sai.llvm.Coreutils.echo, "echo", "@main")
    //runLLSC(sai.llvm.LLSCExpr.complexStruct, "complexStruct", "@main")
    //runLLSC(sai.llvm.LLSCExpr.runCommandLine, "runCommandLine", "@main")
    //runLLSC(parseFile("benchmarks/demo_benchmarks/bubblesort.ll"), "bubble", "@main")
  }
}
