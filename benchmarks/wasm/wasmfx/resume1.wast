(module
  (type $f1 (func))
  (type $c1 (cont $f1))
  (import "spectest" "print_i32" (func $print_i32 (param i32)))
  (func $empty
    i32.const 424242
    call $print_i32
  ) 
  (elem declare func $empty)
  (func (export "main") (result i32)
    (resume $c1 (cont.new $c1 (ref.func $empty)))
    i32.const 42
  )
)

(assert_return (invoke "main") (i32.const 42))

;; (module
;;   (type (;0;) (func))
;;   (type (;1;) (cont 0))
;;   (type (;2;) (func (result i32)))
;;   (export "main" (func 1))
;;   (elem (;0;) declare func 0)
;;   (func (;0;) (type 0))
;;   (func (;1;) (type 2) (result i32)
;;     ref.func 0
;;     cont.new 1
;;     resume 1
;;     i32.const 42
;;   )
;; )
