package sai.utils

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
        case _ => println("[\u001B[91mFAILED\u001B[39m]")
      }
    }
  }

  def runtest(filter: String = "") = {
    testNameFilter = filter
    testall()
  }

  def testall(): Unit
}
