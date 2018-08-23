/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Name,Value,Flag,Name")
    val x2 = new Array[java.lang.String](256)
    var x3: Int = 0
    var x4: Int = 0
    val x5 = new Array[Int](256)
    var x7 : Int = 0
    val x10 = while (x7 < 256) {
      val x8 = x5(x7) = -1
      x7 = x7 + 1
    }
    val x11 = new Array[java.lang.String](65536)
    val x12 = new Array[java.lang.String](65536)
    val x13 = new Array[java.lang.String](65536)
    var x14: Int = 0
    var x15: Int = 0
    val x16 = new Array[Int](65536)
    val x17 = new Array[Int](256)
    val x18 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x19 = x18.next(',')
    val x20 = x18.next(',')
    val x21 = x18.next('\n')
    val x71 = while ({val x22 = x18.hasNext
      x22}) {
      val x24 = x18.next(',')
      val x25 = x18.next(',')
      val x26 = x18.next('\n')
      val x27 = x15
      val x28 = x11(x27) = x24
      val x29 = x12(x27) = x25
      val x30 = x13(x27) = x26
      val x31 = x15 += 1
      val x32 = x24.##
      val x33 = x32.toInt
      val x34 = x33 & 255
      val x63 = {
        //#hash_lookup
        // generated code for hash lookup
        var x35: Int = x34
        val x51 = while ({val x36 = x35
          val x37 = x5(x36)
          val x39 = x37 == -1
          val x44 = if (x39) {
            false
          } else {
            val x40 = x2(x37)
            val x41 = x40 == x24
            val x42 = !x41
            x42
          }
          x44}) {
          val x46 = x35
          val x47 = x46 + 1
          val x48 = x47 & 255
          x35 = x48
          ()
        }
        val x52 = x35
        val x53 = x5(x52)
        val x54 = x53 == -1
        val x61 = if (x54) {
          val x55 = x4
          val x56 = x2(x55) = x24
          val x57 = x4 += 1
          val x58 = x5(x52) = x55
          val x59 = x17(x55) = 0
          x55
        } else {
          x53
        }
        x61
        //#hash_lookup
      }
      val x64 = x17(x63)
      val x65 = x63 * 256
      val x66 = x65 + x64
      val x67 = x16(x66) = x27
      val x68 = x64 + 1
      val x69 = x17(x63) = x68
      x69
    }
    val x72 = x18.close
    val x73 = new scala.lms.tutorial.Scanner("src/data/t.csv")
    val x74 = x73.next(',')
    val x75 = x73.next(',')
    val x76 = x73.next('\n')
    val x123 = while ({val x77 = x73.hasNext
      x77}) {
      val x79 = x73.next(',')
      val x80 = x73.next(',')
      val x81 = x73.next('\n')
      val x82 = x79.##
      val x83 = x82.toInt
      val x84 = x83 & 255
      val x105 = {
        //#hash_lookup
        // generated code for hash lookup
        var x85: Int = x84
        val x101 = while ({val x86 = x85
          val x87 = x5(x86)
          val x89 = x87 == -1
          val x94 = if (x89) {
            false
          } else {
            val x90 = x2(x87)
            val x91 = x90 == x79
            val x92 = !x91
            x92
          }
          x94}) {
          val x96 = x85
          val x97 = x96 + 1
          val x98 = x97 & 255
          x85 = x98
          ()
        }
        val x102 = x85
        val x103 = x5(x102)
        x103
        //#hash_lookup
      }
      val x107 = x105 == -1
      val x121 = if (x107) {
        ()
      } else {
        val x108 = x17(x105)
        val x109 = x105 * 256
        val x110 = x109 + x108
        var x112 : Int = x109
        val x119 = while (x112 < x110) {
          val x113 = x16(x112)
          val x114 = x11(x113)
          val x115 = x12(x113)
          val x116 = x13(x113)
          val x117 = printf("%s,%s,%s,%s\n",x114,x115,x116,x79)
          x112 = x112 + 1
        }
        x119
      }
      x121
    }
    val x124 = x73.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
