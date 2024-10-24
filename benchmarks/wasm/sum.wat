(module
  (type (;0;) (func (param i32)))
  (import "console" "log" (func (type 0)))
  (func (param i32 i32) (result i32)
    local.get 0
    i32.eqz
    if (result i32)
      local.get 1
    else
      local.get 0
      i32.const 1
      i32.sub
      local.get 1
      local.get 0
      i32.add
      (return_call 1)
    end
  )
  (func (result i32)
    i32.const 10
    i32.const 0
    (return_call 1))
  (export "sum10" (func 2))
)