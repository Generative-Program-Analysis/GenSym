#include <stdio.h>
#include <stdlib.h>

#include "pow.wasm.h"

int main(int argc, char** argv) {
  /* Initialize the Wasm runtime. */
  wasm_rt_init();

  /* Declare an instance of the `pow` module. */
  w2c_pow pow;

  /* Construct the module instance. */
  wasm2c_pow_instantiate(&pow);

  /* Call `real_main`, using the mangled name. */
  w2c_pow_real_main(&pow);

  wasm2c_pow_free(&pow);

  /* Free the Wasm runtime state. */
  wasm_rt_free();

  return 0;
}
