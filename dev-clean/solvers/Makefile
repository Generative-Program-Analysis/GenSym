all: solver

solver: solver.cpp sai_imp_sym.hpp
	g++ -std=c++17 solver.cpp -o solver  -I ./ -I ../../immer

stp: stp_playground.c 
	g++ -std=c++17 -O3 -Winline -L /homes/tan279/lib ./stp_playground.c -o ./snippet  -I ./headers -I ../immer -I ../../stp/include -lstp
