(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    (local i32)
    i32.const 0
    local.set 1
    local.get 0
    local.set 0
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        local.set 1
        block  ;; label = @3
          block  ;; label = @4
            block  ;; label = @5
              local.get 0
              local.tee 0
              br_table 0 (;@5;) 1 (;@4;) 2 (;@3;)
            end
            local.get 0
            local.set 0
            br 3 (;@1;)
          end
          i32.const 1
          local.set 0
          br 2 (;@1;)
        end
        local.get 1
        local.get 0
        i32.const -1
        i32.add
        call 0
        i32.add
        local.set 1
        local.get 0
        i32.const -2
        i32.add
        local.set 0
        br 0 (;@2;)
      end
    end
    local.get 1
    local.get 0
    i32.add)
  (func (;1;) (type 1) (result i32)
    i32.const 12
    call 0)
  (start 1)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 16)
  (global (;0;) (mut i32) (i32.const 1048576))
  (global (;1;) i32 (i32.const 1048576))
  (global (;2;) i32 (i32.const 1048576))
  (export "memory" (memory 0))
  (export "fibonacci" (func 0))
  (export "real_main" (func 1))
  (export "__data_end" (global 1))
  (export "__heap_base" (global 2)))
