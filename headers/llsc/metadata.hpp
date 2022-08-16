#ifndef LLSC_METADATA_HEADER
#define LLSC_METADATA_HEADER

#ifdef PURE_STATE
class MetaData: public Printable {
public:
    uint64_t ssid;
    BlockLabel bb;
    bool has_cover_new;
    List<SymObj> symbolics; //TODO rename
    List<PtrVal> cexprefers; // TODO rename

    MetaData(uint64_t ssid, BlockLabel bb, bool covernew, List<SymObj> symbolics, List<PtrVal> cexprefers) :
      ssid(ssid), bb(bb), has_cover_new(covernew), symbolics(symbolics), cexprefers(cexprefers) {}
    MetaData fork() { return MetaData(cov().new_ssid(), bb, false, symbolics, cexprefers); }
    // XXX(GW): what count_name does? just check existence?
    int count_name(const std::string& name) {
      for (auto symobj : symbolics) {
        if (symobj.name == name) return 1;
      }
      return 0;
    }

    MetaData add_incoming_block(BlockLabel blabel) {
      return MetaData(ssid, blabel, has_cover_new, symbolics, cexprefers);
    }
    MetaData cover_block(BlockLabel new_bb) {
      bool is_covernew = cov().is_uncovered(new_bb);
      cov().inc_block(new_bb);
      return MetaData(ssid, bb, has_cover_new | is_covernew, symbolics, cexprefers);
    }
    MetaData add_symbolic(const std::string& name, int size, bool is_whole) {
      return MetaData(ssid, bb, has_cover_new, symbolics.push_back(SymObj(name, size, is_whole)), cexprefers); }
    MetaData add_cex(const PtrVal& cex) { return MetaData(ssid, bb, has_cover_new, symbolics, cexprefers.push_back(cex)); }
    std::string toString() const override {
      std::ostringstream ss;
      ss << "MetaData(" <<
        "ssid : " << ssid << ", " <<
        "bb : " << bb << ", " <<
        "has_cover_new: " << has_cover_new << ", " <<
        "symbolics : " << vec_to_string<SymObj>(symbolics) <<
        "cexprefers : " << vec_to_string<PtrVal>(cexprefers) <<
        ")";
      return ss.str();
    }
};

#endif

#ifdef IMPURE_STATE
class MetaData: public Printable {
  public:
    uint64_t ssid;
    BlockLabel bb;
    bool has_cover_new;
    List<SymObj> symbolics;
    List<PtrVal> cexprefers;

    MetaData(uint64_t ssid, BlockLabel bb, bool cover_new, List<SymObj> symbolics, List<PtrVal> cexprefers) :
      ssid(ssid), bb(bb), has_cover_new(cover_new), symbolics(symbolics), cexprefers(cexprefers) {}
    MetaData fork() {
      return MetaData(cov().new_ssid(), bb, false, symbolics, cexprefers);
    }
    int count_name(const std::string& name) {
      for (auto symobj : symbolics) {
        if (symobj.name == name) return 1;
      }
      return 0;
    }

    void add_incoming_block(BlockLabel blabel) { bb = blabel; }
    void cover_block(BlockLabel new_bb) {
      bool is_cover_new = cov().is_uncovered(new_bb);
      cov().inc_block(new_bb);
      has_cover_new = has_cover_new | is_cover_new;
    }
    void add_symbolic(const std::string& name, int size, bool is_whole) {
      symbolics = symbolics.push_back(SymObj(name, size, is_whole));
    }
    void add_cex(const PtrVal& cex) {
      cexprefers = cexprefers.push_back(cex);
    }
    std::string toString() const override {
      std::ostringstream ss;
      ss << "MetaData(" <<
        "ssid : " << ssid << ", " <<
        "bb : " << bb << ", " <<
        "covernew : " << has_cover_new << ", " <<
        "symbolics : " << vec_to_string<SymObj>(symbolics) <<
        "cexprefers : " << vec_to_string<PtrVal>(cexprefers) <<
        ")";
      return ss.str();
    }
};
#endif

#endif
