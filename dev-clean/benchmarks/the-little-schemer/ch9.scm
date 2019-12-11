(define sub1
  (lambda (n)
    (- n 1)))

(define pick
  (lambda (n lat)
    (cond ((zero? (sub1 n)) (car lat))
          (else (pick (sub1 n) (cdr lat))))))

(define looking
  (lambda (a lat)
    (keep-looking a (pick 1 lat) lat)))

(define keep-looking
  (lambda (a sorn lat)
    (cond ((number? sorn)
           (keep-looking a (pick sorn lat) lat))
          (else (eq? sorn a)))))

(define entity
  (lambda (x)
    (entity x)))

(define first
  (lambda (p)
    (car p)))

(define second
  (lambda (p)
    (car (cdr p))))

(define build
  (lambda (s1 s2)
    (cons s1 (cons s2 '()))))

;; the function shift takes a pair whose first component is
;; a pair and builds a pair by shifting the second pair of
;; the first component into the second component
(define shift
  (lambda (pair)
    (build (first (first pair))
           (build (second (first pair))
                  (second pair)))))

(define atom?
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

(define a-pair?
  (lambda (x)
    (cond ((atom? x) #f)
          ((null? x) #f)
          ((null? (cdr x)) #f)
          ((null? (cdr (cdr x))) #t)
          (else #f))))

(define align
  (lambda (pora)
    (cond ((atom? pora) pora)
          ((a-pair? (first pora))
           (align (shift pora)))
          (else (build (first pora)
                       (align (second pora)))))))
;; expand as follows
(align '((a (b c)) d))
(align (shift '((a (b c)) d)))
(align (build (first (first '((a (b c)) d)))
              (build (second (first '((a (b c)) d)))
                     (second '((a (b c)) d)))))
(align (build 'a '((b c) d)))
(align '(a ((b c) d)))
(build 'a (align '((b c) d)))
(build 'a (align (shift '((b c) d))))
(build 'a (align '(b (c d))))
(build 'a (build 'b (align '(c d))))
(build 'a (build 'b (build 'c (align 'd))))
(build 'a (build 'b (build 'c 'd)))

(define length*
  (lambda (pora)
    (cond ((atom? pora) 1)
          (else (+ (length* (first pora))
                   (length* (second pora)))))))

(define weight*
  (lambda (pora)
    (cond ((atom? pora) 1)
          (else (+ (* (weight* (first pora)) 2)
                   (weight* (second pora)))))))

(define revpair
  (lambda (pair)
    (build (second pair) (first pair))))

(define shuffle
  (lambda (pora)
    (cond ((atom? pora) pora)
          ((a-pair? (first pora))
           (shuffle (revpair pora)))
          (else (build (first pora)
                       (shuffle (second pora)))))))

(define one?
  (lambda (x)
    (eq? x 1)))

(define add1
  (lambda (x)
    (+ x 1)))

;; Lothar Collatz
(define C
  (lambda (n)
    (cond ((one? n) 1)
          (else 
           (cond ((even? n) (C (/ n 2)))
                 (else (C (add1 (* 3 n)))))))))

;; Wilhelm Ackermann
;; total
(define A
  (lambda (n m)
    (cond ((zero? n) (add1 m))
          ((zero? m) (A (sub1 n) 1))
          (else (A (sub1 n)
                   (A n (sub1 m)))))))

;; expand as follows
(A 2 2)
(A 1 (A 2 1))
(A 1 (A 1 (A 2 0)))
(A 1 (A 1 (A 1 1)))
(A 1 (A 1 (A 0 (A 1 0))))
(A 1 (A 1 (A 0 (A 0 1))))
(A 1 (A 1 (A 0 2)))
(A 1 (A 1 3))
(A 1 (A 0 (A 1 2)))
(A 1 (A 0 (A 0 (A 1 1))))
(A 1 (A 0 (A 0 (A 0 (A 1 0)))))
(A 1 (A 0 (A 0 (A 0 (A 0 1)))))
(A 1 (A 0 (A 0 (A 0 2))))
(A 1 (A 0 (A 0 3)))
(A 1 (A 0 4))
(A 1 5)
(A 0 (A 1 4))
(A 0 (A 0 (A 1 3)))
A 0 (A 0 (A 0 (A 1 2))))
(A 0 (A 0 (A 0 (A 0 (A 1 1)))))
(A 0 (A 0 (A 0 (A 0 (A 0 (A 1 0))))))
(A 0 (A 0 (A 0 (A 0 (A 0 (A 0 1))))))
(A 0 (A 0 (A 0 (A 0 (A 0 2)))))
(A 0 (A 0 (A 0 (A 0 3))))
(A 0 (A 0 (A 0 4)))
(A 0 (A 0 5))
(A 0 6)

(define eternity
  (lambda (x)
    (eternity x)))

(define last-try
  (lambda (x)
    (and (will-stop? last-try)
         (eternity x))))

(define mylength
  (lambda (l)
    (cond ((null? l) 0)
          (else (add1 (length (cdr l)))))))

;; length0
((lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l)))))))
 eternity)

;; length<1
((lambda (f)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (f (cdr l)))))))
 ((lambda (g)
    (lambda (l)
      (cond ((null? l) 0)
            (else (add1 (g (cdr l)))))))
  eternity))

;; length<2
((lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l)))))))
 ((lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l)))))))
  ((lambda (length)
     (lambda (l)
       (cond ((null? l) 0)
             (else (add1 (length (cdr l)))))))
   eternity)))

;; length0
((lambda (mk-length)
   (mk-length eternity))
 (lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l))))))))

;; length<1
((lambda (mk-length)
   (mk-length 
    (mk-length eternity)))
 (lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l))))))))

;; length<2
((lambda (mk-length)
   (mk-length
    (mk-length
     (mk-length eternity))))
 (lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l))))))))

;; length<3
((lambda (mk-length)
   (mk-length
    (mk-length
     (mk-length
      (mk-length eternity)))))
 (lambda (length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 (length (cdr l))))))))
 
;; length<1
((lambda (mk-length)
   (mk-length mk-length))
 (lambda (mk-length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 ((mk-length eternity)
                        (cdr l))))))))

;; an application
(((lambda (mk-length)
    (mk-length mk-length))
  (lambda (mk-length)
    (lambda (l)
      (cond ((null? l) 0)
            (else (add1 ((mk-length eternity)
                         (cdr l))))))))
 '(apple))

;; expand as follows
(((lambda (mk-length)
  (lambda (l)
    (cond ((null? l) 0)
          (else (add1 ((mk-length eternity)
                       (cdr l)))))))
 (lambda (mk-length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 ((mk-length eternity)
                        (cdr l))))))))
 '(apple))
((lambda (l)
    (cond ((null? l) 0)
          (else (add1 (((lambda (mk-length)
                          (lambda (l)
                            (cond ((null? l) 0)
                                  (else (add1 ((mk-length eternity)
                                               (cdr l))))))) eternity)
                       (cdr l))))))
 '(apple))
(add1 (((lambda (mk-length)
                          (lambda (l)
                            (cond ((null? l) 0)
                                  (else (add1 ((mk-length eternity)
                                               (cdr l))))))) eternity)
                       (cdr '(apple))))
(add1 ((lambda (l)
          (cond ((null? l) 0)
                (else (add1 ((eternity eternity)
                             (cdr l))))))
       (cdr '(apple))))
(add1 ((lambda (l)
          (cond ((null? l) 0)
                (else (add1 ((eternity eternity)
                             (cdr l))))))
       '()))

((lambda (mk-length)
   (mk-length mk-length))
 (lambda (mk-length)
   (lambda (l)
     (cond ((null? l) 0)
           (else (add1 ((lambda (x)
                          ((mk-length mk-length) x))
                        (cdr l))))))))

((lambda (mk-length)
   (mk-length mk-length))
 (lambda (mk-length)
   ((lambda (length)
      (lambda (l)
        (cond ((null? l) 0)
              (else (add1 (length (cdr l)))))))
    (lambda (x)
      ((mk-length mk-length) x)))))

;; Y combinator
;; Y (lambda f. < 真正的函数体,在内部用f指代自身 >)
(define Y
  (lambda (le)
    ((lambda (f) (f f))
     (lambda (f)
       (le (lambda (x) ((f f) x)))))))

;; http://docs.huihoo.com/homepage/shredderyin/wiki/SchemeYcombinator.html
(define Y
  (lambda (F)
    (let ((W (lambda (x)
               (F (lambda arg (apply (x x) arg))))))
      (W W))))

(define Fact
    (Y (lambda (f)
         (lambda (n)
           (cond ((eq? n 0) 1)
                 (else (* n (f (- n 1)))))))))

