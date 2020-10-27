; Given a comparison routine that returns one of
;	less
;	more
;	equal
;	uncomparable
; return a new comparison routine that applies to sequences.

(define (map f xs)
  (if (null? xs) '()
      (cons (f (car xs)) (map f (cdr xs)))))

(define lexico
    (lambda (base)
	(define lex-fixed
	    (lambda (fixed lhs1 rhs1)
		(define check
		    (lambda (lhs2 rhs2)
			(if (null? lhs2)
			    fixed
			    (let ((probe
					(base (car lhs2)
					    (car rhs2))))
				(if (or (eq? probe 'equal)
					(eq? probe fixed))
				    (check (cdr lhs2)
					(cdr rhs2))
				    'uncomparable)))))
		(check lhs1 rhs1)))
	(define lex-first
	    (lambda (lhs3 rhs3)
		(if (null? lhs3)
		    'equal
		    (let ((probe
				(base (car lhs3)
				    (car rhs3))))
			(case probe
			    ((less more)
				(lex-fixed probe
				    (cdr lhs3)
				    (cdr rhs3)))
			    ((equal)
				(lex-first (cdr lhs3)
				    (cdr rhs3)))
			    ((uncomparable)
				'uncomparable))))))
	lex-first))

(define (make-lattice elem-list cmp-func)
    (cons elem-list cmp-func))

(define lattice->elements (lambda (l1) (car l1)))

(define lattice->cmp (lambda (l2) (cdr l2)))

(define lattice-reverse!
    (letrec ((rotate
		(lambda (fo fum)
		    (let ((next1 (cdr fo)))
			(set-cdr! fo fum)
			(if (null? next1)
			    fo
			    (rotate next1 fo))))))
	(lambda (lst1)
	    (if (null? lst1)
		'()
		(rotate lst1 '())))))

; Select elements of a list which pass some test.
(define zulu-select
    (lambda (test1 lst2)
	(define select-a-1
	    (lambda (ac1 lst3)
		(if (null? lst3)
		    (lattice-reverse! ac1)
		    (select-a-1
			(let ((head1 (car lst3)))
			    (if (test1 head1)
				(cons head1 ac1)
				ac1))
			(cdr lst3)))))
	(select-a-1 '() lst2)))

; Select elements of a list which pass some test and map a function
; over the result.  Note, only efficiency prevents this from being the
; composition of select and map.
(define select-map
    (lambda (test2 func lst4)
	(define select-a-2
	    (lambda (ac2 lst5)
		(if (null? lst5)
		    (lattice-reverse! ac2)
		    (select-a-2
			(let ((head2 (car lst5)))
			    (if (test2 head2)
				(cons (func head2)
				    ac2)
				ac2))
			(cdr lst5)))))
	(select-a-2 '() lst4)))

; This version of map-and tail-recurses on the last test.
(define map-and
    (lambda (proc lst6)
	(if (null? lst6)
	    #T
	    (letrec ((drudge
			(lambda (lst7)
			    (let ((rest (cdr lst7)))
				(if (null? rest)
				    (proc (car lst7))
				    (and (proc (car lst7))
					(drudge rest)))))))
		(drudge lst6)))))

(define (maps-1 source target pas new)
    (let ((scmp (lattice->cmp source))
	    (tcmp (lattice->cmp target)))
	(let ((less
		    (select-map
			(lambda (p1)
			    (eq? 'less
				(scmp (car p1) new)))
			(lambda (l4) (cdr l4))
			pas))
		(more
		    (select-map
			(lambda (p2)
			    (eq? 'more
				(scmp (car p2) new)))
			(lambda (l5) (cdr l5))
			pas)))
	    (zulu-select
		(lambda (t)
		    (and
			(map-and
			    (lambda (t2-1)
				(memq (tcmp t2-1 t) '(less equal)))
			    less)
			(map-and
			    (lambda (t2-2)
				(memq (tcmp t2-2 t) '(more equal)))
			    more)))
		(lattice->elements target)))))

(define (maps-rest source target pas rest to-1 to-collect)
    (if (null? rest)
	(to-1 pas)
	(let ((next2 (car rest))
		(rest (cdr rest)))
	    (to-collect
		(map
		    (lambda (x1)
			(maps-rest source target
			    (cons
				(cons next2 x1)
				pas)
			    rest
			    to-1
			    to-collect))
		    (maps-1 source target pas next2))))))

(define (maps source target)
    (make-lattice
	(maps-rest source
	    target
	    '()
	    (lattice->elements source)
	    (lambda (x2) (list (map (lambda (l6) (cdr l)) x2)))
	    (lambda (x3) (apply append x3)))
	(lexico (lattice->cmp target))))

(define print-frequency 10000)

(define (count-maps source target)
    (let ((count 0))
	(maps-rest source
	    target
	    '()
	    (lattice->elements source)
	    (lambda (x4)
		(set! count (+ count 1))
		(if (= 0 (remainder count print-frequency))
		    (begin
			(display count)
			(display "...")
			(newline))
                    (void))
		1)
	    (lambda (x5) (let loop ((i 0)
				   (l x5))
			  (cond ((null? l) i)
				(else (loop (+ i (car l))
					    (cdr l)))))))))

(let* ((l3
	    (make-lattice '(low high)
		(lambda (lhs4 rhs4)
		    (case lhs4
			((low)
			    (case rhs4
				((low)
				    'equal)
				((high)
				    'less)
				(else
				    (error 'make-lattice "base" rhs4))))
			((high)
			    (case rhs4
				((low)
				    'more)
				((high)
				    'equal)
				(else
				    (error 'make-lattice "base" rhs4))))
			(else
			    (error 'make-lattice "base" lhs4)))))))
  (display (count-maps l3 l3)))
