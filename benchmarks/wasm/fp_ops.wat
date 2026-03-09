(module
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (type (;2;) (func (param f32)))
  (type (;3;) (func (param f64)))
  (import "console" "assert" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local f32 f32 f64 f64)
    f32.const 0x1.cp+1 (;=3.5;)
    local.set 0
    f32.const 0x1p+1 (;=2;)
    local.set 1
    f64.const 0x1.48p+3 (;=10.25;)
    local.set 2
    f64.const 0x1p-1 (;=0.5;)
    local.set 3
    local.get 0
    local.get 1
    f32.add
    f32.const 0x1.6p+2 (;=5.5;)
    f32.eq
    call 0
    local.get 0
    local.get 1
    f32.sub
    f32.const 0x1.8p+0 (;=1.5;)
    f32.eq
    call 0
    local.get 0
    local.get 1
    f32.mul
    f32.const 0x1.cp+2 (;=7;)
    f32.eq
    call 0
    local.get 0
    local.get 1
    f32.div
    f32.const 0x1.cp+0 (;=1.75;)
    f32.eq
    call 0
    local.get 0
    local.get 1
    f32.eq
    i32.eqz
    call 0
    local.get 0
    local.get 1
    f32.ne
    call 0
    local.get 0
    local.get 1
    f32.gt
    call 0
    local.get 0
    local.get 1
    f32.ge
    call 0
    local.get 0
    local.get 1
    f32.lt
    i32.eqz
    call 0
    local.get 0
    local.get 1
    f32.le
    i32.eqz
    call 0
    local.get 2
    local.get 3
    f64.add
    f64.const 0x1.58p+3 (;=10.75;)
    f64.eq
    call 0
    local.get 2
    local.get 3
    f64.sub
    f64.const 0x1.38p+3 (;=9.75;)
    f64.eq
    call 0
    local.get 2
    local.get 3
    f64.mul
    f64.const 0x1.48p+2 (;=5.125;)
    f64.eq
    call 0
    local.get 2
    local.get 3
    f64.div
    f64.const 0x1.48p+4 (;=20.5;)
    f64.eq
    call 0
    local.get 2
    local.get 3
    f64.eq
    i32.eqz
    call 0
    local.get 2
    local.get 3
    f64.ne
    call 0
    local.get 2
    local.get 3
    f64.gt
    call 0
    local.get 2
    local.get 3
    f64.ge
    call 0
    local.get 2
    local.get 3
    f64.lt
    i32.eqz
    call 0
    local.get 2
    local.get 3
    f64.le
    i32.eqz
    call 0)

  ;; symbolic branch guard for f32
  ;; pseudo:
  ;;   if (x > 1.0) {
  ;;     if (x < 2.0) {
  ;;       assert(false);   // forbidden range: (1.0, 2.0)
  ;;     }
  ;;   }
  (func (;2;) (type 2) (param f32)
    local.get 0
    f32.const 0x1p+0 (;=1;)
    f32.gt
    if  ;; label = @1
      local.get 0
      f32.const 0x1p+1 (;=2;)
      f32.lt
      if  ;; label = @2
        i32.const 0
        call 0
      end
    end)

  ;;   if (y > 10.0) {
  ;;     if (y < 11.0) {
  ;;       assert(false);   // forbidden range: (10.0, 11.0)
  ;;     }
  ;;   }
  (func (;3;) (type 3) (param f64)
    local.get 0
    f64.const 0x1.4p+3 (;=10;)
    f64.gt
    if  ;; label = @1
      local.get 0
      f64.const 0x1.6p+3 (;=11;)
      f64.lt
      if  ;; label = @2
        i32.const 0
        call 0
      end
    end)

  ;;   x := symbolic_f32()
  ;;   check_f32_guard(x)
  ;;   y := symbolic_f64()
  ;;   check_f64_guard(y)
  (func (;4;) (type 1)
    i32.const 0
    f32.symbolic
    call 2

    ;; i32.const 1
    ;; f64.symbolic
    ;; call 3
  )

  (export "test_fp" (func 1))
  (export "real_main" (func 4)))
