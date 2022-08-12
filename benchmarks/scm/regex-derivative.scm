; Author: Matthew Might
; Site:   http://matt.might.net/

; In about 100 lines of Scheme, this file implements a 
; regular-expression matcher based on the derivative of
; regular expressions.

; The derivative of a regular expression 're' with respect
; to a character 'c' is a new regular expression which matches
; what the expression 're' would match if it first matched 'c'.

; For example, using POSIX notation, the derivative of 
; (foo|frak)* with respect to 'f' is (oo|rak)(foo|frak)*

; To account for the possibility that a regular expression
; may not match any string, including the empty string,
; regular expressions include an unmatchable pattern:

; <regex> ::= #f                     ; Unmatchable pattern
;          |  #t                     ; Empty/blank pattern
;          |  '<symbol>              ; Symbol
;          |  #\<char>               ; Character
;          |  (alt <regex> <regex>)  ; Alternation
;          |  (seq <regex> <regex>)  ; Sequence
;          |  (rep <regex>)          ; Repetition

; Further reading:

; [1] Janusz Brzozowski. "Derivatives of Regular Expressions." 1964. 
; [2] Scott Owens, John Reppy, Aaron Turon. "Regular expression derivatives re-examined." 2009.

(define (debug-trace) 
  'do-nothing)
  


; Utilities
(define (cadr p) (car (cdr p)))
(define (caddr p) (car (cdr (cdr p))))

;; Special regular expressions.
(define regex-NULL #f)
(define regex-BLANK #t)

;; Predicates.
(define (regex-alt? re)
  (and (pair? re) (eq? (car re) 'alt)))

(define (regex-seq? re)
  (and (pair? re) (eq? (car re) 'seq)))

(define (regex-rep? re)
  (and (pair? re) (eq? (car re) 'rep)))

(define (regex-null? re)
  (eq? re #f))

(define (regex-empty? re)
  (eq? re #t))

(define (regex-atom? re)
  (or (char? re) (symbol? re)))

;; Regex deconstructors.
(define (match-seq re f)
  (and (regex-seq? re)
       (f (cadr re) (caddr re))))

(define (match-alt re f)
  (and (regex-alt? re)
       (f (cadr re) (caddr re))))

(define (match-rep re f)
  (and (regex-rep? re)
       (f (cadr re))))


;; Simplifying regex constructors.
(define (seq pat1 pat2)
  (cond
    ((regex-null? pat1) regex-NULL)
    ((regex-null? pat2) regex-NULL)
    ((regex-empty? pat1) pat2)
    ((regex-empty? pat2) pat1)
    (else (cons 'seq (cons pat1 (cons pat2 '()))))))
     
(define (alt pat1 pat2)
  (cond
    ((regex-null? pat1) pat2)
    ((regex-null? pat2) pat1)
    (else (cons 'alt (cons pat1 (cons pat2 '()))))))

(define (rep pat)
  (cond
    ((regex-null? pat) regex-BLANK)
    ((regex-empty? pat) regex-BLANK)
    (else (cons 'rep (cons pat '())))))

;; Matching functions.

; regex-empty : regex -> boolean
(define (regex-empty re)
  (cond
    ((regex-empty? re) #t)
    ((regex-null? re) #f)
    ((regex-atom? re) #f)
    ((match-seq re (lambda (pat1 pat2)
                     (seq (regex-empty pat1) (regex-empty pat2)))) #f) ;;FIXME
    ((match-alt re (lambda (pat1 pat2)
                     (alt (regex-empty pat1) (regex-empty pat2)))) #f) ;;FIXME
    ((regex-rep? re) #t)
    (else #f)))

; d/dc = regex-derivative
;(define d/dc regex-derivative)

; regex-deritvative : regex regex-atom -> regex
(define (d/dc re c)
  (debug-trace)
  (cond 
    ((regex-empty? re) regex-NULL)
    ((regex-null? re)  regex-NULL)
    ((eq? c re)        regex-BLANK)
    ((regex-atom? re)  regex-NULL)
    ((match-seq re     (lambda (pat1 pat2) 
                         (alt (seq (d/dc pat1 c) pat2)
                              (seq (regex-empty pat1) (d/dc pat2 c))))) #f) ;;FIXME
    ((match-alt re     (lambda (pat1 pat2)
                         (alt (d/dc pat1 c) (d/dc pat2 c)))) #f) ;;FIXME
    ((match-rep re     (lambda (pat)
                         (seq (d/dc pat c) (rep pat)))) #f) ;;FIXME
    (else regex-NULL)
    ))

; regex-match : regex list -> boolean 
(define (regex-match pattern data)
  (if (null? data)
      (regex-empty? (regex-empty pattern))
      (regex-match (d/dc pattern (car data)) (cdr data))))

;; Tests.
(define (check-expect check expect)
  (if (not (equal? check expect))
      (begin (display "check-expect failed; got: ")
             (display check)
             (display "; expected: ")
             (display expect)
             (newline))
      (void)))
       
(check-expect (d/dc 'baz 'f) #f)

;(check-expect (d/dc '(seq foo barn) 'foo) 'barn)

;(check-expect (d/dc '(alt (seq foo bar) (seq foo (rep baz))) 'foo)
;              '(alt bar (rep baz)))

;(check-expect (regex-match '(seq foo (rep bar)) '(foo bar bar bar)) #t)

;(check-expect (regex-match '(seq foo (rep bar)) '(foo bar baz bar bar)) #f)

;(check-expect (regex-match '(seq foo (rep (alt bar baz))) '(foo bar baz bar bar)) #t)
