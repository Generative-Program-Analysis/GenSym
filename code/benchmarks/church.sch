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