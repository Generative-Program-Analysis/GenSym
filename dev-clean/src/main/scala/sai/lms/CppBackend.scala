package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import sai.structure.lattices._
import sai.structure.monad._

trait CPP_SAICodeGenBase extends ExtendedCCodeGen
  with CppCodeGen_List {
  override def remap(m: Manifest[_]): String = super.remap(m)

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "λ", _, _) => false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::(Backend.Const("val"))::Nil, _) =>
      super.traverse(n)
    //emitln(s"val ${quote(f)}_val = ${quote(f)} _")
    case _ => super.traverse(n)
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    val efs = ""
    val stt = dce.statics.toList.map(quoteStatic).mkString(", ")

    emitln("""
    |/*****************************************
    |Emitting C Generated Code
    |*******************************************/
    """.stripMargin)

    val src = run(name, ng)
    emitHeaders(stream)
    emitDatastructures(stream)
    emitFunctions(stream)
    emitInit(stream)
    emitln(s"\n/**************** $name ****************/")
    emit(src)
    emitln("""
    |/*****************************************
    |End of C Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  if (argc != 2) {
    |    printf("usage: %s <arg>\n", argv[0]);
    |    return 0;
    |  }""".stripMargin)
    if (initStream.size > 0)
      emitln("if (init()) return 0;")
    emitln(s"""
    |  // TODO: what is the right way to pass arguments?
    |  printf("%d\\n", $name(${convert("argv[1]", m1)}));
    |  return 0;
    |}""".stripMargin)
  }
}

abstract class CPP_SAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIOps { q =>
  val codegen = new CGenBase with CPP_SAICodeGenBase {
    val IR: q.type = q
    import IR._
  }

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource[A, B](wrapper, "Snippet", new java.io.PrintStream(source))
    (source.toString, statics)
  }

  var compilerCommand = "g++ -std=c++17 -O3"

  def libraries = codegen.libraryFlags.mkString(" ")

  lazy val f: A => Unit = {
    val out = new java.io.PrintStream("./snippet.c")
    out.println(code)
    out.close
    (new java.io.File("./snippet")).delete
    import scala.sys.process._

    time("gcc") {
      val pb: ProcessBuilder = s"$compilerCommand ./snippet.c -o ./snippet $libraries -I ../immer"
      pb.lines.foreach(Console.println _)
    }
    (a: A) => (s"/tmp/snippet $a": ProcessBuilder).lines.foreach(Console.println _)
  }

  def eval(a: A): Unit = {
    val g = f
    time("eval")(g(a))
  }


}

