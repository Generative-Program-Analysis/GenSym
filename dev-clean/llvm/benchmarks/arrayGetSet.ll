; ModuleID = 'arrayGetSet.c'
source_filename = "arrayGetSet.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @arrayGetSet() #0 {
entry:
  %arr = alloca [4 x [3 x [5 x i32]]], align 16
  %arrayidx = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 3
  %arrayidx1 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx, i64 0, i64 2
  %arrayidx2 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx1, i64 0, i64 3
  store i32 323, i32* %arrayidx2, align 4
  %arrayidx3 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 1
  %arrayidx4 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx3, i64 0, i64 0
  %arrayidx5 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx4, i64 0, i64 1
  store i32 101, i32* %arrayidx5, align 4
  %arrayidx6 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 2
  %arrayidx7 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx6, i64 0, i64 1
  %arrayidx8 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx7, i64 0, i64 2
  store i32 211, i32* %arrayidx8, align 4
  %arrayidx9 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 2
  %arrayidx10 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx9, i64 0, i64 1
  %arrayidx11 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx10, i64 0, i64 2
  store i32 212, i32* %arrayidx11, align 4
  %arrayidx12 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 3
  %arrayidx13 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx12, i64 0, i64 2
  %arrayidx14 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx13, i64 0, i64 3
  %0 = load i32, i32* %arrayidx14, align 4
  %arrayidx15 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 1
  %arrayidx16 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx15, i64 0, i64 0
  %arrayidx17 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx16, i64 0, i64 1
  %1 = load i32, i32* %arrayidx17, align 4
  %add = add nsw i32 %0, %1
  %arrayidx18 = getelementptr inbounds [4 x [3 x [5 x i32]]], [4 x [3 x [5 x i32]]]* %arr, i64 0, i64 2
  %arrayidx19 = getelementptr inbounds [3 x [5 x i32]], [3 x [5 x i32]]* %arrayidx18, i64 0, i64 1
  %arrayidx20 = getelementptr inbounds [5 x i32], [5 x i32]* %arrayidx19, i64 0, i64 2
  %2 = load i32, i32* %arrayidx20, align 4
  %add21 = add nsw i32 %add, %2
  ret i32 %add21
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  store i32 0, i32* %retval, align 4
  %call = call i32 @arrayGetSet()
  ret i32 %call
}

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0 (tags/RELEASE_900/final)"}
