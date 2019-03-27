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
      assert(CESK.IntV(40320) == apply(
        """
        (letrec
          ([factorial
            (lambda (k)
              (if (eq? k 0)
                1
                (* k (factorial (- k 1)))))])
          (factorial 8))
        """
      ))
    }

    test("imperative") {
      assert(CESK.IntV(-2) == apply(
        """
        (define a 1)
        (if (eq? a 1) (set! a -2) (void))
        a
        """
      ))
    }

    test("euclid_imp") {
      assert(CESK.IntV(8) == apply(
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
                  (loop_body))))])
          (loop_body))
        """
      ))
    }

    test("euclid_rec") {
      assert(CESK.IntV(8) == apply(
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

    test("define_euclid_rec") {
      assert(CESK.IntV(8) == apply(
        """
        (define
          (gcd a b)
            (if (eq? b 0)
              a
              (gcd b (% a b))))
        (gcd 24 56)
        """
      ))
    }

    test("fibonacci_slow_rec") {
      assert(CESK.IntV(89) == apply(
        """
        (define
          (fib n)
            (if (eq? n 0) 1
              (if (eq? n 1) 1
                (+ (fib (- n 1)) (fib(- n 2))))))
        (fib 10)
        """
      ))
    }

    test("fibonacci_fast_imp") {
      assert(CESK.IntV(1346269) == apply(
        """
        (define curr 1)
        (define prev 1)
        (define (fib_loop n)
          (if (eq? n 0) curr
            (begin
              (define temp curr)
              (set! curr (+ curr prev))
              (set! prev temp)
              (fib_loop (- n 1)))))
        (define (fib n) (if (<= n 1) 1 (fib_loop (- n 1))))
        (fib 30)
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

    test("define_proc") {
      assert(CESK.BoolV(true) == apply(
        """
        (define (not b) (if b #f #t))
        (define (and x y) (if x y #f))
        (define (or x y) (if x #t y))
        (or (and (not #f) (or #f #t)) (not #f))
        """
      ))
    }

    test("define_proc_rec") {
      assert(CESK.IntV(243) == apply(
        """
        (define (pow a b) (if (eq? b 0) 1 (* a (pow a (- b 1)))))
        (pow 3 5)
        """
      ))
    }

    test("power_imp") {
      assert(CESK.IntV(243) == apply(
        """
        (define (do_n_times n f)
          (if (eq? n 0)
            (void)
            (begin (f) (do_n_times (- n 1) f))))

        (define (pow a b)
          (define s 1)
          (do_n_times b (lambda () (set! s (* s a))))
          s)

        (pow 3 5)
        """
      ))
    }

    test("power_rec") {
      assert(CESK.IntV(243) == apply(
        """
        (letrec
          ([pow (lambda (a b) (if (eq? b 0) 1 (* a (pow a (- b 1)))))])
          (pow 3 5))
        """
      ))
    }

    test("sum_list") {
      assert(CESK.IntV(328569) == apply(
        """
        (define (sum l) (if (eq? l '()) 0 (+ (car l) (sum (cdr l)))))
        (sum '(783 123 213 12 321 321321 3123 31 2321 321))
        """
      ))
    }

    test("hof_filter") {
      assert(CESK.ListV(List(CESK.IntV(13), CESK.IntV(21), CESK.IntV(11), CESK.IntV(10))) == apply(
        """
        (define nil '())
        (define (nil? l) (eq? l nil))
        (define (filter f l)
          (if (nil? l)
            nil
            (let
              ([h (car l)]
               [r (filter f (cdr l))])
                (if (f h) (cons h r) r))))
        (filter (lambda (x) (>= x 10)) '(9 13 21 5 2 7 11 10))
        """
      ))
    }

    test("hof_map") {
      assert(CESK.ListV(List(CESK.IntV(3), CESK.IntV(4), CESK.IntV(5))) == apply(
        """
        (define nil '())
        (define (nil? l) (eq? l nil))
        (define (map f l)
          (if (nil? l)
            nil
            (let
              ([h (f (car l))]
              [r (map f (cdr l))])
                (cons h r))))
        (map (lambda (x) (+ x 2)) '(1 2 3))
        """
      ))
    }

    test("hof_foldl") {
      assert(CESK.IntV(328569) == apply("""
        (define nil '())
        (define (nil? l) (eq? l nil))
        (define (foldl f z l)
          (if (nil? l)
            z
            (let ([z_ (f z (car l))]) (foldl f z_ (cdr l)))))

        (define (sum l) (foldl + 0 l))
        (sum '(783 123 213 12 321 321321 3123 31 2321 321))
        """
      ))
    }

    test("quasiquote") {
      assert(CESK.ListV(List(CESK.SymV("+"), CESK.IntV(1), CESK.IntV(2))) == apply("""
        (car '((+ 1 2) hello))
      """))

      // unquote
      assert(CESK.IntV(3) == apply("""
        (car '(,(+ 1 2) hello))
      """))

      // symbol
      assert(CESK.SymV("hello") == apply("""
        (car (cdr '(,(+ 1 2) hello)))
      """))
    }
  }
}
