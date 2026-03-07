(module
  (type (;0;) (func (result f32 f32 f32 f32 i32)))
  (func (;0;) (type 0) (result f32 f32 f32 f32 i32)
    (local f32 f32)
    f32.const 0x1.cp+1 (;=3.5;)
    local.set 0
    f32.const 0x1p+1 (;=2;)
    local.set 1
    local.get 0
    local.get 1
    f32.add
    local.get 0
    local.get 1
    f32.sub
    local.get 0
    local.get 1
    f32.mul
    local.get 0
    local.get 1
    f32.div
    local.get 0
    local.get 1
    f32.gt)
  (export "test_f32" (func 0)))
