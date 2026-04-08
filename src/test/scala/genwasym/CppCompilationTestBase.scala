package gensym.wasm

import java.io.{File, PrintWriter}
import org.scalatest.FunSuite

import sys.process._

abstract class CppCompilationTestBase extends FunSuite {
  protected def firstExistingDir(candidates: Seq[String]): Option[String] =
    candidates.map(path => new File(path)).find(file => file.isDirectory).map(_.getCanonicalPath)

  protected lazy val z3IncludeDir: String = {
    val fromEnv = sys.env.get("Z3_INCLUDE_DIR")
    val fromRepo = firstExistingDir(
      Seq(
        "./third-party/z3/build/z3_install/include",
        "./third-party/z3/build/z3_install/usr/local/include",
        "./third-party/z3/src/api/c++"
      )
    )
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate Z3 include directory. Set Z3_INCLUDE_DIR or build third-party/z3."
      )
    }
  }

  protected lazy val z3LibDir: String = {
    val fromEnv = sys.env.get("Z3_LIB_DIR")
    val fromRepo = firstExistingDir(Seq("./third-party/z3/build/z3_install/lib", "./third-party/z3/build/z3_install/usr/local/lib"))
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate Z3 library directory. Set Z3_LIB_DIR or build third-party/z3."
      )
    }
  }

  protected lazy val immerIncludeDir: String = {
    val fromEnv = sys.env.get("IMMER_INCLUDE_DIR")
    val fromRepo = firstExistingDir(
      Seq(
        "./third-party/immer",
        "./GenSym/third-party/immer"
      )
    )
    fromEnv.orElse(fromRepo).getOrElse {
      throw new RuntimeException(
        "Cannot locate immer header directory. Set IMMER_INCLUDE_DIR or initialize third-party/immer."
      )
    }
  }

  protected lazy val immerIncludeDirs: Seq[String] =
    Seq(immerIncludeDir)
      .map(path => new File(path))
      .filter(_.isDirectory)
      .map(_.getCanonicalPath)

  protected def prependPath(existing: Option[String], prefix: String): String =
    existing.filter(_.nonEmpty).map(old => s"$prefix:$old").getOrElse(prefix)

  protected lazy val z3RuntimeEnv: Seq[(String, String)] = Seq(
    "LD_LIBRARY_PATH" -> prependPath(sys.env.get("LD_LIBRARY_PATH"), z3LibDir),
    "DYLD_LIBRARY_PATH" -> prependPath(sys.env.get("DYLD_LIBRARY_PATH"), z3LibDir)
  )

  protected def compileGeneratedCpp(source: String,
                                    headerFolders: Seq[String],
                                    outputCpp: String,
                                    outputExe: String,
                                    compiler: String = "clang++",
                                    optimizeLevel: Int = 0,
                                    extraIncludeDirs: Seq[String] = Seq.empty,
                                    macroDefs: Seq[String] = Seq.empty,
                                    libraryDirs: Seq[String] = Seq.empty,
                                    runtimeLibraryDirs: Seq[String] = Seq.empty,
                                    libraries: Seq[String] = Seq.empty): Unit = {
    val writer = new PrintWriter(new File(outputCpp))
    try {
      writer.write(source)
    } finally {
      writer.close()
    }

    val includeFlags =
      (headerFolders ++ extraIncludeDirs).flatMap(dir => Seq("-I", dir))
    val macroFlags = macroDefs.map(m => s"-D$m")
    val libraryDirFlags = libraryDirs.flatMap(dir => Seq("-L", dir))
    val runtimeLibraryFlags = runtimeLibraryDirs.map(dir => s"-Wl,-rpath,$dir")
    val libraryFlags = libraries.map(lib => s"-l$lib")
    val compileCmd = Seq(
      compiler,
      "-std=c++17",
      outputCpp,
      "-o",
      outputExe,
      s"-O$optimizeLevel",
      "-g"
    ) ++ includeFlags ++ macroFlags ++ libraryDirFlags ++ runtimeLibraryFlags ++ libraryFlags
    println(s"Compile command: ${compileCmd.mkString(" ")}")

    if (Process(compileCmd).! != 0) {
      throw new RuntimeException(s"Compilation failed for $outputCpp")
    }
  }

  protected def compileGeneratedCppWithZ3Immer(source: String,
                                               headerFolders: Seq[String],
                                               outputCpp: String,
                                               outputExe: String,
                                               compiler: String = "clang++",
                                               optimizeLevel: Int = 0,
                                               macroDefs: Seq[String] = Seq.empty): Unit = {
    compileGeneratedCpp(
      source = source,
      headerFolders = headerFolders,
      outputCpp = outputCpp,
      outputExe = outputExe,
      compiler = compiler,
      optimizeLevel = optimizeLevel,
      extraIncludeDirs = immerIncludeDirs :+ z3IncludeDir,
      macroDefs = macroDefs,
      libraryDirs = Seq(z3LibDir),
      runtimeLibraryDirs = Seq(z3LibDir),
      libraries = Seq("z3")
    )
  }

  protected def runExe(exePath: String, env: Seq[(String, String)] = Seq.empty): String =
    Process(Seq(exePath), None, env: _*).!!

  protected def runExeWithZ3(exePath: String, extraEnv: Seq[(String, String)] = Seq.empty): String =
    runExe(exePath, z3RuntimeEnv ++ extraEnv)

  protected def parseStackValues(output: String): List[Float] = {
    val startMarker = "Stack contents: \n"
    val endMarker = "End of Stack contents"
    val start = output.indexOf(startMarker)
    val end = if (start >= 0) output.indexOf(endMarker, start + startMarker.length) else -1
    require(start >= 0 && end >= 0, s"Could not find markers '$startMarker' and '$endMarker' in output")
    output.substring(start + startMarker.length, end).trim
      .split("\n")
      .map(_.toFloat)
      .toList
  }
}
