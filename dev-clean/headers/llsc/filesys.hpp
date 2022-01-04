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
    content = content.push_back(make_SymV(std::string("FILE_") + name + std::string("_BYTE_") + std::to_string(i)));
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
    Stream(const Stream &s):
      file(s.file),
      mode(s.mode),
      cursor(s.cursor) {}
    Stream(File file):
      file(file),
      mode(O_RDONLY),
      cursor(0) {}
    Stream(File file, int mode):
      file(file),
      mode(mode),
      cursor(0) {}
    Stream(File file, int mode, size_t cursor):
      file(file),
      mode(mode),
      cursor(cursor) {}

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
    size_t get_cursor() const { return cursor; }

    /* TODO: implement write, read
     * ssize_t write(const void *buf, size_t nbytes)
     * - write from the current cursor, update cursor
     * - support only concrete values
     * - can have another function write_sym to handle writing of symbolic values
     * pair<flex_vector<PtrVal>, ssize_t> read(size_t nbytes);
     * - read from the current cursor position, update cursor
     * <2021-11-15, David Deng> */
};

class FS {
  private:
    immer::map<Fd, Stream> opened_files;
    immer::map<std::string, File> files;
    Fd next_fd;

  public:
    FS(): next_fd(3) {
        // default initialize opened_files and files
        /* TODO: set up stdin and stdout using fd 1 and 2 <2021-11-03, David Deng> */
    }

    FS(const FS &fs):
      files(fs.files),
      opened_files(fs.opened_files),
      next_fd(3) {}

    FS(immer::map<Fd, Stream> opened_files,
        immer::map<std::string, File> files,
        status_t status,
        Fd next_fd,
        Fd last_opened_fd):
      opened_files(opened_files),
      files(files),
      next_fd(next_fd) {}

    /* Stream get_stream(Fd fd) { */
    /*   if (opened_files.find(fd) == nullptr) /1* Handle error here *1/ */
    /*     ASSERT(false, "cannot get stream that does not exist"); */
    /*   return opened_files.at(fd); */
    /* } */

    void add_file(File file) {
      ASSERT(!has_file(file.get_name()), "FS::add_file: File already exists");
      files = files.set(file.get_name(), file);
    }

    void remove_file(std::string name) {
      ASSERT(has_file(name), "FS::remove_file: File does not exist");
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
      opened_files = opened_files.set(next_fd, Stream(files.at(name)));
      return next_fd++;
    }

    int close_file(Fd fd) {
      /* TODO: set next_fd the lowest file descriptor? <2021-10-28, David Deng> */
      if (!has_stream(fd)) return -1;
      opened_files = opened_files.erase(fd);
      return 0;
    }

    /* TODO: implement read_file, write_file
     * what should the interface be? a simple wrapper around Stream's read and write?
     * <2021-11-15, David Deng> */
};

#endif
