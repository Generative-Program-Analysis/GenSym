(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  
  (func (;0;) (type 0) (result i32)
    i32.const 42
    global.set 0
    global.get 0
  )
  (func (;1;) (type 1)
    call 0
    ;; should be 42
    ;; drop
    )
  (start 1)
  (memory (;0;) 2)
  (export "main" (func 1))
  (global (;0;) (mut i32) (i32.const 0))
)