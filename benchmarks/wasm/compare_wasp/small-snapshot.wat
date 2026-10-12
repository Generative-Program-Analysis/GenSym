(module
  (type (;0;) (func (param i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32) (result i32)))
  (type (;1;) (func (result i32)))
  (import "spectest" "print_i32" (func (;0;) (param i32)))

  (func (;1;) (type 0) (result i32)
    i32.const 0
    i32.symbolic
    call 2
    if (result i32)
      i32.const 42
    else
      i32.const 19
    end)

  (func (;2;) (param i32) (result i32)
    (local i32) 
    i32.const 11140
    local.set 1
    loop
      i32.const 3
      drop
      local.get 1
      i32.const 1
      i32.sub
      local.tee 1
      br_if 0
    end
    local.get 0)
  (export "f" (func 2))
  (export "main" (func 1))
  (start 1))

