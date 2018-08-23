/**

Shonan Challenge
================

In this tutorial we will develop a solution to the HMM problem from the Shonan Challenge.

The task is to implement a sparse matrix vector product, where the matrix is statically
known but the vector is not. This situation comes up in hidden markov models (HMM),
where a single transition matrix is multiplied by many different observation vectors.
Therefore, it pays off to generate code that is specialized to a particular matrix.

For further reference, see:

- Shonan Challenge for Generative Programming ([PDF](https://www.cs.rutgers.edu/~ccshan/metafx/pepm2013.pdf)) ([official code repository](https://github.com/StagedHPC/shonan-challenge))
  Baris Aktemur, Yukiyoshi Kameyama, Oleg Kiselyov, Chung-chieh Shan. PEPM'13

Outline:
<div id="tableofcontents"></div>

*/

package scala.lms.tutorial

import scala.lms.common._
import scala.reflect.SourceContext

class ShonanTest extends TutorialFunSuite {
  val under = ""
  val A = scala.Array

/**
We first define our static matrix. We notice that some rows are dense,
some rows are sparse, and some rows contain only zeroes.
*/

  val a =
    A(A(1, 1, 1, 1, 1), // dense
      A(0, 0, 0, 0, 0), // null
      A(0, 0, 1, 0, 0), // sparse
      A(0, 0, 0, 0, 0),
      A(0, 0, 1, 0, 1))

/**
STEP 0: starting point
----------------------

We start with a fully static implementation.
*/

  test("shonan-hmm1a") {

    def matrix_vector_prod(a: Array[Array[Int]], v: Array[Int]) = {
      val n = a.length
      val v1 = new Array[Int](n)

      for (i <- (0 until n)) {
        for (j <- (0 until n)) {
          v1(i) = v1(i) + a(i)(j) * v(j)
        }
      }
      v1
    }

    // let's run it on some static input:

    val v = A(1,1,1,1,1)

    val v1 = matrix_vector_prod(a, v)
    val result = v1.mkString(",")

    check("shonan-hmm1a", result)
  }
/**
We get a concrete vector as result:

      .. includecode:: ../../../../out/shonan-hmm1a.check.scala


STEP 0.5: static vs dynamic conditional
---------------------------------------

Now we add basic code generation facilities and, as a recap of how
LMS works, play with static vs dynamic expressions.
*/

  test("shonan-hmm1b") {
    val snippet = new DslDriver[Array[Int],Array[Int]] {
      def snippet(v: Rep[Array[Int]]) = {

        val x = 100 - 5

        if (x > 10) {
          println("hello")
        }

        v
      }
    }
    check("shonan-hmm1b", snippet.code)
  }

/**
The condition above is fully static, so it does not show up in
the generated code:

      .. includecode:: ../../../../out/shonan-hmm1b.check.scala


If we change the condition to depend on the (dynamic) input array,
an `if/else` statement will be generated.

*/
   test("shonan-hmm1b_dyn") {
    val snippet = new DslDriver[Array[Int],Array[Int]] {
      def snippet(v: Rep[Array[Int]]) = {

        val x = 100 - v.length

        if (x > 10) {
          println("hello")
        }

        v
      }
    }
    check("shonan-hmm1b_dyn", snippet.code)
  }

/**
Notice the `if/else` statement in the generated code.

.. includecode:: ../../../../out/shonan-hmm1b_dyn.check.scala

Aside: LMS relies on Scala's local type inference to propagate dynamic
expressions (`Rep` types) automatically within a method, and on
Scala's type checker to ensure the binding times (dynamic vs static)
are consistent.

Aside: In the snippet, the `if/else` statement with the dynamic
condition is peculiar, because its condition is not a plain `Boolean`
but a `Rep[Boolean]`. Hence, LMS also relies on virtualization of
control structures to overload the meaning of control structures such
as `if/else` and `while` -- in the same way that the `for` syntax is
overloaded in Scala.

*/

/**
STEP 1: Staging the matrix vector product
-----------------------------------------

We want to change the `matrix_vector_prod` function to take a dynamic
vector and a static matrix. We move it into the `DslDriver` trait so
that it can use `Rep`.

We generate nested loops by default (type `Rep[Range]`). This means that
the loop index becomes a `Rep[Int]` value. To access the static matrix,
we need to lift it to a `Rep` value as well. We accomplish this with
the help of `staticData`.

We also need to replace `new Array` with `NewArray`: instead of creating
an array at staging time, we want to generate code to create a the array.

Now we can play with the loop shapes: `Range` loops are computed
statically, i.e. unrolled at staging time. `Rep[Range]` loops cause
generation of loop code.

We can make unrolling decisions in a very fine-grained manner:
for dense rows we want to generate a loop, for sparse rows
we want to unroll. We just add a condition `sparse` and dispatch
to either a `Range` or a `Rep[Range]` loop.
*/

  test("shonan-hmm1c") {
    val snippet = new DslDriver[Array[Int],Array[Int]] {
      def snippet(v: Rep[Array[Int]]) = {

        def matrix_vector_prod(a0: Array[Array[Int]], v: Rep[Array[Int]]) = {
          val n = a0.length
          val a = staticData(a0)
          val v1 = NewArray[Int](n)

          for (i <- (0 until n):Range) {
            val sparse = a0(i).count(_ != 0) < 3
            if (sparse) {
              for (j <- (0 until n):Range) {
                v1(i) = v1(i) + a(i).apply(j) * v(j)
              }
            } else {
              for (j <- (0 until n):Rep[Range]) {
                v1(i) = v1(i) + a(i).apply(j) * v(j)
              }
            }
          }
          v1
        }

        val v1 = matrix_vector_prod(a, v)
        v1
      }
    }
    check("shonan-hmm1c", snippet.code)
  }

/**
Looking at the generated code, we can easily see that we get a while
loop for row 0, which is dense, no operations for row 1, and an
unrolled loop for row 2, which is sparse:

      .. includecode:: ../../../../out/shonan-hmm1c.check.scala


STEP 2: Conditional unrolling
-----------------------------

The code duplication of the loop body above is not very nice. Fortunately,
we can create arbitrary staging-time abstractions. Here, we create an auxiliary
method `unrollIf` that captures the conditional unrolling pattern in a general
way. The `matrix_vector_prod` function no longer needs to express the loop
body twice.

The generated code is identical: _"abstraction without regret"_ FTW!
*/

  test("shonan-hmm1d") {
    val snippet = new DslDriver[Array[Int],Array[Int]] {
      def snippet(v: Rep[Array[Int]]) = {

        def unrollIf(c:Boolean,r: Range) = new {
          def foreach(f: Rep[Int] => Rep[Unit]) = {
            if (c) for (j <- (r.start until r.end):Range)      f(j)
            else   for (j <- (r.start until r.end):Rep[Range]) f(j)
          }
        }

        def matrix_vector_prod(a0: Array[Array[Int]], v: Rep[Array[Int]]) = {
          val n = a0.length
          val a = staticData(a0)
          val v1 = NewArray[Int](n)

          for (i <- (0 until n):Range) {
            val sparse = a0(i).count(_ != 0) < 3
            for (j <- unrollIf(sparse, 0 until n)) {
              v1(i) = v1(i) + a(i).apply(j) * v(j)
            }
          }
          v1
        }

        val v1 = matrix_vector_prod(a, v)
        v1
      }
    }
    check("shonan-hmm1d", snippet.code)
  }
}

/**
What's next?
------------

Go back to the [tutorial index](index.html) or continue with the [Regular Expression Matcher](regex.html).
*/
