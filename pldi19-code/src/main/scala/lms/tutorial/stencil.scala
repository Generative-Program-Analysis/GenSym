/**
Sliding Stencil
===============

Shonan Challenge 3.3 Stencil
https://groups.google.com/d/msg/stagedhpc/r5L4xGJETwE/1kOdwJo0kgAJ

Outline:
<div id="tableofcontents"></div>

*/

package scala.lms.tutorial

import scala.lms.common.ForwardTransformer
import scala.reflect.SourceContext

// Shonan Challenge 3.3 Stencil
// https://groups.google.com/d/msg/stagedhpc/r5L4xGJETwE/1kOdwJo0kgAJ

trait Sliding extends Dsl {
  def infix_sliding[T:Typ](n: Rep[Int], f: Rep[Int] => Rep[T]): Rep[Array[T]] = {
    val a = NewArray[T](n)
    sliding(0,n)(i => a(i) = f(i))
    a
  }
  def infix_sliding(r: Rep[Range]) = new {
    def foreach(f: Rep[Int] => Rep[Unit]): Rep[Unit] =
      sliding(r.start, r.end)(f)
  }
  def sliding(start: Rep[Int], end: Rep[Int])(f: Rep[Int] => Rep[Unit]): Rep[Unit]
}

trait NoSlidingExp extends DslExp with Sliding {
  // not actually sliding -- just to have a baseline reference
  def sliding(start: Rep[Int], end: Rep[Int])(f: Rep[Int] => Rep[Unit]): Rep[Unit] = {
    (start until end) foreach f
  }
}

trait SlidingExp extends DslExp with Sliding {
  object trans extends ForwardTransformer {
    val IR: SlidingExp.this.type = SlidingExp.this
  }
  def log(x: Any): Unit = {
    System.out.println("sliding log: "+x)
  }

  // some arithmetic rewrites to maximize sliding sharing
  override def int_plus(lhs: Exp[Int], rhs: Exp[Int])(implicit pos: SourceContext): Exp[Int] = ((lhs,rhs) match {
    case (Def(IntPlus(x:Exp[Int],Const(y:Int))), Const(z:Int)) => int_plus(x, unit(y+z)) // (x+y)+z --> x+(y+z)
    case (Def(IntMinus(x:Exp[Int],Const(y:Int))), Const(z:Int)) => int_minus(x, unit(y-z)) // (x-y)+z --> x-(y-z)
    case (x: Exp[Int], Const(z:Int)) if z < 0 => int_minus(x, unit(-z))
    case _ => super.int_plus(lhs,rhs)
  }).asInstanceOf[Exp[Int]]

  override def int_minus(lhs: Exp[Int], rhs: Exp[Int])(implicit pos: SourceContext): Exp[Int] = ((lhs,rhs) match {
    case (Def(IntMinus(x:Exp[Int],Const(y:Int))), Const(z:Int)) => int_minus(x, unit(y+z)) // (x-y)-z --> x-(y+z)
    case (Def(IntPlus(x:Exp[Int],Const(y:Int))), Const(z:Int)) => int_plus(x, unit(y-z)) // (x+y)-z --> x+(y-z)
    case (x: Exp[Int], Const(z:Int)) if z < 0 => int_plus(x, unit(-z))
    case _ => super.int_minus(lhs,rhs)
  }).asInstanceOf[Exp[Int]]

  def sliding(start: Rep[Int], end: Rep[Int])(f: Rep[Int] => Rep[Unit]): Rep[Unit] = {
    val i = fresh[Int]

    val save = context
    // evaluate loop contents f(i)
    val (r0,stms0) = reifySubGraph(f(i))
    val (((r1,stms1,subst1),(r2,stms2,subst2)), _) = reifySubGraph {
      reflectSubGraph(stms0)
      context = save
      // evaluate loop contents f(i+1)
      val ((r1,subst1),stms1) = reifySubGraph(trans.withSubstScope(i -> (i+1)) {
        stms0.foreach(s=>trans.traverseStm(s))
        (trans(r0), trans.subst)
      })
      val ((r2,stms2,subst2), _) = reifySubGraph {
        reflectSubGraph(stms1)
        context = save
        // evaluate loop contents f(i+2)
        val ((r2,subst2),stms2) = reifySubGraph(trans.withSubstScope(i -> (i+2)) {
          stms0.foreach(s=>trans.traverseStm(s))
          (trans(r0), trans.subst)
        })
        (r2,stms2,subst2)
      }
      ((r1,stms1,subst1), (r2,stms2,subst2))
    }
    context = save

    val defs = stms0.flatMap(_.lhs)
    // find overlap syms: defined by f(i), used by f(i+1) and f(i+2)
    val overlap01 = stms1.flatMap { case TP(s,d) => syms(d) filter (defs contains _) }.distinct
    val overlap02 = stms2.flatMap { case TP(s,d) => syms(d) filter (defs contains _) }.distinct

    if (overlap02.nonEmpty)
      log("Overlap beyond a single loop iteration is ignored, since not yet implemented.")

    val overlap0 = (overlap01++overlap02).distinct
    val overlap1 = overlap0 map subst1
    // build a variable for each overlap sym.
    // init variables by peeling first loop iteration.
    if (end > start) {
      val (rX,substX) = trans.withSubstScope(i -> start) {
        stms0.foreach(s=>trans.traverseStm(s))
        (trans(r0), trans.subst)
      }
      val vars = overlap0 map (x => var_new(substX(x))(x.tp,x.pos.head))
      // now generate the loop
      for (j <- (start + unit(1)) until end) {
        // read the overlap variables
        val reads = (overlap0 zip vars) map (p => (p._1, readVar(p._2)(p._1.tp,p._1.pos.head)))
        // emit the transformed loop body
        val (r,substY1) = trans.withSubstScope((reads:+(i->(j-unit(1)))): _*) {
          stms1.foreach(s=>trans.traverseStm(s))
          (trans(r1), trans.subst)
        }
        // write the new values to the overlap vars
        val writes = (overlap1 zip vars) map (p => (p._1, var_assign(p._2, substY1(p._1))(p._1.tp,p._1.pos.head)))
      }
    }
  }
}

trait SlidingWarmup extends Sliding {
  def snippet(n: Rep[Int]): Rep[Array[Int]] = {
    def compute(i: Rep[Int]) = 2*i+3
    n sliding { i => compute(i) + compute(i+1) }
  }
}
abstract class SlidingWarmupDriver extends DslDriver[Int,Array[Int]] with SlidingWarmup
class SlidingWarmupTest extends TutorialFunSuite {
  val under = "sliding"

  test("warmup without sliding") {
    val sliding0 = new SlidingWarmupDriver with NoSlidingExp
    check("0", sliding0.code)
  }

  test("warmup with sliding") {
    val sliding1 = new SlidingWarmupDriver with SlidingExp
    check("1", sliding1.code)
  }
}

trait Stencil extends Sliding {
  def snippet(v: Rep[Array[Double]]): Rep[Array[Double]] = {
    val n = v.length
    val input = v
    val output = NewArray[Double](n)
    def a(j: Rep[Int]) = input(j)
    def w1(j: Rep[Int]) = a(j) * a(j+1)
    def wm(j: Rep[Int]) = a(j) - w1(j) + w1(j-1)
    def w2(j: Rep[Int]) = wm(j) * wm(j+1)
    def b(j: Rep[Int]) = wm(j) - w2(j) + w2(j-1)
    for (i <- (2 until n-2).sliding) {
      output(i) = b(i)
    }
    output
  }
}
abstract class StencilDriver extends DslDriver[Array[Double],Array[Double]] with Stencil
class StencilTest extends TutorialFunSuite {
  val under = "stencil"

  test("stencil without sliding") {
    val stencil0 = new StencilDriver with NoSlidingExp
    check("0", stencil0.code)
  }

  test("stencil with sliding") {
    val stencil1 = new StencilDriver with SlidingExp
    check("1", stencil1.code)
  }
}
