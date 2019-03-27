/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Int)=>(Array[Int])) {
  def apply(x0:Int): Array[Int] = {
    val x1 = new Array[Int](x0)
    val x20 = x0 > 0
    val x39 = if (x20) {
      val x21 = x1(0) = 8
      var x22: Int = 5
      var x23: Int = 1
      var x25 : Int = 1
      val x37 = while (x25 < x0) {
        val x26 = x22
        val x27 = x23
        val x29 = x25 + 1
        val x30 = 2 * x29
        val x31 = x30 + 3
        val x32 = x26 + x31
        val x33 = x1(x27) = x32
        x22 = x31
        x23 = x29
        x25 = x25 + 1
      }
      x37
    } else {
      ()
    }
    x1
  }
}
/*****************************************
End of Generated Code
*******************************************/
