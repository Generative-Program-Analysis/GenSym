;; this file contains some wasm instructions to test if the compiler works,
;; and its execution is meaningless.
(module $push-drop
  (global (;0;) (mut i32) (i32.const 1048576))
  (func (;0;) (type 1) (result i32)
    (local i32 i32)
    i32.const 2
    i32.const 2
    local.get 0
    local.get 1
    local.set 0
    local.tee 1
    drop
    drop
    i32.add
    nop
    (call 1)
    global.get 1
    i32.const 3
    global.set 2 ;; TODO: this line was compiled to global.get, fix the parser!

    if (result i32)  ;; label = @1
      i32.const 1
    else
      local.get 1
    end
    (block
      (block
        i32.const 4
        i32.const 2
        br_table 0 1 0 ;; the compilation of br_table is problematic now
      )
    )

    (loop
      i32.const 5
      br 0)
    return
    i32.const 6
  )
  (func (;1;) (type 1) (param i32 i32) (result i32)
    (local i32 i32)
    local.get 0
    local.get 1)
  (start 0))