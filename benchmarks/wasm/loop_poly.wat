(module
  (type (;0;) (func))
  (type (;1;) (func (param i32) (result i32)))
  (func $test_poly_loop (;0;) (type 0)
    i32.const 42
    i32.const 0
    block (param i32 i32) (result i32 i32)
      loop (type 1) (param i32) (result i32) ;; label = @1
        i32.const 1
        i32.const 2
        br 1 (;@1;)
      end
    end
    ;; this is not a valid wasm program due to the mismatch of stack shape,
    ;; we only do this for testing purposes.
    ;; i32.add
    ;; drop
  )
  (start 0)
)