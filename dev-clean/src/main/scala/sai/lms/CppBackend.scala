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

import sai.lmsx.smt._

trait CppSAICodeGenBase extends ExtendedCPPCodeGen
    with CppCodeGen_List with CppCodeGen_Tuple   with CppCodeGen_Map
    with CppCodeGen_Set  with STPCodeGen_SMTBase with STPCodeGen_SMTBV 
    with STPCodeGen_SMTArray {

  //override def remap(m: Manifest[_]): String = super.remap(m)
  registerLibraryPath("../stp/build/lib")

  val SMT_DEBUG = true

  override def quoteBlockPReturn(f: => Unit) = {
    def wraper(numStms: Int, l: Option[Node], y: Block)(f: => Unit) = {
      emitln("{")
      f
      // Return a a monostate value, otherwise g++ -O3 will generate a binary with core dump
      if (y.res == Const(())) es"return std::monostate{};"
      else es"return ${y.res};"
      emitln(quoteEff(y.eff))
      emit("}")
    }
    withWraper(wraper _)(f)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "?", _, _) => false // ternary condition
    case Node(s, "λ", _, _) => false
    case Node(s, "sai-ap-clo", _, _) => false
    case Node(s, "sai-comp-clo", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case n @ Node(s, "P", List(x), _) => es"std::cout << $x << std::endl"
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
    case n @ Node(f, "λ", (b: Block)::Const(0)::_, _) =>
      // Note: top-level functions
      super.traverse(n)
    case n @ Node(f, "λ", (b: Block)::rest, _) =>
      // TODO: what are the rest?
      // TODO: regression test
      /* Note: generate a declaration with full type annotation first,
       * and then generate the actual closure.
       */
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes})> ${quote(f)};")
      // TODO: pass by ref vs pass by val?
      //emitln(s"std::function<$retType(${argTypes})&> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      //quoteTypedBlock(b, false, true, capture = "&", argMod = Some(List.fill(b.in.length)("&")))
      quoteTypedBlock(b, false, true, capture = "&")
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
    |Emitting Generated Code
    |*******************************************/
    """.stripMargin)

    val src = run(name, ng)
    emitHeaders(stream)
    emitln("using namespace immer;")
    emitFunctionDecls(stream)
    emitDatastructures(stream)
    emitFunctions(stream)
    emitInit(stream)

    emitln(s"\n/**************** $name ****************/")
    emit(src)
    emitln("""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  initRand();
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

