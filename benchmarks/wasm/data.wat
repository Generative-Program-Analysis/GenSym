(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    local.get 0
    i32.load8_u)
  (func (;1;) (type 1) (result i32)
    ;; Call the `load_byte` function with an address of 0
    i32.const 0
    call 0)
  (memory (;0;) 1)
;;   (start 1) ;; should return 72
  ;; Define a memory with an initial size of 1 page (64KB)
  (export "memory" (memory 0))
  (export "main" (func 1))
  ;; Initialize memory using the `data` directive at offset 0
  (data (;0;) (i32.const 0) "Hello"))
