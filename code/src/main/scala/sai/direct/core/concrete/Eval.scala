package sai.direct.core.ai

// Big-step, denotational, continuation-passing style interpreter

object Eval {
  type Env = Map[String, Value]
  type Cont[R[_]] = R[Value] => R[Value]

  sealed trait Term
  sealed trait Value
  case class I(n: Int) extends Value with Term
  case class B(b: Boolean) extends Value with Term
  case class V(x: String) extends Term {
    override def toString = "V(\"" + x + "\")"
  }
  case class P(prim: String) extends Value with Term {
    override def toString = "P(\"" + prim + "\")"
  }
  case class L(compile: Boolean, param: String, body: Term) extends Term {
    override def toString = "L(" + compile + ", \"" + param + "\", " + body + ")"
  }
  case class A(fun: Term, args: List[Term]) extends Term
  case class If(cnd: Term, thn: Term, els: Term) extends Term

  case class Clo(param: String, body: Term, env: Env) extends Value

  //TODO: Put function in here
  case class Evalfun(key: String) extends Value {
    override def toString = "Evalfun(\"" + key + "\")"
  }

  case class Code[R[_]](c: R[Value]) extends Value

  var funs = Map[String, Value => Value]()
  def addFun(f: Value => Value): String = {
    val key = "f"+funs.size
    funs += (key -> f)
    key
  }
  def reset() { funs = funs.empty }
  def evalfun(f: Value => Value) = Evalfun(addFun(f))

  def apply_primitive(prim: String, args: List[Value]): Value = (prim, args) match {
    case ("<", List(I(a), I(b))) => B(a < b)
    case ("+", List(I(a), I(b))) => I(a + b)
    case ("-", List(I(a), I(b))) => I(a - b)
  }

  trait Ops[R[_]] {
    type T[A]
    def valueT: T[Value]
    def lift(v: Value): R[Value]
    def base_apply(fun: R[Value], args: List[R[Value]], env: Env, cont: Cont[R]): R[Value]
    def isTrue(v: R[Value]): R[Boolean]
    def ifThenElse[A:T](cnd: R[Boolean], thn: => R[A], els: => R[A]): R[A]
    def makeFun(f: R[Value] => R[Value]): R[Value]
    def inRep: Boolean
  }

  type NoRep[A] = A

  implicit object OpsNoRep extends Ops[NoRep] {
    type T[A] = Unit
    def valueT = ()
    def lift(v: Value) = v
    def base_apply(fun: Value, args: List[Value], env: Env, cont: Cont[NoRep]) =
      base_apply_norep(fun, args, env, cont)
    def isTrue(v: Value) = B(false) != v
    def ifThenElse[A:T](cnd: Boolean, thn: => A, els: => A): A = if (cnd) thn else els
    def makeFun(f: Value => Value) = evalfun(f)
    def inRep = false
  }

  def base_apply_norep(fun: Value, args: List[Value], env: Env, cont: Cont[NoRep]) = fun match {
    case Clo(param, body, cenv) => base_eval[NoRep](body, cenv + (param -> args(0)), cont)
    case Evalfun(key) => cont(funs(key)(args(0)))
    case P(p) => cont(apply_primitive(p, args))
  }

  def base_evlist[R[_]:Ops](exps: List[Term], env: Env, cont: List[R[Value]] => R[Value]): R[Value] = exps match {
    case Nil => cont(Nil)
    case e::es => base_eval[R](e, env, { v => base_evlist[R](es, env, { vs => cont(v::vs) }) })
  }

  def top_eval[R[_]:Ops](exp: Term): R[Value] = {
    reset(); base_eval[R](exp, Map(), x => x)
  }

  def base_eval[R[_]:Ops](exp: Term, env: Env, cont: Cont[R]): R[Value] = {
    val o = implicitly[Ops[R]]; import o._
    exp match {
      case e@I(n) => cont(lift(e))
      case e@B(b) => cont(lift(e))
      case e@P(p) => cont(lift(e))
      case V(x) => env.get(x) match {
        case Some(Code(v)) => cont(v.asInstanceOf[R[Value]])
        case Some(v) => cont(lift(v))
        case None => ??? //TODO: if we return a representation itself, do we have a partial evaluator?
      }
      case L(compile, param, body) =>
        if (!compile) {
          cont(lift(Clo(param, body, env)))
        }
        else if (!inRep) {
          trait Program extends EvalDsl {
            def snippet(arg: Rep[Value]): Rep[Value] = {
              base_eval[Rep](body, env + (param -> Code(arg)), x => x)(OpsRep)
            }
          }
          val r = new EvalDslDriver with Program
          println(r.code)
          r.precompile
          val f = arg => r.eval(arg)
          cont(lift(evalfun(f)))
        }
        else {
          val f = makeFun(arg => base_eval[R](body, env + (param -> Code(arg)), x => x))
          cont(f)
        }
      case A(fun, args) => base_eval[R](fun, env, { funv =>
                                          base_evlist[R](args, env, { vs => base_apply(funv, vs, env, cont) })
                                        })
      case If(cnd, thn, els) => base_eval[R](cnd, env, { vc =>
                                               ifThenElse(isTrue(vc),
                                                          base_eval[R](thn, env, cont),
                                                          base_eval[R](els, env, cont))(valueT)
                                             })
    }
  }
}

import Eval._
import scala.lms.common._
import scala.lms.tutorial._

trait EvalDsl extends Dsl with UncheckedOps {
  implicit def valTyp: Typ[Value]
  def base_apply_rep(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Cont[Rep]): Rep[Value]
  implicit object OpsRep extends scala.Serializable with Ops[Rep] {
    type T[A] = Typ[A]
    def valueT = typ[Value]
    def lift(v: Value) = unit(v)
    def base_apply(f: Rep[Value], args: List[Rep[Value]], env: Env, cont: Cont[Rep]) =
      base_apply_rep(f, args, env, cont)
    def isTrue(v: Rep[Value]): Rep[Boolean] = unit[Value](B(false)) != v
    def ifThenElse[A:T](cnd: Rep[Boolean], thn: => Rep[A], els: => Rep[A]): Rep[A] =
      if (cnd) thn else els
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

  override def quote(x: Exp[Any]): String = x match {
    case Const(Code(c)) => quote(c.asInstanceOf[Rep[Value]])
    case Const(Clo(param, body, env)) => "Clo(\""+param+"\", "+body+", "+env_quote(env)+")"
    case _ => super.quote(x)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case BaseApplyRep(f, args, env, cont) =>
      emitValDef(sym, "base_apply_norep("+quote(f)+", List("+args.map(quote).mkString(", ")+"), "+env_quote(env)+", "+quote(cont)+")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait EvalDslImp extends EvalDslExp { q =>
  val codegen = new EvalDslGen {
    val IR: q.type = q

    override def remap[A](m: Typ[A]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }

    override def emitSource[A:Typ](args: List[Sym[_]], body: Block[A], className: String, stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      stream.println("import sai.aam.Eval._")
      super.emitSource(args, body, className, stream)
    }
  }
}

abstract class EvalDslDriver extends EvalDsl with EvalDslImp with CompileScala {
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

object TestEvaluator extends TutorialFunSuite {
  val under = "eval_"

  def ex_id(c: Boolean) = A(L(c, "x", V("x")), List(I(1)))
  def Y(c: Boolean) = L(c, "fun", A(L(c, "F", A(V("F"), List(V("F")))), List(L(c, "F", A(V("fun"), List(L(c, "x", A(A(V("F"), List(V("F"))), List(V("x"))))))))))
  def fib(c: Boolean) = L(c, "fib", L(c, "n", If(A(P("<"), List(V("n"), I(2))), V("n"), A(P("+"), List(A(V("fib"), List(A(P("-"), List(V("n"), I(1))))), A(V("fib"), List(A(P("-"), List(V("n"), I(2))))))))))
  def sumf(c: Boolean) = L(c, "f", L(c, "sumf", L(c, "n", If(A(P("<"), List(V("n"), I(0))), I(0), A(P("+"), List(A(V("f"), List(V("n"))), A(V("sumf"), List(A(P("-"), List(V("n"), I(1)))))))))))

  def main(args: Array[String]) {
    val v = top_eval[NoRep](sumf(true))
    println(v)
    println(funs)
  }
}
