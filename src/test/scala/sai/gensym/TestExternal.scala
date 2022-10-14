package gensym

import sai.lang.llvm._
import sai.lang.llvm.IR._
import gensym.IRUtils._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

import sys.process._
import org.scalatest.FunSuite

class ExternalTestDriver(folder: String = "./headers/test") extends SAISnippet[Int, Unit]
    with SAIOps with GenExternal with ExternalUtil { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericGSCodeGen = new GenericGSCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Backend.Sym, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      // g.show
      val ng = init(g)
      val src = run(name, ng)
      emitln(s"""#include <iostream>
        |#define IMPURE_STATE
        |#include "../gensym.hpp"
        |
        |PtrVal intV_0 = make_IntV(0);
        |PtrVal intV_1 = make_IntV(1);
        |PtrVal intV_2 = make_IntV(2);
        |PtrVal intV_3 = make_IntV(3);
        |PtrVal intV_4 = make_IntV(4);
        |PtrVal intV_5 = make_IntV(5);
        |PtrVal intV_6 = make_IntV(6);
        |PtrVal intV_7 = make_IntV(7);
        |PtrVal intV_8 = make_IntV(8);
        |PtrVal intV_9 = make_IntV(9);
        """.stripMargin)
      emitFunctionDecls(stream)
      emitFunctions(stream)
      emit(src)
      emitln(s"""
        |int main(int argc, char *argv[]) {
          |  test(0);
          |  return 0;
        |} """.stripMargin)
    }
  }

  import FS._

  def iv(n: Int): Rep[Value] = cmacro[Value](s"intV_$n")

  def genAll: Unit = {
    val mainStream = new PrintStream(s"$folder/external_test.cpp")
    val statics = Adapter.emitCommon1("test", codegen, mainStream)(manifest[Int], manifest[Unit])(x => Unwrap(wrapper(Wrap[Int](x))))
    mainStream.close
  }

  def testReadAt: Rep[Unit] = {
    unchecked("/* test readAt */")
    val f = File("A", List(iv(0), iv(1), iv(2)))
    assertEq(f.readAt(unit(0), unit(2)), List(iv(0), iv(1)), "readAt")
    assertEq(f.readAt(unit(1), unit(4)), List(iv(1), iv(2)), "readAt with more bytes")
    assertEq(f.readAt(unit(0), unit(0)), List[Value](), "readAt with no bytes")
  }

  def testSize: Rep[Unit] = {
    unchecked("/* test size */")
    val f = File("A", List(iv(0), iv(1), iv(2)))
    assertEq(f.content.size, 3, "size of non-empty file")
    assertEq(File("B").content.size, 0, "size of an empty file")
  }

  def testMake_SymFile: Rep[Unit] = {
    unchecked("/* test make_SymFile */")
    // val f = makeSymFile("A", 5)
    // assertEq(f.content.size, 5, "make_SymFile returns file of correct size")
  }

  def testClear: Rep[Unit] = {
    unchecked("/* test clear */")
    val f = File("A", List(iv(0), iv(1), iv(2)))
    f.clear()
    assertEq(f.content.size, 0, "clear should result in empty file")
  }

  def testWriteAtNoFill: Rep[Unit] = {
    unchecked("/* test writeAtNoFill */")
    val f1 = File("A", List(iv(0), iv(1), iv(2)))
    val f2 = File("A", List(iv(0), iv(1), iv(2)))
    val f3 = File("A", List(iv(0), iv(1), iv(2)))

    f1.writeAtNoFill(List(iv(3), iv(4), iv(5)), unit(3))
    assertEq(f1.content,
      List(iv(0), iv(1), iv(2), iv(3), iv(4), iv(5)),
      "write at the end of a file")

    f2.writeAtNoFill(List(iv(3), iv(4), iv(5)), unit(2))
    assertEq(f2.content,
      List(iv(0), iv(1), iv(3), iv(4), iv(5)),
      "write at the middle of a file, exceeding the end")

    f3.writeAtNoFill(List(iv(4)), unit(1))
    assertEq(f3.content,
      List(iv(0), iv(4), iv(2)),
      "write at the middle of a file, not exceeding the end")

  }
  def testWriteAt: Rep[Unit] = {
    unchecked("/* test writeAt */")
    val f1 = File("A", List(iv(0), iv(1), iv(2)))
    val f2 = File("A", List(iv(0), iv(1), iv(2)))
    val f3 = File("A", List(iv(0), iv(1), iv(2)))

    f1.writeAt(List(iv(4)), unit(5), iv(0))
    assertEq(f1.content,
      List(iv(0), iv(1), iv(2), iv(0), iv(0), iv(4)),
      "write after the end of the file, a hole should be created")

    f2.writeAt(List(iv(4)), unit(3), iv(0))
    f3.writeAtNoFill(List(iv(4)), unit(3))
    assertEq(f2.content, f3.content,
      "writeAt and writeAtNoFill should behave the same when not writing after the end")
  }

  def testStream = {
    val f = File("A", List(iv(0), iv(1), iv(2)))
    val s = Stream(f)
    assertEq(s.cursor, 0, "cursor should default to 0")
  }

  def testReadStatField = {
    unchecked("/* testReadStatField */")
    val f = File("A")
    val st = Range(0, 120).toList
    f.stat = List(st.map(IntV(_, 8)): _*)
    val mode = f.readStatField("st_mode")

    val (pos, len) = StructCalc()(null).getFieldOffsetSize(StatType.types, getFieldIdx(statFields, "st_mode"))
    val modeAssert = "from-bytes".reflectWith[Value](List(st.drop(pos).take(len).map(IntV(_, 8)): _*))
    assertEq(mode, modeAssert, "testReadStatField")
  }

  def testWriteStatField = {
    unchecked("/* testWriteStatField */")
    val f = File("A")
    val st: List[Int] = StaticList.fill(120)(-1)
    f.stat = List(st.map(IntV(_, 8)): _*)
    val mode = IntV(0xdeadbeef, 32)
    f.writeStatField("st_mode", mode)
    assertEq(f.readStatField("st_mode"), mode, "testWriteStatField")
  }

  def testPtrDeref = {
    unchecked("/* testing ptrderef. deref shouldn't generate explicit 'any' typed variable */")
    val pv: Rep[Value] = IntV(3)
    unchecked(pv.deref)
    unchecked(pv.deref)
  }

  def testStringOps = {
    unchecked("/* test stringops */")
    val str: Rep[String] = "hello world"
    val str2: Rep[String] = "another phrase that is longer"
    val seg = str.split(" ")
    val seg2 = str2.split(" ")
    assertEq(seg.size, 2, "segment should have two elements")
    assertEq(seg2.size, 5, "segment should have five elements")
  }

  def testStreamCopy = {
    unchecked("/* test stream copy constructor */")
    val strm1 = Stream(File("A"), O_RDONLY, 1L) // offset 1
    val strm1ref = strm1
    val strm2 = Stream.copy(strm1)
    unchecked(strm1ref)
    strm1ref.cursor = 5L
    unchecked(strm1)
    assertEq(strm1.cursor, 5L, "strm1 should be updated")
    assertEq(strm2.cursor, 1L, "strm2 should not be updated")
  }

  def testDirStructure = {
    unchecked("/* test directory structure */")
    val fs = FS()
    fs.setFile("/a", File("a"))
    fs.setFile("/a/b", File("b"))
    fs.setFile("/a/b/c", File("c"))
    val f = fs.getFile("/a/b/c")
    assertNeq(f, NullPtr[Value], "file should exist")
  }

  def testFullPath = {
    unchecked("/* test full path */")
    val fs = FS()
    fs.setFile("/a", File("a"))
    fs.setFile("/a/b", File("b"))
    fs.setFile("/a/b/c", File("c"))
    val f = fs.getFile("/a/b/c")
    assertEq(f.fullPath, "/a/b/c", "full path should be correct")
  }

  def testEither = {
    // NOTE: becuase of optimization, currently they all generate assert(true) <2022-05-26, David Deng> //
    val v: Rep[Either[Int, String]] = Either.right[Int, String]("abcdef")
    unchecked("/* test isLeft */")
    unchecked(v.isLeft)
    assert(!v.isLeft, "Left value should not be set")
    unchecked("/* test isRight */")
    unchecked(v.isRight)
    assert(v.isRight, "Right value should not be set")
    unchecked("/* test get value */")
    assertEq(v.right.value, "abcdef", "Right value should be set")
    val s: Rep[String] = v.right.value
    assertEq(s, "abcdef", "assigning to a string should work")
  }

  def testSetFileType = {
    // TODO: come up with a way to assert on symbolic values <2022-09-30, David Deng> //
    // val f = File("A")
    // val f1 = _set_file_type(f, unchecked[Int]("S_IFREG"))
    // assertEq(_has_file_type(f, unchecked[Int]("S_IFREG")), true, "file type should be correctly set")
  }

  def testFsCopy = {
    unchecked("/* test fs copy */")
    val fs = FS()
    fs.setFile("/a", File("a"))
    fs.setFile("/a/b", File("b"))
    fs.setFile("/a/b/c", File("c", List(iv(0), iv(1), iv(2))))

    unchecked("/* make a copy */")
    val fs2 = FS.dcopy(fs)
    val f2 = fs2.getFile("/a/b/c")
    assertNeq(f2, NullPtr[Value], "file should exist in the copied file system")

    unchecked("/* modify a file in the copied file system */")
    f2.writeAtNoFill(List(iv(3), iv(4), iv(5)), unit(3))
    unchecked("/* check that the original file is not modified */")
    val f1 = fs.getFile("/a/b/c")
    assertEq(f1.content, List(iv(0), iv(1), iv(2)), "file should not be modified")

    unchecked("/* add a file in the original system */")
    fs.setFile("/a/b/d", File("d"))
    unchecked("/* check that the copied file system is not modified */")
    val f3 = fs2.getFile("/a/b/d")
    assertEq(f3, NullPtr[Value], "file should not be added to the copied file system")
  }

  def testFsCopyWithStream = {
    unchecked("/* test fs copy with stream */")
    val fs = FS()
    fs.setFile("/a", File("a"))
    fs.setFile("/a/b", File("b"))
    fs.setFile("/a/b/c", File("c", List(iv(0), iv(1), iv(2))))
    fs.setStream(3, Stream(fs.getFile("/a/b/c"), O_RDONLY, 1L))

    unchecked("/* make a copy */")
    val fs2 = FS.dcopy(fs)
    assertEq(fs2.hasStream(3), true, "stream should exist in the copied file system")
    val s = fs2.getStream(3)
    assertEq(s.file, fs2.getFile("/a/b/c"), "stream should point to the correct file")
    assertEq(s.cursor, 1L, "stream should point to the correct offset")
    assertEq(s.mode, O_RDONLY, "stream should be read-only")

    unchecked("/* modify a stream in the original file system */")
    val s2 = fs.getStream(3)
    s2.seekCur(unit(3))

    unchecked("/* check that the copied file system is not modified */")
    val s3 = fs2.getStream(3)
    assertEq(s3.cursor, 1L, "stream should still point to the old offset")

    unchecked("/* add a stream in the original system */")
    fs.setStream(4, Stream(fs.getFile("/a"), O_RDONLY, 0L))
    unchecked("/* check that the copied file system is not modified */")
    assertEq(fs2.hasStream(4), false, "stream should not exist in the copied file system")
  }

  // def testSeek: Rep[Unit] = {
  //   unchecked("/* test seek */")
  //   val s1 = Stream(File("A"), ListOps.fill(20)(iv(0)))
  //   val pos1 = s1.seekStart(15)
  //   assertEq(pos1, 15, "seek start")

  //   val s2 = Stream(File("A"), ListOps.fill(20)(iv(0)))
  //   val pos2 = s2.seekEnd(15)
  //   assertEq(pos2, 18, "seek end")

  //   val s3 = Stream(File("A"), ListOps.fill(20)(iv(0)))
  //   s3.seekCur(7)
  //   val pos3 = s3.seekCur(8)
  //   assertEq(pos3, 15, "seek cursor")
  // }

  // def testSeekError: Rep[Unit] = {
  //   unchecked("/* test seek error */")
  //   val s1 = Stream(s) //    Stream s1(s)
  //   pos = s1.seekStart(-1)
  //   assertEq(pos, -1, "should set error")

  //   val s2 = Stream(s) //    Stream s2(s)
  //   pos = s2.seekCur(1)
  //   pos = s2.seekCur(-2)
  //   assertEq(pos, -1, "should set error")

  //   val s3 = Stream(s) //    Stream s3(s)
  //   pos = s3.seekEnd(-5)
  //   assertEq(pos, -1, "should set error")

  // }
  // def testReadStream: Rep[Unit] = {
  //   unchecked("/* test read stream */")
  //   val f = File("A", List(iv(0), iv(1), iv(2), iv(3), iv(4)))
  //   val s1 = Stream(f) //    Stream s1(f)

  //   auto content = s1.read(3)
  //   assertEq(content, f.readAt(unit(0), unit(3)), "should read the first three bytes")

  //   content = s1.read(999)
  //   assertEq(content, f.readAt(unit(3), unit(2)), "should return the rest of the file")

  //   content = s1.read(999)
  //   assertEq(content, List(), "should return nothing")

  // }

  // def testWriteStream: Rep[Unit] = {
  //   unchecked("/* test write stream */")
  //   val f = File("A", List(iv(0), iv(1), iv(2), iv(3), iv(4)))
  //   val s1 = Stream(f) //    Stream s1(f)
  //   int nbytes

  //   nbytes = s1.write(List(iv(5), iv(6)), 5)
  //   assertEq(nbytes, 2, "should write two bytes")

  //   val s2 = Stream(f) //    Stream s2(f)
  //   nbytes = s2.write(List(iv(5), iv(6)), 1)
  //   assertEq(nbytes, 1, "should write one byte")

  //   val s3 = Stream(f) //    Stream s3(f)
  //   s3.write(List(iv(5), iv(6)), 2)
  //   assertEq(s3.seekCur(0), 2, "cursor should have advanced by two")
  //   s3.seekStart(0)
  //   assertEq(s3.read(5),
  //     List(iv(5), iv(6), iv(2), iv(3), iv(4)),
  //     "content should be updated")

  //   val s4 = Stream(f) //    Stream s4(f)
  //   s4.seekEnd(2)
  //   nbytes = s4.write(List(iv(5), iv(6)), 2)
  //   assertEq(nbytes, 2, "should have written two bytes")
  //   assertEq(s4.seekEnd(0), 9, "should have 9 bytes in total")
  //   s4.seekStart(0)
  //   assertEq(s4.read(999),
  //     List(iv(0), iv(1), iv(2), iv(3), iv(4), IntV0, IntV0, iv(5), iv(6)),
  //     "content should be updated")
  // }

  // def testStat = {
  //   Stat st1, st2

  //   unchecked("/* test initialization */")
  //   assertEq(st1.get_struct().size, Stat::total_size, "stat has correct default size")
  //   ASSERT(st1.get_struct().at(0) != nullptr, "stat should not be initialized to nullptr")

  //   unchecked("/* test read and write field */")
  //   auto field_name = Stat::st_rdev
  //   auto field_pos = Stat::field_pos_size.at(field_name).first
  //   auto field_size = Stat::field_pos_size.at(field_name).second
  //   st1.write_field(field_name, immer::flex_vector<PtrVal>(field_size, iv(0))); // write 0s to the field

  //   auto field_content = st1.read_field(field_name)
  //   assertEq(field_content.size, field_size, "the field being read should have the correct size")
  //   for (int i=0; i<field_size; i++) {
  //     assertEq(field_content.at(i), iv(0), "the field should have correct content")
  //   }

  //   unchecked("/* test copy construction */")
  //   st2 = st1
  //   for (int i=0; i<Stat::total_size; i++) {
  //     assertEq(st1.get_struct().at(i), st2.get_struct().at(i),
  //       "the two struct instance should have the same content")
  //   }
  // }

  // def test_dup_sketch {
  //   typedef std::shared_ptr<Stream> StreamRef;
  //   immer::map<Fd, StreamRef> opened_files;
  //   File f = File("A", immer::flex_vector<PtrVal>{intV_0, intV_1, intV_2});
  //   opened_files = opened_files.set(1, std::make_shared<Stream>(f, 0, 0));
  //   StreamRef strm = opened_files.at(1); // reference? copy?
  //   opened_files = opened_files.set(2, strm);
  //   strm->cursor = 2; // should update the reference
  //   std::cout << "opened_files.at(1): " << *opened_files.at(1) << std::endl;
  //   std::cout << "opened_files.at(2): " << *opened_files.at(2) << std::endl;
  // }

  def snippet(u: Rep[Int]) = {
    testReadAt
    testSize
    testClear
    testWriteAt
    testWriteAtNoFill
    testStream
    testReadStatField
    testWriteStatField
    testPtrDeref
    testStringOps
    testStreamCopy
    testDirStructure
    testFullPath
    testEither
    testSetFileType
    testFsCopy
    testFsCopyWithStream
    // testSeek
    ()
  }
}

class TestGenExternal extends FunSuite {
  def testUnit(path: String, name: String, j: Int = 4): Unit = {
    test(s"${name}_unit") {
      val dir = new java.io.File(path)
      val retMake = Process(s"make -j$j $name", dir).!
      assert(retMake == 0, "Make failed")
      val retTest = Process(s"./$name", dir).!
      assert(retTest == 0, s"Test $name failed")
    }
  }

  val code = new ExternalTestDriver
  code.genAll
  testUnit("./headers/test", "external_test")
}

