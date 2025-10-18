.PHONY: all run clean

all: large-branch.wat.cpp.imm.noreuse.exe large-branch.wat.cpp.imm.exe large-branch.wat.cpp.imm.cost-model.exe

large-branch.wat.cpp.imm.noreuse.exe: large-branch.wat.cpp
	g++ large-branch.wat.cpp -o large-branch.wat.cpp.imm.noreuse.exe -DUSE_IMM -I ../../../headers -lz3 -DENABLE_PROFILE_TIME -O3 -DNO_REUSE -DENABLE_PROFILE_STEP

large-branch.wat.cpp.imm.exe: large-branch.wat.cpp
	g++ large-branch.wat.cpp -o large-branch.wat.cpp.imm.exe -DUSE_IMM -I ../../../headers -lz3 -DENABLE_PROFILE_TIME -O3 -DENABLE_PROFILE_STEP

large-branch.wat.cpp.imm.cost-model.exe: large-branch.wat.cpp
	g++ large-branch.wat.cpp -o large-branch.wat.cpp.imm.cost-model.exe -DUSE_IMM -I ../../../headers -lz3 -DENABLE_PROFILE_TIME -O3 -DUSE_COST_MODEL


run: all
	./large-branch.wat.cpp.imm.noreuse.exe
	./large-branch.wat.cpp.imm.exe

clean:
	rm -f *.exe