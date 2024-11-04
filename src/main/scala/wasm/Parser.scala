package gensym.wasm.parser

import gensym.wasm.ast._
import gensym.wasm.source._

import scala.util.Try
import scala.util.parsing.combinator._
import scala.util.parsing.input.Positional
import scala.util.matching.Regex
import scala.language.postfixOps
import scala.annotation.tailrec
import org.antlr.v4.runtime._

import scala.collection.JavaConverters._
import collection.mutable.{HashMap, ListBuffer}
import gensym.wasm._

import java.io.OutputStream


import scala.collection.mutable

class GSWasmVisitor extends WatParserBaseVisitor[WIR] {
  import WatParser._

  /* Some helper functions */

  val fnMap: HashMap[String, Int] = HashMap()

  // Note: we construct a mapping from indices to function-like definitions, which helps
  // function call resolution in the later phase.
  // TODO: instead of using WIR, define a trait for function-like definitions
  val fnMapInv: HashMap[Int, WIR] = HashMap()

  def error = throw new RuntimeException("Unspported")

  def getVar(ctx: BindVarContext): Option[String] =
    if (ctx != null) Some(ctx.VAR().getText) else None

  def getVar(ctx: TypeUseContext): Option[String] =
    if (ctx == null) None
    else Some(getVar(ctx.idx()))

  // Note(GW): in some downstream uses of `getVar`, we cast the value to int.
  // This is not complete wrt the spec.
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
    Module(None, ctx.moduleField.asScala.toList.map(visitModuleField(_)).asInstanceOf[List[Definition]], fnMapInv)
  }

  override def visitModule_(ctx: Module_Context): WIR = {
    val name = if (ctx.VAR() != null) Some(ctx.VAR().getText) else None
    Module(name, ctx.moduleField.asScala.toList.map(visitModuleField(_)).asInstanceOf[List[Definition]], fnMapInv)
  }

  override def visitModuleField(ctx: ModuleFieldContext): WIR = {
    visitChildren(ctx)
  }

  override def visitStart_(ctx: Start_Context): WIR = {
    val id = getVar(ctx.idx())
    Start(id.toInt)
  }

  override def visitNumType(ctx: NumTypeContext): NumType = toNumType(ctx.VALUE_TYPE().getText)

  override def visitVecType(ctx: VecTypeContext): VecType = VecType(V128Type)

  override def visitRefType(ctx: RefTypeContext): RefType = {
    if (ctx.FUNCREF != null) RefType(FuncRefType)
    else if (ctx.EXTERNREF != null) RefType(ExternRefType)
    else if (ctx.REF != null) {
      RefType(RefFuncType(getVar(ctx.idx).toInt))
    }
    else error
  }

  override def visitFuncParamType(ctx: FuncParamTypeContext): WIR = {
    val names = ctx.bindVar().asScala.toList.map(getVar(_)).map {
      case Some(s) => s
      case None => ""
    }
    val types = ctx.valType().asScala.map(visitValType(_)).toList.asInstanceOf[List[ValueType]]
    FuncType(names, types, List())
  }

  override def visitFuncResType(ctx: FuncResTypeContext): WIR = {
    val types = ctx.valType().asScala.map(visitValType(_)).toList.asInstanceOf[List[ValueType]]
    FuncType(List(), List(), types)
  }

  override def visitFuncType(ctx: FuncTypeContext): FuncType = {
    val FuncType(names, args, _) = visitFuncParamType(ctx.funcParamType())
    val FuncType(_, _, rets) = visitFuncResType(ctx.funcResType())
    FuncType(names, args, rets)
  }

  override def visitTypeDef(ctx: TypeDefContext): WIR = {
    if (ctx.defType.FUNC != null) {
      TypeDef(getVar(ctx.bindVar()), visit(ctx.defType.funcType).asInstanceOf[FuncType])
    } else if (ctx.defType.CONT != null) {
      // TODO: here, the getVar is more link the typeUse one, although it uses the IdxContext one
       TypeDef(getVar(ctx.bindVar()), ContType(getVar(ctx.defType.idx).toInt))
    } else {
      error
    }
  }

  override def visitFunction(ctx: FunctionContext): FuncDef = {
    val name = getVar(ctx.bindVar())
    name match {
      case Some(realName) => fnMap(realName) = fnMap.size
      case _ =>
        println(s"[Parser] Warning: unnamed function at ${fnMap.size}")
        fnMap(s"UNNAMED_${fnMap.size}") = fnMap.size
    }
    val funcField = visit(ctx.funcFields).asInstanceOf[FuncField]
    val f = FuncDef(name, funcField)
    fnMapInv(fnMapInv.size) = f
    f
  }

  override def visitSimport(ctx: SimportContext): WIR = {
    val module = ctx.name(0).getText.substring(1).dropRight(1)
    val name = ctx.name(1).getText.substring(1).dropRight(1)
    val desc = visitImportDesc(ctx.importDesc).asInstanceOf[ImportDesc]
    val im = Import(module, name, desc)
    if (desc.isInstanceOf[ImportFuncTyUse] || desc.isInstanceOf[ImportFuncTy]) {
      fnMapInv(fnMapInv.size) = im
    }
    im
  }

  override def visitImportDesc(ctx: ImportDescContext): WIR = {
    val name: Option[String] = getVar(ctx.bindVar)
    if (ctx.typeUse != null) {
      val typeUse = getVar(ctx.typeUse).get.toInt
      ImportFuncTyUse(name, typeUse)
    } else if (ctx.funcType != null) {
      val t = visit(ctx.funcType).asInstanceOf[FuncType]
      ImportFuncTy(name, t)
    } else if (ctx.tableType != null) {
      ???
    } else if (ctx.memoryType != null) {
      ???
    } else if (ctx.globalType != null) {
      ???
    } else
      ???
  }

  // TODO: This doesn't seems quite correct
  def parseHexFloat(text: String): Float = {
    if (text.startsWith("0x") || text.startsWith("-0x") || text.startsWith("+0x")) {
      // Remove optional sign and "0x" prefix
      val cleanText = text.replaceFirst("^[+-]?0x", "")
      // why removing the seemling irrelevant following two lines will effect
      // the value being parsed?
      val value: Float =  BigDecimal(text).floatValue
      print(f"cleanText = $cleanText, value = $value\n")

      val Array(mantissa, exponent) = cleanText.split("p", 2)

      // Convert mantissa and exponent
      val mantissaValue = java.lang.Float.intBitsToFloat(java.lang.Integer.parseUnsignedInt(mantissa.replace(".", ""), 16))
      val exponentValue = Math.pow(2, exponent.toInt).toFloat
      // print(s"mantissaValue = $mantissaValue, exponentValue = $exponentValue\n")
      mantissaValue * exponentValue
    } else {
      text.toFloat // Fall back to regular decimal parsing
    }
  }


  def visitLiteralWithType(ctx: LiteralContext, ty: NumType): Num = {
    if (ctx.NAT != null) {
      ty.kind match {
        case I32Type => {
          if (ctx.NAT.getText.startsWith("0x")) {
            I32V(Integer.parseInt(ctx.NAT.getText.substring(2), 16))
          } else {
            I32V(ctx.NAT.getText.toInt)
          }
        }
        case I64Type => {
          if (ctx.NAT.getText.startsWith("0x")) {
            I64V(java.lang.Long.parseLong(ctx.NAT.getText.substring(2), 16))
          } else {
            I64V(ctx.NAT.getText.toLong)
          }
        }
      }
    } else if (ctx.INT != null) {
      ty.kind match {
        case I32Type => {
          if (ctx.INT.getText.startsWith("0x")) {
            I32V(Integer.parseInt(ctx.INT.getText.substring(2), 16))
          } else {
            I32V(ctx.INT.getText.toInt)
          }
        }
        case I64Type => {
          if (ctx.INT.getText.startsWith("0x")) {
            I64V(java.lang.Long.parseLong(ctx.INT.getText.substring(2), 16))
          } else {
            I64V(ctx.INT.getText.toLong)
          }
        }
      }
    // TODO: parsing support for hex representation for f32/f64 not quite there yet
    } else if (ctx.FLOAT != null) {
      ty.kind match {
        case F32Type => 
          val parsedValue = Try(parseHexFloat(ctx.FLOAT.getText).toFloat).getOrElse(ctx.FLOAT.getText.toFloat)
          F32V(parsedValue)
          
        case F64Type => 
          // TODO: not processed at all
          val parsedValue = ctx.FLOAT.getText.toDouble
          F64V(parsedValue)
      }
    }
    else error
  }

  override def visitPlainInstr(ctx: PlainInstrContext): Instr = {
    if (ctx.UNREACHABLE() != null) Unreachable
    else if (ctx.NOP() != null) Nop
    else if (ctx.DROP() != null) Drop
    else if (ctx.selectInstr() != null) Select(None)
    else if (ctx.BR() != null) {
      var id = getVar(ctx.idx(0))
      Br(id.toInt)
      // try Br(id.toInt) catch { case _: java.lang.NumberFormatException => BrUnresolved(id) }
    }
    else if (ctx.BR_IF() != null) BrIf(getVar(ctx.idx(0)).toInt)
    else if (ctx.BR_TABLE() != null) {
      val labels = ctx.idx().asScala.map(getVar(_).toInt).toList
      BrTable(labels.dropRight(1), labels.last)
    }
    else if (ctx.RETURN() != null) Return
    else if (ctx.CALL() != null) {
      val id = getVar(ctx.idx(0))
      try Call(id.toInt) catch {
        case _: java.lang.NumberFormatException =>
          if (fnMap.contains(id)) Call(fnMap(id))
          else CallUnresolved(id)
      }
    }
    else if (ctx.RETURN_CALL() != null) {
      val id = getVar(ctx.idx(0))
      try Call(id.toInt) catch {
        case _: java.lang.NumberFormatException =>
          if (fnMap.contains(id)) ReturnCall(fnMap(id))
          else CallUnresolved(id)
      }
    }
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
      val ty = visitNumType(ctx.store.numType)
      val memSize = if (ctx.store.MEM_SIZE() != null) {
        Some(visitMemSize(ctx.store.MEM_SIZE.getText))
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
    else if (ctx.CONST != null) {
      val Array(ty, _) = ctx.CONST.getText.split("\\.")
      Const(visitLiteralWithType(ctx.literal, toNumType(ty)))
    }
    else if (ctx.TEST != null) {
      val Array(ty, _) = ctx.TEST.getText.split("\\.")
      Test(Eqz(toNumType(ty)))
    }
    else if (ctx.COMPARE != null) {
      val Array(tyStr, opStr) = ctx.COMPARE.getText.split("\\.")
      val ty = toNumType(tyStr)
      val op = opStr match {
        case "eq" => Eq(ty)
        case "ne" => Ne(ty)
        case "lt_s" => LtS(ty)
        case "lt_u" => LtU(ty)
        case "le_u" => LeU(ty)
        case "le_s" => LeS(ty)
        case "gt_s" => GtS(ty)
        case "gt_u" => GtU(ty)
        case "ge_u" => GeU(ty)
        case "ge_s" => GeS(ty)
        // Those only defined for floating numbers
        case "lt" => Lt(ty)
        case "le" => Le(ty)
        case "gt" => Gt(ty)
        case "ge" => Ge(ty)
        case _ => error
      }
      Compare(op)
    }
    else if (ctx.UNARY != null) {
      val Array(tyStr, opStr) = ctx.COMPARE.getText.split("\\.")
      val ty = toNumType(tyStr)
      val op = opStr match {
        case "clz" => Clz(ty)
        case "ctz" => Ctz(ty)
        case "popcnt" => Popcnt(ty)
        case "neg" => Neg(ty)
        case "abs" => Abs(ty)
        case "sqrt" => Sqrt(ty)
        case "ceil" => Ceil(ty)
        case "floor" => Floor(ty)
        case "trunc" => Trunc(ty)
        case "nearest" => Nearest(ty)
        case _ => error
      }
      Unary(op)
    }
    else if (ctx.BINARY != null) {
      val Array(tyStr, opStr) = ctx.BINARY.getText.split("\\.")
      val ty = toNumType(tyStr)
      val op = opStr match {
        case "add" => Add(ty)
        case "sub" => Sub(ty)
        case "mul" => Mul(ty)
        case "div" => Div(ty)
        case "div_s" => DivS(ty)
        case "div_u" => DivU(ty)
        case "rem_s" => RemS(ty)
        case "rem_u" => RemU(ty)
        case "and" => And(ty)
        case "or" => Or(ty)
        case "xor" => Xor(ty)
        case "shl" => Shl(ty)
        case "shr_s" => ShrS(ty)
        case "shr_u" => ShrU(ty)
        case "rotl" => Rotl(ty)
        case "rotr" => Rotr(ty)
        case "min" => Min(ty)
        case "max" => Max(ty)
        case "copysign" => Copysign(ty)
        case _ => error
      }
      Binary(op)
    }
    else if (ctx.CONVERT != null) {
      val Array(toTyStr, rest) = ctx.CONVERT.getText.split("\\.")
      val toTy = toNumType(toTyStr)
      val Array(opStr, fromTySign) = rest.split("_", 2)
      val op = opStr match {
        case "wrap" =>
          val fromTy = toNumType(fromTySign)
          Wrap(fromTy, toTy)
        case "trunc" =>
          // TODO: handle trunc_sat instr, split by "_" current not work for that
          val Array(fromTyStr, signStr) = fromTySign.split("_")
          val fromTy = toNumType(fromTyStr)
          val sign = visitSignExt(signStr)
          TruncTo(fromTy, toTy, sign)
        case "extend" =>
          val Array(fromTyStr, signStr) = fromTySign.split("_")
          val fromTy = toNumType(fromTyStr)
          val sign = visitSignExt(signStr)
          Extend(fromTy, toTy, sign)
        case "convert" =>
          val Array(fromTyStr, signStr) = fromTySign.split("_")
          val fromTy = toNumType(fromTyStr)
          val sign = visitSignExt(signStr)
          ConvertTo(fromTy, toTy, sign)
        case "demote" =>
          val fromTy = toNumType(fromTySign)
          Demote(fromTy, toTy)
        case "promote" =>
          val fromTy = toNumType(fromTySign)
          Promote(fromTy, toTy)
        case "reinterpret" =>
          val fromTy = toNumType(fromTySign)
          Reinterpret(fromTy, toTy)
      }
      Convert(op)
     } else if (ctx.SYM_ASSERT != null) {
       SymAssert
     } else if (ctx.SYMBOLIC != null) {
       val Array(ty, _) = ctx.SYMBOLIC.getText.split("\\.")
       Symbolic(toNumType(ty))
     } else if (ctx.callIndirectInstr() != null) {
       val instr = ctx.callIndirectInstr()
       val idx = if (instr.idx != null) instr.idx.getText.toInt else 0
       val typeUse = getVar(instr.typeUse).get.toInt
       CallIndirect(typeUse, idx)
     } else if (ctx.ALLOC != null) {
       Alloc
     } else if (ctx.FREE != null) {
       Free
     } else if (ctx.SUSPEND != null) {
        Suspend(getVar(ctx.idx(0)).toInt)
     } else if (ctx.CONTNEW != null) {
        ContNew(getVar(ctx.idx(0)).toInt)
     } else if (ctx.REFFUNC != null) {
        RefFunc(getVar(ctx.idx(0)).toInt)
     }
    else {
      println(s"unimplemented parser for: ${ctx.getText}")
      error
    }
  }

  override def visitBlockType(ctx: BlockTypeContext): BlockType = {
    if (ctx.typeUse != null) {
      // TODO: explicit type use
      val tyIndex = -1
      val funcType = visitFuncType(ctx.funcType)
      VarBlockType(tyIndex, Some(funcType))
    } else if (ctx.funcType != null){
      // abbreviation form
      val ty = visitFuncType(ctx.funcType)
      // TODO: append ty to the type definition list of parsing context, when necessarily
      VarBlockType(-1, Some(ty))
    }
    else {
      // just one explicit result type
      if (ctx.valType != null) {
        ValBlockType(Some(visitValType(ctx.valType()).asInstanceOf[ValueType]))
      } else {
        ValBlockType(None)
      }
    }
  }

  override def visitHandlerInstr(ctx: HandlerInstrContext): Handler = {
    val tagId = getVar(ctx.idx(0)).toInt
    val onYieldBlockId = getVar(ctx.idx(1)).toInt
    Handler(tagId, onYieldBlockId)
  }

  override def visitResumeInstr(ctx: ResumeInstrContext): WIR = {
    val funcTypeId = getVar(ctx.idx).toInt
    val handlers = ctx.handlerInstr().asScala.map(visitHandlerInstr).toList
    Resume(funcTypeId, handlers)
  }

  override def visitBlock(ctx: BlockContext): WIR = {
    val ty = visitBlockType(ctx.blockType())
    val InstrList(instrs) = visit(ctx.instrList)
    Block(ty, instrs)
  }

  override def visitBlockInstr(ctx: BlockInstrContext): WIR = {
    // Note: ignoring all bindVar/label...
    if (ctx.BLOCK != null) {
      visit(ctx.block)
    } else if (ctx.LOOP != null) {
      val Block(ty, instrs) = visit(ctx.block)
      Loop(ty, instrs)
    } else if (ctx.IF != null) {
      val Block(ty, thn) = visit(ctx.block)
      // Note(GW): `else` branch seems mandatory?
      // https://webassembly.github.io/spec/core/text/instructions.html#control-instructions
      val els = if (ctx.ELSE != null) {
        val InstrList(elsInstr) = visit(ctx.instrList)
        elsInstr
      } else List()
      If(ty, thn, els)
    } else error
  }

  override def visitFoldedInstr(ctx: FoldedInstrContext): WIR = visit(ctx.expr)

  override def visitExpr(ctx: ExprContext): WIR = {
    if (ctx.plainInstr != null) {
      val inst = visitPlainInstr(ctx.plainInstr)
      val rest = ctx.expr.asScala.map(visitExpr).toList.asInstanceOf[List[Instr]]
      // Could desugar to sequence of instructions
      // XXX: is it right?
      // https://webassembly.github.io/spec/core/text/instructions.html#folded-instructions
      InstrList(rest ++ List(inst))
    } else if (ctx.CALL_INDIRECT != null) {
      // Seems not included in the current WASM spec?
      ???
    } else if (ctx.BLOCK != null) {
      // Note: ignoring all bindVar/label
      visit(ctx.block)
    } else if (ctx.LOOP != null) {
      val Block(ty, instrs) = visit(ctx.block)
      Loop(ty, instrs)
    } else if (ctx.IF != null) {
      val cnd = ctx.foldedInstr.asScala.map(visit).foldLeft(List[Instr]()) {
        case (acc, inst: Instr) => acc ++ List(inst)
        case (acc, InstrList(instrs)) => acc ++ instrs
      }
      val ty = visitBlockType(ctx.blockType())
      val InstrList(thn) = visit(ctx.instrList(0))
      val els = if (ctx.ELSE != null) {
        val InstrList(elsInstr) = visit(ctx.instrList(1))
        elsInstr
      } else List()
      InstrList(cnd ++ List(If(ty, thn, els)))
    } else error
  }

  override def visitInstrList(ctx: InstrListContext): WIR = {
    val last =
      if (ctx.callIndirectInstr() != null)
        List(visitCallIndirectInstr(ctx.callIndirectInstr()))
      else List()
    val instrs = ctx.instr.asScala.map(visit(_)).foldLeft(List[Instr]()) {
      case (acc, inst: Instr) => acc ++ List(inst)
      case (acc, InstrList(instrs)) => acc ++ instrs
    }
    // Note: callIndirectInstr has not been handled (should it?)
    InstrList(instrs ++ last.asInstanceOf[List[Instr]])
  }

  override def visitFuncBody(ctx: FuncBodyContext): WIR = {
    val names = ctx.bindVar().asScala.toList.map(getVar(_)).map {
      case Some(s) => s
      case None => "?"
    }
    val types = ctx.valType().asScala.map(visitValType(_)).toList
    val InstrList(instrs) = visit(ctx.instrList())
    FuncBodyDef(null, names, types.asInstanceOf[List[ValueType]], instrs)
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

  override def visitTableType(ctx: TableTypeContext): TableType = {
    val (n, m) =
      if (ctx.NAT.size == 1) (ctx.NAT(0).getText.toInt, None)
      else (ctx.NAT(0).getText.toInt, Some(ctx.NAT(1).getText.toInt))
    val ty = visitRefType(ctx.refType)
    TableType(n, m, ty)
  }

  override def visitTableField(ctx: TableFieldContext): TableField = {
    if (ctx.tableType != null && ctx.inlineImport == null) visitTableType(ctx.tableType)
    else if (ctx.inlineImport != null) {
      ???
    } else if (ctx.inlineExport != null) {
      ???
    } else if (ctx.refType != null) {
      ???
    } else error
  }

  override def visitTable(ctx: TableContext): Table = {
    val name: Option[String] = getVar(ctx.bindVar)
    val field = visitTableField(ctx.tableField)
    Table(name, field)
  }

  override def visitMemoryType(ctx: MemoryTypeContext): MemoryType = {
    val (n, m) =
      if (ctx.NAT.size == 1) (ctx.NAT(0).getText.toInt, None)
      else (ctx.NAT(0).getText.toInt, Some(ctx.NAT(1).getText.toInt))
    MemoryType(n, m)
  }

  override def visitMemoryField(ctx: MemoryFieldContext): MemoryField = {
    if (ctx.memoryType != null && ctx.inlineImport == null) visitMemoryType(ctx.memoryType)
    else if (ctx.inlineImport != null) ???
    else if (ctx.inlineExport != null) ???
    else error
  }

  override def visitMemory(ctx: MemoryContext): Memory = {
    val name: Option[String] = getVar(ctx.bindVar)
    val field = visitMemoryField(ctx.memoryField)
    Memory(name, field)
  }

  override def visitGlobalType(ctx: GlobalTypeContext): GlobalType = {
    val ty = visitValType(ctx.valType).asInstanceOf[ValueType]
    val mut = ctx.MUT != null
    GlobalType(ty, mut)
  }

  override def visitGlobalField(ctx: GlobalFieldContext): GlobalField = {
    if (ctx.constExpr != null) {
      val ty = visitGlobalType(ctx.globalType)
      val InstrList(instrs) = visit(ctx.constExpr)
      GlobalValue(ty, instrs)
    } else if (ctx.inlineImport != null) ???
    else if (ctx.inlineExport != null) ???
    else error
  }

  override def visitGlobal(ctx: GlobalContext): Global = {
    val name: Option[String] = getVar(ctx.bindVar)
    val field = visitGlobalField(ctx.globalField)
    Global(name, field)
  }

  override def visitExport_(ctx: Export_Context): WIR = {
    val name: String  = ctx.name.getText.substring(1).dropRight(1)
    val desc = visitExportDesc(ctx.exportDesc).asInstanceOf[ExportDesc]
    Export(name, desc)
  }

  override def visitExportDesc(ctx: ExportDescContext): WIR = {
    val id = if (ctx.idx.VAR() != null) {
      println(s"Warning: we don't support labeling yet")
      throw new RuntimeException("Unsupported")
    } else {
      getVar(ctx.idx()).toInt
    }
    if (ctx.FUNC != null) ExportFunc(id)
    else if (ctx.TABLE != null)  ExportTable(id)
    else if (ctx.MEMORY != null)  ExportMemory(id)
    else if (ctx.GLOBAL != null) ExportGlobal(id)
    else error
  }

  override def visitScriptModule(ctx: ScriptModuleContext): Module = {
    if (ctx.module_ != null) {
      visitModule_(ctx.module_).asInstanceOf[Module]
    } else {
      throw new RuntimeException("Unsupported")
    }
  }

  override def visitAction_(ctx: Action_Context): Action = {
    if (ctx.INVOKE != null) {
      val instName = if (ctx.VAR != null) Some(ctx.VAR().getText) else None
      var name = ctx.name.getText.substring(1).dropRight(1)
      var args = for (constCtx <- ctx.constList.wconst.asScala) yield {
        val Array(ty, _) = constCtx.CONST.getText.split("\\.")
        visitLiteralWithType(constCtx.literal, toNumType(ty))
      }
      Invoke(instName, name, args.toList)
    } else {
      throw new RuntimeException("Unsupported")
    }
  }

  override def visitAssertion(ctx: AssertionContext): Assertion = {
    if (ctx.ASSERT_RETURN != null) {
      val action = visitAction_(ctx.action_)
      val expect = for (constCtx <- ctx.constList.wconst.asScala) yield {
        val Array(ty, _) = constCtx.CONST.getText.split("\\.")
        visitLiteralWithType(constCtx.literal, toNumType(ty))
      }
      println(s"expect = $expect")
      AssertReturn(action, expect.toList)
    } else {
      throw new RuntimeException("Unsupported")
    }
  }

  override def visitCmd(ctx: CmdContext): Cmd = {
    if (ctx.assertion != null) {
      visitAssertion(ctx.assertion)
    } else if (ctx.scriptModule != null) {
      CmdModule(visitScriptModule(ctx.scriptModule))
    } else {
      throw new RuntimeException("Unsupported")
    }
  }

  override def visitScript(ctx: ScriptContext): WIR = {
    val cmds = for (cmd <- ctx.cmd.asScala) yield {
      visitCmd(cmd)
    }
    Script(cmds.toList)
  }

  override def visitTag(ctx: TagContext): WIR = {
    val name = getVar(ctx.bindVar)
    val ty = visitFuncType(ctx.funcType)
    Tag(name, ty)
  }

  // Function to convert a hex string representation to an Array[Byte]
  def hexStringToByteArray(hex: String): Array[Byte] = {
    // Split the input string by '\' and filter out empty strings
    val byteStrings = hex.split("\\\\").filter(_.nonEmpty)

    byteStrings.map { byteStr =>
      // Parse the hex value to a byte
      Integer.parseInt(byteStr, 16).toByte
    }
  }

}


object Parser {
  private def makeWatVisitor(input: String) = {
    val charStream = new ANTLRInputStream(input)
    val lexer = new WatLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    new WatParser(tokens)
  }

  def parse(input: String): Module = {
    val parser = makeWatVisitor(input)
    val visitor = new GSWasmVisitor()
    val res: Module  = visitor.visit(parser.module).asInstanceOf[Module]
    res
  }

  def parseFile(filepath: String): Module = parse(scala.io.Source.fromFile(filepath).mkString)

  // parse extended webassembly script language
  def parseScript(input: String): Option[Script] = {
    val parser = makeWatVisitor(input)
    val visitor = new GSWasmVisitor()
    val tree = parser.script()
    val errorNumer = parser.getNumberOfSyntaxErrors()
    if (errorNumer != 0) None
    else {
      val res: Script = visitor.visitScript(tree).asInstanceOf[Script]
      Some(res)
    }
  }

  def parseScriptFile(filepath: String): Option[Script] =
    parseScript(scala.io.Source.fromFile(filepath).mkString)
}
