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

@virtualize
trait FileSysDefs extends ExternalUtil { self: SAIOps with BasicDefs with ValueDefs =>
  trait File
  trait Stream

  // TODO: refactor using getTySize and calculateOffsetStatic <2022-05-26, David Deng> //
  val statFieldMap: Map[String, (Int, Int)] = StaticMap(
      "st_dev" -> (0, 8),
      "st_ino" -> (8, 8),
      "st_nlink" -> (16, 8),
      "st_mode" -> (24, 4),
      "st_uid" -> (28, 4),
      "st_gid" -> (32, 4),
      // padding, (36, 4)},
      "st_rdev" -> (40, 8),
      "st_size" -> (48, 8),
      "st_blksi" -> (56, 8),
      "st_block" -> (64, 8),
      "st_atim" -> (72, 16),
      "st_mtim" -> (88, 16),
      "st_ctim" -> (104, 16),
  )

  object File {
    def apply(name: Rep[String]) = "File::create".reflectWith[File](name)
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

    def readStatField(f: String): Rep[Value] = {
      val (pos, size) = statFieldMap(f)
      "from-bytes".reflectMutableWith[Value](file.stat.drop(pos).take(size))
    }

    def writeStatField(f: String, v: Rep[Value]): Rep[Unit] = {
      val (pos, size) = statFieldMap(f)
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
    def apply(opened_files: Rep[Map[Fd, Stream]], root_file: Rep[File]) = "FS".reflectCtrlWith[FS](opened_files, root_file)
    def dcopy(fs: Rep[FS]) = {
      val rootFile = File.dcopy(fs.rootFile)
      val openedFiles = Map[Fd, Stream]()
      val newFS = "FS".reflectCtrlWith[FS](openedFiles, rootFile)
      fs.openedFiles.foreach({ case (fd, s) => {
        val strm = Stream(newFS.getFile(s.file.fullPath), s.mode, s.cursor)
        newFS.setStream(fd, strm)
      }})
      newFS
    }
    def SEEK_SET = cmacro[Int]("SEEK_SET")
    def SEEK_CUR = cmacro[Int]("SEEK_CUR")
    def SEEK_END = cmacro[Int]("SEEK_END")

    def S_IFDIR = cmacro[Int]("S_IFDIR")
    def S_IFREG = cmacro[Int]("S_IFREG")

    def O_RDONLY = cmacro[Int]("O_RDONLY")

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

    def openedFiles_= (rhs: Rep[Map[Fd, Stream]]): Unit = "field-assign".reflectCtrlWith(fs, "opened_files", rhs)
    def rootFile_= (rhs: Rep[File]): Unit = "field-assign".reflectCtrlWith(fs, "root_file", rhs)

    def seekFile(fd: Rep[Fd], o: Rep[Long], w: Rep[Int]): Rep[Long] =
      if (!fs.hasStream(fd)) -1L
      else {
        val strm = fs.getStream(fd)
        if (w == SEEK_SET) strm.seekStart(o)
        else if (w == SEEK_CUR) strm.seekCur(o)
        else if (w == SEEK_END) strm.seekEnd(o)
        else -1L
      }

    def getFreshFd(): Rep[Fd] = "method-@".reflectCtrlWith[Fd](fs, "get_fresh_fd")

    // TODO: recursively search <2022-05-25, David Deng> //
    def hasFile(name: Rep[String]): Rep[Boolean]            = fs.getFile(name) != NullPtr[File]
    def getFile(name: Rep[String]): Rep[File]               = {
      unchecked("/* getFile */")
      getFileFromPathSegments(fs.rootFile, getPathSegments(name))
    }

    // would set the file corresponding to name, parent should exist
    def setFile(name: Rep[String], f: Rep[File]): Rep[Unit] = {
      unchecked("/* setFile */")
      val segs: Rep[List[String]] = getPathSegments(name)
      val parent: Rep[File] = getFileFromPathSegments(fs.rootFile, segs.take(segs.size - 1))
      assertEq(segs.last, f.name, "setFile name should equal to last segment")
      if (parent != NullPtr[File]) parent.setChild(f.name, f)
    }

    def removeFile(name: Rep[String]): Rep[Unit]          = fs.rootFile.removeChild(name)
    def hasStream(fd: Rep[Fd]): Rep[Boolean]              = fs.openedFiles.contains(fd)
    def getStream(fd: Rep[Fd]): Rep[Stream]               = fs.openedFiles(fd)
    def setStream(fd: Rep[Fd], s: Rep[Stream]): Rep[Unit] = fs.openedFiles = fs.openedFiles + (fd, s)
    def removeStream(fd: Rep[Fd]): Rep[Unit]              = fs.openedFiles = fs.openedFiles - fd
  }

}
