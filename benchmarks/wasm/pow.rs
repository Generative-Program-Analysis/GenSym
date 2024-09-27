#[no_mangle]
#[inline(never)]
fn power(a: i32, b: i32) -> i32 {
    if b == 0 {
        1
    } else {
        a * power(a, b - 1)
    }
}

#[no_mangle]
fn real_main() -> i32 {
    power(2, 10)
}
