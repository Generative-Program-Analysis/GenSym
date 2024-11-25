(module
  (type (;0;) (func (param i32)))
  (type (;1;) (cont 0))
  (type (;2;) (func (result i32)))
  (type (;3;) (func (param i32 i32)))
  (type (;4;) (func))
  (import "spectest" "print_i32" (func (;0;) (type 0)))
  (tag (;0;) (type 2) (result i32))
  (export "_start" (func 4))
  (start 4)
  (elem (;0;) declare func 3)
  (func (;1;) (type 0) (param i32)
    local.get 0
    call 0
    suspend 0
    call 0
  )
  (func (;2;) (type 3) (param i32 i32)
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
  (func (;3;) (type 0) (param i32)
    local.get 0
    i32.const 13
    call 2
  )
  (func (;4;) (type 4)
    (local (ref 1) i32)
    ref.func 3
    cont.new 1
    local.set 0
    i32.const 10
    local.set 1
    block ;; label = @1
      loop ;; label = @2
        block (result (ref 1)) ;; label = @3
          local.get 1
          local.get 0
          resume 1 (on 0 0 (;@3;))
          i32.const -2
          call 0
          br 2 (;@1;)
        end
        local.set 0
        local.get 1
        i32.const 1
        i32.add
        local.set 1
        i32.const -1
        call 0
        br 0 (;@2;)
      end
    end
  )
)
