(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (cont 0))
  (export "main" (func 2))
  ;; (elem (;0;) declare func 0 1)
  (func (;0;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 10
    i32.add
  )
  (func (;1;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 100
    i32.add
    ref.func 0
    cont.new 1
    resume 1
    i32.const 1000
    i32.add
  )
  (func (;2;) (type 0) (param i32) (result i32)
    local.get 0
    ref.func 1
    cont.new 1
    resume 1
  )
)

(assert_return (invoke "main" (i32.const 4)) (i32.const 1114))
(assert_return (invoke "main" (i32.const 5)) (i32.const 1115))
(assert_return (invoke "main" (i32.const 9)) (i32.const 1119))
