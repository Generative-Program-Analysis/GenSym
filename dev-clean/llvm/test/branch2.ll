; ModuleID = 'branch2.c'
source_filename = "branch2.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [2 x i8] c"a\00", align 1
@.str.1 = private unnamed_addr constant [2 x i8] c"b\00", align 1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @f(i32 %x, i32 %z) #0 {
entry:
  %x.addr = alloca i32, align 4
  %z.addr = alloca i32, align 4
  %y = alloca i32, align 4
  store i32 %x, i32* %x.addr, align 4
  store i32 %z, i32* %z.addr, align 4
  store i32 0, i32* %y, align 4
  %0 = load i32, i32* %x.addr, align 4
  %cmp = icmp sgt i32 %0, 0
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
  %sub3 = sub nsw i32 %7, 1
  store i32 %sub3, i32* %y, align 4
  %8 = load i32, i32* %z.addr, align 4
  %cmp4 = icmp eq i32 %8, 12
  br i1 %cmp4, label %if.then5, label %if.else7

if.then5:                                         ; preds = %if.end
  %9 = load i32, i32* %y, align 4
  %add6 = add nsw i32 %9, 1
  store i32 %add6, i32* %y, align 4
  br label %if.end9

if.else7:                                         ; preds = %if.end
  %10 = load i32, i32* %y, align 4
  %sub8 = sub nsw i32 %10, 1
  store i32 %sub8, i32* %y, align 4
  br label %if.end9

if.end9:                                          ; preds = %if.else7, %if.then5
  %11 = load i32, i32* %y, align 4
  ret i32 %11
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  %a = alloca i32, align 4
  %b = alloca i32, align 4
  store i32 0, i32* %retval, align 4
  %0 = bitcast i32* %a to i8*
  call void @klee_make_symbolic(i8* %0, i64 4, i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str, i64 0, i64 0))
  %1 = bitcast i32* %b to i8*
  call void @klee_make_symbolic(i8* %1, i64 4, i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.1, i64 0, i64 0))
  %2 = load i32, i32* %a, align 4
  %3 = load i32, i32* %b, align 4
  %call = call i32 @f(i32 %2, i32 %3)
  ret i32 %call
}

declare dso_local void @klee_make_symbolic(i8*, i64, i8*) #1

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0-2~ubuntu18.04.2 (tags/RELEASE_900/final)"}
