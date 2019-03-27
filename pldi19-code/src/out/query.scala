object query {
  def run(fn: String) = {
    val s = new Snippet()
    s(fn)
  }
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("usage: query <filename>")
    } else {
      run(args(0))
    }
  }
}
