all: fact_lms fact_plain

fact_lms: fact_lms.cpp sai.hpp sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_lms.cpp -o ./fact_lms  -I ./ -I ../../immer

fact_plain: fact_plain.cpp sai.hpp sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_plain.cpp -o ./fact_plain  -I ./ -I ../../immer

sym_test: sym_test.cpp sai_imp_sym.hpp
	g++ -std=c++17 sym_test.cpp -o sym_test  -I ./ -I ../../immer
