(module
  (type (;0;) (func))
  (type (;1;) (func (param i32)))
  (import "console" "assert" (func (type 1)))
  (func (;1;) (type 0)
    (local i32 i32)
    i32.const 1
    local.set 0
    i32.const 0
    local.set 1
    block
      loop
        local.get 0
        i32.const 10000
        i32.ge_s
        br_if 1
        local.get 1
        i32.const 0
        i32.add
        local.set 1
        local.get 0
        i32.const 1
        i32.add
        local.set 0
        br 0
      end
    end
    i32.const 10000
    local.set 0
    i32.const 0
    local.set 1
    block
      loop
      local.get 0
      i32.eqz
      br_if 1        ;; break if counter == 0
      local.get 1
      i32.const 0
      i32.sub        ;; acc - 0 (no change)
      local.set 1
      local.get 0
      i32.const 1
      i32.sub
      local.set 0    ;; counter--
      br 0           ;; repeat loop
      end
    end
    local.get 1
    if
      i32.const 0
      call 0
    end
  )
  (start 1))
