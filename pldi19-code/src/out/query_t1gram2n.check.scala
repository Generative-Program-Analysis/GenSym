/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Word,Value,Word,Year,MatchCount,VolumeCount")
    val x2 = new scala.lms.tutorial.Scanner("src/data/words.csv")
    val x3 = x2.next(',')
    val x4 = x2.next('\n')
    val x24 = while ({val x5 = x2.hasNext
      x5}) {
      val x7 = x2.next(',')
      val x8 = x2.next('\n')
      val x9 = new scala.lms.tutorial.Scanner(x0)
      val x21 = while ({val x10 = x9.hasNext
        x10}) {
        val x12 = x9.next('\t')
        val x13 = x9.next('\t')
        val x14 = x9.next('\t')
        val x15 = x9.next('\n')
        val x16 = x7 == x12
        val x19 = if (x16) {
          val x17 = printf("%s,%s,%s,%s,%s,%s\n",x7,x8,x12,x13,x14,x15)
          x17
        } else {
          ()
        }
        x19
      }
      val x22 = x9.close
      x22
    }
    val x25 = x2.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
