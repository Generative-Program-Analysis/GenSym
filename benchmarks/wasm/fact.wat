(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (result i32)))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1) (param i32) (result i32)
    (local i32 i32)
    local.get 0
    local.set 1
    i32.const 1
    local.set 2
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        call 0
        local.get 1
        i32.const 0
        i32.eq
        if  ;; label = @3
          br 2 (;@1;)
        else
          local.get 1
          local.get 2
          i32.mul
          local.set 2
          local.get 1
          i32.const 1
          i32.sub
          local.set 1
        end
        br 0 (;@2;)
      end
    end
    local.get 2)
  (func $main (type 2) (result i32)
    i32.const 5
    call 1
  )
  (start 2)
)