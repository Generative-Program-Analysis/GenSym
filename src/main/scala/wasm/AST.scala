package gensym.wasm.ast

import scala.collection.mutable.HashMap
import gensym.wasm.miniwasm.ModuleInstance
import gensym.wasm.source._

abstract class WIR

case class Module(name: Option[String], definitions: List[Definition], funcEnv: HashMap[Int, Callable]) extends WIR

abstract class Definition extends WIR
case class FuncDef(name: Option[String], f: FuncField) extends Definition with Callable
case class TypeDef(id: Option[String], tipe: FuncLikeType) extends Definition
case class Table(id: Option[String], f: TableField) extends Definition
case class Memory(id: Option[String], f: MemoryField) extends Definition
case class Global(id: Option[String], f: GlobalField) extends Definition
case class Elem(id: Option[Int], offset: List[Instr], elemList: ElemList) extends Definition
case class Data(id: Option[String], value: String) extends Definition
case class Start(id: Int) extends Definition
case class Import(mod: String, name: String, desc: ImportDesc) extends Definition with Callable
case class Export(name: String, desc: ExportDesc) extends Definition
case class Tag(id: Option[String], tipe: FuncType) extends Definition
// FIXME: missing top-level module fields, see WatParser.g4

abstract class ImportDesc extends WIR
case class ImportFuncTy(name: Option[String], t: FuncType) extends ImportDesc
case class ImportFuncTyUse(name: Option[String], u: Int) extends ImportDesc

abstract class ElemList extends WIR
case class ElemListFunc(funcs: List[String]) extends ElemList
case class ElemListExpr(exprs: List[List[Instr]]) extends ElemList

abstract class FuncField extends WIR
case class FuncBodyDef(tipe: FuncType, localNames: List[String], locals: List[ValueType], body: List[Instr])
    extends FuncField
case class FunInlineImport(mod: String, name: String, typeUse: Option[Int], imports: Any /*FIXME*/ ) extends FuncField
case class FunInlineExport(fd: List[FuncDef]) extends FuncField

abstract class TableField extends WIR
case class TableType(min: Int, max: Option[Int], ty: RefType) extends TableField
case class TabInlineImport(ty: TableType) extends TableField
case class TabInlineExport(field: TableField) extends TableField

abstract class MemoryField extends WIR
case class MemoryType(min: Int, max: Option[Int]) extends MemoryField
case class MemInlineImport(ty: MemoryType) extends MemoryField
case class MemInlineExprot(field: MemoryField) extends MemoryField

abstract class GlobalField extends WIR
// e is constant expression
case class GlobalValue(ty: GlobalType, e: List[Instr]) extends GlobalField
case class GloInlineImport(ty: GlobalType) extends GlobalField
case class GloInlineExprot(field: GlobalField) extends GlobalField

// Intermediate result
case class InstrList(instrs: List[Instr]) extends WIR

abstract class Instr extends WIR
case object Unreachable extends Instr
case object Nop extends Instr
case object Drop extends Instr
case object Alloc extends Instr
case object Free extends Instr
case class Select(ty: Option[List[ValueType]]) extends Instr
case class Block(ty: BlockType, instrs: List[Instr]) extends Instr
case class IdBlock(id: Int, ty: BlockType, instrs: List[Instr]) extends Instr
case class Loop(ty: BlockType, instrs: List[Instr]) extends Instr
case class IdLoop(id: Int, ty: BlockType, instrs: List[Instr]) extends Instr
case class If(ty: BlockType, thenInstrs: List[Instr], elseInstrs: List[Instr]) extends Instr
case class IdIf(ty: BlockType, thenInstrs: IdBlock, elseInstrs: IdBlock) extends Instr
// FIXME: labelId can be string?
case class Br(labelId: Int) extends Instr
case class BrIf(labelId: Int) extends Instr
case class BrTable(labels: List[Int], default: Int) extends Instr
case object Return extends Instr
// normal call
case class Call(func: Int) extends Instr
case class CallIndirect(ty: Int, table: Int) extends Instr
// tail call
case class ReturnCall(func: Int) extends Instr
case class ReturnCallIndirect(ty: Int, table: Int) extends Instr
case class LocalGet(local: Int) extends Instr
case class LocalSet(local: Int) extends Instr
case class LocalTee(local: Int) extends Instr
case class GlobalGet(global: Int) extends Instr
case class GlobalSet(global: Int) extends Instr
// case class TableGet(table: Int) extends Instr
// case class TableSet(table: Int) extends Instr
// case class TableSize(table: Int) extends Instr
// case class TableGrow(table: Int) extends Instr
// case class TableFill(table: Int) extends Instr
// case class TableCopy(dst: Int, src: Int) extends Instr
// case class TableInit(seg: Int, table: Int) extends Instr
// case class ElemDrop(seg: Int) extends Instr
case class Load(op: LoadOp) extends Instr
case class Store(op: StoreOp) extends Instr
// case class VecLoad(op: VecLoadOp) extends Instr
// case class VecStore(op: VecStoreOp) extends Instr
// case class VecLoadLane(op: VecLaneOp) extends Instr
// case class VecStoreLane(op: VecLaneOp) extends Instr
case object MemorySize extends Instr
case object MemoryGrow extends Instr
case object MemoryFill extends Instr
case object MemoryCopy extends Instr
case class MemoryInit(seg: Int) extends Instr
// case class DataDrop(seg: Int) extends Instr
case class Const(num: Num) extends Instr
case class Test(op: TestOp) extends Instr
case class Compare(op: RelOp) extends Instr
case class Unary(op: UnaryOp) extends Instr
case class Binary(op: BinOp) extends Instr
case class Convert(op: CvtOp) extends Instr
// case class VecConst(vec: Vec) extends Instr
// case class VecTest(op: VecTestOp) extends Instr
// case class VecCompare(op: VecRelOp) extends Instr
// case class VecUnary(op: VecUnOp) extends Instr
// case class VecBinary(op: VecBinOp) extends Instr
// case class VecConvert(op: VecCvtOp) extends Instr
// case class VecShift(op: VecShiftOp) extends Instr
// case class VecBitmask(op: VecBitmaskOp) extends Instr
// case class VecTestBits(op: VecVTestOp) extends Instr
// case class VecUnaryBits(op: VecVUnOp) extends Instr
// case class VecBinaryBits(op: VecVBinOp) extends Instr
// case class VecTernaryBits(op: VecVTernOp) extends Instr
// case class VecSplat(op: VecSplatOp) extends Instr
// case class VecExtract(op: VecExtractOp) extends Instr
// case class VecReplace(op: VecReplaceOp) extends Instr

// Instructions for symbolic execution
case class PushSym(name: String, concreteVal: Num) extends Instr
case class Symbolic(ty: ValueType) extends Instr
case object SymAssert extends Instr

// TODO: add wasmfx instructions
// TODO: should I take care of the unresolved cases?
case class Suspend(tag: Int) extends Instr
// note that cont.new can only be called with a func type
case class ContNew(ty: Int) extends Instr
case class ContBind(OldContTy: Int, NewContTy: Int) extends Instr
// case class RefNull(ty: RefType) extends Instr
// case object RefIsNull extends Instr
// note that ref.func can be called with any of the extended function type
case class RefFunc(func: Int) extends Instr
case class CallRef(ty: Int) extends Instr

case class Resume(ty: Int, ons: List[Handler]) extends Instr
// TODO: make sure this class wants to extend WIR
case class Handler(tag: Int, label: Int) extends WIR

trait Unresolved
case class CallUnresolved(name: String) extends Instr with Unresolved
case class BrUnresolved(name: String) extends Instr with Unresolved

abstract class BinOp
case class Add(ty: NumType) extends BinOp
case class Sub(ty: NumType) extends BinOp
case class Mul(ty: NumType) extends BinOp
case class Div(ty: NumType) extends BinOp
// Note: integer div must be either signed or unsigned
case class DivS(ty: NumType) extends BinOp
case class DivU(ty: NumType) extends BinOp
case class RemS(ty: NumType) extends BinOp
case class RemU(ty: NumType) extends BinOp
case class And(ty: NumType) extends BinOp
case class Or(ty: NumType) extends BinOp
case class Xor(ty: NumType) extends BinOp
case class Shl(ty: NumType) extends BinOp
case class ShrS(ty: NumType) extends BinOp
case class ShrU(ty: NumType) extends BinOp
case class Rotl(ty: NumType) extends BinOp
case class Rotr(ty: NumType) extends BinOp
case class Min(ty: NumType) extends BinOp
case class Max(ty: NumType) extends BinOp
case class Copysign(ty: NumType) extends BinOp

abstract class TestOp
case class Eqz(ty: NumType) extends TestOp

abstract class RelOp
case class Eq(ty: NumType) extends RelOp
case class Ne(ty: NumType) extends RelOp
case class LtS(ty: NumType) extends RelOp
case class LeS(ty: NumType) extends RelOp
case class LtU(ty: NumType) extends RelOp
case class LeU(ty: NumType) extends RelOp
case class GtS(ty: NumType) extends RelOp
case class GeS(ty: NumType) extends RelOp
case class GtU(ty: NumType) extends RelOp
case class GeU(ty: NumType) extends RelOp
case class Lt(ty: NumType) extends RelOp
case class Le(ty: NumType) extends RelOp
case class Gt(ty: NumType) extends RelOp
case class Ge(ty: NumType) extends RelOp

abstract class UnaryOp
case class Clz(ty: NumType) extends UnaryOp
case class Ctz(ty: NumType) extends UnaryOp
case class Popcnt(ty: NumType) extends UnaryOp
case class ExtendS(ty: NumType) extends UnaryOp
case class Neg(ty: NumType) extends UnaryOp
case class Abs(ty: NumType) extends UnaryOp
case class Sqrt(ty: NumType) extends UnaryOp
case class Ceil(ty: NumType) extends UnaryOp
case class Floor(ty: NumType) extends UnaryOp
case class Trunc(ty: NumType) extends UnaryOp
case class Nearest(ty: NumType) extends UnaryOp

abstract class CvtOp
// Note: the actual possible types are more restricted than what is allowed
// by the AST, see https://webassembly.github.io/spec/core/text/instructions.html
case class Wrap(from: NumType, to: NumType) extends CvtOp
case class TruncTo(from: NumType, to: NumType, sign: Extension) extends CvtOp
case class TruncSat(from: NumType, to: NumType, sign: Extension) extends CvtOp
case class Extend(from: NumType, to: NumType, sign: Extension) extends CvtOp
case class ConvertTo(from: NumType, to: NumType, sign: Extension) extends CvtOp
case class Demote(from: NumType, to: NumType) extends CvtOp
case class Promote(from: NumType, to: NumType) extends CvtOp
case class Reinterpret(from: NumType, to: NumType) extends CvtOp

abstract class PackSize extends WIR
case object Pack8 extends PackSize
case object Pack16 extends PackSize
case object Pack32 extends PackSize
case object Pack64 extends PackSize

abstract class Extension extends WIR
case object SX extends Extension
case object ZX extends Extension

abstract class MemOp(align: Int, offset: Int) extends WIR
case class StoreOp(align: Int, offset: Int, tipe: NumType, packSize: Option[PackSize]) extends MemOp(align, offset)
case class LoadOp(align: Int, offset: Int, tipe: NumType, packSize: Option[PackSize], extension: Option[Extension])
    extends MemOp(align, offset)

// Types

abstract class NumKind extends WIR
case object I32Type extends NumKind
case object I64Type extends NumKind
case object F32Type extends NumKind
case object F64Type extends NumKind

abstract class VecKind extends WIR
case object V128Type extends VecKind

trait FuncLikeType

abstract class HeapType extends WIR
case object FuncRefType extends HeapType
case object ExternRefType extends HeapType
case class RefFuncType(tyId: Int) extends HeapType
case class FuncType(argNames /*optional*/: List[String], inps: List[ValueType], out: List[ValueType]) extends HeapType with FuncLikeType
case class ContType(funcTypeId: Int) extends HeapType with FuncLikeType

abstract class WasmType extends WIR

abstract class ValueType extends WasmType
case class NumType(kind: NumKind) extends ValueType
case class VecType(kind: VecKind) extends ValueType
case class RefType(kind: HeapType) extends ValueType

case class GlobalType(ty: ValueType, mut: Boolean) extends WasmType

abstract class BlockType extends WIR
case class VarBlockType(index: Int, tipe: Option[FuncType]) extends BlockType
case class ValBlockType(tipe: Option[ValueType]) extends BlockType;

// Exports
abstract class ExportDesc extends WIR
case class ExportFunc(i: Int) extends ExportDesc
case class ExportTable(i: Int) extends ExportDesc
case class ExportMemory(i: Int) extends ExportDesc
case class ExportGlobal(i: Int) extends ExportDesc

case class Script(cmds: List[Cmd]) extends WIR
abstract class Cmd extends WIR
// TODO: can we turn abstract class sealed?
case class CmdModule(module: Module) extends Cmd

abstract class Action extends WIR
case class Invoke(instName: Option[String], name: String, args: List[Value]) extends Action

abstract class Assertion extends Cmd
case class AssertReturn(action: Action, expect: List[Num] /* TODO: support multiple expect result type*/)
    extends Assertion
case class AssertTrap(action: Action, message: String) extends Assertion

/* Runtime structures */

// Values
abstract class Value {
  def tipe(implicit m: ModuleInstance): ValueType
}

abstract class Num extends Value {
  def tipe(implicit m: ModuleInstance): ValueType =
    NumType(this match {
      case I32V(_) => I32Type
      case I64V(_) => I64Type
      case F32V(_) => F32Type
      case F64V(_) => F64Type
    })
}

case class I32V(value: Int) extends Num
case class I64V(value: Long) extends Num
case class F32V(value: Float) extends Num
case class F64V(value: Double) extends Num

trait Callable

// https://webassembly.github.io/function-references/core/exec/runtime.html
abstract class Ref extends Value with Callable
case class RefNullV(t: HeapType) extends Ref {
  def tipe(implicit m: ModuleInstance): ValueType = RefType(t)
}
case class RefFuncV(funcAddr: Int) extends Ref {
  def tipe(implicit m: ModuleInstance): ValueType =
    m.funcs(funcAddr) match {
      case FuncDef(_, FuncBodyDef(ty, _, _, _)) => RefType(ty)
    }
}

// RefContV only refer to an adresss
// but this should contain a scala function
case class RefContV(funcAddr: Int) extends Ref {
  def tipe(implicit m: ModuleInstance): ValueType = ???
}
case class RefExternV(externAddr: Int) extends Ref {
  def tipe(implicit m: ModuleInstance): ValueType = ???
}

case class RTGlobal(ty: GlobalType, var value: Value)

