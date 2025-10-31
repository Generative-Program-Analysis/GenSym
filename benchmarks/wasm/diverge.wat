(module $diverge
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32)))
  (import "console" "assert" (func (;0;) (type 2)))
  (import "console" "log" (func (;1;) (type 2)))
  ;; f x = if x == 0 then 42 else f x
  (func (;2;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 42
    else
      local.get 0
      call 1
      local.get 0
      call 2
      i32.const 0
      call 0
      unreachable
    end
  )
  (func $real_main (;2;) (type 1)
    i32.const 0
    i32.symbolic
    call 2
    drop
  )
  (start 3)
  (export "main" (func 3))

)