(module definition binary
  "\00\61\73\6d\01\00\00\00\01\86\80\80\80\00\02\60"
  "\00\00\5d\00\03\82\80\80\80\00\01\00\07\88\80\80"
  "\80\00\01\04\6d\61\69\6e\00\00\0a\91\80\80\80\00"
  "\01\8b\80\80\80\00\01\01\63\01\20\00\e1\01\01\1a"
  "\0b"
)
(module instance)
(assert_trap (invoke "main") "null continuation")
