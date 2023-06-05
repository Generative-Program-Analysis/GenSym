package gensym.wasm.parser

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.values._

import scala.util.parsing.combinator._
import scala.util.parsing.input.Positional
import scala.util.matching.Regex
import scala.language.postfixOps

import scala.annotation.tailrec
import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import gensym.wasm._

/*
class Parser extends RegexParsers {
  override def skipWhitespace = true
  // override val whiteSpace: Regex = "(\\s|.*|\\(.*\\))+".r

  def resolveCalls(instr: Instr): Instr = instr match {
    case CallUnresolved(name) => Call(fnMap(name))
    case Block(label, instrs) => Block(label, instrs.map(resolveCalls))
    case Loop(label, instrs) => Loop(label, instrs.map(resolveCalls))
    case _ => instr
  }

  def isValid(instrs: Seq[Instr]): Boolean = {
    def isValidInstr(instr: Instr): Boolean = instr match {
      case CallUnresolved(_) => false
      case Block(_, instrs) => isValid(instrs)
      case Loop(_, instrs) => isValid(instrs)
      case _ => true
    }

    instrs.forall(isValidInstr)
  }

  var fnMap: Map[String, Int] = Map()

  def int: Parser[Long] = """-?[0-9]+""".r ^^ { _.toLong }
  def float: Parser[Float] = """[0-9]+\.[0-9]+""".r ^^ { _.toFloat }
  def name: Parser[String] = """[@$a-zA-Z_][a-zA-Z0-9_]*""".r

  def module: Parser[Module] = {
    "(" ~> "module" ~> rep(definition) <~ ")" ^^ { Module(_) }
  }

  def definition: Parser[Definition] = {
    (funcDef | typeDef)
  }

  // TODO: this is actually a comment but it's helpful
  // since wasm2wat does output the comment consistently
  // it should work for all rustc output
  def id: Parser[Long] = "(;" ~> int <~ ";)"

  // TODO: figure out how to ignore
  def comment = "(;" ~> "[^;]*".r <~ ";)"
  def lineComment = ";;" ~> "[^\n]*".r

  def params: Parser[Seq[ValueType]] = "(" ~> "param" ~> rep(valueType) <~ ")"
  def results: Parser[Seq[ValueType]] = "(" ~> "result" ~> rep(valueType) <~ ")"

  def locals: Parser[Seq[ValueType]] = "(" ~> "local" ~> rep(valueType) <~ ")"

  def funcDef: Parser[FuncDef] =
    ("(" ~> "func" ~> name ~ "(type" ~ int ~ ")" ~ opt(params) ~ opt(results) ~ opt(locals) ~ rep(instr) <~ ")" ^^ {
      case funcName ~ _ ~ _typeId ~ _ ~ params ~ results ~ locals ~ body => {
        fnMap = fnMap + (funcName -> fnMap.size)
        val resolvedBody = body.map(resolveCalls)
        FuncDef(
          funcName,
          FuncType(params.getOrElse(Seq()),
            results.getOrElse(Seq())),
          locals.getOrElse(Seq()),
          resolvedBody
        )
      }
    })

  def typeDef: Parser[TypeDef] =
    "(" ~> "type" ~> id ~ valueType <~ ")" ^^ { case id ~ tipe => TypeDef(id.toInt, tipe) }

  def numType: Parser[NumType] = {
    ("i32" ^^ { _ => I32Type } |
      "i64" ^^ { _ => I64Type } |
      "f32" ^^ { _ => F32Type } |
      "f64" ^^ { _ => F64Type }) ^^ { NumType(_) }
  }

  def funcType: Parser[FuncType] = {
    ("(" ~> "func" ~> opt(params) ~ opt(results) <~ ")" ^^ {
      case params ~ results => FuncType(params.getOrElse(Seq()), results.getOrElse(Seq()))
    })
  }

  def label: Parser[String] = { "label" ~ "=" ~> name }

  def valueType: Parser[ValueType] =
    ((numType ^^ { _.asInstanceOf[ValueType] }) |
      (funcType ^^ { _.asInstanceOf[ValueType] }))

  def offset: Parser[Int] = { "offset=" ~> int } ^^ { _.toInt }
  def align: Parser[Int] = { "align=" ~> int } ^^ { _.toInt }

  def instr: Parser[Instr] = {
    ("unreachable" ^^ { _ => Unreachable } |
      "nop" ^^ { _ => Nop } |
      "return" ^^ { _ => Return } |
      "local.get" ~> int ^^ { _.toInt } ^^ { LocalGet(_) } |
      "local.set" ~> int ^^ { _.toInt } ^^ { LocalSet(_) } |
      "i32.const" ~> int ^^ { _.toInt } ^^ { x => Const(I32(x)) } |
      "i64.const" ~> int ^^ { _.toLong } ^^ { x => Const(I64(x)) } |
      "f32.const" ~> float ^^ { _.toFloat } ^^ { x => Const(F32(x)) } |
      "f64.const" ~> float ^^ { _.toDouble } ^^ { x => Const(F64(x)) } |
      "i32.add" ^^ { _ => Binary(BinOp.Int(Add)) } |
      "i32.sub" ^^ { _ => Binary(BinOp.Int(Sub)) } |
      "i32.mul" ^^ { _ => Binary(BinOp.Int(Mul)) } |
      "i32.eqz" ^^ { _ => Test(TestOp.Int(Eqz)) } |
      "call" ~> name ^^ { CallUnresolved(_) } |
      (numType ~ ".store" ~ offset ~ opt(align)) ^^ {
        case tipe ~ _ ~ offset ~ align => Store(StoreOp(align.getOrElse(0), offset, tipe, None))
      } |
      (numType ~ ".load" ~ offset ~ opt(align)) ^^ {
        case tipe ~ _ ~ offset ~ align => Load(LoadOp(align.getOrElse(0), offset, tipe, None, None))
      } |
      ("global.get" ~> int ^^ { _.toInt }) ^^ { x => GlobalGet(x) } |
      ("block" ~ lineComment ~> rep(instr) <~ "end") ^^ {
        case instrs => {
          // TODO: add label to module, figure out type
          Block(ValBlockType(None), instrs)
        }
      } |
      ("loop" ~ ";;" ~ label ~ rep(instr) <~ "end") ^^ {
        case _ ~ _ ~ label ~ instrs => {
          Loop(ValBlockType(None), instrs)
        }
      } |
      ("br" ~> int <~ comment) ^^ {
        case label => Br(label.toInt)
      } |
      ("br_if" ~> int <~ comment) ^^ {
        case label => BrIf(label.toInt)
      }
      )
  }
 }
 */

class GSWasmVisitor extends WatParserBaseVisitor[WIR] {
  import WatParser._
  def error = throw new RuntimeException("Unspported")

  def getVar(ctx: BindVarContext): Option[String] =
    if (ctx != null) Some(ctx.VAR().getText) else None

  def getVar(ctx: TypeUseContext): Option[String] = {
    if (ctx == null) return None
    val varCtx = ctx.var_()
    if (varCtx.VAR() != null) Some(varCtx.VAR().getText)
    else Some(varCtx.NAT().getText.toString)
  }

  override def visitModule(ctx: ModuleContext): WIR = {
    if (ctx.module_() != null) return visit(ctx.module_())
    Module(None, ctx.moduleField.asScala.map(visitModuleField(_)).asInstanceOf[Seq[Definition]])
  }

  override def visitModule_(ctx: Module_Context): WIR = {
    val name = if (ctx.VAR() != null) Some(ctx.VAR().getText) else None
    Module(name, ctx.moduleField.asScala.map(visitModuleField(_)).asInstanceOf[Seq[Definition]])
  }

  override def visitModuleField(ctx: ModuleFieldContext): WIR = {
    visitChildren(ctx)
  }

  override def visitFuncType(ctx: FuncTypeContext): WIR = {
    ???
  }

  override def visitTypeDef(ctx: TypeDefContext): WIR = {
    TypeDef(getVar(ctx.bindVar()), visit(ctx.type_()).asInstanceOf[ValueType])
  }

  override def visitFunc_(ctx: Func_Context): WIR = {
    val name = getVar(ctx.bindVar())
    FuncDef(name, visit(ctx.funcFields).asInstanceOf[FuncField])
  }

  override def visitFuncFieldsBody(ctx: FuncFieldsBodyContext): WIR = {
    ???
  }

  override def visitFuncFields(ctx: FuncFieldsContext): WIR = {
    if (ctx.funcFieldsBody() != null) {
      val typeUse = getVar(ctx.typeUse)
      // TODO: typeUse is not current used
      visit(ctx.funcFieldsBody)
    } else if (ctx.inlineImport() != null) {
      ???
    } else if (ctx.inlineExport() != null) {
      ???
    } else error
  }
}

object Parser {
  def parse(input: String): Module = {
    val charStream = new ANTLRInputStream(input)
    val lexer = new WatLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new WatParser(tokens)
    val visitor = new GSWasmVisitor()
    val res: Module  = visitor.visit(parser.module).asInstanceOf[Module]
    res
  }

  def parseFile(filepath: String): Module = parse(scala.io.Source.fromFile(filepath).mkString)
}
