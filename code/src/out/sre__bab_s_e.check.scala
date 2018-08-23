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
    val x38 = if (x5) {
      var x6: Int = 1
      val x7 = 1 == x1
      var x8: Boolean = x7
      var x9: Boolean = false
      val x31 = while ({val x10 = x9
        val x15 = if (x10) {
          false
        } else {
          val x12 = x8
          val x13 = !x12
          x13
        }
        val x19 = if (x15) {
          val x16 = x6
          val x17 = x16 < x1
          x17
        } else {
          false
        }
        x19}) {
        val x21 = x6
        val x22 = x0(x21)
        val x23 = 'b' == x22
        val x24 = !x23
        x9 = x24
        val x26 = x6 += 1
        val x27 = x6
        val x28 = x27 == x1
        x8 = x28
        ()
      }
      val x32 = x9
      val x36 = if (x32) {
        false
      } else {
        val x34 = x8
        x34
      }
      x36
    } else {
      false
    }
    x38
  }
}
/*****************************************
End of Generated Code
*******************************************/
