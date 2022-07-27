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
  def getStringAt(ptr: Rep[Value], s: Rep[SS]): Rep[String] = "get_string_at".reflectWith[String](ptr, s)

  /* 
   * int open(const char *pathname, int flags);
   * int open(const char *pathname, int flags, mode_t mode);
   */
  def open[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val name: Rep[String] = getStringAt(args(0), ss)
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

  /*
   * int openat(int dirfd, const char *pathname, int flags);
   * int openat(int dirfd, const char *pathname, int flags, mode_t mode);
   */
  def openat[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    // TODO: implement this <2022-01-23, David Deng> //
    // int __fd_openat(int basefd, const char *pathname, int flags, mode_t mode);
    // if (fd == AT_FDCWD), call open
    k(ss, IntV(0, 32))
  }

  /* 
   * int close(int fd);
   */
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

  /*
   * ssize_t read(int fd, void *buf, size_t count);
   */
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

  /* 
   * ssize_t write(int fd, const void *buf, size_t count);
   */
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

  /*
   * off_t lseek(int fd, off_t offset, int whence);
   */
  def lseek[T: Manifest](fs: Rep[FS], args: Rep[List[Value]], k: (Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val fd: Rep[Fd] = args(0).int.toInt
    val o: Rep[Long] = args(1).int
    val w: Rep[Int] = args(2).int.toInt
    val pos: Rep[Long] = fs.seekFile(fd, o, w)
    k(fs, IntV(pos, 64))
  }

  /*
   * int stat(const char *pathname, struct stat *statbuf);
   */
  def stat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val ptr = args(0)
    val name: Rep[String] = getStringAt(ptr, ss)
    val buf: Rep[Value] = args(1)
    if (!fs.hasFile(name)) {
      k(ss, fs, IntV(-1, 32))
    } else {
      val stat = fs.getFile(name).stat
      val ss1 = ss.updateSeq(buf, stat)
      k(ss1, fs, IntV(0, 32))
    }
  }

  /*
   * int mkdir(const char *pathname, mode_t mode);
   */
  def mkdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
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

  /*
   * int rmdir(const char *pathname);
   */
  def rmdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
    val dir = fs.getFile(path)
    // TODO: set errno <2022-05-28, David Deng> //
    if (dir == NullPtr[File] || !_has_file_type(dir, S_IFDIR)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: first get the parent to optimize <2022-05-28, David Deng> //
      fs.removeFile(path)
      k(ss, fs, IntV(0, 32))
    }
  }

  /*
   * int creat(const char *pathname, mode_t mode);
   */
  def creat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
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

  /*
   * int unlink(const char *pathname);
   */
  def unlink[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
    val file = fs.getFile(path)
    // TODO: set errno <2022-05-28, David Deng> //
    if (file == NullPtr[File] || !_has_file_type(file, S_IFREG)) k(ss, fs, IntV(-1, 32))
    else {
      // TODO: first get the parent to optimize <2022-05-28, David Deng> //
      fs.removeFile(path)
      k(ss, fs, IntV(0, 32))
    }
  }

  /*
   * int chmod(const char *pathname, mode_t mode);
   */
  def chmod[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
    val file = fs.getFile(path)
    val mode: Rep[Value] = args(1)
    // TODO: set errno <2022-05-28, David Deng> //
    if (file == NullPtr[File]) k(ss, fs, IntV(-1, 32))
    else {
      _set_file_mode(file, mode.int.toInt)
      k(ss, fs, IntV(0, 32))
    }
  }

  /*
   * int chown(const char *pathname, uid_t owner, gid_t group);
   */
  def chown[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]): Rep[T] = {
    val path: Rep[String] = getStringAt(args(0), ss)
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
  def gen_k(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[Unit]) => Rep[Unit]):
      ((Rep[SS], Rep[List[Value]], Rep[Cont]) => Rep[Unit]) = { case (ss, l, k) =>
    gen(ss, l, { case (s,v) => k(s,v) })
  }

  def gen_p(gen: (Rep[SS], Rep[List[Value]], (Rep[SS], Rep[Value]) => Rep[List[(SS, Value)]]) => Rep[List[(SS, Value)]]): ((Rep[SS], Rep[List[Value]]) => Rep[List[(SS, Value)]]) = { case (ss, l) =>
    gen(ss, l, { case (s,v) => List[(SS, Value)]((s,v)) })
  }

  // bridge SS and FS
  def brg_fs[T: Manifest](f: (Rep[FS], Rep[List[Value]], ((Rep[FS], Rep[Value]) => Rep[T])) => Rep[T])
  (ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    def kp(fs: Rep[FS], ret: Rep[Value]): Rep[T] = {
      ss.setFs(fs)
      k(ss, ret)
    }
    f(ss.getFs, args, kp)
  }

  def brg_fs[T: Manifest](f: (Rep[SS], Rep[FS], Rep[List[Value]], ((Rep[SS], Rep[FS], Rep[Value]) => Rep[T])) => Rep[T])
  (ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    def kp(ss: Rep[SS], fs: Rep[FS], ret: Rep[Value]): Rep[T] = {
      ss.setFs(fs)
      k(ss, ret)
    }
    f(ss, ss.getFs, args, kp)
  }

}

@virtualize
trait ExternalUtil { self: BasicDefs with ValueDefs with SAIOps =>

  def equalExplicit[T: Manifest](lhs: Rep[T], rhs: Rep[T]): Rep[Boolean] = {
    val m = manifest[T]
    if (m == manifest[Value]) equalExplicit(lhs.asRepOf[Value].deref, rhs.asRepOf[Value].deref)
    else "==".reflectCtrlWith[Boolean](lhs, rhs)
  }

  def assertEq[T: Manifest](lhs: Rep[T], rhs: Rep[T], msg: String = ""): Rep[Unit] = {
    unchecked[Unit]("/* assertEq */")
    val e: Rep[Boolean] = equalExplicit(lhs, rhs)
    assert(e, msg)
  }

  def assertNeq[T: Manifest](lhs: Rep[T], rhs: Rep[T], msg: String = ""): Rep[Unit] = {
    unchecked[Unit]("/* assertNeq */")
    val e: Rep[Boolean] = equalExplicit(lhs, rhs)
    assert(!e, msg)
  }

  def assert(cond: Rep[Boolean], msg: String = ""): Rep[Unit] = {
    "assert".reflectCtrlWith[Unit](cond, unit(msg))
  }
}

class ExternalLLSCDriver(folder: String = "./headers/llsc") extends SAISnippet[Int, Unit]
    with SAIOps with GenExternal { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericLLSCCodeGen = new GenericLLSCCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Int, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def remap(m: Manifest[_]): String = {
      if (m == manifest[Cont]) "Cont"
      else super.remap(m)
    }
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      val ng = init(g)
      run(name, ng)
      emitln("/* LLSC - External utility functions and library modeling functions */")
      emitln("/* Generated by sai.llsc.TestGenerateExternal */")
      emitln("#include \"external_shared.hpp\"")
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
