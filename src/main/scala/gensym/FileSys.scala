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
import gensym.lmsx.smt.SMTBool

import scala.collection.immutable.{List => StaticList, Map => StaticMap, Set => StaticSet, ListMap}
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

@virtualize
trait FileSysDefs extends ExternalUtil { self: SAIOps with BasicDefs with ValueDefs =>
  trait File
  trait Stream

  // %struct.stat = type { i64, i64, i64, i32, i32, i32, i32 (padding), i64, i64, i64, i64, %struct.timespec, %struct.timespec, %struct.timespec, [3 x i64] }
  // %struct.timespec = type { i64, i64 }

  // TODO: model nested fields? <2022-08-09, David Deng> //

  val statFields = ListMap[String, LLVMType](
    "st_dev"     -> IntType(64),
    "st_ino"     -> IntType(64),
    "st_nlink"   -> IntType(64),
    "st_mode"    -> IntType(32),
    "st_uid"     -> IntType(32),
    "st_gid"     -> IntType(32),
    "st_rdev"    -> IntType(64),
    "st_size"    -> IntType(64),
    "st_blksize" -> IntType(64),
    "st_block"   -> IntType(64),
    "st_atim"    -> IntType(128),
    "st_mtim"    -> IntType(128),
    "st_ctim"    -> IntType(128),
    "padding"    -> IntType(128), // simulate padding
    )
  val StatType = Struct(statFields.values.toList)

  // %struct.statfs = type { i64, i64, i64, i64, i64, i64, i64, %struct.__fsid_t, i64, i64, i64, [4 x i64] }
  // %struct.__fsid_t = type { [2 x i32] }

  // struct statfs {
  //    __fsword_t f_type;    /* Type of filesystem (see below) */
  //    __fsword_t f_bsize;   /* Optimal transfer block size */
  //    fsblkcnt_t f_blocks;  /* Total data blocks in filesystem */
  //    fsblkcnt_t f_bfree;   /* Free blocks in filesystem */
  //    fsblkcnt_t f_bavail;  /* Free blocks available to
  //                             unprivileged user */
  //    fsfilcnt_t f_files;   /* Total file nodes in filesystem */
  //    fsfilcnt_t f_ffree;   /* Free file nodes in filesystem */
  //    fsid_t     f_fsid;    /* Filesystem ID */
  //    __fsword_t f_namelen; /* Maximum length of filenames */
  //    __fsword_t f_frsize;  /* Fragment size (since Linux 2.6) */
  //    __fsword_t f_flags;   /* Mount flags of filesystem
  //                             (since Linux 2.6.36) */
  //    __fsword_t f_spare[xxx];
  //                    /* Padding bytes reserved for future use */
  // };

  val statfsFields = ListMap(
    "f_type"    -> IntType(64),
    "f_bsize"   -> IntType(64),
    "f_blocks"  -> IntType(64),
    "f_bfree"   -> IntType(64),
    "f_bavail"  -> IntType(64),
    "f_files"   -> IntType(64),
    "f_ffree"   -> IntType(64),
    "f_fsid"    -> IntType(64),
    "f_namelen" -> IntType(64),
    "f_frsize"  -> IntType(64),
    "f_flags"   -> IntType(64),
    // IntType(64),
    // IntType(64),
    // IntType(64),
    // IntType(64),
    )

  val StatfsType: StructType = Struct(statfsFields.values.toList)

  val termiosFields = ListMap(
    "c_iflag" -> IntType(64),
    "c_oflag" -> IntType(64),
    "c_cflag" -> IntType(64),
    "c_lflag" -> IntType(64),
    "c_cc"    -> ArrayType(10, IntType(8)),
    )

  val TermiosType: StructType = Struct(termiosFields.values.toList)

  def getFieldIdx(fields: ListMap[String, _], f: String): Int = {
    val idx = fields.keys.toList.indexOf(f)
    if (idx < 0) throw new Exception(s"field $f not found in fields $fields")
    idx
  }

  object File {
    def apply(name: Rep[String]) = "File::create".reflectWith[File](name, 0)
    def apply(name: Rep[String], content: Rep[List[Value]]) = "File::create".reflectWith[File](name, content)
    def apply(name: Rep[String], content: Rep[List[Value]], stat: Rep[List[Value]]) =
      "File::create".reflectWith[File](name, content, stat)
    def copy(f: Rep[File]) = "File::shallow_copy".reflectCtrlWith[File](f)
    def dcopy(f: Rep[File]) = "File::deep_copy".reflectCtrlWith[File](f)

    // auto f2 = Ptr<File>(new File(*f));
    // immer::map_transient<String, Ptr<File>> children;
    // for (auto &p: f->children) {
    //   auto child = deep_copy(p.second);
    //   child->parent = f2;
    //   children.set(p.first, child);
    // }
    // f2->children = children.persistent();
    // return f2;
  }

  implicit class FileOps(file: Rep[File]) {
    // fields
    def name: Rep[String]                = "ptr-field-@".reflectWith[String](file, "name")
    def content: Rep[List[Value]]        = "ptr-field-@".reflectMutableWith[List[Value]](file, "content")
    def size: Rep[Int]                   = content.size
    def stat: Rep[List[Value]]           = "ptr-field-@".reflectMutableWith[List[Value]](file, "stat")
    def children: Rep[Map[String, File]] = "ptr-field-@".reflectMutableWith[Map[String, File]](file, "children")
    def parent: Rep[File]                = "ptr-field-@".reflectMutableWith[File](file, "parent")

    // assign ptr-field
    // TODO: Is this valid? <2022-05-12, David Deng> //
    def name_=(rhs: Rep[String]): Unit                = "ptr-field-assign".reflectCtrlWith(file, "name", rhs)
    def content_=(rhs: Rep[List[Value]]): Unit        = "ptr-field-assign".reflectCtrlWith(file, "content", rhs)
    def stat_=(rhs: Rep[List[Value]]): Unit           = "ptr-field-assign".reflectCtrlWith(file, "stat", rhs)
    def children_=(rhs: Rep[Map[String, File]]): Unit = "ptr-field-assign".reflectCtrlWith[Map[String, File]](file, "children", rhs)
    def parent_=(rhs: Rep[File]): Unit                = "ptr-field-assign".reflectCtrlWith[File](file, "parent", rhs)

    // directory-related methods

    def hasChild(name: Rep[String]): Rep[Boolean]            = file.children.contains(name)
    def getChild(name: Rep[String]): Rep[File]               = file.children(name)
    def setChild(name: Rep[String], f: Rep[File]): Rep[Unit] = {
      f.parent = file
      file.children = file.children + (name, f)
    }
    def removeChild(name: Rep[String]): Rep[Unit]            = {
      file.children(name).parent = NullPtr[File]
      file.children = file.children - name
    }

    def isRootFile = file.parent == NullPtr[File] && file.name == unit("/")

    def fullPath: Rep[String]                                = {
      // var f = file
      val f: Var[File] = __newVar(file)
      // var path = file.name
      var path: Var[String] = __newVar(file.name)
      // as long as f has a parent, assign f = f.parent and prepend f.name to path
      while (readVar(f).parent != NullPtr[File]) {
        __assign(f, readVar(f).parent)
        if (readVar(f).isRootFile) {
          __assign(path, unit("/") + readVar(path))
        } else {
          __assign(path, readVar(f).name + unit("/") + readVar(path))
        }
      }
      assertEq(readVar(f).name, "/", "Outermost ancestor should be named /")
      path
    }
    // content-related methods

    def readAt(pos: Rep[Long], len: Rep[Long]): Rep[List[Value]] = content.drop(pos.toInt).take(len.toInt)

    def readStatFieldPosSize(f: String): (Int, Int) = 
      StructCalc()(null).getFieldOffsetSize(StatType.types, getFieldIdx(statFields, f))

    def readStatBw(f: String): Int =
      Constants.BYTE_SIZE * readStatFieldPosSize(f)._2

    def readStatField(f: String): Rep[Value] = {
      val (pos, size) = readStatFieldPosSize(f)
      "from-bytes".reflectMutableWith[Value](file.stat.drop(pos).take(size))
    }

    def writeStatField(f: String, v: Rep[Value]): Rep[Unit] = {
      val (pos, size) = readStatFieldPosSize(f)
      val bytes = "to-bytes".reflectWith[List[Value]](v)
      file.stat = file.stat.take(pos) ++ bytes ++ file.stat.drop(pos + bytes.size)
    }

    def writeAtNoFill(c: Rep[List[Value]], pos: Rep[Long]): Rep[Unit] = {
      unchecked("// File.writeAtNoFill")
      file.content = file.content.take(pos.toInt) ++ c ++ file.content.drop(pos.toInt + c.size)
    }
    def writeAt(c: Rep[List[Value]], pos: Rep[Long], fill: Rep[Value]): Rep[Unit] = {
      unchecked("// File.writeAt")
      val fillSize = pos.toInt - file.content.size
      if (fillSize > 0) {
        file.content = file.content ++ List.fill(fillSize)(fill)
      }
      file.writeAtNoFill(c, pos)
    }
    def append(c: Rep[List[Value]]): Rep[Unit] = file.writeAtNoFill(c, file.content.size)
    def clear(): Rep[File] = {
      file.content = List[Value]()
      file
    }
    def getPreferredCex(): Rep[List[Value]] = {
      // sample default values
      // st_dev    : 2053
      // st_ino    : 417975
      // st_nlink  : 3
      // st_mode   : 16893
      // st_uid    : 1000
      // st_gid    : 1000
      // st_rdev   : 0
      // st_size   : 4096
      // st_blksize: 4096
      // st_blocks : 8

      val st_mode_bw = readStatBw("st_mode")
      val st_mode = readStatField("st_mode")

      val cex: Rep[List[Value]] = List(
      // klee_prefer_cex(s, !(s->st_mode & ~(S_IFMT | 0777)));
      IntOp2.eq(st_mode & IntV(unchecked[Long]("~(S_IFMT | 0777)"), st_mode_bw), IntV(0, st_mode_bw)),
      // klee_prefer_cex(s, (s->st_mode&0700) == 0600);
      // klee_prefer_cex(s, (s->st_mode&0070) == 0040);
      // klee_prefer_cex(s, (s->st_mode&0007) == 0004);
      IntOp2.eq(st_mode & IntV(unchecked[Long]("0777"), st_mode_bw), IntV(unchecked[Long]("0644"), st_mode_bw)),
      // IntOp2.eq(st_mode & IntV(unchecked[Long]("0700"), st_mode_bw), IntV(unchecked[Long]("0600"), st_mode_bw)),
      // IntOp2.eq(st_mode & IntV(unchecked[Long]("0070"), st_mode_bw), IntV(unchecked[Long]("0040"), st_mode_bw)),
      // IntOp2.eq(st_mode & IntV(unchecked[Long]("0007"), st_mode_bw), IntV(unchecked[Long]("0004"), st_mode_bw)),
      // klee_prefer_cex(s, (s->st_mode&S_IFMT) == S_IFREG);
      IntOp2.eq(st_mode & IntV(FS.S_IFMT, st_mode_bw), IntV(FS.S_IFREG, st_mode_bw)),
      // klee_prefer_cex(s, s->st_nlink == 1);
      IntOp2.eq(readStatField("st_nlink"), IntV(1, readStatBw("st_nlink"))),
      // klee_prefer_cex(s, s->st_blksize == 4096);
      IntOp2.eq(readStatField("st_blksize"), IntV(4096, readStatBw("st_blksize"))),
      // klee_prefer_cex(s, s->st_dev == defaults->st_dev);
      IntOp2.eq(readStatField("st_dev"), IntV(2053, readStatBw("st_dev"))),
      // klee_prefer_cex(s, s->st_rdev == defaults->st_rdev);
      IntOp2.eq(readStatField("st_rdev"), IntV(0, readStatBw("st_rdev"))),
      // klee_prefer_cex(s, s->st_uid == defaults->st_uid);
      IntOp2.eq(readStatField("st_uid"), IntV(1000, readStatBw("st_uid"))),
      // klee_prefer_cex(s, s->st_gid == defaults->st_gid);
      IntOp2.eq(readStatField("st_gid"), IntV(1000, readStatBw("st_gid"))),

      // TODO: 16 byte nested struct not modeled <2022-08-30, David Deng> //
      // klee_prefer_cex(s, s->st_atime == defaults->st_atime);
      // klee_prefer_cex(s, s->st_mtime == defaults->st_mtime);
      // klee_prefer_cex(s, s->st_ctime == defaults->st_ctime);
         )

      cex
    }
  }

  // TODO: Change to pointer type for dup syscall? <2022-05-20, David Deng> //
  object Stream {
    def apply(f: Rep[File]) = "Stream::create".reflectCtrlWith[Stream](f)
    def apply(f: Rep[File], m: Rep[Int]) = "Stream::create".reflectCtrlWith[Stream](f, m)
    def apply(f: Rep[File], m: Rep[Int], c: Rep[Long]) = "Stream::create".reflectCtrlWith[Stream](f, m, c)
    def copy(strm: Rep[Stream]) = "Stream::shallow_copy".reflectCtrlWith[Stream](strm)
  }

  implicit class StreamOps(strm: Rep[Stream]) {
    // fields
    def name: Rep[String] = strm.file.name
    def file: Rep[File]   = "ptr-field-@".reflectCtrlWith[File](strm, "file")
    def cursor: Rep[Long] = "ptr-field-@".reflectCtrlWith[Long](strm, "cursor")
    def mode: Rep[Int]    = "ptr-field-@".reflectCtrlWith[Int](strm, "mode")

    // assign field
    def file_= (rhs: Rep[File]): Unit   = "ptr-field-assign".reflectCtrlWith[File](strm, "file", rhs)
    def cursor_= (rhs: Rep[Long]): Unit = "ptr-field-assign".reflectCtrlWith[Long](strm, "cursor", rhs)
    def mode_= (rhs: Rep[Int]): Unit    = "ptr-field-assign".reflectCtrlWith[Int](strm, "mode", rhs)

    def read(n: Rep[Long]): Rep[List[Value]] = {
      val content = file.readAt(strm.cursor, n)
      strm.cursor = strm.cursor + content.size
      content
    }

    def write(c: Rep[List[Value]], n: Rep[Long]): Rep[Long] = {
      val content = c.take(n.toInt)
      strm.file.writeAt(content, strm.cursor, cmacro("IntV0"))
      strm.cursor = strm.cursor + content.size;
      return content.size
    }

    def seekStart(o: Rep[Long]): Rep[Long] = {
      if (o < 0L) -1L
      else {
        strm.cursor = o
        o
      }
    }

    def seekEnd(o: Rep[Long]): Rep[Long] = {
      val newCursor = strm.file.content.size + o
      if (newCursor < 0L) -1L
      else {
        strm.cursor = newCursor
        newCursor
      }
    }

    def seekCur(o: Rep[Long]): Rep[Long] = {
      val newCursor = strm.cursor + o
      if (newCursor < 0L) -1L
      else {
        strm.cursor = newCursor
        newCursor
      }
    }
  }

  object FS {
    def apply() = "FS".reflectCtrlWith[FS]()
    def apply(opened_files: Rep[Map[Fd, Stream]], root_file: Rep[File], fs: Rep[FS]) = 
      "FS".reflectCtrlWith[FS](opened_files, root_file, fs)
    def dcopy(fs: Rep[FS]) = {
      val rootFile = File.dcopy(fs.rootFile)
      val openedFiles = Map[Fd, Stream]()
      val newFS = FS(openedFiles, rootFile, fs)
      fs.openedFiles.foreach { case (fd, s) =>
        val strm = Stream(newFS.getFile(s.file.fullPath), s.mode, s.cursor)
        newFS.setStream(fd, strm)
      }
      newFS
    }
    def SEEK_SET = cmacro[Int]("SEEK_SET")
    def SEEK_CUR = cmacro[Int]("SEEK_CUR")
    def SEEK_END = cmacro[Int]("SEEK_END")

    def O_RDONLY = cmacro[Int]("O_RDONLY")
    def O_RDWR   = cmacro[Int]("O_RDWR")
    def O_WRONLY = cmacro[Int]("O_WRONLY")
    def O_CREAT  = cmacro[Int]("O_CREAT")
    def O_TRUNC  = cmacro[Int]("O_TRUNC")
    def O_EXCL   = cmacro[Int]("O_EXCL")

    def S_IRUSR = cmacro[Int]("S_IRUSR")
    def S_IWUSR = cmacro[Int]("S_IWUSR")
    def S_IRGRP = cmacro[Int]("S_IRGRP")
    def S_IWGRP = cmacro[Int]("S_IWGRP")
    def S_IROTH = cmacro[Int]("S_IROTH")
    def S_IWOTH = cmacro[Int]("S_IWOTH")
    def S_IFDIR = cmacro[Int]("S_IFDIR")
    def S_IFREG = cmacro[Int]("S_IFREG")
    def S_IFMT  = cmacro[Int]("S_IFMT")

    def getPathSegments(path: Rep[String]): Rep[List[String]] = path.split("/").filter(_.length > 0)

    // TODO: use option type? <2022-05-26, David Deng> //
    def getFileFromPathSegments(file: Rep[File], segs: Rep[List[String]]): Rep[File] = {
      segs.foldLeft(file: Rep[File])((f: Rep[File], seg: Rep[String]) => {
        if (f == NullPtr[File] || !f.hasChild(seg)) NullPtr[File]
        else f.getChild(seg)
        // TODO: check that file is a directory <2022-05-26, David Deng> //
      })
    }
  }

  implicit class FSOps(fs: Rep[FS]) {
    import FS._
    def openedFiles: Rep[Map[Fd, Stream]] = "field-@".reflectCtrlWith[Map[Fd, Stream]](fs, "opened_files")
    def rootFile: Rep[File]               = "field-@".reflectCtrlWith[File](fs, "root_file")
    def statFs: Rep[List[Value]]          = "field-@".reflectCtrlWith[List[Value]](fs, "statfs")
    def nextFd: Rep[Fd]                   = "field-@".reflectCtrlWith[Fd](fs, "next_fd")
    def preferredCex: Rep[List[Value]]    = "field-@".reflectCtrlWith[List[Value]](fs, "preferred_cex")

    def openedFiles_= (rhs: Rep[Map[Fd, Stream]]): Unit = "field-assign".reflectCtrlWith(fs, "opened_files", rhs)
    def rootFile_= (rhs: Rep[File]): Unit               = "field-assign".reflectCtrlWith(fs, "root_file", rhs)
    def preferredCex_= (rhs: Rep[List[Value]]): Unit    = "field-assign".reflectCtrlWith[List[Value]](fs, "preferred_cex", rhs)

    def getFreshFd(): Rep[Fd] = "method-@".reflectCtrlWith[Fd](fs, "get_fresh_fd")

    def hasFile(name: Rep[String]): Rep[Boolean]            = fs.getFile(name) != NullPtr[File]
    def getFile(name: Rep[String]): Rep[File]               = getFileFromPathSegments(fs.rootFile, getPathSegments(name))

    // would set the file corresponding to name, parent should exist
    def setFile(name: Rep[String], f: Rep[File]): Rep[Unit] = {
      unchecked("/* setFile */")
      val segs: Rep[List[String]] = getPathSegments(name)
      val parent: Rep[File] = getFileFromPathSegments(fs.rootFile, segs.take(segs.size - 1))
      assertEq(segs.last, f.name, "setFile name should equal to last segment")
      if (parent != NullPtr[File]) parent.setChild(f.name, f)
      fs.preferredCex = fs.preferredCex ++ f.getPreferredCex()
    }

    // TODO: This is wrong <2022-07-28, David Deng> //
    def removeFile(name: Rep[String]): Rep[Unit]          = fs.rootFile.removeChild(name)
    def hasStream(fd: Rep[Fd]): Rep[Boolean]              = fs.openedFiles.contains(fd)
    def getStream(fd: Rep[Fd]): Rep[Stream]               = fs.openedFiles(fd)
    def setStream(fd: Rep[Fd], s: Rep[Stream]): Rep[Unit] = fs.openedFiles = fs.openedFiles + (fd, s)
    def removeStream(fd: Rep[Fd]): Rep[Unit]              = fs.openedFiles = fs.openedFiles - fd
  }

}
