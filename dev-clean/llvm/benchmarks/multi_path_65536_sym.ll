; ModuleID = 'multi_path_65536_sym.c'
source_filename = "multi_path_65536_sym.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @f(i32 %x0, i32 %x1, i32 %x2, i32 %x3, i32 %x4, i32 %x5, i32 %x6, i32 %x7, i32 %x8, i32 %x9, i32 %x10, i32 %x11, i32 %x12, i32 %x13, i32 %x14, i32 %x15) #0 {
entry:
  %x0.addr = alloca i32, align 4
  %x1.addr = alloca i32, align 4
  %x2.addr = alloca i32, align 4
  %x3.addr = alloca i32, align 4
  %x4.addr = alloca i32, align 4
  %x5.addr = alloca i32, align 4
  %x6.addr = alloca i32, align 4
  %x7.addr = alloca i32, align 4
  %x8.addr = alloca i32, align 4
  %x9.addr = alloca i32, align 4
  %x10.addr = alloca i32, align 4
  %x11.addr = alloca i32, align 4
  %x12.addr = alloca i32, align 4
  %x13.addr = alloca i32, align 4
  %x14.addr = alloca i32, align 4
  %x15.addr = alloca i32, align 4
  store i32 %x0, i32* %x0.addr, align 4
  store i32 %x1, i32* %x1.addr, align 4
  store i32 %x2, i32* %x2.addr, align 4
  store i32 %x3, i32* %x3.addr, align 4
  store i32 %x4, i32* %x4.addr, align 4
  store i32 %x5, i32* %x5.addr, align 4
  store i32 %x6, i32* %x6.addr, align 4
  store i32 %x7, i32* %x7.addr, align 4
  store i32 %x8, i32* %x8.addr, align 4
  store i32 %x9, i32* %x9.addr, align 4
  store i32 %x10, i32* %x10.addr, align 4
  store i32 %x11, i32* %x11.addr, align 4
  store i32 %x12, i32* %x12.addr, align 4
  store i32 %x13, i32* %x13.addr, align 4
  store i32 %x14, i32* %x14.addr, align 4
  store i32 %x15, i32* %x15.addr, align 4
  %0 = load i32, i32* %x0.addr, align 4
  %cmp = icmp sgt i32 %0, 0
  br i1 %cmp, label %if.then, label %if.end

if.then:                                          ; preds = %entry
  %1 = load i32, i32* %x13.addr, align 4
  %2 = load i32, i32* %x3.addr, align 4
  %add = add nsw i32 %1, %2
  %add1 = add nsw i32 %add, 56
  store i32 %add1, i32* %x0.addr, align 4
  br label %if.end

if.end:                                           ; preds = %if.then, %entry
  %3 = load i32, i32* %x1.addr, align 4
  %cmp2 = icmp sgt i32 %3, 1
  br i1 %cmp2, label %if.then3, label %if.end6

if.then3:                                         ; preds = %if.end
  %4 = load i32, i32* %x0.addr, align 4
  %5 = load i32, i32* %x15.addr, align 4
  %add4 = add nsw i32 %4, %5
  %add5 = add nsw i32 %add4, 78
  store i32 %add5, i32* %x1.addr, align 4
  br label %if.end6

if.end6:                                          ; preds = %if.then3, %if.end
  %6 = load i32, i32* %x2.addr, align 4
  %cmp7 = icmp sgt i32 %6, 2
  br i1 %cmp7, label %if.then8, label %if.end11

if.then8:                                         ; preds = %if.end6
  %7 = load i32, i32* %x15.addr, align 4
  %8 = load i32, i32* %x10.addr, align 4
  %add9 = add nsw i32 %7, %8
  %add10 = add nsw i32 %add9, 2
  store i32 %add10, i32* %x2.addr, align 4
  br label %if.end11

if.end11:                                         ; preds = %if.then8, %if.end6
  %9 = load i32, i32* %x3.addr, align 4
  %cmp12 = icmp sgt i32 %9, 3
  br i1 %cmp12, label %if.then13, label %if.end16

if.then13:                                        ; preds = %if.end11
  %10 = load i32, i32* %x12.addr, align 4
  %11 = load i32, i32* %x3.addr, align 4
  %add14 = add nsw i32 %10, %11
  %add15 = add nsw i32 %add14, 96
  store i32 %add15, i32* %x3.addr, align 4
  br label %if.end16

if.end16:                                         ; preds = %if.then13, %if.end11
  %12 = load i32, i32* %x4.addr, align 4
  %cmp17 = icmp sgt i32 %12, 4
  br i1 %cmp17, label %if.then18, label %if.end21

if.then18:                                        ; preds = %if.end16
  %13 = load i32, i32* %x1.addr, align 4
  %14 = load i32, i32* %x7.addr, align 4
  %add19 = add nsw i32 %13, %14
  %add20 = add nsw i32 %add19, 57
  store i32 %add20, i32* %x4.addr, align 4
  br label %if.end21

if.end21:                                         ; preds = %if.then18, %if.end16
  %15 = load i32, i32* %x5.addr, align 4
  %cmp22 = icmp sgt i32 %15, 5
  br i1 %cmp22, label %if.then23, label %if.end26

if.then23:                                        ; preds = %if.end21
  %16 = load i32, i32* %x4.addr, align 4
  %17 = load i32, i32* %x4.addr, align 4
  %add24 = add nsw i32 %16, %17
  %add25 = add nsw i32 %add24, 35
  store i32 %add25, i32* %x5.addr, align 4
  br label %if.end26

if.end26:                                         ; preds = %if.then23, %if.end21
  %18 = load i32, i32* %x6.addr, align 4
  %cmp27 = icmp sgt i32 %18, 6
  br i1 %cmp27, label %if.then28, label %if.end31

if.then28:                                        ; preds = %if.end26
  %19 = load i32, i32* %x7.addr, align 4
  %20 = load i32, i32* %x3.addr, align 4
  %add29 = add nsw i32 %19, %20
  %add30 = add nsw i32 %add29, 29
  store i32 %add30, i32* %x6.addr, align 4
  br label %if.end31

if.end31:                                         ; preds = %if.then28, %if.end26
  %21 = load i32, i32* %x7.addr, align 4
  %cmp32 = icmp sgt i32 %21, 7
  br i1 %cmp32, label %if.then33, label %if.end36

if.then33:                                        ; preds = %if.end31
  %22 = load i32, i32* %x4.addr, align 4
  %23 = load i32, i32* %x2.addr, align 4
  %add34 = add nsw i32 %22, %23
  %add35 = add nsw i32 %add34, 57
  store i32 %add35, i32* %x7.addr, align 4
  br label %if.end36

if.end36:                                         ; preds = %if.then33, %if.end31
  %24 = load i32, i32* %x8.addr, align 4
  %cmp37 = icmp sgt i32 %24, 8
  br i1 %cmp37, label %if.then38, label %if.end41

if.then38:                                        ; preds = %if.end36
  %25 = load i32, i32* %x0.addr, align 4
  %26 = load i32, i32* %x9.addr, align 4
  %add39 = add nsw i32 %25, %26
  %add40 = add nsw i32 %add39, 27
  store i32 %add40, i32* %x8.addr, align 4
  br label %if.end41

if.end41:                                         ; preds = %if.then38, %if.end36
  %27 = load i32, i32* %x9.addr, align 4
  %cmp42 = icmp sgt i32 %27, 9
  br i1 %cmp42, label %if.then43, label %if.end46

if.then43:                                        ; preds = %if.end41
  %28 = load i32, i32* %x14.addr, align 4
  %29 = load i32, i32* %x12.addr, align 4
  %add44 = add nsw i32 %28, %29
  %add45 = add nsw i32 %add44, 84
  store i32 %add45, i32* %x9.addr, align 4
  br label %if.end46

if.end46:                                         ; preds = %if.then43, %if.end41
  %30 = load i32, i32* %x10.addr, align 4
  %cmp47 = icmp sgt i32 %30, 10
  br i1 %cmp47, label %if.then48, label %if.end51

if.then48:                                        ; preds = %if.end46
  %31 = load i32, i32* %x5.addr, align 4
  %32 = load i32, i32* %x4.addr, align 4
  %add49 = add nsw i32 %31, %32
  %add50 = add nsw i32 %add49, 28
  store i32 %add50, i32* %x10.addr, align 4
  br label %if.end51

if.end51:                                         ; preds = %if.then48, %if.end46
  %33 = load i32, i32* %x11.addr, align 4
  %cmp52 = icmp sgt i32 %33, 11
  br i1 %cmp52, label %if.then53, label %if.end56

if.then53:                                        ; preds = %if.end51
  %34 = load i32, i32* %x13.addr, align 4
  %35 = load i32, i32* %x4.addr, align 4
  %add54 = add nsw i32 %34, %35
  %add55 = add nsw i32 %add54, 38
  store i32 %add55, i32* %x11.addr, align 4
  br label %if.end56

if.end56:                                         ; preds = %if.then53, %if.end51
  %36 = load i32, i32* %x12.addr, align 4
  %cmp57 = icmp sgt i32 %36, 12
  br i1 %cmp57, label %if.then58, label %if.end61

if.then58:                                        ; preds = %if.end56
  %37 = load i32, i32* %x2.addr, align 4
  %38 = load i32, i32* %x1.addr, align 4
  %add59 = add nsw i32 %37, %38
  %add60 = add nsw i32 %add59, 28
  store i32 %add60, i32* %x12.addr, align 4
  br label %if.end61

if.end61:                                         ; preds = %if.then58, %if.end56
  %39 = load i32, i32* %x13.addr, align 4
  %cmp62 = icmp sgt i32 %39, 13
  br i1 %cmp62, label %if.then63, label %if.end66

if.then63:                                        ; preds = %if.end61
  %40 = load i32, i32* %x0.addr, align 4
  %41 = load i32, i32* %x14.addr, align 4
  %add64 = add nsw i32 %40, %41
  %add65 = add nsw i32 %add64, 64
  store i32 %add65, i32* %x13.addr, align 4
  br label %if.end66

if.end66:                                         ; preds = %if.then63, %if.end61
  %42 = load i32, i32* %x14.addr, align 4
  %cmp67 = icmp sgt i32 %42, 14
  br i1 %cmp67, label %if.then68, label %if.end71

if.then68:                                        ; preds = %if.end66
  %43 = load i32, i32* %x7.addr, align 4
  %44 = load i32, i32* %x13.addr, align 4
  %add69 = add nsw i32 %43, %44
  %add70 = add nsw i32 %add69, 93
  store i32 %add70, i32* %x14.addr, align 4
  br label %if.end71

if.end71:                                         ; preds = %if.then68, %if.end66
  %45 = load i32, i32* %x15.addr, align 4
  %cmp72 = icmp sgt i32 %45, 15
  br i1 %cmp72, label %if.then73, label %if.end76

if.then73:                                        ; preds = %if.end71
  %46 = load i32, i32* %x10.addr, align 4
  %47 = load i32, i32* %x11.addr, align 4
  %add74 = add nsw i32 %46, %47
  %add75 = add nsw i32 %add74, 0
  store i32 %add75, i32* %x15.addr, align 4
  br label %if.end76

if.end76:                                         ; preds = %if.then73, %if.end71
  %48 = load i32, i32* %x12.addr, align 4
  %49 = load i32, i32* %x11.addr, align 4
  %add77 = add nsw i32 %48, %49
  ret i32 %add77
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  store i32 0, i32* %retval, align 4
  ret i32 0
}

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0 (tags/RELEASE_900/final)"}
