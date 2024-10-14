(module
  (type (;0;) (func))
  (type (;1;) (func (result i32)))
  (func (type 1)
    block
      i32.const 42
      return
    end
    i32.const 100
  )
  (func (type 0)
    call 0
    unreachable
  )
  (export "$real_main" (func 1))
)
