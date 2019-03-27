/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Int)=>(Int)) {
  def apply(x0:Int): Int = {
    var x5 = null.asInstanceOf[scala.Function1[Int, Int]]
    var x1 = null.asInstanceOf[scala.Function1[Int, Int]]
    val x9 = {x10: (Int) =>
      // ack_0
      val x12 = x10 + 1
      x12: Int
    }
    x5 = {x6: (Int) =>
      // ack_1
      val x8 = x6 == 0
      val x20 = if (x8) {
        val x14 = x9(1)
        x14
      } else {
        val x16 = x6 - 1
        val x17 = x5(x16)
        val x18 = x9(x17)
        x18
      }
      x20: Int
    }
    x1 = {x2: (Int) =>
      // ack_2
      val x4 = x2 == 0
      val x28 = if (x4) {
        val x22 = x5(1)
        x22
      } else {
        val x24 = x2 - 1
        val x25 = x1(x24)
        val x26 = x5(x25)
        x26
      }
      x28: Int
    }
    val x30 = x1(x0)
    x30
  }
}
/*****************************************
End of Generated Code
*******************************************/
