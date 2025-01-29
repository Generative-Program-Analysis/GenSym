(module
  (type (;0;) (func))
  (type (;1;) (cont 0))
  (type (;2;) (func))
  (type (;3;) (cont 2))
  (type (;4;) (func (param i32)))
  (import "spectest" "print_i32" (func (;0;) (type 4)))
  (tag (;0;) (type 0))
  (export "main" (func 3))
  (start 3)
  (elem (;0;) declare func 1)
  (elem (;1;) declare func 2)

  (func (;1;) (type 0)
    i32.const 111
    call 0 ;; output buffer [0, 111]
    suspend 0
    i32.const 333 ;; output buffer [0, 111, 222, 333]
    call 0
  )

  (func (;2;) (type 0)
    i32.const 0
    call 0 ;; output buffer: [0]
    ref.func 1
    cont.new 1
    resume 1
    i32.const 444 ;; output buffer [0, 111, 222, 333, 444]
    call 0
    return
  )

  (func (;3;) (type 0)
    block (result (ref null 3)) ;; label = @1
      ref.func 2
      cont.new 1
      resume 1 (on 0 0 (;@1;))
      unreachable
    end
    i32.const 222
    call 0 ;; output buffer [0, 111, 222]
    resume 3
    i32.const 555 ;; output buffer [0, 111, 222, 333, 444, 555]
    call 0
  )
)
