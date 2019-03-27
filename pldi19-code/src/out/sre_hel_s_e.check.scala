/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    var x1: Int = -1
    var x2: Boolean = false
    val x6 = x0.length
    val x59 = while ({val x3 = x2
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
        val x15 = 'h' == x14
        x15
      } else {
        false
      }
      val x56 = if (x16) {
        val x17 = x12 + 1
        val x18 = x17 < x6
        val x21 = if (x18) {
          val x19 = x0(x17)
          val x20 = 'e' == x19
          x20
        } else {
          false
        }
        val x55 = if (x21) {
          val x22 = x17 + 1
          var x23: Int = x22
          val x24 = x22 == x6
          var x25: Boolean = x24
          var x26: Boolean = false
          val x48 = while ({val x27 = x26
            val x32 = if (x27) {
              false
            } else {
              val x29 = x25
              val x30 = !x29
              x30
            }
            val x36 = if (x32) {
              val x33 = x23
              val x34 = x33 < x6
              x34
            } else {
              false
            }
            x36}) {
            val x38 = x23
            val x39 = x0(x38)
            val x40 = 'l' == x39
            val x41 = !x40
            x26 = x41
            val x43 = x23 += 1
            val x44 = x23
            val x45 = x44 == x6
            x25 = x45
            ()
          }
          val x49 = x26
          val x53 = if (x49) {
            false
          } else {
            val x51 = x25
            x51
          }
          x53
        } else {
          false
        }
        x55
      } else {
        false
      }
      x2 = x56
      ()
    }
    val x60 = x2
    x60
  }
}
/*****************************************
End of Generated Code
*******************************************/
