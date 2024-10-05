;; https://developer.mozilla.org/en-US/docs/WebAssembly/Reference/Control_flow/loop
(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32)
    loop  ;; label = @1
      local.get 0
      i32.const 1
      i32.add
      local.set 0
      local.get 0
      call 0
      local.get 0
      i32.const 10
      i32.lt_s
      br_if 0 (;@1;)
    end
    local.get 0)
  (start 1))
