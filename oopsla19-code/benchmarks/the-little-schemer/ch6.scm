(define atom?
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

(define numbered?
  (lambda (aexp) 
    (cond ((atom? aexp) (number? aexp))
          ((eq? (car (cdr aexp)) '+) 
           (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp))))))
          ((eq? (car (cdr aexp)) '*) 
           (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp))))))
          ((eq? (car (cdr aexp)) '^)
           (and (numbered? (car aexp)) (numbered? (car (cdr (cdr aexp)))))))))

;; Simplified numbered?
(define numbered?
  (lambda (aexp)
    (cond ((atom? aexp) (number? aexp))
          (else (and (numbered? (car aexp))
                     (numbered? (car (cdr (cdr aexp)))))))))

(define ^
  (lambda (n m)
    (cond ((zero? m) 1)
          (else (* n (^ n (- m 1)))))))

;; evaluate for infix expression
(define value
  (lambda (nexp)
    (cond ((atom? nexp) nexp)
          ((eq? (car (cdr nexp)) '+)
           (+ (value (car nexp)) (value (car (cdr (cdr nexp))))))
          ((eq? (car (cdr nexp)) '*)
           (* (value (car nexp)) (value (car (cdr (cdr nexp))))))
          (else
           (^ (value (car nexp)) (value (car (cdr (cdr nexp)))))))))
(value '((5 ^ 1) * (3 + 3)))

(define 1st-sub-exp
  (lambda (aexp)
    (car (cdr aexp))))
(define 2nd-sub-exp
  (lambda (aexp)
    (car (cdr (cdr aexp)))))
(define operator
  (lambda (aexp)
    (car aexp)))

;; evaluate for prefix expression
(define value
  (lambda (nexp)
    (cond ((atom? nexp) nexp)
          ((eq? (operator nexp) '+)
           (+ (value (1st-sub-exp nexp)) (value (2nd-sub-exp nexp))))
          ((eq? (operator nexp) '*)
           (* (value (1st-sub-exp nexp)) (value (2nd-sub-exp nexp))))
          ((eq? (operator nexp) '^)
           (^ (value (1st-sub-exp nexp)) (value (2nd-sub-exp nexp)))))))

;; another way to represent natural number
;; () 0
;; (()) 1
;; (() ()) 2

(define sero?
  (lambda (n) (null? n)))
(define edd1
  (lambda (n) (cons '() n)))
(define zub1
  (lambda (n) (cdr n)))
(define o<
  (lambda (n m)
    (cond ((sero? m) #f)
          ((sero? n) #t)
          (else (o< (zub1 n) (zub1 m))))))
(define o>
  (lambda (n m)
    (cond ((sero? n) #f)
          ((sero? m) #t)
          (else (o> (zub1 n) (zub1 m))))))
(define o=
  (lambda (n m)
    (cond ((o> n m) #f)
          ((o< n m) #f)
          (else #t))))
(define o+
  (lambda (n m)
    (cond ((sero? m) n)
          (else (edd1 (o+ n (zub1 m)))))))
(define o-
  (lambda (n m)
    (cond ((sero? m) n)
          (else (zub1 (o- n (zub1 m)))))))
(define o*
  (lambda (n m)
    (cond ((sero? m) '())
          (else (o+ n (o* n (zub1 m)))))))
(define o/
  (lambda (n m)
    (cond ((o< n m) '())
          (else (edd1 (o/ (o- n m) m))))))

(define lat?
  (lambda (l)
    (cond ((null? l) #t)
          ((atom? (car l)) (lat? (cdr l)))
          (else #f))))
