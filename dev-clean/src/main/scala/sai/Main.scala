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
    val snippet = new SAIDriver[Int, Int] {
      @virtualize
      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x - 1)
      def snippet(b: Rep[Int]): Rep[Int] = power(b, 5)
    }

    println(snippet.code)
    assert(snippet.eval(2) == 32)
  }

  def test_power_c() = {
    val snippet = new CppSAIDriver[Int, Int] {
      @virtualize
      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x - 1)
      def snippet(b: Rep[Int]): Rep[Int] = power(b, 5)
    }

    println(snippet.code)
  }

  def test_list_c() = {
    val snippet = new CppSAIDriver[Int, Int] {
      @virtualize
      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x - 1)

      @virtualize
      def listget(xs: Rep[List[Int]]): Rep[Int] = {
        val ys: Rep[List[Int]] = List(1, 2, 3)
        val zs = ys ++ xs // List(1, 2, 3, xs)
        val plustwo = zs.map(x => x + 2)
        val plusthree = plustwo.map(x => x + 3) // List(6, 7, 8, xs + 5)
        val zss = plusthree.flatMap(z => List(z * 2)) // List(12, 14, 16, 2(xs+5))
        val n = zss.foldLeft(0) ({
          case (a: Rep[Int], b: Rep[Int]) => a + b
        })
        n
      }

      //def snippet(b: Rep[Int]) = power(b, 10)
      def snippet(xs: Rep[Int]) = listget(List(xs))
    }
    println(snippet.code)
    snippet.eval(5)
  }

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

        val twolist = List(List(1, 2), List(2, 3))
        println(twolist.foldLeft(1) { case (x, ys) =>
          x * (ys.foldLeft(0) { case (a, b) => a + b })
        })

        plusone(2) + plustwo.tail.head + zss.head + bs(0)
      }

      //def snippet(b: Rep[Int]) = power(b, 10)
      def snippet(xs: Rep[List[Int]]) = listget(xs)
    }
    println(snippet.code)
    assert(snippet.eval(List(1,2,3)) == 4 + 4 + 4)
  }

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
        def w(z: Rep[Int], kv: (Rep[Int], Rep[Int])): Rep[Int] = z + kv._2 // this will be inlined
        val sumKey = m1.foldLeft(0)(w)
        /*
        {
          case (z, (k, v)) => z + v
        }
        */
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
        val m4: Rep[List[(Int, Int)]] = m2.map {
          case (k: Rep[Int], v: Rep[Int]) => (k, v + 1)
        }
        println(m4)

        val m5: Rep[Map[Int, Int]] = Map[Int, Int]()
        println(m5)

        m1(1)
      }

      def snippet(m: Rep[Map[Int, Int]]) = maptest(m)
    }
    println(snippet.code)
    assert(snippet.eval(Map((0,2), (1,3))) == 2)
  }

  def test_map_c() = {
    val snippet = new CppSAIDriver[Int, Int] {
      @virtualize
      def maptest(n: Rep[Int]): Rep[Int] = {
        val t1 = (unit(1), unit(2))
        val t2 = (3, 4)
        val m1 = Map((n, n), t1, t2)
        def w(z: Rep[Int], kv: (Rep[Int], Rep[Int])): Rep[Int] = z + kv._2 // this will be inlined
        val sumKey = m1.foldLeft(0)(w)
        val m2 = m1 + (5 -> 6)
        val prodKey = m2.foldLeft(1) {
          case (acc, (k, v)) =>
            val acc1 = acc + 1
            acc1 * v
        }
        val m3: Rep[Map[Int, Int]] = m2.map({ case (k: Rep[Int], v: Rep[Int]) => (k+1, v+1) })
        val sumKey2 = m3.foldLeft(0)(w)
        val xs: Rep[List[Int]] = m2.map({ case (k: Rep[Int], v: Rep[Int]) => k + v })
        val sumXs = xs.foldLeft(0)({ case (x: Rep[Int], y: Rep[Int]) => x + y })
        sumKey + prodKey + sumKey2 + sumXs
      }
      def snippet(n: Rep[Int]) = maptest(n)
    }
    println(snippet.code)
    snippet.eval(3)
  }

  def test_opt() = {
    val snippet = new SAIDriver[Int, Int] {
      @virtualize
      def f(i: Rep[Int]): Rep[Int] = {
        val m1 = Map[String, Int]()
        val m2 = Map[Int, Int]()
        val m3 = m1 + ("a", 1)
        val m4 = m2 + (1, 2)
        println(m3)
        println(m4)
        val m5 = Map((1, "2"), (2, "3"))
        println(m5)
        i
      }
      def snippet(i: Rep[Int]) = f(i)
    }

    println(snippet.code)
  }

  def test_set() = {
    val snippet = new SAIDriver[Set[Int], Int] {
      @virtualize
      def settest(s: Rep[Set[Int]]): Rep[Int] = {
        val sum = s.foldLeft(0) { case (z, x) => z + x }
        val s2 = s.map(x => x + 1)
        println(s2)
        val s3 = s2.filter(x => x > 3)
        println(s3)
        if (s3(5)) {
          println("contains")
        }
        else {
          println("not contains")
        }
        sum
      }

      def snippet(s: Rep[Set[Int]]) = settest(s)
    }

    println(snippet.code)
    assert(snippet.eval(Set(1,2,3,4)) == 10)
  }

  def test_either() = {
    val snippet = new SAIDriver[String, Int] {
      @virtualize
      def eithertest(s: Rep[String]): Rep[Int] = {
        val l = Either.left[String, Int](s)
        println(l.left.value)
        val r = Either.right[String, Int](unit(3))
        if (r.isRight) {
          println("right")
        } else {
          println("left")
        }
        r.right.value
      }

      def snippet(s: Rep[String]) = eithertest(s)
    }

    println(snippet.code)
  }

  def main(args: Array[String]) {
    /*
    test_list()
    test_map()
    test_set()
    test_opt()
     */
    //test_either()
    //test_power_c()
    //test_list_c()
    test_map_c()
  }
}
