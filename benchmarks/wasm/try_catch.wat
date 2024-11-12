(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32)
    try
      i32.const 1
      call 0
      throw
      i32.const 2
      call 0
    catch
      i32.const 3
      call 0
    end)
  (start 1))
