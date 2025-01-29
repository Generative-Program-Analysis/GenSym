(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func))
  (func (;0;) (type 0) (param i32) (result i32)
    local.get 0
    i32.eqz
    if (result i32) ;; label = @1
      local.get 0
    else
      local.get 0
      i32.const 1
      i32.sub
      return_call 0
    end
  )
  (func (;1;) (type 1)
    ;; TODO: now setting it to 100K will result in stack overflow
    i32.const 10000 ;; it will not terminate when it's 1mil
    ;; TODO: this doesn't seem like an error in our semantics
    ;; but something about sbt. But why?
    call 0
  )
  (start 1)
)