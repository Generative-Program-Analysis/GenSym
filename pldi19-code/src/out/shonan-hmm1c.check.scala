/*****************************************
Emitting Generated Code
*******************************************/
class Snippet(px6:Array[Int]) extends ((Array[Int])=>(Array[Int])) {
  def apply(x0:Array[Int]): Array[Int] = {
    val x2 = new Array[Int](5)
    val x6 = px6 // static data: Array(1,1,1,1,1)
    var x4 : Int = 0
    val x13 = while (x4 < 5) {
      val x5 = x2(0)
      val x7 = x6(x4)
      val x8 = x0(x4)
      val x9 = x7 * x8
      val x10 = x5 + x9
      val x11 = x2(0) = x10
      x4 = x4 + 1
    }
    val x14 = x2(1)
    val x17 = x2(1) = x14
    val x22 = x2(2)
    val x24 = x2(2) = x22
    val x19 = x0(2)
    val x25 = x22 + x19
    val x26 = x2(2) = x25
    val x27 = x2(3)
    val x29 = x2(3) = x27
    val x30 = x2(4)
    val x32 = x2(4) = x30
    val x33 = x30 + x19
    val x34 = x2(4) = x33
    val x21 = x0(4)
    val x35 = x33 + x21
    val x36 = x2(4) = x35
    x2
  }
}
/*****************************************
End of Generated Code
*******************************************/
