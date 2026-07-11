QUICKSORT_CONFIGS ?= sym3.size5 sym3.size10
QUICKSORT_REQUIRED_CONFIGS := sym2.size20 sym2.size30 sym2.size40 sym2.size50
override QUICKSORT_CONFIGS := $(sort $(QUICKSORT_CONFIGS) $(QUICKSORT_REQUIRED_CONFIGS))
GENWASYM_TEST_DIR ?= genwasym-test-artifacts
QUICKSORT_BUILD_ROOT ?= artifacts
QUICKSORT_BUILD_DIR ?= $(QUICKSORT_BUILD_ROOT)/wasm
QUICKSORT_NORMALIZED_DIR ?= genwasym-test-input
QUICKSORT_RAW_WAT_DIR ?= $(QUICKSORT_BUILD_ROOT)/raw-wat
QUICKSORT_WAST_DIR ?= wasp-test-input
NORMALIZE_WAT ?= ../normalize_wat.py

QUICKSORT_WASM = $(foreach cfg,$(QUICKSORT_CONFIGS),$(QUICKSORT_BUILD_DIR)/quicksort.$(cfg).wasm)
QUICKSORT_WAT = $(foreach cfg,$(QUICKSORT_CONFIGS),$(QUICKSORT_NORMALIZED_DIR)/quicksort.$(cfg).wat)
QUICKSORT_WAST = $(foreach cfg,$(QUICKSORT_CONFIGS),$(QUICKSORT_WAST_DIR)/quicksort.$(cfg).wast)

quicksort_sym = $(patsubst sym%,%,$(word 1,$(subst ., ,$(1))))
quicksort_size = $(patsubst size%,%,$(word 2,$(subst ., ,$(1))))
quicksort_prelude_flag = $(if $(filter sym3.%,$(1)),-DPRELUDE,)

WASI_SYSROOT ?= /opt/wasi-sdk/share/wasi-sysroot
WASI_CLANG ?= /opt/wasi-sdk/bin/wasm32-wasip1-clang
ifneq ($(wildcard $(WASI_CLANG)),)
WASM_CC ?= $(WASI_CLANG)
else
WASM_CC ?= clang
endif
WASM_LD ?= /opt/wasi-sdk/bin/wasm-ld
WASM_CFLAGS ?= --target=wasm32-wasip1 -mexec-model=reactor -mno-nontrapping-fptoint -mno-bulk-memory -mno-bulk-memory-opt
QUICKSORT_WASM_OPT_CFLAGS ?= -O0 -fdata-sections -ffunction-sections -fno-stack-protector -DNDEBUG
WASM2WAT ?= wasm2wat
WAT2WASM ?= wat2wasm
R_CFLAGS ?= -fPIC -w

ifneq ($(wildcard $(WASI_SYSROOT)),)
WASM_CFLAGS += --sysroot=$(WASI_SYSROOT)
endif

.PHONY: all quicksort-wasm quicksort-wat quicksort-wast clean

all: quicksort-wat quicksort-wast

quicksort-wasm: $(QUICKSORT_WASM)

quicksort-wat: $(QUICKSORT_WAT)

quicksort-wast: $(QUICKSORT_WAST)

define QUICKSORT_RULES
$(QUICKSORT_BUILD_DIR)/quicksort.$(1).wasm.o: quicksort.c
	mkdir -p $$(QUICKSORT_BUILD_DIR)
	$$(WASM_CC) -c $$(R_CFLAGS) $$(filter-out -mexec-model=reactor,$$(WASM_CFLAGS)) $$(QUICKSORT_WASM_OPT_CFLAGS) $(call quicksort_prelude_flag,$(1)) -DSYM_SIZE=$(call quicksort_sym,$(1)) -DSIZE=$(call quicksort_size,$(1)) $$< -o $$@ -I.

$(QUICKSORT_BUILD_DIR)/quicksort.$(1).wasm: $(QUICKSORT_BUILD_DIR)/quicksort.$(1).wasm.o
	$$(WASM_LD) $$< --entry=_start --export=main --export=_start --allow-undefined -o $$@

$(QUICKSORT_NORMALIZED_DIR)/quicksort.$(1).wat: $(QUICKSORT_BUILD_DIR)/quicksort.$(1).wasm
	mkdir -p $$(QUICKSORT_NORMALIZED_DIR)
	mkdir -p $$(QUICKSORT_RAW_WAT_DIR)
	$$(WASM2WAT) $$< -o $$(QUICKSORT_RAW_WAT_DIR)/quicksort.$(1).raw.wat
	if grep -q '^[[:space:]]*(start [0-9][0-9]*)' $$(QUICKSORT_RAW_WAT_DIR)/quicksort.$(1).raw.wat; then \
	  cp $$(QUICKSORT_RAW_WAT_DIR)/quicksort.$(1).raw.wat $$@.tmp; \
	else \
	  perl -pe 'print "  (start $$$$1)\n" if /^  \(export "_start" \(func ([^)]+)\)\)/' $$(QUICKSORT_RAW_WAT_DIR)/quicksort.$(1).raw.wat > $$@.tmp; \
	fi
	$$(WAT2WASM) $$@.tmp -o $$(QUICKSORT_BUILD_DIR)/quicksort.$(1).roundtrip.wasm
	$$(WASM2WAT) $$(QUICKSORT_BUILD_DIR)/quicksort.$(1).roundtrip.wasm -o $$@
	$$(RM) $$@.tmp

$(QUICKSORT_WAST_DIR)/quicksort.$(1).wast: $(QUICKSORT_NORMALIZED_DIR)/quicksort.$(1).wat
	mkdir -p $$(QUICKSORT_WAST_DIR)
	python3 $$(NORMALIZE_WAT) --wat-to-wast $$< $$@
endef

$(foreach cfg,$(QUICKSORT_CONFIGS),$(eval $(call QUICKSORT_RULES,$(cfg))))

clean:
	$(RM) -r $(QUICKSORT_BUILD_ROOT)
	$(RM) $(QUICKSORT_WAST)
