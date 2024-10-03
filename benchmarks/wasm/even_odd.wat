(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (result i32)))
  (func (;0;) (type 0) (param i32) (result i32)
    block  ;; label = @1
      local.get 0
      br_if 0 (;@1;)
      i32.const 1
      return
    end
    local.get 0
    i32.const -1
    i32.add
    call 1)
  (func (;1;) (type 0) (param i32) (result i32)
    block  ;; label = @1
      local.get 0
      br_if 0 (;@1;)
      i32.const 0
      return
    end
    local.get 0
    i32.const -1
    i32.add
    call 0)
  (func (;2;) (type 1) (result i32)
    i32.const 13
    call 1)
  (start 2)
  (memory (;0;) 16)
)
