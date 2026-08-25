package demo

import java.nio.file.Paths

import gensym.wasm.GenWasym

object CompileWat {
  def main(args: Array[String]): Unit = {
    if (args.length < 2 || args.length > 3) {
      Console.err.println("Usage: CompileWat INPUT.wat OUTPUT.cpp [MAIN_EXPORT]")
      System.exit(2)
    }

    val generatedCpp = GenWasym.compileFile(
      input = Paths.get(args(0)),
      output = Some(Paths.get(args(1))),
      mainExport = args.lift(2)
    )

    println(s"Library demo generated: $generatedCpp")
  }
}
