/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Name,Value,Flag,Name1")
    val x2 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x3 = x2.next(',')
    val x4 = x2.next(',')
    val x5 = x2.next('\n')
    val x25 = while ({val x6 = x2.hasNext
      x6}) {
      val x8 = x2.next(',')
      val x9 = x2.next(',')
      val x10 = x2.next('\n')
      val x11 = new scala.lms.tutorial.Scanner("src/data/t.csv")
      val x12 = x11.next(',')
      val x13 = x11.next(',')
      val x14 = x11.next('\n')
      val x22 = while ({val x15 = x11.hasNext
        x15}) {
        val x17 = x11.next(',')
        val x18 = x11.next(',')
        val x19 = x11.next('\n')
        val x20 = printf("%s,%s,%s,%s\n",x8,x9,x10,x17)
        x20
      }
      val x23 = x11.close
      x23
    }
    val x26 = x2.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
