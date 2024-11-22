(module

  ;; Import only required for printing
  (import "spectest" "print_i32" (func $print_i32 (param i32)))

  (func $process (param $x i32)
    (call $print_i32 (local.get $x)) 
  )

  (func $range (param $from i32) (param $to i32) 
    (local $i i32) 
    (local.set $i (local.get $from))
    (block $b
      (loop $l
        (br_if $b (i32.gt_u (local.get $i) (local.get $to))) 
        (call $process (local.get $i))
        (local.set $i (i32.add (local.get $i) (i32.const 1)))
        (br $l))))

  (type $task (func (param i32)))
  (func $run (param $task1 (ref $task)) (param $task2 (ref $task))
    (call_ref $task (i32.const 10) (local.get $task1))
    (call_ref $task (i32.const 20) (local.get $task2)))

  (elem declare func $task1)
  (elem declare func $task2)
  
  (func $task1 (param $x i32) (call $range (local.get $x) (i32.const 13))) 
  (func $task2 (param $x i32) (call $range (local.get $x) (i32.const 23))) 
  (func $main   (export "_start")
    (call $run (ref.func $task1) (ref.func $task2)))
  (start $main)
)