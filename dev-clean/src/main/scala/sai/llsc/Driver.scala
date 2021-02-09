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

abstract class LLSCDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIOps with LLSCEngine { q =>
  val codegen = new CGenBase with SymStagedLLVMGen {
    val IR: q.type = q
    import IR._
  }

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource[A, B](wrapper, "Snippet", new java.io.PrintStream(source))
    (source.toString, statics)
  }

  val compilerCommand = "g++ -std=c++17 -O3"

  def save(to: String): Unit = {
    val out = new java.io.PrintStream("./"+to)
    out.println(code)
    out.close
  }

  def compile(to: String): String = {
    import scala.sys.process._
    val libraries = codegen.libraryFlags.mkString(" ")
    val includes = codegen.joinPaths(codegen.includePaths, "-I")
    val libraryPaths = codegen.joinPaths(codegen.libraryPaths, "-L")

    val bin = to.split('.')(0)
    (new java.io.File(bin)).delete
    val pb = s"$compilerCommand $to -o $bin $libraries $includes $libraryPaths"
    System.out.println("Compile command: " + pb)
    time("cc-compile") { (pb: ProcessBuilder).lines.foreach(Console.println _) }
    bin
  }

  def saveCompileRun(to: String): A => Unit = {
    import scala.sys.process._
    save(to)
    val bin = compile(to)

    (a: A) => {
      System.out.println(s"Running ./$bin $a")
      // FIXME: LD_LIBRARY_PATH?
      // export LD_LIBRARY_PATH=../stp/build/lib
      Process(s"./$bin $a", None, "LD_LIBRARY_PATH"->"../stp/build/lib").lines.foreach(Console.println _)
    }
  }

  def eval(a: A): Unit = {
    val g = saveCompileRun("snippet.c")
    time("eval")(g(a))
  }
}

object TestStagedSymExec {
  @virtualize
  def specialize(m: Module, fname: String): LLSCDriver[Int, Unit] =
    new LLSCDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = List[Value](
          SymV("x0"), SymV("x1"), SymV("x2"), 
          SymV("x3"), SymV("x4"), SymV("x5"),
          SymV("x6"), SymV("x7"), SymV("x8"),
          SymV("x9"),
          SymV("x10"), SymV("x11"),
          SymV("x12"), SymV("x13"), SymV("x14"),
          SymV("x15"),
          SymV("x16"), SymV("x17"),
          SymV("x18"), SymV("x19")
        )
        val res = exec(m, fname, args)
        // query a single test
        //res.head._1.pc.toList.foreach(assert(_))
        //handle(query(lit(false)))

        println(res.size)
      }
    }

  def testModule(m: Module, output: String, fname: String) {
    val res = sai.utils.Utils.time {
      val code = specialize(m, fname)
      code.save(s"llsc_gen/$output")
      code.compile(s"llsc_gen/$output")
    }
    println(res)
    //code.eval(0)
  }

  def main(args: Array[String]): Unit = {
    //testModule(sai.llvm.Benchmarks.add, "add.cpp", "@add")
    testModule(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1048576_sym.cpp", "@f")
  }
}
