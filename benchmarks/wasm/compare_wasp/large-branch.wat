(module
  (type (;0;) (func (param i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32) (result i32)))
  (type (;1;) (func (result i32)))
  (import "spectest" "print_i32" (func (;0;) (param i32)))

  (func (;1;) (type 0) (param i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32) (result i32)
    local.get 0
    i32.const 0
    i32.gt_s
    if  ;; label = @1
      local.get 7
      local.get 13
      i32.add
      i32.const 13
      i32.add
      local.set 0
    end
    local.get 1
    i32.const 1
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 7
      i32.add
      i32.const 35
      i32.add
      local.set 1
    end
    local.get 2
    i32.const 2
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 4
      i32.add
      i32.const 69
      i32.add
      local.set 2
    end
    local.get 3
    i32.const 3
    i32.gt_s
    if  ;; label = @1
      local.get 13
      local.get 14
      i32.add
      i32.const 91
      i32.add
      local.set 3
    end
    local.get 4
    i32.const 4
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 10
      i32.add
      i32.const 47
      i32.add
      local.set 4
    end
    local.get 5
    i32.const 5
    i32.gt_s
    if  ;; label = @1
      local.get 15
      local.get 6
      i32.add
      i32.const 74
      i32.add
      local.set 5
    end
    local.get 6
    i32.const 6
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 8
      i32.add
      i32.const 15
      i32.add
      local.set 6
    end
    local.get 7
    i32.const 7
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 9
      i32.add
      i32.const 34
      i32.add
      local.set 7
    end
    local.get 8
    i32.const 8
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 12
      i32.add
      i32.const 57
      i32.add
      local.set 8
    end
    local.get 9
    i32.const 9
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 11
      i32.add
      i32.const 3
      i32.add
      local.set 9
    end
    local.get 10
    i32.const 10
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 6
      i32.add
      i32.const 21
      i32.add
      local.set 10
    end
    local.get 11
    i32.const 11
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 5
      i32.add
      i32.const 72
      i32.add
      local.set 11
    end
    local.get 12
    i32.const 12
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 6
      i32.add
      i32.const 55
      i32.add
      local.set 12
    end
    local.get 13
    i32.const 13
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 3
      i32.add
      i32.const 12
      i32.add
      local.set 13
    end
    local.get 14
    i32.const 14
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 12
      i32.add
      i32.const 69
      i32.add
      local.set 14
    end
    local.get 15
    i32.const 15
    i32.gt_s
    if  ;; label = @1
      local.get 18
      local.get 5
      i32.add
      i32.const 92
      i32.add
      local.set 15
    end
    local.get 16
    i32.const 16
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 12
      i32.add
      i32.const 39
      i32.add
      local.set 16
    end
    local.get 17
    i32.const 17
    i32.gt_s
    if  ;; label = @1
      local.get 15
      local.get 8
      i32.add
      i32.const 39
      i32.add
      local.set 17
    end
    local.get 18
    i32.const 18
    i32.gt_s
    if  ;; label = @1
      local.get 17
      local.get 3
      i32.add
      i32.const 64
      i32.add
      local.set 18
    end
    local.get 19
    i32.const 19
    i32.gt_s
    if  ;; label = @1
      local.get 11
      local.get 5
      i32.add
      i32.const 78
      i32.add
      local.set 19
    end
    local.get 6
    i32.const 6
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 8
      i32.add
      i32.const 15
      i32.add
      local.set 6
    end
    local.get 7
    i32.const 7
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 9
      i32.add
      i32.const 34
      i32.add
      local.set 7
    end
    local.get 8
    i32.const 8
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 12
      i32.add
      i32.const 57
      i32.add
      local.set 8
    end
    local.get 9
    i32.const 9
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 11
      i32.add
      i32.const 3
      i32.add
      local.set 9
    end
    local.get 10
    i32.const 10
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 6
      i32.add
      i32.const 21
      i32.add
      local.set 10
    end
    local.get 11
    i32.const 11
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 5
      i32.add
      i32.const 72
      i32.add
      local.set 11
    end
    local.get 12
    i32.const 12
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 6
      i32.add
      i32.const 55
      i32.add
      local.set 12
    end
    local.get 13
    i32.const 13
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 3
      i32.add
      i32.const 12
      i32.add
      local.set 13
    end
    local.get 14
    i32.const 14
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 12
      i32.add
      i32.const 69
      i32.add
      local.set 14
    end
    local.get 15
    i32.const 15
    i32.gt_s
    if  ;; label = @1
      local.get 18
      local.get 5
      i32.add
      i32.const 92
      i32.add
      local.set 15
    end
    local.get 16
    i32.const 16
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 12
      i32.add
      i32.const 39
      i32.add
      local.set 16
    end
    local.get 1
    local.get 2
    i32.add
    local.get 3
    i32.add
    local.get 4
    i32.add
    local.get 5
    i32.add
    local.get 6
    i32.add
    local.get 7
    i32.add
    local.get 8
    i32.add
    local.get 9
    i32.add
    local.get 10
    i32.add
    local.get 11
    i32.add
    local.get 12
    i32.add
    local.get 13
    i32.add
    local.get 14
    i32.add
    local.get 15
    i32.add
    local.get 16
    i32.add
    local.get 17
    i32.add
    local.get 18
    i32.add
    local.get 19
    i32.add)
  (func (;2;) (type 1) (result i32)
    i32.const 0
    i32.symbolic
    i32.const 1
    i32.symbolic
    i32.const 2
    i32.symbolic
    i32.const 3
    i32.symbolic
    i32.const 4
    i32.symbolic
    i32.const 5
    i32.symbolic
    i32.const 6
    i32.symbolic
    i32.const 7
    i32.symbolic
    i32.const 8
    i32.symbolic
    i32.const 9
    i32.symbolic
    i32.const 10
    i32.const 11
    i32.const 12
    i32.const 13
    i32.const 14
    i32.const 15
    i32.const 16
    i32.const 17
    i32.const 18
    i32.const 19
    call 3)
  (func (;3;) (type 0) (param i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32) (result i32)
    local.get 0
    i32.const 0
    i32.gt_s
    if  ;; label = @1
    local.get 7
    local.get 13
    i32.add
    i32.const 13
    i32.add
    local.set 0
    end
    local.get 1
    i32.const 1
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 7
      i32.add
      i32.const 35
      i32.add
      local.set 1
    end
    local.get 2
    i32.const 2
    i32.gt_s
    if  ;; label = @1
      local.get 0
      local.get 4
      i32.add
      i32.const 69
      i32.add
      local.set 2
    end
    local.get 3
    i32.const 3
    i32.gt_s
    if  ;; label = @1
      local.get 13
      local.get 14
      i32.add
      i32.const 91
      i32.add
      local.set 3
    end
    local.get 4
    i32.const 4
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 10
      i32.add
      i32.const 47
      i32.add
      local.set 4
    end
    local.get 5
    i32.const 5
    i32.gt_s
    if  ;; label = @1
      local.get 15
      local.get 6
      i32.add
      i32.const 74
      i32.add
      local.set 5
    end
    local.get 6
    i32.const 6
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 8
      i32.add
      i32.const 15
      i32.add
      local.set 6
    end
    local.get 6
    i32.const 6
    i32.gt_s
    if  ;; label = @1
      local.get 19
      local.get 8
      i32.add
      i32.const 15
      i32.add
      local.set 6
    end
    local.get 7
    i32.const 7
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 9
      i32.add
      i32.const 34
      i32.add
      local.set 7
    end
    local.get 8
    i32.const 8
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 12
      i32.add
      i32.const 57
      i32.add
      local.set 8
    end
    local.get 9
    i32.const 9
    i32.gt_s
    if  ;; label = @1
      local.get 10
      local.get 11
      i32.add
      i32.const 3
      i32.add
      local.set 9
    end
    local.get 10
    i32.const 10
    i32.gt_s
    if  ;; label = @1
      local.get 6
      local.get 6
      i32.add
      i32.const 21
      i32.add
      local.set 10
    end
    local.get 1
    local.get 2
    i32.add
    local.get 3
    i32.add
    local.get 4
    i32.add
    local.get 5
    i32.add
    local.get 6
    i32.add
    local.get 7
    i32.add
    local.get 8
    i32.add
    local.get 9
    i32.add
    local.get 10
    i32.add
    local.get 11
    i32.add
    local.get 12
    i32.add
    local.get 13
    i32.add
    local.get 14
    i32.add
    local.get 15
    i32.add
    local.get 16
    i32.add
    local.get 17
    i32.add
    local.get 18
    i32.add
    local.get 19
    i32.add
  )
  (export "f" (func 1))
  (export "main" (func 2))
  (start 2))

