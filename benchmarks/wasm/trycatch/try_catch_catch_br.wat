(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32)
    try
      i32.const 1
      call 0
      block
        i32.const 42
        ;; [42]
        throw
        br 0
        i32.const 3
        call 0
      end
      i32.const 6
      call 0
    catch
      ;; [42, resume]
      drop
      local.set 0        ;; abusing the type system
      local.get 0        ;;
      block (param i32)  ;;
        i32.const 2
        call 0
        resume0
        br 0
      end
      i32.const 4
      call 0
      local.get 0
      resume0
    end
    i32.const 5
    call 0
  )
  (start 1))
