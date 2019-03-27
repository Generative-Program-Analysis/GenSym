/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    var x1: Int = -1
    var x2: Boolean = false
    val x6 = x0.length
    val x57 = while ({val x3 = x2
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
      val x54 = if (x16) {
        val x17 = x12 + 1
        val x18 = x17 < x6
        val x21 = if (x18) {
          val x19 = x0(x17)
          val x20 = 'e' == x19
          x20
        } else {
          false
        }
        val x53 = if (x21) {
          val x22 = x17 + 1
          var x23: Int = x22
          var x24: Boolean = true
          var x25: Boolean = false
          val x46 = while ({val x26 = x25
            val x31 = if (x26) {
              false
            } else {
              val x28 = x24
              val x29 = !x28
              x29
            }
            val x35 = if (x31) {
              val x32 = x23
              val x33 = x32 < x6
              x33
            } else {
              false
            }
            x35}) {
            val x37 = x23
            val x38 = x0(x37)
            val x39 = 'l' == x38
            val x40 = !x39
            x25 = x40
            val x42 = x23 += 1
            val x43 = x23
            x24 = true
            ()
          }
          val x47 = x25
          val x51 = if (x47) {
            false
          } else {
            val x49 = x24
            x49
          }
          x51
        } else {
          false
        }
        x53
      } else {
        false
      }
      x2 = x54
      ()
    }
    val x58 = x2
    x58
  }
}
/*****************************************
End of Generated Code
*******************************************/
