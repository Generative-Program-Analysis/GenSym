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
    val x13 = while ({val x6 = x2.hasNext
      x6}) {
      val x8 = x2.next(',')
      val x9 = x2.next(',')
      val x10 = x2.next('\n')
      val x11 = printf("%s\n",x8)
      x11
    }
    val x14 = x2.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
