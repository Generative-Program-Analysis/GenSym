package sai.evaluation

import scala.io.Source
import sai.evaluation.parser._

object TestPrograms {
  def getAST(prog: String) = {
    SchemeParser(prog) match {
      case Some(expr) => 
        ASTUtils.alpha(SchemeASTDesugar(expr), Map())._1
    }
  }

  def sat = getAST(Source.fromFile("benchmarks/sat.scm").mkString)

  def kcfa3 = getAST(Source.fromFile("benchmarks/kcfa3.scm").mkString)

  def kcfa16 = getAST(Source.fromFile("benchmarks/kcfa-worst-case-16.scm").mkString)

  def kcfa32 = getAST(Source.fromFile("benchmarks/kcfa-worst-case-32.scm").mkString)

  def kcfa64 = getAST(Source.fromFile("benchmarks/kcfa-worst-case-64.scm").mkString)

  def kcfa256 = getAST(Source.fromFile("benchmarks/kcfa-worst-case-256.scm").mkString)

  def fermat = getAST(Source.fromFile("benchmarks/fermat.scm").mkString)

  def rsa = getAST(Source.fromFile("benchmarks/rsa.scm").mkString)

  def blur = getAST(Source.fromFile("benchmarks/blur.scm").mkString)

  def fib = getAST("""
    (define
      (fib n)
        (if (eq? n 0) 1
          (if (eq? n 1) 1
            (+ (fib (- n 1)) (fib(- n 2))))))
    (fib 10)
  """)

  def id4 = App(Lam(List("x"), App(App(Var("x"), List(Var("x"))), List(Var("x")))), List(Lam(List("y"), Var("y"))))
  def oneplusone = App(Var("+"), List(IntLit(1), IntLit(1)))
  def fact5 = getAST("(define (fact n) (if (eq? n 0) 1 (* n (fact (- n 1))))) (fact 5)")
  def euclid = getAST(
    """
    (letrec
      ([gcd
        (lambda (a b)
          (if (eq? b 0)
            a
            (gcd b (% a b))))])
      (gcd 24 56))
    """)
  def euclid_imp = getAST(
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

  def letloop = getAST(
    """
    (let loop ((z z0) 
               (c 0)) 
        (if (= c max-count)
            c
            1))
    """
  )

  def omega = getAST(Source.fromFile("benchmarks/omega.scm").mkString)

  // church encoding
  def church = getAST(Source.fromFile("benchmarks/church.sch").mkString)

  // church encoding
  def church_exp = getAST(Source.fromFile("benchmarks/church_exp.sch").mkString)

  // earley make-parser -- from oaam
  def earley = getAST(Source.fromFile("benchmarks/earley.sch").mkString)

  // mbrotZ -- may need primitive complex numbers
  def mbrotZ = getAST(Source.fromFile("benchmarks/mbrotZ.sch").mkString)

  // BOYER -- Logic programming benchmark, originally written by Bob Boyer.
  def boyer = getAST(Source.fromFile("benchmarks/toplas98/boyer.sch").mkString)

  // Dynamic -- Fritz's dynamic type inferencer, set up to run on itself
  def dynamic = getAST(Source.fromFile("benchmarks/toplas98/dynamic.sch").mkString)

  // graphs
  def graphs = getAST(Source.fromFile("benchmarks/toplas98/graphs.sch").mkString)

  // handle -- requires macro expansion
  def handle = getAST(Source.fromFile("benchmarks/toplas98/handle.scm").mkString)

  // lattice
  def lattice = getAST(Source.fromFile("benchmarks/toplas98/lattice.scm").mkString)

  // matrix
  def matrix = getAST(Source.fromFile("benchmarks/toplas98/matrix.scm").mkString)

  // maze -- call/cc
  def maze = getAST(Source.fromFile("benchmarks/toplas98/maze.sch").mkString)

  // nbody
  def nbody = getAST(Source.fromFile("benchmarks/toplas98/nbody.sch").mkString)

  // nucleic
  def nucleic = getAST(Source.fromFile("benchmarks/toplas98/nucleic.sch").mkString)

  // nucleic2 -- define syntax
  def nucleic2 = getAST(Source.fromFile("benchmarks/toplas98/nucleic2.sch").mkString)

  // splay -- old match
  def splay = getAST(Source.fromFile("benchmarks/toplas98/splay.scm").mkString)

}
