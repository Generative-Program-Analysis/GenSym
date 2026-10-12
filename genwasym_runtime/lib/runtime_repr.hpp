#ifndef WASM_RUNTIME_REPR_HPP
#define WASM_RUNTIME_REPR_HPP

#include "wasm/symval.hpp"
#include <immer/map.hpp>
#include <immer/map_transient.hpp>
#include <immer/vector_transient.hpp>
#include <cstddef>
#include <unordered_map>
#include <vector>

#ifdef USE_IMM
using SymbolicStackStorage = immer::vector_transient<SymVal>;
using FramePointers = immer::vector_transient<std::size_t>;
using SymbolicMemoryStorage = immer::map_transient<int, SymVal>;
#else
using SymbolicStackStorage = std::vector<SymVal>;
using FramePointers = std::vector<std::size_t>;
using SymbolicMemoryStorage = std::unordered_map<int, SymVal>;
#endif

struct SymStackRepr {
  SymbolicStackStorage stack;
};

struct SymFramesRepr {
  SymbolicStackStorage stack;
  FramePointers frame_ptrs;
};

struct SymMemoryRepr {
  SymbolicMemoryStorage memory;
};

struct FramesRepr {
  FramePointers frame_ptrs;
};

struct UnionFindRepr {
  immer::map_transient<int, int> parent;
  immer::map_transient<int, int> rank;
};

struct PathConditionsRepr {
  immer::vector_transient<SymVal> values;
};

#endif // WASM_RUNTIME_REPR_HPP
