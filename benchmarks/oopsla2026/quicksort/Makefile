QUICKSORT_CONFIGS ?= sym3.size5 sym3.size10
QUICKSORT_REQUIRED_CONFIGS := sym2.size20 sym2.size30 sym2.size40 sym2.size50
override QUICKSORT_CONFIGS := $(sort $(QUICKSORT_CONFIGS) $(QUICKSORT_REQUIRED_CONFIGS))
QUICKSORT1_CONFIGS := $(filter sym2.%,$(QUICKSORT_CONFIGS))
QUICKSORT2_CONFIGS := $(filter sym3.%,$(QUICKSORT_CONFIGS))
GENWASYM_TEST_DIR ?= genwasym-test-artifacts
QUICKSORT_BUILD_ROOT ?= artifacts
QUICKSORT_BUILD_DIR ?= $(QUICKSORT_BUILD_ROOT)/wasm
QUICKSORT_NORMALIZED_DIR ?= genwasym-test-input
QUICKSORT_RAW_WAT_DIR ?= $(QUICKSORT_BUILD_ROOT)/raw-wat
QUICKSORT_WAST_DIR ?= wasp-test-input
NORMALIZE_WAT ?= ../normalize_wat.py
QUICKSORT_SYM2_SOURCE ?= quicksort1.c
QUICKSORT_SYM3_SOURCE ?= quicksort2.c

QUICKSORT_WASM = $(foreach cfg,$(QUICKSORT1_CONFIGS),$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,quicksort1,$(cfg)).wasm) \
                 $(foreach cfg,$(QUICKSORT2_CONFIGS),$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,quicksort2,$(cfg)).wasm)
QUICKSORT_WAT = $(foreach cfg,$(QUICKSORT1_CONFIGS),$(QUICKSORT_NORMALIZED_DIR)/$(call quicksort_name,quicksort1,$(cfg)).wat) \
                $(foreach cfg,$(QUICKSORT2_CONFIGS),$(QUICKSORT_NORMALIZED_DIR)/$(call quicksort_name,quicksort2,$(cfg)).wat)
QUICKSORT_WAST = $(foreach cfg,$(QUICKSORT1_CONFIGS),$(QUICKSORT_WAST_DIR)/$(call quicksort_name,quicksort1,$(cfg)).wast) \
                 $(foreach cfg,$(QUICKSORT2_CONFIGS),$(QUICKSORT_WAST_DIR)/$(call quicksort_name,quicksort2,$(cfg)).wast)

quicksort_sym = $(patsubst sym%,%,$(word 1,$(subst ., ,$(1))))
quicksort_size = $(patsubst size%,%,$(word 2,$(subst ., ,$(1))))
quicksort_prelude_flag = $(if $(filter sym3.%,$(1)),-DPRELUDE,)
quicksort_source = $(if $(filter quicksort1,$(1)),$(QUICKSORT_SYM2_SOURCE),$(QUICKSORT_SYM3_SOURCE))
quicksort_name = $(1).$(2)

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
$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).wasm.o: $(call quicksort_source,$(1))
	mkdir -p $$(QUICKSORT_BUILD_DIR)
	$$(WASM_CC) -c $$(R_CFLAGS) $$(filter-out -mexec-model=reactor,$$(WASM_CFLAGS)) $$(QUICKSORT_WASM_OPT_CFLAGS) $(call quicksort_prelude_flag,$(2)) -DSYM_SIZE=$(call quicksort_sym,$(2)) -DSIZE=$(call quicksort_size,$(2)) $$< -o $$@ -I.

$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).wasm: $(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).wasm.o
	$$(WASM_LD) $$< --entry=_start --export=main --export=_start --allow-undefined -o $$@

$(QUICKSORT_NORMALIZED_DIR)/$(call quicksort_name,$(1),$(2)).wat: $(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).wasm
	mkdir -p $$(QUICKSORT_NORMALIZED_DIR)
	mkdir -p $$(QUICKSORT_RAW_WAT_DIR)
	$$(WASM2WAT) $$< -o $$(QUICKSORT_RAW_WAT_DIR)/$(call quicksort_name,$(1),$(2)).raw.wat
	if grep -q '^[[:space:]]*(start [0-9][0-9]*)' $$(QUICKSORT_RAW_WAT_DIR)/$(call quicksort_name,$(1),$(2)).raw.wat; then \
	  cp $$(QUICKSORT_RAW_WAT_DIR)/$(call quicksort_name,$(1),$(2)).raw.wat $$@.tmp; \
	else \
	  perl -pe 'print "  (start $$$$1)\n" if /^  \(export "_start" \(func ([^)]+)\)\)/' $$(QUICKSORT_RAW_WAT_DIR)/$(call quicksort_name,$(1),$(2)).raw.wat > $$@.tmp; \
	fi
	$$(WAT2WASM) $$@.tmp -o $$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).roundtrip.wasm
	$$(WASM2WAT) $$(QUICKSORT_BUILD_DIR)/$(call quicksort_name,$(1),$(2)).roundtrip.wasm -o $$@
	$$(RM) $$@.tmp

$(QUICKSORT_WAST_DIR)/$(call quicksort_name,$(1),$(2)).wast: $(QUICKSORT_NORMALIZED_DIR)/$(call quicksort_name,$(1),$(2)).wat
	mkdir -p $$(QUICKSORT_WAST_DIR)
	python3 $$(NORMALIZE_WAT) --wat-to-wast $$< $$@
endef

$(foreach cfg,$(QUICKSORT1_CONFIGS),$(eval $(call QUICKSORT_RULES,quicksort1,$(cfg))))
$(foreach cfg,$(QUICKSORT2_CONFIGS),$(eval $(call QUICKSORT_RULES,quicksort2,$(cfg))))

clean:
	$(RM) -r $(QUICKSORT_BUILD_ROOT)
	$(RM) $(QUICKSORT_WAST)
