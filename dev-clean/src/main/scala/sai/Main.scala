package sai

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize

import lms.core._
import lms.util._
import lms.core.utils.time

import sai.lmsx._


object Main {
  def test_power() = {
    val snippet = new SAIDriver[List[Int], Int] {
      @virtualize
      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x - 1)

      @virtualize
      def listget(xs: Rep[List[Int]]): Rep[Int] = {
        /*
        val a = xs(2) + 1
        val b = xs(1) + a
        b
         */
        val plusone = xs.map((x: Rep[Int]) => x + 1)
        val plustwo = xs.map((x: Rep[Int]) => x + 2)
        plusone(2) + plustwo(1)
      }

      //def snippet(b: Rep[Int]) = power(b, 10)
      def snippet(xs: Rep[List[Int]]) = listget(xs)
    }
    println(snippet.code)
    assert(snippet.eval(List(1,2,3)) == 3)
  }

  def main(args: Array[String]) {
    println("Hello")
    test_power()
  }
}
