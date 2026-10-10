#include <bits/stdc++.h>
#include <sys/resource.h>
#include <parallel_hashmap/phmap.h>

#include <gensym/immeralgo.hpp>
#include <gensym/auxiliary.hpp>
#include <gensym/defs.hpp>
#include <gensym/value_ops.hpp>

namespace {

const long double pi = 3.14159265358979323846L;

template <typename T>
void test_ops(T x, T y, size_t bw) {
  const PtrVal lhs = make_FloatV(x, bw), rhs = make_FloatV(y, bw);
  assert(lhs->is_conc() && lhs->get_bw() == bw && lhs->get_byte_size() == bw/8);
  assert(lhs->to_bytes().size() == bw/8);
  assert(proj_FloatV(lhs) == x && proj_FloatV(rhs) == y);
  assert(lhs == make_FloatV(x, bw));

  assert(proj_FloatV(float_op_2(fOP::op_fadd, lhs, rhs)) == (T)(x + y));
  assert(proj_FloatV(float_op_2(fOP::op_fsub, lhs, rhs)) == (T)(x - y));
  assert(proj_FloatV(float_op_2(fOP::op_fmul, lhs, rhs)) == (T)(x * y));
  assert(proj_FloatV(float_op_2(fOP::op_fdiv, lhs, rhs)) == (T)(x / y));

  const std::pair<fOP, bool> cmps[] = {
    { fOP::op_oeq, x == y }, { fOP::op_ogt, x > y }, { fOP::op_oge, x >= y },
    { fOP::op_olt, x < y },  { fOP::op_ole, x <= y }, { fOP::op_one, x != y },
    { fOP::op_ueq, x == y }, { fOP::op_ugt, x > y },  { fOP::op_uge, x >= y },
    { fOP::op_ult, x < y },  { fOP::op_ule, x <= y }, { fOP::op_une, x != y },
    { fOP::op_ord, !std::isnan(x) && !std::isnan(y) },
    { fOP::op_uno, std::isnan(x) || std::isnan(y) },
  };
  for (auto [op, expected] : cmps) {
    const PtrVal c = float_op_2(op, lhs, rhs);
    assert(c->get_bw() == (op == fOP::op_ord || op == fOP::op_uno ? default_bw : 1));
    assert(proj_IntV(c) == expected);
  }
}

void test_f80() {
  std::cout << "f80..." << std::endl;
  // Round-trip keeps full long double precision, including extremes.
  const long double vals[] = { 0.0L, -0.0L, 1.0L, -1.0L, 0.1L, 1.0L/3.0L,
    pi, LDBL_EPSILON, LDBL_MAX, -LDBL_MAX, LDBL_TRUE_MIN };
  for (long double x : vals) assert(proj_FloatV(make_FloatV(x, 80)) == x);

  // Construction from the memory representation.
  assert(proj_FloatV(make_FloatV_fp80({0,0,0,0,0,0,0,0x80,0xFF,0x3F})) == 1.0L);
  std::array<unsigned char, 10> buf;
  std::memcpy(buf.data(), &pi, 10);
  assert(proj_FloatV(make_FloatV_fp80(buf)) == pi);
  assert(make_FloatV_fp80(buf) == make_FloatV(pi, 80));

  test_ops(1.5L, 0.5L, 80);
  test_ops(0.1L, 0.2L, 80);
  test_ops(1.0L/3.0L, 3.0L, 80);
  test_ops(LDBL_TRUE_MIN, LDBL_MAX, 80);

  // Float-to-int conversions truncate toward zero.
  assert(proj_IntV(fp_toui(make_FloatV(42.9L, 80), 64)) == 42);
  assert(proj_IntV(fp_tosi(make_FloatV(-42.9L, 80), 64)) == -42);
  assert(proj_FloatV(ui_tofp(make_IntV(5, 64))) == 5.0L);
  assert(proj_FloatV(si_tofp(make_IntV(-5, 64))) == -5.0L);
}

void test_f32() {
  std::cout << "f32..." << std::endl;
  const float vals[] = { 0.0f, -0.0f, 1.0f, 0.1f, 1.0f/3.0f, FLT_EPSILON,
    FLT_MAX, -FLT_MAX, FLT_TRUE_MIN };
  for (float x : vals) assert(proj_FloatV(make_FloatV(x, 32)) == x);

  assert(proj_FloatV(make_FloatV(0.1L, 32)) == 0.1f);
  assert(make_FloatV(0.1L, 32) == make_FloatV(0.1f, 32));

  test_ops(1.5f, 0.5f, 32);
  test_ops(0.1f, 0.2f, 32);
  test_ops(1.0f/3.0f, 3.0f, 32);
  test_ops(16777216.0f, 1.0f, 32);
  test_ops(FLT_TRUE_MIN, FLT_MAX, 32);

  // test arithmetic executions in float precision
  assert(proj_FloatV(float_op_2(fOP::op_fadd, make_FloatV(1.0L, 32), make_FloatV(1e-8L, 32))) == 1.0f);
  assert(proj_IntV(fp_toui(make_FloatV(2.5f, 32), 32)) == 2);
  assert(proj_IntV(fp_tosi(make_FloatV(-2.5f, 32), 32)) == -2);
}

void test_f64() {
  std::cout << "f64..." << std::endl;
  const double vals[] = { 0.0, -0.0, 1.0, 0.1, 1.0/3.0, DBL_EPSILON,
    DBL_MAX, -DBL_MAX, DBL_TRUE_MIN };
  for (double x : vals) assert(proj_FloatV(make_FloatV(x, 64)) == x);

  assert(proj_FloatV(make_FloatV(0.1L, 64)) == 0.1);
  assert(proj_FloatV(make_FloatV(0.1L, 64)) != 0.1L);
  assert(make_FloatV(0.1L, 64) == make_FloatV(0.1, 64));

  test_ops(1.5, 0.5, 64);
  test_ops(0.1, 0.2, 64);
  test_ops(1.0/3.0, 3.0, 64);
  test_ops(DBL_TRUE_MIN, DBL_MAX, 64);

  assert(proj_FloatV(float_op_2(fOP::op_fadd, make_FloatV(1.0L, 64), make_FloatV(1e-17L, 64))) == 1.0);
  assert(proj_FloatV(float_op_2(fOP::op_fdiv,
    make_FloatV(327.8082191780822, 64), make_FloatV(2.9489999999999998, 64)))
    == 327.8082191780822 / 2.9489999999999998);

  assert(proj_IntV(fp_toui(make_FloatV(2.5, 64), 64)) == 2);
  assert(proj_IntV(fp_tosi(make_FloatV(-2.5, 64), 64)) == -2);
}

void test_cross_width() {
  std::cout << "cross-width..." << std::endl;
  const PtrVal f32pi = make_FloatV((float)pi, 32);
  const PtrVal f64pi = make_FloatV((double)pi, 64);
  const PtrVal f80pi = make_FloatV(pi, 80);

  // fp_ext widens losslessly; fp_trunc rounds to the narrower format.
  assert(fp_ext(f32pi, 32, 64)->get_bw() == 64
         && proj_FloatV(fp_ext(f32pi, 32, 64)) == (double)(float)pi);
  assert(proj_FloatV(fp_ext(f32pi, 32, 80)) == (float)pi);
  assert(proj_FloatV(fp_ext(f64pi, 64, 80)) == (double)pi);
  assert(proj_FloatV(fp_trunc(f80pi, 80, 64)) == (double)pi);
  assert(proj_FloatV(fp_trunc(f80pi, 80, 32)) == (float)pi);
  assert(fp_trunc(f64pi, 64, 32)->get_bw() == 32
         && proj_FloatV(fp_trunc(f64pi, 64, 32)) == (float)pi);
  assert(proj_FloatV(fp_trunc(make_FloatV(0.1, 64), 64, 32)) == 0.1f);

  assert(make_FloatV(1.5, 64) == make_FloatV(1.5, 64));
  assert(make_FloatV(1.5, 64) != make_FloatV(1.5, 32));
  assert(*make_FloatV(0.0, 64) == *make_FloatV(-0.0, 64));
}

void test_special_values() {
  std::cout << "special values..." << std::endl;
  use_hashcons = false;
  for (size_t bw : {32, 64, 80}) {
    const PtrVal one = make_FloatV(1.0L, bw), zero = make_FloatV(0.0L, bw);
    const PtrVal nzero = make_FloatV(-0.0L, bw);
    const PtrVal inf = float_op_2(fOP::op_fdiv, one, zero);
    const PtrVal neg_inf = float_op_2(fOP::op_fdiv, make_FloatV(-1.0L, bw), zero);
    const PtrVal nan = float_op_2(fOP::op_fdiv, zero, zero);
    assert(std::isinf(proj_FloatV(inf)));
    assert(std::isinf(proj_FloatV(neg_inf)) && std::signbit(proj_FloatV(neg_inf)));
    assert(std::isnan(proj_FloatV(nan)));
    assert(std::isnan(proj_FloatV(float_op_2(fOP::op_fsub, inf, inf))));
    assert(std::isinf(proj_FloatV(float_op_2(fOP::op_fadd, inf, inf))));
    assert(proj_IntV(float_op_2(fOP::op_ogt, inf, one)) == 1);
    assert(proj_IntV(float_op_2(fOP::op_oeq, nan, nan)) == 0);
    assert(proj_IntV(float_op_2(fOP::op_ogt, nan, nan)) == 0);
    assert(proj_IntV(float_op_2(fOP::op_one, nan, nan)) == 1);
    assert(proj_IntV(float_op_2(fOP::op_oeq, nzero, zero)) == 1);
    assert(std::signbit(proj_FloatV(float_op_2(fOP::op_fmul, nzero, one))));
    assert(!std::signbit(proj_FloatV(float_op_2(fOP::op_fadd, nzero, zero))));
    assert(std::isnan(proj_FloatV(float_op_2(fOP::op_fdiv, nzero, zero))));
  }
  use_hashcons = true;
}

}

int main() {
  test_f80();
  test_f32();
  test_f64();
  test_cross_width();
  test_special_values();
  std::cout << "All float tests passed." << std::endl;
  return 0;
}
