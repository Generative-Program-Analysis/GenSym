#ifndef LLSC_STATE_IMP_HEADERS
#define LLSC_STATE_IMP_HEADERS

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout
template <class V, class M>
class PreMem {
  protected:
    M&& move_this() { return std::move(*((M*)this)); }
    std::vector<V> mem;
  public:
    PreMem(std::vector<V> mem) : mem(std::move(mem)) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    M&& update(size_t idx, V val) {
      mem.at(idx) = val;
      return move_this();
    }
    M&& append(V val) {
      mem.push_back(val);
      return move_this();
    }
    M&& append(V val, size_t padding) {
      size_t idx = mem.size();
      return alloc(padding + 1).update(idx, val);
    }
    M&& append(const std::vector<V>& vs) {
      mem.insert(mem.end(), vs.begin(), vs.end());
      return move_this();
    }
    M&& alloc(size_t size) {
      mem.resize(mem.size() + size, nullptr);
      return move_this();
    }
    M&& take(size_t keep) {
      mem.resize(keep);
      return move_this();
    }
    M slice(size_t idx, size_t len) {
      auto off = mem.begin() + idx;
      return M(std::vector(off, off + len));
    }
    // PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    const std::vector<V>& getMem() { return mem; }
};

class Mem: public PreMem<PtrVal, Mem> {
  class IterVals {
    const std::vector<PtrVal>& mem;
    size_t begin0, end0, idx2;
    bool first;

  public:
    IterVals(const std::vector<PtrVal>& m, size_t idx, int size)
      : mem(m), begin0(idx), end0(idx + size), first(true) { }

    std::optional<std::tuple<size_t, PtrVal, size_t>> next() {
      // assumptions:
      //   1. values do not overlap
      //   2. mem is sufficiently long
      size_t idx; PtrVal ret;
      if (first) {
        first = false;
        for (idx = begin0; (ret = mem.at(idx)) == make_ShadowV(); idx--);
        if (idx < begin0) {
          idx2 = idx + ret->get_bw() / 8;
          return std::tuple(idx, ret, idx2);
        }
        idx2 = begin0;
      }
      if ((idx = idx2) < end0) {
        ret = mem.at(idx);
        if (ret) {
          return std::tuple(idx, ret, (idx2 = idx + ret->get_bw()/8));
        } else {
          for (idx2 = idx + 1; idx2 < end0 && !mem.at(idx2); idx2++);
          return std::tuple(idx, PtrVal(nullptr), idx2);
        }
      }
      return std::nullopt;
    }
  };

  inline PtrVal q_extract(PtrVal v, size_t end, size_t hi, size_t lo) const {
    return bv_extract(v, (end - hi) * 8 - 1, (end - lo) * 8);
  }

public:
  Mem(std::vector<PtrVal> mem) : PreMem(std::move(mem)) {}
  using PreMem::at;
  using PreMem::update;

  PtrVal at(size_t begin_req, int size_req) {
    IterVals iter(mem, begin_req, size_req);
    size_t end_req = begin_req + size_req;
    // first value
    size_t begin_cur, end_cur; PtrVal v_cur;
    std::tie(begin_cur, v_cur, end_cur) = *(iter.next());
    if (begin_cur == begin_req && end_cur == end_req) return v_cur;
    assert(v_cur);  // otherwise partially undefined
    if (begin_cur < begin_req || end_req < end_cur)
      v_cur = q_extract(v_cur, end_cur, begin_req, std::min(end_req, end_cur));
    // append more values
    PtrVal v_ret = std::move(v_cur);
    while (end_cur < end_req) {
      std::tie(begin_cur, v_cur, end_cur) = *(iter.next());
      assert(v_cur);  // otherwise partially undefined
      if (end_cur > end_req)
        v_cur = q_extract(v_cur, end_cur, begin_cur, end_req);
      v_ret = bv_concat(v_ret, v_cur);
    }
    return v_ret;
  }

  Mem&& update(size_t begin_orig, PtrVal v_orig, int size_orig) {
    size_t end_orig = begin_orig + size_orig;
    IterVals iter(mem, begin_orig, size_orig);
    auto tmp = iter.next();
    do {
      size_t begin_cur, end_cur; PtrVal v_cur;
      std::tie(begin_cur, v_cur, end_cur) = *tmp;
      // cut v_new from v_orig
      size_t begin_new = std::max(begin_orig, begin_cur);
      size_t end_new = std::min(end_orig, end_cur);
      PtrVal v_new = (begin_orig < begin_new || end_new < end_orig) ?
                      q_extract(v_orig, end_orig, begin_new, end_new) : v_orig;
      // prepend & append
      if (begin_cur < begin_new) {
        auto v_head = q_extract(v_cur, end_cur, begin_cur, begin_new);
        v_new = bv_concat(v_head, v_new);
      }
      if (end_new < end_cur) {
        auto v_tail = q_extract(v_cur, end_cur, end_new, end_cur);
        v_new = bv_concat(v_new, v_tail);
      }
      // store
      mem.at(begin_cur) = v_new;
      if (!v_cur) {
        for (size_t i = begin_cur + 1; i < end_cur; i++)
          mem.at(i) = make_ShadowV();
      }
    } while (tmp = iter.next());
    return move_this();
  }
};

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
    PtrVal at(size_t idx, int size) { return mem.at(idx, size); }
    PtrVal at_struct(size_t idx, int size) {
      return std::make_shared<StructV>(mem.slice(idx, size).getMem());
    }
    Stack&& update(size_t idx, PtrVal val) {
      mem.update(idx, val);
      return std::move(*this);
    }
    Stack&& update(size_t idx, PtrVal val, int size) {
      mem.update(idx, val, size);
      return std::move(*this);
    }
    Stack&& alloc(size_t size) {
      mem.alloc(size);
      return std::move(*this);
    }
};

class PC {
  private:
    std::vector<PtrVal> pc;
  public:
    PC(std::vector<PtrVal> pc) : pc(std::move(pc)) {}
    PC&& add(PtrVal e) {
      pc.push_back(e);
      return std::move(*this);
    }
    PC&& add_set(const std::set<PtrVal>& new_pc) {
      pc.insert(pc.end(), new_pc.begin(), new_pc.end());
      return std::move(*this);
    }
    PC&& add_set(const List<PtrVal>& new_pc) {
      pc.insert(pc.end(), new_pc.begin(), new_pc.end());
      return std::move(*this);
    }
    PC&& pop_back() {
      pc.pop_back();
      return std::move(*this);
    }
    const std::vector<PtrVal>& get_path_conds() { return pc; }
    PtrVal get_last_cond() {
      if (pc.size() > 0) return pc.back();
      return nullptr;
    }
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
    SS(List<PtrVal> heap, Stack stack, PC pc, BlockLabel bb) :
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
      return heap.at(loc->l, size);
    }
    PtrVal at_struct(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at_struct(loc->l, size);
      return std::make_shared<StructV>(heap.slice(loc->l, size).getMem());
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr, -1); }
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
    SS&& update(PtrVal addr, PtrVal val, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        stack.update(loc->l, val, size);
      else
        heap.update(loc->l, val, size);
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
    SS&& assign_seq(List<Id> ids, List<PtrVal> vals) {
      return assign_seq(
        std::vector<Id>(ids.begin(), ids.end()),
        std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& heap_append(const std::vector<PtrVal>& vals) {
      heap.append(vals);
      return std::move(*this);
    }
    SS&& heap_append(List<PtrVal> vals) {
      return heap_append(std::vector<PtrVal>(vals.begin(), vals.end()));
    }
    SS&& add_PC(PtrVal e) {
      pc.add(e);
      return std::move(*this);
    }
    SS&& add_PC_cet(const std::set<PtrVal>& s) {
      pc.add_set(s);
      return std::move(*this);
    }
    SS&& add_PC_set(const List<PtrVal>& s) {
      std::set cs(s.begin(), s.end());
      pc.add_set(cs);
      return std::move(*this);
    }
    SS&& add_incoming_block(BlockLabel blabel) {
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
    PC get_PC() { return pc; }
    void set_PC(PC _pc) { pc = _pc; }
    const std::vector<PtrVal>& get_path_conds() { return pc.get_path_conds(); }
    // TODO temp solution
    PtrVal getVarargLoc() { return stack.getVarargLoc(); }
};

using SSVal = std::pair<SS, PtrVal>;

inline const Mem mt_mem = Mem(std::vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, std::vector<Frame>{});
inline const PC mt_pc = PC(std::vector<PtrVal>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

inline const List<SSVal> mt_path_result = List<SSVal>{};

using func_t = List<SSVal> (*)(SS&, List<PtrVal>);

inline PtrVal make_FunV(func_t f) {
  return std::make_shared<FunV<func_t>>(f);
}

inline List<SSVal> direct_apply(PtrVal v, SS ss, List<PtrVal> args) {
  auto f = std::dynamic_pointer_cast<FunV<func_t>>(v);
  if (f) return f->f(ss, args);
  ABORT("direct_apply: not applicable");
}

using func_cps_t = std::monostate (*)(SS&, List<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);

inline PtrVal make_CPSFunV(func_cps_t f) {
  return std::make_shared<FunV<func_cps_t>>(f);
}

inline std::monostate cps_apply(PtrVal v, SS ss, List<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  auto f = std::dynamic_pointer_cast<FunV<func_cps_t>>(v);
  if (f) return f->f(ss, args, k);
  ABORT("cps_apply: not applicable");
}

#endif
