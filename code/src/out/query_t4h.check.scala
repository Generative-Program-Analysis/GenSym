/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Name,Value,Flag,Name1")
    var x2: Int = 0
    var x3: Int = 0
    val x4 = new Array[Int](256)
    var x6 : Int = 0
    val x9 = while (x6 < 256) {
      val x7 = x4(x6) = -1
      x6 = x6 + 1
    }
    val x10 = new Array[java.lang.String](65536)
    val x11 = new Array[java.lang.String](65536)
    val x12 = new Array[java.lang.String](65536)
    var x13: Int = 0
    var x14: Int = 0
    val x15 = new Array[Int](65536)
    val x16 = new Array[Int](256)
    val x17 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x18 = x17.next(',')
    val x19 = x17.next(',')
    val x20 = x17.next('\n')
    val x31 = 0L.toInt
    val x32 = x31 & 255
    val x38 = !true
    val x65 = while ({val x21 = x17.hasNext
      x21}) {
      val x23 = x17.next(',')
      val x24 = x17.next(',')
      val x25 = x17.next('\n')
      val x26 = x14
      val x27 = x10(x26) = x23
      val x28 = x11(x26) = x24
      val x29 = x12(x26) = x25
      val x30 = x14 += 1
      val x57 = {
        //#hash_lookup
        // generated code for hash lookup
        var x33: Int = x32
        val x46 = while ({val x34 = x33
          val x35 = x4(x34)
          val x37 = x35 == -1
          val x39 = if (x37) {
            false
          } else {
            x38
          }
          x39}) {
          val x41 = x33
          val x42 = x41 + 1
          val x43 = x42 & 255
          x33 = x43
          ()
        }
        val x47 = x33
        val x48 = x4(x47)
        val x49 = x48 == -1
        val x55 = if (x49) {
          val x50 = x3
          val x51 = x3 += 1
          val x52 = x4(x47) = x50
          val x53 = x16(x50) = 0
          x50
        } else {
          x48
        }
        x55
        //#hash_lookup
      }
      val x58 = x16(x57)
      val x59 = x57 * 256
      val x60 = x59 + x58
      val x61 = x15(x60) = x26
      val x62 = x58 + 1
      val x63 = x16(x57) = x62
      x63
    }
    val x66 = x17.close
    val x67 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x68 = x67.next(',')
    val x69 = x67.next(',')
    val x70 = x67.next('\n')
    val x110 = while ({val x71 = x67.hasNext
      x71}) {
      val x73 = x67.next(',')
      val x74 = x67.next(',')
      val x75 = x67.next('\n')
      val x92 = {
        //#hash_lookup
        // generated code for hash lookup
        var x76: Int = x32
        val x88 = while ({val x77 = x76
          val x78 = x4(x77)
          val x80 = x78 == -1
          val x81 = if (x80) {
            false
          } else {
            x38
          }
          x81}) {
          val x83 = x76
          val x84 = x83 + 1
          val x85 = x84 & 255
          x76 = x85
          ()
        }
        val x89 = x76
        val x90 = x4(x89)
        x90
        //#hash_lookup
      }
      val x94 = x92 == -1
      val x108 = if (x94) {
        ()
      } else {
        val x95 = x16(x92)
        val x96 = x92 * 256
        val x97 = x96 + x95
        var x99 : Int = x96
        val x106 = while (x99 < x97) {
          val x100 = x15(x99)
          val x101 = x10(x100)
          val x102 = x11(x100)
          val x103 = x12(x100)
          val x104 = printf("%s,%s,%s,%s\n",x101,x102,x103,x73)
          x99 = x99 + 1
        }
        x106
      }
      x108
    }
    val x111 = x67.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
