#include <ostream>
#include <variant>
#include <string>
#include <vector>

#include <immer/flex_vector.hpp>
#include <sai.hpp>

#ifndef SAI_IMP_SYM_HEADERS
#define SAI_IMP_SYM_HEADERS

/**********************************************************/

/*
struct IntV;
struct BoolV;
struct SymV;
struct SymE;
using Value = std::variant<IntV, BoolV, SymV, SymE>;
*/

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

std::shared_ptr<Value> op_2(String op, std::shared_ptr<Value> v1, std::shared_ptr<Value> v2) {
	auto i1 = std::dynamic_pointer_cast<IntV>(v1);
	auto i2 = std::dynamic_pointer_cast<IntV>(v2);

	if (i1 && i2) {
    auto r = std::make_shared<IntV>(0);
		if (op == "+") {
			r->i = i1->i + i1->i;
      return r;
		} else if (op == "-") {
			r->i = i1->i - i1->i;
      return r;
		} else if (op == "*") {
			r->i = i1->i * i1->i;
      return r;
		} else if (op == "/") {
			r->i = i1->i / i1->i;
      return r;
		} else {
			auto b = std::make_shared<BoolV>(true);
			if (op == "==") {
				b->b = i1->i == i2->i;
			} else if (op == ">=") {
				b->b = i1->i >= i2->i;
			} else if (op == ">") {
				b->b = i1->i > i2->i;
			} else if (op == "<=") {
				b->b = i1->i <= i2->i;
			} else if (op == "<") {
				b->b = i1->i < i2->i;
			} else if (op == "!=") {
				b->b = i1->i != i2->i;
			}
		}
	}

	ASSERT(false, "not a valid operator");
}

/*
std::ostream& operator<<(std::ostream& os, const Value& v) {
	return os;
}

std::ostream& operator<<(std::ostream& os, const IntV& i) {
  return os << "IntV(" << i.i << ")";
}

std::ostream& operator<<(std::ostream& os, const BoolV& b) {
  return os << "BoolV(" << b.b << ")";
}

std::ostream& operator<<(std::ostream& os, const SymV& s) {
  return os << "SymV(" << s.s << ")";
}

std::ostream& operator<<(std::ostream& os, const SymE& s) {
  os << "SymE(" << s.op << ", ";
  for (int i = 0; i < s.args.size(); i++) {
    os << s.args.at(i);
    if (i != s.args.size()-1) os << ", ";
  }
	os << ")";
	return os;
}
*/

/*
std::ostream& operator<<(std::ostream& os, const Value& v) {
  return std::visit(overloaded {
    [&](IntV i)->std::ostream& { return os << i; },
		[&](BoolV b)->std::ostream& { return os << b; },
		[&](SymV s)->std::ostream& { return os << s; },
		[&](SymE s)->std::ostream& { return os << s; },
  }, v);
}
*/

/**********************************************************/

#endif
