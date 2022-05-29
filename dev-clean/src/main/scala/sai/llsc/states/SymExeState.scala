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

/* Naming convention for IR nodes:
   - If the node can be and should be handled by the default case of the codegen,
     i.e. directly emitting the IR name and arguments sequentially,
     we shall use underscore `_` as the delimiter in the IR name, e.g. `proj_LocV`.
   - If the node requires additional care in the codegen,
     we should use dash `-` as the delimiter in the IR name, e.g. `ss-lookup-stack`.
     The reason is that in common codegen targets (such as C/C++/Scala), `-` is
     not a valid use of identifiers.
 */

@virtualize
trait SymExeDefs extends SAIOps with StagedNondet with BasicDefs with ValueDefs with Opaques with Coverage with ExternalUtil {
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)
  type Cont = PCont[Id]

  trait Future[T]

  object ThreadPool {
    def enqueue[T: Manifest](f: Rep[Unit] => Rep[T]): Rep[Future[T]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[Unit](x))))
      Wrap[Future[T]](Adapter.g.reflectWrite("tp-enqueue", block)(Adapter.CTRL))
    }
    def async[T: Manifest](f: Rep[Unit] => Rep[T]): Rep[Future[T]] = {
      val block = Adapter.g.reify(x => Unwrap(f(Wrap[Unit](x))))
      Wrap[Future[T]](Adapter.g.reflectWrite("tp-async", block)(Adapter.CTRL))
    }
    def get[T: Manifest](f: Rep[Future[T]]): Rep[T] =
      "tp-future-get".reflectWriteWith[T](f)(Adapter.CTRL)

    def canPar: Rep[Boolean] = "can-par".reflectWriteWith[Boolean]()(Adapter.CTRL)
  }

  def reify[T: Manifest](s: Rep[SS])(comp: Comp[E, Rep[T]]): Rep[List[(SS, T)]] = {
    val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[T])] =
      State.runState[Nondet ⊗ ∅, Rep[SS], Rep[T]](s)(comp)
    val p2: Comp[Nondet ⊗ ∅, Rep[(SS, T)]] = p1.map(a => a)
    val p3: Comp[∅, Rep[List[(SS, T)]]] = runRepNondet[(SS, T)](p2)
    p3
  }

  def reflect[T: Manifest](res: Rep[List[(SS, T)]]): Comp[E, Rep[T]] = {
    for {
      ssu <- select[E, (SS, T)](res)
      _ <- put[Rep[SS], E](ssu._1)
    } yield ssu._2
  }

  implicit class PCOps(pc: Rep[PC]) {
    def addPC(e: Rep[SMTBool]): Rep[PC] = "add-pc".reflectWith[PC](pc, e)
  }

  class SSOps(ss: Rep[SS]) {
    private def assignSeq(xs: List[Int], vs: Rep[List[Value]]): Rep[SS] =
      "ss-assign-seq".reflectWith[SS](ss, xs, vs)

    def lookup(x: String): Rep[Value] = "ss-lookup-env".reflectWith[Value](ss, x.hashCode)
    def assign(x: String, v: Rep[Value]): Rep[SS] = "ss-assign".reflectWith[SS](ss, x.hashCode, v)
    def assign(xs: List[String], vs: Rep[List[Value]]): Rep[SS] = assignSeq(xs.map(_.hashCode), vs)
    def lookup(addr: Rep[Value], size: Int = 1, isStruct: Int = 0): Rep[Value] = {
      require(size > 0)
      if (isStruct == 0) "ss-lookup-addr".reflectWith[Value](ss, addr, size)
      else "ss-lookup-addr-struct".reflectWith[Value](ss, addr, size)
    }
    def lookupSeq(addr: Rep[Value], count: Rep[Int]): Rep[List[Value]] = "ss-lookup-addr-seq".reflectWith[List[Value]](ss, addr, count)

    def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int, k: Rep[Cont]): Rep[Unit] =
      "ss-array-lookup".reflectWith[Unit](ss, base, offset, eSize, k)
    def arrayLookup(base: Rep[Value], offset: Rep[Value], eSize: Int): Rep[List[(SS, Value)]] =
      "ss-array-lookup".reflectWith[List[(SS, Value)]](ss, base, offset, eSize)

    def update(a: Rep[Value], v: Rep[Value], sz: Int): Rep[SS] = "ss-update".reflectWith[SS](ss, a, v, sz)
    def updateSeq(a: Rep[Value], v: Rep[List[Value]]): Rep[SS] = "ss-update-seq".reflectWith[SS](ss, a, v)
    def allocStack(n: Int, align: Int): Rep[SS] = "ss-alloc-stack".reflectWith[SS](ss, n)

    def heapLookup(addr: Rep[Addr]): Rep[Value] = "ss-lookup-heap".reflectWith[Value](ss, addr)
    def heapSize: Rep[Int] = "ss-heap-size".reflectWith[Int](ss)
    def heapAppend(vs: Rep[List[Value]]) = "ss-heap-append".reflectWith[SS](ss, vs)

    def stackSize: Rep[Int] = "ss-stack-size".reflectWith[Int](ss)
    def freshStackAddr: Rep[Addr] = stackSize

    def push: Rep[SS] = "ss-push".reflectWith[SS](ss)
    def pop(keep: Rep[Int]): Rep[SS] = "ss-pop".reflectWith[SS](ss, keep)
    def addPC(e: Rep[SMTBool]): Rep[SS] = "ss-addpc".reflectWith[SS](ss, e)
    def addPCSet(es: Rep[List[SMTBool]]): Rep[SS] = "ss-addpcset".reflectWith[SS](ss, es)
    def pc: Rep[PC] = "get-pc".reflectWith[PC](ss)
    def updateArg: Rep[SS] = "ss-arg".reflectWith[SS](ss)
    def updateErrorLoc: Rep[SS] = "ss-error-loc".reflectWith[SS](ss)

    def addIncomingBlock(x: String): Rep[SS] = "ss-add-incoming-block".reflectWith[SS](ss, x.hashCode)
    def incomingBlock: Rep[BlockLabel] = "ss-incoming-block".reflectWith[BlockLabel](ss)

    def getFs: Rep[FS] = "ss-get-fs".reflectCtrlWith[FS](ss)
    def setFs(fs: Rep[FS]): Unit = "ss-set-fs".reflectCtrlWith[FS](ss, fs)
  }

  implicit class SSOpsOpt(ss: Rep[SS]) extends SSOps(ss) {
    private def lookupOpt(x: Int, s: Backend.Def, default: => Rep[Value], bound: Int): Rep[Value] =
      if (bound == 0) default
      else s match {
        case gNode("ss-assign", StaticList(ss0, bConst(y), v: bExp)) if y == x => Wrap[Value](v)
        case gNode("ss-assign-seq", StaticList(ss0, bConst(vars: StaticList[Int]), vals: bExp)) =>
          val idx = vars.indexOf(x)
          if (idx != -1) (Wrap[List[Value]](vals): Rep[List[Value]])(idx)
          else lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-assign", StaticList(ss0, _, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-alloc-stack", StaticList(ss0, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-update", StaticList(ss0, _, _, _)) => lookupOpt(x, ss0, default, bound-1)
        case gNode("ss-add-incoming-block", StaticList(ss0, _)) => lookupOpt(x, ss0, default, bound-1)
        // TODO: update-seq?
        case _ => default
      }

    override def lookup(x: String): Rep[Value] =
      if (Config.opt) lookupOpt(x.hashCode, Unwrap(ss), super.lookup(x), 30)
      else super.lookup(x)

    override def stackSize: Rep[Int] =
      if (Config.opt) {
        Unwrap(ss) match {
          case gNode("ss-alloc-stack", StaticList(ss0: bExp, bConst(inc: Int))) => Wrap[SS](ss0).stackSize + inc
          case gNode("ss-assign", StaticList(ss0: bExp, _, _)) => Wrap[SS](ss0).stackSize
          case _ => super.stackSize
        }
      } else { super.stackSize }

  }

  def literal[T: Manifest](s: String): Rep[T] = "literal".reflectUnsafeWith[T](unit(s))

  // TODO: refactor using getTySize and calculateOffsetStatic <2022-05-26, David Deng> //
  val statFieldMap: Map[String, (Int, Int)]                 = StaticMap(
      "st_dev" -> (0, 8),
      "st_ino" -> (8, 8),
      "st_nlink" -> (16, 8),
      "st_mode" -> (24, 4),
      "st_uid" -> (28, 4),
      "st_gid" -> (32, 4),
      // padding,      {36,  4  }},
      "st_rdev" -> (40, 8),
      "st_size" -> (48, 8),
      "st_blksi" -> (56, 8),
      "st_block" -> (64, 8),
      "st_atim" -> (72, 16),
      "st_mtim" -> (88, 16),
      "st_ctim" -> (104, 16),
  )
  // val statType: StructType = Struct(
  //   IntType(8)
  // )

  object File {
    def apply(name: Rep[String])                            = "File::create".reflectWith[File](name)
    def apply(name: Rep[String], content: Rep[List[Value]]) = "File::create".reflectWith[File](name, content)
    def apply(name: Rep[String], content: Rep[List[Value]], stat: Rep[List[Value]]) = "File::create".reflectWith[File](name, content, stat)
    def copy(f: Rep[File])                            = "File::create".reflectCtrlWith[File](f)
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
    def setChild(name: Rep[String], f: Rep[File]): Rep[Unit] = file.children = file.children + (name, f)
    def removeChild(name: Rep[String]): Rep[Unit]            = file.children = file.children - name

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
    def copy(strm: Rep[Stream]) = "Stream::create".reflectCtrlWith[Stream](strm)
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
      strm.file.writeAt(content, strm.cursor, literal("IntV0"))
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

  // path helpers
  def getPathSegments(path: Rep[String]): Rep[List[String]] = {
    path.split("/").filter(_.length > 0)
  }

  // TODO: use option type? <2022-05-26, David Deng> //
  def getFileFromPathSegments(file: Rep[File], segs: Rep[List[String]]): Rep[File] = {
    segs.foldLeft(file: Rep[File])((f: Rep[File], seg: Rep[String]) => {
      if (f == NullPtr() || !f.hasChild(seg)) NullPtr()
      else f.getChild(seg)
      // TODO: check that file is a directory <2022-05-26, David Deng> //
    })
  }

  object FS {
    def apply() = "FS".reflectCtrlWith[FS]()
  }

  implicit class FSOps(fs: Rep[FS]) {
    def openedFiles: Rep[Map[Fd, Stream]] = "field-@".reflectCtrlWith[Map[Fd, Stream]](fs, "opened_files")
    def rootFile: Rep[File]               = "field-@".reflectCtrlWith[File](fs, "root_file")

    def openedFiles_= (rhs: Rep[Map[Fd, Stream]]): Unit = "field-assign".reflectCtrlWith(fs, "opened_files", rhs)
    def rootFile_= (rhs: Rep[File]): Unit = "field-assign".reflectCtrlWith(fs, "root_file", rhs)

    def seekFile(fd: Rep[Fd], o: Rep[Long], w: Rep[Int]): Rep[Long] = {
      if (!fs.hasStream(fd)) -1L
      else {
        val strm = fs.getStream(fd)
        if (w == literal("SEEK_SET")) strm.seekStart(o)
        else if (w == literal("SEEK_CUR")) strm.seekCur(o)
        else if (w == literal("SEEK_END")) strm.seekEnd(o)
        else -1L
      }
    }

    def getFreshFd(): Rep[Fd] = "method-@".reflectCtrlWith[Fd](fs, "get_fresh_fd")

    // TODO: recursively search <2022-05-25, David Deng> //
    def hasFile(name: Rep[String]): Rep[Boolean]            = fs.getFile(name) != NullPtr()
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
      if (parent != NullPtr()) parent.setChild(f.name, f)
    }
    def removeFile(name: Rep[String]): Rep[Unit]            = fs.rootFile.removeChild(name)

    def hasStream(fd: Rep[Fd]): Rep[Boolean]              = fs.openedFiles.contains(fd)
    def getStream(fd: Rep[Fd]): Rep[Stream]               = fs.openedFiles(fd)
    def setStream(fd: Rep[Fd], s: Rep[Stream]): Rep[Unit] = fs.openedFiles = fs.openedFiles + (fd, s)
    def removeStream(fd: Rep[Fd]): Rep[Unit]              = fs.openedFiles = fs.openedFiles - fd
  }

  def putState(s: Rep[SS]): Comp[E, Rep[Unit]] = for { _ <- put[Rep[SS], E](s) } yield ()
  def getState: Comp[E, Rep[SS]] = get[Rep[SS], E]

  def updateState(f: Rep[SS] => Rep[SS]): Comp[E, Rep[Unit]] =
    for {
      ss <- getState
      _ <- putState(f(ss))
    } yield ()

  def heapAppend(vs: Rep[List[Value]]): Comp[E, Rep[Unit]]  = updateState(_.heapAppend(vs))
  def stackUpdate(xs: List[String], vs: Rep[List[Value]]): Comp[E, Rep[Unit]] = updateState(_.assign(xs, vs))
  def stackUpdate(x: String, v: Rep[Value]): Comp[E, Rep[Unit]] = updateState(_.assign(x, v))
  def pushFrame: Comp[E, Rep[Unit]] = updateState(_.push)
  def popFrame(keep: Rep[Int]): Comp[E, Rep[Unit]] = updateState(_.pop(keep))
  def updateMem(k: Rep[Value], v: Rep[Value], sz: Int): Comp[E, Rep[Unit]] = updateState(_.update(k, v, sz))
  def updatePCSet(x: Rep[List[SMTBool]]): Comp[E, Rep[Unit]] = updateState(_.addPCSet(x))
  def updatePC(x: Rep[SMTBool]): Comp[E, Rep[Unit]] = updateState(_.addPC(x))
  def updateIncomingBlock(x: String): Comp[E, Rep[Unit]] = updateState(_.addIncomingBlock(x))
  def initializeArg: Comp[E, Rep[Unit]] = updateState(_.updateArg)
  def initializeErrorLoc: Comp[E, Rep[Unit]] = updateState(_.updateErrorLoc)
}
