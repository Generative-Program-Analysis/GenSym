all: fact_lms fact_plain sym_test conc_test

fact_lms: fact_lms.cpp ../sai.hpp ../sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_lms.cpp -o ./fact_lms  -I ../ -I ../../../immer

fact_plain: fact_plain.cpp ../sai.hpp ../sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_plain.cpp -o ./fact_plain  -I ../ -I ../../../immer

sym_test: sym_test.cpp ../sai.hpp ../sai_imp_sym.hpp
	g++ -std=c++17 sym_test.cpp -o sym_test  -I ../ -I ../../../immer

llsc_test: llsc_test.cpp ../llsc.hpp 
	g++ -std=c++17 llsc_test.cpp -o llsc_test  -I ../ -I ../../../immer -I ../../../stp/build/include/ -L ../../../stp/build/lib/ -lstp -fPIC

conc_test: conc_test.cpp ../conc.hpp 
	g++ -std=c++17 conc_test.cpp -o conc_test  -I ../ -I ../../../immer -I ../../../stp/build/include/ -L ../../../stp/build/lib/ -lstp -fPIC

clean:
	rm fact_lms
	rm fact_plain
	rm sym_test
