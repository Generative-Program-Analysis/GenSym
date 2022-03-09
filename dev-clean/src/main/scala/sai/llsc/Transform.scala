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
        transform(ss)
      case Node(s, "ss-assign", StaticList(ss: Sym, Const(x: Int), v), _) if eliminable(x) =>
        transform(ss)
      case Node(s, "ss-alloc-stack", StaticList(s1: Sym, Const(n1: Int)), es1) =>
        g.findDefinition(transform(s1)) match {
          case Some(Node(_, "ss-alloc-stack", List(s2: Sym, Const(n2: Int)), es2)) =>
            // s.stack_alloc(n2).stack_alloc(n1) ⇒ s.stack_allock(n2+n1)
            g.reflect("ss-alloc-stack", s2, Const(n1+n2))
          case Some(Node(_, "ss-assign", (s2: Sym)::assarg, es2)) =>
            g.findDefinition(s2) match {
              case Some(Node(_, "ss-alloc-stack", List(s3: Sym, Const(n2: Int)), es3)) =>
                // s.stack_alloc(n2).assign(x, v).stack_alloc(n1) ⇒
                // s.assign(x.v).stack_alloc(n1+n2)
                val e1 = g.reflect("ss-assign", (s3::assarg):_*) // TODO type of e1?
                val e2 = g.reflect("ss-alloc-stack", e1, Const(n1+n2))
                e2
              case _ => super.transform(n)
            }
          case _ => super.transform(n)
        }
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
