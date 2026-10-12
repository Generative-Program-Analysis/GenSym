(module
  (type (func (param i32 i32) (result i32)))
  (type (func (result i32)))

  ;; Parameters 0 and 1 are x and y.
  (func $f (type 0) (param i32 i32) (result i32)
    ;; if (x <= 0 || y <= 0) return -1;
    ;; Short-circuit: test y only when x > 0.
    local.get 0
    i32.const 0
    i32.le_s
    if (result i32)
      i32.const -1  ;; x <= 0: return -1.
    else
      local.get 1
      i32.const 0
      i32.le_s
      if (result i32)
        i32.const -1  ;; x > 0 and y <= 0: return -1.
      else
        ;; if (x * x + y * y == 25) return 1;
        local.get 0
        local.get 0
        i32.mul
        local.get 1
        local.get 1
        i32.mul
        i32.add
        i32.const 25
        i32.eq
        if (result i32)
          i32.const 1  ;; The sum of squares is 25: return 1.
        else
          i32.const 0  ;; Otherwise, return 0.
        end
      end
    end)

  (func $real_main (type 1) (result i32)
    ;; Create symbolic x with identifier 0.
    i32.const 0
    i32.symbolic
    ;; Create symbolic y with identifier 1.
    i32.const 1
    i32.symbolic
    call 0)  ;; Call f(x, y).

  (export "real_main" (func 1)))
