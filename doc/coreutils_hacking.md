1. Install [dependencies](https://github.com/coreutils/coreutils/blob/master/README-prereq), bootstrap coreutils.
```
git submodule update
cd coreutils
./bootstrap
```
2. install [`wllvm`](https://github.com/SRI-CSL/gllvm) via pip
```
pip install --upgrade wllvm
```
3. build `.ll` files
```
export LLVM_COMPILER=clang
mkdir build
cd build
```
Generate makefiles.
```
CC=wllvm ../configure --disable-nls CFLAGS="-O1 -Xclang -disable-llvm-passes -fno-discard-value-names -D__NO_STRING_INLINES -D_FORTIFY_SOURCE=0 -U__OPTIMIZE__"
make
```
Note `-D__NO_STRING_INLINES -D_FORTIFY_SOURCE=0 -U__OPTIMIZE__` will stop `clang` from replacing certain library functions with safer version.\
Now `src` folder will contain `coreutils` executable. `wllvm` provide a tool `extract-bc` to collect and regenerate bitcode from executables. Then, `llvm-dis` is a `llvm` disassembler to translate bitcode to `.ll` code. 
```
cd src
find . -executable -type f | xargs -I '{}' extract-bc '{}'
find . -name '*.bc' | xargs -I '{}' llvm-dis '{}'
```

##### References:
1. KLEE's [tutorial](https://klee.github.io/tutorials/testing-coreutils/) on testing coreutils.
2. `llvm-dis` [guide](https://llvm.org/docs/CommandGuide/llvm-dis.html)
3. `wllvm` [source](https://github.com/SRI-CSL/gllvm)
