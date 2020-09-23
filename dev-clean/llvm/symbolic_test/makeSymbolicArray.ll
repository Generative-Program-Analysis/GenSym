; ModuleID = 'makeSymbolicArray.c'
source_filename = "makeSymbolicArray.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  %a = alloca [2 x i32], align 4
  store i32 0, i32* %retval, align 4
  %arraydecay = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 0
  %call = call i32 (i32*, i32, ...) bitcast (i32 (...)* @make_symbolic to i32 (i32*, i32, ...)*)(i32* %arraydecay, i32 2)
  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 0
  %0 = load i32, i32* %arrayidx, align 4
  %cmp = icmp sgt i32 %0, 2
  br i1 %cmp, label %if.then, label %if.else

if.then:                                          ; preds = %entry
  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 1
  %1 = load i32, i32* %arrayidx1, align 4
  %add = add nsw i32 %1, 3
  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 1
  store i32 %add, i32* %arrayidx2, align 4
  br label %if.end

if.else:                                          ; preds = %entry
  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 1
  %2 = load i32, i32* %arrayidx3, align 4
  %add4 = add nsw i32 %2, 3
  %arrayidx5 = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 1
  store i32 %add4, i32* %arrayidx5, align 4
  br label %if.end

if.end:                                           ; preds = %if.else, %if.then
  %arrayidx6 = getelementptr inbounds [2 x i32], [2 x i32]* %a, i64 0, i64 1
  %3 = load i32, i32* %arrayidx6, align 4
  %cmp7 = icmp slt i32 %3, 2
  br i1 %cmp7, label %if.then8, label %if.else10

if.then8:                                         ; preds = %if.end
  %call9 = call i32 (...) @assert()
  br label %if.end12

if.else10:                                        ; preds = %if.end
  %call11 = call i32 (...) @assert()
  br label %if.end12

if.end12:                                         ; preds = %if.else10, %if.then8
  %4 = load i32, i32* %retval, align 4
  ret i32 %4
}

declare dso_local i32 @make_symbolic(...) #1

declare dso_local i32 @assert(...) #1

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0 (tags/RELEASE_900/final)"}
