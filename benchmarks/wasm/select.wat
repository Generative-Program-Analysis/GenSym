(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "assert" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    i32.const 11
    i32.const 22
    i32.const 1
    select
    i32.const 11
    i32.eq
    call 0
    i64.const 33
    i64.const 44
    i32.const 0
    select
    i64.const 44
    i64.eq
    call 0
    f32.const 0x1.6p+2 (;=5.5;)
    f32.const 0x1.ap+2 (;=6.5;)
    i32.const 1
    select
    f32.const 0x1.6p+2 (;=5.5;)
    f32.eq
    call 0
    f64.const 0x1.dp+2 (;=7.25;)
    f64.const 0x1.08p+3 (;=8.25;)
    i32.const 0
    select
    f64.const 0x1.08p+3 (;=8.25;)
    f64.eq
    call 0)
  (export "real_main" (func 1)))
