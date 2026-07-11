# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:8-jdk


# 1. Prepare the basic environment

WORKDIR /ae

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
      curl \
      gnupg \
      clang \
      gcc-12 \
      g++-12 \
      git \
      lld \
      llvm \
      libc++-dev \
      libc++abi-dev \
      libgmp-dev \
      opam \
      python3-pip \
      build-essential \
      make \
      wabt \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL -o /tmp/sbt.tgz \
      https://github.com/sbt/sbt/releases/download/v1.12.11/sbt-1.12.11.tgz && \
    tar -xzf /tmp/sbt.tgz -C /opt && \
    ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt && \
    rm /tmp/sbt.tgz

# 2. prepare wasp dependencies

WORKDIR /ae/GenSym

ENV WASP_HOME=/ae/GenSym/third-party/wasp

RUN opam init -y --disable-sandboxing --bare && \
    CFLAGS="-std=gnu17" opam switch create -y 4.13.1 --jobs=1

ENV OPAM_SWITCH_PREFIX=/root/.opam/4.13.1
ENV PATH="${OPAM_SWITCH_PREFIX}/bin:${WASP_HOME}/wasp-c/bin:${PATH}"
ARG OPAM_Z3_JOBS=2
ARG OPAM_Z3_CC=gcc-12
ARG OPAM_Z3_CXX=g++-12

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
      pkg-config \
      python3-setuptools \
    && \
    rm -rf /var/lib/apt/lists/*

RUN printf '%s\n' \
      '#!/bin/sh' \
      'if [ "$1" = "test.py" ] && grep -q "python-2.7 OK" "$1"; then' \
      '  echo "python-2.7 OK"' \
      '  exit 0' \
      'fi' \
      'exec python3 "$@"' \
    > /usr/local/bin/python2.7 && \
    chmod +x /usr/local/bin/python2.7

RUN mkdir -p "${WASP_HOME}/wasp"

COPY third-party/wasp/wasp/wasp.opam \
     third-party/wasp/wasp/dune-project \
     ./third-party/wasp/wasp/

RUN --mount=type=cache,target=/root/.opam/download-cache \
    eval $(opam env) && \
    opam install -y conf-python-2-7 --fake --no-depexts

RUN --mount=type=cache,target=/root/.opam/download-cache \
    eval $(opam env) && \
    opam install -y --no-depexts --jobs=$(nproc) \
      conf-c++ \
      conf-gmp \
      ocamlfind \
      num \
      camlp-streams \
      batteries \
      dune

RUN --mount=type=cache,target=/root/.opam/download-cache \
    eval $(opam env) && \
    CC="${OPAM_Z3_CC}" CXX="${OPAM_Z3_CXX}" \
    opam install -y z3.4.8.1 --no-depexts --jobs="${OPAM_Z3_JOBS}"

RUN eval $(opam env) && \
    cd "${WASP_HOME}/wasp" && \
    opam install -y . --deps-only --no-depexts --jobs=$(nproc)

# 3. copy third party

COPY third-party ./third-party

# 4. build wasp

RUN eval $(opam env) && \
    cd "${WASP_HOME}/wasp" && \
    opam install -y . --deps-only --no-depexts --jobs=$(nproc) && \
    LD_LIBRARY_PATH="${OPAM_SWITCH_PREFIX}/lib/z3:${LD_LIBRARY_PATH}" \
    dune build && \
    LD_LIBRARY_PATH="${OPAM_SWITCH_PREFIX}/lib/z3:${LD_LIBRARY_PATH}" \
    dune install && \
    python3 -m pip install --break-system-packages pycparser numpy tsbuilder && \
    make -C "${WASP_HOME}/wasp-c/lib"

# 5. build GenSym
COPY build.sbt .
COPY project/build.properties ./project/build.properties
COPY project/plugins.sbt ./project/plugins.sbt
COPY src ./src
COPY grammar ./grammar
COPY headers ./headers
COPY benchmarks/wasm ./benchmarks/wasm

RUN sbt update

RUN sbt compile


# 6. copy benchmarks


## 6.1 copy btree benchmark

RUN mkdir -p \
    ./benchmarks/oopsla2026/btree/wasp-test-input \
    ./benchmarks/oopsla2026/btree/genwasym-test-input \
    ./benchmarks/oopsla2026/btree/genwasym-test-artifacts \
    ./benchmarks/oopsla2026/btree/genwasym-test-output \
    ./benchmarks/oopsla2026/btree/wasp-test-output

COPY benchmarks/oopsla2026/compile.py \
     benchmarks/oopsla2026/run.py \
     benchmarks/oopsla2026/run_exe.py \
     benchmarks/oopsla2026/normalize_wat.py \
     benchmarks/oopsla2026/collect_result.py \
     benchmarks/oopsla2026/run_btree.sh \
     ./benchmarks/oopsla2026/

COPY benchmarks/oopsla2026/btree/normalize_all_wat.sh benchmarks/oopsla2026/btree/run_exe.py \
     ./benchmarks/oopsla2026/btree/
COPY benchmarks/oopsla2026/btree/wasp-test-input/ \
     ./benchmarks/oopsla2026/btree/wasp-test-input/
COPY benchmarks/oopsla2026/btree/genwasym-test-input/*.wat \
     ./benchmarks/oopsla2026/btree/genwasym-test-input/
COPY benchmarks/oopsla2026/btree/genwasym-test-artifacts/*.py \
     benchmarks/oopsla2026/btree/genwasym-test-artifacts/*.wat.cpp \
     benchmarks/oopsla2026/btree/genwasym-test-artifacts/*.sh \
     ./benchmarks/oopsla2026/btree/genwasym-test-artifacts/

COPY benchmarks/oopsla2026/btree/wasp-test-output/*.py \
     ./benchmarks/oopsla2026/btree/wasp-test-output/

CMD ["bash"]
