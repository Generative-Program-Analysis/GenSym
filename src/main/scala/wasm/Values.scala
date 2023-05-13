package gensym.wasm.values

import gensym.wasm.types._

abstract class Value

abstract class Num extends Value
case class I32(value: Int) extends Num
case class I64(value: Long) extends Num
case class F32(value: Float) extends Num
case class F64(value: Double) extends Num
