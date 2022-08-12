#include <iostream>
#include <sai_imp_sym.hpp>

void test_set() {
	Ptr<Value> v1 = make_IntV(1);
	Ptr<Value> v2 = make_IntV(2);
	Ptr<Value> v3 = make_IntV(2);

	Ptr<Value> v4 = make_SymE("+", { v1, v2 }); // 1 + 2
	Ptr<Value> v5 = make_SymE("+", { v1, v3 }); // 1 + 2


	std::cout << "v1 == v2: " << (v1 == v2) << std::endl;
	std::cout << "v2 == v3: " << (v2 == v3) << std::endl;
	std::cout << "v4 == v5: " << (v4 == v5) << std::endl;

	auto s1 = immer::set<Ptr<Value>>();
	auto s2 = s1.insert(v1);
	auto s3 = s2.insert(v2);

	std::cout << "s2.size(): " << s2.size() << std::endl;
	std::cout << "s3.size(): " << s3.size() << std::endl;

	auto s4 = s3.insert(v3);
	print_set(s4); std::cout << std::endl;

	Ptr<Value> v6 = make_SymE("*", { make_SymV("a"), make_IntV(4) });
	Ptr<Value> v7 = make_SymE("*", { make_SymV("a"), make_IntV(4) });
	std::cout << "v6 == v7: " << (v6 == v7) << std::endl;

	Ptr<Value> v8 = make_SymE("/", { make_IntV(1), v6 });
	Ptr<Value> v9 = make_SymE("/", { make_IntV(1), v7 });
	std::cout << "v8 == v9: " << (v8 == v9) << std::endl;
}

int main() {
	//Value x = (struct IntV){1};
	//Value x = IntV(1);
	IntV x(1);
	std::cout << x << std::endl;

	Ptr<IntV> x2 = make_IntV(2);
	Ptr<IntV> x3 = x2;
	std::cout << *op_2("*", x2, x3) << std::endl;

	//std::cout << (*x2 == *x3) << std::endl; //FIXME

	//Value& y = x;
	//std::cout << y << std::endl;

	simple_ptr<Value> z = make_simple<IntV>(x);
	std::cout << *z << std::endl;
	//std::cout << (*z == x) << std::endl;

	std::cout << *op_2("+", z, make_simple<IntV>(x)) << std::endl;
	
	BoolV b(true);
	std::cout << b << std::endl;

	// not well-typed
	//std::cout << *op_2("+", z, std::make_shared<BoolV>(b)) << std::endl;

	//SymV a("a");
	Ptr<SymV> a = make_SymV("a");
	std::cout << *a << std::endl;

	//SymE s("+", { std::make_shared<IntV>(x), std::make_shared<BoolV>(b) }); //(struct SymE){ "+", { x, b } };
	Ptr<SymE> s = make_SymE("+", {x2, a});
	std::cout << *s << std::endl;

	std::cout << *s->args.at(0) << std::endl;
	
	auto v = immer::set<simple_ptr<Value>>();
	v = v.insert(make_simple<BoolV>(b));

	test_set();

  auto m = std::monostate{};

  return 0;
}
