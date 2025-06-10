#include <stdio.h>
#include <stdlib.h>

#include "ack.wasm.h"

int main(int argc, char** argv) {
  /* Initialize the Wasm runtime. */
  wasm_rt_init();

  /* Declare an instance of the `ack` module. */
  w2c_ack ack;

  /* Construct the module instance. */
  wasm2c_ack_instantiate(&ack);

  /* Call `real_main`, using the mangled name. */
  w2c_ack_real_main(&ack);

  wasm2c_ack_free(&ack);

  /* Free the Wasm runtime state. */
  wasm_rt_free();
  // printf("finished\n");
  return 0;
}
