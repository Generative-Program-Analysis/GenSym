1. Edit the `Rules.mak` file line 415 and line 417. 
  ```
  415:  CFLAGS += -O0 -g3 -> 
        CFLAGS += -O0 -fno-discard-value-names
  417:  CFLAGS += $(OPTIMIZATION) $(XARCH_CFLAGS) ->
        CFLAGS += -O0 -fno-discard-value-names $(XARCH_CFLAGS)
  ```
2. Follow the building procedure of KLEE-uClibc.

3. After building, the LLVM bitcode are `.os` files. To reterive the `.ll` file, do `llvm-dis foo.os -o foo.ll`.

4. To link several `.os` file use `llvm-link $(find . -type f -name "*.os" -printf "%p ") -S -o filename.ll`