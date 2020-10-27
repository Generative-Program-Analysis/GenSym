;; Fermat and Solovay-Strassen primality testing in Scheme.

;; Author: Matthew Might
;; Site:   http://matt.might.net/

;; Mathematical support.

; square(x) = x^2
(define (square x) (* x x))

; modulo-power: a fast modular exponentiation routine.
; modulo-power(base,exp,n) = base^exp [mod n]
(define (modulo-power base exp n)
  (if (= exp 0)
      1
      (if (odd? exp)
          (modulo (* base (modulo-power base (- exp 1) n)) n)
          (modulo (square (modulo-power base (/ exp 2) n)) n))))

;; Random number utilities.

;(define (random-char) 
;  (call-with-input-file "/dev/random" 
;    (lambda (port)
;     (read-char port))))

;(define (random-num)
;  (let ((n (char->integer (random-char))))
;    (if (= n 65533)
;        (random-num)
;        n)))

;(define (random-bit) (modulo (random-num) 2))

;(define (random-byte) (+ (modulo (random-num) ;128) (* 128 (random-bit))))

;(define (random bytes)
;  (if (<= bytes 0)
;      0
;      (+ (* 256 (random (- bytes 1))) (random-byte))))

;; Primality tests.

; is-trivial-composite?: divisibility tests with the first few primes.
(define (is-trivial-composite? n)
  (or (= (modulo n 2) 0)
      (= (modulo n 3) 0)
      (= (modulo n 5) 0)
      (= (modulo n 7) 0)
      (= (modulo n 11) 0)
      (= (modulo n 13) 0)
      (= (modulo n 17) 0)
      (= (modulo n 19) 0)
      (= (modulo n 23) 0)))

; is-fermat-prime?:
; Check, for many values of a:
;  a^(n-1) = 1 [mod n] ?  
;   If yes, could be prime.  
;   If no, then composite.
; Warning: Some Carmichael numbers (though rare) defeat this test.
(define (is-fermat-prime? n iterations)
  (or (<= iterations 0)
      (let* ((byte-size (ceiling (/ (log n) (log 2))))
             (a (random byte-size)))
        (if (= (modulo-power a (- n 1) n) 1)
            (is-fermat-prime? n (- iterations 1))
            #f))))

;; Prime generation.

; generate-fermat-prime(byte-size) yields a prime satisfying the Fermat test.
(define (generate-fermat-prime byte-size iterations)
  (let ((n (random byte-size)))
    (if
     (and (not (is-trivial-composite? n)) (is-fermat-prime? n iterations))
     n
     (generate-fermat-prime byte-size iterations))))

;; Example

(define iterations 10)
(define byte-size 15)

(display "Generating prime...") 
(newline)
(display (generate-fermat-prime byte-size iterations))
(display " is prime with at least probability 1 - 1/2^")
(display iterations)
(newline)
(display " if it is not a Carmichael number.")
(newline)
