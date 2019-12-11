;; Mathematical routines.

; extended-gcd(a,b) = (x,y), such that a*x + b*y = gcd(a,b)
(define (extended-gcd a b)
  (if (= (modulo a b) 0)
      (cons 0 1)
      (let* ((x:y (extended-gcd b (modulo a b)))
             (x (car x:y))
             (y (cdr x:y)))
        (cons y (- x (* y (quotient a b)))))))

; modulo-inverse(a,n) = b, such that a*b = 1 [mod n].
(define (modulo-inverse a n)
  (modulo (car (extended-gcd a n)) n))

; totient(n) = (p - 1)*(q - 1), 
;  where pq is the prime factorization of n.
(define (totient p q) (* (- p 1) (- q 1)))

; square(x) = x^2
(define (square x) (* x x))

; modulo-power(base,exp,n) = base^exp [mod n]
(define (modulo-power base exp n)
  (if (= exp 0)
      1
      (if (odd? exp)
          (modulo (* base (modulo-power base (- exp 1) n)) n)
          (modulo (square (modulo-power base (/ exp 2) n)) n))))

;; RSA routines.

; A legal public exponent e is between
;  1 and totient(n), and gcd(e,totient(n)) = 1
(define (is-legal-public-exponent? e p q)
  (and (< 1 e) 
       (< e (totient p q))
       (= 1 (gcd e (totient p q)))))

; The private exponent is the inverse of the public exponent, mod n.
(define (private-exponent e p q) 
  (if (is-legal-public-exponent? e p q)
      (modulo-inverse e (totient p q))
      (error "Not a legal public exponent for that modulus.")))

; An encrypted message is c = m^e [mod n].
(define (encrypt m e n)
  (if (> m n)
      (error "The modulus is too small to encrypt the message.")
      (modulo-power m e n)))

; A decrypted message is m = c^d [mod n].
(define (decrypt c d n)
  (modulo-power c d n))

;; RSA example.

(define p 41)       ; A "large" prime.
(define q 47)       ; Another "large" prime.
(define n (* p q))  ; The public modulus.

(define e 7)                        ; The public exponent.
(define d (private-exponent e p q)) ; The private exponent.

(define plaintext  42)           
(define ciphertext (encrypt plaintext e n)) 

(define decrypted-ciphertext (decrypt ciphertext d n))


(display "The plaintext is:            ")
(display plaintext)
(newline)

(display "The ciphertext is:           ")
(display ciphertext)
(newline)

(display "The decrypted ciphertext is: ")
(display decrypted-ciphertext)
(newline)

(if (not (= plaintext decrypted-ciphertext))
  (error "RSA fail!")
  (display "Success"))
