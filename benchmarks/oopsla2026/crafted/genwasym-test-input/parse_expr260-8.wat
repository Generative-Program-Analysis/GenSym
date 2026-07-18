(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32 i32) (result i32)))
  (func (;0;) (type 0) (result i32)
    (local i32 i32 i32)
    global.get 0
    global.get 1
    i32.add
    local.set 0
    local.get 0
    i32.load
    i32.const 255
    i32.and)
  (func (;1;) (type 1)
    global.get 1
    i32.const 1
    i32.add
    global.set 1)
  (func (;2;) (type 0) (result i32)
    (local i32 i32)
    i32.const 0
    local.set 0
    loop  ;; label = @1
      call 0
      local.tee 1
      i32.const 48
      i32.ge_s
      local.get 1
      i32.const 57
      i32.le_s
      i32.and
      if  ;; label = @2
        local.get 0
        i32.const 10
        i32.mul
        local.get 1
        i32.const 48
        i32.sub
        i32.add
        local.set 0
        call 1
        br 1 (;@1;)
      end
    end
    local.get 0)
  (func (;3;) (type 0) (result i32)
    (local i32)
    call 0
    local.tee 0
    i32.const 48
    i32.ge_s
    local.get 0
    i32.const 57
    i32.le_s
    i32.and
    if (result i32)  ;; label = @1
      call 2
    else
      local.get 0
      i32.const 120
      i32.eq
      if (result i32)  ;; label = @2
        call 1
        global.get 2
      else
        i32.const 0
      end
    end)
  (func (;4;) (type 0) (result i32)
    (local i32 i32 i32)
    call 3
    local.set 0
    block  ;; label = @1
      loop  ;; label = @2
        call 0
        local.tee 1
        i32.const 42
        i32.ne
        br_if 1 (;@1;)
        call 1
        call 3
        local.set 2
        local.get 0
        local.get 2
        i32.mul
        local.set 0
        br 0 (;@2;)
      end
    end
    local.get 0)
  (func (;5;) (type 0) (result i32)
    (local i32 i32 i32)
    call 4
    local.set 0
    block  ;; label = @1
      loop  ;; label = @2
        call 0
        local.tee 1
        i32.const 43
        i32.eq
        local.get 1
        i32.const 45
        i32.eq
        i32.or
        i32.eqz
        br_if 1 (;@1;)
        call 1
        call 4
        local.set 2
        local.get 1
        i32.const 45
        i32.eq
        if  ;; label = @3
          local.get 0
          local.get 2
          i32.sub
          local.set 0
        else
          local.get 0
          local.get 2
          i32.add
          local.set 0
        end
        br 0 (;@2;)
      end
    end
    local.get 0)
  (func (;6;) (type 2) (param i32 i32) (result i32)
    local.get 0
    global.set 2
    local.get 1
    global.set 0
    i32.const 0
    global.set 1
    call 5)
  (func (;7;) (result i32)
    (local i32 i32)
    i32.const 0
    i32.symbolic
    local.tee 1
    i32.const 1024
    call 6
    local.tee 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    local.get 0
    if
    end
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    drop
    i32.const 10   ;; x
    i32.const 1024
    call 6
    local.get 1
    i32.const 0
    call 6
    local.tee 0
    if
    end
    local.get 0
    i32.const 1
    i32.add
    if
    end
    local.get 0
    i32.const 2
    i32.add
    if
    end
    local.get 0
    i32.const 3
    i32.add
    if
    end
    local.get 0
    i32.const 4
    i32.add
    if
    end
    local.get 0
    i32.const 5
    i32.add
    if
    end
  )
  (import "console" "assert" (func (param i32)))
  (start 7)
  (memory (;0;) 1)
  (global (;0;) (mut i32) (i32.const 0))
  (global (;1;) (mut i32) (i32.const 0))
  (global (;2;) (mut i32) (i32.const 0))
  (export "memory" (memory 0))
  (export "eval_expr" (func 6))
  (data (i32.const 0) "x+1\00")
  (data (;0;) (i32.const 1024) "50+x+70-11+x+130+x+170+33+x+50+x+70-11+x+130+x+170+33+x+50+x+70-11+x+130+x+170+33+x+3+x+5+x+7-11+x+13+x+17+6+1+10+x+14-11+x+26+x+34+9+x+15+x+21-11+x+39+x+51+12+x+20+x+28-11+x+52+x+68+15+x+25+x+x+x+35-11+x+65+x+85+18+x+30+x+42-11+x+78+x+102+21+x+35+x+49-11+x+91+x+119+24+x+40+x+56-11-x+104-x+136+27-x+45-x+63-11-x+117-x+153+30-x+50-x+70-11-x+130-x+170+33-x+55-x+77-11-x+143-x+187+36-x+60-x+84-11-x+156-x+204+39-x+65-x+91-11-x+169-x+221+42-x+70-x+98-11-x+182-x+238+45-x+75-x+105-11-x+1+3-x+5-x+7-11+x+x+17+x+10+x+182+x+238+45+x+75+x+105-11+x+1+3-x+5-x+7-11-x+x+17+x+10+17+6-x+10-x+14-11-x+49-11-x+91-x+119+24-x+40-x+56-11-x+104-x+136+27-x+45-x+49-11-x+91-x+119+24-x+40-x+56-11-x+104-x+136+27-x+45\00"))
