(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (cont 0))
  (type (;3;) (cont 1))
  (type (;4;) (func (param i32)))
  (type (;5;) (func (param i32 (ref 2) (ref 3))))
  (type (;6;) (func (param (ref 3) (ref 2))))
  (type (;7;) (func (result i32 (ref 2))))
  (type (;8;) (func (param (ref 2) (ref 3))))
  (type (;9;) (func))
  ;; (import "spectest" "print_i32" (func (;0;) (type 4)))

  (tag (;0;) (type 4) (param i32))
  (tag (;1;) (type 0) (result i32))
  (export "pipe" (func 3))
  (export "run" (func 6))
  (start 7)
  (export "main" (func 7))
  (elem (;0;) declare func 4 5)
  (func (type 4) (param i32)
  )
  
  (func (;1;) (type 5) (param i32 (ref 2) (ref 3))
    block (result (ref 3)) ;; label = @1
      local.get 0
      local.get 2
      resume 3 (on 1 0 (;@1;))
      return
    end
    local.set 2
    local.get 2
    local.get 1
    return_call 2
  )
  (func (;2;) (type 6) (param (ref 3) (ref 2))
    (local i32)
    block (type 7) (result i32 (ref 2)) ;; label = @1
      local.get 1
      resume 2 (on 0 0 (;@1;))
      return
    end
    local.set 1
    local.set 2
    local.get 2
    local.get 1
    local.get 0
    return_call 1
  )
  (func (;3;) (type 8) (param (ref 2) (ref 3))
    i32.const -1
    local.get 0
    local.get 1
    call 1
  )
  (func (;4;) (type 1) (param i32) (result i32)
    loop ;; label = @1
      i32.const -1
      call 0
      local.get 0
      call 0
      local.get 0
      suspend 0
      i32.const 44444
      call 0
      local.get 0
      i32.const 1
      i32.add
      local.set 0
      br 0 (;@1;)
    end
    unreachable
  )
  (func (;5;) (type 1) (param i32) (result i32)
    (local i32 i32)
    i32.const 3
    local.set 1
    i32.const 0
    local.set 2
    loop ;; label = @1
      local.get 2
      suspend 1
      i32.const 55555
      call 0
      i32.add
      local.set 2
      i32.const -2
      call 0
      local.get 2
      call 0
      local.get 1
      i32.const 1
      i32.sub
      local.set 1
      local.get 1
      i32.const 0
      i32.ne
      br_if 0 (;@1;)
    end
    local.get 2
    return
  )
  (func (;6;) (type 4) (param i32)
    local.get 0
    ref.func 4
    cont.new 3
    cont.bind 3 2
    ref.func 5
    cont.new 3
    call 3
  )
  (func (;7;) (type 9)
    i32.const 1
    call 6
  )
)