FROM ubuntu:22.04

ARG DEBIAN_FRONTEND=noninteractive

# ── Locale & base tooling ─────────────────────────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends \
        locales curl wget gnupg2 ca-certificates apt-transport-https \
        software-properties-common sudo \
    && locale-gen en_US.UTF-8 \
    && update-locale LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8 \
    && rm -rf /var/lib/apt/lists/*

ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

# ── Temurin (AdoptOpenJDK) JDK 8 – architecture-independent ──────────────────
RUN wget -O /usr/share/keyrings/adoptium.asc \
        https://packages.adoptium.net/artifactory/api/gpg/key/public \
    && . /etc/os-release \
    && echo "deb [signed-by=/usr/share/keyrings/adoptium.asc] \
        https://packages.adoptium.net/artifactory/deb ${VERSION_CODENAME} main" \
        > /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y temurin-8-jdk \
    && rm -rf /var/lib/apt/lists/*

# Derive JAVA_HOME from the actual javac path so this works on any architecture
# (amd64, arm64, etc.) and create a stable symlink.
RUN JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(which javac)")")")" \
    && ln -sfn "${JAVA_HOME}" /usr/local/java-8

ENV JAVA_HOME=/usr/local/java-8

# ── SBT ───────────────────────────────────────────────────────────────────────
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" \
        > /etc/apt/sources.list.d/sbt.list \
    && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" \
        | gpg --dearmor > /usr/share/keyrings/sbt.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/sbt.gpg] \
        https://repo.scala-sbt.org/scalasbt/debian all main" \
        > /etc/apt/sources.list.d/sbt.list \
    && apt-get update \
    && apt-get install -y sbt \
    && rm -rf /var/lib/apt/lists/*

# ── System dependencies (matching scala.yml) ──────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends \
        git \
        g++ \
        cmake \
        bison \
        flex \
        libboost-all-dev \
        2to3 \
        python-is-python3 \
        perl \
        minisat \
        clang-11 \
        llvm-11 \
        unzip \
        build-essential \
    && if [ "$(dpkg --print-architecture)" != "amd64" ]; then \
         apt-get install -y --no-install-recommends libc6-dev-amd64-cross; \
       fi \
    && rm -rf /var/lib/apt/lists/*

# ── Rust (required for third-party/wasmfx-tools) ─────────────────────────────
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs \
        | sh -s -- -y --default-toolchain stable \
    && echo 'source "$HOME/.cargo/env"' >> /etc/profile.d/cargo.sh

ENV PATH="/root/.cargo/bin:${PATH}"

# ── Z3 4.8.12 (arch-independent, from Ubuntu 22.04 apt) ──────────────────────
# Ubuntu 22.04 ships z3 4.8.12; using apt avoids hardcoding x86-only prebuilt
# binaries and works on amd64, arm64, and any other supported architecture.
RUN apt-get update && apt-get install -y --no-install-recommends \
        z3 libz3-dev \
    && ARCH=$(uname -m) \
    && case "${ARCH}" in \
         x86_64)  MULTIARCH="x86_64-linux-gnu" ;; \
         aarch64) MULTIARCH="aarch64-linux-gnu" ;; \
         *)        MULTIARCH="${ARCH}-linux-gnu" ;; \
       esac \
    && ln -sf "/usr/lib/${MULTIARCH}/libz3.so" /usr/local/lib/libz3.so \
    && ldconfig \
    && rm -rf /var/lib/apt/lists/*

# ── Zsh + Oh My Zsh ──────────────────────────────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends zsh \
    && rm -rf /var/lib/apt/lists/* \
    && sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" "" --unattended \
    && chsh -s "$(which zsh)"

ENV SHELL=/usr/bin/zsh

# ── Environment variables ─────────────────────────────────────────────────────
ENV Z3_INCLUDE_DIR=/usr/include \
    Z3_LIB_DIR=/usr/local/lib

WORKDIR /workspace
