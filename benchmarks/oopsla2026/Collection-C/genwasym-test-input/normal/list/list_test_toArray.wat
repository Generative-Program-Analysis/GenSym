(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (type (;2;) (func))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32) (result i32)))
  (type (;5;) (func (param i32 i32)))
  (type (;6;) (func (param i32 i32 i32) (result i32)))
  (import "i32" "symbolic" (func (;0;) (type 4)))
  (import "i32" "sym_assume" (func (;1;) (type 1)))
  (import "i32" "sym_assert" (func (;2;) (type 1)))
  (import "i32" "is_symbolic" (func (;3;) (type 0)))
  (import "sym" "get_sym_int32" (func (;4;) (type 4)))
  (import "mem" "alloc" (func (;5;) (type 0)))
  (import "mem" "free" (func (;6;) (type 1)))
  (func (;7;) (type 2)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 0
    global.set 0
    i32.const 1044
    call 14
    drop
    i32.const 1048
    call 14
    drop
    i32.const 0
    i32.const 1038
    call 0
    i32.store offset=1052
    i32.const 0
    i32.const 1036
    call 0
    i32.store offset=1056
    i32.const 0
    i32.const 1034
    call 0
    i32.store offset=1060
    i32.const 0
    i32.const 1032
    call 0
    i32.store offset=1064
    i32.const 0
    i32.const 1030
    call 0
    i32.store offset=1068
    i32.const 0
    i32.const 1028
    call 0
    i32.store offset=1072
    i32.const 0
    i32.const 1026
    call 0
    i32.store offset=1076
    i32.const 0
    i32.const 1024
    call 0
    i32.store offset=1080
    local.get 0
    i32.const 4
    call 10
    i32.store offset=12
    local.get 0
    i32.const 4
    call 10
    i32.store offset=8
    local.get 0
    i32.const 4
    call 10
    i32.store offset=4
    local.get 0
    i32.const 4
    call 10
    i32.store
    local.get 0
    i32.load offset=12
    i32.const 0
    i32.load offset=1052
    i32.store
    local.get 0
    i32.load offset=8
    i32.const 0
    i32.load offset=1056
    i32.store
    local.get 0
    i32.load offset=4
    i32.const 0
    i32.load offset=1060
    i32.store
    local.get 0
    i32.load
    i32.const 0
    i32.load offset=1064
    i32.store
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.load offset=12
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.load offset=8
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.load offset=4
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.load
    call 21
    drop
    local.get 0
    i32.const 4
    call 10
    i32.store offset=12
    local.get 0
    i32.const 4
    call 10
    i32.store offset=8
    local.get 0
    i32.const 4
    call 10
    i32.store offset=4
    local.get 0
    i32.const 4
    call 10
    i32.store
    local.get 0
    i32.load offset=12
    i32.const 0
    i32.load offset=1068
    i32.store
    local.get 0
    i32.load offset=8
    i32.const 0
    i32.load offset=1072
    i32.store
    local.get 0
    i32.load offset=4
    i32.const 0
    i32.load offset=1076
    i32.store
    local.get 0
    i32.load
    i32.const 0
    i32.load offset=1080
    i32.store
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=12
    call 21
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=8
    call 21
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=4
    call 21
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load
    call 21
    drop
    local.get 0
    i32.const 16
    i32.add
    global.set 0)
  (func (;8;) (type 2)
    i32.const 0
    i32.load offset=1044
    i32.const 1
    call 19
    i32.const 0
    i32.load offset=1048
    call 16)
  (func (;9;) (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=12
    call 7
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 8
    i32.add
    call 26
    drop
    local.get 0
    i32.const 0
    i32.store
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.load
        i32.const 4
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        i32.const 0
        i32.load offset=1044
        local.get 0
        i32.load
        local.get 0
        i32.const 4
        i32.add
        call 25
        drop
        local.get 0
        i32.load offset=8
        local.get 0
        i32.load
        i32.const 2
        i32.shl
        i32.add
        i32.load
        local.get 0
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        call 2
        local.get 0
        local.get 0
        i32.load
        i32.const 1
        i32.add
        i32.store
        br 0 (;@2;)
      end
    end
    local.get 0
    i32.load offset=8
    call 6
    call 8
    local.get 0
    i32.load offset=12
    local.set 1
    local.get 0
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;10;) (type 4) (param i32) (result i32)
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
  (func (;11;) (type 0) (param i32 i32) (result i32)
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
  (func (;12;) (type 1) (param i32)
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
  (func (;13;) (type 1) (param i32)
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
    i32.store
    local.get 1
    i32.load offset=12
    i32.const 3
    i32.store offset=4
    local.get 1
    i32.load offset=12
    i32.const 1
    i32.store offset=8)
  (func (;14;) (type 4) (param i32) (result i32)
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
    call 13
    local.get 1
    local.get 1
    i32.load offset=12
    call 15
    local.set 0
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    local.get 0)
  (func (;15;) (type 0) (param i32 i32) (result i32)
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
    i32.const 24
    local.get 2
    i32.load offset=8
    i32.load offset=4
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
      i32.load
      local.get 2
      i32.load offset=8
      i32.load
      i32.store offset=12
      local.get 2
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.store offset=16
      local.get 2
      i32.load
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.store offset=20
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
  (func (;16;) (type 1) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=12
    block  ;; label = @1
      local.get 1
      i32.load offset=12
      i32.load
      i32.const 0
      i32.gt_u
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 1
      i32.load offset=12
      call 17
      drop
    end
    local.get 1
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=20
    call_indirect (type 1)
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func (;17;) (type 4) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    global.set 0
    local.get 1
    local.get 0
    i32.store offset=8
    local.get 1
    local.get 1
    i32.load offset=8
    i32.const 0
    call 18
    i32.store offset=4
    block  ;; label = @1
      block  ;; label = @2
        local.get 1
        i32.load offset=4
        i32.eqz
        br_if 0 (;@2;)
        local.get 1
        i32.load offset=8
        i32.const 0
        i32.store offset=4
        local.get 1
        i32.load offset=8
        i32.const 0
        i32.store offset=8
        local.get 1
        i32.const 0
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 1
      i32.const 7
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
  (func (;18;) (type 0) (param i32 i32) (result i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=24
        i32.load
        br_if 0 (;@2;)
        local.get 2
        i32.const 0
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=24
      i32.load offset=4
      i32.store offset=16
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.load offset=16
          i32.const 0
          i32.ne
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 2
          local.get 2
          i32.load offset=16
          i32.load offset=4
          i32.store offset=12
          block  ;; label = @4
            local.get 2
            i32.load offset=20
            i32.const 0
            i32.ne
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            i32.load offset=16
            i32.load
            local.get 2
            i32.load offset=20
            call_indirect (type 1)
          end
          local.get 2
          i32.load offset=24
          local.get 2
          i32.load offset=16
          call 24
          drop
          local.get 2
          local.get 2
          i32.load offset=12
          i32.store offset=16
          br 0 (;@3;)
        end
      end
      local.get 2
      i32.const 1
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
  (func (;19;) (type 5) (param i32 i32)
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
    call 20
    drop
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=12
    i32.load offset=20
    call_indirect (type 1)
    local.get 2
    i32.const 16
    i32.add
    global.set 0)
  (func (;20;) (type 0) (param i32 i32) (result i32)
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
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=4
    call 18
    i32.store
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=8
        i32.const 0
        i32.store offset=4
        local.get 2
        i32.load offset=8
        i32.const 0
        i32.store offset=8
        local.get 2
        i32.const 0
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.const 7
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
  (func (;21;) (type 0) (param i32 i32) (result i32)
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
    call 22
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;22;) (type 0) (param i32 i32) (result i32)
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
    i32.const 12
    local.get 2
    i32.load offset=8
    i32.load offset=16
    call_indirect (type 0)
    i32.store
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load
        i32.const 0
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load
      local.get 2
      i32.load offset=4
      i32.store
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=8
          i32.load
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load
          i32.store offset=4
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load
          i32.store offset=8
          br 1 (;@2;)
        end
        local.get 2
        i32.load
        local.get 2
        i32.load offset=8
        i32.load offset=8
        i32.store offset=8
        local.get 2
        i32.load offset=8
        i32.load offset=8
        local.get 2
        i32.load
        i32.store offset=4
        local.get 2
        i32.load offset=8
        local.get 2
        i32.load
        i32.store offset=8
      end
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
  (func (;23;) (type 6) (param i32 i32 i32) (result i32)
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
        block  ;; label = @3
          local.get 3
          i32.load offset=24
          i32.const 0
          i32.ne
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=20
          local.get 3
          i32.load offset=24
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 3
        i32.const 8
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 3
      i32.const 0
      i32.store offset=8
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=20
          local.get 3
          i32.load offset=24
          i32.load
          i32.const 1
          i32.shr_u
          i32.lt_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 3
          local.get 3
          i32.load offset=24
          i32.load offset=4
          i32.store offset=8
          local.get 3
          i32.const 0
          i32.store offset=12
          block  ;; label = @4
            loop  ;; label = @5
              local.get 3
              i32.load offset=12
              local.get 3
              i32.load offset=20
              i32.lt_u
              i32.const 1
              i32.and
              i32.eqz
              br_if 1 (;@4;)
              local.get 3
              local.get 3
              i32.load offset=8
              i32.load offset=4
              i32.store offset=8
              local.get 3
              local.get 3
              i32.load offset=12
              i32.const 1
              i32.add
              i32.store offset=12
              br 0 (;@5;)
            end
          end
          br 1 (;@2;)
        end
        local.get 3
        local.get 3
        i32.load offset=24
        i32.load offset=8
        i32.store offset=8
        local.get 3
        local.get 3
        i32.load offset=24
        i32.load
        i32.const 1
        i32.sub
        i32.store offset=12
        block  ;; label = @3
          loop  ;; label = @4
            local.get 3
            i32.load offset=12
            local.get 3
            i32.load offset=20
            i32.gt_u
            i32.const 1
            i32.and
            i32.eqz
            br_if 1 (;@3;)
            local.get 3
            local.get 3
            i32.load offset=8
            i32.load offset=8
            i32.store offset=8
            local.get 3
            local.get 3
            i32.load offset=12
            i32.const -1
            i32.add
            i32.store offset=12
            br 0 (;@4;)
          end
        end
      end
      local.get 3
      i32.load offset=16
      local.get 3
      i32.load offset=8
      i32.store
      local.get 3
      i32.const 0
      i32.store offset=28
    end
    local.get 3
    i32.load offset=28)
  (func (;24;) (type 0) (param i32 i32) (result i32)
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
    local.get 2
    i32.load offset=8
    i32.load
    i32.store offset=4
    block  ;; label = @1
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=8
      i32.load offset=8
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.store offset=4
    end
    block  ;; label = @1
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.const 0
      i32.eq
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.store offset=4
    end
    block  ;; label = @1
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.const 0
      i32.eq
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.store offset=8
    end
    block  ;; label = @1
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=8
      i32.load offset=4
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.store offset=8
    end
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=12
    i32.load offset=20
    call_indirect (type 1)
    local.get 2
    i32.load offset=12
    local.tee 1
    local.get 1
    i32.load
    i32.const -1
    i32.add
    i32.store
    local.get 2
    i32.load offset=4
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;25;) (type 6) (param i32 i32 i32) (result i32)
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
    local.get 3
    local.get 3
    i32.load offset=28
    local.get 3
    i32.load offset=24
    local.get 3
    i32.const 16
    i32.add
    call 23
    i32.store offset=12
    block  ;; label = @1
      local.get 3
      i32.load offset=12
      br_if 0 (;@1;)
      local.get 3
      i32.load offset=20
      local.get 3
      i32.load offset=16
      i32.load
      i32.store
    end
    local.get 3
    i32.load offset=12
    local.set 2
    local.get 3
    i32.const 32
    i32.add
    global.set 0
    local.get 2)
  (func (;26;) (type 0) (param i32 i32) (result i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=24
        i32.load
        br_if 0 (;@2;)
        local.get 2
        i32.const 3
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=24
      i32.load
      i32.const 4
      local.get 2
      i32.load offset=24
      i32.load offset=16
      call_indirect (type 0)
      i32.store offset=16
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
      local.get 2
      i32.load offset=24
      i32.load offset=4
      i32.store offset=12
      local.get 2
      i32.const 0
      i32.store offset=8
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load offset=24
          i32.load
          i32.lt_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 2
          i32.load offset=16
          local.get 2
          i32.load offset=8
          i32.const 2
          i32.shl
          i32.add
          local.get 2
          i32.load offset=12
          i32.load
          i32.store
          local.get 2
          local.get 2
          i32.load offset=12
          i32.load offset=4
          i32.store offset=12
          local.get 2
          local.get 2
          i32.load offset=8
          i32.const 1
          i32.add
          i32.store offset=8
          br 0 (;@3;)
        end
      end
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
  (func (;27;) (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;28;) (type 0) (param i32 i32) (result i32)
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
  (func (;29;) (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;30;) (type 4) (param i32) (result i32)
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
  (global (;0;) (mut i32) (i32.const 66624))
  (export "memory" (memory 0))
  (export "__original_main" (func 9))
  (elem (;0;) (i32.const 1) func 12 10 11)
  (data (;0;) (i32.const 1024) "h\00g\00f\00e\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1040) "@\04\01\00"))
