package gensym

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import gensym.llvm._
import gensym.llvm.IR._
import gensym.IRUtils._
import gensym.lmsx._

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

// Writing models of syscall/external functions is often painful and error-prone
// in a low-level language, and we don't want to maintain multiple version of those
// external/intrinsic functions with only slightly backend difference.
// Can we generate them from our Scala DSL?

@virtualize
trait GenExternal extends SymExeDefs {
  trait Auto
  def info(msg: String) = unchecked("INFO(\"[FS] \" << \"" + msg + "\")")
  def info_obj(p: Rep[_], l: String = ""): Rep[Unit] = unchecked("INFO(\"", if (l == "") "" else l + ": ", "\" << ", p, ")")
  def info_ptrval(p: Rep[Value], l: String = ""): Rep[Unit] = unchecked("INFO(\"", if (l == "") "" else l + ": ", "\" << ", p, "->toString())")

  def stop[T: Manifest](ss: Rep[SS]): Rep[T] = {
    if (manifest[T] == manifest[Unit])
      "stop".reflectCtrlWith[T](ss, unchecked[List[Value]]("List<PtrVal>{}"), unchecked[(SS, Value) => Rep[T]]("halt"))
    else
      "stop".reflectCtrlWith[T](ss, unchecked[List[Value]]("List<PtrVal>{}"))
  }

  import FS._

  type ExtCont[T] = (Rep[SS], Rep[FS], Rep[Value]) => Rep[T]
  type Ext[T] = (Rep[SS], Rep[FS], Rep[List[Value]], ExtCont[T]) => Rep[T]

  def brFs[T: Manifest](ss: Rep[SS], fs: Rep[FS], cond: Rep[Value],
    tk: (Rep[SS], Rep[FS]) => Rep[T], fk: (Rep[SS], Rep[FS]) => Rep[T]) = {
      if (cond.isConc) {
        if (cond.int == 0) fk(ss, fs)
        else tk(ss, fs)
      } else symExecBrFs(ss, fs, cond, !cond, tk, fk)
  }

  def symExecBrFs[T: Manifest](ss: Rep[SS], fs: Rep[FS], tCond: Rep[Value], fCond: Rep[Value],
    tk: (Rep[SS], Rep[FS]) => Rep[T], fk: (Rep[SS], Rep[FS]) => Rep[T]) = {
      unchecked("INFO(\"symExecBrFs: tCond is symbolic: \" << ", tCond, "->toString())")
      val ssf = ss.fork
      val tpcSat = checkPC(ss.addPC(tCond).pc)
      val fpcSat = checkPC(ssf.addPC(fCond).pc)
      if (tpcSat && fpcSat) {
        unchecked("INFO(\"symExecBrFs: both satisfiable\")")
        Coverage.incPath(1)
        // false branch
        fk(ssf.addPC(fCond), FS.dcopy(fs))
        // true branch
        tk(ss.addPC(tCond), fs)
        // TODO: add second stage ++ operation <2022-08-19, David Deng> //
        // This version would lose result on non CPS versions
        // resF ++ resT
      } else if (tpcSat) {
        unchecked("INFO(\"symExecBrFs: only true satisfiable\")")
        tk(ss.addPC(tCond), fs)
      } else {
        unchecked("INFO(\"symExecBrFs: only false satisfiable\")")
        fk(ssf.addPC(fCond), fs)
      }
  }

  def sym_exit[T: Manifest](ss: Rep[SS], args: Rep[List[Value]]): Rep[T] =
    "sym_exit".reflectWith[T](ss, args)

  def getSymString(ptr: Rep[Value], s: Rep[SS]): Rep[List[Value]] = "get_sym_string_at".reflectCtrlWith[List[Value]](s, ptr)

  def hasPermission(flags: Rep[Value], f: Rep[File]): Rep[Value] = {
    // the requested permission
    // TODO: support symbolic flags <2022-08-17, David Deng> //
    val readAccess: Rep[SymV] = SymV.fromBool((flags.int & O_RDONLY: Rep[Boolean]) || (flags.int & O_RDWR))
    val writeAccess: Rep[SymV] = SymV.fromBool((flags.int & O_WRONLY: Rep[Boolean]) || (flags.int & O_RDWR))

    // the permission of the file
    val mode: Rep[Value] = f.readStatField("st_mode")
    val bw = f.readStatBw("st_mode")

    val canRead = ((mode & IntV(S_IRUSR, bw)) | (mode & IntV(S_IRGRP, bw)) | (mode & IntV(S_IROTH, bw)))
    val illegalRead = IntOp2.eq(canRead, IntV(0, bw))
   
    val canWrite = ((mode & IntV(S_IWUSR, bw)) | (mode & IntV(S_IWGRP, bw)) | (mode & IntV(S_IWOTH, bw)))
    val illegalWrite = IntOp2.eq(canWrite, IntV(0, bw))

    !((readAccess & illegalRead) | (writeAccess & illegalWrite))
  }

  // returns a SymV that encodes each element in the list equals the element in the other list
  def listEq(l1: Rep[List[Value]], l2: Rep[List[Value]]): Rep[Value] = 
    l1.zip(l2).foldLeft[Value](SymV.fromBool(true))((symv, pair) =>
        IntOp2("and", symv, IntOp2.eq(pair._1, pair._2)))

  // takes the current ss, fs, a symbolic file path, 
  // the continuation k takes the new ss, fs, and a concrete path, 
  // with the assumption that the symbolic file path is resolved to the concrete path.
  // The continuation tk can be called multiple times
  // The continuation fk will be called only one time when the resolution failed.
  def resolvePath[T: Manifest](ss: Rep[SS], fs: Rep[FS], symPath: Rep[List[Value]], 
    tk: (Rep[SS], Rep[FS], Rep[String]) => Rep[T], fk: (Rep[SS], Rep[FS]) => Rep[T]): Rep[T] = {
      if (symPath.foldLeft[Boolean](true)((b, v) => b && v.isConc)) {
        info("symPath is concrete")
        // if symPath is concrete
        tk(ss, fs, "proj_List_String".reflectWith[String](symPath))
      } else {
        info_obj(symPath.size, "symPath.size")
        // resolve for a number of possible concrete paths
        val concPaths = fs.rootFile.children
          .filter(nf => {
            // TODO: handle absolute paths and recursively traverse the FS <2022-10-01, David Deng> //
            val (name, _) = nf
            // exclude stdin, stdout, and stderr
            name.substring(0, 1) != "@" &&
            // the symbolic name must be long enough
            name.length < symPath.size
          }).map(_._1)
          info_obj(concPaths.size, "concPaths.size")
          if (concPaths.size > 0) {
            // val init: Rep[T] = if (manifest[T] == manifest[Unit]) () else List()
            concPaths.foldLeft[Unit](())((acc, concPath) => {
              // whether it is possible that the path matches the symbolic path
              val cond = listEq(concPath.split("").map((s: Rep[String]) => IntV(s(0).asRepOf[Long], 8): Rep[Value]), symPath)
              // TODO: add path constraint to the final null byte in string <2022-10-01, David Deng> //
              info_ptrval(cond, "path equal condition")
              symExecBrFs[T](ss.fork, FS.dcopy(fs), cond, !cond,
                (ss, fs) => {
                  // match
                  tk(ss, fs, concPath)
                },
                (ss, fs) => {
                  // not match
                  stop[T](ss)
                })
              acc 
            })
          } else {
            fk(ss, fs)
          }
          // TODO: collect results for non-CPS version <2022-10-01, David Deng> //
          // NOTE: will end up having one more path here due to the use of foreach //
          stop[T](ss.fork)
      }
  }

  /* 
   * int open(const char *pathname, int flags);
   * int open(const char *pathname, int flags, mode_t mode);
   */
  // type Res = Either[Unit, List[(SS, Value)]]
  // How to write union type?
  def open[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"open syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    val flags = args(1)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      info_obj(concPath, "concPath")
      if (!(flags.int & O_CREAT: Rep[Boolean]) && (flags.int & O_EXCL)) {
        k(ss.setErrorLoc(flag("EACCES")), fs, IntV(-1, 32))
      } else if (!fs.hasFile(concPath) && !(flags.int & O_CREAT: Rep[Boolean])) {
        k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
      } else if (fs.hasFile(concPath) && (flags.int & O_CREAT: Rep[Boolean]) && (flags.int & O_EXCL)) {
        k(ss.setErrorLoc(flag("EEXIST")), fs, IntV(-1, 32))
      } else if ((flags.int & O_TRUNC: Rep[Boolean]) && (flags.int & O_RDONLY)) {
        // The (undefined) effect of O_RDONLY | O_TRUNC varies among implementations.
        k(ss.setErrorLoc(flag("EEXIST")), fs, IntV(-1, 32))
      } else {
        if (!fs.hasFile(concPath)) {
          val f = File(getPathSegments(concPath).last, List[Value]())
          val regF = _set_file_type(f, S_IFREG)
          fs.setFile(concPath, regF)
        }
        info("fs.getFile")
        val file = fs.getFile(concPath)
        info("hasPermission")
        val hp: Rep[Value] = hasPermission(flags, file)
        brFs[T](ss, fs, hp,
          (ss, fs) => {
            if (flags.int & O_TRUNC) {
              file.content = List[Value]()
            }
            val fd: Rep[Fd] = fs.getFreshFd()
            fs.setStream(fd, Stream(file))
            k(ss, fs, IntV(fd, 32))
          },
          (ss, fs) => k(ss.setErrorLoc(flag("EACCES")), fs, IntV(-1, 32)) // does not have permission
          )
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int openat(int dirfd, const char *pathname, int flags);
   * int openat(int dirfd, const char *pathname, int flags, mode_t mode);
   */
  def openat[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    unchecked("INFO(\"openat syscall\")")
    // TODO: implement this <2022-01-23, David Deng> //
    k(ss, IntV(0, 32))
  }

  /* 
   * int close(int fd);
   */
  def close[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"close syscall\")")
    val fd: Rep[Fd] = args(0).int.toInt
    unchecked("INFO(\"fd: \" << ", fd, ")")
    if (!fs.hasStream(fd)) 
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 32))
    else {
      val strm = fs.getStream(fd)
      val name = strm.file.name
      if (fs.hasFile(name)) {
        // remove the stream associated with fd, write content to the actual file if the file still exists.
        fs.setFile(name, strm.file)
        fs.removeStream(fd)
      }
      k(ss, fs, IntV(0, 32))
    }
  }

  /*
   * ssize_t read(int fd, void *buf, size_t count);
   */
  def read[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"read syscall\")")
    val fd: Rep[Int] = args(0).int.toInt
    info_obj(fd, "fd")
    val loc: Rep[Value] = args(1)
    info_ptrval(loc, "loc")
    val count: Rep[Int] = args(2).int.toInt
    info_obj(count, "count")
    if (!fs.hasStream(fd)) 
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 64))
    else {
      val strm = fs.getStream(fd)
      val content: Rep[List[Value]] = strm.read(count)
      // will update the cursor in strm
      fs.setStream(fd, strm)
      val size = content.size
      k(ss.updateSeq(loc, content), fs, IntV(size, 64))
    }
  }

  /* 
   * ssize_t write(int fd, const void *buf, size_t count);
   */
  def write[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"write syscall\")")
    val fd: Rep[Int] = args(0).int.toInt // NOTE: .int => Rep[Long], .toInt => Rep[Int]
    info_obj(fd, "fd")
    val buf: Rep[Value] = args(1)
    info_ptrval(buf, "buf")
    val count: Rep[Int] = args(2).int.toInt
    info_obj(count, "count")
    val content: Rep[List[Value]] = ss.lookupSeq(buf, count)
    if (!fs.hasStream(fd)) {
      info("write syscall failed")
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 64))
    }
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
  def lseek[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"lseek syscall\")")
    val fd: Rep[Fd] = args(0).int.toInt
    val o: Rep[Long] = args(1).int
    val w: Rep[Int] = args(2).int.toInt
    if (!fs.hasStream(fd)) 
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 64))
    else {
      val strm = fs.getStream(fd)
      val pos = {
        if (w == SEEK_SET) strm.seekStart(o)
        else if (w == SEEK_CUR) strm.seekCur(o)
        else if (w == SEEK_END) strm.seekEnd(o)
        else -1L
      }
      if (pos == -1L) 
        k(ss.setErrorLoc(flag("EINVAL")), fs, IntV(-1, 64))
      else {
        fs.setStream(fd, strm)
        k(ss, fs, IntV(pos, 64))
      }
    }
  }

  /*
   * int stat(const char *pathname, struct stat *statbuf);
   */
  def stat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"stat syscall\")")
    val ptr = args(0)
    val path: Rep[List[Value]] = getSymString(ptr, ss)
    val buf: Rep[Value] = args(1)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      if (!fs.hasFile(concPath)) {
        info("stat does not have file")
        k(ss, fs, IntV(-1, 32))
      } else {
        info("stat has file")
        val stat = fs.getFile(concPath).stat
        val ss1 = ss.updateSeq(buf, stat)
        k(ss1, fs, IntV(0, 32))
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int fstat(int fd, struct stat *statbuf);
   */
  def fstat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"fstat syscall\")")
    val fd: Rep[Fd] = args(0).int.toInt
    val buf: Rep[Value] = args(1)
    if (!fs.hasStream(fd)) 
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 32))
    else {
      val stat = fs.getStream(fd).file.stat
      val ss1 = ss.updateSeq(buf, stat)
      k(ss1, fs, IntV(0, 32))
    }
  }

  /*
   * int lstat(const char *pathname, struct stat *statbuf);
   */
  def lstat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"lstat syscall\")")
    stat(ss, fs, args, k)
  }

  /*
   * int statfs(const char *path, struct statfs *buf);
   */
  def statfs[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"statfs syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      if (!fs.hasFile(concPath)) {
        k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
      } else {
        val buf: Rep[Value] = args(1)
        val statFs = fs.statFs
        k(ss.updateSeq(buf, statFs), fs, IntV(0, 32))
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int mkdir(const char *pathname, mode_t mode);
   */
  def mkdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"mkdir syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      // TODO: set mode <2022-05-28, David Deng> //
      val mode: Rep[Value] = args(1)
      val name: Rep[String] = getPathSegments(concPath).last
      if (fs.hasFile(concPath)) k(ss, fs, IntV(-1, 32))
      else {
        val f = _set_file_type(File(name, List[Value](), List.fill(144)(IntV(0, 8))), S_IFDIR)
        unchecked("/* mkdir: fs.setFile */")
        fs.setFile(concPath, f)
        unchecked("/* mkdir: return */")
        k(ss, fs, IntV(0, 32))
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int rmdir(const char *pathname);
   */
  def rmdir[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"rmdir syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      val dir = fs.getFile(concPath)
      if (dir == NullPtr[File])
        k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
      else {
        brFs[T](ss, fs, _has_file_type(dir, S_IFDIR),
          (ss, fs) => {
            // is a dir
            fs.removeFile(concPath)
            k(ss, fs, IntV(0, 32))
          },
          (ss, fs) => {
            // is not a dir
            k(ss.setErrorLoc(flag("ENOTDIR")), fs, IntV(-1, 32))
          })
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int creat(const char *pathname, mode_t mode);
   */
  def creat[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"creat syscall\")")
    // A call to creat() is equivalent to calling open() with flags equal to O_CREAT|O_WRONLY|O_TRUNC.
    open(ss, fs, List(args(0), IntV(O_CREAT | O_WRONLY | O_TRUNC), args(1)), k)
  }

  /*
   * int unlink(const char *pathname);
   */
  def unlink[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"unlink syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      val file = fs.getFile(concPath)
      if (file == NullPtr[File])
        k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
      else {
        brFs[T](ss, fs, _has_file_type(file, S_IFREG),
          (ss, fs) => {
            // is a regular file
            fs.removeFile(concPath)
            k(ss, fs, IntV(0, 32))
          },
          (ss, fs) => {
            // is not a regular file
            k(ss.setErrorLoc(flag("EISDIR")), fs, IntV(-1, 32))
          })
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int chmod(const char *pathname, mode_t mode);
   */
  def chmod[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"chmod syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      val file = fs.getFile(concPath)
      val mode: Rep[Value] = args(1)
      if (file == NullPtr[File]) k(ss, fs, IntV(-1, 32))
      else {
        _set_file_mode(file, mode.int.toInt)
        k(ss, fs, IntV(0, 32))
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int chown(const char *pathname, uid_t owner, gid_t group);
   */
  def chown[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"chown syscall\")")
    val path: Rep[List[Value]] = getSymString(args(0), ss)
    resolvePath(ss, fs, path, (ss, fs, concPath) => {
      val file = fs.getFile(concPath)
      val owner: Rep[Value] = args(1)
      val group: Rep[Value] = args(2)
      if (file == NullPtr[File]) k(ss, fs, IntV(-1, 32))
      else {
        file.writeStatField("st_uid", owner)
        file.writeStatField("st_gid", group)
        k(ss, fs, IntV(0, 32))
      }
    }, (ss, fs) => k(ss.setErrorLoc(flag("ENOENT")), fs, IntV(-1, 32))
    )
  }

  /*
   * int ioctl(int fd, int request, ...);
   */
  def ioctl[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"ioctl syscall\")")
    val fd: Rep[Fd] = args(0).int.toInt
    unchecked("INFO(\"with fd:\" << ", fd, ")")
    val request: Rep[Value] = args(1)
    unchecked("INFO(\"with request:\" << ", request, "->toString())")
    if (!fs.hasStream(fd) || fd <= 2) {
      k(ss.setErrorLoc(flag("EBADF")), fs, IntV(-1, 32))
    } else {
      if (request.int == 0x5401) { // TCGETS
        // TODO: generate staged variable name based on fd <2022-08-11, David Deng> //
        val buf: Rep[Value] = args(2)
        val ss1 = ss.updateSeq(buf, SymV.makeSymVList(TermiosType.size(null), s"fs_termios_fd_${fd}_"))
        val mode: Rep[Value] = fs.getStream(fd).file.readStatField("st_mode")
        val bw = Constants.BYTE_SIZE * StructCalc()(null).getFieldOffsetSize(StatType.types, getFieldIdx(statFields, "st_mode"))._2
        val ischr: Rep[Value] = IntOp2.eq(mode & IntV(cmacro[Int]("S_IFMT"), bw), IntV(cmacro[Int]("S_IFCHR"), bw))
        brFs(ss, fs, ischr,
        (ss, fs) => { 
          info("is character")
          k(ss, fs, IntV(0, 32))
        }, 
        (ss, fs) => {
          info("is not a character, return error")
          k(ss.setErrorLoc(flag("ENOTTY")), fs, IntV(-1, 32))
        })
      } else {
        k(ss.setErrorLoc(flag("EINVAL")), fs, IntV(-1, 32))
      }
    }
  }

  /*
   * int fcntl(int fd, int cmd, ...);
   */
  def fcntl[T: Manifest](ss: Rep[SS], fs: Rep[FS], args: Rep[List[Value]], k: ExtCont[T]): Rep[T] = {
    unchecked("INFO(\"fcntl syscall\")")
    val fd: Rep[Value] = args(0)
    val cmd: Rep[Value] = args(1)
    k(ss, fs, IntV(-1, 32))
  }

  ////////////////////////
  //  helper functions  //
  ////////////////////////
  def flag(f: String, bw: Int = 32): Rep[IntV] = IntV(unchecked[Long](f), bw)

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
    val bw = f.readStatBw("st_mode")
    val mode = f.readStatField("st_mode")
    // want to unset the file type bits and leave the other bits unchanged
    val newMode = (mode & flag("~S_IFMT", bw)) | IntV(mask, bw)
    f.writeStatField("st_mode", newMode)
    f
  }

  def _set_file_mode(f: Rep[File], mask: Rep[Int]): Rep[File] = {
    unchecked("/* _set_file_mode */")
    val bw = f.readStatBw("st_mode")
    val mode = f.readStatField("st_mode")
    // preserve the file type bits
    val newMode = (mode & flag("S_IFMT", bw)) | IntV(mask, bw)
    f.writeStatField("st_mode", newMode)
    f
  }

  def _errno_location[T: Manifest](ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = 
    k(ss, ss.getErrorLoc)

  def _has_file_type(f: Rep[File], mask: Rep[Int]): Rep[Value] = {
    val stat = f.readStatField("st_mode")
    val bw = f.readStatBw("st_mode")
    IntOp2.neq(stat & IntV(mask, bw), IntV(0, bw)) // cast to boolean
  }

  def _get_preferred_cex(f: Rep[File]): Rep[List[Value]] = f.getPreferredCex()

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
    unchecked("auto start = steady_clock::now();")
    val res = f(ss.getFs, args, kp)
    unchecked("auto end = steady_clock::now();")
    unchecked("fs_time += duration_cast<microseconds>(end - start).count();")
    res
  }

  def brg_fs[T: Manifest](f: Ext[T])(ss: Rep[SS], args: Rep[List[Value]], k: (Rep[SS], Rep[Value]) => Rep[T]): Rep[T] = {
    val start = unchecked[Auto]("steady_clock::now()")
    def kp(ss: Rep[SS], fs: Rep[FS], ret: Rep[Value]): Rep[T] = {
      ss.setFs(fs)
      val end = unchecked[Auto]("steady_clock::now()")
      val duration = unchecked[Auto]("duration_cast<microseconds>(", end, " - ", start, ").count() ")
      unchecked("fs_time += ", duration)
      k(ss, ret)
    }
    val res = f(ss, ss.getFs, args, kp)
    res
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

class ExternalGSDriver(folder: String = "./headers/gensym") extends SAISnippet[Int, Unit]
    with SAIOps with GenExternal { q =>
  import java.io.{File, PrintStream}
  import scala.collection.mutable.HashMap

  val codegen: GenericGSCodeGen = new GenericGSCodeGen {
    val codegenFolder: String = folder
    val blockNameMap: HashMap[Backend.Sym, String] = new HashMap()
    setFunMap(funNameMap)
    setBlockMap(blockNameMap)
    override def remap(m: Manifest[_]): String = {
      if (m == manifest[Cont]) "Cont"
      else super.remap(m)
    }
    override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
      val ng = init(g)
      run(name, ng)
      emitln("/* GenSym - External utility functions and library modeling functions */")
      emitln("/* Generated by gensym.TestGenerateExternal */")
      emitln("#include \"external_shared.hpp\"")
      emitln("#ifndef GS_EXTERNAL_HEADERS_GEN")
      emitln("#define GS_EXTERNAL_HEADERS_GEN")
      emitln(s"inline extern const int stat_size = ${StatType.size(null)};")
      emitln(s"inline extern const int statfs_size = ${StatfsType.size(null)};")
      emitFunctionDecls(stream)
      emitFunctions(stream)
      emitln("#endif // GS_EXTERNAL_HEADERS_GEN")
    }
  }

  def genHeader: Unit = {
    val mainStream = new PrintStream(s"$folder/external.hpp")
    val statics = Adapter.emitCommon1("header", codegen, mainStream)(manifest[Int], manifest[Unit])(x => Unwrap(wrapper(Wrap[Int](x))))
    mainStream.close
  }

  def snippet(u: Rep[Int]) = {
    hardTopFun(gen_p(brg_fs(open(_,_,_,_))), "syscall_open", "inline")
    hardTopFun(gen_k(brg_fs(open(_,_,_,_))), "syscall_open", "inline")
    hardTopFun(gen_p(brg_fs(close(_,_,_,_))), "syscall_close", "inline")
    hardTopFun(gen_k(brg_fs(close(_,_,_,_))), "syscall_close", "inline")
    hardTopFun(gen_p(brg_fs(read(_,_,_,_))), "syscall_read", "inline")
    hardTopFun(gen_k(brg_fs(read(_,_,_,_))), "syscall_read", "inline")
    hardTopFun(gen_p(brg_fs(write(_,_,_,_))), "syscall_write", "inline")
    hardTopFun(gen_k(brg_fs(write(_,_,_,_))), "syscall_write", "inline")
    hardTopFun(gen_p(brg_fs(lseek(_,_,_,_))), "syscall_lseek", "inline")
    hardTopFun(gen_k(brg_fs(lseek(_,_,_,_))), "syscall_lseek", "inline")
    hardTopFun(gen_p(brg_fs(lseek(_,_,_,_))), "syscall_lseek64", "inline")
    hardTopFun(gen_k(brg_fs(lseek(_,_,_,_))), "syscall_lseek64", "inline")
    hardTopFun(gen_p(brg_fs(stat(_,_,_,_))), "syscall_stat", "inline")
    hardTopFun(gen_k(brg_fs(stat(_,_,_,_))), "syscall_stat", "inline")
    hardTopFun(gen_p(brg_fs(fstat(_,_,_,_))), "syscall_fstat", "inline")
    hardTopFun(gen_k(brg_fs(fstat(_,_,_,_))), "syscall_fstat", "inline")
    hardTopFun(gen_p(brg_fs(lstat(_,_,_,_))), "syscall_lstat", "inline")
    hardTopFun(gen_k(brg_fs(lstat(_,_,_,_))), "syscall_lstat", "inline")
    hardTopFun(gen_p(brg_fs(statfs(_,_,_,_))), "syscall_statfs", "inline")
    hardTopFun(gen_k(brg_fs(statfs(_,_,_,_))), "syscall_statfs", "inline")
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
    hardTopFun(gen_p(brg_fs(ioctl(_,_,_,_))), "syscall_ioctl", "inline")
    hardTopFun(gen_k(brg_fs(ioctl(_,_,_,_))), "syscall_ioctl", "inline")
    hardTopFun(gen_p(brg_fs(fcntl(_,_,_,_))), "syscall_fcntl", "inline")
    hardTopFun(gen_k(brg_fs(fcntl(_,_,_,_))), "syscall_fcntl", "inline")
    hardTopFun(_set_file(_,_,_), "set_file", "inline")
    hardTopFun(_set_file_type(_,_), "set_file_type", "inline")
    hardTopFun(_get_preferred_cex(_), "get_preferred_cex", "inline")
    hardTopFun(gen_p(_errno_location), "__errno_location", "inline")
    hardTopFun(gen_k(_errno_location), "__errno_location", "inline")
    // hardTopFun(gen_p(openat), "openat", "inline")
    // hardTopFun(gen_k(openat), "openat", "inline")
    ()
  }
}

object GenerateExternal {
  def main(args: Array[String]): Unit = {
    val code = new ExternalGSDriver
    code.genHeader
  }
}
