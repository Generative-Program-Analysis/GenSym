/**
An auto-specializing interpreter
================================

We show an end-to-end LMS example, exploiting once more that a "staged
interpreter is a compiler" but with a twist: parametric staging!

- Georg Ofenbeck et al. at GPCE 2013
  _Spiral in Scala: Towards the Systematic Construction of Generators for Performance Libraries_
  The technique of abstracting over staging decision is inspired by this work.

- Kenichi Asai at GPCE 2014
  _Compiling a Reflective Language using MetaOCaml_
  Specializing lambdas in our interpreter is loosely inspired by this
  paper, which features a much more powerful and flexible Scheme-like
  object language.

*/

package scala.lms.tutorial

/**
Our Base Evaluator
------------------

We use an object `eval` to which we can easily refer in the generated code.

*/
object eval {

/**

We use plain Scala ASTs for our `Term`s and `Value`s. We change the
`toString` representation of the ASTs so that each is self-evaluating in
Scala, so that they can be printed as such in the generated code.

*/
  sealed trait Term
  type Env = Map[String, Value]
/**
We see our first `R[_]` to abstract over whether we have a `Rep` (staged) value or a `NoRep` (unstaged) one.
*/
  type Cont[R[_]] = R[Value] => R[Value]
  sealed trait Value
/**
Our object language just has enough features to execute fibonacci.
Hence, the terms include just the following:
*/
  // number
  case class I(n: Int) extends Value with Term
  // boolean
  case class B(b: Boolean) extends Value with Term
  // variable
  case class V(s: String) extends Term {
    override def toString = "V(\""+s+"\")"
  }
  // primitive
  case class P(p: String) extends Value with Term {
    override def toString = "P(\""+p+"\")"
  }
  // lambda
  case class L(compile: Boolean, param: String, body: Term) extends Term {
    override def toString = "L("+compile+", \""+param+"\", "+body+")"
  }
  // application
  case class A(fun: Term, args: List[Term]) extends Term
  // if-expression
  case class If(c: Term, thenp: Term, elsep: Term) extends Term
/**
Now, on to non-trivial values.

A `Closure` represents the value of uncompiled lambda. We leave the quoting of closures to the
code generation because we need to quote the environment.
*/
  case class Clo(param: String, body: Term, env: Map[String, Value]) extends Value
/**
An `Evalfun` represents the value of a compiled lambda, that is a function `Value => Value`.
However, in order for these to self-quote nicely, we unfortunately need a level of 
indirection from a key (a `String`) to be mapped to the actual compiled function
below.
*/
  case class Evalfun(key: String) extends Value {
    override def toString = "Evalfun(\""+key+"\")"
  }
/**
Finally, we sometimes need a `Rep[Value]` for the parameter of a lambda to be compiled.
A `Code` enables such a representation.
*/
  case class Code[R[_]](c: R[Value]) extends Value

/**
Our map from the key of each `Evalfun` to the actual compiled function.
This is a bit of a leak, and so we need to reset the map between runs.
*/
  var funs = Map[String, Value => Value]()
  def addFun(f: Value => Value): String = {
    val key = "f"+funs.size
    funs += (key -> f)
    key
  }
  def reset() {
    funs = funs.empty
  }
  def evalfun(f: Value => Value) = Evalfun(addFun(f))

/**
Applying a primitive is straightforward.
*/
  def apply_primitive(p: String, args: List[Value]): Value = (p, args) match {
    case ("<", List(I(a), I(b))) => B(a < b)
    case ("+", List(I(a), I(b))) => I(a+b)
    case ("-", List(I(a), I(b))) => I(a-b)
  }

/**
The `Ops` typeclass encapsulates all we need to know in order to parametrically stage the code. We define `Ops[Rep]` later outside the object.
*/
  trait Ops[R[_]] {
    type T[A]
    def valueT: T[Value]
    def lift(v: Value): R[Value]
    def base_apply(fun: R[Value], args: List[R[Value]], env: Env, cont: Cont[R]): R[Value]
    def isTrue(v: R[Value]): R[Boolean]
    def ifThenElse[A:T](cond: R[Boolean], thenp: => R[A], elsep: => R[A]): R[A]
    def makeFun(f: R[Value] => R[Value]): R[Value]
    def inRep: Boolean
  }
/**
The type `NoRep` enables us to run the evaluator "now".
*/
  type NoRep[A] = A
  implicit object OpsNoRep extends Ops[NoRep] {
    type T[A] = Unit
    def valueT = ()
    def lift(v: Value) = v
    def base_apply(fun: Value, args: List[Value], env: Env, cont: Cont[NoRep]) =
      base_apply_norep(fun, args, env, cont)
    def isTrue(v: Value) = B(false)!=v
    def ifThenElse[A:T](cond: Boolean, thenp: => A, elsep: => A): A = if (cond) thenp else elsep
    def makeFun(f: Value => Value) = evalfun(f)
    def inRep = false
  }

/**
The unstaged base application will also be called from the generated code.
*/
  def base_apply_norep(fun: Value, args: List[Value], env: Env, cont: Cont[NoRep]) = fun match {
    case Clo(param, body, cenv) =>
      base_eval[NoRep](body, cenv + (param -> args(0)), cont)
    case Evalfun(key) =>
      val f = funs(key)
      cont(f(args(0)))
    case P(p) =>
      cont(apply_primitive(p, args))
  }

/**
Now, on to our evaluation functions.

Evaluate a list of terms.
*/
  def base_evlist[R[_]:Ops](exps: List[Term], env: Env, cont: List[R[Value]] => R[Value]): R[Value] = exps match {
    case Nil => cont(Nil)
    case e::es => base_eval[R](e, env, { v =>
      base_evlist[R](es, env, { vs =>
        cont(v::vs)
      })
    })
  }

/**
Evaluate a top-level term.
*/
  def top_eval[R[_]:Ops](exp: Term): R[Value] = {
    reset()
    base_eval[R](exp, Map(), x => x)
  }
/**
Finally, the evaluator.

A key take-away is that by just staging the continuation
from a `Value => Value` to a `Rep[Value] => Rep[Value]`,
we can generate code for a known term instead of evaluating it directly.

However, the cost of combining the staged and unstaged `base_eval` is
that the function is no longer tail-recursive.
*/
  def base_eval[R[_]:Ops](exp: Term, env: Env, cont: Cont[R]): R[Value] = {
    val o = implicitly[Ops[R]]; import o._
    exp match {
      case e@I(n) => cont(lift(e))
      case e@B(b) => cont(lift(e))
      case e@P(p) => cont(lift(e))
      case V(s) => env.get(s) match {
        case Some(Code(v)) => cont(v.asInstanceOf[R[Value]])
        case Some(v) => cont(lift(v))
      }
      case L(compile, param, body) =>
        if (!compile) {
          cont(lift(Clo(param, body, env)))
        } else if (!inRep) {
          trait Program extends EvalDsl {
            def snippet(arg: Rep[Value]): Rep[Value] =
              base_eval[Rep](body, env+(param -> Code(arg)), x => x)(OpsRep)
          }
          val r = new EvalDslDriver with Program
          println(r.code)
          r.precompile
          val f = arg => r.eval(arg)
          cont(lift(evalfun(f)))
        } else {
          val f = makeFun(arg => base_eval[R](body, env+(param -> Code(arg)), x => x))
          cont(f)
        }
      case A(fun, args) => base_eval[R](fun, env, { v =>
        base_evlist[R](args, env, { vs =>
          base_apply(v, vs, env, cont)
        })
      })
      case If(cond, thenp, elsep) => base_eval[R](cond, env, { vc =>
        ifThenElse(isTrue(vc),
          base_eval[R](thenp, env, cont),
          base_eval[R](elsep, env, cont))(valueT)
      })
    }
  }
}

/**
The LMS bits
------------
*/
import eval._
import scala.lms.common._

trait EvalDsl extends Dsl with UncheckedOps {
  implicit def valTyp: Typ[Value]
  def base_apply_rep(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Cont[Rep]): Rep[Value]
  implicit object OpsRep extends scala.Serializable with Ops[Rep] {
    type T[A] = Typ[A]
    def valueT = typ[Value]
    def lift(v: Value) = unit(v)
    def base_apply(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Cont[Rep]) =
      base_apply_rep(f, args, env, cont)
    def isTrue(v: Rep[Value]): Rep[Boolean] = unit[Value](B(false))!=v
    def ifThenElse[A:T](cond: Rep[Boolean], thenp: => Rep[A], elsep: => Rep[A]): Rep[A] = if (cond) thenp else elsep
    def makeFun(f: Rep[Value] => Rep[Value]) = unchecked("evalfun(", fun(f), ")")
    def inRep = true
  }

  def snippet(x: Rep[Value]): Rep[Value]
}

trait EvalDslExp extends EvalDsl with DslExp with UncheckedOpsExp {
  implicit def valTyp: Typ[Value] = manifestTyp
  case class BaseApplyRep(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Rep[Cont[NoRep]]) extends Def[Value]
  def base_apply_rep(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Cont[Rep]): Rep[Value] =
    reflectEffect(BaseApplyRep(f, args, env, fun(cont)))
}

trait EvalDslGen extends ScalaGenFunctions with ScalaGenIfThenElse with ScalaGenEqual with ScalaGenUncheckedOps {
  val IR: EvalDslExp
  import IR._

  def env_quote(env: Env) =
    "Map("+(for ((k,v) <- env) yield ("(\""+k+"\" -> "+quote(Const(v))+")")).mkString(", ")+")"

  override def quote(x: Exp[Any]) : String = x match {
    case Const(Code(c)) => quote(c.asInstanceOf[Rep[Value]])
    case Const(Clo(param, body, env)) =>  "Clo(\""+param+"\", "+body+", "+env_quote(env)+")"
    case _ => super.quote(x)
  }
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case BaseApplyRep(f, args, env, cont) =>
      emitValDef(sym, "base_apply_norep("+quote(f)+", List("+args.map(quote).mkString(", ")+"), "+env_quote(env)+", "+quote(cont)+")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait EvalDslImpl extends EvalDslExp { q =>
  val codegen = new EvalDslGen {
    val IR: q.type = q

    override def remap[A](m: Typ[A]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }

    override def emitSource[A : Typ](args: List[Sym[_]], body: Block[A], className: String, stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      stream.println("import scala.lms.tutorial.eval._")
      super.emitSource(args, body, className, stream)
    }

  }
}

abstract class EvalDslDriver extends EvalDsl with EvalDslImpl with CompileScala {
  lazy val f = compile(snippet)
  def precompile: Unit = { Console.print("// "); f }
  def precompileSilently: Unit = utils.devnull(f)
  def eval(x: Value): Value = f(x)
  lazy val code: String = {
    val source = new java.io.StringWriter()
    codegen.emitSource(snippet, "Snippet", new java.io.PrintWriter(source))
    source.toString
  }
}

/**
The Tests
---------
*/
class TestEvaluator extends TutorialFunSuite {
  val under = "eval_"

  def ex_id(c: Boolean) = A(L(c, "x", V("x")), List(I(1)))
  test ("id evaluated") {
    assertResult(I(1)){top_eval[NoRep](ex_id(false))}
  }

  test ("id compiled") {
    checkOut("id", "scala",
      assertResult(I(1)){top_eval[NoRep](ex_id(true))})
  }

  def Y(c: Boolean) = L(c, "fun", A(L(c, "F", A(V("F"), List(V("F")))), List(L(c, "F", A(V("fun"), List(L(c, "x", A(A(V("F"), List(V("F"))), List(V("x"))))))))))
  def fib(c: Boolean) = L(c, "fib", L(c, "n", If(A(P("<"), List(V("n"), I(2))), V("n"), A(P("+"), List(A(V("fib"), List(A(P("-"), List(V("n"), I(1))))), A(V("fib"), List(A(P("-"), List(V("n"), I(2))))))))))
  def sumf(c: Boolean) = L(c, "f", L(c, "sumf", L(c, "n", If(A(P("<"), List(V("n"), I(0))), I(0), A(P("+"), List(A(V("f"), List(V("n"))), A(V("sumf"), List(A(P("-"), List(V("n"), I(1)))))))))))

  test ("fib 7 evaluated") {
    assertResult(I(13)){
      top_eval[NoRep](A(A(Y(false), List(fib(false))), List(I(7))))}
  }

  test ("fib 7 compiled fib") {
    checkOut("yfibc", "scala",
      assertResult(I(13)){
        top_eval[NoRep](A(A(Y(false), List(fib(true))), List(I(7))))})
  }

  test ("fib 7 compiled all") {
    checkOut("ycfibc", "scala",
      assertResult(I(13)){
        top_eval[NoRep](A(A(Y(true), List(fib(true))), List(I(7))))})
  }

  test ("sum of fibs evaluated") {
    assertResult(I(33)){
      top_eval[NoRep](A(A(Y(false), List(A(sumf(false), List(A(Y(false), List(fib(false))))))), List(I(7))))}
  }

  test ("sum of fibs compiled") {
    assertResult(I(33)){
      top_eval[NoRep](A(A(Y(true), List(A(sumf(true), List(A(Y(true), List(fib(true))))))), List(I(7))))}
  }
}

/**
Generated Code
-------------

This is code generated while specializing the `fib` lambda.

      .. includecode:: ../../../../out/eval_yfibc.check.scala

*/
