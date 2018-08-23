/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Word,Value,Word,Year,MatchCount,VolumeCount")
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
    var x13: Int = 0
    var x14: Int = 0
    val x15 = new Array[Int](65536)
    val x16 = new Array[Int](256)
    val x17 = new scala.lms.tutorial.Scanner("src/data/words.csv")
    val x18 = x17.next(',')
    val x19 = x17.next('\n')
    val x67 = while ({val x20 = x17.hasNext
      x20}) {
      val x22 = x17.next(',')
      val x23 = x17.next('\n')
      val x24 = x14
      val x25 = x11(x24) = x22
      val x26 = x12(x24) = x23
      val x27 = x14 += 1
      val x28 = x22.##
      val x29 = x28.toInt
      val x30 = x29 & 255
      val x59 = {
        //#hash_lookup
        // generated code for hash lookup
        var x31: Int = x30
        val x47 = while ({val x32 = x31
          val x33 = x5(x32)
          val x35 = x33 == -1
          val x40 = if (x35) {
            false
          } else {
            val x36 = x2(x33)
            val x37 = x36 == x22
            val x38 = !x37
            x38
          }
          x40}) {
          val x42 = x31
          val x43 = x42 + 1
          val x44 = x43 & 255
          x31 = x44
          ()
        }
        val x48 = x31
        val x49 = x5(x48)
        val x50 = x49 == -1
        val x57 = if (x50) {
          val x51 = x4
          val x52 = x2(x51) = x22
          val x53 = x4 += 1
          val x54 = x5(x48) = x51
          val x55 = x16(x51) = 0
          x51
        } else {
          x49
        }
        x57
        //#hash_lookup
      }
      val x60 = x16(x59)
      val x61 = x59 * 256
      val x62 = x61 + x60
      val x63 = x15(x62) = x24
      val x64 = x60 + 1
      val x65 = x16(x59) = x64
      x65
    }
    val x68 = x17.close
    val x69 = new scala.lms.tutorial.Scanner(x0)
    val x116 = while ({val x70 = x69.hasNext
      x70}) {
      val x72 = x69.next('\t')
      val x73 = x69.next('\t')
      val x74 = x69.next('\t')
      val x75 = x69.next('\n')
      val x76 = x72.##
      val x77 = x76.toInt
      val x78 = x77 & 255
      val x99 = {
        //#hash_lookup
        // generated code for hash lookup
        var x79: Int = x78
        val x95 = while ({val x80 = x79
          val x81 = x5(x80)
          val x83 = x81 == -1
          val x88 = if (x83) {
            false
          } else {
            val x84 = x2(x81)
            val x85 = x84 == x72
            val x86 = !x85
            x86
          }
          x88}) {
          val x90 = x79
          val x91 = x90 + 1
          val x92 = x91 & 255
          x79 = x92
          ()
        }
        val x96 = x79
        val x97 = x5(x96)
        x97
        //#hash_lookup
      }
      val x101 = x99 == -1
      val x114 = if (x101) {
        ()
      } else {
        val x102 = x16(x99)
        val x103 = x99 * 256
        val x104 = x103 + x102
        var x106 : Int = x103
        val x112 = while (x106 < x104) {
          val x107 = x15(x106)
          val x108 = x11(x107)
          val x109 = x12(x107)
          val x110 = printf("%s,%s,%s,%s,%s,%s\n",x108,x109,x72,x73,x74,x75)
          x106 = x106 + 1
        }
        x112
      }
      x114
    }
    val x117 = x69.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
