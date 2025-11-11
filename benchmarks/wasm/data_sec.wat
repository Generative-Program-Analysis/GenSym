(module
  ;; define one page of memory (64 KiB)
  (memory 1)

  ;; data bytes at address 0: 01 02 03 04
  (data (i32.const 0) "\01\02\03\04")

  ;; load all 4 bytes as one i32 (little-endian → 0x04030201 = 67305985)
  (func (result i32)
    i32.const 0
    i32.load
  )
  (start 0)
)