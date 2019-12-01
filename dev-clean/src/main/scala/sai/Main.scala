package sai

import lms.core.stub._
import lms.macros.SourceContext
import lms.core.virtualize

import lms.core._
import lms.util._
import lms.core.utils.time

import sai.lmsx._


object Main {
  /*
  def test_list() = {
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
  */
  def test_map() = {
    val snippet = new SAIDriver[Map[Int, Int], Int] {
      @virtualize
      def maptest(m: Rep[Map[Int, Int]]): Rep[Int] = {
        val t1: Rep[(Int, Int)] = (unit(1), unit(2))
        val t2: Rep[(Int, Int)] = (1, 2) //unit handles this directly as constant
        val t3: Rep[(Int, Int)] = Tuple2(1, 2)
        val t4: Rep[(Int, Int)] = Tuple2(unit(1), 2)
        t1._1 + t2._1 + t3._2 + t4._1
        val m1 = Map((1, 2), (2, 5))
        val sumKey = m1.foldLeft(0) {
          case (z, (k, v)) => z + v
        }
        println(sumKey)
        val m2 = m1 + (unit(2) -> 3)
        m2.foreach {
          case (k, v) =>
            print("value: "); println(v)
        }
        val m3: Rep[Map[Int, Int]] = m2.map {
          case (k: Rep[Int], v: Rep[Int]) => (k, v + 1)
        }
        println(m3)
        m1(1)
      }

      def snippet(m: Rep[Map[Int, Int]]) = maptest(m)
    }
    println(snippet.code)
    assert(snippet.eval(Map((0,2), (1,3))) == 2)
  }

  def main(args: Array[String]) {
    println("Hello")
    //test_list()
    test_map()
  }
}
