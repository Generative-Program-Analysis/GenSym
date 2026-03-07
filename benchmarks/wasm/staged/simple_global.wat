(module $simple_global
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (result i32)))
  (type (;2;) (func (param i32)))
  (func $real_main (type 1) (result i32)
    (local i32)
    i32.const 0
    i32.symbolic
    local.tee 0
    local.get 0
    global.set 0
    if
    else
      i32.const 0
      call 1
    end)
  (import "console" "assert" (func (type 2)))
  (memory (;0;) 16)
  (global $__stack_pointer (mut i32) (i32.const 1048576))
  (global (;1;) i32 (i32.const 1048576))
  (global (;2;) i32 (i32.const 1048576))
  (export "real_main" (func 0)))
