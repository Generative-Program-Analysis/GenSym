
(module
  ;; Import only required for printing
  (import "spectest" "print_i32" (func $print_i32 (param i32)))
  
  (func (param i32) (result i32)
    local.get 0
    i32.const 1
    i32.add
  )

  (func (param)
    i32.const 10
    ref.func 1
    cont.new 1
    resume 1
  )

  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (cont 0))

  (start 2)
)



