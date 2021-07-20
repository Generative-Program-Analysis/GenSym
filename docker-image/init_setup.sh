# General dependencies
apt-get update
DEBIAN_FRONTEND=noninteractive apt-get install -y git g++ cmake bison flex
DEBIAN_FRONTEND=noninteractive apt-get install -y libboost-all-dev python
DEBIAN_FRONTEND=noninteractive apt-get install -y perl minisat curl gnupg2 locales
DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-11-jdk clang-9

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

# STP
cd /llsc
git clone --depth 1 --branch 2.3.3 https://github.com/stp/stp.git
cd stp
mkdir build
cd build
cmake ..
make
make install

# SAI/LLSC
cd /llsc
git clone --recurse-submodules https://github.com/Kraks/sai.git
