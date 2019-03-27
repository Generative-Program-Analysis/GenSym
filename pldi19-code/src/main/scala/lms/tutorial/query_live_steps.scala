package scala.lms.tutorial

import scala.lms.common._


class QueryLiveStepsTest extends TutorialFunSuite {
  val under = "query_live_"

  test("demo0") {

    def run = {
      println("When I grow up I'm gonna be a SQL engine!")
    }

    exec("data", utils.captureOut(run), suffix="csv")
  }




  test("demo1") {

    def run = {
      println("room,title")
      val in = new Scanner("src/data/talks.csv")
      in.next('\n')
      while (in.hasNext) {
        val tid   = in.next(',')
        val time  = in.next(',')
        val title = in.next(',')
        val room  = in.next('\n')
        if (time == "09:00 AM")
          printf("%s,%s\n",room,title)
      }
      in.close
    }

    exec("data", utils.captureOut(run), suffix="csv")
  }




  test("demo2") {

    type Schema = Vector[String]
    case class Record(fields: Vector[String], schema: Vector[String]) {
      def apply(name: String) = fields(schema indexOf name)
    }

    def run = {
      println("room,title")
      val in = new Scanner("src/data/talks.csv")

      val schema = in.next('\n').split(",").toVector

      while (in.hasNext) {
        val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
        val rec = Record(fields, schema)

        if (rec("time") == "09:00 AM")
          printf("%s,%s\n",rec("room"),rec("title"))
      }
      in.close
    }

    exec("data", utils.captureOut(run), suffix="csv")
  }


  test("demo3") {

    type Schema = Vector[String]
    case class Record(fields: Vector[String], schema: Vector[String]) {
      def apply(name: String) = fields(schema indexOf name)
    }

    def processCSV(file: String)(yld: Record => Unit): Unit = {
      val in = new Scanner("src/data/talks.csv")
      val schema = in.next('\n').split(",").toVector
      while (in.hasNext) {
        val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
        yld(Record(fields, schema))
      }
    }

    def run = {
      println("room,title")

      processCSV("src/data/talks.csv") { rec =>
        if (rec("time") == "09:00 AM")
          printf("%s,%s\n",rec("room"),rec("title"))
      }
    }

    exec("data", utils.captureOut(run), suffix="csv")
  }



  test("demo4") {

    object Engine extends SimpleQueryProcessor with SQLParser {

      case class Record(fields: Vector[String], schema: Vector[String]) {
        def apply(name: String) = fields(schema indexOf name)
      }

      def processCSV(file: String)(yld: Record => Unit): Unit = {
        val in = new Scanner(file)
        val schema = in.next('\n').split(",").toVector
        while (in.hasNext) {
          val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
          yld(Record(fields, schema))
        }
      }

      def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
        case Scan(file, _, _, _)      => processCSV(file)(yld)
        case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
      }


      def printFields(fields: Vector[String]) = printf(fields.map(_ => "%s").mkString(",")+"\n", fields: _*)

      def run = {
        val ops = parseSql("select * from src/data/talks.csv")
        
        println(ops)
        println("---")
        execOp(PrintCSV(ops)) { _ => }
      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }


  test("demo5") {

    object Engine extends SimpleQueryProcessor with SQLParser {

      case class Record(fields: Vector[String], schema: Vector[String]) {
        def apply(name: String): String          = fields(schema indexOf name)
        def apply(names: Schema): Vector[String] = names map (this apply _)
      }

      def processCSV(file: String)(yld: Record => Unit): Unit = {
        val in = new Scanner(file)
        val schema = in.next('\n').split(",").toVector
        while (in.hasNext) {
          val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
          yld(Record(fields, schema))
        }
      }

      def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
        case Scan(file, _, _, _)      => processCSV(file)(yld)
        case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
        case Project(outSchema, inSchema, parent) =>
          execOp(parent) { rec => yld(Record(rec(inSchema), outSchema))}
      }


      def printFields(fields: Vector[String]): Unit = printf(fields.map(_ => "%s").mkString("",",","\n"), fields: _*)

      def run = {
        val ops = parseSql("select time,room,title from src/data/talks.csv")
        
        println(ops)
        println("---")
        execOp(PrintCSV(ops)) { _ => }
      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }



  test("demo6") {

    object Engine extends SimpleQueryProcessor with SQLParser {

      case class Record(fields: Vector[String], schema: Vector[String]) {
        def apply(name: String): String          = fields(schema indexOf name)
        def apply(names: Schema): Vector[String] = names map (this apply _)
      }

      def processCSV(file: String)(yld: Record => Unit): Unit = {
        val in = new Scanner(file)
        val schema = in.next('\n').split(",").toVector
        while (in.hasNext) {
          val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
          yld(Record(fields, schema))
        }
      }

      def evalRef(p: Ref)(rec: Record) = p match {
        case Value(a) => a
        case Field(name) => rec(name)
      }

      def evalPred(p: Predicate)(rec: Record) = p match {
        case Eq(a,b) => evalRef(a)(rec) == evalRef(b)(rec)
      }

      def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
        case Scan(file, _, _, _)      => processCSV(file)(yld)
        case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
        case Project(outSchema, inSchema, parent) =>
          execOp(parent) { rec => yld(Record(rec(inSchema), outSchema))}
        case Filter(pred, parent) =>
          execOp(parent) { rec => if (evalPred(pred)(rec)) yld(rec) }
      }


      def printFields(fields: Vector[String]) = printf(fields.map(_ => "%s").mkString("",",","\n"), fields: _*)

      def run = {
        val ops = parseSql("select time,room,title from src/data/talks.csv where time = '09:00 AM'")
        
        println(ops)
        println("---")
        execOp(PrintCSV(ops)) { _ => }
      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }



  test("demo7") {

    object Engine extends SimpleQueryProcessor with SQLParser {

      case class Record(fields: Vector[String], schema: Vector[String]) {
        def apply(name: String): String          = fields(schema indexOf name)
        def apply(names: Schema): Vector[String] = names map (this apply _)
      }

      def processCSV(file: String)(yld: Record => Unit): Unit = {
        val in = new Scanner(file)
        val schema = in.next('\n').split(",").toVector
        while (in.hasNext) {
          val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
          yld(Record(fields, schema))
        }
      }

      def evalRef(p: Ref)(rec: Record) = p match {
        case Value(a) => a
        case Field(name) => rec(name)
      }

      def evalPred(p: Predicate)(rec: Record) = p match {
        case Eq(a,b) => evalRef(a)(rec) == evalRef(b)(rec)
      }

      def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
        case Scan(file, _, _, _)      => processCSV(file)(yld)
        case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
        case Project(outSchema, inSchema, parent) =>
          execOp(parent) { rec => yld(Record(rec(inSchema), outSchema))}
        case Filter(pred, parent) =>
          execOp(parent) { rec => if (evalPred(pred)(rec)) yld(rec) }
      }


      def printFields(fields: Vector[String]) = printf(fields.map(_ => "%s").mkString("",",","\n"), fields: _*)

      def run = {
        val ops = parseSql("select time,room,title from src/data/talks.csv where time = '09:00 AM'")
        
        println(ops)
        println("---")
        execOp(PrintCSV(ops)) { _ => }
      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }


  // staging!

  test("demo8") {

    object Engine extends SimpleQueryProcessor with SQLParser {

      case class Record(fields: Vector[String], schema: Vector[String]) {
        def apply(name: String): String          = fields(schema indexOf name)
        def apply(names: Schema): Vector[String] = names map (this apply _)
      }

      def processCSV(file: String)(yld: Record => Unit): Unit = {
        val in = new Scanner(file)
        val schema = in.next('\n').split(",").toVector
        while (in.hasNext) {
          val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
          yld(Record(fields, schema))
        }
      }

      def evalRef(p: Ref)(rec: Record) = p match {
        case Value(a) => a
        case Field(name) => rec(name)
      }

      def evalPred(p: Predicate)(rec: Record) = p match {
        case Eq(a,b) => evalRef(a)(rec) == evalRef(b)(rec)
      }

      def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
        case Scan(file, _, _, _)      => processCSV(file)(yld)
        case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
        case Project(outSchema, inSchema, parent) =>
          execOp(parent) { rec => yld(Record(rec(inSchema), outSchema))}
        case Filter(pred, parent) =>
          execOp(parent) { rec => if (evalPred(pred)(rec)) yld(rec) }
      }


      def printFields(fields: Vector[String]) = printf(fields.map(_ => "%s").mkString("",",","\n"), fields: _*)

      def run = {

        val snippet = new LMS_Driver[Int,Unit] {
          def snippet(x: Rep[Int]): Rep[Unit] = {
            if (x < 4)
              println("hello")
            else
              printf("world")
          }
        }

        println(snippet.code)
        println("--- now running: ---")

        println(snippet.eval(4))

/*
        val ops = parseSql("select time,room,title from src/data/talks.csv where time = '09:00 AM'")
        
        println(ops)
        println("---")
        execOp(PrintCSV(ops)) { _ => }
*/
      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }


  test("demo9") {

    object Engine extends SimpleQueryProcessor with SQLParser {
      def run = {
        val snippet = new LMS_Driver[Int,Unit] {

          case class Record(fields: Vector[Rep[String]], schema: Vector[String]) {
            def apply(name: String): Rep[String]          = fields(schema indexOf name)
            def apply(names: Schema): Vector[Rep[String]] = names map (this apply _)
          }

          def processCSV(file: String, schema: Vector[String])(yld: Record => Unit): Unit = {
            val in = newScanner(file)
            in.next('\n')
            while (in.hasNext) {
              val fields = schema map (n => in.next(if (n == schema.last) '\n' else ','))
              yld(Record(fields, schema))
            }
          }

          def evalRef(p: Ref)(rec: Record): Rep[String] = p match {
            case Value(a: String) => a
            case Field(name) => rec(name)
          }

          def evalPred(p: Predicate)(rec: Record): Rep[Boolean] = p match {
            case Eq(a,b) => evalRef(a)(rec) == evalRef(b)(rec)
          }

          def execOp(op: Operator)(yld: Record => Unit): Unit = op match {
            case Scan(file, schema, _, _) => processCSV(file,schema)(yld)
            case PrintCSV(parent)         => execOp(parent) { rec => printFields(rec.fields) }
            case Project(outSchema, inSchema, parent) =>
              execOp(parent) { rec => yld(Record(rec(inSchema), outSchema))}
            case Filter(pred, parent) =>
              execOp(parent) { rec => if (evalPred(pred)(rec)) yld(rec) }
          }

          def printFields(fields: Vector[Rep[String]]) = printf(fields.map(_ => "%s").mkString("",",","\n"), fields: _*)

          def snippet(x: Rep[Int]): Rep[Unit] = {
            val ops = parseSql("select time,room,title from src/data/talks.csv where time = '09:00 AM'")
            execOp(PrintCSV(ops)) { _ => }
          }
        }

        println(snippet.code)
        println("--- now running: ---")
        snippet.precompile
        utils.time {
          snippet.eval(4)
        }

      }

    }

    exec("data", utils.captureOut(Engine.run), suffix="csv")
  }


  trait SimpleQueryProcessor extends PlainQueryProcessor with SQLParser {
    def dynamicFilePath(table: String): Table = table
    def execQuery(q: Operator): Unit = ???
    def version: String = "simple"
  }


  abstract class LMS_Driver[A:Manifest,B:Manifest] extends DslDriver[A,B] with ScannerExp { q =>
    override val codegen = new DslGen with ScalaGenScanner {
      val IR: q.type = q
    }
  }

  /*abstract class LMS_Driver[A:Typ,B:Typ] extends DslDriver[A,B] with ScannerExp
    with StagedEngine with MainEngine with query_staged.QueryCompiler { q =>
      override val codegen = new DslGen with ScalaGenScanner {
        val IR: q.type = q
      }
  }*/


}
