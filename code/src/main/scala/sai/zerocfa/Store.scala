package sai.zerocfa

import sai.parser.cps._

object SetExtractor {
  def unapplySeq[T](s: Set[T]): Option[Seq[T]] = Some(s.toSeq)
}

case class Store(map: Map[String, Set[Lam]]) {
  case class UndefinedAddrException(addr: Expr) extends RuntimeException

  def lookup(addr: Expr): Set[Lam] = {
    addr match {
      case Lit(_) => Set()
      case Var(name) => lookup(name)
      case lam@Lam(_, _) => Set(lam)
      case _ => throw UndefinedAddrException(addr)
    }
  }

  def lookup(addr: String): Set[Lam] = map.getOrElse(addr, Set[Lam]())

  def update(addr: String, d: Set[Lam]): Store = {
    //println(s"update $addr -> $d")
    val oldd = map.getOrElse(addr, Set[Lam]())
    Store(map ++ Map(addr -> (d ++ oldd)))
  }

  def update(addr: String, v: Lam): Store = update(addr, Set(v))

  def update(addrs: List[String], ds: List[Set[Lam]]): Store = {
    (addrs zip ds).foldLeft(this) {
      case (store, (addr, v)) => store.update(addr, v)
    }
  }
}

object Store {
  def bot = Set()
  def mtStore = Store(Map())
}
