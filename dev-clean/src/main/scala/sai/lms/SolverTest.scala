package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

/*
abstract class SATSnippet[A:Manifest, B:Manifest] extends StagedSATOps {
  def wrapper(x: Rep[A]): Rep[B] = snippet(x)
  def snippet(x: Rep[A]): Rep[B]
}

abstract class CppSATDriver[A: Manifest, B: Manifest] extends SATSnippet[A, B] with SMTStagedOps { q =>
  val codegen = new CGenBase with CppSAICodeGenBase {
    val IR: q.type = q
    import IR._
  }

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource[A, B](wrapper, "Snippet", new java.io.PrintStream(source))
    (source.toString, statics)
  }

  var compilerCommand = "g++ -std=c++17 -O3 -Winline -L /homes/tan279/lib"

  def libraries = codegen.libraryFlags.mkString(" ")

  def save(to: String): Unit = {
    val out = new java.io.PrintStream("./"+to)
    out.println(code)
    out.close
  }

  lazy val f: A => Unit = {
    val out = new java.io.PrintStream("./snippet.c")
    out.println(code)
    out.close
    (new java.io.File("./snippet")).delete
    import scala.sys.process._
    val includes =
      if (codegen.includePaths.isEmpty) ""
      else s"-I ${codegen.includePaths.mkString(" -I ")}"
    val pb = s"$compilerCommand ./snippet.c -o ./snippet $libraries $includes -lstp"
    System.out.println("\nCompile command: " + pb + "\n")

    time("gcc") {
      (pb: ProcessBuilder).lines.foreach(Console.println _)
    }
    (a: A) => {
      System.out.println(s"Running ./snippet $a")
      Process(s"./snippet $a", None, "LD_LIBRARY_PATH"->"/homes/tan279/lib").lines.foreach(Console.println _)
    }
  }

  def eval(a: A): Unit = {
    val g = f
    time("eval")(g(a))
  }
}
 */

object SATTest extends App {
  /*
  def testExprBuilder(): Unit = {
    val sat = new SMTLib2ExprBuilder {
      import SyntaxSAT._
      val x = boolVar("x")
      val y = boolVar("y")
      assert(x ⇔ y)
      assert(x == true)
      assert(! x)
      assert(x ==> y)
      //assert(y ≡ false)
    }
    println(sat.getModel)
    println(sat.print_debug)
  }
   */

  def testSAT(): CppSAIDriver[Int, Unit] = new CppSAIDriver[Int, Unit] {
    def build(expect: Boolean)(e: => Rep[Int]): Unit = {
      push
      val res = e
      unchecked("vc_printAsserts(vc, 1);")
      println(res)
      if (expect) unchecked("assert(", res, ")")
      else unchecked("assert(!", res, ")")
      pop
    }

    def snippet(x: Rep[Int]) = {
      // import SyntaxSMT._
      // import SyntaxSAT._
      // A few sanity checks
      val p = boolVar("p")
      val q = boolVar("q")
      val r = boolVar("r")
      /*
      build(false) {
        isValid(or(p, q))
      }
      build(true) {
        assert(p)
        isValid(or(p, q))
      }
      build(true) {
        assert(p)
        assert(q)
        isValid(and(p, q))
      }
      build(false) {
        isValid(p)
      }
      build(true) {
        assert(q)
        isValid(imply(p, q))
      }
      build(false) {
        assert(q)
        isValid(imply(q, p))
      }
      build(false) {
        assert(not(p))
        isValid(p)
      }
      */
      build(true) {
        assert(p)
        val np = not(p)
        assert(np)
        assert(lit(false))
        // isValid(and(p, np))
        isValid(lit(false))
      }
      println("Done")
    }
  }

  def testSMT(): CppSAIDriver[Int, Unit] = new CppSAIDriver[Int, Unit] {
    def snippet(x: Rep[Int]) = {
      {
        import SyntaxSMT._
        import SyntaxSAT._
        implicit val bw: Int = 32
        val c = bvVar("c")
        val a: Rep[BV] = 5
        val b: Rep[BV] = 6
        handle(isValid((a + b) ≠ c))
      }
      println("Done")
    }
  }
      
  val code = testSAT()
  print(code.code)
  code.eval(0)
}

