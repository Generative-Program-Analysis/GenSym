#include <iostream>
#include <sai_imp_sym.hpp>

int main() {
	//Value x = (struct IntV){1};
	//Value x = IntV(1);
	IntV x(1);
	std::cout << x << std::endl;

	Value& y = x;
	std::cout << y << std::endl;

	std::shared_ptr<Value> z = std::make_shared<IntV>(x);
	std::cout << *z << std::endl;

	std::cout << *op_2("+", z, std::make_shared<IntV>(x)) << std::endl;
	
	BoolV b(true);
	std::cout << b << std::endl;

	SymV a("a");
	std::cout << a << std::endl;

	SymE s("+", { std::make_shared<IntV>(x), std::make_shared<BoolV>(b) }); //(struct SymE){ "+", { x, b } };
	std::cout << s << std::endl;

	//std::cout << s.args.at(0) << std::endl;
	/*
	auto w = immer::flex_vector<Value>();
	w = w.push_back(b);
	*/
	
	//auto v = immer::set<Value>();
	//v = v.insert(x);
	
  return 0;
}
