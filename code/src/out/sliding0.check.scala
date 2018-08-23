/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Int)=>(Array[Int])) {
  def apply(x0:Int): Array[Int] = {
    val x1 = new Array[Int](x0)
    var x3 : Int = 0
    val x12 = while (x3 < x0) {
      val x4 = 2 * x3
      val x5 = x4 + 3
      val x6 = x3 + 1
      val x7 = 2 * x6
      val x8 = x7 + 3
      val x9 = x5 + x8
      val x10 = x1(x3) = x9
      x3 = x3 + 1
    }
    x1
  }
}
/*****************************************
End of Generated Code
*******************************************/
