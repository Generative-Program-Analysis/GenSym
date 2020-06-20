; ModuleID = 'branch.c'
source_filename = "branch.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @f(i32 %x) #0 {
entry:
  %x.addr = alloca i32, align 4
  %y = alloca i32, align 4
  store i32 %x, i32* %x.addr, align 4
  store i32 0, i32* %y, align 4
  %0 = load i32, i32* %x.addr, align 4
  %cmp = icmp eq i32 %0, 0
  br i1 %cmp, label %if.then, label %if.else

if.then:                                          ; preds = %entry
  %1 = load i32, i32* %x.addr, align 4
  %add = add nsw i32 %1, 1
  store i32 %add, i32* %y, align 4
  %2 = load i32, i32* %y, align 4
  %3 = load i32, i32* %y, align 4
  %add1 = add nsw i32 %2, %3
  store i32 %add1, i32* %y, align 4
  br label %if.end

if.else:                                          ; preds = %entry
  %4 = load i32, i32* %x.addr, align 4
  %add2 = add nsw i32 %4, 2
  store i32 %add2, i32* %y, align 4
  %5 = load i32, i32* %y, align 4
  %6 = load i32, i32* %y, align 4
  %sub = sub nsw i32 %5, %6
  store i32 %sub, i32* %y, align 4
  br label %if.end

if.end:                                           ; preds = %if.else, %if.then
  %7 = load i32, i32* %y, align 4
  ret i32 %7
}

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0-2~ubuntu18.04.2 (tags/RELEASE_900/final)"}
