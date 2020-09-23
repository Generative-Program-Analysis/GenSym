; ModuleID = 'makeSymbolicDouble.c'
source_filename = "makeSymbolicDouble.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  %a = alloca i32, align 4
  %b = alloca i32, align 4
  store i32 0, i32* %retval, align 4
  %call = call i32 (i32*, i32, ...) bitcast (i32 (...)* @make_symbolic to i32 (i32*, i32, ...)*)(i32* %a, i32 1)
  %call1 = call i32 (i32*, i32, ...) bitcast (i32 (...)* @make_symbolic to i32 (i32*, i32, ...)*)(i32* %b, i32 1)
  %0 = load i32, i32* %a, align 4
  %cmp = icmp sgt i32 %0, 1
  br i1 %cmp, label %if.then, label %if.else

if.then:                                          ; preds = %entry
  %1 = load i32, i32* %b, align 4
  %add = add nsw i32 %1, 1
  store i32 %add, i32* %b, align 4
  br label %if.end

if.else:                                          ; preds = %entry
  %2 = load i32, i32* %b, align 4
  %sub = sub nsw i32 %2, 1
  store i32 %sub, i32* %b, align 4
  br label %if.end

if.end:                                           ; preds = %if.else, %if.then
  %3 = load i32, i32* %b, align 4
  %cmp2 = icmp slt i32 %3, 2
  br i1 %cmp2, label %if.then3, label %if.else5

if.then3:                                         ; preds = %if.end
  %call4 = call i32 (...) @assert()
  br label %if.end7

if.else5:                                         ; preds = %if.end
  %call6 = call i32 (...) @assert()
  br label %if.end7

if.end7:                                          ; preds = %if.else5, %if.then3
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
