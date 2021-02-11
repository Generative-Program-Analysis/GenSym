package sai.llsc

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

abstract class LLSCDriver[A: Manifest, B: Manifest](name: String, folder: String = ".") extends SAISnippet[A, B] with SAIOps with LLSCEngine { q =>
  import java.io.{File, PrintStream}

  val codegen = new SymStagedLLVMGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$name/"
  }
  val compilerCommand = "g++ -std=c++17 -O3"

  def genSource: Unit = {
    val folderFile = new File(folder)
    if (!folderFile.exists()) folderFile.mkdir
    (new File(codegen.codegenFolder)).mkdir
    val mainStream = new PrintStream(s"$folder/$name/$name.cpp")
    val statics = Adapter.emitCommon1(name, codegen, mainStream)(manifest[A], manifest[B])(x => Unwrap(wrapper(Wrap[A](x))))
    mainStream.close
  }

  // TODO: export LD_LIBRARY_PATH=../stp/build/lib
  def genMakefile: Unit = {
    val out = new PrintStream(s"$folder/$name/Makefile")
    val curDir = new File(".").getCanonicalPath
    val libraries = codegen.libraryFlags.mkString(" ")
    val includes = codegen.includePaths.map(s"-I $curDir/" + _).mkString(" ")
    val libraryPaths = codegen.libraryPaths.map(s"-L $curDir/" + _).mkString(" ")

    out.println(s"""|BUILD_DIR = build
    |SRC_DIR = .
    |SOURCES = $$(shell find $$(SRC_DIR)/ -name "*.cpp")
    |TARGET = $name
    |OBJECTS = $$(SOURCES:$$(SRC_DIR)/%.cpp=$$(BUILD_DIR)/%.o)
    |CC = g++ -std=c++17 -O3
    |CXXFLAGS = $includes
    |LDFLAGS = $libraryPaths
    |LDLIBS = $libraries
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
    |\t@rm main 2>/dev/null || true
    |\t@rm build -rf 2>/dev/null || true
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

object TestStagedSymExec {
  @virtualize
  def specialize(m: Module, name: String, fname: String): LLSCDriver[Int, Unit] =
    new LLSCDriver[Int, Unit](name, "./llsc_gen") {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = List[Value](
          IntV(10000),
          /*
          SymV("x0"), SymV("x1"), SymV("x2"), 
          SymV("x3"), SymV("x4"), SymV("x5"),
          SymV("x6"), SymV("x7"), SymV("x8"),
          SymV("x9"),
          SymV("x10"), SymV("x11"),
          SymV("x12"), SymV("x13"), SymV("x14"),
          SymV("x15"),
          SymV("x16"), SymV("x17"),
          SymV("x18"), SymV("x19")
           */
        )
        val res = exec(m, fname, args)
        // query a single test
        //res.head._1.pc.toList.foreach(assert(_))
        //handle(query(lit(false)))

        println(res.size)
      }
    }

  def testModule(m: Module, name: String, fname: String) {
    val res = sai.utils.Utils.time {
      val code = specialize(m, name, fname)
      code.genAll
    }
    println(res)
  }

  def main(args: Array[String]): Unit = {
    //testModule(sai.llvm.Benchmarks.add, "add.cpp", "@add")
    //testModule(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1m", "@f")
    testModule(sai.llvm.Benchmarks.largeStackArray, "largeStackArray", "@f")
  }
}
