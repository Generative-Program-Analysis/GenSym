#ifndef LLSC_FS_HEADERS
#define LLSC_FS_HEADERS

/* TODO: Is the name field necessary? <2021-10-12, David Deng> */
class File {
  private:
    std::string name;
    immer::flex_vector<PtrVal> content;
  public:
    friend std::ostream& operator<<(std::ostream& os, const File& f) {
      os << "File(name=" << f.name << ", content=[" << std::endl;
      for (auto ptrval: f.content) {
        os << "\t" << *ptrval << "," << std::endl;
      }
      os << "])";
      return os;
    }
    File(std::string name): name(name) {}
    File(std::string name, immer::flex_vector<PtrVal> content): name(name), content(content) {}
    File(const File& f): name(f.name), content(f.content) {}

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
inline File make_SymFile(std::string name, size_t size) {
  immer::flex_vector<PtrVal> content;
  for (int i = 0; i < size; i++) {
    content = content.push_back(make_SymV(std::string("FILE_") + name + std::string("_BYTE_") + std::to_string(i), 8));
  }
  return File(name, content);
};

/* TODO: what is the rule about the lowest file descriptor guarantee?
 * How do we model that rule? Is there a usecase for that?
 * Use a particular data structure? <2021-10-12, David Deng> */

// An opened file
struct Stream {
  private:
    File file;
    int mode; // a combination of O_RDONLY, O_WRONLY, O_RDWR, etc.
    off_t cursor;
  public:
    friend std::ostream& operator<<(std::ostream& os, const Stream& s) {
      os << "Stream(name=" << s.get_name() << ", mode=" << s.mode << ", cursor=" << s.cursor << ")";
      return os;
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

class FS {
  private:
    immer::map<Fd, Stream> opened_files;
    immer::map<std::string, File> files;
    Fd next_fd;
    Fd get_fresh_fd() {
      /* TODO: traverse through opened files to find the lowest available fd <2022-01-25, David Deng> */
      return next_fd++;
    }

  public:
    friend std::ostream& operator<<(std::ostream& os, const FS& fs) {
      os << "FS(nfiles=" << fs.files.size() << ", nstreams=" << fs.opened_files.size() << std::endl;
      os <<"\tfiles:" << std::endl;
      for (auto pf: fs.files) {
        os << pf.second << std::endl << std::endl;
      }
      os <<"\topened_files:" << std::endl;
      for (auto pf: fs.opened_files) {
        os << pf.second << std::endl << std::endl;
      }
      os << ")";
      return os;
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

    void add_file(File file) {
      ASSERT(!has_file(file.get_name()), "FS::add_file: File already exists");
      files = files.set(file.get_name(), file);
    }

    void remove_file(std::string name) {
      ASSERT(has_file(name), "FS::remove_file: File does not exist");
      // NOTE: should behave correctly if the file is open, 
      // because the file in opened_files is not removed until it is close_file is called.
      files = files.erase(name);
    };

    inline bool has_file(std::string name) const {
      return files.find(name) != nullptr;
    }

    inline bool has_stream(Fd fd) const {
      return opened_files.find(fd) != nullptr;
    }

    Fd open_file(std::string name, int mode = O_RDONLY) {
      /* TODO: handle different mode <2021-10-12, David Deng> */
      if (!has_file(name)) return -1;
      Fd fd = get_fresh_fd();
      opened_files = opened_files.set(fd, Stream(get_file(name)));
      return fd;
    }

    int close_file(Fd fd) {
      // remove the stream associated with fd,
      // write content to the actual file if the file still exists.
      if (!has_stream(fd)) return -1;
      auto strm = get_stream(fd);
      auto name = strm.get_name();
      if (!has_file(name)) return 0;
      files = files.set(name, strm.get_file());
      opened_files = opened_files.erase(fd);
      return 0;
    }


    std::pair<immer::flex_vector<PtrVal>, ssize_t> read_file(Fd fd, size_t nbytes) {
      if (!has_stream(fd)) return std::make_pair(immer::flex_vector<PtrVal>{}, -1);
      auto strm = get_stream(fd);
      auto content = strm.read(nbytes);
      opened_files = opened_files.set(fd, strm);
      return std::make_pair(content, content.size());
    }

    ssize_t write_file(Fd fd, immer::flex_vector<PtrVal> content, size_t nbytes) {
      if (!has_stream(fd)) return -1;
      auto strm = get_stream(fd);
      auto written = strm.write(content, nbytes);
      opened_files = opened_files.set(fd, strm);
      return written;
    }

};

inline int default_sym_file_size = 5;
inline FS initial_fs;

#endif
