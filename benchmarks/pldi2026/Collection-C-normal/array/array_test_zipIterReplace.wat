(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32 i32 i32 i32) (result i32)))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (type (;6;) (func (param i32 i32 i32)))
  (type (;7;) (func (param i32 i32 i32 i32 i32) (result i32)))
  (func $__original_main (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 128
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=124
    i32.const 1048
    call $array_new
    drop
    local.get 0
    i32.const 1040
    i32.symbolic
    i32.store offset=120
    local.get 0
    local.get 0
    i32.load offset=120
    i32.store8 offset=118
    local.get 0
    i32.const 0
    i32.store8 offset=119
    local.get 0
    i32.const 1038
    i32.symbolic
    i32.store offset=112
    local.get 0
    local.get 0
    i32.load offset=112
    i32.store8 offset=110
    local.get 0
    i32.const 0
    i32.store8 offset=111
    local.get 0
    i32.const 1036
    i32.symbolic
    i32.store offset=104
    local.get 0
    local.get 0
    i32.load offset=104
    i32.store8 offset=102
    local.get 0
    i32.const 0
    i32.store8 offset=103
    local.get 0
    i32.const 1034
    i32.symbolic
    i32.store offset=96
    local.get 0
    local.get 0
    i32.load offset=96
    i32.store8 offset=94
    local.get 0
    i32.const 0
    i32.store8 offset=95
    local.get 0
    i32.const 1032
    i32.symbolic
    i32.store offset=88
    local.get 0
    local.get 0
    i32.load offset=88
    i32.store8 offset=86
    local.get 0
    i32.const 0
    i32.store8 offset=87
    local.get 0
    i32.const 1030
    i32.symbolic
    i32.store offset=80
    local.get 0
    local.get 0
    i32.load offset=80
    i32.store8 offset=78
    local.get 0
    i32.const 0
    i32.store8 offset=79
    local.get 0
    i32.const 1028
    i32.symbolic
    i32.store offset=72
    local.get 0
    local.get 0
    i32.load offset=72
    i32.store8 offset=70
    local.get 0
    i32.const 0
    i32.store8 offset=71
    local.get 0
    i32.const 1026
    i32.symbolic
    i32.store offset=64
    local.get 0
    local.get 0
    i32.load offset=64
    i32.store8 offset=62
    local.get 0
    i32.const 0
    i32.store8 offset=63
    local.get 0
    i32.const 1024
    i32.symbolic
    i32.store offset=56
    local.get 0
    local.get 0
    i32.load offset=56
    i32.store8 offset=54
    local.get 0
    i32.const 0
    i32.store8 offset=55
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.const 118
      i32.add
      local.get 0
      i32.const 110
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 110
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 118
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 94
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 94
      i32.add
      local.get 0
      i32.const 110
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 118
      i32.add
      local.get 0
      i32.const 62
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 62
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 94
      i32.add
      local.get 0
      i32.const 62
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 110
      i32.add
      local.get 0
      i32.const 62
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 54
      i32.add
      local.get 0
      i32.const 86
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 54
      i32.add
      local.get 0
      i32.const 70
      i32.add
      call $strcmp
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.const 54
      i32.add
      local.get 0
      i32.const 78
      i32.add
      call $strcmp
      i32.const 0
      i32.eq
      i32.const -1
      i32.xor
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 118
    i32.add
    call $array_add
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 110
    i32.add
    call $array_add
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 102
    i32.add
    call $array_add
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 94
    i32.add
    call $array_add
    drop
    i32.const 1052
    call $array_new
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 86
    i32.add
    call $array_add
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 78
    i32.add
    call $array_add
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 70
    i32.add
    call $array_add
    drop
    local.get 0
    i32.const 32
    i32.add
    i32.const 0
    i32.load offset=1048
    i32.const 0
    i32.load offset=1052
    call $array_zip_iter_init
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.const 32
        i32.add
        local.get 0
        i32.const 28
        i32.add
        local.get 0
        i32.const 24
        i32.add
        call $array_zip_iter_next
        i32.const 9
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          local.get 0
          i32.load offset=28
          local.get 0
          i32.const 110
          i32.add
          call $strcmp
          br_if 0 (;@3;)
          local.get 0
          i32.const 32
          i32.add
          local.get 0
          i32.const 62
          i32.add
          local.get 0
          i32.const 54
          i32.add
          local.get 0
          i32.const 20
          i32.add
          local.get 0
          i32.const 16
          i32.add
          call $array_zip_iter_replace
          drop
        end
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 62
    i32.add
    local.get 0
    i32.const 12
    i32.add
    call $array_index_of
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 1
    local.get 0
    i32.load offset=12
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 54
    i32.add
    local.get 0
    i32.const 12
    i32.add
    call $array_index_of
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 1
    local.get 0
    i32.load offset=12
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 1
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 62
    i32.add
    call $array_contains
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 1
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 54
    i32.add
    call $array_contains
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1052
    call $array_destroy
    i32.const 0
    i32.load offset=1048
    call $array_destroy
    local.get 0
    i32.const 128
    i32.add
    global.set 0
    i32.const 0)
  (func $array_new (type 1) (param i32) (result i32)
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
    call $array_conf_init
    local.get 1
    i32.const 8
    i32.add
    local.get 1
    i32.load offset=28
    call $array_new_conf
    local.set 0
    local.get 1
    i32.const 32
    i32.add
    global.set 0
    local.get 0)
  (func $array_conf_init (type 2) (param i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    f32.const 0x1p+1 (;=2;)
    f32.store offset=4
    local.get 1
    i32.load offset=12
    i32.const 8
    i32.store
    local.get 1
    i32.load offset=12
    i32.const 1
    i32.store offset=8
    local.get 1
    i32.load offset=12
    i32.const 2
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.const 3
    i32.store offset=16)
  (func $array_new_conf (type 0) (param i32 i32) (result i32)
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
      i32.const 28
      local.get 2
      i32.load offset=24
      i32.load offset=12
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
      i32.load offset=8
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
        i32.load offset=16
        call_indirect (type 2)
        local.get 2
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
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
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=8
      i32.store offset=16
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=12
      i32.store offset=20
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=24
      i32.load offset=16
      i32.store offset=24
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
  (func $array_destroy (type 2) (param i32)
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
  (func $array_add (type 0) (param i32 i32) (result i32)
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
        local.get 2
        i32.load offset=8
        i32.load offset=4
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=8
        call $expand_capacity
        i32.store
        block  ;; label = @3
          local.get 2
          i32.load
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load
          i32.store offset=12
          br 2 (;@1;)
        end
      end
      local.get 2
      i32.load offset=8
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load
      i32.const 2
      i32.shl
      i32.add
      local.get 2
      i32.load offset=4
      i32.store
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
  (func $array_replace_at (type 4) (param i32 i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 4
    local.get 0
    i32.store offset=24
    local.get 4
    local.get 1
    i32.store offset=20
    local.get 4
    local.get 2
    i32.store offset=16
    local.get 4
    local.get 3
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        local.get 4
        i32.load offset=16
        local.get 4
        i32.load offset=24
        i32.load
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 4
        i32.const 8
        i32.store offset=28
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 4
        i32.load offset=12
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 4
        i32.load offset=12
        local.get 4
        i32.load offset=24
        i32.load offset=12
        local.get 4
        i32.load offset=16
        i32.const 2
        i32.shl
        i32.add
        i32.load
        i32.store
      end
      local.get 4
      i32.load offset=24
      i32.load offset=12
      local.get 4
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      local.get 4
      i32.load offset=20
      i32.store
      local.get 4
      i32.const 0
      i32.store offset=28
    end
    local.get 4
    i32.load offset=28)
  (func $array_index_of (type 5) (param i32 i32 i32) (result i32)
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
    local.get 3
    i32.const 0
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        loop  ;; label = @3
          local.get 3
          i32.load offset=12
          local.get 3
          i32.load offset=24
          i32.load
          i32.lt_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          block  ;; label = @4
            local.get 3
            i32.load offset=24
            i32.load offset=12
            local.get 3
            i32.load offset=12
            i32.const 2
            i32.shl
            i32.add
            i32.load
            local.get 3
            i32.load offset=20
            i32.eq
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 3
            i32.load offset=16
            local.get 3
            i32.load offset=12
            i32.store
            local.get 3
            i32.const 0
            i32.store offset=28
            br 3 (;@1;)
          end
          local.get 3
          local.get 3
          i32.load offset=12
          i32.const 1
          i32.add
          i32.store offset=12
          br 0 (;@3;)
        end
      end
      local.get 3
      i32.const 8
      i32.store offset=28
    end
    local.get 3
    i32.load offset=28)
  (func $array_contains (type 0) (param i32 i32) (result i32)
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
    i32.const 0
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
        i32.load
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          local.get 2
          i32.load offset=12
          i32.load offset=12
          local.get 2
          i32.load
          i32.const 2
          i32.shl
          i32.add
          i32.load
          local.get 2
          i32.load offset=8
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load offset=4
          i32.const 1
          i32.add
          i32.store offset=4
        end
        local.get 2
        local.get 2
        i32.load
        i32.const 1
        i32.add
        i32.store
        br 0 (;@2;)
      end
    end
    local.get 2
    i32.load offset=4)
  (func $array_zip_iter_init (type 6) (param i32 i32 i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 3
    local.get 0
    i32.store offset=12
    local.get 3
    local.get 1
    i32.store offset=8
    local.get 3
    local.get 2
    i32.store offset=4
    local.get 3
    i32.load offset=12
    local.get 3
    i32.load offset=8
    i32.store
    local.get 3
    i32.load offset=12
    local.get 3
    i32.load offset=4
    i32.store offset=4
    local.get 3
    i32.load offset=12
    i32.const 0
    i32.store offset=8
    local.get 3
    i32.load offset=12
    i32.const 0
    i32.store offset=12)
  (func $array_zip_iter_next (type 5) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 3
    local.get 0
    i32.store offset=8
    local.get 3
    local.get 1
    i32.store offset=4
    local.get 3
    local.get 2
    i32.store
    block  ;; label = @1
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=8
          i32.load offset=8
          local.get 3
          i32.load offset=8
          i32.load
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=8
          i32.load offset=8
          local.get 3
          i32.load offset=8
          i32.load offset=4
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 3
        i32.const 9
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 3
      i32.load offset=4
      local.get 3
      i32.load offset=8
      i32.load
      i32.load offset=12
      local.get 3
      i32.load offset=8
      i32.load offset=8
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 3
      i32.load
      local.get 3
      i32.load offset=8
      i32.load offset=4
      i32.load offset=12
      local.get 3
      i32.load offset=8
      i32.load offset=8
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 3
      i32.load offset=8
      local.tee 2
      local.get 2
      i32.load offset=8
      i32.const 1
      i32.add
      i32.store offset=8
      local.get 3
      i32.load offset=8
      i32.const 0
      i32.store offset=12
      local.get 3
      i32.const 0
      i32.store offset=12
    end
    local.get 3
    i32.load offset=12)
  (func $array_zip_iter_replace (type 7) (param i32 i32 i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 5
    global.set 0
    local.get 5
    local.get 0
    i32.store offset=24
    local.get 5
    local.get 1
    i32.store offset=20
    local.get 5
    local.get 2
    i32.store offset=16
    local.get 5
    local.get 3
    i32.store offset=12
    local.get 5
    local.get 4
    i32.store offset=8
    block  ;; label = @1
      block  ;; label = @2
        block  ;; label = @3
          local.get 5
          i32.load offset=24
          i32.load offset=8
          i32.const 1
          i32.sub
          local.get 5
          i32.load offset=24
          i32.load
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          br_if 0 (;@3;)
          local.get 5
          i32.load offset=24
          i32.load offset=8
          i32.const 1
          i32.sub
          local.get 5
          i32.load offset=24
          i32.load offset=4
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 5
        i32.const 8
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 5
      i32.load offset=24
      i32.load
      local.get 5
      i32.load offset=20
      local.get 5
      i32.load offset=24
      i32.load offset=8
      i32.const 1
      i32.sub
      local.get 5
      i32.load offset=12
      call $array_replace_at
      drop
      local.get 5
      i32.load offset=24
      i32.load offset=4
      local.get 5
      i32.load offset=16
      local.get 5
      i32.load offset=24
      i32.load offset=8
      i32.const 1
      i32.sub
      local.get 5
      i32.load offset=8
      call $array_replace_at
      drop
      local.get 5
      i32.const 0
      i32.store offset=28
    end
    local.get 5
    i32.load offset=28
    local.set 4
    local.get 5
    i32.const 32
    i32.add
    global.set 0
    local.get 4)
  (func $strcmp (type 0) (param i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 32
    i32.sub
    local.tee 2
    local.get 0
    i32.store offset=28
    local.get 2
    local.get 1
    i32.store offset=24
    local.get 2
    i32.const 0
    i32.store offset=16
    local.get 2
    local.get 2
    i32.load offset=28
    i32.store offset=12
    local.get 2
    local.get 2
    i32.load offset=24
    i32.store offset=8
    block  ;; label = @1
      local.get 2
      i32.load offset=12
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=8
      i32.const 0
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.const 0
      i32.store offset=20
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.load offset=12
          i32.load8_u
          i32.const 24
          i32.shl
          i32.const 24
          i32.shr_s
          i32.eqz
          br_if 1 (;@2;)
          block  ;; label = @4
            local.get 2
            i32.load offset=12
            i32.load8_u
            i32.const 24
            i32.shl
            i32.const 24
            i32.shr_s
            local.get 2
            i32.load offset=8
            i32.load8_u
            i32.const 24
            i32.shl
            i32.const 24
            i32.shr_s
            i32.ne
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            i32.const 1
            i32.store offset=16
          end
          local.get 2
          local.get 2
          i32.load offset=12
          i32.const 1
          i32.add
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
    end
    local.get 2
    i32.load offset=16)
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
  (table (;0;) 4 4 funcref)
  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 66592))
  (export "memory" (memory 0))
  (export "__original_main" (func $__original_main))
  (elem (;0;) (i32.const 1) $malloc $calloc $free)
  (data (;0;) (i32.const 1024) "i\00h\00g\00f\00e\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1044) " \04\01\00"))