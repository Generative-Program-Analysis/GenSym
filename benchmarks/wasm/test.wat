(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func $power (type 0) (param i32 i32) (result i32)
    (local i32)
    i32.const 1
    local.set 2
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
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
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          local.get 1
          br_if 0 (;@3;)
          i32.const 1
          local.set 1
          local.get 0
          i32.const -1
          i32.add
          local.set 0
          br 1 (;@2;)
        end
        local.get 0
        local.get 1
        i32.const -1
        i32.add
        call $ack
        local.set 1
        local.get 0
        i32.const -1
        i32.add
        local.set 0
        br 0 (;@2;)
      end
    end
    local.get 1
    i32.const 1
    i32.add)
  (func $real_main (type 1) (result i32)
    i32.const 2
    i32.const 8
    call $ack)
  )
