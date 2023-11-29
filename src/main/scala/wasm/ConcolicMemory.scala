package gensym.wasm.concolicmemory

import gensym.wasm.symbolic._
import gensym.wasm.ast._

import scala.collection.mutable.HashMap

object ConcolicMemory {
  def apply(): ConcolicMemory = {
    ConcolicMemory(Map(), None, HashMap())
  }
}

case class ConcolicMemory(map: Map[Int, (Byte, SymVal)], parent: Option[ConcolicMemory], chunktable: HashMap[Int, Int]) {
  def storeByte(addr: Int, value: (Byte, SymVal)): ConcolicMemory = {
    ConcolicMemory(map + (addr -> value), parent, chunktable)
  }

  def loadByte(addr: Int): (Byte, SymVal) = {
    def aux(node: ConcolicMemory): Option[(Byte, SymVal)] = node.map.get(addr) match {
        case res@Some(_) => res
        case None => node.parent.flatMap(aux)
    }

    aux(this) match {
      case Some(value) => value
      case None => (0, Concrete(I32V(0))) // wasp returns (0, 0)
    }
  }

  def loadBytes(addr: Int, size: Int): (List[Byte], SymVal) = {
    val loads = (0 until size).map(i => loadByte(addr + i))
    val (bytes, symval) = loads.unzip
    val sv = symval.reduce(MemConcat(_, _))
    (bytes.toList, sv)
  }

  def storeBytes(addr: Int, bytes: List[Byte], symval: SymVal): ConcolicMemory = {
    bytes.zipWithIndex.foldLeft(this) { case (mem, (byte, i)) =>
      mem.storeByte(addr + i, (byte, symval))
    }
  }

  def checkAccess(base: Int, ptr: Int, offset: Int): Boolean = this.chunktable.get(base) match {
    case Some(chunkSize) => {
      val (lowBound, highBound) = (base, base + chunkSize)
      val ptrOffset = ptr + offset
      (lowBound <= ptrOffset) && (ptrOffset < highBound)
    }
    case None => false
  }

  def storeInt(addr: Int, value: (Int, SymVal)): Unit = {
    val (int, symval) = value
    val bytes = intToBytes(int)
    val newMem = storeBytes(addr, bytes, symval)
  }

  def loadInt(addr: Int): (Int, SymVal) = {
    val (bytes, symval) = loadBytes(addr, 4)
    val int = bytesToInt(bytes)
    (int, symval)
  }

  def bytesToInt(bytes: List[Byte]): Int = {
    bytes.zipWithIndex.foldLeft(0) { case (acc, (byte, i)) =>
      acc | (byte.toInt << (i * 8))
    }
  }

  def intToBytes(int: Int): List[Byte] = {
    for (i <- (0 until 4).toList) yield {
      ((int >> (i * 8)) & 0xff).toByte
    }
  }
}
