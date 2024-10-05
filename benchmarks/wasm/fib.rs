#[no_mangle]
#[inline(never)]
fn fibonacci(n: i32) -> i32 {
    if n == 0 { 0 }
    else if n == 1 { 1 }
    else { fibonacci(n - 1) + fibonacci(n - 2) }
}

#[no_mangle]
fn real_main() -> i32 {
    fibonacci(12)
}
