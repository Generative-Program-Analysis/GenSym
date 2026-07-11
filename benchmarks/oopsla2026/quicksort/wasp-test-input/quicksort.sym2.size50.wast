(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (result i32)))
  (type (;3;) (func (param i32)))
  (type (;4;) (func (param i32 i32)))
  (type (;5;) (func (param i32 i32) (result i32)))
  (func (;0;) (type 0) unreachable)
  (func (;1;) (type 1)
    call 2
    drop
    return)
  (func (;2;) (type 2) (result i32)
    i32.const 2
    call 3
    i32.const 2
    call 4
    i32.const 0
    i32.const 3
    call 5
    i32.const 2
    call 3
    i32.const 0
    i32.const 49
    call 5
    i32.const 0
    return)
  (func (;3;) (type 3) (param i32)
    (local i32 i32 i32 i32 i32)
    global.get 0
    i32.const 16
    i32.sub
    local.set 1
    local.get 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.const 0
    i32.store offset=8
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        i32.load offset=8
        local.get 1
        i32.load offset=12
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 1
        i32.load offset=8
        i32.symbolic
        local.set 2
        local.get 1
        i32.load offset=8
        local.set 3
        i32.const 1024
        global.get 1
        i32.add
        local.get 3
        i32.const 2
        i32.shl
        i32.add
        local.get 2
        i32.store
        local.get 1
        local.get 1
        i32.load offset=8
        i32.const 1
        i32.add
        i32.store offset=8
        br 0 (;@2;)
      end
    end
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        i32.load offset=8
        i32.const 50
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 1
        i32.load offset=8
        local.set 4
        i32.const 50
        local.get 4
        i32.sub
        local.set 5
        local.get 4
        i32.const 2
        i32.shl
        i32.const 1024
        global.get 1
        i32.add
        i32.add
        local.get 5
        i32.store
        local.get 1
        local.get 1
        i32.load offset=8
        i32.const 1
        i32.add
        i32.store offset=8
        br 0 (;@2;)
      end
    end
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    return)
  (func (;4;) (type 3) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.set 1
    local.get 1
    local.get 0
    i32.store offset=12
    return)
  (func (;5;) (type 4) (param i32 i32)
    (local i32 i32 i32 i32 i32 i32)
    global.get 0
    i32.const 32
    i32.sub
    local.set 2
    local.get 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=28
    local.get 2
    local.get 1
    i32.store offset=24
    block  ;; label = @1
      local.get 2
      i32.load offset=28
      local.get 2
      i32.load offset=24
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      local.get 2
      i32.load offset=24
      i32.const 2
      i32.shl
      i32.const 1024
      global.get 1
      i32.add
      i32.add
      i32.load
      i32.store offset=20
      local.get 2
      local.get 2
      i32.load offset=28
      i32.const 1
      i32.sub
      i32.store offset=16
      local.get 2
      local.get 2
      i32.load offset=28
      i32.store offset=12
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.load offset=12
          local.get 2
          i32.load offset=24
          i32.le_s
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          block  ;; label = @4
            local.get 2
            i32.load offset=12
            i32.const 2
            i32.shl
            i32.const 1024
            global.get 1
            i32.add
            i32.add
            i32.load
            local.get 2
            i32.load offset=20
            i32.le_s
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            local.get 2
            i32.load offset=16
            i32.const 1
            i32.add
            i32.store offset=16
            local.get 2
            i32.load offset=12
            local.set 3
            i32.const 1024
            global.get 1
            i32.add
            local.set 4
            i32.const 2
            local.set 5
            local.get 2
            local.get 4
            local.get 3
            local.get 5
            i32.shl
            i32.add
            i32.load
            i32.store offset=8
            local.get 4
            local.get 2
            i32.load offset=16
            local.get 5
            i32.shl
            i32.add
            i32.load
            local.set 6
            local.get 4
            local.get 2
            i32.load offset=12
            local.get 5
            i32.shl
            i32.add
            local.get 6
            i32.store
            local.get 2
            i32.load offset=8
            local.set 7
            local.get 4
            local.get 2
            i32.load offset=16
            local.get 5
            i32.shl
            i32.add
            local.get 7
            i32.store
          end
          local.get 2
          local.get 2
          i32.load offset=12
          i32.const 1
          i32.add
          i32.store offset=12
          br 0 (;@3;)
        end
      end
      local.get 2
      i32.load offset=28
      local.get 2
      i32.load offset=16
      i32.const 1
      i32.sub
      call 5
      local.get 2
      i32.load offset=16
      i32.const 1
      i32.add
      local.get 2
      i32.load offset=24
      call 5
    end
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    return)
  (func (;6;) (type 2) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 16
    i32.sub
    local.set 0
    local.get 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=12
    call 2
    local.set 1
    local.get 0
    i32.const 16
    i32.add
    global.set 0
    local.get 1
    return)
  (func (;7;) (type 5) (param i32 i32) (result i32)
    call 6
    return)
  (table (;0;) 1 1 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66768))
  (global (;1;) i32 (i32.const 0))
  (export "memory" (memory 0))
  (export "_start" (func 1))
  (export "main" (func 7))
  (start 1))
