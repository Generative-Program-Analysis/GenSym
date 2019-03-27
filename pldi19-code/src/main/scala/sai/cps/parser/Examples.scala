package sai.cps.parser

object Examples {
  def example1 = App(Lam(List("x", "k"),
                            App(Var("k"), List(Lam(List("a"), App(Var("halt"), List(Var("a"))))))),
                     List(Lit(3), Lam(List("z"), App(Var("halt"), List(Var("z"))))))

  def example2 = App(Lam(List("x"), App(Var("halt"), List(Var("x")))),
                     List(Lam(List("y"), App(Var("halt"), List(Var("y"))))))

  //TODO: double check ex3 and ex4
  val example3_str = """((lambda (f c1)
                              ((lambda (x c2)
                                       (f x c2))
                                 2
                                 c1))
                         (lambda (y c3) (+ y c3))
                         1))"""
  val example3_expr = App(Lam(List("f","c1"), App(Lam(List("x","c2"), App(Var("f"),
                                                                          List(Var("x"), Var("c2")))),
                                                  List(Lit(2), Var("c1")))),
                          List(Lam(List("y","c3"), App(Op("+"),
                                                       List(Var("y"), Var("c3")))), Lit(1)))

  val example4_str = """((lambda (apply k1)
                                 (apply (lambda (x1 k2) (+ x1 1 k2))
                                        (lambda (t2) (apply t2 (lambda (t3) (t3 2 k1))))))
                         (lambda (f k3) (k3 (lambda (x2 k4) (f x2 k4))))
                         (lambda (x) (halt x))))"""
  val example4_expr = App(Lam(List("apply","k1"),
                              App(Var("apply"),
                                  List(Lam(List("x1","k2"), App(Op("+"),
                                                                List(Var("x1"), Lit(1), Var("k2")))),
                                       Lam(List("t2"), App(Var("apply"),
                                                           List(Var("t2"), Lam(List("t3"),
                                                                               App(Var("t3"),
                                                                                   List(Lit(2), Var("k1")))))))))),
                          List(Lam(List("f","k3"),
                                   App(Var("k3"),
                                       List(Lam(List("x2","k4"), App(Var("f"),
                                                                     List(Var("x2"), Var("k4"))))))),
                               Lam(List("x"), App(Var("halt"),List(Var("x"))))))

  val example5_str = """((lambda (f1 k1)
      ((lambda (f2 k2)
         ((lambda (f3 k3)
            ((lambda (f4 k4)
               ((lambda (f5 k5)
                  ((lambda (f6 k6)
                     ((lambda (f7 k7)
                        ((lambda (f8 k8)
                           ((lambda (f9 k9)
                              ((lambda (f10 k10)
                                 ((lambda (f11 k11)
                                    ((lambda (f12 k12) (k12 f12))
                                     f11
                                     k11))
                                  f10
                                  k10))
                               f9
                               k9))
                            f8
                            k8))
                         f7
                         k7))
                      f6
                      k6))
                   f5
                   k5))
                f4
                k4))
             f3
             k3))
          f2
          k2))
       f1
       k1))
    (lambda (y) (halt y))
    (lambda (x) (halt x)))"""
  val example5_expr = SimpleCPSSchemeParser(example5_str).get
}
