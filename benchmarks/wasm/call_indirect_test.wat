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
  (func (;2;) (type 0) (param i32) (result i32)
    local.get 0
    i32.const 3
    i32.sub)
  (func (;3;) (type 1) (param i32 i32) (result i32)
    local.get 1
    local.get 0
    call_indirect (type 0))
  (func (;4;) (type 2) (result i32)
    ;; (1 + 10) + (2 * 10) + (10 - 3) = 38
    i32.const 0
    i32.const 10
    call 3
    i32.const 1
    i32.const 10
    call 3
    i32.add
    i32.const 2
    i32.const 10
    call 3
    i32.add)
  (table (;0;) 3 funcref)
  (export "run" (func 4))
  (export "dispatch" (func 3))
  (elem (;0;) (i32.const 0) func 0 1 2)
  (start 4))
