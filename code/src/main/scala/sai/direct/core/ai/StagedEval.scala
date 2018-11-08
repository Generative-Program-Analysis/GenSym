package sai.direct.core.ai
import scala.language.implicitConversions
import scala.language.higherKinds
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common._

object eval {
  sealed trait Term
  sealed trait Value
  case class Lit(n: Int) extends Value with Term
  case class Bool(b: Boolean) extends Value with Term
  case class Var(s: String) extends Term {
    override def toString = "Var(\""+s+"\")"
  }
  case class Prim(p: String) extends Value with Term { //Why this is a Value?
    override def toString = "Prim(\""+p+"\")"
  }
  case class Lam(param: String, body: Term) extends Term {
    override def toString = "Lam(" + "\"" + param + "\", " + body + ")"
  }
  case class App(fun: Term, args: List[Term]) extends Term
  case class If(cnd: Term, thn: Term, els: Term) extends Term

  case class Clo(lam: Lam, env: Env) extends Value
  case class CompClo(f: Value=>Value) extends Value
  case class Code[R[_]](c: R[Value]) extends Value

  type Env = Map[String, Value]
  
  def apply_primitive(p: String, args: List[Value]): Value = (p, args) match {
    case ("<", List(Lit(a), Lit(b))) => Bool(a<b)
    case ("+", List(Lit(a), Lit(b))) => Lit(a+b)
    case ("-", List(Lit(a), Lit(b))) => Lit(a-b)
  }

  trait Ops[R[_]] {
    type T[A]
    def valueT: T[Value]
    def lift(v: Value): R[Value]
    def base_apply(fun: R[Value], args: List[R[Value]], env: Env): R[Value]
    def isTrue(v: R[Value]): R[Boolean]
    def ifThenElse[A:T](cnd: R[Boolean], thn: => R[A], els: => R[A]): R[A]
    def makeFun(f: R[Value] => R[Value]): R[Value]
  }
}

import eval._

object UnstagedEval {
  type NoRep[A] = A
  implicit object OpsNoRep extends Ops[NoRep] {
    type T[A] = Unit
    def valueT = ()
    def lift(v: Value) = v match {
      case Clo(Lam(x, body), env) => 
        trait Program extends EvalDsl {
          def snippet(arg: Rep[Value]): Rep[Value] = base_eval[Rep](body, env + (x -> Code(arg)))(OpsRep)
        }
        val r = new EvalDslDriver with Program; r.precompile
        CompClo(arg => r.eval(arg))
      case v => v
    }
    def base_apply(fun: Value, args: List[Value], env: Env) = base_apply_norep(fun, args, env)
    def isTrue(v: Value) = Bool(false) != v
    def ifThenElse[A:T](cnd: Boolean, thn: => A, els: => A): A = if (cnd) thn else els
    def makeFun(f: Value => Value) = CompClo(f)
  }

  def base_apply_norep(fun: Value, args: List[Value], env: Env) = fun match {
    //case Clo(param, body, cenv) => base_eval[NoRep](body, cenv + (param -> args(0)))
    case CompClo(f) => f(args(0))
    case Prim(p) => apply_primitive(p, args)
  }

  def base_evlist[R[_]:Ops](exps: List[Term], env: Env): List[R[Value]] = {
    exps.map(e => base_eval[R](e, env))
  }

  def top_eval[R[_]:Ops](exp: Term): R[Value] = base_eval[R](exp, Map())
  
  def base_eval[R[_]:Ops](exp: Term, env: Env): R[Value] = {
    val o = implicitly[Ops[R]]; import o._
    exp match {
      case e@Lit(n) => lift(e)
      case e@Bool(b) => lift(e)
      case e@Prim(p) => lift(e)
      case Var(s) => env.get(s) match {
        case Some(Code(v)) => v.asInstanceOf[R[Value]]
        case Some(v) => lift(v)
      }
      case e@Lam(x, body) => lift(Clo(e, env))
      case App(fun, args) => 
        val fv = base_eval[R](fun, env)
        val argvs = base_evlist[R](args, env)
        base_apply(fv, argvs, env)
      case If(cnd, thn, els) => 
        val vc = base_eval[R](cnd, env)
        ifThenElse(isTrue(vc),
          base_eval[R](thn, env),
          base_eval[R](els, env))(valueT)
    }
  }
}

import UnstagedEval._

trait EvalDsl extends Dsl with UncheckedOps {
  implicit def valTyp: Typ[Value]
  def base_apply_rep(f: Rep[Value], args: List[Rep[Value]], env: Env): Rep[Value]
  implicit object OpsRep extends scala.Serializable with Ops[Rep] {
    type T[A] = Typ[A]
    def valueT = typ[Value]
    def lift(v: Value) = v match {
      case Clo(Lam(x, body), env) =>
        makeFun(arg => base_eval[Rep](body, env + (x -> Code(arg))))
      case v => unit(v)
    }
    def base_apply(f: Rep[Value], args: List[Rep[Value]], env: Env) =
      base_apply_rep(f, args, env)
    def isTrue(v: Rep[Value]): Rep[Boolean] = unit[Value](Bool(false))!=v
    def ifThenElse[A:T](cnd: Rep[Boolean], thn: => Rep[A], els: => Rep[A]): Rep[A] = if (cnd) thn else els
    def makeFun(f: Rep[Value] => Rep[Value]) = unchecked("CompClo(", fun(f), ")")
  }

  def snippet(x: Rep[Value]): Rep[Value]
}

trait EvalDslExp extends EvalDsl with DslExp with UncheckedOpsExp {
  implicit def valTyp: Typ[Value] = manifestTyp
  case class BaseApplyRep(f: Rep[Value], args: List[Rep[Value]], env: Env) extends Def[Value]
  def base_apply_rep(f: Rep[Value], args: List[Rep[Value]], env: Env): Rep[Value] =
    reflectEffect(BaseApplyRep(f, args, env))
}

trait EvalDslGen extends ScalaGenFunctions with ScalaGenIfThenElse with ScalaGenEqual with ScalaGenUncheckedOps {
  val IR: EvalDslExp
  import IR._
  def env_quote(env: Env) =
    "Map("+(for ((k,v) <- env) yield ("(\""+k+"\" -> "+quote(Const(v))+")")).mkString(", ")+")"

  override def quote(x: Exp[Any]) : String = x match {
    case Const(Code(c)) => quote(c.asInstanceOf[Rep[Value]])
    //case Const(Clo(param, body, env)) =>  "Clo(\""+param+"\", "+body+", "+env_quote(env)+")"
    case _ => super.quote(x)
  }
  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case BaseApplyRep(f, args, env) =>
      emitValDef(sym, "base_apply_norep("+quote(f)+", List("+args.map(quote).mkString(", ")+"), "+env_quote(env)+")")
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
      stream.println("import sai.direct.core.ai.eval._")
      stream.println("import sai.direct.core.ai.UnstagedEval._")
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

object StagedEval extends TutorialFunSuite {
  val under = "wow"
  def id = App(Lam("x", Var("x")), List(Lit(1)))
  def Y(c: Boolean) = Lam("fun", App(Lam("F", App(Var("F"), List(Var("F")))), List(Lam("F", App(Var("fun"), List(Lam("x", App(App(Var("F"), List(Var("F"))), List(Var("x"))))))))))
  def fib(c: Boolean) = Lam("fib", Lam("n", If(App(Prim("<"), List(Var("n"), Lit(2))), Var("n"), App(Prim("+"), List(App(Var("fib"), List(App(Prim("-"), List(Var("n"), Lit(1))))), App(Var("fib"), List(App(Prim("-"), List(Var("n"), Lit(2))))))))))
  def sumf(c: Boolean) = Lam("f", Lam("sumf", Lam("n", If(App(Prim("<"), List(Var("n"), Lit(0))), Lit(0), App(Prim("+"), List(App(Var("f"), List(Var("n"))), App(Var("sumf"), List(App(Prim("-"), List(Var("n"), Lit(1)))))))))))
  def main(arg: Array[String]) {
    val fib7 = App(App(Y(true), List(fib(true))), List(Lit(7)))
    //println(top_eval[NoRep](fib7))
    trait Program extends EvalDsl {
      def snippet(arg: Rep[Value]): Rep[Value] = 
        base_eval[Rep](fib7, Map())
    }
    val r = new EvalDslDriver with Program
    println(r.code)
    println(r.eval(Lit(7)))
  }
}
