(define member?
  (lambda (a lat)
    (cond 
      ((null? lat) #f)
      (else (or (eq? (car lat) a)
                (member? a (cdr lat)))))))

(define set?
  (lambda (lat)
    (cond ((null? lat) #t)
          ((member? (car lat) (cdr lat)) #f)
          (else (set? (cdr lat))))))

(define makeset
  (lambda (lat)
    (cond ((null? lat) '())
          ((member? (car lat) (cdr lat))
           (makeset (cdr lat)))
          (else (cons (car lat) (makeset (cdr lat)))))))

;; Multi remove member
(define multirember
  (lambda (a lat)
    (cond ((null? lat) (quote ()))
          ((eq? (car lat) a) (multirember a (cdr lat)))
          (else (cons (car lat) (multirember a (cdr lat)))))))

(define makeset
  (lambda (lat)
    (cond ((null? lat) '())
          (else 
           (cons (car lat) 
                 (makeset (multirember (car lat) (cdr lat))))))))

(define subset?
  (lambda (set1 set2)
    (cond ((null? set1) #t)
          ((member? (car set1) set2)
           (subset? (cdr set1) set2))
          (else #f))))

(define subset?
  (lambda (set1 set2)
    (cond ((null? set1) #t)
          ((and (member? (car set1) set2)
               (subset? (cdr set1) set2))))))

(define eqset?
  (lambda (set1 set2)
    (and (subset? set1 set2) (subset? set2 set1))))

(define intersect?
  (lambda (set1 set2)
    (cond ((null? set1) #f)
          ((member? (car set1) set2) #t)
          (else (intersect? (cdr set1) set2)))))

(define intersect?
  (lambda (set1 set2)
    (cond ((null? set1) #f)
          (else (or (member? (car set1) set2)
                    (intersect? (cdr set1) set2))))))

(define intersect
  (lambda (set1 set2)
    (cond ((null? set1) '())
          ((member? (car set1) set2)
           (cons (car set1) (intersect (cdr set1) set2)))
          (else (intersect (cdr set1) set2)))))

(define union
  (lambda (set1 set2)
    (cond ((null? set1) set2)
          ((member? (car set1) set2)
           (union (cdr set1) set2))
          (else (cons (car set1) (union (cdr set1) set2))))))

;; atoms in set1 that are not in set2
(define xxx
  (lambda (set1 set2)
    (cond ((null? set1) '())
          ((member? (car set1) set2)
           (xxx (cdr set1) set2))
          (else (cons (car set1) (xxx (cdr set1) set2))))))

(define intersectall
  (lambda (l-set)
    (cond ((null? (cdr l-set)) (car l-set))
          (else (intersect (car l-set)
                           (intersectall (cdr l-set)))))))

(define a-pair?
  (lambda (x)
    (cond ((atom? x) #f)
          ((null? x) #f)
          ((null? (cdr x)) #f)
          ((null? (cdr (cdr x))) #t)
          (else #f))))

(define first
  (lambda (p) (car p)))

(define second 
  (lambda (p) (car (cdr p))))

(define build
  (lambda (s1 s2)
    (cons s1 (cons s2 '()))))

(define third
  (lambda (l)
    (car (cdr (cdr l)))))

(define firsts
  (lambda (l)
    (cond ((null? l) '())
          (cons (first (car l)) (firsts (cdr l))))))

(define seconds
  (lambda (l)
    (cond ((null? l) '())
          (cons (second (car l)) (seconds (cdr l))))))

;; rel stand for relation
;; fun stand for function

(define fun?
  (lambda (rel)
    (set? (firsts rel))))

(define revrel
  (lambda (rel)
    (cond ((null? rel) '())
          (else (cons (build (second (car rel))
                             (first (car rel)))
                      (revrel (cdr rel)))))))

(define revpair
  (lambda (pair)
    (build (second pair) (first pair))))

(define revrel
  (lambda (rel)
    (cond ((null? rel) '())
          (else (cons (revpair (car rel))
                      (revrel (cdr rel)))))))

(define fullfun?
  (lambda (fun)
    (set? (seconds fun))))

(define one-to-one?
  (lambda (fun)
    (fun? (revrel fun))))