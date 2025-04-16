(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (func (;0;) (type 0) (param i32 i32) (result i32)
    local.get 0
    i32.const 0
    i32.le_s
    if (result i32)  ;; label = @1
      i32.const 1
    else
      local.get 1
      i32.const 0
      i32.le_s
    end
    if (result i32)  ;; label = @1
      i32.const -1
    else
      local.get 0
      local.get 0
      i32.mul
      local.get 1
      local.get 1
      i32.mul
      i32.add
      i32.const 25
      i32.eq
      if (result i32)  ;; label = @2
        i32.const 1
      else
        i32.const 0
        call 2
      end
    end
  )
  (export "f" (func 0))
  (func $real_main
    ;; TODO: is there a better way to put symbolic values on the stack?
    i32.const 0
    i32.symbolic
    i32.const 1
    i32.symbolic
    call 0
  )
  (import "console" "assert" (func (type 1)))
  (export "real_main" (func 1))
)
