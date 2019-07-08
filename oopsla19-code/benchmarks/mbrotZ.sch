;;; MBROT -- Generation of Mandelbrot set fractal
;;; using Scheme's complex numbers.

(define (count z0 step z)

  (let* ((max-count 64)
         (radius    4.0)
         (radius^2  (fl* radius radius)))

    (let ((z0 (+ z0 (* z step))))
      
      (let loop1 ((z z0)
                 (c 0))
        (if (= c max-count)
            c
            (let* ((zr (real-part z))
                   (zi (imag-part z))
                   (zr^2 (fl* zr zr))
                   (zi^2 (fl* zi zi)))
              (if (fl> (fl+ zr^2 zi^2) radius^2)
                  c
                  (loop1 (+ (* z z) z0) (+ c 1)))))))))

(define (mbrot matrix z0 step n)
  (let loop4 ((y (- n 1)))
    (if (>= y 0)
        (let loop5 ((x (- n 1)))
          (if (>= x 0)
              (begin
               (vector-set! (vector-ref matrix x)
                            y
                            (count z0
                                   step
                                   (make-rectangular (->fl x)
                                                     (->fl y))))
               (loop5 (- x 1)))
              (loop4 (- y 1)))) 
    (void))))

(define (test n)
  (let ((matrix (make-vector n)))
    (let loop2 ((i (- n 1)))
      (if (>= i 0)
        (begin
          (vector-set! matrix i (make-vector n))
          (loop2 (- i 1)))  (void)))
    (mbrot matrix -1.0-0.5i 0.005 n)
    (vector-ref (vector-ref matrix 0) 0)))

(define (main)
  (let* ((count-n (read))
         (input1 (read))
         (output (read))
         (s2 (number->string count-n))
         (s1 (number->string input1))
         (name "mbrot"))
    (let ([ok?
           (lambda (result) (= result output))]
          [thunk (lambda () (test input1))])
      (let loop3 ([i 0]
                 [result (void)])
        (cond [(< i count-n)
               (loop3 (+ i 1) (thunk))]
              [(ok? result) result]
              [else
               (display "ERROR: returned incorrect result: ")
               (write result)
               (newline)
               result])))))

(main)
