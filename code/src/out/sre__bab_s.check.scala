/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    val x1 = x0.length
    val x2 = 0 < x1
    val x5 = if (x2) {
      val x3 = x0(0)
      val x4 = 'a' == x3
      x4
    } else {
      false
    }
    val x36 = if (x5) {
      var x6: Int = 1
      var x7: Boolean = true
      var x8: Boolean = false
      val x29 = while ({val x9 = x8
        val x14 = if (x9) {
          false
        } else {
          val x11 = x7
          val x12 = !x11
          x12
        }
        val x18 = if (x14) {
          val x15 = x6
          val x16 = x15 < x1
          x16
        } else {
          false
        }
        x18}) {
        val x20 = x6
        val x21 = x0(x20)
        val x22 = 'b' == x21
        val x23 = !x22
        x8 = x23
        val x25 = x6 += 1
        val x26 = x6
        x7 = true
        ()
      }
      val x30 = x8
      val x34 = if (x30) {
        false
      } else {
        val x32 = x7
        x32
      }
      x34
    } else {
      false
    }
    x36
  }
}
/*****************************************
End of Generated Code
*******************************************/
