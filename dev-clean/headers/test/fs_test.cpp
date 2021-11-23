#include <iostream>
#include <assert.h>
#include "../llsc.hpp"

void test_file() {
  PtrVal intV_0 = make_IntV(0);
  PtrVal intV_1 = make_IntV(1);
  PtrVal intV_2 = make_IntV(2);
  PtrVal intV_3 = make_IntV(3);
  PtrVal intV_4 = make_IntV(4);
  PtrVal intV_5 = make_IntV(5);
  PtrVal intV_6 = make_IntV(6);
  PtrVal intV_7 = make_IntV(7);
  PtrVal intV_8 = make_IntV(8);
  PtrVal intV_9 = make_IntV(9);

  {
    // test read_at
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    ASSERT((f.read_at(0, 2) == immer::flex_vector<PtrVal>{intV_0, intV_1}), "read_at");
    ASSERT((f.read_at(1, 4) == immer::flex_vector<PtrVal>{intV_1, intV_2}), "read_at with more bytes");
    ASSERT((f.read_at(0, 0) == immer::flex_vector<PtrVal>{}), "read_at with no bytes");
  }
  {
    // test size
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    ASSERT(f.get_size() == 3, "size of non-empty file");
    ASSERT(File("B").get_size() == 0, "size of an empty file");
  }
  {
    // test make_SymFile
    File f = make_SymFile("A", 5);
    ASSERT(f.get_size() == 5, "make_SymFile returns file of correct size");
    /* std::cout << f << std::endl; */
  }
  {
    // test clear
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    f.clear();
    ASSERT((f.get_size() == 0), "clear should result in empty file");
  }
  {
    // test write_at_no_fill
    File f1 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f2 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f3 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});

    
    f1.write_at_no_fill(immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5}, 3);
    ASSERT((f1.get_content() ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4, intV_5}), 
        "write at the end of a file");

    f2.write_at_no_fill(immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5}, 2);
    ASSERT((f2.get_content() ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_3, intV_4, intV_5}), 
        "write at the middle of a file, exceeding the end");

    f3.write_at_no_fill(immer::flex_vector<PtrVal>{intV_4}, 1);
    ASSERT((f3.get_content() ==
          immer::flex_vector<PtrVal>{intV_0, intV_4, intV_2}), 
        "write at the middle of a file, not exceeding the end");

  }
  {
    // test write_at
    File f1 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f2 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f3 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});

    f1.write_at(immer::flex_vector<PtrVal>{intV_4}, 5, intV_0);
    ASSERT((f1.get_content() ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_0, intV_0, intV_4}), 
        "write after the end of the file, a hole should be created");

    f2.write_at(immer::flex_vector<PtrVal>{intV_4}, 3, intV_0);
    f3.write_at_no_fill(immer::flex_vector<PtrVal>{intV_4}, 3);
    ASSERT((f2.get_content() == f3.get_content()),
        "write_at and write_at_no_fill should behave the same when not writing after the end");
  }
}

void test_stream() {
  PtrVal intV_0 = make_IntV(0);
  PtrVal intV_1 = make_IntV(1);
  PtrVal intV_2 = make_IntV(2);

  File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
  Stream s = Stream(f);
  off_t pos;
  {
    ASSERT((s.get_cursor() == 0), "cursor should default to 0");
  }
  {
    // test seek
    Stream s1(s);
    pos = s1.seek_start(15);
    ASSERT(pos == 15, "seek start");

    Stream s2(s);
    pos = s2.seek_end(15);
    ASSERT(pos == 18, "seek end");

    Stream s3(s);
    pos = s3.seek_cur(7);
    pos = s3.seek_cur(8);
    ASSERT(pos == 15, "seek cursor");
  }
  {
    // test seek error
    
    Stream s1(s);
    pos = s1.seek_start(-1);
    ASSERT(pos == -1, "should set error");

    Stream s2(s);
    pos = s2.seek_cur(1);
    pos = s2.seek_cur(-2);
    ASSERT(pos == -1, "should set error");

    Stream s3(s);
    pos = s3.seek_end(-5);
    ASSERT(pos == -1, "should set error");
  }
}

void test_fs() {
  File file_a = make_SymFile("A", 5);
  File file_b = make_SymFile("B", 5);
  Fd fd;
  {
    // test add_file, remove_file
    FS fs;
    fs.add_file(file_a);
    ASSERT(fs.has_file(file_a.get_name()), "file_a is added");

    fs.add_file(file_b);
    ASSERT(fs.has_file(file_b.get_name()), "file_b is added");
    fs.remove_file(file_b.get_name());
    ASSERT(!fs.has_file(file_b.get_name()), "file_b is removed");
  }
  {
    // test open_file
    FS fs;
    fs.add_file(file_a);
    Fd fd_a;

    fd = fs.open_file(file_a.get_name());
    ASSERT((fd != -1), "open_file should return valid fd");
    ASSERT((fs.has_stream(fd)), "stream should be opened");
    fd_a = fd;

    fd = fs.open_file("non-existing-file");
    ASSERT((fd == -1), "open_file should return -1 on non-existing file name");

    // test close_file
    fd = fs.close_file(fd_a);
    ASSERT((fd == 0), "close_file should return 0");

    fd = fs.close_file(-999);
    ASSERT((fd == -1), "close_file should return -1 on non-existing file descriptor");
  }
}

int main() {
  test_file();
  test_stream();
  test_fs();
}