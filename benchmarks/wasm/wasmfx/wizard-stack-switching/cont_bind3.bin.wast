(module definition binary
  "\00\61\73\6d\01\00\00\00\01\91\80\80\80\00\05\60"
  "\00\00\5d\00\60\01\7f\00\5d\02\60\01\7f\01\7f\03"
  "\83\80\80\80\00\02\02\04\06\86\80\80\80\00\01\7f"
  "\01\41\0b\0b\07\88\80\80\80\00\01\04\6d\61\69\6e"
  "\00\01\09\85\80\80\80\00\01\03\00\01\00\0a\a1\80"
  "\80\80\00\02\86\80\80\80\00\00\20\00\24\00\0b\90"
  "\80\80\80\00\00\20\00\d2\00\e0\03\e1\03\01\e3\01"
  "\00\23\00\0b"
)
(module instance)
(assert_return (invoke "main" (i32.const 0x16)) (i32.const 0x16))
(assert_return (invoke "main" (i32.const 0xffff_fe44)) (i32.const 0xffff_fe44))