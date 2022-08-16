#ifndef LLSC_METADATA_HEADER
#define LLSC_METADATA_HEADER

class MetaData: public Printable {
public:
    uint64_t ssid;
    BlockLabel bb;
    bool has_cover_new;
    List<SymObj> sym_objs;
    List<PtrVal> preferred_cex;

    MetaData(uint64_t ssid, BlockLabel bb, bool covernew, List<SymObj> sym_objs, List<PtrVal> preferred_cex) :
      ssid(ssid), bb(bb), has_cover_new(covernew), sym_objs(sym_objs), preferred_cex(preferred_cex) {}
    MetaData fork() { return MetaData(cov().new_ssid(), bb, false, sym_objs, preferred_cex); }
    // XXX(GW): what count_name does? just check existence?
    int count_name(const std::string& name) {
      for (auto symobj : sym_objs) {
        if (symobj.name == name) return 1;
      }
      return 0;
    }
    std::string toString() const override {
      std::ostringstream ss;
      ss << "MetaData(" <<
        "ssid : " << ssid << ", " <<
        "bb : " << bb << ", " <<
        "has_cover_new : " << has_cover_new << ", " <<
        "sym_objs : " << vec_to_string<SymObj>(sym_objs) <<
        "preferred_cex : " << vec_to_string<PtrVal>(preferred_cex) << ")";
      return ss.str();
    }

#ifdef PURE_STATE
    MetaData add_incoming_block(BlockLabel blabel) {
      return MetaData(ssid, blabel, has_cover_new, sym_objs, preferred_cex);
    }
    MetaData cover_block(BlockLabel new_bb) {
      bool is_covernew = cov().is_uncovered(new_bb);
      cov().inc_block(new_bb);
      return MetaData(ssid, bb, has_cover_new | is_covernew, sym_objs, preferred_cex);
    }
    MetaData add_symbolic(const std::string& name, int size, bool is_whole) {
      return MetaData(ssid, bb, has_cover_new, sym_objs.push_back(SymObj(name, size, is_whole)), preferred_cex); }
    MetaData add_cex(const PtrVal& cex) { return MetaData(ssid, bb, has_cover_new, sym_objs, preferred_cex.push_back(cex)); }
#endif
#ifdef IMPURE_STATE
    void add_incoming_block(BlockLabel blabel) { bb = blabel; }
    void cover_block(BlockLabel new_bb) {
      bool is_cover_new = cov().is_uncovered(new_bb);
      cov().inc_block(new_bb);
      has_cover_new = has_cover_new | is_cover_new;
    }
    void add_symbolic(const std::string& name, int size, bool is_whole) {
      sym_objs = sym_objs.push_back(SymObj(name, size, is_whole));
    }
    void add_cex(const PtrVal& cex) {
      preferred_cex = preferred_cex.push_back(cex);
    }
#endif
};

#endif
