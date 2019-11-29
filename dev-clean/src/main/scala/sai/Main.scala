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
        val plusone = xs.map { case x: Rep[Int] =>
          val y = x + 1
          val z = y - 1
          z + 1
        }
        val plustwo = xs.map((x: Rep[Int]) => x + 2)
        val plusthree = xs.map((x: Rep[Int]) => x + 3)
        val ys = xs.sortBy(x => -x)
        println(ys)
        val zs = 4::plusthree
        println(zs.mkString(","))
        val zss = zs.flatMap(z => List(z + 1 - 1))
        val sum = zss.foldLeft(0)({
          case (a: Rep[Int], b: Rep[Int]) => a + b
        })
        println(sum)
        val bs = List(0,1,2,3)
        plusone(2) + plustwo.tail.head + zss.head + bs(0)
      }

      //def snippet(b: Rep[Int]) = power(b, 10)
      def snippet(xs: Rep[List[Int]]) = listget(xs)
    }
    println(snippet.code)
    assert(snippet.eval(List(1,2,3)) == 4 + 4 + 4)
  }

  def main(args: Array[String]) {
    println("Hello")
    test_power()
  }
}
