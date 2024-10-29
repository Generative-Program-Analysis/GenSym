(module
  (func $for_loop (result i32)
    (local i32)  ;; local 0
    (local i32)  ;; local 1: loop counter

    ;; For
    (for
      ;; init
      (local.set 0 (i32.const 0))
      (local.set 1 (i32.const 0))

      ;; cond
      (i32.lt_s (local.get 1) (i32.const 10))

      ;; post
      (local.set 1 (i32.add (local.get 1) (i32.const 1)))

      ;; es
      (local.set 0 (i32.add (local.get 0) (i32.const 1)))
    )

    ;;
    (local.get 0)
  )
)
