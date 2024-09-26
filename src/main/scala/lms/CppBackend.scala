package gensym
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import gensym.lmsx.smt._

trait CppSAICodeGenBase extends ExtendedCPPCodeGen
    with CppCodeGen_List with CppCodeGen_Tuple   with CppCodeGen_Map
    with CppCodeGen_Set  with CppCodeGen_String   with CppCodeGen_Either
    with STPCodeGen_SMTBase with STPCodeGen_SMTBV with STPCodeGen_SMTArray {

  override def remap(m: Manifest[_]): String = {
    val name = m.runtimeClass.getName
    //println(s"name: $name")
    if (name.startsWith("scala.Function")) {
      val ret = remap(m.typeArguments.last)
      val params = m.typeArguments.dropRight(1).map(remap(_)).mkString(", ")
      s"std::function<${ret}($params)>"
    } else if (name.endsWith("Ref")) {
      val kty = m.typeArguments(0)
      s"${remap(kty)}&"
    } else if (name.endsWith("Ptr")) {
      val kty = m.typeArguments(0)
      s"${remap(kty)}*"
    } else super.remap(m)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  override def primitive(t: String): String = t match {
    case "Unit" => "std::monostate"
    case "java.lang.String" => "std::string"
    case "Long" => "int64_t"
    case _ => {
      //println(s"t: $t")
      super.primitive(t)
    }
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(s, "?", _, _) => false
    case Node(s, "λ", _, _) => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case n @ Node(s, "P", List(x), _) => es"std::cout << $x << std::endl"
    case n @ Node(s, "print", List(x), _) => es"std::cout << $x"
    case n @ Node(s, "macro", List(Const(m: String)), _) => emit(m)
    case n @ Node(s, "method-@", obj::method::args, _) =>
      shallow(obj)
      emit(s".$method(")
      args.headOption.foreach(h => {
        shallow(h)
        args.tail.foreach(a => { emit(", "); shallow(a) })
      })
      emit(")")
    case n @ Node(s, "field-@", obj::Const(field: String)::Nil, _) =>
      es"$obj.$field"
    case n @ Node(s, "field-assign", obj::Const(field: String)::rhs::Nil, _) =>
      es"$obj.$field = $rhs"
    case n @ Node(s, "ptr-field-@", obj::Const(field: String)::Nil, _) =>
      es"$obj->$field"
    case n @ Node(s, "ptr-field-assign", obj::Const(field: String)::rhs::Nil, _) =>
      es"$obj->$field = $rhs"
    case _ => super.shallow(n)
  }

  override def function(sig: List[Manifest[_]]): String = "auto"

  override def quoteBlockPReturn(f: => Unit) = {
    def wraper(numStms: Int, l: Option[Node], y: Block)(f: => Unit) = {
      emitln("{")
      f
      es"return ${y.res};"
      emitln(quoteEff(y.eff))
      emit("}")
    }
    withWraper(wraper _)(f)
  }

  override def traverse(n: Node): Unit = n match {
    case n @ Node(f, "λ", (b: Block)::Const("val")::_, _) => ???
    case n @ Node(f, "top-λ", (b: Block)::Const(0)::Const(dec: String)::Nil, _) =>
      // Note: top-level functions
      registerTopLevelFunctionDecl(quote(f)) {
        emitFunctionSignature(quote(f), b, argNames = false, ending = ";\n")
      }
      registerTopLevelFunction(quote(f)) {
        emitFunction(quote(f), b, dec)
      }
    case n @ Node(f, "λ", (b: Block)::Const(0)::rest, _) =>
      super.traverse(n)
    case n @ Node(f, "λ", (b: Block)::rest, _) =>
      // TODO: what are the rest?
      // TODO: regression test
      /* Note: First, generate a function declaration with full type annotation,
       * and then generate the actual closure.
       */
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes})> ${quote(f)};")
      // TODO: pass by ref vs pass by val?
      //emitln(s"std::function<$retType(${argTypes})&> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      quoteTypedBlock(b, false, true, capture = "=")
      emitln(";")
    case _ => super.traverse(n)
  }

  override def convert(arg: String, m: Manifest[_]): String = {
    if (m == manifest[Int]) s"atoi($arg)"
    else if (m == manifest[String]) arg
    else if (m == manifest[Unit]) "0"
    else {
      println(m)
      ???
    }
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
    //emitln("using namespace immer;")
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
      Process(s"./$bin $a").lines.foreach(Console.println _)
    }
  }

  def eval(a: A): Unit = {
    val g = saveCompileRun("snippet.c")
    time("eval")(g(a))
  }

}

