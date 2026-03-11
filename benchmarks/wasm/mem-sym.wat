(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (param i32)))
  (import "console" "assert" (func (type 3)))
  (func (;1;) (type 2) (param i32) (result i32)
    i32.const 0
    local.get 0
    i32.store
    i32.const 0
    i32.load
    i32.const 25
    i32.eq
    if  ;; if x == 25
      i32.const 0
      call 0 ;; assert false
    end
    i32.const 1
    i32.load
    i32.const 1
    i32.eq
    if ;; if x >> 8 == 1
      i32.const 0
      call 0 ;; assert false
    end
    i32.const 4
    i64.load
    i64.eqz
    i32.eqz
    if
      i32.const 0
      call 0 ;; assert false
    end
    i32.const 0
    i64.load
    i64.const 32
    i64.shr_u
    i64.eqz
    i32.eqz
    if
      i32.const 0
      call 0 ;; assert false
    end
    i32.const 8
    i64.const 0x0102030405060708
    i64.store
    i32.const 8
    i64.load
    i64.const 0x0102030405060708
    i64.ne
    if
      i32.const 0
      call 0 ;; assert false
    end
    i32.const 1
  )
  (func (;2;) (type 1)
    i32.const 0
    i32.symbolic ;; call it x
    call 1
  )
  (start 2)
  (memory (;0;) 2)
  (export "main" (func 1))
  (global (;0;) (mut i32) (i32.const 42))
)
