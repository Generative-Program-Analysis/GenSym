#include <iostream>
#include <assert.h>
#include "../gensym.hpp"

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

void test_file() {
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
    ASSERT(f.content.size() == 3, "size of non-empty file");
    ASSERT(File("B").content.size() == 0, "size of an empty file");
  }
  {
    // test make_SymFile
    File f = make_SymFile("A", 5);
    ASSERT(f.content.size() == 5, "make_SymFile returns file of correct size");
    /* std::cout << f << std::endl; */
  }
  {
    // test clear
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    f.clear();
    ASSERT((f.content.size() == 0), "clear should result in empty file");
  }
  {
    // test write_at_no_fill
    File f1 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f2 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f3 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});

    
    f1.write_at_no_fill(immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5}, 3);
    ASSERT((f1.content ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4, intV_5}), 
        "write at the end of a file");

    f2.write_at_no_fill(immer::flex_vector<PtrVal>{intV_3, intV_4, intV_5}, 2);
    ASSERT((f2.content ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_3, intV_4, intV_5}), 
        "write at the middle of a file, exceeding the end");

    f3.write_at_no_fill(immer::flex_vector<PtrVal>{intV_4}, 1);
    ASSERT((f3.content ==
          immer::flex_vector<PtrVal>{intV_0, intV_4, intV_2}), 
        "write at the middle of a file, not exceeding the end");

  }
  {
    // test write_at
    File f1 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f2 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
    File f3 = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});

    f1.write_at(immer::flex_vector<PtrVal>{intV_4}, 5, intV_0);
    ASSERT((f1.content ==
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_0, intV_0, intV_4}), 
        "write after the end of the file, a hole should be created");

    f2.write_at(immer::flex_vector<PtrVal>{intV_4}, 3, intV_0);
    f3.write_at_no_fill(immer::flex_vector<PtrVal>{intV_4}, 3);
    ASSERT((f2.content == f3.content),
        "write_at and write_at_no_fill should behave the same when not writing after the end");
  }
}

void test_dup_sketch() {
  typedef std::shared_ptr<Stream> StreamRef;
  immer::map<Fd, StreamRef> opened_files;
  File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
  opened_files = opened_files.set(1, std::make_shared<Stream>(f, 0, 0));
  StreamRef strm = opened_files.at(1); // reference? copy?
  opened_files = opened_files.set(2, strm);
  strm->cursor = 2; // should update the reference
  std::cout << "opened_files.at(1): " << *opened_files.at(1) << std::endl;
  std::cout << "opened_files.at(2): " << *opened_files.at(2) << std::endl;
}

void test_stream() {
  File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
  Stream s = Stream(f);
  off_t pos;
  {
    ASSERT((s.cursor == 0), "cursor should default to 0");
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
  {
    // test read stream
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4});
    Stream s1(f);

    auto content = s1.read(3);
    ASSERT(content == f.read_at(0, 3), "should read the first three bytes");

    content = s1.read(999);
    ASSERT(content == f.read_at(3, 2), "should return the rest of the file");

    content = s1.read(999);
    ASSERT(content == immer::flex_vector<PtrVal>{}, "should return nothing");
  }
  {
    // test write stream
    File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4});
    Stream s1(f);
    int nbytes;

    nbytes = s1.write(immer::flex_vector<PtrVal>{intV_5, intV_6}, 5);
    ASSERT(nbytes == 2, "should write two bytes");

    Stream s2(f);
    nbytes = s2.write(immer::flex_vector<PtrVal>{intV_5, intV_6}, 1);
    ASSERT(nbytes == 1, "should write one byte");
    
    Stream s3(f);
    s3.write(immer::flex_vector<PtrVal>{intV_5, intV_6}, 2);
    ASSERT((s3.seek_cur(0) == 2), "cursor should have advanced by two");
    s3.seek_start(0);
    ASSERT((s3.read(5) == 
          immer::flex_vector<PtrVal>{intV_5, intV_6, intV_2, intV_3, intV_4}), 
        "content should be updated");

    Stream s4(f);
    s4.seek_end(2);
    nbytes = s4.write(immer::flex_vector<PtrVal>{intV_5, intV_6}, 2);
    ASSERT(nbytes == 2, "should have written two bytes");
    ASSERT((s4.seek_end(0) == 9), "should have 9 bytes in total");
    s4.seek_start(0);
    ASSERT((s4.read(999) == 
          immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2, intV_3, intV_4, IntV0, IntV0, intV_5, intV_6}), 
        "content should be updated");
  }
}

void test_stat() {
  Stat st1, st2;

  // test initialization
  ASSERT((st1.get_struct().size() == Stat::total_size), "stat has correct default size");
  ASSERT((st1.get_struct().at(0) != nullptr), "stat should not be initialized to nullptr");

  // test read and write field
  auto field_name = Stat::st_rdev;
  auto field_pos = Stat::field_pos_size.at(field_name).first;
  auto field_size = Stat::field_pos_size.at(field_name).second;
  st1.write_field(field_name, immer::flex_vector<PtrVal>(field_size, intV_0)); // write 0s to the field

  auto field_content = st1.read_field(field_name);
  ASSERT((field_content.size() == field_size), "the field being read should have the correct size");
  for (int i=0; i<field_size; i++) {
    ASSERT((field_content.at(i) == intV_0), "the field should have correct content");
  }

  // test copy construction
  st2 = st1;
  for (int i=0; i<Stat::total_size; i++) {
    ASSERT((st1.get_struct().at(i) == st2.get_struct().at(i)), 
        "the two struct instance should have the same content");
  }
}

/* void test_fs() { */
/*   File file_a = make_SymFile("A", 5); */
/*   File file_b = make_SymFile("B", 5); */
/*   Fd fd; */
/*   { */
/*     // test add_file, remove_file */
/*     FS fs; */
/*     fs.add_file(file_a); */
/*     ASSERT(fs.has_file(file_a.get_name()), "file_a is added"); */

/*     fs.add_file(file_b); */
/*     ASSERT(fs.has_file(file_b.get_name()), "file_b is added"); */
/*     fs.remove_file(file_b.get_name()); */
/*     ASSERT(!fs.has_file(file_b.get_name()), "file_b is removed"); */
/*   } */
/*   { */
/*     // test open_file, write_file, read_file, close_file */
/*     FS fs; */
/*     fs.add_file(file_a); */
/*     Fd fd_a; */
/*     ssize_t ret; */

/*     fd = fs.open_file(file_a.get_name()); */
/*     ASSERT((fd != -1), "open_file should return valid fd"); */
/*     ASSERT((fs.has_stream(fd)), "stream should be opened"); */
/*     fd_a = fd; */

/*     fd = fs.open_file("non-existing-file"); */
/*     ASSERT((fd == -1), "open_file should return -1 on non-existing file name"); */

/*     ret = fs.write_file(fd_a, immer::flex_vector<PtrVal>{intV_0, intV_1}, 5); */
/*     ASSERT((ret == 2), "should return the correct number of bytes written"); */

/*     ret = fs.write_file(-999, immer::flex_vector<PtrVal>{intV_0, intV_1}, 5); */
/*     ASSERT((ret == -1), "write_file should return -1 on unopened file"); */

/*     auto retp = fs.read_file(fd_a, 999); */
/*     ret = retp.second; */
/*     ASSERT((ret == 3), "should have read the last 3 bytes"); */

/*     retp = fs.read_file(-999, 999); */
/*     ret = retp.second; */
/*     ASSERT((ret == -1), "read_file should return -1 on unopened file"); */
/*     /1* TODO: Check content when seek is implemented <2022-01-25, David Deng> *1/ */

/*     fd = fs.close_file(fd_a); */
/*     ASSERT((fd == 0), "close_file should return 0"); */

/*     fd = fs.close_file(-999); */
/*     ASSERT((fd == -1), "close_file should return -1 on non-existing file descriptor"); */
/*   } */
/* } */


int main() {
  /* test_file(); */
  /* test_stream(); */
  /* test_stat(); */
  test_dup_sketch();
}
