; ModuleID = 'multipath_65536.c'
source_filename = "multipath_65536.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @f(i32 %a, i32 %b, i32 %c) #0 {
entry:
  %a.addr = alloca i32, align 4
  %b.addr = alloca i32, align 4
  %c.addr = alloca i32, align 4
  %x = alloca i32, align 4
  %y = alloca i32, align 4
  %z = alloca i32, align 4
  store i32 %a, i32* %a.addr, align 4
  store i32 %b, i32* %b.addr, align 4
  store i32 %c, i32* %c.addr, align 4
  %0 = load i32, i32* %a.addr, align 4
  store i32 %0, i32* %x, align 4
  %1 = load i32, i32* %b.addr, align 4
  store i32 %1, i32* %y, align 4
  %2 = load i32, i32* %c.addr, align 4
  store i32 %2, i32* %z, align 4
  %3 = load i32, i32* %x, align 4
  %cmp = icmp sgt i32 %3, 1
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %4 = load i32, i32* %x, align 4
  %5 = load i32, i32* %z, align 4
  %add = add nsw i32 %4, %5
  %add1 = add nsw i32 %add, 24
  store i32 %add1, i32* %y, align 4
  %6 = load i32, i32* %x, align 4
  %7 = load i32, i32* %y, align 4
  %add2 = add nsw i32 %6, %7
  %add3 = add nsw i32 %add2, 2
  store i32 %add3, i32* %z, align 4
  br label %if.end

if.end:                                           ; preds = %if.then, %entry
  %8 = load i32, i32* %x, align 4
  %cmp4 = icmp sgt i32 %8, 2
  br i1 %cmp4, label %if.then5, label %if.end10

if.then5:                                         ; preds = %if.end
  %9 = load i32, i32* %x, align 4
  %10 = load i32, i32* %z, align 4
  %add6 = add nsw i32 %9, %10
  %add7 = add nsw i32 %add6, 48
  store i32 %add7, i32* %y, align 4
  %11 = load i32, i32* %x, align 4
  %12 = load i32, i32* %y, align 4
  %add8 = add nsw i32 %11, %12
  %add9 = add nsw i32 %add8, 12
  store i32 %add9, i32* %z, align 4
  br label %if.end10

if.end10:                                         ; preds = %if.then5, %if.end
  %13 = load i32, i32* %x, align 4
  %cmp11 = icmp sgt i32 %13, 3
  br i1 %cmp11, label %if.then12, label %if.end17

if.then12:                                        ; preds = %if.end10
  %14 = load i32, i32* %x, align 4
  %15 = load i32, i32* %z, align 4
  %add13 = add nsw i32 %14, %15
  %add14 = add nsw i32 %add13, 47
  store i32 %add14, i32* %y, align 4
  %16 = load i32, i32* %x, align 4
  %17 = load i32, i32* %y, align 4
  %add15 = add nsw i32 %16, %17
  %add16 = add nsw i32 %add15, 16
  store i32 %add16, i32* %z, align 4
  br label %if.end17

if.end17:                                         ; preds = %if.then12, %if.end10
  %18 = load i32, i32* %x, align 4
  %cmp18 = icmp sgt i32 %18, 4
  br i1 %cmp18, label %if.then19, label %if.end24

if.then19:                                        ; preds = %if.end17
  %19 = load i32, i32* %x, align 4
  %20 = load i32, i32* %z, align 4
  %add20 = add nsw i32 %19, %20
  %add21 = add nsw i32 %add20, 95
  store i32 %add21, i32* %y, align 4
  %21 = load i32, i32* %x, align 4
  %22 = load i32, i32* %y, align 4
  %add22 = add nsw i32 %21, %22
  %add23 = add nsw i32 %add22, 51
  store i32 %add23, i32* %z, align 4
  br label %if.end24

if.end24:                                         ; preds = %if.then19, %if.end17
  %23 = load i32, i32* %x, align 4
  %cmp25 = icmp sgt i32 %23, 5
  br i1 %cmp25, label %if.then26, label %if.end31

if.then26:                                        ; preds = %if.end24
  %24 = load i32, i32* %x, align 4
  %25 = load i32, i32* %z, align 4
  %add27 = add nsw i32 %24, %25
  %add28 = add nsw i32 %add27, 43
  store i32 %add28, i32* %y, align 4
  %26 = load i32, i32* %x, align 4
  %27 = load i32, i32* %y, align 4
  %add29 = add nsw i32 %26, %27
  %add30 = add nsw i32 %add29, 30
  store i32 %add30, i32* %z, align 4
  br label %if.end31

if.end31:                                         ; preds = %if.then26, %if.end24
  %28 = load i32, i32* %x, align 4
  %cmp32 = icmp sgt i32 %28, 6
  br i1 %cmp32, label %if.then33, label %if.end38

if.then33:                                        ; preds = %if.end31
  %29 = load i32, i32* %x, align 4
  %30 = load i32, i32* %z, align 4
  %add34 = add nsw i32 %29, %30
  %add35 = add nsw i32 %add34, 69
  store i32 %add35, i32* %y, align 4
  %31 = load i32, i32* %x, align 4
  %32 = load i32, i32* %y, align 4
  %add36 = add nsw i32 %31, %32
  %add37 = add nsw i32 %add36, 86
  store i32 %add37, i32* %z, align 4
  br label %if.end38

if.end38:                                         ; preds = %if.then33, %if.end31
  %33 = load i32, i32* %x, align 4
  %cmp39 = icmp sgt i32 %33, 7
  br i1 %cmp39, label %if.then40, label %if.end45

if.then40:                                        ; preds = %if.end38
  %34 = load i32, i32* %x, align 4
  %35 = load i32, i32* %z, align 4
  %add41 = add nsw i32 %34, %35
  %add42 = add nsw i32 %add41, 66
  store i32 %add42, i32* %y, align 4
  %36 = load i32, i32* %x, align 4
  %37 = load i32, i32* %y, align 4
  %add43 = add nsw i32 %36, %37
  %add44 = add nsw i32 %add43, 72
  store i32 %add44, i32* %z, align 4
  br label %if.end45

if.end45:                                         ; preds = %if.then40, %if.end38
  %38 = load i32, i32* %x, align 4
  %cmp46 = icmp sgt i32 %38, 8
  br i1 %cmp46, label %if.then47, label %if.end52

if.then47:                                        ; preds = %if.end45
  %39 = load i32, i32* %x, align 4
  %40 = load i32, i32* %z, align 4
  %add48 = add nsw i32 %39, %40
  %add49 = add nsw i32 %add48, 14
  store i32 %add49, i32* %y, align 4
  %41 = load i32, i32* %x, align 4
  %42 = load i32, i32* %y, align 4
  %add50 = add nsw i32 %41, %42
  %add51 = add nsw i32 %add50, 63
  store i32 %add51, i32* %z, align 4
  br label %if.end52

if.end52:                                         ; preds = %if.then47, %if.end45
  %43 = load i32, i32* %x, align 4
  %cmp53 = icmp sgt i32 %43, 9
  br i1 %cmp53, label %if.then54, label %if.end59

if.then54:                                        ; preds = %if.end52
  %44 = load i32, i32* %x, align 4
  %45 = load i32, i32* %z, align 4
  %add55 = add nsw i32 %44, %45
  %add56 = add nsw i32 %add55, 92
  store i32 %add56, i32* %y, align 4
  %46 = load i32, i32* %x, align 4
  %47 = load i32, i32* %y, align 4
  %add57 = add nsw i32 %46, %47
  %add58 = add nsw i32 %add57, 33
  store i32 %add58, i32* %z, align 4
  br label %if.end59

if.end59:                                         ; preds = %if.then54, %if.end52
  %48 = load i32, i32* %x, align 4
  %cmp60 = icmp sgt i32 %48, 10
  br i1 %cmp60, label %if.then61, label %if.end66

if.then61:                                        ; preds = %if.end59
  %49 = load i32, i32* %x, align 4
  %50 = load i32, i32* %z, align 4
  %add62 = add nsw i32 %49, %50
  %add63 = add nsw i32 %add62, 3
  store i32 %add63, i32* %y, align 4
  %51 = load i32, i32* %x, align 4
  %52 = load i32, i32* %y, align 4
  %add64 = add nsw i32 %51, %52
  %add65 = add nsw i32 %add64, 78
  store i32 %add65, i32* %z, align 4
  br label %if.end66

if.end66:                                         ; preds = %if.then61, %if.end59
  %53 = load i32, i32* %x, align 4
  %cmp67 = icmp sgt i32 %53, 11
  br i1 %cmp67, label %if.then68, label %if.end73

if.then68:                                        ; preds = %if.end66
  %54 = load i32, i32* %x, align 4
  %55 = load i32, i32* %z, align 4
  %add69 = add nsw i32 %54, %55
  %add70 = add nsw i32 %add69, 94
  store i32 %add70, i32* %y, align 4
  %56 = load i32, i32* %x, align 4
  %57 = load i32, i32* %y, align 4
  %add71 = add nsw i32 %56, %57
  %add72 = add nsw i32 %add71, 56
  store i32 %add72, i32* %z, align 4
  br label %if.end73

if.end73:                                         ; preds = %if.then68, %if.end66
  %58 = load i32, i32* %x, align 4
  %cmp74 = icmp sgt i32 %58, 12
  br i1 %cmp74, label %if.then75, label %if.end80

if.then75:                                        ; preds = %if.end73
  %59 = load i32, i32* %x, align 4
  %60 = load i32, i32* %z, align 4
  %add76 = add nsw i32 %59, %60
  %add77 = add nsw i32 %add76, 73
  store i32 %add77, i32* %y, align 4
  %61 = load i32, i32* %x, align 4
  %62 = load i32, i32* %y, align 4
  %add78 = add nsw i32 %61, %62
  %add79 = add nsw i32 %add78, 34
  store i32 %add79, i32* %z, align 4
  br label %if.end80

if.end80:                                         ; preds = %if.then75, %if.end73
  %63 = load i32, i32* %x, align 4
  %cmp81 = icmp sgt i32 %63, 13
  br i1 %cmp81, label %if.then82, label %if.end87

if.then82:                                        ; preds = %if.end80
  %64 = load i32, i32* %x, align 4
  %65 = load i32, i32* %z, align 4
  %add83 = add nsw i32 %64, %65
  %add84 = add nsw i32 %add83, 67
  store i32 %add84, i32* %y, align 4
  %66 = load i32, i32* %x, align 4
  %67 = load i32, i32* %y, align 4
  %add85 = add nsw i32 %66, %67
  %add86 = add nsw i32 %add85, 64
  store i32 %add86, i32* %z, align 4
  br label %if.end87

if.end87:                                         ; preds = %if.then82, %if.end80
  %68 = load i32, i32* %x, align 4
  %cmp88 = icmp sgt i32 %68, 14
  br i1 %cmp88, label %if.then89, label %if.end94

if.then89:                                        ; preds = %if.end87
  %69 = load i32, i32* %x, align 4
  %70 = load i32, i32* %z, align 4
  %add90 = add nsw i32 %69, %70
  %add91 = add nsw i32 %add90, 74
  store i32 %add91, i32* %y, align 4
  %71 = load i32, i32* %x, align 4
  %72 = load i32, i32* %y, align 4
  %add92 = add nsw i32 %71, %72
  %add93 = add nsw i32 %add92, 98
  store i32 %add93, i32* %z, align 4
  br label %if.end94

if.end94:                                         ; preds = %if.then89, %if.end87
  %73 = load i32, i32* %x, align 4
  %cmp95 = icmp sgt i32 %73, 15
  br i1 %cmp95, label %if.then96, label %if.end101

if.then96:                                        ; preds = %if.end94
  %74 = load i32, i32* %x, align 4
  %75 = load i32, i32* %z, align 4
  %add97 = add nsw i32 %74, %75
  %add98 = add nsw i32 %add97, 73
  store i32 %add98, i32* %y, align 4
  %76 = load i32, i32* %x, align 4
  %77 = load i32, i32* %y, align 4
  %add99 = add nsw i32 %76, %77
  %add100 = add nsw i32 %add99, 68
  store i32 %add100, i32* %z, align 4
  br label %if.end101

if.end101:                                        ; preds = %if.then96, %if.end94
  %78 = load i32, i32* %x, align 4
  %cmp102 = icmp sgt i32 %78, 16
  br i1 %cmp102, label %if.then103, label %if.end108

if.then103:                                       ; preds = %if.end101
  %79 = load i32, i32* %x, align 4
  %80 = load i32, i32* %z, align 4
  %add104 = add nsw i32 %79, %80
  %add105 = add nsw i32 %add104, 90
  store i32 %add105, i32* %y, align 4
  %81 = load i32, i32* %x, align 4
  %82 = load i32, i32* %y, align 4
  %add106 = add nsw i32 %81, %82
  %add107 = add nsw i32 %add106, 62
  store i32 %add107, i32* %z, align 4
  br label %if.end108

if.end108:                                        ; preds = %if.then103, %if.end101
  %83 = load i32, i32* %x, align 4
  %84 = load i32, i32* %y, align 4
  %add109 = add nsw i32 %83, %84
  %85 = load i32, i32* %c.addr, align 4
  %add110 = add nsw i32 %add109, %85
  ret i32 %add110
}

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="all" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 10.0.0-4ubuntu1 "}
