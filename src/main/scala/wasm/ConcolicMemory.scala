package gensym.wasm.concolicmemory

import gensym.wasm.symbolic._

case class ConcolicMemory(map: Map[Int, (Byte, SymVal)], parent: Option[ConcolicMemory]) {
  def storeByte(addr: Int, value: (Byte, SymVal)): ConcolicMemory = {
    ConcolicMemory(map + (addr -> value), parent)
  }

  def loadByte(addr: Int): (Byte, SymVal) = {
    def aux(node: ConcolicMemory): Option[(Byte, SymVal)] = node.map.get(addr) match {
        case res@Some(_) => res
        case None => node.parent.flatMap(aux)
    }

    aux(this) match {
      case Some(value) => value
      case None => ???
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
}
