package scala.lms.tutorial

import scala.lms.common._
import scala.reflect.SourceContext

// sbt ~test-only *ShonanLive*

class ShonanLiveTest extends TutorialFunSuite {
  val under = "0-live-"

/*  playground for live demo */

  test("shonan-hmm-live") {
    val A = scala.Array

    val a =
      A(A(1, 1, 1, 1, 1), // dense
        A(0, 0, 0, 0, 0), // null
        A(0, 0, 1, 0, 0), // sparse
        A(0, 0, 0, 0, 0),
        A(0, 0, 1, 0, 1))

    def matrix_vector_prod(a: Array[Array[Int]], v: Array[Int]) = {
      val n = a.length
      val v1 = new Array[Int](n)

      for (i <- (0 until n): Range) {
        for (j <- (0 until n)) {
          v1(i) = v1(i) + a(i).apply(j) * v(j)
        }
      }
      v1
    }

    // let's run it on some static input:
    val v = A(1,1,1,1,1)
    val v1 = matrix_vector_prod(a, v)
    
    val result = v1.mkString(",")
    check("shonan-hmm-live", result)
  }




  /*
    val snippet = new LMS_Driver[Array[Int],Array[Int]] {
      def snippet(v: Rep[Array[Int]]) = {
        println("hello")
        v
      }
    }
    exec("shonan-hmm-live", snippet.code)
  */



/*
  DEMO:
  1) add compile snippet
  2) add conditional
  3) stage mv prod
      - staticData(a)
      - NewArray[Int](n)     
  4) Range vs Rep[Range]
  5) unrollIf
*/

abstract class LMS_Driver[A:Manifest,B:Manifest] extends DslDriver[A,B]


}


/*    
    val snippet = new LMS_Driver[Array[Int],Array[Int]] {
      def unrollIf(c: Boolean)(r: Range) = new {
        def foreach(f: Rep[Int] => Rep[Unit]): Rep[Unit] = {
          if (c) {
            for (j <- r) {
              f(j)
            }
          } else {
            for (j <- (r.start until r.end):Rep[Range]) {
              f(j)
            }
          }
        }
      }

      def matrix_vector_prod(a0: Array[Array[Int]], v: Rep[Array[Int]]) = {
        val n = a0.length
        val v1 = NewArray[Int](n)

        val a = staticData(a0)

        for (i <- (0 until n):Range) {
          val sparse = a0(i).count(_ != 0) < 3
          for (j <- unrollIf(sparse)(0 until n)) {
            v1(i) = v1(i) + a(i).apply(j) * v(j)
          }
        }
        v1
      }

      def snippet(v: Rep[Array[Int]]) = {
        matrix_vector_prod(a,v)
      }

    }
*/
