(module
  (import "spectest" "print_i32" (func $print_i32 (param i32)))
  (type $task (func (param i32)))
  (type $cont (cont $task))

  (tag $yield (result i32))
  ;; DH: tag's input + continuation 
  ;;     == block's output ((ref $cont))
  ;;     == the values on the stack after suspend
  ;;     tag's output 
  ;;     == continuation's input
  ;;     == the values on the stack when suspended computation is resumed 

  (func $process (param $x i32)
    (call $print_i32 (local.get $x))
    (suspend $yield) ;; DH: levaes a continuation on the stack, jump to the tag $yield
                     ;;     when come back, the stack should contains a i32 value, since the return type of $yield is i32
    (call $print_i32))

  (func $range (param $from i32) (param $to i32) 
    (local $i i32) 
    (local.set $i (local.get $from))
    (block $b
      (loop $l
        (br_if $b (i32.gt_u (local.get $i) (local.get $to))) 
        (call $process (local.get $i))
        (local.set $i (i32.add (local.get $i) (i32.const 1)))
        (br $l))))
  
  (func $task1 (param $x i32) (call $range (local.get $x) (i32.const 13)))
  
  (func $main (export "_start")
    (local $k (ref $cont))
    (local $i i32)
    (local.set $k (cont.new $cont (ref.func $task1)))
    (local.set $i (i32.const 10))
    (block $h
      (loop $l
        (block $on_yield (result (ref $cont))
          (resume $cont 
            (on $yield $on_yield) 
            (local.get $i)
            (local.get $k))
          (call $print_i32 (i32.const -2)) ;; this code is executed only when no suspend is called in $k
          (br $h))
        ;; $on_yield lands here, with the continuation on the stack
        (local.set $k) ;; grab the continuation and save it
        (local.set $i (i32.add (local.get $i) (i32.const 1)))
        (call $print_i32 (i32.const -1)) 
        (br $l))))

  (elem declare func $task1)

  (start $main)
)