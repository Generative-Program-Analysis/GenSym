# Makefile for building different versions of 2o1u-unlabeled.wat.cpp

CXX      := g++
SRC      := 2o1u-unlabeled.wat.cpp
HEADERS  := ../../headers/
LDFLAGS  := -lz3
CXXFLAGS := -g -I $(HEADERS) -std=c++17 -DENABLE_PROFILE -fno-omit-frame-pointer -O3 -DENABLE_PROFILE_TIME

.PHONY: all clean benchmark

all: 2o1u-unlabeled.wat.cpp.imm.no-snap.exe 2o1u-unlabeled.wat.cpp.imm.exe 2o1u-unlabeled.wat.cpp.no-snap.exe 2o1u-unlabeled.wat.cpp.exe \
	 2o1u-unlabeled.wat.cpp.concolic.imm.no-snap.exe 2o1u-unlabeled.wat.cpp.concolic.imm.exe 2o1u-unlabeled.wat.cpp.concolic.no-snap.exe 2o1u-unlabeled.wat.cpp.concolic.exe 2o1u-unlabeled.wat.cpp.concolic.imm.cost-model.exe

2o1u-unlabeled.wat.cpp.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DRUN_ONCE

2o1u-unlabeled.wat.cpp.no-snap.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DNO_REUSE -DRUN_ONCE

2o1u-unlabeled.wat.cpp.imm.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DUSE_IMM -DRUN_ONCE

2o1u-unlabeled.wat.cpp.imm.no-snap.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DUSE_IMM -DNO_REUSE -DRUN_ONCE


## Concolic execution tests
2o1u-unlabeled.wat.cpp.concolic.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DBY_COVERAGE

2o1u-unlabeled.wat.cpp.concolic.no-snap.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DNO_REUSE -DBY_COVERAGE

2o1u-unlabeled.wat.cpp.concolic.imm.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DUSE_IMM -DBY_COVERAGE

2o1u-unlabeled.wat.cpp.concolic.imm.no-snap.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DUSE_IMM -DNO_REUSE -DBY_COVERAGE

2o1u-unlabeled.wat.cpp.concolic.imm.cost-model.exe: $(SRC)
	$(CXX) $< -o $@ $(CXXFLAGS) $(LDFLAGS) -DUSE_IMM -DNO_REUSE -DBY_COVERAGE -DUSE_COST_MODEL

benchmark: all
	hyperfine './2o1u-unlabeled.wat.cpp.imm.no-snap.exe' \
	          './2o1u-unlabeled.wat.cpp.imm.exe' \
	          './2o1u-unlabeled.wat.cpp.no-snap.exe' \
	          './2o1u-unlabeled.wat.cpp.exe'
	hyperfine './2o1u-unlabeled.wat.cpp.concolic.imm.no-snap.exe' \
	          './2o1u-unlabeled.wat.cpp.concolic.imm.exe' \
	          './2o1u-unlabeled.wat.cpp.concolic.no-snap.exe' \
	          './2o1u-unlabeled.wat.cpp.concolic.exe'
clean:
	rm -f *.exe
