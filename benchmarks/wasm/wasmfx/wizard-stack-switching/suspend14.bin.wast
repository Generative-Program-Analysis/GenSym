(module definition binary
  "\00\61\73\6d\01\00\00\00\01\9b\80\80\80\00\07\60"
  "\00\00\5d\00\60\01\7f\00\5d\02\60\01\7e\00\60\01"
  "\7f\01\7e\60\00\02\7e\63\01\03\83\80\80\80\00\02"
  "\02\05\0d\85\80\80\80\00\02\00\04\00\04\07\88\80"
  "\80\80\00\01\04\6d\61\69\6e\00\01\09\85\80\80\80"
  "\00\01\03\00\01\00\0a\c7\80\80\80\00\02\95\80\80"
  "\80\00\00\20\00\41\00\48\04\40\20\00\ac\e2\01\05"
  "\20\00\ac\e2\00\0b\0b\a7\80\80\80\00\00\02\06\02"
  "\06\20\00\d2\00\e0\03\e3\03\02\00\00\00\00\01\01"
  "\42\0b\d0\01\0b\1a\42\98\78\7d\0f\0b\1a\42\b0\70"
  "\7d\0f\0b"
)
(module instance)
(assert_return (invoke "main" (i32.const 0xffff_ffe9)) (i64.const 0x7b9))
(assert_return (invoke "main" (i32.const 0xffff_ffd5)) (i64.const 0x7a5))
(assert_return (invoke "main" (i32.const 0x17)) (i64.const 0x3ff))
(assert_return (invoke "main" (i32.const 0x2b)) (i64.const 0x413))
