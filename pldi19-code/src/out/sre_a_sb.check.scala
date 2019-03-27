/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    var x1: Int = -1
    var x2: Boolean = false
    val x6 = x0.length
    val x56 = while ({val x3 = x2
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
      var x13: Int = x12
      val x14 = x12 < x6
      val x17 = if (x14) {
        val x15 = x0(x12)
        val x16 = 'b' == x15
        x16
      } else {
        false
      }
      val x19 = x17
      var x20: Boolean = x19
      var x21: Boolean = false
      val x48 = while ({val x22 = x21
        val x27 = if (x22) {
          false
        } else {
          val x24 = x20
          val x25 = !x24
          x25
        }
        val x31 = if (x27) {
          val x28 = x13
          val x29 = x28 < x6
          x29
        } else {
          false
        }
        x31}) {
        val x33 = x13
        val x34 = x0(x33)
        val x35 = 'a' == x34
        val x36 = !x35
        x21 = x36
        val x38 = x13 += 1
        val x39 = x13
        val x40 = x39 < x6
        val x43 = if (x40) {
          val x41 = x0(x39)
          val x42 = 'b' == x41
          x42
        } else {
          false
        }
        val x45 = x43
        x20 = x45
        ()
      }
      val x49 = x21
      val x53 = if (x49) {
        false
      } else {
        val x51 = x20
        x51
      }
      x2 = x53
      ()
    }
    val x57 = x2
    x57
  }
}
/*****************************************
End of Generated Code
*******************************************/
