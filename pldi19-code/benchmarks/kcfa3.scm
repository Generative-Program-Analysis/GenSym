;; Another working example (bigger)
;; 1749 states, 3846 edges for 1CFA
;; Normalized by pretty-print
((lambda (f1) (let ((a (f1 #t))) (f1 #f)))
 (lambda (x1)
   ((lambda (f2) (let ((b (f2 #t))) (f2 #f)))
    (lambda (x2)
      ((lambda (f3) (let ((c (f3 #t))) (f3 #f)))
       (lambda (x3) ((lambda (z) (z x1 x2 x3)) (lambda (y1 y2 y3) y1))))))))
