(module binary
  "\00\61\73\6d\01\00\00\00\01\87\80\80\80\00\01\60"
  "\02\7d\7d\01\7d\03\82\80\80\80\00\01\00\07\87\80"
  "\80\80\00\01\03\61\64\64\00\00\0a\8d\80\80\80\00"
  "\01\87\80\80\80\00\00\20\00\20\01\92\0b"
)
(assert_return
  (invoke "add" (f32.const -0x0p+0) (f32.const -0x0p+0))
  (f32.const -0x0p+0)
)
(assert_return
  (invoke "add" (f32.const -0x0p+0) (f32.const 0x0p+0))
  (f32.const 0x0p+0)
)
(assert_return
  (invoke "add" (f32.const 0x0p+0) (f32.const -0x0p+0))
  (f32.const 0x0p+0)
)
(assert_return
  (invoke "add" (f32.const 0x0p+0) (f32.const 0x0p+0))
  (f32.const 0x0p+0)
)
(assert_return
  (invoke "add" (f32.const -0x0p+0) (f32.const -0x1p-149))
  (f32.const -0x1p-149)
)
(assert_return
  (invoke "add" (f32.const -0x0p+0) (f32.const 0x1p-149))
  (f32.const 0x1p-149)
)
(assert_return
  (invoke "add" (f32.const 0x0p+0) (f32.const -0x1p-149))
  (f32.const -0x1p-149)
)
(assert_return
  (invoke "add" (f32.const 0x0p+0) (f32.const 0x1p-149))
  (f32.const 0x1p-149)
)
(assert_return
  (invoke "add" (f32.const -0x0p+0) (f32.const -0x1p-126))
  (f32.const -0x1p-126)
)
