(module definition binary
  "\00\61\73\6d\01\00\00\00\01\90\80\80\80\00\04\60"
  "\00\00\5d\00\60\02\7e\7c\00\60\00\02\7e\7c\03\83"
  "\80\80\80\00\02\00\03\0d\83\80\80\80\00\01\00\02"
  "\07\88\80\80\80\00\01\04\6d\61\69\6e\00\01\09\85"
  "\80\80\80\00\01\03\00\01\00\0a\b6\80\80\80\00\02"
  "\82\80\80\80\00\00\0b\a9\80\80\80\00\00\1f\40\01"
  "\00\00\00\42\51\44\9a\99\99\99\99\99\01\c0\d2\00"
  "\e0\01\e4\01\00\00\00\0b\42\89\06\44\00\00\00\00"
  "\00\d0\74\40\0b"
)
(module instance)
(assert_return
  (invoke "main")
  (i64.const 0xffff_ffff_ffff_ffd1)
  (f64.const -0x1.1999_9999_9999_ap+1)
)
