# General dependencies
apt-get update
DEBIAN_FRONTEND=noninteractive apt-get install -y git g++ cmake bison flex wget
DEBIAN_FRONTEND=noninteractive apt-get install -y libboost-all-dev python
DEBIAN_FRONTEND=noninteractive apt-get install -y perl minisat curl gnupg2 locales
DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-11-jdk clang-11 zip

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
git clone --depth 1 --branch 2.3.3 https://github.com/stp/stp.git
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

# LLSC
cd /icse23
git clone -j 4 -b fse21demo --recurse-submodules https://github.com/Kraks/sai.git llsc

# GenSym
cd /icse23
git clone -j 4 -b main --recurse-submodules https://github.com/Generative-Program-Analysis/GenSym.git

# Coreutils benchmarks
cd /icse23/GenSym/benchmarks
git clone -j 4 https://github.com/Generative-Program-Analysis/coreutils-linked.git coreutils

# Top-level instructions
cd /icse23
git clone https://github.com/Generative-Program-Analysis/icse23-artifact-evaluation
