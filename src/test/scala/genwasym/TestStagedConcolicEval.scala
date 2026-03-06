package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import java.io.{File, PrintWriter}
import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

class TestStagedConcolicEval extends FunSuite {
  import sys.process._

  private def firstExistingDir(candidates: Seq[String]): Option[String] =
    candidates.map(path => new File(path)).find(file => file.isDirectory).map(_.getCanonicalPath)

  // Get the z3 include path from the GenSym project root
  private lazy val z3IncludeDir: String = {
    val fromEnv = sys.env.get("Z3_INCLUDE_DIR")
    val fromRepo = firstExistingDir(
      Seq(
        "./third-party/z3/build/z3_install/include",
        "./third-party/z3/src/api/c++"
      )
    )
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate Z3 include directory. Set Z3_INCLUDE_DIR or build third-party/z3."
      )
    }
  }

  private lazy val z3LibDir: String = {
    val fromEnv = sys.env.get("Z3_LIB_DIR")
    val fromRepo = firstExistingDir(Seq("./third-party/z3/build/z3_install/lib"))
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate Z3 library directory. Set Z3_LIB_DIR or build third-party/z3."
      )
    }
  }

  private lazy val immerIncludeDir: String = {
    val fromEnv = sys.env.get("IMMER_INCLUDE_DIR")
    val fromRepo = firstExistingDir(Seq("./third-party/immer",
                                        "./GenSym/third-party/immer"
                                        ))
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate immer header directory. Set IMMER_INCLUDE_DIR or initialize third-party/immer."
      )
    }
  }

  private def prependPath(existing: Option[String], prefix: String): String =
    existing.filter(_.nonEmpty).map(old => s"$prefix:$old").getOrElse(prefix)

  private lazy val extraIncludeDirs: Seq[String] =
    Seq(immerIncludeDir)
      .map(path => new File(path))
      .filter(_.isDirectory)
      .map(_.getCanonicalPath)

  private lazy val exeEnv: Seq[(String, String)] = Seq(
    "LD_LIBRARY_PATH" -> prependPath(sys.env.get("LD_LIBRARY_PATH"), z3LibDir),
    "DYLD_LIBRARY_PATH" -> prependPath(sys.env.get("DYLD_LIBRARY_PATH"), z3LibDir)
  )

  private def compileToExeWithZ3(moduleInst: ModuleInstance,
                                 main: Option[String],
                                 inputCpp: String,
                                 outputExe: String,
                                 printRes: Boolean,
                                 optimizeLevel: Int,
                                 macros: String*): Unit = {
    val generated = WasmToCppCompiler.compile(moduleInst, main, printRes)
    val writer = new PrintWriter(new File(inputCpp))
    try {
      writer.write(generated.source)
    } finally {
      writer.close()
    }

    val includeFlags =
      generated.headerFolders.flatMap(f => Seq("-I", f)) ++
      extraIncludeDirs.flatMap(d => Seq("-I", d)) ++
      Seq("-I", z3IncludeDir)
    val macroFlags = macros.map(m => s"-D$m")
    val compileCmd = Seq("clang++", "-std=c++17", inputCpp, "-o", outputExe, s"-O$optimizeLevel", "-g") ++
      includeFlags ++ macroFlags ++ Seq("-L", z3LibDir, s"-Wl,-rpath,$z3LibDir", "-lz3")
    println(s"Compile command: ${compileCmd.mkString(" ")}")

    if (Process(compileCmd).! != 0) {
      throw new RuntimeException(s"Compilation failed for $inputCpp")
    }
  }

  private def runExe(exePath: String, extraEnv: (String, String)*): String =
    Process(Seq(exePath), None, (exeEnv ++ extraEnv): _*).!!

  def testFileConcolicCpp(filename: String,
                          main: Option[String] = None,
                          exitByCoverage: Boolean = false) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exploreTreeFile = {
      // Do concolic execution with snapshot reuse
      val exe = s"$cppFile.exe"
      val exploreTreeFile = s"$filename.tree.dot"
      compileToExeWithZ3(moduleInst, main, cppFile, exe, true, optimizeLevel=0, if (exitByCoverage) "BY_COVERAGE" else "EARLY_EXIT")
      println(s"Running compiled concolic execution with snapshot reuse: $exe")
      val result = runExe(s"./$exe", "TREE_FILE" -> exploreTreeFile)
      println(result)
      exploreTreeFile
    }
    val exploreTreeFileNoReuse = {
      // Do concolic execution without snapshot reuse
      val exe = s"$cppFile.noreuse.exe"
      val exploreTreeFile = s"$filename.noreuse.tree.dot"
      compileToExeWithZ3(moduleInst, main, cppFile, exe, false, optimizeLevel=0, if (exitByCoverage) "BY_COVERAGE" else "EARLY_EXIT")
      println(s"Running compiled concolic execution without snapshot reuse: $exe")
      val result = runExe(s"./$exe", "TREE_FILE" -> exploreTreeFile)
      println(result)
      exploreTreeFile
    }
    val exploreTreeFileImm = {
      // Do concolic execution with immutable data structure and snapshot reuse
      val exe = s"$cppFile.imm.exe"
      val exploreTreeFile = s"$filename.imm.tree.dot"
      compileToExeWithZ3(moduleInst, main, cppFile, exe, true, optimizeLevel=0, if (exitByCoverage) "BY_COVERAGE" else "EARLY_EXIT", "USE_IMM")
      println(s"Running compiled concolic execution with immutable data structure and snapshot reuse: $exe")
      val result = runExe(s"./$exe", "TREE_FILE" -> exploreTreeFile)
      println(result)
      exploreTreeFile
    }
    // The explore tree generated by two executions should be same
    import java.nio.file.Files
    assert(
      Files.readAllBytes(java.nio.file.Paths.get(exploreTreeFile))
      sameElements Files.readAllBytes(java.nio.file.Paths.get(exploreTreeFileNoReuse)),
      s"Explore trees $exploreTreeFile and $exploreTreeFileNoReuse are different!"
    )
    assert(
      Files.readAllBytes(java.nio.file.Paths.get(exploreTreeFile))
      sameElements Files.readAllBytes(java.nio.file.Paths.get(exploreTreeFileImm)),
      s"Explore trees $exploreTreeFile and $exploreTreeFileImm are different!"
    )
  }

  // only test concrete execution and its result
  def testFileConcreteCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    compileToExeWithZ3(moduleInst, main, cppFile, exe, true, optimizeLevel=0, "NO_INFO", "RUN_ONCE", "USE_IMM")
    val result = runExe(s"./$exe")
    println(result)

    expect.map(vs => {
      val stackValues = {
        val startMarker = "Stack contents: \n"
        val endMarker = "End of Stack contents"
        val start = result.indexOf(startMarker)
        val end = if (start >= 0) result.indexOf(endMarker, start + startMarker.length) else -1
        require(start >= 0 && end >= 0, s"Could not find markers '$startMarker' and '$endMarker' in output")
        result.substring(start + startMarker.length, end).trim
          .split("\n")
          .map(_.toFloat)
          .toList
      }
      assert(vs == stackValues)
    })
  }

  test("ack-cpp-concolic") { testFileConcolicCpp("./benchmarks/wasm/ack.wat", Some("real_main")) }

  test("bug-finding-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/branch-strip-buggy.wat", Some("real_main"))
  }

  test("brtable-bug-finding-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/staged/brtable_concolic.wat")
  }

  test("bug-finding-cov-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/branch-strip-buggy.wat", Some("real_main"), exitByCoverage=true)
  }

  test("brtable-bug-finding-cov-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/staged/brtable_concolic.wat", exitByCoverage=true)
  }

  test("simple-global-bug-finding-cov-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/staged/simple_global.wat", Some("real_main"), exitByCoverage=true)
  }

  test("mem-sym-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/mem-sym.wat", None, exitByCoverage=true)
  }

  test("mem-sym-extract-concolic") {
    testFileConcolicCpp("./benchmarks/wasm/mem-sym-extract.wat", None, exitByCoverage=true)
  }
  test("btree-bug-finding-concolic") { testFileConcolicCpp("./benchmarks/wasm/btree/2o1u-unlabeled.wat", exitByCoverage = true) }

  // Don't run this test by default since it takes too long and is only for performance comparison
  // test("long-trivial-execution-concrete") {
  //   // This is a example to show how much performance improvement we can get by immutable data structure
  //   testFileConcreteCpp("./benchmarks/wasm/staged/long-trivial-execution.wat", None)
  // }

  test("return-poly - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/staged/return_poly.wat", Some("$real_main"), expect=Some(List(42)))
  }
  test("ack-cpp - concrete") { testFileConcreteCpp("./benchmarks/wasm/ack.wat", Some("real_main"), expect=Some(List(7))) }
  test("power - concrete") { testFileConcreteCpp("./benchmarks/wasm/pow.wat", Some("real_main"), expect=Some(List(1024))) }
  test("start - concrete") { testFileConcreteCpp("./benchmarks/wasm/start.wat") }
  test("fact - concrete") { testFileConcreteCpp("./benchmarks/wasm/fact.wat", None, expect=Some(List(120))) }
  // TODO: Waiting more symbolic operators' implementations
  // test("loop - concrete") { testFileConcreteCpp("./benchmarks/wasm/loop.wat", None, expect=Some(List(10))) }
  test("even-odd - concrete") { testFileConcreteCpp("./benchmarks/wasm/even_odd.wat", None, expect=Some(List(1))) }
  test("global - concrete") { testFileConcreteCpp("./benchmarks/wasm/global-sym.wat", None) }
  // TODO: Waiting symbolic memory's implementations
  test("load - concrete") { testFileConcreteCpp("./benchmarks/wasm/load.wat", None, expect=Some(List(1))) }
  test("load overflow 1 - concrete") { testFileConcreteCpp("./benchmarks/wasm/load-overflow1.wat", None, expect=Some(List(1))) }
  test("load overflow 2 - concrete") { testFileConcreteCpp("./benchmarks/wasm/load-overflow2.wat", None, expect=Some(List(1))) }

  test("load offset - concrete") { testFileConcreteCpp("./benchmarks/wasm/load-offset.wat", None, expect=Some(List(1))) }

  // test("btree - concrete") { testFileConcreteCpp("./benchmarks/wasm/btree/2o1u-unlabeled.wat") }
  test("fib - concrete") { testFileConcreteCpp("./benchmarks/wasm/fib.wat", None, expect=Some(List(144))) }
  test("tribonacci - concrete") { testFileConcreteCpp("./benchmarks/wasm/tribonacci.wat", None, expect=Some(List(504))) }

  // test("return - concrete") {
  //   Since all of the thrown exceptions had been captured in concolic driver, this test is not valid anymore
  //   intercept[java.lang.RuntimeException] {
  //     testFileConcreteCpp("./benchmarks/wasm/return.wat", Some("$real_main"))
  //   }
  // }

  test("return_call - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/sum.wat", Some("sum10"), expect=Some(List(55)))
  }

  test("block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("real_main"), expect=Some(List(9)))
  }
  test("loop block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_loop_input"), expect=Some(List(55)))
  }
  test("if block input - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_if_input"), expect=Some(List(25)))
  }
  test("block input - poly br - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/block.wat", Some("test_poly_br"), expect=Some(List(0)))
  }
  test("loop block - poly br - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/loop_poly.wat", None, expect=Some(List(2, 1)))
  }

  test("brtable-cpp - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/staged/brtable.wat")
  }

  test("large-branch-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/compare_wasp/large-branch.wat")
  }

  test("small-snapshot-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/compare_wasp/small-snapshot.wat", Some("main"))
  }

  test("f32-operations-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/f32_test.wat", Some("test_f32"))
  }

  test("call-indirect-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/call_indirect_test.wat", expect=Some(List(42)))
  }

  test("data-section-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/data_sec.wat", expect=Some(List(67305985)))
  }

  test("long-loop-concrete") {
    testFileConcreteCpp("./benchmarks/wasm/long_loop.wat", expect=Some(List(100000)))
  }

  // test("diverge") {
  //   testFileConcolicCpp("./benchmarks/wasm/diverge.wat", Some("main"))
  // }

}
