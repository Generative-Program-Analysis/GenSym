; ModuleID = 'maze.c'
source_filename = "maze.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@maze = global [7 x [11 x i8]] [[11 x i8] c"+-+---+---+", [11 x i8] c"| |     |#|", [11 x i8] c"| | --+ | |", [11 x i8] c"| |   | | |", [11 x i8] c"| +-- | | |", [11 x i8] c"|     |   |", [11 x i8] c"+-----+---+"], align 16
@.str = private unnamed_addr constant [3 x i8] c"%c\00", align 1
@.str.1 = private unnamed_addr constant [2 x i8] c"\0A\00", align 1
@.str.2 = private unnamed_addr constant [24 x i8] c"Maze dimensions: %dx%d\0A\00", align 1
@.str.3 = private unnamed_addr constant [19 x i8] c"Player pos: %dx%d\0A\00", align 1
@.str.4 = private unnamed_addr constant [18 x i8] c"Iteration no. %d\0A\00", align 1
@.str.5 = private unnamed_addr constant [67 x i8] c"Program the player moves with a sequence of 'w', 's', 'a' and 'd'\0A\00", align 1
@.str.6 = private unnamed_addr constant [28 x i8] c"Try to reach the price(#)!\0A\00", align 1
@.str.7 = private unnamed_addr constant [40 x i8] c"Wrong command!(only w,s,a,d accepted!)\0A\00", align 1
@.str.8 = private unnamed_addr constant [12 x i8] c"You loose!\0A\00", align 1
@.str.9 = private unnamed_addr constant [10 x i8] c"You win!\0A\00", align 1
@.str.10 = private unnamed_addr constant [22 x i8] c"Your solution <%42s>\0A\00", align 1
@.str.11 = private unnamed_addr constant [34 x i8] c"Iteration no. %d. Action: %c. %s\0A\00", align 1
@.str.12 = private unnamed_addr constant [9 x i8] c"Blocked!\00", align 1
@.str.13 = private unnamed_addr constant [1 x i8] zeroinitializer, align 1
@.str.14 = private unnamed_addr constant [11 x i8] c"You loose\0A\00", align 1

; br, ret, swtich, unreachable
; alloca, store, load, getelementptr
; icmp, sext, zext, add
; call, phi, select

; Function Attrs: noinline nounwind optnone uwtable
define void @draw() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  store i32 0, i32* %1, align 4
  br label %3

; <label>:3:                                      ; preds = %25, %0
  %4 = load i32, i32* %1, align 4
  %5 = icmp slt i32 %4, 7
  br i1 %5, label %6, label %28

; <label>:6:                                      ; preds = %3
  store i32 0, i32* %2, align 4
  br label %7

; <label>:7:                                      ; preds = %20, %6
  %8 = load i32, i32* %2, align 4
  %9 = icmp slt i32 %8, 11
  br i1 %9, label %10, label %23

; <label>:10:                                     ; preds = %7
  %11 = load i32, i32* %1, align 4
  %12 = sext i32 %11 to i64
  %13 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %12
  %14 = load i32, i32* %2, align 4
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds [11 x i8], [11 x i8]* %13, i64 0, i64 %15
  %17 = load i8, i8* %16, align 1
  %18 = sext i8 %17 to i32
  %19 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i32 0, i32 0), i32 %18)
  br label %20

; <label>:20:                                     ; preds = %10
  %21 = load i32, i32* %2, align 4
  %22 = add nsw i32 %21, 1
  store i32 %22, i32* %2, align 4
  br label %7

; <label>:23:                                     ; preds = %7
  %24 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.1, i32 0, i32 0))
  br label %25

; <label>:25:                                     ; preds = %23
  %26 = load i32, i32* %1, align 4
  %27 = add nsw i32 %26, 1
  store i32 %27, i32* %1, align 4
  br label %3

; <label>:28:                                     ; preds = %3
  %29 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.str.1, i32 0, i32 0))
  ret void
}

declare i32 @printf(i8*, ...) #1

; Function Attrs: noinline nounwind optnone uwtable
define i32 @main(i32, i8**) #0 {
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i8**, align 8
  %6 = alloca i32, align 4
  %7 = alloca i32, align 4
  %8 = alloca i32, align 4
  %9 = alloca i32, align 4
  %10 = alloca i32, align 4
  %11 = alloca [28 x i8], align 16
  store i32 0, i32* %3, align 4
  store i32 %0, i32* %4, align 4
  store i8** %1, i8*** %5, align 8
  store i32 0, i32* %10, align 4
  store i32 1, i32* %6, align 4
  store i32 1, i32* %7, align 4
  %12 = load i32, i32* %7, align 4
  %13 = sext i32 %12 to i64
  %14 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %13
  %15 = load i32, i32* %6, align 4
  %16 = sext i32 %15 to i64
  %17 = getelementptr inbounds [11 x i8], [11 x i8]* %14, i64 0, i64 %16
  store i8 88, i8* %17, align 1
  %18 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([24 x i8], [24 x i8]* @.str.2, i32 0, i32 0), i32 11, i32 7)
  %19 = load i32, i32* %6, align 4
  %20 = load i32, i32* %7, align 4
  %21 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str.3, i32 0, i32 0), i32 %19, i32 %20)
  %22 = load i32, i32* %10, align 4
  %23 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([18 x i8], [18 x i8]* @.str.4, i32 0, i32 0), i32 %22)
  %24 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([67 x i8], [67 x i8]* @.str.5, i32 0, i32 0))
  %25 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([28 x i8], [28 x i8]* @.str.6, i32 0, i32 0))
  call void @draw()
  %26 = getelementptr inbounds [28 x i8], [28 x i8]* %11, i32 0, i32 0
  %27 = call i32 (i32, i8*, i32, ...) bitcast (i32 (...)* @read to i32 (i32, i8*, i32, ...)*)(i32 0, i8* %26, i32 28)
  br label %28

; <label>:28:                                     ; preds = %131, %2
  %29 = load i32, i32* %10, align 4
  %30 = icmp slt i32 %29, 28
  br i1 %30, label %31, label %141

; <label>:31:                                     ; preds = %28
  %32 = load i32, i32* %6, align 4
  store i32 %32, i32* %8, align 4
  %33 = load i32, i32* %7, align 4
  store i32 %33, i32* %9, align 4
  %34 = load i32, i32* %10, align 4
  %35 = sext i32 %34 to i64
  %36 = getelementptr inbounds [28 x i8], [28 x i8]* %11, i64 0, i64 %35
  %37 = load i8, i8* %36, align 1
  %38 = sext i8 %37 to i32
  switch i32 %38, label %51 [
    i32 119, label %39
    i32 115, label %42
    i32 97, label %45
    i32 100, label %48
  ]

; <label>:39:                                     ; preds = %31
  %40 = load i32, i32* %7, align 4
  %41 = add nsw i32 %40, -1
  store i32 %41, i32* %7, align 4
  br label %54

; <label>:42:                                     ; preds = %31
  %43 = load i32, i32* %7, align 4
  %44 = add nsw i32 %43, 1
  store i32 %44, i32* %7, align 4
  br label %54

; <label>:45:                                     ; preds = %31
  %46 = load i32, i32* %6, align 4
  %47 = add nsw i32 %46, -1
  store i32 %47, i32* %6, align 4
  br label %54

; <label>:48:                                     ; preds = %31
  %49 = load i32, i32* %6, align 4
  %50 = add nsw i32 %49, 1
  store i32 %50, i32* %6, align 4
  br label %54

; <label>:51:                                     ; preds = %31
  %52 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([40 x i8], [40 x i8]* @.str.7, i32 0, i32 0))
  %53 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([12 x i8], [12 x i8]* @.str.8, i32 0, i32 0))
  call void @exit(i32 -1) #3
  unreachable

; <label>:54:                                     ; preds = %48, %45, %42, %39
  %55 = load i32, i32* %7, align 4
  %56 = sext i32 %55 to i64
  %57 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %56
  %58 = load i32, i32* %6, align 4
  %59 = sext i32 %58 to i64
  %60 = getelementptr inbounds [11 x i8], [11 x i8]* %57, i64 0, i64 %59
  %61 = load i8, i8* %60, align 1
  %62 = sext i8 %61 to i32
  %63 = icmp eq i32 %62, 35
  br i1 %63, label %64, label %68

; <label>:64:                                     ; preds = %54
  %65 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str.9, i32 0, i32 0))
  %66 = getelementptr inbounds [28 x i8], [28 x i8]* %11, i32 0, i32 0
  %67 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([22 x i8], [22 x i8]* @.str.10, i32 0, i32 0), i8* %66)
  call void @exit(i32 1) #3
  unreachable

; <label>:68:                                     ; preds = %54
  %69 = load i32, i32* %7, align 4
  %70 = sext i32 %69 to i64
  %71 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %70
  %72 = load i32, i32* %6, align 4
  %73 = sext i32 %72 to i64
  %74 = getelementptr inbounds [11 x i8], [11 x i8]* %71, i64 0, i64 %73
  %75 = load i8, i8* %74, align 1
  %76 = sext i8 %75 to i32
  %77 = icmp ne i32 %76, 32
  br i1 %77, label %78, label %100

; <label>:78:                                     ; preds = %68
  %79 = load i32, i32* %7, align 4
  %80 = icmp eq i32 %79, 2
  br i1 %80, label %81, label %97

; <label>:81:                                     ; preds = %78
  %82 = load i32, i32* %7, align 4
  %83 = sext i32 %82 to i64
  %84 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %83
  %85 = load i32, i32* %6, align 4
  %86 = sext i32 %85 to i64
  %87 = getelementptr inbounds [11 x i8], [11 x i8]* %84, i64 0, i64 %86
  %88 = load i8, i8* %87, align 1
  %89 = sext i8 %88 to i32
  %90 = icmp eq i32 %89, 124
  br i1 %90, label %91, label %97

; <label>:91:                                     ; preds = %81
  %92 = load i32, i32* %6, align 4
  %93 = icmp sgt i32 %92, 0
  br i1 %93, label %94, label %97

; <label>:94:                                     ; preds = %91
  %95 = load i32, i32* %6, align 4
  %96 = icmp slt i32 %95, 11
  br i1 %96, label %100, label %97

; <label>:97:                                     ; preds = %94, %91, %81, %78
  %98 = load i32, i32* %8, align 4
  store i32 %98, i32* %6, align 4
  %99 = load i32, i32* %9, align 4
  store i32 %99, i32* %7, align 4
  br label %100

; <label>:100:                                    ; preds = %97, %94, %68
  %101 = load i32, i32* %6, align 4
  %102 = load i32, i32* %7, align 4
  %103 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str.3, i32 0, i32 0), i32 %101, i32 %102)
  %104 = load i32, i32* %10, align 4
  %105 = load i32, i32* %10, align 4
  %106 = sext i32 %105 to i64
  %107 = getelementptr inbounds [28 x i8], [28 x i8]* %11, i64 0, i64 %106
  %108 = load i8, i8* %107, align 1
  %109 = sext i8 %108 to i32
  %110 = load i32, i32* %8, align 4
  %111 = load i32, i32* %6, align 4
  %112 = icmp eq i32 %110, %111
  br i1 %112, label %113, label %117

; <label>:113:                                    ; preds = %100
  %114 = load i32, i32* %9, align 4
  %115 = load i32, i32* %7, align 4
  %116 = icmp eq i32 %114, %115
  br label %117

; <label>:117:                                    ; preds = %113, %100
  %118 = phi i1 [ false, %100 ], [ %116, %113 ]
  %119 = zext i1 %118 to i64
  %120 = select i1 %118, i8* getelementptr inbounds ([9 x i8], [9 x i8]* @.str.12, i32 0, i32 0), i8* getelementptr inbounds ([1 x i8], [1 x i8]* @.str.13, i32 0, i32 0)
  %121 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([34 x i8], [34 x i8]* @.str.11, i32 0, i32 0), i32 %104, i32 %109, i8* %120)
  %122 = load i32, i32* %8, align 4
  %123 = load i32, i32* %6, align 4
  %124 = icmp eq i32 %122, %123
  br i1 %124, label %125, label %131

; <label>:125:                                    ; preds = %117
  %126 = load i32, i32* %9, align 4
  %127 = load i32, i32* %7, align 4
  %128 = icmp eq i32 %126, %127
  br i1 %128, label %129, label %131

; <label>:129:                                    ; preds = %125
  %130 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.14, i32 0, i32 0))
  call void @exit(i32 -2) #3
  unreachable

; <label>:131:                                    ; preds = %125, %117
  %132 = load i32, i32* %7, align 4
  %133 = sext i32 %132 to i64
  %134 = getelementptr inbounds [7 x [11 x i8]], [7 x [11 x i8]]* @maze, i64 0, i64 %133
  %135 = load i32, i32* %6, align 4
  %136 = sext i32 %135 to i64
  %137 = getelementptr inbounds [11 x i8], [11 x i8]* %134, i64 0, i64 %136
  store i8 88, i8* %137, align 1
  call void @draw()
  %138 = load i32, i32* %10, align 4
  %139 = add nsw i32 %138, 1
  store i32 %139, i32* %10, align 4
  %140 = call i32 (i32, ...) bitcast (i32 (...)* @sleep to i32 (i32, ...)*)(i32 1)
  br label %28

; <label>:141:                                    ; preds = %28
  %142 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str.14, i32 0, i32 0))
  %143 = load i32, i32* %3, align 4
  ret i32 %143
}

declare i32 @read(...) #1

; Function Attrs: noreturn nounwind
declare void @exit(i32) #2

declare i32 @sleep(...) #1

attributes #0 = { noinline nounwind optnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #2 = { noreturn nounwind "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #3 = { noreturn nounwind }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 6.0.0-1ubuntu2 (tags/RELEASE_600/final)"}
