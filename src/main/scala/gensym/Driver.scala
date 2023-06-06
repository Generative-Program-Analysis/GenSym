package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.llvm.parser.Parser._
import gensym.lmsx._
import gensym.utils.Utils.time
import gensym.imp.Mut
import gensym.imp.ImpGSEngine
import gensym.imp.ImpCPSGSEngine
import gensym.Constants._

import scala.collection.immutable.{List => StaticList,Map => StaticMap}
import scala.collection.mutable.HashMap

import sys.process._

abstract class GenericGSDriver[A: Manifest, B: Manifest]
    extends SAISnippet[A, B] with SAIOps { q =>
  import java.io.{File, PrintStream}

  val appName: String
  val folder: String
  val config: Config

  val codegen: GenericGSCodeGen
  var extraFlags: String = ""

  // Assuming the working directory only contains subdir "build" or "tests"
  // TODO: remove this to somewhere for utilities
  def prepareBuildDir: Boolean = {
    val codegenFolderFile = new File(codegen.codegenFolder)
    if (!codegenFolderFile.exists()) codegenFolderFile.mkdir
    else {
      val entries = codegenFolderFile.list()
      entries.map(x => {
        if (x == "build" || x == "tests") {
          val buildDir = new File(codegenFolderFile.getPath, x)
          buildDir.list.map(x => new File(buildDir.getPath, x).delete)
          buildDir.delete
        }
        else new File(codegenFolderFile.getPath, x).delete
      })
      codegenFolderFile.delete
      codegenFolderFile.mkdir
    }
  }

  def transform(g0: Graph): Graph = g0
  def addRewrite: Unit = ()

  def genSource: Unit = {
    val folderFile = new File(folder)
    if (!folderFile.exists()) folderFile.mkdir
    prepareBuildDir
    val mainStream = new PrintStream(s"$folder/$appName/$appName.cpp")
    val g0 = Adapter.genGraph1(manifest[A], manifest[B]) { x =>
      addRewrite
      Unwrap(wrapper(Wrap[A](x)))
    }
    val g1 = transform(g0)
    val statics = lms.core.utils.time("codegen") {
      codegen.typeMap = Adapter.typeMap
      codegen.stream = mainStream
      codegen.emitAll(g1, appName)(manifest[A], manifest[B])
      codegen.extractAllStatics
    }
    mainStream.close
  }

  def genMakefile: Unit = {
    val out = new PrintStream(s"$folder/$appName/Makefile")
    val curDir = new File(".").getCanonicalPath
    val libraries = codegen.libraryFlags.mkString(" ")
    val includes = codegen.includePaths.map(s"-I $curDir/" + _).mkString(" ")
    val libraryPaths = codegen.libraryPaths.map(p => s"-L $curDir/$p -Wl,-rpath $curDir/$p").mkString(" ")
    val debugFlags = if (Global.config.genDebug) "-g -DDEBUG" else ""
    val featureFlags = if (Global.config.symbolicUninit) "-DGENSYM_SYMBOLIC_UNINIT" else "-DGENSYM_RANDOM_UNINIT"

    out.println(s"""|BUILD_DIR = build
    |TARGET = $appName
    |SRC_DIR = .
    |SOURCES = $$(shell find $$(SRC_DIR)/ -name "*.cpp" ! -name "$${TARGET}.cpp")
    |OBJECTS = $$(SOURCES:$$(SRC_DIR)/%.cpp=$$(BUILD_DIR)/%.o)
    |OPT = -O3
    |CC = g++ -std=c++17 -Wno-format-security
    |PERFFLAGS = -fno-omit-frame-pointer $debugFlags
    |FEATUREFLAGS = $featureFlags
    |CXXFLAGS = $includes $extraFlags $$(PERFFLAGS) $$(FEATUREFLAGS)
    |LDFLAGS = $libraryPaths
    |LDLIBS = $libraries -lpthread
    |
    |default: $$(TARGET)
    |
    |.SECONDEXPANSION:
    |
    |$$(OBJECTS): $$$$(patsubst $$(BUILD_DIR)/%.o,$$(SRC_DIR)/%.cpp,$$$$@)
    |\tmkdir -p $$(@D)
    |\t$$(CC) $$(OPT) -c -o $$@ $$< $$(CXXFLAGS)
    |
    |$$(BUILD_DIR)/$${TARGET}.o : $${TARGET}.cpp
    |\tmkdir -p $$(@D)
    |\t$$(CC) -${config.mainFileOpt} -c -o $$@ $$< $$(CXXFLAGS)
    |
    |$$(TARGET): $$(OBJECTS) $$(BUILD_DIR)/$${TARGET}.o
    |\t$$(CC) $$(OPT) -o $$@ $$^ $$(LDFLAGS) $$(LDLIBS)
    |
    |clean:
    |\t@rm $${TARGET} 2>/dev/null || true
    |\t@rm build -rf 2>/dev/null || true
    |\t@rm tests -rf 2>/dev/null || true
    |
    |.PHONY: default clean
    |""".stripMargin)
    out.close
  }

  def genAll: Unit = { genSource; genMakefile }

  def make(j: Int = 1): Int = Process(s"make -j$j", new File(s"$folder/$appName")).!

  def makeWithAllCores: Int = {
    val cores = Process("nproc", new File(s"$folder/$appName")).!!.replaceAll("[\\n\\t ]", "").toInt
    Process(s"make -j$cores", new File(s"$folder/$appName")).!
  }

  // returns the number of paths, obtained by parsing the output
  def run(opt: String = ""): Int = {
    val cmd = s"./$appName $opt"
    System.out.println(s"running $cmd")
    val ret = Process(cmd, new File(s"$folder/$appName")).!!
    ret.split("\n").last.split(" ").last.toInt
  }

  // returns the number of paths, and the return status of the process
  def runWithStatus(opt: Seq[String], launcher: String = ""): (String, Int) = {
    import collection.mutable.ListBuffer
    val cmd = if (launcher.nonEmpty) launcher.split("\\s+").toSeq++Seq(s"./$appName")++opt else Seq(s"./$appName")++opt
    System.out.println("running " + cmd.mkString(" "))
    val output = ListBuffer[String]()
    val ret = Process(cmd, new File(s"$folder/$appName")).run(ProcessLogger(r => output += r, e => output += e)).exitValue
    (output.mkString("\n"), ret)
  }
}

abstract class PureEngineDriver[A: Manifest, B: Manifest] extends GenericGSDriver[A, B] {
  q: EngineBase =>
  override lazy val codegen: GenericGSCodeGen = new PureGSCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    setFunMap(q.funNameMap)
    setBlockMap(q.nodeBlockMap)
  }

  override def transform(g0: Graph): Graph = {
    if (Global.config.opt) {
      val (g1, subst1) = AssignElim.transform(g0)
      codegen.reconsMapping(subst1)
      g1
    } else g0
  }
}

abstract class ImpureEngineDriver[A: Manifest, B: Manifest] extends GenericGSDriver[A, B] {
  q: EngineBase =>
  override lazy val codegen: GenericGSCodeGen = new ImpureGSCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    setFunMap(q.funNameMap)
    setBlockMap(q.nodeBlockMap)
  }

  override def transform(g0: Graph): Graph = {
    if (Global.config.opt) {
      val (g1, subst1) = AssignElim.impTransform(g0)
      codegen.reconsMapping(subst1)
      g1
    } else g0
  }

  override def addRewrite: Unit = {
    if (!Global.config.opt) return ()
    val bConst = Backend.Const
    type bExp = Backend.Exp
    // Note: these are transformation for the imperative version; should be
    //       refactor to the right place.
    val g = Adapter.g
    g.addRewrite {
      // val sz = s.stackSize
      // s.alloc(8)
      // val a1 = StackLocV(sz)
      // s.alloc(4)
      // val a2 = StackLocV(sz + 8)
      case ("ss-stack-size", StaticList(s: bExp)) if g.curEffects.allEff.contains(s) =>
        def tryRewrite: Option[bExp] = {
          var sz: Int = 0
          for ((k, _) <- g.curEffects.allEff(s)) {
            g.findDefinition(k) collect {
              case Node(_, "ss-alloc-stack", StaticList(_, bConst(n: Mut[Int])), _) =>
                sz = sz + n.x
            }
          }
          for ((_, lrs) <- g.curEffects.allEff(s)) {
            for (k <- lrs) {
              g.findDefinition(k) collect {
                case Node(n, "ss-stack-size", StaticList(_), _) =>
                  return Some(g.reflect("+", n, bConst(sz)))
              }
            }
          }
          None
        }
        tryRewrite
      case ("ss-lookup-env", StaticList(s: bExp, bConst(x: Int)))
          if g.curEffects.allEff.contains(s) =>
        def tryRewrite: Option[bExp] = {
          for ((k, _) <- g.curEffects.allEff(s)) {
            g.findDefinition(k) collect {
              case Node(_, "ss-assign", StaticList(_, bConst(y: Int), v: bExp), _) if x == y =>
                return Some(v)
              case Node(_, "ss-assign-seq", StaticList(_, bConst(vars: List[Int]), vals: bExp), _) =>
                val idx = vars.indexOf(x)
                if (idx != -1) return Some(g.reflect("list-apply", vals, bConst(idx)))
            }
          }
          None
        }
        tryRewrite
    }
  }

}

// Using immer data structures for
//   1) internal state/memory representation
//   2) function call argument list
//   3) function return result list
abstract class PureGSDriver[A: Manifest, B: Manifest](
  val m: Module, val appName: String, val folder: String, val config: Config)
    extends PureEngineDriver[A, B] with GSEngine

// Using immer data structures but generating CPS code,
// avoding reifying the returned nondet list.
abstract class PureCPSGSDriver[A: Manifest, B: Manifest](
  val m: Module, val appName: String, val folder: String, val config: Config)
    extends PureEngineDriver[A, B] with PureCPSGSEngine

// Using C++ std containers for internal state/memory representation,
// but still using immer containers for function call argument lists and result lists.
abstract class ImpGSDriver[A: Manifest, B: Manifest](
  val m: Module, val appName: String, val folder: String, val config: Config)
    extends ImpureEngineDriver[A, B] with ImpGSEngine

// Using C++ std containers for internal state/memory representation,
// function call argument lists, and result lists.
// Note the composition with `StdVectorCodeGen`.
abstract class ImpVecGSDriver[A: Manifest, B: Manifest](
  val m: Module, val appName: String, val folder: String, val config: Config)
    extends ImpureEngineDriver[A, B] with ImpGSEngine { q =>
  override lazy val codegen = new ImpureGSCodeGen with StdVectorCodeGen {
    val IR: q.type = q
    val codegenFolder = s"$folder/$appName/"
    setFunMap(q.funNameMap)
    setBlockMap(q.nodeBlockMap)
  }
}

// Generting CPS code with C++ containers for internal state/memory representation.
// Function call argument lists and result lists still use immer containers.
abstract class ImpCPSGSDriver[A: Manifest, B: Manifest](
  val m: Module, val appName: String, val folder: String , val config: Config)
    extends ImpureEngineDriver[A, B] with ImpCPSGSEngine

trait GenSym {
  val insName: String
  var libdef: Option[ModDef] = None  // for linking with prepared library
  def extraFlags: String = "" // -D USE_LKFREE_Q
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit]
  def run(m: Module, name: String, fname: String, config: Config, libPath: Option[String] = None): GenericGSDriver[Int, Unit] = {
    libdef = libPath match {
      case Some(p) =>  // linking with external library - load its manifest
        import java.io._
        val ois = new ObjectInputStream(new FileInputStream(s"$p/Manifest"))
        try { Some(ois.readObject().asInstanceOf[ModDef]) } finally { ois.close }
      case None => None
    }
    libdef match {
      case Some(modref) =>  // library linking mode - set counters to specified values
        Counter.block.reset(modref.counters.blks)
        Counter.variable.reset(modref.counters.vars)
      case None =>  // standalone mode - clear counters
        Counter.block.reset
        Counter.variable.reset
    }
    val (code, t) = time {
      val code = newInstance(m, name, fname, config)
      code.extraFlags = extraFlags
      code.genAll
      code
    }
    println(s"[$insName] compiling $name, time $t ms")
    code
  }
}

trait PureState { self: GenSym =>
  override def extraFlags = "-D PURE_STATE"
}

trait ImpureState { self: GenSym =>
  override def extraFlags = "-D IMPURE_STATE"
}

class PureGS extends GenSym with PureState {
  val insName = "PureGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new PureGSDriver[Int, Unit](m, name, outputDir, config) {
      implicit val me: this.type = this
      @virtualize
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args).foreach { s => checkPCToFile(s._1) }
        ()
      }
    }
}

class PureCPSGS extends GenSym with PureState {
  val insName = "PureCPSGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new PureCPSGSDriver[Int, Unit](m, name, outputDir, config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args, fun { case sv => checkPCToFile(sv._1) })
      }
    }
}

class ImpGS extends GenSym with ImpureState {
  val insName = "ImpGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpGSDriver[Int, Unit](m, name, outputDir, config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args).foreach { s => checkPCToFile(s._1) }
        ()
      }
    }
}

class ImpVecGS extends GenSym with ImpureState {
  val insName = "ImpGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpVecGSDriver[Int, Unit](m, name, outputDir, config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args).foreach { s => checkPCToFile(s._1) }
        ()
      }
    }
}

class ImpCPSGS extends GenSym with ImpureState {
  val insName = "ImpCPSGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpCPSGSDriver[Int, Unit](m, name, outputDir, config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args, fun { case sv => checkPCToFile(sv._1) })
      }
    }
}

class ImpCPSGS_lib extends GenSym with ImpureState {
  val insName = "ImpCPSGS_lib"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpCPSGSDriver[Int, Unit](m, name, outputDir, config) { q =>
      import java.io.{File,PrintStream}
      implicit val me: this.type = this
      override lazy val codegen: GenericGSCodeGen = new ImpureGSCodeGen {
        val IR: q.type = q
        val codegenFolder = s"$folder/$appName/"
        setFunMap(q.funNameMap)
        setBlockMap(q.nodeBlockMap)
        override def emitHeaderFile: Unit = {
          val filename = codegenFolder + "/common.h"
          val out = new java.io.PrintStream(filename)
          val branchStatStr = Counter.printBranchStat
          withStream(out) {
            emitln("/* Emitting header file */")
            emitHeaders(stream)
            emitln("using namespace immer;")
            emitFunctionDecls(stream)
            emitDatastructures(stream)
            emitln(s"""
            |inline Monitor& cov() {
            |  static Monitor m;
            |  return m;
            |}""".stripMargin)
            emitln("/* End of header file */")
          }
          out.close
        }
        registerHeader("<gensym/libcpolyfill.hpp>")
      }
      override def genSource: Unit = {
        val folderFile = new File(folder)
        if (!folderFile.exists()) folderFile.mkdir
        prepareBuildDir
        val g0 = Adapter.genGraph1(manifest[Int], manifest[Unit]) { x =>
          addRewrite
          Unwrap(wrapper(Wrap[Int](x)))
        }
        val g1 = transform(g0)
        val statics = lms.core.utils.time("codegen") {
          codegen.typeMap = Adapter.typeMap
          codegen.capture {
            codegen.emitAll(g1, appName)(manifest[Int], manifest[Unit])
          }
          codegen.extractAllStatics
        }
      }
      override def genMakefile: Unit = {
        val out = new PrintStream(s"$folder/$appName/Makefile")
        val curDir = new File(".").getCanonicalPath
        val includes = codegen.includePaths.map(s"-I $curDir/" + _).mkString(" ")
        val debugFlags = if (Global.config.genDebug) "-g -DDEBUG" else ""

        out.println(s"""|BUILD_DIR = build
        |TARGET = $appName.a
        |SRC_DIR = .
        |INITFILE = initlib
        |SOURCES = $$(shell find $$(SRC_DIR)/ -name "*.cpp" ! -name "$$(INITFILE).cpp")
        |OBJECTS = $$(SOURCES:$$(SRC_DIR)/%.cpp=$$(BUILD_DIR)/%.o)
        |OPT = -O3
        |CC = g++ -std=c++17 -Wno-format-security
        |AR = ar cvq
        |PERFFLAGS = -fno-omit-frame-pointer $debugFlags
        |CXXFLAGS = $includes $extraFlags $$(PERFFLAGS)
        |
        |default: $$(TARGET)
        |
        |.SECONDEXPANSION:
        |
        |$$(OBJECTS): $$$$(patsubst $$(BUILD_DIR)/%.o,$$(SRC_DIR)/%.cpp,$$$$@)
        |\tmkdir -p $$(@D)
        |\t$$(CC) $$(OPT) -c -o $$@ $$< $$(CXXFLAGS)
        |
        |$$(BUILD_DIR)/$$(INITFILE).o : $$(INITFILE).cpp
        |\tmkdir -p $$(@D)
        |\t$$(CC) -${config.mainFileOpt} -c -o $$@ $$< $$(CXXFLAGS)
        |
        |$$(TARGET): $$(BUILD_DIR)/$$(INITFILE).o $$(OBJECTS)
        |\t$$(AR) $$@ $$^
        |
        |clean:
        |\t@rm $${TARGET} 2>/dev/null || true
        |\t@rm build -rf 2>/dev/null || true
        |\t@rm tests -rf 2>/dev/null || true
        |
        |.PHONY: default clean
        |""".stripMargin)
        out.close
      }

      def snippet(u: Rep[Int]): Rep[Unit] = {
        // assume the application's `main` function will be compiled to `app_main`.
        val externMapping = StaticMap("app_main" -> "app_main") ++ StaticList(
          // Currently, we neither generate native calls nor model the
          // functionalities for those functions in this list (need to
          // "synthesize" complex arguments or returned values),
          // we provide a `gs_dummy` (libcpolyfill.hpp) implementation, which
          // simply halts (not invoking the continuation) and is potentially unsafe.
          "gettimeofday", "sigprocmask", "__syscall_rt_sigaction", "select",
          "fstatfs", "fstat64", "stat64", "execve", "times", "uname", "adjtimex",
          "wait4", "nanosleep", "setitimer", "getcwd", "sigsuspend", "sigwaitinfo",
          "__getdents64", "__getdents", "setgroups", "waitpid"
        ).map(_ -> "gs_dummy").toMap
        ExternalFun.prepare(externMapping)

        def preHeapGen(ss: Rep[Ref[SS]], vals: Rep[List[Value]], cont: Rep[Cont]): Rep[Unit] = {
          val heap = List(precompileHeapLists(m::Nil):_*)
          Coverage.printMap
          ss.heapAppend(heap)
          cont(ss, NullLoc())
        }

        // refer all the functions with an intrinsic
        // the driver here will not be emitted.
        val preHeapRep = topFun(preHeapGen(_, _, _))
        funNameMap(Unwrap(preHeapRep).asInstanceOf[Backend.Sym]) = "initlib"
        "ss-generate".reflectWriteWith[Unit](preHeapRep)(Adapter.CTRL)
        for ((f, d) <- funMap) compile(d)
        for ((n, f) <- FunFuns) "ss-generate".reflectWriteWith[Unit](f)(Adapter.CTRL)

        generateManifest
      }
      def generateManifest: Unit = {
        val funclist = for ((f, n) <- funNameMap) yield {
          val pat = "__GS_USER_(\\w+)".r
          n match {
            case pat(nshort) => FuncDef(nshort, s"__GS_USER_$nshort")
            case _ => FuncDef(n, n)
          }
        }
        val aliaslist = for ((f, s) <- symDefMap) yield { s.const match {
          case GlobalId(n) => FuncDef(f.tail, s"__GS_USER_${n.tail}")
          case _ => ???
        }}
        val varlist = for ((n, g) <- heapEnv) yield { g() match {
          case LocV(off0, _, size0, _) => (
              (Unwrap(off0), Unwrap(size0)) match {
                case (Backend.Const(off: Long), Backend.Const(size: Long)) =>
                  VarDef(n, off, size)
                case _ => ???
              }
            )
        }}
        import java.io._
        val module = ModDef(
          funclist.toList ++ aliaslist.toList,
          varlist.toList,
          folder,
          appName,
          CntInfo(Counter.variable.count, Counter.block.count))
        val oos = new ObjectOutputStream(new FileOutputStream(s"$folder/$appName/Manifest"))
        oos.writeObject(module)
        oos.close
      }
    }
}

class ImpCPSGS_app extends GenSym with ImpureState {
  val insName = "ImpCPSGS_app"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpCPSGSDriver[Int, Unit](m, name, outputDir, config) { q =>
      override val mainRename = "app_main"
      val libcdef = libdef.get
      implicit val me: this.type = this
      override lazy val codegen: GenericGSCodeGen = new ImpureGSCodeGen {
        val IR: q.type = q
        val codegenFolder = s"$folder/$appName/"
        setFunMap(q.funNameMap)
        setBlockMap(q.nodeBlockMap)
        override def emitHeaderFile: Unit = {
          val filename = codegenFolder + "/common.h"
          val out = new java.io.PrintStream(filename)
          val branchStatStr = Counter.printBranchStat
          withStream(out) {
            emitln("/* Emitting header file */")
            emitHeaders(stream)
            emitln("using namespace immer;")
            emitFunctionDecls(stream)
            emitDatastructures(stream)
            emitln("/* End of header file */")
          }
          out.close
        }
        override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
          val ng = init(g)
          val efs = ""
          val stt = dce.statics.toList.map(quoteStatic).mkString(", ")

          val src = run(name, ng)

          emitHeaderFile
          emitFunctionFiles
          emitInit(stream)

          emitln(s"/* Generated main file: $name */")
          emitln("#include \"common.h\"")
          emit(src)
          emitln(s"""
          |int main(int argc, char *argv[]) {
          |  prelude(argc, argv);
          |  $name(0);
          |  epilogue();
          |  return exit_code.load().value_or(0);
          |} """.stripMargin)
        }
        registerHeader(libcdef.folder, s"<${libcdef.libName}/common.h>")
        registerLibraryPath(s"${libcdef.folder}/${libcdef.libName}")
        val libnamepat = "lib(\\w+)".r
        registerLibrary(libcdef.libName match {
          case libnamepat(n) => s"-l$n"
          case _ => ???
        })
      }
      def snippet(u: Rep[Int]) = {
        ExternalFun.prepare(libcdef.funlist.map{ x => x.ref -> x.name}.toMap)
        implicit val ctx = Ctx(fname, findFirstBlock(fname).label.get)
        val initmain: Rep[Cont] = fun { case (ss, v) =>
          val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil, libcdef.varlist):_*)
          Coverage.printMap
          Coverage.incPath(1)
          ss.heapAppend(preHeap)
          val fv = eval(GlobalId(fname), VoidType, ss)
          ss.push
          ss.updateArg
          ss.initErrorLoc
          val k: Rep[Cont] = fun { case sv => checkPCToFile(sv._1) }
          "start_gs_main".reflectReadWith[Unit](ss, config.args, k)(fv)
        }
        val ss0 = initState
        "initlib".reflectWith[Unit](ss0, List[Value](), initmain)
      }
    }
}
