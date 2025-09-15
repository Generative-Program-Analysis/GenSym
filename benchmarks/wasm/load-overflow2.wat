(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (func (;0;) (type 0) (result i32)
    i32.const 0
    i32.const 65536
    i32.store 
    i32.const 2
    i32.load
  )
  (func (;1;) (type 1)
    call 0
    ;; should be 1
    ;; drop
  )
  (start 1)
  (memory (;0;) 2)
  (export "main" (func 1))
)