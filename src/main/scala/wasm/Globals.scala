package gensym.wasm.globals

import gensym.wasm.types._
import gensym.wasm.values._

case class GlobalType(valueType: ValueType, mutable: Boolean)
case class Global(tipe: GlobalType, var value: Value)
