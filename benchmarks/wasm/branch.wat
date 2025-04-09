(module
  (func $f (param $x i32) (param $y i32) (result i32)
    ;; if (x <= 0 || y <= 0)
    (if (result i32)
      (if (result i32)
        (i32.le_s (local.get $x) (i32.const 0))
        (then (i32.const 1))
        (else (i32.le_s (local.get $y) (i32.const 0)))
      )
      (then (i32.const -1)) ;; return -1
      (else
        ;; if (x * x + y * y == 25)
        (if (result i32)
          (i32.eq
            (i32.add
              (i32.mul (local.get $x) (local.get $x))
              (i32.mul (local.get $y) (local.get $y))
            )
            (i32.const 25)
          )
          (then (i32.const 1)) ;; return 1
          (else (i32.const 0)) ;; return 0
        )
      )
    )
  )

  ;; Optionally export the function
  (export "f" (func $f))
)
