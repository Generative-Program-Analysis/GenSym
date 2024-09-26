(module
  (type (;0;) (func))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32 i32) (result i32)))
  (func $__wasm_call_ctors (type 0))
  (func $sym_int (type 1) (param i32) (result i32)
    (local i32 i32 i32 i32 i32 i32 i32 i32)
    global.get 0
    local.set 1
    i32.const 16
    local.set 2
    local.get 1
    local.get 2
    i32.sub
    local.set 3
    local.get 3
    local.get 0
    i32.store offset=12
    i32.const 0
    local.set 4
    local.get 4
    i32.load offset=1028
    local.set 5
    i32.const 1
    local.set 6
    local.get 5
    local.get 6
    i32.add
    local.set 7
    i32.const 0
    local.set 8
    local.get 8
    local.get 7
    i32.store offset=1028
    local.get 5
    return)
  (func $assert (type 2) (param i32)
    (local i32 i32 i32)
    global.get 0
    local.set 1
    i32.const 16
    local.set 2
    local.get 1
    local.get 2
    i32.sub
    local.set 3
    local.get 3
    local.get 0
    i32.store offset=12
    return)
  (func $__original_main (type 3) (result i32)
    (local i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32)
    global.get 0
    local.set 0
    i32.const 16
    local.set 1
    local.get 0
    local.get 1
    i32.sub
    local.set 2
    local.get 2
    global.set 0
    i32.const 1026
    local.set 3
    i32.const 1024
    local.set 4
    i32.const 0
    local.set 5
    local.get 2
    local.get 5
    i32.store offset=12
    local.get 4
    i32.symbolic
    local.set 6
    local.get 2
    local.get 6
    i32.store offset=8
    local.get 3
    i32.symbolic
    local.set 7
    local.get 2
    local.get 7
    i32.store offset=4
    local.get 2
    i32.load offset=8
    local.set 8
    local.get 2
    i32.load offset=4
    local.set 9
    local.get 8
    local.set 10
    local.get 9
    local.set 11
    local.get 10
    local.get 11
    i32.eq
    local.set 12
    i32.const 1
    local.set 13
    local.get 12
    local.get 13
    i32.and
    local.set 14
    block  ;; label = @1
      block  ;; label = @2
        local.get 14
        i32.eqz
        br_if 0 (;@2;)
        i32.const 1
        local.set 15
        local.get 2
        local.get 15
        i32.store offset=12
        br 1 (;@1;)
      end
      i32.const 0
      local.set 16
      local.get 2
      local.get 16
      i32.store offset=12
    end
    local.get 2
    i32.load offset=12
    local.set 17
    i32.const 16
    local.set 18
    local.get 2
    local.get 18
    i32.add
    local.set 19
    local.get 19
    global.set 0
    local.get 17
    return)
  (func $main (type 4) (param i32 i32) (result i32)
    (local i32)
    call $__original_main
    local.set 2
    local.get 2
    return)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66576))
  (global (;1;) i32 (i32.const 1024))
  (global (;2;) i32 (i32.const 1032))
  (global (;3;) i32 (i32.const 1024))
  (global (;4;) i32 (i32.const 66576))
  (global (;5;) i32 (i32.const 0))
  (global (;6;) i32 (i32.const 1))
  (export "memory" (memory 0))
  (export "__wasm_call_ctors" (func $__wasm_call_ctors))
  (export "sym_int" (func $sym_int))
  (export "assert" (func $assert))
  (export "__original_main" (func $__original_main))
  (export "main" (func $main))
  (export "__dso_handle" (global 1))
  (export "__data_end" (global 2))
  (export "__global_base" (global 3))
  (export "__heap_base" (global 4))
  (export "__memory_base" (global 5))
  (export "__table_base" (global 6))
  (data (;0;) (i32.const 1024) "a\00b\00"))
