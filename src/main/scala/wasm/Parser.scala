package gensym.wasm.parser

import gensym.wasm.ast._
import gensym.wasm.source._

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

  /* Some helper functions */

  def error = throw new RuntimeException("Unspported")

  def getVar(ctx: BindVarContext): Option[String] =
    if (ctx != null) Some(ctx.VAR().getText) else None

  def getVar(ctx: TypeUseContext): Option[String] =
    if (ctx == null) None
    else Some(getVar(ctx.idx()))

  // Note(GW): in some downstream uses of `getVar`, we cast the value to int. This is not complete wrt the spec.
  def getVar(ctx: IdxContext): String =
    if (ctx.VAR() != null) ctx.VAR().getText
    else ctx.NAT().getText.toString

  def visitMemSize(n: String): PackSize = n match {
    case "8" => Pack8
    case "16" => Pack16
    case "32" => Pack32
    case "64" => Pack64
  }

  def visitSignExt(n: String): Extension = n match {
    case "u" => ZX
    case "s" => SX
  }

  def defaultAlign(ty: NumType, pack: Option[PackSize]): Int = {
    pack match {
      case None => ty.kind match {
        case I32Type => 4
        case I64Type => 8
        case F32Type => 4
        case F64Type => 8
      }
      case Some(Pack8) => 1
      case Some(Pack16) => 2
      case Some(Pack32) => 4
      case Some(Pack64) => 8
    }
  }

  def toNumKind(t: String): NumKind =
    t match {
      case "i32" => I32Type
      case "i64" => I64Type
      case "f32" => F32Type
      case "f64" => F64Type
    }

  def toNumType(t: String): NumType = NumType(toNumKind(t))

  /* Overriding visitors */

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

  override def visitNumType(ctx: NumTypeContext): NumType = toNumType(ctx.VALUE_TYPE().getText)

  override def visitVecType(ctx: VecTypeContext): VecType = VecType(V128Type)

  override def visitRefType(ctx: RefTypeContext): RefType = {
    if (ctx.FUNCREF != null) RefType(FuncRefType)
    else if (ctx.EXTERNREF != null) RefType(ExternRefType)
    else error
  }

  override def visitFuncParamType(ctx: FuncParamTypeContext): WIR = {
    val names = ctx.bindVar().asScala.map(getVar(_)).map {
      case Some(s) => s
      case None => ""
    }
    val types = ctx.valType().asScala.map(visitValType(_)).toSeq.asInstanceOf[Seq[ValueType]]
    FuncType(names, types, Seq())
  }

  override def visitFuncResType(ctx: FuncResTypeContext): WIR = {
    val types = ctx.valType().asScala.map(visitValType(_)).toSeq.asInstanceOf[Seq[ValueType]]
    FuncType(Seq(), Seq(), types)
  }

  override def visitFuncType(ctx: FuncTypeContext): WIR = {
    val FuncType(names, args, _) = visitFuncParamType(ctx.funcParamType())
    val FuncType(_, _, rets) = visitFuncResType(ctx.funcResType())
    FuncType(names, args, rets)
  }

  override def visitTypeDef(ctx: TypeDefContext): WIR = {
    TypeDef(getVar(ctx.bindVar()), visit(ctx.defType().funcType()).asInstanceOf[FuncType])
  }

  override def visitFunction(ctx: FunctionContext): WIR = {
    val name = getVar(ctx.bindVar())
    FuncDef(name, visit(ctx.funcFields).asInstanceOf[FuncField])
  }

  def visitLiteralWithType(ctx: LiteralContext, ty: NumType): Num = {
    if (ctx.NAT != null) {
      ty.kind match {
        case I32Type => I32V(ctx.NAT.getText.toInt)
        case I64Type => I64V(ctx.NAT.getText.toLong)
      }
    } else if (ctx.INT != null) {
      ty.kind match {
        case I32Type => I32V(ctx.NAT.getText.toInt)
        case I64Type => I64V(ctx.NAT.getText.toLong)
      }
    } else if (ctx.FLOAT != null) {
      ty.kind match {
        case F32Type => F32V(ctx.NAT.getText.toFloat)
        case F64Type => F64V(ctx.NAT.getText.toDouble)
      }
    } else error
  }

  override def visitPlainInstr(ctx: PlainInstrContext): WIR = {
    if (ctx.UNREACHABLE() != null) Unreachable
    else if (ctx.NOP() != null) Nop
    else if (ctx.DROP() != null) Drop
    else if (ctx.SELECT() != null) Select(None)
    else if (ctx.BR() != null) Br(getVar(ctx.idx(0)).toInt)
    else if (ctx.BR_IF() != null) BrIf(getVar(ctx.idx(0)).toInt)
    else if (ctx.BR_TABLE() != null) {
      val labels = ctx.idx().asScala.map(getVar(_).toInt).toList
      BrTable(labels.dropRight(1), labels.last)
    }
    else if (ctx.RETURN() != null) Return
    else if (ctx.CALL() != null) Call(getVar(ctx.idx(0)).toInt)
    else if (ctx.LOCAL_GET() != null) LocalGet(getVar(ctx.idx(0)).toInt)
    else if (ctx.LOCAL_SET() != null) LocalSet(getVar(ctx.idx(0)).toInt)
    else if (ctx.LOCAL_TEE() != null) LocalTee(getVar(ctx.idx(0)).toInt)
    else if (ctx.GLOBAL_SET() != null) GlobalGet(getVar(ctx.idx(0)).toInt)
    else if (ctx.GLOBAL_GET() != null) GlobalGet(getVar(ctx.idx(0)).toInt)
    else if (ctx.load() != null) {
      val ty = visitNumType(ctx.load.numType)
      val (memSize, sign) = if (ctx.load.MEM_SIZE() != null) {
        (Some(visitMemSize(ctx.load.MEM_SIZE.getText)),
          Some(visitSignExt(ctx.load.SIGN_POSTFIX.getText)))
      } else (None, None)
      val offset = if (ctx.offsetEq() != null) {
        ctx.offsetEq.NAT.getText.toInt
      } else 0
      val align = if (ctx.alignEq() != null) {
        ctx.alignEq.NAT.getText.toInt
      } else defaultAlign(ty, memSize)
      Load(LoadOp(align, offset, ty, memSize, sign))
    }
    else if (ctx.store() != null) {
      val ty = visitNumType(ctx.load.numType)
      val memSize = if (ctx.load.MEM_SIZE() != null) {
        Some(visitMemSize(ctx.load.MEM_SIZE.getText))
      } else None
      val offset = if (ctx.offsetEq() != null) {
        ctx.offsetEq.NAT.getText.toInt
      } else 0
      val align = if (ctx.alignEq() != null) {
        ctx.alignEq.NAT.getText.toInt
      } else defaultAlign(ty, memSize)
      Store(StoreOp(align, offset, ty, memSize))
    }
    else if (ctx.MEMORY_SIZE() != null) MemorySize
    else if (ctx.MEMORY_GROW() != null) MemoryGrow
    else if (ctx.MEMORY_FILL() != null) MemoryFill
    else if (ctx.MEMORY_COPY() != null) MemoryCopy
    else if (ctx.MEMORY_INIT() != null) MemoryInit(getVar(ctx.idx(0)).toInt)
    else if (ctx.CONST() != null) {
      val ty = visitNumType(ctx.numType)
      Const(visitLiteralWithType(ctx.literal, ty))
    }
    else if (ctx.test() != null) {
      val ty = toNumType(ctx.test.IXX.getText)
      val op =
        if (ctx.test.OP_EQZ != null) Eqz(ty)
        else error
      Test(op)
    }
    else if (ctx.compare() != null) {
      val ty =
        if (ctx.compare.IXX != null) toNumType(ctx.compare.IXX.getText)
        else if (ctx.compare.FXX != null) toNumType(ctx.compare.FXX.getText)
        else error
      val op =
        if (ctx.compare.OP_EQ != null) Eq(ty)
        else if (ctx.compare.OP_NE != null) Ne(ty)
        else if (ctx.compare.OP_LTS != null) LtS(ty)
        else if (ctx.compare.OP_LTU != null) LtU(ty)
        else if (ctx.compare.OP_LEU != null) LeU(ty)
        else if (ctx.compare.OP_LES != null) LeS(ty)
        else if (ctx.compare.OP_GTS != null) GtS(ty)
        else if (ctx.compare.OP_GTU != null) GtU(ty)
        else if (ctx.compare.OP_GEU != null) GeU(ty)
        else if (ctx.compare.OP_GES != null) GeS(ty)
        // Those only defined for floating numbers
        else if (ctx.compare.OP_LT != null) Lt(ty)
        else if (ctx.compare.OP_LE != null) Le(ty)
        else if (ctx.compare.OP_GT != null) Gt(ty)
        else if (ctx.compare.OP_GE != null) Ge(ty)
        else error
      Compare(op)
    }
    else if (ctx.unary() != null) {
      val ty =
        if (ctx.unary.IXX != null) toNumType(ctx.unary.IXX.getText)
        else if (ctx.unary.FXX != null) toNumType(ctx.unary.FXX.getText)
        else error
      val op =
        if (ctx.unary.OP_CLZ != null) Clz(ty)
        else if (ctx.unary.OP_CTZ != null) Ctz(ty)
        else if (ctx.unary.OP_POPCNT != null) Popcnt(ty)
        else if (ctx.unary.OP_NEG != null) Neg(ty)
        else if (ctx.unary.OP_ABS != null) Abs(ty)
        else if (ctx.unary.OP_SQRT != null) Sqrt(ty)
        else if (ctx.unary.OP_CEIL != null) Ceil(ty)
        else if (ctx.unary.OP_FLOOR != null) Floor(ty)
        else if (ctx.unary.OP_TRUNC != null) Trunc(ty)
        else if (ctx.unary.OP_NEAREST != null) Nearest(ty)
        else error
      Unary(op)
    }
    else if (ctx.binary() != null) {
      val ty =
        if (ctx.binary.IXX != null) toNumType(ctx.binary.IXX.getText)
        else if (ctx.binary.FXX != null) toNumType(ctx.binary.FXX.getText)
        else error
      val op =
        if (ctx.binary.OP_ADD != null) Add(ty)
        else if (ctx.binary.OP_SUB != null) Sub(ty)
        else if (ctx.binary.OP_MUL != null) Mul(ty)
        else if (ctx.binary.OP_DIV != null) Div(ty)
        else if (ctx.binary.OP_DIV_S != null) DivS(ty)
        else if (ctx.binary.OP_DIV_U != null) DivU(ty)
        else if (ctx.binary.OP_REM_S != null) RemS(ty)
        else if (ctx.binary.OP_REM_U != null) RemU(ty)
        else if (ctx.binary.OP_AND != null) And(ty)
        else if (ctx.binary.OP_OR != null) Or(ty)
        else if (ctx.binary.OP_XOR != null) Xor(ty)
        else if (ctx.binary.OP_SHL != null) Shl(ty)
        else if (ctx.binary.OP_SHR_S != null) ShrS(ty)
        else if (ctx.binary.OP_SHR_U != null) ShrU(ty)
        else if (ctx.binary.OP_ROTL != null) Rotl(ty)
        else if (ctx.binary.OP_ROTR != null) Rotr(ty)
        else if (ctx.binary.OP_MIN != null) Min(ty)
        else if (ctx.binary.OP_MAX != null) Max(ty)
        else if (ctx.binary.OP_COPYSIGN != null) Copysign(ty)
        else error
      Binary(op)
    }
    else if (ctx.convert() != null) {
      val op =
        if (ctx.convert.OP_WRAP != null) {
          val from = toNumType(ctx.convert.I64.getText)
          val to = toNumType(ctx.convert.I32.getText)
          Wrap(from, to)
        } else if (ctx.convert.OP_TRUNC_ != null) {
          val from = toNumType(ctx.convert.FXX.getText)
          val to = toNumType(ctx.convert.IXX.getText)
          val sign = visitSignExt(ctx.convert.SIGN_POSTFIX.getText)
          TruncTo(from, to, sign)
        } else if (ctx.convert.OP_TRUNC_SAT != null) {
          val from = toNumType(ctx.convert.FXX.getText)
          val to = toNumType(ctx.convert.IXX.getText)
          val sign = visitSignExt(ctx.convert.SIGN_POSTFIX.getText)
          TruncSat(from, to, sign)
        } else if (ctx.convert.OP_EXTEND != null) {
          val from = toNumType(ctx.convert.I32.getText)
          val to = toNumType(ctx.convert.I64.getText)
          val sign = visitSignExt(ctx.convert.SIGN_POSTFIX.getText)
          Extend(from, to, sign)
        } else if (ctx.convert.OP_CONVERT != null) {
          val from = toNumType(ctx.convert.IXX.getText)
          val to = toNumType(ctx.convert.FXX.getText)
          val sign = visitSignExt(ctx.convert.SIGN_POSTFIX.getText)
          ConvertTo(from, to, sign)
        } else if (ctx.convert.OP_DEMOTE != null) {
          val from = toNumType(ctx.convert.F64.getText)
          val to = toNumType(ctx.convert.F32.getText)
          Demote(from, to)
        } else if (ctx.convert.OP_PROMOTE != null) {
          val from = toNumType(ctx.convert.F32.getText)
          val to = toNumType(ctx.convert.F64.getText)
          Promote(from, to)
        } else if (ctx.convert.OP_REINTER != null) {
          // TODO: test this
          val from = ctx.convert.getChild(2).accept[WIR](this).asInstanceOf[NumType]
          val to = ctx.convert.getChild(0).accept[WIR](this).asInstanceOf[NumType]
          Promote(from, to)
        } else error
      Convert(op)
    }
    else error
  }

  override def visitInstrList(ctx: InstrListContext): WIR = {
    val last =
      if (ctx.callIndirectInstr() != null)
        List(visitCallIndirectInstr(ctx.callIndirectInstr()))
      else List()
    val instrs = ctx.instr.asScala.map(visit(_)).map(_.asInstanceOf[Instr]).toList
    FuncBodyDef(null, null, null, instrs ++ last.asInstanceOf[List[Instr]])
  }

  override def visitFuncBody(ctx: FuncBodyContext): WIR = {
    val names = ctx.bindVar().asScala.map(getVar(_)).map {
      case Some(s) => s
      case None => ""
    }
    val types = ctx.valType().asScala.map(visitValType(_)).toSeq
    val FuncBodyDef(_, _, _, instrs) = visit(ctx.instrList())
    FuncBodyDef(null, names, types.asInstanceOf[Seq[ValueType]], instrs)
  }

  override def visitFuncFieldsBody(ctx: FuncFieldsBodyContext): WIR = {
    val ty = visit(ctx.funcType).asInstanceOf[FuncType]
    val FuncBodyDef(_, names, locals, body) = visit(ctx.funcBody)
    FuncBodyDef(ty, names, locals, body)
  }

  override def visitFuncFields(ctx: FuncFieldsContext): WIR = {
    if (ctx.funcFieldsBody() != null) {
      val typeUse = getVar(ctx.typeUse)
      // FIXME: typeUse is not current used
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
