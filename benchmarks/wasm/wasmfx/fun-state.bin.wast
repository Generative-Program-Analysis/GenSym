(module definition $state binary
  "\00\61\73\6d\01\00\00\00\01\a6\80\80\80\00\08\60"
  "\01\7f\01\7f\60\00\01\7f\5d\00\5d\01\60\01\7f\00"
  "\60\02\64\02\7f\01\7f\60\00\02\7f\64\03\60\02\7f"
  "\64\03\01\7f\03\85\80\80\80\00\04\05\07\01\01\0d"
  "\85\80\80\80\00\02\00\01\00\04\07\87\80\80\80\00"
  "\01\03\72\75\6e\00\03\09\85\80\80\80\00\01\03\00"
  "\01\02\0a\ec\80\80\80\00\04\9d\80\80\80\00\00\02"
  "\64\02\02\06\20\01\20\00\e3\02\02\00\00\01\00\01"
  "\00\0f\0b\12\01\0b\20\01\12\00\0b\9b\80\80\80\00"
  "\00\02\64\02\02\06\20\01\e3\03\02\00\00\01\00\01"
  "\00\0f\0b\12\01\0b\20\00\12\00\0b\95\80\80\80\00"
  "\00\41\07\e2\01\e2\00\41\02\41\03\e2\01\41\03\e2"
  "\00\6a\6c\6a\0b\8a\80\80\80\00\00\41\00\d2\02\e0"
  "\03\10\01\0b"
)
(module instance $state $state)
(assert_return (invoke "run") (i32.const 0x13))