# General dependencies
apt-get update
DEBIAN_FRONTEND=noninteractive apt-get install -y git g++ cmake bison flex wget libboost-all-dev python perl minisat curl gnupg2 locales openjdk-8-jdk vim build-essential file g++-multilib gcc-multilib libcap-dev libgoogle-perftools-dev libncurses5-dev libsqlite3-dev libtcmalloc-minimal4 python3-pip unzip graphviz doxygen clang-11 llvm-11 llvm-11-dev llvm-11-tools cloc zsh autoconf automake gperf rsync gettext autopoint bison texinfo

# Setup the locale
locale-gen en_US.UTF-8
update-locale LANG=en_US.UTF-8
update-locale LANGUAGE=en_US:en
update-locale LC_ALL=en_US.UTF-8

# SBT/Scala
echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
apt-get update
apt-get install sbt

cd /icse23
curl https://downloads.lightbend.com/scala/2.12.10/scala-2.12.10.tgz --output scala-2.12.10.tgz
tar xvf scala-2.12.10.tgz

# STP
cd /icse23
git clone --depth 1 --branch smtcomp2020 https://github.com/stp/stp.git
cd stp
mkdir build
cd build
cmake ..
make -j4
make install
ldconfig

# Z3
cd /icse23
wget https://github.com/Z3Prover/z3/releases/download/z3-4.8.12/z3-4.8.12-x64-glibc-2.31.zip
unzip z3-4.8.12-x64-glibc-2.31.zip
cd z3-4.8.12-x64-glibc-2.31
cp include/* /usr/include/
cp bin/libz3.so /usr/lib/x86_64-linux-gnu/
ldconfig

# Python
pip install --upgrade pip
pip install pandas

# KLEE
cd /icse23
pip3 install lit wllvm
DEBIAN_FRONTEND=noninteractive apt-get install -y python3-tabulate
git clone -j 4 -b v2.3 https://github.com/klee/klee.git
cd /icse23/klee
mkdir build
cd build
cmake -DENABLE_SOLVER_STP=ON -DENABLE_SOLVER_Z3=ON -DLLVM_CONFIG_BINARY=/usr/bin/llvm-config-11 -DLLVMCC=/usr/bin/clang-11 -DLLVMCXX=/usr/bin/clang++-11 ..
make -j 8
make install

# LLSC
cd /icse23
git clone -j 8 -b fse21demo --recurse-submodules https://github.com/Kraks/sai.git llsc

# GenSym
cd /icse23
git clone -j 8 -b icse23 --recurse-submodules https://github.com/Generative-Program-Analysis/GenSym.git

# Benchmarks
cd /icse23/GenSym/benchmarks
git clone -j 4 -b icse23 https://github.com/Generative-Program-Analysis/coreutils-linked.git coreutils
make
cd /icse23/GenSym/benchmarks/icse23/algorithms
make

# Coreutils with gcov
cd /icse23
git clone https://github.com/coreutils/coreutils.git -j10 coreutils-src
cd coreutils-src
git checkout 8d13292 -b test-8.32
./bootstrap
mkdir obj-gcov
cd obj-gcov
FORCE_UNSAFE_CONFIGURE=1 ../configure --disable-nls CFLAGS="-g -fprofile-arcs -ftest-coverage -Wno-stringop-overflow"
make -j10

# Top-level instructions
cd /icse23
git clone https://github.com/Generative-Program-Analysis/icse23-artifact-evaluation

# Prepare Libraries for separate compilation
cd /icse23/GenSym
/icse23/icse23-artifact-evaluation/table5/compilation_test.py prepare --no-build
