(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func (param i32 i32)))
  (type (;2;) (func (param (ref 0) (ref 0))))
  (type (;3;) (func))
  (import "spectest" "print_i32" (func (;0;) (type 0)))
  (export "_start" (func 6))
  (start 6)
  (elem (;0;) declare func 4)
  (elem (;1;) declare func 5)
  (func (;1;) (type 0) (param i32)
    local.get 0
    call 0
  )
  (func (;2;) (type 1) (param i32 i32)
    (local i32)
    local.get 0
    local.set 2
    block ;; label = @1
      loop ;; label = @2
        local.get 2
        local.get 1
        i32.gt_u
        br_if 1 (;@1;)
        local.get 2
        call 1
        local.get 2
        i32.const 1
        i32.add
        local.set 2
        br 0 (;@2;)
      end
    end
  )
  (func (;3;) (type 2) (param (ref 0) (ref 0))
    i32.const 10
    local.get 0
    call_ref 0
    i32.const 20
    local.get 1
    call_ref 0
  )
  (func (;4;) (type 0) (param i32)
    local.get 0
    i32.const 13
    call 2
  )
  (func (;5;) (type 0) (param i32)
    local.get 0
    i32.const 23
    call 2
  )
  (func (;6;) (type 3)
    ref.func 4
    ref.func 5
    call 3
  )
)
