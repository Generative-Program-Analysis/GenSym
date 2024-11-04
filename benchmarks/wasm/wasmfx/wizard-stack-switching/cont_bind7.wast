(module
;;  (type $f1 (func (result i32)))
;;  (type $c1 (cont $f1))

  (type $f2 (func (param i32) (result i32)))
  (type $c2 (cont $f2))

  (type $f3 (func (param i64 i32) (result i32)))
  (type $c3 (cont $f3))

  (func $sub (param i64 i32) (result i32) (i32.sub (i32.wrap_i64 (local.get 0)) (local.get 1)))
  (elem declare func $sub)

  (func (export "main") (param i64 i32) (result i32)
    (resume $c2
      (local.get 1)
      (cont.bind $c3 $c2
        (local.get 0)
        (cont.new $c3 (ref.func $sub))))
  )
)

(assert_return (invoke "main" (i64.const 22) (i32.const 44)) (i32.const -22))
(assert_return (invoke "main" (i64.const -444) (i32.const 111)) (i32.const -555))

