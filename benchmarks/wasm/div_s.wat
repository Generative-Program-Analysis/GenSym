(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (func (;0;) (type 0) (result i32)
    i32.const 12
    i32.const -5

    i32.div_s ;; divide one number by the other
  )
  (func (;1;) (type 1)
    call 0
    ;; drop
    )
  (start 1)
  (memory (;0;) 2)
  (export "main" (func 1))
)
