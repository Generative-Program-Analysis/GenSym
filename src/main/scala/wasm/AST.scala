package gensym.wasm.ast

import gensym.wasm.types._
import gensym.wasm.values._
import gensym.wasm.source._

case class Module(definitions: Seq[Definition])
abstract class Definition

case class FuncDef(name: String, tipe: FuncType, locals: Seq[ValueType], body: Seq[Instr]) extends Definition
case class TypeDef(id: Int, tipe: ValueType) extends Definition

abstract class Instr
case object Unreachable extends Instr
case object Nop extends Instr
case object Drop extends Instr
case class Select(ty: Option[Seq[ValueType]]) extends Instr
case class Block(ty: BlockType, instrs: Seq[Instr]) extends Instr
case class Loop(ty: BlockType, instrs: Seq[Instr]) extends Instr
case class If(ty: BlockType, thenInstrs: Seq[Instr], elseInstrs: Seq[Instr]) extends Instr
case class Br(labelId: Int) extends Instr
case class BrIf(labelId: Int) extends Instr
// case class BrTable(labels: Seq[Int], default: Int) extends Instr
case object Return extends Instr
case class Call(func: Int) extends Instr
// case class CallIndirect(ty: Int, table: Int) extends Instr
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
// case object MemoryGrow extends Instr
// case object MemoryFill extends Instr
// case object MemoryCopy extends Instr
// case class MemoryInit(seg: Int) extends Instr
// case class DataDrop(seg: Int) extends Instr
// case class RefNull(ty: RefType) extends Instr
// case class RefFunc(func: Int) extends Instr
// case object RefIsNull extends Instr
case class Const(num: Num) extends Instr
case class Test(op: TestOp) extends Instr
case class Compare(op: RelOp) extends Instr
case class Unary(op: UnaryOp) extends Instr
case class Binary(op: BinOp) extends Instr
// case class Convert(op: CvtOp) extends Instr
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

abstract class IntOp
abstract class UnaryIntOp extends IntOp
abstract class BinaryIntOp extends IntOp
abstract class RelIntOp extends IntOp

case object Clz extends UnaryIntOp
case object Ctz extends UnaryIntOp
case object Popcnt extends UnaryIntOp
case object ExtendS extends UnaryIntOp

case object Add extends BinaryIntOp
case object Sub extends BinaryIntOp
case object Mul extends BinaryIntOp
case object DivS extends BinaryIntOp
case object DivU extends BinaryIntOp
case object RemS extends BinaryIntOp
case object RemU extends BinaryIntOp
case object And extends BinaryIntOp
case object Or extends BinaryIntOp
case object Xor extends BinaryIntOp
case object Shl extends BinaryIntOp
case object ShrS extends BinaryIntOp
case object ShrU extends BinaryIntOp
case object Rotl extends BinaryIntOp
case object Rotr extends BinaryIntOp

case object Eq extends RelIntOp
case object Ne extends RelIntOp
case object Lt extends RelIntOp
case object Le extends RelIntOp
case object Gt extends RelIntOp
case object Ge extends RelIntOp

abstract class IntTestOp
case object Eqz extends IntTestOp

abstract class BinOp
object BinOp {
  case class Int(op: BinaryIntOp) extends BinOp
}

abstract class TestOp
object TestOp {
  case class Int(op: IntTestOp) extends TestOp
}

abstract class RelOp
object RelOp {
  case class Int(op: RelIntOp) extends BinOp
}

abstract class UnaryOp
object UnaryOp {
  case class Int(op: UnaryIntOp) extends BinOp
}

abstract class PackSize
case object Pack8 extends PackSize
case object Pack16 extends PackSize
case object Pack32 extends PackSize
case object Pack64 extends PackSize

abstract class Extension

abstract class MemOp(align: Int, offset: Int)
case class StoreOp(align: Int, offset: Int, tipe: NumType, pack_size: Option[PackSize]) extends MemOp(align, offset)
case class LoadOp(align: Int, offset: Int, tipe: NumType, pack_size: Option[PackSize], extension: Option[Extension]) extends MemOp(align, offset)
