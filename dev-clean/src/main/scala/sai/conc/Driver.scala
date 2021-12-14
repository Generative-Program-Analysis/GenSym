package sai.conc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import lms.core._
import lms.core.Backend._
import lms.core.utils.time
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import scala.collection.immutable.{List => StaticList}

abstract class ConcDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
    extends SAISnippet[A, B] with SAIOps with ConcolicOptEngine { q =>

  import java.io.{File, PrintStream}

  val codegen = new SymStagedLLVMGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
  }

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
}

object RunConc {
  @virtualize
  def specialize(m: Module, name: String, fname: String, cargs: List[Int]=Nil): ConcDriver[Int, Unit] =
    new ConcDriver[Int, Unit](name, "./conc_gen") {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = if (cargs.isEmpty) gen_init_args(false) else List(cargs.map(IntV(_)):_*)
        val sargs: Rep[List[Value]] = gen_init_args(true)
        val s_conc_exec = topFun(conc_exec(_,_))
        FunName.conc_exec = Unwrap(s_conc_exec).asInstanceOf[Backend.Sym].n
        // hack to force invoke conc_exec function
        if (repFalse) s_conc_exec(args, sargs)
        conc_main_single(args, sargs)
      }

      def get_args_num: Int = m.funcDefMap(fname).header.params.size

      def gen_init_args(isSym: Boolean): Rep[List[Value]] = {
        val param_list = m.funcDefMap(fname).header.params.asInstanceOf[List[TypedParam]]
        var x: Int = 0
        val initList = param_list map { u =>
          u.ty match {
            case IntType(size) =>
              if (!isSym) IntV(0, size) else {
                x = x + 1
                SymV("arg" + x, size)
              }
            case PtrType(ty, addrSpace) => ???
          }
        }
        List[Value](initList: _*)
      }

      def conc_exec(args: Rep[List[Value]], sargs: Rep[List[Value]]) = {
        val res = exec(m, fname, args, sargs)
        // println("This return value is: " + res._2.int.toString)
        res._1.pc
      }

      // val s_conc_exec = hardFun(conc_exec(_,_))

      def conc_main(args: Rep[List[Value]], sargs: Rep[List[Value]]): Rep[Unit] =
        "conc_main".reflectWriteWith[Unit](args, sargs)(Adapter.CTRL)

      def conc_main_single(args: Rep[List[Value]], sargs: Rep[List[Value]]): Rep[Unit] =
        "conc_main_single".reflectWriteWith[Unit](args, sargs)(Adapter.CTRL)

      def repFalse = unchecked[Boolean]("false")
    }

  def runConc(f: String, proj: String, fname: String, args: List[Int]=Nil) {
    val m = parseFile(f)
    val (_, t) = sai.utils.Utils.time {
      val code = specialize(m, proj, fname, args)
      code.genAll
    }
    println(s"compiling $proj, time $t ms")
  }

  def calcConcTime(m: Module, name: String, fname: String, args: List[Int]=Nil): Double = {
    val (_, t) = sai.utils.Utils.time {
      val code = specialize(m, name, fname, args)
      code.genAll
    }
    t
  }

  def main(args: Array[String]): Unit = {
    runConc("benchmarks/pepm22/concolic/conc/bin_search.ll", "binary_search_gen", "@main")
    runConc("benchmarks/pepm22/concolic/conc/bubblesort.ll", "bubblesort_gen", "@main")
    runConc("benchmarks/pepm22/concolic/conc/knapsack.ll", "knapsack_gen", "@main")
    runConc("benchmarks/pepm22/concolic/conc/quicksort.ll", "quicksort_gen", "@main")
  }
}
