(module
  (type $f (func))
  (type $c (cont $f))
  (type $fb (func))
  (type $cb (cont $fb))
  (import "spectest" "print_i32" (func $print_i32 (param i32)))
  (tag $fetch)
  (start $main)

  (func $func_b
    (call $print_i32 (i32.const 111))
    (suspend $fetch)
    (call $print_i32 (i32.const 333))
  )
  (elem declare func $func_b)

  (func $func_a
    (call $print_i32 (i32.const 000))
    (resume $c (cont.new $c (ref.func $func_b)))
    (call $print_i32 (i32.const 444))
    (return)
  )
  (elem declare func $func_a)

  (func $func_c
    (block (result (ref null $cb))
      (resume $c (on $fetch 0) (cont.new $c (ref.func $func_a)))
      (call $print_i32 (i32.const 404))
      (return)
    )
    (call $print_i32 (i32.const 222))
    (resume $cb)
    (call $print_i32 (i32.const 555))
  )

  (func $main (export "main")
    (call $print_i32 (i32.const 0))
    (resume $c (cont.new $c (ref.func $func_c)))
    (call $print_i32 (i32.const 666))
  )
)