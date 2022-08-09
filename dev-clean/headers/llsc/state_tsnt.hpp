#ifndef LLSC_STATE_TRANSIENT_HEADERS
#define LLSC_STATE_TRANSIENT_HEADERS

/* Memory, stack, and symbolic state representation */

// Note (5/17): now using a byte-oriented layout
template <class V, class M>
class PreMem {
  protected:
    M&& move_this() { return std::move(*((M*)this)); }
    TrList<V> mem;
  public:
    PreMem(TrList<V> mem) : mem(std::move(mem)) {}
    //PreMem(const PreMem& m) : mem(((PreMem&)m).mem.persistent().transient()) {}
    size_t size() { return mem.size(); }
    V at(size_t idx) { return mem.at(idx); }
    M&& update(size_t idx, V val) {
      mem.set(idx, val);
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
    M&& append(TrList<V>& vs) {
      mem.append(vs);
      return move_this();
    }
    M&& alloc(size_t size) {
      mem.append(List<V>(size, nullptr).transient());
      return move_this();
    }
    M&& take(size_t keep) {
      mem.take(keep);
      return move_this();
    }
    M slice(size_t idx, size_t len) {
      // XXX: why not returning M&&?
      auto m = mem.persistent().take(idx + len).drop(idx);
      return M(m.transient());
    }
    // PreMem<V> drop(size_t d) { return PreMem<V>(mem.drop(d)); }
    TrList<V> get_mem() { return mem; }
    List<V> get_pmem() { return mem.persistent(); }
};

class Mem: public PreMem<PtrVal, Mem> {
  // endian-ness: https://stackoverflow.com/questions/46289636/z3-endian-ness-mixup-between-extract-and-concat
  static PtrVal q_extract(PtrVal v0, size_t b0, size_t b, size_t e, size_t e0) {
    return bv_extract(v0, (e - b0) * 8 - 1, (b - b0) * 8);
  }

  static PtrVal q_concat(PtrVal v1, PtrVal v2) {
    return bv_concat(v2, v1);
  }

  struct Segment {
    PtrVal val; size_t begin, size, end;
    Segment(PtrVal v, size_t b, size_t s): val(v), begin(b), size(s), end(b + s) { }
    // assume intersection; no checks
    Segment intersect(const Segment &rhs) const {
      size_t b = std::max(begin, rhs.begin), e = std::min(end, rhs.end);
      PtrVal v = (begin < b || e < end) ? q_extract(val, begin, b, e, end) : val;
      return {v, b, e - b};
    }
    Segment left_sub(const Segment &rhs) const {
      PtrVal v = (rhs.begin > begin) ? q_extract(val, begin, begin, rhs.begin, end) : nullptr;
      return {v, begin, rhs.begin};
    }
    Segment right_sub(const Segment &lhs) const {
      PtrVal v = (lhs.end < end) ? q_extract(val, begin, lhs.end, end, end) : nullptr;
      return {v, lhs.end, end};
    }
  };

  Segment lookup(size_t idx, size_t size) const {
    auto cur = mem.at(idx);
    if (!cur) {
      size_t sz = 1;
      while (sz < size && !mem.at(idx + sz)) sz++;
      return { cur, idx, sz };
    }
    while (std::dynamic_pointer_cast<ShadowV>(cur)) cur = mem.at(--idx);
    return { cur, idx, size_t(cur->get_bw() + 7) / 8 };
  }

  bool is_intact(const Segment &seg) const {
    for (size_t idx = seg.begin; idx < seg.end; ) {
      auto s = lookup(idx, seg.end - idx);
      if (s.begin < seg.begin || s.end > seg.end)
        return false;
      idx = s.end;
    }
    return true;
  }

  void write_back(const Segment &seg, PtrVal v) {
    mem.set(seg.begin, v);
    if (!seg.val)
      for (size_t i = seg.begin + 1; i < seg.end; i++)
    mem.set(i, make_ShadowV());
  }

  static void possible_partial_undef(PtrVal &v) {
    assert(v);
  }

public:
  Mem(TrList<PtrVal> mem) : PreMem(std::move(mem)) {}
  Mem(List<PtrVal> mem) : PreMem(std::move(mem.transient())) {}
  using PreMem::at;
  using PreMem::update;

  PtrVal at(size_t idx, int size) {
    auto first = lookup(idx, size);
    auto part = first.intersect({nullptr, idx, size_t(size)});
    auto cur = part.val;
    if (part.size < size) {
      auto next = at(idx + part.size, size - part.size);
      possible_partial_undef(cur);
      possible_partial_undef(next);
      cur = q_concat(cur, next);
    }
    return cur;
  }

  Mem&& update(size_t idx, PtrVal val, int size) {
    Segment newval {val, idx, size_t(size)};
    if (is_intact(newval)) {
      for (idx = newval.begin; idx < newval.end; ) {
        auto curval = lookup(idx, newval.end - idx);
        auto v = (curval.begin == newval.begin) ? newval.val : make_ShadowV();
        write_back(curval, v);
        idx = curval.end;
      }
    }
    else {
      for (idx = newval.begin; idx < newval.end; ) {
        // load current
        auto curval = lookup(idx, newval.end - idx);
        auto newcur = newval.intersect(curval);
        auto v_new = newcur.val;
        auto v_head = curval.left_sub(newcur).val;
        if (v_head) v_new = q_concat(v_head, v_new);
        auto v_tail = curval.right_sub(newcur).val;
        if (v_tail) v_new = q_concat(v_new, v_tail);
        // store & step
        write_back(curval, v_new);
        idx = curval.end;
      }
    }
    return move_this();
  }
};

class Frame {
  public:
    using Env = std::map<Id, PtrVal>;
    using Cont = std::function<std::monostate(SS&, PtrVal)>;
    Cont cont;
  private:
    Env env;
  public:
    Frame(Cont ct): cont(ct), env() {}
    Frame(Env env) : env(std::move(env)) {}
    Frame() : env(std::map<Id, PtrVal>{}) {}
    size_t size() { return env.size(); }
    PtrVal lookup_id(Id id) const { return env.at(id); }
    Frame&& assign(Id id, PtrVal v) {
      env.insert_or_assign(id, v);
      return std::move(*this);
    }
    Frame&& assign_seq(const List<Id>& ids, const List<PtrVal>& vals) {
      for (size_t i = 0; i < ids.size(); i++) {
        env.insert_or_assign(ids.at(i), vals.at(i));
      }
      return std::move(*this);
    }
};

class Stack {
  private:
    Mem mem;
    TrList<Frame> env;
    PtrVal errno_location;
  public:
    Stack(Mem mem, TrList<Frame> env, PtrVal errno_location) :
      mem(std::move(mem)), env(std::move(env)), errno_location(std::move(errno_location)) {}
  //Stack(const Stack& s) : mem(s.mem), env(((Stack&)s).env.persistent().transient()), errno_location(errno_location) {}
    size_t mem_size() { return mem.size(); }
    size_t frame_depth() { return env.size(); }
    PtrVal vararg_loc() { return env.at(env.size()-2).lookup_id(0); }
    Stack&& init_error_loc() {
      auto error_addr = mem.size();
      mem.alloc(8);
      mem.update(error_addr, make_IntV(0, 32), 4);
      errno_location = make_LocV(error_addr, LocV::kStack, 4);
      return std::move(*this);
    }
    PtrVal error_loc() { return errno_location; }
    typename Frame::Cont pop(size_t keep) {
      auto &it = env.at(env.size() - 1);
      auto ret = it.cont;
      mem.take(keep);
      env.take(env.size() - 1);
      return ret;
    }
    Stack&& push() {
      return push(Frame());
    }
    Stack&& push(Frame f) {
      env.push_back(std::move(f));
      return std::move(*this);
    }
    Stack&& push(std::function<std::monostate(SS&, PtrVal)> cont) {
      return push(Frame(cont));
    }

    Stack&& assign(Id id, PtrVal val) {
      env.update(env.size()-1, [&](auto f) { return f.assign(id, val); });
      return std::move(*this);
    }
    Stack&& assign_seq(const List<Id>& ids, List<PtrVal> vals) {
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
          vals = vals.take(id_size - 1).push_back(make_LocV(msize, LocV::kStack, mem.size() - msize));
        }
        env.update(env.size()-1, [&](auto f) { return f.assign_seq(ids, vals); });
      }
      return std::move(*this);
    }
    PtrVal lookup_id(Id id) { return env[env.size()-1].lookup_id(id); }

    PtrVal at(size_t idx) { return mem.at(idx); }
    PtrVal at(size_t idx, int size) { return mem.at(idx, size); }
    PtrVal at_struct(size_t idx, int size) {
      auto ret = make_simple<StructV>(mem.slice(idx, size).get_pmem());
      return hashconsing(ret);
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
    TrList<PtrVal> pc;
  public:
    PC(TrList<PtrVal> pc) : pc(std::move(pc)) {}
  //PC(const PC& pc) : pc(((PC&)pc).pc.persistent().transient()) {}
    PC&& add(PtrVal e) {
      pc.push_back(e);
      return std::move(*this);
    }
    PC&& add_set(const List<PtrVal>& new_pc) {
      for (auto& it : new_pc) {
        pc.push_back(it);
      }
      return std::move(*this);
    }
    PC&& pop_back() {
      pc.take(pc.size()-1);
      return std::move(*this);
    }
    const TrList<PtrVal>& get_path_conds() { return pc; }
    PtrVal get_last_cond() {
      if (pc.size() > 0) return pc[pc.size()-1];
      return nullptr;
    }
    PC&& replace_last_cond(PtrVal e) {
      if (pc.size() == 0) return std::move(*this);
      pc.set(pc.size()-1, e);
      return std::move(*this);
    }
    void print() { print_set(pc); }
};

class SS {
  private:
    Mem heap;
    Stack stack;
    PC pc;
    BlockLabel bb;
    FS fs;
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
      heap(std::move(heap)), stack(std::move(stack)), pc(std::move(pc)), bb(bb), fs(initial_fs) {}
    SS(List<PtrVal> heap, Stack stack, PC pc, BlockLabel bb) :
      heap(std::move(heap.transient())),
      stack(std::move(stack)), pc(std::move(pc)), bb(bb), fs(initial_fs)  {}
    SS copy() { return *this; }
    PtrVal env_lookup(Id id) { return stack.lookup_id(id); }
    size_t heap_size() { return heap.size(); }
    size_t stack_size() { return stack.mem_size(); }
    size_t fresh_stack_addr() { return stack_size(); }
    size_t frame_depth() { return frame_depth(); }
    //[[deprecated]]
    PtrVal at(PtrVal addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    PtrVal at(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      if (loc != nullptr) {
        if (loc->k == LocV::kStack) return stack.at(loc->l, size);
        return heap.at(loc->l, size);
      }
      // TODO GW: should refactor this piece of code, strive for readability and maintainability
      auto symloc = std::dynamic_pointer_cast<SymLocV>(addr);
      ASSERT(symloc != nullptr && symloc->size >= size, "Lookup an non-address value");
      std::vector<std::pair<PtrVal, int>> result;
      auto offsym = std::dynamic_pointer_cast<SymV>(symloc->off);
      ASSERT(offsym && (offsym->get_bw() == addr_index_bw), "Invalid sym offset");
      if (SymLocStrategy::one == symloc_strategy || SymLocStrategy::feasible == symloc_strategy) {
        int cnt_bound = -1;
        int cnt = 0;
        if (SymLocStrategy::one == symloc_strategy)
          cnt_bound = 1;
        auto low_cond = int_op_2(iOP::op_sge, offsym, make_IntV(0, addr_index_bw));
        auto high_cond = int_op_2(iOP::op_sle, offsym, make_IntV(symloc->size - size, addr_index_bw));
        auto pc2 = pc;
        pc2.add(low_cond).add(high_cond);
        auto res = get_sat_value(pc2, offsym);
        while (res.first) {
          cnt++;
          int offset_val = res.second;
          auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
          result.push_back(std::make_pair(t_cond, offset_val));
          if (cnt_bound == cnt)
            break;
          pc2.add(SymV::neg(t_cond));
          res = get_sat_value(pc2, offsym);
        }
        ASSERT(cnt > 0, "No satisfiable offset value");
      } else {
        ASSERT(SymLocStrategy::all == symloc_strategy, "Bad symloc strategy");
        for (int offset_val=0; offset_val <= (symloc->size - size); offset_val++) {
          auto t_cond = int_op_2(iOP::op_eq, offsym, make_IntV(offset_val, offsym->get_bw()));
          result.push_back(std::make_pair(t_cond, offset_val));
        }
      }
      PtrVal read_res = nullptr;
      for(auto it = result.rbegin(); it != result.rend(); ++it) {
        auto val = at(make_LocV(symloc->base, symloc->k, symloc->size, it->second), size);
        if (result.rbegin() == it) {
          read_res = val;
        } else {
          read_res = ite(it->first, val, read_res);
        }
      }
      ASSERT(read_res, "Bad result");
      // Todo: should we modify the pc to add the in-bound constraints
      return read_res;
    }
    PtrVal at_struct(PtrVal addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at_struct(loc->l, size);
      auto ret = make_simple<StructV>(heap.slice(loc->l, size).get_pmem());
      return hashconsing(ret);
    }
    List<PtrVal> at_seq(PtrVal addr, int count) {
      auto s = std::dynamic_pointer_cast<StructV>(at_struct(addr, count));
      ASSERT(s, "failed to read struct");
      return s->fs;
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
    //[[deprecated]]
    SS&& update(PtrVal addr, PtrVal val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        stack.update(loc->l, val);
      else
        heap.update(loc->l, val);
      return std::move(*this);
    }
    SS&& update(PtrVal addr, PtrVal val, size_t size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack)
        stack.update(loc->l, val, size);
      else
        heap.update(loc->l, val, size);
      return std::move(*this);
    }
    SS&& update_seq(PtrVal addr, List<PtrVal> vals) {
      for (int i = 0; i < vals.size(); i++) {
        update(addr + i, vals.at(i));
      }
      return std::move(*this);
    }
    SS&& push() {
      stack.push();
      return std::move(*this);
    }
    SS&& push(std::function<std::monostate(SS&, PtrVal)> cont) {
      stack.push(cont);
      return std::move(*this);
    }
    typename Frame::Cont pop(size_t keep) {
      return stack.pop(keep);
    }
    SS&& assign(Id id, PtrVal val) {
#ifdef LAZYALLOC
      do_allocs();
#endif
      stack.assign(id, val);
      return std::move(*this);
    }
    SS&& assign_seq(List<Id> ids, List<PtrVal> vals) {
      stack.assign_seq(std::move(ids), std::move(vals));
      return std::move(*this);
    }
    SS&& heap_append(TrList<PtrVal> vals) {
      heap.append(vals);
      return std::move(*this);
    }
    SS&& heap_append(List<PtrVal> vals) {
      return heap_append(vals.transient());
    }
    SS&& add_PC(PtrVal e) {
      pc.add(e);
      return std::move(*this);
    }
    SS&& add_PC_set(const List<PtrVal>& s) {
      pc.add_set(s);
      return std::move(*this);
    }
    SS&& add_incoming_block(BlockLabel blabel) {
      bb = blabel;
      return std::move(*this);
    }
    SS&& init_arg() {
      ASSERT(stack.mem_size() == 0, "Stack is not new");
      // Todo: Can adapt argv to be located somewhere other than 0 as well.
      // Configure a global LocV pointing to it.
      unsigned num_args = cli_argv.size();
      // allocate space for the array of pointers
      // with additional ternimating null for empty envp array
      // and an additional terminating null that uclibc seems to expect for the ELF header.
      // Todo: support non-empty envp
      auto stack_ptr = make_LocV(stack.mem_size(), LocV::kStack, (num_args + 3) * 8);
      alloc_stack((num_args + 3) * 8);

      // copy each argument onto the stack, and update the pointers
      for (int i = 0; i < num_args; ++i) {
        auto arg = cli_argv.at(i);
        auto addr = stack_size(); // top of the stack
        alloc_stack(arg.size());
        auto arg_ptr = make_LocV(addr, LocV::kStack, arg.size());
        update_seq(arg_ptr, arg); // copy the values to the newly allocated space
        update(stack_ptr + (8 * i), arg_ptr); // copy the pointer value
      }
      update(stack_ptr + (8 * num_args), make_LocV_null()); // terminate the array of pointers
      update(stack_ptr + (8 * (num_args + 1)), make_LocV_null()); // terminate the empty envp array
      update(stack_ptr + (8 * (num_args + 2)), make_LocV_null()); // additional terminating null that uclibc seems to expect for the ELF header
      return std::move(*this);
    }

    PC& get_PC() { return pc; }
    void set_PC(PC _pc) { pc = _pc; }
    const TrList<PtrVal>& get_path_conds() { return pc.get_path_conds(); }

    // TODO temp solution
    PtrVal vararg_loc() { return stack.vararg_loc(); }

    SS&& init_error_loc() {
      stack.init_error_loc();
      return std::move(*this);
    }
    PtrVal error_loc() { return stack.error_loc(); }

    void set_fs(FS new_fs) { fs = new_fs; }
    FS get_fs() { return fs; }
};

using SSVal = std::pair<SS, PtrVal>;

inline const Mem mt_mem = Mem(TrList<PtrVal>{});
inline const Stack mt_stack = Stack(mt_mem, TrList<Frame>{}, nullptr);
inline const PC mt_pc = PC(TrList<PtrVal>{});
inline const BlockLabel mt_bb = 0;
inline const SS mt_ss = SS(mt_mem, mt_stack, mt_pc, mt_bb);

inline const List<SSVal> mt_path_result = List<SSVal>{};

using func_t = List<SSVal> (*)(SS&, List<PtrVal>);

inline PtrVal make_FunV(func_t f) {
  auto ret = make_simple<FunV<func_t>>(f);
  return hashconsing(ret);
}

inline List<SSVal> direct_apply(PtrVal v, SS ss, List<PtrVal> args) {
  auto f = std::dynamic_pointer_cast<FunV<func_t>>(v);
  if (f) return f->f(ss, args);
  ABORT("direct_apply: not applicable");
}

using func_cps_t = std::monostate (*)(SS&, List<PtrVal>, std::function<std::monostate(SS&, PtrVal)>);

inline PtrVal make_CPSFunV(func_cps_t f) {
  auto ret = make_simple<FunV<func_cps_t>>(f);
  return hashconsing(ret);
}

inline std::monostate cps_apply(PtrVal v, SS ss, List<PtrVal> args, std::function<std::monostate(SS&, PtrVal)> k) {
  auto f = std::dynamic_pointer_cast<FunV<func_cps_t>>(v);
  if (f) return f->f(ss, args, k);
  ABORT("cps_apply: not applicable");
}

inline std::monostate cont_apply(std::function<std::monostate(SS&, PtrVal)> cont, SS& ss, PtrVal val) {
  return cont(ss, val);
}

#endif
