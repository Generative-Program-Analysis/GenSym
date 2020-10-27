(define-data handle owner ref)
(define-data Aspace name)

(defmacro def-macro args 
  (match args
    [((name . pat) . body)
     `(defmacro ,name args2
        (match args2
          [,pat (let () ,@body)]))]))

(def-macro (with-aspace aspace exp)
  `(let ((current-aspace (lambda () ,aspace)))
     ,exp))

(let* ((a (make-Aspace "foo"))
       (b (make-handle a 0))
       (c (make-Aspace "bar"))
       (d (make-handle b 0)))
  (handle-ref d)
  (handle-ref b))
 
