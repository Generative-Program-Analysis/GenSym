(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (param i32 i32) (result i32)))
  (type (;2;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 1
    i32.add)
  (func (;1;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 2
    i32.mul)
  (func (;2;) (type 1) (param i32 i32) (result i32)
    local.get 1
    local.get 0
    call_indirect (type 0))
  (func (;3;) (type 2) (result i32)
    i32.const 0
    i32.const 41
    call 2)
  (table (;0;) 2 funcref)
  (export "run" (func 3))
  (export "dispatch" (func 2))
  (elem (;0;) (i32.const 0) func 0 1)
  (start 3))
