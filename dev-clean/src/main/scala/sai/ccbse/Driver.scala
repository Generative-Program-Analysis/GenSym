package sai.ccbse

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
import scala.collection.mutable.{Map => StaticMutMap, Queue => StaticQueue, Set => StaticSet}

abstract class CCBSEDriver[A: Manifest, B: Manifest](appName: String, folder: String = ".")
    extends SAISnippet[A, B] with SAIOps with CCBSEEngine { q =>

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

/* example
what is the call-chain distance to main?
1) main 0, g 1 does this make sense?
2) main 2, g 1?

f:
  target

g:
  call f

main:
  call g
  call f
*/

object RunCCBSE {
  @virtualize
  def specialize(m: Module, name: String, fname: String): CCBSEDriver[Int, Unit] =
    new CCBSEDriver[Int, Unit](name, "./ccbse_gen") {
      def snippet(u: Rep[Int]) = {
        prepareCompileTimeRuntime(m)
        analyze_fun(m, fname)
        // (caller, callee)
        var workList: StaticList[(String, String)] = StaticList((fname, ""))
        while (workList.nonEmpty) {
          val currFun = workList.head._1
          val fromFun = workList.head._2
          workList = CompileTimeRuntime.callGraph.getOrElse(currFun, StaticMutMap()).toList.sortBy(_._2).map{
            case (s, i) => (s, currFun)
          } ++ workList.tail
          val res = exec(m, currFun, SymV.makeSymVList(get_args_num(currFun), currFun))
          println(res.size)
        }

        // query SMT for 1 test
        //SS.checkPCToFile(res(0)._1)
        ()
      }

      def get_args_num(fname: String): Int = m.funcDefMap(fname).header.params.size
    }

  def runCCBSE(m: Module, name: String, fname: String) {
    val (_, t) = sai.utils.Utils.time {
      val code = specialize(m, name, fname)
      code.genAll
    }
    println(s"compiling $name, time $t ms")
  }

  def main(args: Array[String]): Unit = {
    val usage = """
    Usage: ccbse <.ll-filepath> <app-name>
    """
    if (args.size < 3) {
      println(usage)
    } else {
      val filepath = args(0)
      val appName = args(1)
      val fun = args(2)
      val bbNum = args(3).toInt
      val instrNum = args(4).toInt
      runCCBSE(parseFile(filepath), appName, fun)
    }

    runCCBSE(sai.llvm.TestCCBSE.simple1, "simple1", "@f")

  }
}
