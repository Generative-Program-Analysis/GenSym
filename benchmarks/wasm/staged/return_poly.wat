(module
  (type (;0;) (func))
  (type (;1;) (func (result i32))) 
  ;; TODO: It seems that our parser or preprocessor has some problems; the result type of the last line doesn't take effect
  (func (result i32)
    block
      i32.const 21
      i32.const 35
      i32.const 42
      return
    end
    i32.const 100
  )
  (func (type 0)
    call 0
    ;; unreachable
  )
  (export "$real_main" (func 1))
)
