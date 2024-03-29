package gensym.wasm.parser

import gensym.wasm.ast._
import gensym.wasm.source._
import gensym.wasm.types._
import gensym.wasm.values._

import scala.util.parsing.combinator._
import scala.util.parsing.input.Positional
import scala.util.matching.Regex
import scala.language.postfixOps

trait Unresolved
case class CallUnresolved(name: String) extends Instr with Unresolved

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

object Parser extends Parser {
  def parseString(code: String): Module = {
    parseAll(module, code) match {
      case Success(result, _) =>
        result
      case Failure(msg,_) =>
        throw new Exception(s"FAILURE: $msg")
      case Error(msg,_) =>
        throw new Exception(s"ERROR: $msg")
    }
  }
}
