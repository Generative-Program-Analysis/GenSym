
(letrec ((id (lambda (x) x))

         (blur (lambda (y) y))

         (lp (lambda (a n)
               (if (<= n 1)
                   (id a)
                   (let* ((r ((blur id) #t))
                          (s ((blur id) #f)))
                     (not ((blur lp) s (- n 1))))))))
  (lp #f 2))
