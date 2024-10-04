#[no_mangle]
#[inline(never)]
fn is_even(n: u32) -> bool {
    if n == 0 { true }
    else { is_odd(n - 1) }
}

#[no_mangle]
#[inline(never)]
fn is_odd(n: u32) -> bool {
    if n == 0 { false }
    else { is_even(n - 1) }
}

#[no_mangle]
fn real_main() -> bool {
    is_even(12)
}
