package gensym.wasm

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

import scala.collection.JavaConverters._
import scala.util.control.NonFatal

import gensym.wasm.miniwasm.ModuleInstance
import gensym.wasm.parser.Parser
import gensym.wasm.stagedconcolicminiwasm.WasmToCppCompiler

/** Public Scala and command-line entry point for the GenWasym compiler. */
object GenWasym {
  final case class CliConfig(
      input: Option[Path] = None,
      inputDir: Option[Path] = None,
      output: Option[Path] = None,
      outputDir: Option[Path] = None,
      mainExport: Option[String] = None,
      printResult: Boolean = false,
      recursive: Boolean = false)

  val usage: String =
    """Usage:
      |  GenWasym --input FILE [--output FILE | --output-dir DIR]
      |           [--main EXPORT] [--print-result]
      |
      |  GenWasym --input-dir DIR --output-dir DIR [--recursive]
      |           [--main EXPORT] [--print-result]
      |
      |Options:
      |  -i, --input FILE       Compile one WebAssembly text file.
      |      --input-dir DIR    Compile every .wat file in a directory.
      |  -o, --output FILE      C++ output path for a single input file.
      |      --output-dir DIR   Directory for generated C++ files.
      |  -m, --main EXPORT      Use this exported Wasm function as the entry.
      |                          Without it, use the module start function.
      |      --recursive        Recurse below --input-dir and preserve layout.
      |      --print-result     Emit generated code that prints the final stack.
      |  -h, --help             Show this help.
      |""".stripMargin

  /**
    * Parse one `.wat` file, stage the concolic evaluator, and write C++.
    *
    * @return the normalized path of the generated C++ file
    */
  def compileFile(
      input: Path,
      output: Option[Path] = None,
      mainExport: Option[String] = None,
      printResult: Boolean = false): Path = {
    val inputPath = input.toAbsolutePath.normalize
    if (!Files.isRegularFile(inputPath)) {
      throw new IllegalArgumentException(s"Input is not a file: $inputPath")
    }
    if (!inputPath.getFileName.toString.endsWith(".wat")) {
      throw new IllegalArgumentException(s"Input must be a .wat file: $inputPath")
    }

    val outputPath = output
      .map(_.toAbsolutePath.normalize)
      .getOrElse(Paths.get(inputPath.toString + ".cpp"))
    Option(outputPath.getParent).foreach(path => Files.createDirectories(path))

    println(s"Parsing WebAssembly text: $inputPath")
    val ast = Parser.parseFile(inputPath.toString)
    val moduleInstance = ModuleInstance(ast)

    println(s"Staging concolic executor with entry: ${mainExport.getOrElse("<start>")}")
    val generated = WasmToCppCompiler.compile(moduleInstance, mainExport, printResult)

    Files.write(outputPath, generated.source.getBytes(StandardCharsets.UTF_8))
    println(s"Generated C++: $outputPath")
    outputPath
  }

  /** Compile a directory of `.wat` files, optionally recursively. */
  def compileDirectory(
      inputDir: Path,
      outputDir: Path,
      mainExport: Option[String] = None,
      printResult: Boolean = false,
      recursive: Boolean = false): Seq[Path] = {
    val inputRoot = inputDir.toAbsolutePath.normalize
    val outputRoot = outputDir.toAbsolutePath.normalize
    if (!Files.isDirectory(inputRoot)) {
      throw new IllegalArgumentException(s"Input directory does not exist: $inputRoot")
    }
    Files.createDirectories(outputRoot)

    val paths = if (recursive) Files.walk(inputRoot) else Files.list(inputRoot)
    val inputs = try {
      paths.iterator.asScala
        .filter(path => Files.isRegularFile(path) && path.getFileName.toString.endsWith(".wat"))
        .toSeq
        .sortBy(_.toString)
    } finally {
      paths.close()
    }

    if (inputs.isEmpty) {
      throw new IllegalArgumentException(s"No .wat files found in: $inputRoot")
    }

    inputs.map { inputPath =>
      val relative = inputRoot.relativize(inputPath)
      val outputPath = outputRoot.resolve(relative.toString + ".cpp")
      compileFile(inputPath, Some(outputPath), mainExport, printResult)
    }
  }

  private def parseArgs(args: List[String], config: CliConfig = CliConfig()): Either[String, CliConfig] =
    args match {
      case Nil => Right(config)
      case ("--input" | "-i") :: value :: rest =>
        parseArgs(rest, config.copy(input = Some(Paths.get(value))))
      case "--input-dir" :: value :: rest =>
        parseArgs(rest, config.copy(inputDir = Some(Paths.get(value))))
      case ("--output" | "-o") :: value :: rest =>
        parseArgs(rest, config.copy(output = Some(Paths.get(value))))
      case "--output-dir" :: value :: rest =>
        parseArgs(rest, config.copy(outputDir = Some(Paths.get(value))))
      case ("--main" | "-m") :: value :: rest =>
        parseArgs(rest, config.copy(mainExport = Some(value)))
      case "--print-result" :: rest =>
        parseArgs(rest, config.copy(printResult = true))
      case "--recursive" :: rest =>
        parseArgs(rest, config.copy(recursive = true))
      case option :: Nil if Set("--input", "-i", "--input-dir", "--output", "-o", "--output-dir", "--main", "-m").contains(option) =>
        Left(s"Missing value for $option")
      case unknown :: _ => Left(s"Unknown argument: $unknown")
    }

  private def compile(config: CliConfig): Either[String, Seq[Path]] =
    (config.input, config.inputDir) match {
      case (Some(_), Some(_)) =>
        Left("Use either --input or --input-dir, not both")
      case (None, None) =>
        Left("Missing --input or --input-dir")
      case (Some(input), None) =>
        if (config.output.nonEmpty && config.outputDir.nonEmpty) {
          Left("Use either --output or --output-dir, not both")
        } else if (config.recursive) {
          Left("--recursive requires --input-dir")
        } else {
          val output = config.output.orElse(config.outputDir.map { directory =>
            directory.resolve(input.getFileName.toString + ".cpp")
          })
          Right(Seq(compileFile(input, output, config.mainExport, config.printResult)))
        }
      case (None, Some(inputDir)) =>
        if (config.output.nonEmpty) {
          Left("--output is only valid with --input; use --output-dir")
        } else {
          config.outputDir match {
            case None => Left("--input-dir requires --output-dir")
            case Some(outputDir) =>
              Right(compileDirectory(
                inputDir,
                outputDir,
                config.mainExport,
                config.printResult,
                config.recursive))
          }
        }
    }

  def main(args: Array[String]): Unit = {
    if (args.contains("--help") || args.contains("-h")) {
      println(usage)
      return
    }

    parseArgs(args.toList) match {
      case Left(message) =>
        Console.err.println(s"GenWasym: $message")
        Console.err.println(usage)
        System.exit(2)
      case Right(config) =>
        try {
          compile(config) match {
            case Left(message) =>
              Console.err.println(s"GenWasym: $message")
              Console.err.println(usage)
              System.exit(2)
            case Right(outputs) =>
              println(s"Compiled ${outputs.size} WebAssembly file(s).")
          }
        } catch {
          case NonFatal(error) =>
            val detail = Option(error.getMessage).getOrElse(error.getClass.getSimpleName)
            Console.err.println(s"GenWasym compilation failed: $detail")
            System.exit(1)
        }
    }
  }
}
