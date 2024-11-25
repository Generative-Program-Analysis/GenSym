(module
  (type (;0;) (func))
  (type (;1;) (cont 0))
  (type (;2;) (func (param i32)))
  (type (;3;) (func (result i32 (ref 1))))
  (import "spectest" "print_i32" (func (;0;) (type 2)))
  (tag (;0;) (type 2) (param i32))
  (start 2)
  (elem (;0;) declare func 1)
  (func (;1;) (type 0)
    (local i32)
    i32.const 100
    local.set 0
    loop ;; label = @1
      local.get 0
      suspend 0
      local.get 0
      i32.const 1
      i32.sub
      local.tee 0
      br_if 0 (;@1;)
    end
  )
  (func (;2;) (type 0)
    (local (ref 1))
    ref.func 1
    cont.new 1
    local.set 0
    loop ;; label = @1
      block (result i32 (ref 1)) ;; label = @2
        local.get 0
        resume 1 (on 0 0 (;@2;)) ;; wasmfx ref interpreter has a bug on this, you can add a bracket around `resume ..` to get around
        i32.const 42 
        call 0
        br 2 (;@2;)
      end
      local.set 0
      call 0
      br 0 (;@1;)
    end
  )
)
