(module
  (func $test_block (param i32 i32 i32) (result i32)
  	local.get 0
  	local.get 1
  	local.get 2
    block (param i32 i32 i32) (result i32 i32)
      i32.add
    end
    i32.add
  )

  (func $real_main (result i32)
	  i32.const 1
	  i32.const 3
	  i32.const 5
	  call $test_block
  )
  (export "real_main" (func $real_main))
)
