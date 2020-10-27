(define pred
  (lambda (n)
    (lambda (rf)
      (lambda (rx)
        (((n (lambda (g) (lambda (h) (h (g rf)))))
          (lambda (ignored) rx))
         (lambda (id) id))))))

(define church0 (lambda (f0) (lambda (x0) x0)))
(define church1 (lambda (f1) (lambda (x1) (f1 x1))))
(define church0? (lambda (z) ((z (lambda (zx) #f)) #t)))

(define ff 
  (lambda (e)
  (if (church0? e)
    e
    (ff ((church1 pred) e)))))
(ff church1)
