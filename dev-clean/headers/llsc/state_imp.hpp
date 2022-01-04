#ifndef LLSC_STATE_IMP_HEADERS
#define LLSC_STATE_IMP_HEADERS

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout
template <class V>
class PreMem {
  private:
    std::vector<V> mem;
  public:
    PreMem(std::vector<V> mem) : mem(std::move(mem)) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    PreMem&& update(size_t idx, V val) {
      mem.at(idx) = val;
      return std::move(*this);
    }
    PreMem&& append(V val) {
      mem.push_back(val);
      return std::move(*this);
    }
    PreMem&& append(V val, size_t padding) {
      size_t idx = mem.size();
      return alloc(padding + 1).update(idx, val);
    }
    PreMem&& append(const std::vector<V>& vs) {
      mem.insert(mem.end(), vs.begin(), vs.end());
      return std::move(*this);
    }
    PreMem&& alloc(size_t size) {
      mem.resize(mem.size() + size, nullptr);
      return std::move(*this);
    }
    PreMem&& take(size_t keep) {
      mem.resize(keep);
      return std::move(*this);
    }
    PreMem slice(size_t idx, size_t len) {
      auto off = mem.begin() + idx;
      return PreMem(std::vector(off, off + len));
    }
    // PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    const std::vector<V>& getMem() { return mem; }
};

using Mem = PreMem<PtrVal>;

class Frame {
  public:
    using Env = std::map<Id, PtrVal>;
  private:
    Env env;
  public:
    Frame(Env env) : env(std::move(env)) {}
    Frame() : env(std::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame&& assign(Id id, PtrVal v) {
      env.insert_or_assign(id, v);
      return std::move(*this);
    }
    Frame&& assign_seq(const std::vector<Id>& ids, const std::vector<PtrVal>& vals) {
      for (size_t i = 0; i < ids.size(); i++) {
        env.insert_or_assign(ids.at(i), vals.at(i));
      }
      return std::move(*this);
    }
};

class Stack {
  private:
    Mem mem;
    std::vector<Frame> env;
  public:
    Stack(Mem mem, std::vector<Frame> env) : mem(std::move(mem)), env(std::move(env)) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    Stack&& pop(size_t keep) {
      mem.take(keep);
      env.pop_back();
      return std::move(*this);
    }
    Stack&& push() {
      return push(Frame());
    }
    Stack&& push(Frame f) {
      env.push_back(std::move(f));
      return std::move(*this);
    }

    Stack&& assign(Id id, PtrVal val) {
      env.back().assign(id, val);
      return std::move(*this);
    }
    Stack&& assign_seq(const std::vector<Id>& ids, std::vector<PtrVal> vals) {
      // varargs
      size_t id_size = ids.size();
      if (id_size > 0) {
        if (ids.back() == 0) {
          auto msize = mem.size();
          for (size_t i = id_size - 1; i < vals.size(); i++) {
            // FIXME: magic value 8, as vararg is retrived from +8 address
            mem.append(vals.at(i), 7);
          }
          if (mem.size() == msize) mem.alloc(8);
          vals.resize(id_size - 1);
          vals.push_back(make_LocV(msize, LocV::kStack));
        }
        env.back().assign_seq(ids, vals);
      }
      return std::move(*this);
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    PtrVal at(size_t idx, int size) {
      return std::make_shared<StructV>(mem.slice(idx, size).getMem());
    }
    Stack&& update(size_t idx, PtrVal val) {
      mem.update(idx, val);
      return std::move(*this);
    }
    Stack&& alloc(size_t size) {
      mem.alloc(size);
      return std::move(*this);
    }
};

class PC {
  private:
    std::set<SExpr> pc;
    SExpr last;
  public:
    PC(std::set<SExpr> pc, SExpr last = nullptr) : pc(std::move(pc)), last(last) {}
    PC&& add(SExpr e) {
      pc.insert(e);
      return std::move(*this);
    }
    PC&& addSet(const std::set<SExpr>& new_pc) {
      pc.insert(new_pc.begin(), new_pc.end());
      return std::move(*this);
    }
    PC&& addSet(const immer::set<SExpr>& new_pc) {
      pc.insert(new_pc.begin(), new_pc.end());
      return std::move(*this);
    }
    const std::set<SExpr>& getPC() { return pc; }
    SExpr getLast() { return last; }
    void print() { print_set(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
#ifdef LAZYALLOC
    std::vector< std::pair<std::string, size_t> > pending_allocs;

    void do_allocs() {
      for (auto &ac: pending_allocs) {
        if (ac.first == "stack")
          stack.alloc(ac.second);
        else
          heap.alloc(ac.second);
      }
      pending_allocs.clear();
    }
#endif

  public:
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) :
      heap(std::move(heap)), stack(std::move(stack)), pc(std::move(pc)), bb(bb) {}
    SS(immer::flex_vector<PtrVal> heap, Stack stack, PC pc, BlockLabel bb) :
      heap(std::move(std::vector(heap.begin(), heap.end()))), stack(std::move(stack)), pc(std::move(pc)), bb(bb) {}
    SS copy() { return *this; }
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
    size_t heap_size() { return heap.size(); }
    size_t stack_size() { return stack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    PtrVal at(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    PtrVal at(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l, size);
      return std::make_shared<StructV>(heap.slice(loc->l, size).getMem());
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr); }
    BlockLabel incoming_block() { return bb; }
    SS&& alloc_stack(size_t size) {
#ifdef LAZYALLOC
      pending_allocs.push_back({"stack", size});
#else
      stack.alloc(size);
#endif
      return std::move(*this);
    }
    SS&& alloc_heap(size_t size) {
#ifdef LAZYALLOC
      pending_allocs.push_back({"heap", size});
#else
      heap.alloc(size);
#endif
      return std::move(*this);
    }
    SS&& update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        stack.update(loc->l, val);
      else
        heap.update(loc->l, val);
      return std::move(*this);
    }
    SS&& push() {
      stack.push();
      return std::move(*this);
    }
    SS&& pop(size_t keep) {
      stack.pop(keep);
      return std::move(*this);
    }
    SS&& assign(Id id, PtrVal val) {
#ifdef LAZYALLOC
      do_allocs();
#endif
      stack.assign(id, val);
      return std::move(*this);
    }
    SS&& assign_seq(const std::vector<Id>& ids, std::vector<PtrVal> vals) {
      stack.assign_seq(ids, std::move(vals));
      return std::move(*this);
    }
    SS&& assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return assign_seq(
        std::vector<Id>(ids.begin(), ids.end()),
        std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& heap_append(const std::vector<PtrVal>& vals) {
      heap.append(vals);
      return std::move(*this);
    }
    SS&& heap_append(immer::flex_vector<PtrVal> vals) {
      return heap_append(std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& addPC(SExpr e) {
      pc.add(e);
      return std::move(*this);
    }
    SS&& addPCSet(const std::set<SExpr>& s) {
      pc.addSet(s);
      return std::move(*this);
    }
    SS&& addPCSet(const immer::set<SExpr>& s) {
      std::set cs(s.begin(), s.end());
      pc.addSet(cs);
      return std::move(*this);
    }
    SS&& addIncomingBlock(BlockLabel blabel) {
      bb = blabel;
      return std::move(*this);
    }
    SS&& init_arg(int len) {
      ASSERT(stack.mem_size() == 0, "Stack Not New");
      // FIXME: ptr size magic
      stack.alloc(17 + len + 1);
      stack.update(0, make_LocV(16, LocV::kStack));
      stack.update(8, make_LocV(17, LocV::kStack));
      stack.update(16, make_IntV(0));
      int arg_index = 17;
      for (int i = 0; i < len; i++) {
        stack.update(arg_index, make_SymV("ARG" + std::to_string(i)));
        arg_index++;
      }
      stack.update(arg_index, make_IntV(0));
      return std::move(*this);
    }
    const std::set<SExpr>& getPC() { return pc.getPC(); }
    // TODO temp solution
    PtrVal getVarargLoc() { return stack.getVarargLoc(); }
};

inline const Mem mt_mem = Mem(std::vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, std::vector<Frame>{});
inline const PC mt_pc = PC(std::set<SExpr>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

inline const immer::flex_vector<std::pair<SS, PtrVal>> mt_path_result =
  immer::flex_vector<std::pair<SS, PtrVal>>{};

#endif
