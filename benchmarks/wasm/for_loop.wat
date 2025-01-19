(module
  (func $for_loop (result i32)
    (local i32)
    (local i32)

    for
       (
        ;; init
        i32.const 0
        local.set 0
        i32.const 0
        local.set 1
        |
        ;; cond
        local.get 1
        i32.const 10
        i32.gt_s
        i32.eqz
        |
        ;; post
        local.get 1
        i32.const 1
        i32.add
        local.set 1
        )

        ;; es
        local.get 0
        local.get 1
        i32.add
        local.set 0

     local.get 0


     )

     (export "for_loop" (func 0))

  )