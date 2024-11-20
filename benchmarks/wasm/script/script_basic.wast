(module
 (func $one (result i32)
       i32.const 1)
 (export "one" (func 0))
)

(assert_return (invoke "one") (i32.const 1))

