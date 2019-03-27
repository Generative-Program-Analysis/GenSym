;这里是注释
;第一章源代码

(define atom? 
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))))

(atom? 'atom)

(atom? 'turkey)

(atom? 1942)

(atom? 'u)

(atom? '*abc$)

(list? '(atom))

(list? '(atom turkey or))

; 会报错
; (list? '(atom turkey) or)

(list? '((atom turkey) or))

(list? '())

(atom? '())

(car '(a b c))

(car '((a b c) x y z))

; 会报错
;(car 'hotdog)

(cdr '(a b c))

(cdr '((a b c) x y z))

(cdr '())

(cons 'peanut '(butter and jelly))

(cons '(banana and) '(peanut butter and jelly))

(null? (quote ()))
