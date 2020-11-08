#include <ostream>
#include <variant>
#include <string>
#include <vector>
#include <iostream>

#include <immer/flex_vector.hpp>
#include <sai.hpp>

#ifndef SAI_IMP_SYM_HEADERS
#define SAI_IMP_SYM_HEADERS

/**********************************************************/

struct Value {
  friend std::ostream& operator<<(std::ostream&os, const Value& v) {
    return v.toString(os);
  }
  virtual std::ostream& toString(std::ostream& os) const = 0;
};

struct IntV : Value {
  int i;
  IntV(int i) : i(i) {}
  IntV(const IntV& v) { i = v.i; }
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "IntV(" << i << ")";
  }
};

inline Ptr<IntV> make_IntV(int i) {
  return std::make_shared<IntV>(i);
}

#define proj_IntV(v) (std::dynamic_pointer_cast<IntV>(v)->i)

struct BoolV : Value {
  bool b;
  BoolV(bool b) : b(b) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "BoolV(" << b << ")";
  }
};

inline Ptr<BoolV> make_BoolV(bool b) {
  return std::make_shared<BoolV>(b);
}

#define proj_BoolV(v) (std::dynamic_pointer_cast<BoolV>(v)->b)

inline bool is_BoolV(Ptr<Value>& v) {
  return std::dynamic_pointer_cast<BoolV>(v) != nullptr;
}

struct SymV : Value {
  String s;
  SymV(String s) : s(s) {}
  virtual std::ostream& toString(std::ostream& os) const override {
    return os << "SymV(" << s << ")";
  }
};

inline Ptr<SymV> make_SymV(String s) {
  return std::make_shared<SymV>(s);
}

struct SymE : Value {
  String op;
  std::vector<std::shared_ptr<Value>> args;
  SymE(String op, std::vector<std::shared_ptr<Value>> args) : op(op), args(args) {}

  virtual std::ostream& toString(std::ostream& os) const override {
    os << "SymE(" << op << ", ";
    for (int i = 0; i < args.size(); i++) {
      os << *args.at(i);
      if (i != args.size()-1) os << ", ";
    }
    os << ")";
    return os;
  }
};

inline Ptr<SymE> make_SymE(String op, std::vector<Ptr<Value>> args) {
  return std::make_shared<SymE>(op, args);
}

Ptr<Value> op_2(String op, Ptr<Value> v1, Ptr<Value> v2) {
  auto i1 = std::dynamic_pointer_cast<IntV>(v1);
  auto i2 = std::dynamic_pointer_cast<IntV>(v2);

  if (i1 && i2) {
    if (op == "+") {
      return make_IntV(i1->i + i2->i);
    } else if (op == "-") {
      return make_IntV(i1->i - i2->i);
    } else if (op == "*") {
      return make_IntV(i1->i * i2->i);
    } else if (op == "/") {
      return make_IntV(i1->i / i2->i);
    } else if (op == "==") {
      return make_BoolV(i1->i == i2->i);
    } else if (op == ">=") {
      return make_BoolV(i1->i >= i2->i);
    } else if (op == ">") {
      return make_BoolV(i1->i > i2->i);
    } else if (op == "<=") {
      return make_BoolV(i1->i <= i2->i);
    } else if (op == "<") {
      return make_BoolV(i1->i < i2->i);
    } else if (op == "!=") {
      return make_BoolV(i1->i != i2->i);
    } else {
      ASSERT(false, "invalid operator");
    }
  } else {
    // TODO: check whether op is valid
    return make_SymE(op, { v1, v2 });
  }
}

const static auto& IntV_ty = typeid(struct IntV);
const static auto& BoolV_ty = typeid(struct BoolV);
const static auto& SymV_ty = typeid(struct SymV);
const static auto& SymE_ty = typeid(struct SymE);

inline bool operator==(const Ptr<Value>& lhs, const Ptr<Value>& rhs);

inline bool operator==(const Ptr<IntV>& lhs, const Ptr<IntV>& rhs) {
  return lhs->i == rhs->i;
}

inline bool operator==(const Ptr<BoolV>& lhs, const Ptr<BoolV>& rhs) {
  return lhs->b == rhs->b;
}

inline bool operator==(const Ptr<SymV>& lhs, const Ptr<SymV>& rhs) {
  return lhs->s == rhs->s;
}

inline bool operator==(const Ptr<SymE>& lhs, const Ptr<SymE>& rhs) {
  return (lhs->op == rhs->op) && (lhs->args == rhs->args);
}

inline bool operator==(const Ptr<Value>& lhs, const Ptr<Value>& rhs){
  auto& lhs_ty = typeid(*lhs);
  auto& rhs_ty = typeid(*rhs);

  if (lhs_ty == IntV_ty && rhs_ty == IntV_ty) {
    return std::dynamic_pointer_cast<IntV>(lhs) ==
      std::dynamic_pointer_cast<IntV>(rhs);
  } else if (lhs_ty == BoolV_ty && rhs_ty == BoolV_ty) {
    return std::dynamic_pointer_cast<BoolV>(lhs) ==
      std::dynamic_pointer_cast<BoolV>(rhs);
  } else if (lhs_ty == SymV_ty && rhs_ty == SymV_ty) {
    return std::dynamic_pointer_cast<SymV>(lhs) ==
      std::dynamic_pointer_cast<SymV>(rhs);
  } else if (lhs_ty == SymE_ty && rhs_ty == SymE_ty) {
    return std::dynamic_pointer_cast<SymE>(lhs) ==
      std::dynamic_pointer_cast<SymE>(rhs);
  }
  return false;
}

namespace std {
  template<>
  struct equal_to<Ptr<Value>> {
    bool operator()(const Ptr<Value>& lhs, const Ptr<Value>& rhs) const {
      return lhs == rhs;
    }
  };

  template<>
  struct hash<IntV> {
    std::size_t operator()(const IntV& v) const {
      return std::hash<int>{}(v.i);
    }
  };

  template<>
  struct hash<BoolV> {
    std::size_t operator()(const BoolV& v) const {
      return std::hash<bool>{}(v.b);
    }
  };

  template<>
  struct hash<SymV> {
    std::size_t operator()(const SymV& v) const {
      return std::hash<std::string>{}(v.s);
    }
  };

  template<>
  struct hash<Ptr<Value>> {
    std::size_t operator()(const Ptr<Value>& v) const {
      auto& v_ty = typeid(*v);
      if (v_ty == IntV_ty) {
        return std::hash<IntV>{}(*std::dynamic_pointer_cast<IntV>(v));
      } else if (v_ty == BoolV_ty) {
        return std::hash<BoolV>{}(*std::dynamic_pointer_cast<BoolV>(v));
      } else if (v_ty == SymV_ty) {
        return std::hash<SymV>{}(*std::dynamic_pointer_cast<SymV>(v));
      } else if (v_ty == SymE_ty) {
        //TODO: have to copy-paste code here?
        auto sv = std::dynamic_pointer_cast<SymE>(v);
        auto h1 = std::hash<std::string>{}(sv->op);
        auto vec_hash = [](const std::vector<Ptr<Value>>& vec) {
          std::size_t seed = vec.size();
          for(auto& i : vec) {
            auto h = std::hash<Ptr<Value>>{}(i);
            seed ^= h + 0x9e3779b9 + (seed << 6) + (seed >> 2);
          }
          return seed;
        };
        auto h2 = vec_hash(sv->args);
        return h2 + 0x9e3779b9 + (h1 << 6) + (h1 >> 2);
      } else {
        ASSERT(false, "not of type Ptr<Value>");
      }
    }
  };

  template<>
  struct hash<std::vector<Ptr<Value>>> {
    std::size_t operator()(const std::vector<Ptr<Value>>& vec) const {
      std::size_t seed = vec.size();
      for(auto& i : vec) {
        auto h = std::hash<Ptr<Value>>{}(i);
        // https://stackoverflow.com/questions/4948780/magic-number-in-boosthash-combine
        seed ^= h + 0x9e3779b9 + (seed << 6) + (seed >> 2);
      }
      return seed;
    }
  };

  template<>
  struct hash<SymE> {
    std::size_t operator()(const SymE& v) const {
      auto h1 = std::hash<std::string>{}(v.op);
      auto h2 = std::hash<std::vector<std::shared_ptr<Value>>>{}(v.args);
      return h2 + 0x9e3779b9 + (h1 << 6) + (h1 >> 2);
    }
  };
}

static int loop_count = 0;
const static int loop_upperbound = 10;

Ptr<Value> continue_loop(Ptr<Value> c) {
  std::cout << *c << std::endl;
  if (is_BoolV(c))
    return c;
  if (loop_count < loop_upperbound) {
    loop_count++;
    return make_BoolV(true);
  } else {
    return make_BoolV(false);
  }
}

/**********************************************************/

#endif
