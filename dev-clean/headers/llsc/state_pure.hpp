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
    M update(size_t idx, const V& val) {
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

/* Mem0 is the base memory model that assumes integer/symbolic values are
 * stored as sequences of bytes (following little endian), i.e. every IntV/SymV
 * in an instance of Mem0 has `bw` of 8.
 * LocV/FunV values are stored with additional ``shadow'' values, which don't
 * store information but simply serve as placeholders, prohibiting reading or
 * updating a fragment of a location/function value.
 * This version also disallows reading uninitialized values (represented by nullptr),
 * which might be overly constrained.
 */
class Mem0 : public PreMem<PtrVal, Mem0> {
private:
  bool is_well_formed() const {
    size_t i = 0;
    while (i < mem.size()) {
      auto v = mem.at(i);
      if (std::dynamic_pointer_cast<IntV>(v) ||
          std::dynamic_pointer_cast<SymV>(v)) {
        ASSERT(v->get_bw() <= 8, "Bitwidth too large");
        i += 1;
      } else if (std::dynamic_pointer_cast<LocV>(v)) {
      // FIXME: function value
      //std::dynamic_pointer_cast<FunV<>(v)) {
        for (int j = i+1; j <= i + 7; j++) {
          ASSERT(std::dynamic_pointer_cast<ShadowV>(mem.at(j)),
                 "Loc/Fun value does not properly shadow its region");
        }
        i += 7;
      } else if (auto fun_v = std::dynamic_pointer_cast<FloatV>(v)) {
        for (int j = i+1; j <= i + 3; j++) {
          ASSERT(std::dynamic_pointer_cast<ShadowV>(mem.at(j)),
                 "Float value does not properly shadow its region");
        }
        i += 3;
      } else if (v == nullptr) {
        std::cout << "Warning: nullptr at " << i << " of an Mem0\n";
        i += 1;
      } else {
        ABORT("Unknown value");
      }
    }
    return true;
  }
public:
  using PreMem::at;
  using PreMem::update;
  Mem0(List<PtrVal> mem) : PreMem(mem) {}
  PtrVal at(size_t idx, size_t byte_size) {
    auto val = mem.at(idx);
    if (std::dynamic_pointer_cast<IntV>(val) || std::dynamic_pointer_cast<SymV>(val))
      return Value::from_bytes(Vec::slice(mem, idx, byte_size));
    ASSERT(val != nullptr, "Reading an uninitialized (nullptr) value");
    ASSERT(!std::dynamic_pointer_cast<ShadowV>(val), "Reading a shadowed value");
    return val;
  }
  Mem0 update(size_t idx, const PtrVal& val, size_t byte_size) {
    ASSERT(!std::dynamic_pointer_cast<ShadowV>(mem.at(idx)), "Updating a shadowed value");
    ASSERT(val->get_byte_size() == byte_size, "Mismatched value and size to write");
    auto bytes = val->to_bytes();
    ASSERT(bytes.size() == byte_size, "Size of byte-representation of value not equal to argument byte_size");
    auto mem = this->mem.transient();
    for (size_t i = 0; i < byte_size; i++) { mem.set(idx+i, bytes.at(i)); }
    return Mem0(mem.persistent());
  }
};

/* MemIdxShadow only works with _indexed_ shadow values.
 */
class MemIdxShadow : public PreMem<PtrVal, MemIdxShadow> {
public:
  using PreMem::at;
  using PreMem::update;
  MemIdxShadow(List<PtrVal> mem) : PreMem(mem) {}
  PtrVal at(size_t idx, size_t byte_size) {
    auto val = mem.at(idx);
    if (std::dynamic_pointer_cast<IntV>(val) || std::dynamic_pointer_cast<SymV>(val)) {
      auto val_size = val->get_byte_size();
      if (val_size == byte_size) return val;
      if (val_size >  byte_size) return Value::from_bytes(val->to_bytes().take(byte_size));
      if (val_size <  byte_size) return Value::from_bytes_shadow(Vec::slice(mem, idx, byte_size));
    }
    if (auto sv = std::dynamic_pointer_cast<ShadowV>(val)) {
      auto src = mem.at(idx + sv->offset);
      ASSERT(!std::dynamic_pointer_cast<LocV>(src), "Reading shadow of a LocV"); // XXX function too
      auto src_bytes = src->to_bytes().drop(-sv->offset);
      if (src_bytes.size() == byte_size) return Value::from_bytes(std::move(src_bytes));
      if (src_bytes.size() >  byte_size) return Value::from_bytes(src_bytes.take(byte_size));
      if (src_bytes.size() <  byte_size)
        return Value::from_bytes_shadow(src_bytes + Vec::slice(mem, idx+src_bytes.size(), byte_size-src_bytes.size()));
    }
    ASSERT(val != nullptr, "Reading a nullptr value");
    return val;
  }
  MemIdxShadow update(size_t idx, const PtrVal& val, size_t byte_size) {
    ASSERT(val->get_byte_size() == byte_size, "Mismatched value and size to write: " << val->get_byte_size() << " vs " << byte_size);
    auto old_val = mem.at(idx);
    auto mem = this->mem.transient();
    if (auto sv = std::dynamic_pointer_cast<ShadowV>(old_val)) {
      auto src_idx = idx + sv->offset;
      auto src = mem.at(src_idx);
      auto src_bytes = src->to_bytes();
      // We don't need to write the whole byte seq of src, since the part of it will be overwritten anyway
      for (size_t i = 0; i < abs(sv->offset); i++) { mem.set(src_idx+i, src_bytes.at(i)); }
      for (size_t i = abs(sv->offset)+byte_size; i < src_bytes.size(); i++) { mem.set(src_idx+i, src_bytes.at(i)); }
    }
    auto bytes = val->to_bytes_shadow();
    for (size_t i = 0; i < byte_size; i++) {
      auto w = mem.at(idx + i);
      if (w && byte_size-i < w->get_byte_size()) {
        ASSERT(std::dynamic_pointer_cast<IntV>(w) || std::dynamic_pointer_cast<SymV>(w), "Overwriting a LocV or FunV");
        // Some value to be overwritten is extended beyond byte_size, needs to reify its shadowed value
        auto w_bytes = w->to_bytes();
        // We don't need to write the whole byte seq of w, since the left-hand side part of it will be overwritten anyway
        for (size_t j = byte_size-i; j < w_bytes.size(); j++) { mem.set(idx + i + j, w_bytes.at(j)); }
      }
      mem.set(idx + i, bytes.at(i));
    }
    return MemIdxShadow(mem.persistent());
  }
};

class MemShadow: public PreMem<PtrVal, MemShadow> {
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

  using ListTransient = List<PtrVal>::transient_type;
  static void write_back(ListTransient &mem, const Segment &seg, PtrVal v) {
    mem.set(seg.begin, v);
    if (!seg.val)
      for (size_t i = seg.begin + 1; i < seg.end; i++)
        mem.set(i, make_ShadowV());
  }

  static void possible_partial_undef(PtrVal &v) {
    assert(v);
  }

public:
  MemShadow(List<PtrVal> mem) : PreMem(mem) { }
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

  MemShadow update(size_t idx, const PtrVal& val, int size) {
    auto mem = this->mem.transient();
    Segment newval {val, idx, size_t(size)};
    if (is_intact(newval)) {
      for (idx = newval.begin; idx < newval.end; ) {
        auto curval = lookup(idx, newval.end - idx);
        auto v = (curval.begin == newval.begin) ? newval.val : make_ShadowV();
        write_back(mem, curval, v);
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
        write_back(mem, curval, v_new);
        idx = curval.end;
      }
    }
    return MemShadow(mem.persistent());
  }
};

//using Mem = MemIdxShadow;
using Mem = MemShadow;

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
    Frame assign(Id id, const PtrVal& v) const { return Frame(env.insert({id, v})); }
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

    Stack assign(Id id, const PtrVal& val) {
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
    Stack update(size_t idx, const PtrVal& val) { return Stack(mem.update(idx, val), env); }
    Stack update(size_t idx, const PtrVal& val, int size) { return Stack(mem.update(idx, val, size), env); }
    Stack alloc(size_t size) { return Stack(mem.alloc(size), env); }
};

class PC: public Printable {
  private:
    List<PtrVal> pc;
  public:
    PC(List<PtrVal> pc) : pc(pc) {}
    PC add(const PtrVal& e) { return PC(pc.push_back(e)); }
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
    PtrVal at(const PtrVal& addr) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l);
      return heap.at(loc->l);
    }
    PtrVal at(const PtrVal& addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at(loc->l, size);
      return heap.at(loc->l, size);
    }
    PtrVal at_struct(const PtrVal& addr, int size) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return stack.at_struct(loc->l, size);
      return std::make_shared<StructV>(heap.take(loc->l + size).drop(loc->l).getMem());
    }
    List<PtrVal> at_seq(const PtrVal& addr, int count) {
      auto s = std::dynamic_pointer_cast<StructV>(at_struct(addr, count));
      ASSERT(s, "failed to read struct");
      return s->fs;
    }
    PtrVal heap_lookup(size_t addr) { return heap.at(addr, -1); }
    BlockLabel incoming_block() { return bb; }
    SS alloc_stack(size_t size) { return SS(heap, stack.alloc(size), pc, bb, fs); }
    SS alloc_heap(size_t size) { return SS(heap.alloc(size), stack, pc, bb, fs); }
    SS update(const PtrVal& addr, const PtrVal& val) {
      auto loc = std::dynamic_pointer_cast<LocV>(addr);
      ASSERT(loc != nullptr, "Lookup an non-address value");
      if (loc->k == LocV::kStack) return SS(heap, stack.update(loc->l, val), pc, bb, fs);
      return SS(heap.update(loc->l, val), stack, pc, bb, fs);
    }
    SS update(const PtrVal& addr, const PtrVal& val, int size) {
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
    SS assign(Id id, const PtrVal& val) { return SS(heap, stack.assign(id, val), pc, bb, fs); }
    SS assign_seq(List<Id> ids, List<PtrVal> vals) {
      return SS(heap, stack.assign_seq(ids, vals), pc, bb, fs);
    }
    SS heap_append(List<PtrVal> vals) {
      return SS(heap.append(vals), stack, pc, bb, fs);
    }
    SS add_PC(const PtrVal& e) { return SS(heap, stack, pc.add(e), bb, fs); }
    SS add_PC_set(List<PtrVal> s) { return SS(heap, stack, pc.add_set(s), bb, fs); }
    SS add_incoming_block(BlockLabel blabel) { return SS(heap, stack, pc, blabel, fs); }
    SS init_arg() {
      ASSERT(stack.mem_size() == 0, "Stack is not new");
      // Todo: Can adapt argv to be located somewhere other than 0 as well.
      // Configure a global LocV pointing to it.

      SS updated_ss = *this;

      unsigned num_args = cli_argv.size();
      auto stack_ptr = stack.mem_size(); // top of the stack
      updated_ss = updated_ss.alloc_stack((num_args + 1) * 8); // allocate space for the array of pointers

      // copy each argument onto the stack, and update the pointers
      for (int i = 0; i < num_args; ++i) {
        auto arg = cli_argv.at(i);
        auto addr = updated_ss.stack_size(); // top of the stack
        updated_ss = updated_ss.alloc_stack(arg.size());
        updated_ss = updated_ss.update_seq(make_LocV(addr, LocV::kStack), arg); // copy the values to the newly allocated space
        updated_ss = updated_ss.update(make_LocV(stack_ptr + (8 * i), LocV::kStack), make_LocV(addr, LocV::kStack)); // copy the pointer value
      }
      updated_ss = updated_ss.update(make_LocV(stack_ptr + (8 * num_args), LocV::kStack), make_LocV_null()); // terminate the array of pointers
      return updated_ss;
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

inline List<SSVal> direct_apply(const PtrVal& v, const SS& ss, List<PtrVal> args) {
  auto f = std::dynamic_pointer_cast<FunV<func_t>>(v);
  if (f) return f->f(ss, args);
  ABORT("direct_apply: not applicable");
}

using func_cps_t = std::monostate (*)(SS, List<PtrVal>, std::function<std::monostate(SS, PtrVal)>);

inline PtrVal make_CPSFunV(func_cps_t f) {
  return std::make_shared<FunV<func_cps_t>>(f);
}

inline std::monostate cps_apply(const PtrVal& v, const SS& ss, List<PtrVal> args, std::function<std::monostate(SS, PtrVal)> k) {
  auto f = std::dynamic_pointer_cast<FunV<func_cps_t>>(v);
  if (f) return f->f(ss, args, k);
  ABORT("cps_apply: not applicable");
}

#endif
