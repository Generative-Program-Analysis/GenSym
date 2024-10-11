(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    local.get 0
    if (result i32)  ;; label = @1
      local.get 0
      i32.const 1
      i32.eq
      if (result i32)  ;; label = @2
        i32.const 1
      else
        local.get 0
        i32.const 2
        i32.eq
        if (result i32)  ;; label = @3
          i32.const 1
        else
          local.get 0
          i32.const 1
          i32.sub
          call 0
          local.get 0
          i32.const 2
          i32.sub
          call 0
          i32.add
          local.get 0
          i32.const 3
          i32.sub
          call 0
          i32.add
        end
      end
    else
      i32.const 0
    end)
  (func (;1;) (type 1) (result i32)
    i32.const 12
    call 0)
  (start 1)
  (memory (;0;) 0)
  (export "memory" (memory 0)))
