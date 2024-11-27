(module
  (type (;0;) (func (param i32)))
  (type (;1;) (cont 0))
  (type (;2;) (func (result i32)))
  (type (;3;) (func))
  (import "spectest" "print_i32" (func (;0;) (type 0)))
  (tag (;0;) (type 2) (result i32))
  (export "_start" (func 2))
  (start 2)
  (elem (;0;) declare func 1)
  (func (;1;) (type 0) (param i32)
    local.get 0
    call 0
    suspend 0
    call 0
  )
  (func (;2;) (type 3)
    (local i32 (ref 1))
    ref.func 1
    cont.new 1
    local.set 1
    i32.const 10
    local.set 0
    block ;; label = @1
      block (result (ref 1)) ;; label = @2
        local.get 0
        local.get 1
        resume 1 (on 0 0 (;@2;))
        i32.const -2
        call 0
        br 1 (;@1;)
      end
      local.set 1
      local.get 0
      i32.const 1
      i32.add
      local.set 0
      block ;; label = @2
        block (result (ref 1)) ;; label = @3
          local.get 0
          local.get 1
          resume 1 (on 0 0 (;@3;))
          i32.const 42
          call 0
          br 1 (;@2;)
        end
        i32.const 111
        call 0
        drop
      end
    end
  )
)
