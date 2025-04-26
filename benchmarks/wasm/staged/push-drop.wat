(module $push-drop
  (func (;0;) (type 1) (result i32)
    (local i32 i32)
    i32.const 2
    i32.const 2
    local.get 0
    local.get 1
    drop
    drop
    (call 1))
  (func (;1;) (type 1) (param i32 i32) (result i32)
    (local i32 i32)
    local.get 0
    local.get 1)
  (start 0))