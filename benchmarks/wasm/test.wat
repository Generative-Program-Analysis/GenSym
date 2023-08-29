(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (result i32)))
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
  (func $ack (type 0) (param i32 i32) (result i32)
    local.get 0
    local.set 0
    local.get 1
    local.set 1
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        local.set 1
        local.get 0
        local.tee 0
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          block  ;; label = @4
            local.get 1
            br_if 0 (;@4;)
            i32.const 1
            local.set 1
            br 1 (;@3;)
          end
          local.get 0
          local.get 1
          i32.const -1
          i32.add
          call $ack
          local.set 1
        end
        local.get 0
        i32.const -1
        i32.add
        local.set 0
        local.get 1
        local.set 1
        br 0 (;@2;)
      end
    end
    local.get 1
    i32.const 1
    i32.add)
  (func $opaque (type 1) (param i32) (result i32)
    local.get 0
    i32.const 1
    i32.add)
  (func $opaque1 (type 1) (param i32) (result i32)
    local.get 0
    i32.const 5
    i32.add)
  (func $opaque2 (type 1) (param i32) (result i32)
    local.get 0
    i32.const 100
    i32.add)
  (func $conditional (type 1) (param i32) (result i32)
    block  ;; label = @1
      local.get 0
      br_if 0 (;@1;)
      i32.const 10
      return
    end
    local.get 0
    call $opaque)
  (func $real_main (type 2) (result i32)
    i32.const 2
    i32.const 8
    call $ack)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 16)
  (global $__stack_pointer (mut i32) (i32.const 1048576))
  (global (;1;) i32 (i32.const 1048576))
  (global (;2;) i32 (i32.const 1048576))
  (export "memory" (memory 0))
  (export "power" (func $power))
  (export "ack" (func $ack))
  (export "opaque" (func $opaque))
  (export "opaque1" (func $opaque1))
  (export "opaque2" (func $opaque2))
  (export "conditional" (func $conditional))
  (export "real_main" (func $real_main))
  (export "__data_end" (global 1))
  (export "__heap_base" (global 2)))
