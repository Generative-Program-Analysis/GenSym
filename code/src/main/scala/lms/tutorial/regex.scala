/**
From Interpreter to Compiler
============================

Outline:
<div id="tableofcontents"></div>


A staged interpreter is a compiler. This is useful, because an
interpreter is usually much easier to implement than a compiler. In
this section, we illustrate how to turn a vanilla interpreter into a
compiler, using lightweight modular staging (LMS). The gist is to let
LMS generate code for the interpreter specialized to a particular
program -- the program is fixed at staging time, while the input(s) to
the program may vary in the generated code. Hence, staging an
interpreter should be as simple as wrapping the types of expressions
that vary in ``Rep[_]`` while leaving the types of expressions we want
specialized as is.

As a case study, we stage a simple regular expression matcher. Our
vanilla regular expression matcher is invoked on a regular expression
string and an input string. The staged regular expression matcher is
invoked on a _static_ regular expression constant string and a _dynamic_ input
string of type ``Rep[String]``, and generates code specialized to match
any input string against the constant regular expression pattern.

We could further optimize the generated code by additional staged
transformations, but here, we only illustrate the basic process of
staging an interpreter. This process is widely applicable. For
example, we used the same process to stage a bytecode interpreter into
a bytecode compiler.
*/

package scala.lms.tutorial

import org.scalatest.FunSuite


/**
Regular Expression Matcher
--------------------------

We start with a small regular expression matcher, ported to Scala from
[a C version, written by Rob Pike and Brian Kernighan](http://www.cs.princeton.edu/courses/archive/spr09/cos333/beautiful.html).
*/

trait RegexpMatcher {

  /* search for regexp anywhere in text */
  def matchsearch(regexp: String, text: String): Boolean = {
    if (regexp(0) == '^')
      matchhere(regexp, 1, text, 0)
    else {
      var start = -1
      var found = false
      while (!found && start < text.length) {
        start += 1
        found = matchhere(regexp, 0, text, start)
      }
      found
    }
  }


  /* search for restart of regexp at start of text */
  def matchhere(regexp: String, restart: Int, text: String, start: Int): Boolean = {
    if (restart==regexp.length)
      true
    else if (regexp(restart)=='$' && restart+1==regexp.length)
      start==text.length
    else if (restart+1 < regexp.length && regexp(restart+1)=='*')
      matchstar(regexp(restart), regexp, restart+2, text, start)
    else if (start < text.length && matchchar(regexp(restart), text(start)))
      matchhere(regexp, restart+1, text, start+1)
    else false
  }

  /* search for c* followed by restart of regexp at start of text */
  def matchstar(c: Char, regexp: String, restart: Int, text: String, start: Int): Boolean = {
    var sstart = start
    var found = matchhere(regexp, restart, text, sstart)
    var failed = false
    while (!failed && !found && sstart < text.length) {
      failed = !matchchar(c, text(sstart))
      sstart += 1
      found = matchhere(regexp, restart, text, sstart)
    }
    !failed && found
  }

  def matchchar(c: Char, t: Char): Boolean = {
    c == '.' || c == t
  }
}

class RegexpMatcherTest extends FunSuite with RegexpMatcher {
  def testmatch(regexp: String, text: String, expected: Boolean) {
    test(s"""matchsearch("$regexp", "$text") == $expected""") {
      assertResult(expected){matchsearch(regexp, text)}
    }
  }

  testmatch("^hello$", "hello", true)
  testmatch("^hello$", "hell", false)
  testmatch("hell", "hello", true);
  testmatch("hell", "hell", true);
  testmatch("hel*", "he", true);
  testmatch("hel*$", "hello", false);
  testmatch("hel*", "yo hello", true);
  testmatch("ab", "hello ab hello", true);
  testmatch("^ab", "hello ab hello", false);
  testmatch("a*b", "hello aab hello", true);
  testmatch("^ab*", "abcd", true);
  testmatch("^ab*", "a", true);
  testmatch("^ab*", "ac", true);
  testmatch("^ab*", "bac", false);
  testmatch("^ab*$", "ac", false);
}

/**
Staged Interpreter
--------------------------

The staged interpreter simply consist in wrapping the variable
parameters in ``Rep[_]`` types. Otherwise, the code is the same.
*/

trait StagedRegexpMatcher extends Dsl {

  /* search for regexp anywhere in text */
  def matchsearch(regexp: String, text: Rep[String]): Rep[Boolean] = {
    if (regexp(0) == '^')
      matchhere(regexp, 1, text, 0)
    else {
      var start = -1
      var found = false
      while (!found && start < text.length) {
        start += 1
        found = matchhere(regexp, 0, text, start)
      }
      found
    }
  }

  /* search for restart of regexp at start of text */
  def matchhere(regexp: String, restart: Int, text: Rep[String], start: Rep[Int]): Rep[Boolean] = {
    if (restart==regexp.length)
      true
    else if (regexp(restart)=='$' && restart+1==regexp.length)
      start==text.length
    else if (restart+1 < regexp.length && regexp(restart+1)=='*')
      matchstar(regexp(restart), regexp, restart+2, text, start)
    else if (start < text.length && matchchar(regexp(restart), text(start)))
      matchhere(regexp, restart+1, text, start+1)
    else false
  }

  /* search for c* followed by restart of regexp at start of text */
  def matchstar(c: Char, regexp: String, restart: Int, text: Rep[String], start: Rep[Int]): Rep[Boolean] = {
    var sstart = start
    var found = matchhere(regexp, restart, text, sstart)
    var failed = false
    while (!failed && !found && sstart < text.length) {
      failed = !matchchar(c, text(sstart))
      sstart += 1
      found = matchhere(regexp, restart, text, sstart)
    }
    !failed && found
  }

  def matchchar(c: Char, t: Rep[Char]): Rep[Boolean] = {
    c == '.' || c == t
  }
}

class StagedRegexpMatcherTest extends TutorialFunSuite {
  val under = "sre"
  var m = Map.empty[String, DslDriver[String,Boolean]]
  def cache(k: String, b: => DslDriver[String,Boolean]): DslDriver[String,Boolean] = {
    m.get(k) match {
      case Some(v) => v
      case None =>
        m = m.updated(k, b)
        m(k)
    }
  }
  def testmatch(regexp: String, text: String, expected: Boolean) {
    test(s"""matchsearch("$regexp", "$text") == $expected""") {
      val snippet = cache(regexp,
        new DslDriver[String,Boolean] with StagedRegexpMatcher {
          def snippet(x: Rep[String]) = matchsearch(regexp, x)
        })
      check("_"+regexp.replace("^", "_b").replace("*", "_s").replace("$", "_e"),
            snippet.code)
      assertResult(expected){snippet.eval(text)}
    }
  }

  testmatch("^hello$", "hello", true)
  testmatch("^hello$", "hell", false)
  testmatch("hell", "hello", true)
  testmatch("hell", "hell", true)
  testmatch("hel*", "he", true)
  testmatch("hel*$", "hello", false)
  testmatch("hel*", "yo hello", true)
  testmatch("ab", "hello ab hello", true)
  testmatch("^ab", "hello ab hello", false)
  testmatch("a*b", "hello aab hello", true)
  testmatch("^ab*", "abcd", true)
  testmatch("^ab*", "a", true)
  testmatch("^ab*", "ac", true)
  testmatch("^ab*", "bac", false)
  testmatch("^ab*$", "ac", false)
}

/**
Generated Code
--------------

As an example, here is the code generated for `^ab`.

.. includecode:: ../../../../out/sre__bab.check.scala


What's next?
------------

Go back to the [tutorial index](index.html) or continue with the [Ackermann's Function](ack.html).
*/
