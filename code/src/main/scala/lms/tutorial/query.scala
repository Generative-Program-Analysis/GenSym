/**
A SQL Query Compiler
====================

Commercial and open source database systems consist of millions of lines of highly 
optimized C code. Yet, their performance on individual queries falls 10x or 100x 
short of what a hand-written, specialized, implementation of the same query can 
achieve.

In this tutorial, we will build a small SQL processing engine that consists of 
just about 500 lines of high-level Scala code. Whereas other systems interpret query 
plans, operator by operator, we will use LMS to generate and compile low-level C 
code for entire queries.

We keep the query functionality intentionally simple. A more complete engine
that handles the full TPCH benchmark suite and consists of about 3000 lines of
code has been developed in the [LegoBase](http://data.epfl.ch/legobase) project, which recently received
a best paper award at VLDB'14.

See also:

- Building Efficient Query Engines in a High-Level Language ([PDF](http://infoscience.epfl.ch/record/198693/files/801-klonatos.pdf))
  Yannis Klonatos, Christoph Koch, Tiark Rompf, Hassan Chafi. VLDB'14
- Functional pearl: A SQL to C Compiler in 500 Lines of Code ([PDF](https://www.cs.purdue.edu/homes/rompf/papers/rompf-icfp15.pdf))
  Tiark Rompf, Nada Amin. ICFP'15

Outline:
<div id="tableofcontents"></div>


Setting the Stage
-----------------

Let us run a few quick benchmarks to get an idea of the relative performance of different
data processing systems. We take a data sample from [the Google Books NGram Viewer](https://books.google.com/ngrams) project.
The 2GB file that contains statistics for words starting with the letter 'A' is a good candidate to run
some simple queries. We might be interested in all occurrences of the word 'Auswanderung':

    select * from 1gram_a where n_gram = 'Auswanderung'

Here are some timings:

- Loading the CSV file into a MySQL database takes > 5 minutes, running the query about 50 seconds.

- PostgreSQL takes 3 minutes to load, the first query run takes 46 seconds, but subsequent runs get faster over time (down to 7 seconds).

- An [AWK script](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand.awk) that processes the CSV file directly takes 45 seconds.

- A [query interpreter](https://github.com/scala-lms/tutorials/blob/master/src/test/scala/lms/tutorial/query_unstaged.scala) written in Scala takes 39 sec.

- A hand-written specialized [Scala program](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand0.scala/) takes 13 sec.

- A similar hand-written [C program](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand.c/) performs marginally faster,
  but with [more optimizations](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand2.c/) we can get as good as 3.2 seconds.

The query processor we will develop in this tutorial matches the performance of the handwritten Scala and C queries (13s and 3s, respectively).

More details on running the benchmarks are available [here](https://github.com/scala-lms/tutorials/blob/master/src/data/README.md). We now turn to our actual implementation.

*/

package scala.lms.tutorial

import scala.lms.common._

/**
Relational Algebra AST
----------------------

The core of any query processing engine is an AST representation of
relational algebra operators.
*/
trait QueryAST {
  type Table
  type Schema = Vector[String]

  // relational algebra ops
  sealed abstract class Operator
  case class Scan(name: Table, schema: Schema, delim: Char, extSchema: Boolean) extends Operator
  case class PrintCSV(parent: Operator) extends Operator
  case class Project(outSchema: Schema, inSchema: Schema, parent: Operator) extends Operator
  case class Filter(pred: Predicate, parent: Operator) extends Operator
  case class Join(parent1: Operator, parent2: Operator) extends Operator
  case class Group(keys: Schema, agg: Schema, parent: Operator) extends Operator
  case class HashJoin(parent1: Operator, parent2: Operator) extends Operator

  // filter predicates
  sealed abstract class Predicate
  case class Eq(a: Ref, b: Ref) extends Predicate

  sealed abstract class Ref
  case class Field(name: String) extends Ref
  case class Value(x: Any) extends Ref

  // some smart constructors
  def Schema(schema: String*): Schema = schema.toVector
  def Scan(tableName: String): Scan = Scan(tableName, None, None)
  def Scan(tableName: String, schema: Option[Schema], delim: Option[Char]): Scan
}


/**
SQL Parser
----------

We add a parser that takes a SQL(-like) string and converts it to tree of operators.
*/

trait SQLParser extends QueryAST {
  import scala.util.parsing.combinator._

  def parseSql(input: String) = Grammar.parseAll(input)

  object Grammar extends JavaTokenParsers with PackratParsers {

    def stm: Parser[Operator] = 
      selectClause ~ fromClause ~ whereClause ~ groupClause ^^ {
        case p ~ s ~ f ~ g => g(p(f(s))) }
    def selectClause: Parser[Operator=>Operator] =
      "select" ~> ("*" ^^ { _ => (op:Operator) => op } | fieldList ^^ { 
        case (fs,fs1) => Project(fs,fs1,_:Operator) })
    def fromClause: Parser[Operator] =
      "from" ~> joinClause
    def whereClause: Parser[Operator=>Operator] =
      opt("where" ~> predicate ^^ { p => Filter(p, _:Operator) }) ^^ { _.getOrElse(op => op)}
    def groupClause: Parser[Operator=>Operator] =
      opt("group" ~> "by" ~> fieldIdList ~ ("sum" ~> fieldIdList) ^^ { 
        case p1 ~ p2 => Group(p1,p2, _:Operator) }) ^^ { _.getOrElse(op => op)}

    def joinClause: Parser[Operator] =
      ("nestedloops" ~> repsep(tableClause, "join") ^^ { _.reduceLeft((a,b) => Join(a,b)) }) |
      (repsep(tableClause, "join") ^^ { _.reduceLeft((a,b) => HashJoin(a,b)) })
    def tableClause: Parser[Operator] =
      tableIdent ~ opt("schema" ~> fieldIdList) ~ 
        opt("delim" ~> ("""\t""" ^^ (_ => '\t') | """.""".r ^^ (_.head))) ^^ {
          case table ~ schema ~ delim => Scan(table, schema, delim) } |
      ("(" ~> stm <~ ")")

    def fieldIdent: Parser[String] = """[\w\#]+""".r
    def tableIdent: Parser[String] = """[\w_\-/\.]+""".r | "?"
    def fieldList:  Parser[(Schema,Schema)] =
      repsep(fieldIdent ~ opt("as" ~> fieldIdent), ",") ^^ { fs2s =>
        val (fs,fs1) = fs2s.map { case a~b => (b.getOrElse(a),a) }.unzip
        (Schema(fs:_*),Schema(fs1:_*)) }
    def fieldIdList:  Parser[Schema] = 
      repsep(fieldIdent,",") ^^ (fs => Schema(fs:_*))

    def predicate: Parser[Predicate] = 
      ref ~ "=" ~ ref ^^ { case a ~ _ ~ b => Eq(a,b) }
    def ref: Parser[Ref] = 
      fieldIdent ^^ Field | 
      """'[^']*'""".r ^^ (s => Value(s.drop(1).dropRight(1))) |
      """[0-9]+""".r ^^ (s => Value(s.toInt))
  
    def parseAll(input: String): Operator = parseAll(stm,input) match {
      case Success(res,_)  => res
      case res => throw new Exception(res.toString)
    }
  }
}



/**
Iterative Development of a Query Processor
------------------------------------------

We develop our SQL engine in multiple steps. Each steps leads to a
working processor, and each successive step either adds a feature or
optimization. 

### Step 1: A (Plain) Query Interpreter

We start with a plain query processor: an interpreter.

- [query_unstaged.scala](query_unstaged.html)


### Step 2: A Staged Query Interpreter (= Compiler)

Staging our query interpreter yields a query compiler.
In the first iteration we generate Scala code but we disregard 
operators that require internal data structures:

- [query_staged0](query_staged0.html)


### Step 3: Specializing Data Structures

The next iteration adds optimized data structure implementations
that follow a column-store layout. This includes specialized hash 
tables for groupBy and join operators:

- [query_staged](query_staged.html)


### Step 4: Switching to C and Optimizing IO

For additional low-level optimizations we switch to generating C 
code:

- [query_optc](query_optc.html)

On the C level, we optimize the IO layer by mapping files directly
into memory and we further specialize internal data structures
to minimize data conversions and to enable representing string objects
directly as pointers into the memory-mapped input file.


Plumbing
--------

To actually run queries and test the different implementations side
by side, a little bit of plumbing is necessary. We define a common 
interface for all query processors (plain or staged, Scala or C).

*/
trait QueryProcessor extends QueryAST {
  def version: String
  val defaultFieldDelimiter = ','

  def filePath(table: String): String = table
  def dynamicFilePath(table: String): Table

  def Scan(tableName: String, schema: Option[Schema], delim: Option[Char]): Scan = {
    val dfile = dynamicFilePath(tableName)
    val (schema1, externalSchema) = schema.map(s=>(s,true)).getOrElse((loadSchema(filePath(tableName)),false))
    Scan(dfile, schema1, delim.getOrElse(defaultFieldDelimiter), externalSchema)
  }

  def loadSchema(filename: String): Schema = {
    val s = new Scanner(filename)
    val schema = Schema(s.next('\n').split(defaultFieldDelimiter): _*)
    s.close
    schema
  }

  def execQuery(q: Operator): Unit
}

trait PlainQueryProcessor extends QueryProcessor {
  type Table = String
}

trait StagedQueryProcessor extends QueryProcessor with Dsl {
  type Table = Rep[String] // dynamic filename
  override def filePath(table: String) = if (table == "?") throw new Exception("file path for table ? not available") else super.filePath(table)
}


/**
Interactive Mode
----------------

Examples:

    test:run unstaged "select * from ? schema Phrase, Year, MatchCount, VolumeCount delim \\t where Phrase='Auswanderung'" src/data/t1gram.csv
    test:run c        "select * from ? schema Phrase, Year, MatchCount, VolumeCount delim \\t where Phrase='Auswanderung'" src/data/t1gram.csv
*/
trait Engine extends QueryProcessor with SQLParser {
  def query: String
  def filename: String
  def liftTable(n: String): Table
  def eval: Unit
  def prepare: Unit = {}
  def run: Unit = execQuery(PrintCSV(parseSql(query)))
  override def dynamicFilePath(table: String): Table =
    liftTable(if (table == "?") filename else filePath(table))
  def evalString = {
    val source = new java.io.ByteArrayOutputStream()
    utils.withOutputFull(new java.io.PrintStream(source)) {
      eval
    }
    source.toString
  }
}
trait StagedEngine extends Engine with StagedQueryProcessor {
  override def liftTable(n: String) = unit(n)
}

object Run {
  var qu: String = _
  var fn: String = _

  trait MainEngine extends Engine {
    override def query = qu
    override def filename =  fn
  }

  def unstaged_engine: Engine =
    new Engine with MainEngine with query_unstaged.QueryInterpreter {
      override def liftTable(n: Table) = n
      override def eval = run
    }
  def scala_engine =
    new DslDriver[String,Unit] with ScannerExp
    with StagedEngine with MainEngine with query_staged.QueryCompiler { q =>
      override val codegen = new DslGen with ScalaGenScanner {
        val IR: q.type = q
      }
      override def snippet(fn: Table): Rep[Unit] = run
      override def prepare: Unit = precompile
      override def eval: Unit = eval(filename)
    }
  def c_engine =
    new DslDriverC[String,Unit] with ScannerLowerExp
    with StagedEngine with MainEngine with query_optc.QueryCompiler { q =>
      override val codegen = new DslGenC with CGenScannerLower {
        val IR: q.type = q
      }
      override def snippet(fn: Table): Rep[Unit] = run
      override def prepare: Unit = {}
      override def eval: Unit = eval(filename)
    }

  def main(args: Array[String]) {
    if (args.length < 2) {
      println("syntax:")
      println("   test:run (unstaged|scala|c) sql [file]")
      println()
      println("example usage:")
      println("   test:run c \"select * from ? schema Phrase, Year, MatchCount, VolumeCount delim \\t where Phrase='Auswanderung'\" src/data/t1gram.csv")
      return
    }
    val version = args(0)
    val engine = version match {
      case "c" => c_engine
      case "scala" => scala_engine
      case "unstaged" => unstaged_engine
      case _ => println("warning: unexpected engine, using 'unstaged' by default")
        unstaged_engine
    }
    qu = args(1)
    if (args.length > 2)
      fn = args(2)

    try {
      engine.prepare
      utils.time(engine.eval)
    } catch {
      case ex: Exception =>
        println("ERROR: " + ex)
    }
  }
}


/**

Unit Tests
----------

*/
class QueryTest extends TutorialFunSuite {
  val under = "query_"

  trait TestDriver extends SQLParser with QueryProcessor with ExpectedASTs {
    def runtest: Unit
    override def filePath(table: String) = dataFilePath(table)

    def name: String
    def query: String
    def parsedQuery: Operator = if (query.isEmpty) expectedAstForTest(name) else parseSql(query)
  }

  trait PlainTestDriver extends TestDriver with PlainQueryProcessor {
    override def dynamicFilePath(table: String): Table = if (table == "?") defaultEvalTable else filePath(table)
    def eval(fn: Table): Unit = {
      execQuery(PrintCSV(parsedQuery))
    }
  }

  trait StagedTestDriver extends TestDriver with StagedQueryProcessor {
    var dynamicFileName: Table = _
    override def dynamicFilePath(table: String): Table = if (table == "?") dynamicFileName else unit(filePath(table))
    def snippet(fn: Table): Rep[Unit] = {
      dynamicFileName = fn
      execQuery(PrintCSV(parsedQuery))
    }
  }

  abstract class ScalaPlainQueryDriver(val name: String, val query: String) extends PlainTestDriver with QueryProcessor { q =>
    override def runtest: Unit = {
      test(version+" "+name) {
        for (expectedParsedQuery <- expectedAstForTest.get(name)) {
          assert(expectedParsedQuery==parsedQuery)
        }
        checkOut(name, "csv", eval(defaultEvalTable))
      }
    }
  }

  abstract class ScalaStagedQueryDriver(val name: String, val query: String) extends DslDriver[String,Unit] with StagedTestDriver with StagedQueryProcessor with ScannerExp { q =>
    override val codegen = new DslGen with ScalaGenScanner {
      val IR: q.type = q
    }

    override def runtest: Unit = {
      if (version == "query_staged0" && List("Group","HashJoin").exists(parsedQuery.toString contains _)) return ()
      test(version+" "+name) {
        for (expectedParsedQuery <- expectedAstForTest.get(name)) {
          assert(expectedParsedQuery==parsedQuery)
        }
        check(name, code)
        precompile
        checkOut(name, "csv", eval(defaultEvalTable))
      }
    }
  }

  abstract class CStagedQueryDriver(val name: String, val query: String) extends DslDriverC[String,Unit] with StagedTestDriver with StagedQueryProcessor with ScannerLowerExp { q =>
    override val codegen = new DslGenC with CGenScannerLower {
      val IR: q.type = q
    }
    override def runtest: Unit = {
      test(version+" "+name) {
        for (expectedParsedQuery <- expectedAstForTest.get(name)) {
          assert(expectedParsedQuery==parsedQuery)
        }
        check(name, code, "c")
        //precompile
        checkOut(name, "csv", eval(defaultEvalTable))
      }
    }
  }

  def testquery(name: String, query: String = "") {
    val drivers: List[TestDriver] =
      List(
        new ScalaPlainQueryDriver(name, query) with query_unstaged.QueryInterpreter,
        new ScalaStagedQueryDriver(name, query) with query_staged0.QueryCompiler,
        new ScalaStagedQueryDriver(name, query) with query_staged.QueryCompiler,
        new CStagedQueryDriver(name, query) with query_optc.QueryCompiler {
          // FIXME: hack so i don't need to replace Value -> #Value in all the files right now
          override def isNumericCol(s: String) = s == "Value" || super.isNumericCol(s)
        }
      )
    drivers.foreach(_.runtest)
  }

  // NOTE: we can use "select * from ?" to use dynamic file names (not used here right now)

  trait ExpectedASTs extends QueryAST {
    val scan_t = Scan("t.csv")
    val scan_t1gram = Scan("?",Some(Schema("Phrase", "Year", "MatchCount", "VolumeCount")),Some('\t'))

    val expectedAstForTest = Map(
      "t1" -> scan_t,
      "t2" -> Project(Schema("Name"), Schema("Name"), scan_t),
      "t3" -> Project(Schema("Name"), Schema("Name"),
                      Filter(Eq(Field("Flag"), Value("yes")),
                             scan_t)),
      "t4" -> Join(scan_t,
                   Project(Schema("Name1"), Schema("Name"), scan_t)),
      "t5" -> Join(scan_t,
                   Project(Schema("Name"), Schema("Name"), scan_t)),
      "t4h" -> HashJoin(scan_t,
                   Project(Schema("Name1"), Schema("Name"), scan_t)),
      "t5h" -> HashJoin(scan_t,
                   Project(Schema("Name"), Schema("Name"), scan_t)),
      "t6"  -> Group(Schema("Name"),Schema("Value"), scan_t),

      "t1gram1" -> scan_t1gram,
      "t1gram2" -> Filter(Eq(Field("Phrase"), Value("Auswanderung")), scan_t1gram)
    )
  }

  testquery("t1", "select * from t.csv")
  testquery("t2", "select Name from t.csv")
  testquery("t3", "select Name from t.csv where Flag='yes'")
  testquery("t4", "select * from nestedloops t.csv join (select Name as Name1 from t.csv)")
  testquery("t5", "select * from nestedloops t.csv join (select Name from t.csv)")
  testquery("t4h","select * from t.csv join (select Name as Name1 from t.csv)")
  testquery("t5h","select * from t.csv join (select Name from t.csv)")
  testquery("t6", "select * from t.csv group by Name sum Value") // not 100% right syntax, but hey ...

  val defaultEvalTable = dataFilePath("t1gram.csv")
  val t1gram = "? schema Phrase, Year, MatchCount, VolumeCount delim \\t"
  testquery("t1gram1", s"select * from $t1gram")
  testquery("t1gram2", s"select * from $t1gram where Phrase='Auswanderung'")
  testquery("t1gram2n", s"select * from nestedloops words.csv join (select Phrase as Word, Year, MatchCount, VolumeCount from $t1gram)")
  testquery("t1gram2h", s"select * from words.csv join (select Phrase as Word, Year, MatchCount, VolumeCount from $t1gram)")
  testquery("t1gram3", s"select * from nestedloops words.csv join (select * from $t1gram)")
  testquery("t1gram3h", s"select * from words.csv join (select * from $t1gram)")
  testquery("t1gram4", s"select * from nestedloops words.csv join (select Phrase as Word, Year, MatchCount, VolumeCount from $t1gram)")
  testquery("t1gram4h", s"select * from words.csv join (select Phrase as Word, Year, MatchCount, VolumeCount from $t1gram)")
}



/**
Suggestions for Exercises
-------------------------

The query engine we presented is decidedly simple, so as to present an
end-to-end system that can be understood in total. Below are a few
suggestions for interesting extensions.

- Implement a scanner that reads on demand from a URL.

  (Cool with: a new operator that only prints the first N results.)

- (easy) Implement a typed schema in the Scala version, so that the
  types of columns are statically known, while the values are not.

  (Hint: the C version already does this, but is also more involved
  because of the custom type representations.)

- (easy) Implement more predicates (e.g. `LessThan`) and predicate
  combinators (e.g. `And`, `Or`) in order to run more interesting
  queries.

- (medium) Implement a real column-oriented database, where each column has its
  own file so that it can be read independently.

- (hard) Implement an optimizer on the relational algebra before generating code.
  (Hint: smart constructors might help.)

  The query optimizer should rearrange query operator trees for a better join ordering, i.e. decide whether to execute joins on relations S0 x S1 x S2 as  (S0 x (S1 x S2)) vs ((S0 x S1) x S2).

  Use a dynamic programming algorithm, that for n joins on tables S0 x S1 x ...x Sn tries to find an optimal solution for S1 x .. x Sn first, and then the optimal combination with S0. 

  To find an optimal combination, try all alternatives and estimate the cost of each. Cost can be measured roughly as number of records processed. As a simple approximation, you can use the size of each input table and assume that all filter predicates match uniformly with probability 0.5.


*/
