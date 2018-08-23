/**
# Automata-Based Regex Matcher

Outline:
<div id="tableofcontents"></div>


Specializing string matchers and parsers is a popular benchmark in the partial
evaluation and supercompilation literature.

<!--
- http://dblp.uni-trier.de/db/ journals/ipl/ConselD89
- http://dblp.uni-trier.de/db/ journals/toplas/AgerDR06
- http://dblp.uni-trier.de/db/ journals/toplas/SperberT00
- http://dblp.uni-trier.de/db/ journals/toplas/Turchin86,
- http://dblp.uni-trier.de/db/ journals/jfp/SorensenGJ96
-->

We consider "multi-threaded" regular expression matchers, that spawn a new
conceptual thread to process alternatives in parallel. Of course these matchers
do not actually spawn OS-level threads, but rather need to be advanced manually
by client code. Thus, they are similar to coroutines.
*/

package scala.lms.tutorial


/**
Regexp Matchers as Nondeterministic Finite Automata (NFA)
---------------------------------------------------------

*/

class AutomataTest extends TutorialFunSuite {
  val under = "dfa_"

  test("findAAB") {
    val p = new AutomataDriver with NFAtoDFA {
      def snippet(x: Rep[Unit]) = {
/**
Here is a simple example for the fixed regular expression `.*AAB`:
*/
        def findAAB(): NIO = {
          guard(C('A')) {
            guard(C('A')) {
              guard(C('B'), true) {
                stop()
          }}} ++
          guard(W) { findAAB() } // in parallel ...
        }

        // NFA to DFA conversion via staging explained below.
        convertNFAtoDFA((findAAB(), false))
      }
    }

    // Some tests.
    assertResult(true){p.matches("AAB")}
    assertResult(false){p.matches("AAC")}
    assertResult(true){p.matches("AACAAB")}
    assertResult(true){p.matches("AACAABAAC")}

    // The generated code for the DFA is shown at the end.
    check("aab", p.code)
  }
}

/**
We can easily add combinators on top of the core abstractions that take care
of producing matchers from textual regular expressions. However, the point
here is to demonstrate how the implementation works.
*/

trait NFAOps extends scala.lms.util.ClosureCompare {
/**
The given matcher uses an API that models nondeterministic finite automata
(NFA):

An NFA state consists of a list of possible transitions. Each transition may
be guarded by a set of characters and it may  have a flag to be signaled if
the transition is taken. It also knows how to compute the following state. We
use `Char`s for simplicity, but of course we could  use generic types as well.
Note that the API does not mention where input is obtained from (files,
streams, etc).
*/
  type NIO = List[NTrans] // state: many possible transitions
  def guard(cond: CharSet, found: => Boolean = false)(e: => NIO): NIO = {
    List(NTrans(cond, () => found, () => e))
  }
  def stop(): NIO = Nil
  def trans(c: CharSet)(s: () => NIO): NIO = List(NTrans(c, () => false, s))
  def guards(conds: List[CharSet], found: Boolean = false)(e: => NIO): NIO = {
    conds.flatMap(guard(_, found)(e))
  }
  case class NTrans(c: CharSet, e: () => Boolean, s: () => NIO) extends Ordered[NTrans] {
    override def compare(o: NTrans) = {
      val i = this.c.compare(o.c)
      if (i != 0) i else {
        val i2 = this.e().compare(o.e())
        if (i2 != 0) i2 else {
          val tf = canonicalize(this.s())
          val of = canonicalize(o.s())
          if (tf == of) 0 else tf.compare(of)
	      }
      }
    }
  }

  sealed abstract class CharSet extends Ordered[CharSet] {
    override def compare(o: CharSet) = (this,o) match {
      case (W,W) => 0
      case (W,_) => 1
      case (_,W) => -1
      case (C(c1),C(c2)) => c1.compare(c2)
    }
  }
  case class C(c: Char) extends CharSet
  case object W extends CharSet
}

/**
From NFA to DFA using Staging
-----------------------------

We will translate NFAs to DFAs using staging. This is the staged DFA API,
which is just a thin wrapper over an unstaged API with no `Rep`s:
*/

case class Automaton[@specialized(Char) I, @specialized(Boolean) O](
  out: O, next: I => Automaton[I,O])

trait DFAOps extends Dsl {
  implicit def dfaTyp: Typ[DfaState]
  type DfaState = Automaton[Char,Boolean]
  type DIO = Rep[DfaState]
  def dfa_trans(f: Rep[Char] => DIO): DIO = dfa_trans(false)(f)
  def dfa_trans(e: Boolean)(f: Rep[Char] => DIO): DIO
}

trait NFAtoDFA extends NFAOps with DFAOps {
/**
Translating an NFA to a DFA is accomplished by creating a DFA state for each
encountered NFA configuration (removing duplicate states via `canonicalize`):
*/
  def convertNFAtoDFA(in: (NIO, Boolean)): DIO = {
    def iterate(flag: Boolean, state: NIO): DIO = {
      dfa_trans(flag){ c: Rep[Char] => exploreNFA(canonicalize(state), c) { iterate }
    }}
    iterate(in._2, in._1)
  }

  def canonicalize(state: NIO): NIO = {
    if (state.isEmpty) state else {
      val state_sorted = state.sorted
      state_sorted.head :: (for ((s,sn) <- (state_sorted zip state_sorted.tail)
        if s.compare(sn) != 0) yield sn)
    }
  }

/**
The LMS framework memoizes functions which ensures termination if the
NFA is indeed finite.

We use a separate function to explore the NFA space,
advancing the automaton by a symbolic character `cin` to invoke its
continuations `k` with a new automaton, i.e. the possible set of
states after consuming `cin`. The given implementation assumes
character sets contain either zero or one characters, the empty set
denoting a wildcard match. More elaborate cases such as
character ranges are easy to add. The algorithm tries to remove as
many redundant checks and impossible branches as possible. This only
works because the character guards are staging time values.
*/
  def exploreNFA[A:Typ](xs: NIO, cin: Rep[Char])(
    k: (Boolean, NIO) => Rep[A]): Rep[A] = xs match {
    case Nil => k(false, Nil)
    case NTrans(W, e, s)::rest =>
      val (xs1, xs2) = xs.partition(_.c != W)
      exploreNFA(xs1,cin)((flag,acc) =>
        k(flag || xs2.exists(_.e()), acc ++ xs2.flatMap(_.s())))
    case NTrans(cset, e, s)::rest =>
      if (cset contains cin) {
        val xs1 = for (
          NTrans(rcset, re, rs) <- rest;
          kcset <- rcset knowing cset
        ) yield NTrans(kcset,re,rs)
        exploreNFA(xs1,cin)((flag,acc) => k(flag || e(), acc ++ s()))
      } else {
        val xs1 = for (
          NTrans(rcset, re, rs) <- rest;
          kcset <- rcset knowing_not cset
        ) yield NTrans(kcset,re,rs)
        exploreNFA(xs1, cin)(k)
      }
  }

  def infix_contains(s: CharSet, c: Rep[Char]): Rep[Boolean] = s match {
    case C(c1) => c == c1
    case W => unit(true)
  }
  def infix_knowing(s1: CharSet, s2: CharSet): Option[CharSet] = (s1,s2) match {
    case (W,_) => Some(W)
    case (C(c1),C(c2)) if c1 == c2 => Some(W)
    case _ => None
  }
  def infix_knowing_not(s1: CharSet, s2: CharSet): Option[CharSet] = (s1,s2) match {
    case (C(c1), C(c2)) if c1 == c2 => None
    case _ => Some(s1)
  }
}

/**
The generated code is shown further down below. Each function
corresponds to one DFA state. Note how  negative information has been used to
prune the transition space: Given input such as `...AAB` the  automaton jumps
back to the initial state, i.e. it recognizes that the last character `B`
cannot also be `A` and starts looking for two `A`s after the `B`.

Generated State Machine Code
----------------------------
*/
trait DFAOpsExp extends DslExp with DFAOps {
  implicit def dfaTyp: Typ[DfaState] = manifestTyp
  case class DFAState(e: Boolean, f: Rep[Char => DfaState]) extends Def[DfaState]
  def dfa_trans(e: Boolean)(f: Rep[Char] => DIO): DIO = DFAState(e, doLambda(f))
}

trait ScalaGenDFAOps extends DslGen {
  val IR: DFAOpsExp
  import IR._
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case dfa@DFAState(b,f) =>
      emitValDef(sym, "scala.lms.tutorial.Automaton(" + quote(b) + ", " + quote(f) + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

/**
To use the generated code, we use the `matches` small driver loop:
*/
abstract class AutomataDriver extends DslDriver[Unit,Automaton[Char,Boolean]] with DFAOpsExp { q =>
  override val codegen = new ScalaGenDFAOps {
    val IR: q.type = q
  }
  def matches(s: String): Boolean = {
    var a: Automaton[Char,Boolean] = f(())
    var i: Int = 0
    while (!a.out && i < s.length) {
      a = a.next(s(i))
      i += 1
    }
    a.out
  }
}

/**
If the matcher and input iteration logic is generated together, further
translations can be applied to transform the mutually recursive lambdas into
tight imperative state machines.

Here is the generated code for `.*AAB`, shown initially:

      .. includecode:: ../../../../out/dfa_aab.check.scala

What's next?
------------

Go back to the [tutorial index](index.html) or continue with the [SQL Query Compiler](query.html).

*/
