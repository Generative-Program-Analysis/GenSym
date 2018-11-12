package sai.utils

import java.lang._

trait TestTrait {
  var testNameFilter = ""
  var tests = 0
  var passed = 0
  var failed = 0
  var fatal = 0
  var omitted = 0
  def test(name: String)(f: => Unit) = {
    if (testNameFilter == "" || testNameFilter == name) {
      val dots = "." * (32 - name.length)
      print(s"""Running test "${name}"$dots""")
      tests += 1
      try {
        f
        passed += 1
        println("[\u001B[92mPASSED\u001B[39m]")
      } catch {
        case e: AssertionError =>
          failed += 1
          println("[\u001B[91mFAILED\u001B[39m]")
        case e: Throwable =>
          fatal += 1
          println("[\u001B[91mFATAL\u001B[39m]\u001B[91m")
          e.printStackTrace()
          print("\u001B[39m")
      }
    } else {
      omitted += 1
    }
  }

  def runtest(filter: String = "") = {
    testNameFilter = filter
    tests = 0
    passed = 0
    failed = 0
    fatal = 0
    testall()
    println("Test Summary:")
    println(s"\u001B[94mTESTS:\u001B[39m $tests")
    println(s"\u001B[92mPASSED:\u001B[39m $passed")
    println(s"\u001B[91mFAILED:\u001B[39m $failed")
    println(s"\u001B[93mFATAL:\u001B[39m $fatal")
    println(s"\u001B[90mOMITTED:\u001B[39m $omitted")
  }

  def testall(): Unit
}
