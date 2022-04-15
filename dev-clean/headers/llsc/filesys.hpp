#ifndef LLSC_FS_HEADERS
#define LLSC_FS_HEADERS

inline unsigned int fs_var_name = 0;
inline int default_sym_file_size = 5;
inline PtrVal make_SymV_fs(int bw = 8) {
  return make_SymV("fs_x" + std::to_string(fs_var_name++), bw);
}

class Stat: Printable {
  private:
    immer::flex_vector<PtrVal> content;
  public:
    /***************
    *  constants  *
    ***************/
    static const unsigned total_size = 144;
    enum st_field_t {
      st_dev,    // 8 bytes
      st_ino,    // 8 bytes
      st_nlink,  // 8 bytes
      st_mode,   // 4 bytes
      st_uid,    // 4 bytes
      st_gid,    // 4 bytes
      // pad     // 4 bytes
      st_rdev,   // 8 bytes
      st_size,   // 8 bytes
      st_blksi,  // 8 bytes
      st_block,  // 8 bytes
      st_atim,   // 16 bytes
      st_mtim,   // 16 bytes
      st_ctim,   // 16 bytes
      // NOTE: last 24 bytes reserved but not used.
    };
    inline static const std::map<st_field_t, std::pair<unsigned, unsigned>> field_pos_size = {
      {Stat::st_dev,   {0,   8  }},
      {Stat::st_ino,   {8,   8  }},
      {Stat::st_nlink, {16,  8  }},
      {Stat::st_mode,  {24,  4  }},
      {Stat::st_uid,   {28,  4  }},
      {Stat::st_gid,   {32,  4  }},
      // padding,      {36,  4  }},
      {Stat::st_rdev,  {40,  8  }},
      {Stat::st_size,  {48,  8  }},
      {Stat::st_blksi, {56,  8  }},
      {Stat::st_block, {64,  8  }},
      {Stat::st_atim,  {72,  16 }},
      {Stat::st_mtim,  {88,  16 }},
      {Stat::st_ctim,  {104, 16 }}
    };

    /*************
    *  methods  *
    *************/
    std::string toString() const override {
      return std::string("Stat()");
    }
    Stat(): content(total_size, nullptr) {
      // default initialize to 44 SymV values
      for (int i=0; i<total_size; i++) {
        content = content.set(i, make_SymV_fs());
      }
    }
    Stat(immer::flex_vector<PtrVal> c) {
      ASSERT(c.size() == total_size, "Stat initialized with wrong sized vector.");
      content = c;
    }
    Stat(const Stat& st): content(st.content) {}
    immer::flex_vector<PtrVal> get_struct() const {
      return content;
    }
    immer::flex_vector<PtrVal> read_field(st_field_t field) const {
      unsigned pos, size;
      std::tie(pos, size) = field_pos_size.at(field);
      return content.drop(pos).take(size);
    }
    void write_field(st_field_t field, immer::flex_vector<PtrVal> c) {
      unsigned pos, size;
      std::tie(pos, size) = field_pos_size.at(field);
      for (int i=0; i<size; i++) {
        content = content.set(pos+i, c.at(i));
      }
    }
};

class File: public Printable {
  private:
    std::string name;
    immer::flex_vector<PtrVal> content;
    Stat stat;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "File(name=" << name << ", content=[";
      for (auto ptrval: content) {
        ss << ptrval_to_string(ptrval) << ", ";
      }
      ss << "])";
      return ss.str();
    }
    File(std::string name): name(name) {}
    File(std::string name, immer::flex_vector<PtrVal> content): name(name), content(content) {}
    File(const File& f): name(f.name), content(f.content) {}

    Stat& get_stat() {
      return stat;
    }

    // if writing beyond the last byte, will simply append to the end without filling
    void write_at_no_fill(immer::flex_vector<PtrVal> new_content, size_t pos) {
      content = content.take(pos) + new_content + content.drop(pos + new_content.size());
    }
    void write_at(immer::flex_vector<PtrVal> new_content, size_t pos, PtrVal fill_val) {
      int fill_size = pos - content.size();
      if (fill_size > 0) {
        // fill the new values to reflect the actual pos
        content = content + immer::flex_vector(fill_size, fill_val);
      }
      write_at_no_fill(new_content, pos);
    }
    void append(immer::flex_vector<PtrVal> new_content) {
      write_at_no_fill(new_content, content.size());
    }
    void clear() {
      content = immer::flex_vector<PtrVal>();
    }
    size_t get_size() const {
      return content.size();
    }
    std::string get_name() const {
      return name;
    }
    immer::flex_vector<PtrVal> read_at(size_t pos, size_t length) const {
      return content.drop(pos).take(length);
    }
    immer::flex_vector<PtrVal> get_content() const {
      return read_at(0, content.size());
    }
};

// return a symbolic file with size bytes
/* TODO: add a global fileId counter, and a map from fileId to file instance
 * Purpose 1: For supporting directory structure
 * Purpose 2: To avoid potentially conflicting var names in sym values
 * <2022-02-07, David Deng> */
inline File make_SymFile(std::string name, size_t size) {
  immer::flex_vector<PtrVal> content;
  for (int i = 0; i < size; i++) {
    content = content.push_back(make_SymV_fs());
  }
  return File(name, content);
};

/* TODO: what is the rule about the lowest file descriptor guarantee?
 * How do we model that rule? Is there a usecase for that?
 * Use a particular data structure? <2021-10-12, David Deng> */

// An opened file
struct Stream: public Printable {
  private:
    File file;
    int mode; // a combination of O_RDONLY, O_WRONLY, O_RDWR, etc.
    off_t cursor;
  public:
    std::string toString() const override {
      std::ostringstream ss;
      ss << "Stream(name=" << get_name() << ", mode=" << mode << ", cursor=" << cursor << ")";
      return ss.str();
    }
    Stream(const Stream &s): file(s.file), mode(s.mode), cursor(s.cursor) {}
    Stream(File file): file(file), mode(O_RDONLY), cursor(0) {}
    Stream(File file, int mode): file(file), mode(mode), cursor(0) {}
    Stream(File file, int mode, size_t cursor): file(file), mode(mode), cursor(cursor) {}

    off_t seek_start(off_t offset) {
      if (offset < 0) return -1;
      cursor = offset;
      return cursor;
    }
    off_t seek_end(off_t offset) {
      off_t new_cursor = file.get_size() + offset;
      if (new_cursor < 0) return -1;
      cursor = new_cursor;
      return cursor;
    }
    off_t seek_cur(off_t offset) {
      off_t new_cursor = cursor + offset;
      if (new_cursor < 0) return -1;
      cursor = new_cursor;
      return cursor;
    }
    inline size_t get_cursor() const { return cursor; }

    inline std::string get_name() const { return file.get_name(); }

    // used for close. Return by value. Modification should be done through write and other interface function.
    inline File get_file() const { return file; }

    immer::flex_vector<PtrVal> read(size_t nbytes) {
      // read from current position, up to nbytes
      auto content = file.read_at(cursor, nbytes);
      cursor += content.size();
      return content;
    }

    ssize_t write(immer::flex_vector<PtrVal> content, size_t nbytes) {
      // write nbytes of buf into the stream, return the num of bytes written.
      // if cursor is beyond the end of file, IntV0 is filled for the hole.
      /* TODO: when file is opened with O_APPEND flag, write to the end <2022-01-25, David Deng> */
      auto new_content = content.take(nbytes);
      file.write_at(new_content, cursor, IntV0);
      cursor += new_content.size();
      return new_content.size();
    }
};

class FS: public Printable {
  private:
    immer::map<Fd, Stream> opened_files;
    /* TODO: implement directory structure
     * 1. change the string key to a fileId, similar to inode number 
     * 2. add a root directory file, with fileId=0
     * <2022-02-08, David Deng> */
    immer::map<std::string, File> files;
    Fd next_fd;

  public:
    Fd get_fresh_fd() {
      /* TODO: traverse through opened files to find the lowest available fd <2022-01-25, David Deng> */
      return next_fd++;
    }
    std::string toString() const override {
      std::ostringstream ss;
      ss << "FS(nfiles=" << files.size() << ", nstreams=" << opened_files.size() << ", files=[";
      for (auto pf: files) {
        ss << pf.second << ", ";
      }
      ss << "], opened_files=[";
      for (auto pf: opened_files) {
        ss << pf.second << ", ";
      }
      ss << "])";
      return ss.str();
    }
    FS() : next_fd(3) {
      // default initialize opened_files and files
      /* TODO: set up stdin and stdout using fd 1 and 2 <2021-11-03, David Deng> */
    }

    FS(const FS &fs) : files(fs.files), opened_files(fs.opened_files), next_fd(3) {}

    FS(immer::map<Fd, Stream> opened_files, immer::map<std::string, File> files, status_t status, Fd next_fd, Fd last_opened_fd) :
      opened_files(opened_files), files(files), next_fd(next_fd) {}

    inline File get_file(std::string name) {
      return files.at(name);
    }
    inline Stream get_stream(Fd fd) {
      return opened_files.at(fd);
    }
    inline void set_stream(Fd fd, Stream s) {
      opened_files = opened_files.set(fd, s);
    }

    void set_file(std::string name, File file) {
      files = files.set(name, file);
    }

    void remove_file(std::string name) {
      ASSERT(has_file(name), "FS::remove_file: File does not exist");
      // NOTE: should behave correctly if the file is open,
      // because the file in opened_files is not removed until it is close_file is called.
      files = files.erase(name);
    };

    void remove_stream(Fd fd) {
      ASSERT(has_stream(fd), "FS::remove_stream: stream does not exist");
      // NOTE: should behave correctly if the file is open,
      // because the file in opened_files is not removed until it is close_file is called.
      opened_files = opened_files.erase(fd);
    };

    inline bool has_file(std::string name) const {
      return files.find(name) != nullptr;
    }

    inline bool has_stream(Fd fd) const {
      return opened_files.find(fd) != nullptr;
    }

    std::pair<immer::flex_vector<PtrVal>, int> stat_file(std::string name) {
      if (!has_file(name)) return std::make_pair(immer::flex_vector<PtrVal>{}, -1);
      auto file = files.at(name);
      auto stat = file.get_stat();
      return std::make_pair(stat.get_struct(), 0);
    }

    off_t seek_file(Fd fd, off_t offset, int whence) {
      if (!has_stream(fd)) return -1;
      auto strm = get_stream(fd);
      off_t ret;
      switch (whence) {
        case SEEK_SET:
          ret = strm.seek_start(offset);
          break;
        case SEEK_CUR:
          ret = strm.seek_cur(offset);
          break;
        case SEEK_END:
          ret = strm.seek_end(offset);
          break;
        default:
          std::cout << "invalid whence flag: " << whence << std::endl;
          return -1;
      }
      /* TODO: use reference to get stream instead <2022-03-15, David Deng> */
      if (ret != -1) opened_files = opened_files.set(fd, strm);
      return ret;
    }
};

inline FS initial_fs;

#endif
