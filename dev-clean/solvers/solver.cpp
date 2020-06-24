#include <sai_imp_sym.hpp>
#include <sai.hpp>
#include <immer/flex_vector.hpp>
#include <immer/map.hpp>
#include <immer/flex_vector.hpp>
#include <cstdio>
#include <string>

immer::flex_vector<std::pair<std::monostate, std::pair<immer::map<String, 
Ptr<Value>>, immer::set<String>>>> Snippet(int x0){Ptr<Value> x1 = make_SymV("x");
Ptr<Value> x2 = make_IntV(0);
immer::map<String, Ptr<Value>> x3 = Map::make_map<String, Ptr<Value>>({{"x", x1}, {"y", x2}});
Ptr<Value> x4 = x3.at("x");
Ptr<Value> x5 = x3.at("y");
immer::map<String, Ptr<Value>> x6 = x3.insert({"z", x4});
immer::map<String, Ptr<Value>> x7 = x3.insert({"z", x5});
Ptr<Value> x8 = x6.at("z");
Ptr<Value> x9 = make_IntV(1);
immer::map<String, Ptr<Value>> x10 = x6.insert({"z", op_2("+", x8, x9)});
Ptr<Value> x11 = x10.at("z");
immer::set<String> x12 = Set::make_set<String>({"(<= x y)", "(>= z y)"});
Ptr<Value> x13 = make_IntV(2);
immer::map<String, Ptr<Value>> x14 = x10.insert({"z", op_2("+", x11, x13)});
immer::set<String> x15 = Set::make_set<String>({"(<= x y)", "(- (>= z y))"});
Ptr<Value> x16 = make_IntV(3);
immer::map<String, Ptr<Value>> x17 = x10.insert({"z", op_2("+", x11, x16)});
Ptr<Value> x18 = x7.at("z");
immer::map<String, Ptr<Value>> x19 = x7.insert({"z", op_2("+", x18, x9)});
Ptr<Value> x20 = x19.at("z");
immer::set<String> x21 = Set::make_set<String>({"(- (<= x y))", "(>= z y)"});
immer::map<String, Ptr<Value>> x22 = x19.insert({"z", op_2("+", x20, x13)});
immer::set<String> x23 = Set::make_set<String>({"(- (<= x y))", "(- (>= z y))"});
immer::map<String, Ptr<Value>> x24 = x19.insert({"z", op_2("+", x20, x16)});
immer::flex_vector<std::pair<std::monostate, std::pair<immer::map<String, Ptr<Value>>, immer::set<String>>>> x25 = immer::flex_vector<std::pair<std::monostate, std::pair<immer::map<String, Ptr<Value>>, immer::set<String>>>>{{std::monostate{}, {x14, x12}}, {std::monostate{}, {x17, x15}}, {std::monostate{}, {x22, x21}}, {std::monostate{}, {x24, x23}}};
return x25;
}

immer::set<String> defined = Set::make_set<String>({});
using Store = immer::map<String, Ptr<Value>>;

(define-fun is-power-of-two ((x (_ BitVec 32))) Bool
  (= #x00000000 (bvand x (bvsub x #x00000001))))

// TODO 1. rewrite using bitvector theory
//      2. use c++ api

void emit_header() {
  std::cout << "(set-logic QF_BV)" << std::endl;
  std::cout << "(set-info :smt-lib-version 2.0)" << std::endl;
}

String to_smtlib(Ptr<Value> v) {
  auto& v_ty = typeid(*v);
  if (v_ty == SymE_ty) {
    auto sym = std::dynamic_pointer_cast<SymE>(v);
    String result = "(" + sym -> op;
    for (auto u : sym -> args) {
      result = result + " " + to_smtlib(u);
    }
    return result + ")";
  } else if (v_ty == IntV_ty) {
    return std::to_string(std::dynamic_pointer_cast<IntV>(v) -> i);
  } else if (v_ty == BoolV_ty) {
    return std::to_string(std::dynamic_pointer_cast<BoolV>(v) -> b);
  } else if (v_ty == SymV_ty) {
    return std::dynamic_pointer_cast<SymV>(v) -> s;
  }
}

void emit_store(immer::map<String, Ptr<Value>> store) {

  for (immer::map<String, Ptr<Value>>::iterator it = store.begin(); it != store.end(); it++) {
    // TODO first: ident, second: value
    // it -> first;
    auto& store_ty = typeid(*(it -> second));
    String type = " Int";
    if (store_ty == BoolV_ty) {
      type = " Bool";
    } else if (store_ty == SymV_ty) {
      if (std::dynamic_pointer_cast<SymV>(it -> second)->s != it->first) {
        std::cout << "(declare-const " << std::dynamic_pointer_cast<SymV>(it -> second)->s << type << ")" << std::endl;
      }
    }
    std::cout << "(declare-const " << it -> first << type << ")" << std::endl;
  }

  for (immer::map<String, Ptr<Value>>::iterator it = store.begin(); it != store.end(); it++) {
    auto v = it -> second;
    auto& store_ty = typeid(*v);
    if (store_ty == IntV_ty) {
      auto i1 = std::dynamic_pointer_cast<IntV>(v);
      std::cout << "(assert (= " << it -> first << " " << i1 -> i << "))" << std::endl;
    } else if (store_ty == BoolV_ty) {
      auto b1 = std::dynamic_pointer_cast<BoolV>(v);
      std::cout << "(assert (= " << it -> first << " " << b1 -> b << "))" << std::endl;
    } else if (store_ty == SymE_ty) {
      auto sym = std::dynamic_pointer_cast<SymE>(v);
      std::cout << "(assert (= " << it -> first << " " << to_smtlib(v) << "))" << std::endl;
    }
  }
}

void emit_path_condition(immer::set<String> pc) {
  for (auto u : pc) {
    //std::cout << "(assert " << u << ")" << std::endl;
    std::cout << parse_path_condition(u) << std::endl;
  }
}

String parse_path_condition(String pc) {
  bool not = false;
  if (pc.find("(-") == 0) {
    not = true;
  }
  // TODO add query to variables
  // find next "(", then substr
}


/* 
int main(int argc, char **argv) {
  VC vc = vc_createValidityChecker();

  // 32-bit variable 'c'
  Expr c = vc_varExpr(vc, "c", vc_bvType(vc, 32));

  // 32 bit constant value 5
  Expr a = vc_bvConstExprFromInt(vc, 32, 5);

  // 32 bit constant value 6
  Expr b = vc_bvConstExprFromInt(vc, 32, 6);

  // a+b!=c
  Expr xp1 = vc_bvPlusExpr(vc, 32, a, b);
  Expr eq = vc_eqExpr(vc, xp1, c);
  Expr eq2 = vc_notExpr(vc, eq);

  //Is a+b!=c always correct?
  int ret = vc_query(vc, eq2);

  //No, c=a+b is a counterexample
  assert(ret == false);

  //print c = 11 counterexample
  vc_printCounterExample(vc);

  //Delete validity checker
  vc_Destroy(vc);

  return 0;
}
*/

int apicallMain() {
  auto paths = Snippet(0);
  for (auto path : paths) {
    Store s = path.second.first;
    immer::set<String> pc = path.second.second;
    VC vc = vc_createValidityChecker();
  }
}


void emit_body(immer::flex_vector<std::pair<std::monostate, std::pair<immer::map<String, Ptr<Value>>, immer::set<String>>>> paths) {
  for (auto u : paths) {
    emit_store(u.second.first);
    emit_path_condition(u.second.second);
    std::cout << "(check-sat)" << std::endl;
    std::cout << "(reset)" << std::endl;
  }
}

int main(int argc, char *argv[]) {
  freopen("snippet.smt2", "w", stdout);
  //emit_header();
  emit_body(Snippet(0));
  return 0;
}