; ModuleID = 'multipath.c'
source_filename = "multipath.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
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
  store i32 %add, i32* %y, align 4
  %6 = load i32, i32* %x, align 4
  %7 = load i32, i32* %y, align 4
  %add1 = add nsw i32 %6, %7
  store i32 %add1, i32* %z, align 4
  br label %if.end

if.end:                                           ; preds = %if.then, %entry
  %8 = load i32, i32* %x, align 4
  %cmp2 = icmp sgt i32 %8, 2
  br i1 %cmp2, label %if.then3, label %if.end6

if.then3:                                         ; preds = %if.end
  %9 = load i32, i32* %x, align 4
  %10 = load i32, i32* %z, align 4
  %add4 = add nsw i32 %9, %10
  store i32 %add4, i32* %y, align 4
  %11 = load i32, i32* %x, align 4
  %12 = load i32, i32* %y, align 4
  %add5 = add nsw i32 %11, %12
  store i32 %add5, i32* %z, align 4
  br label %if.end6

if.end6:                                          ; preds = %if.then3, %if.end
  %13 = load i32, i32* %x, align 4
  %cmp7 = icmp sgt i32 %13, 3
  br i1 %cmp7, label %if.then8, label %if.end11

if.then8:                                         ; preds = %if.end6
  %14 = load i32, i32* %x, align 4
  %15 = load i32, i32* %z, align 4
  %add9 = add nsw i32 %14, %15
  store i32 %add9, i32* %y, align 4
  %16 = load i32, i32* %x, align 4
  %17 = load i32, i32* %y, align 4
  %add10 = add nsw i32 %16, %17
  store i32 %add10, i32* %z, align 4
  br label %if.end11

if.end11:                                         ; preds = %if.then8, %if.end6
  %18 = load i32, i32* %x, align 4
  %cmp12 = icmp sgt i32 %18, 4
  br i1 %cmp12, label %if.then13, label %if.end16

if.then13:                                        ; preds = %if.end11
  %19 = load i32, i32* %x, align 4
  %20 = load i32, i32* %z, align 4
  %add14 = add nsw i32 %19, %20
  store i32 %add14, i32* %y, align 4
  %21 = load i32, i32* %x, align 4
  %22 = load i32, i32* %y, align 4
  %add15 = add nsw i32 %21, %22
  store i32 %add15, i32* %z, align 4
  br label %if.end16

if.end16:                                         ; preds = %if.then13, %if.end11
  %23 = load i32, i32* %x, align 4
  %cmp17 = icmp sgt i32 %23, 5
  br i1 %cmp17, label %if.then18, label %if.end21

if.then18:                                        ; preds = %if.end16
  %24 = load i32, i32* %x, align 4
  %25 = load i32, i32* %z, align 4
  %add19 = add nsw i32 %24, %25
  store i32 %add19, i32* %y, align 4
  %26 = load i32, i32* %x, align 4
  %27 = load i32, i32* %y, align 4
  %add20 = add nsw i32 %26, %27
  store i32 %add20, i32* %z, align 4
  br label %if.end21

if.end21:                                         ; preds = %if.then18, %if.end16
  %28 = load i32, i32* %x, align 4
  %cmp22 = icmp sgt i32 %28, 6
  br i1 %cmp22, label %if.then23, label %if.end26

if.then23:                                        ; preds = %if.end21
  %29 = load i32, i32* %x, align 4
  %30 = load i32, i32* %z, align 4
  %add24 = add nsw i32 %29, %30
  store i32 %add24, i32* %y, align 4
  %31 = load i32, i32* %x, align 4
  %32 = load i32, i32* %y, align 4
  %add25 = add nsw i32 %31, %32
  store i32 %add25, i32* %z, align 4
  br label %if.end26

if.end26:                                         ; preds = %if.then23, %if.end21
  %33 = load i32, i32* %x, align 4
  %cmp27 = icmp sgt i32 %33, 7
  br i1 %cmp27, label %if.then28, label %if.end31

if.then28:                                        ; preds = %if.end26
  %34 = load i32, i32* %x, align 4
  %35 = load i32, i32* %z, align 4
  %add29 = add nsw i32 %34, %35
  store i32 %add29, i32* %y, align 4
  %36 = load i32, i32* %x, align 4
  %37 = load i32, i32* %y, align 4
  %add30 = add nsw i32 %36, %37
  store i32 %add30, i32* %z, align 4
  br label %if.end31

if.end31:                                         ; preds = %if.then28, %if.end26
  %38 = load i32, i32* %x, align 4
  %cmp32 = icmp sgt i32 %38, 8
  br i1 %cmp32, label %if.then33, label %if.end36

if.then33:                                        ; preds = %if.end31
  %39 = load i32, i32* %x, align 4
  %40 = load i32, i32* %z, align 4
  %add34 = add nsw i32 %39, %40
  store i32 %add34, i32* %y, align 4
  %41 = load i32, i32* %x, align 4
  %42 = load i32, i32* %y, align 4
  %add35 = add nsw i32 %41, %42
  store i32 %add35, i32* %z, align 4
  br label %if.end36

if.end36:                                         ; preds = %if.then33, %if.end31
  %43 = load i32, i32* %x, align 4
  %cmp37 = icmp sgt i32 %43, 9
  br i1 %cmp37, label %if.then38, label %if.end41

if.then38:                                        ; preds = %if.end36
  %44 = load i32, i32* %x, align 4
  %45 = load i32, i32* %z, align 4
  %add39 = add nsw i32 %44, %45
  store i32 %add39, i32* %y, align 4
  %46 = load i32, i32* %x, align 4
  %47 = load i32, i32* %y, align 4
  %add40 = add nsw i32 %46, %47
  store i32 %add40, i32* %z, align 4
  br label %if.end41

if.end41:                                         ; preds = %if.then38, %if.end36
  %48 = load i32, i32* %x, align 4
  %cmp42 = icmp sgt i32 %48, 10
  br i1 %cmp42, label %if.then43, label %if.end46

if.then43:                                        ; preds = %if.end41
  %49 = load i32, i32* %x, align 4
  %50 = load i32, i32* %z, align 4
  %add44 = add nsw i32 %49, %50
  store i32 %add44, i32* %y, align 4
  %51 = load i32, i32* %x, align 4
  %52 = load i32, i32* %y, align 4
  %add45 = add nsw i32 %51, %52
  store i32 %add45, i32* %z, align 4
  br label %if.end46

if.end46:                                         ; preds = %if.then43, %if.end41
  %53 = load i32, i32* %x, align 4
  %54 = load i32, i32* %y, align 4
  %add47 = add nsw i32 %53, %54
  %55 = load i32, i32* %c.addr, align 4
  %add48 = add nsw i32 %add47, %55
  ret i32 %add48
}

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0-2~ubuntu18.04.2 (tags/RELEASE_900/final)"}
