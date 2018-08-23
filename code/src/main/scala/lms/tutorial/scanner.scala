package scala.lms.tutorial

import scala.lms.common._
import scala.reflect.SourceContext

// tests for the non-staged Scanner library
// provided by scannerlib.scala
// which is in a separate file so that it can easily be included
// independently of the whole project
class ScannerLibTest extends LibSuite {
  test("low-level first field scanning") {
    val s = new Scanner(dataFilePath("t.csv"))
    assert(s.next(',')=="Name")
    s.close
  }
  test("low-level first 2 fields scanning") {
    val s = new Scanner(dataFilePath("t.csv"))
    assert(s.next(',')=="Name")
    assert(s.next(',')=="Value")
    s.close
  }
  test("low-level first line scanning") {
    val s = new Scanner(dataFilePath("t.csv"))
    assert(s.next('\n')=="Name,Value,Flag")
    s.close
  }
  test("low-level schema scanning") {
    val s = new Scanner(dataFilePath("t.csv"))
    val defaultFieldDelimiter = ','
    val v = s.next('\n').split(defaultFieldDelimiter).toVector
    assert(v==Vector("Name","Value","Flag"))
    s.close
  }
  test("low-level record scanning knowing schema") {
    val s = new Scanner(dataFilePath("t.csv"))
    val schema = Vector("Name","Value","Flag")
    val fieldDelimiter = ','
    val last = schema.last
    val v = schema.map{x => s.next(if (x==last) '\n' else fieldDelimiter)}
    assert(v==Vector("Name","Value","Flag"))
    s.close
  }
}

trait ScannerBase extends Base { this: Dsl =>
  implicit def scannerTyp: Typ[Scanner]
  implicit class RepScannerOps(s: Rep[Scanner]) {
    def next(d: Char)(implicit pos: SourceContext) = scannerNext(s, d)
    def hasNext(implicit pos: SourceContext) = scannerHasNext(s)
    def close(implicit pos: SourceContext) = scannerClose(s)
  }
  def newScanner(fn: Rep[String])(implicit pos: SourceContext): Rep[Scanner]
  def scannerNext(s: Rep[Scanner], d: Char)(implicit pos: SourceContext): Rep[String]
  def scannerHasNext(s: Rep[Scanner])(implicit pos: SourceContext): Rep[Boolean]
  def scannerClose(s: Rep[Scanner])(implicit pos: SourceContext): Rep[Unit]
}

trait ScannerExp extends ScannerBase with EffectExp { this: DslExp =>
  implicit def scannerTyp: Typ[Scanner] = manifestTyp

  case class ScannerNew(fn: Exp[String]) extends Def[Scanner]
  case class ScannerNext(s: Exp[Scanner], d: Exp[Char]) extends Def[String]
  case class ScannerHasNext(s: Exp[Scanner]) extends Def[Boolean]
  case class ScannerClose(s: Exp[Scanner]) extends Def[Unit]

  override def newScanner(fn: Rep[String])(implicit pos: SourceContext): Rep[Scanner] =
    reflectMutable(ScannerNew(fn))
  override def scannerNext(s: Rep[Scanner], d: Char)(implicit pos: SourceContext): Rep[String] =
    reflectWrite(s)(ScannerNext(s, Const(d)))
  override def scannerHasNext(s: Rep[Scanner])(implicit pos: SourceContext): Rep[Boolean] =
    reflectWrite(s)(ScannerHasNext(s))
  override def scannerClose(s: Rep[Scanner])(implicit pos: SourceContext): Rep[Unit] =
    reflectWrite(s)(ScannerClose(s))

  override def mirror[A:Typ](e: Def[A], f: Transformer)(implicit pos: SourceContext): Exp[A] = (e match {
    case Reflect(e@ScannerNew(fn), u, es) => reflectMirrored(Reflect(ScannerNew(f(fn)), mapOver(f,u), f(es)))(mtype(manifest[A]), pos)
    case Reflect(ScannerNext(s, d), u, es) => reflectMirrored(Reflect(ScannerNext(f(s), f(d)), mapOver(f,u), f(es)))(mtype(manifest[A]), pos)
    case Reflect(ScannerHasNext(s), u, es) => reflectMirrored(Reflect(ScannerHasNext(f(s)), mapOver(f,u), f(es)))(mtype(manifest[A]), pos)
    case Reflect(ScannerClose(s), u, es) => reflectMirrored(Reflect(ScannerClose(f(s)), mapOver(f,u), f(es)))(mtype(manifest[A]), pos)
    case _ => super.mirror(e,f)
  }).asInstanceOf[Exp[A]]
}


trait ScalaGenScanner extends ScalaGenEffect {
  val IR: ScannerExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ScannerNew(fn) => emitValDef(sym, src"new scala.lms.tutorial.Scanner($fn)")
    case ScannerNext(s, d) => emitValDef(sym, src"$s.next($d)")
    case ScannerHasNext(s) => emitValDef(sym, src"$s.hasNext")
    case ScannerClose(s) => emitValDef(sym, src"$s.close")
    case _ => super.emitNode(sym, rhs)
  }
}

trait ScannerLowerBase extends Base with UncheckedOps { this: Dsl =>
  def open(name: Rep[String]): Rep[Int]
  def close(fd: Rep[Int]): Rep[Unit]
  def filelen(fd: Rep[Int]): Rep[Int]
  def mmap[T:Typ](fd: Rep[Int], len: Rep[Int]): Rep[Array[T]]
  def stringFromCharArray(buf: Rep[Array[Char]], pos: Rep[Int], len: Rep[Int]): Rep[String]
  def prints(s: Rep[String]): Rep[Int]
  def infix_toInt(c: Rep[Char]): Rep[Int] = c.asInstanceOf[Rep[Int]]
}

trait ScannerLowerExp extends ScannerLowerBase with UncheckedOpsExp { this: DslExp =>
  def open(name: Rep[String]) = uncheckedPure[Int]("open(",name,",0)")
  def close(fd: Rep[Int]) = unchecked[Unit]("close(",fd,")")
  def filelen(fd: Rep[Int]) = uncheckedPure[Int]("fsize(",fd,")") // FIXME: fresh name
  def mmap[T:Typ](fd: Rep[Int], len: Rep[Int]) = uncheckedPure[Array[T]]("mmap(0, ",len,", PROT_READ, MAP_FILE | MAP_SHARED, ",fd,", 0)")
  def stringFromCharArray(data: Rep[Array[Char]], pos: Rep[Int], len: Rep[Int]): Rep[String] = uncheckedPure[String](data,"+",pos)
  def prints(s: Rep[String]): Rep[Int] = unchecked[Int]("printll(",s,")")
}

trait CGenScannerLower extends CGenUncheckedOps
