package gensym.wasm.memory

import scala.collection.mutable.ArrayBuffer

case class Memory(data: ArrayBuffer[Byte]) {
  def storeInt(offset: Int, value: Int) = {
    data(offset) = (value & 0xff).toByte
    data(offset + 1) = ((value >> 8) & 0xff).toByte
    data(offset + 2) = ((value >> 16) & 0xff).toByte
    data(offset + 3) = ((value >> 24) & 0xff).toByte
  }

  def loadInt(offset: Int) = {
    val b1 = data(offset)
    val b2 = data(offset + 1)
    val b3 = data(offset + 2)
    val b4 = data(offset + 3)
    (b1 & 0xff) | ((b2 & 0xff) << 8) | ((b3 & 0xff) << 16) | ((b4 & 0xff) << 24)
  }
}
