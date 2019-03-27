package scala.lms.tutorial

import scala.lms.common._

object query_live {
trait QueryInterpreter extends PlainQueryProcessor {
  override def version = "query_live"

  def execQuery(q: Operator): Unit = {
    println("hello")
  }
}
}

class QueryLiveTest extends TutorialFunSuite {
  val under = "query_live_"

  def engine =
    new Engine with query_live.QueryInterpreter {
      override def liftTable(n: Table) = n
      override def eval = run

    //trait QueryCompiler extends Dsl with StagedQueryProcessor with ScannerBase {
    /**
    new DslDriver[String,Unit] with ScannerExp with StagedEngine with query_live.QueryCompiler { q =>
      override val codegen = new DslGen with ScalaGenScanner {
        val IR: q.type = q
      }
      override def prepare: Unit = precompile
    */
    //trait QueryCompiler extends Dsl with StagedQueryProcessor with ScannerLowerBase {
    /**
    new DslDriverC[String,Unit] with ScannerLowerExp with StagedEngine with query_live.QueryCompiler { q =>
      override val codegen = new DslGenC with CGenScannerLower {
        val IR: q.type = q
      }
    */
    /**
      override def snippet(fn: Table): Rep[Unit] = run
      override def eval = eval(filename)
    */
      override def query = s"select * from $t1gram"
      override def filename = dataFilePath("t1gram.csv")
      val t1gram = "? schema Phrase, Year, MatchCount, VolumeCount delim \\t"

    }
  test("demo") {
    //exec("code", engine.code, suffix="scala")
    exec("data", engine.evalString, suffix="csv")
  }
}
