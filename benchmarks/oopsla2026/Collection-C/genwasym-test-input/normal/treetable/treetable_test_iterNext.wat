(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32 i32)))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (import "i32" "symbolic" (func (;0;) (type 2)))
  (import "i32" "sym_assume" (func (;1;) (type 1)))
  (import "i32" "sym_assert" (func (;2;) (type 1)))
  (import "i32" "is_symbolic" (func (;3;) (type 0)))
  (import "sym" "get_sym_int32" (func (;4;) (type 2)))
  (import "mem" "alloc" (func (;5;) (type 0)))
  (import "mem" "free" (func (;6;) (type 1)))
  (func (;7;) (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 96
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=92
    i32.const 1
    i32.const 1044
    call 12
    drop
    local.get 0
    i32.const 1028
    call 0
    i32.store offset=88
    local.get 0
    i32.const 1026
    call 0
    i32.store offset=84
    local.get 0
    i32.const 1024
    call 0
    i32.store offset=80
    local.get 0
    i32.const 1030
    call 0
    i32.store offset=76
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=88
      local.get 0
      i32.load offset=84
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.load offset=84
      local.get 0
      i32.load offset=76
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=76
      local.get 0
      i32.load offset=80
      i32.lt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.const 1038
    call 0
    i32.store offset=72
    local.get 0
    local.get 0
    i32.load offset=72
    i32.store8 offset=70
    local.get 0
    i32.const 0
    i32.store8 offset=71
    local.get 0
    i32.const 1036
    call 0
    i32.store offset=64
    local.get 0
    local.get 0
    i32.load offset=64
    i32.store8 offset=62
    local.get 0
    i32.const 0
    i32.store8 offset=63
    local.get 0
    i32.const 1034
    call 0
    i32.store offset=56
    local.get 0
    local.get 0
    i32.load offset=56
    i32.store8 offset=54
    local.get 0
    i32.const 0
    i32.store8 offset=55
    local.get 0
    i32.const 1032
    call 0
    i32.store offset=48
    local.get 0
    local.get 0
    i32.load offset=48
    i32.store8 offset=46
    local.get 0
    i32.const 0
    i32.store8 offset=47
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 80
    i32.add
    local.get 0
    i32.const 70
    i32.add
    call 18
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 76
    i32.add
    local.get 0
    i32.const 62
    i32.add
    call 18
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 84
    i32.add
    local.get 0
    i32.const 54
    i32.add
    call 18
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 88
    i32.add
    local.get 0
    i32.const 46
    i32.add
    call 18
    drop
    local.get 0
    i32.const 0
    i32.store offset=40
    local.get 0
    i32.const 0
    i32.store offset=36
    local.get 0
    i32.const 0
    i32.store offset=32
    local.get 0
    i32.const 0
    i32.store offset=28
    local.get 0
    i32.const 16
    i32.add
    i32.const 0
    i32.load offset=1044
    call 22
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.const 16
        i32.add
        local.get 0
        i32.const 8
        i32.add
        call 23
        i32.const 9
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 0
        local.get 0
        i32.load offset=8
        i32.store offset=4
        block  ;; label = @3
          local.get 0
          i32.load offset=4
          i32.load
          local.get 0
          i32.load offset=88
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 0
          local.get 0
          i32.load offset=40
          i32.const 1
          i32.add
          i32.store offset=40
        end
        block  ;; label = @3
          local.get 0
          i32.load offset=4
          i32.load
          local.get 0
          i32.load offset=84
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 0
          local.get 0
          i32.load offset=36
          i32.const 1
          i32.add
          i32.store offset=36
        end
        block  ;; label = @3
          local.get 0
          i32.load offset=4
          i32.load
          local.get 0
          i32.load offset=80
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 0
          local.get 0
          i32.load offset=32
          i32.const 1
          i32.add
          i32.store offset=32
        end
        block  ;; label = @3
          local.get 0
          i32.load offset=4
          i32.load
          local.get 0
          i32.load offset=76
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 0
          local.get 0
          i32.load offset=28
          i32.const 1
          i32.add
          i32.store offset=28
        end
        br 0 (;@2;)
      end
    end
    i32.const 1
    local.get 0
    i32.load offset=40
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    local.get 0
    i32.load offset=36
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    local.get 0
    i32.load offset=32
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    local.get 0
    i32.load offset=28
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.load offset=1044
    call 14
    local.get 0
    i32.load offset=92
    local.set 1
    local.get 0
    i32.const 96
    i32.add
    global.set 0
    local.get 1)
  (func (;8;) (type 2) (param i32) (result i32)
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
    i32.load offset=1040
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
        i32.load offset=1040
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
    i32.load offset=1040
    local.get 1
    i32.load offset=12
    i32.add
    i32.store offset=1040
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
  (func (;9;) (type 0) (param i32 i32) (result i32)
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
    i32.load offset=1040
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
        i32.load offset=1040
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
    i32.load offset=1040
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.mul
    i32.add
    i32.store offset=1040
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
  (func (;10;) (type 1) (param i32)
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
  (func (;11;) (type 1) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.const 2
    i32.store offset=4
    local.get 1
    i32.load offset=12
    i32.const 3
    i32.store offset=8
    local.get 1
    i32.load offset=12
    i32.const 4
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.const 0
    i32.store)
  (func (;12;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=28
    local.get 2
    local.get 1
    i32.store offset=24
    local.get 2
    i32.const 8
    i32.add
    call 11
    local.get 2
    local.get 2
    i32.load offset=28
    i32.store offset=8
    local.get 2
    i32.const 8
    i32.add
    local.get 2
    i32.load offset=24
    call 13
    local.set 1
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    local.get 1)
  (func (;13;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=24
    local.get 2
    local.get 1
    i32.store offset=20
    local.get 2
    i32.const 1
    i32.const 28
    local.get 2
    i32.load offset=24
    i32.load offset=8
    call_indirect (type 0)
    i32.store offset=16
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 2
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.const 1
      i32.const 24
      local.get 2
      i32.load offset=24
      i32.load offset=8
      call_indirect (type 0)
      i32.store offset=12
      block  ;; label = @2
        local.get 2
        i32.load offset=12
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        i32.load offset=12
        call_indirect (type 1)
        local.get 2
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=12
      i32.const 1
      i32.store8 offset=8
      local.get 2
      i32.load offset=16
      i32.const 0
      i32.store offset=8
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load
      i32.store offset=12
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=4
      i32.store offset=16
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=8
      i32.store offset=20
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=12
      i32.store offset=24
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=12
      i32.store
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=12
      i32.store offset=4
      local.get 2
      i32.load offset=20
      local.get 2
      i32.load offset=16
      i32.store
      local.get 2
      i32.const 0
      i32.store offset=28
    end
    local.get 2
    i32.load offset=28
    local.set 1
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    local.get 1)
  (func (;14;) (type 1) (param i32)
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
    local.get 1
    i32.load offset=12
    i32.load
    call 15
    local.get 1
    i32.load offset=12
    i32.load offset=4
    local.get 1
    i32.load offset=12
    i32.load offset=24
    call_indirect (type 1)
    local.get 1
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=24
    call_indirect (type 1)
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func (;15;) (type 4) (param i32 i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        local.get 2
        i32.load offset=12
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=16
      call 15
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=20
      call 15
      local.get 2
      i32.load offset=8
      local.get 2
      i32.load offset=12
      i32.load offset=24
      call_indirect (type 1)
    end
    local.get 2
    i32.const 16
    i32.add
    global.set 0)
  (func (;16;) (type 0) (param i32 i32) (result i32)
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
    local.get 2
    i32.load offset=12
    i32.load offset=4
    i32.store offset=4
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.load offset=16
        local.get 2
        i32.load offset=4
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 2
        local.get 2
        i32.load offset=8
        i32.load offset=16
        i32.store offset=8
        br 0 (;@2;)
      end
    end
    local.get 2
    i32.load offset=8)
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
        i32.load offset=4
        i32.const 0
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 0
        i32.store offset=12
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 2
        i32.load offset=4
        i32.load offset=20
        local.get 2
        i32.load offset=8
        i32.load offset=4
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=8
        local.get 2
        i32.load offset=4
        i32.load offset=20
        call 16
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=4
      i32.load offset=12
      i32.store
      loop  ;; label = @2
        i32.const 0
        local.set 1
        block  ;; label = @3
          local.get 2
          i32.load
          local.get 2
          i32.load offset=8
          i32.load offset=4
          i32.ne
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=4
          local.get 2
          i32.load
          i32.load offset=20
          i32.eq
          local.set 1
        end
        block  ;; label = @3
          local.get 1
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load
          i32.store offset=4
          local.get 2
          local.get 2
          i32.load
          i32.load offset=12
          i32.store
          br 1 (;@2;)
        end
      end
      local.get 2
      local.get 2
      i32.load
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
  (func (;18;) (type 5) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 3
    global.set 0
    local.get 3
    local.get 0
    i32.store offset=24
    local.get 3
    local.get 1
    i32.store offset=20
    local.get 3
    local.get 2
    i32.store offset=16
    local.get 3
    local.get 3
    i32.load offset=24
    i32.load offset=4
    i32.store offset=12
    local.get 3
    local.get 3
    i32.load offset=24
    i32.load
    i32.store offset=8
    block  ;; label = @1
      block  ;; label = @2
        loop  ;; label = @3
          local.get 3
          i32.load offset=8
          local.get 3
          i32.load offset=24
          i32.load offset=4
          i32.ne
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 3
          local.get 3
          i32.load offset=20
          local.get 3
          i32.load offset=8
          i32.load
          local.get 3
          i32.load offset=24
          i32.load offset=12
          call_indirect (type 0)
          i32.store offset=4
          local.get 3
          local.get 3
          i32.load offset=8
          i32.store offset=12
          block  ;; label = @4
            block  ;; label = @5
              local.get 3
              i32.load offset=4
              i32.const 0
              i32.lt_s
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 3
              local.get 3
              i32.load offset=8
              i32.load offset=16
              i32.store offset=8
              br 1 (;@4;)
            end
            block  ;; label = @5
              block  ;; label = @6
                local.get 3
                i32.load offset=4
                i32.const 0
                i32.gt_s
                i32.const 1
                i32.and
                i32.eqz
                br_if 0 (;@6;)
                local.get 3
                local.get 3
                i32.load offset=8
                i32.load offset=20
                i32.store offset=8
                br 1 (;@5;)
              end
              local.get 3
              i32.load offset=8
              local.get 3
              i32.load offset=16
              i32.store offset=4
              local.get 3
              i32.const 0
              i32.store offset=28
              br 4 (;@1;)
            end
          end
          br 0 (;@3;)
        end
      end
      local.get 3
      i32.const 24
      local.get 3
      i32.load offset=24
      i32.load offset=16
      call_indirect (type 2)
      i32.store
      block  ;; label = @2
        local.get 3
        i32.load
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 3
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 3
      i32.load
      local.get 3
      i32.load offset=16
      i32.store offset=4
      local.get 3
      i32.load
      local.get 3
      i32.load offset=20
      i32.store
      local.get 3
      i32.load
      local.get 3
      i32.load offset=12
      i32.store offset=12
      local.get 3
      i32.load
      local.get 3
      i32.load offset=24
      i32.load offset=4
      i32.store offset=16
      local.get 3
      i32.load
      local.get 3
      i32.load offset=24
      i32.load offset=4
      i32.store offset=20
      local.get 3
      i32.load offset=24
      local.tee 2
      local.get 2
      i32.load offset=8
      i32.const 1
      i32.add
      i32.store offset=8
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=12
          local.get 3
          i32.load offset=24
          i32.load offset=4
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=24
          local.get 3
          i32.load
          i32.store
          local.get 3
          i32.load
          i32.const 1
          i32.store8 offset=8
          br 1 (;@2;)
        end
        local.get 3
        i32.load
        i32.const 0
        i32.store8 offset=8
        block  ;; label = @3
          block  ;; label = @4
            local.get 3
            i32.load offset=20
            local.get 3
            i32.load offset=12
            i32.load
            local.get 3
            i32.load offset=24
            i32.load offset=12
            call_indirect (type 0)
            i32.const 0
            i32.lt_s
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 3
            i32.load offset=12
            local.get 3
            i32.load
            i32.store offset=16
            br 1 (;@3;)
          end
          local.get 3
          i32.load offset=12
          local.get 3
          i32.load
          i32.store offset=20
        end
        local.get 3
        i32.load offset=24
        local.get 3
        i32.load
        call 19
      end
      local.get 3
      i32.const 0
      i32.store offset=28
    end
    local.get 3
    i32.load offset=28
    local.set 2
    local.get 3
    i32.const 32
    i32.add
    global.set 0
    local.get 2)
  (func (;19;) (type 4) (param i32 i32)
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
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.load offset=12
        i32.load8_u offset=8
        i32.const 24
        i32.shl
        i32.const 24
        i32.shr_s
        br_if 1 (;@1;)
        block  ;; label = @3
          block  ;; label = @4
            local.get 2
            i32.load offset=8
            i32.load offset=12
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=12
            i32.load offset=16
            i32.eq
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=12
            i32.load offset=20
            i32.store offset=4
            block  ;; label = @5
              block  ;; label = @6
                local.get 2
                i32.load offset=4
                i32.load8_u offset=8
                i32.const 24
                i32.shl
                i32.const 24
                i32.shr_s
                br_if 0 (;@6;)
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.const 1
                i32.store8 offset=8
                local.get 2
                i32.load offset=4
                i32.const 1
                i32.store8 offset=8
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.load offset=12
                i32.const 0
                i32.store8 offset=8
                local.get 2
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.load offset=12
                i32.store offset=8
                br 1 (;@5;)
              end
              block  ;; label = @6
                local.get 2
                i32.load offset=8
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.load offset=20
                i32.eq
                i32.const 1
                i32.and
                i32.eqz
                br_if 0 (;@6;)
                local.get 2
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.store offset=8
                local.get 2
                i32.load offset=12
                local.get 2
                i32.load offset=8
                call 20
              end
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=12
              i32.const 0
              i32.store8 offset=8
              local.get 2
              i32.load offset=12
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=12
              call 21
            end
            br 1 (;@3;)
          end
          local.get 2
          local.get 2
          i32.load offset=8
          i32.load offset=12
          i32.load offset=12
          i32.load offset=16
          i32.store offset=4
          block  ;; label = @4
            block  ;; label = @5
              local.get 2
              i32.load offset=4
              i32.load8_u offset=8
              i32.const 24
              i32.shl
              i32.const 24
              i32.shr_s
              br_if 0 (;@5;)
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=4
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=12
              i32.const 0
              i32.store8 offset=8
              local.get 2
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=12
              i32.store offset=8
              br 1 (;@4;)
            end
            block  ;; label = @5
              local.get 2
              i32.load offset=8
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=16
              i32.eq
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 2
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.store offset=8
              local.get 2
              i32.load offset=12
              local.get 2
              i32.load offset=8
              call 21
            end
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.const 1
            i32.store8 offset=8
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=12
            i32.const 0
            i32.store8 offset=8
            local.get 2
            i32.load offset=12
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=12
            call 20
          end
        end
        br 0 (;@2;)
      end
    end
    local.get 2
    i32.load offset=12
    i32.load
    i32.const 1
    i32.store8 offset=8
    local.get 2
    i32.const 16
    i32.add
    global.set 0)
  (func (;20;) (type 4) (param i32 i32)
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
    local.get 2
    i32.load offset=8
    i32.load offset=20
    i32.store offset=4
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=4
    i32.load offset=16
    i32.store offset=20
    block  ;; label = @1
      local.get 2
      i32.load offset=4
      i32.load offset=16
      local.get 2
      i32.load offset=12
      i32.load offset=4
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=4
      i32.load offset=16
      local.get 2
      i32.load offset=8
      i32.store offset=12
    end
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=8
    i32.load offset=12
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.load offset=12
        local.get 2
        i32.load offset=12
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=12
        local.get 2
        i32.load offset=4
        i32.store
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load offset=8
          i32.load offset=12
          i32.load offset=16
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=8
          i32.load offset=12
          local.get 2
          i32.load offset=4
          i32.store offset=16
          br 1 (;@2;)
        end
        local.get 2
        i32.load offset=8
        i32.load offset=12
        local.get 2
        i32.load offset=4
        i32.store offset=20
      end
    end
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=8
    i32.store offset=16
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=4
    i32.store offset=12)
  (func (;21;) (type 4) (param i32 i32)
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
    local.get 2
    i32.load offset=8
    i32.load offset=16
    i32.store offset=4
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=4
    i32.load offset=20
    i32.store offset=16
    block  ;; label = @1
      local.get 2
      i32.load offset=4
      i32.load offset=20
      local.get 2
      i32.load offset=12
      i32.load offset=4
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=4
      i32.load offset=20
      local.get 2
      i32.load offset=8
      i32.store offset=12
    end
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=8
    i32.load offset=12
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.load offset=12
        local.get 2
        i32.load offset=12
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=12
        local.get 2
        i32.load offset=4
        i32.store
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load offset=8
          i32.load offset=12
          i32.load offset=20
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=8
          i32.load offset=12
          local.get 2
          i32.load offset=4
          i32.store offset=20
          br 1 (;@2;)
        end
        local.get 2
        i32.load offset=8
        i32.load offset=12
        local.get 2
        i32.load offset=4
        i32.store offset=16
      end
    end
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=8
    i32.store offset=20
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=4
    i32.store offset=12)
  (func (;22;) (type 4) (param i32 i32)
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
    i32.store
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.load offset=4
    i32.store offset=4
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=8
    i32.load
    call 16
    local.set 1
    local.get 2
    i32.load offset=12
    local.get 1
    i32.store offset=8
    local.get 2
    i32.const 16
    i32.add
    global.set 0)
  (func (;23;) (type 0) (param i32 i32) (result i32)
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
        i32.load offset=8
        local.get 2
        i32.load offset=8
        i32.load
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 9
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=4
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.load offset=4
      i32.store offset=4
      local.get 2
      i32.load offset=4
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.load
      i32.store
      local.get 2
      i32.load offset=8
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.store offset=4
      local.get 2
      i32.load offset=8
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=4
      call 17
      local.set 1
      local.get 2
      i32.load offset=8
      local.get 1
      i32.store offset=8
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
  (func (;24;) (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 2
    local.get 0
    i32.store offset=24
    local.get 2
    local.get 1
    i32.store offset=20
    local.get 2
    local.get 2
    i32.load offset=24
    i32.load
    i32.store offset=16
    local.get 2
    local.get 2
    i32.load offset=20
    i32.load
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=12
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const -1
        i32.store offset=28
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=12
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 0
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.const 1
      i32.store offset=28
    end
    local.get 2
    i32.load offset=28)
  (func (;25;) (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;26;) (type 1) (param i32)
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
  (func (;28;) (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;29;) (type 2) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12)
  (table (;0;) 5 5 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66592))
  (export "memory" (memory 0))
  (export "__original_main" (func 7))
  (elem (;0;) (i32.const 1) func 24 8 9 10)
  (data (;0;) (i32.const 1024) "z\00y\00x\00w\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1040) " \04\01\00"))
