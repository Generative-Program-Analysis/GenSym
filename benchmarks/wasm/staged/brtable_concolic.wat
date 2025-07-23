(module $brtable
  (global (;0;) (mut i32) (i32.const 1048576))
  (type (;0;) (func (param i32)))
  (func (;0;) (type 1) (result i32)
    i32.const 2
    (block
      (block
        (block
          i32.const 0
          i32.symbolic
          br_table 0 1 2 0 ;; br_table will consume an element from the stack
        )
        i32.const 1
        call 1
        br 1
      )
      i32.const 0
      call 1
    )
  )
  (import "console" "assert" (func (type 0)))
  (start 0))
