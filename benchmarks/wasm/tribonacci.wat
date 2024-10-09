(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    (local i32 i32)
    local.get 0
    i32.const 2
    i32.shl
    i32.const 1048576
    i32.add
    local.set 1
    i32.const 0
    local.set 2
    local.get 0
    local.set 0
    loop (result i32)  ;; label = @1
      local.get 2
      local.set 2
      local.get 1
      local.set 1
      block  ;; label = @2
        local.get 0
        local.tee 0
        i32.const 2
        i32.gt_u
        br_if 0 (;@2;)
        local.get 1
        i32.load
        local.get 2
        i32.add
        return
      end
      local.get 1
      i32.const -12
      i32.add
      local.set 1
      local.get 0
      i32.const -1
      i32.add
      call 0
      local.get 0
      i32.const -2
      i32.add
      call 0
      i32.add
      local.get 2
      i32.add
      local.set 2
      local.get 0
      i32.const -3
      i32.add
      local.set 0
      br 0 (;@1;)
    end)
  (func (;1;) (type 1) (result i32)
    i32.const 12
    call 0)
  (start 1)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 17)
  (global (;0;) (mut i32) (i32.const 1048576))
  (global (;1;) i32 (i32.const 1048588))
  (global (;2;) i32 (i32.const 1048592))
  (export "memory" (memory 0))
  (export "tribonacci" (func 0))
  (export "real_main" (func 1))
  (export "__data_end" (global 1))
  (export "__heap_base" (global 2)))
