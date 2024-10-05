package gensym.wasm.memory

import scala.collection.mutable.ArrayBuffer

case class RTMemoryException(message: String) extends Exception(message)
case class RTMemoryType(min: Int, max: Option[Int])

case class RTMemory(var memType: RTMemoryType, data: ArrayBuffer[Byte]) {
  def size: Int = (data.size / RTMemory.pageSize).toInt

  def grow(delta: Int): Option[RTMemoryException] = {
    val oldSize = memType.min
    val newSize = oldSize + delta
    if (newSize < oldSize) {
      Some(RTMemoryException("SizeOverflow"))
    } else {
      memType = RTMemoryType(newSize, memType.max)
      data ++= Array.fill[Byte](delta * RTMemory.pageSize.toInt)(0)
      None
    }
  }

  def loadByte(a: Long): Byte = {
    if (a >= data.size) throw new RTMemoryException("Bounds")
    data(a.toInt)
  }

  def storeByte(a: Long, b: Byte): Unit = {
    if (a >= data.size) throw new RTMemoryException("Bounds")
    data(a.toInt) = b
  }

  def loadn(a: Long, o: Int, n: Int): Long = {
    assert(n > 0 && n <= 8)
    var result: Long = 0
    for (i <- (n - 1) to 0 by -1) { // Little-endian: start from least significant byte
      result = (result << 8) | (loadByte(a + i) & 0xff)
    }
    result
  }

  def storen(a: Long, o: Int, n: Int, x: Long): Unit = {
    assert(n > 0 && n <= 8)
    var temp = x
    for (i <- 0 until n) {
      storeByte(a + i, (temp & 0xff).toByte) // Little-endian: store least significant byte first
      temp = temp >> 8
    }
  }

  // TODO: store/load different types
  def loadInt(addr: Long): Int = {
    val result: Long = loadn(addr, 0, 4)
    result.toInt
  }

  def storeInt(addr: Long, num: Int): Unit = {
    storen(addr, 0, 4, num)
  }

  def fill(offset: Long, size: Long, value: Byte): Unit = {
    // TODO: instead of using storeByte and loadByte, check
    // bounds up front so we can avoid the checks in load/storeByte
    for (i <- offset until offset + size) {
      storeByte(i, value)
    }
  }

  def copy(src: Long, dst: Long, size: Long): Unit = {
    // TODO: instead of using storeByte and loadByte, check
    // bounds up front so we can avoid the checks in load/storeByte
    if (src < dst) {
      for (i <- size - 1 to 0 by -1) {
        storeByte(dst + i, loadByte(src + i))
      }
    } else {
      for (i <- 0 until size.toInt) {
        storeByte(dst + i, loadByte(src + i))
      }
    }
  }
}

object RTMemory {
  val pageSize = 65536 // https://www.w3.org/TR/wasm-core-2/exec/runtime.html#memory-instances
  def apply(): RTMemory = this.apply(1024)
  def apply(size: Int): RTMemory = this.apply(size, None)
  def apply(size: Int, maxSize: Option[Int]): RTMemory = {
    new RTMemory(RTMemoryType(size, maxSize),
                 ArrayBuffer.fill[Byte](size * pageSize.toInt)(0))
  }
}
