#ifndef LLSC_STATE_PURE_HEADERS
#define LLSC_STATE_PURE_HEADERS

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout

template <class V, class M>
class PreMem: public Printable {
  protected:
    List<V> mem;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "PreMem(";
      for (int i = 0; i < mem.size(); i++) {
        auto ptrval = mem.at(i);
        ss << i << ": " << ptrval_to_string(ptrval) << ", ";
      }
      ss << ")";
      return ss.str();
    }
    PreMem(List<V> mem) : mem(mem) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    M update(size_t idx, V val) {
      ASSERT(idx < mem.size(), "PreMem update index out of bound");
      return M(mem.set(idx, val));
    }
    M append(V val) { return M(mem.push_back(val)); }
    M append(V val, size_t padding) {
      size_t idx = mem.size();
      return M(alloc(padding + 1).update(idx, val));
    }
    M append(List<V> vs) { return M(mem + vs); }
    M alloc(size_t size) {
      auto m = mem.transient();
      for (int i = 0; i < size; i++) { m.push_back(nullptr); }
      return M(m.persistent());
    }
    M take(size_t keep) { return M(mem.take(keep)); }
    M drop(size_t d) { return M(mem.drop(d)); }
    List<V> getMem() { return mem; }
};

class Mem: public PreMem<PtrVal, Mem> {
  std::tuple<PtrVal, size_t, size_t> lookup(size_t idx, size_t size) {
    auto cur = mem.at(idx);
    if (!cur) {
      size_t sz = 1;
      while (sz < size && !mem.at(idx + sz)) sz++;
      return std::make_tuple(cur, idx, sz);
    }
    while (cur == make_ShadowV()) cur = mem.at(--idx);
    return std::make_tuple(cur, idx, (cur->get_bw() + 7) / 8);
  }

  inline void possible_partial_undef(PtrVal &v) const {
    assert(v);
  }

  // endian-ness: https://stackoverflow.com/questions/46289636/z3-endian-ness-mixup-between-extract-and-concat
  inline PtrVal q_concat(PtrVal v1, PtrVal v2) const {
    return bv_concat(v2, v1);
  }

  inline PtrVal q_extract(PtrVal v0, size_t begin0, size_t begin, size_t end, size_t end0) const {
    return bv_extract(v0, (end - begin0) * 8 - 1, (begin - begin0) * 8);
  }

public:
  Mem(List<PtrVal> mem) : PreMem(mem) { }
  using PreMem::at;
  using PreMem::update;

  PtrVal at(size_t idx, int size) {
    PtrVal cur; size_t cidx, csize;
    std::tie(cur, cidx, csize) = lookup(idx, size);
    if (cidx < idx || csize > size) {
      size_t size2 =  std::min<size_t>(size, cidx + csize - idx);
      cur = q_extract(cur, cidx, idx, idx + size2, cidx + csize);
      csize = size2;
    }
    if (csize < size) {
      auto next = at(idx + csize, size - csize);
      possible_partial_undef(cur);
      possible_partial_undef(next);
      cur = q_concat(cur, next);
    }
    return cur;
  }

  Mem update(size_t idx, PtrVal v_orig, int size) {
    auto mem = this->mem.transient();
    size_t begin_orig = idx, end_orig = idx + size;
    while (size > 0) {
      // load current
      size_t begin_cur, size_cur; PtrVal v_cur;
      std::tie(v_cur, begin_cur, size_cur) = lookup(idx, size);
      size_t end_cur = begin_cur + size_cur;
      // cut v_new from v_orig
      size_t begin_new = std::max(begin_orig, begin_cur);
      size_t end_new = std::min(end_orig, end_cur);
      PtrVal v_new = (begin_orig < begin_new || end_new < end_orig) ?
                      q_extract(v_orig, begin_orig, begin_new, end_new, end_orig) : v_orig;
      // prepend & append
      if (begin_cur < begin_new) {
        auto v_head = q_extract(v_cur, begin_cur, begin_cur, begin_new, end_cur);
        v_new = q_concat(v_head, v_new);
      }
      if (end_new < end_cur) {
        auto v_tail = q_extract(v_cur, begin_cur, end_new, end_cur, end_cur);
        v_new = q_concat(v_new, v_tail);
      }
      // store
      mem.set(begin_cur, v_new);
      if (!v_cur) {
        for (size_t i = begin_cur + 1; i < end_cur; i++)
          mem.set(i, make_ShadowV());
      }
      // step
      idx = end_new;
      size = end_orig - end_new;
    }
    return Mem(mem.persistent());
  }
};

template class PreMem<PtrVal, Mem>; // instantiate the class

class Frame: public Printable {
  public:
    using Env = immer::map<Id, PtrVal>;
  private:
    Env env;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "Frame(";
      for (auto p : env) {
        ss << p.first << ": " << ptrval_to_string(p.second) << ", ";
      }
      ss << ")";
      return ss.str();
    }
    Frame(Env env) : env(env) {}
    Frame() : env(immer::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame assign(Id id, PtrVal v) const { return Frame(env.insert({id, v})); }
    Frame assign_seq(List<Id> ids, List<PtrVal> vals) const {
      Env env1 = env;
      for (size_t i = 0; i < ids.size(); i++) {
        env1 = env1.insert({ids.at(i), vals.at(i)});
      }
      return Frame(env1);
    }
};

class Stack: public Printable {
  private:
    Mem mem;
    List<Frame> env;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "Stack(" <<
        "mem=" << mem << ", " <<
        "env=" << vec_to_string(env) <<
        ")";
      return ss.str();
    }
    Stack(Mem mem, List<Frame> env) : mem(mem), env(env) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    Stack pop(size_t keep) { return Stack(mem.take(keep), env.take(env.size()-1)); }
    Stack push() { return Stack(mem, env.push_back(Frame())); }
    Stack push(Frame f) { return Stack(mem, env.push_back(f)); }

    Stack assign(Id id, PtrVal val) {
      return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign(id, val); }));
    }
    Stack assign_seq(List<Id> ids, List<PtrVal> vals) {
      // varargs
      size_t id_size = ids.size();
      if (id_size == 0) return Stack(mem, env);
      if (ids.at(id_size - 1) == 0) {
        auto updated_mem = mem;
        for (size_t i = id_size - 1; i < vals.size(); i++) {
          // FIXME: magic value 8, as vararg is retrived from +8 address
          updated_mem = updated_mem.append(vals.at(i), 7);
        }
        if (updated_mem.size() == mem.size()) updated_mem = updated_mem.alloc(8);
        auto updated_vals = vals.take(id_size - 1).push_back(make_LocV(mem.size(), LocV::kStack));
        auto stack = Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
        return Stack(updated_mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, updated_vals); }));
      } else {
        return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, vals); }));
      }
    }
    PtrVal lookup_id(Id id) { return env.back().lookup_id(id); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    PtrVal at(size_t idx, int size) { return mem.at(idx, size); }
    PtrVal at_struct(size_t idx, int size) {
      return std::make_shared<StructV>(mem.take(idx + size).drop(idx).getMem());
    }
    Stack update(size_t idx, PtrVal val) { return Stack(mem.update(idx, val), env); }
    Stack update(size_t idx, PtrVal val, int size) { return Stack(mem.update(idx, val, size), env); }
    Stack alloc(size_t size) { return Stack(mem.alloc(size), env); }
};

class PC: public Printable {
  private:
    List<PtrVal> pc;
  public:
    PC(List<PtrVal> pc) : pc(pc) {}
    PC add(PtrVal e) { return PC(pc.push_back(e)); }
    PC add_set(List<PtrVal> new_pc) { return PC(pc + new_pc); }
    List<PtrVal> get_path_conds() { return pc; }
    PtrVal get_last_cond() {
      if (pc.size() > 0) return pc.back();
      return nullptr;
    }
    std::string toString() const override {
      std::ostringstream ss;
      ss << "PC(" << vec_to_string<PtrVal>(pc) << ")";
      return ss.str();
    }
};

class SS: public Printable {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
    FS fs;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "SS(" <<
        "stack => {{ " << stack << " }}, " <<
        "heap => {{ " << heap << " }}, " <<
        "pc => {{ " << pc << " }}, " <<
        "bb => {{ " << bb << " }}, " <<
        "fs => {{ " << fs << " }}, " <<
        ")";
      return ss.str();
    }
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) : heap(heap), stack(stack), pc(pc), bb(bb), fs(initial_fs) {}
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb, FS fs) : heap(heap), stack(stack), pc(pc), bb(bb), fs(fs) {}
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
      return std::make_shared<StructV>(heap.take(loc->l + size).drop(loc->l).getMem());
    }
    List<PtrVal> at_seq(PtrVal addr, int count) {
      auto s = std::dynamic_pointer_cast<StructV>(at_struct(addr, count));
      ASSERT(s, "failed to read struct");
      return s->fs;
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr, -1); }
    BlockLabel incoming_block() { return bb; }
    SS alloc_stack(size_t size) { return SS(heap, stack.alloc(size), pc, bb, fs); }
    SS alloc_heap(size_t size) { return SS(heap.alloc(size), stack, pc, bb, fs); }
    SS update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return SS(heap, stack.update(loc->l, val), pc, bb, fs);
      return SS(heap.update(loc->l, val), stack, pc, bb, fs);
    }
    SS update(PtrVal addr, PtrVal val, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return SS(heap, stack.update(loc->l, val, size), pc, bb, fs);
      return SS(heap.update(loc->l, val, size), stack, pc, bb, fs);
    }
    SS update_seq(PtrVal addr, List<PtrVal> vals) {
      SS updated_ss = *this;
      for (int i = 0; i < vals.size(); i++) {
        updated_ss = updated_ss.update(addr + i, vals.at(i));
      }
      return updated_ss;
    }
    SS push() { return SS(heap, stack.push(), pc, bb, fs); }
    SS pop(size_t keep) { return SS(heap, stack.pop(keep), pc, bb, fs); }
    SS assign(Id id, PtrVal val) { return SS(heap, stack.assign(id, val), pc, bb, fs); }
    SS assign_seq(List<Id> ids, List<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), pc, bb, fs);
    }
    SS heap_append(List<PtrVal> vals) {
      return SS(heap.append(vals), stack, pc, bb, fs);
    }
    SS add_PC(PtrVal e) { return SS(heap, stack, pc.add(e), bb, fs); }
    SS add_PC_set(List<PtrVal> s) { return SS(heap, stack, pc.add_set(s), bb, fs); }
    SS add_incoming_block(BlockLabel blabel) { return SS(heap, stack, pc, blabel, fs); }
    SS init_arg(int len) {
      ASSERT(stack.mem_size() == 0, "Stack Not New");
      // FIXME: ptr size magic
      auto res_stack = stack.alloc(17 + len + 1);
      res_stack = res_stack.update(0, make_LocV(16, LocV::kStack));
      res_stack = res_stack.update(8, make_LocV(17, LocV::kStack));
      res_stack = res_stack.update(16, make_IntV(0));
      int arg_index = 17;
      for (int i = 0; i < len; i++) {
        res_stack = res_stack.update(arg_index, make_SymV("ARG" + std::to_string(i)));
        arg_index++;
      }
      res_stack = res_stack.update(arg_index, make_IntV(0));
      return SS(heap, res_stack, pc, bb, fs);
    }
    PC get_PC() { return pc; }
    // TODO temp solution
    PtrVal getVarargLoc() { return stack.getVarargLoc(); }
    void set_fs(FS new_fs) { fs = new_fs; }
    FS get_fs() { return fs; }
};

using SSVal = std::pair<SS, PtrVal>;

inline const Mem mt_mem = Mem(List<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, List<Frame>{});
inline const PC mt_pc = PC(List<PtrVal>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);
inline const List<SSVal> mt_path_result = List<SSVal>{};

using func_t = List<SSVal> (*)(SS, List<PtrVal>);

inline PtrVal make_FunV(func_t f) {
  return std::make_shared<FunV<func_t>>(f);
}

inline List<SSVal> direct_apply(PtrVal v, SS ss, List<PtrVal> args) {
  auto f = std::dynamic_pointer_cast<FunV<func_t>>(v);
  if (f) return f->f(ss, args);
  ABORT("direct_apply: not applicable");
}

using func_cps_t = std::monostate (*)(SS, List<PtrVal>, std::function<std::monostate(SS, PtrVal)>);

inline PtrVal make_CPSFunV(func_cps_t f) {
  return std::make_shared<FunV<func_cps_t>>(f);
}

inline std::monostate cps_apply(PtrVal v, SS ss, List<PtrVal> args, std::function<std::monostate(SS, PtrVal)> k) {
  auto f = std::dynamic_pointer_cast<FunV<func_cps_t>>(v);
  if (f) return f->f(ss, args, k);
  ABORT("cps_apply: not applicable");
}

#endif
