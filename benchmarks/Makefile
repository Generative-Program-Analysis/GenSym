SUBDIRS := $(subst /Makefile,,$(wildcard */Makefile))

all: $(SUBDIRS)

$(SUBDIRS): 
	$(MAKE) -C $@

clean:
	for dir in $(SUBDIRS); do \
		echo "Cleaning $$dir..."; \
		$(MAKE) -C $$dir clean; \
	done 

.PHONY: all $(SUBDIRS) clean
