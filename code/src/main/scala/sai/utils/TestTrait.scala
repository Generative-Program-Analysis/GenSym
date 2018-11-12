package sai.utils

import java.lang._

trait TestTrait {
  var testNameFilter = ""
  def test(name: String)(f: => Unit) = {
    if (testNameFilter == "" || testNameFilter == name) {
      val dots = "." * (32 - name.length)
      print(s"""Running test "${name}"$dots""")
      try {
        f
        println("[\u001B[92mPASSED\u001B[39m]")
      } catch {
        case e: AssertionError => println("[\u001B[91mFAILED\u001B[39m]")
        case e: Exception =>
          println("[\u001B[91mFATAL\u001B[39m]\u001B[91m")
          e.printStackTrace()
          print("\u001B[39m")
      }
    }
  }

  def runtest(filter: String = "") = {
    testNameFilter = filter
    testall()
  }

  def testall(): Unit
}
