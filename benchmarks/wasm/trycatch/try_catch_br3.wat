;; ignored example
(module
  ;; output: 1, 2, 3, 4, 5
  ;;         4 is printed, because the delimited continuation is kept when breaking out of the block,
  ;;         it's inside the trail1
  (type (;0;) (func (param i32)))
  (type (;1;) (func))
  (import "console" "log" (func (;0;) (type 0)))
  (func (;1;) (type 1)
    (local i32)
    try
      i32.const 1
      call 0
      block
        block
          i32.const 42
          ;; [42]
          throw
          br 0
        end
      end
      i32.const 3
      call 0
    catch
      ;; [42, resume]
      i32.const 2
      call 0
      drop
      resume0
      i32.const 4
      call 0
    end
    i32.const 5
    call 0
    )
  (start 1))
