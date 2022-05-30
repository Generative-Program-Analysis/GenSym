package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.lmsx.smt.SMTBool

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

// Writing models of syscall/external functions is often painful and error-prone
// in a low-level language, and we don't want to maintain multiple version of those
// external/intrinsic functions with only slightly backend difference.
// Can we generate them from our Scala DSL?

@virtualize
trait GenExternal extends SymExeDefs {
  import FS._

  // TODO: sym_exit return type in C should be void
  def sym_exit[T: Manifest](ss: Rep[SS], args: Rep[List[Value]]): Rep[T] =
    "sym_exit".reflectWith[T](ss, args)

  // TODO: Utility functions
  // 1. object constructors, factories (e.g. new_stream, new_stat) 
  // Use the apply() method on classes as constructors/factories
  // <2022-05-12, David Deng> //
  def getString(ptr: Rep[Value], s: Rep[SS]): Rep[String] = "get_string".reflectWith[String](ptr, s)

  def open[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val ptr = args(0)
    val name: Rep[String] = getString(ptr, ss)
    val flags = args(1)
    /* TODO: handle different mode <2021-10-12, David Deng> */
    if (!fs.hasFile(name)) k(ss, fs, IntV(-1, 32))
    else {
      val fd: Rep[Fd] = fs.getFreshFd()
      val file = fs.getFile(name)
      fs.setStream(fd, Stream(file))
      k(ss, fs, IntV(fd, 32))
    }
  }

  def close[T: Manifest](fs: Rep[FS], args: Rep[List[Value]], k: (Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Fd] = args(0).int.toInt
    if (!fs.hasStream(fd)) k(fs, IntV(-1, 32))
    else {
      val strm = fs.getStream(fd)
      val name = strm.file.name
      if (fs.hasFile(name)) {
        // remove the stream associated with fd, write content to the actual file if the file still exists.
        // TODO: implement update in MapOpsOpt <2022-04-21, David Deng> //
        fs.setFile(name, strm.file)
        fs.removeStream(fd)
      }
      k(fs, IntV(0, 32))
    }
  }

  def read[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Int] = args(0).int.toInt
    val loc: Rep[Value] = args(1)
    val count: Rep[Int] = args(2).int.toInt
    if (!fs.hasStream(fd)) k(ss, fs, IntV(-1, 64))
    else {
      val strm = fs.getStream(fd)
      val content: Rep[List[Value]] = strm.read(count)
      // will update the cursor in strm
      // Thought: use a reference so that we don't have to set it back? 
      // But then we will have to manually make copies upon branches <2022-04-14, David Deng> //
      fs.setStream(fd, strm)
      val size = content.size
      val ss1 = ss.updateSeq(loc, content)
      k(ss1, fs, IntV(size, 64))
    }
  }

  def write[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Int] = args(0).int.toInt // NOTE: .int => Rep[Long], .toInt => Rep[Int]
    val buf: Rep[Value] = args(1)
    val count: Rep[Int] = args(2).int.toInt
    val content: Rep[List[Value]] = ss.lookupSeq(buf, count)
    if (!fs.hasStream(fd)) k(ss, fs, IntV(-1, 64))
    else {
      val strm = fs.getStream(fd)
      val size = strm.write(content, count)
      fs.setStream(fd, strm)
      k(ss, fs, IntV(size, 64))
    }
  }

  def lseek[T: Manifest](fs: Rep[FS], args: Rep[List[Value]], k: (Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Fd] = args(0).int.toInt
    val o: Rep[Long] = args(1).int
    val w: Rep[Int] = args(2).int.toInt
    val pos: Rep[Long] = fs.seekFile(fd, o, w)
    k(fs, IntV(pos, 64))
  }

  def stat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val ptr = args(0)
    val name: Rep[String] = getString(ptr, ss)
    val buf: Rep[Value] = args(1)
    if (!fs.hasFile(name)) {
      k(ss, fs, IntV(-1, 32))
    } else {
      val stat = fs.getFile(name).stat
      val ss1 = ss.updateSeq(buf, stat)
      k(ss1, fs, IntV(0, 32))
    }
  }

  // int mkdir(const char *pathname, mode_t mode);
  def mkdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    // TODO: set mode <2022-05-28, David Deng> //
    val mode: Rep[Value] = args(1)
    val name: Rep[String] = getPathSegments(path).last
    // TODO: set errno <2022-05-28, David Deng> //
    if (fs.hasFile(path)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: refactor a get_dir method? <2022-05-28, David Deng> //
      val f = _set_file_type(File(name, List[Value](), List.fill(144)(IntV(0, 8))), S_IFDIR)
      unchecked("/* mkdir: fs.setFile */")
      fs.setFile(path, f)
      unchecked("/* mkdir: return */")
      k(ss, fs, IntV(0, 32))
    }
  }

  // int rmdir(const char *pathname);
  def rmdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    val dir = fs.getFile(path)
    // TODO: set errno <2022-05-28, David Deng> //
    if (dir == NullPtr[File] || !_has_file_type(dir, S_IFDIR)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: first get the parent to optimize <2022-05-28, David Deng> //
      fs.removeFile(path)
      k(ss, fs, IntV(0, 32))
    }
  }

  // int creat(const char *pathname, mode_t mode);
  def creat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    // TODO: set mode <2022-05-28, David Deng> //
    val mode: Rep[Value] = args(1)
    val name: Rep[String] = getPathSegments(path).last
    // TODO: set errno <2022-05-28, David Deng> //
    if (fs.hasFile(path)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: refactor a get_dir method? <2022-05-28, David Deng> //
      val f = _set_file_type(File(name, List[Value](), List.fill(144)(IntV(0, 8))), S_IFREG)
      fs.setFile(path, f)
      k(ss, fs, IntV(0, 32))
    }
  }

  // int unlink(const char *pathname);
  def unlink[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    val file = fs.getFile(path)
    // TODO: set errno <2022-05-28, David Deng> //
    if (file == NullPtr[File] || !_has_file_type(file, S_IFREG)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: first get the parent to optimize <2022-05-28, David Deng> //
      fs.removeFile(path)
      k(ss, fs, IntV(0, 32))
    }
  }

  // int chmod(const char *pathname, mode_t mode);
  def chmod[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    val file = fs.getFile(path)
    val mode: Rep[Value] = args(1)
    // TODO: set errno <2022-05-28, David Deng> //
    if (file == NullPtr[File]) k(ss, fs, IntV(-1, 32))
    else {
      _set_file_mode(file, mode.int.toInt)
      k(ss, fs, IntV(0, 32))
    }
  }

  // int chown(const char *pathname, uid_t owner, gid_t group);
  def chown[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getString(args(0), ss)
    val file = fs.getFile(path)
    val owner: Rep[Value] = args(1)
    val group: Rep[Value] = args(2)
    // TODO: set errno <2022-05-28, David Deng> //
    if (file == NullPtr[File]) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: If the owner or group is specified as -1, then that ID is not changed. <2022-05-28, David Deng> //
      file.writeStatField("st_uid", owner)
      file.writeStatField("st_gid", group)
      k(ss, fs, IntV(0, 32))
    }
  }

  def openat[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    // TODO: implement this <2022-01-23, David Deng> //
    // int __fd_openat(int basefd, const char *pathname, int flags, mode_t mode);
    // if (fd == AT_FDCWD), call open
    k(ss, IntV(0, 32))
  }

  ////////////////////////
  //  helper functions  //
  ////////////////////////
  def flag(f: String, bw: Int): Rep[Value] = IntV(unchecked[Long](f), bw)

  def S_IFMT: Rep[Value] = flag("S_IFMT", 32)
  def NEG_S_IFMT: Rep[Value] = flag("~S_IFMT", 32)

  def _set_file(fs: Rep[FS], p: Rep[String], f: Rep[File]): Rep[FS] = {
    fs.setFile(p, f)
    fs
  }

  // mask values:
  // S_IFSOCK: 00140000
  // S_IFLNK: 00120000
  // S_IFREG: 00100000
  // S_IFBLK: 00060000
  // S_IFDIR: 00040000
  // S_IFCHR: 00020000
  // S_IFIFO: 00010000

  // NOTE: return type might not be necessary if using pointers <2022-05-27, David Deng> //
  def _set_file_type(f: Rep[File], mask: Rep[Int]): Rep[File] = {
    unchecked("/* _set_file_type */")
    // want to unset the file type bits and leave the other bits unchanged
    val clearMask: Rep[Value] = NEG_S_IFMT
    val stat = f.readStatField("st_mode")
    val newStat = IntV((stat.int & clearMask.int) | mask, 32)
    f.writeStatField("st_mode", newStat)
    f
  }

  def _set_file_mode(f: Rep[File], mask: Rep[Int]): Rep[File] = {
    unchecked("/* _set_file_mode */")
    // preserve the file type bits
    val clearMask: Rep[Value] = S_IFMT
    val stat = f.readStatField("st_mode")
    val newStat = IntV((stat.int & clearMask.int) | mask, 32)
    f.writeStatField("st_mode", newStat)
    f
  }

  // TODO: rename? <2022-05-28, David Deng> //
  def _has_file_type(f: Rep[File], mask: Rep[Int]): Rep[Boolean] = {
    val stat = f.readStatField("st_mode")
    stat.int & mask
  }

  // generate different return style
  def gen_k(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[Unit]) => Rep[Unit]): ((Rep[SS], Rep[List[Value]], Rep[Cont]) => Rep[Unit]) = { case (ss, l, k) =>
    gen(ss, l, { case (s,v) => k(s,v) })
  }

  def gen_p(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[List[(SS, Value)]]) => Rep[List[(SS, Value)]]): ((Rep[SS], Rep[List[Value]]) => Rep[List[(SS, Value)]]) = { case (ss, l) =>
    gen(ss, l, { case (s,v) => List[(SS, Value)]((s,v)) })
  }

  // bridge SS and FS
  def brg_fs[T: Manifest](f: (Rep[FS], Rep[List[Value]], ((Rep[FS], Rep[Value]) => Rep[T])) => Rep[T])
  (ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): (Rep[T]) = {
    def kp(fs: Rep[FS], ret: Rep[Value]): Rep[T] = {
      ss.setFs(fs)
      k(ss, ret)
    }
    f(ss.getFs, args, kp)
  }

  def brg_fs[T: Manifest](f: (Rep[SS], Rep[FS], Rep[List[Value]], ((Rep[SS], Rep[FS], Rep[Value]) => Rep[T])) => Rep[T])
  (ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): (Rep[T]) = {
    def kp(ss: Rep[SS], fs: Rep[FS], ret: Rep[Value]): Rep[T] = {
      ss.setFs(fs)
      k(ss, ret)
    }
    f(ss, ss.getFs, args, kp)
  }
}

trait ExternalUtil { self: BasicDefs with ValueDefs with SAIOps =>

  def equalExplicit[T: Manifest](lhs: Rep[T], rhs: Rep[T]): Rep[Boolean] = {
    val m = manifest[T]
    if (m == manifest[Value]) equalExplicit(lhs.asRepOf[Value].deref, rhs.asRepOf[Value].deref)
    else "==".reflectCtrlWith[Boolean](lhs, rhs)
  }

  @virtualize
  def assertEq[T: Manifest](lhs: Rep[T], rhs: Rep[T], msg: String = ""): Rep[Unit] = {
    unchecked[Unit]("/* assertEq */")
    val e: Rep[Boolean] = equalExplicit(lhs, rhs)
    assert(e, msg)
  }

  @virtualize
  def assertNeq[T: Manifest](lhs: Rep[T], rhs: Rep[T], msg: String = ""): Rep[Unit] = {
    unchecked[Unit]("/* assertNeq */")
    val e: Rep[Boolean] = equalExplicit(lhs, rhs)
    assert(!e, msg)
  }

  def assert(cond: Rep[Boolean], msg: String = ""): Rep[Unit] = {
    "assert".reflectCtrlWith[Unit](cond, unit(msg))
  }
}

class ExternalTestDriver(folder: String = "./headers/test") extends SAISnippet[Int, Unit] with SAIOps with GenExternal with ExternalUtil { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericLLSCCodeGen = new GenericLLSCCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Int, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      // g.show
      val ng = init(g)
      val src = run(name, ng)
      emitln(s"""#include <iostream>
        |#define PURE_STATE
        |#include "../llsc.hpp"
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

    val (pos, len) = statFieldMap("st_mode")
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
    val f = File("A")
    val f1 = _set_file_type(f, unchecked[Int]("S_IFREG"))
    assertEq(_has_file_type(f, unchecked[Int]("S_IFREG")), true, "file type should be correctly set")
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
    testEither
    testSetFileType
    // testSeek
    ()
  }
}

object GenerateExternalTest {
  def main(args: Array[String]): Unit = {
    val code = new ExternalTestDriver
    code.genAll
  }
}

class ExternalLLSCDriver(folder: String = "./headers/llsc") extends SAISnippet[Int, Unit] with SAIOps with GenExternal { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericLLSCCodeGen = new GenericLLSCCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Int, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      val ng = init(g)
      run(name, ng)
      emitln("/* LLSC - External utility functions and library modeling functions */")
      emitln("/* Generated by sai.llsc.TestGenerateExternal */")
      emitln("#ifndef LLSC_EXTERNAL_HEADERS_GEN")
      emitln("#define LLSC_EXTERNAL_HEADERS_GEN")
      emitFunctionDecls(stream)
      emitFunctions(stream)
      emitln("#endif // LLSC_EXTERNAL_HEADERS_GEN")
    }
  }

  def genHeader: Unit = {
    val mainStream = new PrintStream(s"$folder/external.hpp")
    val statics = Adapter.emitCommon1("header", codegen, mainStream)(manifest[Int], manifest[Unit])(x => Unwrap(wrapper(Wrap[Int](x))))
    mainStream.close
  }

  def snippet(u: Rep[Int]) = {
    // TODO: llsc_assert_k depends on sym_exit, which doesn't have a _k version right now <2022-01-23, David Deng> //
    // hardTopFun(gen_p(llsc_assert), "llsc_assert", "inline")
    // hardTopFun(gen_k(llsc_assert), "llsc_assert", "inline")
    hardTopFun(gen_p(brg_fs(open(_,_,_,_))), "syscall_open", "inline")
    hardTopFun(gen_k(brg_fs(open(_,_,_,_))), "syscall_open", "inline")
    hardTopFun(gen_p(brg_fs(close(_,_,_))), "syscall_close", "inline")
    hardTopFun(gen_k(brg_fs(close(_,_,_))), "syscall_close", "inline")
    hardTopFun(gen_p(brg_fs(read(_,_,_,_))), "syscall_read", "inline")
    hardTopFun(gen_k(brg_fs(read(_,_,_,_))), "syscall_read", "inline")
    hardTopFun(gen_p(brg_fs(write(_,_,_,_))), "syscall_write", "inline")
    hardTopFun(gen_k(brg_fs(write(_,_,_,_))), "syscall_write", "inline")
    hardTopFun(gen_p(brg_fs(lseek(_,_,_))), "syscall_lseek", "inline")
    hardTopFun(gen_k(brg_fs(lseek(_,_,_))), "syscall_lseek", "inline")
    hardTopFun(gen_p(brg_fs(stat(_,_,_,_))), "syscall_stat", "inline")
    hardTopFun(gen_k(brg_fs(stat(_,_,_,_))), "syscall_stat", "inline")
    hardTopFun(gen_p(brg_fs(mkdir(_,_,_,_))), "syscall_mkdir", "inline")
    hardTopFun(gen_k(brg_fs(mkdir(_,_,_,_))), "syscall_mkdir", "inline")
    hardTopFun(gen_p(brg_fs(rmdir(_,_,_,_))), "syscall_rmdir", "inline")
    hardTopFun(gen_k(brg_fs(rmdir(_,_,_,_))), "syscall_rmdir", "inline")
    hardTopFun(gen_p(brg_fs(creat(_,_,_,_))), "syscall_creat", "inline")
    hardTopFun(gen_k(brg_fs(creat(_,_,_,_))), "syscall_creat", "inline")
    hardTopFun(gen_p(brg_fs(unlink(_,_,_,_))), "syscall_unlink", "inline")
    hardTopFun(gen_k(brg_fs(unlink(_,_,_,_))), "syscall_unlink", "inline")
    hardTopFun(gen_p(brg_fs(chmod(_,_,_,_))), "syscall_chmod", "inline")
    hardTopFun(gen_k(brg_fs(chmod(_,_,_,_))), "syscall_chmod", "inline")
    hardTopFun(gen_p(brg_fs(chown(_,_,_,_))), "syscall_chown", "inline")
    hardTopFun(gen_k(brg_fs(chown(_,_,_,_))), "syscall_chown", "inline")
    hardTopFun(_set_file(_,_,_), "set_file", "inline")
    hardTopFun(_set_file_type(_,_), "set_file_type", "inline")
    hardTopFun(_has_file_type(_,_), "has_file_type", "inline")
    // hardTopFun(gen_p(openat), "openat", "inline")
    // hardTopFun(gen_k(openat), "openat", "inline")
    ()
  }
}

object GenerateExternal {
  def main(args: Array[String]): Unit = {
    val code = new ExternalLLSCDriver
    code.genHeader
  }
}
