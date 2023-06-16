// #[no_mangle]
// #[inline(never)]
// fn test(a: i32, b: i32) -> i32 {
//     a + b
// }

// #[no_mangle]
// #[inline(never)]
// fn factorial(n: i32) -> i32 {
//     if n == 0 {
//         1
//     } else {
//         n * factorial(n - 1)
//     }
// }

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
#[inline(never)]
fn opaque(x: i32) -> i32 {
    x + 1
}

#[no_mangle]
#[inline(never)]
fn opaque1(x: i32) -> i32 {
    x + 5
}

#[no_mangle]
#[inline(never)]
fn opaque2(x: i32) -> i32 {
    x + 100
}

#[no_mangle]
#[inline(never)]
fn conditional(x: i32) -> i32 {
    if x == 0 {
        x + 10
    } else {
        opaque(x)
    }
}

#[no_mangle]
fn real_main() -> i32 {
    // conditional(5)
    // factorial(5)
    // power(3, 3)
    ack(2, 2)
}
