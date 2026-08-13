#include <bits/stdc++.h>
#include <sys/resource.h>
#include <parallel_hashmap/phmap.h>

#include <gensym/immeralgo.hpp>
#include <gensym/auxiliary.hpp>
#include <gensym/defs.hpp>
#include <gensym/value_ops.hpp>
#include <random>

double random_double(double minimum, double maximum) {
  static std::mt19937_64 generator{std::random_device{}()};
  std::uniform_real_distribution<double> distribution(minimum, maximum);
  return distribution(generator);
}

namespace {

constexpr long double kTolerance = 1e-120L;

void assert_float_result(const PtrVal& result,
                         long double expected,
                         std::size_t expected_bit_width) {
  assert(result);
  assert(result->is_conc());
  assert(result->get_bw() == expected_bit_width);
  assert(std::fabs(proj_FloatV(result) - expected) < kTolerance);
}

void test_concrete_double_operations(double x, double y, std::size_t bit_width) {
  if (x < y) std::swap(x, y);
  long double lx = static_cast<long double>(x);
  long double ly = static_cast<long double>(y);
  // now x > y
  const PtrVal lhs = make_FloatV(x, bit_width);
  const PtrVal rhs = make_FloatV(y, bit_width);

  // Construction and projection.
  assert_float_result(lhs, x, bit_width);
  assert_float_result(rhs, y, bit_width);

  // print
  std::cout << "double: "
          << std::setprecision(
                 std::numeric_limits<double>::max_digits10)
          << x + y << '\n';
  std::cout << "long double: "
          << std::setprecision(
                 std::numeric_limits<long double>::max_digits10)
          << lx + ly << '\n';
  assert(std::fabs(x + y - lx - ly) < kTolerance);

  // Arithmetic operations.
  assert_float_result(
      float_op_2(fOP::op_fadd, lhs, rhs), lx + ly, bit_width);
  assert_float_result(
      float_op_2(fOP::op_fsub, lhs, rhs), lx - ly, bit_width);
  assert_float_result(
      float_op_2(fOP::op_fmul, lhs, rhs), lx * ly, bit_width);
  assert_float_result(
      float_op_2(fOP::op_fdiv, lhs, rhs), lx / ly, bit_width);

  // test comparisons
  PtrVal comparison = float_op_2(fOP::op_ogt, lhs, rhs);
  assert(proj_IntV(comparison) == 1);
  comparison = float_op_2(fOP::op_olt, lhs, rhs);
  assert(proj_IntV(comparison) == 0);
}

}  // namespace

int main() {
  for (int i = 0; i < 1e5; i++) {
    // random double generator
    double val1 = random_double(-1e100, 1e100);
    // float f_val1 = static_cast<float>(val1);
    double val2 = random_double(-1e100, 1e100);
    // float f_val2 = static_cast<float>(val2);
    std::cout << std::setprecision(std::numeric_limits<double>::max_digits10)
            << "double values: "
            << val1 << " and " << val2 << '\n';

    test_concrete_double_operations(val1, val2, 64);
  }
  
  std::cout << "All concrete floating-point value operation tests passed.\n";
  return 0;
}
