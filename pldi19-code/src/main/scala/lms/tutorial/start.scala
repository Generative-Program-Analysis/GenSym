/**
Getting Started
===============

Outline:
<div id="tableofcontents"></div>

Staging and LMS
---------------

What is staging? The idea behind staging is to delay computation of
certain expressions, generating code to compute them later. The
benefit is *abstraction without regret*: using high-level programming
abstractions once, to structure generated code, instead of all the
time during execution.

Lightweight Modular Staging (LMS) is a staging technique driven by
types. In addition to the *staging* aspect, the technique is *lightweight*,
because it is purely library based. It is also *modular*, because
features can be mixed and matched and the framework is easy to
extend.


My First Multi-Stage Program
------------------------------

This tutorial is a [literate Scala file](https://github.com/scala-lms/tutorials/tree/master/src/test/scala/lms/tutorial/start.scala). We invite you to clone the
[GitHub repo](https://github.com/scala-lms/tutorials/) and play with the code as you follow along.

*/




package scala.lms.tutorial

import scala.lms.common._

class GettingStartedTest extends TutorialFunSuite {
  val under = "dslapi"


/**
### Rep[T] vs T

In LMS, `Rep[T]` represents a delayed computation of type `T`. Thus,
during staging, an expression of type `Rep[T]` becomes part of
the generated code, while an expression of bare type `T` becomes a
constant in the generated code. For core Scala features, adding
`Rep` types should be enough to build a program generator, as we
will see later.
*/

  test("1") {
    val snippet = new DslDriver[Int,Int] {
      def snippet(x: Rep[Int]) = {

        def compute(b: Boolean): Rep[Int] = {
          // the if is executed in the first stage
          if (b) 1 else x
        }
        compute(true)+compute(1==1)

      }
    }
    assert(snippet.eval(0) === 2)
    check("1", snippet.code)
  }
/**
      .. includecode:: ../../../../out/dslapi1.check.scala

Contrast the snippet above, where `b` is a `Boolean`, with the snippet
below, where `b` is a `Rep[Boolean]`. The expression `if (b) 1 else x`
is executed at staging time when `b` is a `Boolean`, while it is delayed
causing code to be generated for the `if` expression when `b` is a
`Rep[Boolean]` -- indeed, the actual value of `b` is not known at staging
time, but only when the generated code is executed.
*/

  test("2") {
    val snippet = new  DslDriver[Int,Int] {
      def snippet(x: Rep[Int]) = {

        def compute(b: Rep[Boolean]): Rep[Int] = {
          // the if is deferred to the second stage
          if (b) 1 else x
        }
        compute(x==1)

      }
    }
    assert(snippet.eval(2) === 2)
    check("2", snippet.code)
  }
/**
      .. includecode:: ../../../../out/dslapi2.check.scala


### Rep[A => B] vs Rep[A]=>Rep[B]

In the previous snippets, we already notice some *abstraction without
regret*: the ``compute`` function exists only at staging time, and is
not part of the generated code -- more generally, we can freely use
abstractions to structure and compose the staged program, but these
abstractions are not part of the generated code when their type is a
bare ``T`` as opposed of a ``Rep[T]``. In the second snippet,
``compute`` has the type ``Rep[Boolean] => Rep[Int]``, not
``Rep[Boolean => Int]`` -- its type already tells us that the function
is known at staging time.
*/

  test("power") {
    val snippet = new DslDriver[Int,Int] {

      def power(b: Rep[Int], x: Int): Rep[Int] =
        if (x == 0) 1
        else b * power(b, x-1)

      def snippet(b: Rep[Int]) =
        power(b, 3)

    }
    assert(snippet.eval(2) === 8)
    check("power", snippet.code)
  }

/**
      .. includecode:: ../../../../out/dslapipower.check.scala


### Rep[Range] vs Range

Loops can be unrolled in the first stage, or be generated as loops in
the second stage, driven by the type of their condition.
*/

  test("range1") {
    val snippet = new DslDriver[Int,Unit] {
      def snippet(x: Rep[Int]) = comment("for", verbose = false) {

        for (i <- (0 until 3): Range) {
          println(i)
        }

      }
    }
    check("range1", snippet.code)
  }
/**
      .. includecode:: ../../../../out/dslapirange1.check.scala for
*/

  test("range2") {
    val snippet = new DslDriver[Int,Unit] {
      def snippet(x: Rep[Int]) = comment("for", verbose = false) {

        for (i <- (0 until x): Rep[Range]) {
          println(i)
        }

      }
    }
    check("range2", snippet.code)
  }
}
/**
      .. includecode:: ../../../../out/dslapirange2.check.scala for


What's next?
------------

Go back to the [tutorial index](index.html) or continue with the [Shonan Challenge](shonan.html).

*/
