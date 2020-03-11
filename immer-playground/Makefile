all: vec map variant set

vec: vec.cpp
	g++ -std=c++17 -O2 -o vec vec.cpp -I ../immer -I ../dev-clean/headers

map: map.cpp
	g++ -std=c++17 -O2 -o map map.cpp -I ../immer -I ../dev-clean/headers

set: set.cpp
	g++ -std=c++17 -O2 -o set set.cpp -I ../immer -I ../dev-clean/headers

variant: variant.cpp
	g++ -std=c++17 -O2 -o variant variant.cpp -I ../immer -I ../dev-clean/headers

clean:
	rm vec
	rm map
	rm variant
	rm set
