(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func))
  (type (;4;) (func (result i32)))
  (type (;5;) (func (param i32 i32 i32)))
  (type (;6;) (func (param i32 i32 i32) (result i32)))
  (import "i32" "symbolic" (func (;0;) (type 1)))
  (import "i32" "sym_assume" (func (;1;) (type 2)))
  (import "i32" "sym_assert" (func (;2;) (type 2)))
  (import "i32" "is_symbolic" (func (;3;) (type 0)))
  (import "sym" "get_sym_int32" (func (;4;) (type 1)))
  (import "mem" "alloc" (func (;5;) (type 0)))
  (import "mem" "free" (func (;6;) (type 2)))
  (func (;7;) (type 3)
    i32.const 0
    i32.const 1036
    call 11
    i32.store offset=1040)
  (func (;8;) (type 3)
    i32.const 0
    i32.load offset=1036
    call 15)
  (func (;9;) (type 4) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=28
    call 7
    local.get 0
    i32.const 1024
    call 0
    i32.store offset=24
    local.get 0
    i32.const 1026
    call 0
    i32.store offset=20
    local.get 0
    i32.const 1028
    call 0
    i32.store offset=16
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 24
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 20
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 16
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    call 21
    i32.const 0
    i32.load offset=1036
    i32.const 0
    local.get 0
    i32.const 12
    i32.add
    call 20
    drop
    i32.const 0
    i32.load offset=1036
    i32.const 1
    local.get 0
    i32.const 8
    i32.add
    call 20
    drop
    i32.const 0
    i32.load offset=1036
    i32.const 2
    local.get 0
    i32.const 4
    i32.add
    call 20
    drop
    local.get 0
    i32.load offset=16
    local.get 0
    i32.load offset=12
    i32.load
    i32.eq
    i32.const 1
    i32.and
    call 2
    local.get 0
    i32.load offset=20
    local.get 0
    i32.load offset=8
    i32.load
    i32.eq
    i32.const 1
    i32.and
    call 2
    local.get 0
    i32.load offset=24
    local.get 0
    i32.load offset=4
    i32.load
    i32.eq
    i32.const 1
    i32.and
    call 2
    call 8
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    call 10
    local.get 0
    i32.const 32
    i32.add
    global.set 0
    i32.const 0)
  (func (;10;) (type 3)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 0
    global.set 0
    call 7
    local.get 0
    i32.const 1
    i32.store offset=28
    local.get 0
    i32.const 2
    i32.store offset=24
    local.get 0
    i32.const 3
    i32.store offset=20
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 28
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 24
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 20
    i32.add
    call 16
    drop
    i32.const 0
    i32.load offset=1036
    call 21
    i32.const 0
    i32.load offset=1036
    i32.const 0
    local.get 0
    i32.const 16
    i32.add
    call 20
    drop
    i32.const 0
    i32.load offset=1036
    i32.const 1
    local.get 0
    i32.const 12
    i32.add
    call 20
    drop
    i32.const 0
    i32.load offset=1036
    i32.const 2
    local.get 0
    i32.const 8
    i32.add
    call 20
    drop
    call 8
    local.get 0
    i32.const 32
    i32.add
    global.set 0)
  (func (;11;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=28
    local.get 1
    i32.const 8
    i32.add
    local.tee 0
    call 12
    local.get 0
    local.get 1
    i32.load offset=28
    call 13
    local.set 0
    local.get 1
    i32.const 32
    i32.add
    global.set 0
    local.get 0)
  (func (;12;) (type 2) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.const 8
    i32.store
    local.get 1
    i32.load offset=12
    i32.const 1
    i32.store offset=4
    local.get 1
    i32.load offset=12
    i32.const 2
    i32.store offset=8
    local.get 1
    i32.load offset=12
    i32.const 3
    i32.store offset=12)
  (func (;13;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=8
    local.get 2
    local.get 1
    i32.store offset=4
    local.get 2
    i32.const 1
    i32.const 32
    local.get 2
    i32.load offset=8
    i32.load offset=8
    call_indirect (type 0)
    i32.store
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 2
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=8
      i32.load
      i32.const 2
      i32.shl
      local.get 2
      i32.load offset=8
      i32.load offset=4
      call_indirect (type 1)
      local.set 1
      local.get 2
      i32.load
      local.get 1
      i32.store offset=16
      block  ;; label = @2
        local.get 1
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 2
        i32.load
        local.get 2
        i32.load offset=8
        i32.load offset=12
        call_indirect (type 2)
        local.get 2
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.store offset=20
      local.get 2
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.store offset=24
      local.get 2
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=12
      i32.store offset=28
      local.get 2
      i32.load offset=8
      i32.load
      call 14
      local.set 1
      local.get 2
      i32.load
      local.get 1
      i32.store offset=4
      local.get 2
      i32.load
      i32.const 0
      i32.store offset=8
      local.get 2
      i32.load
      i32.const 0
      i32.store offset=12
      local.get 2
      i32.load
      i32.const 0
      i32.store
      local.get 2
      i32.load offset=4
      local.get 2
      i32.load
      i32.store
      local.get 2
      i32.const 0
      i32.store offset=12
    end
    local.get 2
    i32.load offset=12
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;14;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=8
    block  ;; label = @1
      block  ;; label = @2
        local.get 1
        i32.load offset=8
        i32.const -2147483648
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 1
        i32.const -2147483648
        i32.store offset=12
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 1
        i32.load offset=8
        br_if 0 (;@2;)
        local.get 1
        i32.const 2
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 1
      local.get 1
      i32.load offset=8
      i32.const -1
      i32.add
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.const 1
      i32.shr_u
      i32.or
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.const 2
      i32.shr_u
      i32.or
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.const 4
      i32.shr_u
      i32.or
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.const 8
      i32.shr_u
      i32.or
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.const 16
      i32.shr_u
      i32.or
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      i32.const 1
      i32.add
      i32.store offset=8
      local.get 1
      local.get 1
      i32.load offset=8
      i32.store offset=12
    end
    local.get 1
    i32.load offset=12)
  (func (;15;) (type 2) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=16
    local.get 1
    i32.load offset=12
    i32.load offset=28
    call_indirect (type 2)
    local.get 1
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=28
    call_indirect (type 2)
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func (;16;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=12
    local.get 2
    local.get 1
    i32.store offset=8
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    call 17
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;17;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=8
    local.get 2
    local.get 1
    i32.store offset=4
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.load offset=4
        local.get 2
        i32.load offset=8
        i32.load
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=8
        call 18
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=8
      i32.load offset=16
      local.get 2
      i32.load offset=8
      i32.load offset=12
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load offset=4
      i32.store
      local.get 2
      i32.load offset=8
      local.get 2
      i32.load offset=8
      i32.load offset=12
      i32.const 1
      i32.add
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.const 1
      i32.sub
      i32.and
      i32.store offset=12
      local.get 2
      i32.load offset=8
      local.tee 1
      local.get 1
      i32.load
      i32.const 1
      i32.add
      i32.store
      local.get 2
      i32.const 0
      i32.store offset=12
    end
    local.get 2
    i32.load offset=12
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;18;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=8
    block  ;; label = @1
      block  ;; label = @2
        local.get 1
        i32.load offset=8
        i32.load offset=4
        i32.const -2147483648
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 1
        i32.const 4
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 1
      local.get 1
      i32.load offset=8
      i32.load offset=4
      i32.const 1
      i32.shl
      i32.store offset=4
      local.get 1
      local.get 1
      i32.load offset=4
      i32.const 4
      local.get 1
      i32.load offset=8
      i32.load offset=24
      call_indirect (type 0)
      i32.store
      block  ;; label = @2
        local.get 1
        i32.load
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 1
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load
      i32.const 0
      call 19
      local.get 1
      i32.load offset=8
      i32.load offset=16
      local.get 1
      i32.load offset=8
      i32.load offset=28
      call_indirect (type 2)
      local.get 1
      i32.load offset=8
      i32.const 0
      i32.store offset=8
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=8
      i32.load
      i32.store offset=12
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load offset=4
      i32.store offset=4
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load
      i32.store offset=16
      local.get 1
      i32.const 0
      i32.store offset=12
    end
    local.get 1
    i32.load offset=12
    local.set 0
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    local.get 0)
  (func (;19;) (type 5) (param i32 i32 i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 3
    global.set 0
    local.get 3
    local.get 0
    i32.store offset=28
    local.get 3
    local.get 1
    i32.store offset=24
    local.get 3
    local.get 2
    i32.store offset=20
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=20
        i32.const 0
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        block  ;; label = @3
          block  ;; label = @4
            local.get 3
            i32.load offset=28
            i32.load offset=12
            local.get 3
            i32.load offset=28
            i32.load offset=8
            i32.gt_u
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 3
            i32.load offset=24
            local.get 3
            i32.load offset=28
            i32.load offset=16
            local.get 3
            i32.load offset=28
            i32.load offset=8
            i32.const 2
            i32.shl
            i32.add
            local.get 3
            i32.load offset=28
            i32.load
            i32.const 2
            i32.shl
            call 25
            drop
            br 1 (;@3;)
          end
          local.get 3
          local.get 3
          i32.load offset=28
          i32.load offset=12
          i32.store offset=16
          local.get 3
          local.get 3
          i32.load offset=28
          i32.load offset=4
          local.get 3
          i32.load offset=28
          i32.load offset=8
          i32.sub
          i32.store offset=12
          local.get 3
          i32.load offset=24
          local.get 3
          i32.load offset=28
          i32.load offset=16
          local.get 3
          i32.load offset=28
          i32.load offset=8
          i32.const 2
          i32.shl
          i32.add
          local.get 3
          i32.load offset=12
          i32.const 2
          i32.shl
          call 25
          drop
          local.get 3
          i32.load offset=24
          local.get 3
          i32.load offset=12
          i32.const 2
          i32.shl
          i32.add
          local.get 3
          i32.load offset=28
          i32.load offset=16
          local.get 3
          i32.load offset=16
          i32.const 2
          i32.shl
          call 25
          drop
        end
        br 1 (;@1;)
      end
      local.get 3
      i32.const 0
      i32.store offset=8
      block  ;; label = @2
        loop  ;; label = @3
          local.get 3
          i32.load offset=8
          local.get 3
          i32.load offset=28
          i32.load
          i32.lt_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 3
          local.get 3
          i32.load offset=28
          i32.load offset=8
          local.get 3
          i32.load offset=8
          i32.add
          local.get 3
          i32.load offset=28
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.and
          i32.store offset=4
          local.get 3
          i32.load offset=28
          i32.load offset=16
          local.get 3
          i32.load offset=4
          i32.const 2
          i32.shl
          i32.add
          i32.load
          local.get 3
          i32.load offset=20
          call_indirect (type 1)
          local.set 2
          local.get 3
          i32.load offset=24
          local.get 3
          i32.load offset=8
          i32.const 2
          i32.shl
          i32.add
          local.get 2
          i32.store
          local.get 3
          local.get 3
          i32.load offset=8
          i32.const 1
          i32.add
          i32.store offset=8
          br 0 (;@3;)
        end
      end
    end
    local.get 3
    i32.const 32
    i32.add
    global.set 0)
  (func (;20;) (type 6) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 3
    local.get 0
    i32.store offset=24
    local.get 3
    local.get 1
    i32.store offset=20
    local.get 3
    local.get 2
    i32.store offset=16
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=20
        local.get 3
        i32.load offset=24
        i32.load
        i32.gt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.const 8
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 3
      local.get 3
      i32.load offset=24
      i32.load offset=8
      local.get 3
      i32.load offset=20
      i32.add
      local.get 3
      i32.load offset=24
      i32.load offset=4
      i32.const 1
      i32.sub
      i32.and
      i32.store offset=12
      local.get 3
      i32.load offset=16
      local.get 3
      i32.load offset=24
      i32.load offset=16
      local.get 3
      i32.load offset=12
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 3
      i32.const 0
      i32.store offset=28
    end
    local.get 3
    i32.load offset=28)
  (func (;21;) (type 2) (param i32)
    (local i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=44
    local.get 1
    local.get 1
    i32.load offset=44
    i32.load
    i32.store offset=32
    local.get 1
    local.get 1
    i32.load offset=44
    i32.load offset=4
    i32.const 1
    i32.sub
    i32.store offset=28
    local.get 1
    local.get 1
    i32.load offset=44
    i32.load offset=8
    i32.store offset=24
    local.get 1
    i32.const 0
    i32.store offset=40
    local.get 1
    local.get 1
    i32.load offset=32
    i32.const 1
    i32.sub
    i32.store offset=36
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        i32.load offset=40
        local.get 1
        i32.load offset=32
        i32.const 1
        i32.sub
        i32.const 1
        i32.shr_u
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 1
        local.get 1
        i32.load offset=24
        local.get 1
        i32.load offset=40
        i32.add
        local.get 1
        i32.load offset=28
        i32.and
        i32.store offset=20
        local.get 1
        local.get 1
        i32.load offset=24
        local.get 1
        i32.load offset=36
        i32.add
        local.get 1
        i32.load offset=28
        i32.and
        i32.store offset=16
        local.get 1
        local.get 1
        i32.load offset=44
        i32.load offset=16
        local.get 1
        i32.load offset=20
        i32.const 2
        i32.shl
        i32.add
        i32.load
        i32.store offset=12
        local.get 1
        i32.load offset=44
        i32.load offset=16
        local.get 1
        i32.load offset=20
        i32.const 2
        i32.shl
        i32.add
        local.get 1
        i32.load offset=44
        i32.load offset=16
        local.get 1
        i32.load offset=16
        i32.const 2
        i32.shl
        i32.add
        i32.load
        i32.store
        local.get 1
        i32.load offset=44
        i32.load offset=16
        local.get 1
        i32.load offset=16
        i32.const 2
        i32.shl
        i32.add
        local.get 1
        i32.load offset=12
        i32.store
        local.get 1
        local.get 1
        i32.load offset=40
        i32.const 1
        i32.add
        i32.store offset=40
        local.get 1
        local.get 1
        i32.load offset=36
        i32.const -1
        i32.add
        i32.store offset=36
        br 0 (;@2;)
      end
    end)
  (func (;22;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.const 0
    i32.load offset=1032
    i32.store offset=8
    local.get 1
    i32.const 0
    i32.store offset=4
    block  ;; label = @1
      loop  ;; label = @2
        local.get 1
        i32.load offset=4
        local.get 1
        i32.load offset=12
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        i32.const 0
        i32.load offset=1032
        local.get 1
        i32.load offset=4
        i32.add
        i32.const 105
        i32.store8
        local.get 1
        local.get 1
        i32.load offset=4
        i32.const 1
        i32.add
        i32.store offset=4
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.const 0
    i32.load offset=1032
    local.get 1
    i32.load offset=12
    i32.add
    i32.store offset=1032
    local.get 1
    i32.load offset=8
    local.get 1
    i32.load offset=12
    call 5
    local.set 0
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    local.get 0)
  (func (;23;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=12
    local.get 2
    local.get 1
    i32.store offset=8
    local.get 2
    i32.const 0
    i32.load offset=1032
    i32.store offset=4
    local.get 2
    i32.const 0
    i32.store
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        i32.load
        local.get 2
        i32.load offset=12
        local.get 2
        i32.load offset=8
        i32.mul
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        i32.const 0
        i32.load offset=1032
        local.get 2
        i32.load
        i32.add
        i32.const 0
        i32.store8
        local.get 2
        local.get 2
        i32.load
        i32.const 1
        i32.add
        i32.store
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.const 0
    i32.load offset=1032
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.mul
    i32.add
    i32.store offset=1032
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.mul
    call 5
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;24;) (type 2) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    call 6
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func (;25;) (type 6) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 3
    local.get 0
    i32.store offset=28
    local.get 3
    local.get 1
    i32.store offset=24
    local.get 3
    local.get 2
    i32.store offset=20
    local.get 3
    local.get 3
    i32.load offset=28
    i32.store offset=12
    local.get 3
    local.get 3
    i32.load offset=24
    i32.store offset=8
    block  ;; label = @1
      local.get 3
      i32.load offset=12
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 3
      i32.load offset=8
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 3
      i32.const 0
      i32.store offset=16
      block  ;; label = @2
        loop  ;; label = @3
          local.get 3
          i32.load offset=16
          local.get 3
          i32.load offset=20
          i32.lt_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 3
          i32.load offset=12
          local.get 3
          i32.load offset=8
          i32.load8_u
          i32.store8
          local.get 3
          local.get 3
          i32.load offset=16
          i32.const 1
          i32.add
          i32.store offset=16
          local.get 3
          local.get 3
          i32.load offset=12
          i32.const 1
          i32.add
          i32.store offset=12
          local.get 3
          local.get 3
          i32.load offset=8
          i32.const 1
          i32.add
          i32.store offset=8
          br 0 (;@3;)
        end
      end
    end
    local.get 3
    i32.load offset=28)
  (func (;26;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;27;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 2
    local.get 0
    i32.store offset=12
    local.get 2
    local.get 1
    i32.store offset=8
    local.get 2
    i32.load offset=12)
  (func (;28;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;29;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12)
  (table (;0;) 4 4 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66592))
  (export "memory" (memory 0))
  (export "__original_main" (func 9))
  (elem (;0;) (i32.const 1) func 22 23 24)
  (data (;0;) (i32.const 1024) "a\00b\00c\00")
  (data (;1;) (i32.const 1032) " \04\01\00"))
