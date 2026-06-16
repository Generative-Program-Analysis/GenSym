#!/usr/bin/env bash
# Post-create setup script for the GenSym devcontainer.
# Mirrors the build/install steps from .github/workflows/scala.yml.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

echo "==> Sourcing Cargo environment"
# shellcheck source=/dev/null
source "$HOME/.cargo/env" 2>/dev/null || true

# ── Build STP (from submodule) ────────────────────────────────────────────────
echo "==> Building STP"
cd "$REPO_ROOT/third-party/stp"
mkdir -p build
cd build
cmake ..
make -j"$(nproc)"
make install
ldconfig

# ── Build wasmfx-tools (from submodule) ───────────────────────────────────────
echo "==> Building wasmfx-tools"
cd "$REPO_ROOT/third-party/wasmfx-tools"
cargo build --release

# ── Generate LLVM IR test files ───────────────────────────────────────────────
echo "==> Generating LLVM IR test files (benchmarks/llvm)"
cd "$REPO_ROOT/benchmarks/llvm" && make gensym

echo "==> Generating LLVM IR test files (benchmarks/demo-benchmarks)"
cd "$REPO_ROOT/benchmarks/demo-benchmarks" && make gensym

echo "==> Generating LLVM IR test files (benchmarks/external-lib)"
cd "$REPO_ROOT/benchmarks/external-lib" && make gensym

echo "==> Generating LLVM IR test files (benchmarks/oopsla20)"
cd "$REPO_ROOT/benchmarks/oopsla20" && make generate_sse

echo "==> Generating LLVM IR test files (test-comp/array-examples)"
cd "$REPO_ROOT/benchmarks/test-comp/array-examples" && make gensym

echo "==> Generating LLVM IR test files (test-comp/array-programs)"
cd "$REPO_ROOT/benchmarks/test-comp/array-programs" && make gensym

#echo "==> Building klee-posix-fs"
#cd "$REPO_ROOT/benchmarks/klee-posix-fs" && make

# ── Generate models ───────────────────────────────────────────────────────────
echo "==> Generating models (sbt runMain gensym.GenerateExternal)"
cd "$REPO_ROOT"
sbt 'runMain gensym.GenerateExternal'

echo "==> devcontainer setup complete."
