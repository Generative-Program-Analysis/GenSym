/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Name,Value")
    val x2 = new Array[java.lang.String](256)
    var x3: Int = 0
    var x4: Int = 0
    val x5 = new Array[Int](256)
    var x7 : Int = 0
    val x10 = while (x7 < 256) {
      val x8 = x5(x7) = -1
      x7 = x7 + 1
    }
    val x11 = new Array[Int](256)
    var x12: Int = 0
    val x13 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x14 = x13.next(',')
    val x15 = x13.next(',')
    val x16 = x13.next('\n')
    val x59 = while ({val x17 = x13.hasNext
      x17}) {
      val x19 = x13.next(',')
      val x20 = x13.next(',')
      val x21 = x13.next('\n')
      val x22 = x19.##
      val x23 = x22.toInt
      val x24 = x23 & 255
      val x53 = {
        //#hash_lookup
        // generated code for hash lookup
        var x25: Int = x24
        val x41 = while ({val x26 = x25
          val x27 = x5(x26)
          val x29 = x27 == -1
          val x34 = if (x29) {
            false
          } else {
            val x30 = x2(x27)
            val x31 = x30 == x19
            val x32 = !x31
            x32
          }
          x34}) {
          val x36 = x25
          val x37 = x36 + 1
          val x38 = x37 & 255
          x25 = x38
          ()
        }
        val x42 = x25
        val x43 = x5(x42)
        val x44 = x43 == -1
        val x51 = if (x44) {
          val x45 = x4
          val x46 = x2(x45) = x19
          val x47 = x4 += 1
          val x48 = x5(x42) = x45
          val x49 = x11(x45) = 0
          x45
        } else {
          x43
        }
        x51
        //#hash_lookup
      }
      val x54 = x11(x53)
      val x55 = x20.toInt
      val x56 = x54 + x55
      val x57 = x11(x53) = x56
      ()
    }
    val x60 = x13.close
    val x61 = x4
    var x63 : Int = 0
    val x69 = while (x63 < x61) {
      val x64 = x2(x63)
      val x65 = x11(x63)
      val x66 = (x65).toString()
      val x67 = printf("%s,%s\n",x64,x66)
      x63 = x63 + 1
    }
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
