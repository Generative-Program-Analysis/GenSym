(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "assert" (func (;0;) (type 0)))

  (func (;1;) (type 1)
    (local i64 i64 i32)
    i64.const 42
    local.set 0
    i64.const 10
    local.set 1
    i32.const 3
    local.set 2

    ;; arithmetic
    local.get 0
    local.get 1
    i64.add
    i64.const 52
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.sub
    i64.const 32
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.mul
    i64.const 420
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.div_s
    i64.const 4
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.rem_u
    i64.const 2
    i64.eq
    call 0

    ;; bitwise
    local.get 0
    local.get 1
    i64.and
    i64.const 10
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.or
    i64.const 42
    i64.eq
    call 0

    local.get 0
    local.get 1
    i64.xor
    i64.const 32
    i64.eq
    call 0

    ;; shifts and rotates
    local.get 0
    local.get 2
    i64.extend_i32_u
    i64.shl
    i64.const 336
    i64.eq
    call 0

    local.get 0
    local.get 2
    i64.extend_i32_u
    i64.shr_s
    i64.const 5
    i64.eq
    call 0

    ;; local.get 0
    ;; local.get 2
    ;; i64.extend_i32_u
    ;; i64.rotr
    ;; i64.const 4611686018427387909
    ;; i64.eq
    ;; call 0

    ;; comparisons
    local.get 0
    local.get 1
    i64.eq
    i32.eqz
    call 0

    local.get 0
    local.get 1
    i64.lt_s
    i32.eqz
    call 0

    local.get 0
    local.get 1
    i64.gt_u
    call 0

    local.get 0
    local.get 1
    i64.ge_s
    call 0

    ;; integer extension
    i32.const -7
    i64.extend_i32_s
    i64.const -7
    i64.eq
    call 0

    i32.const -7
    i64.extend_i32_u
    i64.const 4294967289
    i64.eq
    call 0
  )

  (export "test_i64" (func 1))
)