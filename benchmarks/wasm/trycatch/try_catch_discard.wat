(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32)
    try
      i32.const 1
      call 0
      i32.const 42
      ;; [42]
      throw
      i32.const 3
      call 0
    catch
      ;; [42, resume]
      call 0
      drop
      i32.const 4
      call 0
    end
    i32.const 5
    call 0
    )
  (start 1))
