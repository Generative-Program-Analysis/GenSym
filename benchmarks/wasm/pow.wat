(module $pow.temp
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func $power (type 0) (param i32 i32) (result i32)
    (local i32)
    i32.const 1
    local.set 2
    local.get 1
    local.set 1
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        local.set 2
        local.get 1
        local.tee 1
        i32.eqz
        br_if 1 (;@1;)
        local.get 2
        local.get 0
        i32.mul
        local.set 2
        local.get 1
        i32.const -1
        i32.add
        local.set 1
        br 0 (;@2;)
      end
    end
    local.get 2)
  (func $real_main (type 1) (result i32)
    i32.const 2
    i32.const 10
    call $power)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 16)
  (global $__stack_pointer (mut i32) (i32.const 1048576))
  (global (;1;) i32 (i32.const 1048576))
  (global (;2;) i32 (i32.const 1048576))
  (export "memory" (memory 0))
  (export "power" (func 0))
  (export "real_main" (func 1))
  (export "__data_end" (global 1))
  (export "__heap_base" (global 2)))
