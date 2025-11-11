(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32)))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func (param i32 i32 i32) (result i32)))
  (type (;5;) (func (param i32 i32)))
  (type (;6;) (func (param i32 i32 i32)))
  (func $__original_main (type 3) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=12
    i32.const 1
    i32.const 1036
    call $treeset_new
    drop
    local.get 0
    i32.const 1028
    i32.symbolic
    i32.store offset=8
    local.get 0
    i32.const 1026
    i32.symbolic
    i32.store offset=4
    local.get 0
    i32.const 1024
    i32.symbolic
    i32.store
    i32.const 0
    local.set 1
    block  ;; label = @1
      local.get 0
      i32.load offset=8
      local.get 0
      i32.load offset=4
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.load offset=8
      local.get 0
      i32.load
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=4
      local.get 0
      i32.load
      i32.ne
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    sym_assume
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 8
    i32.add
    call $treeset_add
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 4
    i32.add
    call $treeset_add
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    call $treeset_add
    drop
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 8
    i32.add
    i32.const 0
    call $treeset_remove
    drop
    i32.const 2
    i32.const 0
    i32.load offset=1036
    call $treeset_size
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    i32.const 0
    i32.const 0
    i32.load offset=1036
    local.get 0
    i32.const 8
    i32.add
    call $treeset_contains
    i32.eq
    i32.const 1
    i32.and
    sym_assert
    local.get 0
    i32.load offset=12
    local.set 1
    local.get 0
    i32.const 16
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
  (func $treeset_conf_init (type 1) (param i32)
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
    call $treetable_conf_init
    local.get 1
    i32.const 16
    i32.add
    global.set 0)
  (func $treeset_new (type 0) (param i32 i32) (result i32)
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
    call $treeset_conf_init
    local.get 2
    local.get 2
    i32.load offset=28
    i32.store offset=8
    local.get 2
    i32.const 8
    i32.add
    local.get 2
    i32.load offset=24
    call $treeset_new_conf
    local.set 1
    local.get 2
    i32.const 32
    i32.add
    global.set 0
    local.get 1)
  (func $treeset_new_conf (type 0) (param i32 i32) (result i32)
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
    i32.const 20
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
      local.get 2
      i32.load offset=24
      local.get 2
      i32.const 12
      i32.add
      call $treetable_new_conf
      i32.store offset=8
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        i32.load offset=12
        call_indirect (type 1)
        local.get 2
        local.get 2
        i32.load offset=8
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=12
      i32.store
      local.get 2
      i32.load offset=16
      i32.const 1
      i32.store offset=4
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=4
      i32.store offset=8
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=8
      i32.store offset=12
      local.get 2
      i32.load offset=16
      local.get 2
      i32.load offset=24
      i32.load offset=12
      i32.store offset=16
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
  (func $treeset_add (type 0) (param i32 i32) (result i32)
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
    i32.load
    local.get 2
    i32.load offset=8
    local.get 2
    i32.load offset=12
    i32.load offset=4
    call $treetable_add
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func $treeset_remove (type 4) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 3
    global.set 0
    local.get 3
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
        local.get 3
        i32.load offset=8
        i32.load
        local.get 3
        i32.load offset=4
        local.get 3
        i32.load
        call $treetable_remove
        i32.const 6
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.const 7
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 3
      i32.const 0
      i32.store offset=12
    end
    local.get 3
    i32.load offset=12
    local.set 2
    local.get 3
    i32.const 16
    i32.add
    global.set 0
    local.get 2)
  (func $treeset_contains (type 0) (param i32 i32) (result i32)
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
    i32.load
    local.get 2
    i32.load offset=8
    call $treetable_contains_key
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func $treeset_size (type 2) (param i32) (result i32)
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
    i32.load
    call $treetable_size
    local.set 0
    local.get 1
    i32.const 16
    i32.add
    global.set 0
    local.get 0)
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
  (func $treetable_size (type 2) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.load offset=8)
  (func $treetable_contains_key (type 0) (param i32 i32) (result i32)
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
    call $get_tree_node_by_key
    i32.store
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load
        i32.const 0
        i32.ne
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
  (func $treetable_add (type 4) (param i32 i32 i32) (result i32)
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
  (func $rebalance_after_insert (type 5) (param i32 i32)
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
  (func $rotate_left (type 5) (param i32 i32)
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
  (func $rotate_right (type 5) (param i32 i32)
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
  (func $treetable_remove (type 4) (param i32 i32 i32) (result i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=12
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        br_if 0 (;@2;)
        local.get 3
        i32.const 6
        i32.store offset=28
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 3
        i32.load offset=16
        i32.const 0
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=16
        local.get 3
        i32.load offset=12
        i32.load offset=4
        i32.store
      end
      local.get 3
      i32.load offset=24
      local.get 3
      i32.load offset=12
      call $remove_node
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
  (func $remove_node (type 5) (param i32 i32)
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
    i32.store offset=16
    local.get 2
    local.get 2
    i32.load offset=16
    i32.load8_u offset=8
    i32.const 24
    i32.shl
    i32.const 24
    i32.shr_s
    i32.store offset=12
    block  ;; label = @1
      block  ;; label = @2
        local.get 2
        i32.load offset=24
        i32.load offset=16
        local.get 2
        i32.load offset=28
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        local.get 2
        i32.load offset=24
        i32.load offset=20
        i32.store offset=20
        local.get 2
        i32.load offset=28
        local.get 2
        i32.load offset=24
        local.get 2
        i32.load offset=24
        i32.load offset=20
        call $transplant
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          local.get 2
          i32.load offset=24
          i32.load offset=20
          local.get 2
          i32.load offset=28
          i32.load offset=4
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load offset=24
          i32.load offset=16
          i32.store offset=20
          local.get 2
          i32.load offset=28
          local.get 2
          i32.load offset=24
          local.get 2
          i32.load offset=24
          i32.load offset=16
          call $transplant
          br 1 (;@2;)
        end
        local.get 2
        local.get 2
        i32.load offset=28
        local.get 2
        i32.load offset=24
        i32.load offset=20
        call $tree_min
        i32.store offset=16
        local.get 2
        local.get 2
        i32.load offset=16
        i32.load8_u offset=8
        i32.const 24
        i32.shl
        i32.const 24
        i32.shr_s
        i32.store offset=12
        local.get 2
        local.get 2
        i32.load offset=16
        i32.load offset=20
        i32.store offset=20
        block  ;; label = @3
          block  ;; label = @4
            local.get 2
            i32.load offset=16
            i32.load offset=12
            local.get 2
            i32.load offset=24
            i32.eq
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 2
            i32.load offset=20
            local.get 2
            i32.load offset=16
            i32.store offset=12
            br 1 (;@3;)
          end
          local.get 2
          i32.load offset=28
          local.get 2
          i32.load offset=16
          local.get 2
          i32.load offset=16
          i32.load offset=20
          call $transplant
          local.get 2
          i32.load offset=16
          local.get 2
          i32.load offset=24
          i32.load offset=20
          i32.store offset=20
          local.get 2
          i32.load offset=16
          i32.load offset=20
          local.get 2
          i32.load offset=16
          i32.store offset=12
        end
        local.get 2
        i32.load offset=28
        local.get 2
        i32.load offset=24
        local.get 2
        i32.load offset=16
        call $transplant
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        i32.load offset=16
        i32.store offset=16
        local.get 2
        i32.load offset=16
        i32.load offset=16
        local.get 2
        i32.load offset=16
        i32.store offset=12
        local.get 2
        i32.load offset=16
        local.get 2
        i32.load offset=24
        i32.load8_u offset=8
        i32.store8 offset=8
      end
    end
    block  ;; label = @1
      local.get 2
      i32.load offset=12
      i32.const 1
      i32.eq
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 2
      i32.load offset=28
      local.get 2
      i32.load offset=20
      call $rebalance_after_delete
    end
    local.get 2
    i32.load offset=24
    local.get 2
    i32.load offset=28
    i32.load offset=24
    call_indirect (type 1)
    local.get 2
    i32.load offset=28
    local.tee 1
    local.get 1
    i32.load offset=8
    i32.const -1
    i32.add
    i32.store offset=8
    local.get 2
    i32.const 32
    i32.add
    global.set 0)
  (func $transplant (type 6) (param i32 i32 i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=8
        i32.load offset=12
        local.get 3
        i32.load offset=12
        i32.load offset=4
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=12
        local.get 3
        i32.load offset=4
        i32.store
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=8
          local.get 3
          i32.load offset=8
          i32.load offset=12
          i32.load offset=16
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=8
          i32.load offset=12
          local.get 3
          i32.load offset=4
          i32.store offset=16
          br 1 (;@2;)
        end
        local.get 3
        i32.load offset=8
        i32.load offset=12
        local.get 3
        i32.load offset=4
        i32.store offset=20
      end
    end
    local.get 3
    i32.load offset=4
    local.get 3
    i32.load offset=8
    i32.load offset=12
    i32.store offset=12)
  (func $rebalance_after_delete (type 5) (param i32 i32)
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
    loop  ;; label = @1
      i32.const 0
      local.set 1
      block  ;; label = @2
        local.get 2
        i32.load offset=8
        local.get 2
        i32.load offset=12
        i32.load
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.load offset=8
        i32.load8_u offset=8
        i32.const 24
        i32.shl
        i32.const 24
        i32.shr_s
        i32.const 1
        i32.eq
        local.set 1
      end
      block  ;; label = @2
        local.get 1
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        block  ;; label = @3
          block  ;; label = @4
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
            br_if 0 (;@4;)
            local.get 2
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=20
            i32.store offset=4
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
              i32.load offset=4
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.const 0
              i32.store8 offset=8
              local.get 2
              i32.load offset=12
              local.get 2
              i32.load offset=8
              i32.load offset=12
              call $rotate_left
              local.get 2
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=20
              i32.store offset=4
            end
            block  ;; label = @5
              block  ;; label = @6
                local.get 2
                i32.load offset=4
                i32.load offset=16
                i32.load8_u offset=8
                i32.const 24
                i32.shl
                i32.const 24
                i32.shr_s
                i32.const 1
                i32.eq
                i32.const 1
                i32.and
                i32.eqz
                br_if 0 (;@6;)
                local.get 2
                i32.load offset=4
                i32.load offset=20
                i32.load8_u offset=8
                i32.const 24
                i32.shl
                i32.const 24
                i32.shr_s
                i32.const 1
                i32.eq
                i32.const 1
                i32.and
                i32.eqz
                br_if 0 (;@6;)
                local.get 2
                i32.load offset=4
                i32.const 0
                i32.store8 offset=8
                local.get 2
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.store offset=8
                br 1 (;@5;)
              end
              block  ;; label = @6
                local.get 2
                i32.load offset=4
                i32.load offset=20
                i32.load8_u offset=8
                i32.const 24
                i32.shl
                i32.const 24
                i32.shr_s
                i32.const 1
                i32.eq
                i32.const 1
                i32.and
                i32.eqz
                br_if 0 (;@6;)
                local.get 2
                i32.load offset=4
                i32.load offset=16
                i32.const 1
                i32.store8 offset=8
                local.get 2
                i32.load offset=4
                i32.const 0
                i32.store8 offset=8
                local.get 2
                i32.load offset=12
                local.get 2
                i32.load offset=4
                call $rotate_right
                local.get 2
                local.get 2
                i32.load offset=8
                i32.load offset=12
                i32.load offset=20
                i32.store offset=4
              end
              local.get 2
              i32.load offset=4
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load8_u offset=8
              i32.store8 offset=8
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=4
              i32.load offset=20
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=12
              local.get 2
              i32.load offset=8
              i32.load offset=12
              call $rotate_left
              local.get 2
              local.get 2
              i32.load offset=12
              i32.load
              i32.store offset=8
            end
            br 1 (;@3;)
          end
          local.get 2
          local.get 2
          i32.load offset=8
          i32.load offset=12
          i32.load offset=16
          i32.store offset=4
          block  ;; label = @4
            local.get 2
            i32.load offset=4
            i32.load8_u offset=8
            i32.const 24
            i32.shl
            i32.const 24
            i32.shr_s
            br_if 0 (;@4;)
            local.get 2
            i32.load offset=4
            i32.const 1
            i32.store8 offset=8
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.const 0
            i32.store8 offset=8
            local.get 2
            i32.load offset=12
            local.get 2
            i32.load offset=8
            i32.load offset=12
            call $rotate_right
            local.get 2
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load offset=16
            i32.store offset=4
          end
          block  ;; label = @4
            block  ;; label = @5
              local.get 2
              i32.load offset=4
              i32.load offset=20
              i32.load8_u offset=8
              i32.const 24
              i32.shl
              i32.const 24
              i32.shr_s
              i32.const 1
              i32.eq
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 2
              i32.load offset=4
              i32.load offset=16
              i32.load8_u offset=8
              i32.const 24
              i32.shl
              i32.const 24
              i32.shr_s
              i32.const 1
              i32.eq
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 2
              i32.load offset=4
              i32.const 0
              i32.store8 offset=8
              local.get 2
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.store offset=8
              br 1 (;@4;)
            end
            block  ;; label = @5
              local.get 2
              i32.load offset=4
              i32.load offset=16
              i32.load8_u offset=8
              i32.const 24
              i32.shl
              i32.const 24
              i32.shr_s
              i32.const 1
              i32.eq
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 2
              i32.load offset=4
              i32.load offset=20
              i32.const 1
              i32.store8 offset=8
              local.get 2
              i32.load offset=4
              i32.const 0
              i32.store8 offset=8
              local.get 2
              i32.load offset=12
              local.get 2
              i32.load offset=4
              call $rotate_left
              local.get 2
              local.get 2
              i32.load offset=8
              i32.load offset=12
              i32.load offset=16
              i32.store offset=4
            end
            local.get 2
            i32.load offset=4
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.load8_u offset=8
            i32.store8 offset=8
            local.get 2
            i32.load offset=8
            i32.load offset=12
            i32.const 1
            i32.store8 offset=8
            local.get 2
            i32.load offset=4
            i32.load offset=16
            i32.const 1
            i32.store8 offset=8
            local.get 2
            i32.load offset=12
            local.get 2
            i32.load offset=8
            i32.load offset=12
            call $rotate_right
            local.get 2
            local.get 2
            i32.load offset=12
            i32.load
            i32.store offset=8
          end
        end
        br 1 (;@1;)
      end
    end
    local.get 2
    i32.load offset=8
    i32.const 1
    i32.store8 offset=8
    local.get 2
    i32.const 16
    i32.add
    global.set 0)
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
  (global (;0;) (mut i32) (i32.const 66576))
  (export "memory" (memory 0))
  (export "__original_main" (func $__original_main))
  (elem (;0;) (i32.const 1) $cmp $malloc $calloc $free)
  (data (;0;) (i32.const 1024) "c\00b\00a\00")
  (data (;1;) (i32.const 1032) "\10\04\01\00"))