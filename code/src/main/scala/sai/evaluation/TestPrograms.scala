package sai.evaluation

import scala.io.Source
import sai.direct.large.parser._

object TestPrograms {
  def getAST(prog: String) = {
    LargeSchemeParser(prog) match {
      case Some(expr) => LargeSchemeASTDesugar(expr)
    }
  }

  // church encoding
  def church = getAST(Source.fromFile("benchmarks/church.sch").mkString)

  // earley make-parser -- from oaam
  def earley = getAST(Source.fromFile("benchmarks/earley.sch").mkString)

  // mbrotZ -- may need primitive complex numbers
  def mbrotZ = getAST(Source.fromFile("benchmarks/mbrotZ.sch").mkString)

  // BOYER -- Logic programming benchmark, originally written by Bob Boyer.
  def boyer = getAST(Source.fromFile("benchmarks/toplas98/boyer.sch").mkString)

  // Dynamic -- Fritz's dynamic type inferencer, set up to run on itself
  def dynamic = getAST(Source.fromFile("benchmarks/toplas98/boyer.sch").mkString)

  // graphs
  def graphs = getAST(Source.fromFile("benchmarks/toplas98/graphs.sch").mkString)

  // handle -- requires macro expansion
  def handle = getAST(Source.fromFile("benchmarks/toplas98/handle.scm").mkString)

  // lattice
  def lattice = getAST(Source.fromFile("benchmarks/toplas98/lattice.scm").mkString)

  // matrix
  def matrix = getAST(Source.fromFile("benchmarks/toplas98/matrix.scm").mkString)

  // maze -- call/cc
  def maze = getAST(Source.fromFile("benchmarks/toplas98/maze.sch").mkString)

  // nbody
  def nbody = getAST(Source.fromFile("benchmarks/toplas98/nbody.sch").mkString)

  // nucleic
  def nucleic = getAST(Source.fromFile("benchmarks/toplas98/nucleic.sch").mkString)

  // nucleic2 -- define syntax
  def nucleic2 = getAST(Source.fromFile("benchmarks/toplas98/nucleic2.sch").mkString)

  // splay -- old match
  def splay = getAST(Source.fromFile("benchmarks/toplas98/splay.scm").mkString)

}
