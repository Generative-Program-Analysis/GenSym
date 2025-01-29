;; pushed to meta continuation example
(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32 i32)
    i32.const 0
    local.set 1
    try
      i32.const 1
      call 0
      block
        block
          i32.const 42
          ;; [42]
          throw
        end
        i32.const 6
        call 0
        i32.const 42
        ;; [42]
        throw
      end
      i32.const 3
      call 0
    catch
      ;; increment local 1
      i32.const 1
      local.get 1
      i32.add
      local.set 1
      ;; [42, resume]
      i32.const 2
      call 0
      drop
      local.get 1
      i32.const 1
      i32.eq
      if (param i32 (; input cont actually ;))
        resume0
      else
        i32.const 7
        call 0
      end
      i32.const 4
      call 0
    end
    i32.const 5
    call 0
    )
  (start 1))
