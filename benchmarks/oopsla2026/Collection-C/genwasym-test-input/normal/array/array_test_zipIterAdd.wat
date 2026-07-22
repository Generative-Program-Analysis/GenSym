(module
  (type (;0;) (func (param i32 i32) (result i32)))
  (type (;1;) (func (param i32) (result i32)))
  (type (;2;) (func (param i32)))
  (type (;3;) (func (result i32)))
  (type (;4;) (func))
  (type (;5;) (func (param i32 i32 i32) (result i32)))
  (type (;6;) (func (param i32 i32 i32)))
  (import "i32" "symbolic" (func (;0;) (type 1)))
  (import "i32" "sym_assume" (func (;1;) (type 2)))
  (import "i32" "sym_assert" (func (;2;) (type 2)))
  (import "i32" "is_symbolic" (func (;3;) (type 0)))
  (import "sym" "get_sym_int32" (func (;4;) (type 1)))
  (import "mem" "alloc" (func (;5;) (type 0)))
  (import "mem" "free" (func (;6;) (type 2)))
  (func (;7;) (type 3) (result i32)
    (local i32 i32 i32 i32)
    global.get 0
    i32.const 112
    i32.sub
    local.tee 0
    global.set 0
    i32.const 0
    local.set 1
    local.get 0
    i32.const 0
    i32.store offset=108
    i32.const 1048
    call 15
    drop
    local.get 0
    i32.const 1024
    call 0
    i32.store offset=104
    local.get 0
    local.get 0
    i32.load offset=104
    i32.store8 offset=102
    local.get 0
    i32.const 0
    i32.store8 offset=103
    local.get 0
    i32.const 1026
    call 0
    i32.store offset=96
    local.get 0
    local.get 0
    i32.load offset=96
    i32.store8 offset=94
    local.get 0
    i32.const 0
    i32.store8 offset=95
    local.get 0
    i32.const 1028
    call 0
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
    call 0
    i32.store offset=80
    local.get 0
    local.get 0
    i32.load offset=80
    i32.store8 offset=78
    local.get 0
    i32.const 0
    i32.store8 offset=79
    local.get 0
    i32.const 1032
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
    i32.const 1034
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
    i32.const 1036
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
    i32.const 1038
    call 0
    i32.store offset=48
    local.get 0
    local.get 0
    i32.load offset=48
    i32.store8 offset=46
    local.get 0
    i32.const 0
    i32.store8 offset=47
    local.get 0
    i32.const 1040
    call 0
    i32.store offset=40
    local.get 0
    local.get 0
    i32.load offset=40
    i32.store8 offset=38
    local.get 0
    i32.const 0
    i32.store8 offset=39
    block  ;; label = @1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 94
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 86
      i32.add
      local.get 0
      i32.const 94
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 86
      i32.add
      local.get 0
      i32.const 102
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 86
      i32.add
      local.get 0
      i32.const 78
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 78
      i32.add
      local.get 0
      i32.const 94
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 102
      i32.add
      local.get 0
      i32.const 46
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 86
      i32.add
      local.get 0
      i32.const 46
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 78
      i32.add
      local.get 0
      i32.const 46
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 94
      i32.add
      local.get 0
      i32.const 46
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 38
      i32.add
      local.get 0
      i32.const 70
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 38
      i32.add
      local.get 0
      i32.const 54
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.const 38
      i32.add
      local.get 0
      i32.const 62
      i32.add
      call 9
      i32.const 0
      i32.eq
      i32.const -1
      i32.xor
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    call 1
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 102
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 94
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 86
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 78
    i32.add
    call 19
    drop
    i32.const 1052
    call 15
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 70
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 62
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 54
    i32.add
    call 19
    drop
    local.get 0
    i32.const 16
    i32.add
    i32.const 0
    i32.load offset=1048
    i32.const 0
    i32.load offset=1052
    call 25
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
        call 26
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
          call 9
          br_if 0 (;@3;)
          local.get 0
          i32.const 16
          i32.add
          local.get 0
          i32.const 46
          i32.add
          local.get 0
          i32.const 38
          i32.add
          call 27
          drop
        end
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 46
    i32.add
    local.tee 2
    local.get 0
    i32.const 4
    i32.add
    local.tee 1
    call 22
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 2
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 38
    i32.add
    local.tee 3
    local.get 1
    call 22
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 2
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 86
    i32.add
    local.get 1
    call 22
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 3
    local.get 0
    i32.load offset=4
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    i32.const 0
    i32.load offset=1048
    local.get 2
    call 23
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 1
    i32.const 0
    i32.load offset=1052
    local.get 3
    call 23
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 5
    i32.const 0
    i32.load offset=1048
    call 24
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 4
    i32.const 0
    i32.load offset=1052
    call 24
    i32.eq
    i32.const 1
    i32.and
    call 2
    i32.const 0
    i32.load offset=1052
    call 18
    i32.const 0
    i32.load offset=1048
    call 18
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    call 8
    local.get 0
    i32.const 112
    i32.add
    global.set 0
    i32.const 0)
  (func (;8;) (type 4)
    (local i32 i32)
    global.get 0
    i32.const 112
    i32.sub
    local.tee 0
    global.set 0
    i32.const 0
    local.set 1
    i32.const 1048
    call 15
    drop
    local.get 0
    i32.const 1
    i32.store offset=108
    local.get 0
    local.get 0
    i32.load offset=108
    i32.store8 offset=106
    local.get 0
    i32.const 0
    i32.store8 offset=107
    local.get 0
    i32.const 2
    i32.store offset=100
    local.get 0
    local.get 0
    i32.load offset=100
    i32.store8 offset=98
    local.get 0
    i32.const 0
    i32.store8 offset=99
    local.get 0
    i32.const 3
    i32.store offset=92
    local.get 0
    local.get 0
    i32.load offset=92
    i32.store8 offset=90
    local.get 0
    i32.const 0
    i32.store8 offset=91
    local.get 0
    i32.const 4
    i32.store offset=84
    local.get 0
    local.get 0
    i32.load offset=84
    i32.store8 offset=82
    local.get 0
    i32.const 0
    i32.store8 offset=83
    local.get 0
    i32.const 5
    i32.store offset=76
    local.get 0
    local.get 0
    i32.load offset=76
    i32.store8 offset=74
    local.get 0
    i32.const 0
    i32.store8 offset=75
    local.get 0
    i32.const 6
    i32.store offset=68
    local.get 0
    local.get 0
    i32.load offset=68
    i32.store8 offset=66
    local.get 0
    i32.const 0
    i32.store8 offset=67
    local.get 0
    i32.const 7
    i32.store offset=60
    local.get 0
    local.get 0
    i32.load offset=60
    i32.store8 offset=58
    local.get 0
    i32.const 0
    i32.store8 offset=59
    local.get 0
    i32.const 8
    i32.store offset=52
    local.get 0
    local.get 0
    i32.load offset=52
    i32.store8 offset=50
    local.get 0
    i32.const 0
    i32.store8 offset=51
    local.get 0
    i32.const 9
    i32.store offset=44
    local.get 0
    local.get 0
    i32.load offset=44
    i32.store8 offset=42
    local.get 0
    i32.const 0
    i32.store8 offset=43
    block  ;; label = @1
      local.get 0
      i32.const 106
      i32.add
      local.get 0
      i32.const 98
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 90
      i32.add
      local.get 0
      i32.const 98
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 90
      i32.add
      local.get 0
      i32.const 106
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 90
      i32.add
      local.get 0
      i32.const 82
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 82
      i32.add
      local.get 0
      i32.const 98
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 106
      i32.add
      local.get 0
      i32.const 50
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 90
      i32.add
      local.get 0
      i32.const 50
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 82
      i32.add
      local.get 0
      i32.const 50
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 98
      i32.add
      local.get 0
      i32.const 50
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 42
      i32.add
      local.get 0
      i32.const 74
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      i32.const 0
      local.set 1
      local.get 0
      i32.const 42
      i32.add
      local.get 0
      i32.const 58
      i32.add
      call 9
      i32.eqz
      br_if 0 (;@1;)
      local.get 0
      i32.const 42
      i32.add
      local.get 0
      i32.const 66
      i32.add
      call 9
      i32.const 0
      i32.eq
      i32.const -1
      i32.xor
      local.set 1
    end
    local.get 1
    i32.const 1
    i32.and
    call 1
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 106
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 98
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 90
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1048
    local.get 0
    i32.const 82
    i32.add
    call 19
    drop
    i32.const 1052
    call 15
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 74
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 66
    i32.add
    call 19
    drop
    i32.const 0
    i32.load offset=1052
    local.get 0
    i32.const 58
    i32.add
    call 19
    drop
    local.get 0
    i32.const 24
    i32.add
    i32.const 0
    i32.load offset=1048
    i32.const 0
    i32.load offset=1052
    call 25
    block  ;; label = @1
      loop  ;; label = @2
        local.get 0
        i32.const 24
        i32.add
        local.get 0
        i32.const 20
        i32.add
        local.get 0
        i32.const 16
        i32.add
        call 26
        i32.const 9
        i32.ne
        i32.const 1
        i32.and
        i32.eqz
        br_if 1 (;@1;)
        block  ;; label = @3
          local.get 0
          i32.load offset=20
          local.get 0
          i32.const 98
          i32.add
          call 9
          br_if 0 (;@3;)
          local.get 0
          i32.const 24
          i32.add
          local.get 0
          i32.const 50
          i32.add
          local.get 0
          i32.const 42
          i32.add
          call 27
          drop
        end
        br 0 (;@2;)
      end
    end
    i32.const 0
    i32.load offset=1052
    call 18
    i32.const 0
    i32.load offset=1048
    call 18
    local.get 0
    i32.const 112
    i32.add
    global.set 0)
  (func (;9;) (type 0) (param i32 i32) (result i32)
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
  (func (;10;) (type 1) (param i32) (result i32)
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
    call 5
    local.set 1
    local.get 2
    i32.const 16
    i32.add
    global.set 0
    local.get 1)
  (func (;12;) (type 2) (param i32)
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
  (func (;13;) (type 5) (param i32 i32 i32) (result i32)
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
  (func (;14;) (type 5) (param i32 i32 i32) (result i32)
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
  (func (;15;) (type 1) (param i32) (result i32)
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
    call 16
    local.get 0
    local.get 1
    i32.load offset=28
    call 17
    local.set 0
    local.get 1
    i32.const 32
    i32.add
    global.set 0
    local.get 0)
  (func (;16;) (type 2) (param i32)
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
  (func (;17;) (type 0) (param i32 i32) (result i32)
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
  (func (;18;) (type 2) (param i32)
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
  (func (;19;) (type 0) (param i32 i32) (result i32)
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
        call 20
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
  (func (;20;) (type 1) (param i32) (result i32)
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
      call 13
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
  (func (;21;) (type 5) (param i32 i32 i32) (result i32)
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
    block  ;; label = @1
      block  ;; label = @2
        local.get 3
        i32.load offset=16
        local.get 3
        i32.load offset=24
        i32.load
        i32.eq
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        local.get 3
        i32.load offset=24
        local.get 3
        i32.load offset=20
        call 19
        i32.store offset=28
        br 1 (;@1;)
      end
      block  ;; label = @2
        block  ;; label = @3
          block  ;; label = @4
            local.get 3
            i32.load offset=24
            i32.load
            br_if 0 (;@4;)
            local.get 3
            i32.load offset=16
            br_if 1 (;@3;)
          end
          local.get 3
          i32.load offset=16
          local.get 3
          i32.load offset=24
          i32.load
          i32.const 1
          i32.sub
          i32.gt_u
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
      block  ;; label = @2
        local.get 3
        i32.load offset=24
        i32.load
        local.get 3
        i32.load offset=24
        i32.load offset=4
        i32.ge_u
        i32.const 1
        i32.and
        i32.eqz
        br_if 0 (;@2;)
        local.get 3
        local.get 3
        i32.load offset=24
        call 20
        i32.store offset=12
        block  ;; label = @3
          local.get 3
          i32.load offset=12
          i32.eqz
          br_if 0 (;@3;)
          local.get 3
          local.get 3
          i32.load offset=12
          i32.store offset=28
          br 2 (;@1;)
        end
      end
      local.get 3
      local.get 3
      i32.load offset=24
      i32.load
      local.get 3
      i32.load offset=16
      i32.sub
      i32.const 2
      i32.shl
      i32.store offset=8
      local.get 3
      i32.load offset=24
      i32.load offset=12
      local.get 3
      i32.load offset=16
      i32.const 1
      i32.add
      i32.const 2
      i32.shl
      i32.add
      local.get 3
      i32.load offset=24
      i32.load offset=12
      local.get 3
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      local.get 3
      i32.load offset=8
      call 14
      drop
      local.get 3
      i32.load offset=24
      i32.load offset=12
      local.get 3
      i32.load offset=16
      i32.const 2
      i32.shl
      i32.add
      local.get 3
      i32.load offset=20
      i32.store
      local.get 3
      i32.load offset=24
      local.tee 2
      local.get 2
      i32.load
      i32.const 1
      i32.add
      i32.store
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
  (func (;22;) (type 5) (param i32 i32 i32) (result i32)
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
  (func (;23;) (type 0) (param i32 i32) (result i32)
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
  (func (;24;) (type 1) (param i32) (result i32)
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
  (func (;25;) (type 6) (param i32 i32 i32)
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
  (func (;26;) (type 5) (param i32 i32 i32) (result i32)
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
  (func (;27;) (type 5) (param i32 i32 i32) (result i32)
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
    i32.load offset=24
    local.tee 2
    local.get 2
    i32.load offset=8
    local.tee 2
    i32.const 1
    i32.add
    i32.store offset=8
    local.get 3
    local.get 2
    i32.store offset=12
    local.get 3
    local.get 3
    i32.load offset=24
    i32.load
    i32.store offset=8
    local.get 3
    local.get 3
    i32.load offset=24
    i32.load offset=4
    i32.store offset=4
    block  ;; label = @1
      block  ;; label = @2
        block  ;; label = @3
          block  ;; label = @4
            local.get 3
            i32.load offset=8
            i32.load
            local.get 3
            i32.load offset=8
            i32.load offset=4
            i32.eq
            i32.const 1
            i32.and
            i32.eqz
            br_if 0 (;@4;)
            local.get 3
            i32.load offset=8
            call 20
            br_if 1 (;@3;)
          end
          local.get 3
          i32.load offset=4
          i32.load
          local.get 3
          i32.load offset=4
          i32.load offset=4
          i32.eq
          i32.const 1
          i32.and
          i32.eqz
          br_if 1 (;@2;)
          local.get 3
          i32.load offset=4
          call 20
          i32.eqz
          br_if 1 (;@2;)
        end
        local.get 3
        i32.const 1
        i32.store offset=28
        br 1 (;@1;)
      end
      local.get 3
      i32.load offset=8
      local.get 3
      i32.load offset=20
      local.get 3
      i32.load offset=12
      call 21
      drop
      local.get 3
      i32.load offset=4
      local.get 3
      i32.load offset=16
      local.get 3
      i32.load offset=12
      call 21
      drop
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
  (func (;28;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;29;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;30;) (type 0) (param i32 i32) (result i32)
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
  (func (;31;) (type 2) (param i32)
    global.get 0
    i32.const 16
    i32.sub
    local.get 0
    i32.store offset=12)
  (func (;32;) (type 1) (param i32) (result i32)
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
  (export "__original_main" (func 7))
  (elem (;0;) (i32.const 1) func 10 11 12)
  (data (;0;) (i32.const 1024) "a\00b\00c\00d\00e\00f\00g\00h\00i\00")
  (data (;1;) (i32.const 1044) " \04\01\00"))
