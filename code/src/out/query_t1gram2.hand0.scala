class Snippet extends (String=>Unit) {
  def apply(fn: String): Unit = {
    println("Phrase,Year,MatchCount,VolumeCount")
    val s = new scala.lms.tutorial.Scanner(fn)
    while (s.hasNext) {
      val phrase = s.next('\t')
      if (phrase == "Auswanderung") {
        val year = s.next('\t')
        val matchCount = s.next('\t')
        val volumeCount = s.next('\n')
        println(s"$phrase,$year,$matchCount,$volumeCount")
      } else {
        // next two tab scans could be optimized away,
        // assuming input has expected format
        val year = s.next('\t')
        val matchCount = s.next('\t')
        val volumeCount = s.next('\n')
      }
    }
    s.close
  }
}
