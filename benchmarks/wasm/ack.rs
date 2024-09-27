#[no_mangle]
#[inline(never)]
fn ack(m: i32, n: i32) -> i32 {
    if m == 0 {
        n + 1
    } else if n == 0 {
        ack(m - 1, 1)
    } else {
        ack(m - 1, ack(m, n - 1))
    }
}

#[no_mangle]
fn real_main() -> i32 {
    ack(2, 2)
}
