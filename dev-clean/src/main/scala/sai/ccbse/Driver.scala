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
        var runtimeCallGraph: Rep[Map[String, List[(String, Int)]]] = Map[String, List[(String, Int)]]()
        CompileTimeRuntime.callGraph.foreach { case ((s, m)) =>
          val tempList = m.toList.sortBy(_._2)
          runtimeCallGraph = runtimeCallGraph + (s -> (tempList))
        }

        for (fun <- CompileTimeRuntime.funMap.values) {
          val param_list = fun.header.params.asInstanceOf[List[TypedParam]]
          val sizes = param_list map { u =>
            u.ty match {
              case IntType(size) => size
              case PtrType(ty, addrSpace) => ???
            }
          }
          addArgSize(fun.id, sizes)
          addFun(fun.id, getRealFunName(CompileTimeRuntime.FunFuns(fun.id)))
        }
        // Note: may need to add external fun as well
        addFun("@target", "target")
        addFun("@make_symbolic", "make_symbolic")

        // (caller, callee)
        var workList: Rep[List[(String, Int)]] = List((fname, 0))

        ccbse_main(workList, runtimeCallGraph, CompileTimeRuntime.concreteHeap,
          CompileTimeRuntime.symbolicHeap)

        exec(m, "@main", SymV.makeSymVList(1))
        exec(m, "@f", SymV.makeSymVList(1))
        exec(m, "@g", SymV.makeSymVList(1))

        // while (workList.nonEmpty) {
        //   val currFun = workList.head._1
        //   val fromFun = workList.head._2

        //   val res = exec(m, currFun, SymV.makeSymVList(get_args_num(currFun), currFun))
          
        //   if (contains_target(res)) {
        //     workList = (CompileTimeRuntime.callGraph.getOrElse(currFun, StaticMutMap()).map{
        //       case (s, i) => (s, currFun)
        //     }.toList ++ workList.tail).sortBy(_._2)
        //     println(res.size)
        //   } else {}
        //   if (currFun == "@main") workList = StaticList()
        // }

        // query SMT for 1 test
        //SS.checkPCToFile(res(0)._1)
        ()
      }

      def get_args_num(fname: String): Int = m.funcDefMap(fname).header.params.size

      def contains_target(res: Rep[List[(SS, Value)]]): Rep[Boolean] =
        "contains_target".reflectReadWith[Boolean](res)(Adapter.CTRL)
      def ccbse_main(res: Rep[List[(String, Int)]],
        callGraph: Rep[Map[String, List[(String, Int)]]],
        ch: Rep[List[Value]], sh: Rep[List[Value]]): Rep[Unit] =
        "ccbse_main".reflectWriteWith[Unit](res, callGraph, ch, sh)(Adapter.CTRL)

      def addArgSize(fname: String, size: Rep[List[Int]]): Rep[Unit] =
        "add-arg-size".reflectWriteWith[Unit](fname, size)(Adapter.CTRL)
      def addFun(fname: String, fp: String): Rep[Unit] =
        "add-fun".reflectWriteWith[Unit](fname, unchecked[String](fp))(Adapter.CTRL)
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

    runCCBSE(parseFile("benchmarks/ccbse/otter_example4.ll"), "example", "@g")

  }
}
