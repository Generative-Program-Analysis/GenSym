(module definition $f binary
  "\00\61\73\6d\01\00\00\00\01\84\80\80\80\00\01\60"
  "\00\00\03\82\80\80\80\00\01\00\07\88\80\80\80\00"
  "\01\04\6d\61\69\6e\00\00\0a\88\80\80\80\00\01\82"
  "\80\80\80\00\00\0b"
)
(module instance $f $f)
(assert_return (invoke "main"))
