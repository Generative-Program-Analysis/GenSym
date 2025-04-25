(module $push-drop
  (func $real_main (type 1) (result i32)
    (local i32 i32)
    i32.const 2
    i32.const 2
    local.get 0
    local.get 1
    drop
    drop)
  (start 0))