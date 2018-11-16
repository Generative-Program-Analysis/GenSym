package sai

import scala.io.Source

import sai.utils._
import sai.direct.large.ai._
import sai.evaluation.TestPrograms._

object Evaluation extends AbsLamCalTrait {
  def main(args: Array[String]) = {
    val prog = boyer
    println(prog)
//    println("staged: " + evalStaged(prog))
    println("unstaged: " + evalUnstaged(prog))
  }
}
