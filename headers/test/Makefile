FLAGS := -I ../ -I ../../../third-party/immer -I ../../../third-party/parallel-hashmap -I ../../../stp/build/include/ -L ../../../stp/build/lib/ -lstp -fPIC

targets = fact_lms fact_plain sym_test conc_test stp_test fs_test external_test

all: $(targets) 

fact_lms: fact_lms.cpp ../sai.hpp ../sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_lms.cpp -o ./fact_lms $(FLAGS)

fact_plain: fact_plain.cpp ../sai.hpp ../sai_imp_concrete.hpp
	g++ -std=c++17 -O3 -Winline -finline-functions ./fact_plain.cpp -o ./fact_plain $(FLAGS)

sym_test: sym_test.cpp ../sai.hpp ../sai_imp_sym.hpp
	g++ -std=c++17 sym_test.cpp -o sym_test $(FLAGS)

fs_test: fs_test.cpp ../llsc.hpp
	g++ -std=c++17 fs_test.cpp -o fs_test $(FLAGS)

external_test: external_test.cpp ../llsc.hpp
	g++ -std=c++17 external_test.cpp -o external_test $(FLAGS)

args_test: args_test.cpp ../llsc.hpp
	g++ -std=c++17 args_test.cpp -o args_test $(FLAGS)

llsc_test: llsc_test.cpp ../llsc.hpp
	g++ -std=c++17 llsc_test.cpp -o llsc_test $(FLAGS)

conc_test: conc_test.cpp ../conc.hpp
	g++ -std=c++17 conc_test.cpp -o conc_test $(FLAGS)

stp_test: stp_test.cpp
	g++ -std=c++17 stp_test.cpp -o stp_test $(FLAGS)

clean:
	$(RM) $(targets)
