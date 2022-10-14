FLAGS := -I ../ -I ../../third-party/immer -I ../../third-party/parallel-hashmap -I ../../third-party/stp/build/include/ -L ../../third-party/stp/build/lib/ -lstp -fPIC

targets = fact_lms fact_plain sym_test conc_test stp_test fs_test external_test

all: $(targets)

fs_test: fs_test.cpp ../llsc.hpp
	g++ -std=c++17 fs_test.cpp -o fs_test $(FLAGS)

external_test: external_test.cpp ../llsc.hpp
	g++ -std=c++17 external_test.cpp -o external_test $(FLAGS)

args_test: args_test.cpp ../llsc.hpp
	g++ -std=c++17 args_test.cpp -o args_test $(FLAGS)

llsc_test: llsc_test.cpp ../llsc.hpp
	g++ -std=c++17 llsc_test.cpp -o llsc_test $(FLAGS)

clean:
	$(RM) $(targets)
