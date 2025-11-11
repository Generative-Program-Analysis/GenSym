(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func))
  (type (;4;) (func (result i32)))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (type (;6;) (func (param i32 i32)))
  (func $setup_tests (type 3)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 0
    global.set 0
    i32.const 1048
    i32.const 1
    call $pqueue_new
    drop
    local.get 0
    i32.const 8
    i32.add
    i32.const 2
    call $pqueue_conf_init
    local.get 0
    i32.const 8
    i32.add
    i32.const 1052
    call $pqueue_new_conf
    drop
    local.get 0
    i32.const 32
    i32.add
    global.set 0)
  (func $comp2 (type 0) (param i32 i32) (result i32)
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
    i32.load offset=12
    i32.load
    local.get 2
    i32.load offset=8
    i32.load
    i32.sub)
  (func $comp (type 0) (param i32 i32) (result i32)
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
    i32.load offset=24
    i32.load offset=4
    i32.store offset=12
    local.get 2
    local.get 2
    i32.load offset=20
    i32.load
    i32.store offset=8
    local.get 2
    local.get 2
    i32.load offset=20
    i32.load offset=4
    i32.store offset=4
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=8
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=8
        i32.sub
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=4
      i32.sub
      i32.store offset=28
    end
    local.get 2
    i32.load offset=28)
  (func $teardown_tests (type 3)
    i32.const 0
    i32.load offset=1048
    call $pqueue_destroy
    i32.const 0
    i32.load offset=1052
    call $pqueue_destroy)
  (func $__original_main (type 4) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=44
    call $setup_tests
    local.get 0
    i32.const 1040
    i32.symbolic
    i32.store offset=40
    local.get 0
    i32.const 1038
    i32.symbolic
    i32.store offset=36
    local.get 0
    i32.const 1036
    i32.symbolic
    i32.store offset=32
    local.get 0
    i32.const 1034
    i32.symbolic
    i32.store offset=28
    local.get 0
    i32.const 1032
    i32.symbolic
    i32.store offset=24
    local.get 0
    i32.const 1030
    i32.symbolic
    i32.store offset=20
    local.get 0
    i32.const 1028
    i32.symbolic
    i32.store offset=16
    local.get 0
    i32.const 1026
    i32.symbolic
    i32.store offset=12
    local.get 0
    i32.const 1024
    i32.symbolic
    i32.store offset=8
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=16
      i32.const 8388608
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=16
      i32.const -8388608
      i32.gt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=12
      i32.const 8388608
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=12
      i32.const -8388608
      i32.gt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=8
      i32.const 8388608
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=8
      i32.const -8388608
      i32.gt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=16
      local.get 0
      i32.load offset=8
      i32.gt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=8
      local.get 0
      i32.load offset=12
      i32.gt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 12
    i32.add
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 16
    i32.add
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 8
    i32.add
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 4
    i32.add
    call $pqueue_pop
    drop
    local.get 0
    i32.const 16
    i32.add
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 4
    i32.add
    call $pqueue_pop
    drop
    local.get 0
    i32.const 8
    i32.add
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 4
    i32.add
    call $pqueue_pop
    drop
    local.get 0
    i32.const 12
    i32.add
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    local.get 0
    i32.load offset=40
    i32.store offset=1056
    i32.const 0
    local.get 0
    i32.load offset=36
    i32.store offset=1060
    i32.const 0
    local.get 0
    i32.load offset=32
    i32.store offset=1064
    i32.const 0
    local.get 0
    i32.load offset=28
    i32.store offset=1068
    i32.const 0
    local.get 0
    i32.load offset=24
    i32.store offset=1072
    i32.const 0
    local.get 0
    i32.load offset=20
    i32.store offset=1076
    i32.const 0
    local.set 1
    block  ;; label = @1
      i32.const 1072
      i32.const 1056
      call $comp
      i32.const 0
      i32.gt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      i32.const 1056
      i32.const 1064
      call $comp
      i32.const 0
      i32.gt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    i32.load offset=1052
    i32.const 1056
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1052
    i32.const 1064
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1052
    i32.const 1072
    call $pqueue_push
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    call $pqueue_pop
    drop
    i32.const 1072
    local.get 0
    i32.load
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1052
    local.get 0
    call $pqueue_pop
    drop
    i32.const 1056
    local.get 0
    i32.load
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1052
    local.get 0
    call $pqueue_pop
    drop
    i32.const 1064
    local.get 0
    i32.load
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    call $teardown_tests
    local.get 0
    i32.const 48
    i32.add
    global.set 0
    i32.const 0)
  (func $malloc (type 1) (param i32) (result i32)
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
    i32.load offset=1044
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
        i32.load offset=1044
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
    i32.load offset=1044
    local.get 1
    i32.load offset=12
    i32.add
    i32.store offset=1044
    local.get 1
    i32.load offset=8
    local.get 1
    i32.load offset=12
    alloc
    local.set 0
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    local.get 0)
  (func $calloc (type 0) (param i32 i32) (result i32)
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
    i32.load offset=1044
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
        i32.load offset=1044
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
    i32.load offset=1044
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.mul
    i32.add
    i32.store offset=1044
    local.get 2
    i32.load offset=4
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.mul
    alloc
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func $free (type 2) (param i32)
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
    free
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func $memcpy (type 5) (param i32 i32 i32) (result i32)
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
  (func $pqueue_conf_init (type 6) (param i32 i32)
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
    i32.load offset=12
    i32.const 3
    i32.store offset=12
    local.get 2
    i32.load offset=12
    i32.const 4
    i32.store offset=16
    local.get 2
    i32.load offset=12
    i32.const 5
    i32.store offset=20
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=8
    i32.store offset=8
    local.get 2
    i32.load offset=12
    f32.const 0x1p+1 (;=2;)
    f32.store offset=4
    local.get 2
    i32.load offset=12
    i32.const 8
    i32.store)
  (func $pqueue_new (type 0) (param i32 i32) (result i32)
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
    local.get 2
    i32.load offset=24
    call $pqueue_conf_init
    local.get 2
    local.get 2
    i32.load offset=28
    call $pqueue_new_conf
    local.set 1
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    local.get 1)
  (func $pqueue_new_conf (type 0) (param i32 i32) (result i32)
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
        f32.load offset=4
        f32.const 0x1p+0 (;=1;)
        f32.le
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        f32.const 0x1p+1 (;=2;)
        f32.store offset=16
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=24
      f32.load offset=4
      f32.store offset=16
    end
    block  ;; label = @1
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=24
          i32.load
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          f32.load offset=16
          i32.const 16777216
          local.get 2
          i32.load offset=24
          i32.load
          i32.div_u
          f32.convert_i32_u
          f32.ge
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 2
        i32.const 2
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.const 1
      i32.const 32
      local.get 2
      i32.load offset=24
      i32.load offset=16
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
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=24
      i32.load
      i32.const 2
      i32.shl
      local.get 2
      i32.load offset=24
      i32.load offset=12
      call_indirect (type 1)
      i32.store offset=8
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=12
        local.get 2
        i32.load offset=24
        i32.load offset=20
        call_indirect (type 2)
        local.get 2
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=12
      i32.store offset=16
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=16
      i32.store offset=20
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=20
      i32.store offset=24
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=8
      i32.store offset=28
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.store offset=12
      local.get 2
      i32.load offset=12
      local.get 2
      f32.load offset=16
      f32.store offset=8
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load
      i32.store offset=4
      local.get 2
      i32.load offset=20
      local.get 2
      i32.load offset=12
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
  (func $pqueue_destroy (type 2) (param i32)
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
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=24
    call_indirect (type 2)
    local.get 1
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=24
    call_indirect (type 2)
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func $pqueue_push (type 0) (param i32 i32) (result i32)
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
    local.get 2
    i32.load offset=24
    i32.load
    i32.store offset=16
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        i32.load offset=4
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=24
        call $expand_capacity
        i32.store offset=12
        block  ;; label = @3
          local.get 2
          i32.load offset=12
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load offset=12
          i32.store offset=28
          br 2 (;@1;)
        end
      end
      local.get 2
      i32.load offset=24
      i32.load offset=12
      local.get 2
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load offset=20
      i32.store
      local.get 2
      i32.load offset=24
      local.tee 1
      local.get 1
      i32.load
      i32.const 1
      i32.add
      i32.store
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        br_if 0 (;@2;)
        local.get 2
        i32.const 0
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=24
      i32.load offset=12
      local.get 2
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=8
      local.get 2
      local.get 2
      i32.load offset=24
      i32.load offset=12
      local.get 2
      i32.load offset=16
      i32.const 1
      i32.sub
      i32.const 1
      i32.shr_u
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=4
      loop  ;; label = @2
        i32.const 0
        local.set 1
        block  ;; label = @3
          local.get 2
          i32.load offset=16
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=8
          local.get 2
          i32.load offset=4
          local.get 2
          i32.load offset=24
          i32.load offset=28
          call_indirect (type 0)
          i32.const 0
          i32.gt_s
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
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 2
          i32.shl
          i32.add
          i32.load
          i32.store
          local.get 2
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 2
          i32.shl
          i32.add
          local.get 2
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 1
          i32.sub
          i32.const 1
          i32.shr_u
          i32.const 2
          i32.shl
          i32.add
          i32.load
          i32.store
          local.get 2
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 1
          i32.sub
          i32.const 1
          i32.shr_u
          i32.const 2
          i32.shl
          i32.add
          local.get 2
          i32.load
          i32.store
          local.get 2
          local.get 2
          i32.load offset=16
          i32.const 1
          i32.sub
          i32.const 1
          i32.shr_u
          i32.store offset=16
          local.get 2
          local.get 2
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 2
          i32.shl
          i32.add
          i32.load
          i32.store offset=8
          local.get 2
          local.get 2
          i32.load offset=24
          i32.load offset=12
          local.get 2
          i32.load offset=16
          i32.const 1
          i32.sub
          i32.const 1
          i32.shr_u
          i32.const 2
          i32.shl
          i32.add
          i32.load
          i32.store offset=4
          br 1 (;@2;)
        end
      end
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
  (func $expand_capacity (type 1) (param i32) (result i32)
    (local i32 f32)
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
        i32.const 16777216
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
      block  ;; label = @2
        block  ;; label = @3
          local.get 1
          i32.load offset=8
          local.tee 0
          i32.load offset=4
          f32.convert_i32_u
          local.get 0
          f32.load offset=8
          f32.mul
          local.tee 2
          f32.const 0x1p+32 (;=4.29497e+09;)
          f32.lt
          local.get 2
          f32.const 0x0p+0 (;=0;)
          f32.ge
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.trunc_f32_u
          local.set 0
          br 1 (;@2;)
        end
        i32.const 0
        local.set 0
      end
      local.get 1
      local.get 0
      i32.store offset=4
      block  ;; label = @2
        block  ;; label = @3
          local.get 1
          i32.load offset=4
          local.get 1
          i32.load offset=8
          i32.load offset=4
          i32.le_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 1
          i32.load offset=8
          i32.const 16777216
          i32.store offset=4
          br 1 (;@2;)
        end
        local.get 1
        i32.load offset=8
        local.get 1
        i32.load offset=4
        i32.store offset=4
      end
      local.get 1
      local.get 1
      i32.load offset=4
      i32.const 2
      i32.shl
      local.get 1
      i32.load offset=8
      i32.load offset=16
      call_indirect (type 1)
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
      i32.load
      local.get 1
      i32.load offset=8
      i32.load offset=12
      local.get 1
      i32.load offset=8
      i32.load
      i32.const 2
      i32.shl
      call $memcpy
      drop
      local.get 1
      i32.load offset=8
      i32.load offset=12
      local.get 1
      i32.load offset=8
      i32.load offset=24
      call_indirect (type 2)
      local.get 1
      i32.load offset=8
      local.get 1
      i32.load
      i32.store offset=12
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
  (func $pqueue_pop (type 0) (param i32 i32) (result i32)
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
        i32.load
        br_if 0 (;@2;)
        local.get 2
        i32.const 8
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=8
      i32.load offset=12
      i32.load
      i32.store
      local.get 2
      i32.load offset=8
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load
      i32.const 1
      i32.sub
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 2
      i32.load offset=8
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load
      i32.const 1
      i32.sub
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load
      i32.store
      local.get 2
      local.get 2
      i32.load offset=8
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load
      i32.const 1
      i32.sub
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 2
      i32.load offset=8
      local.tee 1
      local.get 1
      i32.load
      i32.const -1
      i32.add
      i32.store
      local.get 2
      i32.load offset=8
      i32.const 0
      call $pqueue_heapify
      block  ;; label = @2
        local.get 2
        i32.load offset=4
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=4
        local.get 2
        i32.load
        i32.store
      end
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
  (func $pqueue_heapify (type 6) (param i32 i32)
    (local i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 2
    global.set 0
    local.get 2
    local.get 0
    i32.store offset=44
    local.get 2
    local.get 1
    i32.store offset=40
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=44
        i32.load
        i32.const 1
        i32.le_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        br 1 (;@1;)
      end
      local.get 2
      local.get 2
      i32.load offset=40
      i32.const 1
      i32.shl
      i32.const 1
      i32.add
      i32.store offset=36
      local.get 2
      local.get 2
      i32.load offset=40
      i32.const 1
      i32.shl
      i32.const 2
      i32.add
      i32.store offset=32
      local.get 2
      local.get 2
      i32.load offset=40
      i32.store offset=28
      local.get 2
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=36
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=24
      local.get 2
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=32
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=20
      local.get 2
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=40
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=16
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=36
          local.get 2
          i32.load offset=44
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=32
          local.get 2
          i32.load offset=44
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        local.get 2
        i32.load offset=44
        i32.load offset=28
        call_indirect (type 0)
        i32.const 0
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=24
        i32.store offset=16
        local.get 2
        local.get 2
        i32.load offset=36
        i32.store offset=40
      end
      block  ;; label = @2
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=20
        local.get 2
        i32.load offset=44
        i32.load offset=28
        call_indirect (type 0)
        i32.const 0
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=20
        i32.store offset=16
        local.get 2
        local.get 2
        i32.load offset=32
        i32.store offset=40
      end
      local.get 2
      i32.load offset=40
      local.get 2
      i32.load offset=28
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=28
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store offset=12
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=28
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=40
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 2
      i32.load offset=44
      i32.load offset=12
      local.get 2
      i32.load offset=40
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load offset=12
      i32.store
      local.get 2
      i32.load offset=44
      local.get 2
      i32.load offset=40
      call $pqueue_heapify
    end
    local.get 2
    i32.const 48
    i32.add
    global.set 0)
  (func $assume (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func $assert (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func $alloc (type 0) (param i32 i32) (result i32)
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
  (func $dealloc (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func $sym_int (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12)
  (table (;0;) 6 6 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66624))
  (export "memory" (memory 0))
  (export "__original_main" (func $__original_main))
  (elem (;0;) (i32.const 1) $comp2 $comp $malloc $calloc $free)
  (data (;0;) (i32.const 1024) "z\00y\00x\00f\00e\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1044) "@\04\01\00"))