(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func))
  (type (;4;) (func (result i32)))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (type (;6;) (func (param i32 i32 i32)))
  (import "i32" "symbolic" (func (;0;) (type 1)))
  (import "i32" "sym_assume" (func (;1;) (type 2)))
  (import "i32" "sym_assert" (func (;2;) (type 2)))
  (import "i32" "is_symbolic" (func (;3;) (type 0)))
  (import "sym" "get_sym_int32" (func (;4;) (type 1)))
  (import "mem" "alloc" (func (;5;) (type 0)))
  (import "mem" "free" (func (;6;) (type 2)))
  (func (;7;) (type 3)
    i32.const 0
    i32.const 1044
    call 16
    i32.store offset=1048)
  (func (;8;) (type 3)
    i32.const 0
    i32.load offset=1044
    call 20)
  (func (;9;) (type 4) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 112
    i32.sub
    local.tee 0
    global.set 0
    local.get 0
    i32.const 0
    i32.store offset=108
    call 7
    local.get 0
    i32.const 1036
    call 0
    i32.store offset=104
    local.get 0
    i32.load offset=104
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=104
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=104
    i32.store8 offset=102
    local.get 0
    i32.const 0
    i32.store8 offset=103
    local.get 0
    i32.const 1034
    call 0
    i32.store offset=96
    local.get 0
    i32.load offset=96
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=96
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=96
    i32.store8 offset=94
    local.get 0
    i32.const 0
    i32.store8 offset=95
    local.get 0
    i32.const 1032
    call 0
    i32.store offset=88
    local.get 0
    i32.load offset=88
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=88
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=88
    i32.store8 offset=86
    local.get 0
    i32.const 0
    i32.store8 offset=87
    local.get 0
    i32.const 1030
    call 0
    i32.store offset=80
    local.get 0
    i32.load offset=80
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=80
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=80
    i32.store8 offset=78
    local.get 0
    i32.const 0
    i32.store8 offset=79
    local.get 0
    i32.const 1028
    call 0
    i32.store offset=72
    local.get 0
    i32.load offset=72
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=72
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=72
    i32.store8 offset=70
    local.get 0
    i32.const 0
    i32.store8 offset=71
    local.get 0
    i32.const 1026
    call 0
    i32.store offset=64
    local.get 0
    i32.load offset=64
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=64
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    local.get 0
    i32.load offset=64
    i32.store8 offset=62
    local.get 0
    i32.const 0
    i32.store8 offset=63
    local.get 0
    i32.const 1024
    call 0
    i32.store offset=56
    local.get 0
    i32.load offset=56
    i32.const 0
    i32.gt_s
    i32.const 1
    i32.and
    call 1
    local.get 0
    i32.load offset=56
    i32.const 127
    i32.lt_s
    i32.const 1
    i32.and
    call 1
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
      i32.load offset=96
      local.get 0
      i32.load offset=104
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.load offset=96
      local.get 0
      i32.load offset=88
      i32.ne
      i32.const 1
      i32.and
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.load offset=96
      local.get 0
      i32.load offset=80
      i32.ne
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    call 1
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 102
    i32.add
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 94
    i32.add
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 86
    i32.add
    call 21
    drop
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 78
    i32.add
    call 21
    drop
    local.get 0
    i32.const 48
    i32.add
    call 16
    drop
    local.get 0
    i32.load offset=48
    local.get 0
    i32.const 70
    i32.add
    call 21
    drop
    local.get 0
    i32.load offset=48
    local.get 0
    i32.const 62
    i32.add
    call 21
    drop
    local.get 0
    i32.load offset=48
    local.get 0
    i32.const 54
    i32.add
    call 21
    drop
    local.get 0
    i32.const 1038
    call 0
    i32.store offset=44
    local.get 0
    local.get 0
    i32.load offset=44
    i32.store8 offset=42
    local.get 0
    i32.const 0
    i32.store8 offset=43
    local.get 0
    i32.const 1038
    call 0
    i32.store offset=36
    local.get 0
    local.get 0
    i32.load offset=36
    i32.store8 offset=34
    local.get 0
    i32.const 0
    i32.store8 offset=35
    local.get 0
    i32.const 16
    i32.add
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.load offset=48
    call 30
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.const 16
        i32.add
        local.get 0
        i32.const 12
        i32.add
        local.get 0
        i32.const 8
        i32.add
        call 31
        i32.const 9
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          local.get 0
          i32.load offset=12
          local.get 0
          i32.const 94
          i32.add
          call 10
          br_if 0 (;@3;)
          local.get 0
          i32.const 16
          i32.add
          local.get 0
          i32.const 42
          i32.add
          local.get 0
          i32.const 34
          i32.add
          call 32
          drop
        end
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 42
    i32.add
    local.get 0
    i32.const 4
    i32.add
    call 27
    drop
    i32.const 2
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 34
    i32.add
    local.get 0
    i32.const 4
    i32.add
    call 27
    drop
    i32.const 2
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 86
    i32.add
    local.get 0
    i32.const 4
    i32.add
    call 27
    drop
    i32.const 3
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    i32.const 0
    i32.load offset=1044
    local.get 0
    i32.const 42
    i32.add
    call 28
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    local.get 0
    i32.load offset=48
    local.get 0
    i32.const 34
    i32.add
    call 28
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 5
    i32.const 0
    i32.load offset=1044
    call 29
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 4
    local.get 0
    i32.load offset=48
    call 29
    i32.eq
    i32.const 1
    i32.and
    call 2
    local.get 0
    i32.load offset=48
    call 20
    call 8
    local.get 0
    i32.const 112
    i32.add
    global.set 0
    i32.const 0)
  (func (;10;) (type 0) (param i32 i32) (result i32)
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
  (func (;11;) (type 1) (param i32) (result i32)
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
  (func (;12;) (type 0) (param i32 i32) (result i32)
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
  (func (;13;) (type 2) (param i32)
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
  (func (;14;) (type 5) (param i32 i32 i32) (result i32)
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
  (func (;15;) (type 5) (param i32 i32 i32) (result i32)
    (local i32 i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 3
    local.tee 4
    local.get 0
    i32.store offset=44
    local.get 4
    local.get 1
    i32.store offset=40
    local.get 4
    local.get 2
    i32.store offset=36
    local.get 4
    local.get 4
    i32.load offset=44
    i32.store offset=32
    local.get 4
    local.get 4
    i32.load offset=40
    i32.store offset=28
    local.get 4
    i32.load offset=36
    local.set 2
    local.get 4
    local.get 3
    i32.store offset=24
    local.get 3
    local.get 2
    i32.const 15
    i32.add
    i32.const -16
    i32.and
    i32.sub
    local.tee 3
    drop
    local.get 4
    local.get 2
    i32.store offset=20
    local.get 4
    i32.const 0
    i32.store offset=16
    block  ;; label = @1
      loop  ;; label = @2
        local.get 4
        i32.load offset=16
        local.get 4
        i32.load offset=36
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 3
        local.get 4
        i32.load offset=16
        i32.add
        local.get 4
        i32.load offset=28
        local.get 4
        i32.load offset=16
        i32.add
        i32.load8_u
        i32.store8
        local.get 4
        local.get 4
        i32.load offset=16
        i32.const 1
        i32.add
        i32.store offset=16
        br 0 (;@2;)
      end
    end
    local.get 4
    i32.const 0
    i32.store offset=12
    block  ;; label = @1
      loop  ;; label = @2
        local.get 4
        i32.load offset=12
        local.get 4
        i32.load offset=36
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 4
        i32.load offset=32
        local.get 4
        i32.load offset=12
        i32.add
        local.get 3
        local.get 4
        i32.load offset=12
        i32.add
        i32.load8_u
        i32.store8
        local.get 4
        local.get 4
        i32.load offset=12
        i32.const 1
        i32.add
        i32.store offset=12
        br 0 (;@2;)
      end
    end
    local.get 4
    i32.load offset=24
    drop
    local.get 4
    i32.load offset=44)
  (func (;16;) (type 1) (param i32) (result i32)
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
    call 17
    local.get 1
    i32.const 8
    i32.add
    local.get 1
    i32.load offset=28
    call 18
    local.set 0
    local.get 1
    i32.const 32
    i32.add
    global.set 0
    local.get 0)
  (func (;17;) (type 2) (param i32)
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
  (func (;18;) (type 0) (param i32 i32) (result i32)
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
      call 19
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
  (func (;19;) (type 1) (param i32) (result i32)
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
  (func (;20;) (type 2) (param i32)
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
        call 23
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
  (func (;23;) (type 1) (param i32) (result i32)
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
      call 25
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
  (func (;24;) (type 0) (param i32 i32) (result i32)
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
        i32.load offset=8
        call 23
        i32.eqz
        br_if 0 (;@2;)
        local.get 2
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 2
      i32.load offset=8
      local.get 2
      i32.load offset=8
      i32.load offset=8
      i32.const 1
      i32.sub
      local.get 2
      i32.load offset=8
      i32.load offset=4
      i32.const 1
      i32.sub
      i32.and
      i32.store offset=8
      local.get 2
      i32.load offset=8
      i32.load offset=16
      local.get 2
      i32.load offset=8
      i32.load offset=8
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
  (func (;25;) (type 6) (param i32 i32 i32)
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
            call 14
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
          call 14
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
          call 14
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
  (func (;26;) (type 5) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 3
    global.set 0
    local.get 3
    local.get 0
    i32.store offset=40
    local.get 3
    local.get 1
    i32.store offset=36
    local.get 3
    local.get 2
    i32.store offset=32
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=32
        local.get 3
        i32.load offset=40
        i32.load
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.const 8
        i32.store offset=44
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 3
        i32.load offset=40
        i32.load offset=4
        local.get 3
        i32.load offset=40
        i32.load
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=40
        call 23
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.const 1
        i32.store offset=44
        br 1 (;@1;)
      end
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.const 1
      i32.sub
      i32.store offset=28
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=12
      local.get 3
      i32.load offset=28
      i32.and
      i32.store offset=24
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=8
      local.get 3
      i32.load offset=28
      i32.and
      i32.store offset=20
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=8
      local.get 3
      i32.load offset=32
      i32.add
      local.get 3
      i32.load offset=28
      i32.and
      i32.store offset=16
      block  ;; label = @2
        local.get 3
        i32.load offset=32
        br_if 0 (;@2;)
        local.get 3
        local.get 3
        i32.load offset=40
        local.get 3
        i32.load offset=36
        call 24
        i32.store offset=44
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 3
        i32.load offset=32
        local.get 3
        i32.load offset=28
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        local.get 3
        i32.load offset=40
        local.get 3
        i32.load offset=36
        call 22
        i32.store offset=44
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=32
          local.get 3
          i32.load offset=40
          i32.load
          i32.const 1
          i32.shr_u
          i32.const 1
          i32.sub
          i32.le_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          block  ;; label = @4
            block  ;; label = @5
              block  ;; label = @6
                local.get 3
                i32.load offset=16
                local.get 3
                i32.load offset=20
                i32.lt_u
                i32.const 1
                i32.and
                br_if 0 (;@6;)
                local.get 3
                i32.load offset=20
                br_if 1 (;@5;)
              end
              block  ;; label = @6
                block  ;; label = @7
                  local.get 3
                  i32.load offset=20
                  i32.eqz
                  br_if 0 (;@7;)
                  local.get 3
                  i32.load offset=28
                  local.get 3
                  i32.load offset=20
                  i32.sub
                  i32.const 1
                  i32.add
                  local.set 2
                  br 1 (;@6;)
                end
                i32.const 0
                local.set 2
              end
              local.get 3
              local.get 2
              i32.store offset=12
              local.get 3
              local.get 3
              i32.load offset=16
              i32.store offset=8
              local.get 3
              local.get 3
              i32.load offset=40
              i32.load offset=16
              i32.load
              i32.store offset=4
              block  ;; label = @6
                local.get 3
                i32.load offset=20
                i32.eqz
                br_if 0 (;@6;)
                local.get 3
                i32.load offset=40
                i32.load offset=16
                local.get 3
                i32.load offset=20
                i32.const 1
                i32.sub
                i32.const 2
                i32.shl
                i32.add
                local.get 3
                i32.load offset=40
                i32.load offset=16
                local.get 3
                i32.load offset=20
                i32.const 2
                i32.shl
                i32.add
                local.get 3
                i32.load offset=12
                i32.const 2
                i32.shl
                call 15
                drop
              end
              block  ;; label = @6
                local.get 3
                i32.load offset=16
                i32.eqz
                br_if 0 (;@6;)
                local.get 3
                i32.load offset=40
                i32.load offset=16
                local.get 3
                i32.load offset=40
                i32.load offset=16
                i32.const 4
                i32.add
                local.get 3
                i32.load offset=8
                i32.const 2
                i32.shl
                call 15
                drop
              end
              local.get 3
              i32.load offset=40
              i32.load offset=16
              local.get 3
              i32.load offset=28
              i32.const 2
              i32.shl
              i32.add
              local.get 3
              i32.load offset=4
              i32.store
              br 1 (;@4;)
            end
            local.get 3
            i32.load offset=40
            i32.load offset=16
            local.get 3
            i32.load offset=20
            i32.const 1
            i32.sub
            i32.const 2
            i32.shl
            i32.add
            local.get 3
            i32.load offset=40
            i32.load offset=16
            local.get 3
            i32.load offset=20
            i32.const 2
            i32.shl
            i32.add
            local.get 3
            i32.load offset=32
            i32.const 2
            i32.shl
            call 15
            drop
          end
          local.get 3
          i32.load offset=40
          local.get 3
          i32.load offset=40
          i32.load offset=8
          i32.const 1
          i32.sub
          local.get 3
          i32.load offset=28
          i32.and
          i32.store offset=8
          br 1 (;@2;)
        end
        block  ;; label = @3
          block  ;; label = @4
            block  ;; label = @5
              local.get 3
              i32.load offset=16
              local.get 3
              i32.load offset=24
              i32.gt_u
              i32.const 1
              i32.and
              br_if 0 (;@5;)
              local.get 3
              i32.load offset=24
              local.get 3
              i32.load offset=28
              i32.eq
              i32.const 1
              i32.and
              i32.eqz
              br_if 1 (;@4;)
            end
            local.get 3
            local.get 3
            i32.load offset=40
            i32.load offset=16
            local.get 3
            i32.load offset=28
            i32.const 2
            i32.shl
            i32.add
            i32.load
            i32.store
            block  ;; label = @5
              local.get 3
              i32.load offset=16
              local.get 3
              i32.load offset=28
              i32.ne
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 3
              i32.load offset=40
              i32.load offset=16
              local.get 3
              i32.load offset=16
              i32.const 1
              i32.add
              i32.const 2
              i32.shl
              i32.add
              local.get 3
              i32.load offset=40
              i32.load offset=16
              local.get 3
              i32.load offset=16
              i32.const 2
              i32.shl
              i32.add
              local.get 3
              i32.load offset=28
              local.get 3
              i32.load offset=16
              i32.sub
              i32.const 2
              i32.shl
              call 15
              drop
            end
            block  ;; label = @5
              local.get 3
              i32.load offset=24
              local.get 3
              i32.load offset=28
              i32.ne
              i32.const 1
              i32.and
              i32.eqz
              br_if 0 (;@5;)
              local.get 3
              i32.load offset=40
              i32.load offset=16
              i32.const 4
              i32.add
              local.get 3
              i32.load offset=40
              i32.load offset=16
              local.get 3
              i32.load offset=24
              i32.const 1
              i32.add
              i32.const 2
              i32.shl
              call 15
              drop
            end
            local.get 3
            i32.load offset=40
            i32.load offset=16
            local.get 3
            i32.load
            i32.store
            br 1 (;@3;)
          end
          local.get 3
          i32.load offset=40
          i32.load offset=16
          local.get 3
          i32.load offset=16
          i32.const 1
          i32.add
          i32.const 2
          i32.shl
          i32.add
          local.get 3
          i32.load offset=40
          i32.load offset=16
          local.get 3
          i32.load offset=16
          i32.const 2
          i32.shl
          i32.add
          local.get 3
          i32.load offset=40
          i32.load
          local.get 3
          i32.load offset=32
          i32.sub
          i32.const 2
          i32.shl
          call 15
          drop
        end
        local.get 3
        i32.load offset=40
        local.get 3
        i32.load offset=40
        i32.load offset=12
        i32.const 1
        i32.add
        local.get 3
        i32.load offset=28
        i32.and
        i32.store offset=12
      end
      local.get 3
      i32.load offset=40
      i32.load offset=16
      local.get 3
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      local.get 3
      i32.load offset=36
      i32.store
      local.get 3
      i32.load offset=40
      local.tee 2
      local.get 2
      i32.load
      i32.const 1
      i32.add
      i32.store
      local.get 3
      i32.const 0
      i32.store offset=44
    end
    local.get 3
    i32.load offset=44
    local.set 2
    local.get 3
    i32.const 48
    i32.add
    global.set 0
    local.get 2)
  (func (;27;) (type 5) (param i32 i32 i32) (result i32)
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
          local.get 3
          local.get 3
          i32.load offset=24
          i32.load offset=8
          local.get 3
          i32.load offset=12
          i32.add
          local.get 3
          i32.load offset=24
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.and
          i32.store offset=8
          block  ;; label = @4
            local.get 3
            i32.load offset=24
            i32.load offset=16
            local.get 3
            i32.load offset=8
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
  (func (;28;) (type 0) (param i32 i32) (result i32)
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
    i32.const 0
    i32.store offset=20
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        i32.load offset=20
        local.get 2
        i32.load offset=28
        i32.load
        i32.lt_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        local.get 2
        local.get 2
        i32.load offset=28
        i32.load offset=8
        local.get 2
        i32.load offset=20
        i32.add
        local.get 2
        i32.load offset=28
        i32.load offset=4
        i32.const 1
        i32.sub
        i32.and
        i32.store offset=12
        block  ;; label = @3
          local.get 2
          i32.load offset=28
          i32.load offset=16
          local.get 2
          i32.load offset=12
          i32.const 2
          i32.shl
          i32.add
          i32.load
          local.get 2
          i32.load offset=24
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 0 (;@3;)
          local.get 2
          local.get 2
          i32.load offset=16
          i32.const 1
          i32.add
          i32.store offset=16
        end
        local.get 2
        local.get 2
        i32.load offset=20
        i32.const 1
        i32.add
        i32.store offset=20
        br 0 (;@2;)
      end
    end
    local.get 2
    i32.load offset=16)
  (func (;29;) (type 1) (param i32) (result i32)
    (local i32)
    global.get 0
    i32.const 16
    i32.sub
    local.tee 1
    local.get 0
    i32.store offset=12
    local.get 1
    i32.load offset=12
    i32.load)
  (func (;30;) (type 6) (param i32 i32 i32)
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
  (func (;31;) (type 5) (param i32 i32 i32) (result i32)
    (local i32)
    global.get 0
    i32.const 48
    i32.sub
    local.tee 3
    local.get 0
    i32.store offset=40
    local.get 3
    local.get 1
    i32.store offset=36
    local.get 3
    local.get 2
    i32.store offset=32
    local.get 3
    local.get 3
    i32.load offset=40
    i32.load
    i32.load offset=4
    i32.const 1
    i32.sub
    i32.store offset=28
    local.get 3
    local.get 3
    i32.load offset=40
    i32.load
    i32.load offset=12
    local.get 3
    i32.load offset=28
    i32.and
    i32.store offset=24
    local.get 3
    local.get 3
    i32.load offset=40
    i32.load
    i32.load offset=8
    local.get 3
    i32.load offset=28
    i32.and
    i32.store offset=20
    block  ;; label = @1
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=24
          local.get 3
          i32.load offset=20
          i32.eq
          i32.const 1
          i32.and
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=40
          i32.load offset=8
          local.get 3
          i32.load offset=40
          i32.load
          i32.load
          i32.ge_u
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 3
        i32.const 9
        i32.store offset=44
        br 1 (;@1;)
      end
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.load offset=4
      i32.const 1
      i32.sub
      i32.store offset=16
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.load offset=12
      local.get 3
      i32.load offset=16
      i32.and
      i32.store offset=12
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.load offset=8
      local.get 3
      i32.load offset=16
      i32.and
      i32.store offset=8
      block  ;; label = @2
        block  ;; label = @3
          local.get 3
          i32.load offset=12
          local.get 3
          i32.load offset=8
          i32.eq
          i32.const 1
          i32.and
          br_if 0 (;@3;)
          local.get 3
          i32.load offset=40
          i32.load offset=8
          local.get 3
          i32.load offset=40
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
        i32.store offset=44
        br 1 (;@1;)
      end
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load
      i32.load offset=8
      local.get 3
      i32.load offset=40
      i32.load offset=8
      i32.add
      local.get 3
      i32.load offset=28
      i32.and
      i32.store offset=4
      local.get 3
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.load offset=8
      local.get 3
      i32.load offset=40
      i32.load offset=8
      i32.add
      local.get 3
      i32.load offset=16
      i32.and
      i32.store
      local.get 3
      i32.load offset=36
      local.get 3
      i32.load offset=40
      i32.load
      i32.load offset=16
      local.get 3
      i32.load offset=4
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 3
      i32.load offset=32
      local.get 3
      i32.load offset=40
      i32.load offset=4
      i32.load offset=16
      local.get 3
      i32.load
      i32.const 2
      i32.shl
      i32.add
      i32.load
      i32.store
      local.get 3
      i32.load offset=40
      local.tee 2
      local.get 2
      i32.load offset=8
      i32.const 1
      i32.add
      i32.store offset=8
      local.get 3
      i32.load offset=40
      i32.const 0
      i32.store offset=12
      local.get 3
      i32.const 0
      i32.store offset=44
    end
    local.get 3
    i32.load offset=44)
  (func (;32;) (type 5) (param i32 i32 i32) (result i32)
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
        i32.const 8
        i32.store offset=12
        br 1 (;@1;)
      end
      block  ;; label = @2
        local.get 3
        i32.load offset=8
        i32.load
        i32.load offset=4
        local.get 3
        i32.load offset=8
        i32.load
        i32.load
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=8
        i32.load
        call 23
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=8
        i32.load offset=4
        i32.load offset=4
        local.get 3
        i32.load offset=8
        i32.load offset=4
        i32.load
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.load offset=8
        i32.load offset=4
        call 23
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        i32.const 1
        i32.store offset=12
        br 1 (;@1;)
      end
      local.get 3
      i32.load offset=8
      i32.load
      local.get 3
      i32.load offset=4
      local.get 3
      i32.load offset=8
      i32.load offset=8
      call 26
      drop
      local.get 3
      i32.load offset=8
      i32.load offset=4
      local.get 3
      i32.load
      local.get 3
      i32.load offset=8
      i32.load offset=8
      call 26
      drop
      local.get 3
      i32.load offset=8
      local.tee 2
      local.get 2
      i32.load offset=8
      i32.const 1
      i32.add
      i32.store offset=8
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
  (func (;33;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;34;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;35;) (type 0) (param i32 i32) (result i32)
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
  (func (;36;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;37;) (type 1) (param i32) (result i32)
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
  (elem (;0;) (i32.const 1) func 11 12 13)
  (data (;0;) (i32.const 1024) "g\00f\00e\00d\00c\00b\00a\00X\00")
  (data (;1;) (i32.const 1040) " \04\01\00"))
