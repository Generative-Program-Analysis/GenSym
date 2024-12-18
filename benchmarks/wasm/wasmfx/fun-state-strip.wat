(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (type (;2;) (cont 0))
  (type (;3;) (cont 1))
  (type (;4;) (func (param i32)))
  (type (;5;) (func (param (ref 2) i32) (result i32)))
  (type (;6;) (func (result i32 (ref 3))))
  (type (;7;) (func (param i32 (ref 3)) (result i32)))
  (tag (;0;) (type 1) (result i32))
  (tag (;1;) (type 4) (param i32))
  (export "main" (func 3))
  (start 3)
  (elem (;0;) declare func 2)
  (func (;0;) (type 5) (param (ref 2) i32) (result i32)
    block (result (ref 2)) ;; label = @1
      block (type 6) (result i32 (ref 3)) ;; label = @2
        local.get 1
        local.get 0
        resume 2 (on 0 1 (;@1;)) (on 1 0 (;@2;))
        return
      end
      return_call 1
    end
    local.get 1
    return_call 0
  )
  (func (;1;) (type 7) (param i32 (ref 3)) (result i32)
    block (result (ref 2)) ;; label = @1
      block (type 6) (result i32 (ref 3)) ;; label = @2
        local.get 1
        resume 3 (on 0 1 (;@1;)) (on 1 0 (;@2;))
        return
      end
      return_call 1
    end
    local.get 0
    return_call 0
  )
  (func (;2;) (type 1) (result i32)
    i32.const 7
    suspend 1
    suspend 0
    i32.const 2
    i32.const 3
    suspend 1
    i32.const 3
    suspend 0
    i32.add
    i32.mul
    i32.add
  )
  (func (;3;) (type 1) (result i32)
    i32.const 0
    ref.func 2
    cont.new 3
    call 1
  )
)
