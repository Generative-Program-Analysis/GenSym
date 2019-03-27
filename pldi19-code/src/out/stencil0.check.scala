/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Array[Double])=>(Array[Double])) {
  def apply(x0:Array[Double]): Array[Double] = {
    val x1 = x0.length
    val x2 = new Array[Double](x1)
    val x3 = x1 - 2
    var x5 : Int = 2
    val x40 = while (x5 < x3) {
      val x6 = x0(x5)
      val x7 = x5 + 1
      val x8 = x0(x7)
      val x9 = x6 * x8
      val x10 = x6 - x9
      val x11 = x5 - 1
      val x12 = x0(x11)
      val x13 = x11 + 1
      val x14 = x0(x13)
      val x15 = x12 * x14
      val x16 = x10 + x15
      val x17 = x7 + 1
      val x18 = x0(x17)
      val x19 = x8 * x18
      val x20 = x8 - x19
      val x21 = x20 + x9
      val x22 = x16 * x21
      val x23 = x16 - x22
      val x24 = x12 - x15
      val x25 = x11 - 1
      val x26 = x0(x25)
      val x27 = x25 + 1
      val x28 = x0(x27)
      val x29 = x26 * x28
      val x30 = x24 + x29
      val x31 = x13 + 1
      val x32 = x0(x31)
      val x33 = x14 * x32
      val x34 = x14 - x33
      val x35 = x34 + x15
      val x36 = x30 * x35
      val x37 = x23 + x36
      val x38 = x2(x5) = x37
      x5 = x5 + 1
    }
    x2
  }
}
/*****************************************
End of Generated Code
*******************************************/
