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

trait CppSAICodeGenBase extends ExtendedCCodeGen
    with CppCodeGen_List with CppCodeGen_Tuple with CppCodeGen_Map
    with CppCodeGen_Set {
  //override def remap(m: Manifest[_]): String = super.remap(m)

  val SMT_DEBUG = true

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "?", _, _) => false // ternary condition
    case Node(s, "λ", _, _) => false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case n @ Node(s, "P", List(x), _) =>
      emit("std::cout << ")
      shallow(x)
      emit(" << std::endl")
    case _ => super.shallow(n)
  }

  override def primitive(rawType: String): String =
    rawType match {
      case "java.lang.String" => "std::string"
      case "Unit" => "void" //FIXME
      case _ => super.primitive(rawType)
    }

  override def function(sig: List[Manifest[_]]): String = "auto"

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::Backend.Const("val")::_, _) =>
      ???
    case n @ Node(f, "λ", (b: Block)::_, _) =>
      /* Note: generate a declaration with full type annotation first,
       * and then generate the actual closure.
       */
      System.out.println(n)
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes}&)> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      quoteTypedBlock(b, false, true, capture = "&", argMod = Some(List("&")))
      emitln(";")
      //super.traverse(n)
    case _ => super.traverse(n)
  }

  override def convert(arg: String, m: Manifest[_]): String = {
    if (m == manifest[Int]) s"atoi($arg)"
    else if (m == manifest[String]) arg
    else if (m == manifest[Unit]) "0"
    else ???
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

    if (SMT_DEBUG) emitln("VC vc = vc_createValidityChecker();")

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
    |  $name(${convert("argv[1]", m1)});
    |  return 0;
    |}""".stripMargin)
  }
}

abstract class CppSAIDriver[A: Manifest, B: Manifest] extends SAISnippet[A, B] with SAIOps { q =>
  val codegen = new CGenBase with CppSAICodeGenBase {
    val IR: q.type = q
    import IR._
  }

  lazy val (code, statics) = {
    val source = new java.io.ByteArrayOutputStream()
    val statics = codegen.emitSource[A, B](wrapper, "Snippet", new java.io.PrintStream(source))
    (source.toString, statics)
  }

  var compilerCommand = "g++ -std=c++17 -O3 -Winline"

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
    val pb = s"$compilerCommand ./snippet.c -o ./snippet $libraries $includes"
    System.out.println("Compile command: " + pb)

    time("gcc") {
      (pb: ProcessBuilder).lines.foreach(Console.println _)
    }
    (a: A) => {
      System.out.println(s"Running ./snippet $a")
      (s"./snippet $a": ProcessBuilder).lines.foreach(Console.println _)
    }
  }

  def eval(a: A): Unit = {
    val g = f
    time("eval")(g(a))
  }

}

