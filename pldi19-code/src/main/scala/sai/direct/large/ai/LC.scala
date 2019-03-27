package sai.direct.large.ai

import sai.evaluation.TestPrograms._

import sai.direct.large.parser._

import scala.util.continuations._
import scala.language.implicitConversions
import scala.language.higherKinds

import sai.utils._
import sai.common.ai._
import sai.common.ai.Lattices.{NoRep => _, _}
import sai.direct.large.parser._

import sai.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOps => _, SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, ListOps ⇒ _, ListOpsExp ⇒ _, ScalaGenListOps ⇒ _, _}

object LamCal {
  trait Semantics {
    type R[+_]
    type Ident = String
    type Addr
    type Value
    type Env
    type Store
    type Ans = (R[Value], R[Store])
    val primops = Set("+", "-", "*", "/", "%", "eq?", "null?", "pair?", "and", 
      "or", "not", ">", "<", ">=", "<=", "=", "list", "cons", "car", "caar", "cadr", "cadddr",
      "caddr", "cdr", "cddr", "vector", "make-vector", "set-cdr!", "error", 
      "vector-set!", "vector-ref", "vector-length", "display", "write", "newline", 
      "read", "number->string", "remainder", "memq", "gcd", "modulo", "quotient", "odd?",
      "make-rectangular", "->fl", "fl*", "fl+", "real-part", "imag-part", "fl>", "name")
    def get(ρ: R[Env], x: Ident): R[Addr]
    def put(ρ: R[Env], x: Ident, a: R[Addr]): R[Env]
    def get(σ: R[Store], a: R[Addr]): R[Value]
    def put(σ: R[Store], a: R[Addr], v: R[Value]): R[Store]
    def alloc(σ: R[Store], x: Ident): R[Addr]
    def close(ev: EvalFun)(λ: Lam, ρ: R[Env]): R[Value] //TODO: can we just call eval_top
    def sym(s: Sym): R[Value]
    def int(i: IntLit): R[Value]
    def bool(b: BoolLit): R[Value]
    def float(f: FloatLit): R[Value]
    def char(c: CharLit): R[Value]
    def void(): R[Value]
    def apply_closure(ev: EvalFun)(f: R[Value], args: List[R[Value]], σ: R[Store]): Ans
    def prim_eval(op: String, lv: List[R[Value]]): R[Value]
    def branch(cnd: R[Value], thn: => Ans, els: => Ans): Ans
    val ρ0: R[Env]; val σ0: R[Store]
    type EvalFun = (Expr, R[Env], R[Store]) => Ans
    def fix(ev: EvalFun => EvalFun): EvalFun = (e, ρ, σ) => ev(fix(ev))(e, ρ, σ)
    def eval(ev: EvalFun)(e: Expr, ρ: R[Env], σ: R[Store]): Ans = {
      e match {
        case Sym(s) => (sym(Sym(s)), σ)
        case Var(x) => (get(σ, get(ρ, x)), σ)
        case IntLit(i) => (int(IntLit(i)), σ)
        case FloatLit(f) => (float(FloatLit(f)), σ)
        case BoolLit(b) => (bool(BoolLit(b)), σ)
        case CharLit(c) => (char(CharLit(c)), σ)
        case Lam(args, e) => (close(ev)(Lam(args, e), ρ), σ)
        case App(Var(op), args) if (primops(op)) =>
          val (σf, lrv): (R[Store], List[R[Value]]) = (args foldLeft((σ, List[R[Value]]()))) {
            case ((σ, lrv), arg) =>
              val (pv, pσ) = ev(arg, ρ, σ)
              (pσ, lrv :+ pv)
          }
          (prim_eval(op, lrv), σf)
        case App(e1, args) =>
          val (e1v, e1σ) = ev(e1, ρ, σ)
          val (σf, lrv): (R[Store], List[R[Value]]) = (args foldLeft((e1σ, List[R[Value]]()))) {
            case ((σ___, lrv), arg) =>
              val (pv, pσ) = ev(arg, ρ, σ___)
              (pσ, lrv :+ pv)
          }
          apply_closure(ev)(e1v, lrv, σf)
        case If(cnd, thn, els) =>
          val (cndv, cndσ) = ev(cnd, ρ, σ)
          branch(cndv, ev(thn, ρ, cndσ), ev(els, ρ, cndσ))
        case Void() => (void(), σ)
        case Set_!(x, e) =>
          val (v, σ_) = ev(e, ρ, σ)
          (void(), put(σ_, get(ρ, x), v))
      }
    }
    def eval_top(e: Expr): Ans = eval_top(e, ρ0, σ0)
    def eval_top(e: Expr, ρ: R[Env], σ: R[Store]): Ans = fix(eval)(e, ρ, σ)
  }

  trait Concrete extends Semantics {
    type Addr = Int
    sealed trait Value
    case class IntV(i: Int) extends Value
    case class FloatV(d: Double) extends Value
    case class CharV(c: Char) extends Value
    case class BoolV(b: Boolean) extends Value
    case class ListV(l: List[Value]) extends Value
    case class VectorV(v: Vector[Value]) extends Value
    case class CloV(λ: Lam, ρ: Env) extends Value
    case class VoidV() extends Value
    case class SymV(s: String) extends Value

    type Env = Map[Ident, Addr]
    type Store = Map[Addr, Value]
  }

  object ConcInterp extends Concrete {
    type R[+T] = T
    val ρ0: Env = Map[Ident,Addr]()
    val σ0: Store = Map[Addr,Value]()
    def get(ρ: Env, x: Ident): Addr = ρ(x)
    def put(ρ: Env, x: Ident, a: Addr): Env = ρ + (x → a)
    def get(σ: Store, a: Addr): Value = σ(a)
    def put(σ: Store, a: Addr, v: Value): Store = σ + (a → v)
    def alloc(σ: Store, x: Ident): Addr = σ.size + 1
    def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = CloV(λ, ρ)
    def sym(s: Sym): Value = SymV(s.x)
    def int(i: IntLit): Value = IntV(i.x)
    def bool(b: BoolLit): Value = BoolV(b.x)
    def float(f: FloatLit): Value = FloatV(f.x)
    def char(c: CharLit): Value = CharV(c.x)
    def void(): Value = VoidV()
    def apply_closure(ev: EvalFun)(f: Value, argvs: List[Value], σ: Store): Ans = f match {
      // TODO: What about PrimV? Load them into initial context?
      case CloV(Lam(args, e), ρ) =>
        val (ρ_*, σ_*): (Env, Store) = ((args zip argvs) foldLeft((ρ, σ))) {
          case ((ρ_, σ_), (argn, argv)) =>
            val α = alloc(σ_, argn)
            (put(ρ_, argn, α), put(σ_, α, argv))
        }
        ev(e, ρ_*, σ_*)
    }

    def branch(cnd: Value, thn: => Ans, els: => Ans): Ans = cnd match {
      case BoolV(b) => if (b) thn else els
    }

    def prim_eval(op: String, lv: List[Value]): Value = (op, lv) match {
      case ("+", List(IntV(n1), IntV(n2))) => IntV(n1 + n2)
      case ("-", List(IntV(n1), IntV(n2))) => IntV(n1 - n2)
      case ("*", List(IntV(n1), IntV(n2))) => IntV(n1 * n2)
      case ("/", List(IntV(n1), IntV(n2))) => IntV(n1 / n2)
      case ("%", List(IntV(n1), IntV(n2))) => IntV(n1 % n2)
      case ("eq?", List(v1, v2)) => BoolV(v1 == v2)
      case (">", List(IntV(n1), IntV(n2))) => BoolV(n1 > n2)
      case ("<", List(IntV(n1), IntV(n2))) => BoolV(n1 < n2)
      case (">=", List(IntV(n1), IntV(n2))) => BoolV(n1 >= n2)
      case ("<=", List(IntV(n1), IntV(n2))) => BoolV(n1 <= n2)
    }
  }

  trait LMSOps extends Dsl with MapOps with UncheckedOps with TupleOps with SetOps with TupledFunctions with ListOps
  trait RepConcInterpOps extends Concrete with LMSOps {
    type R[+T] = Rep[T]
    implicit def valueTyp: Typ[Value]
    val ρ0: Rep[Env] = Map[Ident,Addr]()
    val σ0: Rep[Store] = Map[Addr,Value]()
    def get(ρ: Rep[Env], x: Ident): Rep[Addr] = ρ(x)
    def put(ρ: Rep[Env], x: Ident, a: Rep[Addr]): Rep[Env] = ρ + (unit(x) → a)
    def get(σ: Rep[Store], a: Rep[Addr]): Rep[Value] = σ(a)
    def put(σ: Rep[Store], a: Rep[Addr], v: Rep[Value]): Rep[Store] = σ + (a → v)
    def alloc(σ: Rep[Store], x: Ident): Rep[Addr] = σ.size + 1
    def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
      val Lam(args, e) = λ
      // TODO: runtime error when using pattern matching
      // scala.MatchError: UnboxedTuple(List(Sym(7), Sym(8))) (of class scala.lms.common.TupledFunctionsExp$UnboxedTuple)
      //val f: Rep[(Value,Store)]=>Rep[(Value,Store)] = {
      //  case (arg: Rep[Value], σ: Rep[Store]) =>
      val f: Rep[(List[Value],Store)]=>Rep[(Value,Store)] = (as: Rep[(List[Value], Store)]) => {
        val argvs = as._1; val σ = as._2;
        def aux(argns: List[Ident], argvs: Rep[List[Value]], ρ: Rep[Env], σ: Rep[Store]): (Rep[Env], Rep[Store]) = argns match {
          case Nil => (ρ, σ)
          case x :: xs =>
            val α = alloc(σ, x)
            aux(xs, argvs.tail, put(ρ, x, α), put(σ, α, argvs.head))
        }
        val (ρ_*, σ_*) = aux(args, argvs, ρ, σ)
        ev(e, ρ_*, σ_*)
      }
      unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
    }

    def sym(s: Sym): Rep[Value] = unchecked("SymV(", s.x, ")")
    def int(i: IntLit): Rep[Value] = unchecked("IntV(", i.x, ")")
    def bool(b: BoolLit): Rep[Value] = unchecked("BoolV(", b.x, ")")
    def float(f: FloatLit): Rep[Value] = unchecked("FloatV(", f.x, ")")
    def char(c: CharLit): Rep[Value] = unchecked("CharV(", c.x, ")")
    def void(): Rep[Value] = unchecked("VoidV()")

    def branch(cnd: Rep[Value], thn: => Ans, els: => Ans): Ans = {
      val b = unchecked[Boolean](cnd, ".asInstanceOf[BoolV].b")
      (if (b) thn else els).asInstanceOf[Rep[(Value,Store)]] //FIXME: Why?
    }

    def prim_eval(op: String, lv: List[Rep[Value]]): Rep[Value] = op match {
      case "eq?" =>
        unchecked("BoolV(", lv(0), " == ", lv(1), ")")
      case op if ((scala.collection.immutable.Set(">", "<", ">=", "<="))(op)) =>
        val v1i = unchecked(lv(0), ".asInstanceOf[IntV].i")
        val v2i = unchecked(lv(1), ".asInstanceOf[IntV].i")
        unchecked("BoolV(", v1i, op, v2i, ")")
      case op if ((scala.collection.immutable.Set("+", "-", "*", "/", "%"))(op)) =>
        val v1i = unchecked(lv(0), ".asInstanceOf[IntV].i")
        val v2i = unchecked(lv(1), ".asInstanceOf[IntV].i")
        unchecked("IntV(", v1i, op, v2i, ")")

    }
  }

  trait LMSOpsExp extends DslExp with MapOpsExp with UncheckedOpsExp with TupleOpsExp with SetOpsExp with TupledFunctionsRecursiveExp with ListOpsExp
  trait RepConcInterpOpsExp extends RepConcInterpOps with LMSOpsExp {
    implicit def valueTyp: Typ[Value] = manifestTyp
    case class ApplyClosure(f: Rep[Value], args: List[Rep[Value]], σ: Rep[Store]) extends Def[(Value, Store)]
    def apply_closure(ev: EvalFun)(f: Rep[Value], args: List[Rep[Value]], σ: Rep[Store]): Ans = {
      reflectEffect(ApplyClosure(f, args, σ))
    }
  }

  trait RepConcSemGen extends GenericNestedCodegen {
    val IR: RepConcInterpOpsExp
    import IR._
    override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
      //case ApplyClosure(f, arg, σ) => emitValDef(sym, "apply_closure_norep(" + quote(f) + "," + quote(arg) + "," + quote(σ) + ")")
      case ApplyClosure(f, args, σ) =>
        val argsstr = args.map(quote).mkString(", ")
        emitValDef(sym, quote(f) + ".asInstanceOf[CompiledClo].f(List(" + argsstr + ")," + quote(σ) + ")")
      case Struct(tag, elems) =>
        //TODO: merge back to LMS
        registerStruct(structName(sym.tp), elems)
        val typeName = sym.tp.runtimeClass.getSimpleName + "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
        emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
      case _ => super.emitNode(sym, rhs)
    }
  }

  trait MyScalaGenTupleOps extends ScalaGenBase with TupleGenBase with ScalaGenStruct {
    val IR: TupleOpsExp
    import IR._

    override def remap[A](m: Typ[A]) = m.runtimeClass.getSimpleName match {
      case "Tuple2" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple3" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple4" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case "Tuple5" => m.runtimeClass.getSimpleName + "[" + m.typeArguments.map(a => remap(a)).mkString(",") + "]"
      case _ => super.remap(m)
    }
  }

  trait RepConcSemDriver extends DslDriver[Unit, Unit] with RepConcInterpOpsExp { q =>
    override val codegen = new DslGen with ScalaGenMapOps with MyScalaGenTupleOps
      with RepConcSemGen with MyScalaGenTupledFunctions with ScalaGenUncheckedOps
      with ScalaGenSetOps with ScalaGenListOps {
      val IR: q.type = q
      override def remap[A](m: Typ[A]): String = {
        if (m.toString.endsWith("$Value")) "Value"
        else super.remap(m)
      }
      override def emitSource[A : Typ](args: List[Sym[_]], body: Block[A],
                                       className: String,
                                       stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
        val prelude = """
  import sai.direct.large.parser._
  object RTSupport {
    trait Value
    case class IntV(i: Int) extends Value
    case class FloatV(d: Double) extends Value
    case class CharV(c: Char) extends Value
    case class BoolV(b: Boolean) extends Value
    case class ListV(l: List[Value]) extends Value
    case class VectorV(v: Vector[Value]) extends Value
    case class PrimV(f: List[Value] => Value) extends Value
    case class VoidV() extends Value
    case class SymV(s: String) extends Value
    case class CompiledClo(f: (List[Value], Map[Int,Value]) => (Value, Map[Int,Value]), λ: Lam, ρ: Map[String,Int]) extends Value
/*
    def apply_closure_norep(f: Value, arg: Value, σ: Map[Int,Value]) = f match {
      case CompiledClo(fun, λ, ρ) => fun(arg, σ)
    }
*/
  }
  import RTSupport._
        """
        stream.println(prelude)
        super.emitSource(args, body, className, stream)
      }
    }
  }
}

object SADI5 {
  import LamCal._
  def specialize(p: Expr): DslDriver[Unit, Unit] =
    new RepConcSemDriver {
      def snippet(unit: Rep[Unit]): Rep[Unit] = {
        val (v, s) = eval_top(p)
        println(v); println(s)
      }
    }

  def getAST(prog: String) = {
    LargeSchemeParser(prog) match {
      case Some(expr) => LargeSchemeASTDesugar(expr)
    }
  }

  def evalUnstaged(prog: Expr) = {
    ConcInterp.eval_top(prog)
  }

  def evalStaged(prog: Expr) = {
    val code = specialize(prog)
    println(code.code)
    code.eval(())
  }

  def main(args: Array[String]) {
    val id4 = App(Lam(List("x"), App(App(Var("x"), List(Var("x"))), List(Var("x")))), List(Lam(List("y"), Var("y"))))
    val oneplusone = App(Var("+"), List(IntLit(1), IntLit(1)))
    val fact5 = getAST("(define (fact n) (if (eq? n 0) 1 (* n (fact (- n 1))))) (fact 5)")
    val euclid = getAST(
      """
      (letrec
        ([gcd
          (lambda (a b)
            (if (eq? b 0)
              a
              (gcd b (% a b))))])
        (gcd 24 56))
      """)
    val euclid_imp = getAST(
      """
      (define x 24)
      (define y 56)
      (if (<= x y)
        (let ([temp x])
          (set! x y)
          (set! y temp))
        (void))
      (define r (% x y))
      (letrec
        ([loop_body
          (lambda ()
            (if (eq? r 0)
              y
              (begin
                (set! x y)
                (set! y r)
                (set! r (% x y))
                (loop_body))))])
        (loop_body))
      """
    )

    val church = getAST("""
      (define plus
        (lambda (p1)
          (lambda (p2)
            (lambda (pf)
        (lambda (x) ((p1 pf) ((p2 pf) x)))))))

      (define mult
        (lambda (m1)
          (lambda (m2)
            (lambda (mf) (m2 (m1 mf))))))

      (define pred
        (lambda (n)
          (lambda (rf)
            (lambda (rx)
        (((n (lambda (g) (lambda (h) (h (g rf)))))
          (lambda (ignored) rx))
        (lambda (id) id))))))

      (define sub
        (lambda (s1)
          (lambda (s2)
            ((s2 pred) s1))))


      (define church0 (lambda (f0) (lambda (x0) x0)))
      (define church1 (lambda (f1) (lambda (x1) (f1 x1))))
      (define church2 (lambda (f2) (lambda (x2) (f2 (f2 x2)))))
      (define church3 (lambda (f3) (lambda (x3) (f3 (f3 (f3 x3))))))
      (define church0? (lambda (z) ((z (lambda (zx) #f)) #t)))
      (define church=?
        (lambda (e1)
          (lambda (e2)
            (if (church0? e1)
          (church0? e2)
          (if (church0? e2)
              #f
              ((church=? ((sub e1) church1)) ((sub e2) church1)))))))

      ;; multiplication distributes over addition
      ((church=? ((mult church2) ((plus church1) church3)))
      ((plus ((mult church2) church1)) ((mult church2) church3)))
      """)
    println("staged: " + evalStaged(church))
    println("unstaged: " + evalUnstaged(church))
  }
}
