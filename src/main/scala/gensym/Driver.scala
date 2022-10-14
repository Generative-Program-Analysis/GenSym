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

import scala.collection.immutable.{List => StaticList}
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
    val debugFlags = if (Config.genDebug) "-g -DDEBUG" else ""

    out.println(s"""|BUILD_DIR = build
    |TARGET = $appName
    |SRC_DIR = .
    |SOURCES = $$(shell find $$(SRC_DIR)/ -name "*.cpp" ! -name "$${TARGET}.cpp")
    |OBJECTS = $$(SOURCES:$$(SRC_DIR)/%.cpp=$$(BUILD_DIR)/%.o)
    |OPT = -O3
    |CC = g++ -std=c++17 -Wno-format-security
    |PERFFLAGS = -fno-omit-frame-pointer $debugFlags
    |CXXFLAGS = $includes $extraFlags $$(PERFFLAGS)
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
    if (Config.opt) {
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
    if (Config.opt) {
      val (g1, subst1) = AssignElim.impTransform(g0)
      codegen.reconsMapping(subst1)
      g1
    } else g0
  }

  override def addRewrite: Unit = {
    if (!Config.opt) return ()
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
  def extraFlags: String = "" // -D USE_LKFREE_Q
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit]
  def run(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] = {
    Counter.block.reset
    Counter.variable.reset
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
    new PureGSDriver[Int, Unit](m, name, "./gs_gen", config) {
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
    new PureCPSGSDriver[Int, Unit](m, name, "./gs_gen", config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args, fun { case sv => checkPCToFile(sv._1) })
      }
    }
}

class ImpGS extends GenSym with ImpureState {
  val insName = "ImpGS"
  def newInstance(m: Module, name: String, fname: String, config: Config): GenericGSDriver[Int, Unit] =
    new ImpGSDriver[Int, Unit](m, name, "./gs_gen", config) {
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
    new ImpVecGSDriver[Int, Unit](m, name, "./gs_gen", config) {
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
    new ImpCPSGSDriver[Int, Unit](m, name, "./gs_gen", config) {
      implicit val me: this.type = this
      def snippet(u: Rep[Int]) = {
        exec(fname, config.args, fun { case sv => checkPCToFile(sv._1) })
      }
    }
}
