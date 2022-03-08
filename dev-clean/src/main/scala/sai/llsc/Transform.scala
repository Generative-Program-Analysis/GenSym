package sai.llsc

import lms.core._
import lms.core.Backend._
import lms.core.stub.{While => _, _}
import scala.collection.immutable.{List => StaticList}
import scala.collection.mutable.{HashMap,HashSet}

object AssignElim {
  type Subst = HashMap[Sym, Exp]

  class CollectLookup extends Traverser {
    val ids = new HashSet[Int]()
    override def traverse(n: Node): Unit = n match {
      case Node(s, "ss-lookup-env", StaticList(ss, Const(x: Int)), _) => ids.add(x)
      case _ => super.traverse(n)
    }
  }

  class ElimAssign(val ids: HashSet[Int]) extends Transformer {
    override val name = "ElimAssign"
    def eliminable(x: Int): Boolean = !ids.contains(x)
    def eliminable(xs: List[Int]): Boolean =
      // Note: the xs.last == 0 case is reserved for var_arg
      if (xs.size > 0 && xs.last == 0) false
      else xs.forall(eliminable)

    override def transform(n: Node): Exp = n match {
      case Node(s, "ss-assign-seq", StaticList(ss: Sym, Const(xs: List[Int]), vs), _) if eliminable(xs) =>
        subst(ss)
      case Node(s, "ss-assign", StaticList(ss: Sym, Const(x: Int), v), _) if eliminable(x) =>
        subst(ss)
      case _ => super.transform(n)
    }
    override def transform(graph: Graph): Graph = {
      g = Adapter.mkGraphBuilder()
      Adapter.g = g
      try {
        super.transform(graph)
      } finally {
        g = null; Adapter.g = null
      }
    }
  }

  def transform(g: Graph): (Graph, Subst) = {
    val collect = new CollectLookup
    collect(g)
    val elim = new ElimAssign(collect.ids)
    (elim.transform(g), elim.subst)
  }
}
