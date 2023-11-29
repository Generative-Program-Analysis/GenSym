package gensym.wasm.learnstaging

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize

import gensym.lmsx._

case class Var(name: String) extends Term
case class Abs(name: String, body: Term) extends Term
case class LApp(f: Term, v: Term) extends Term
case class Const(value: Int) extends Term
case class Add(l: Term, r: Term) extends Term

abstract class Term {
  def eval(env: Map[String, Term]): Term = this match {
    case Var(name) => env(name)
    case LApp(f, v) => f.eval(env) match {
      case Abs(n, b) => b.eval(env + (n -> v.eval(env)))
      case _ => throw new Exception("not a function")
    }
    case Add(l, r) => (l.eval(env), r.eval(env)) match {
      case (Const(lv), Const(rv)) => Const(lv + rv)
      case _ => throw new Exception("not a number")
    }
    case _ => this
  }
}

object Lambda extends App {
  val t = Add(Const(1), Const(2))
  println(t.eval(Map()))
}

abstract class Instr
case class PushConst(value: Int) extends Instr
case class GetVar(name: String) extends Instr
case object Add extends Instr

@virtualize
trait StagedExec extends Dsl with SAIOps {
  def eval(instrs: List[Instr], stack: Rep[List[Int]], env: Rep[Map[String, Int]]): Rep[List[Int]] = instrs match {
    case Nil => stack
    case PushConst(value) :: rest => eval(rest, value :: stack, env)
    case GetVar(name) :: rest => eval(rest, env(name) :: stack, env)
    case Add :: nextInstrs => {
      val (l, r) = (stack(0), stack(1))
      eval(nextInstrs, (l + r) :: stack.drop(2), env)
    }
    // case Add :: nextInstrs => stack match {
    //   case l :: r :: rest => eval(nextInstrs, (l + r) :: rest, env)
    //   case _ => throw new Exception("not a number")
    // }
  }
}

// @virtualize
// trait StagedLambda extends Dsl {
//   def eval(term: Term, env: Rep[Map[String, Term]]): Rep[Term] = term match {
//     case Var(name) => env(name)
//     case LApp(f, v) => eval(f, env) match {
//       case Abs(n, b) => eval(b, env + (n -> eval(v, env)))
//       case _ => throw new Exception("not a function")
//     }
//     case Add(l, r) => (l.eval(env), r.eval(env)) match {
//       case (Const(lv), Const(rv)) => Const(lv + rv)
//       case _ => throw new Exception("not a number")
//     }
//     case _ => term
//   }
// }

@virtualize
trait StagedRegexpMatcher extends Dsl {

  /* search for regexp anywhere in text */
  def matchsearch(regexp: String, text: Rep[String]): Rep[Boolean] = {
    if (regexp(0) == '^')
      matchhere(regexp, 1, text, 0)
    else {
      var start = -1
      var found = false
      while (!found && start < text.length) {
        start += 1
        found = matchhere(regexp, 0, text, start)
      }
      found
    }
  }

  /* search for restart of regexp at start of text */
  def matchhere(regexp: String, restart: Int, text: Rep[String], start: Rep[Int]): Rep[Boolean] = {
    if (restart==regexp.length)
      true
    else if (regexp(restart)=='$' && restart+1==regexp.length)
      start==text.length
    else if (restart+1 < regexp.length && regexp(restart+1)=='*')
      matchstar(regexp(restart), regexp, restart+2, text, start)
    else if (start < text.length && matchchar(regexp(restart), text(start)))
      matchhere(regexp, restart+1, text, start+1)
    else false
  }

  /* search for c* followed by restart of regexp at start of text */
  def matchstar(c: Char, regexp: String, restart: Int, text: Rep[String], start: Rep[Int]): Rep[Boolean] = {
    var sstart = start
    var found = matchhere(regexp, restart, text, sstart)
    var failed = false
    while (!failed && !found && sstart < text.length) {
      failed = !matchchar(c, text(sstart))
      sstart += 1
      found = matchhere(regexp, restart, text, sstart)
    }
    !failed && found
  }

  def matchchar(c: Char, t: Rep[Char]): Rep[Boolean] = {
    //c == '.' || c == t
    // NOTE: implicits with macros currently do not 
    // catch (Boolean || Rep[Boolean])
    if (c == '.') true else c == t
  }
}

@virtualize
object ReTest extends App {
  def mkVMSnippet(instrs: List[Instr]): DslDriver[Map[String, Int], List[Int]] with StagedExec = {
    new DslDriver[Map[String, Int], List[Int]] with StagedExec {
      def snippet(env: Rep[Map[String, Int]]): Rep[List[Int]] = {
        eval(instrs, Nil, env)
      }
    }
  }

  def mkSnippet(re: String): DslDriver[String,Boolean] with StagedRegexpMatcher = {
    new DslDriver[String,Boolean] with StagedRegexpMatcher {
      def snippet(text: Rep[String]): Rep[Boolean] = {
        matchsearch(re, text)
      }
    }
  }

  // val instrs = List(PushConst(1), PushConst(2), Add)
  // val snippet = mkVMSnippet(instrs)
  // println(snippet.code)
  // println(snippet.eval(Map()))
  val re = "^$"
  val snippet = mkSnippet(re)
  println(snippet.code)
  // println(snippet.eval("aaab"))
  // println(snippet.eval("aaa"))
}
