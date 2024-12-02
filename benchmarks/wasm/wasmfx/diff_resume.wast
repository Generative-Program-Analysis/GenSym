(module
  (import "spectest" "print_i32" (func $print_i32 (param i32)))
  (type $task (func (param i32)))
  (type $cont (cont $task))

  (tag $yield (result i32))

  (func $task1 (param $x i32)
    (call $print_i32 (local.get $x))
    (suspend $yield) ;; DH: levaes a continuation on the stack, jump to the tag $yield
                     ;;     when come back, the stack should contains a i32 value, since the return type of $yield is i32
    (call $print_i32)
  )

  (func $main (export "_start")
    (local $i i32)
    (local $k (ref $cont))
    (local.set $k (cont.new $cont (ref.func $task1)))
    (local.set $i (i32.const 10))
    (block $h
        (block $on_yield (result (ref $cont))
          (resume $cont
            (on $yield $on_yield)
            (local.get $i)
            (local.get $k)
          )
          (call $print_i32 (i32.const -2))
          (br $h))
        ;; $on_yield lands here, with the continuation on the stack
        (local.set $k)
        (local.set $i (i32.add (local.get $i) (i32.const 1)))
        (block $h
          (block $on_yield2 (result (ref $cont))
            (resume $cont
              (on $yield $on_yield2)
              (local.get $i)
              (local.get $k)
            )
            (call $print_i32 (i32.const 42))
            (br $h)
          )
          ;; $on_yield2 lands here, with the continuation on the stack
          (call $print_i32 (i32.const 111))
          drop
        )
    )
    )

  (elem declare func $task1)

  (start $main)
)