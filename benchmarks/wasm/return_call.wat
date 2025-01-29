(module
  (type (;0;) (func (param i64) (result i32)))
  (type (;1;) (func))
  (func (;0;) (type 0) (param i64) (result i32)
    local.get 0
    i64.eqz
    if (result i32) ;; label = @1
      i32.const 44
    else
      local.get 0
      i64.const 1
      i64.sub
      return_call 1
    end
  )
  (func (;1;) (type 0) (param i64) (result i32)
    local.get 0
    i64.eqz
    if (result i32) ;; label = @1
      i32.const 99
    else
      local.get 0
      i64.const 1
      i64.sub
      return_call 0
    end
  )
  (func (;2;) (type 1)
    i64.const 100000
    call 0
  )
  (start 2)
)