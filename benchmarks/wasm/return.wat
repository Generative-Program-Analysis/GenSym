(module
  (func (result i32)
    block
      i32.const 42
      return
    end
  )
  (func (result i32)
    call 0
    unreachable
  )
  (start 1)
)
