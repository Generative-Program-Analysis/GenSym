(module $push-drop
  (global (;0;) (mut i32) (i32.const 1048576))
  (func (;0;) (type 1) (result i32)
    i32.const 2
    (block
     (block
      i32.const 1
      br_table 0 1 0 ;; br_table will consume an element from the stack
     )
    )
  )
  (start 0))
