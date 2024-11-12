(module
  ;; Import only required for printing
  (import "spectest" "print_i32" (func $print_i32 (param i32)))

  (func (param i32) (result i32)
    local.get 0
    i32.const 1
    i32.add
    call 0 ;; print_i32
    suspend 0
  )

  (elem declare func 1)

  (func (result i32)
    block (; $h ;)
      block (; $on_yield 0 ;) (result (ref 1))
        i32.const 10
        ref.func 1
        cont.new 1
        resume 1 
          (on 0 (; tag $yield ;) 0 (; $on_yield ;))
        i32.const 999
        call 0 ;; never executed because of the suspend
        br 1 (; $h ;)
      end
      resume 1
    end
  )

  (start 2)

  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (cont 0))
  (type (;2;) (func))
  (tag (type 2) (; 0 ;))
  ;;    ^^^^^^^
  ;;    todo: should this be optional?

)
