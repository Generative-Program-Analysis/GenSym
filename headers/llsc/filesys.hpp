#ifndef LLSC_FS_HEADER
#define LLSC_FS_HEADER
/* NOTE: FS var naming convention:
 * symfile content: fs_file_<filename>_x<n>. E.g. fs_file_A_x0 is the variable name for the first byte in file A
 * symfile stat:    fs_stat_<filename>_x<n>. E.g. fs_stat_A_x0 is the variable name for the first byte in file A's stat
 */

inline std::atomic<unsigned int> fs_var_name = 0;
inline int default_sym_file_size = 5;
extern const int stat_size;
extern const int statfs_size;

// return a list of PtrVal with the specified variable prefix
inline List<PtrVal> make_SymList(String prefix, int n) {
    TrList<PtrVal> res;
    for (int i = 0; i < n; i++) {
        res.push_back(make_SymV(prefix + "_x" + std::to_string(i), 8));
    }
    return res.persistent();
}


struct File: public Printable {
  String name;
  List<PtrVal> content;
  List<PtrVal> stat;
  // use transient? Need mutable map support in front end.
  immer::map<String, Ptr<File>> children;
  Ptr<File> parent;

  String toString() const override {
    std::ostringstream ss;
    ss << "File(name=" << name << ", content=[";
    for (auto ptrval: content) {
      ss << ptrval_to_string(ptrval) << ", ";
    }
    ss << "], stat=[";
    for (auto ptrval: stat) {
      /* ss << ptrval_to_string(ptrval) << ", "; */
    }
    ss << "], nchildren=" << children.size();
    ss << ", children=[";
    for (auto &p: children) {
      ss << p.first << ", ";
    }
    ss << "])";
    return ss.str();
  }

  File(String name, int size) : name(name) {
    this->content = make_SymList("fs_file_" + name, size);
    this->stat = make_SymList("fs_stat_" + name, stat_size);
  }

  File(String name, List<PtrVal> content) : name(name) {
    this->content = content;
    this->stat = make_SymList("fs_stat_" + name, stat_size);
  }

  File(String name, List<PtrVal> content, List<PtrVal> stat) : name(name) {
    this->content = content;
    this->stat = stat;
  }
  File(const File& f) = default;

  /* NOTE: should only manipulate File objects through pointers <2022-07-11, David Deng> */
  /* NOTE: parent and children will not be copied <2022-07-11, David Deng> */
  /* Q: usage? <2022-07-11, David Deng> */
  [[nodiscard]] inline static Ptr<File> shallow_copy(Ptr<File> f) {
    return std::make_shared<File>(f->name, f->content, f->stat);
  }
  [[nodiscard]] inline static Ptr<File> deep_copy(Ptr<File> f) {
    auto f2 = std::make_shared<File>(*f);
    immer::map_transient<String, Ptr<File>> children;
    for (auto &p: f->children) {
      auto child = deep_copy(p.second);
      child->parent = f2;
      children.set(p.first, child);
    }
    f2->children = children.persistent();
    return f2;
  }
};

// return a symbolic file with size bytes
inline Ptr<File> make_SymFile(String name, size_t size) {
  return std::make_shared<File>(name, size);
};


/* TODO: model the lowest file descriptor guarantee? */

// An opened file
struct Stream: public Printable {
  Ptr<File> file;
  int mode; // a combination of O_RDONLY, O_WRONLY, O_RDWR, etc.
  off_t cursor;

  String toString() const override {
    std::ostringstream ss;
    ss << "Stream(file=" << *file << ", mode=" << mode << ", cursor=" << cursor << ")";
    return ss.str();
  }

  Stream(Ptr<File> file, int mode=O_RDONLY, size_t cursor=0): file(file), mode(mode), cursor(cursor) {}
  Stream(const Stream &s) = default;

  // a shallow copy, only copy mode and cursor, but sharing the same underlying file
  [[nodiscard]] inline static Ptr<Stream> shallow_copy(Ptr<Stream> s) {
    return std::make_shared<Stream>(s->file, s->mode, s->cursor);
  }
};

struct FS: public Printable {
  immer::map<Fd, Ptr<Stream>> opened_files;
  /* TODO: implement directory structure
   * 1. change the string key to a fileId, similar to inode number
   * 2. add a root directory file, with fileId=0
   * <2022-02-08, David Deng> */
  Ptr<File> root_file;
  /* immer::map<String, Ptr<File>> files; */
  Fd next_fd;
  List<PtrVal> statfs;

  Fd get_fresh_fd() {
    /* TODO: traverse through opened files to find the lowest available fd <2022-01-25, David Deng> */
    return next_fd++;
  }

  void set_stdin(int size) {
    auto f = make_SymFile("@stdin", size);
    root_file->children = root_file->children.set("@stdin", f);
    opened_files = opened_files.set(0, std::make_shared<Stream>(f, O_RDONLY, 0));
  }

  String toString() const override {
    std::ostringstream ss;
    ss << "FS(nstreams=" << opened_files.size();
    ss << ", root_file=" << *root_file;
    ss << ", opened_files=[";
    for (auto pf: opened_files) {
      ss << pf.second << ", ";
    }
    ss << "])";
    return ss.str();
  }
  FS(const FS &fs) = default;
  FS() : next_fd(3), root_file(make_SymFile("/", 0)) {
    statfs = make_SymList("fs_statfs", statfs_size);
  }
  FS(immer::map<Fd, Ptr<Stream>> opened_files, Ptr<File> root_file) :
    opened_files(opened_files), root_file(root_file), next_fd(3) {
      statfs = make_SymList("fs_statfs", statfs_size);
    }
};

inline FS initial_fs;

#endif
