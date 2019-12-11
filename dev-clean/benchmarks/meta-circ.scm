;; Utilities.

(define (cadr p) (car (cdr p)))
(define (cddr p) (cdr (cdr p)))
(define (cdddr p) (cdr (cdr (cdr p))))
(define (caadr p) (car (car (cdr p))))
(define (cdadr p) (cdr (car (cdr p))))
(define (caddr p) (car (cdr (cdr p))))
(define (cadar p) (car (cdr (car p))))
(define (cadddr p) (car (cdr (cdr (cdr p)))))

(define (map f lst) 
  (if (pair? lst)
      (cons (f (car lst))
            (map f (cdr lst)))
      '()))


(define (for-each f lst) 
  (begin
    (if (pair? lst)
        (cons (f (car lst))
              (map f (cdr lst)))
        '())
    (void)))
  

(define (append lst1 lst2)
  (if (not (pair? lst1))
      lst2
      (cons (car lst1) 
            (append (cdr lst1) lst2))))

(define (reverse lst)
  (if (not (pair? lst))
      lst
      (append (reverse (cdr lst)) (cons (car lst) '()))))
      

(define (string->list s)
  (define (f i)
    (if (< i (string-length s))
        (cons (string-ref s i)
              (f (+ i 1)))
        '()))
  (f 0))


(define (apply f l)
  (let ((len (length l)))
  (cond
    ((= len 0) (f))
    ((= len 1) (f (car l)))
    ((= len 2) (f (car l) (cadr l)))
    ((= len 3) (f (car l) (cadr l) (caddr l)))
    ((= len 4) (f (car l) (cadr l) (caddr l) (cadddr l))))))


(define (assv x f)
  (if (pair? f)
      (if (eqv? (car (car f)) x)
          (car f)
          (assv x (cdr f)))
      #f))


(define (assq x f)
  (if (pair? f)
      (if (eq? (car (car f)) x)
          (car f)
          (assv x (cdr f)))
      #f))



; gensym-count : integer
(define gensym-count 0)

; gensym : symbol -> symbol
(define gensym (lambda (name)
                 (begin
                   (set! gensym-count (+ gensym-count 1))
                   (string->symbol (string-append 
                                    (if (symbol? name)
                                        (symbol->string name)
                                        name)
                                    "$"
                                    (number->string gensym-count))))))


; three-d-tagged-list? : symbol s-exp -> boolean
(define (three-d-tagged-list? tag lst)
  (and (pair? lst)
       (or (eq? (car lst) tag)
           (and (procedure? (car lst)) ; in case it's a three-d s-expression
                (equal? ((car lst)) (initial tag))))))

; tagged-list? : symbol s-exp -> boolean
(define (tagged-list? tag lst)
  (and (pair? lst)
       (eq? (car lst) tag)))

; singlet? : list -> boolean
(define (singlet? list)
  (and (pair? list)
       (null? (cdr list))))

; partition-k : 
(define (partition-k pred list k)
  (if (not (pair? list))
      (k '() '())
      (partition-k pred (cdr list) (lambda (trues falses)
                                    (if (pred (car list))
                                        (k (cons (car list) trues) falses)
                                        (k trues (cons (car list) falses)))))))

(define (unzip-amap-k amap k)
  (if (not (pair? amap))
      (k '() '())
      (unzip-amap-k (cdr amap) (lambda (xs ys)
                                 (k (cons (car (car amap)) xs)
                                    (cons (cadr (car amap)) ys))))))

(define (void) (if #f #t))

;; Definition types.
(define (define? s-exp)     (tagged-list? 'define s-exp))
(define (define-var? s-exp) (and (define? s-exp) (symbol? (cadr s-exp))))
(define (define-fun? s-exp) (and (define? s-exp) (pair? (cadr s-exp))))

(define (define->var s-exp) (cond
                              ((define-var? s-exp) (cadr s-exp))
                              ((define-fun? s-exp) (caadr s-exp))
                              (else                (error "unknown define!"))))

(define (define->exp s-exp) (cond
                              ((define-var? s-exp) (caddr s-exp))
                              ((define-fun? s-exp) (cons three-d-lambda
                                                         (cons (cdadr s-exp)
                                                               (cddr s-exp))))))

;; Expression types.

; if
(define (if? s-exp)        (three-d-tagged-list? 'if s-exp))
(define (if-single? s-exp) (and (if? s-exp) (null? (cdddr s-exp))))
(define (if->cond  s-exp)  (cadr s-exp))
(define (if->true  s-exp)  (caddr s-exp))
(define (if->false s-exp)  (cadddr s-exp))

; quote
(define (quote? s-exp)     (three-d-tagged-list? 'quote s-exp))
(define (quote->text s-exp) (cadr s-exp))

; lambda
(define (lambda? s-exp)               (three-d-tagged-list? 'lambda s-exp))
(define (lambda-multi? s-exp)         (and (lambda? s-exp) (symbol? (cadr s-exp))))
(define (lambda->formals s-exp)       (cadr s-exp))
(define (lambda->body s-exp)          (cddr s-exp))
(define (lambda->body-as-exp s-exp)   (make-begin (cddr s-exp)))
       
; begin
(define (begin? s-exp)      (three-d-tagged-list? 'begin s-exp))
(define (begin->body s-exp) (cdr s-exp))
  
(define (make-begin exps)     (cond
                                ((singlet? exps) (car exps))
                                (else (cons three-d-begin exps))))

; application
(define (app? s-exp)      (pair? s-exp))
(define (app->fun s-exp)  (car s-exp))
(define (app->args s-exp) (cdr s-exp))

; binding
(define (binding->var binding) (car binding))
(define (binding->exp binding) (cadr binding))

; letrec
(define (letrec? s-exp)             (three-d-tagged-list? 'letrec s-exp))
(define (letrec->bindings s-exp)    (cadr s-exp))
(define (letrec->body s-exp)        (cddr s-exp))
(define (letrec->body-as-exp s-exp) (make-begin (letrec->body s-exp)))

; let
(define (let? s-exp)             (three-d-tagged-list? 'let s-exp))
(define (let->bindings s-exp)    (cadr s-exp))
(define (let->body s-exp)        (cddr s-exp))
(define (let->body-as-exp s-exp) (make-begin (let->body s-exp)))

; let*
(define (let*? s-exp)             (three-d-tagged-list? 'let* s-exp))
(define (let*->bindings s-exp)    (cadr s-exp))
(define (let*->body s-exp)        (cddr s-exp))
(define (let*->body-as-exp s-exp) (make-begin (let*->body s-exp)))

; or
(define (or? s-exp)      (three-d-tagged-list? 'or s-exp))
(define (or->exps s-exp) (cdr s-exp))

(define (make-or exps)
  (cond 
    ((null? exps)    #f)
    ((singlet? exps) (car exps))
    (else            (cons 'or exps))))


; and
(define (and? s-exp)      (three-d-tagged-list? 'and s-exp))
(define (and->exps s-exp) (cdr s-exp))

(define (make-and exps)
  (cond
    ((null? exps)    #t)
    ((singlet? exps) (car exps))
    (else            (cons 'and exps))))


; cond
(define (cond? s-exp) (three-d-tagged-list? 'cond s-exp))
(define (cond->clauses s-exp) (cdr s-exp))

(define (arrow-clause? clause) (and (pair? clause)
                                    (pair? (cdr clause))
                                    (eq? (cadr clause) '=>)))
(define (else-clause? clause) (three-d-tagged-list? 'else clause))

(define (cond-clause->exp clause)
  (cond
    ((singlet? clause) (car clause))
    ((else-clause? clause) (cadr clause))
    ((arrow-clause? clause) (caddr clause))
    (else (make-begin (cdr clause)))))

(define (cond-clause->test clause)
  (cond
    ((singlet? clause) (car clause))
    ((else-clause? clause) #t)
    ((arrow-clause? clause) (car clause))
    (else (car clause))))

; set!
(define (set!? s-exp)     (three-d-tagged-list? 'set! s-exp))
(define (set!->var s-exp) (cadr s-exp))
(define (set!->exp s-exp) (caddr s-exp))

; macro
(define (macro? s-exp)      (three-d-tagged-list? 'macro s-exp))
(define (macro->proc s-exp) (cadr s-exp))


;; Special values.

; syntax-primitive
(define (syntax-primitive? value)      (three-d-tagged-list? 'syntax-primitive value))
(define (syntax-primitive->eval value) (cadr value))



;; Transformations.

; <def> => <binding>
(define (def->binding def)    (list (define->var def)
                                    (define->exp def)))

; <body> => <letrec-exp>
(define (body->letrec decs)   (partition-k 
                               define?
                               decs 
                               (lambda (defs exps)
                                 (if (null? defs)
                                     (make-begin exps)
                                     (let ((bindings (map def->binding defs)))
                                       (list three-d-letrec
                                             bindings
                                             (make-begin exps)))))))
                                                         



; <letrec-exp> => <let&set!-exp>
(define (letrec->lets+sets exp)
; (display "deletrecing: ")
; (display exp)
; (newline)
  (if (not (letrec? exp))
      exp
      (let ((bindings (map (lambda (binding)
                             (list (binding->var binding)
                                   #f))
                           (letrec->bindings exp)))
            (sets (map (lambda (binding)
                         (list three-d-set!
                               (binding->var binding)
                               (binding->exp binding)))
                       (letrec->bindings exp))))
        (list three-d-let
              bindings
              (make-begin
               (append sets
                       (letrec->body exp)))))))

  

; <cond-exp> => <if-exp>
(define (cond->if cond-exp)
  (if (not (cond? cond-exp))
      cond-exp
      (cond-clauses->if (cond->clauses cond-exp))))

(define (cond-clauses->if clauses)
  (if (null? clauses)
      '(void)
      (let ((clause (car clauses)))
        (cond
          ((singlet? clause)      (make-or (cons clause
                                                 (cond-clauses->if (cdr clauses)))))
          ((else-clause? clause)  (cond-clause->exp clause))
          (else                   (list three-d-if
                                        (cond-clause->test clause)
                                        (cond-clause->exp clause)
                                        (cond-clauses->if (cdr clauses))))))))


; <and-exp> => <if-exp>
(define (and->if exp)
  (if (not (and? exp))
      exp
      (let ((exps (and->exps exp)))
        (cond 
          ((null? exps)    #t)
          ((singlet? exps) (car exps))
          (else            (list three-d-if (car exps)
                                 (and->if (cons 'and (cdr exps)))
                                 #f))))))
       


; <or-exp> => <if-exp>
(define (or->if exp)
  (if (not (or? exp))
      exp
      (let ((exps (or->exps exp)))
        (cond 
          ((null? exps)    #f)
          ((singlet? exps) (car exps))
          (else            (let (($tmp (gensym "or-tmp")))
                             (list three-d-let (list (list $tmp (car exps)))
                                   (list three-d-if $tmp $tmp
                                         (or->if (cons three-d-or (cdr exps)))))))))))
  


; <let*-exp> => <let-exp>
(define (let*->let exp)
  (if (not (let*? exp))
      exp
      (let*-bindings->let (let*->bindings exp)
                          (let*->body exp))))

(define (let*-bindings->let bindings body)
  (cond
    ((singlet? bindings) (cons three-d-let (cons (list (car bindings)) body)))
    ((null? bindings)    (make-begin body))
    (else                (cons three-d-let (cons (list (car bindings)) 
                                          (list (let*-bindings->let (cdr bindings) body)))))))


;; <let-exp> => <app-exp>
(define (let->app exp)
  (if (not (let? exp))
      exp
      (unzip-amap-k (let->bindings exp) (lambda (vars exps)
                                          (cons (cons three-d-lambda 
                                                      (cons vars 
                                                            (let->body exp)))
                                                exps)))))
                                            




;; Evaluation
(define (eval exp env)
  ;(display "evaluating: ")
  ;(display exp)
  ;(newline)
  (cond
    ((symbol? exp)    (env-lookup env exp))
    
    ((number? exp)    exp)
    
    ((boolean? exp)   exp)
    
    ((string? exp)    exp)
    
    ; three-d-syntax is invoked to produce a value:
    ((procedure? exp) (exp))
    
    ;((quote? exp)    (eval-quote exp env))
    
    ;((if? exp)      (eval-if exp env))
    
    ;((cond? exp)    (eval-cond exp env))
    
    ;((or? exp)      (eval-or exp env))
    
    ;((and? exp)     (eval-and exp env))
    
    ;((lambda? exp)  (eval-lambda exp env))
    
    ;((let? exp)     (eval (let->app exp) env))
    
    ;((let*? exp)    (eval (let*->let exp) env))
    
    ;((letrec? exp)  (eval (letrec->lets+sets exp) env))
    
    ;((begin? exp)   (eval-begin exp env))
    
    ;((set!? exp)    (eval-set! exp env))
    
    ;((macro? exp)   (list 'macro (eval (macro->proc exp) env)))
    
    ((app? exp)     (perform-apply (eval (app->fun exp) env) exp env))))

; eval-with : env -> exp -> value
(define (eval-with env) 
  (lambda (exp)
    (eval exp env)))

; eval* : list[exp] env -> list[value]
(define (eval* exps env) (map (eval-with env) exps))

; eval-quote : quote-exp env -> value
(define (eval-quote exp env)
  (quote->text exp))

; eval-if : if-exp env -> value
(define (eval-if exp env)
  (if (if-single? exp)
      (if (eval (if->cond exp) env)
          (eval (if->true exp) env))
      (if (eval (if->cond exp) env)
          (eval (if->true exp) env)
          (eval (if->false exp) env))))

; eval-cond : cond-exp env -> value
(define (eval-cond exp env)
  (eval (cond->if exp) env))

; eval-and : and-exp env -> value
(define (eval-and exp env)
  (eval (and->if exp) env))

; eval-exp : or-exp env -> value
(define (eval-or exp env)
  (eval (or->if exp) env))

; eval-let : let-exp env -> value
(define (eval-let exp env)
  (eval (let->app exp) env))

; eval-let* : let*-exp env -> value
(define (eval-let* exp env)
  (eval (let*->let exp) env))

; eval-letrec : letrec-exp env -> value
(define (eval-letrec exp env)
  (eval (letrec->lets+sets exp) env))

; eval-begin : begin-exp env -> value
(define (eval-begin exp env)
  ; DEBUG
  ;(display 'eval-begin:)
  ;(display exp)
  ;(newline)
  (let ((simplified (body->letrec (begin->body exp))))
    ;(display 'simplified:)
    ;(display simplified)
    ;(newline)
    (cond
      ((begin? simplified) (car (reverse (eval* (begin->body exp) env))))
      (else                (eval simplified env)))))

; eval-set! : set!-exp env -> value
(define (eval-set! exp env)
  (env-set! env (set!->var exp) (eval (set!->exp exp) env)))
            
; eval-lambda : lambda-exp env -> value      
(define (eval-lambda exp env)
  (let ((formals (lambda->formals exp)))
    (lambda args
      (if (symbol? formals)
          (eval (lambda->body-as-exp exp)
                (env-extend env formals args))
          (eval (lambda->body-as-exp exp)
                (env-extend* env formals args))))))

; eval-macro : macro-exp env -> value
(define (eval-macro exp env)
  (list 'macro (eval (macro->proc exp) env)))


;; Mutable environments are constructed from closures:

;  type env = var bool value -> value

; If the second parameter to an environment is true, 
; then it modifies the value of that variable.

; If the second parameter is false,
; then it returns the value of that variable.

; env-lookup : env var -> value
(define (env-lookup env var)
  ;DEBUG
  ;(display 'lookup:)
  ;(display var)
  ;(newline)
  (let ((value (env var #f 'unused)))
    ;(display 'value:)
    ;(display value)
    ;(newline)
    value))

; env-set! : env var value -> void
(define (env-set! env var value)
  ;DEBUG
  ;(display 'set!:)
  ;(display var)
  ;(newline)  
  (env var #t value))
  
; env-extend : env var value -> env
(define (env-extend env var value)
  (lambda (seek-var modify? value!)
    (if (eq? var seek-var)
        (if modify?
            (set! value value!)
            value)
        (env seek-var modify? value!))))

; env-extend* : env list[var] list[value] -> env
(define (env-extend* env vars values)
  (if (pair? vars)
      (env-extend* (env-extend env (car vars) (car values)) (cdr vars) (cdr values))
      env))

; empty-env : env
(define (empty-env var modify? value!)
  (if modify?
      (begin (display "error: cannot modify undefined variable: ")
             (display var)
             (display " with ")
             (display value!))
      (begin (display "error: cannot look up undefined variable: ")
             (display var)))
  (newline)
  (error))

; initial-environment-amap : alist[symbol,value]
(define initial-environment-amap
  (list (list 'apply      apply)
        (list '+          +)
        (list 'not        not)
        (list 'display    display)
        (list 'newline    newline)
        (list 'cons       cons) 
        (list 'car        car)
        (list 'cdr        cdr)
        (list 'cadr       cadr)
        (list 'caadr      caadr)
        (list 'cadar      cadar)
        (list 'cddr       cddr)
        (list 'cdddr      cdddr)
;        (list 'list       list)
        (list 'null?      null?)
        (list 'pair?      pair?)
        (list 'list?      list?)
        (list 'number?    number?)
        (list 'string?    string?)
        (list 'symbol?    symbol?)
        (list 'procedure? procedure?)
        (list 'eq?        eq?)
        (list '=          =)
        (list 'gensym     gensym)
        (list 'void       void)
        
        (list 'quote    (list 'syntax-primitive eval-quote))
        (list 'if       (list 'syntax-primitive eval-if))
        (list 'cond     (list 'syntax-primitive eval-cond))
        (list 'and      (list 'syntax-primitive eval-and))
        (list 'or       (list 'syntax-primitive eval-or))
        (list 'let      (list 'syntax-primitive eval-let))
        (list 'let*     (list 'syntax-primitive eval-let*))
        (list 'letrec   (list 'syntax-primitive eval-letrec))
        (list 'begin    (list 'syntax-primitive eval-begin))
        (list 'set!     (list 'syntax-primitive eval-set!))
        (list 'lambda   (list 'syntax-primitive eval-lambda))
        (list 'macro    (list 'syntax-primitive eval-macro))

        ))


; environment : -> env
(define (initial-environment)
  (unzip-amap-k initial-environment-amap (lambda (symbols values)
                                           (env-extend* empty-env 
                                                        symbols
                                                        values))))

; initial : symbol -> value
(define (initial sym) 
  (env-lookup (initial-environment) sym))

; three-d : value -> three-d-s-exp
(define (three-d value)
  (lambda () value))
  
; three-d primitives:
(define three-d-quote   (three-d (initial 'quote)))
(define three-d-if      (three-d (initial 'if)))
(define three-d-cond    (three-d (initial 'cond)))
(define three-d-and     (three-d (initial 'and)))
(define three-d-or      (three-d (initial 'or)))
(define three-d-let     (three-d (initial 'let)))
(define three-d-let*    (three-d (initial 'let*)))
(define three-d-letrec  (three-d (initial 'letrec)))
(define three-d-set!    (three-d (initial 'set!)))
(define three-d-lambda  (three-d (initial 'lambda)))
(define three-d-begin   (three-d (initial 'begin)))


(define (perform-apply fun app-exp env)
  (let ((args (app->args app-exp)))
    (cond
      ((macro? fun)            (eval (apply (macro->proc fun) args) env))
      ((syntax-primitive? fun) ((syntax-primitive->eval fun) app-exp env))
      (else                    (let ((arg-values (eval* args env)))
                                 (apply fun arg-values))))))
  

(eval '((lambda (x) x) 10) (initial-environment))
