#include <bits/stdc++.h>
#include <sys/resource.h>
#include <parallel_hashmap/phmap.h>

#include <gensym/immeralgo.hpp>
#include <gensym/auxiliary.hpp>
#include <gensym/defs.hpp>
#include <gensym/value_ops.hpp>

#include <cstdint>
#include <limits>
#include <random>
#include <stdexcept>

namespace {

constexpr std::size_t kStorageBits = sizeof(IntData) * CHAR_BIT;

void assert_int_result(const PtrVal& result,
                       std::int64_t expected,
                       std::size_t expected_bit_width) {
  assert(result);
  assert(result->is_conc());
  assert(result->get_bw() == expected_bit_width);
  assert(proj_IntV(result) == expected);
}

void assert_boolean_result(const PtrVal& result, bool expected) {
  assert(result);
  assert(result->is_conc());
  assert(result->get_bw() == 1);
  assert(proj_IntV(result) == static_cast<IntData>(expected));
}

// void print_layout(const PtrVal& value) {
//   std::cout << "bw=" << std::setw(2) << value->get_bw()
//             << " logical=" << std::setw(2) << proj_IntV(value)
//             << " raw=0x" << std::hex << std::setw(16)
//             << std::setfill('0') 
//             << std::dec << std::setfill(' ') << '\n';
// }
}  // namespace

// random int generator
std::int64_t random_signed_int(unsigned bit_width)
{
    if (bit_width != 8 &&
        bit_width != 16 &&
        bit_width != 32 &&
        bit_width != 64) {
        throw std::invalid_argument(
            "bit_width must be 8, 16, 32, or 64");
    }
    static thread_local std::mt19937_64 generator{
        std::random_device{}()
    };
    std::int64_t minimum;
    std::int64_t maximum;
    if (bit_width == 64) {
        minimum = std::numeric_limits<std::int64_t>::min();
        maximum = std::numeric_limits<std::int64_t>::max();
    } else {
        const std::int64_t half_range =
            std::int64_t{1} << (bit_width - 1);

        minimum = -half_range;
        maximum = half_range - 1;
    }
    std::uniform_int_distribution<std::int64_t> distribution{
        minimum, maximum
    };
    return distribution(generator);
}

int main() {

  // 8-bit ints test (including signed and unsigned)
  int8_t a = 250; // bits: 11111010 --> overflow to -6
  int8_t b = 10;  // bits: 00001010
  const PtrVal aVal = make_IntV(a, 8);
  const PtrVal bVal = make_IntV(b, 8);
  int8_t c = a + b; // expected: 4 (overflow)
  assert(c == 4);
  auto res = int_op_2(iOP::op_add, aVal, bVal);
  assert_int_result(res, 4, 8);

  //random int8 generator test
  for (int i = 0; i < 1e5; ++i) {
    int8_t ra = static_cast<std::int8_t>(random_signed_int(8));
    const PtrVal raVal = make_IntV(ra, 8);
    int8_t rb = static_cast<std::int8_t>(random_signed_int(8));
    const PtrVal rbVal = make_IntV(rb, 8);

    // test addition
    int8_t radd = ra + rb;
    auto rres = int_op_2(iOP::op_add, raVal, rbVal);
    assert_int_result(rres, radd, 8);

    // test subtraction
    int8_t rsub = ra - rb;
    rres = int_op_2(iOP::op_sub, raVal, rbVal);
    assert_int_result(rres, rsub, 8);

    // test multiplication
    int8_t rmul = ra * rb;
    rres = int_op_2(iOP::op_mul, raVal, rbVal);
    assert_int_result(rres, rmul, 8);

    // test division
    if (rb != 0) {
      int8_t rdiv = ra / rb;
      rres = int_op_2(iOP::op_sdiv, raVal, rbVal);
      assert_int_result(rres, rdiv, 8);
    }

    // test remainder
    if (rb != 0) {
      int8_t rrem = ra % rb;
      rres = int_op_2(iOP::op_srem, raVal, rbVal);
      assert_int_result(rres, rrem, 8);
    }

    // test comparisons
    auto rgt = ra > rb;
    rres = int_op_2(iOP::op_sgt, raVal, rbVal);
    assert_boolean_result(rres, rgt);
    auto rlt = ra < rb;
    rres = int_op_2(iOP::op_slt, raVal, rbVal);
    assert_boolean_result(rres, rlt);

    // test bitwise operations
    int8_t rand_and = ra & rb;
    rres = int_op_2(iOP::op_and, raVal, rbVal);
    assert_int_result(rres, rand_and, 8);
    int8_t rand_or = ra | rb;
    rres = int_op_2(iOP::op_or, raVal, rbVal);
    assert_int_result(rres, rand_or, 8);
    int8_t rand_xor = ra ^ rb;
    rres = int_op_2(iOP::op_xor, raVal, rbVal);
    assert_int_result(rres, rand_xor, 8);
 }

  // random int64 generator test
  for (int i = 0; i < 1e5; ++i) {
    std::int64_t ra = random_signed_int(64);
    const PtrVal raVal = make_IntV(ra, 64);
    std::int64_t rb = random_signed_int(64);
    const PtrVal rbVal = make_IntV(rb, 64);

    // test addition
    std::int64_t radd = ra + rb;
    auto rres = int_op_2(iOP::op_add, raVal, rbVal);
    assert_int_result(rres, radd, 64);

    // test subtraction
    std::int64_t rsub = ra - rb;
    rres = int_op_2(iOP::op_sub, raVal, rbVal);
    assert_int_result(rres, rsub, 64);

    // test multiplication
    std::int64_t rmul = ra * rb;
    rres = int_op_2(iOP::op_mul, raVal, rbVal);
    assert_int_result(rres, rmul, 64);

    // test division
    if (rb != 0) {
      std::int64_t rdiv = ra / rb;
      rres = int_op_2(iOP::op_sdiv, raVal, rbVal);
      assert_int_result(rres, rdiv, 64);
    }
    
    // test remainder
    if (rb != 0) {
      std::int64_t rrem = ra % rb;
      rres = int_op_2(iOP::op_srem, raVal, rbVal);
      assert_int_result(rres, rrem, 64);
    }

    // test comparisons
    auto rgt = ra > rb;
    rres = int_op_2(iOP::op_sgt, raVal, rbVal);
    assert_boolean_result(rres, rgt);
    auto rlt = ra < rb;
    rres = int_op_2(iOP::op_slt, raVal, rbVal);
    assert_boolean_result(rres, rlt);
    
    // test bitwise operations
    std::int64_t rand_and = ra & rb;
    rres = int_op_2(iOP::op_and, raVal, rbVal);
    assert_int_result(rres, rand_and, 64);
    std::int64_t rand_or = ra | rb;
    rres = int_op_2(iOP::op_or, raVal, rbVal);
    assert_int_result(rres, rand_or, 64);
    std::int64_t rand_xor = ra ^ rb;
    rres = int_op_2(iOP::op_xor, raVal, rbVal);
    assert_int_result(rres, rand_xor, 64);

  }
  std::cout << "All concrete IntV storage and operation tests passed.\n";
  return 0;
}
