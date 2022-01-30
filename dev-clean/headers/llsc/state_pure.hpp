#ifndef LLSC_STATE_PURE_HEADERS
#define LLSC_STATE_PURE_HEADERS

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout

template <class V, class M>
class PreMem {
  protected:
    immer::flex_vector<V> mem;
  public:
    PreMem(immer::flex_vector<V> mem) : mem(mem) {}
    size_t size() { return mem.size(); }
    V at(size_t idx, int size) { return mem.at(idx); }
    M update(size_t idx, V val) {
      ASSERT(idx < mem.size(), "PreMem update index out of bound");
      return M(mem.set(idx, val));
    }
    M append(V val) { return M(mem.push_back(val)); }
    M append(V val, size_t padding) {
      size_t idx = mem.size();
      return M(alloc(padding + 1).update(idx, val));
    }
    M append(immer::flex_vector<V> vs) { return M(mem + vs); }
    M alloc(size_t size) {
      auto m = mem.transient();
      for (int i = 0; i < size; i++) { m.push_back(nullptr); }
      return M(m.persistent());
    }
    M take(size_t keep) { return M(mem.take(keep)); }
    M drop(size_t d) { return M(mem.drop(d)); }
    immer::flex_vector<V> getMem() { return mem; }
};

class Mem: public PreMem<PtrVal, Mem> {
  class IterVals {
    immer::flex_vector<PtrVal> mem;
    size_t begin0, end0, idx2;
    bool first;

  public:
    IterVals(immer::flex_vector<PtrVal> m, size_t idx, int size)
      : mem(m), begin0(idx), end0(idx + size), first(true) { }
    
    std::optional<std::tuple<size_t, PtrVal, size_t>> next() {
      // assumptions:
      //   1. values do not overlap
      //   2. mem is sufficiently long
      size_t idx; PtrVal ret;
      if (first) {
        first = false;
        for (idx = begin0; idx <= begin0 && !(ret = mem.at(idx)); idx--);
        if (ret && (idx2 = idx + ret->get_bw() / 8) > begin0)
          return std::tuple(idx, ret, idx2);
        else idx2 = begin0;
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
  Mem(immer::flex_vector<PtrVal> mem) : PreMem(mem) { }

  PtrVal at(size_t begin_req, int size_req) {
    if (size_req == -1) return PreMem::at(begin_req, size_req);
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

  Mem update(size_t begin_orig, PtrVal v_orig) {
    if (!v_orig) {  // memcpy cases
      assert(!mem.at(begin_orig));
      return PreMem::update(begin_orig, v_orig);
    }
    auto mem = this->mem;
    size_t size_orig = v_orig->get_bw()/8, end_orig = begin_orig + size_orig;
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
      mem = mem.set(begin_cur, v_new);
    } while (tmp = iter.next());
    return Mem(mem);
  }
};

class Frame {
  public:
    using Env = immer::map<Id, PtrVal>;
  private:
    Env env;
  public:
    Frame(Env env) : env(env) {}
    Frame() : env(immer::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame assign(Id id, PtrVal v) const { return Frame(env.insert({id, v})); }
    Frame assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) const {
      Env env1 = env;
      for (size_t i = 0; i < ids.size(); i++) {
        env1 = env1.insert({ids.at(i), vals.at(i)});
      }
      return Frame(env1);
    }
};

class Stack {
  private:
    Mem mem;
    immer::flex_vector<Frame> env;
  public:
    Stack(Mem mem, immer::flex_vector<Frame> env) : mem(mem), env(env) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal getVarargLoc() { return env.at(env.size()-2).lookup_id(0); }
    Stack pop(size_t keep) { return Stack(mem.take(keep), env.take(env.size()-1)); }
    Stack push() { return Stack(mem, env.push_back(Frame())); }
    Stack push(Frame f) { return Stack(mem, env.push_back(f)); }

    Stack assign(Id id, PtrVal val) {
      return Stack(mem, env.update(env.size()-1, [&](auto f) { return f.assign(id, val); }));
    }
    Stack assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
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

    PtrVal at(size_t idx, int size) { return mem.at(idx, size); }
    PtrVal at_struct(size_t idx, int size) {
      return std::make_shared<StructV>(mem.take(idx + size).drop(idx).getMem());
    }
    Stack update(size_t idx, PtrVal val) { return Stack(mem.update(idx, val), env); }
    Stack alloc(size_t size) { return Stack(mem.alloc(size), env); }
};

class PC {
  private:
    immer::flex_vector<PtrVal> pc;
  public:
    PC(immer::flex_vector<PtrVal> pc) : pc(pc) {}
    PC add(PtrVal e) { return PC(pc.push_back(e)); }
    PC add_set(immer::flex_vector<PtrVal> new_pc) { return PC(pc + new_pc); }
    immer::flex_vector<PtrVal> get_path_conds() { return pc; }
    PtrVal get_last_cond() {
      if (pc.size() > 0) return pc.back();
      return nullptr;
    }
    void print() { print_vec(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
    FS fs;
  public:
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb) : heap(heap), stack(stack), pc(pc), bb(bb), fs(initial_fs) {}
    SS(Mem heap, Stack stack, PC pc, BlockLabel bb, FS fs) : heap(heap), stack(stack), pc(pc), bb(bb), fs(fs) {}
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
    size_t heap_size() { return heap.size(); }
    size_t stack_size() { return stack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    PtrVal at(PtrVal addr, int size = -1) {
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
    immer::flex_vector<PtrVal> at_seq(PtrVal addr, int count) {
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
    SS update_seq(PtrVal addr, immer::flex_vector<PtrVal> vals) {
      SS updated_ss = *this;
      for (int i = 0; i < vals.size(); i++) {
        updated_ss = updated_ss.update(make_LocV_inc(addr, i), vals.at(i));
      }
      return updated_ss;
    }
    SS push() { return SS(heap, stack.push(), pc, bb, fs); }
    SS pop(size_t keep) { return SS(heap, stack.pop(keep), pc, bb, fs); }
    SS assign(Id id, PtrVal val) { return SS(heap, stack.assign(id, val), pc, bb, fs); }
    SS assign_seq(immer::flex_vector<Id> ids, immer::flex_vector<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), pc, bb, fs);
    }
    SS heap_append(immer::flex_vector<PtrVal> vals) {
      return SS(heap.append(vals), stack, pc, bb, fs);
    }
    SS add_PC(PtrVal e) { return SS(heap, stack, pc.add(e), bb, fs); }
    SS add_PC_set(immer::flex_vector<PtrVal> s) { return SS(heap, stack, pc.add_set(s), bb, fs); }
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

inline const Mem mt_mem = Mem(immer::flex_vector<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, immer::flex_vector<Frame>{});
inline const PC mt_pc = PC(immer::flex_vector<PtrVal>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

inline const immer::flex_vector<std::pair<SS, PtrVal>> mt_path_result =
  immer::flex_vector<std::pair<SS, PtrVal>>{};

using func_t = immer::flex_vector<std::pair<SS, PtrVal>> (*)(SS, immer::flex_vector<PtrVal>);

inline PtrVal make_FunV(func_t f) {
  return std::make_shared<FunV<func_t>>(f);
}

inline immer::flex_vector<std::pair<SS, PtrVal>> direct_apply(PtrVal v, SS ss, immer::flex_vector<PtrVal> args) {
  auto f = std::dynamic_pointer_cast<FunV<func_t>>(v);
  if (f) return f->f(ss, args);
  ABORT("direct_apply: not applicable");
}

using func_cps_t = std::monostate (*)(SS, immer::flex_vector<PtrVal>, std::function<std::monostate(SS, PtrVal)>);

inline PtrVal make_CPSFunV(func_cps_t f) {
  return std::make_shared<CPSFunV<func_cps_t>>(f);
}

inline std::monostate cps_apply(PtrVal v, SS ss, immer::flex_vector<PtrVal> args, std::function<std::monostate(SS, PtrVal)> k) {
  auto f = std::dynamic_pointer_cast<CPSFunV<func_cps_t>>(v);
  if (f) return f->f(ss, args, k);
  ABORT("cps_apply: not applicable");
}

#endif
