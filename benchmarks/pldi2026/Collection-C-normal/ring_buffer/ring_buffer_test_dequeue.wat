(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (type (;2;) (func))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32) (result i32)))
  (type (;5;) (func (param i32 i32)))
  (func $setup_test (type 2)
    i32.const 1048
    call $rbuf_new
    drop)
  (func $teardown_test (type 2)
    i32.const 0
    i32.load offset=1048
    call $rbuf_destroy)
  (func $__original_main (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 144
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=140
    call $setup_test
    local.get 0
    i32.const 1042
    i32.symbolic
    i32.store offset=92
    local.get 0
    local.get 0
    i32.load offset=92
    i32.store8 offset=90
    local.get 0
    i32.const 0
    i32.store8 offset=91
    local.get 0
    i32.const 1040
    i32.symbolic
    i32.store offset=84
    local.get 0
    local.get 0
    i32.load offset=84
    i32.store8 offset=82
    local.get 0
    i32.const 0
    i32.store8 offset=83
    local.get 0
    i32.const 1038
    i32.symbolic
    i32.store offset=76
    local.get 0
    local.get 0
    i32.load offset=76
    i32.store8 offset=74
    local.get 0
    i32.const 0
    i32.store8 offset=75
    local.get 0
    i32.const 1036
    i32.symbolic
    i32.store offset=68
    local.get 0
    local.get 0
    i32.load offset=68
    i32.store8 offset=66
    local.get 0
    i32.const 0
    i32.store8 offset=67
    local.get 0
    i32.const 1034
    i32.symbolic
    i32.store offset=60
    local.get 0
    local.get 0
    i32.load offset=60
    i32.store8 offset=58
    local.get 0
    i32.const 0
    i32.store8 offset=59
    local.get 0
    i32.const 1032
    i32.symbolic
    i32.store offset=52
    local.get 0
    local.get 0
    i32.load offset=52
    i32.store8 offset=50
    local.get 0
    i32.const 0
    i32.store8 offset=51
    local.get 0
    i32.const 1030
    i32.symbolic
    i32.store offset=44
    local.get 0
    local.get 0
    i32.load offset=44
    i32.store8 offset=42
    local.get 0
    i32.const 0
    i32.store8 offset=43
    local.get 0
    i32.const 1028
    i32.symbolic
    i32.store offset=36
    local.get 0
    local.get 0
    i32.load offset=36
    i32.store8 offset=34
    local.get 0
    i32.const 0
    i32.store8 offset=35
    local.get 0
    i32.const 1026
    i32.symbolic
    i32.store offset=28
    local.get 0
    local.get 0
    i32.load offset=28
    i32.store8 offset=26
    local.get 0
    i32.const 0
    i32.store8 offset=27
    local.get 0
    i32.const 1024
    i32.symbolic
    i32.store offset=20
    local.get 0
    local.get 0
    i32.load offset=20
    i32.store8 offset=18
    local.get 0
    i32.const 0
    i32.store8 offset=19
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=92
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=84
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=76
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=68
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=60
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=52
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=44
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=36
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=28
    call $rbuf_enqueue
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.load offset=20
    call $rbuf_enqueue
    local.get 0
    i32.const 96
    i32.add
    local.tee 1
    i64.const 0
    i64.store
    local.get 1
    i32.const 32
    i32.add
    i64.const 0
    i64.store
    local.get 1
    i32.const 24
    i32.add
    i64.const 0
    i64.store
    local.get 1
    i32.const 16
    i32.add
    i64.const 0
    i64.store
    local.get 1
    i32.const 8
    i32.add
    i64.const 0
    i64.store
    local.get 0
    local.get 0
    i32.load offset=92
    i32.store offset=96
    local.get 0
    local.get 0
    i32.load offset=84
    i32.store offset=100
    local.get 0
    local.get 0
    i32.load offset=76
    i32.store offset=104
    local.get 0
    local.get 0
    i32.load offset=68
    i32.store offset=108
    local.get 0
    local.get 0
    i32.load offset=60
    i32.store offset=112
    local.get 0
    local.get 0
    i32.load offset=52
    i32.store offset=116
    local.get 0
    local.get 0
    i32.load offset=44
    i32.store offset=120
    local.get 0
    local.get 0
    i32.load offset=36
    i32.store offset=124
    local.get 0
    local.get 0
    i32.load offset=28
    i32.store offset=128
    local.get 0
    local.get 0
    i32.load offset=20
    i32.store offset=132
    local.get 0
    i32.const 0
    i32.store offset=8
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.load offset=8
        i32.const 10
        i32.lt_s
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        i32.const 0
        i32.load offset=1048
        local.get 0
        i32.const 12
        i32.add
        call $rbuf_dequeue
        drop
        local.get 0
        i32.const 96
        i32.add
        local.get 0
        i32.load offset=8
        i32.const 2
        i32.shl
        i32.add
        i32.load
        local.get 0
        i32.load offset=12
        i32.eq
        i32.const 1
        i32.and
        sym_assert
        local.get 0
        i32.const 12
        i32.add
        i32.const 0
        i32.store
        local.get 0
        local.get 0
        i32.load offset=8
        i32.const 1
        i32.add
        i32.store offset=8
        br 0 (;@2;)
      end
    end
    call $teardown_test
    local.get 0
    i32.const 144
    i32.add
    global.set 0
    i32.const 0)
  (func $malloc (type 4) (param i32) (result i32)
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
  (func $free (type 1) (param i32)
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
  (func $rbuf_new (type 4) (param i32) (result i32)
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
    call $rbuf_conf_init
    local.get 1
    i32.const 8
    i32.add
    local.get 1
    i32.load offset=28
    call $rbuf_conf_new
    local.set 0
    local.get 1
    i32.const 32
    i32.add
    global.set 0
    local.get 0)
  (func $rbuf_conf_init (type 1) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.const 10
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
  (func $rbuf_conf_new (type 0) (param i32 i32) (result i32)
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
      i32.const 4
      local.get 2
      i32.load offset=8
      i32.load offset=8
      call_indirect (type 0)
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
        call_indirect (type 1)
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
      i32.load
      local.get 2
      i32.load offset=8
      i32.load
      i32.store offset=4
      local.get 2
      i32.load
      i32.const 0
      i32.store
      local.get 2
      i32.load
      i32.const 0
      i32.store offset=8
      local.get 2
      i32.load
      i32.const 0
      i32.store offset=12
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
  (func $rbuf_destroy (type 1) (param i32)
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
    call_indirect (type 1)
    local.get 1
    i32.load offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=28
    call_indirect (type 1)
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func $rbuf_is_empty (type 4) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.load
    i32.const 0
    i32.eq
    i32.const 1
    i32.and)
  (func $rbuf_enqueue (type 5) (param i32 i32)
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
    block  ;; label = @1
      local.get 2
      i32.load offset=12
      i32.load offset=8
      local.get 2
      i32.load offset=12
      i32.load offset=12
      i32.eq
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=12
      i32.load offset=12
      i32.const 1
      i32.add
      local.get 2
      i32.load offset=12
      i32.load offset=4
      i32.rem_u
      i32.store offset=12
    end
    local.get 2
    i32.load offset=12
    i32.load offset=16
    local.get 2
    i32.load offset=12
    i32.load offset=8
    i32.const 2
    i32.shl
    i32.add
    local.get 2
    i32.load offset=8
    i32.store
    local.get 2
    i32.load offset=12
    local.get 2
    i32.load offset=12
    i32.load offset=8
    i32.const 1
    i32.add
    local.get 2
    i32.load offset=12
    i32.load offset=4
    i32.rem_u
    i32.store offset=8
    block  ;; label = @1
      local.get 2
      i32.load offset=12
      i32.load
      local.get 2
      i32.load offset=12
      i32.load offset=4
      i32.lt_u
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=12
      local.tee 2
      local.get 2
      i32.load
      i32.const 1
      i32.add
      i32.store
    end)
  (func $rbuf_dequeue (type 0) (param i32 i32) (result i32)
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
        call $rbuf_is_empty
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 8
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=4
      local.get 2
      i32.load offset=8
      i32.load offset=16
      local.get 2
      i32.load offset=8
      i32.load offset=12
      i32.const 2
      i32.shl
      i32.add
      i32.load
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
      i32.rem_u
      i32.store offset=12
      local.get 2
      i32.load offset=8
      local.tee 1
      local.get 1
      i32.load
      i32.const -1
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
  (func $assert (type 1) (param i32)
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
  (func $dealloc (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func $sym_int (type 4) (param i32) (result i32)
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
  (export "__original_main" (func $__original_main))
  (elem (;0;) (i32.const 1) $malloc $calloc $free)
  (data (;0;) (i32.const 1024) "j\00i\00h\00g\00f\00e\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1044) " \04\01\00"))