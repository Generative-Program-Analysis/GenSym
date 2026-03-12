(module
  (type (;0;) (func (result i32)))
  (type (;1;) (func))
  (type (;2;) (func (param i32) (result i32)))
  (type (;3;) (func (param i32)))
  (import "console" "assert" (func (;0;) (type 3)))

  ;; Concrete memory operations.
  ;; Address layout:
  ;;   0..39   : i32 tests
  ;;   40..95  : i64 tests
  ;;   96..111 : floating-point tests
  (func (;1;) (type 1)
    ;; Full-width i32 roundtrip.
    i32.const 0
    i32.const 0x11223344 ;; bytes in memory: 44 33 22 11
    i32.store            ;; write 4 bytes at address 0
    i32.const 0
    i32.load             ;; read the same 4 bytes back
    i32.const 0x11223344
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i32.store8 / i32.load8_u.
    ;; Only the low byte 0xcd is written, at address 17.
    i32.const 16
    i32.const 0
    i32.store
    i32.const 17
    i32.const 0xabcd
    i32.store8           ;; keeps only byte 0xcd
    i32.const 16
    i32.load             ;; expect byte 1 of the word to become 0xcd
    i32.const 0x0000cd00
    i32.ne
    if
      i32.const 0
      call 0
    end
    i32.const 17
    i32.load8_u          ;; zero-extend one byte to i32
    i32.const 0xcd
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i32.load8_s should sign-extend 0xef to -17.
    i32.const 20
    i32.const 0xef       ;; signed 8-bit value -17
    i32.store8
    i32.const 20
    i32.load8_s          ;; sign-extend one byte to i32
    i32.const -17
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i32.store16 / i32.load16_u.
    ;; 0xabcd is written as bytes cd ab in little-endian order.
    i32.const 24
    i32.const 0
    i32.store
    i32.const 25
    i32.const 0xabcd
    i32.store16          ;; write bytes at addresses 25 and 26
    i32.const 24
    i32.load             ;; expect bytes 1..2 of the word to be cd ab
    i32.const 0x00abcd00
    i32.ne
    if
      i32.const 0
      call 0
    end
    i32.const 25
    i32.load16_u         ;; zero-extend 16 bits to i32
    i32.const 0xabcd
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i32.load16_s should sign-extend 0xcdef.
    i32.const 32
    i32.const 0xcdef     ;; signed 16-bit value -12817
    i32.store16
    i32.const 32
    i32.load16_s         ;; sign-extend 16 bits to i32
    i32.const -12817
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; Full-width i64 roundtrip.
    i32.const 40
    i64.const 0x0102030405060708
    i64.store            ;; write 8 bytes at address 40
    i32.const 40
    i64.load             ;; read the same 8 bytes back
    i64.const 0x0102030405060708
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i64.store8 / i64.load8_u.
    ;; Even for i64, store8 writes just one byte.
    i32.const 48
    i64.const 0
    i64.store
    i32.const 49
    i64.const 0xabcdef
    i64.store8           ;; keeps only byte 0xef
    i32.const 49
    i64.load8_u          ;; zero-extend one byte to i64
    i64.const 0xef
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i64.load8_s should sign-extend 0xef to -17.
    i32.const 50
    i64.const 0xef
    i64.store8
    i32.const 50
    i64.load8_s          ;; sign-extend one byte to i64
    i64.const -17
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i64.store16 / i64.load16_u.
    i32.const 56
    i64.const 0
    i64.store
    i32.const 57
    i64.const 0xabcd
    i64.store16          ;; write bytes cd ab
    i32.const 57
    i64.load16_u         ;; zero-extend 16 bits to i64
    i64.const 0xabcd
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; Concrete i64.load16_s should sign-extend 0xcdef.
    i32.const 60
    i64.const 0xcdef
    i64.store16
    i32.const 60
    i64.load16_s         ;; sign-extend 16 bits to i64
    i64.const -12817
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; i64.store32 should overwrite only the upper 4 bytes of the 64-bit word at 64.
    ;; Start with: 08 07 06 05 04 03 02 01
    ;; After store32 at address 68: 08 07 06 05 dd cc bb aa
    i32.const 64
    i64.const 0x0102030405060708
    i64.store
    i32.const 68
    i64.const 0xaabbccdd
    i64.store32
    i32.const 64
    i64.load
    i64.const 0xaabbccdd05060708
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; i64.load32_u should zero-extend the stored low 32 bits to i64.
    i32.const 80
    i64.const 0
    i64.store
    i32.const 80
    i64.const 0x89abcdef
    i64.store32          ;; low 32 bits in memory: ef cd ab 89
    i32.const 80
    i64.load32_u         ;; expect 0x0000000089abcdef
    i64.const 0x89abcdef
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; i64.load32_s should sign-extend the stored low 32 bits to i64.
    i32.const 88
    i64.const 0
    i64.store
    i32.const 88
    i64.const 0x89abcdef
    i64.store32
    i32.const 88
    i64.load32_s         ;; expect 0xffffffff89abcdef
    i64.const -1985229329
    i64.ne
    if
      i32.const 0
      call 0
    end

    ;; Full-width f32 roundtrip.
    i32.const 96
    f32.const 1.5
    f32.store
    i32.const 96
    f32.load
    f32.const 1.5
    f32.ne
    if
      i32.const 0
      call 0
    end

    ;; Full-width f64 roundtrip.
    i32.const 104
    f64.const 3.25
    f64.store
    i32.const 104
    f64.load
    f64.const 3.25
    f64.ne
    if
      i32.const 0
      call 0
    end
    )

  ;; Symbolic i32 memory operations only.
  ;; This exercises symbolic full-width i32 stores plus packed i32 store/load behavior.
  (func (;2;) (type 3) (param i32)
    ;; Full-width symbolic i32 store/load interaction.
    i32.const 0
    local.get 0
    i32.store
    i32.const 0
    i32.load
    i32.const 25
    i32.eq
    if
      i32.const 0
      call 0
    end

    i32.const 1
    i32.load
    i32.const 1
    i32.eq
    if
      i32.const 0
      call 0
    end

    ;; store8 should update only one byte of the word at 16.
    ;; Expected word: (x & 0xff) shifted into byte position 1.
    i32.const 16
    i32.const 0
    i32.store
    i32.const 17
    local.get 0
    i32.store8
    i32.const 16
    i32.load
    local.get 0
    i32.const 255
    i32.and
    i32.const 8
    i32.shl
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; store16 should update only two bytes of the word at 20.
    ;; Expected word: (x & 0xffff) shifted into byte positions 1 and 2.
    i32.const 20
    i32.const 0
    i32.store
    i32.const 21
    local.get 0
    i32.store16
    i32.const 20
    i32.load
    local.get 0
    i32.const 65535
    i32.and
    i32.const 8
    i32.shl
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; load8_u should produce the low byte of x, zero-extended to i32.
    i32.const 32
    i32.const 0
    i32.store
    i32.const 32
    local.get 0
    i32.store8
    i32.const 32
    i32.load8_u
    local.get 0
    i32.const 255
    i32.and
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; load8_s should sign-extend the low byte of x.
    ;; `(x << 24) >> 24` is the expected i32 sign-extension formula.
    i32.const 36
    i32.const 0
    i32.store
    i32.const 36
    local.get 0
    i32.store8
    i32.const 36
    i32.load8_s
    local.get 0
    i32.const 24
    i32.shl
    i32.const 24
    i32.shr_s
    i32.ne
    if
      i32.const 0
      call 0
    end

    ;; load16_u should produce the low 16 bits of x, zero-extended to i32.
    i32.const 40
    i32.const 0
    i32.store
    i32.const 40
    local.get 0
    i32.store16
    i32.const 40
    i32.load16_u
    local.get 0
    i32.const 65535
    i32.and
    i32.ne
    if
      i32.const 0
      call 0
    end
)


  ;; Driver: run the concrete test first, then run the symbolic-i32 test.
  (func (;3;) (type 2) (param i32) (result i32)
    call 1
    local.get 0
    call 2
    i32.const 1)

  ;; Entry point: create one symbolic i32 and pass it to the symbolic test.
  (func (;4;) (type 1)
    i32.const 0
    i32.symbolic
    call 3
    drop)

  (memory (;0;) 2)
  (global (;0;) (mut i32) (i32.const 42))
  (export "main" (func 3))
  (start 4))
