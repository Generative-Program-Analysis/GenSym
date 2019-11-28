package sai

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize

object Main {
  def test_power() = {
    val snippet = new DslDriver[Int, Int] {
      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x - 1)
      def snippet(b: Rep[Int]) = power(b, 10)
    }
    println(snippet.code)
    assert(snippet.eval(2) == 1024)
  }

  def main(args: Array[String]) {
    println("Hello")
    test_power()
  }
}
