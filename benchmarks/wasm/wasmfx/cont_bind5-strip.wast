(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (cont 0))
  (type (;2;) (func (param i32 i32) (result i32)))
  (type (;3;) (cont 2))
  (type (;4;) (func (param i32)))
  (type (;5;) (func))
  (import "spectest" "print_i32" (func (;0;) (type 4)))
  (export "main" (func 3))
  (start 3)
  (elem (;0;) declare func 1)
  (func (;1;) (type 2) (param i32 i32) (result i32)
    local.get 0
    local.get 1
    i32.sub
  )
  (func (;2;) (type 2) (param i32 i32) (result i32)
    local.get 1
    local.get 0
    ref.func 1
    cont.new 3
    cont.bind 3 1
    resume 1
  )
  (func (;3;) (type 5)
    i32.const 22
    i32.const 44
    call 2
    call 0
  )
)
