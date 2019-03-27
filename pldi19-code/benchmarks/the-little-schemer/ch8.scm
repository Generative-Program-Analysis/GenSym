(define atom?
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

;; Note
;; = is for number
;; eq? is for atom and number
;; equal? is list, atom and number

(define rember-f
  (lambda (test? a l)
    (cond ((null? l) '())
          ((test? a (car l)) (cdr l))
          (else (cons (car l) (rember-f test? a (cdr l)))))))

(define eq?-c
  (lambda (a)
    (lambda (x)
      (eq? x a))))

(define eq?-salad (eq?-c 'salad))

(define rember-f
  (lambda (test?)
    (lambda (a l)
      (cond ((null? l) '())
            ((test? (car l) a) (cdr l))
            (else (cons (car l) ((rember-f test?) a (cdr l))))))))

(define rember-eq? (rember-f eq?))
(define rember-equal? (rember-f equal?))

(define insertL-f
  (lambda (test?)
    (lambda (new old l)
      (cond ((null? l) '())
            ((test? (car l) old)
             (cons new (cons old (cdr l))))
            (else (cons (car l) ((insertL-f test?) new old (cdr l))))))))

(define insertR-f
  (lambda (test?)
    (lambda (new old l)
      (cond ((null? l) '())
            ((test? (car l) old)
             (cons old (cons new (cdr l))))
            (else (cons (car l) ((insertR-f test?) new old (cdr l))))))))

;; My implementation of insert-g
(define insert-left
  (lambda (new old l)
    (cons new (cons old l))))
(define insert-right
  (lambda (new old l)
    (cons old (cons new l))))

(define insert-g
  (lambda (test?)
    (lambda (insert)
      (lambda (new old l)
        (cond ((null? l) '())
              ((test? (car l) old)
               (insert new old (cdr l)))
              (else (cons (car l)
                          (((insert-g test?) insert) new old (cdr l)))))))))

(((insert-g equal?) insert-left) 'a 'b '(c d e b))
(((insert-g equal?) insert-right) 'a 'b '(c d e b))
;; My implementation of insert-g end

(define seqL
  (lambda (new old l)
    (cons new (cons old l))))

(define seqR
  (lambda (new old l)
    (cons old (cons new l))))

(define insert-g
  (lambda (seq)
    (lambda (new old l)
      (cond ((null? l) '())
            ((eq? (car l) old)
             (seq new old (cdr l)))
            (else (cons (car l) ((insert-g seq) new old (cdr l))))))))

(define insertL (insert-g seqL))
(define insertR (insert-g seqR))

(define insertL (insert-g 
                 (lambda (new old l)
                   (cons new (cons old l)))))

(define seqS
  (lambda (new old l)
    (cons new l)))

(define subst (insert-g seqS))

(define seqrem
  (lambda (new old l) l))

(define yyy
  (lambda (a l)
    ((insert-g seqrem) #f a l)))

;; value in ch6, evaluate prefix expression
(define ^
  (lambda (n m)
    (expt n m)))
(define operator 
  (lambda (aexp)
    (car aexp)))
(define 1st-sub-exp
  (lambda (aexp)
    (car (cdr aexp))))
(define 2nd-sub-exp
  (lambda (aexp)
    (car (cdr (cdr aexp)))))

(define value
  (lambda (nexp)
    (cond ((atom? nexp) nexp)
          ((eq? (operator nexp) '+)
           (+ (value (1st-sub-exp nexp))
              (value (2nd-sub-exp nexp))))
          ((eq? (operator nexp) '*)
           (* (value (1st-sub-exp nexp))
              (value (2nd-sub-exp nexp))))
          (else 
           (^ (value (1st-sub-exp nexp))
              (value (2nd-sub-exp nexp)))))))

(define atom-to-function
  (lambda (x)
    (cond ((eq? x '+) +)
          ((eq? x '*) *)
          (else ^))))

(define value
  (lambda (nexp)
    (cond ((atom? nexp) nexp)
          (else ((atom-to-function (operator nexp))
                 (value (1st-sub-exp nexp))
                 (value (2nd-sub-exp nexp)))))))

(define multirember-f
  (lambda (test?)
    (lambda (a lat)
      (cond ((null? lat) '())
            ((test? (car lat) a)
             ((multirember-f test?) a (cdr lat)))
            (else (cons (car lat)
                        ((multirember-f test?) a (cdr lat))))))))

(define multirember-eq?
  (multirember-f eq?))

(define multiremberT
  (lambda (test? lat)
    (cond ((null? lat) '())
          ((test? (car lat))
           (multiremberT test? (cdr lat)))
          (else (cons (car lat)
                      (multiremberT test? (cdr lat)))))))

(multiremberT (lambda (x) (eq? x 'a)) '(b c d a x a))

;; multirember&co looks at every atom of the lat to see whether it is
;; eq? to a. Those atoms that are not are collected in one
;; list ls1; the others for which the answer is true are collected
;; in a second list ls2. Finally, it determines the value os
;; (f ls1 ls2)

;; col is collector, sometimes called a continuation

(define multirember&co
  (lambda (a lat col)
    (cond ((null? lat)
           (col '() '()))
          ((eq? (car lat) a)
           (multirember&co a (cdr lat)
                           (lambda (newlat seen)
                             (col newlat (cons (car lat) seen)))))
          (else
           (multirember&co a (cdr lat)
                           (lambda (newlat seen)
                             (col (cons (car lat) newlat) seen)))))))

(define a-friend
  (lambda (x y)
    (null? y)))

;; expand as follows
(multirember&co 'a '(a) a-friend)
(multirember&co 'a '() (lambda (newlat seen) (a-friend newlat (cons (car '(a)) seen))))
((lambda (newlat seen) (a-friend newlat (cons (car '(a)) seen))) '() '())

;; expand as follows
(multirember&co 'a '(b a) a-friend)
(multirember&co 'a '(a) (lambda (newlat seen) (a-friend (cons (car '(b a)) newlat) seen)))
(multirember&co 'a '() (lambda (newlat seen) 
                         ((lambda (newlat seen) (a-friend (cons (car '(b a)) newlat) seen))
                          newlat
                          (cons (car '(a)) seen))))
((lambda (newlat seen) 
   ((lambda (newlat seen) (a-friend (cons (car '(b a)) newlat) seen))
    newlat
    (cons (car '(a)) seen)))
 '() '())

;; numbers except a
(multirember&co 'a '(a b c d a b a) (lambda (x y) (length x)))

;; display x
(multirember&co 'a '(a b c d a b a) (lambda (x y)
                                      (display (cons "x:" x))))

;; display y
(multirember&co 'a '(a b c d a b a) (lambda (x y)
                                      (display (cons "y:" y))))

(define multiinsertL
  (lambda (new old lat)
    (cond ((null? lat) '())
          ((eq? old (car lat))
           (cons new (cons old (multiinsertL new old (cdr lat)))))
          (else (cons (car lat) (multiinsertL new old (car lat)))))))

(define multiinsertR
  (lambda (new old lat)
    (cond ((null? lat) '())
          ((eq? old (car lat))
           (cons old (cons new (multiinsertR new old (cdr lat)))))
          (else (cons (car lat) (multiinsertR new old (cdr lat)))))))

(define multiinsertLR
  (lambda (new oldL oldR lat)
    (cond ((null? lat) '())
          ((eq? (car lat) oldL)
           (cons new (cons oldL (multiinsertLR new oldL oldR (cdr lat)))))
          ((eq? (car lat) oldR)
           (cons oldR (cons new (multiinsertLR new oldL oldR (cdr lat)))))
          (else (cons (car lat) (multiinsertLR new oldL oldR (cdr lat)))))))

(define add1
  (lambda (x) (+ x 1)))

(define multiinsertLR&co
  (lambda (new oldL oldR lat col)
    (cond ((null? lat)
           (col '() 0 0))
          ((eq? (car lat) oldL)
           (multiinsertLR&co new oldL oldR (cdr lat)
                             (lambda (newlat L R) (col (cons new (cons oldL newlat)) (add1 L) R))))
          ((eq? (car lat) oldR)
           (multiinsertLR&co new oldL oldR (cdr lat)
                             (lambda (newlat L R) (col (cons oldR (cons new newlat)) L (add1 R)))))
          (else 
           (multiinsertLR&co new oldL oldR (cdr lat)
                             (lambda (newlat L R) (col (cons (car lat) newlat) L R)))))))

(multiinsertLR&co 'salty 'fish 'chips '(chips and fish or fish and chips) 
                  (lambda (newlat Lcount Rcount) newlat))

(define sub1
  (lambda (n)
    (- n 1)))

(define o+
  (lambda (n m)
    (cond ((zero? m) n)
          (else (add1 (o+ n (sub1 m)))))))

(define o-
  (lambda (n m)
    (cond ((zero? m) n)
          (else (sub1 (o- n (sub1 m)))))))

(define o*
  (lambda (n m)
    (cond ((zero? m) 0)
          (else (o+ n (o* n (sub1 m)))))))

(define o/
  (lambda (n m)
    (cond ((< n m) 0)
          (else (add1 (o/ (o- n m) m))))))

(define my-even?
  (lambda (n)
    (= (o* (o/ n 2) 2) n)))

(define evens-only*
  (lambda (l)
    (cond ((null? l) '())
          ((atom? (car l))
                  (cond ((even? (car l))
                         (cons (car l) (evens-only* (cdr l))))
                        (else (evens-only* (cdr l)))))
          (else (cons (evens-only* (car l)) (evens-only* (cdr l)))))))

(define evens-only*&co
  (lambda (l col)
    (cond ((null? l)
           (col '() 1 0))
          ((atom? (car l))
           (cond ((even? (car l))
                  (evens-only*&co (cdr l)
                                  (lambda (newl p s)
                                    (col (cons (car l) newl) (* (car l) p) s))))
                 (else (evens-only*&co (cdr l)
                                       (lambda (newl p s)
                                         (col newl p (+ (car l) s)))))))
          (else (evens-only*&co (car l)
                                (lambda (al ap as) 
                                  (evens-only*&co (cdr l)
                                                  (lambda (dl dp ds)
                                                    (col (cons al dl)
                                                         (* ap dp)
                                                         (+ as ds))))))))))

(evens-only*&co '((9 1 2 8) 3 10 ((9 9) 7 6) 2) (lambda (newl product sum)
                                                  (cons sum (cons product newl))))
