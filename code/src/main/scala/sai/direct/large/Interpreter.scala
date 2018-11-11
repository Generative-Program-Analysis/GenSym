package sai.direct.large

import sai.direct.large.parser._
import sai.direct.large.concrete._
import sai.utils.TestTrait
import scala.io.Source

object LargeSchemeInterpreter extends TestTrait {
  def apply(s: String) = {
    val ast = LargeSchemeParser(s) match {
      case Some(expr) =>
        LargeSchemeASTDesugar(expr)
    }
    (BigStepCES.eval(ast))._1
  }

  def main(args: Array[String]) = {
    if (args.isEmpty) {
      runtest()
    } else if (args(0) == "-f") {
      println(apply(Source.fromFile(args(1)).mkString))
    } else if (args(0) == "-t") {
      runtest(args(1))
    } else {
      println(apply(args(0)))
    }
  }

  def testall() = {
    test("factorial") {
      assert(CESK.NumV(40320) == apply(
      """
      (letrec
        ([factorial
          (lambda (k)
            (if (eq? k 0)
              1
              (* k (factorial (- k 1)))))])
        (factorial 8))
      """))
    }

    test("imperative") {
      assert(CESK.NumV(-2) == apply(
      """
      (define a 1)
      (if (eq? a 1) (set! a -2) (void))
      a
      """))
    }

    test("euclid_imp") {
      assert(CESK.NumV(8) == apply(
      """
      (define x 56)
      (define y 24)
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
                (loop_body)
              )
            )
          )])
        (loop_body))
      """))
    }

    test("euclid_rec") {
      assert(CESK.NumV(8) == apply(
      """
      (letrec
        ([gcd
          (lambda (a b)
            (if (eq? b 0)
              a
              (gcd b (% a b))))])
        (gcd 24 56))
      """
      ))
    }

    test("bool_fun") {
      assert(CESK.BoolV(false) == apply(
        """
        (define not (lambda (x) (if x #f #t)))
        (not #t)
        """
      ))
    }

    test("bool_logic") {
      assert(CESK.BoolV(false) == apply(
        """
        (define not (lambda (x) (if x #f #t)))
        (define and (lambda (x y) (if x y #f)))
        (define or (lambda (x y) (if x #t y)))
        (and (or #f #t) (not (or #t #t)))
        """
      ))
    }
  }
}
