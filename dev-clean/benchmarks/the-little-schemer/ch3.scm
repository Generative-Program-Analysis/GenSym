;; remove member
(define rember
  (lambda (a lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) a) (cdr lat))
          (else (cons (car lat) (rember a (cdr lat)))))))
(rember 'a '(b c a d))

(define first
  (lambda (l)
    (cond ((null? l) (quote ()))
          (else (cons (car (car l)) (first (cdr l)))))))

(first '((a b) (c d) (e f)))

;; insert element at right of old
(define insertR
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old) (cons old (cons new (cdr lat))))
          (else (cons (car lat) (insertR new old (cdr lat)))))))
(insertR 'z 'b '(a b c d))

(define insertL
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old) (cons new (cons old (cdr lat))))
          (else (cons (car lat) (insertL new old (cdr lat)))))))

(define insertL
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old) (cons new lat))
          (else (cons (car lat) (insertL new old (cdr lat)))))))
(insertL 'z 'b '(a b c d))

(define subst
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old) (cons new (cdr lat)))
          (else (cons (car lat) (subst new old (cdr lat)))))))
(subst 'z 'b '(a b c d))

(define subst2
  (lambda (new o1 o2 lat)
    (cond ((null? lat) (quote ()))
          ((or (eq? (car lat) o1)
               (eq? (car lat) o2))
           (cons new (cdr lat)))
          (else (cons (car lat) (subst2 new o1 o2 (cdr lat)))))))
(subst2 'z 'c 'b '(a b c d))
(subst2 'z 'c 'b '(a c b d))

(define multirember
  (lambda (a lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) a) (multirember a (cdr lat)))
          (else (cons (car lat) (multirember a (cdr lat)))))))
(multirember 'a '(x y a z a))

(define multiinsertR
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old)
           (cons old (cons new (multiinsertR new old (cdr lat)))))
          (else (cons (car lat) (multiinsertR new old (cdr lat)))))))
(multiinsertR 'a 'b '(x b y b z))

(define multiinsertL
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old)
           (cons new (cons old (multiinsertL new old (cdr lat)))))
          (else (cons (car lat) (multiinsertL new old (cdr lat)))))))
(multiinsertL 'a 'b '(x b y b z))

(define multisubst
  (lambda (new old lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) old)
           (cons new (multisubst new old (cdr lat))))
          (else (cons (car lat) (multisubst new old (cdr lat)))))))
(multisubst 'a 'b '(x b y b z))
