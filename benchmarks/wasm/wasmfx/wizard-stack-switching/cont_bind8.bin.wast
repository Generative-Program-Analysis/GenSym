(module definition binary
  "\00\61\73\6d\01\00\00\00\01\96\80\80\80\00\06\60"
  "\00\01\7f\5d\00\60\01\7f\01\7f\5d\02\60\02\7e\7f"
  "\01\7f\5d\04\03\83\80\80\80\00\02\04\04\07\88\80"
  "\80\80\00\01\04\6d\61\69\6e\00\01\09\85\80\80\80"
  "\00\01\03\00\01\00\0a\a6\80\80\80\00\02\88\80\80"
  "\80\00\00\20\00\a7\20\01\6b\0b\93\80\80\80\00\00"
  "\20\01\20\00\d2\00\e0\05\e1\05\03\e1\03\01\e3\01"
  "\00\0b"
)
(module instance)
(assert_return
  (invoke "main" (i64.const 0x16) (i32.const 0x2c))
  (i32.const 0xffff_ffea)
)
(assert_return
  (invoke "main" (i64.const 0xffff_ffff_ffff_fe44) (i32.const 0x6f))
  (i32.const 0xffff_fdd5)
)
