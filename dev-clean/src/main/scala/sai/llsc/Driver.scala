package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._

trait CppSymStagedLLVMDriver[A, B] extends CppSAIDriver[A, B] with LLSCEngine { q =>
  override val codegen = new CGenBase with SymStagedLLVMGen {
    val IR: q.type = q
    import IR._

    override def primitive(t: String): String = t match {
      case "Unit" => "std::monostate"
      case _ => super.primitive(t)
    }

    // TODO: move to code gen?
    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "PtrVal"
      else if (m.toString.endsWith("$Addr")) "Addr"
      else if (m.toString.endsWith("$Mem")) "Mem"
      else if (m.toString.endsWith("$SS")) "SS"
      else if (m.toString.endsWith("SMTExpr")) "Expr"
      else super.remap(m)
    }
  }
}

object TestStagedSymExec {
  @virtualize
  def specialize(m: Module, fname: String): CppSAIDriver[Int, Unit] =
    new CppSymStagedLLVMDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        val args: Rep[List[Value]] = List[Value](
          SymV("x0"), SymV("x1"), SymV("x2"), 
          SymV("x3"), SymV("x4"), SymV("x5"),
          SymV("x6"), SymV("x7"), SymV("x8"),
          SymV("x9"),
          SymV("x10"), SymV("x11"),
          SymV("x12"), SymV("x13"), SymV("x14"),
          SymV("x15"),
          SymV("x16"), SymV("x17"),
          SymV("x18"), SymV("x19")
        )
        val res = exec(m, fname, args)
        // query a single test
        //res.head._1.pc.toList.foreach(assert(_))
        //handle(query(lit(false)))

        println(res.size)
      }
    }

  def testModule(m: Module, output: String, fname: String) {
    val res = sai.utils.Utils.time {
      val code = specialize(m, fname)
      code.save(s"llsc_gen/$output")
      code.compile(s"llsc_gen/$output")
    }
    println(res)
    //code.eval(0)
  }

  def main(args: Array[String]): Unit = {
    //testModule(sai.llvm.Benchmarks.add, "add.cpp", "@add")
    testModule(sai.llvm.OOPSLA20Benchmarks.mp1048576, "mp1048576_sym.cpp", "@f")
  }
}
