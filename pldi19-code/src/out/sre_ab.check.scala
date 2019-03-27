/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    var x1: Int = -1
    var x2: Boolean = false
    val x6 = x0.length
    val x27 = while ({val x3 = x2
      val x9 = if (x3) {
        false
      } else {
        val x5 = x1
        val x7 = x5 < x6
        x7
      }
      x9}) {
      val x11 = x1 += 1
      val x12 = x1
      val x13 = x12 < x6
      val x16 = if (x13) {
        val x14 = x0(x12)
        val x15 = 'a' == x14
        x15
      } else {
        false
      }
      val x24 = if (x16) {
        val x17 = x12 + 1
        val x18 = x17 < x6
        val x21 = if (x18) {
          val x19 = x0(x17)
          val x20 = 'b' == x19
          x20
        } else {
          false
        }
        val x23 = x21
        x23
      } else {
        false
      }
      x2 = x24
      ()
    }
    val x28 = x2
    x28
  }
}
/*****************************************
End of Generated Code
*******************************************/
