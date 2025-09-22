(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32) (result i32)))
  
  (func (;0;) (type 2) (param i32) (result i32)
    i32.const 0
    i32.const 1
    i32.store
    i32.const 0
    local.get 0
    i32.store 
    i32.const 0
    i32.load
  )
  (func (;1;) (type 1)
    i32.const 0
    i32.symbolic
    call 0
    )
  (start 1)
  (memory (;0;) 2)
  (export "main" (func 1))
  (global (;0;) (mut i32) (i32.const 42))
)