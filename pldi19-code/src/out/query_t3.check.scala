/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Name")
    val x2 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x3 = x2.next(',')
    val x4 = x2.next(',')
    val x5 = x2.next('\n')
    val x16 = while ({val x6 = x2.hasNext
      x6}) {
      val x8 = x2.next(',')
      val x9 = x2.next(',')
      val x10 = x2.next('\n')
      val x11 = x10 == "yes"
      val x14 = if (x11) {
        val x12 = printf("%s\n",x8)
        x12
      } else {
        ()
      }
      x14
    }
    val x17 = x2.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
