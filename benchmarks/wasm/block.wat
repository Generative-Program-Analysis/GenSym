(module
  (func $test_block (param i32 i32 i32) (result i32)
    local.get 0
    local.get 1
    local.get 2
    block (param i32 i32 i32) (result i32 i32)
      i32.add
    end
    i32.add
  )
  (func $real_main (result i32)
    i32.const 1
    i32.const 3
    i32.const 5
    call $test_block
  )
  ;; Sum from [0, 10]
  (func $test_loop_input (result i32)
    (local i32)
    i32.const 10
    local.set 0
    i32.const 0
    loop (param i32) (result i32)
      local.get 0
      i32.add
      local.get 0
      i32.const 1
      i32.sub
      local.set 0
      i32.const 0
      local.get 0
      i32.ne
      br_if 0
    end
  )
  (func $test_if_input (result i32)
    i32.const 10
    i32.const 5
    i32.const 1
    if (param i32 i32) (result i32 i32)
      i32.const 10
      i32.add
      else
    end
    i32.add
  )
  (func $test_poly_br (result i32)
    i32.const -30
    i32.const 0 ;; unused
    i32.const 0 ;; unused
    i32.const 0 ;; unused
    block (param i32 i32 i32) (result i32 i32)
      i32.const 0 ;; truncated
      i32.const 10000 ;; truncated
      i32.const 10
      i32.const 20
      br 0
      i32.add
    end
    i32.add
    i32.add ;; add value -30 and 30
    ;; i32.add
    ;; We can't use i32.add instruction here, because the overflowed value has been truncted
    ;; when block exited.
  )
  (export "real_main" (func 1))
  (export "test_loop_input" (func 2))
  (export "test_if_input" (func 3))
  (export "test_poly_br" (func 4))
)
