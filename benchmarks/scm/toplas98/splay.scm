(let ([box list]
      [unbox (lambda (l) (car l))]
      [set-box! (lambda (l v) (set-car! l v))]
      [*numTest* 1])

  ;;------------------------------
   ;;     General Binary Trees     
   ;;------------------------------
   ;; a tree is either () or a node #(v l r), where v is the value
   ;; l is the left child and r is the right child
   [define node (lambda (v l r) (vector v l r))]
   [define tree-foreach
    (lambda (t f)
      (recur foreach ([t t])
	(match t
	  [() ()]
	  [#(v l r) (begin (f v) (foreach l) (foreach r))])))]
   [define tree-forall?
    (lambda (t f)
      (recur forall? ([t t])
	(match t
	  [() #t]
	  [#(v l r) (and (f v) (forall? l) (forall? r))])))]
   [define tree-exists?
    (lambda (t f)
      (not (tree-forall? t (lambda (x) (not (f x))))))]   
   [define tree-fold
    (lambda (t b f)
      (recur fold ([t t])
	(match t
	  [() b]
	  [#(v l r) (f v (fold l) (fold r))])))]   
   [define tree-size
    (lambda (t)
      (tree-fold t 0 (lambda (_ l r) (+ 1 l r))))]   
   [define tree->list
    (lambda (t)
      (recur ->list ([t t] [accum ()])
	(match t
	  [() accum]
	  [#(v l r) (->list l (cons v (->list r accum)))])))]
   ;;------------------------------
   ;;         Splay Trees
   ;;------------------------------
   [define splay
    (lambda (t k)
      (recur splay ([t t])
	(match t
	  [#(v l r)
	   (cond
	    [(= k v) t]
	    [(< k v)
	     (match l
	       [() t]
	       [#(v1 l1 r1)
		(cond
		 [(= k v1) (node v1 l1 (node v r1 r))]
		 [(< k v1)
		  (if (null? l1)
		      (node v1 () (node v r1 r))
		      (match-let ([#(v2 l2 r2) (splay l1)])
			(node v2 l2 (node v1 r2 (node v r1 r)))))]
		 [(null? r1)  (node v1 l1 (node v () r))]
		 [else (match-let ([#(v2 l2 r2) (splay r1)])
			 (node v2 (node v1 l1 l2)
			       (node v r2 r)))])])]
	    [(null? r)  t]
	    [else (match-let ([#(v1 l1 r1) r])
		    (cond
		     [(= k v1) (node v1 (node v l l1) r1)]
		     [(< k v1)
		      (if (null? l1)
			  (node v1 (node v l ()) r1)
			  (match-let ([#(v2 l2 r2) (splay l1)])
			    (node v2 (node v l l2)
				  (node v1 r2 r1))))]
		     [(null? r1)  (node v1 (node v l l1) ())]
		     [else (match-let ([#(v2 l2 r2) (splay r1)])
			     (node v2
				   (node v1 (node v l l1) l2)
				   r2))]))])])))]
   [define splay-to-max
    (rec splay
      (match-lambda
	[#(v l ()) (vector v l)]
	[#(v l #(v1 l1 ())) (vector v1 (node v l l1))]
	[#(v l #(v1 l1 r1))
	 (match-let ([#(v2 l2) (splay r1)])
	   (vector v2 (node v1 (node v l l1) l2)))]))]
   [define splay-to-min
    (rec splay
      (match-lambda
	[#(v () r) (vector v r)]
	[#(v #(v1 () r1) r) (vector v1 (node v r1 r))]
	[#(v #(v1 l1 r1) r)
	 (match-let ([#(v2 r2) (splay l1)])
	   (vector v2 (node v1 r2 (node v r1 r))))]))]
   [define join
    (match-lambda*
     [(() ())  ()]
     [(() t)   t]
     [(t ())   t]
     [(t t1)   (match-let ([#(v l) (splay-to-max t)])
		 (node v l t1))])]
   [define split
    (lambda (t x no yes)
      (match t
	[#(x1 l r)
	 (cond
	  [(= x x1) (yes l r)]
	  [(< x x1) (no l (node x1 () r))]
	  [else (no (node x1 l ()) r)])]))]
   [define splay-and-split
    (lambda (t x no yes)
      (if (null? t)
	  (no () ())
	  (split (splay t x) x no yes)))]
   [define splay-keep-all
    (lambda (t p?)
      (tree-fold t ()
		 (lambda (v l r)
		   (if (p? v) (node v l r) (join l r)))))]
   [define splay-combine
    (lambda (base alone-left alone-right left both)
      (rec combine
	(match-lambda*
	 [(() ())  base]
	 [(() t)   (alone-right t)]
	 [(t ())   (alone-left t)]
	 [(#(x l r) t)
	  (let ([continue
		 (lambda (c)
		   (lambda (l1 r1)
		     (c x (lambda () (combine l l1))
			(lambda () (combine r r1)))))])
	    (splay-and-split
	     t x (continue left) (continue both)))])))]
   ;;------------------------------
   ;;             Sets             
   ;;------------------------------
   ;; a set is a boxed tree
   [define set box]
   [define set-tree! set-box!]
   [define tree unbox]
   [define empty (lambda () (set ()))]
   [define empty? (lambda (s) (null? (tree s)))]
   [define singleton (lambda (x) (set (node x () ())))]
   [define size (lambda (s) (tree-size (tree s)))]
   [define extremum
    (lambda (splay-to node)
      (lambda (s)
	(let ([t (tree s)])
	  (if (null? t)
	      (error 'ordset-extremum "empty set")  
	      (match-let ([#(x lr) (splay-to t)])
		(set-tree! s (node x lr))
		x)))))]
   [define min (extremum splay-to-min (lambda (x r) (node x () r)))]
   [define max (extremum splay-to-max (lambda (x l) (node x l ())))]
   [define set-split
    (lambda (s x no yes)
      (let ([t (tree s)])
	(if (null? t)
	    (no () ())
	    (let ([t (splay (tree s) x)])
	      (set-tree! s t)
	      (split t x no yes)))))]
   [define contains?
    (lambda (s x)
      (set-split s x (lambda _ #f) (lambda _ #t)))]
   [define add
    (lambda (s x)
      (set-split s x
		 (lambda (l r) (set (node x l r)))
		 (lambda _ s)))]
   [define remove
    (lambda (s x)
      (set-split
       s x (lambda _ s)
       (lambda (l r)
	 (set (cond
	       [(null? l) r]
	       [(null? r) l]
	       [else (match-let ([#(v l) (splay-to-max l)])
		       (set-tree! s (node x (node v l ()) r))
		       (node v l r))])))))]
   [define subset (lambda (s p?) (set (splay-keep-all (tree s) p?)))]
   [define set-combine
    (lambda (base alone no yes top)
      (lambda (left both right)
	(let* ([node (lambda (b) (if b yes no))]
	       [combine (splay-combine
			 base (alone left) (alone right)
			 (node left) (node both))])
	  (lambda (s s1)
	    (top (combine (tree s) (tree s1)))))))]

   [define combine-bool
    (set-combine #t (lambda (b) (lambda _ b))
		 (lambda _ #f)
		 (lambda (_ l r) (and (l) (r)))
		 (lambda (b) b))]
   [define subset?   (combine-bool #f #t #t)]
   [define superset? (combine-bool #t #t #f)]
   [define disjoint? (combine-bool #t #f #t)]
   [define splay-equal?    (combine-bool #f #t #f)]
   [define combine-sets
    (set-combine
     () (lambda (b) (if b (lambda (t) t) (lambda _ ())))
     (lambda (_ l r) (join (l) (r)))
     (lambda (x l r) (node x (l) (r)))
     set)]
   [define union        (combine-sets #t #t #t)]
   [define intersection (combine-sets #f #t #f)]
   [define difference   (combine-sets #t #f #f)]
   [define list->
    (lambda (l)
      (recur loop ([l l] [s (empty)])
	(if (null? l)
	    s
	    (loop (cdr l) (add s (car l))))))]
   [define ->list (lambda (s) (tree->list (tree s)))]
   [define exists? (lambda (s p?) (tree-exists? (tree s) p?))]
   [define forall? (lambda (s p?) (tree-forall? (tree s) p?))]
   [define foreach (lambda (s f) (tree-foreach (tree s) f))]

  ;----------------------------------------
  ;                Tests
  ;----------------------------------------
  (let loop ((i 0))
    (cond ((= i *numTest*))
	  (else (let ([empty (empty)]
		      [odds (list-> (list 1 3 5 7 9))]
		      [evens (list-> (list 2 4 6 8 10))]
		      [mix (list-> (list 1 2 4 7 8))])
		  (unless (and (empty? empty)
			       (not (empty? odds))
			       (= 0 (size empty))
			       (= 5 (size odds))
			       (splay-equal? odds odds)
			       (not (splay-equal? odds evens))
			       (= 10 (max evens))
			       (= 1 (min odds))
			       (contains? odds 5)
			       (not (contains? odds 6))
			       (subset? odds odds)
			       (not (subset? mix odds))
			       (subset? mix (union odds evens))
			       (superset? (add odds 11) odds)
			       (superset? odds (difference mix evens))
			       (disjoint? (difference mix odds) odds)
			       (not (disjoint? evens evens))
			       (superset? odds (remove odds 1))
			       (splay-equal? odds (remove odds 2))
			       (forall? odds (lambda (n) (odd? n)))
			       (forall? evens (lambda (n) (even? n)))
			       (subset? (subset mix (lambda (n) (even? n))) evens)
			       (let ([sum 0])
				 (foreach odds (lambda (x) (set! sum (+ sum x))))
				 (= sum 25))
			       (subset? (intersection mix odds) odds)
			       )
			  (error 'splay-test ""))
		  (loop (+ i 1)))))))


