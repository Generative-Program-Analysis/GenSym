(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "assert" (func (;0;) (type 0)))

  (func (;1;) (type 1)
    (local f32 f32 f64 f64)

    f32.const 3.5
    local.set 0
    f32.const 2.0
    local.set 1

    f64.const 10.25
    local.set 2
    f64.const 0.5
    local.set 3

    ;; f32 arithmetic
    local.get 0
    local.get 1
    f32.add
    f32.const 5.5
    f32.eq
    call 0

    local.get 0
    local.get 1
    f32.sub
    f32.const 1.5
    f32.eq
    call 0

    local.get 0
    local.get 1
    f32.mul
    f32.const 7.0
    f32.eq
    call 0

    local.get 0
    local.get 1
    f32.div
    f32.const 1.75
    f32.eq
    call 0

    ;; f32 comparisons
    local.get 0
    local.get 1
    f32.eq
    i32.eqz
    call 0

    local.get 0
    local.get 1
    f32.ne
    call 0

    local.get 0
    local.get 1
    f32.gt
    call 0

    local.get 0
    local.get 1
    f32.ge
    call 0

    local.get 0
    local.get 1
    f32.lt
    i32.eqz
    call 0

    local.get 0
    local.get 1
    f32.le
    i32.eqz
    call 0

    ;; f64 arithmetic
    local.get 2
    local.get 3
    f64.add
    f64.const 10.75
    f64.eq
    call 0

    local.get 2
    local.get 3
    f64.sub
    f64.const 9.75
    f64.eq
    call 0

    local.get 2
    local.get 3
    f64.mul
    f64.const 5.125
    f64.eq
    call 0

    local.get 2
    local.get 3
    f64.div
    f64.const 20.5
    f64.eq
    call 0

    ;; f64 comparisons
    local.get 2
    local.get 3
    f64.eq
    i32.eqz
    call 0

    local.get 2
    local.get 3
    f64.ne
    call 0

    local.get 2
    local.get 3
    f64.gt
    call 0

    local.get 2
    local.get 3
    f64.ge
    call 0

    local.get 2
    local.get 3
    f64.lt
    i32.eqz
    call 0

    local.get 2
    local.get 3
    f64.le
    i32.eqz
    call 0
  )

  (export "test_fp" (func 1))
)
