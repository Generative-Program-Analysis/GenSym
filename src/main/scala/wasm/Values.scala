package gensym.wasm.values

import gensym.wasm.types._

abstract class Value {
  def tipe: ValueType
}

abstract class Num extends Value {
  def tipe: ValueType = NumType(this match {
    case I32(_) => I32Type
    case I64(_) => I64Type
    case F32(_) => F32Type
    case F64(_) => F64Type
  })
}

case class I32(value: Int) extends Num
case class I64(value: Long) extends Num
case class F32(value: Float) extends Num
case class F64(value: Double) extends Num
