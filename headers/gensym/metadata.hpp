#ifndef GS_METADATA_HEADER
#define GS_METADATA_HEADER

class MetaData: public Printable {
public:
    uint64_t ssid;
    BlockLabel bb;
    BlockLabel current_bb;
    bool has_cover_new;
    List<SymObj> sym_objs;
    List<PtrVal> preferred_cex;

    MetaData(uint64_t ssid, BlockLabel bb, bool covernew, List<SymObj> sym_objs, List<PtrVal> preferred_cex) :
      ssid(ssid), bb(bb), current_bb(unknown_block_id), has_cover_new(covernew),
      sym_objs(sym_objs), preferred_cex(preferred_cex) {}
    MetaData fork() {
      MetaData result(ss_fork(ssid), bb, false, sym_objs, preferred_cex);
      result.current_bb = current_bb;
      return result;
    }
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
        "current_bb : " << current_bb << ", " <<
        "has_cover_new : " << has_cover_new << ", " <<
        "sym_objs : " << vec_to_string<List, SymObj>(sym_objs) <<
        "preferred_cex : " << vec_to_string<List, PtrVal>(preferred_cex) << ")";
      return ss.str();
    }

    void add_incoming_block(BlockLabel blabel) { bb = blabel; }
    void cover_block(BlockLabel new_bb) {
      current_bb = new_bb;
      bool is_cover_new = cov().inc_block(new_bb);
      has_cover_new = has_cover_new | is_cover_new;
    }
    void add_symbolic(const std::string& name, int size, bool is_whole) {
      sym_objs = sym_objs.push_back(SymObj(name, size, is_whole));
    }
    void add_cex(const PtrVal& cex) {
      preferred_cex = preferred_cex.push_back(cex);
    }
};

#endif
