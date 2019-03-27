/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Boolean)) {
  def apply(x0:java.lang.String): Boolean = {
    var x1: Int = -1
    var x2: Boolean = false
    val x6 = x0.length
    val x39 = while ({val x3 = x2
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
      val x36 = if (x16) {
        val x17 = x12 + 1
        val x18 = x17 < x6
        val x21 = if (x18) {
          val x19 = x0(x17)
          val x20 = 'e' == x19
          x20
        } else {
          false
        }
        val x35 = if (x21) {
          val x22 = x17 + 1
          val x23 = x22 < x6
          val x26 = if (x23) {
            val x24 = x0(x22)
            val x25 = 'l' == x24
            x25
          } else {
            false
          }
          val x34 = if (x26) {
            val x27 = x22 + 1
            val x28 = x27 < x6
            val x31 = if (x28) {
              val x29 = x0(x27)
              val x30 = 'l' == x29
              x30
            } else {
              false
            }
            val x33 = x31
            x33
          } else {
            false
          }
          x34
        } else {
          false
        }
        x35
      } else {
        false
      }
      x2 = x36
      ()
    }
    val x40 = x2
    x40
  }
}
/*****************************************
End of Generated Code
*******************************************/
