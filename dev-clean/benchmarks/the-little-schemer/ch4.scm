(define atom?
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

(define add1
  (lambda (x)
    (+ x 1)))

(define sub1
  (lambda (x)
    (- x 1)))

(define o+
  (lambda (n m)
    (cond
      ((zero? m) n)
      (else (add1 (o+ n (sub1 m)))))))
(o+ 3 2)
;; expand as follows
(add1 (o+ 3 1))
(add1 (add1 (o+ 3 0)))
(add1 (add1 3))

(define o-
  (lambda (n m)
    (cond
      ((zero? m) n)
      (else (sub1 (o- n (sub1 m)))))))

;; sum all the number in the tup(list)
(define addtup
  (lambda (tup)
    (cond ((null? tup) 0)
          (else (o+ (car tup) (addtup (cdr tup)))))))

;; expand as follows
(addtup '(1 2 3))
(o+ 1 (addtup '(2 3)))
(o+ 1 (o+ 2 (addtup '(3))))
(o+ 1 (o+ 2 (o+ 3 (addtup '()))))

(define o*
  (lambda (n m)
    (cond ((zero? m) 0)
          (else (o+ n (o* n (sub1 m)))))))

;; expand as follows
(o* 5 3)
(o+ 5 (o* 5 (sub1 3)))
(o+ 5 (o* 5 2))
(o+ 5 (o+ 5 (o* 5 (sub1 2))))
(o+ 5 (o+ 5 (o* 5 1)))
(o+ 5 (o+ 5 (o+ 5 (o* 5 (sub1 1)))))
(o+ 5 (o+ 5 (o+ 5 0)))

(define tup+
  (lambda (tup1 tup2)
    (cond ((null? tup1) tup2)
          ((null? tup2) tup1)
          (else (cons (+ (car tup1) (car tup2)) 
                      (tup+ (cdr tup1) (cdr tup2)))))))

(tup+ '(3 7) '(4 6)) ;; (7 13)

(define o>
  (lambda (n m)
    (cond ((zero? n) #f)
          ((zero? m) #t)
          (else (o> (sub1 n) (sub1 m))))))

(define o<
  (lambda (n m)
    (cond ((zero? m) #f)
          ((zero? n) #t)
          (else (o< (sub1 n) (sub1 m))))))

(define o=
  (lambda (n m)
    (cond ((o> n m) #f)
          ((o< n m) #t)
          (else #t))))

(define ^
  (lambda (n m)
    (cond ((zero? m) 1)
          (else (o* n (^ n (sub1 m)))))))

(^ 2 3)
(o* 2 (^ 2 (sub1 3)))
(o* 2 (^ 2 2))
(o* 2 (o* 2 (^ 2 (sub1 2))))
(o* 2 (o* 2 (^ 2 1)))
(o* 2 (o* 2 (o* 2 (^ 2 (sub1 1)))))
(o* 2 (o* 2 (o* 2 (^ 2 0))))
(o* 2 (o* 2 (o* 2 1)))

;; Count how many times the second arguments fits into the first one
(define ???
  (lambda (n m)
    (cond ((o< n m) 0)
          (else (add1 (??? (o- n m) m))))))

;; Divison
(define o/
  (lambda (n m)
    (cond ((o< n m) 0)
          (else (add1 (o/ (o- n m) m))))))

;; Expand as follows
(o/ 7 2)
(add1 (o/ (o- 7 2) 2))
(add1 (o/ 5 2))
(add1 (add1 (o/ (o- 5 2) 2)))
(add1 (add1 (o/ 3 2)))
(add1 (add1 (add1 (o/ (o- 3 2) 2))))
(add1 (add1 (add1 (o/ 1 2))))
(add1 (add1 (add1 0)))

(define my-length
  (lambda (lat)
    (cond ((null? lat) 0)
          (else (add1 (length (cdr lat)))))))

(define pick
  (lambda (n lat)
    (cond ((zero? (sub1 n)) (car lat))
          (else (pick (sub1 n) (cdr lat))))))

;; Remove pick
(define rempick
  (lambda (n lat)
    (cond ((zero? (sub1 n)) (cdr lat))
          (else (cons (car lat) (rempick (sub1 n) (cdr lat)))))))

(define no-nums
  (lambda (lat)
    (cond ((null? lat) '())
          ((number? (car lat)) (no-nums (cdr lat)))
          (else (cons (car lat) (no-nums (cdr lat)))))))

(define all-nums
  (lambda (lat)
    (cond ((null? lat) '())
          ((number? (car lat)) (cons (car lat) (all-nums (cdr lat))))
          (else (all-nums (cdr lat))))))

(define eqan?
  (lambda (a1 a2)
    (cond ((and (number? a1) (number? a2)) (= a1 a2))
          ((or (number? a1) (number? a2)) #f)
          (else (eq? a1 a2)))))

(define occur
  (lambda (a lat)
    (cond ((null? lat) 0)
          ((eqan? a (car lat)) (add1 (occur a (cdr lat))))
          (else (occur a (cdr lat))))))

(define one?
  (lambda (n)
    (cond ((zero? n) #f)
          (else (zero? (sub1 n))))))

(define one?
  (lambda (n)
    (= n 1)))

(define rempick
  (lambda (n lat)
    (cond ((one? n) (cdr lat))
          (else (cons (car lat) (rempick (sub1 n) (cdr lat)))))))
