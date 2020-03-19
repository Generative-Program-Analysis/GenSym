#include <iostream>
#include <sai_imp_sym.hpp>

int main() {
	//Value x = (struct IntV){1};
	//Value x = IntV(1);
	IntV x(1);
	std::cout << x << std::endl;

	Ptr<IntV> x2 = make_IntV(2);
	Ptr<IntV> x3 = x2;
	std::cout << *op_2("*", x2, x3) << std::endl;

	std::cout << (*x2 == *x3) << std::endl;

	//Value& y = x;
	//std::cout << y << std::endl;

	std::shared_ptr<Value> z = std::make_shared<IntV>(x);
	std::cout << *z << std::endl;
	//std::cout << (*z == x) << std::endl;

	std::cout << *op_2("+", z, std::make_shared<IntV>(x)) << std::endl;
	
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
	
	auto v = immer::set<std::shared_ptr<Value>>();
	v = v.insert(std::make_shared<BoolV>(b));
	
  return 0;
}
