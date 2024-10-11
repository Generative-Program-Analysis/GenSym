#[no_mangle]
#[inline(never)]
fn tribonacci(n: i32) -> i32 {
    if n == 0 { 0 }
    else if n == 1 { 1 }
    else if n == 2 { 1 }
    else { tribonacci(n - 1) + tribonacci(n - 2) + tribonacci(n - 3) }
}

#[no_mangle]
fn real_main() -> i32 {
    tribonacci(12)
}
