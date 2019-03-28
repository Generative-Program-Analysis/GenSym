(define atom?
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

(define add1
  (lambda (x)
    (+ x 1)))

(define rember?
  (lambda (a l)
    (cond ((null? l) '())
          ((atom? (car l))
           (cond ((eq? (car l) a) (rember* a (cdr l)))
                 (else (cons (car l) (rember* a (cdr l))))))
          (else (cons (rember* a (car l)) (rember* a (cdr l)))))))

(define insertR*
  (lambda (new old l)
    (cond ((null? l) '())
          ((atom? (car l))
           (cond ((eq? (car l) old) 
                  (cons (car l) 
                        (cons new (insertR* new old (cdr l)))))
                 (else (cons (car l) (insertR* new old (cdr l))))))
          (else (cons (insertR* new old (car l)) 
                      (insertR* new old (cdr l)))))))

(define occur*
  (lambda (a l)
    (cond ((null? l) 0)
          ((atom? (car l))
           (cond ((eq? (car l) a)
                  (add1 (occur* a (cdr l))))
                 (else (occur* a (cdr l)))))
          (else (+ (occur* a (car l))
                   (occur* a (cdr l)))))))

(define subst*
  (lambda (new old l)
    (cond ((null? l) '())
          ((atom? (car l))
           (cond ((eq? (car l) old)
                  (cons new (subst* new old (cdr l))))
                 (else (subst* new old (cdr l)))))
          (else (cons (subst* new old (car l)) (subst* new old (cdr l)))))))

(define insertL*
  (lambda (new old l)
    (cond ((null? l) '())
          ((atom? (car l))
           (cond ((eq? (car l) old)
                  (cons new (cons old (insertL* new old (cdr l)))))
                 (else (cons (car l) (insertL* new old (cdr l))))))
          (else (cons (insertL* new old (car l)) (insertL* new old (cdr l)))))))

(define member*
  (lambda (a l)
    (cond ((null? l) #f)
          ((atom? (car l))
           (cond ((eq? (car l) a) #t)
                 (else (member* a (cdr l)))))
          (else (or (member* a (car l)) (member* a (cdr l)))))))

(define leftmost 
  (lambda (l)
    (cond ((atom? (car l)) (car l))
          (else (leftmost (car l))))))

(define eqan?
  (lambda (a1 a2)
    (cond ((and (number? a1) (number? a2)) (= a1 a2))
          ((or (number? a1) (number? a2)) #f)
          (else (eq? a1 a2)))))

(define eqlist?
  (lambda (l1 l2)
    (cond
      ((and (null? l1) (null? l2)) #t)
      ((and (null? l1) (atom? (car l2))) #f)
      ((null? l1) #f)
      ((and (atom? (car l1)) (null? l2)) #f)
      ((and (atom? (car l1)) (atom? (car l2)))
       (and (eqan? (car l1) (car l2))
            (eqlist? (cdr l1) (cdr l2))))
      ((atom? (car l1)) #f)
      ((null? l2) #f)
      ((atom? (car l2)) #f)
      (else (and (eqlist? (car l1) (car l2))
                 (eqlist? (cdr l1) (cdr l2)))))))

;; Rewrite eqlist?
(define eqlist?
  (lambda (l1 l2)
    (cond
      ((and (null? l1) (null? l2)) #t)
      ((or (null? l1) (null? l2)) #f)
      ((and (atom? (car l1)) (atom? (car l2)))
       (and (eqan? (car l1) (car l2))
            (eqlist? (cdr l1) (cdr l2))))
      ((or (atom? (car l1)) (atom? (car l2))) #f)
      (else
       (and (eqlist? (car l1) (car l2))
            (eqlist? (cdr l2) (cdr l2)))))))

(define my-equal?
  (lambda (s1 s2)
    (cond
      ((and (atom? s1) (atom? s2)) (eqan? s1 s2))
      ((or (atom? s1) (atom? s2)) #f)
      (else (eqlist? s1 s2)))))

(define eqlist?
  (lambda (l1 l2)
    (cond 
      ((and (null? l1) (null? l2)) #t)
      ((or (null? l1) (null? l2)) #f)
      (else (and (my-equal? (car l1) (car l2))
                 (eqlist? (cdr l1) (cdr l2)))))))

(define rember
  (lambda (s l)
    (cond ((null? l) '())
          ((my-equal? (car l) s) (cdr l))
          (else (cons (car l) (rember s (cdr l)))))))

