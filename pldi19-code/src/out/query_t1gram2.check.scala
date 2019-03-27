/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Phrase,Year,MatchCount,VolumeCount")
    val x2 = new scala.lms.tutorial.Scanner(x0)
    val x14 = while ({val x3 = x2.hasNext
      x3}) {
      val x5 = x2.next('\t')
      val x6 = x2.next('\t')
      val x7 = x2.next('\t')
      val x8 = x2.next('\n')
      val x9 = x5 == "Auswanderung"
      val x12 = if (x9) {
        val x10 = printf("%s,%s,%s,%s\n",x5,x6,x7,x8)
        x10
      } else {
        ()
      }
      x12
    }
    val x15 = x2.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
