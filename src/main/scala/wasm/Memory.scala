package gensym.wasm.memory

import scala.collection.mutable.ArrayBuffer

case class MemoryException(message: String) extends Exception(message)
case class MemoryType(min: Int, max: Option[Int])

case class Memory(var memType: MemoryType, data: ArrayBuffer[Byte]) {
  def size: Int = (data.size / Memory.pageSize).toInt

  def grow(delta: Int): Option[MemoryException] = {
    val oldSize = memType.min
    val newSize = oldSize + delta
    if (newSize < oldSize) {
      Some(MemoryException("SizeOverflow"))
    } else {
      memType = MemoryType(newSize, memType.max)
      data ++= Array.fill[Byte](delta * Memory.pageSize.toInt)(0)
      None
    }
  }

  def loadByte(a: Long): Byte = {
    if (a >= data.size) throw new MemoryException("Bounds")
    data(a.toInt)
  }

  def storeByte(a: Long, b: Byte): Unit = {
    if (a >= data.size) throw new MemoryException("Bounds")
    data(a.toInt) = b
  }

  def loadn(a: Long, o: Int, n: Int): Long = {
    assert(n > 0 && n <= 8)
    var result: Long = 0
    for (i <- 0 until n) {
      result = (result << 8) | (loadByte(a + i) & 0xff)
    }
    result
  }

  def storen(a: Long, o: Int, n: Int, x: Long): Unit = {
    assert(n > 0 && n <= 8)
    var temp = x
    for (i <- 0 until n) {
      storeByte(a + i, (temp & 0xff).toByte)
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

object Memory {
  val pageSize = 65536 // https://www.w3.org/TR/wasm-core-2/exec/runtime.html#memory-instances
  def apply(): Memory = this.apply(1024)
  def apply(size: Int): Memory = this.apply(size, None)
  def apply(size: Int, maxSize: Option[Int]): Memory = {
    new Memory(MemoryType(size, maxSize), ArrayBuffer.fill[Byte](size * pageSize.toInt)(0))
  }
}
