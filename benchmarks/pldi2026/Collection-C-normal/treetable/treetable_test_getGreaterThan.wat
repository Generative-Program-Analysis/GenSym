(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32 i32)))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (func $__original_main (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 64
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=60
    i32.const 1
    i32.const 1044
    call $treetable_new
    drop
    local.get 0
    i32.const 1028
    i32.symbolic
    i32.store offset=56
    local.get 0
    i32.const 1026
    i32.symbolic
    i32.store offset=52
    local.get 0
    i32.const 1024
    i32.symbolic
    i32.store offset=48
    local.get 0
    i32.const 1030
    i32.symbolic
    i32.store offset=44
    local.get 0
    i32.const 1038
    i32.symbolic
    i32.store offset=40
    local.get 0
    local.get 0
    i32.load offset=40
    i32.store8 offset=38
    local.get 0
    i32.const 0
    i32.store8 offset=39
    local.get 0
    i32.const 1036
    i32.symbolic
    i32.store offset=32
    local.get 0
    local.get 0
    i32.load offset=32
    i32.store8 offset=30
    local.get 0
    i32.const 0
    i32.store8 offset=31
    local.get 0
    i32.const 1034
    i32.symbolic
    i32.store offset=24
    local.get 0
    local.get 0
    i32.load offset=24
    i32.store8 offset=22
    local.get 0
    i32.const 0
    i32.store8 offset=23
    local.get 0
    i32.const 1032
    i32.symbolic
    i32.store offset=16
    local.get 0
    local.get 0
    i32.load offset=16
    i32.store8 offset=14
    local.get 0
    i32.const 0
    i32.store8 offset=15
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=56
      local.get 0
      i32.load offset=52
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.load offset=52
      local.get 0
      i32.load offset=48
      i32.lt_s
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=48
      local.get 0
      i32.load offset=44
      i32.lt_s
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 48
    i32.add
    local.get 0
    i32.const 38
    i32.add
    call $treetable_add
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 44
    i32.add
    local.get 0
    i32.const 30
    i32.add
    call $treetable_add
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 52
    i32.add
    local.get 0
    i32.const 22
    i32.add
    call $treetable_add
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 56
    i32.add
    local.get 0
    i32.const 14
    i32.add
    call $treetable_add
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 52
    i32.add
    local.get 0
    i32.const 8
    i32.add
    call $treetable_get_greater_than
    drop
    local.get 0
    i32.load offset=48
    local.get 0
    i32.load offset=8
    i32.load
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.load offset=1044
    call $treetable_destroy
    local.get 0
    i32.load offset=60
    local.set 1
    local.get 0
    i32.const 64
    i32.add
    global.set 0
    local.get 1)
  (func $malloc (type 2) (param i32) (result i32)
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
  (func $treetable_conf_init (type 1) (param i32)
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
  (func $treetable_new (type 0) (param i32 i32) (result i32)
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
    call $treetable_conf_init
    local.get 2
    local.get 2
    i32.load offset=28
    i32.store offset=8
    local.get 2
    i32.const 8
    i32.add
    local.get 2
    i32.load offset=24
    call $treetable_new_conf
    local.set 1
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    local.get 1)
  (func $treetable_new_conf (type 0) (param i32 i32) (result i32)
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
  (func $treetable_destroy (type 1) (param i32)
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
    call $tree_destroy
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
  (func $tree_destroy (type 4) (param i32 i32)
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
      call $tree_destroy
      local.get 2
      i32.load offset=12
      local.get 2
      i32.load offset=8
      i32.load offset=20
      call $tree_destroy
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
  (func $get_tree_node_by_key (type 0) (param i32 i32) (result i32)
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
        i32.load offset=8
        br_if 0 (;@2;)
        local.get 2
        i32.const 0
        i32.store offset=28
        br 1 (;@1;)
      end
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
      loop  ;; label = @2
        local.get 2
        local.get 2
        i32.load offset=20
        local.get 2
        i32.load offset=16
        i32.load
        local.get 2
        i32.load offset=24
        i32.load offset=12
        call_indirect (type 0)
        i32.store offset=8
        block  ;; label = @3
          block  ;; label = @4
            local.get 2
            i32.load offset=8
            i32.const 0
            i32.lt_s
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            local.get 2
            i32.load offset=16
            i32.load offset=16
            i32.store offset=16
            br 1 (;@3;)
          end
          block  ;; label = @4
            block  ;; label = @5
              local.get 2
              i32.load offset=8
              i32.const 0
              i32.gt_s
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 2
              local.get 2
              i32.load offset=16
              i32.load offset=20
              i32.store offset=16
              br 1 (;@4;)
            end
            local.get 2
            local.get 2
            i32.load offset=16
            i32.store offset=28
            br 3 (;@1;)
          end
        end
        i32.const 0
        local.set 1
        block  ;; label = @3
          local.get 2
          i32.load offset=16
          local.get 2
          i32.load offset=12
          i32.ne
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          i32.load offset=8
          i32.const 0
          i32.ne
          local.set 1
        end
        local.get 1
        i32.const 1
        i32.and
        br_if 0 (;@2;)
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
  (func $tree_min (type 0) (param i32 i32) (result i32)
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
  (func $treetable_get_greater_than (type 5) (param i32 i32 i32) (result i32)
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
    local.get 3
    i32.load offset=20
    call $get_tree_node_by_key
    i32.store offset=12
    local.get 3
    local.get 3
    i32.load offset=24
    local.get 3
    i32.load offset=12
    call $get_successor_node
    i32.store offset=8
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=12
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=8
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=16
        local.get 3
        i32.load offset=8
        i32.load
        i32.store
        local.get 3
        i32.const 0
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 3
      i32.const 6
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
  (func $get_successor_node (type 0) (param i32 i32) (result i32)
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
        call $tree_min
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
  (func $treetable_add (type 5) (param i32 i32 i32) (result i32)
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
        call $rebalance_after_insert
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
  (func $rebalance_after_insert (type 4) (param i32 i32)
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
                call $rotate_left
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
              call $rotate_right
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
              call $rotate_right
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
            call $rotate_left
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
  (func $rotate_left (type 4) (param i32 i32)
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
  (func $rotate_right (type 4) (param i32 i32)
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
  (func $cmp (type 0) (param i32 i32) (result i32)
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
  (func $assume (type 1) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
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
  (func $sym_int (type 2) (param i32) (result i32)
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
  (export "__original_main" (func $__original_main))
  (elem (;0;) (i32.const 1) $cmp $malloc $calloc $free)
  (data (;0;) (i32.const 1024) "z\00y\00x\00w\00d\00c\00b\00a\00")
  (data (;1;) (i32.const 1040) " \04\01\00"))