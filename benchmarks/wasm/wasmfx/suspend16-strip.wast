(module
  (type (;0;) (func (result i32)))
  (type (;1;) (cont 0))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (cont 2))
  (type (;4;) (func))
  (type (;5;) (func (param i32)))
  (import "spectest" "print_i32" (func (;0;) (type 5)))
  (tag (;0;) (type 4))
  (tag (;1;) (type 0) (result i32))
  (start 4)
  (elem (;0;) declare func 1)
  (elem (;1;) declare func 2)
  (func (;1;) (type 0) (result i32)
    suspend 1
    i32.const 111
    call 0
    i32.const 78
  )
  (func (;2;) (type 0) (result i32)
    ref.func 1
    cont.new 1
    resume 1
    i32.const 100
    call 0
    i32.const 10
    i32.add
    return
  )
  (func (;3;) (type 0) (result i32)
    i32.const 78
    block (result (ref null 3)) ;; label = @1
      ref.func 2
      cont.new 1
      resume 1 (on 1 0 (;@1;))
      i32.const 404
      return
    end
    resume 3
  )
  (func
    call 3
    call 0
  )
)
