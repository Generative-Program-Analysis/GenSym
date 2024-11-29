(module
  (type (;0;) (func))
  (type (;1;) (cont 0))
  (type (;2;) (func (param i32)))
  (import "spectest" "print_i32" (func (;0;) (type 2)))
  (tag (;0;) (type 0))
  (tag (;1;) (type 0))
  (export "_start" (func 3))
  (start 3)
  (elem (;0;) declare func 1 2)
  (func (;1;) (type 0)
    suspend 0
    suspend 1
  )
  (func (;2;) (type 0)
    block ;; label = @1
      block (result (ref 1)) ;; label = @2
        ref.func 1
        cont.new 1
        resume 1 (on 0 0 (;@2;))
        call 0 
        br 1 (;@1;)
      end
      i32.const 0
      call 0
      resume 1
    end
  )
  (func (;3;) (type 0)
    block ;; label = @1
      block (result (ref 1)) ;; label = @2
        ref.func 2
        cont.new 1
        resume 1 (on 1 0 (;@2;))
        br 1 (;@1;)
      end
      drop
      i32.const 1
      call 0
    end
  )
)
