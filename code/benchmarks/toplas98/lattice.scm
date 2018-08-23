; Given a comparison routine that returns one of
;	less
;	more
;	equal
;	uncomparable
; return a new comparison routine that applies to sequences.
(define lexico
    (lambda (base)
	(define lex-fixed
	    (lambda (fixed lhs rhs)
		(define check
		    (lambda (lhs rhs)
			(if (null? lhs)
			    fixed
			    (let ((probe
					(base (car lhs)
					    (car rhs))))
				(if (or (eq? probe 'equal)
					(eq? probe fixed))
				    (check (cdr lhs)
					(cdr rhs))
				    'uncomparable)))))
		(check lhs rhs)))
	(define lex-first
	    (lambda (lhs rhs)
		(if (null? lhs)
		    'equal
		    (let ((probe
				(base (car lhs)
				    (car rhs))))
			(case probe
			    ((less more)
				(lex-fixed probe
				    (cdr lhs)
				    (cdr rhs)))
			    ((equal)
				(lex-first (cdr lhs)
				    (cdr rhs)))
			    ((uncomparable)
				'uncomparable))))))
	lex-first))

(define (make-lattice elem-list cmp-func)
    (cons elem-list cmp-func))

(define lattice->elements (lambda (l) (car l)))

(define lattice->cmp (lambda (l) (cdr l)))

; Select elements of a list which pass some test.
(define zulu-select
    (lambda (test lst)
	(define select-a
	    (lambda (ac lst)
		(if (null? lst)
		    (lattice-reverse! ac)
		    (select-a
			(let ((head (car lst)))
			    (if (test head)
				(cons head ac)
				ac))
			(cdr lst)))))
	(select-a '() lst)))

(define lattice-reverse!
    (letrec ((rotate
		(lambda (fo fum)
		    (let ((next (cdr fo)))
			(set-cdr! fo fum)
			(if (null? next)
			    fo
			    (rotate next fo))))))
	(lambda (lst)
	    (if (null? lst)
		'()
		(rotate lst '())))))

; Select elements of a list which pass some test and map a function
; over the result.  Note, only efficiency prevents this from being the
; composition of select and map.
(define select-map
    (lambda (test func lst)
	(define select-a
	    (lambda (ac lst)
		(if (null? lst)
		    (lattice-reverse! ac)
		    (select-a
			(let ((head (car lst)))
			    (if (test head)
				(cons (func head)
				    ac)
				ac))
			(cdr lst)))))
	(select-a '() lst)))



; This version of map-and tail-recurses on the last test.
(define map-and
    (lambda (proc lst)
	(if (null? lst)
	    #T
	    (letrec ((drudge
			(lambda (lst)
			    (let ((rest (cdr lst)))
				(if (null? rest)
				    (proc (car lst))
				    (and (proc (car lst))
					(drudge rest)))))))
		(drudge lst)))))

(define (maps-1 source target pas new)
    (let ((scmp (lattice->cmp source))
	    (tcmp (lattice->cmp target)))
	(let ((less
		    (select-map
			(lambda (p)
			    (eq? 'less
				(scmp (car p) new)))
			(lambda (l) (cdr l))
			pas))
		(more
		    (select-map
			(lambda (p)
			    (eq? 'more
				(scmp (car p) new)))
			(lambda (l) (cdr l))
			pas)))
	    (zulu-select
		(lambda (t)
		    (and
			(map-and
			    (lambda (t2)
				(memq (tcmp t2 t) '(less equal)))
			    less)
			(map-and
			    (lambda (t2)
				(memq (tcmp t2 t) '(more equal)))
			    more)))
		(lattice->elements target)))))

(define (maps-rest source target pas rest to-1 to-collect)
    (if (null? rest)
	(to-1 pas)
	(let ((next (car rest))
		(rest (cdr rest)))
	    (to-collect
		(map
		    (lambda (x)
			(maps-rest source target
			    (cons
				(cons next x)
				pas)
			    rest
			    to-1
			    to-collect))
		    (maps-1 source target pas next))))))

(define (maps source target)
    (make-lattice
	(maps-rest source
	    target
	    '()
	    (lattice->elements source)
	    (lambda (x) (list (map (lambda (l) (cdr l)) x)))
	    (lambda (x) (apply append x)))
	(lexico (lattice->cmp target))))

(define print-frequency 10000)

(define (count-maps source target)
    (let ((count 0))
	(maps-rest source
	    target
	    '()
	    (lattice->elements source)
	    (lambda (x)
		(set! count (+ count 1))
		(if (= 0 (remainder count print-frequency))
		    (begin
			(display count)
			(display "...")
			(newline))
                    (void))
		1)
	    (lambda (x) (let loop ((i 0)
				   (l x))
			  (cond ((null? l) i)
				(else (loop (+ i (car l))
					    (cdr l)))))))))

(let* ((l2
	    (make-lattice '(low high)
		(lambda (lhs rhs)
		    (case lhs
			((low)
			    (case rhs
				((low)
				    'equal)
				((high)
				    'less)
				(else
				    (error 'make-lattice "base" rhs))))
			((high)
			    (case rhs
				((low)
				    'more)
				((high)
				    'equal)
				(else
				    (error 'make-lattice "base" rhs))))
			(else
			    (error 'make-lattice "base" lhs)))))))
  (display (count-maps l2 l2)))
