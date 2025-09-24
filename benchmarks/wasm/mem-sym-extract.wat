(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (param i32)))
  (import "console" "assert" (func (type 3)))
  (func (;1;) (type 2) (param i32) (result i32)
    i32.const 0
    local.get 0
    i32.store
    i32.const 0
    i32.load
    i32.const 1
    i32.eq
    if (result i32)  ;; if x == 256
      i32.const 1 ;; return 1
    else
      i32.const 0 
      call 0 ;; assert false
      i32.const 1 ;; to satisfy the type checker, this line will never be reached
    end
  )
  (func (;2;) (type 1)
    i32.const 0
    i32.symbolic ;; call it x
    call 1
  )
  (start 2)
  (memory (;0;) 2)
  (export "main" (func 1))
  (global (;0;) (mut i32) (i32.const 42))
)